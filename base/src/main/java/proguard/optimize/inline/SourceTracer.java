package proguard.optimize.inline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.InstructionFactory;
import proguard.classfile.instruction.VariableInstruction;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;
import proguard.evaluation.value.InstructionOffsetValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class has a few static methods which can be used to trace the source instruction of another instruction.
 * <p>
 * For example, you have a method call that uses an argument, the argument is pushed onto the stack at offset 18. In
 * this case you can use the <code>traceParameter</code> to find which instruction was originally responsible for
 * creating this value. For example the argument was loaded using <code>iload_3</code>, the previous instruction was
 * <code>istore_3</code>, the one before that was <code>iload_2</code>, the one before that was <code>istore_2</code>
 * and the instruction before that was <code>bipush 10</code>. In this case <code>bipush 10</code> would be considered
 * the source instruction. It is the original producer of the value.
 * <p>
 * The methods provided here only look in the same method, so if there was no <code>bipush 10</code> but the last
 * instruction we found was <code>iload_2</code> then we know that the source of this instruction is argument 2.
 * <p>
 * This is useful for when you are for example, trying to inline a lambda, you can see which method call in a method
 * consumes a certain argument that we know is a lambda. In that case we can try to inline that lambda into that
 * particular method call.
 */
public class SourceTracer {
    private static final Logger logger = LogManager.getLogger(SourceTracer.class);

    /**
     * Given an offset of an instruction, trace the source producer value.
     */
    public static Instruction traceParameter(PartialEvaluator partialEvaluator, CodeAttribute codeAttribute, int offset) {
        List<InstructionAtOffset> trace = traceParameterOffset(partialEvaluator, codeAttribute, offset);
        if (trace == null)
            return null;
        return trace.get(trace.size() - 1).instruction();
    }

    /**
     * Given an offset of an instruction, trace the source producer values.
     */
    public static List<Instruction> traceParameterSources(PartialEvaluator partialEvaluator, CodeAttribute codeAttribute, int offset) {
        ArrayList<InstructionAtOffset> leafNodes = new ArrayList<>();
        traceParameterTree(partialEvaluator, codeAttribute, offset, leafNodes);
        return leafNodes.stream().map(InstructionAtOffset::instruction).collect(Collectors.toList());
    }

    /**
     * This method will return a chain of instructions going from the original starting instruction to the source
     * instruction. It traces backwards so instructions with a bigger offset will appear before instructions with a
     * smaller offset.
     * @param partialEvaluator The partial evaluator that has visited this code attribute.
     * @param codeAttribute The code attribute in which we are searching.
     * @param offset The starting offset of the instruction for which we want to trace the source value.
     * @return A chain of instructions going from the instruction at offset to the source instruction.
     */
    public static List<InstructionAtOffset> traceParameterOffset(PartialEvaluator partialEvaluator, CodeAttribute codeAttribute, int offset) {
        List<InstructionAtOffset> trace = new ArrayList<>();
        TracedStack currentTracedStack;
        int currentOffset = offset;
        Instruction currentInstruction = InstructionFactory.create(codeAttribute.code, currentOffset);
        trace.add(new InstructionAtOffset(currentInstruction, currentOffset));

        // We stop when we found the source instruction
        while (
                (!isLoad(currentInstruction) ||
                !partialEvaluator.getVariablesBefore(currentOffset).getProducerValue(((VariableInstruction) currentInstruction).variableIndex).instructionOffsetValue().isMethodParameter(0)) &&
                isNotSourceInstruction(currentInstruction)
        ) {
            logger.debug(currentInstruction.toString(currentOffset));
            currentTracedStack = partialEvaluator.getStackBefore(currentOffset);
            logger.debug(currentTracedStack);

            // There is no stack value, it's coming from the variables
            if (isLoad(currentInstruction)) {
                InstructionOffsetValue offsetValue = partialEvaluator.getVariablesBefore(currentOffset).getProducerValue(((VariableInstruction) currentInstruction).variableIndex).instructionOffsetValue();
                // There are multiple sources, we don't know which one!
                if (offsetValue.instructionOffsetCount() > 1) {
                    return null;
                }
                currentOffset = offsetValue.instructionOffset(0);
            } else {
                int newOffset = currentTracedStack.getTopActualProducerValue(0).instructionOffsetValue().instructionOffset(0);
                if (newOffset == currentOffset) {
                    return null;
                }
                currentOffset = newOffset;
            }
            currentInstruction = InstructionFactory.create(codeAttribute.code, currentOffset);
            trace.add(new InstructionAtOffset(currentInstruction, currentOffset));
        }
        return trace;
    }

