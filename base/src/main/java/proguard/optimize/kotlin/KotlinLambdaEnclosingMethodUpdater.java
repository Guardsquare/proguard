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
    private SortedSet<Integer> offsetsWhereLambdaIsReferenced;
    private static final RetargetedInnerClassAttributeRemover retargetedInnerClassAttributeRemover = new RetargetedInnerClassAttributeRemover();

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
        /*
        enclosingMethodAttribute.referencedMethodAccept(this);
        if (enclosingMethodAttribute.referencedMethod == null)
        {
            logger.warn("No enclosing method was found for {}, so lambda merging will break the code that uses this class.", lambdaClass);
            logger.info("Assuming that {} is used in the <clinit> constructor of {}", lambdaClass, enclosingClass);
            Method clinitMethod = enclosingClass.findMethod(ClassConstants.METHOD_NAME_CLINIT, null);
            if (clinitMethod == null)
            {
                logger.warn("No <clinit> method found for {}, so the usage of {} will not be updated ...", enclosingClass, lambdaClass);
            }
            else
            {
                clinitMethod.accept(enclosingClass, this);
            }
        }
        else {
            Method inlinedMethod = enclosingClass.findMethod(enclosingMethodAttribute.referencedMethod.getName(enclosingClass) + "$$forInline", null);
            if (inlinedMethod != null)
            {
                logger.info("The enclosing method of lambda class {} has an inlined version, which will also be updated.", lambdaClass);
                inlinedMethod.accept(enclosingClass, this);
            }
        }
        */

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
            logger.error("{} is incorrectly used to visit non-program class / method {} / {}", this.getClass().getName(), enclosingClass, enclosingMethod);
        }
    }

    public void visitCodeAttribute(ProgramClass enclosingClass, ProgramMethod enclosingMethod, CodeAttribute codeAttribute)
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

        InstructionSequencesReplacer replacer = createInstructionSequenceReplacer(branchTargetFinder, codeAttributeEditor);

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
        // TODO: ensure that the correct <init> method is selected
        // TODO: decide what to do if multiple <init> methods exist
        Method initMethod   = currentLambdaClass.findMethod(ClassConstants.METHOD_NAME_INIT, null);
        Method specificInvokeMethod = KotlinLambdaGroupBuilder.getInvokeMethod((ProgramClass)currentLambdaClass, false);
        Method bridgeInvokeMethod = KotlinLambdaGroupBuilder.getInvokeMethod((ProgramClass)currentLambdaClass, true);
        return new Instruction[][][]
                {
                        // Empty closure lambda's
                        {
                                // Lambda is 'instantiated' by referring to its static INSTANCE field
                                builder.getstatic(currentLambdaClass.getName(),
                                        KotlinConstants.KOTLIN_OBJECT_INSTANCE_FIELD_NAME,
                                        "L" + currentLambdaClass.getName() + ";").__(),

                                builder.new_(lambdaGroup)
                                        .dup()
                                        .iconst(classId)
                                        .iconst(arity)
                                        .invokespecial(lambdaGroup.getName(), ClassConstants.METHOD_NAME_INIT, "(II)V").__()
                        },
                        {
                                // Lambda is explicitly instantiated
                                builder.new_(currentLambdaClass)
                                        .dup()
                                        .invokespecial(currentLambdaClass, initMethod).__(),

                                builder.new_(lambdaGroup)
                                        .dup()
                                        .iconst(classId)
                                        .iconst(arity)
                                        .invokespecial(lambdaGroup.getName(), ClassConstants.METHOD_NAME_INIT, "(II)V").__()
                        },
                        // Non-empty closure lambda's
                        {
                                // Lambda is explicitly instantiated with free variables as arguments (part 1)
                                builder.new_(currentLambdaClass)
                                        //.dup()
                                        .__(),

                                builder.new_(lambdaGroup)
                                        //.dup()
                                        .__()
                        },
                        {
                                builder.invokespecial(currentLambdaClass, initMethod).__(),

                                builder.iconst(classId)
                                       .iconst(arity)
                                       .invokespecial(lambdaGroup.getName(), ClassConstants.METHOD_NAME_INIT, constructorDescriptor)
                                       .__()
                        },
                        // Direct invocation of named lambda's
                        {
                                builder.invokevirtual(currentLambdaClass, specificInvokeMethod)
                                       .__(),

                                builder.invokevirtual(lambdaGroup.getName(), KotlinLambdaGroupInvokeMethodBuilder.METHOD_NAME_INVOKE, specificInvokeMethod.getDescriptor(currentLambdaClass))
                                       .__()
                        },
                        {
                                builder.invokevirtual(currentLambdaClass, bridgeInvokeMethod)
                                       .__(),

                                builder.invokevirtual(lambdaGroup.getName(), KotlinLambdaGroupInvokeMethodBuilder.METHOD_NAME_INVOKE, bridgeInvokeMethod.getDescriptor(currentLambdaClass))
                                       .__()
                        }
                };
    }

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
                logger.warn("Lambda class {} is referenced by {}, while no enclosing class was linked to this lambda class.", currentLambdaClass, programClass);
            }
            else if (currentEnclosingClass.equals(programClass))
            {
                logger.warn("Lambda class {} is referenced by {}, which is not the enclosing class.", currentLambdaClass, programClass);
            }
            // This programClass references the lambda class, so any referencing instructions
            // must be updated.
            programClass.methodsAccept(this);

            // remove any old links between lambda's and their inner classes
            programClass.accept(KotlinLambdaEnclosingMethodUpdater.retargetedInnerClassAttributeRemover);

            // Remove any constants referring to the old lambda class.
            programClass.accept(new ConstantPoolShrinker());
            this.extraDataEntryNameMap.addExtraClassToClass(programClass, currentLambdaClass);
        }
    }
}

