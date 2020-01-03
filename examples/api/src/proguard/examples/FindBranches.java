package proguard.examples;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.AllMethodVisitor;
import proguard.io.*;

import java.io.*;

/**
 * This sample application illustrates how to find branches in the code and
 * print out some results. This is a very basic analysis. The sample
 * EvaluateCode performs more advanced analysis of the control flow and
 * data flow.
 *
 * Usage:
 *     java proguard.examples.FindBranches input.jar
 */
public class FindBranches
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
     * This AttributeVisitor finds branches in the code of each code attribute
     * that it visits and then prints out information about the values that it
     * may return.
     */
    private static class MyMethodAnalyzer
    extends              SimplifiedVisitor
    implements           AttributeVisitor,
                         InstructionVisitor
    {
        private final BranchTargetFinder branchTargetFinder = new BranchTargetFinder();

        // Implementations for AttributeVisitor.

        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
        {
            // Find all branches in the code.
            branchTargetFinder.visitCodeAttribute(clazz, method, codeAttribute);

            // Go over all instructions to print out some information about
            // the results.
            codeAttribute.instructionsAccept(clazz, method, this);
        }


        // Implementations for InstructionVisitor.

        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
        {
            // Is the instruction a branch target?
            if (branchTargetFinder.isBranchTarget(offset))
            {
                System.out.println(
                    ClassUtil.externalClassName(clazz.getName()) + ": " +
                    ClassUtil.externalFullMethodDescription(clazz.getName(),
                                                            method.getAccessFlags(),
                                                            method.getName(clazz),
                                                            method.getDescriptor(clazz)) +
                    ": branch to " + instruction.toString(offset));
            }

            // Is the instruction the start of an exception handler?
            if (branchTargetFinder.isExceptionHandler(offset))
            {
                System.out.println(
                    ClassUtil.externalClassName(clazz.getName()) + ": " +
                    ClassUtil.externalFullMethodDescription(clazz.getName(),
                                                            method.getAccessFlags(),
                                                            method.getName(clazz),
                                                            method.getDescriptor(clazz)) +
                    ": exception handler at " + instruction.toString(offset));
            }

            // Is the instruction a branch origin?
            if (branchTargetFinder.isBranchOrigin(offset))
            {
                System.out.println(
                    ClassUtil.externalClassName(clazz.getName()) + ": " +
                    ClassUtil.externalFullMethodDescription(clazz.getName(),
                                                            method.getAccessFlags(),
                                                            method.getName(clazz),
                                                            method.getDescriptor(clazz)) +
                    ": branch from " + instruction.toString(offset));
            }
        }
    }
}
