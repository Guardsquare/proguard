package proguard.optimize.inline;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.NameAndTypeConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import java.util.*;

public class CastRemover implements InstructionVisitor {
    private final CodeAttributeEditor codeAttributeEditor;
    private final List<Integer> keepList;
    private int argIndex;

    // The names of all method taking a boxed type variable and returning the variable with the unboxed type
    private final Set<String> castingMethodNames = new HashSet<>(Arrays.asList("intValue", "booleanValue", "byteValue", "shortValue", "longValue", "floatValue", "doubleValue", "charValue"));


    public CastRemover(CodeAttributeEditor codeAttributeEditor, List<Integer> keepList) {
        this.codeAttributeEditor = codeAttributeEditor;
        this.argIndex = 0;
        this.keepList = keepList;
    }

    public CastRemover(CodeAttributeEditor codeAttributeEditor) {
        this(codeAttributeEditor, new ArrayList<>());
    }

    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
        /* Casting on a lambda invoke call looks like this :
         * iload
         * invokestatic valueOf
         * invokeinterface invoke   //lambda call
         * checkast
         * invokevirtual intValue
         * istore
         *
         * We remove valueOf, checkast and intValue
         */
        if (constantInstruction.opcode == Instruction.OP_CHECKCAST) {
            codeAttributeEditor.deleteInstruction(offset);
        } else if (constantInstruction.opcode == Instruction.OP_INVOKESTATIC) {
            if (getInvokedMethodName(clazz, constantInstruction).equals("valueOf")) {
                // Don't remove valueOf call when the lambda takes an object as argument
                if (!keepList.contains(argIndex)) {
                    codeAttributeEditor.deleteInstruction(offset);
                }
                argIndex++;
            }
        } else if (constantInstruction.opcode == Instruction.OP_INVOKEVIRTUAL) {
            if (castingMethodNames.contains(getInvokedMethodName(clazz, constantInstruction))) {
                codeAttributeEditor.deleteInstruction(offset);
            }
        }
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {
    }

    private String getInvokedMethodName(Clazz clazz, ConstantInstruction constantInstruction) {
        final String[] invokedMethodName = new String[1];
        clazz.constantPoolEntryAccept(constantInstruction.constantIndex, new ConstantVisitor() {
            @Override public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant) {
                clazz.constantPoolEntryAccept(anyMethodrefConstant.u2nameAndTypeIndex, new ConstantVisitor() {
                    @Override
                    public void visitNameAndTypeConstant(Clazz clazz, NameAndTypeConstant nameAndTypeConstant) {
                        invokedMethodName[0] = nameAndTypeConstant.getName(clazz);
                    }
                });
            }
        });
        return invokedMethodName[0];
    }
}
