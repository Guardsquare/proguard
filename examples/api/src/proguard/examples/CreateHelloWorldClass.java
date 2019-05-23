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
        // Create an empty class.
        ProgramClass programClass =
            new ProgramClass(ClassConstants.CLASS_VERSION_1_8,
                             1,
                             new Constant[1],
                             ClassConstants.ACC_PUBLIC,
                             0,
                             0);

        ConstantPoolEditor constantPoolEditor =
            new ConstantPoolEditor(programClass);

        programClass.u2thisClass =
            constantPoolEditor.addClassConstant(CLASS_NAME,
                                                programClass);

        programClass.u2superClass =
            constantPoolEditor.addClassConstant(ClassConstants.NAME_JAVA_LANG_OBJECT,
                                                null);

        // Add the main method to the class.
        new ClassEditor(programClass)
            .addMethod(createMethod(programClass));

        return programClass;
    }


    /**
     * Creates a HelloWorld method in the given class.
     */
    private static Method createMethod(ProgramClass programClass)
    {
        ConstantPoolEditor constantPoolEditor =
            new ConstantPoolEditor(programClass);

        // Create an empty main method.
        ProgramMethod programMethod =
            new ProgramMethod(ClassConstants.ACC_PUBLIC |
                              ClassConstants.ACC_STATIC,
                              constantPoolEditor.addUtf8Constant("main"),
                              constantPoolEditor.addUtf8Constant("([Ljava/lang/String;)V"),
                              null);

        // Add the method body to the method.
        new AttributesEditor(programClass, programMethod, true)
            .addAttribute(createCodeAttribute(programClass, programMethod));

        return programMethod;
    }


    /**
     * Creates a HelloWorld method body in the given method.
     */
    private static CodeAttribute createCodeAttribute(ProgramClass  programClass,
                                                     ProgramMethod programMethod)
    {
        ConstantPoolEditor constantPoolEditor =
            new ConstantPoolEditor(programClass);

        // Create an empty code attribute.
        CodeAttribute codeAtribute =
            new CodeAttribute(constantPoolEditor.addUtf8Constant(ClassConstants.ATTR_Code));

        // Add instructions to the code attribute.
        CodeAttributeComposer codeAttributeComposer =
            new CodeAttributeComposer();

        codeAttributeComposer.appendInstructions(
            new InstructionSequenceBuilder(constantPoolEditor)
                .getstatic("java/lang/System", "out", "Ljava/io/PrintStream;")
                .ldc(MESSAGE)
                .invokevirtual("java/io/PrintStream", "println", "(Ljava/lang/String;)V")
                .return_()
                .instructions());

        codeAttributeComposer.visitCodeAttribute(programClass,
                                                 programMethod,
                                                 codeAtribute);

        // Preverify the code.
        new CodePreverifier(false).visitCodeAttribute(programClass,
                                                      programMethod,
                                                      codeAtribute);

        return codeAtribute;
    }
}
