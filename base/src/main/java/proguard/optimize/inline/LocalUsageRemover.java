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

/**
 * This class can be used to remove the usage of a local variable from a method. The class accomplishes that by shifting
 * usages of load and store.
 * <p>
 * So for example we want to remove the usage of local variable 2. We will then keep all the load and stores to variable
 * 0 and 1, but we will shift load and store instructions that are higher than 2 down so <code>aload_3</code> becomes
 * <code>aload_2</code> and so on. Instructions such as <code>aload_2</code> that might still be inside the
 * codeAttribute will be replaced with the replacementInstruction which by default is <code>aconst_null</code>. So it
 * still pushes something onto the stack just like before we removed the usage. In the case of <code>astore_2</code> we
 * would replace that with a <code>pop</code> instruction, so it still removes one element form the stack just like
 * before.
 * <p>
 * This class is used when inlining lambdas, we inline the lambda and afterward we don't need the local that originally
 * stored the lambda anymore. In that case we can use this class to remove the local from the method. In some cases
 * null checks are done on the lambda in this case there is still a usage and replacing it with <code>aconst_null</code>
 * would result in incorrect behaviour, in that case we will use a different <code>replacementInstruction</code>.
 */
public class LocalUsageRemover implements MemberVisitor, InstructionVisitor, AttributeVisitor {
    private final CodeAttributeEditor codeAttributeEditor;
    private final int argumentIndex;
    private final Instruction replacementInstruction;

    public LocalUsageRemover(CodeAttributeEditor codeAttributeEditor, int argumentIndex, Instruction replacementInstruction) {
        this.codeAttributeEditor = codeAttributeEditor;
        this.argumentIndex = argumentIndex;
        this.replacementInstruction = replacementInstruction;
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
                codeAttributeEditor.replaceInstruction(offset, replacementInstruction);
            }
        } else if (variableInstruction.variableIndex > argumentIndex){
            codeAttributeEditor.replaceInstruction(offset, new VariableInstruction(variableInstruction.canonicalOpcode(), variableInstruction.variableIndex - 1, variableInstruction.constant));
        }
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}
}
