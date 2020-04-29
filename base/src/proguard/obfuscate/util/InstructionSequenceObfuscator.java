/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */
package proguard.obfuscate.util;

import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;

import java.util.Arrays;

public class InstructionSequenceObfuscator
implements   ClassVisitor,
             MemberVisitor
{

    private final PeepholeEditor peepholeOptimizer;

    public InstructionSequenceObfuscator(ReplacementSequences replacementSequences)
    {
        BranchTargetFinder  branchTargetFinder  = new BranchTargetFinder();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

        peepholeOptimizer = new PeepholeEditor(
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
    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.methodsAccept(this);
    }

    @Override
    public void visitLibraryClass(LibraryClass libraryClass) { }

    // Implementations for MemberVisitor.

    @Override
    public void visitAnyMember(Clazz clazz, Member member) { }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        programMethod.attributesAccept(programClass, peepholeOptimizer);
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
