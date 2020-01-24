package proguard.examples;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.io.*;

import java.io.*;

/**
 * This sample application illustrates how to use visitors to iterate over
 * specific instructions and exception handlers. In this example, it prints out
 * an approximation of the cyclomatic complexity of all methods, as introduced
 * by T.J. McCabe in "IEEE Transactions on Software Engineering", p308-320
 * (Dec 1976)
 *
 * Usage:
 *     java proguard.examples.CyclomaticComplexity input.jar
 */
public class CyclomaticComplexity
{
    public static void main(String[] args)
    {
        String inputJarFileName = args[0];

        try
        {
            // Parse all classes from the input jar and stream their code to the
            // control flow analyzer.
            DirectoryPump directoryPump =
                new DirectoryPump(new File(inputJarFileName));

            directoryPump.pumpDataEntries(
                new JarReader(
                new ClassFilter(
                new ClassReader(false, false, false, false, null,
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new ControlFlowAnalyzer()))))));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * This AttributeVisitor calculates (an approximation of) the cyclomatic
     * complexity of each code attribute that it visits and then prints out this
     * complexity.
     */
    private static class ControlFlowAnalyzer
    extends              SimplifiedVisitor
    implements           AttributeVisitor,
                         InstructionVisitor,
                         ExceptionInfoVisitor
    {
        // Used to store the complexity while visiting instructions and
        // exception handlers.
        private int complexity;


        // Implementations for AttributeVisitor.

        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
        {
            // Because every method should contain a ?RETURN or ATHROW
            // instruction, we start our complexity with 0 and count them later.
            complexity = 0;

            // Visit all instructions.
            codeAttribute.instructionsAccept(clazz, method, this);
            // Visit all exception handlers (try-catch/try-finally blocks also
            // influence the cyclomatic complexity).
            codeAttribute.exceptionsAccept(clazz, method, this);

            System.out.println(
                ClassUtil.externalClassName(clazz.getName()) +
                ": " +
                ClassUtil.externalFullMethodDescription(clazz.getName(),
                                                        clazz.getAccessFlags(),
                                                        method.getName(clazz),
                                                        method.getDescriptor(clazz)) +
                " has a cyclomatic complexity of " +
                complexity);
        }


        // Implementations for InstructionVisitor.


        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
        {
            switch (instruction.opcode)
            {
                case Instruction.OP_IRETURN:
                case Instruction.OP_LRETURN:
                case Instruction.OP_FRETURN:
                case Instruction.OP_DRETURN:
                case Instruction.OP_ARETURN:
                case Instruction.OP_RETURN:
                case Instruction.OP_ATHROW:
                    // All ?RETURN instrucions and ATHROW end the method
                    // prematurely, and must be counted.
                    complexity++;
                    break;
                case Instruction.OP_RET:
                    // RET is ignored because RET instructions are (were) only
                    // used by the Java compiler to implement finally blocks,
                    // which are counted seperately.
                    break;
            }
        }


        public void visitBranchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, BranchInstruction branchInstruction)
        {
            // The canonicalOpcode method returns the opcode of the instruction,
            // ignoring the _w extension denoting wide instructions.
            // GOTO is ignored because GOTO instructions is (in most cases) part
            // of a larger control flow structure (switch statement, for loop...),
            // which are already counted.
            // JSR is ignored because JSR instructions are (were) only used by
            // the Java compiler to implement finally blocks, which are counted
            // seperately.
            if (branchInstruction.canonicalOpcode() != Instruction.OP_GOTO &&
                branchInstruction.canonicalOpcode() != Instruction.OP_JSR)
            {
                complexity++;
                // We do not use any of the unique BranchInstruction fields, so
                // theoretically this code could be placed in the
                // visitAnyInstruction method. However, in this method we know
                // for sure the instruction is a BranchInstruction, and can
                // filter out the GOTO and JSR instructions, whereas in the
                // visitAnyInstruction method, we would have to specify every
                // single relevant opcode.
            }
        }


        public void visitAnySwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SwitchInstruction switchInstruction)
        {
            complexity++;
            for (int index = 0; index < switchInstruction.jumpOffsets.length; index++) {
                // Increment for any switch case, even if the case falls through.
                // As the best way for handling fallthrough switch cases is
                // debatable, we choose the easier option: incrementing every
                // time.
                complexity++;
            }
        }


        // Implementations for ExceptionInfoVisitor.


        public void visitExceptionInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, ExceptionInfo exceptionInfo)
        {
            // Counting catch and finally blocks.
            complexity++;
        }
    }
}
