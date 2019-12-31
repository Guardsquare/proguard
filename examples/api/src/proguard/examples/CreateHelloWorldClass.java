package proguard.examples;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.*;
import proguard.classfile.io.ProgramClassWriter;
import proguard.preverify.CodePreverifier;

import java.io.*;

/**
 * This sample application illustrates how to create a class with the ProGuard API.
 *
 * Usage:
 *     java proguard.examples.CreateHelloWorldClass
 */
public class CreateHelloWorldClass
{
    private static final String CLASS_NAME = "HelloWorld";
    private static final String MESSAGE    = "Hello, world!";


    public static void main(String[] args)
    {
        try
        {
            // Create the class.
            ProgramClass programClass = createClass();

            // Write out the class.
            String classFileName = CLASS_NAME + ClassConstants.CLASS_FILE_EXTENSION;

            DataOutputStream dataOutputStream =
                new DataOutputStream(
                new FileOutputStream(classFileName));

            try
            {
                programClass.accept(
                    new ProgramClassWriter(dataOutputStream));
            }
            finally
            {
                dataOutputStream.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Creates a HelloWorld class.
     */
    private static ProgramClass createClass()
    {
        return
            // Start building the class.
            new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                CLASS_NAME,
                ClassConstants.NAME_JAVA_LANG_OBJECT)

                // Add the main method.
                .addMethod(
                    AccessConstants.PUBLIC |
                    AccessConstants.STATIC,
                    "main",
                    "([Ljava/lang/String;)V",
                    50,

                    // Compose the equivalent of this java code:
                    //     System.out.println("Hello, world!");
                    code -> code
                        .getstatic("java/lang/System", "out", "Ljava/io/PrintStream;")
                        .ldc(MESSAGE)
                        .invokevirtual("java/io/PrintStream", "println", "(Ljava/lang/String;)V")
                        .return_())

                // We don't need to preverify simple code that doesn't have
                // special control flow. It works fine without a stack map
                // table attribute.

                // Retrieve the final class.
                .getProgramClass();
    }
}
