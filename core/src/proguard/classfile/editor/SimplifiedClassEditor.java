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
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.util.ProcessingFlags;

import java.util.*;

import static proguard.classfile.ClassConstants.*;

/**
 * This editor allows to build and/or edit classes (ProgramClass instances).
 * It provides methods to easily add fields and methods to classes.
 *
 * @author Johan Leys
 */
public class SimplifiedClassEditor
{
    private static final String EXTRA_CLINIT_METHOD_PREFIX     = "clinit$";
    private static final String EXTRA_CLINIT_METHOD_DESCRIPTOR = "()V";
    private static final String EXTRA_INIT_METHOD_NAME         = "init$";
    private static final String EXTRA_INIT_METHOD_DESCRIPTOR   = "()V";

    private final ProgramClass       programClass;
    private final ClassEditor        classEditor;
    private final ConstantPoolEditor constantPoolEditor;

    private String superClassName;
    private int    staticInitializerCounter;

    private final List<MyCodeComposer> methodComposers = new ArrayList<MyCodeComposer>();


    /**
     * Creates a new SimplifiedClassEditor for the Java class with the given
     * name.
     *
     * @param u2accessFlags   access flags for the new class.
     * @param className       the fully qualified name of the new class.
     * @param processingFlags processing flags for the new class.
     *
     * @see ClassConstants
     */
    public SimplifiedClassEditor(int    u2accessFlags,
                                 String className,
                                 int    processingFlags)
    {
        this(u2accessFlags, className, null, processingFlags);
    }


    /**
     * Creates a new SimplifiedClassEditor for the Java class with the given
     * name and super class.
     *
     * @param u2accessFlags   access flags for the new class.
     * @param className       the fully qualified name of the new class.
     * @param superclassName  the fully qualified name of the super class.
     * @param processingFlags processing flags for the new class.
     *
     * @see ClassConstants
     */
    public SimplifiedClassEditor(int    u2accessFlags,
                                 String className,
                                 String superclassName,
                                 int    processingFlags)
    {
        this(new ProgramClass(ClassConstants.CLASS_VERSION_1_2,
                              1,
                              new Constant[10],
                              u2accessFlags,
                              0,
                              0,
                              processingFlags));

        programClass.u2thisClass =
            constantPoolEditor.addClassConstant(className, programClass);

        if (superclassName != null)
        {
            programClass.u2superClass =
                constantPoolEditor.addClassConstant(superclassName, null);
            this.superClassName = superclassName;
        }
    }


    /**
     * Creates a new SimplifiedClassEditor for the given class.
     *
     * @param programClass the class to be edited.
     */
    public SimplifiedClassEditor(ProgramClass programClass)
    {
        this.programClass  = programClass;
        classEditor        = new ClassEditor(programClass);
        constantPoolEditor = new ConstantPoolEditor(programClass);
    }


    /**
     * Finalizes the editing of the class. This method does not initialize
     * references to/from related classes. At least one of the finishEditing
     * methods should be called before calling {@link #getProgramClass}.
     *
     * @see #finishEditing(ClassPool, ClassPool)
     */
    public void finishEditing()
    {
        for (MyCodeComposer composer : methodComposers)
        {
            composer.finishEditing();
        }

        methodComposers.clear();
    }


    /**
     * Finalizes the editing of the class, and initializes all references
     * of the edited class w.r.t. the given program and library class pool.
     * At least one of the finishEditing methods should be called before
     * calling {@link #getProgramClass}.
     *
     * @param programClassPool the program class pool
     * @param libraryClassPool the library class pool
     */
    public void finishEditing(ClassPool programClassPool,
                              ClassPool libraryClassPool)
    {
        finishEditing();

        // Initialize all references to/from the edited class.
        if (superClassName != null)
        {
            new ClassSuperHierarchyInitializer(programClassPool, libraryClassPool, null, null).visitProgramClass(programClass);
            new ClassSubHierarchyInitializer().visitProgramClass(programClass);
        }
        new ClassReferenceInitializer(programClassPool, libraryClassPool).visitProgramClass(programClass);
    }


    /**
     * Returns the edited ProgramClass instance. Make sure to call one of the
     * finishEditing methods after finishing editing, before calling this
     * method.
     *
     * @return the edited ProgramClass instance.
     *
     * @see #finishEditing()
     * @see #finishEditing(ClassPool, ClassPool)
     */
    public ProgramClass getProgramClass()
    {
        return programClass;
    }


