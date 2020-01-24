package proguard.examples;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.AllMethodVisitor;
import proguard.evaluation.*;
import proguard.evaluation.value.*;
import proguard.io.*;

import java.io.*;
import java.util.Arrays;

/**
 * This sample application illustrates how to evaluate bytecode to get
 * information about its control flow and data flow. In this example,
 * it visualizes the control flow between the instructions.
 *
 * The example EvaluateCode also evaluates the code, to get more detailed
 * information about the data.
 *
 * Usage:
 *     java proguard.examples.VisualizeControlFlow input.jar
 */
public class VisualizeControlFlow
{
    public static void main(String[] args)
    {
        String inputJarFileName = args[0];

        try
        {
            // Parse all classes from the input jar,
            // and stream their code to the method analyzer.
            DirectoryPump directoryPump =
                new DirectoryPump(
                new File(inputJarFileName));

            directoryPump.pumpDataEntries(
                new JarReader(
                new ClassFilter(
                new ClassReader(false, false, false, false, null,
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new MyMethodAnalyzer()))))));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * This AttributeVisitor performs symbolic evaluation of the code of
     * each code attribute that it visits and then visualizes the control flow.
     */
    private static class MyMethodAnalyzer
    extends              SimplifiedVisitor
    implements           AttributeVisitor,
                         InstructionVisitor
    {
        // The partial evaluator and its support classes determine the
        // precision of the analysis. In this case, we don't need any detailed
        // information about the values. For this value factory, we also don't
        // need to initialize the cached cross-references between classes.
        private final ValueFactory     valueFactory     = new BasicValueFactory();
        private final PartialEvaluator partialEvaluator = new PartialEvaluator(valueFactory,
                                                                               new BasicInvocationUnit(valueFactory),
                                                                               false);
        private final int[] lineStarts = new int[16];
        private final int[] lineEnds   = new int[16];

        // Implementations for AttributeVisitor.

        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
        {
            // Print out a header.
            System.out.println(
                ClassUtil.externalClassName(clazz.getName()) + ": " +
                ClassUtil.externalFullMethodDescription(clazz.getName(),
                                                        method.getAccessFlags(),
                                                        method.getName(clazz),
                                                        method.getDescriptor(clazz)));

            // Evaluate the code.
            partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);

            // Reset the vertical lines of the control flow graph.
            Arrays.fill(lineStarts, -1);
            Arrays.fill(lineEnds,   -1);

            // Print out all instructions and the flow between them.
            codeAttribute.instructionsAccept(clazz, method, this);
        }


        // Implementations for InstructionVisitor.

        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
        {
            // Check whether the instruction is a branch target or origin.
            boolean isBranchTarget = partialEvaluator.isBranchTarget(offset);
            boolean isBranchOrigin = partialEvaluator.isBranchOrigin(offset);

            // Activate the vertical lines for future branch offsets from and
            // to this instruction.
            activateLines(offset, partialEvaluator.branchTargets(offset));
            activateLines(offset, partialEvaluator.branchOrigins(offset));

            // Start without a horizontal line.
            boolean horizontalLine = false;

            // Print out the lines of the control flow.
            for (int index = 0; index < lineEnds.length; index++)
            {
                int lineStart = lineStarts[index];
                int lineEnd   = lineEnds[index];

                boolean isNode =
                    offset == lineStart ||
                    offset == lineEnd;

                char c =
                    isNode           ? '+' :
                    offset < lineEnd ? '|' :
                    horizontalLine   ? '-' :
                                       ' ';

                System.out.print(c);

                if (isNode)
                {
                    horizontalLine = true;
                }
            }

            // Print an arrow at the end of this line, if any.
            System.out.print(horizontalLine ? '-' : ' ');
            System.out.print(isBranchTarget ? '>' :
                             isBranchOrigin ? '<' :
                             horizontalLine ? '-' : ' ');

            // Print a marker for exception handlers.
            System.out.print(partialEvaluator.isExceptionHandler(offset) ? 'E' :' ');

            // Print out the instruction.
            System.out.println(instruction.toString(offset));

            deactivateLine(offset);
        }


        /**
         * Activate vertical lines to be printed out.
         */
        private void activateLines(int lineStart, InstructionOffsetValue lineEnds)
        {
            if (lineEnds != null)
            {
                for (int index = 0; index < lineEnds.instructionOffsetCount(); index++)
                {
                    // Only consider forward branches and ignore the instruction
                    // offsets after conditional branches.
                    int branch = lineEnds.instructionOffset(index);
                    if (branch > lineStart &&
                        (partialEvaluator.isBranchTarget(branch) ||
                         partialEvaluator.isBranchOrigin(branch)))
                    {
                        activateLine(lineStart, branch);
                    }
                }
            }
        }


        /**
         * Activate a vertical line to be printed out.
         */
        private void activateLine(int lineStart, int lineEnd)
        {
            for (int index = 0; index < lineEnds.length; index++)
            {
                // Distribute the lines more uniformly by reversing the 4 bits.
                // This is of course a very basic layout strategy.
                int reverseIndex = Integer.reverse(index) >>> 28;

                if (lineEnds[reverseIndex] < 0)
                {
                    lineStarts[reverseIndex] = lineStart;
                    lineEnds[reverseIndex]   = lineEnd;

                    return;
                }
            }
        }


        /**
         * Deactivate a vertical line, so it is no longer printed.
         */
        private void deactivateLine(int lineEnd)
        {
            for (int index = 0; index < lineEnds.length; index++)
            {
                if (lineEnds[index] == lineEnd)
                {
                    lineStarts[index] = -1;
                    lineEnds[index]   = -1;
                }
            }
        }
    }
}
