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
     * Checks if an instruction is considered a source instruction, this is an instruction that produces a value by
     * itself, without having to look further up the chain of instructions. We use this as a stopping condition when we
     * are tracing the source of an instruction.
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
                instruction.opcode != Instruction.OP_GETFIELD;
    }
}