    /**
     * Adds a new field to the edited class.
     *
     * @param u2accessFlags    access flags for the new field.
     * @param fieldName        name of the new field.
     * @param fieldDescriptor  descriptor of the new field.
     *
     * @return the created field.
     */
    public Field addField(int    u2accessFlags,
                          String fieldName,
                          String fieldDescriptor)
    {
        return addField(u2accessFlags,
                        fieldName,
                        fieldDescriptor,
                        0);
    }


    /**
     * Adds a new field to the edited class.
     *
     * @param u2accessFlags    access flags for the new field.
     * @param fieldName        name of the new field.
     * @param fieldDescriptor  descriptor of the new field.
     *
     * @return the created field.
     */
    public ProgramField addField(int    u2accessFlags,
                                 String fieldName,
                                 String fieldDescriptor,
                                 int    processingFlags)
    {
        ProgramField field = new ProgramField(u2accessFlags,
                                              constantPoolEditor.addUtf8Constant(fieldName),
                                              constantPoolEditor.addUtf8Constant(fieldDescriptor),
                                              null,
                                              processingFlags);
        classEditor.addField(field);

        return field;
    }


    /**
     * Adds a new method to the edited class, with the given instructions array.
     *
     * @param u2accessFlags         access flags for the new method.
     * @param methodName            name of the new method.
     * @param methodDescriptor      descriptor of the new method.
     * @param instructions          the instructions of the new method.
     */
    public ProgramMethod addMethod(int           u2accessFlags,
                                   String        methodName,
                                   String        methodDescriptor,
                                   Instruction[] instructions)
    {
        return addMethod(u2accessFlags,
                         methodName,
                         methodDescriptor,
                         0,
                         instructions);
    }


    /**
     * Adds a new method to the edited class, with the given instructions array.
     *
     * @param u2accessFlags         access flags for the new method.
     * @param methodName            name of the new method.
     * @param methodDescriptor      descriptor of the new method.
     * @param processingFlags       processing flags for the new method.
     * @param instructions          the instructions of the new method.
     */
    public ProgramMethod addMethod(int           u2accessFlags,
                                   String        methodName,
                                   String        methodDescriptor,
                                   int           processingFlags,
                                   Instruction[] instructions)
    {
        return addMethod(u2accessFlags,
                         methodName,
                         methodDescriptor,
                         processingFlags,
                         instructions,
                         null,
                         null);
    }


    /**
     * Adds a new method to the edited class. The returned composer can be used
     * to attach code to the method.
     *
     * @param u2accessFlags         access flags for the new method.
     * @param methodName            name of the new method.
     * @param methodDescriptor      descriptor of the new method.
     * @param maxCodeFragmentLength maximum length for the code fragment of the
     *                              new method.
     *
     * @return the composer for adding code to the created method.
     */
    public CompactCodeAttributeComposer addMethod(int    u2accessFlags,
                                                  String methodName,
                                                  String methodDescriptor,
                                                  int    maxCodeFragmentLength)
    {
        return addMethod(u2accessFlags,
                         methodName,
                         methodDescriptor,
                         maxCodeFragmentLength,
                         0);
    }


    /**
     * Adds a new method to the edited class. The returned composer can be used
     * to attach code to the method.
     *
     * @param u2accessFlags         access flags for the new method.
     * @param methodName            name of the new method.
     * @param methodDescriptor      descriptor of the new method.
     * @param maxCodeFragmentLength maximum length for the code fragment of the
     *                              new method.
     * @param processingFlags       processing flags for the new method.
     *
     * @return the composer for adding code to the created method.
     */
    public CompactCodeAttributeComposer addMethod(int     u2accessFlags,
                                                  String  methodName,
                                                  String  methodDescriptor,
                                                  int     maxCodeFragmentLength,
                                                  int     processingFlags)
    {
        ProgramMethod method = new ProgramMethod(u2accessFlags,
                                                 constantPoolEditor.addUtf8Constant(methodName),
                                                 constantPoolEditor.addUtf8Constant(methodDescriptor),
                                                 null,
                                                 processingFlags);

        MyCodeComposer composer = new MyCodeComposer(method, maxCodeFragmentLength);
        methodComposers.add(composer);

        return composer;
    }


