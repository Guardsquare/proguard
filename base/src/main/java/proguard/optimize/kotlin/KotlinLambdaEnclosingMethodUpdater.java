package proguard.optimize.kotlin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.Configuration;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.InnerClassesInfoVisitor;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.RefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.ConstantPoolShrinker;
import proguard.classfile.editor.InnerClassesAttributeEditor;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.ClassPrinter;
import proguard.classfile.visitor.MemberVisitor;
import proguard.resources.file.ResourceFilePool;
import proguard.shrink.Shrinker;

import java.io.IOException;
import java.util.*;

public class KotlinLambdaEnclosingMethodUpdater implements AttributeVisitor, MemberVisitor, InstructionVisitor {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private final ProgramClass lambdaGroup;
    private final int classId;
    private boolean visitEnclosingMethodAttribute = false;
    private boolean visitEnclosingMethod = false;
    private boolean visitEnclosingCode = false;
    private static final Logger logger = LogManager.getLogger(KotlinLambdaEnclosingMethodUpdater.class);
    private Clazz currentLambdaClass;
    private SortedSet<Integer> offsetsWhereLambdaIsReferenced;
    private int lambdaConstantIndex;

    public KotlinLambdaEnclosingMethodUpdater(ClassPool        programClassPool,
                                              ClassPool        libraryClassPool,
                                              ProgramClass lambdaGroup,
                                              int classId) {
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;
        this.lambdaGroup = lambdaGroup;
        this.classId = classId;
    }

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute)
    {

    }

    @Override
    public void visitEnclosingMethodAttribute(Clazz lambdaClass, EnclosingMethodAttribute enclosingMethodAttribute) {
        // the given method must be the method where the lambda is defined
        Clazz enclosingClass = enclosingMethodAttribute.referencedClass;
        if (visitEnclosingMethodAttribute || enclosingClass == lambdaClass) {
            return;
        }

        visitEnclosingMethodAttribute = true;
        currentLambdaClass = lambdaClass;
        enclosingMethodAttribute.referencedMethodAccept(this);
        currentLambdaClass = null;
        visitEnclosingMethodAttribute = false;

        // remove lambda class as inner class of its enclosing class
        enclosingClass.attributeAccept("InnerClasses", new InnerClassRemover(lambdaClass));

        // remove all references to lambda class from the constant pool of its enclosing class
        enclosingClass.accept(new ConstantPoolShrinker());

    }

    @Override
    public void visitProgramMethod(ProgramClass enclosingClass, ProgramMethod enclosingMethod) {
        // the given class must be the class that defines the lambda
        // the given method must be the method where the lambda is defined
        if (!visitEnclosingMethodAttribute || visitEnclosingMethod) {
            return;
        }
        this.lambdaConstantIndex = findConstantIndexOfLambdaInEnclosingClass(this.currentLambdaClass, enclosingClass);
        if (this.lambdaConstantIndex < 0)
        {
            // the lambda class is not used: this should not happen
            logger.warn("Lambda class {} not found in constant pool of class {}", this.currentLambdaClass, enclosingClass);
            return;
        }
        this.offsetsWhereLambdaIsReferenced = new TreeSet<>();
        visitEnclosingMethod = true;
        enclosingMethod.attributesAccept(enclosingClass, this);
        visitEnclosingMethod = false;
    }

    private int findConstantIndexOfLambdaInEnclosingClass(Clazz lambdaClass, ProgramClass enclosingClass)
    {
        ClassConstantFinder finder = new ClassConstantFinder(lambdaClass);
        logger.info("{} constants in constant pool of class {}", enclosingClass.u2constantPoolCount, enclosingClass);
        for (int index = 0; index < enclosingClass.u2constantPoolCount; index++)
        {
            try {
                enclosingClass.constantPoolEntryAccept(index, finder);
                if (finder.isClassFound()) {
                    return index;
                }
            }
            catch (NullPointerException exception)
            {
                logger.error("Could not read constant at index {} in constant pool of class {}", index, enclosingClass);
            }
        }
        return -1;
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
        if (!visitEnclosingMethodAttribute || !visitEnclosingMethod || visitEnclosingCode)
        {
            return;
        }
        visitEnclosingCode = true;
        // find the offsets where the lambda class is instantiated or loaded or referenced
        codeAttribute.instructionsAccept(enclosingClass, enclosingMethod, this);

        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        // enclosing method {
        //   ...
        //   Function0 lambda = new LambdaGroup(classId);
        //   ...
        // }
        // create instructions to instantiate the lambda group with the correct id
        InstructionSequenceBuilder builder = new InstructionSequenceBuilder(enclosingClass,
                this.programClassPool,
                this.libraryClassPool);
        Instruction[] instantiateLambdaGroupInstructions = builder
                .new_(lambdaGroup)
                .dup()
                .iconst(this.classId)
                .invokespecial(lambdaGroup, lambdaGroup.findMethod(ClassConstants.METHOD_NAME_INIT, "(I)V"))
                .instructions();

        codeAttributeEditor.reset(codeAttribute.u4codeLength);

        logger.info("Amount of offsets where {} is referenced in {}: {}", this.currentLambdaClass, enclosingClass, this.offsetsWhereLambdaIsReferenced.size());
        for (int offset : this.offsetsWhereLambdaIsReferenced)
        {
            codeAttributeEditor.replaceInstruction(offset, instantiateLambdaGroupInstructions);
        }

        codeAttributeEditor.visitCodeAttribute(enclosingClass, enclosingMethod, codeAttribute);

        logger.debug("Updated enclosing class after modifying enclosing method code:");
        //enclosingClass.accept(new ClassPrinter());

        visitEnclosingCode = false;
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
    }

    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
        try
        {
            visitConstantInstruction((ProgramClass) clazz, (ProgramMethod) method, codeAttribute, offset, constantInstruction);
        }
        catch (ClassCastException exception)
        {
            logger.error(exception);
        }
    }

    public void visitConstantInstruction(ProgramClass enclosingClass, ProgramMethod enclosingMethod, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        // check if constant instruction refers to constant in constant pool that is related to the lambda class: the lambda itself, a field in the lambda class
        /* we expect:
            new LambdaClass
            dup
            invokespecial LambdaClass.<init>(...)V
            ...
           or:
            getfield LambdaClass.INSTANCE:LambdaClass
         */

        // note offset of this instruction, so it can be replaced later
        //logger.info("ConstantInstruction {}", constantInstruction);
        //logger.info("Lambda constant index: {}", this.lambdaConstantIndex);
        if (constantReferencesClass(enclosingClass.getConstant(constantInstruction.constantIndex), this.currentLambdaClass))
        {
            this.offsetsWhereLambdaIsReferenced.add(offset);
        }
    }

    private boolean constantReferencesClass(Constant constant, Clazz clazz)
    {
        try
        {
            return constantReferencesClass((RefConstant) constant, clazz);
        }
        catch (ClassCastException exception)
        {
            return false;
        }
    }

    private boolean constantReferencesClass(RefConstant refConstant, Clazz clazz)
    {
        return refConstant.referencedClass == clazz || Objects.equals(refConstant.referencedClass.getName(), clazz.getName());
    }
}

