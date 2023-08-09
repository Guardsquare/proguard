package proguard.optimize.inline;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.InstructionFactory;
import proguard.classfile.instruction.visitor.InstructionVisitor;

public class IterativeInstructionVisitor {
    private boolean changed;

    /**
     * This helper method does something similar to codeAttribute.instructionsAccept but has the ability to stop and
     * restart visiting  the instructions, this is useful for when you change the code during iteration.
     */
    public void instructionsAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, InstructionVisitor instructionVisitor) {
        int offset = 0;
        while (offset < codeAttribute.u4codeLength) {
            Instruction instruction = InstructionFactory.create(codeAttribute.code, offset);
            int instructionLength = instruction.length(offset);
            changed = false;
            instruction.accept(clazz, method, codeAttribute, offset, instructionVisitor);
            if (changed) {
                offset = 0;
                continue;
            }
            offset += instructionLength;
        }
    }

    public boolean codeHasChanged() {
        return changed;
    }

    public void setCodeChanged(boolean changed) {
        this.changed = changed;
    }
}
