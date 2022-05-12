package proguard.optimize.kotlin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.AppView;
import proguard.Configuration;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.ClassConstantToClassVisitor;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.LineNumberTableAttributeTrimmer;
import proguard.classfile.instruction.visitor.InstructionCounter;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.ClassInitializer;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.*;
import proguard.optimize.info.ProgramClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfoSetter;
import proguard.optimize.info.ProgramMemberOptimizationInfoSetter;
import proguard.optimize.peephole.LineNumberLinearizer;
import proguard.optimize.peephole.SameClassMethodInliner;
import proguard.pass.Pass;
import proguard.shrink.*;
import proguard.util.PrintWriterUtil;
import proguard.util.ProcessingFlags;

import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;

public class KotlinLambdaMerger implements Pass {

    public static final String NAME_KOTLIN_LAMBDA     = "kotlin/jvm/internal/Lambda";
    public static final String NAME_KOTLIN_FUNCTION0  = "kotlin/jvm/functions/Function0";
    public static final String NAME_KOTLIN_FUNCTIONN  = "kotlin/jvm/functions/FunctionN";

    private static final Logger logger = LogManager.getLogger(KotlinLambdaMerger.class);
    private final Configuration configuration;

    public KotlinLambdaMerger(Configuration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    public void execute(AppView appView) throws Exception
    {
        // Remove old processing info
        appView.programClassPool.classesAccept(new ClassCleaner());
        appView.libraryClassPool.classesAccept(new ClassCleaner());

        // get the Lambda class and the Function0 interface
        Clazz kotlinLambdaClass = getKotlinLambdaClass(appView.programClassPool, appView.libraryClassPool);
        if (kotlinLambdaClass == null) {
            logger.warn("The Kotlin class '{}' is not found, but it is needed to perform lambda merging.", NAME_KOTLIN_LAMBDA);
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
            appView.programClassPool.classesAccept(new ClassProcessingFlagFilter(ProcessingFlags.DONT_OPTIMIZE, 0, newProgramClassPoolFiller));
            appView.programClassPool.classesAccept(new ClassProcessingFlagFilter(0, ProcessingFlags.DONT_OPTIMIZE,
                                                   new ImplementedClassFilter(kotlinLambdaClass, false,
                                                   //new ClassMethodFilter(ClassConstants.METHOD_NAME_INIT, ClassConstants.METHOD_TYPE_INIT,
                                                   new ClassPoolFiller(lambdaClassPool),
                                                   //newProgramClassPoolFiller),
                                                   newProgramClassPoolFiller))
            );

            // group the lambda's per package
            PackageGrouper packageGrouper = new PackageGrouper();
            lambdaClassPool.classesAccept(packageGrouper);

            // add optimisation info to the lambda's, so that it can be filled out later
            appView.programClassPool.classesAccept(new ProgramClassOptimizationInfoSetter());
            appView.programClassPool.classesAccept(new AllMemberVisitor(
                                                   new ProgramMemberOptimizationInfoSetter()));

            ClassPool lambdaGroupClassPool = new ClassPool();
            ClassPool notMergedLambdaClassPool = new ClassPool();

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
                logger.info("Printing lambda group mapping to [{}]...", PrintWriterUtil.fileName(configuration.printLambdaGroupMapping));

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
            inlineMethodsInsideLambdaGroups(newProgramClassPool, appView.libraryClassPool, lambdaGroupClassPool);

            // remove the unused helper methods from the lambda groups
            shrinkLambdaGroups(newProgramClassPool, appView.libraryClassPool, appView.resourceFilePool, lambdaGroupClassPool);

            logger.info("Considered {} lambda classes for merging", lambdaClassPool.size());
            logger.info("of which {} lambda classes were not merged.", notMergedLambdaClassPool.size());
            logger.info("{} lambda group(s) created.", lambdaGroupClassPool.size());
            logger.info("#lambda groups/#merged lambda classes ratio = {}/{} = {}%",
                        lambdaGroupClassPool.size(),
                        lambdaClassPool.size() - notMergedLambdaClassPool.size(),
                        100 * lambdaGroupClassPool.size() / (lambdaClassPool.size() - notMergedLambdaClassPool.size()));
            logger.info("Size of original program class pool: {}", appView.programClassPool.size());
            logger.info("Size of new program class pool: {}", newProgramClassPool.size());

            appView.programClassPool.clear();
            newProgramClassPool.classesAccept(new ClassPoolFiller(appView.programClassPool));
        }
    }

    private void inlineMethodsInsideLambdaGroups(ClassPool programClassPool, ClassPool libraryClassPool, ClassPool lambdaGroupClassPool)
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

    private void shrinkLambdaGroups(ClassPool programClassPool, ClassPool libraryClassPool, ResourceFilePool resourceFilePool, ClassPool lambdaGroupClassPool)
    {
        SimpleUsageMarker simpleUsageMarker = new SimpleUsageMarker();
        ClassUsageMarker classUsageMarker = new ClassUsageMarker(simpleUsageMarker);

        // make sure that the used methods of the lambda groups are marked as used
        // by marking all classes and methods
        // note: if -dontshrink is
        /*new UsageMarker(configuration).mark(programClassPool,
                                            libraryClassPool,
                                            resourceFilePool,
                                            simpleUsageMarker,
                                            classUsageMarker);*/
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

        // ensure that the interfaces of the lambda group are not removed
        lambdaGroupClassPool.classesAccept(new InterfaceUsageMarker(
                                           classUsageMarker));

        // mark the lambda groups themselves as used
        // remove the unused parts of the lambda groups, such as the inlined invoke helper methods
        // and make sure that the line numbers are updated
        //ClassPool newLambdaGroupClassPool = new ClassPool();
        lambdaGroupClassPool.classesAccept(new MultiClassVisitor(
                                           new UsedClassFilter(simpleUsageMarker,
                                           new ClassShrinker(simpleUsageMarker)),
                                           new LineNumberLinearizer(),
                                           new AllAttributeVisitor(true,
                                           new LineNumberTableAttributeTrimmer())));//,
//                                           new ClassPoolFiller(newLambdaGroupClassPool))));
        //lambdaGroupClassPool.clear();
        //newLambdaGroupClassPool.classesAccept(new ClassPoolFiller(lambdaGroupClassPool));
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
     * Checks whether the given lambda class should still be merged.
     * Returns true if the lambda class has not yet been merged and is allowed to be merged.
     * @param lambdaClass the lambda class for which should be checked whether it should be merged
     */
    public static boolean shouldMerge(ProgramClass lambdaClass)
    {
        ProgramClassOptimizationInfo optimizationInfo = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(lambdaClass);
        return (lambdaClass.getProcessingFlags() & (ProcessingFlags.DONT_OPTIMIZE | ProcessingFlags.DONT_SHRINK)) == 0
                && lambdaClass.extendsOrImplements(NAME_KOTLIN_LAMBDA)
                && optimizationInfo != null // if optimisation info is null, then the lambda was not enqueued to be merged
                && optimizationInfo.getLambdaGroup() == null
                && optimizationInfo.mayBeMerged();
    }

    public static void ensureCanMerge(ProgramClass lambdaClass, boolean mergeLambdaClassesWithUnexpectedMethods, ClassPool programClassPool) throws IllegalArgumentException
    {
        String externalClassName = ClassUtil.externalClassName(lambdaClass.getName());
        if (!lambdaClassHasExactlyOneInitConstructor(lambdaClass))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because it has more than 1 <init> constructor.");
        }
        else if (!lambdaClassHasNoBootstrapMethod(lambdaClass))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because it contains a bootstrap method that would not be merged into the lambda group.");
        }
        else if (!lambdaClassHasNoAccessibleStaticMethods(lambdaClass))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because it contains a static method that could be used outside the class itself.");
        }
        else if (!lambdaClassIsNotDirectlyInvoked(lambdaClass))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because it is directly invoked with its specific invoke method.");
        }
        else if (!nonINSTANCEFieldsAreNotReferencedFromSamePackage(lambdaClass, programClassPool))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because one of its fields, other than the 'INSTANCE' field is referenced by one of its inner classes.");
        }
        else if (!lambdaClassHasTotalMethodCodeSizeThatCanBeInlined(lambdaClass))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because its methods are too big to be inlined.");
        }
        else if (!mergeLambdaClassesWithUnexpectedMethods && !lambdaClassHasNoUnexpectedMethods(lambdaClass))
        {
            throw new IllegalArgumentException("Lambda class " + externalClassName + " cannot be merged, because it contains unexpected methods.");
        }
    }

    private static boolean lambdaClassHasNoBootstrapMethod(ProgramClass lambdaClass) {
        final boolean[] bootstrapMethodFound = {false};
        lambdaClass.attributeAccept(Attribute.BOOTSTRAP_METHODS, new AttributeVisitor() {
            @Override
            public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute) {
                bootstrapMethodFound[0] = true;
            }
        });
        return !bootstrapMethodFound[0];
    }

    public static boolean canMerge(ProgramClass lambdaClass)
    {
        return lambdaClassHasExactlyOneInitConstructor(lambdaClass) && lambdaClassHasNoUnexpectedMethods(lambdaClass);
    }

    public static boolean lambdaClassHasNoAccessibleStaticMethods(ProgramClass lambdaClass)
    {
        for (int index = 0; index < lambdaClass.u2methodsCount; index++)
        {
            ProgramMethod method = lambdaClass.methods[index];
            if (!method.getName(lambdaClass).equals(ClassConstants.METHOD_NAME_CLINIT)
                && (method.getAccessFlags() & AccessConstants.STATIC) != 0
                && ((method.getAccessFlags() & AccessConstants.PRIVATE) == 0))
            {
                logger.warn("Lambda class {} contains a static method that cannot be merged: {}", ClassUtil.externalClassName(lambdaClass.getName()), ClassUtil.externalFullMethodDescription(lambdaClass.getName(), method.getAccessFlags(), method.getName(lambdaClass), method.getDescriptor(lambdaClass)));
                return false;
            }
        }
        return true;
    }

    public static boolean lambdaClassHasTotalMethodCodeSizeThatCanBeInlined(ProgramClass lambdaClass)
    {
        String methodNameRegularExpression = "!" + ClassConstants.METHOD_NAME_INIT;
        methodNameRegularExpression += ",!" + ClassConstants.METHOD_NAME_CLINIT;
        // TODO: create a dedicated CodeSizeCounter visitor
        final int[] totalCodeSize = {0};
        lambdaClass.methodsAccept(new MemberNameFilter(methodNameRegularExpression,
                                  new AllAttributeVisitor(new AttributeVisitor() {
                                      @Override
                                      public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

                                      @Override
                                      public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
                                          totalCodeSize[0] += codeAttribute.u4codeLength;
                                      }
                                  })));
        return totalCodeSize[0] <= KotlinLambdaGroupBuilder.MAXIMUM_INLINED_INVOKE_METHOD_CODE_LENGTH;
    }

    public static boolean lambdaClassHasNoUnexpectedMethods(ProgramClass lambdaClass)
    {
        for (int methodIndex = 0; methodIndex < lambdaClass.u2methodsCount; methodIndex++)
        {
            Method method = lambdaClass.methods[methodIndex];
            String methodName = method.getName(lambdaClass);
            switch (methodName)
            {
                case ClassConstants.METHOD_NAME_INIT:
                case ClassConstants.METHOD_NAME_CLINIT:
                case KotlinLambdaGroupInvokeMethodBuilder.METHOD_NAME_INVOKE:
                    break;
                default:
                    logger.warn("Lambda class {} contains an unexpected method: {}", ClassUtil.externalClassName(lambdaClass.getName()), ClassUtil.externalFullMethodDescription(lambdaClass.getName(), method.getAccessFlags(), methodName, method.getDescriptor(lambdaClass)));
                    return false;
            }
        }
        return true;
    }

    public static boolean lambdaClassHasExactlyOneInitConstructor(ProgramClass lambdaClass)
    {
        int count = 0;
        for (int methodIndex = 0; methodIndex < lambdaClass.u2methodsCount; methodIndex++)
        {
            Method method = lambdaClass.methods[methodIndex];
            if (method.getName(lambdaClass).equals(ClassConstants.METHOD_NAME_INIT))
            {
                count++;
            }
        }
        if (count != 1)
        {
            logger.warn("Lambda class {} has {} <init> constructors.", ClassUtil.externalClassName(lambdaClass.getName()), count);
        }
        return count == 1;
    }

    public static boolean lambdaClassIsNotDirectlyInvoked(ProgramClass lambdaClass)
    {
        Method invokeMethod = KotlinLambdaGroupBuilder.getInvokeMethod(lambdaClass, false);
        if (invokeMethod == null)
        {
            return true;
        }
        MethodReferenceFinder methodReferenceFinder = new MethodReferenceFinder(invokeMethod, lambdaClass);
        lambdaClass.attributeAccept(Attribute.ENCLOSING_METHOD, new AttributeVisitor() {
            @Override
            public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute) {
                enclosingMethodAttribute.referencedClass.constantPoolEntriesAccept(methodReferenceFinder);
            }
        });
        return !methodReferenceFinder.methodReferenceFound();
    }

    public static boolean nonINSTANCEFieldsAreNotReferencedFromSamePackage(ProgramClass lambdaClass, ClassPool programClassPool)
    {
        AtomicBoolean nonINSTANCEFieldReferenced = new AtomicBoolean(false);
        programClassPool.classesAccept(new ClassNameFilter(ClassUtil.internalPackagePrefix(lambdaClass.getName()) + "*",
                                       new ClassNameFilter(lambdaClass.getName(), (ClassVisitor)null,
                                       clazz1 -> {
                                           if (clazz1 != lambdaClass) {
                                               clazz1.constantPoolEntriesAccept(new ConstantVisitor() {
                                                   @Override
                                                   public void visitAnyConstant(Clazz clazz, Constant constant) {
                                                   }

                                                   @Override
                                                   public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant) {
                                                       if (fieldrefConstant.referencedClass == lambdaClass
                                                               && !KotlinConstants.KOTLIN_OBJECT_INSTANCE_FIELD_NAME.equals(fieldrefConstant.getName(clazz)))
                                                       {
                                                           nonINSTANCEFieldReferenced.set(true);
                                                           logger.warn("{} references non-INSTANCE field {} of lambda class {}.",
                                                                       ClassUtil.externalClassName(clazz.getName()),
                                                                       ClassUtil.externalFullFieldDescription(fieldrefConstant.referencedField.getAccessFlags(),
                                                                                                              fieldrefConstant.getName(clazz),
                                                                                                              fieldrefConstant.getType(clazz)),
                                                                       ClassUtil.externalClassName(lambdaClass.getName()));
                                                       }
                                                   }
                                               });
                                           }
                                       })));
        return !nonINSTANCEFieldReferenced.get();
    }
}
