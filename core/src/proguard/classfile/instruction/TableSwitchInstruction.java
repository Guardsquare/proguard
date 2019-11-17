/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.instruction;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.visitor.InstructionVisitor;

/**
 * This Instruction represents a table switch instruction.
 *
 * @author Eric Lafortune
 */
public class TableSwitchInstruction extends SwitchInstruction
{
    public int lowCase;
    public int highCase;


    /**
     * Creates an uninitialized TableSwitchInstruction.
     */
    public TableSwitchInstruction() {}


    /**
     * Creates a new TableSwitchInstruction with the given arguments.
     * All offsets are relative to this instruction's offset.
     */
    public TableSwitchInstruction(byte  opcode,
                                  int   defaultOffset,
                                  int   lowCase,
                                  int   highCase,
                                  int[] jumpOffsets)
    {
        this.opcode        = opcode;
        this.defaultOffset = defaultOffset;
        this.lowCase       = lowCase;
        this.highCase      = highCase;
        this.jumpOffsets   = jumpOffsets;
    }


    /**
     * Copies the given instruction into this instruction.
     * @param tableSwitchInstruction the instruction to be copied.
     * @return this instruction.
     */
    public TableSwitchInstruction copy(TableSwitchInstruction tableSwitchInstruction)
    {
        this.opcode        = tableSwitchInstruction.opcode;
        this.defaultOffset = tableSwitchInstruction.defaultOffset;
        this.lowCase       = tableSwitchInstruction.lowCase;
        this.highCase      = tableSwitchInstruction.highCase;
        this.jumpOffsets   = tableSwitchInstruction.jumpOffsets;

        return this;
    }


    // Implementations for Instruction.

    public Instruction shrink()
    {
        // There aren't any ways to shrink this instruction.
        return this;
    }

    protected void readInfo(byte[] code, int offset)
    {
        // Skip up to three padding bytes.
        offset += -offset & 3;

        // Read the three 32-bit arguments.
        defaultOffset = readInt(code, offset); offset += 4;
        lowCase       = readInt(code, offset); offset += 4;
        highCase      = readInt(code, offset); offset += 4;

        // Read the jump offsets.
        jumpOffsets = new int[highCase - lowCase + 1];

        for (int index = 0; index < jumpOffsets.length; index++)
        {
            jumpOffsets[index] = readInt(code, offset); offset += 4;
        }
    }


    protected void writeInfo(byte[] code, int offset)
    {
        // Write up to three padding bytes.
        while ((offset & 3) != 0)
        {
            writeByte(code, offset++, 0);
        }

        // Write the three 32-bit arguments.
        writeInt(code, offset, defaultOffset); offset += 4;
        writeInt(code, offset, lowCase);       offset += 4;
        writeInt(code, offset, highCase);      offset += 4;

        // Write the jump offsets.
        int length = highCase - lowCase + 1;
        for (int index = 0; index < length; index++)
        {
            writeInt(code, offset, jumpOffsets[index]); offset += 4;
        }
    }


    public int length(int offset)
    {
        return 1 + (-(offset+1) & 3) + 12 + (highCase - lowCase + 1) * 4;
    }


    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, InstructionVisitor instructionVisitor)
    {
        instructionVisitor.visitTableSwitchInstruction(clazz, method, codeAttribute, offset, this);
    }
}
