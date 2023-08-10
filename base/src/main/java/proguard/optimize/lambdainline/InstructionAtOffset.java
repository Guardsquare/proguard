package proguard.optimize.lambdainline;

import proguard.classfile.instruction.Instruction;

/**
 * A class that holds an instruction and offset at which it was found, this is useful in some cases where we need to
 * return both of them form somewhere.
 */
public final class InstructionAtOffset {
    private final Instruction instruction;
    private final int offset;

    InstructionAtOffset(Instruction instruction, int offset) {
        this.instruction = instruction;
        this.offset = offset;
    }

    public Instruction instruction() {
        return instruction;
    }

    public int offset() {
        return offset;
    }
}
