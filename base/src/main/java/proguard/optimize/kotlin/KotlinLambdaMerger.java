package proguard.optimize.kotlin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.*;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.visitor.*;
import proguard.classfile.editor.LineNumberTableAttributeTrimmer;
import proguard.classfile.instruction.visitor.InstructionCounter;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.optimize.info.*;
import proguard.optimize.peephole.*;
import proguard.pass.Pass;
import proguard.shrink.*;
import proguard.util.*;
import java.io.PrintWriter;

public class KotlinLambdaMerger implements Pass
{

    public static final String NAME_KOTLIN_LAMBDA     = "kotlin/jvm/internal/Lambda";
    public static final String NAME_KOTLIN_FUNCTION  = "kotlin/jvm/functions/Function";
    public static final String NAME_KOTLIN_FUNCTIONN  = "kotlin/jvm/functions/FunctionN";

    public static boolean lambdaMergingDone = false;
    private static final Logger logger = LogManager.getLogger(KotlinLambdaMerger.class);
    private final Configuration configuration;

    public KotlinLambdaMerger(Configuration configuration)
    {
        this.configuration = configuration;
    }

    // Implementations for Pass
    @Override
    public void execute(AppView appView) throws Exception
    {
        if (lambdaMergingDone)
        {
            return;
        }
        // Remove old processing info
        appView.programClassPool.classesAccept(new ClassCleaner());
        appView.libraryClassPool.classesAccept(new ClassCleaner());

        // get the Lambda class and the Function0 interface
        Clazz kotlinLambdaClass = getKotlinLambdaClass(appView.programClassPool, appView.libraryClassPool);
        if (kotlinLambdaClass == null) {
            logger.warn("The Kotlin class '{}' is not found, but it is needed to perform lambda merging.",
                        NAME_KOTLIN_LAMBDA);
        }
        else
        {
            // A class pool where the applicable lambda's will be stored
            ClassPool lambdaClassPool = new ClassPool();
            ClassPool newProgramClassPool = new ClassPool();
            ClassPoolFiller newProgramClassPoolFiller = new ClassPoolFiller(newProgramClassPool);

            // find all lambda classes with an empty closure
            // assume that the lambda classes have exactly 1 instance constructor, which has descriptor ()V
            //  (i.e. no arguments) if the closure is empty
            appView.programClassPool.classesAccept(new MultiClassVisitor(
                                                   new ClassProcessingFlagFilter(ProcessingFlags.DONT_OPTIMIZE,
                                                                                 0,
                                                   newProgramClassPoolFiller),
                                                   new ClassProcessingFlagFilter(0,
                                                                                 ProcessingFlags.DONT_OPTIMIZE,
                                                   new ImplementedClassFilter(kotlinLambdaClass,
                                                                              false,
                                                   new ClassPoolFiller(lambdaClassPool),
                                                   newProgramClassPoolFiller)),
                                                   // add optimisation info to the lambda's,
                                                   // so that it can be filled out later
                                                   new ProgramClassOptimizationInfoSetter(),
                                                   new AllMemberVisitor(
                                                   new ProgramMemberOptimizationInfoSetter())));

            // group the lambda's per package
            PackageGrouper packageGrouper = new PackageGrouper();
            lambdaClassPool.classesAccept(packageGrouper);

            ClassPool    lambdaGroupClassPool     = new ClassPool();
            ClassPool    notMergedLambdaClassPool = new ClassPool();
            ClassCounter mergedLambdaClassCounter = new ClassCounter();

            // merge the lambda's per package
            KotlinLambdaClassMerger merger = new KotlinLambdaClassMerger(this.configuration,
                                                                         appView.programClassPool,
                                                                         appView.libraryClassPool,
                                                                         new MultiClassVisitor(
                                                                         new ClassPoolFiller(lambdaGroupClassPool),
                                                                         newProgramClassPoolFiller),
                                                                         new MultiClassVisitor(
                                                                         new ClassPoolFiller(notMergedLambdaClassPool),
                                                                         newProgramClassPoolFiller),
                                                                         appView.extraDataEntryNameMap);
            packageGrouper.packagesAccept(merger);

            // Print out the mapping, if requested.
            PrintWriter out = new PrintWriter(System.out, true);
            if (configuration.printLambdaGroupMapping != null)
            {
                logger.info("Printing lambda group mapping to [{}]...",
                            PrintWriterUtil.fileName(configuration.printLambdaGroupMapping));

                PrintWriter mappingWriter =
                         PrintWriterUtil.createPrintWriter(configuration.printLambdaGroupMapping, out);

                try
                {
                    // Print out lambda's that have been merged and their lambda groups.
                    lambdaClassPool.classesAcceptAlphabetically(
                            new LambdaGroupMappingPrinter(mappingWriter));
                }
                finally
                {
                    PrintWriterUtil.closePrintWriter(configuration.printMapping,
                            mappingWriter);
                }
            }

            // initialise the references from and to the newly created lambda groups and their enclosing classes
            newProgramClassPool.classesAccept(new ClassInitializer(newProgramClassPool, appView.libraryClassPool));

            // inline the helper invoke methods into the general invoke method
            inlineMethodsInsideLambdaGroups(lambdaGroupClassPool);

            // remove the unused helper methods from the lambda groups
            shrinkLambdaGroups(newProgramClassPool, appView.libraryClassPool, lambdaGroupClassPool);

            logger.info("Considered {} lambda classes for merging", lambdaClassPool.size());
            logger.info("of which {} lambda classes were not merged.", notMergedLambdaClassPool.size());
            logger.info("{} lambda group(s) created.", lambdaGroupClassPool.size());
            logger.info("#lambda groups/#merged lambda classes ratio = {}/{} = {}%",
                        lambdaGroupClassPool.size(),
                        lambdaClassPool.size() - notMergedLambdaClassPool.size(),
                        100 * lambdaGroupClassPool.size() / (lambdaClassPool.size() - notMergedLambdaClassPool.size()));
            logger.info("Size of original program class pool: {}", appView.programClassPool.size());
            logger.info("Size of new program class pool: {}", newProgramClassPool.size());

            appView.programClassPool.classesAccept(new ClassCleaner());
            appView.libraryClassPool.classesAccept(new ClassCleaner());
            newProgramClassPool.classesAccept(new ClassCleaner());
            appView.programClassPool.clear();
            newProgramClassPool.classesAccept(new ClassPoolFiller(appView.programClassPool));
        }
        lambdaMergingDone = true;
    }

