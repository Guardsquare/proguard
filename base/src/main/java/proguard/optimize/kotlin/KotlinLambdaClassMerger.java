package proguard.optimize.kotlin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.Configuration;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.ExceptionsAttribute;
import proguard.classfile.attribute.LineNumberTableAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.editor.ClassEditor;
import proguard.classfile.editor.ConstantPoolEditor;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.ClassSuperHierarchyInitializer;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.*;
import proguard.io.ExtraDataEntryNameMap;
import proguard.optimize.info.ProgramClassOptimizationInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KotlinLambdaClassMerger implements ClassPoolVisitor {

    private static final String NAME_LAMBDA_GROUP = "LambdaGroup";
    private final ClassVisitor lambdaGroupVisitor;
    private final Configuration configuration;
    private final ClassPool programClassPool;
    private final ClassPool libraryClassPool;
    private final ExtraDataEntryNameMap extraDataEntryNameMap;
    private static final Logger logger = LogManager.getLogger(KotlinLambdaClassMerger.class);

    public KotlinLambdaClassMerger(final Configuration         configuration,
                                   final ClassPool             programClassPool,
                                   final ClassPool             libraryClassPool,
                                   final ClassVisitor          lambdaGroupVisitor,
                                   final ExtraDataEntryNameMap extraDataEntryNameMap)
    {
        this.configuration         = configuration;
        this.programClassPool      = programClassPool;
        this.libraryClassPool      = libraryClassPool;
        this.lambdaGroupVisitor    = lambdaGroupVisitor;
        this.extraDataEntryNameMap = extraDataEntryNameMap;
    }

    @Override
    public void visitClassPool(ClassPool lambdaClassPool)
    {
        // choose a name for the lambda group
        // ensure that the lambda group is in the same package as the classes of the class pool
        String lambdaGroupName = getPackagePrefixOfClasses(lambdaClassPool) + NAME_LAMBDA_GROUP;
        logger.info("Creating lambda group with name {}", lambdaGroupName);

        // create a lambda group builder
        KotlinLambdaGroupBuilder lambdaGroupBuilder = new KotlinLambdaGroupBuilder(lambdaGroupName,
                                                                                   this.configuration,
                                                                                   this.programClassPool,
                                                                                   this.libraryClassPool);

        // visit each lambda of this package to add their implementations to the lambda group
        lambdaClassPool.classesAccept(lambdaGroupBuilder);

        ProgramClass lambdaGroup = lambdaGroupBuilder.build();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(lambdaGroup);
        ProgramClassOptimizationInfo optimizationInfo = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(lambdaGroup);
        optimizationInfo.setLambdaGroup(lambdaGroup);

        // let the lambda group visitor visit the newly created lambda group
        this.lambdaGroupVisitor.visitProgramClass(lambdaGroup);

        // ensure that this newly created class is part of the resulting output
        extraDataEntryNameMap.addExtraClass(lambdaGroup.getName());
    }

    private static String getPackagePrefixOfClasses(ClassPool classPool)
    {
        // Assume that all classes in the given class pool are in the same package.
        String someClassName = classPool.classNames().next();
        return ClassUtil.internalPackagePrefix(someClassName);
    }
}