class ClassConstantFinder implements ConstantVisitor
{
    private Clazz classToBeFound;
    private boolean classFound = false;
    private static final Logger logger = LogManager.getLogger(ClassConstantFinder.class);

    public ClassConstantFinder(Clazz classToBeFound)
    {
        this.classToBeFound = classToBeFound;
    }

    @Override
    public void visitAnyConstant(Clazz clazz, Constant constant)
    {
    }

    @Override
    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        //logger.info("Visiting class constant {}, referencing class {}", classConstant, classConstant.referencedClass);
        if (classConstant.referencedClass == this.classToBeFound || Objects.equals(classConstant.referencedClass.getName(), this.classToBeFound.getName()))
        {
            this.classFound = true;
        }
    }

    public boolean isClassFound()
    {
        return this.classFound;
    }
}

class InnerClassRemover implements AttributeVisitor, InnerClassesInfoVisitor
{
    private final Clazz classToBeRemoved;
    private Set<InnerClassesInfo> innerClassesEntriesToBeRemoved = new HashSet<>();
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
        logger.info("{} inner class entries are removed from class {}", innerClassesEntriesToBeRemoved.size(), clazz);
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
            logger.info("Removing inner classes entry of class {} enqueued to be removed from class {}", innerClassName, clazz);
            innerClassesEntriesToBeRemoved.add(innerClassesInfo);
        }
    }
}