    private void inlineMethodsInsideLambdaGroups(ClassPool lambdaGroupClassPool)
    {
        InstructionCounter methodInliningCounter = new InstructionCounter();

        lambdaGroupClassPool.classesAccept(new MultiClassVisitor(
                                           new ProgramClassOptimizationInfoSetter(),
                                           new AllMemberVisitor(
                                           new ProgramMemberOptimizationInfoSetter()),
                                           new AllMethodVisitor(
                                           new AllAttributeVisitor(
                                           new SameClassMethodInliner(configuration.microEdition,
                                                                      configuration.android,
                                                                      configuration.allowAccessModification,
                                                                      methodInliningCounter)))));
        logger.debug("{} methods inlined inside lambda groups.", methodInliningCounter.getCount());
    }

    private void shrinkLambdaGroups(ClassPool programClassPool,
                                    ClassPool libraryClassPool,
                                    ClassPool lambdaGroupClassPool)
    {
        SimpleUsageMarker simpleUsageMarker = new SimpleUsageMarker();
        ClassUsageMarker classUsageMarker = new ClassUsageMarker(simpleUsageMarker);

        // make sure that the used methods of the lambda groups are marked as used
        // by marking all classes and methods
        libraryClassPool.classesAccept(classUsageMarker);
        // but don't mark the lambda groups and their members in case they are not used,
        // e.g. inlined helper invoke methods
        programClassPool.classesAccept(new ClassNameFilter(
                                       "**/" + KotlinLambdaClassMerger.NAME_LAMBDA_GROUP,
                                       (ClassVisitor) null,
                                       new MultiClassVisitor(
                                       classUsageMarker,
                                       new AllMemberVisitor(
                                       classUsageMarker))));

        lambdaGroupClassPool.classesAccept(new AllMemberVisitor(
                                           new MultiMemberVisitor(
                                           new MemberNameFilter(KotlinConstants.METHOD_NAME_LAMBDA_INVOKE,
                                           classUsageMarker),
                                           new MemberNameFilter(ClassConstants.METHOD_NAME_INIT,
                                           classUsageMarker),
                                           new MemberNameFilter(ClassConstants.METHOD_NAME_CLINIT,
                                           classUsageMarker))));


        // ensure that the interfaces of the lambda group are not removed
        lambdaGroupClassPool.classesAccept(new InterfaceUsageMarker(
                                           classUsageMarker));

        // mark the lambda groups themselves as used
        // remove the unused parts of the lambda groups, such as the inlined invoke helper methods
        // and make sure that the line numbers are updated
        lambdaGroupClassPool.classesAccept(new MultiClassVisitor(
                                           new UsedClassFilter(simpleUsageMarker,
                                           new ClassShrinker(simpleUsageMarker)),
                                           new LineNumberLinearizer(),
                                           new AllAttributeVisitor(true,
                                           new LineNumberTableAttributeTrimmer())));
    }