    public static boolean isLoad(Instruction instruction) {
        return instruction.getClass().equals(VariableInstruction.class) && ((VariableInstruction) instruction).isLoad();
    }

    /**
     * Similar to {@link #traceParameterOffset(PartialEvaluator partialEvaluator, CodeAttribute codeAttribute, int offset)}
     * but this method will build a tree instead of just a list. A list is not always possible because sometimes a
     * instruction can have multiple source values. For example when one of the source values is in an if statement.
     * We don't know if the branch will be taken or not so we have 2 potential source values. In this case the tree
     * structure is very useful.
     * @param partialEvaluator The partial evaluator  that has visited this code attribute.
     * @param codeAttribute The code attribute in which we are searching.
     * @param offset The offset of the instruction we are trying to find the source values for.
     * @param leafNodes The source nodes of the tree, this method will add all possible source instructions to this list
     * @return A node which is the root node of the tree, it starts at the offset and then goes up in the code attribute
     *         so just like the method that creates a chain this one goes from higher instruction offsets to lower
     *         instruction offsets.
     * @see #traceParameterOffset(PartialEvaluator partialEvaluator, CodeAttribute codeAttribute, int offset)
     */
    public static Node traceParameterTree(PartialEvaluator partialEvaluator, CodeAttribute codeAttribute, int offset, List<InstructionAtOffset> leafNodes) {
        TracedStack currentTracedStack;
        Instruction currentInstruction = InstructionFactory.create(codeAttribute.code, offset);
        Node root = new Node(new InstructionAtOffset(currentInstruction, offset), new ArrayList<>());

        // We stop when we found the source instruction
        if (isNotSourceInstruction(currentInstruction)) {
            logger.debug(currentInstruction.toString(offset));
            currentTracedStack = partialEvaluator.getStackBefore(offset);
            logger.debug(currentTracedStack);

            // There is no stack value, it's coming from the variables
            InstructionOffsetValue offsetValue;
            if (isLoad(currentInstruction)) {
                offsetValue = partialEvaluator.getVariablesBefore(offset).getProducerValue(((VariableInstruction) currentInstruction).variableIndex).instructionOffsetValue();
            }
            else {
                offsetValue = currentTracedStack.getTopActualProducerValue(0).instructionOffsetValue();
            }
            for (int i = 0; i < offsetValue.instructionOffsetCount(); i++) {
                if (!isLoad(currentInstruction) || !partialEvaluator.getVariablesBefore(offset).getProducerValue(((VariableInstruction) currentInstruction).variableIndex).instructionOffsetValue().isMethodParameter(i)) {
                    int newOffset = offsetValue.instructionOffset(i);
                    if (newOffset == offset) {
                        return root;
                    }
                    root.children.add(traceParameterTree(partialEvaluator, codeAttribute, newOffset, leafNodes));
                }
                else {
                    leafNodes.add(root.value);
                }
            }
        }
        else {
            // This is a leaf node, add the instruction to the list.
            leafNodes.add(root.value);
        }
        return root;
    }

    /**
     * Checks if an instruction is not considered a source instruction, a source instruction is an instruction that
     * produces a value by itself, without having to look further up the chain of instructions. We use this as a
     * stopping condition when we are tracing the source of an instruction.
     */
    private static boolean isNotSourceInstruction(Instruction instruction) {
        return instruction.opcode != Instruction.OP_ACONST_NULL &&
                instruction.canonicalOpcode() != Instruction.OP_ICONST_0 &&
                instruction.canonicalOpcode() != Instruction.OP_DCONST_0 &&
                instruction.canonicalOpcode() != Instruction.OP_FCONST_0 &&
                instruction.canonicalOpcode() != Instruction.OP_LCONST_0 &&
                instruction.opcode != Instruction.OP_LDC &&
                instruction.opcode != Instruction.OP_LDC_W &&
                instruction.opcode != Instruction.OP_LDC2_W &&
                instruction.opcode != Instruction.OP_NEW &&
                instruction.opcode != Instruction.OP_GETSTATIC &&
                instruction.opcode != Instruction.OP_INVOKESTATIC &&
                instruction.opcode != Instruction.OP_INVOKEVIRTUAL &&
                instruction.opcode != Instruction.OP_INVOKESPECIAL &&
                instruction.opcode != Instruction.OP_INVOKEINTERFACE &&
                instruction.opcode != Instruction.OP_GETFIELD;
    }
}
