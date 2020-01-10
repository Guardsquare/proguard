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
package proguard.classfile.editor;

import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.*;
import proguard.classfile.util.BranchTargetFinder;

/**
 * This {@link InstructionVisitor} replaces multiple instruction sequences at once.
 * <p/>
 * The replacement sequences are optional, defaulting to the empty sequence,
 * to delete the matched pattern sequences.
 *
 * @see InstructionSequenceReplacer
 * @author Eric Lafortune
 */
public class InstructionSequencesReplacer
extends      MultiInstructionVisitor
implements   InstructionVisitor
{
    private static final int PATTERN_INDEX     = 0;
    private static final int REPLACEMENT_INDEX = 1;

    private static final Instruction[] EMPTY_INSTRUCTIONS = new Instruction[0];


    /**
     * Creates a new InstructionSequencesReplacer.
     * @param constants               any constants referenced by the pattern
     *                                instructions and replacement instructions.
     * @param instructionSequences    the instruction sequences to be replaced,
     *                                with subsequently the sequence pair index,
     *                                the patten/replacement index (0 or 1),
     *                                and the instruction index in the sequence.
     * @param branchTargetFinder      a branch target finder that has been
     *                                initialized to indicate branch targets
     *                                in the visited code.
     * @param codeAttributeEditor     a code editor that can be used for
     *                                accumulating changes to the code.
     */
    public InstructionSequencesReplacer(Constant[]          constants,
                                        Instruction[][][]   instructionSequences,
                                        BranchTargetFinder  branchTargetFinder,
                                        CodeAttributeEditor codeAttributeEditor)
    {
        this(constants,
             instructionSequences,
             branchTargetFinder,
             codeAttributeEditor,
             null);
    }


    /**
     * Creates a new InstructionSequenceReplacer.
     * @param constants               any constants referenced by the pattern
     *                                instructions and replacement instructions.
     * @param instructionSequences    the instruction sequences to be replaced,
     *                                with subsequently the sequence pair index,
     *                                the patten/replacement index (0 or 1),
     *                                and the instruction index in the sequence.
     * @param branchTargetFinder      a branch target finder that has been
     *                                initialized to indicate branch targets
     *                                in the visited code.
     * @param codeAttributeEditor     a code editor that can be used for
     *                                accumulating changes to the code.
     * @param extraInstructionVisitor an optional extra visitor for all deleted
     *                                load instructions.
     */
    public InstructionSequencesReplacer(Constant[]          constants,
                                        Instruction[][][]   instructionSequences,
                                        BranchTargetFinder  branchTargetFinder,
                                        CodeAttributeEditor codeAttributeEditor,
                                        InstructionVisitor  extraInstructionVisitor)
    {
        super(createInstructionSequenceReplacers(constants,
                                                 instructionSequences,
                                                 branchTargetFinder,
                                                 codeAttributeEditor,
                                                 extraInstructionVisitor));
    }


    /**
     * Creates an array of InstructionSequenceReplacer instances.
     * @param constants               any constants referenced by the pattern
     *                                instructions and replacement instructions.
     * @param instructionSequences    the instruction sequences to be replaced,
     *                                with subsequently the sequence pair index,
     *                                the from/to index (0 or 1), and the
     *                                instruction index in the sequence.
     * @param branchTargetFinder      a branch target finder that has been
     *                                initialized to indicate branch targets
     *                                in the visited code.
     * @param codeAttributeEditor     a code editor that can be used for
     *                                accumulating changes to the code.
     * @param extraInstructionVisitor an optional extra visitor for all deleted
     *                                load instructions.
     */
    private static InstructionVisitor[] createInstructionSequenceReplacers(Constant[]          constants,
                                                                           Instruction[][][]   instructionSequences,
                                                                           BranchTargetFinder  branchTargetFinder,
                                                                           CodeAttributeEditor codeAttributeEditor,
                                                                           InstructionVisitor  extraInstructionVisitor)
    {
        InstructionVisitor[] instructionSequenceReplacers =
            new InstructionVisitor[instructionSequences.length];

        for (int index = 0; index < instructionSequenceReplacers.length; index++)
        {
            Instruction[][] instructionSequencePair =
                instructionSequences[index];

            Instruction[] patternInstructions =
                instructionSequencePair[PATTERN_INDEX];

            // The replacement sequence is optional.
            Instruction[] replacementInstructions =
                instructionSequencePair.length > REPLACEMENT_INDEX ?
                    instructionSequencePair[REPLACEMENT_INDEX] :
                    EMPTY_INSTRUCTIONS;

            instructionSequenceReplacers[index] =
                new InstructionSequenceReplacer(constants,
                                                patternInstructions,
                                                constants,
                                                replacementInstructions,
                                                branchTargetFinder,
                                                codeAttributeEditor,
                                                extraInstructionVisitor);
        }

        return instructionSequenceReplacers;
    }
}