    private Clazz getKotlinLambdaClass(ClassPool programClassPool, ClassPool libraryClassPool)
    {
        Clazz kotlinLambdaClass = programClassPool.getClass(NAME_KOTLIN_LAMBDA);
        if (kotlinLambdaClass == null) {
            kotlinLambdaClass = libraryClassPool.getClass(NAME_KOTLIN_LAMBDA);
        }
        return kotlinLambdaClass;
    }

    /**
     * Returns the arity of the function interface that is implemented by a given class.
     * @param programClass the class of which the arity must be determined
     * @return the value following the base name "kotlin/jvm/functions/Function". This can be a number going from
     *         0 to 22 or the character "N". For example, for a class implementing the interface
     *         "kotlin/jvm/functions/Function5" the value "5" is returned.
     * @throws IllegalArgumentException if the given programClass does not implement a Kotlin function interface.
     */
    public static String getArityFromInterface(ProgramClass programClass) throws IllegalArgumentException
    {
        for (int interfaceIndex = 0; interfaceIndex < programClass.u2interfacesCount; interfaceIndex++)
        {
            String interfaceName = programClass.getInterfaceName(interfaceIndex);
            if (interfaceName.startsWith(NAME_KOTLIN_FUNCTION) && interfaceName.length() > NAME_KOTLIN_FUNCTION.length())
            {
                return interfaceName.substring(NAME_KOTLIN_FUNCTION.length());
            }
        }
        throw new IllegalArgumentException("Class " + ClassUtil.externalClassName(programClass.getName()) +
                                           " does not implement a Kotlin function interface.");
    }

    /**
     * Checks whether the given lambda class should still be merged.
     * Returns true if the lambda class has not yet been merged and is allowed to be merged.
     * @param lambdaClass the lambda class for which should be checked whether it should be merged
     */
    public static boolean shouldMerge(ProgramClass lambdaClass)
    {
        ProgramClassOptimizationInfo optimizationInfo =
                ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(lambdaClass);
        return (lambdaClass.getProcessingFlags() & (ProcessingFlags.DONT_OPTIMIZE | ProcessingFlags.DONT_SHRINK)) == 0
                && lambdaClass.extendsOrImplements(NAME_KOTLIN_LAMBDA)
                // if optimisation info is null, then the lambda was not enqueued to be merged
                && optimizationInfo != null
                && optimizationInfo.getLambdaGroup() == null
                && optimizationInfo.mayBeMerged();
    }