    /**
     * Adds the given static initializer instructions to the edited class.
     * If the class already contains a static initializer, the new instructions
     * will be appended to the existing initializer.
     *
     * @param instructions                 the instructions to be added.
     * @param mergeIntoExistingInitializer indicates whether the instructions should
     *                                     be added to the existing static initializer
     *                                     (if it exists), or if a new method should
     *                                     be created, which is then called from the
     *                                     existing initializer.
     */
    public void addStaticInitializerInstructions(Instruction[] instructions,
                                                 boolean       mergeIntoExistingInitializer)
    {
        Method method = programClass.findMethod(METHOD_NAME_CLINIT, METHOD_TYPE_CLINIT);

        if (method == null)
        {
            addMethod(ACC_STATIC,
                      METHOD_NAME_CLINIT,
                      METHOD_TYPE_CLINIT,
                      0,
                      instructions,
                      null,
                      new SimpleInstruction(InstructionConstants.OP_RETURN));
        }
        else
        {
            if (!mergeIntoExistingInitializer)
            {
                // Create a new static initializer.
                ProgramMethod newMethod =
                    addMethod(ACC_STATIC,
                              EXTRA_CLINIT_METHOD_PREFIX + staticInitializerCounter++,
                              EXTRA_CLINIT_METHOD_DESCRIPTOR,
                              // Make sure that such methods are not optimized (inlined)
                              // to prevent potential overflow errors during conversion.
                              ProcessingFlags.DONT_OPTIMIZE,
                              instructions,
                              null,
                              new SimpleInstruction(InstructionConstants.OP_RETURN));

                // Call the new initializer from the existing one.
                InstructionSequenceBuilder builder = new InstructionSequenceBuilder(programClass);
                builder.invokestatic(programClass.getName(),
                                      newMethod.getName(programClass),
                                      "()V",
                                      programClass,
                                      newMethod);
                instructions = builder.instructions();
            }
            CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
            ((ProgramMethod) method).attributesAccept(programClass,
                                                      new CodeAttributeEditorResetter(codeAttributeEditor));
            codeAttributeEditor.insertBeforeOffset(0, instructions);
            ((ProgramMethod) method).attributesAccept(programClass,
                                                      codeAttributeEditor);
        }
    }


    /**
     * Adds the given initialization instructions to the edited class.
     *
     * - If the class doesn't contain a constructor yet, it will be created,
     *   and the instructions will be added to this constructor.
     * - If there is a single super-calling constructor, the instructions will
     *   be added at the beginning of it's code attribute.
     * - If there are multiple super-calling constructors, a new private
     *   parameterless helper method will be created, to which the instructions
     *   will be added. An invocation to this new method will be added at the
     *   beginning of the code attribute of all super-calling constructors.
     *
     * @param instructions the instructions to be added.
     */
    public void addInitializerInstructions(Instruction[] instructions)
    {
        Method method = programClass.findMethod(METHOD_NAME_INIT, null);

        if (method == null)
        {
            // First call the super constructor.
            Instruction[] firstInstruction =
            {
                new VariableInstruction(InstructionConstants.OP_ALOAD_0),
                new ConstantInstruction(
                    InstructionConstants.OP_INVOKESPECIAL,
                    constantPoolEditor.addMethodrefConstant(programClass.getSuperName(),
                                                            METHOD_NAME_INIT,
                                                            METHOD_TYPE_INIT,
                                                            null,
                                                            null))
            };

            // End by calling return.
            SimpleInstruction lastInstruction =
                new SimpleInstruction(InstructionConstants.OP_RETURN);

            addMethod(ACC_PUBLIC,
                      METHOD_NAME_INIT,
                      METHOD_TYPE_INIT,
                      0,
                      instructions,
                      firstInstruction,
                      lastInstruction);
        }
        else
        {
            // Find all super-calling constructors.
            Set<Method> constructors =  new HashSet<Method>();
            programClass.methodsAccept(
                new ConstructorMethodFilter(
                new MethodCollector(constructors), null, null));

            if (constructors.size() == 1)
            {
                // There is only one supper-calling constructor.
                // Add the code to this constructor.
                constructors.iterator().next().accept(programClass,
                    new AllAttributeVisitor(
                    new MyInstructionInserter(instructions)));
            }
            else
            {
                // There are multiple super-calling constructors. Add the
                // instructions to a separate, parameterless initialization
                // method, and invoke this method from all super-calling
                // constructors.
                ProgramMethod initMethod = (ProgramMethod) programClass.findMethod(EXTRA_INIT_METHOD_NAME,
                                                                                   EXTRA_INIT_METHOD_DESCRIPTOR);
                if (initMethod == null)
                {
                    // There is no init$ method yet. Create it now, and add the
                    // given instructions to it.
                    initMethod = addMethod(ACC_PRIVATE,
                                           EXTRA_INIT_METHOD_NAME,
                                           EXTRA_INIT_METHOD_DESCRIPTOR,
                                           // Make sure that such methods are not optimized (inlined)
                                           // to prevent potential overflow errors during conversion.
                                           ProcessingFlags.DONT_OPTIMIZE,
                                           instructions,
                                           null,
                                           new SimpleInstruction(InstructionConstants.OP_RETURN));

                    // Insert a call to the new init$ method in all super-calling constructors.
                    InstructionSequenceBuilder builder = new InstructionSequenceBuilder(programClass);
                    builder.aload_0();
                    builder.invokespecial(programClass.getName(),
                                          EXTRA_INIT_METHOD_NAME,
                                          EXTRA_INIT_METHOD_DESCRIPTOR,
                                          programClass,
                                          initMethod);

                    programClass.methodsAccept(
                        new ConstructorMethodFilter(
                        new AllAttributeVisitor(
                        new MyInstructionInserter(instructions)), null, null));
                }
                else
                {
                    // There is already an init$ method. Add the instructions to this method.
                    initMethod.accept(programClass,
                        new AllAttributeVisitor(
                        new MyInstructionInserter(instructions)));
                }
            }
        }
    }


