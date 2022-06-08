package proguard.optimize.kotlin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.InnerClassesInfoVisitor;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.BranchTargetFinder;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.*;
import proguard.io.ExtraDataEntryNameMap;
import proguard.optimize.peephole.RetargetedInnerClassAttributeRemover;

import java.util.*;

public class KotlinLambdaEnclosingMethodUpdater implements ClassVisitor, AttributeVisitor, MemberVisitor {

    private final ClassPool programClassPool;
    private final ClassPool libraryClassPool;
    private final ProgramClass lambdaGroup;
    private final int classId;
    private final int arity;
    private final String constructorDescriptor;
    private final ExtraDataEntryNameMap extraDataEntryNameMap;
    private boolean visitEnclosingMethodAttribute = false;
    private boolean visitEnclosingMethod = false;
    private boolean visitEnclosingCode = false;
    private static final Logger logger = LogManager.getLogger(KotlinLambdaEnclosingMethodUpdater.class);
    private Clazz currentLambdaClass;
    private Clazz currentEnclosingClass;
    private static final RetargetedInnerClassAttributeRemover retargetedInnerClassAttributeRemover =
            new RetargetedInnerClassAttributeRemover();

    public KotlinLambdaEnclosingMethodUpdater(ClassPool        programClassPool,
                                              ClassPool        libraryClassPool,
                                              ProgramClass lambdaClass,
                                              ProgramClass lambdaGroup,
                                              int classId,
                                              int arity,
                                              String constructorDescriptor,
                                              ExtraDataEntryNameMap extraDataEntryNameMap)
    {
        this.programClassPool      = programClassPool;
        this.libraryClassPool      = libraryClassPool;
        this.currentLambdaClass    = lambdaClass;
        this.lambdaGroup           = lambdaGroup;
        this.classId               = classId;
        this.arity                 = arity;
        this.constructorDescriptor = constructorDescriptor;
        this.extraDataEntryNameMap = extraDataEntryNameMap;
    }

    // Implementations for MemberVisitor

    @Override
    public void visitProgramMethod(ProgramClass enclosingClass, ProgramMethod enclosingMethod)
    {
        // the given class must be the class that defines the lambda
        // the given method must be the method where the lambda is defined

        if (visitEnclosingMethod) {
            return;
        }
        visitEnclosingMethod = true;
        enclosingMethod.attributesAccept(enclosingClass, this);
        visitEnclosingMethod = false;
    }

    // Implementations for AttributeVisitor

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

    @Override
    public void visitEnclosingMethodAttribute(Clazz lambdaClass, EnclosingMethodAttribute enclosingMethodAttribute)
    {
        // the given method must be the method where the lambda is defined
        Clazz enclosingClass = enclosingMethodAttribute.referencedClass;
        if (visitEnclosingMethodAttribute || enclosingClass == lambdaClass) {
            return;
        }

        visitEnclosingMethodAttribute = true;
        currentLambdaClass = lambdaClass;
        currentEnclosingClass = enclosingClass;

        // Visit all methods of the enclosing class, assuming that those are the only methods that can contain
        // references to this lambda class.
        enclosingClass.methodsAccept(this);

        visitEnclosingMethodAttribute = false;

        // remove lambda class as inner class of its enclosing class
        enclosingClass.attributeAccept(Attribute.INNER_CLASSES,
                                       new InnerClassRemover(lambdaClass));

        // remove all references to lambda class from the constant pool of its enclosing class
        enclosingClass.accept(new ConstantPoolShrinker());

        // ensure that the newly created lambda group is part of the resulting output as a dependency of this enclosing class
        extraDataEntryNameMap.addExtraClassToClass(enclosingClass, this.lambdaGroup);
    }

    @Override
    public void visitCodeAttribute(Clazz enclosingClass, Method enclosingMethod, CodeAttribute codeAttribute)
    {
        // This attribute visitor should only be used for program classes.
        try
        {
            visitCodeAttribute((ProgramClass) enclosingClass, (ProgramMethod) enclosingMethod, codeAttribute);
        }
        catch (ClassCastException exception)
        {
            logger.error("{} is incorrectly used to visit non-program class / method {} / {}",
                         this.getClass().getName(), enclosingClass, enclosingMethod);
        }
    }

    public void visitCodeAttribute(ProgramClass enclosingClass,
                                   ProgramMethod enclosingMethod,
                                   CodeAttribute codeAttribute)
    {
        // the given class must be the class that defines the lambda
        // the given method must be the method where the lambda is defined
        // the given code attribute must contain the original definition of the lambda:
        //  - load LambdaClass.INSTANCE
        //  - or instantiate LambdaClass()
        if (!visitEnclosingMethod || visitEnclosingCode)
        {
            return;
        }
        visitEnclosingCode = true;

        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        BranchTargetFinder branchTargetFinder = new BranchTargetFinder();

        InstructionSequencesReplacer replacer = createInstructionSequenceReplacer(branchTargetFinder,
                                                                                  codeAttributeEditor);

        codeAttribute.accept(enclosingClass,
                             enclosingMethod,
                             new PeepholeEditor(branchTargetFinder,
                                                codeAttributeEditor,
                                                replacer));
        visitEnclosingCode = false;
    }

    private InstructionSequencesReplacer createInstructionSequenceReplacer(BranchTargetFinder branchTargetFinder,
                                                                           CodeAttributeEditor codeAttributeEditor)
    {
        InstructionSequenceBuilder builder = new InstructionSequenceBuilder(programClassPool, libraryClassPool);
        Instruction[][][] replacementPatterns = createReplacementPatternsForLambda(builder);
        return new InstructionSequencesReplacer(builder.constants(),
                                                replacementPatterns,
                                                branchTargetFinder,
                                                codeAttributeEditor);
    }

