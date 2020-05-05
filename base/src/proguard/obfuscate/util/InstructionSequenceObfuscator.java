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
package proguard.obfuscate.util;

import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.*;
import proguard.classfile.util.BranchTargetFinder;
import proguard.classfile.visitor.*;

import java.util.Arrays;

public class InstructionSequenceObfuscator
implements   ClassVisitor,
             MemberVisitor
{
    private final PeepholeEditor peepholeEditor;


    public InstructionSequenceObfuscator(ReplacementSequences replacementSequences)
    {
        BranchTargetFinder  branchTargetFinder  = new BranchTargetFinder();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

        peepholeEditor = new PeepholeEditor(
                                branchTargetFinder, codeAttributeEditor,
                                new MyInstructionSequenceReplacer(
                                    replacementSequences.getConstants(),
                                    replacementSequences.getSequences(),
                                    branchTargetFinder,
                                    codeAttributeEditor
                                ));
    }

    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) { }


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.methodsAccept(this);
    }


    // Implementations for MemberVisitor.

    @Override
    public void visitAnyMember(Clazz clazz, Member member) { }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        programMethod.attributesAccept(programClass, peepholeEditor);
    }


    // Helper classes.

    private static class MyInstructionSequenceReplacer extends MultiInstructionVisitor
    {

        MyInstructionSequenceReplacer(Constant[]          constants,
                                      Instruction[][][]   insSequences,
                                      BranchTargetFinder  branchTargetFinder,
                                      CodeAttributeEditor codeAttributeEditor)
        {
            super(createInstructionSequenceReplacers(constants, insSequences, branchTargetFinder, codeAttributeEditor));
        }

        private static InstructionVisitor[] createInstructionSequenceReplacers(Constant[]          constants,
                                                                               Instruction[][][]   insSequences,
                                                                               BranchTargetFinder  branchTargetFinder,
                                                                               CodeAttributeEditor codeAttributeEditor)
        {
            InstructionVisitor[] isReplacers = new InstructionSequenceReplacer[insSequences.length];

            Arrays.setAll(
                isReplacers,
                index -> new InstructionSequenceReplacer(constants,
                                                         insSequences[index][0],
                                                         constants,
                                                         insSequences[index][1],
                                                         branchTargetFinder,
                                                         codeAttributeEditor,
                                                         null)
            );

            return isReplacers;
        }
    }
}
