package proguard.optimize.inline;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.ConstantPoolEditor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

public class CallReplacer implements InstructionVisitor {
    private final CodeAttributeEditor codeAttributeEditor;
    private final Clazz methodOwnerClazz;
    private final Method oldMethod;
    private final Method newMethod;

    public CallReplacer(CodeAttributeEditor codeAttributeEditor, Clazz methodOwnerClazz, Method oldMethod, Method newMethod) {
        this.codeAttributeEditor = codeAttributeEditor;
        this.methodOwnerClazz = methodOwnerClazz;
        this.oldMethod = oldMethod;
        this.newMethod = newMethod;
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}

    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
        if (constantInstruction.opcode == Instruction.OP_INVOKESTATIC) {
            ConstantPoolEditor constantPoolEditor = new ConstantPoolEditor((ProgramClass) clazz);
            clazz.constantPoolEntryAccept(constantInstruction.constantIndex, new ConstantVisitor() {
                @Override
                public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant) {
                    if (anyMethodrefConstant.referencedMethod != null && anyMethodrefConstant.referencedMethod.equals(oldMethod)) {
                        int newMethodIndex = constantPoolEditor.addMethodrefConstant(methodOwnerClazz, newMethod);
                        codeAttributeEditor.reset(codeAttribute.u4codeLength);
                        codeAttributeEditor.replaceInstruction(offset, new ConstantInstruction(Instruction.OP_INVOKESTATIC, newMethodIndex));
                        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
                    }
                }
            });
        }
    }
}
