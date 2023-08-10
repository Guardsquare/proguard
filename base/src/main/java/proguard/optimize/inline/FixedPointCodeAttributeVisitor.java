package proguard.optimize.inline;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.InstructionFactory;
import proguard.classfile.instruction.visitor.InstructionVisitor;

/**
 * This class allows the programmer to visit the instructions of a given code attribute but if the code is changed
 * restart the iteration of the instructions so that the program can operate on these new instructions.
 * <p>
 * You just use instructionsAccept to start iterating over the instructions, you provide an instructionVisitor and it
 * will visit all the instructions in the code attribute.
 * <p>
 * By using setCodeChanged you can tell this class that you changed the code, it will when fetching the next instruction
 * jump back to the first one in the updated code attribute.
 */
public class FixedPointCodeAttributeVisitor implements AttributeVisitor {
    private boolean changed;
    private final InstructionVisitor instructionVisitor;

    public FixedPointCodeAttributeVisitor(InstructionVisitor instructionVisitor) {
        this.instructionVisitor = instructionVisitor;
    }

    /**
     * This helper method does something similar to codeAttribute.instructionsAccept but has the ability to stop and
     * restart visiting  the instructions, this is useful for when you change the code during iteration.
     */
    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
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

    /**
     * Allows checking if the code was reported to be changed, this makes it possible to skip certain operations that
     * normally execute before restarting the iteration.
     */
    public boolean codeHasChanged() {
        return changed;
    }

    /**
     * This method notifies this class that the instruction sequence it is visiting has been changed and that it should
     * restart.
     */
    public void setCodeChanged(boolean changed) {
        this.changed = changed;
    }
}
