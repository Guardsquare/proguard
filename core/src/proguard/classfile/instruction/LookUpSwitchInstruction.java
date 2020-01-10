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
 * This {@link Instruction} represents a lookup switch instruction.
 *
 * @author Eric Lafortune
 */
public class LookUpSwitchInstruction extends SwitchInstruction
{
    public int[] cases;


    /**
     * Creates an uninitialized LookUpSwitchInstruction.
     */
    public LookUpSwitchInstruction() {}


    /**
     * Creates a new LookUpSwitchInstruction with the given arguments.
     * All offsets are relative to this instruction's offset.
     */
    public LookUpSwitchInstruction(byte  opcode,
                                   int   defaultOffset,
                                   int[] cases,
                                   int[] jumpOffsets)
    {
        this.opcode        = opcode;
        this.defaultOffset = defaultOffset;
        this.cases         = cases;
        this.jumpOffsets   = jumpOffsets;
    }


    /**
     * Copies the given instruction into this instruction.
     * @param lookUpSwitchInstruction the instruction to be copied.
     * @return this instruction.
     */
    public LookUpSwitchInstruction copy(LookUpSwitchInstruction lookUpSwitchInstruction)
    {
        this.opcode        = lookUpSwitchInstruction.opcode;
        this.defaultOffset = lookUpSwitchInstruction.defaultOffset;
        this.cases         = lookUpSwitchInstruction.cases;
        this.jumpOffsets   = lookUpSwitchInstruction.jumpOffsets;

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

        // Read the two 32-bit arguments.
        defaultOffset       = readInt(code, offset); offset += 4;
        int jumpOffsetCount = readInt(code, offset); offset += 4;

        // Read the matches-offset pairs.
        cases       = new int[jumpOffsetCount];
        jumpOffsets = new int[jumpOffsetCount];

        for (int index = 0; index < jumpOffsetCount; index++)
        {
            cases[index]       = readInt(code, offset); offset += 4;
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

        // Write the two 32-bit arguments.
        writeInt(code, offset, defaultOffset); offset += 4;
        writeInt(code, offset, cases.length);  offset += 4;

        // Write the matches-offset pairs.
        for (int index = 0; index < cases.length; index++)
        {
            writeInt(code, offset, cases[index]);       offset += 4;
            writeInt(code, offset, jumpOffsets[index]); offset += 4;
        }
    }


    public int length(int offset)
    {
        return 1 + (-(offset+1) & 3) + 8 + cases.length * 8;
    }


    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, InstructionVisitor instructionVisitor)
    {
        instructionVisitor.visitLookUpSwitchInstruction(clazz, method, codeAttribute, offset, this);
    }
}
