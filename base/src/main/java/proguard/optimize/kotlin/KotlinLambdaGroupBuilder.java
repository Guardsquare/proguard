package proguard.optimize.kotlin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.Configuration;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.editor.*;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.*;
import proguard.io.ExtraDataEntryNameMap;
import proguard.optimize.info.*;
import proguard.optimize.peephole.SameClassMethodInliner;
import proguard.preverify.CodePreverifier;
import proguard.util.ProcessingFlags;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This ClassVisitor can be used to visit Kotlin lambda classes that should be merged into one lambda group.
 */
public class KotlinLambdaGroupBuilder implements ClassVisitor {

    public static final String FIELD_NAME_ID                             = "classId";
    public static final String FIELD_TYPE_ID                             = "I";
    public static final String METHOD_NAME_SUFFIX_INVOKE                 = "$invoke";
    protected static final int MAXIMUM_INLINED_INVOKE_METHOD_CODE_LENGTH = Integer.parseInt(System.getProperty("maximum.resulting.code.length", "65535"));

    private final ClassBuilder classBuilder;
    private final Configuration configuration;
    private final ClassPool programClassPool;
    private final ClassPool libraryClassPool;
    private final ClassVisitor notMergedLambdaVisitor;
    private final Map<Integer, KotlinLambdaGroupInvokeMethodBuilder> invokeMethodBuilders;
    private final InterfaceAdder interfaceAdder;
    private final ExtraDataEntryNameMap extraDataEntryNameMap;
    private final KotlinLambdaGroupInitUpdater initUpdater;
    private static final Logger logger = LogManager.getLogger(KotlinLambdaGroupBuilder.class);

    /**
     * Initialises a new Kotlin lambda group builder with the given name as the name for the lambda group to be built.
     * @param lambdaGroupName a name for the new lambda group
     * @param programClassPool a program class pool containing classes that can be referenced by the new lambda group
     * @param libraryClassPool a library class pool containing classes that can be referenced by the new lambda group
     */
    public KotlinLambdaGroupBuilder(final String lambdaGroupName,
                                    final Configuration configuration,
                                    final ClassPool programClassPool,
                                    final ClassPool libraryClassPool,
                                    final ExtraDataEntryNameMap extraDataEntryNameMap,
                                    final ClassVisitor notMergedLambdaVisitor)
    {
        this.classBuilder           = getNewLambdaGroupClassBuilder(lambdaGroupName,
                                                                    programClassPool,
                                                                    libraryClassPool);
        this.configuration          = configuration;
        this.programClassPool       = programClassPool;
        this.libraryClassPool       = libraryClassPool;
        this.invokeMethodBuilders   = new HashMap<>();
        this.interfaceAdder         = new InterfaceAdder(this.classBuilder.getProgramClass());
        this.extraDataEntryNameMap  = extraDataEntryNameMap;
        this.notMergedLambdaVisitor = notMergedLambdaVisitor;
        this.initUpdater            = new KotlinLambdaGroupInitUpdater(programClassPool, libraryClassPool);
        initialiseLambdaGroup();
    }

    private static ClassBuilder getNewLambdaGroupClassBuilder(String lambdaGroupName,
                                                              ClassPool programClassPool,
                                                              ClassPool libraryClassPool)
    {
        // The initial builder is used to set up the initial lambda group class
        ClassBuilder initialBuilder = new ClassBuilder(VersionConstants.CLASS_VERSION_1_8,
                                                       AccessConstants.FINAL | AccessConstants.SUPER,
                                                       lambdaGroupName,
                                                       KotlinLambdaMerger.NAME_KOTLIN_LAMBDA);
        ProgramClass lambdaGroup = initialBuilder.getProgramClass();
        lambdaGroup.accept(new ProgramClassOptimizationInfoSetter());

        // The new builder receives the class pools, such that references can be added when necessary
        return new ClassBuilder(lambdaGroup, programClassPool, libraryClassPool);
    }

    private void initialiseLambdaGroup()
    {
        addIdField();
    }

    private void addIdField()
    {
        classBuilder.addAndReturnField(AccessConstants.PRIVATE, FIELD_NAME_ID, FIELD_TYPE_ID);
    }

    private KotlinLambdaGroupInvokeMethodBuilder getInvokeMethodBuilder(int arity)
    {
        KotlinLambdaGroupInvokeMethodBuilder builder = this.invokeMethodBuilders.get(arity);
        if (builder == null)
        {
            builder = new KotlinLambdaGroupInvokeMethodBuilder(arity,
                                                               this.classBuilder,
                                                               this.programClassPool,
                                                               this.libraryClassPool);
            this.invokeMethodBuilders.put(arity, builder);
        }
        return builder;
    }

    @Override
    public void visitAnyClass(Clazz clazz) {}

