package proguard.optimize.inline;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.VariableInstruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.MemberVisitor;

public class LocalUsageRemover implements MemberVisitor, InstructionVisitor, AttributeVisitor {
    private final CodeAttributeEditor codeAttributeEditor;
    private final int argumentIndex;

    public LocalUsageRemover(CodeAttributeEditor codeAttributeEditor, int argumentIndex) {
        this.codeAttributeEditor = codeAttributeEditor;
        this.argumentIndex = argumentIndex;
    }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
        programMethod.accept(programClass, new AllAttributeVisitor(this));
    }

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        codeAttributeEditor.reset(codeAttribute.u4codeLength);
        codeAttribute.instructionsAccept(clazz, method, this);
        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
    }

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

    @Override
    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction) {
        if (variableInstruction.variableIndex == argumentIndex) {
            if (variableInstruction.isStore()) {
                codeAttributeEditor.replaceInstruction(offset, new VariableInstruction(Instruction.OP_POP));
            } else {
                codeAttributeEditor.replaceInstruction(offset, new VariableInstruction(Instruction.OP_ACONST_NULL));
            }
        } else if (variableInstruction.variableIndex > argumentIndex){
            codeAttributeEditor.replaceInstruction(offset, new VariableInstruction(variableInstruction.canonicalOpcode(), variableInstruction.variableIndex - 1, variableInstruction.constant));
        }
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}
}
