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

/**
 * This sample application illustrates how to evaluate bytecode and print out
 * some results. In this example, it prints out the potential return values of
 * all methods, as accurately as the analysis allows.
 *
 * ProGuard applies more elaborate analyses to simplify the code, propagating
 * parameters and return values across methods, in proguard.optimize.Optimizer.
 *
 * Usage:
 *     java proguard.examples.EvaluateReturnValues input.jar
 */
public class EvaluateCode
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
     * each code attribute that it visits and then prints out information
     * about the values that it may return.
     */
    private static class MyMethodAnalyzer
    extends              SimplifiedVisitor
    implements           AttributeVisitor,
                         InstructionVisitor
    {
        // The partial evaluator and its support classes determine
        // the precision of the analysis.
        private final RangeValueFactory valueFactory     = new RangeValueFactory(new ArrayReferenceValueFactory());
        private final PartialEvaluator  partialEvaluator = new PartialEvaluator(valueFactory,
                                                                                new BasicInvocationUnit(valueFactory),
                                                                                false);

        // Implementations for AttributeVisitor.

        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
        {
            // Evaluate the code.
            partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);

            // Go over all instruction to print out some information about
            // the results.
            codeAttribute.instructionsAccept(clazz, method, this);
        }


        // Implementations for InstructionVisitor.

        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


        public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
        {
            // Was the instruction reachable?
            if (partialEvaluator.isTraced(offset))
            {
                // Does the instruction return a value?
                switch (simpleInstruction.opcode)
                {
                    case Instruction.OP_IRETURN:
                    case Instruction.OP_LRETURN:
                    case Instruction.OP_FRETURN:
                    case Instruction.OP_DRETURN:
                    case Instruction.OP_ARETURN:
                    {
                        // Print our the return value.
                        System.out.println(
                            ClassUtil.externalClassName(clazz.getName()) +
                            ": " +
                            ClassUtil.externalFullMethodDescription(clazz.getName(),
                                                                    clazz.getAccessFlags(),
                                                                    method.getName(clazz),
                                                                    method.getDescriptor(clazz)) +
                            " may return " +
                            partialEvaluator.getStackBefore(offset).getTop(0));

                        break;
                    }
                }
            }
        }
    }
}
