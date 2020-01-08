/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.io.ProgramClassWriter;
import proguard.classfile.visitor.MemberVisitor;

import java.io.*;

/**
 * This editor allows to build or extend classes (ProgramClass instances).
 * It provides methods to easily add interfaces, fields, and methods,
 * optionally with method bodies.
 *
 * @author Johan Leys
 * @author Eric Lafortune
 */
public class ClassBuilder
{
    //private static final String EXTRA_CLINIT_METHOD_PREFIX     = "clinit$";
    //private static final String EXTRA_CLINIT_METHOD_DESCRIPTOR = "()V";
    //private static final String EXTRA_INIT_METHOD_NAME         = "init$";
    //private static final String EXTRA_INIT_METHOD_DESCRIPTOR   = "()V";

    private final ProgramClass       programClass;
    private final ClassEditor        classEditor;
    private final ConstantPoolEditor constantPoolEditor;

    //private int staticInitializerCounter;


    /**
     * Creates a new ClassBuilder for the Java class with the given
     * name and super class.
     *
     * @param u4version      the class version.
     * @param u2accessFlags  access flags for the new class.
     * @param className      the fully qualified name of the new class.
     * @param superclassName the fully qualified name of the super class.
     *
     * @see VersionConstants
     * @see AccessConstants
     */
    public ClassBuilder(int    u4version,
                        int    u2accessFlags,
                        String className,
                        String superclassName)
    {
        this(u4version,
             u2accessFlags,
             className,
             superclassName,
             null,
             0,
             null);
    }


    /**
     * Creates a new ClassBuilder for the Java class with the given
     * name and super class.
     *
     * @param u4version       the class version.
     * @param u2accessFlags   access flags for the new class.
     * @param className       the fully qualified name of the new class.
     * @param superclassName  the fully qualified name of the super class.
     * @param featureName     an optional feature name for the new class.
     * @param processingFlags optional processing flags for the new class.
     * @param processingInfo  optional processing info for the new class.
     *
     * @see VersionConstants
     * @see AccessConstants
     */
    public ClassBuilder(int    u4version,
                        int    u2accessFlags,
                        String className,
                        String superclassName,
                        String featureName,
                        int    processingFlags,
                        Object processingInfo)
    {
        this(new ProgramClass(u4version,
                              1,
                              new Constant[ClassEstimates.TYPICAL_CONSTANT_POOL_SIZE],
                              u2accessFlags,
                              0,
                              0,
                              featureName,
                              processingFlags,
                              processingInfo));

        programClass.u2thisClass =
            constantPoolEditor.addClassConstant(className, programClass);

        if (superclassName != null)
        {
            programClass.u2superClass =
                constantPoolEditor.addClassConstant(superclassName, null);
        }
    }


    /**
     * Creates a new ClassBuilder for the given class.
     *
     * @param programClass the class to be edited.
     */
    public ClassBuilder(ProgramClass programClass)
    {
        this(programClass, null, null);
    }


    /**
     * Creates a new ClassBuilder for the given class, that automatically
     * initializes class references and class member references in new
     * constants.
     *
     * @param programClass     the class to be edited.
     * @param programClassPool the program class pool from which new constants
     *                         can be initialized.
     * @param libraryClassPool the library class pool from which new constants
     *                         can be initialized.
     */
    public ClassBuilder(ProgramClass programClass,
                        ClassPool    programClassPool,
                        ClassPool    libraryClassPool)

    {
        this.programClass  = programClass;
        classEditor        = new ClassEditor(programClass);
        constantPoolEditor = new ConstantPoolEditor(programClass,
                                                    programClassPool,
                                                    libraryClassPool);
    }


    /**
     * Returns the created or edited ProgramClass instance. This is a live
     * instance; any later calls to the builder will still affect the
     * instance.
     */
    public ProgramClass getProgramClass()
    {
        return programClass;
    }


    /**
     * Adds a new interface to the edited class.
     *
     * @param interfaceClass the interface class.
     * @return this instance of ClassBuilder.
     */
    public ClassBuilder addInterface(Clazz interfaceClass)
    {
        return addInterface(interfaceClass.getName(), interfaceClass);
    }


    /**
     * Adds a new interface to the edited class.
     *
     * @param interfaceName the name of the interface.
     * @return this instance of ClassBuilder.
     */
    public ClassBuilder addInterface(String interfaceName)
    {
        return addInterface(interfaceName, null);
    }


