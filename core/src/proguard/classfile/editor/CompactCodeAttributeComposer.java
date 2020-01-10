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
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.*;
import proguard.classfile.util.*;

/**
 * This {@link AttributeVisitor} accumulates instructions, exceptions and line numbers,
 * in a compact and fluent style, and then copies them into code attributes
 * that it visits.
 * <p/>
 * The class supports composing
 *   instructions       ({@link #appendInstruction(Instruction)}),
 *   labels             ({@link #createLabel()} and {@link #label(Label)}),
 *   exception handlers ({@link #catch_(Label, Label, String, Clazz)}), and
 *   line numbers       ({@link #line(int)}).
 * <p/>
 * The labels are numeric labels that you can choose freely, for example
 * instruction offsets from existing code that you are copying. You can then
 * refer to them in branches and exception handlers. You can compose the
 * code as a hierarchy of code fragments with their own local labels.
 * <p/>
 * You should provide an estimated maximum size (expressed in number of
 * bytes in the bytecode), so the implementation can efficiently allocate
 * the necessary internal buffers without reallocating them as the code
 * grows.
 * <p/>
 * For example:
 * <pre>
 *     ProgramClass  programClass  = ...
 *     ProgramMethod programMethod = ...
 *     CodeAttribute codeAttribute = ...
 *
 *     // Compose the code.
 *     CompactCodeAttributeComposer composer =
 *         new CompactCodeAttributeComposer(programClass);
 *
 *     final Label TRY_START = composer.createLabel();
 *     final Label TRY_END   = composer.createLabel();
 *     final Label ELSE      = composer.createLabel();
 *
 *     composer
 *         .beginCodeFragment(50)
 *         .label(TRY_START)
 *         .iconst_1()
 *         .iconst_2()
 *         .ificmplt(ELSE)
 *
 *         .iconst_1()
 *         .ireturn()
 *
 *         .label(ELSE)
 *         .iconst_2()
 *         .ireturn()
 *         .label(TRY_END)
 *
 *         .catch_(TRY_START, TRY_END, "java/lang/Exception", null)
 *         .iconst_m1()
 *         .ireturn()
 *         .endCodeFragment();
 *
 *     // Put the code in the given code attribute.
 *     composer.visitCodeAttribute(programClass, programMethod, codeAttribute);
 * </pre>
 * <p/>
 * This class is mostly convenient to compose code programmatically from
 * scratch. To compose code based on existing code, where the instructions
 * are already available, see {@link CompactCodeAttributeComposer}.
 *
 * @author Eric Lafortune
 */