    @Override
    public void visitProgramClass(ProgramClass lambdaClass) {
        if (!KotlinLambdaMerger.shouldMerge(lambdaClass))
        {
            return;
        }

        try
        {
            mergeLambdaClass(lambdaClass);
        }
        catch(Exception exception)
        {
            logger.error("Lambda class {} could not be merged: {}",
                         ClassUtil.externalClassName(lambdaClass.getName()),
                         exception);
            if (this.notMergedLambdaVisitor != null) {
                lambdaClass.accept(this.notMergedLambdaVisitor);
            }
        }
    }

    private void mergeLambdaClass(ProgramClass lambdaClass)
    {
        KotlinLambdaMerger.ensureCanMerge(lambdaClass, programClassPool);

        ProgramClass lambdaGroup = this.classBuilder.getProgramClass();

        // update optimisation info of lambda to show lambda has been merged or is going to be merged
        ProgramClassOptimizationInfo optimizationInfo = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(lambdaClass);
        optimizationInfo.setLambdaGroup(lambdaGroup);
        optimizationInfo.setTargetClass(lambdaGroup);

        logger.info("Looking at inner lambda's of {}", ClassUtil.externalClassName(lambdaClass.getName()));

        // merge any inner lambda's before merging the current lambda
        lambdaClass.attributeAccept(Attribute.INNER_CLASSES,
                                    new ModifiedAllInnerClassesInfoVisitor(
                                    new InnerClassInfoClassConstantVisitor(
                                    new ClassConstantToClassVisitor(
                                    new ClassNameFilter(lambdaClass.getName(),
                                    (ClassVisitor)null,
                                    (ClassVisitor)this)
                                    ), // don't revisit the current lambda
                                    null)));

        // possibly, some inner lambda classes were not mentioned in the inner classes attribute of this lambda,
        // so we have to find them by looking for their occurrence in this lambda class
        // Normally, this should be redundant if all inner lambda classes are correctly mentioned as inner classes.
        lambdaClass.constantPoolEntriesAccept(new ClassConstantToClassVisitor(
                                              new ClassNameFilter(lambdaClass.getName(),
                                              (ClassVisitor)null,
                                              new ImplementedClassFilter(
                                              programClassPool.getClass(KotlinLambdaMerger.NAME_KOTLIN_LAMBDA),
                                              false,
                                              new MultiClassVisitor(this,
                                              new SimpleClassPrinter(true)),
                                              null))));

        canonicalizeLambdaClassFields(lambdaClass);

        // Add interfaces of lambda class to the lambda group
        // TODO: ensure that only Function interfaces are added
        lambdaClass.interfaceConstantsAccept(this.interfaceAdder);

        logger.info("Adding lambda {} to lambda group {}",
                    ClassUtil.externalClassName(lambdaClass.getName()),
                    ClassUtil.externalClassName(lambdaGroup.getName()));

        // First copy the <init> constructors to the lambda group
        Method initMethod = copyOrMergeLambdaInitIntoLambdaGroup(lambdaClass);

        String constructorDescriptor = initMethod.getDescriptor(lambdaGroup);

        // Then inline the specific invoke methods into the bridge invoke methods, within the lambda class
        inlineLambdaInvokeMethods(lambdaClass);

        // and copy the bridge invoke methods to the lambda group
        ProgramMethod copiedMethod = copyLambdaInvokeToLambdaGroup(lambdaClass);
        int arity = ClassUtil.internalMethodParameterCount(copiedMethod.getDescriptor(lambdaGroup));
        if (arity == 1 && lambdaClass.extendsOrImplements(KotlinLambdaMerger.NAME_KOTLIN_FUNCTIONN)
                && Objects.equals(copiedMethod.getDescriptor(lambdaGroup), KotlinLambdaGroupInvokeMethodBuilder.METHOD_TYPE_INVOKE_FUNCTIONN))
        {
            arity = -1;
        }
        int lambdaClassId = getInvokeMethodBuilder(arity).addCallTo(copiedMethod);


        // replace instantiation of lambda class with instantiation of lambda group with correct id
        updateLambdaInstantiationSite(lambdaClass, lambdaClassId, arity, constructorDescriptor);
        optimizationInfo.setLambdaGroupClassId(lambdaClassId);
        SubclassRemover subclassRemover = new SubclassRemover(lambdaClass);
        lambdaClass.getSuperClass().accept(subclassRemover);
        lambdaClass.interfaceConstantsAccept(new ClassConstantToClassVisitor(
                                             subclassRemover));
    }

