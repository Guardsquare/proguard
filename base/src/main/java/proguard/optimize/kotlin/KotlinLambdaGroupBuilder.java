package proguard.optimize.kotlin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.Configuration;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AllInnerClassesInfoVisitor;
import proguard.classfile.attribute.visitor.ClassConstantToClassVisitor;
import proguard.classfile.attribute.visitor.InnerClassInfoClassConstantVisitor;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.editor.InterfaceAdder;
import proguard.classfile.util.ClassReferenceInitializer;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.*;
import proguard.io.ExtraDataEntryNameMap;
import proguard.optimize.MethodInlinerWrapper;
import proguard.optimize.info.ProgramClassOptimizationInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This ClassVisitor can be used to visit Kotlin lambda classes that should be merged into one lambda group.
 */
public class KotlinLambdaGroupBuilder implements ClassVisitor {

    public static final String FIELD_NAME_ID = "classId";
    public static final String FIELD_TYPE_ID = "I";
    public static final String FIELD_NAME_PREFIX_FREE_VARIABLE = "freeVar";
    public static final String FIELD_TYPE_FREE_VARIABLE = "Ljava/lang/Object;";
    public static final String METHOD_NAME_SUFFIX_INVOKE = "$invoke";

    private final ClassBuilder classBuilder;
    private final Configuration configuration;
    private final ClassPool programClassPool;
    private final ClassPool libraryClassPool;
    private final Map<Integer, KotlinLambdaGroupInvokeMethodBuilder> invokeMethodBuilders;
    private final InterfaceAdder interfaceAdder;
    private final ExtraDataEntryNameMap extraDataEntryNameMap;
    private static final Logger logger = LogManager.getLogger(KotlinLambdaGroupBuilder.class);

    /**
     * Initialises a new Kotlin lambda group builder with the given name as the name for the lambda group to be built.
     * @param lambdaGroupName a name for the new lambda group
     * @param programClassPool a program class pool containing classes that can be referenced by the new lambda group
     * @param libraryClassPool a library class pool containing classes that can be referenced by the new lambda group
     */
    public KotlinLambdaGroupBuilder(String lambdaGroupName, Configuration configuration, ClassPool programClassPool, ClassPool libraryClassPool,
                                    final ExtraDataEntryNameMap extraDataEntryNameMap)
    {
        this.classBuilder = getNewLambdaGroupClassBuilder(lambdaGroupName, programClassPool, libraryClassPool);
        this.configuration = configuration;
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;
        this.invokeMethodBuilders = new HashMap<>();
        this.interfaceAdder = new InterfaceAdder(this.classBuilder.getProgramClass());
        this.extraDataEntryNameMap = extraDataEntryNameMap;
        initialiseLambdaGroup();
    }

    private static ClassBuilder getNewLambdaGroupClassBuilder(String lambdaGroupName, ClassPool programClassPool, ClassPool libraryClassPool)
    {
        // The initial builder is used to set up the initial lambda group class
        ClassBuilder initialBuilder = new ClassBuilder(VersionConstants.CLASS_VERSION_1_8,
                                                       AccessConstants.PUBLIC,
                                                       lambdaGroupName,
                                                       KotlinLambdaMerger.NAME_KOTLIN_LAMBDA);
        ProgramClass lambdaGroup = initialBuilder.getProgramClass();
        // The new builder receives the class pools, such that references can be added when necessary
        return new ClassBuilder(lambdaGroup, programClassPool, libraryClassPool);
    }

    private void initialiseLambdaGroup()
    {
        addIdField();
        addFreeVariableFields();
    }

    private void addIdField()
    {
        classBuilder.addAndReturnField(AccessConstants.PRIVATE, FIELD_NAME_ID, FIELD_TYPE_ID);
    }

    private void addFreeVariableFields()
    {
        // TODO: add support for non-empty closures
        //  by adding fields
    }