    /**
     * Adds a new interface to the edited class.
     *
     * @param interfaceName       the name of the interface.
     * @param referencedInterface the referenced interface.
     * @return this instance of ClassBuilder.
     */
    public ClassBuilder addInterface(String interfaceName,
                                     Clazz  referencedInterface)
    {
        // Add it to the class.
        classEditor.addInterface(constantPoolEditor.addClassConstant(interfaceName,
                                                                     referencedInterface));

        return this;
    }


    /**
     * Adds a new field to the edited class.
     *
     * @param u2accessFlags    access flags for the new field.
     * @param fieldName        name of the new field.
     * @param fieldDescriptor  descriptor of the new field.
     * @return this instance of ClassBuilder.
     */
    public ClassBuilder addField(int    u2accessFlags,
                                 String fieldName,
                                 String fieldDescriptor)
    {
        return addField(u2accessFlags,
                        fieldName,
                        fieldDescriptor,
                        null);
    }


    /**
     * Adds a new field to the edited class.
     *
     * @param u2accessFlags    access flags for the new field.
     * @param fieldName        name of the new field.
     * @param fieldDescriptor  descriptor of the new field.
     * @return this instance of ClassBuilder.
     */
    public ClassBuilder addField(int           u2accessFlags,
                                 String        fieldName,
                                 String        fieldDescriptor,
                                 MemberVisitor extraMemberVisitor)
    {
        // Create a simple field.
        ProgramField programField =
            new ProgramField(u2accessFlags,
                             constantPoolEditor.addUtf8Constant(fieldName),
                             constantPoolEditor.addUtf8Constant(fieldDescriptor),
                             null);

        // Add it to the class.
        classEditor.addField(programField);

        // Let the optional visitor visit the new field.
        if (extraMemberVisitor != null)
        {
            extraMemberVisitor.visitProgramField(programClass, programField);
        }

        return this;
    }


    /**
     * Adds a new method to the edited class.
     *
     * @param u2accessFlags      the access flags of the new method.
     * @param methodName         the name of the new method.
     * @param methodDescriptor   the descriptor of the new method.
     * @return this instance of ClassBuilder.
     */
    public ClassBuilder addMethod(int    u2accessFlags,
                                  String methodName,
                                  String methodDescriptor)
    {
        return addMethod(u2accessFlags,
                         methodName,
                         methodDescriptor,
                         null);
    }


    /**
     * Adds a new method to the edited class.
     *
     * @param u2accessFlags      the access flags of the new method.
     * @param methodName         the name of the new method.
     * @param methodDescriptor   the descriptor of the new method.
     * @param extraMemberVisitor an optional visitor for the method after
     *                           it has been created and added to the class.
     * @return this instance of ClassBuilder.
     */
    public ClassBuilder addMethod(int           u2accessFlags,
                                  String        methodName,
                                  String        methodDescriptor,
                                  MemberVisitor extraMemberVisitor)
    {
        return addMethod(u2accessFlags,
                         methodName,
                         methodDescriptor,
                         0,
                         null,
                         extraMemberVisitor);
    }


    /**
     * Adds a new method with a code attribute to the edited class.
     *
     * @param u2accessFlags         the access flags of the new method.
     * @param methodName            the name of the new method.
     * @param methodDescriptor      the descriptor of the new method.
     * @param maxCodeFragmentLength the maximum length for the code fragment.
     * @param codeBuilder           the provider of a composer to create code
     *                              attributes.
     * @return this instance of ClassBuilder.
     */
    public ClassBuilder addMethod(int         u2accessFlags,
                                  String      methodName,
                                  String      methodDescriptor,
                                  int         maxCodeFragmentLength,
                                  CodeBuilder codeBuilder)
    {
        return addMethod(u2accessFlags,
                         methodName,
                         methodDescriptor,
                         maxCodeFragmentLength,
                         codeBuilder,
                         null);
    }