    private void canonicalizeLambdaClassFields(ProgramClass lambdaClass)
    {
        FieldRenamer fieldRenamer = new FieldRenamer(true);
        // Assumption: the only name clash of fields of different classes is
        // for fields with the name "INSTANCE".
        // We don't need these fields anyway, so we don't rename them.
        // TODO: handle name clashes correctly - this happens also in the case of inner lambda's
        //  accessing their enclosing lambda class via a public field
        String fieldNameRegularExpression = "!" + KotlinConstants.KOTLIN_OBJECT_INSTANCE_FIELD_NAME;
        // Note: the order of the fields is not necessarily the order in which they are assigned
        // For now, let's assume the order matches the order in which they are assigned.
        lambdaClass.fieldsAccept(new MemberNameFilter(fieldNameRegularExpression,
                                 fieldRenamer));
    }

    private void inlineMethodsInsideClass(ProgramClass lambdaClass)
    {
        lambdaClass.accept(new ProgramClassOptimizationInfoSetter());

        lambdaClass.accept(new AllMemberVisitor(
                           new ProgramMemberOptimizationInfoSetter()));

        // Allow methods to be inlined
        lambdaClass.accept(new AllMethodVisitor(
                           new AllAttributeVisitor(
                           new SameClassMethodInliner(configuration.microEdition,
                                                      configuration.android,
                                                      MAXIMUM_INLINED_INVOKE_METHOD_CODE_LENGTH,
                                                      configuration.allowAccessModification,
                                                      true,
                                                      null))));
    }

    private void inlineLambdaInvokeMethods(ProgramClass lambdaClass)
    {
        // Make the non-bridge invoke methods private, so they can be inlined.
        lambdaClass.methodsAccept(new MemberNameFilter(KotlinConstants.METHOD_NAME_LAMBDA_INVOKE,
                                  new MemberAccessFilter(0, AccessConstants.BRIDGE,
                                  new MultiMemberVisitor(
                                  new MemberAccessFlagCleaner(AccessConstants.PUBLIC),
                                  new MemberAccessFlagSetter(AccessConstants.PRIVATE)))));
        inlineMethodsInsideClass(lambdaClass);
    }

    private ProgramMethod copyOrMergeLambdaInitIntoLambdaGroup(ProgramClass lambdaClass)
    {
        ProgramClass lambdaGroup = this.classBuilder.getProgramClass();
        logger.trace("Copying <init> method of {} to lambda group {}",
                     ClassUtil.externalClassName(lambdaClass.getName()),
                     ClassUtil.externalClassName(this.classBuilder.getProgramClass().getName()));
        ProgramMethod initMethod = (ProgramMethod)lambdaClass.findMethod(ClassConstants.METHOD_NAME_INIT,
                                                                         null);
        if (initMethod == null)
        {
            throw new NullPointerException("No <init> method was found in lambda class " + lambdaClass);
        }
        logger.trace("<init> method of lambda class {}: {}",
                     ClassUtil.externalClassName(lambdaClass.getName()),
                     ClassUtil.externalFullMethodDescription(lambdaClass.getName(),
                                                             initMethod.getAccessFlags(),
                                                             initMethod.getName(lambdaClass),
                                                             initMethod.getDescriptor(lambdaClass)));

        String oldInitDescriptor = initMethod.getDescriptor(lambdaClass);
        String newInitDescriptor = KotlinLambdaGroupInitUpdater.getNewInitMethodDescriptor(lambdaClass, initMethod);

        // Check whether an init method with this descriptor exists already
        ProgramMethod existingInitMethod = (ProgramMethod)lambdaGroup.findMethod(ClassConstants.METHOD_NAME_INIT,
                                                                                 newInitDescriptor);

        if (existingInitMethod != null)
        {
            return existingInitMethod;
        }

        initMethod.accept(lambdaClass, new MethodCopier(lambdaGroup,
                                                        ClassConstants.METHOD_NAME_INIT,
                                                        oldInitDescriptor,
                                                        AccessConstants.PUBLIC));
        ProgramMethod newInitMethod = (ProgramMethod)lambdaGroup.findMethod(ClassConstants.METHOD_NAME_INIT,
                                                                            oldInitDescriptor);

        // Update the <init> descriptor
        // Add the necessary instructions to entirely new <init> methods
        newInitMethod.accept(lambdaGroup, this.initUpdater);
        return newInitMethod;
    }

