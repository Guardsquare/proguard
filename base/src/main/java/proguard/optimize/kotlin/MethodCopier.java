package proguard.optimize.kotlin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.MemberVisitor;

class MethodCopier implements MemberVisitor, AttributeVisitor, InstructionVisitor
{
    private final ProgramClass destinationClass;
    private final ClassBuilder classBuilder;
    private final String newMethodNamePrefix;
    private final ConstantAdder constantAdder;
    private final CodeAttributeComposer codeAttributeComposer = new CodeAttributeComposer();
    private final ExceptionInfoAdder exceptionInfoAdder;
    private int methodCounter = 0;
    private static final Logger logger = LogManager.getLogger(MethodCopier.class);


    public MethodCopier(ProgramClass destinationClass, String newMethodNamePrefix)
    {
        this.destinationClass = destinationClass;
        this.classBuilder = new ClassBuilder(destinationClass);
        this.newMethodNamePrefix = newMethodNamePrefix;
        this.constantAdder = new ConstantAdder(destinationClass);
        this.exceptionInfoAdder = new ExceptionInfoAdder(this.destinationClass, this.codeAttributeComposer);
    }

    private int getNewMethodIndex()
    {
        int methodIndex = this.methodCounter;
        this.methodCounter++;
        return methodIndex;
    }

    @Override
    public void visitAnyMember(Clazz clazz, Member member) {
    }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
        codeAttributeComposer.reset();
        programMethod.attributesAccept(programClass, this);
        int methodIndex = getNewMethodIndex();
        String newMethodName = newMethodNamePrefix;
        if (methodIndex > 1)
        {
            newMethodName += "$" + methodIndex;
        }
        ProgramMethod newMethod = classBuilder.addAndReturnMethod(AccessConstants.PRIVATE, newMethodName, programMethod.getDescriptor(programClass));
        codeAttributeComposer.addCodeAttribute(this.destinationClass, newMethod);
    }

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {
    }

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        codeAttributeComposer.beginCodeFragment(codeAttribute.u4codeLength);
        // copy code and exceptions
        codeAttribute.instructionsAccept(clazz, method, this);
        codeAttribute.exceptionsAccept(clazz, method, this.exceptionInfoAdder);
        codeAttribute.attributesAccept(clazz, method, this);
        codeAttributeComposer.endCodeFragment();
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {
        // copy instruction
        codeAttributeComposer.appendInstruction(offset, instruction);
    }

    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
        // ensure the referenced constant is in the constant pool at the correct index
        constantInstruction.constantIndex = this.constantAdder.addConstant(clazz, constantInstruction.constantIndex);
        // copy instruction
        codeAttributeComposer.appendInstruction(offset, constantInstruction);
    }
}
