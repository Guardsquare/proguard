package proguard.optimize.inline;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.InstructionFactory;
import proguard.classfile.instruction.VariableInstruction;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;
import proguard.evaluation.value.InstructionOffsetValue;
import proguard.optimize.peephole.MethodInliner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Util {
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

        // Maybe make use of stackPopCount, if an instruction doesn't pop anything from the stack then it is the producer?
        while (
                (!isLoad(currentInstruction) ||
                        !partialEvaluator.getVariablesBefore(currentOffset).getProducerValue(((VariableInstruction) currentInstruction).variableIndex).instructionOffsetValue().isMethodParameter(0)) &&
                        currentInstruction.opcode != Instruction.OP_ACONST_NULL &&
                        currentInstruction.canonicalOpcode() != Instruction.OP_ICONST_0 &&
                        currentInstruction.opcode != Instruction.OP_LDC &&
                        currentInstruction.opcode != Instruction.OP_LDC_W &&
                        currentInstruction.opcode != Instruction.OP_LDC2_W &&
                        currentInstruction.opcode != Instruction.OP_NEW &&
                        currentInstruction.opcode != Instruction.OP_GETSTATIC &&
                        currentInstruction.opcode != Instruction.OP_INVOKESTATIC &&
                        currentInstruction.opcode != Instruction.OP_INVOKEVIRTUAL &&
                        currentInstruction.opcode != Instruction.OP_GETFIELD
        ) {
            System.out.println(currentInstruction.toString(currentOffset));
            currentTracedStack = partialEvaluator.getStackBefore(currentOffset);
            System.out.println(currentTracedStack);

            // There is no stack value, it's coming from the variables
            if (isLoad(currentInstruction)) {
                InstructionOffsetValue offsetValue = partialEvaluator.getVariablesBefore(currentOffset).getProducerValue(((VariableInstruction) currentInstruction).variableIndex).instructionOffsetValue();
                // There are multiple sources, we don't know which one! We could in the future return a tree instead of just null if  that would be useful.
                if (offsetValue.instructionOffsetCount() > 1) {
                    return null;
                }
                currentOffset = offsetValue.instructionOffset(0);
            } else {
                currentOffset = currentTracedStack.getTopActualProducerValue(0).instructionOffsetValue().instructionOffset(0);
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

        // Maybe make use of stackPopCount, if an instruction doesn't pop anything from the stack then it is the producer?
        if (
                /*(!isLoad(currentInstruction) ||
                        !partialEvaluator.getVariablesBefore(offset).getProducerValue(((VariableInstruction) currentInstruction).variableIndex).instructionOffsetValue().isMethodParameter(0)) &&*/
            currentInstruction.opcode != Instruction.OP_ACONST_NULL &&
            currentInstruction.canonicalOpcode() != Instruction.OP_ICONST_0 &&
            currentInstruction.opcode != Instruction.OP_LDC &&
            currentInstruction.opcode != Instruction.OP_LDC_W &&
            currentInstruction.opcode != Instruction.OP_LDC2_W &&
            currentInstruction.opcode != Instruction.OP_NEW &&
            currentInstruction.opcode != Instruction.OP_GETSTATIC &&
            currentInstruction.opcode != Instruction.OP_INVOKESTATIC &&
            currentInstruction.opcode != Instruction.OP_INVOKEVIRTUAL &&
            currentInstruction.opcode != Instruction.OP_GETFIELD
        ) {
            System.out.println(currentInstruction.toString(offset));
            currentTracedStack = partialEvaluator.getStackBefore(offset);
            System.out.println(currentTracedStack);

            // There is no stack value, it's coming from the variables
            InstructionOffsetValue offsetValue;
            if (isLoad(currentInstruction)) {
                offsetValue = partialEvaluator.getVariablesBefore(offset).getProducerValue(((VariableInstruction) currentInstruction).variableIndex).instructionOffsetValue();
            }
            else {
                offsetValue = currentTracedStack.getTopActualProducerValue(0).instructionOffsetValue();
            }
            for (int i = 0; i < offsetValue.instructionOffsetCount(); i++) {
                if (!isLoad(currentInstruction) || !partialEvaluator.getVariablesBefore(offset).getProducerValue(((VariableInstruction) currentInstruction).variableIndex).instructionOffsetValue().isMethodParameter(i))
                    root.children.add(traceParameterTree(partialEvaluator, codeAttribute, offsetValue.instructionOffset(i), leafNodes));
                else
                    leafNodes.add(root.value);
            }
        }
        else {
            // This is a leaf node, add the instruction to the list.
            leafNodes.add(root.value);
        }
        return root;
    }

    /**
     * Inline a specified method in the locations it is called in the current clazz.
     * @param clazz The clazz in which we want to inline the targetMethod
     * @param targetMethod The method we want to inline.
     */
    public static void inlineMethodInClass(Clazz clazz, Method targetMethod) {
        clazz.methodsAccept(new AllAttributeVisitor(new MethodInliner(false, false, 20000, true, false, null) {
            @Override
            protected boolean shouldInline(Clazz clazz, Method method, CodeAttribute codeAttribute) {
                return method.equals(targetMethod);
            }
        }));
    }
}
