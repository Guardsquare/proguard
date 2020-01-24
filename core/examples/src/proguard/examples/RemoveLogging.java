package proguard.examples;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.io.*;
import proguard.preverify.CodePreverifier;

import java.io.*;

/**
 * This sample application illustrates how to modify bytecode with the ProGuard API.
 * It removes Android logging invocations from all methods of all classes that it
 * processes. It does not attempt to remove the related strings and other arguments
 * from the code.
 *
 * Usage:
 *     java proguard.examples.RemoveLogging input.jar output.jar
 */
public class RemoveLogging
{
    public static void main(String[] args)
    {
        String inputJarFileName  = args[0];
        String outputJarFileName = args[1];

        // Create replacement sequences. We're using a variable name
        // consisting of underscores to focus on the bytecode instructions.
        InstructionSequenceBuilder ____ =
            new InstructionSequenceBuilder();

        Instruction[][][] replacements =
        {
            {   // Log.v(String,String) + pop -> pop2
                ____.invokestatic("android/util/Log", "v", "(Ljava/lang/String;Ljava/lang/String;)I")
                    .pop().__(),

                ____.pop2().__()
            },
            {   // Log.v(String,String,Throwable) + pop -> pop3
                ____.invokestatic("android/util/Log", "v", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I")
                    .pop().__(),

                ____.pop2()
                    .pop().__()
            },
            {   // Log.d(String,String) + pop -> pop2
                ____.invokestatic("android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I")
                    .pop().__(),

                ____.pop2().__()
            },
            {   // Log.d(String,String,Throwable) + pop -> pop3
                ____.invokestatic("android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I")
                    .pop().__(),

                ____.pop2()
                    .pop().__()
            },
        };

        Constant[] constants = ____.constants();

        try
        {
            // We'll write the output to a jar file.
            JarWriter jarWriter =
                new JarWriter(
                new ZipWriter(
                new FixedFileWriter(
                new File(outputJarFileName))));

            // Parse and push all classes from the input jar.
            DirectoryPump directoryPump =
                new DirectoryPump(
                new File(inputJarFileName));

            BranchTargetFinder  branchTargetFinder  = new BranchTargetFinder();
            CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

            directoryPump.pumpDataEntries(
                new JarReader(
                new ClassFilter(
                new ClassReader(false, false, false, false, null,
                new MultiClassVisitor(
                    // Modify the methods of the class.
                    new AllMethodVisitor(
                    new AllAttributeVisitor(
                    new PeepholeEditor(branchTargetFinder, codeAttributeEditor,
                    new InstructionSequencesReplacer(constants,
                                                     replacements,
                                                     branchTargetFinder,
                                                     codeAttributeEditor)))),

                    // For simple changes that don't change the control flow,
                    // we don't need to preverify the processed code from
                    // scratch. The updated stack map tables remain valid.

                    // For clean results, shrink and sort the constants of the class.
                    new ConstantPoolShrinker(),
                    new ConstantPoolSorter(),

                    // Write the class file.
                    new DataEntryClassWriter(jarWriter)
                )))));

            jarWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
