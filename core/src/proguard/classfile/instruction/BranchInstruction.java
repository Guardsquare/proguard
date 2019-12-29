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
 * This interface describes an instruction that branches to a given offset in
 * the code.
 *
 * @author Eric Lafortune
 */
public class BranchInstruction extends Instruction
{
    public int branchOffset;


    /**
     * Creates an uninitialized BranchInstruction.
     */
    public BranchInstruction() {}


    /**
     * Creates a BranchInstruction with the given branch offset.
     * The branch offset is relative to this instruction's offset.
     */
    public BranchInstruction(byte opcode, int branchOffset)
    {
        this.opcode       = opcode;
        this.branchOffset = branchOffset;
    }


    /**
     * Copies the given instruction into this instruction.
     * @param branchInstruction the instruction to be copied.
     * @return this instruction.
     */
    public BranchInstruction copy(BranchInstruction branchInstruction)
    {
        this.opcode       = branchInstruction.opcode;
        this.branchOffset = branchInstruction.branchOffset;

        return this;
    }


    // Implementations for Instruction.

    public byte canonicalOpcode()
    {
        // Remove the _w extension, if any.
        switch (opcode)
        {
            case Instruction.OP_GOTO_W: return Instruction.OP_GOTO;

            case Instruction.OP_JSR_W: return Instruction.OP_JSR;

            default: return opcode;
        }
    }

    public Instruction shrink()
    {
        // Do we need an ordinary branch or a wide branch?
        if (requiredBranchOffsetSize() == 2)
        {
            // Can we replace the wide branch by an ordinary branch?
            if      (opcode == Instruction.OP_GOTO_W)
            {
                opcode = Instruction.OP_GOTO;
            }
            else if (opcode == Instruction.OP_JSR_W)
            {
                opcode = Instruction.OP_JSR;
            }
        }
        else
        {
            // Can we provide a wide branch?
            if      (opcode == Instruction.OP_GOTO ||
                     opcode == Instruction.OP_GOTO_W)
            {
                opcode = Instruction.OP_GOTO_W;
            }
            else if (opcode == Instruction.OP_JSR)
            {
                opcode = Instruction.OP_JSR_W;
            }
            else
            {
                throw new IllegalArgumentException("Branch instruction can't be widened ("+this.toString()+")");
            }
        }

        return this;
    }

    protected void readInfo(byte[] code, int offset)
    {
        branchOffset = readSignedValue(code, offset, branchOffsetSize());
    }


    protected void writeInfo(byte[] code, int offset)
    {
        if (requiredBranchOffsetSize() > branchOffsetSize())
        {
            throw new IllegalArgumentException("Instruction has invalid branch offset size ("+this.toString(offset)+")");
        }

        writeSignedValue(code, offset, branchOffset, branchOffsetSize());
    }


    public int length(int offset)
    {
        return 1 + branchOffsetSize();
    }


    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, InstructionVisitor instructionVisitor)
    {
        instructionVisitor.visitBranchInstruction(clazz, method, codeAttribute, offset, this);
    }


    public String toString(int offset)
    {
        return "["+offset+"] "+toString()+" (target="+(offset+branchOffset)+")";
    }


    // Implementations for Object.

    public String toString()
    {
        return getName()+" "+(branchOffset >= 0 ? "+" : "")+branchOffset;
    }


    // Small utility methods.

    /**
     * Returns the branch offset size for this instruction.
     */
    private int branchOffsetSize()
    {
        return opcode == Instruction.OP_GOTO_W ||
               opcode == Instruction.OP_JSR_W  ? 4 :
                                                          2;
    }


    /**
     * Computes the required branch offset size for this instruction's branch
     * offset.
     */
    private int requiredBranchOffsetSize()
    {
        return (short)branchOffset == branchOffset ? 2 : 4;
    }
}
