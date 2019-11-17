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

/**
 * This Instruction represents a switch instruction.
 *
 * @author Eric Lafortune
 */
public abstract class SwitchInstruction extends Instruction
{
    public int   defaultOffset;
    public int[] jumpOffsets;


    /**
     * Creates an uninitialized SwitchInstruction.
     */
    public SwitchInstruction() {}


    /**
     * Creates a new SwitchInstruction with the given arguments.
     * All offsets are relative to this instruction's offset.
     */
    public SwitchInstruction(byte  opcode,
                             int   defaultOffset,
                             int[] jumpOffsets)
    {
        this.opcode        = opcode;
        this.defaultOffset = defaultOffset;
        this.jumpOffsets   = jumpOffsets;
    }


    /**
     * Copies the given instruction into this instruction.
     * @param switchInstruction the instruction to be copied.
     * @return this instruction.
     */
    public SwitchInstruction copy(SwitchInstruction switchInstruction)
    {
        this.opcode        = switchInstruction.opcode;
        this.defaultOffset = switchInstruction.defaultOffset;
        this.jumpOffsets   = switchInstruction.jumpOffsets;

        return this;
    }


    // Implementations for Instruction.

    public String toString(int offset)
    {
        return "["+offset+"] "+toString()+" (target="+(offset+defaultOffset)+")";
    }


    // Implementations for Object.

    public String toString()
    {
        return getName()+" ("+jumpOffsets.length+" offsets, default="+defaultOffset+")";
    }
}
