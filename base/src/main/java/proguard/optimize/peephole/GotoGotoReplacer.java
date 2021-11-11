/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.optimize.peephole;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;

/**
 * This InstructionVisitor simplifies unconditional branches to other
 * unconditional branches.
 *
 * @author Eric Lafortune
 */
public class GotoGotoReplacer
implements   InstructionVisitor
{
    private final CodeAttributeEditor codeAttributeEditor;
    private final InstructionVisitor  extraInstructionVisitor;


    /**
     * Creates a new GotoGotoReplacer.
     * @param codeAttributeEditor     a code editor that can be used for
     *                                accumulating changes to the code.
     */
    public GotoGotoReplacer(CodeAttributeEditor codeAttributeEditor)
    {
        this(codeAttributeEditor, null);
    }


    /**
     * Creates a new GotoGotoReplacer.
     * @param codeAttributeEditor     a code editor that can be used for
     *                                accumulating changes to the code.
     * @param extraInstructionVisitor an optional extra visitor for all replaced
     *                                goto instructions.
     */
    public GotoGotoReplacer(CodeAttributeEditor codeAttributeEditor,
                            InstructionVisitor  extraInstructionVisitor)
    {
        this.codeAttributeEditor     = codeAttributeEditor;
        this.extraInstructionVisitor = extraInstructionVisitor;
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitBranchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, BranchInstruction branchInstruction)
    {
        // Check if the instruction is an unconditional goto instruction.
        byte opcode = branchInstruction.opcode;
        if (opcode == Instruction.OP_GOTO ||
            opcode == Instruction.OP_GOTO_W)
        {
            // Check if the goto instruction points to another simple goto
            // instruction.
            int branchOffset = branchInstruction.branchOffset;
            int targetOffset = offset + branchOffset;

            if (branchOffset != 0                                &&
                branchOffset != branchInstruction.length(offset) &&
                !codeAttributeEditor.isModified(offset)          &&
                !codeAttributeEditor.isModified(targetOffset))
            {
                Instruction targetInstruction =
                    InstructionFactory.create(codeAttribute.code, targetOffset);

                if (targetInstruction.opcode == Instruction.OP_GOTO)
                {
                    // Simplify the goto instruction.
                    int targetBranchOffset = ((BranchInstruction)targetInstruction).branchOffset;

                    Instruction newBranchInstruction =
                         new BranchInstruction(opcode,
                                               (branchOffset + targetBranchOffset));
                    codeAttributeEditor.replaceInstruction(offset,
                                                           newBranchInstruction);

                    // Visit the instruction, if required.
                    if (extraInstructionVisitor != null)
                    {
                        extraInstructionVisitor.visitBranchInstruction(clazz, method, codeAttribute, offset, branchInstruction);
                    }
                }
            }
        }
    }
}
