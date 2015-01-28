/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2015 Eric Lafortune @ GuardSquare
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
package proguard.optimize.evaluation;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.RefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.evaluation.TracedStack;
import proguard.evaluation.value.*;
import proguard.optimize.info.*;

import java.util.Arrays;

/**
 * This AttributeVisitor simplifies the code attributes that it visits, based
 * on partial evaluation.
 *
 * @author Eric Lafortune
 */
public class EvaluationShrinker
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    //*
    private static final boolean DEBUG_RESULTS  = false;
    private static final boolean DEBUG          = false;
    /*/
    private static boolean DEBUG          = System.getProperty("es") != null;
    private static boolean DEBUG_RESULTS  = DEBUG;
    //*/

    private static final int UNSUPPORTED      = -1;
    private static final int NOP              = InstructionConstants.OP_NOP     & 0xff;
    private static final int POP              = InstructionConstants.OP_POP     & 0xff;
    private static final int POP2             = InstructionConstants.OP_POP2    & 0xff;
    private static final int DUP              = InstructionConstants.OP_DUP     & 0xff;
    private static final int DUP_X1           = InstructionConstants.OP_DUP_X1  & 0xff;
    private static final int DUP_X2           = InstructionConstants.OP_DUP_X2  & 0xff;
    private static final int DUP2             = InstructionConstants.OP_DUP2    & 0xff;
    private static final int DUP2_X1          = InstructionConstants.OP_DUP2_X1 & 0xff;
    private static final int DUP2_X2          = InstructionConstants.OP_DUP2_X2 & 0xff;
    private static final int SWAP             = InstructionConstants.OP_SWAP    & 0xff;
    private static final int MOV_X2           = DUP_X2  | (POP    << 8);
    private static final int MOV2_X1          = DUP2_X1 | (POP2   << 8);
    private static final int MOV2_X2          = DUP2_X2 | (POP2   << 8);
    private static final int POP_X1           = SWAP    | (POP    << 8);
    private static final int POP_X2           = DUP2_X1 | (POP2   << 8) | (POP    << 16);
    private static final int POP_X3           = UNSUPPORTED;
    private static final int POP2_X1          = DUP_X2  | (POP    << 8) | (POP2   << 16);
    private static final int POP2_X2          = DUP2_X2 | (POP2   << 8) | (POP2   << 16);
    private static final int POP3             = POP2    | (POP    << 8);
    private static final int POP4             = POP2    | (POP2   << 8);
    private static final int POP_DUP          = POP     | (DUP    << 8);
    private static final int POP_SWAP_POP     = POP     | (SWAP   << 8) | (POP    << 16);
    private static final int POP2_SWAP_POP    = POP2    | (SWAP   << 8) | (POP    << 16);
    private static final int SWAP_DUP_X1      = SWAP    | (DUP_X1 << 8);
    private static final int SWAP_DUP_X1_SWAP = SWAP    | (DUP_X1 << 8) | (SWAP   << 16);
    private static final int SWAP_POP_DUP     = SWAP    | (POP    << 8) | (DUP    << 16);
    private static final int SWAP_POP_DUP_X1  = SWAP    | (POP    << 8) | (DUP_X1 << 16);
    private static final int DUP_X2_POP2      = DUP_X2  | (POP2   << 8);
    private static final int DUP2_X1_POP3     = DUP2_X1 | (POP2   << 8) | (POP    << 16);
    private static final int DUP2_X2_POP3     = DUP2_X2 | (POP2   << 8) | (POP    << 16);
    private static final int DUP2_X2_SWAP_POP = DUP2_X2 | (SWAP   << 8) | (POP    << 16);


    private final InstructionVisitor extraDeletedInstructionVisitor;
    private final InstructionVisitor extraAddedInstructionVisitor;

    private final PartialEvaluator               partialEvaluator;
    private final PartialEvaluator               simplePartialEvaluator       = new PartialEvaluator();
    private final SideEffectInstructionChecker   sideEffectInstructionChecker = new SideEffectInstructionChecker(true, true);
    private final MyUnusedParameterSimplifier    unusedParameterSimplifier    = new MyUnusedParameterSimplifier();
    private final MyProducerMarker               producerMarker               = new MyProducerMarker();
    private final MyVariableInitializationMarker variableInitializationMarker = new MyVariableInitializationMarker();
    private final MyStackConsistencyFixer        stackConsistencyFixer        = new MyStackConsistencyFixer();
    private final CodeAttributeEditor            codeAttributeEditor          = new CodeAttributeEditor(false, false);

    private boolean[][] stacksNecessaryAfter   = new boolean[ClassConstants.TYPICAL_CODE_LENGTH][ClassConstants.TYPICAL_STACK_SIZE];
    private boolean[][] stacksSimplifiedBefore = new boolean[ClassConstants.TYPICAL_CODE_LENGTH][ClassConstants.TYPICAL_STACK_SIZE];
    private boolean[]   instructionsNecessary  = new boolean[ClassConstants.TYPICAL_CODE_LENGTH];

    private int maxMarkedOffset;


    /**
     * Creates a new EvaluationShrinker.
     */
    public EvaluationShrinker()
    {
        this(new PartialEvaluator(), null, null);
    }


    /**
     * Creates a new EvaluationShrinker.
     * @param partialEvaluator               the partial evaluator that will
     *                                       execute the code and provide
     *                                       information about the results.
     * @param extraDeletedInstructionVisitor an optional extra visitor for all
     *                                       deleted instructions.
     * @param extraAddedInstructionVisitor   an optional extra visitor for all
     *                                       added instructions.
     */
    public EvaluationShrinker(PartialEvaluator   partialEvaluator,
                              InstructionVisitor extraDeletedInstructionVisitor,
                              InstructionVisitor extraAddedInstructionVisitor)
    {
        this.partialEvaluator               = partialEvaluator;
        this.extraDeletedInstructionVisitor = extraDeletedInstructionVisitor;
        this.extraAddedInstructionVisitor   = extraAddedInstructionVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
//        DEBUG = DEBUG_RESULTS =
//            clazz.getName().equals("abc/Def") &&
//            method.getName(clazz).equals("abc");

        // TODO: Remove this when the evaluation shrinker has stabilized.
        // Catch any unexpected exceptions from the actual visiting method.
        try
        {
            // Process the code.
            visitCodeAttribute0(clazz, method, codeAttribute);
        }
        catch (RuntimeException ex)
        {
            System.err.println("Unexpected error while shrinking instructions after partial evaluation:");
            System.err.println("  Class       = ["+clazz.getName()+"]");
            System.err.println("  Method      = ["+method.getName(clazz)+method.getDescriptor(clazz)+"]");
            System.err.println("  Exception   = ["+ex.getClass().getName()+"] ("+ex.getMessage()+")");
            System.err.println("Not optimizing this method");

            if (DEBUG)
            {
                method.accept(clazz, new ClassPrinter());

                throw ex;
            }
        }
    }


    public void visitCodeAttribute0(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        if (DEBUG_RESULTS)
        {
            System.out.println();
            System.out.println("EvaluationShrinker ["+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz)+"]");
        }

        // Initialize the necessary array.
        initializeNecessary(codeAttribute);

        // Evaluate the method.
        partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);

        // Evaluate the method the way the JVM verifier would do it.
        simplePartialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);

        int codeLength = codeAttribute.u4codeLength;

        // Reset the code changes.
        codeAttributeEditor.reset(codeLength);

        // Mark any unused method parameters on the stack.
        if (DEBUG) System.out.println("Invocation simplification:");

        for (int offset = 0; offset < codeLength; offset++)
        {
            if (partialEvaluator.isTraced(offset))
            {
                Instruction instruction = InstructionFactory.create(codeAttribute.code,
                                                                    offset);

                instruction.accept(clazz, method, codeAttribute, offset, unusedParameterSimplifier);
            }
        }

        // Mark all essential instructions that have been encountered as used.
        if (DEBUG) System.out.println("Usage initialization: ");

        maxMarkedOffset = -1;

        // The invocation of the "super" or "this" <init> method inside a
        // constructor is always necessary.
        int superInitializationOffset = partialEvaluator.superInitializationOffset();
        if (superInitializationOffset != PartialEvaluator.NONE)
        {
            if (DEBUG) System.out.print("(super.<init>)");

            markInstruction(superInitializationOffset);
        }

        // Also mark infinite loops and instructions that cause side effects.
        for (int offset = 0; offset < codeLength; offset++)
        {
            if (partialEvaluator.isTraced(offset))
            {
                Instruction instruction = InstructionFactory.create(codeAttribute.code,
                                                                    offset);

                // Mark that the instruction is necessary if it is an infinite loop.
                if (instruction.opcode == InstructionConstants.OP_GOTO &&
                    ((BranchInstruction)instruction).branchOffset == 0)
                {
                    if (DEBUG) System.out.print("(infinite loop)");
                    markInstruction(offset);
                }

                // Mark that the instruction is necessary if it has side effects.
                else if (sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                     method,
                                                                     codeAttribute,
                                                                     offset,
                                                                     instruction))
                {
                    markInstruction(offset);
                }
            }
        }
        if (DEBUG) System.out.println();


        // Globally mark instructions and their produced variables and stack
        // entries on which necessary instructions depend.
        // Instead of doing this recursively, we loop across all instructions,
        // starting at the highest previously unmarked instruction that has
        // been been marked.
        if (DEBUG) System.out.println("Usage marking:");

        while (maxMarkedOffset >= 0)
        {
            int offset = maxMarkedOffset;

            maxMarkedOffset = offset - 1;

            if (partialEvaluator.isTraced(offset))
            {
                if (isInstructionNecessary(offset))
                {
                    Instruction instruction = InstructionFactory.create(codeAttribute.code,
                                                                        offset);

                    instruction.accept(clazz, method, codeAttribute, offset, producerMarker);
                }

                // Check if this instruction is a branch origin from a branch
                // that straddles some marked code.
                markStraddlingBranches(offset,
                                       partialEvaluator.branchTargets(offset),
                                       true);

                // Check if this instruction is a branch target from a branch
                // that straddles some marked code.
                markStraddlingBranches(offset,
                                       partialEvaluator.branchOrigins(offset),
                                       false);
            }

            if (DEBUG)
            {
                if (maxMarkedOffset > offset)
                {
                    System.out.println(" -> "+maxMarkedOffset);
                }
            }
        }
        if (DEBUG) System.out.println();


        // Mark variable initializations, even if they aren't strictly necessary.
        // The virtual machine's verification step is not smart enough to see
        // this, and may complain otherwise.
        if (DEBUG) System.out.println("Initialization marking: ");

        for (int offset = 0; offset < codeLength; offset++)
        {
            if (isInstructionNecessary(offset))
            {
                // Mark initializations of the required instruction.
                Instruction instruction = InstructionFactory.create(codeAttribute.code,
                                                                    offset);

                instruction.accept(clazz, method, codeAttribute, offset, variableInitializationMarker);
            }
        }
        if (DEBUG) System.out.println();


        // Locally fix instructions, in order to keep the stack consistent.
        if (DEBUG) System.out.println("Stack consistency fixing:");

        maxMarkedOffset = codeLength - 1;

        while (maxMarkedOffset >= 0)
        {
            int offset = maxMarkedOffset;

            maxMarkedOffset = offset - 1;

            if (partialEvaluator.isTraced(offset))
            {
                Instruction instruction = InstructionFactory.create(codeAttribute.code,
                                                                    offset);

                instruction.accept(clazz, method, codeAttribute, offset, stackConsistencyFixer);

                // Check if this instruction is a branch origin from a branch
                // that straddles some marked code.
                markStraddlingBranches(offset,
                                       partialEvaluator.branchTargets(offset),
                                       true);

                // Check if this instruction is a branch target from a branch
                // that straddles some marked code.
                markStraddlingBranches(offset,
                                       partialEvaluator.branchOrigins(offset),
                                       false);
            }
        }
        if (DEBUG) System.out.println();


        // Replace traced but unmarked backward branches by infinite loops.
        // The virtual machine's verification step is not smart enough to see
        // the code isn't reachable, and may complain otherwise.
        // Any clearly unreachable code will still be removed elsewhere.
        if (DEBUG) System.out.println("Infinite loop fixing:");

        for (int offset = 0; offset < codeLength; offset++)
        {
            // Is it a traced but unmarked backward branch, without an unmarked
            // straddling forward branch? Note that this is still a heuristic.
            if (partialEvaluator.isTraced(offset) &&
                !isInstructionNecessary(offset)   &&
                isAllSmallerThanOrEqual(partialEvaluator.branchTargets(offset),
                                        offset)   &&
                !isAnyUnnecessaryInstructionBranchingOver(lastNecessaryInstructionOffset(offset),
                                                          offset))
            {
                replaceByInfiniteLoop(clazz, offset);
            }
        }
        if (DEBUG) System.out.println();


        // Insert infinite loops after jumps to subroutines that don't return.
        // The virtual machine's verification step is not smart enough to see
        // the code isn't reachable, and may complain otherwise.
        if (DEBUG) System.out.println("Non-returning subroutine fixing:");

        for (int offset = 0; offset < codeLength; offset++)
        {
            // Is it a traced but unmarked backward branch, without an unmarked
            // straddling forward branch? Note that this is still a heuristic.
            if (isInstructionNecessary(offset) &&
                partialEvaluator.isSubroutineInvocation(offset))
            {
                Instruction instruction = InstructionFactory.create(codeAttribute.code,
                                                                    offset);

                int nextOffset = offset + instruction.length(offset);
                if (!isInstructionNecessary(nextOffset))
                {
                    replaceByInfiniteLoop(clazz, nextOffset);
                }
            }
        }
        if (DEBUG) System.out.println();


        // Delete all instructions that are not used.
        int offset = 0;
        do
        {
            Instruction instruction = InstructionFactory.create(codeAttribute.code,
                                                                offset);
            if (!isInstructionNecessary(offset))
            {
                codeAttributeEditor.clearModifications(offset);
                codeAttributeEditor.deleteInstruction(offset);

                // Visit the instruction, if required.
                if (extraDeletedInstructionVisitor != null)
                {
                    instruction.accept(clazz, method, codeAttribute, offset, extraDeletedInstructionVisitor);
                }
            }

            offset += instruction.length(offset);
        }
        while (offset < codeLength);


        if (DEBUG_RESULTS)
        {
            System.out.println("Simplification results:");

            offset = 0;
            do
            {
                Instruction instruction = InstructionFactory.create(codeAttribute.code,
                                                                    offset);
                System.out.println((isInstructionNecessary(offset) ? " + " : " - ")+instruction.toString(offset));

                if (partialEvaluator.isTraced(offset))
                {
                    int initializationOffset = partialEvaluator.initializationOffset(offset);
                    if (initializationOffset != PartialEvaluator.NONE)
                    {
                        System.out.println("     is to be initialized at ["+initializationOffset+"]");
                    }

                    InstructionOffsetValue branchTargets = partialEvaluator.branchTargets(offset);
                    if (branchTargets != null)
                    {
                        System.out.println("     has overall been branching to "+branchTargets);
                    }

                    boolean deleted = codeAttributeEditor.deleted[offset];
                    if (isInstructionNecessary(offset) && deleted)
                    {
                        System.out.println("     is deleted");
                    }

                    Instruction preInsertion = codeAttributeEditor.preInsertions[offset];
                    if (preInsertion != null)
                    {
                        System.out.println("     is preceded by: "+preInsertion);
                    }

                    Instruction replacement = codeAttributeEditor.replacements[offset];
                    if (replacement != null)
                    {
                        System.out.println("     is replaced by: "+replacement);
                    }

                    Instruction postInsertion = codeAttributeEditor.postInsertions[offset];
                    if (postInsertion != null)
                    {
                        System.out.println("     is followed by: "+postInsertion);
                    }
                }

                offset += instruction.length(offset);
            }
            while (offset < codeLength);
        }

        // Apply all accumulated changes to the code.
        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
    }


    /**
     * This MemberVisitor marks stack entries that aren't necessary because
     * parameters aren't used in the methods that are visited.
     */
    private class MyUnusedParameterSimplifier
    extends       SimplifiedVisitor
    implements    InstructionVisitor,
                  ConstantVisitor,
                  MemberVisitor
    {
        private int                 invocationOffset;
        private ConstantInstruction invocationInstruction;


        // Implementations for InstructionVisitor.

        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


        public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
        {
            switch (constantInstruction.opcode)
            {
                case InstructionConstants.OP_INVOKEVIRTUAL:
                case InstructionConstants.OP_INVOKESPECIAL:
                case InstructionConstants.OP_INVOKESTATIC:
                case InstructionConstants.OP_INVOKEINTERFACE:
                    this.invocationOffset      = offset;
                    this.invocationInstruction = constantInstruction;
                    clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                    break;
            }
        }


        // Implementations for ConstantVisitor.

        public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant)
        {
            refConstant.referencedMemberAccept(this);
        }


        // Implementations for MemberVisitor.

        public void visitAnyMember(Clazz clazz, Member member) {}


        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
        {
            // Get the total size of the parameters.
            int parameterSize = ParameterUsageMarker.getParameterSize(programMethod);

            // Make the method invocation static, if possible.
            if ((programMethod.getAccessFlags() & ClassConstants.ACC_STATIC) == 0 &&
                !ParameterUsageMarker.isParameterUsed(programMethod, 0))
            {
                replaceByStaticInvocation(programClass,
                                          invocationOffset,
                                          invocationInstruction);
            }

            // Remove unused parameters.
            for (int index = 0; index < parameterSize; index++)
            {
                if (!ParameterUsageMarker.isParameterUsed(programMethod, index))
                {
                    TracedStack stack =
                        partialEvaluator.getStackBefore(invocationOffset);

                    int stackIndex = stack.size() - parameterSize + index;

                    if (DEBUG)
                    {
                        System.out.println("  ["+invocationOffset+"] Ignoring parameter #"+index+" of "+programClass.getName()+"."+programMethod.getName(programClass)+programMethod.getDescriptor(programClass)+"] (stack entry #"+stackIndex+" ["+stack.getBottom(stackIndex)+"])");
                        System.out.println("    Full stack: "+stack);
                    }

                    markStackSimplificationBefore(invocationOffset, stackIndex);
                }
            }
        }
    }


    /**
     * This InstructionVisitor marks the producing instructions and produced
     * variables and stack entries of the instructions that it visits.
     * Simplified method arguments are ignored.
     */
    private class MyProducerMarker
    extends       SimplifiedVisitor
    implements    InstructionVisitor
    {
        // Implementations for InstructionVisitor.

        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
        {
            markStackProducers(clazz, offset, instruction);
        }


        public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
        {
            switch (simpleInstruction.opcode)
            {
                case InstructionConstants.OP_DUP:
                    conditionallyMarkStackEntryProducers(offset, 0, 0);
                    conditionallyMarkStackEntryProducers(offset, 1, 0);
                    break;
                case InstructionConstants.OP_DUP_X1:
                    conditionallyMarkStackEntryProducers(offset, 0, 0);
                    conditionallyMarkStackEntryProducers(offset, 1, 1);
                    conditionallyMarkStackEntryProducers(offset, 2, 0);
                    break;
                case InstructionConstants.OP_DUP_X2:
                    conditionallyMarkStackEntryProducers(offset, 0, 0);
                    conditionallyMarkStackEntryProducers(offset, 1, 1);
                    conditionallyMarkStackEntryProducers(offset, 2, 2);
                    conditionallyMarkStackEntryProducers(offset, 3, 0);
                    break;
                case InstructionConstants.OP_DUP2:
                    conditionallyMarkStackEntryProducers(offset, 0, 0);
                    conditionallyMarkStackEntryProducers(offset, 1, 1);
                    conditionallyMarkStackEntryProducers(offset, 2, 0);
                    conditionallyMarkStackEntryProducers(offset, 3, 1);
                    break;
                case InstructionConstants.OP_DUP2_X1:
                    conditionallyMarkStackEntryProducers(offset, 0, 0);
                    conditionallyMarkStackEntryProducers(offset, 1, 1);
                    conditionallyMarkStackEntryProducers(offset, 2, 2);
                    conditionallyMarkStackEntryProducers(offset, 3, 0);
                    conditionallyMarkStackEntryProducers(offset, 4, 1);
                    break;
                case InstructionConstants.OP_DUP2_X2:
                    conditionallyMarkStackEntryProducers(offset, 0, 0);
                    conditionallyMarkStackEntryProducers(offset, 1, 1);
                    conditionallyMarkStackEntryProducers(offset, 2, 2);
                    conditionallyMarkStackEntryProducers(offset, 3, 3);
                    conditionallyMarkStackEntryProducers(offset, 4, 0);
                    conditionallyMarkStackEntryProducers(offset, 5, 1);
                    break;
                case InstructionConstants.OP_SWAP:
                    conditionallyMarkStackEntryProducers(offset, 0, 1);
                    conditionallyMarkStackEntryProducers(offset, 1, 0);
                    break;
                default:
                    markStackProducers(clazz, offset, simpleInstruction);
                    break;
            }
        }


        public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction)
        {
            // Is the variable being loaded or incremented?
            if (variableInstruction.isLoad())
            {
                markVariableProducers(offset, variableInstruction.variableIndex);
            }
            else
            {
                markStackProducers(clazz, offset, variableInstruction);
            }
        }


        public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
        {
            // Mark the initializer invocation, if this is a 'new' instruction.
            if (constantInstruction.opcode == InstructionConstants.OP_NEW)
            {
                markInitialization(offset);
            }

            markStackProducers(clazz, offset, constantInstruction);
        }


        public void visitBranchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, BranchInstruction branchInstruction)
        {
            // Explicitly mark the produced stack entry of a 'jsr' instruction,
            // because the consuming 'astore' instruction of the subroutine is
            // cleared every time it is traced.
            if (branchInstruction.opcode == InstructionConstants.OP_JSR ||
                branchInstruction.opcode == InstructionConstants.OP_JSR_W)
            {
                markStackEntryAfter(offset, 0);
            }
            else
            {
                markStackProducers(clazz, offset, branchInstruction);
            }
        }
    }


    /**
     * This InstructionVisitor marks variable initializations that are
     * necessary to appease the JVM.
     */
    private class MyVariableInitializationMarker
    extends       SimplifiedVisitor
    implements    InstructionVisitor
    {
        // Implementations for InstructionVisitor.

        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


        public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction)
        {
            // Is the variable being loaded or incremented?
            if (variableInstruction.isLoad())
            {
                // Mark any variable initializations for this variable load that
                // are required according to the JVM.
                markVariableInitializersBefore(offset, variableInstruction.variableIndex);
            }
        }
    }


    /**
     * This InstructionVisitor fixes instructions locally, popping any unused
     * produced stack entries after marked instructions, and popping produced
     * stack entries and pushing missing stack entries instead of unmarked
     * instructions.
     */
    private class MyStackConsistencyFixer
    extends       SimplifiedVisitor
    implements    InstructionVisitor
    {
        // Implementations for InstructionVisitor.

        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
        {
            // Has the instruction been marked?
            if (isInstructionNecessary(offset))
            {
                // Check all stack entries that are popped.
                // Unusual case: an exception handler with an exception that is
                // no longer consumed directly by a method.
                // Typical case: a freshly marked variable initialization that
                // requires some value on the stack.
                int popCount = instruction.stackPopCount(clazz);
                if (popCount > 0)
                {
                    TracedStack tracedStack =
                        partialEvaluator.getStackBefore(offset);

                    int stackSize = tracedStack.size();

                    int requiredPopCount  = 0;
                    int requiredPushCount = 0;
                    for (int stackIndex = stackSize - popCount; stackIndex < stackSize; stackIndex++)
                    {
                        boolean stackSimplifiedBefore =
                            isStackSimplifiedBefore(offset, stackIndex);
                        boolean stackEntryPresentBefore =
                            isStackEntryPresentBefore(offset, stackIndex);

                        if (stackSimplifiedBefore)
                        {
                            // Is this stack entry pushed by any producer
                            // (maybe an exception in an exception handler)?
                            if (isStackEntryPresentBefore(offset, stackIndex))
                            {
                                // Mark all produced stack entries.
                                markStackEntryProducers(offset, stackIndex);

                                // Remember to pop it.
                                requiredPopCount++;
                            }
                        }
                        else
                        {
                            // Is this stack entry pushed by any producer
                            // (because it is required by other consumers)?
                            if (stackEntryPresentBefore)
                            {
                                // Mark all produced stack entries.
                                markStackEntryProducers(offset, stackIndex);
                            }
                            else
                            {
                                // Remember to push it.
                                requiredPushCount++;
                            }
                        }
                    }

                    // Pop some unnecessary stack entries.
                    if (requiredPopCount > 0)
                    {
                        if (DEBUG) System.out.println("  Inserting before marked consumer "+instruction.toString(offset));

                        insertPopInstructions(offset, false, true, popCount);
                    }

                    // Push some necessary stack entries.
                    if (requiredPushCount > 0)
                    {
                        if (DEBUG) System.out.println("  Inserting before marked consumer "+instruction.toString(offset));

                        if (requiredPushCount > (instruction.isCategory2() ? 2 : 1))
                        {
                            throw new IllegalArgumentException("Unsupported stack size increment ["+requiredPushCount+"] at ["+offset+"]");
                        }

                        insertPushInstructions(offset, false, true, tracedStack.getTop(0).computationalType());
                    }
                }

                // Check all other stack entries, if this is a return
                // instruction.
                // Typical case: the code returns, but there are still other
                // entries left on the stack. These have to be consistent.
                InstructionOffsetValue branchTargets =
                    partialEvaluator.branchTargets(offset);
                if (branchTargets != null &&
                    branchTargets.instructionOffsetCount() == 0)
                {
                    TracedStack tracedStack =
                        partialEvaluator.getStackBefore(offset);

                    int unpoppedStackSize = tracedStack.size() - popCount;

                    for (int stackIndex = 0; stackIndex < unpoppedStackSize; stackIndex++)
                    {
                        // Is this stack entry pushed by any producer
                        // (because it is required by other consumers)?
                        if (isStackEntryPresentBefore(offset, stackIndex))
                        {
                            // Mark all produced stack entries.
                            markStackEntryProducers(offset, stackIndex);
                        }
                    }
                }

                // Check all stack entries that are pushed.
                // Typical case: a return value that wasn't really required and
                // that should be popped.
                int pushCount = instruction.stackPushCount(clazz);
                if (pushCount > 0)
                {
                    TracedStack tracedStack =
                        partialEvaluator.getStackAfter(offset);

                    int stackSize = tracedStack.size();

                    int requiredPopCount = 0;
                    for (int stackIndex = stackSize - pushCount; stackIndex < stackSize; stackIndex++)
                    {
                        // Is the stack entry required by consumers?
                        if (!isStackEntryNecessaryAfter(offset, stackIndex))
                        {
                            // Remember to pop it.
                            requiredPopCount++;
                        }
                    }

                    // Pop the unnecessary stack entries.
                    if (requiredPopCount > 0)
                    {
                        if (DEBUG) System.out.println("  Inserting after marked producer "+instruction.toString(offset));

                        insertPopInstructions(offset, false, false, requiredPopCount);
                    }
                }
            }
            else
            {
                // Check all stack entries that would be popped.
                // Typical case: a stack value that is required elsewhere and
                // that still has to be popped.
                int popCount = instruction.stackPopCount(clazz);
                if (popCount > 0)
                {
                    TracedStack tracedStack =
                        partialEvaluator.getStackBefore(offset);

                    int stackSize = tracedStack.size();

                    int expectedPopCount = 0;
                    for (int stackIndex = stackSize - popCount; stackIndex < stackSize; stackIndex++)
                    {
                        // Is this stack entry pushed by any producer
                        // (because it is required by other consumers)?
                        if (isStackEntryPresentBefore(offset, stackIndex))
                        {
                            // Mark all produced stack entries.
                            markStackEntryProducers(offset, stackIndex);

                            // Remember to pop it.
                            expectedPopCount++;
                        }
                    }

                    // Pop the unnecessary stack entries.
                    if (expectedPopCount > 0)
                    {
                        if (DEBUG) System.out.println("  Replacing unmarked consumer "+instruction.toString(offset));

                        insertPopInstructions(offset, true, false, expectedPopCount);
                    }
                }

                // Check all stack entries that would be pushed.
                // Typical case: never.
                int pushCount = instruction.stackPushCount(clazz);
                if (pushCount > 0)
                {
                    TracedStack tracedStack =
                        partialEvaluator.getStackAfter(offset);

                    int stackSize = tracedStack.size();

                    int expectedPushCount = 0;
                    for (int stackIndex = stackSize - pushCount; stackIndex < stackSize; stackIndex++)
                    {
                        // Is the stack entry required by consumers?
                        if (isStackEntryNecessaryAfter(offset, stackIndex))
                        {
                            // Remember to push it.
                            expectedPushCount++;
                        }
                    }

                    // Push some necessary stack entries.
                    if (expectedPushCount > 0)
                    {
                        if (DEBUG) System.out.println("  Replacing unmarked producer "+instruction.toString(offset));

                        insertPushInstructions(offset, true, false, tracedStack.getTop(0).computationalType());
                    }
                }
            }
        }


        public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
        {
            if (isInstructionNecessary(offset) &&
                isDupOrSwap(simpleInstruction))
            {
                int stackSizeBefore = partialEvaluator.getStackBefore(offset).size();

                // Check all stack entries that are popped.
                // Typical case: a freshly marked variable initialization that
                // requires some value on the stack.
                int popCount = simpleInstruction.stackPopCount(clazz);
                if (popCount > 0)
                {
                    for (int stackIndex = stackSizeBefore - popCount; stackIndex < stackSizeBefore; stackIndex++)
                    {
                        // Is this stack entry pushed by any producer
                        // (because it is required by other consumers)?
                        if (isStackEntryPresentBefore(offset, stackIndex))
                        {
                            // Mark all produced stack entries.
                            markStackEntryProducers(offset, stackIndex);
                        }
                    }
                }

                int topBefore = stackSizeBefore - 1;
                int topAfter  = partialEvaluator.getStackAfter(offset).size() - 1;

                byte oldOpcode = simpleInstruction.opcode;

                // Simplify the dup/swap instruction if possible.
                int newOpcodes = fixDupSwap(offset, oldOpcode, topBefore, topAfter);

                // Did we find a suitabe (extended) opcode?
                if (newOpcodes == UNSUPPORTED)
                {
                    // We can't easily emulate some constructs.
                    throw new UnsupportedOperationException("Can't handle "+simpleInstruction.toString()+" instruction at ["+offset +"]");
                }

                // Is there a single replacement opcode?
                if ((newOpcodes & ~0xff) == 0)
                {
                    byte newOpcode = (byte)newOpcodes;

                    if      (newOpcode == InstructionConstants.OP_NOP)
                    {
                        // Delete the instruction.
                        codeAttributeEditor.deleteInstruction(offset);

                        if (extraDeletedInstructionVisitor != null)
                        {
                            extraDeletedInstructionVisitor.visitSimpleInstruction(null, null, null, offset, null);
                        }

                        if (DEBUG) System.out.println("  Deleting marked instruction "+simpleInstruction.toString(offset));
                    }
                    else if (newOpcode == oldOpcode)
                    {
                        // Leave the instruction unchanged.
                        codeAttributeEditor.undeleteInstruction(offset);

                        if (DEBUG) System.out.println("  Marking unchanged instruction "+simpleInstruction.toString(offset));
                    }
                    else
                    {
                        // Replace the instruction.
                        Instruction replacementInstruction = new SimpleInstruction(newOpcode);
                        codeAttributeEditor.replaceInstruction(offset,
                                                               replacementInstruction);

                        if (DEBUG) System.out.println("  Replacing instruction "+simpleInstruction.toString(offset)+" by "+replacementInstruction.toString());
                    }
                }
                else
                {
                    // Collect the replacement instructions.
                    Instruction[] replacementInstructions = new Instruction[4];

                    if (DEBUG) System.out.println("  Replacing instruction "+simpleInstruction.toString(offset)+" by");
                    int count = 0;
                    while (newOpcodes != 0)
                    {
                        SimpleInstruction replacementInstruction = new SimpleInstruction((byte)newOpcodes);
                        replacementInstructions[count++] = replacementInstruction;

                        if (DEBUG) System.out.println("    "+replacementInstruction.toString());
                        newOpcodes >>>= 8;
                    }

                    // Create a properly sized array.
                    if (count < 4)
                    {
                        Instruction[] newInstructions = new Instruction[count];
                        System.arraycopy(replacementInstructions, 0, newInstructions, 0, count);
                        replacementInstructions = newInstructions;
                    }

                    codeAttributeEditor.replaceInstruction(offset,
                                                           replacementInstructions);
                }
            }
            else
            {
                visitAnyInstruction(clazz, method, codeAttribute, offset, simpleInstruction);
            }
        }


        /**
         * Returns a dup/swap opcode that is corrected for the stack entries
         * that are present before the instruction and necessary after the
         * instruction. The returned integer opcode may contain multiple byte
         * opcodes (least significant byte first).
         * @param instructionOffset the offset of the dup/swap instruction.
         * @param dupSwapOpcode     the original dup/swap opcode.
         * @param topBefore         the index of the top stack entry before
         *                          the instruction (counting from the bottom).
         * @param topAfter          the index of the top stack entry after
         *                          the instruction (counting from the bottom).
         * @return the corrected opcode.
         */
        private int fixDupSwap(int  instructionOffset,
                               byte dupSwapOpcode,
                               int  topBefore,
                               int  topAfter)
        {
            switch (dupSwapOpcode)
            {
                case InstructionConstants.OP_DUP:     return fixedDup    (instructionOffset, topBefore, topAfter);
                case InstructionConstants.OP_DUP_X1:  return fixedDup_x1 (instructionOffset, topBefore, topAfter);
                case InstructionConstants.OP_DUP_X2:  return fixedDup_x2 (instructionOffset, topBefore, topAfter);
                case InstructionConstants.OP_DUP2:    return fixedDup2   (instructionOffset, topBefore, topAfter);
                case InstructionConstants.OP_DUP2_X1: return fixedDup2_x1(instructionOffset, topBefore, topAfter);
                case InstructionConstants.OP_DUP2_X2: return fixedDup2_x2(instructionOffset, topBefore, topAfter);
                case InstructionConstants.OP_SWAP:    return fixedSwap   (instructionOffset, topBefore, topAfter);
                default: throw new IllegalArgumentException("Not a dup/swap opcode ["+dupSwapOpcode+"]");
            }
        }


        private int fixedDup(int instructionOffset, int topBefore, int topAfter)
        {
            boolean stackEntryPresent0 = isStackEntryPresentBefore(instructionOffset, topBefore - 0);

            boolean stackEntryNecessary0 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 0);
            boolean stackEntryNecessary1 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 1);

            // Figure out which stack entries should be moved,
            // copied, or removed.
            return
                stackEntryNecessary0 ?
                    stackEntryNecessary1 ? DUP : // ...O -> ...OO
                                           NOP : // ...O -> ...O
                stackEntryNecessary1     ? NOP : // ...O -> ...O
                stackEntryPresent0       ? POP : // ...O -> ...
                                           NOP;  // ...  -> ...
        }


        private int fixedDup_x1(int instructionOffset, int topBefore, int topAfter)
        {
            boolean stackEntryPresent0 = isStackEntryPresentBefore(instructionOffset, topBefore - 0);
            boolean stackEntryPresent1 = isStackEntryPresentBefore(instructionOffset, topBefore - 1);

            boolean stackEntryNecessary0 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 0);
            boolean stackEntryNecessary1 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 1);
            boolean stackEntryNecessary2 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 2);

            // Figure out which stack entries should be moved,
            // copied, or removed.
            return
                stackEntryNecessary1 ?
                    stackEntryNecessary2 ?
                        stackEntryNecessary0 ? DUP_X1       : // ...XO -> ...OXO
                                               SWAP         : // ...XO -> ...OX
                    // !stackEntryNecessary2
                        stackEntryNecessary0 ? NOP          : // ...XO -> ...XO
                        stackEntryPresent0   ? POP          : // ...XO -> ...X
                                               NOP          : // ...X  -> ...X
                stackEntryPresent1 ?
                    stackEntryNecessary2 ?
                        stackEntryNecessary0 ? SWAP_POP_DUP : // ...XO -> ...OO
                                               POP_X1       : // ...XO -> ...O
                    // !stackEntryNecessary2
                        stackEntryNecessary0 ? POP_X1       : // ...XO -> ...O
                        stackEntryPresent0   ? POP2         : // ...XO -> ...
                                               POP          : // ...X  -> ...
                // !stackEntryPresent1
                    stackEntryNecessary2 ?
                        stackEntryNecessary0 ? DUP          : // ...O -> ...OO
                                               NOP          : // ...O -> ...O
                    // !stackEntryNecessary2
                        stackEntryNecessary0 ? NOP          : // ...O -> ...O
                        stackEntryPresent0   ? POP          : // ...O -> ...
                                               NOP;           // ...  -> ...
        }


        private int fixedDup_x2(int instructionOffset, int topBefore, int topAfter)
        {
            boolean stackEntryPresent0 = isStackEntryPresentBefore(instructionOffset, topBefore - 0);
            boolean stackEntryPresent1 = isStackEntryPresentBefore(instructionOffset, topBefore - 1);
            boolean stackEntryPresent2 = isStackEntryPresentBefore(instructionOffset, topBefore - 2);

            boolean stackEntryNecessary0 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 0);
            boolean stackEntryNecessary1 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 1);
            boolean stackEntryNecessary2 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 2);
            boolean stackEntryNecessary3 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 3);

            // Figure out which stack entries should be moved,
            // copied, or removed.
            return
                stackEntryNecessary1 ?
                    stackEntryNecessary2 ?
                        stackEntryNecessary3 ?
                            stackEntryNecessary0 ? DUP_X2          : // ...XYO -> ...OXYO
                                                   MOV_X2          : // ...XYO -> ...OXY
                        // !stackEntryNecessary3
                            stackEntryNecessary0 ? NOP             : // ...XYO -> ...XYO
                            stackEntryPresent0   ? POP             : // ...XYO -> ...XY
                                                   NOP             : // ...XY  -> ...XY
                    stackEntryPresent2 ?
                        stackEntryNecessary3 ?
                        //  stackEntryNecessary0 ? UNSUPPORTED     : // ...XYO -> ...OYO
                                                   UNSUPPORTED     : // ...XYO -> ...OY
                        // !stackEntryNecessary3
                            stackEntryNecessary0 ? POP_X2          : // ...XYO -> ...YO
                            stackEntryPresent0   ? POP_SWAP_POP    : // ...XYO -> ...Y
                                                   POP_X1          : // ...XY  -> ...Y
                    // !stackEntryPresent2
                        stackEntryNecessary3 ?
                            stackEntryNecessary0 ? DUP_X1          : // ...YO -> ...OYO
                                                   SWAP            : // ...YO -> ...OY
                        // !stackEntryNecessary3
                            stackEntryNecessary0 ? NOP             : // ...YO -> ...YO
                            stackEntryPresent0   ? POP             : // ...YO -> ...Y
                                                   NOP             : // ...Y  -> ...Y
                stackEntryPresent1 ?
                    stackEntryNecessary2 ?
                        stackEntryNecessary3 ?
                            stackEntryNecessary0 ? SWAP_POP_DUP_X1 : // ...XYO -> ...OXO
                                                   DUP_X2_POP2     : // ...XYO -> ...OX
                        // !stackEntryNecessary3
                            stackEntryNecessary0 ? POP_X1          : // ...XYO -> ...XO
                            stackEntryPresent0   ? POP2            : // ...XYO -> ...X
                                                   POP             : // ...XY  -> ...X
                    stackEntryPresent2 ?
                        stackEntryNecessary3 ?
                            stackEntryNecessary0 ? UNSUPPORTED     : // ...XYO -> ...OO
                                                   POP2_X1         : // ...XYO -> ...O
                        // !stackEntryNecessary3
                            stackEntryNecessary0 ? POP2_X1         : // ...XYO -> ...O
                            stackEntryPresent0   ? POP3            : // ...XYO -> ...
                                                   POP2            : // ...XY  -> ...
                    // !stackEntryPresent2
                        stackEntryNecessary3 ?
                            stackEntryNecessary0 ? SWAP_POP_DUP    : // ...YO -> ...OO
                                                   POP_X1          : // ...YO -> ...O
                        // !stackEntryNecessary3
                            stackEntryNecessary0 ? POP_X1          : // ...YO -> ...O
                            stackEntryPresent0   ? POP2            : // ...YO -> ...
                                                   POP             : // ...Y  -> ...
                // !stackEntryPresent1
                    stackEntryNecessary2 ?
                        stackEntryNecessary3 ?
                            stackEntryNecessary0 ? DUP_X1          : // ...XO -> ...OXO
                                                   SWAP            : // ...XO -> ...OX
                        // !stackEntryNecessary3
                            stackEntryNecessary0 ? NOP             : // ...XO -> ...XO
                            stackEntryPresent0   ? POP             : // ...XO -> ...X
                                                   NOP             : // ...X  -> ...X
                    stackEntryPresent2 ?
                        stackEntryNecessary3 ?
                            stackEntryNecessary0 ? SWAP_POP_DUP    : // ...XO -> ...OO
                                                   POP_X1          : // ...XO -> ...O
                        // !stackEntryNecessary3
                            stackEntryNecessary0 ? POP_X1          : // ...XO -> ...O
                            stackEntryPresent0   ? POP2            : // ...XO -> ...
                                                   POP             : // ...X  -> ...
                    // !stackEntryPresent2
                        stackEntryNecessary3 ?
                            stackEntryNecessary0 ? DUP             : // ...O -> ...OO
                                                   NOP             : // ...O -> ...O
                        // !stackEntryNecessary3
                            stackEntryNecessary0 ? NOP             : // ...O -> ...O
                            stackEntryPresent0   ? POP             : // ...O -> ...
                                                   NOP;              // ...  -> ...
        }


        private int fixedDup2(int instructionOffset, int topBefore, int topAfter)
        {
            boolean stackEntryPresent0 = isStackEntryPresentBefore(instructionOffset, topBefore - 0);
            boolean stackEntryPresent1 = isStackEntryPresentBefore(instructionOffset, topBefore - 1);

            boolean stackEntryNecessary0 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 0);
            boolean stackEntryNecessary1 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 1);
            boolean stackEntryNecessary2 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 2);
            boolean stackEntryNecessary3 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 3);

            return
                stackEntryNecessary3 ?
                    stackEntryNecessary2 ?
                        stackEntryNecessary1 ?
                            stackEntryNecessary0 ? DUP2             : // ...AB -> ...ABAB
                                                   SWAP_DUP_X1      : // ...AB -> ...ABA
                        // !stackEntryNecessary1
                            stackEntryNecessary0 ? DUP              : // ...AB -> ...ABB
                                                   NOP              : // ...AB -> ...AB
                    // !stackEntryNecessary2
                        stackEntryNecessary1 ?
                            stackEntryNecessary0 ? SWAP_DUP_X1_SWAP : // ...AB -> ...AAB
                            stackEntryPresent0   ? POP_DUP          : // ...AB -> ...AA
                                                   DUP              : // ...A  -> ...AA
                        // !stackEntryNecessary1
                            stackEntryNecessary0 ? NOP              : // ...AB -> ...AB
                            stackEntryPresent0   ? POP              : // ...AB -> ...A
                                                   NOP              : // ...A  -> ...A
                // !stackEntryNecessary3
                    stackEntryNecessary2 ?
                        stackEntryNecessary1 ?
                            stackEntryNecessary0 ? DUP_X1           : // ...AB -> ...BAB
                                                   SWAP             : // ...AB -> ...BA
                        stackEntryPresent1 ?
                            stackEntryNecessary0 ? SWAP_POP_DUP     : // ...AB -> ...BB
                                                   POP_X1           : // ...AB -> ...B
                        // !stackEntryPresent1
                            stackEntryNecessary0 ? POP              : // ...B  -> ...BB
                                                   NOP              : // ...B  -> ...B
                    // !stackEntryNecessary2
                        stackEntryNecessary1 ?
                            stackEntryNecessary0 ? NOP              : // ...AB -> ...AB
                            stackEntryPresent0   ? POP              : // ...AB -> ...A
                                                   NOP              : // ...A  -> ...A
                        stackEntryPresent1 ?
                            stackEntryNecessary0 ? POP_X1           : // ...AB -> ...B
                            stackEntryPresent0   ? POP2             : // ...AB -> ...
                                                   POP              : // ...A  -> ...
                        // !stackEntryPresent1
                            stackEntryNecessary0 ? NOP              : // ...B  -> ...B
                            stackEntryPresent0   ? POP              : // ...B  -> ...
                                                   NOP;               // ...   -> ...
        }


        private int fixedDup2_x1(int instructionOffset, int topBefore, int topAfter)
        {
            // We're currently assuming the value to be duplicated
            // is a long or a double, taking up two slots, or at
            // least consistent.
            boolean stackEntriesPresent01 = isStackEntriesPresentBefore(instructionOffset, topBefore - 0, topBefore - 1);
            boolean stackEntryPresent2    = isStackEntryPresentBefore(  instructionOffset, topBefore - 2);

            boolean stackEntriesNecessary01 = isStackEntriesNecessaryAfter(instructionOffset, topAfter - 0, topAfter - 1);
            boolean stackEntryNecessary2    = isStackEntryNecessaryAfter(  instructionOffset, topAfter - 2);
            boolean stackEntriesNecessary34 = isStackEntriesNecessaryAfter(instructionOffset, topAfter - 3, topAfter - 4);

            // Figure out which stack entries should be moved,
            // copied, or removed.
            return
                stackEntryNecessary2 ?
                    stackEntriesNecessary34 ?
                        stackEntriesNecessary01 ? DUP2_X1      : // ...XAB -> ...ABXAB
                                                  MOV2_X1      : // ...XAB -> ...ABX
                    // !stackEntriesNecessary34
                        stackEntriesNecessary01 ? NOP          : // ...XAB -> ...XAB
                        stackEntriesPresent01   ? POP2         : // ...XAB -> ...X
                                                  NOP          : // ...X   -> ...X
                stackEntryPresent2 ?
                    stackEntriesNecessary34 ?
                        stackEntriesNecessary01 ? UNSUPPORTED  : // ...XAB -> ...ABAB
                                                  POP_X2       : // ...XAB -> ...AB
                    // !stackEntriesNecessary34
                        stackEntriesNecessary01 ? DUP2_X1_POP3 : // ...XAB -> ...AB
                        stackEntriesPresent01   ? POP3         : // ...XAB -> ...
                                                  POP          : // ...X   -> ...
                // !stackEntryPresent2
                    stackEntriesNecessary34 ?
                        stackEntriesNecessary01 ? DUP2         : // ...AB -> ...ABAB
                                                  NOP          : // ...AB -> ...AB
                    // !stackEntriesNecessary34
                        stackEntriesNecessary01 ? NOP          : // ...AB -> ...AB
                        stackEntriesPresent01   ? POP2         : // ...AB -> ...
                                                  NOP;           // ...   -> ...
        }


        private int fixedDup2_x2(int instructionOffset, int topBefore, int topAfter)
        {
            // We're currently assuming the value to be duplicated
            // is a long or a double, taking up two slots, or at
            // least consistent.
            boolean stackEntriesPresent01 = isStackEntriesPresentBefore(instructionOffset, topBefore - 0, topBefore - 1);
            boolean stackEntryPresent2    = isStackEntryPresentBefore(  instructionOffset, topBefore - 2);
            boolean stackEntryPresent3    = isStackEntryPresentBefore(  instructionOffset, topBefore - 3);

            boolean stackEntriesNecessary01 = isStackEntriesNecessaryAfter(instructionOffset, topAfter - 0, topAfter - 1);
            boolean stackEntryNecessary2    = isStackEntryNecessaryAfter(  instructionOffset, topAfter - 2);
            boolean stackEntryNecessary3    = isStackEntryNecessaryAfter(  instructionOffset, topAfter - 3);
            boolean stackEntriesNecessary45 = isStackEntriesNecessaryAfter(instructionOffset, topAfter - 4, topAfter - 5);

            // Figure out which stack entries should be moved,
            // copied, or removed.
            return
                stackEntryNecessary2 ?
                    stackEntryNecessary3 ?
                        stackEntriesNecessary45 ?
                            stackEntriesNecessary01 ? DUP2_X2          : // ...XYAB -> ...ABXYAB
                                                      MOV2_X2          : // ...XYAB -> ...ABXY
                        // !stackEntriesNecessary45
                            stackEntriesNecessary01 ? NOP              : // ...XYAB -> ...XYAB
                            stackEntriesPresent01   ? POP2             : // ...XYAB -> ...XY
                                                      NOP              : // ...XY   -> ...XY
                    stackEntryPresent3 ?
                        stackEntriesNecessary45 ?
                            stackEntriesNecessary01 ? UNSUPPORTED      : // ...XYAB -> ...ABYAB
                                                      DUP2_X2_SWAP_POP : // ...XYAB -> ...ABY
                        // !stackEntriesNecessary45
                            stackEntriesNecessary01 ? POP_X3           : // ...XYAB -> ...YAB
                            stackEntriesPresent01   ? POP2_SWAP_POP    : // ...XYAB -> ...Y
                                                      POP_X1           : // ...XY   -> ...Y
                    // !stackEntryPresent3
                        stackEntriesNecessary45 ?
                            stackEntriesNecessary01 ? DUP2_X1          : // ...YAB -> ...ABYAB
                                                      MOV2_X1          : // ...YAB -> ...ABY
                        // !stackEntriesNecessary45
                            stackEntriesNecessary01 ? NOP              : // ...YAB -> ...YAB
                            stackEntriesPresent01   ? POP2             : // ...YAB -> ...Y
                                                      NOP              : // ...Y   -> ...Y
                stackEntryPresent2 ?
                    stackEntryNecessary3 ?
                        stackEntriesNecessary45 ?
                            stackEntriesNecessary01 ? UNSUPPORTED      : // ...XYAB -> ...ABXAB
                                                      DUP2_X2_POP3     : // ...XYAB -> ...ABX
                        // !stackEntriesNecessary45
                            stackEntriesNecessary01 ? POP_X2           : // ...XYAB -> ...XAB
                            stackEntriesPresent01   ? POP3             : // ...XYAB -> ...X
                                                      POP              : // ...XY   -> ...X
                    stackEntryPresent3 ?
                        stackEntriesNecessary45 ?
                            stackEntriesNecessary01 ? UNSUPPORTED      : // ...XYAB -> ...ABAB
                                                      POP2_X2          : // ...XYAB -> ...AB
                        // !stackEntriesNecessary45
                            stackEntriesNecessary01 ? POP2_X2          : // ...XYAB -> ...AB
                            stackEntriesPresent01   ? POP4             : // ...XYAB -> ...
                                                      POP2             : // ...XY   -> ...
                    // !stackEntryPresent3
                        stackEntriesNecessary45 ?
                            stackEntriesNecessary01 ? UNSUPPORTED      : // ...YAB -> ...ABAB
                                                      POP_X2           : // ...YAB -> ...AB
                        // !stackEntriesNecessary45
                            stackEntriesNecessary01 ? POP_X2           : // ...YAB -> ...AB
                            stackEntriesPresent01   ? POP3             : // ...YAB -> ...
                                                      POP              : // ...Y   -> ...
                // !stackEntryPresent2
                    stackEntryNecessary3 ?
                        stackEntriesNecessary45 ?
                            stackEntriesNecessary01 ? DUP2_X1          : // ...XAB -> ...ABXAB
                                                      MOV2_X1          : // ...XAB -> ...ABX
                        // !stackEntriesNecessary45
                            stackEntriesNecessary01 ? NOP              : // ...XAB -> ...XAB
                            stackEntriesPresent01   ? POP2             : // ...XAB -> ...X
                                                      NOP              : // ...X   -> ...X
                    stackEntryPresent3 ?
                        stackEntriesNecessary45 ?
                            stackEntriesNecessary01 ? UNSUPPORTED      : // ...XAB -> ...ABAB
                                                      POP_X2           : // ...XAB -> ...AB
                        // !stackEntriesNecessary45
                            stackEntriesNecessary01 ? POP_X2           : // ...XAB -> ...AB
                            stackEntriesPresent01   ? POP3             : // ...XAB -> ...
                                                      POP              : // ...X   -> ...
                    // !stackEntryPresent3
                        stackEntriesNecessary45 ?
                            stackEntriesNecessary01 ? DUP2             : // ...AB -> ...ABAB
                                                      NOP              : // ...AB -> ...AB
                        // !stackEntriesNecessary45
                            stackEntriesNecessary01 ? NOP              : // ...AB -> ...AB
                            stackEntriesPresent01   ? POP2             : // ...AB -> ...
                                                      NOP;               // ...   -> ...
        }


        private int fixedSwap(int instructionOffset, int topBefore, int topAfter)
        {
            boolean stackEntryPresent0 = isStackEntryPresentBefore(instructionOffset, topBefore - 0);
            boolean stackEntryPresent1 = isStackEntryPresentBefore(instructionOffset, topBefore - 1);

            boolean stackEntryNecessary0 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 0);
            boolean stackEntryNecessary1 = isStackEntryNecessaryAfter(instructionOffset, topAfter - 1);

            // Figure out which stack entries should be moved
            // or removed.
            return
                stackEntryNecessary0 ?
                    stackEntryNecessary1 ? SWAP   : // ...AB -> ...BA
                    stackEntryPresent0   ? POP    : // ...AB -> ...A
                                           NOP    : // ...A  -> ...A
                stackEntryPresent1       ? POP_X1 : // ...AB -> ...B
                                           NOP;     // ...B -> ...B
        }
    }


    // Small utility methods.

    /**
     * Marks the producing instructions of the variable consumer at the given
     * offset.
     * @param consumerOffset the offset of the variable consumer.
     * @param variableIndex  the index of the variable that is loaded.
     */
    private void markVariableProducers(int consumerOffset,
                                       int variableIndex)
    {
        InstructionOffsetValue producerOffsets =
            partialEvaluator.getVariablesBefore(consumerOffset).getProducerValue(variableIndex).instructionOffsetValue();

        if (producerOffsets != null)
        {
            int offsetCount = producerOffsets.instructionOffsetCount();
            for (int offsetIndex = 0; offsetIndex < offsetCount; offsetIndex++)
            {
                // Make sure the variable and the instruction are marked
                // at the producing offset.
                int offset = producerOffsets.instructionOffset(offsetIndex);

                markInstruction(offset);
            }
        }
    }


    /**
     * Ensures that the given variable is initialized before the specified
     * consumer of that variable, in the JVM's view.
     * @param consumerOffset the instruction offset before which the variable
     *                       needs to be initialized.
     * @param variableIndex  the index of the variable.
     */
    private void markVariableInitializersBefore(int consumerOffset,
                                                int variableIndex)
    {
        // Make sure the variable is initialized after all producers.
        // Use the simple evaluator, to get the JVM's view of what is
        // initialized.
        InstructionOffsetValue producerOffsets =
            simplePartialEvaluator.getVariablesBefore(consumerOffset).getProducerValue(variableIndex).instructionOffsetValue();

        int offsetCount = producerOffsets.instructionOffsetCount();
        for (int offsetIndex = 0; offsetIndex < offsetCount; offsetIndex++)
        {
            // Avoid infinite loops by only looking at producers before
            // the consumer.
            int producerOffset =
                producerOffsets.instructionOffset(offsetIndex);
            if (producerOffset < consumerOffset)
            {
                markVariableInitializersAfter(producerOffset, variableIndex);
            }
        }
    }


    /**
     * Ensures that the given variable is initialized after the specified
     * producer of that variable, in the JVM's view.
     * @param producerOffset the instruction offset after which the variable
     *                       needs to be initialized.
     * @param variableIndex  the index of the variable.
     */
    private void markVariableInitializersAfter(int producerOffset,
                                               int variableIndex)
    {
        // No problem if the producer has already been marked.
        if (!isInstructionNecessary(producerOffset))
        {
            // Is the unmarked producer a variable initialization?
            if (isVariableInitialization(producerOffset, variableIndex))
            {
                // Mark the producer.
                if (DEBUG) System.out.print("  Marking initialization of v"+variableIndex+" at ");

                markInstruction(producerOffset);

                if (DEBUG) System.out.println();
            }
            else
            {
                // Don't mark the producer, but recursively look at the
                // preceding producers of the same variable. Their values
                // will fall through, replacing this producer.
                markVariableInitializersBefore(producerOffset, variableIndex);
            }
        }
    }


    /**
     * Marks the stack entries and their producing instructions of the
     * consumer at the given offset.
     * @param clazz          the containing class.
     * @param consumerOffset the offset of the consumer.
     * @param consumer       the consumer of the stack entries.
     */
    private void markStackProducers(Clazz       clazz,
                                    int         consumerOffset,
                                    Instruction consumer)
    {
        TracedStack tracedStack =
            partialEvaluator.getStackBefore(consumerOffset);

        int stackSize = tracedStack.size();

        // Mark the producers of the popped values.
        int popCount = consumer.stackPopCount(clazz);
        for (int stackIndex = stackSize - popCount; stackIndex < stackSize; stackIndex++)
        {
            markStackEntryProducers(consumerOffset, stackIndex);
        }
    }


    /**
     * Marks the stack entry and the corresponding producing instructions
     * of the consumer at the given offset, if the stack entry of the
     * consumer is marked.
     * @param consumerOffset     the offset of the consumer.
     * @param consumerTopStackIndex the index of the stack entry to be checked
     *                           (counting from the top).
     * @param producerTopStackIndex the index of the stack entry to be marked
     *                           (counting from the top).
     */
    private void conditionallyMarkStackEntryProducers(int consumerOffset,
                                                      int consumerTopStackIndex,
                                                      int producerTopStackIndex)
    {
        int consumerBottomStackIndex = partialEvaluator.getStackAfter(consumerOffset).size() - consumerTopStackIndex - 1;

        if (isStackEntryNecessaryAfter(consumerOffset, consumerBottomStackIndex))
        {
            int producerBottomStackIndex = partialEvaluator.getStackBefore(consumerOffset).size() - producerTopStackIndex - 1;

            markStackEntryProducers(consumerOffset, producerBottomStackIndex);
        }
    }


    /**
     * Marks the stack entry and the corresponding producing instructions
     * of the consumer at the given offset.
     * @param consumerOffset the offset of the consumer.
     * @param stackIndex     the index of the stack entry to be marked
     *                        (counting from the bottom).
     */
    private void markStackEntryProducers(int consumerOffset,
                                         int stackIndex)
    {
        if (!isStackSimplifiedBefore(consumerOffset, stackIndex))
        {
            markStackEntryProducers(partialEvaluator.getStackBefore(consumerOffset).getBottomProducerValue(stackIndex).instructionOffsetValue(),
                                    stackIndex);
        }
    }


    /**
     * Marks the stack entry and its producing instructions at the given
     * offsets.
     * @param producerOffsets the offsets of the producers to be marked.
     * @param stackIndex      the index of the stack entry to be marked
     *                        (counting from the bottom).
     */
    private void markStackEntryProducers(InstructionOffsetValue producerOffsets,
                                         int                    stackIndex)
    {
        if (producerOffsets != null)
        {
            int offsetCount = producerOffsets.instructionOffsetCount();
            for (int offsetIndex = 0; offsetIndex < offsetCount; offsetIndex++)
            {
                // Make sure the stack entry and the instruction are marked
                // at the producing offset.
                int offset = producerOffsets.instructionOffset(offsetIndex);

                markStackEntryAfter(offset, stackIndex);
                markInstruction(offset);
            }
        }
    }


    /**
     * Marks the stack entry and its initializing instruction
     * ('invokespecial *.<init>') for the given 'new' instruction offset.
     * @param newInstructionOffset the offset of the 'new' instruction.
     */
    private void markInitialization(int newInstructionOffset)
    {
        int initializationOffset =
            partialEvaluator.initializationOffset(newInstructionOffset);

        TracedStack tracedStack =
            partialEvaluator.getStackAfter(newInstructionOffset);

        markStackEntryAfter(initializationOffset, tracedStack.size() - 1);
        markInstruction(initializationOffset);
    }


    /**
     * Marks the branch instructions of straddling branches, if they straddle
     * some code that has been marked.
     * @param instructionOffset   the offset of the branch origin or branch target.
     * @param branchOffsets       the offsets of the straddling branch targets
     *                            or branch origins.
     * @param isPointingToTargets <code>true</code> if the above offsets are
     *                            branch targets, <code>false</code> if they
     *                            are branch origins.
     */
    private void markStraddlingBranches(int                    instructionOffset,
                                        InstructionOffsetValue branchOffsets,
                                        boolean                isPointingToTargets)
    {
        if (branchOffsets != null)
        {
            // Loop over all branch offsets.
            int branchCount = branchOffsets.instructionOffsetCount();
            for (int branchIndex = 0; branchIndex < branchCount; branchIndex++)
            {
                // Is the branch straddling forward any necessary instructions?
                int branchOffset = branchOffsets.instructionOffset(branchIndex);

                // Is the offset pointing to a branch origin or to a branch target?
                if (isPointingToTargets)
                {
                    markStraddlingBranch(instructionOffset,
                                         branchOffset,
                                         instructionOffset,
                                         branchOffset);
                }
                else
                {
                    markStraddlingBranch(instructionOffset,
                                         branchOffset,
                                         branchOffset,
                                         instructionOffset);
                }
            }
        }
    }


    private void markStraddlingBranch(int instructionOffsetStart,
                                      int instructionOffsetEnd,
                                      int branchOrigin,
                                      int branchTarget)
    {
        if (!isInstructionNecessary(branchOrigin) &&
            isAnyInstructionNecessary(instructionOffsetStart, instructionOffsetEnd))
        {
            if (DEBUG) System.out.print("["+branchOrigin+"->"+branchTarget+"]");

            // Mark the branch instruction.
            markInstruction(branchOrigin);
        }
    }


    /**
     * Pushes a specified type of stack entry before or at the given offset.
     * The instruction is marked as necessary.
     */
    private void insertPushInstructions(int     offset,
                                        boolean replace,
                                        boolean before,
                                        int     computationalType)
    {
        // Mark this instruction.
        markInstruction(offset);

        // Create a simple push instrucion.
        Instruction replacementInstruction =
            new SimpleInstruction(pushOpcode(computationalType));

        if (DEBUG) System.out.println(": "+replacementInstruction.toString(offset));

        // Replace or insert the push instruction.
        insertInstruction(offset, replace, before, replacementInstruction);
    }


    /**
     * Returns the opcode of a push instruction corresponding to the given
     * computational type.
     * @param computationalType the computational type to be pushed on the stack.
     */
    private byte pushOpcode(int computationalType)
    {
        switch (computationalType)
        {
            case Value.TYPE_INTEGER:            return InstructionConstants.OP_ICONST_0;
            case Value.TYPE_LONG:               return InstructionConstants.OP_LCONST_0;
            case Value.TYPE_FLOAT:              return InstructionConstants.OP_FCONST_0;
            case Value.TYPE_DOUBLE:             return InstructionConstants.OP_DCONST_0;
            case Value.TYPE_REFERENCE:
            case Value.TYPE_INSTRUCTION_OFFSET: return InstructionConstants.OP_ACONST_NULL;
        }

        throw new IllegalArgumentException("No push opcode for computational type ["+computationalType+"]");
    }


    /**
     * Pops the given number of stack entries at or after the given offset.
     * The instructions are marked as necessary.
     */
    private void insertPopInstructions(int     offset,
                                       boolean replace,
                                       boolean before,
                                       int     popCount)
    {
        // Mark this instruction.
        markInstruction(offset);

        switch (popCount)
        {
            case 1:
            {
                // Replace or insert a single pop instruction.
                Instruction popInstruction =
                    new SimpleInstruction(InstructionConstants.OP_POP);

                insertInstruction(offset, replace, before, popInstruction);
                break;
            }
            case 2:
            {
                // Replace or insert a single pop2 instruction.
                Instruction popInstruction =
                    new SimpleInstruction(InstructionConstants.OP_POP2);

                insertInstruction(offset, replace, before, popInstruction);
                break;
            }
            default:
            {
                // Replace or insert the specified number of pop instructions.
                Instruction[] popInstructions =
                    new Instruction[popCount / 2 + popCount % 2];

                Instruction popInstruction =
                    new SimpleInstruction(InstructionConstants.OP_POP2);

                for (int index = 0; index < popCount / 2; index++)
                {
                      popInstructions[index] = popInstruction;
                }

                if (popCount % 2 == 1)
                {
                    popInstruction =
                        new SimpleInstruction(InstructionConstants.OP_POP);

                    popInstructions[popCount / 2] = popInstruction;
                }

                insertInstructions(offset,
                                   replace,
                                   before,
                                   popInstruction,
                                   popInstructions);
                break;
            }
        }
    }


    /**
     * Inserts or replaces the given instruction at the given offset.
     */
    private void insertInstruction(int         offset,
                                   boolean     replace,
                                   boolean     before,
                                   Instruction instruction)
    {
        if (replace)
        {
            codeAttributeEditor.replaceInstruction(offset, instruction);
        }
        else
        {
            if (before)
            {
                codeAttributeEditor.insertBeforeInstruction(offset, instruction);
            }
            else
            {
                codeAttributeEditor.insertAfterInstruction(offset, instruction);
            }

            if (extraAddedInstructionVisitor != null)
            {
                instruction.accept(null, null, null, offset, extraAddedInstructionVisitor);
            }
        }
    }


    /**
     * Inserts or replaces the given instruction at the given offset.
     */
    private void insertInstructions(int           offset,
                                    boolean       replace,
                                    boolean       before,
                                    Instruction   instruction,
                                    Instruction[] instructions)
    {
        if (replace)
        {
            codeAttributeEditor.replaceInstruction(offset, instructions);

            if (extraAddedInstructionVisitor != null)
            {
                for (int index = 1; index < instructions.length; index++)
                {
                    instructions[index].accept(null, null, null, offset, extraAddedInstructionVisitor);
                }
            }
        }
        else
        {
            if (before)
            {
                codeAttributeEditor.insertBeforeInstruction(offset, instructions);
            }
            else
            {
                codeAttributeEditor.insertAfterInstruction(offset, instructions);
            }

            for (int index = 0; index < instructions.length; index++)
            {
                if (extraAddedInstructionVisitor != null)
                {
                    instructions[index].accept(null, null, null, offset, extraAddedInstructionVisitor);
                }
            }
        }
    }


    /**
     * Replaces the instruction at a given offset by a static invocation.
     */
    private void replaceByStaticInvocation(Clazz               clazz,
                                           int                 offset,
                                           ConstantInstruction constantInstruction)
    {
        // Remember the replacement instruction.
        Instruction replacementInstruction =
             new ConstantInstruction(InstructionConstants.OP_INVOKESTATIC,
                                     constantInstruction.constantIndex);

        if (DEBUG) System.out.println("  Replacing by static invocation "+constantInstruction.toString(offset)+" -> "+replacementInstruction.toString());

        codeAttributeEditor.replaceInstruction(offset, replacementInstruction);
    }


    /**
     * Replaces the given instruction by an infinite loop.
     */
    private void replaceByInfiniteLoop(Clazz clazz,
                                       int   offset)
    {
        if (DEBUG) System.out.println("  Inserting infinite loop at ["+offset+"]");

        // Mark the instruction.
        markInstruction(offset);

        // Replace the instruction by an infinite loop.
        Instruction replacementInstruction =
            new BranchInstruction(InstructionConstants.OP_GOTO, 0);

        codeAttributeEditor.replaceInstruction(offset, replacementInstruction);
    }


    // Small utility methods.

    /**
     * Returns whether the given instruction is a dup or swap instruction
     * (dup, dup_x1, dup_x2, dup2, dup2_x1, dup2_x2, swap).
     */
    private boolean isDupOrSwap(Instruction instruction)
    {
        return instruction.opcode >= InstructionConstants.OP_DUP &&
               instruction.opcode <= InstructionConstants.OP_SWAP;
    }


    /**
     * Returns whether the given instruction is a pop instruction
     * (pop, pop2).
     */
    private boolean isPop(Instruction instruction)
    {
        return instruction.opcode == InstructionConstants.OP_POP ||
               instruction.opcode == InstructionConstants.OP_POP2;
    }


    /**
     * Returns whether any traced but unnecessary instruction between the two
     * given offsets is branching over the second given offset.
     */
    private boolean isAnyUnnecessaryInstructionBranchingOver(int instructionOffset1,
                                                             int instructionOffset2)
    {
        for (int offset = instructionOffset1; offset < instructionOffset2; offset++)
        {
            // Is it a traced but unmarked straddling branch?
            if (partialEvaluator.isTraced(offset) &&
                !isInstructionNecessary(offset)   &&
                isAnyLargerThan(partialEvaluator.branchTargets(offset),
                                instructionOffset2))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Returns whether all of the given instruction offsets (at least one)
     * are smaller than or equal to the given offset.
     */
    private boolean isAllSmallerThanOrEqual(InstructionOffsetValue instructionOffsets,
                                            int                    instructionOffset)
    {
        if (instructionOffsets != null)
        {
            // Loop over all instruction offsets.
            int branchCount = instructionOffsets.instructionOffsetCount();
            if (branchCount > 0)
            {
                for (int branchIndex = 0; branchIndex < branchCount; branchIndex++)
                {
                    // Is the offset larger than the reference offset?
                    if (instructionOffsets.instructionOffset(branchIndex) > instructionOffset)
                    {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }


    /**
     * Returns whether any of the given instruction offsets (at least one)
     * is larger than the given offset.
     */
    private boolean isAnyLargerThan(InstructionOffsetValue instructionOffsets,
                                    int                    instructionOffset)
    {
        if (instructionOffsets != null)
        {
            // Loop over all instruction offsets.
            int branchCount = instructionOffsets.instructionOffsetCount();
            if (branchCount > 0)
            {
                for (int branchIndex = 0; branchIndex < branchCount; branchIndex++)
                {
                    // Is the offset larger than the reference offset?
                    if (instructionOffsets.instructionOffset(branchIndex) > instructionOffset)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /**
     * Initializes the necessary data structure.
     */
    private void initializeNecessary(CodeAttribute codeAttribute)
    {
        int codeLength = codeAttribute.u4codeLength;
        int maxLocals  = codeAttribute.u2maxLocals;
        int maxStack   = codeAttribute.u2maxStack;

        // Create new arrays for storing information at each instruction offset.
        if (stacksNecessaryAfter.length    < codeLength ||
            stacksNecessaryAfter[0].length < maxStack)
        {
            stacksNecessaryAfter = new boolean[codeLength][maxStack];
        }
        else
        {
            for (int offset = 0; offset < codeLength; offset++)
            {
                Arrays.fill(stacksNecessaryAfter[offset], 0, maxStack, false);
            }
        }

        if (stacksSimplifiedBefore.length    < codeLength ||
            stacksSimplifiedBefore[0].length < maxStack)
        {
            stacksSimplifiedBefore = new boolean[codeLength][maxStack];
        }
        else
        {
            for (int offset = 0; offset < codeLength; offset++)
            {
                Arrays.fill(stacksSimplifiedBefore[offset], 0, maxStack, false);
            }
        }

        if (instructionsNecessary.length < codeLength)
        {
            instructionsNecessary = new boolean[codeLength];
        }
        else
        {
            Arrays.fill(instructionsNecessary, 0, codeLength, false);
        }
    }


    /**
     * Returns whether the specified variable is initialized at the specified
     * offset.
     */
    private boolean isVariableInitialization(int instructionOffset,
                                             int variableIndex)
    {
        // Wasn't the variable set yet?
        Value valueBefore = simplePartialEvaluator.getVariablesBefore(instructionOffset).getValue(variableIndex);
        if (valueBefore == null)
        {
            return true;
        }

        // Is the computational type different now?
        Value valueAfter = simplePartialEvaluator.getVariablesAfter(instructionOffset).getValue(variableIndex);
        if (valueAfter.computationalType() != valueBefore.computationalType())
        {
            return true;
        }

        // Is the reference type different now?
        if (valueAfter.computationalType() == Value.TYPE_REFERENCE &&
            (valueAfter.referenceValue().isNull() == Value.ALWAYS ||
             !valueAfter.referenceValue().getType().equals(valueBefore.referenceValue().getType())))
        {
            return true;
        }

        // Was the producer an argument (which may be removed)?
        Value producersBefore = simplePartialEvaluator.getVariablesBefore(instructionOffset).getProducerValue(variableIndex);
        return producersBefore.instructionOffsetValue().instructionOffsetCount() == 1 &&
               producersBefore.instructionOffsetValue().instructionOffset(0) == PartialEvaluator.AT_METHOD_ENTRY;
    }


    /**
     * Marks the stack entry after the given offset.
     * @param instructionOffset the offset of the stack entry to be marked.
     * @param stackIndex        the index of the stack entry to be marked
     *                          (counting from the bottom).
     */
    private void markStackEntryAfter(int instructionOffset,
                                     int stackIndex)
    {
        if (!isStackEntryNecessaryAfter(instructionOffset, stackIndex))
        {
            if (DEBUG) System.out.print("["+instructionOffset+".s"+stackIndex+"],");

            stacksNecessaryAfter[instructionOffset][stackIndex] = true;

            if (maxMarkedOffset < instructionOffset)
            {
                maxMarkedOffset = instructionOffset;
            }
        }
    }



    /**
     * Returns whether the stack specified entries before the given offset are
     * present.
     */
    private boolean isStackEntriesPresentBefore(int instructionOffset,
                                                int stackIndex1,
                                                int stackIndex2)
    {
        boolean present1 = isStackEntryPresentBefore(instructionOffset, stackIndex1);
        boolean present2 = isStackEntryPresentBefore(instructionOffset, stackIndex2);

//        if (present1 ^ present2)
//        {
//            throw new UnsupportedOperationException("Can't handle partial use of dup2 instructions");
//        }

        return present1 || present2;
    }


    /**
     * Returns whether the specified stack entry before the given offset is
     * present.
     * @param instructionOffset the offset of the stack entry to be checked.
     * @param stackIndex        the index of the stack entry to be checked
     *                          (counting from the bottom).
     */
    private boolean isStackEntryPresentBefore(int instructionOffset,
                                              int stackIndex)
    {
        TracedStack tracedStack =
            partialEvaluator.getStackBefore(instructionOffset);

        InstructionOffsetValue producerOffsets =
            tracedStack.getBottomProducerValue(stackIndex).instructionOffsetValue();

        return isAnyStackEntryNecessaryAfter(producerOffsets, stackIndex);
    }


    /**
     * Returns whether the stack specified entries after the given offset are
     * necessary.
     */
    private boolean isStackEntriesNecessaryAfter(int instructionOffset,
                                                 int stackIndex1,
                                                 int stackIndex2)
    {
        boolean present1 = isStackEntryNecessaryAfter(instructionOffset, stackIndex1);
        boolean present2 = isStackEntryNecessaryAfter(instructionOffset, stackIndex2);

//        if (present1 ^ present2)
//        {
//            throw new UnsupportedOperationException("Can't handle partial use of dup2 instructions");
//        }

        return present1 || present2;
    }


    /**
     * Returns whether any of the stack entries after the given offsets are
     * necessary.
     * @param instructionOffsets the offsets of the stack entries to be checked.
     * @param stackIndex         the index of the stack entries to be checked
     *                           (counting from the bottom).
     */
    private boolean isAnyStackEntryNecessaryAfter(InstructionOffsetValue instructionOffsets,
                                                  int                    stackIndex)
    {
        int offsetCount = instructionOffsets.instructionOffsetCount();

        for (int offsetIndex = 0; offsetIndex < offsetCount; offsetIndex++)
        {
            if (isStackEntryNecessaryAfter(instructionOffsets.instructionOffset(offsetIndex), stackIndex))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Returns whether the specified stack entry after the given offset is
     * necessary.
     * @param instructionOffset the offset of the stack entry to be checked.
     * @param stackIndex        the index of the stack entry to be checked
     *                          (counting from the bottom).
     */
    private boolean isStackEntryNecessaryAfter(int instructionOffset,
                                               int stackIndex)
    {
        return instructionOffset == PartialEvaluator.AT_CATCH_ENTRY ||
               stacksNecessaryAfter[instructionOffset][stackIndex];
    }


    private void markStackSimplificationBefore(int instructionOffset,
                                               int stackIndex)
    {
        stacksSimplifiedBefore[instructionOffset][stackIndex] = true;
    }


    private boolean isStackSimplifiedBefore(int instructionOffset,
                                            int stackIndex)
    {
        return stacksSimplifiedBefore[instructionOffset][stackIndex];
    }


    private void markInstruction(int instructionOffset)
    {
        if (!isInstructionNecessary(instructionOffset))
        {
            if (DEBUG) System.out.print(instructionOffset+",");

            instructionsNecessary[instructionOffset] = true;

            if (maxMarkedOffset < instructionOffset)
            {
                maxMarkedOffset = instructionOffset;
            }
        }
    }


    private boolean isAnyInstructionNecessary(int instructionOffset1,
                                              int instructionOffset2)
    {
        for (int instructionOffset = instructionOffset1;
             instructionOffset < instructionOffset2;
             instructionOffset++)
        {
            if (isInstructionNecessary(instructionOffset))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Returns the highest offset of an instruction that has been marked as
     * necessary, before the given offset.
     */
    private int lastNecessaryInstructionOffset(int instructionOffset)
    {
        for (int offset = instructionOffset-1; offset >= 0; offset--)
        {
            if (isInstructionNecessary(instructionOffset))
            {
                return offset;
            }
        }

        return 0;
    }


    private boolean isInstructionNecessary(int instructionOffset)
    {
        return instructionOffset == PartialEvaluator.AT_METHOD_ENTRY ||
               instructionsNecessary[instructionOffset];
    }
}
