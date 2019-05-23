package proguard.examples;

import proguard.classfile.ClassConstants;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.BranchTargetFinder;
import proguard.classfile.visitor.*;
import proguard.io.*;
import proguard.preverify.CodePreverifier;

import java.io.*;

/**
 * This sample application illustrates how to modify bytecode with the ProGuard API.
 * It applies peephole optimizations to all code that it processes.
 *
 * Usage:
 *     java proguard.examples.ApplyPeepholeOptimizations input.jar output.jar
 */
public class ApplyPeepholeOptimizations
{
    private static final String STRING_BUFFER = ClassConstants.NAME_JAVA_LANG_STRING_BUFFER;

    private static final int X = InstructionSequenceReplacer.X;
    private static final int Y = InstructionSequenceReplacer.Y;
    private static final int Z = InstructionSequenceReplacer.Z;

    private static final int A = InstructionSequenceReplacer.A;
    private static final int B = InstructionSequenceReplacer.B;
    private static final int C = InstructionSequenceReplacer.C;
    private static final int D = InstructionSequenceReplacer.D;

    private static final int INT_A_STRING = InstructionSequenceReplacer.INT_A_STRING;


    public static void main(String[] args)
    {
        String inputJarFileName  = args[0];
        String outputJarFileName = args[1];

        // Create replacement sequences. We're using a variable name
        // consisting of underscores to focus on the bytecode instructions.
        // This is a small subset of the peephole optimizations that
        // ProGuard applies, just to illustrate the various patterns,
        // including wildcards. You can find more examples in
        // proguard.optimize.peephole.InstructionSequenceConstants
        InstructionSequenceBuilder ____ =
            new InstructionSequenceBuilder();

        Instruction[][][] replacements =
        {
            {   // ... + 0 = ...
                ____.iconst_0()
                    .iadd().__(),
            },

            {   // i=i = nothing
                ____.iload(X)
                    .istore(X).__(),
            },

            {   // putstatic/getstatic = dup/putstatic
                ____.putstatic(X)
                    .getstatic(X).__(),

                ____.dup()
                    .putstatic(X).__()
            },

            {   // new StringBuffer().append(I) = new StringBuffer("....")
                ____.invokespecial(STRING_BUFFER, "<init>", "()V")
                    .iconst(A)
                    .invokevirtual(STRING_BUFFER, "append", "(I)Ljava/lang/StringBuffer;").__(),

                ____.ldc_(INT_A_STRING)
                    .invokespecial(STRING_BUFFER, "<init>", "(Ljava/lang/String;)V").__()
            },
        };

        Constant[] constants = ____.constants();

        try
        {
            // We'll write the output to a jar file.
            JarWriter jarWriter =
                new JarWriter(
                new FixedFileWriter(new File(outputJarFileName)));

            // Parse and push all classes from the input jar.
            DirectoryPump directoryPump =
                new DirectoryPump(new File(inputJarFileName));

            BranchTargetFinder  branchTargetFinder  = new BranchTargetFinder();
            CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

            directoryPump.pumpDataEntries(
                new JarReader(
                new ClassFilter(
                new ClassReader(false, false, false, null,
                new MultiClassVisitor(
                    // Modify the methods of the class.
                    new AllMethodVisitor(
                    new AllAttributeVisitor(
                    new PeepholeEditor(branchTargetFinder, codeAttributeEditor,
                    new InstructionSequencesReplacer(constants,
                                                     replacements,
                                                     branchTargetFinder,
                                                     codeAttributeEditor)))),

                    // Preverify the methods of the class.
                    new AllMethodVisitor(
                    new AllAttributeVisitor(
                    new CodePreverifier(false))),

                    // For clean results, shrink and sort the constants of the class.
                    new ConstantPoolShrinker(),
                    new ConstantPoolSorter(),

                    // Write the class file.
                    new DataEntryClassWriter(jarWriter, new FileDataEntry(null, null))
                )))));

            jarWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