    private Instruction[][][] createReplacementPatternsForLambda(InstructionSequenceBuilder builder)
    {
        Method initMethod   = currentLambdaClass.findMethod(ClassConstants.METHOD_NAME_INIT, null);
        Method specificInvokeMethod = KotlinLambdaGroupBuilder.getInvokeMethod((ProgramClass)currentLambdaClass,
                                                                               false);
        Method bridgeInvokeMethod = KotlinLambdaGroupBuilder.getInvokeMethod((ProgramClass)currentLambdaClass,
                                                                             true);
        return new Instruction[][][]
                {
                        // Empty closure lambda's
                        {
                                // Lambda is 'instantiated' by referring to its static INSTANCE field
                                builder.getstatic(currentLambdaClass.getName(),
                                                  KotlinConstants.KOTLIN_OBJECT_INSTANCE_FIELD_NAME,
                                                  ClassUtil.internalTypeFromClassName(currentLambdaClass.getName()))
                                        .__(),

                                builder.new_(lambdaGroup)
                                        .dup()
                                        .iconst(classId)
                                        .iconst(arity)
                                        .invokespecial(lambdaGroup.getName(),
                                                       ClassConstants.METHOD_NAME_INIT,
                                                       "(II)V")
                                        .__()
                        },
                        {
                                // Lambda is explicitly instantiated
                                builder.new_(currentLambdaClass)
                                        .dup()
                                        .invokespecial(currentLambdaClass, initMethod)
                                        .__(),

                                builder.new_(lambdaGroup)
                                        .dup()
                                        .iconst(classId)
                                        .iconst(arity)
                                        .invokespecial(lambdaGroup.getName(),
                                                       ClassConstants.METHOD_NAME_INIT,
                                                       "(II)V")
                                        .__()
                        },
                        // Non-empty closure lambda's
                        {
                                // Lambda is explicitly instantiated with free variables as arguments (part 1)
                                builder.new_(currentLambdaClass)
                                        .__(),

                                builder.new_(lambdaGroup)
                                        .__()
                        },
                        {
                                builder.invokespecial(currentLambdaClass, initMethod)
                                        .__(),

                                builder.iconst(classId)
                                       .iconst(arity)
                                       .invokespecial(lambdaGroup.getName(),
                                                      ClassConstants.METHOD_NAME_INIT,
                                                      constructorDescriptor)
                                       .__()
                        },
                        // Direct invocation of named lambda's
                        {
                                builder.invokevirtual(currentLambdaClass, specificInvokeMethod)
                                       .__(),

                                builder.invokevirtual(lambdaGroup.getName(),
                                                      KotlinConstants.METHOD_NAME_LAMBDA_INVOKE,
                                                      specificInvokeMethod.getDescriptor(currentLambdaClass))
                                       .__()
                        },
                        {
                                builder.invokevirtual(currentLambdaClass, bridgeInvokeMethod)
                                       .__(),

                                builder.invokevirtual(lambdaGroup.getName(),
                                                      KotlinConstants.METHOD_NAME_LAMBDA_INVOKE,
                                                      bridgeInvokeMethod.getDescriptor(currentLambdaClass))
                                       .__()
                        }
                };
    }

    // Implementations for ClassVisitor

    @Override
    public void visitAnyClass(Clazz clazz) {}

    @Override
    public void visitProgramClass(ProgramClass programClass) {
        ClassReferenceFinder classReferenceFinder = new ClassReferenceFinder(this.currentLambdaClass);
        programClass.constantPoolEntriesAccept(classReferenceFinder);
        if (classReferenceFinder.classReferenceFound())
        {
            if (currentEnclosingClass == null)
            {
                logger.warn("Lambda class {} is referenced by {}, while no enclosing class was linked to this " +
                            "lambda class.",
                            ClassUtil.externalClassName(currentLambdaClass.getName()),
                            ClassUtil.externalClassName(programClass.getName()));
            }
            else if (!currentEnclosingClass.equals(programClass) && !currentLambdaClass.equals(programClass))
            {
                logger.warn("Lambda class {} is referenced by {}, which is not the enclosing class or the " +
                            "lambda class itself.",
                            ClassUtil.externalClassName(currentLambdaClass.getName()),
                            ClassUtil.externalClassName(programClass.getName()));
            }
            // This programClass references the lambda class, so any referencing instructions
            // must be updated.
            // TODO: consider whether this can be removed, given the fact that all constants are updated anyway
            programClass.methodsAccept(this);

            // In some cases a class uses the lambda class as a type in a field or method descriptor
            // All those constants must be updated as well.
            programClass.constantPoolEntriesAccept(new MultiConstantVisitor(
                                                   new DescriptorTypeUpdater(
                                                           ClassUtil.internalTypeFromClassName(this.currentLambdaClass.getName()),
                                                           ClassUtil.internalTypeFromClassName(this.lambdaGroup.getName())),
                                                   new ClassConstantReferenceUpdater(this.currentLambdaClass,
                                                                                     this.lambdaGroup)));

            // remove any old links between lambda's and their inner classes
            programClass.accept(KotlinLambdaEnclosingMethodUpdater.retargetedInnerClassAttributeRemover);

            // Remove any constants referring to the old lambda class.
            programClass.accept(new ConstantPoolShrinker());
            this.extraDataEntryNameMap.addExtraClassToClass(programClass, this.lambdaGroup);
        }
    }
}