class ClassReferenceFinder implements ConstantVisitor
{
    private final Clazz referencedClass;
    private boolean classIsReferenced = false;

    public ClassReferenceFinder(Clazz referencedClass)
    {
        this.referencedClass = referencedClass;
    }

    public void visitAnyConstant(Clazz clazz, Constant constant) {}

    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        if (classConstant.referencedClass != null && classConstant.referencedClass.equals(referencedClass))
        {
            this.classIsReferenced = true;
        }
    }

    public boolean classReferenceFound()
    {
        return this.classIsReferenced;
    }
}

class MethodReferenceFinder implements ConstantVisitor
{
    private final Method referencedMethod;
    private final Clazz referencedClass;
    private boolean methodIsReferenced = false;

    public MethodReferenceFinder(Method referencedMethod, Clazz referencedClass)
    {
        this.referencedMethod = referencedMethod;
        this.referencedClass  = referencedClass;
    }

    public void visitAnyConstant(Clazz clazz, Constant constant) {}

    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        if ((anyMethodrefConstant.referencedMethod != null && anyMethodrefConstant.referencedMethod.equals(referencedMethod)))
            /*||
                (anyMethodrefConstant.referencedClass != null && anyMethodrefConstant.referencedClass.equals(this.referencedClass) &&
                        anyMethodrefConstant.getName(clazz).equals(this.referencedMethod.getName(this.referencedClass)) &&
                        anyMethodrefConstant.referencedMethod.getDescriptor(anyMethodrefConstant.referencedClass).equals(this.referencedMethod.getDescriptor(this.referencedClass))))

             */
        {
            this.methodIsReferenced = true;
        }
    }

    public boolean methodReferenceFound()
    {
        return this.methodIsReferenced;
    }
}

class EnclosingClassRemover implements AttributeVisitor
{
    private final Clazz classToBeRemoved;

    public EnclosingClassRemover(Clazz clazz)
    {
        this.classToBeRemoved = clazz;
    }

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

    @Override
    public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute)
    {
        if (this.classToBeRemoved.equals(enclosingMethodAttribute.referencedClass))
        {

        }
    }

}

class InnerClassRemover implements AttributeVisitor, InnerClassesInfoVisitor
{
    private final Clazz classToBeRemoved;
    private final Set<InnerClassesInfo> innerClassesEntriesToBeRemoved = new HashSet<>();
    private static final Logger logger = LogManager.getLogger(InnerClassRemover.class);

    public InnerClassRemover(Clazz clazz)
    {
        this.classToBeRemoved = clazz;
    }

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {
    }

    @Override
    public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute) {
        innerClassesAttribute.innerClassEntriesAccept(clazz, this);
        InnerClassesAttributeEditor editor = new InnerClassesAttributeEditor(innerClassesAttribute);
        logger.trace("{} inner class entries are removed from class {}", innerClassesEntriesToBeRemoved.size(), clazz);
        for (InnerClassesInfo entry : innerClassesEntriesToBeRemoved)
        {
            editor.removeInnerClassesInfo(entry);
        }
    }

    @Override
    public void visitInnerClassesInfo(Clazz clazz, InnerClassesInfo innerClassesInfo) {
        String innerClassName = clazz.getClassName(innerClassesInfo.u2innerClassIndex);
        if (Objects.equals(innerClassName, this.classToBeRemoved.getName()))
        {
            logger.trace("Removing inner classes entry of class {} enqueued to be removed from class {}", innerClassName, clazz);
            innerClassesEntriesToBeRemoved.add(innerClassesInfo);
        }
    }
}