    /**
     * Checks whether the given lambda class can be merged and throws an exception if it cannot be merged.
     * @param lambdaClass the lambda class for which it should be checked whether it can be merged
     * @param programClassPool the program class pool in which the lambda class is defined and used
     * @throws IllegalArgumentException if the given lambda class cannot be merged
     */
    public static void ensureCanMerge(ProgramClass lambdaClass,
                                      ClassPool programClassPool) throws IllegalArgumentException
    {
        String externalClassName = ClassUtil.externalClassName(lambdaClass.getName());
        if (!lambdaClass.extendsOrImplements(NAME_KOTLIN_LAMBDA))
        {
            throw new IllegalArgumentException("Class " + externalClassName + " cannot be merged in a Kotlin " +
                                               "lambda group, because it is not a subclass of " +
                                               ClassUtil.externalClassName(NAME_KOTLIN_LAMBDA) + ".");
        }
        else if (!lambdaClassHasExactlyOneInitConstructor(lambdaClass))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because " +
                                               "it has more than 1 <init> constructor.");
        }
        else if (!lambdaClassHasNoBootstrapMethod(lambdaClass))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because it " +
                                               "contains a bootstrap method that would not be merged into the lambda group.");
        }
        else if (!lambdaClassHasNoAccessibleStaticMethods(lambdaClass))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because it " +
                                               "contains a static method that could be used outside the class itself.");
        }
        else if (!lambdaClassIsNotDirectlyInvoked(lambdaClass))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because it is " +
                                               "directly invoked with its specific invoke method.");
        }
        else if (!nonINSTANCEFieldsAreNotReferencedFromSamePackage(lambdaClass, programClassPool))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because one of " +
                                               "its fields, other than the 'INSTANCE' field is referenced by one of its " +
                                               "inner classes.");
        }
        else if (!lambdaClassHasTotalMethodCodeSizeThatCanBeInlined(lambdaClass))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because its " +
                                               "methods are too big to be inlined.");
        }
    }

    private static boolean lambdaClassHasNoBootstrapMethod(ProgramClass lambdaClass) {
        AttributeCounter attributeCounter = new AttributeCounter();
        lambdaClass.attributeAccept(Attribute.BOOTSTRAP_METHODS, attributeCounter);
        return attributeCounter.getCount() == 0;
    }

    private static boolean lambdaClassHasNoAccessibleStaticMethods(ProgramClass lambdaClass)
    {
        MethodCounter nonPrivateStaticMethodCounter = new MethodCounter();
        String regularExpression = "!" + ClassConstants.METHOD_NAME_CLINIT;
        lambdaClass.methodsAccept(new MemberAccessFilter(AccessConstants.STATIC, AccessConstants.PRIVATE,
                                  new MemberNameFilter(regularExpression,
                                  new MultiMemberVisitor(
                                  nonPrivateStaticMethodCounter,
                                  new MemberVisitor() {
                                      @Override
                                      public void visitProgramMethod(ProgramClass programClass,
                                                                     ProgramMethod programMethod) {
                                          logger.warn("Lambda class {} contains a static method that cannot be merged: {}",
                                                      ClassUtil.externalClassName(lambdaClass.getName()),
                                                      ClassUtil.externalFullMethodDescription(lambdaClass.getName(),
                                                                                              programMethod.getAccessFlags(),
                                                                                              programMethod.getName(lambdaClass),
                                                                                              programMethod.getDescriptor(lambdaClass)));
                                      }
                                  }))));
        return nonPrivateStaticMethodCounter.getCount() == 0;
    }

    private static boolean lambdaClassHasTotalMethodCodeSizeThatCanBeInlined(ProgramClass lambdaClass)
    {
        String methodNameRegularExpression = "!" + ClassConstants.METHOD_NAME_INIT;
        methodNameRegularExpression       += ",!" + ClassConstants.METHOD_NAME_CLINIT;
        CodeSizeCounter codeSizeCounter = new CodeSizeCounter();
        lambdaClass.methodsAccept(new MemberNameFilter(methodNameRegularExpression,
                                  new AllAttributeVisitor(
                                  codeSizeCounter)));
        return codeSizeCounter.getCount() <= KotlinLambdaGroupBuilder.MAXIMUM_INLINED_INVOKE_METHOD_CODE_LENGTH;
    }

    private static boolean lambdaClassHasExactlyOneInitConstructor(ProgramClass lambdaClass)
    {
        MethodCounter initMethodCounter = new MethodCounter();
        lambdaClass.methodsAccept(new MemberNameFilter(ClassConstants.METHOD_NAME_INIT,
                                  initMethodCounter));
        if (initMethodCounter.getCount() != 1)
        {
            logger.warn("Lambda class {} has {} <init> constructors.",
                        ClassUtil.externalClassName(lambdaClass.getName()), initMethodCounter.getCount());
        }
        return initMethodCounter.getCount() == 1;
    }

    private static boolean lambdaClassIsNotDirectlyInvoked(ProgramClass lambdaClass)
    {
        Method invokeMethod = KotlinLambdaGroupBuilder.getInvokeMethod(lambdaClass, false);
        if (invokeMethod == null)
        {
            return true;
        }
        MethodReferenceFinder methodReferenceFinder = new MethodReferenceFinder(invokeMethod);
        // TODO: move visitors to separate classes
        lambdaClass.attributeAccept(Attribute.ENCLOSING_METHOD, new AttributeVisitor() {
            @Override
            public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute) {
                enclosingMethodAttribute.referencedClass.constantPoolEntriesAccept(methodReferenceFinder);
            }
        });
        return !methodReferenceFinder.methodReferenceFound();
    }

    private static boolean nonINSTANCEFieldsAreNotReferencedFromSamePackage(ProgramClass lambdaClass,
                                                                            ClassPool programClassPool)
    {
        String regularExpression = ClassUtil.internalPackagePrefix(lambdaClass.getName()) + "*";
        String fieldRegularExpression = "!" + KotlinConstants.KOTLIN_OBJECT_INSTANCE_FIELD_NAME;
        FieldReferenceFinder fieldReferenceFinder = new FieldReferenceFinder(lambdaClass,
                                                                             fieldRegularExpression,
                                                                             new ConstantVisitor() {
            @Override
            public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant) {
                logger.warn("{} references non-INSTANCE field {} of lambda class {}.",
                            ClassUtil.externalClassName(clazz.getName()),
                            ClassUtil.externalFullFieldDescription(fieldrefConstant.referencedField.getAccessFlags(),
                                                                   fieldrefConstant.getName(clazz),
                                                                   fieldrefConstant.getType(clazz)),
                            ClassUtil.externalClassName(lambdaClass.getName()));
            }
        });
        programClassPool.classesAccept(new ClassNameFilter(regularExpression,
                                       new ClassNameFilter(lambdaClass.getName(), (ClassVisitor)null,
                                       new AllConstantVisitor(
                                       fieldReferenceFinder))));
        return !fieldReferenceFinder.isFieldReferenceFound();
    }
}
