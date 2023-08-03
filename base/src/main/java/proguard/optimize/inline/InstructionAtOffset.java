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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        InstructionAtOffset that = (InstructionAtOffset) obj;
        return Objects.equals(this.instruction, that.instruction) &&
                this.offset == that.offset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(instruction, offset);
    }

    @Override
    public String toString() {
        return "InstructionAtOffset[" +
                "instruction=" + instruction + ", " +
                "offset=" + offset + ']';
    }

}
