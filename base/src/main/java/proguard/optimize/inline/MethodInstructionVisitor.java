package proguard.optimize.inline;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.MemberVisitor;

public class MethodInstructionVisitor implements MemberVisitor, AttributeVisitor {
    private final InstructionVisitor instructionVisitor;
    private final int startOffset;
    private final int endOffset;

    public MethodInstructionVisitor(int startOffset, int endOffset, InstructionVisitor instructionVisitor) {
        this.instructionVisitor = instructionVisitor;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public MethodInstructionVisitor(InstructionVisitor instructionVisitor) {
        this(-1, -1, instructionVisitor);
    }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
        programMethod.attributesAccept(programClass, this);
    }

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        if (startOffset < 0) codeAttribute.instructionsAccept(clazz, method, instructionVisitor);
        else codeAttribute.instructionsAccept(clazz, method, startOffset, endOffset, instructionVisitor);
    }
}