public class CompactCodeAttributeComposer
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    private final ConstantPoolEditor    constantPoolEditor;
    private final CodeAttributeComposer codeAttributeComposer;

    private int labelCount = 0;


    /**
     * Creates a new CompactCodeAttributeComposer that doesn't allow external
     * branch targets or exception handlers and that automatically shrinks
     * instructions.
     * @param targetClass the class to be edited.
     */
    public CompactCodeAttributeComposer(ProgramClass targetClass)
    {
        this(targetClass, false, false, true);
    }


    /**
     * Creates a new CompactCodeAttributeComposer that doesn't allow external
     * branch targets or exception handlers and that automatically shrinks
     * instructions.
     * @param targetClass      the class to be edited.
     * @param programClassPool the program class pool from which new
     *                         constants can be initialized.
     * @param libraryClassPool the library class pool from which new
     *                         constants can be initialized.
     */
    public CompactCodeAttributeComposer(ProgramClass targetClass,
                                        ClassPool    programClassPool,
                                        ClassPool    libraryClassPool)
    {
        this(targetClass, false, false, true, programClassPool, libraryClassPool);
    }


    /**
     * Creates a new CompactCodeAttributeComposer.
     * @param targetClass                    the class to be edited.
     * @param allowExternalBranchTargets     specifies whether branch targets
     *                                       can lie outside the code fragment
     *                                       of the branch instructions.
     * @param allowExternalExceptionHandlers specifies whether exception
     *                                       handlers can lie outside the code
     *                                       fragment in which exceptions are
     *                                       defined.
     * @param shrinkInstructions             specifies whether instructions
     *                                       should automatically be shrunk
     *                                       before being written.
     */
    public CompactCodeAttributeComposer(ProgramClass targetClass,
                                        boolean      allowExternalBranchTargets,
                                        boolean      allowExternalExceptionHandlers,
                                        boolean      shrinkInstructions)
    {
        this(targetClass, allowExternalBranchTargets, allowExternalExceptionHandlers, shrinkInstructions, null, null);
    }


    /**
     * Creates a new CompactCodeAttributeComposer.
     * @param targetClass                    the class to be edited.
     * @param allowExternalBranchTargets     specifies whether branch targets
     *                                       can lie outside the code fragment
     *                                       of the branch instructions.
     * @param allowExternalExceptionHandlers specifies whether exception
     *                                       handlers can lie outside the code
     *                                       fragment in which exceptions are
     *                                       defined.
     * @param shrinkInstructions             specifies whether instructions
     *                                       should automatically be shrunk
     *                                       before being written.
     * @param programClassPool               the program class pool from which new
     *                                       constants can be initialized.
     * @param libraryClassPool               the library class pool from which new
     *                                       constants can be initialized.
     */
    public CompactCodeAttributeComposer(ProgramClass targetClass,
                                        boolean      allowExternalBranchTargets,
                                        boolean      allowExternalExceptionHandlers,
                                        boolean      shrinkInstructions,
                                        ClassPool    programClassPool,
                                        ClassPool    libraryClassPool)
    {
        constantPoolEditor =
            new ConstantPoolEditor(targetClass,
                                   programClassPool,
                                   libraryClassPool);

        codeAttributeComposer =
            new CodeAttributeComposer(allowExternalBranchTargets,
                                      allowExternalExceptionHandlers,
                                      shrinkInstructions);
    }


    /**
     * Returns the target class for which code is generated.
     */
    public ProgramClass getTargetClass()
    {
        return constantPoolEditor.getTargetClass();
    }


    /**
     * Starts a new code definition.
     */
    public CompactCodeAttributeComposer reset()
    {
        codeAttributeComposer.reset();

        labelCount = 0;

        return this;
    }


    /**
     * Starts a new code fragment. Branch instructions that are added are
     * assumed to be relative within such code fragments.
     * @param maximumCodeFragmentLength the maximum length of the code that will
     *                                  be added as part of this fragment (more
     *                                  precisely, the maximum old instruction
     *                                  offset or label that is specified, plus
     *                                  one).
     * @return this instance of CompactCodeAttributeComposer.
     */
    public CompactCodeAttributeComposer beginCodeFragment(int maximumCodeFragmentLength)
    {
        codeAttributeComposer.beginCodeFragment(maximumCodeFragmentLength);

        return this;
    }


    /**
     * Creates a new label that can be specified and used in the code.
     */
    public Label createLabel()
    {
        return new Label(labelCount++);
    }


    /**
     * Appends the given label at the current offset, so branch instructions
     * and switch instructions can jump to it.
     * @param label the branch label.
     * @return this instance of CompactCodeAttributeComposer.
     */
    public CompactCodeAttributeComposer label(Label label)
    {
        codeAttributeComposer.appendLabel(label.offset);

        return this;
    }


    /**
     * Appends the given instruction without defined offsets.
     * @param instructions the instructions to be appended.
     * @return this instance of CompactCodeAttributeComposer.
     */
    public CompactCodeAttributeComposer appendInstructions(Instruction[] instructions)
    {
        codeAttributeComposer.appendInstructions(instructions);

        return this;
    }


    /**
     * Appends the given instruction.
     * @param instruction the instruction to be appended.
     * @return this instance of CompactCodeAttributeComposer.
     */
    public CompactCodeAttributeComposer appendInstruction(Instruction instruction)
    {
        codeAttributeComposer.appendInstruction(instruction);

        return this;
    }


    /**
     * Starts a catch handler.
     * @param startLabel      the start label of the try block.
     * @param endLabel        the end label of the try block.
     * @param catchType       the exception type.
     * @param referencedClass the exception class, if known.
     * @return this instance of CompactCodeAttributeComposer.
     */
    public CompactCodeAttributeComposer catch_(Label  startLabel,
                                               Label  endLabel,
                                               String catchType,
                                               Clazz  referencedClass)
    {
        // Create and append a label for the current offset.
        Label handlerLabel = createLabel();

        codeAttributeComposer.appendLabel(handlerLabel.offset);

        // Create and append the exception.
        int u2catchType =
            constantPoolEditor.addClassConstant(catchType, referencedClass);

        codeAttributeComposer.appendException(new ExceptionInfo(startLabel.offset,
                                                                endLabel.offset,
                                                                handlerLabel.offset,
                                                                u2catchType));

        return this;
    }


    /**
     * Adds a source line number for the current position.
     * @param lineNumber the line number from the source code.
     * @return this instance of CompactCodeAttributeComposer.
     */
    public CompactCodeAttributeComposer line(int lineNumber)
    {
        // Create and append a label for the current offset.
        Label currentLabel = createLabel();

        codeAttributeComposer.appendLabel(currentLabel.offset);

        // Create and append the line number.
        codeAttributeComposer.appendLineNumber(new LineNumberInfo(currentLabel.offset,
                                                                  lineNumber));

        return this;
    }


    /**
     * Wraps up the current code fragment, continuing with the previous one on
     * the stack.
     * @return this instance of CompactCodeAttributeComposer.
     */
    public CompactCodeAttributeComposer endCodeFragment()
    {
        codeAttributeComposer.endCodeFragment();

        return this;
    }


    // Methods corresponding to the bytecode opcodes.

    public CompactCodeAttributeComposer nop()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_NOP));
    }

    public CompactCodeAttributeComposer aconst_null()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ACONST_NULL));
    }

    public CompactCodeAttributeComposer iconst(int constant)
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ICONST_0, constant));
    }

    public CompactCodeAttributeComposer iconst_m1()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ICONST_M1));
    }

    public CompactCodeAttributeComposer iconst_0()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ICONST_0));
    }

    public CompactCodeAttributeComposer iconst_1()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ICONST_1));
    }

    public CompactCodeAttributeComposer iconst_2()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ICONST_2));
    }

    public CompactCodeAttributeComposer iconst_3()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ICONST_3));
    }

    public CompactCodeAttributeComposer iconst_4()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ICONST_4));
    }

    public CompactCodeAttributeComposer iconst_5()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ICONST_5));
    }

    public CompactCodeAttributeComposer lconst(int constant)
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LCONST_0, constant));
    }

    public CompactCodeAttributeComposer lconst_0()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LCONST_0));
    }

    public CompactCodeAttributeComposer lconst_1()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LCONST_1));
    }

    public CompactCodeAttributeComposer fconst(int constant)
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FCONST_0, constant));
    }

    public CompactCodeAttributeComposer fconst_0()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FCONST_0));
    }

    public CompactCodeAttributeComposer fconst_1()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FCONST_1));
    }

    public CompactCodeAttributeComposer fconst_2()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FCONST_2));
    }

    public CompactCodeAttributeComposer dconst(int constant)
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DCONST_0, constant));
    }

    public CompactCodeAttributeComposer dconst_0()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DCONST_0));
    }

    public CompactCodeAttributeComposer dconst_1()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DCONST_1));
    }

    public CompactCodeAttributeComposer bipush(int constant)
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_BIPUSH, constant));
    }

    public CompactCodeAttributeComposer sipush(int constant)
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_SIPUSH, constant));
    }

    public CompactCodeAttributeComposer ldc(int value)
    {
        return ldc_(constantPoolEditor.addIntegerConstant(value));
    }

    public CompactCodeAttributeComposer ldc(float value)
    {
        return ldc_(constantPoolEditor.addFloatConstant(value));
    }

    public CompactCodeAttributeComposer ldc(String string)
    {
        return ldc(string, null, null);
    }

    public CompactCodeAttributeComposer ldc(Object primitiveArray)
    {
        return ldc_(constantPoolEditor.addPrimitiveArrayConstant(primitiveArray));
    }

    public CompactCodeAttributeComposer ldc(Clazz  clazz,
                                            Member member)
    {
        return ldc(member.getName(clazz), clazz, member);
    }

    public CompactCodeAttributeComposer ldc(String string,
                                            Clazz  referencedClass,
                                            Member referencedMember)
    {
        return ldc_(constantPoolEditor.addStringConstant(string, referencedClass, referencedMember));
    }

    public CompactCodeAttributeComposer ldc(Clazz clazz)
    {
        return ldc(clazz.getName(), clazz);
    }

    public CompactCodeAttributeComposer ldc(String className,
                                            Clazz  referencedClass)
    {
        return ldc_(constantPoolEditor.addClassConstant(className, referencedClass));
    }

    public CompactCodeAttributeComposer ldc_(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_LDC, constantIndex));
    }

    public CompactCodeAttributeComposer ldc_w(int value)
    {
        return ldc_w_(constantPoolEditor.addIntegerConstant(value));
    }

    public CompactCodeAttributeComposer ldc_w(float value)
    {
        return ldc_w_(constantPoolEditor.addFloatConstant(value));
    }

    public CompactCodeAttributeComposer ldc_w(String string)
    {
        return ldc_w(string, null, null);
    }

    public CompactCodeAttributeComposer ldc_w(String string, Clazz referencedClass, Method referencedMember)
    {
        return ldc_w_(constantPoolEditor.addStringConstant(string, referencedClass, referencedMember));
    }

    public CompactCodeAttributeComposer ldc_w(String className, Clazz referencedClass)
    {
        return ldc_w_(constantPoolEditor.addClassConstant(className, referencedClass));
    }

    public CompactCodeAttributeComposer ldc_w_(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_LDC_W, constantIndex));
    }

    public CompactCodeAttributeComposer ldc2_w(long value)
    {
        return ldc2_w(constantPoolEditor.addLongConstant(value));
    }

    public CompactCodeAttributeComposer ldc2_w(double value)
    {
        return ldc2_w(constantPoolEditor.addDoubleConstant(value));
    }

    public CompactCodeAttributeComposer ldc2_w(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_LDC2_W, constantIndex));
    }

    public CompactCodeAttributeComposer iload(int variableIndex)
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ILOAD, variableIndex));
    }

    public CompactCodeAttributeComposer lload(int variableIndex)
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_LLOAD, variableIndex));
    }

    public CompactCodeAttributeComposer fload(int variableIndex)
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_FLOAD, variableIndex));
    }

    public CompactCodeAttributeComposer dload(int variableIndex)
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_DLOAD, variableIndex));
    }

    public CompactCodeAttributeComposer aload(int variableIndex)
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ALOAD, variableIndex));
    }

    public CompactCodeAttributeComposer iload_0()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ILOAD_0));
    }

    public CompactCodeAttributeComposer iload_1()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ILOAD_1));
    }

    public CompactCodeAttributeComposer iload_2()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ILOAD_2));
    }

    public CompactCodeAttributeComposer iload_3()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ILOAD_3));
    }

    public CompactCodeAttributeComposer lload_0()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_LLOAD_0));
    }

    public CompactCodeAttributeComposer lload_1()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_LLOAD_1));
    }

    public CompactCodeAttributeComposer lload_2()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_LLOAD_2));
    }

    public CompactCodeAttributeComposer lload_3()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_LLOAD_3));
    }

    public CompactCodeAttributeComposer fload_0()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_FLOAD_0));
    }

    public CompactCodeAttributeComposer fload_1()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_FLOAD_1));
    }

    public CompactCodeAttributeComposer fload_2()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_FLOAD_2));
    }

    public CompactCodeAttributeComposer fload_3()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_FLOAD_3));
    }

    public CompactCodeAttributeComposer dload_0()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_DLOAD_0));
    }

    public CompactCodeAttributeComposer dload_1()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_DLOAD_1));
    }

    public CompactCodeAttributeComposer dload_2()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_DLOAD_2));
    }

    public CompactCodeAttributeComposer dload_3()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_DLOAD_3));
    }

    public CompactCodeAttributeComposer aload_0()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ALOAD_0));
    }

    public CompactCodeAttributeComposer aload_1()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ALOAD_1));
    }

    public CompactCodeAttributeComposer aload_2()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ALOAD_2));
    }

    public CompactCodeAttributeComposer aload_3()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ALOAD_3));
    }

    public CompactCodeAttributeComposer iaload()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_IALOAD));
    }

    public CompactCodeAttributeComposer laload()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LALOAD));
    }

    public CompactCodeAttributeComposer faload()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FALOAD));
    }

    public CompactCodeAttributeComposer daload()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DALOAD));
    }

    public CompactCodeAttributeComposer aaload()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_AALOAD));
    }

    public CompactCodeAttributeComposer baload()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_BALOAD));
    }

    public CompactCodeAttributeComposer caload()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_CALOAD));
    }

    public CompactCodeAttributeComposer saload()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_SALOAD));
    }

    public CompactCodeAttributeComposer istore(int variableIndex)
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ISTORE, variableIndex));
    }

    public CompactCodeAttributeComposer lstore(int variableIndex)
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_LSTORE, variableIndex));
    }

    public CompactCodeAttributeComposer fstore(int variableIndex)
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_FSTORE, variableIndex));
    }

    public CompactCodeAttributeComposer dstore(int variableIndex)
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_DSTORE, variableIndex));
    }

    public CompactCodeAttributeComposer astore(int variableIndex)
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ASTORE, variableIndex));
    }

    public CompactCodeAttributeComposer istore_0()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ISTORE_0));
    }

    public CompactCodeAttributeComposer istore_1()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ISTORE_1));
    }

    public CompactCodeAttributeComposer istore_2()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ISTORE_2));
    }

    public CompactCodeAttributeComposer istore_3()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ISTORE_3));
    }

    public CompactCodeAttributeComposer lstore_0()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_LSTORE_0));
    }

    public CompactCodeAttributeComposer lstore_1()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_LSTORE_1));
    }

    public CompactCodeAttributeComposer lstore_2()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_LSTORE_2));
    }

    public CompactCodeAttributeComposer lstore_3()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_LSTORE_3));
    }

    public CompactCodeAttributeComposer fstore_0()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_FSTORE_0));
    }

    public CompactCodeAttributeComposer fstore_1()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_FSTORE_1));
    }

    public CompactCodeAttributeComposer fstore_2()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_FSTORE_2));
    }

    public CompactCodeAttributeComposer fstore_3()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_FSTORE_3));
    }

    public CompactCodeAttributeComposer dstore_0()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_DSTORE_0));
    }

    public CompactCodeAttributeComposer dstore_1()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_DSTORE_1));
    }

    public CompactCodeAttributeComposer dstore_2()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_DSTORE_2));
    }

    public CompactCodeAttributeComposer dstore_3()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_DSTORE_3));
    }

    public CompactCodeAttributeComposer astore_0()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ASTORE_0));
    }

    public CompactCodeAttributeComposer astore_1()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ASTORE_1));
    }

    public CompactCodeAttributeComposer astore_2()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ASTORE_2));
    }

    public CompactCodeAttributeComposer astore_3()
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_ASTORE_3));
    }

    public CompactCodeAttributeComposer iastore()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_IASTORE));
    }

    public CompactCodeAttributeComposer lastore()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LASTORE));
    }

    public CompactCodeAttributeComposer fastore()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FASTORE));
    }

    public CompactCodeAttributeComposer dastore()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DASTORE));
    }

    public CompactCodeAttributeComposer aastore()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_AASTORE));
    }

    public CompactCodeAttributeComposer bastore()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_BASTORE));
    }

    public CompactCodeAttributeComposer castore()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_CASTORE));
    }

    public CompactCodeAttributeComposer sastore()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_SASTORE));
    }

    public CompactCodeAttributeComposer pop()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_POP));
    }

    public CompactCodeAttributeComposer pop2()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_POP2));
    }

    public CompactCodeAttributeComposer dup()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DUP));
    }

    public CompactCodeAttributeComposer dup_x1()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DUP_X1));
    }

    public CompactCodeAttributeComposer dup_x2()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DUP_X2));
    }

    public CompactCodeAttributeComposer dup2()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DUP2));
    }

    public CompactCodeAttributeComposer dup2_x1()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DUP2_X1));
    }

    public CompactCodeAttributeComposer dup2_x2()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DUP2_X2));
    }

    public CompactCodeAttributeComposer swap()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_SWAP));
    }

    public CompactCodeAttributeComposer iadd()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_IADD));
    }

    public CompactCodeAttributeComposer ladd()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LADD));
    }

    public CompactCodeAttributeComposer fadd()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FADD));
    }

    public CompactCodeAttributeComposer dadd()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DADD));
    }

    public CompactCodeAttributeComposer isub()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ISUB));
    }

    public CompactCodeAttributeComposer lsub()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LSUB));
    }

    public CompactCodeAttributeComposer fsub()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FSUB));
    }

    public CompactCodeAttributeComposer dsub()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DSUB));
    }

    public CompactCodeAttributeComposer imul()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_IMUL));
    }

    public CompactCodeAttributeComposer lmul()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LMUL));
    }

    public CompactCodeAttributeComposer fmul()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FMUL));
    }

    public CompactCodeAttributeComposer dmul()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DMUL));
    }

    public CompactCodeAttributeComposer idiv()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_IDIV));
    }

    public CompactCodeAttributeComposer ldiv()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LDIV));
    }

    public CompactCodeAttributeComposer fdiv()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FDIV));
    }

    public CompactCodeAttributeComposer ddiv()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DDIV));
    }

    public CompactCodeAttributeComposer irem()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_IREM));
    }

    public CompactCodeAttributeComposer lrem()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LREM));
    }

    public CompactCodeAttributeComposer frem()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FREM));
    }

    public CompactCodeAttributeComposer drem()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DREM));
    }

    public CompactCodeAttributeComposer ineg()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_INEG));
    }

    public CompactCodeAttributeComposer lneg()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LNEG));
    }

    public CompactCodeAttributeComposer fneg()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FNEG));
    }

    public CompactCodeAttributeComposer dneg()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DNEG));
    }

    public CompactCodeAttributeComposer ishl()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ISHL));
    }

    public CompactCodeAttributeComposer lshl()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LSHL));
    }

    public CompactCodeAttributeComposer ishr()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ISHR));
    }

    public CompactCodeAttributeComposer lshr()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LSHR));
    }

    public CompactCodeAttributeComposer iushr()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_IUSHR));
    }

    public CompactCodeAttributeComposer lushr()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LUSHR));
    }

    public CompactCodeAttributeComposer iand()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_IAND));
    }

    public CompactCodeAttributeComposer land()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LAND));
    }

    public CompactCodeAttributeComposer ior()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_IOR));
    }

    public CompactCodeAttributeComposer lor()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LOR));
    }

    public CompactCodeAttributeComposer ixor()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_IXOR));
    }

    public CompactCodeAttributeComposer lxor()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LXOR));
    }

    public CompactCodeAttributeComposer iinc(int variableIndex,
                                               int constant)
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_IINC, variableIndex, constant));
    }

    public CompactCodeAttributeComposer i2l()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_I2L));
    }

    public CompactCodeAttributeComposer i2f()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_I2F));
    }

    public CompactCodeAttributeComposer i2d()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_I2D));
    }

    public CompactCodeAttributeComposer l2i()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_L2I));
    }

    public CompactCodeAttributeComposer l2f()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_L2F));
    }

    public CompactCodeAttributeComposer l2d()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_L2D));
    }

    public CompactCodeAttributeComposer f2i()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_F2I));
    }

    public CompactCodeAttributeComposer f2l()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_F2L));
    }

    public CompactCodeAttributeComposer f2d()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_F2D));
    }

    public CompactCodeAttributeComposer d2i()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_D2I));
    }

    public CompactCodeAttributeComposer d2l()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_D2L));
    }

    public CompactCodeAttributeComposer d2f()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_D2F));
    }

    public CompactCodeAttributeComposer i2b()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_I2B));
    }

    public CompactCodeAttributeComposer i2c()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_I2C));
    }

    public CompactCodeAttributeComposer i2s()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_I2S));
    }

    public CompactCodeAttributeComposer lcmp()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LCMP));
    }

    public CompactCodeAttributeComposer fcmpl()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FCMPL));
    }

    public CompactCodeAttributeComposer fcmpg()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FCMPG));
    }

    public CompactCodeAttributeComposer dcmpl()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DCMPL));
    }

    public CompactCodeAttributeComposer dcmpg()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DCMPG));
    }

    public CompactCodeAttributeComposer ifeq(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFEQ, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ifne(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFNE, branchLabel.offset));
    }

    public CompactCodeAttributeComposer iflt(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFLT, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ifge(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFGE, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ifgt(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFGT, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ifle(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFLE, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ificmpeq(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFICMPEQ, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ificmpne(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFICMPNE, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ificmplt(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFICMPLT, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ificmpge(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFICMPGE, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ificmpgt(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFICMPGT, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ificmple(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFICMPLE, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ifacmpeq(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFACMPEQ, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ifacmpne(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFACMPNE, branchLabel.offset));
    }

    public CompactCodeAttributeComposer goto_(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_GOTO, branchLabel.offset));
    }

    public CompactCodeAttributeComposer jsr(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_JSR, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ret(int variableIndex)
    {
        return appendInstruction(new VariableInstruction(Instruction.OP_RET, variableIndex));
    }

    public CompactCodeAttributeComposer tableswitch(Label   defaultLabel,
                                                    int     lowCase,
                                                    int     highCase,
                                                    Label[] jumpLabels)
    {
        return appendInstruction(new TableSwitchInstruction(Instruction.OP_TABLESWITCH,
                                                            defaultLabel.offset,
                                                            lowCase,
                                                            highCase,
                                                            offsets(jumpLabels)));
    }

    public CompactCodeAttributeComposer lookupswitch(Label   defaultLabel,
                                                     int[]   cases,
                                                     Label[] jumpLabels)
    {
        return appendInstruction(new LookUpSwitchInstruction(Instruction.OP_LOOKUPSWITCH,
                                                             defaultLabel.offset,
                                                             cases,
                                                             offsets(jumpLabels)));
    }

    public CompactCodeAttributeComposer ireturn()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_IRETURN));
    }

    public CompactCodeAttributeComposer lreturn()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_LRETURN));
    }

    public CompactCodeAttributeComposer freturn()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_FRETURN));
    }

    public CompactCodeAttributeComposer dreturn()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_DRETURN));
    }

    public CompactCodeAttributeComposer areturn()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ARETURN));
    }

    public CompactCodeAttributeComposer return_()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_RETURN));
    }

    public CompactCodeAttributeComposer getstatic(Clazz clazz,
                                                  Field field)
    {
        return getstatic(clazz.getName(),
                         field.getName(clazz),
                         field.getDescriptor(clazz),
                         clazz,
                         field);
    }

    public CompactCodeAttributeComposer getstatic(String className,
                                                  String name,
                                                  String descriptor)
    {
        return getstatic(className, name, descriptor, null, null);
    }

    public CompactCodeAttributeComposer getstatic(String className,
                                                  String name,
                                                  String descriptor,
                                                  Clazz  referencedClass,
                                                  Field  referencedField)
    {
        return getstatic(constantPoolEditor.addFieldrefConstant(className,
                                                                name,
                                                                descriptor,
                                                                referencedClass,
                                                                referencedField));
    }

    public CompactCodeAttributeComposer getstatic(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_GETSTATIC, constantIndex));
    }

    public CompactCodeAttributeComposer putstatic(Clazz referencedClass,
                                                  Field referencedField)
    {
        return putstatic(referencedClass.getName(),
                         referencedField.getName(referencedClass),
                         referencedField.getDescriptor(referencedClass),
                         referencedClass,
                         referencedField);
    }

    public CompactCodeAttributeComposer putstatic(String className,
                                                  String name,
                                                  String descriptor)
    {
        return putstatic(className, name, descriptor, null, null);
    }

    public CompactCodeAttributeComposer putstatic(String className,
                                                  String name,
                                                  String descriptor,
                                                  Clazz  referencedClass,
                                                  Field referencedField)
    {
        return putstatic(constantPoolEditor.addFieldrefConstant(className,
                                                                name,
                                                                descriptor,
                                                                referencedClass,
                                                                referencedField));
    }

    public CompactCodeAttributeComposer putstatic(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_PUTSTATIC, constantIndex));
    }

    public CompactCodeAttributeComposer getfield(Clazz clazz,
                                                 Field field)
    {
        return getfield(clazz.getName(),
                        field.getName(clazz),
                        field.getDescriptor(clazz),
                        clazz,
                        field);
    }

    public CompactCodeAttributeComposer getfield(String className,
                                                 String name,
                                                 String descriptor)
    {
        return getfield(className, name, descriptor, null, null);
    }

    public CompactCodeAttributeComposer getfield(String className,
                                                 String name,
                                                 String descriptor,
                                                 Clazz  referencedClass,
                                                 Field  referencedField)
    {
        return getfield(constantPoolEditor.addFieldrefConstant(className,
                                                               name,
                                                               descriptor,
                                                               referencedClass,
                                                               referencedField));
    }

    public CompactCodeAttributeComposer getfield(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_GETFIELD, constantIndex));
    }

    public CompactCodeAttributeComposer putfield(Clazz clazz,
                                                 Field field)
    {
        return putfield(clazz.getName(),
                        field.getName(clazz),
                        field.getDescriptor(clazz),
                        clazz,
                        field);
    }

    public CompactCodeAttributeComposer putfield(String className,
                                                 String name,
                                                 String descriptor)
    {
        return putfield(className, name, descriptor, null, null);
    }

    public CompactCodeAttributeComposer putfield(String className,
                                                 String name,
                                                 String descriptor,
                                                 Clazz  referencedClass,
                                                 Field  referencedField)
    {
        return putfield(constantPoolEditor.addFieldrefConstant(className,
                                                               name,
                                                               descriptor,
                                                               referencedClass,
                                                               referencedField));
    }

    public CompactCodeAttributeComposer putfield(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_PUTFIELD, constantIndex));
    }

    public CompactCodeAttributeComposer invokevirtual(Clazz  clazz,
                                                      Method method)
    {
        return invokevirtual(clazz.getName(),
                             method.getName(clazz),
                             method.getDescriptor(clazz),
                             clazz,
                             method);
    }

    public CompactCodeAttributeComposer invokevirtual(String className,
                                                      String name,
                                                      String descriptor)
    {
        return invokevirtual(className, name, descriptor, null, null);
    }

    public CompactCodeAttributeComposer invokevirtual(String className,
                                                      String name,
                                                      String descriptor,
                                                      Clazz  referencedClass,
                                                      Method referencedMethod)
    {
        return invokevirtual(constantPoolEditor.addMethodrefConstant(className,
                                                                     name,
                                                                     descriptor,
                                                                     referencedClass,
                                                                     referencedMethod));
    }

    public CompactCodeAttributeComposer invokevirtual(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, constantIndex));
    }

    public CompactCodeAttributeComposer invokespecial(Clazz  clazz,
                                                      Method method)
    {
        return invokespecial(clazz.getName(),
                             method.getName(clazz),
                             method.getDescriptor(clazz),
                             clazz,
                             method);
    }

    public CompactCodeAttributeComposer invokespecial(String className,
                                                      String name,
                                                      String descriptor)
    {
        return invokespecial(className, name, descriptor, null, null);
    }

    public CompactCodeAttributeComposer invokespecial(String className,
                                                      String name,
                                                      String descriptor,
                                                      Clazz  referencedClass,
                                                      Method referencedMethod)
    {
        return invokespecial(constantPoolEditor.addMethodrefConstant(className,
                                                                     name,
                                                                     descriptor,
                                                                     referencedClass,
                                                                     referencedMethod));
    }

    public CompactCodeAttributeComposer invokespecial(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex));
    }

    public CompactCodeAttributeComposer invokestatic(Clazz  clazz,
                                                     Method method)
    {
        return invokestatic(clazz.getName(),
                            method.getName(clazz),
                            method.getDescriptor(clazz),
                            clazz,
                            method);
    }

    public CompactCodeAttributeComposer invokestatic(String className,
                                                     String name,
                                                     String descriptor)
    {
        return invokestatic(className, name, descriptor, null, null);
    }

    public CompactCodeAttributeComposer invokestatic(String className,
                                                     String name,
                                                     String descriptor,
                                                     Clazz  referencedClass,
                                                     Method referencedMethod)
    {
        return invokestatic(constantPoolEditor.addMethodrefConstant(className,
                                                                    name,
                                                                    descriptor,
                                                                    referencedClass,
                                                                    referencedMethod));
    }

    public CompactCodeAttributeComposer invokestatic_interface(Clazz  clazz,
                                                               Method method)
    {
        return invokestatic_interface(clazz.getName(),
                                      method.getName(clazz),
                                      method.getDescriptor(clazz),
                                      clazz,
                                      method);
    }

    public CompactCodeAttributeComposer invokestatic_interface(String className,
                                                               String name,
                                                               String descriptor)
    {
        return invokestatic_interface(className, name, descriptor, null, null);
    }

    public CompactCodeAttributeComposer invokestatic_interface(String className,
                                                               String name,
                                                               String descriptor,
                                                               Clazz  referencedClass,
                                                               Method referencedMethod)
    {
        return invokestatic(constantPoolEditor.addInterfaceMethodrefConstant(className,
                                                                             name,
                                                                             descriptor,
                                                                             referencedClass,
                                                                             referencedMethod));
    }

    public CompactCodeAttributeComposer invokestatic(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_INVOKESTATIC, constantIndex));
    }

    public CompactCodeAttributeComposer invokeinterface(Clazz  clazz,
                                                        Method method)
    {
        return invokeinterface(clazz.getName(),
                               method.getName(clazz),
                               method.getDescriptor(clazz),
                               clazz,
                               method);
    }

    public CompactCodeAttributeComposer invokeinterface(String className,
                                                        String name,
                                                        String descriptor)
    {
        return invokeinterface(className, name, descriptor, null, null);
    }

    public CompactCodeAttributeComposer invokeinterface(String className,
                                                        String name,
                                                        String descriptor,
                                                        Clazz  referencedClass,
                                                        Method referencedMethod)
    {
        int invokeinterfaceConstant =
            (ClassUtil.internalMethodParameterSize(descriptor, false)) << 8;

        return invokeinterface(constantPoolEditor.addInterfaceMethodrefConstant(className,
                                                                                name,
                                                                                descriptor,
                                                                                referencedClass,
                                                                                referencedMethod),
                               invokeinterfaceConstant);
    }

    public CompactCodeAttributeComposer invokeinterface(int constantIndex,
                                                        int constant)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, constantIndex, constant));
    }

    public CompactCodeAttributeComposer invokedynamic(int     bootStrapMethodIndex,
                                                      String  name,
                                                      String  descriptor,
                                                      Clazz[] referencedClasses)
    {
        return invokedynamic(constantPoolEditor.addInvokeDynamicConstant(bootStrapMethodIndex,
                                                                         name,
                                                                         descriptor,
                                                                         referencedClasses));
    }

    public CompactCodeAttributeComposer invokedynamic(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, constantIndex));
    }

    public CompactCodeAttributeComposer new_(Clazz clazz)
    {
        return new_(clazz.getName(), clazz);
    }

    public CompactCodeAttributeComposer new_(String className)
    {
        return new_(className, null);
    }

    public CompactCodeAttributeComposer new_(String className, Clazz referencedClass)
    {
        return new_(constantPoolEditor.addClassConstant(className, referencedClass));
    }

    public CompactCodeAttributeComposer new_(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_NEW, constantIndex));
    }

    public CompactCodeAttributeComposer newarray(int constant)
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_NEWARRAY, constant));
    }

    public CompactCodeAttributeComposer anewarray(String className, Clazz referencedClass)
    {
        return anewarray(constantPoolEditor.addClassConstant(className, referencedClass));
    }

    public CompactCodeAttributeComposer anewarray(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_ANEWARRAY, constantIndex));
    }

    public CompactCodeAttributeComposer arraylength()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ARRAYLENGTH));
    }

    public CompactCodeAttributeComposer athrow()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_ATHROW));
    }

    public CompactCodeAttributeComposer checkcast(String className)
    {
        return checkcast(className, null);
    }

    public CompactCodeAttributeComposer checkcast(String className, Clazz referencedClass)
    {
        return checkcast(constantPoolEditor.addClassConstant(className, referencedClass));
    }

    public CompactCodeAttributeComposer checkcast(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_CHECKCAST, constantIndex));
    }

    public CompactCodeAttributeComposer instanceof_(String className, Clazz referencedClass)
    {
        return instanceof_(constantPoolEditor.addClassConstant(className, referencedClass));
    }

    public CompactCodeAttributeComposer instanceof_(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_INSTANCEOF, constantIndex));
    }

    public CompactCodeAttributeComposer monitorenter()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_MONITORENTER));
    }

    public CompactCodeAttributeComposer monitorexit()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_MONITOREXIT));
    }

    public CompactCodeAttributeComposer wide()
    {
        return appendInstruction(new SimpleInstruction(Instruction.OP_WIDE));
    }

    public CompactCodeAttributeComposer multianewarray(String className, Clazz referencedClass)
    {
        return multianewarray(constantPoolEditor.addClassConstant(className, referencedClass));
    }

    public CompactCodeAttributeComposer multianewarray(int constantIndex)
    {
        return appendInstruction(new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, constantIndex));
    }

    public CompactCodeAttributeComposer ifnull(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFNULL, branchLabel.offset));
    }

    public CompactCodeAttributeComposer ifnonnull(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_IFNONNULL, branchLabel.offset));
    }

    public CompactCodeAttributeComposer goto_w(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_GOTO_W, branchLabel.offset));
    }

    public CompactCodeAttributeComposer jsr_w(Label branchLabel)
    {
        return appendInstruction(new BranchInstruction(Instruction.OP_JSR_W, branchLabel.offset));
    }


    // Additional convenience methods.

    /**
     * Pushes the given primitive value on the stack.
     *
     * Operand stack:
     * ... -> ..., value
     *
     * @param primitive the primitive value to be pushed - should never be null.
     * @param internalType      the internal type of the primitive ('Z','B','I',...)
     */
    public CompactCodeAttributeComposer pushPrimitive(Object primitive,
                                                      char   internalType)
    {
        switch (internalType)
        {
            case TypeConstants.BOOLEAN: return ((Boolean)primitive).booleanValue() ? iconst_1() : iconst_0();
            case TypeConstants.BYTE:    return bipush((Byte)primitive);
            case TypeConstants.CHAR:    return ldc(((Character)primitive).charValue());
            case TypeConstants.SHORT:   return sipush((Short)primitive);
            case TypeConstants.INT:     return ldc(((Integer)primitive).intValue());
            case TypeConstants.LONG:    return ldc2_w((Long)primitive);
            case TypeConstants.FLOAT:   return ldc(((Float)primitive).floatValue());
            case TypeConstants.DOUBLE:  return ldc2_w((Double)primitive);
            default: throw new IllegalArgumentException(primitive.toString());
        }
    }


    /**
     * Pushes the given primitive int on the stack in the most efficient way
     * (as an iconst, bipush, sipush, or ldc instruction).
     *
     * @param value the int value to be pushed.
     */
    public CompactCodeAttributeComposer pushInt(int value)
    {
        return
            value >= -1 &&
            value <= 5            ? iconst(value) :
            value == (byte)value  ? bipush(value) :
            value == (short)value ? sipush(value) :
                                    ldc(value);
    }


    /**
     * Pushes the given primitive float on the stack in the most efficient way
     * (as an fconst or ldc instruction).
     *
     * @param value the int value to be pushed.
     */
    public CompactCodeAttributeComposer pushFloat(float value)
    {
        return
            value == 0f ||
            value == 1f ? fconst((int)value) :
                          ldc(value);
    }


    /**
     * Pushes the given primitive long on the stack in the most efficient way
     * (as an lconst or ldc instruction).
     *
     * @param value the int value to be pushed.
     */
    public CompactCodeAttributeComposer pushLong(long value)
    {
        return
            value == 0L ||
            value == 1L ? lconst((int)value) :
                          ldc2_w(value);
    }


    /**
     * Pushes the given primitive double on the stack in the most efficient way
     * (as a dconst or ldc instruction).
     *
     * @param value the int value to be pushed.
     */
    public CompactCodeAttributeComposer pushDouble(double value)
    {
        return
            value == 0. ||
            value == 1. ? dconst((int)value) :
                          ldc2_w(value);
    }


    /**
     * Pushes a new array on the stack.
     *
     * Operand stack:
     * ... -> ..., array
     *
     * @param elementTypeOrClassName the array element type (or class name in case of objects).
     * @param size                   the size of the array to be created.
     */
    public CompactCodeAttributeComposer pushNewArray(String elementTypeOrClassName,
                                                     int    size)
    {
        // Create new array.
        pushInt(size);

        return ClassUtil.isInternalPrimitiveType(elementTypeOrClassName) ?
            newarray(InstructionUtil.arrayTypeFromInternalType(elementTypeOrClassName.charAt(0))) :
            anewarray(elementTypeOrClassName, null);
    }


    /**
     * Loads the given variable onto the stack.
     *
     * Operand stack:
     * ... -> ..., value
     *
     * @param variableIndex the index of the variable to be loaded.
     * @param internalType  the type of the variable to be loaded.
     */
    public CompactCodeAttributeComposer load(int    variableIndex,
                                             String internalType)
    {
        return load(variableIndex, internalType.charAt(0));
    }


    /**
     * Loads the given variable of primitive type onto the stack.
     *
     * Operand stack:
     * ... -> ..., value
     *
     * @param variableIndex the index of the variable to be loaded.
     * @param internalType  the primitive type of the variable to be loaded.
     */
    public CompactCodeAttributeComposer load(int  variableIndex,
                                             char internalType)
    {
        switch (internalType)
        {
            case TypeConstants.BOOLEAN:
            case TypeConstants.BYTE:
            case TypeConstants.CHAR:
            case TypeConstants.SHORT:
            case TypeConstants.INT:    return iload(variableIndex);
            case TypeConstants.LONG:   return lload(variableIndex);
            case TypeConstants.FLOAT:  return fload(variableIndex);
            case TypeConstants.DOUBLE: return dload(variableIndex);
            default:          return aload(variableIndex);
        }
    }


    /**
     * Stores the value on top of the stack in the variable with given index.
     *
     * Operand stsack:
     * ..., value -> ...
     *
     * @param variableIndex the index of the variable where to store the
     *                      value.
     * @param internalType  the type of the value to be stored.
     */
    public CompactCodeAttributeComposer store(int    variableIndex,
                                              String internalType)
    {
        return store(variableIndex, internalType.charAt(0));
    }


    /**
     * Stores the primitve value on top of the stack in the variable with given
     * index.
     *
     * Operand stack:
     * ..., value -> ...
     *
     * @param variableIndex the index of the variable where to store the
     *                      value.
     * @param internalType  the primitive type of the value to be stored.
     */
    public CompactCodeAttributeComposer store(int  variableIndex,
                                              char internalType)
    {
        switch (internalType)
        {
            case TypeConstants.BOOLEAN:
            case TypeConstants.BYTE:
            case TypeConstants.CHAR:
            case TypeConstants.SHORT:
            case TypeConstants.INT:    return istore(variableIndex);
            case TypeConstants.LONG:   return lstore(variableIndex);
            case TypeConstants.FLOAT:  return fstore(variableIndex);
            case TypeConstants.DOUBLE: return dstore(variableIndex);
            default:          return astore(variableIndex);
        }
    }


    /**
     * Stores an element to an array.
     *
     * Operand stack:
     * ..., array, index, value -> ...
     *
     * @param elementType the type of the value to be stored.
     */
    public CompactCodeAttributeComposer storeToArray(String elementType)
    {
        switch (elementType.charAt(0))
        {
            case TypeConstants.BOOLEAN:
            case TypeConstants.BYTE:   return bastore();
            case TypeConstants.CHAR:   return castore();
            case TypeConstants.SHORT:  return sastore();
            case TypeConstants.INT:    return iastore();
            case TypeConstants.LONG:   return lastore();
            case TypeConstants.FLOAT:  return fastore();
            case TypeConstants.DOUBLE: return dastore();
            default:          return aastore();
        }
    }


    /**
     * Appends the proper return statement for the given internal type.
     *
     * @param internalType the return type.
     */
    public CompactCodeAttributeComposer return_(String internalType)
    {
        switch (internalType.charAt(0))
        {
            case TypeConstants.BOOLEAN:
            case TypeConstants.BYTE:
            case TypeConstants.CHAR:
            case TypeConstants.SHORT:
            case TypeConstants.INT:    return ireturn();
            case TypeConstants.LONG:   return lreturn();
            case TypeConstants.FLOAT:  return freturn();
            case TypeConstants.DOUBLE: return dreturn();
            case TypeConstants.VOID:   return return_();
            default:          return areturn();
        }
    }


    /**
     * Appends instructions to print out the given message and the top int on
     * the stack.
     */
    public CompactCodeAttributeComposer appendPrintIntegerInstructions(String message)
    {
        return this
            .appendPrintInstructions(message)
            .appendPrintIntegerInstructions();
    }

    /**
     * Appends instructions to print out the given message and the top int on
     * the stack as a hexadecimal value.
     */
    public CompactCodeAttributeComposer appendPrintIntegerHexInstructions(String message)
    {
        return this
            .appendPrintInstructions(message)
            .appendPrintIntegerHexInstructions();
    }

    /**
     * Appends instructions to print out the given message and the top long on
     * the stack.
     */
    public CompactCodeAttributeComposer appendPrintLongInstructions(String message)
    {
        return this
            .appendPrintInstructions(message)
            .appendPrintLongInstructions();
    }

    /**
     * Appends instructions to print out the given message and the top String on
     * the stack.
     */
    public CompactCodeAttributeComposer appendPrintStringInstructions(String message)
    {
        return this
            .appendPrintInstructions(message)
            .appendPrintStringInstructions();
    }

    /**
     * Appends instructions to print out the given message and the top Object on
     * the stack.
     */
    public CompactCodeAttributeComposer appendPrintObjectInstructions(String message)
    {
        return this
            .appendPrintInstructions(message)
            .appendPrintObjectInstructions();
    }

    /**
     * Appends instructions to print out the given message and the stack trace
     * of the top Throwable on the stack.
     */
    public CompactCodeAttributeComposer appendPrintStackTraceInstructions(String message)
    {
        return this
            .appendPrintInstructions(message)
            .appendPrintStackTraceInstructions();
    }

    /**
     * Appends instructions to print out the given message.
     */
    public CompactCodeAttributeComposer appendPrintInstructions(String message)
    {
        return this
            .getstatic("java/lang/System", "err", "Ljava/io/PrintStream;")
            .ldc(message)
            .invokevirtual("java/io/PrintStream", "println", "(Ljava/lang/String;)V");
    }

    /**
     * Appends instructions to print out the top int on the stack.
     */
    public CompactCodeAttributeComposer appendPrintIntegerInstructions()
    {
        return this
            .dup()
            .getstatic("java/lang/System", "err", "Ljava/io/PrintStream;")
            .swap()
            .invokevirtual("java/io/PrintStream", "println", "(I)V");
    }

    /**
     * Appends instructions to print out the top integer on the stack as a
     * hexadecimal value.
     */
    public CompactCodeAttributeComposer appendPrintIntegerHexInstructions()
    {
        return this
            .dup()
            .getstatic("java/lang/System", "err", "Ljava/io/PrintStream;")
            .swap()
            .invokestatic("java/lang/Integer", "toHexString", "(I)Ljava/lang/String;")
            .invokevirtual("java/io/PrintStream", "println", "(Ljava/lang/String;)V");
    }

    /**
     * Appends instructions to print out the top long on the stack.
     */
    public CompactCodeAttributeComposer appendPrintLongInstructions()
    {
        return this
            .dup2()
            .getstatic("java/lang/System", "err", "Ljava/io/PrintStream;")
            .dup_x2()
            .pop()
            .invokevirtual("java/io/PrintStream", "println", "(J)V");
    }

    /**
     * Appends instructions to print out the top String on the stack.
     */
    public CompactCodeAttributeComposer appendPrintStringInstructions()
    {
        return this
            .dup()
            .getstatic("java/lang/System", "err", "Ljava/io/PrintStream;")
            .swap()
            .invokevirtual("java/io/PrintStream", "println", "(Ljava/lang/String;)V");
    }

    /**
     * Appends instructions to print out the top Object on the stack.
     */
    public CompactCodeAttributeComposer appendPrintObjectInstructions()
    {
        return this
            .dup()
            .getstatic("java/lang/System", "err", "Ljava/io/PrintStream;")
            .swap()
            .invokevirtual("java/io/PrintStream", "println", "(Ljava/lang/Object;)V");
    }

    /**
     * Appends instructions to print out the stack trace of the top Throwable
     * on the stack.
     */
    public CompactCodeAttributeComposer appendPrintStackTraceInstructions()
    {
        return this
            .dup()
            .invokevirtual("java/lang/Throwable", "printStackTrace", "()V");
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        codeAttributeComposer.visitCodeAttribute(clazz, method, codeAttribute);
    }


    // Small utility methods.

    /**
     * Returns the offsets of the given labels.
     */
    private int[] offsets(Label[] labels)
    {
        int[] offsets = new int[labels.length];

        for (int index = 0; index < offsets.length; index++)
        {
            offsets[index] = labels[index].offset;
        }

        return offsets;
    }


    /**
     * This class represents a label to which branch instructions and switch
     * instructions can jump.
     */
    public class Label
    {
        private final int offset;


        private Label(int offset)
        {
            this.offset = offset;
        }
    }


    /**
     * Small sample application that illustrates the use of this class.
     */
    public static void main(String[] args)
    {
        // Create an empty class.
        ProgramClass programClass =
            new ProgramClass(VersionConstants.CLASS_VERSION_1_8,
                             1,
                             new Constant[10],
                             AccessConstants.PUBLIC,
                             0,
                             0);

        // Add its name and superclass.
        ConstantPoolEditor constantPoolEditor =
            new ConstantPoolEditor(programClass);

        programClass.u2thisClass  = constantPoolEditor.addClassConstant("com/example/Test", programClass);
        programClass.u2superClass = constantPoolEditor.addClassConstant(ClassConstants.NAME_JAVA_LANG_OBJECT, null);

        // Create an empty method.
        ProgramMethod programMethod =
            new ProgramMethod(AccessConstants.PUBLIC,
                              constantPoolEditor.addUtf8Constant("test"),
                              constantPoolEditor.addUtf8Constant("()I"),
                              null);

        // Create an empty code attribute.
        CodeAttribute codeAttribute =
            new CodeAttribute(constantPoolEditor.addUtf8Constant(Attribute.CODE));

        // Add the code attribute to the method.
        AttributesEditor attributesEditor =
            new AttributesEditor(programClass, programMethod, false);

        attributesEditor.addAttribute(codeAttribute);

        // Add the method to the class.
        ClassEditor classEditor =
            new ClassEditor(programClass);

        classEditor.addMethod(programMethod);

        // Compose the code -- the equivalent of this java code:
        //     try
        //     {
        //         if (1 < 2) return 1; else return 2;
        //     }
        //     catch (Exception e)
        //     {
        //         return -1;
        //     }
        CompactCodeAttributeComposer composer =
            new CompactCodeAttributeComposer(programClass);

        final Label TRY_START = composer.createLabel();
        final Label TRY_END   = composer.createLabel();
        final Label ELSE      = composer.createLabel();

        composer
            .beginCodeFragment(50)
            .label(TRY_START)
            .iconst_1()
            .iconst_2()
            .ificmplt(ELSE)

            .iconst_1()
            .ireturn()

            .label(ELSE)
            .iconst_2()
            .ireturn()
            .label(TRY_END)

            .catch_(TRY_START, TRY_END, "java/lang/Exception", null)
            .iconst_m1()
            .ireturn()
            .endCodeFragment();

        // Put the code in the given code attribute.
        composer.visitCodeAttribute(programClass, programMethod, codeAttribute);
    }
}