    private ProgramMethod copyLambdaInvokeToLambdaGroup(ProgramClass lambdaClass)
    {
        logger.trace("Copying invoke method of {} to lambda group {}",
                     ClassUtil.externalClassName(lambdaClass.getName()),
                     ClassUtil.externalClassName(this.classBuilder.getProgramClass().getName()));

        // Note: the lambda class is expected to contain two invoke methods:
        //      - a bridge method that implements invoke()Ljava/lang/Object; for the Function0 interface
        //      - a specific method that contains the implementation of the lambda
        // Assumption: the specific invoke method has been inlined into the bridge invoke method, such that
        // copying the bridge method to th  e lambda group is sufficient to retrieve the full implementation
        ProgramMethod invokeMethod = getBridgeInvokeMethod(lambdaClass);
        if (invokeMethod == null)
        {
            throw new NullPointerException("No invoke method was found in lambda class " +
                                           ClassUtil.externalClassName(lambdaClass.getName()));
        }
        String newMethodName = createDerivedInvokeMethodName(lambdaClass);
        invokeMethod.accept(lambdaClass, new MethodCopier(this.classBuilder.getProgramClass(),
                                                          newMethodName,
                                                          AccessConstants.PRIVATE));
        return (ProgramMethod) this.classBuilder.getProgramClass().findMethod(newMethodName,
                                                                              invokeMethod.getDescriptor(lambdaClass));
    }

    private static String createDerivedInvokeMethodName(ProgramClass lambdaClass)
    {
        String shortClassName = ClassUtil.internalShortClassName(lambdaClass.getName());
        return shortClassName + METHOD_NAME_SUFFIX_INVOKE;
    }

    /**
     * Returns the bridge invoke method of the given class.
     * If no bridge invoke method was found, but a non-bridge invoke method was found, then the non-bridge
     * invoke method is returned. If no invoke method was found, then null is returned.
     * @param lambdaClass the lambda class of which a (bridge) invoke method is to be returned
     */
    private static ProgramMethod getBridgeInvokeMethod(ProgramClass lambdaClass)
    {
        // Assuming that all specific invoke methods have been inlined into the bridge invoke methods
        // we can take the bridge invoke method (which overrides the invoke method of the FunctionX interface)
        return getInvokeMethod(lambdaClass, true);
    }

    public static ProgramMethod getInvokeMethod(ProgramClass lambdaClass, boolean isBridgeMethod)
    {
        ProgramMethod invokeMethod = null;
        for (int methodIndex = 0; methodIndex < lambdaClass.u2methodsCount; methodIndex++) {
            ProgramMethod method = lambdaClass.methods[methodIndex];
            if (method.getName(lambdaClass).equals(KotlinConstants.METHOD_NAME_LAMBDA_INVOKE))
            {
                if ((isBridgeMethod && (method.u2accessFlags & AccessConstants.BRIDGE) != 0) ||
                        (!isBridgeMethod && (method.u2accessFlags & AccessConstants.BRIDGE) == 0)) {
                    // we have found the bridge/non-bridge invoke method
                    return method;
                }
                invokeMethod = method;
            }
        }
        return invokeMethod;
    }

    /**
     * Updates enclosing method of the given lambdaClass to instantiate the lambda group that is built by this builder.
     * @param lambdaClass the lambda class of which the enclosing method must be updated
     * @param lambdaClassId the id that is used for the given lambda class to identify its implementation in the lambda group
     */
    private void updateLambdaInstantiationSite(ProgramClass lambdaClass,
                                               int lambdaClassId,
                                               int arity,
                                               String constructorDescriptor)
    {
        logger.info("Updating instantiation of {} in enclosing method(s) to use id {}.",
                    ClassUtil.externalClassName(lambdaClass.getName()), lambdaClassId);
        KotlinLambdaEnclosingMethodUpdater enclosingMethodUpdater =
                                    new KotlinLambdaEnclosingMethodUpdater(this.programClassPool,
                                                                           this.libraryClassPool,
                                                                           lambdaClass,
                                                                           this.classBuilder.getProgramClass(),
                                                                           lambdaClassId,
                                                                           arity,
                                                                           constructorDescriptor,
                                                                           this.extraDataEntryNameMap);
        lambdaClass.attributeAccept(Attribute.ENCLOSING_METHOD, enclosingMethodUpdater);

        // Also update any references that would occur in other classes of the same package.
        String regularExpression = ClassUtil.internalPackagePrefix(lambdaClass.getName()) + "*";
        regularExpression       += ",!" + lambdaClass.getName();
        this.programClassPool.classesAccept(new ClassNameFilter(regularExpression,
                                                                enclosingMethodUpdater));
    }

    private void addInvokeMethods()
    {
        for (KotlinLambdaGroupInvokeMethodBuilder builder : this.invokeMethodBuilders.values())
        {
            builder.build();
        }
    }

    public ProgramClass build()
    {
        addInvokeMethods();
        ProgramClass lambdaGroup = this.classBuilder.getProgramClass();
        lambdaGroup.setProcessingFlags(lambdaGroup.getProcessingFlags() | ProcessingFlags.INJECTED);
        lambdaGroup.accept(new AllMemberVisitor(
                           new AllAttributeVisitor(
                           new CodePreverifier(configuration.microEdition))));
        return lambdaGroup;
    }
}