    /**
     * Adds a new method with a code attribute to the edited class.
     *
     * @param u2accessFlags         the access flags of the new method.
     * @param methodName            the name of the new method.
     * @param methodDescriptor      the descriptor of the new method.
     * @param maxCodeFragmentLength the maximum length for the code fragment.
     * @param codeBuilder           the provider of a composer to create code
     *                              attributes.
     * @param extraMemberVisitor    an optional visitor for the method after
     *                              it has been created and added to the class.
     * @return this instance of ClassBuilder.
     */
    public ClassBuilder addMethod(int           u2accessFlags,
                                  String        methodName,
                                  String        methodDescriptor,
                                  int           maxCodeFragmentLength,
                                  CodeBuilder   codeBuilder,
                                  MemberVisitor extraMemberVisitor)
    {
        // Create an empty method.
        ProgramMethod programMethod =
            new ProgramMethod(u2accessFlags,
                              constantPoolEditor.addUtf8Constant(methodName),
                              constantPoolEditor.addUtf8Constant(methodDescriptor),
                              null);

        if (codeBuilder != null)
        {
            // Create an empty code attribute.
            CodeAttribute codeAttribute =
                new CodeAttribute(constantPoolEditor.addUtf8Constant(Attribute.CODE));

            // Create and set up a composer for the caller.
            CompactCodeAttributeComposer compactCodeAttributeComposer =
                new CompactCodeAttributeComposer(programClass);

            compactCodeAttributeComposer.beginCodeFragment(maxCodeFragmentLength);

            // Let the caller add its instructions, exceptions, etc.
            codeBuilder.compose(compactCodeAttributeComposer);

            // End the composer.
            compactCodeAttributeComposer.endCodeFragment();

            // Copy the accumulated code into the attribute.
            compactCodeAttributeComposer.visitCodeAttribute(programClass, programMethod, codeAttribute);

            // Add the code attribute to the method.
            new AttributesEditor(programClass, programMethod, false).addAttribute(codeAttribute);
        }

        // Add the method to the class.
        classEditor.addMethod(programMethod);

        // Let the optional visitor visit the new method.
        if (extraMemberVisitor != null)
        {
            extraMemberVisitor.visitProgramMethod(programClass, programMethod);
        }

        return this;
    }


//    /**
//     * Adds the given static initializer instructions to the edited class.
//     * If the class already contains a static initializer, the new instructions
//     * will be appended to the existing initializer.
//     *
//     * @param instructions                 the instructions to be added.
//     * @param mergeIntoExistingInitializer indicates whether the instructions should
//     *                                     be added to the existing static initializer
//     *                                     (if it exists), or if a new method should
//     *                                     be created, which is then called from the
//     *                                     existing initializer.
//     */
//    public void addStaticInitializerInstructions(Instruction[] instructions,
//                                                 boolean       mergeIntoExistingInitializer)
//    {
//        Method method = programClass.findMethod(METHOD_NAME_CLINIT, METHOD_TYPE_CLINIT);
//
//        if (method == null)
//        {
//            addMethod(ACC_STATIC,
//                      METHOD_NAME_CLINIT,
//                      METHOD_TYPE_CLINIT,
//                      0,
//                      instructions,
//                      null,
//                      new SimpleInstruction(Instruction.OP_RETURN));
//        }
//        else
//        {
//            if (!mergeIntoExistingInitializer)
//            {
//                // Create a new static initializer.
//                ProgramMethod newMethod =
//                    addMethod(ACC_STATIC,
//                              EXTRA_CLINIT_METHOD_PREFIX + staticInitializerCounter++,
//                              EXTRA_CLINIT_METHOD_DESCRIPTOR,
//                              // Make sure that such methods are not optimized (inlined)
//                              // to prevent potential overflow errors during conversion.
//                              ProcessingFlags.DONT_OPTIMIZE,
//                              instructions,
//                              null,
//                              new SimpleInstruction(Instruction.OP_RETURN));
//
//                // Call the new initializer from the existing one.
//                InstructionSequenceBuilder builder = new InstructionSequenceBuilder(programClass);
//                builder.invokestatic(programClass.getName(),
//                                      newMethod.getName(programClass),
//                                      "()V",
//                                      programClass,
//                                      newMethod);
//                instructions = builder.instructions();
//            }
//            CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
//            ((ProgramMethod) method).attributesAccept(programClass,
//                                                      new CodeAttributeEditorResetter(codeAttributeEditor));
//            codeAttributeEditor.insertBeforeOffset(0, instructions);
//            ((ProgramMethod) method).attributesAccept(programClass,
//                                                      codeAttributeEditor);
//        }
//    }
//
//
//    /**
//     * Adds the given initialization instructions to the edited class.
//     *
//     * - If the class doesn't contain a constructor yet, it will be created,
//     *   and the instructions will be added to this constructor.
//     * - If there is a single super-calling constructor, the instructions will
//     *   be added at the beginning of it's code attribute.
//     * - If there are multiple super-calling constructors, a new private
//     *   parameterless helper method will be created, to which the instructions
//     *   will be added. An invocation to this new method will be added at the
//     *   beginning of the code attribute of all super-calling constructors.
//     *
//     * @param instructions the instructions to be added.
//     */
//    public void addInitializerInstructions(Instruction[] instructions)
//    {
//        Method method = programClass.findMethod(METHOD_NAME_INIT, null);
//
//        if (method == null)
//        {
//            // First call the super constructor.
//            Instruction[] firstInstruction =
//            {
//                new VariableInstruction(Instruction.OP_ALOAD_0),
//                new ConstantInstruction(
//                    Instruction.OP_INVOKESPECIAL,
//                    constantPoolEditor.addMethodrefConstant(programClass.getSuperName(),
//                                                            METHOD_NAME_INIT,
//                                                            METHOD_TYPE_INIT,
//                                                            null,
//                                                            null))
//            };
//
//            // End by calling return.
//            SimpleInstruction lastInstruction =
//                new SimpleInstruction(Instruction.OP_RETURN);
//
//            addMethod(ACC_PUBLIC,
//                      METHOD_NAME_INIT,
//                      METHOD_TYPE_INIT,
//                      0,
//                      instructions,
//                      firstInstruction,
//                      lastInstruction);
//        }
//        else
//        {
//            // Find all super-calling constructors.
//            Set<Method> constructors =  new HashSet<Method>();
//            programClass.methodsAccept(
//                new ConstructorMethodFilter(
//                new MethodCollector(constructors), null, null));
//
//            if (constructors.size() == 1)
//            {
//                // There is only one supper-calling constructor.
//                // Add the code to this constructor.
//                constructors.iterator().next().accept(programClass,
//                    new AllAttributeVisitor(
//                    new MyInstructionInserter(instructions)));
//            }
//            else
//            {
//                // There are multiple super-calling constructors. Add the
//                // instructions to a separate, parameterless initialization
//                // method, and invoke this method from all super-calling
//                // constructors.
//                ProgramMethod initMethod = (ProgramMethod) programClass.findMethod(EXTRA_INIT_METHOD_NAME,
//                                                                                   EXTRA_INIT_METHOD_DESCRIPTOR);
//                if (initMethod == null)
//                {
//                    // There is no init$ method yet. Create it now, and add the
//                    // given instructions to it.
//                    initMethod = addMethod(ACC_PRIVATE,
//                                           EXTRA_INIT_METHOD_NAME,
//                                           EXTRA_INIT_METHOD_DESCRIPTOR,
//                                           // Make sure that such methods are not optimized (inlined)
//                                           // to prevent potential overflow errors during conversion.
//                                           ProcessingFlags.DONT_OPTIMIZE,
//                                           instructions,
//                                           null,
//                                           new SimpleInstruction(Instruction.OP_RETURN));
//
//                    // Insert a call to the new init$ method in all super-calling constructors.
//                    InstructionSequenceBuilder builder = new InstructionSequenceBuilder(programClass);
//                    builder.aload_0();
//                    builder.invokespecial(programClass.getName(),
//                                          EXTRA_INIT_METHOD_NAME,
//                                          EXTRA_INIT_METHOD_DESCRIPTOR,
//                                          programClass,
//                                          initMethod);
//
//                    programClass.methodsAccept(
//                        new ConstructorMethodFilter(
//                        new AllAttributeVisitor(
//                        new MyInstructionInserter(instructions)), null, null));
//                }
//                else
//                {
//                    // There is already an init$ method. Add the instructions to this method.
//                    initMethod.accept(programClass,
//                        new AllAttributeVisitor(
//                        new MyInstructionInserter(instructions)));
//                }
//            }
//        }
//    }


    /**
     * This functional interface provides a code attribute composer to
     * its caller.
     */
    public interface CodeBuilder
    {
        public void compose(CompactCodeAttributeComposer code);
    }


    /**
     * Small sample application that illustrates the use of this class.
     */
    public static void main(String[] args)
    {
        // Create a class with a simple main method.
        ProgramClass programClass =
            new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                "com/example/Test",
                ClassConstants.NAME_JAVA_LANG_OBJECT)

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
                        .ldc("Hello, world!")
                        .invokevirtual("java/io/PrintStream", "println", "(Ljava/lang/String;)V")
                        .return_())

                .getProgramClass();

        // Print out the class.
        //programClass.accept(new ClassPrinter());

        // Write out the class.
        try
        {
            DataOutputStream dataOutputStream =
                new DataOutputStream(
                new FileOutputStream("Test.class"));

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
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
}