    // Small  utility methods.

    /**
     * Adds a new method to the edited class, with the given instructions array.
     *
     * @param u2accessFlags         acces flags for the new method.
     * @param methodName            name of the new method.
     * @param methodDescriptor      descriptor of the new method.
     * @param processingFlags       processing flags for the new method.
     * @param instructions          the instructions of the new method.
     * @param firstInstructions     extra instructions to add in front of the
     *                              new method.
     * @param lastInstruction       extra instruction to add at the end of the
     *                              new method.
     */
    private ProgramMethod addMethod(int           u2accessFlags,
                                    String        methodName,
                                    String        methodDescriptor,
                                    int           processingFlags,
                                    Instruction[] instructions,
                                    Instruction[] firstInstructions,
                                    Instruction   lastInstruction)
    {
        ProgramMethod method = new ProgramMethod(u2accessFlags,
                                                 constantPoolEditor.addUtf8Constant(methodName),
                                                 constantPoolEditor.addUtf8Constant(methodDescriptor),
                                                 null,
                                                 processingFlags);

        CodeAttribute codeAttribute =
            new CodeAttribute(constantPoolEditor.addUtf8Constant(ClassConstants.ATTR_Code));

        CodeAttributeComposer composer = new CodeAttributeComposer();
        composer.reset();
        composer.beginCodeFragment(0);
        if (firstInstructions != null)
        {
            for (Instruction instruction : firstInstructions)
            {
                composer.appendInstruction(instruction);
            }
        }
        composer.appendInstructions(instructions);
        if (lastInstruction != null)
        {
            composer.appendInstruction(lastInstruction);
        }
        composer.endCodeFragment();
        composer.visitCodeAttribute(programClass, method, codeAttribute);

        new AttributesEditor(programClass, method, false).addAttribute(codeAttribute);

        classEditor.addMethod(method);

        return method;
    }


    // Small utility classes.

    /**
     * This attribute visitor adds the given instructions to the start of all
     * code attributes that it visits.
     */
    private class MyInstructionInserter
    extends       SimplifiedVisitor
    implements    AttributeVisitor
    {
        private final Instruction[] instructions;


        public MyInstructionInserter(Instruction[] instructions)
        {
            this.instructions = instructions;
        }


        // Implementations for AttributeVisitor.

        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
        {
            CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
            ((ProgramMethod) method).attributesAccept(programClass, new CodeAttributeEditorResetter(codeAttributeEditor));
            codeAttributeEditor.insertBeforeOffset(0, instructions);
            codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
        }
    }


    /**
     * This CompactCodeAttributeComposer applies its collected changes to a
     * given method when {@link #finishEditing()} is called.
     */
    private class MyCodeComposer
    extends       CompactCodeAttributeComposer
    {
        private final ProgramMethod method;


        public MyCodeComposer(ProgramMethod method,
                              int           maxCodeFragmentLength)
        {
            super(programClass);
            this.method = method;

            beginCodeFragment(maxCodeFragmentLength);
        }


        public void finishEditing()
        {
            endCodeFragment();

            // Create an empty code attribute.
            CodeAttribute codeAttribute =
                new CodeAttribute(constantPoolEditor.addUtf8Constant(ClassConstants.ATTR_Code));

            // Copy the accumulated instructions into the attribute.
            visitCodeAttribute(programClass, method, codeAttribute);

            // Add the attribute to the method.
            new AttributesEditor(programClass, method, false).addAttribute(codeAttribute);

            // Add the method to the class.
            classEditor.addMethod(method);
        }
    }
}
