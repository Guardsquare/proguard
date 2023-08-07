package proguard.optimize.inline;

import proguard.classfile.instruction.Instruction;

import java.util.Objects;

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