    private KotlinLambdaGroupInvokeMethodBuilder getInvokeMethodBuilder(int arity)
    {
        KotlinLambdaGroupInvokeMethodBuilder builder = this.invokeMethodBuilders.get(arity);
        if (builder == null)
        {
            builder = new KotlinLambdaGroupInvokeMethodBuilder(arity, this.classBuilder, this.programClassPool, this.libraryClassPool);
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

        // Add interfaces of lambda class to the lambda group
        // TODO: ensure that only Function interfaces are added
        lambdaClass.interfaceConstantsAccept(this.interfaceAdder);

        // TODO: check whether lambda class has inner lambda's
        //  if so, visit inner lambda's first
        // TODO: ensure that visited inner classes are lambda's
        // TODO: ensure that visited inner classes are lambda's of the expected type (arity, closure size, free variable types)
        lambdaClass.attributeAccept(Attribute.INNER_CLASSES,
                                    new AllInnerClassesInfoVisitor(
                                    new InnerClassInfoClassConstantVisitor(
                                    new ClassConstantToClassVisitor(
                                    new ClassNameFilter(lambdaClass.getName(),
                                                        (ClassVisitor)null,
                                                        (ClassVisitor)this)), // don't revisit the current lambda
                                    null)));

        ProgramClass lambdaGroup = this.classBuilder.getProgramClass();
        logger.info("Adding lambda {} to lambda group {}", lambdaClass.getName(), lambdaGroup.getName());

        inlineLambdaInvokeMethods(lambdaClass);
        ProgramMethod copiedMethod = copyLambdaInvokeToLambdaGroup(lambdaClass);
        int arity = ClassUtil.internalMethodParameterCount(copiedMethod.getDescriptor(lambdaGroup));
        if (arity == 1 && lambdaClass.extendsOrImplements(KotlinLambdaMerger.NAME_KOTLIN_FUNCTIONN)
                && Objects.equals(copiedMethod.getDescriptor(lambdaGroup), KotlinLambdaGroupInvokeMethodBuilder.METHOD_TYPE_INVOKE_FUNCTIONN))
        {
            arity = -1;
        }
        int lambdaClassId = getInvokeMethodBuilder(arity).addCallTo(copiedMethod);

        // replace instantiation of lambda class with instantiation of lambda group with correct id
        updateLambdaInstantiationSite(lambdaClass, lambdaClassId);

        // update optimisation info of lambda to show lambda has been merged
        ProgramClassOptimizationInfo optimizationInfo = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(lambdaClass);
        optimizationInfo.setLambdaGroup(this.classBuilder.getProgramClass());
    }

    private void inlineMethodsInsideClass(ProgramClass lambdaClass)
    {
        // TODO: use a simpler, more reliable, specific method inliner instead of the current wrapper around the MethodInliner
        ClassPool lambdaClassPool = new ClassPool(lambdaClass);
        lambdaClassPool.accept(new MethodInlinerWrapper(this.configuration, this.programClassPool, this.libraryClassPool));
    }

    private void inlineLambdaInvokeMethods(ProgramClass lambdaClass)
    {
        // Make the non-bridge invoke methods private, so they can be inlined.
        lambdaClass.accept(new AllMethodVisitor(
                new MemberVisitor() {
                    @Override
                    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
                        if ((programMethod.u2accessFlags & AccessConstants.BRIDGE) == 0)
                        {
                            if (Objects.equals(programMethod.getName(programClass), KotlinLambdaGroupInvokeMethodBuilder.METHOD_NAME_INVOKE))
                            {
                                programMethod.u2accessFlags &= ~AccessConstants.PUBLIC;
                                programMethod.u2accessFlags |= AccessConstants.PRIVATE;
                            }
                        }
                    }
                }
        ));
        inlineMethodsInsideClass(lambdaClass);
    }

    private ProgramMethod copyLambdaInvokeToLambdaGroup(ProgramClass lambdaClass)
    {
        logger.info("Copying invoke method of {} to lambda group {}", lambdaClass.getName(), this.classBuilder.getProgramClass().getName());

        // Note: the lambda class is expected to contain two invoke methods:
        //      - a bridge method that implements invoke()Ljava/lang/Object; for the Function0 interface
        //      - a specific method that contains the implementation of the lambda
        // Assumption: the specific invoke method has been inlined into the bridge invoke method, such that
        // copying the bridge method to the lambda group is sufficient to retrieve the full implementation
        ProgramMethod invokeMethod = getInvokeMethod(lambdaClass);
        if (invokeMethod == null)
        {
            throw new NullPointerException("No invoke method was found in lambda class " + lambdaClass);
        }
        String newMethodName = createDerivedInvokeMethodName(lambdaClass);
        invokeMethod.accept(lambdaClass, new MethodCopier(this.classBuilder.getProgramClass(), newMethodName));
        return (ProgramMethod)this.classBuilder.getProgramClass().findMethod(newMethodName, invokeMethod.getDescriptor(lambdaClass));
        // TODO: ensure that fields that are referenced by the copied method exist in the lambda group, are initialised,
        //  and cast to the correct type inside the copied method
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
    private static ProgramMethod getInvokeMethod(ProgramClass lambdaClass)
    {
        // Assuming that all specific invoke methods have been inlined into the bridge invoke methods
        // we can take the bridge invoke method (which overrides the invoke method of the FunctionX interface)
        ProgramMethod nonBridgeInvokeMethod = null;
        for (int methodIndex = 0; methodIndex < lambdaClass.u2methodsCount; methodIndex++) {
            ProgramMethod method = lambdaClass.methods[methodIndex];
            if (method.getName(lambdaClass).equals(KotlinLambdaGroupInvokeMethodBuilder.METHOD_NAME_INVOKE))
            {
                if ((method.u2accessFlags & AccessConstants.BRIDGE) != 0) {
                    // we have found the bridge invoke method
                    return method;
                }
                nonBridgeInvokeMethod = method;
            }
        }
        return nonBridgeInvokeMethod;
    }

    /**
     * Updates enclosing method of the given lambdaClass to instantiate the lambda group that is built by this builder.
     * @param lambdaClass the lambda class of which the enclosing method must be updated
     * @param lambdaClassId the id that is used for the given lambda class to identify its implementation in the lambda group
     */
    private void updateLambdaInstantiationSite(ProgramClass lambdaClass, int lambdaClassId)
    {
        logger.info("Updating instantiation of {} in enclosing method to use id {}.", lambdaClass.getName(), lambdaClassId);
        lambdaClass.attributeAccept(Attribute.ENCLOSING_METHOD,
                                    new KotlinLambdaEnclosingMethodUpdater(this.programClassPool,
                                                                           this.libraryClassPool,
                                                                           this.classBuilder.getProgramClass(),
                                                                           lambdaClassId,
                                                                           this.extraDataEntryNameMap));
    }

    private void addInitConstructors()
    {
        // TODO: add support for non-empty closures
        KotlinLambdaGroupInitBuilder builder = new KotlinLambdaGroupInitBuilder(0, this.classBuilder);
        builder.build();
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
        // create <init>(int id)
        // create invoke(...) method, based on invokeArity
        //
        addInitConstructors();
        addInvokeMethods();
        return this.classBuilder.getProgramClass();
    }
}
