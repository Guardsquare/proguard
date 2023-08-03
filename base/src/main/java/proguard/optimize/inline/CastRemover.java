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
import proguard.classfile.instruction.InstructionFactory;
import proguard.classfile.instruction.VariableInstruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.ClassPrinter;

import java.util.*;

public class CastRemover implements InstructionVisitor {
    private final CodeAttributeEditor codeAttributeEditor;
    private List<Integer> keepList;
    private int currentIndex;

    public CastRemover(CodeAttributeEditor codeAttributeEditor, List<Integer> keepList) {
        this.codeAttributeEditor = codeAttributeEditor;
        this.currentIndex = 0;
        this.keepList = keepList;
    }

    public CastRemover(CodeAttributeEditor codeAttributeEditor) {
        this(codeAttributeEditor, new ArrayList<>());
    }

    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
        Set<String> castingMethodNames = new HashSet<>(Arrays.asList("intValue", "booleanValue", "byteValue", "shortValue", "longValue", "floatValue", "doubleValue", "charValue"));
        if (constantInstruction.opcode == Instruction.OP_CHECKCAST) {
            System.out.println("Removing " + InstructionFactory.create(codeAttribute.code, offset).toString(offset));
            codeAttributeEditor.deleteInstruction(offset);
        } else if (constantInstruction.opcode == Instruction.OP_INVOKESTATIC) {
            if (getInvokedMethodName(clazz, constantInstruction).equals("valueOf")) {
                if (!keepList.contains(currentIndex)) {
                    System.out.print("Removing "); InstructionFactory.create(codeAttribute.code, offset).accept(clazz, method, codeAttribute, offset, new ClassPrinter());
                    codeAttributeEditor.deleteInstruction(offset);
                }

                currentIndex++;
            }
        } else if (constantInstruction.opcode == Instruction.OP_INVOKEVIRTUAL) {
            System.out.println("Removing " + InstructionFactory.create(codeAttribute.code, offset).toString(offset));
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
