package proguard.examples;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.io.*;
import proguard.preverify.CodePreverifier;

import java.io.*;

/**
 * This sample application illustrates how to modify bytecode with the ProGuard API.
 * It adds logging code at the start of all methods of all classes that it processes.
 *
 * Usage:
 *     java proguard.examples.AddLogging input.jar output.jar
 */
public class AddLogging
{
    public static void main(String[] args)
    {
        String inputJarFileName  = args[0];
        String outputJarFileName = args[1];

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
                new DirectoryPump(new File(inputJarFileName));

            directoryPump.pumpDataEntries(
                new JarReader(
                new ClassFilter(
                new ClassReader(false, false, false, false, null,
                new MultiClassVisitor(
                    // Modify the class.
                    new AllMethodVisitor(
                    new AllAttributeVisitor(
                    new MyLoggingAdder())),

                    // For simple changes that don't change the control flow,
                    // we don't need to preverify the processed code from
                    // scratch. The updated stack map tables remain valid.

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


    /**
     * This AttributeVisitor inserts logging instructions at the start of every
     * code attribute that it visits.
     */
    private static class MyLoggingAdder
    extends              SimplifiedVisitor
    implements           AttributeVisitor
    {
        private CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor(true, true);


        // Implementations for AttributeVisitor.

        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
        {
            // Create instructions to insert.
            String logMessage =
                ClassUtil.externalClassName(clazz.getName()) +
                ": entering method '" +
                ClassUtil.externalFullMethodDescription(clazz.getName(),
                                                        clazz.getAccessFlags(),
                                                        method.getName(clazz),
                                                        method.getDescriptor(clazz)) +
                "'";

            Instruction[] loggingInstructions =
                new InstructionSequenceBuilder((ProgramClass)clazz)
                    .getstatic("java/lang/System", "err", "Ljava/io/PrintStream;")
                    .ldc(logMessage)
                    .invokevirtual("java/io/PrintStream", "println", "(Ljava/lang/String;)V")
                    .instructions();

            // Insert the instructions at the start of the method.
            codeAttributeEditor.reset(codeAttribute.u4codeLength);
            codeAttributeEditor.insertBeforeInstruction(0, loggingInstructions);
            codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
        }
    }
}
