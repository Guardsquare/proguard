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
import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.*;
import proguard.classfile.util.ClassUtil;

import java.util.*;

/**
 * This utility class allows to construct sequences of instructions and
 * their constants.
 *
 * @author Eric Lafortune
 */
public class InstructionSequenceBuilder
{
    private final ConstantPoolEditor constantPoolEditor;

    private final List<Instruction> instructions = new ArrayList<Instruction>(256);


    /**
     * Creates a new InstructionSequenceBuilder.
     */
    public InstructionSequenceBuilder()
    {
        this(null, null);
    }


    /**
     * Creates a new InstructionSequenceBuilder that automatically initializes
     * class references and class member references in new constants.
     * @param programClassPool the program class pool from which new
     *                         constants can be initialized.
     * @param libraryClassPool the library class pool from which new
     *                         constants can be initialized.
     */
    public InstructionSequenceBuilder(ClassPool programClassPool,
                                      ClassPool libraryClassPool)
    {
        this(new MyDummyClass(),
             programClassPool,
             libraryClassPool);
    }


    /**
     * Creates a new InstructionSequenceBuilder.
     * @param targetClass the target class for the instruction
     *                    constants.
     */
    public InstructionSequenceBuilder(ProgramClass targetClass)
    {
        this(new ConstantPoolEditor(targetClass));
    }


    /**
     * Creates a new InstructionSequenceBuilder that automatically initializes
     * class references and class member references in new constants.
     * @param targetClass      the target class for the instruction
     *                         constants.
     * @param programClassPool the program class pool from which new
     *                         constants can be initialized.
     * @param libraryClassPool the library class pool from which new
     *                         constants can be initialized.
     */
    public InstructionSequenceBuilder(ProgramClass targetClass,
                                      ClassPool    programClassPool,
                                      ClassPool    libraryClassPool)
    {
        this(new ConstantPoolEditor(targetClass, programClassPool, libraryClassPool));
    }


    /**
     * Creates a new InstructionSequenceBuilder.
     * @param constantPoolEditor the editor to use for creating any constants
     *                           for the instructions.
     */
    public InstructionSequenceBuilder(ConstantPoolEditor constantPoolEditor)
    {
         this.constantPoolEditor = constantPoolEditor;
    }


    /**
     * Returns the ConstantPoolEditor used by this builder to
     * create constants.
     *
     * @return the ConstantPoolEditor used by this builder to
     * create constants.
     */
    public ConstantPoolEditor getConstantPoolEditor()
    {
        return constantPoolEditor;
    }


    /**
     * Short for {@link #appendInstruction(Instruction)}.
     *
     * @see InstructionSequenceReplacer#label()
     */
    public InstructionSequenceBuilder label(Instruction instruction)
    {
        return appendInstruction(instruction);
    }


    /**
     * Short for {@link #appendInstruction(Instruction)}.
     *
     * @see InstructionSequenceReplacer#catch_(int,int,int)
     */
    public InstructionSequenceBuilder catch_(Instruction instruction)
    {
        return appendInstruction(instruction);
    }


    /**
     * Appends the given instruction.
     * @param instruction the instruction to be appended.
     * @return the builder itself.
     */
    public InstructionSequenceBuilder appendInstruction(Instruction instruction)
    {
        return add(instruction);
    }


    /**
     * Appends the given instructions.
     * @param instructions the instructions to be appended.
     * @return the builder itself.
     */
    public InstructionSequenceBuilder appendInstructions(Instruction[] instructions)
    {
        for (Instruction instruction : instructions)
        {
            add(instruction);
        }

        return this;
    }


    /**
     * Short for {@link #instructions()}.
     */
    public Instruction[] __()
    {
        return instructions();
    }


    /**
     * Returns the accumulated sequence of instructions
     * and resets the sequence in the builder.
     */
    public Instruction[] instructions()
    {
        Instruction[] instructionsArray = new Instruction[instructions.size()];
        instructions.toArray(instructionsArray);

        instructions.clear();

        return instructionsArray;
    }


    /**
     * Returns the accumulated set of constants
     * and resets the set in the builder.
     */
    public Constant[] constants()
    {
        ProgramClass targetClass = constantPoolEditor.getTargetClass();

        Constant[] constantPool = new Constant[targetClass.u2constantPoolCount];
        System.arraycopy(targetClass.constantPool,
                         0,
                         constantPool,
                         0,
                         targetClass.u2constantPoolCount);

        targetClass.u2constantPoolCount = 0;

        return constantPool;
    }


    /**
     * Returns the number of instructions accumulated by this InstructionSequenceBuilder.
     */
    public int size()
    {
        return instructions.size();
    }


    // Methods corresponding to the bytecode opcodes.

    public InstructionSequenceBuilder nop()
    {
        return add(new SimpleInstruction(Instruction.OP_NOP));
    }

    public InstructionSequenceBuilder aconst_null()
    {
        return add(new SimpleInstruction(Instruction.OP_ACONST_NULL));
    }

    public InstructionSequenceBuilder iconst(int constant)
    {
        return add(new SimpleInstruction(Instruction.OP_ICONST_0, constant));
    }

    public InstructionSequenceBuilder iconst_m1()
    {
        return add(new SimpleInstruction(Instruction.OP_ICONST_M1));
    }

    public InstructionSequenceBuilder iconst_0()
    {
        return add(new SimpleInstruction(Instruction.OP_ICONST_0));
    }

    public InstructionSequenceBuilder iconst_1()
    {
        return add(new SimpleInstruction(Instruction.OP_ICONST_1));
    }

    public InstructionSequenceBuilder iconst_2()
    {
        return add(new SimpleInstruction(Instruction.OP_ICONST_2));
    }

    public InstructionSequenceBuilder iconst_3()
    {
        return add(new SimpleInstruction(Instruction.OP_ICONST_3));
    }

    public InstructionSequenceBuilder iconst_4()
    {
        return add(new SimpleInstruction(Instruction.OP_ICONST_4));
    }

    public InstructionSequenceBuilder iconst_5()
    {
        return add(new SimpleInstruction(Instruction.OP_ICONST_5));
    }

    public InstructionSequenceBuilder lconst(int constant)
    {
        return add(new SimpleInstruction(Instruction.OP_LCONST_0, constant));
    }

    public InstructionSequenceBuilder lconst_0()
    {
        return add(new SimpleInstruction(Instruction.OP_LCONST_0));
    }

    public InstructionSequenceBuilder lconst_1()
    {
        return add(new SimpleInstruction(Instruction.OP_LCONST_1));
    }

    public InstructionSequenceBuilder fconst(int constant)
    {
        return add(new SimpleInstruction(Instruction.OP_FCONST_0, constant));
    }

    public InstructionSequenceBuilder fconst_0()
    {
        return add(new SimpleInstruction(Instruction.OP_FCONST_0));
    }

    public InstructionSequenceBuilder fconst_1()
    {
        return add(new SimpleInstruction(Instruction.OP_FCONST_1));
    }

    public InstructionSequenceBuilder fconst_2()
    {
        return add(new SimpleInstruction(Instruction.OP_FCONST_2));
    }

    public InstructionSequenceBuilder dconst(int constant)
    {
        return add(new SimpleInstruction(Instruction.OP_DCONST_0, constant));
    }

    public InstructionSequenceBuilder dconst_0()
    {
        return add(new SimpleInstruction(Instruction.OP_DCONST_0));
    }

    public InstructionSequenceBuilder dconst_1()
    {
        return add(new SimpleInstruction(Instruction.OP_DCONST_1));
    }

    public InstructionSequenceBuilder bipush(int constant)
    {
        return add(new SimpleInstruction(Instruction.OP_BIPUSH, constant));
    }

    public InstructionSequenceBuilder sipush(int constant)
    {
        return add(new SimpleInstruction(Instruction.OP_SIPUSH, constant));
    }

    public InstructionSequenceBuilder ldc(int value)
    {
        return ldc_(constantPoolEditor.addIntegerConstant(value));
    }

    public InstructionSequenceBuilder ldc(float value)
    {
        return ldc_(constantPoolEditor.addFloatConstant(value));
    }

    public InstructionSequenceBuilder ldc(Object primitiveArray)
    {
        return ldc_(constantPoolEditor.addPrimitiveArrayConstant(primitiveArray));
    }

    public InstructionSequenceBuilder ldc(String string)
    {
        return ldc(string, null, null);
    }

    public InstructionSequenceBuilder ldc(String string, Clazz referencedClass, Method referencedMember)
    {
        return ldc_(constantPoolEditor.addStringConstant(string, referencedClass, referencedMember));
    }

    public InstructionSequenceBuilder ldc(Clazz clazz)
    {
        return ldc(clazz.getName(), clazz);
    }

    public InstructionSequenceBuilder ldc(String className, Clazz referencedClass)
    {
        return ldc_(constantPoolEditor.addClassConstant(className, referencedClass));
    }

    public InstructionSequenceBuilder ldc_(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_LDC, constantIndex));
    }

    public InstructionSequenceBuilder ldc_w(int value)
    {
        return ldc_w_(constantPoolEditor.addIntegerConstant(value));
    }

    public InstructionSequenceBuilder ldc_w(float value)
    {
        // If we can shrink the instruction, we may not need to create a constant.
        return ldc_w_(constantPoolEditor.addFloatConstant(value));
    }

    public InstructionSequenceBuilder ldc_w(String string)
    {
        return ldc_w(string, null, null);
    }

    public InstructionSequenceBuilder ldc_w(String string, Clazz referencedClass, Method referencedMember)
    {
        return ldc_w_(constantPoolEditor.addStringConstant(string, referencedClass, referencedMember));
    }

    public InstructionSequenceBuilder ldc_w(String className, Clazz referencedClass)
    {
        return ldc_w_(constantPoolEditor.addClassConstant(className, referencedClass));
    }

    public InstructionSequenceBuilder ldc_w_(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_LDC_W, constantIndex));
    }

    public InstructionSequenceBuilder ldc2_w(long value)
    {
        // If we can shrink the instruction, we may not need to create a constant.
        return ldc2_w(constantPoolEditor.addLongConstant(value));
    }

    public InstructionSequenceBuilder ldc2_w(double value)
    {
        // If we can shrink the instruction, we may not need to create a constant.
        return ldc2_w(constantPoolEditor.addDoubleConstant(value));
    }

    public InstructionSequenceBuilder ldc2_w(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_LDC2_W, constantIndex));
    }

    public InstructionSequenceBuilder iload(int variableIndex)
    {
        return add(new VariableInstruction(Instruction.OP_ILOAD, variableIndex));
    }

    public InstructionSequenceBuilder lload(int variableIndex)
    {
        return add(new VariableInstruction(Instruction.OP_LLOAD, variableIndex));
    }

    public InstructionSequenceBuilder fload(int variableIndex)
    {
        return add(new VariableInstruction(Instruction.OP_FLOAD, variableIndex));
    }

    public InstructionSequenceBuilder dload(int variableIndex)
    {
        return add(new VariableInstruction(Instruction.OP_DLOAD, variableIndex));
    }

    public InstructionSequenceBuilder aload(int variableIndex)
    {
        return add(new VariableInstruction(Instruction.OP_ALOAD, variableIndex));
    }

    public InstructionSequenceBuilder iload_0()
    {
        return add(new VariableInstruction(Instruction.OP_ILOAD_0));
    }

    public InstructionSequenceBuilder iload_1()
    {
        return add(new VariableInstruction(Instruction.OP_ILOAD_1));
    }

    public InstructionSequenceBuilder iload_2()
    {
        return add(new VariableInstruction(Instruction.OP_ILOAD_2));
    }

    public InstructionSequenceBuilder iload_3()
    {
        return add(new VariableInstruction(Instruction.OP_ILOAD_3));
    }

    public InstructionSequenceBuilder lload_0()
    {
        return add(new VariableInstruction(Instruction.OP_LLOAD_0));
    }

    public InstructionSequenceBuilder lload_1()
    {
        return add(new VariableInstruction(Instruction.OP_LLOAD_1));
    }

    public InstructionSequenceBuilder lload_2()
    {
        return add(new VariableInstruction(Instruction.OP_LLOAD_2));
    }

    public InstructionSequenceBuilder lload_3()
    {
        return add(new VariableInstruction(Instruction.OP_LLOAD_3));
    }

    public InstructionSequenceBuilder fload_0()
    {
        return add(new VariableInstruction(Instruction.OP_FLOAD_0));
    }

    public InstructionSequenceBuilder fload_1()
    {
        return add(new VariableInstruction(Instruction.OP_FLOAD_1));
    }

    public InstructionSequenceBuilder fload_2()
    {
        return add(new VariableInstruction(Instruction.OP_FLOAD_2));
    }

    public InstructionSequenceBuilder fload_3()
    {
        return add(new VariableInstruction(Instruction.OP_FLOAD_3));
    }

    public InstructionSequenceBuilder dload_0()
    {
        return add(new VariableInstruction(Instruction.OP_DLOAD_0));
    }

    public InstructionSequenceBuilder dload_1()
    {
        return add(new VariableInstruction(Instruction.OP_DLOAD_1));
    }

    public InstructionSequenceBuilder dload_2()
    {
        return add(new VariableInstruction(Instruction.OP_DLOAD_2));
    }

    public InstructionSequenceBuilder dload_3()
    {
        return add(new VariableInstruction(Instruction.OP_DLOAD_3));
    }

    public InstructionSequenceBuilder aload_0()
    {
        return add(new VariableInstruction(Instruction.OP_ALOAD_0));
    }

    public InstructionSequenceBuilder aload_1()
    {
        return add(new VariableInstruction(Instruction.OP_ALOAD_1));
    }

    public InstructionSequenceBuilder aload_2()
    {
        return add(new VariableInstruction(Instruction.OP_ALOAD_2));
    }

    public InstructionSequenceBuilder aload_3()
    {
        return add(new VariableInstruction(Instruction.OP_ALOAD_3));
    }

    public InstructionSequenceBuilder iaload()
    {
        return add(new SimpleInstruction(Instruction.OP_IALOAD));
    }

    public InstructionSequenceBuilder laload()
    {
        return add(new SimpleInstruction(Instruction.OP_LALOAD));
    }

    public InstructionSequenceBuilder faload()
    {
        return add(new SimpleInstruction(Instruction.OP_FALOAD));
    }

    public InstructionSequenceBuilder daload()
    {
        return add(new SimpleInstruction(Instruction.OP_DALOAD));
    }

    public InstructionSequenceBuilder aaload()
    {
        return add(new SimpleInstruction(Instruction.OP_AALOAD));
    }

    public InstructionSequenceBuilder baload()
    {
        return add(new SimpleInstruction(Instruction.OP_BALOAD));
    }

    public InstructionSequenceBuilder caload()
    {
        return add(new SimpleInstruction(Instruction.OP_CALOAD));
    }

    public InstructionSequenceBuilder saload()
    {
        return add(new SimpleInstruction(Instruction.OP_SALOAD));
    }

    public InstructionSequenceBuilder istore(int variableIndex)
    {
        return add(new VariableInstruction(Instruction.OP_ISTORE, variableIndex));
    }

    public InstructionSequenceBuilder lstore(int variableIndex)
    {
        return add(new VariableInstruction(Instruction.OP_LSTORE, variableIndex));
    }

    public InstructionSequenceBuilder fstore(int variableIndex)
    {
        return add(new VariableInstruction(Instruction.OP_FSTORE, variableIndex));
    }

    public InstructionSequenceBuilder dstore(int variableIndex)
    {
        return add(new VariableInstruction(Instruction.OP_DSTORE, variableIndex));
    }

    public InstructionSequenceBuilder astore(int variableIndex)
    {
        return add(new VariableInstruction(Instruction.OP_ASTORE, variableIndex));
    }

    public InstructionSequenceBuilder istore_0()
    {
        return add(new VariableInstruction(Instruction.OP_ISTORE_0));
    }

    public InstructionSequenceBuilder istore_1()
    {
        return add(new VariableInstruction(Instruction.OP_ISTORE_1));
    }

    public InstructionSequenceBuilder istore_2()
    {
        return add(new VariableInstruction(Instruction.OP_ISTORE_2));
    }

    public InstructionSequenceBuilder istore_3()
    {
        return add(new VariableInstruction(Instruction.OP_ISTORE_3));
    }

    public InstructionSequenceBuilder lstore_0()
    {
        return add(new VariableInstruction(Instruction.OP_LSTORE_0));
    }

    public InstructionSequenceBuilder lstore_1()
    {
        return add(new VariableInstruction(Instruction.OP_LSTORE_1));
    }

    public InstructionSequenceBuilder lstore_2()
    {
        return add(new VariableInstruction(Instruction.OP_LSTORE_2));
    }

    public InstructionSequenceBuilder lstore_3()
    {
        return add(new VariableInstruction(Instruction.OP_LSTORE_3));
    }

    public InstructionSequenceBuilder fstore_0()
    {
        return add(new VariableInstruction(Instruction.OP_FSTORE_0));
    }

    public InstructionSequenceBuilder fstore_1()
    {
        return add(new VariableInstruction(Instruction.OP_FSTORE_1));
    }

    public InstructionSequenceBuilder fstore_2()
    {
        return add(new VariableInstruction(Instruction.OP_FSTORE_2));
    }

    public InstructionSequenceBuilder fstore_3()
    {
        return add(new VariableInstruction(Instruction.OP_FSTORE_3));
    }

    public InstructionSequenceBuilder dstore_0()
    {
        return add(new VariableInstruction(Instruction.OP_DSTORE_0));
    }

    public InstructionSequenceBuilder dstore_1()
    {
        return add(new VariableInstruction(Instruction.OP_DSTORE_1));
    }

    public InstructionSequenceBuilder dstore_2()
    {
        return add(new VariableInstruction(Instruction.OP_DSTORE_2));
    }

    public InstructionSequenceBuilder dstore_3()
    {
        return add(new VariableInstruction(Instruction.OP_DSTORE_3));
    }

    public InstructionSequenceBuilder astore_0()
    {
        return add(new VariableInstruction(Instruction.OP_ASTORE_0));
    }

    public InstructionSequenceBuilder astore_1()
    {
        return add(new VariableInstruction(Instruction.OP_ASTORE_1));
    }

    public InstructionSequenceBuilder astore_2()
    {
        return add(new VariableInstruction(Instruction.OP_ASTORE_2));
    }

    public InstructionSequenceBuilder astore_3()
    {
        return add(new VariableInstruction(Instruction.OP_ASTORE_3));
    }

    public InstructionSequenceBuilder iastore()
    {
        return add(new SimpleInstruction(Instruction.OP_IASTORE));
    }

    public InstructionSequenceBuilder lastore()
    {
        return add(new SimpleInstruction(Instruction.OP_LASTORE));
    }

    public InstructionSequenceBuilder fastore()
    {
        return add(new SimpleInstruction(Instruction.OP_FASTORE));
    }

    public InstructionSequenceBuilder dastore()
    {
        return add(new SimpleInstruction(Instruction.OP_DASTORE));
    }

    public InstructionSequenceBuilder aastore()
    {
        return add(new SimpleInstruction(Instruction.OP_AASTORE));
    }

    public InstructionSequenceBuilder bastore()
    {
        return add(new SimpleInstruction(Instruction.OP_BASTORE));
    }

    public InstructionSequenceBuilder castore()
    {
        return add(new SimpleInstruction(Instruction.OP_CASTORE));
    }

    public InstructionSequenceBuilder sastore()
    {
        return add(new SimpleInstruction(Instruction.OP_SASTORE));
    }

    public InstructionSequenceBuilder pop()
    {
        return add(new SimpleInstruction(Instruction.OP_POP));
    }

    public InstructionSequenceBuilder pop2()
    {
        return add(new SimpleInstruction(Instruction.OP_POP2));
    }

    public InstructionSequenceBuilder dup()
    {
        return add(new SimpleInstruction(Instruction.OP_DUP));
    }

    public InstructionSequenceBuilder dup_x1()
    {
        return add(new SimpleInstruction(Instruction.OP_DUP_X1));
    }

    public InstructionSequenceBuilder dup_x2()
    {
        return add(new SimpleInstruction(Instruction.OP_DUP_X2));
    }

    public InstructionSequenceBuilder dup2()
    {
        return add(new SimpleInstruction(Instruction.OP_DUP2));
    }

    public InstructionSequenceBuilder dup2_x1()
    {
        return add(new SimpleInstruction(Instruction.OP_DUP2_X1));
    }

    public InstructionSequenceBuilder dup2_x2()
    {
        return add(new SimpleInstruction(Instruction.OP_DUP2_X2));
    }

    public InstructionSequenceBuilder swap()
    {
        return add(new SimpleInstruction(Instruction.OP_SWAP));
    }

    public InstructionSequenceBuilder iadd()
    {
        return add(new SimpleInstruction(Instruction.OP_IADD));
    }

    public InstructionSequenceBuilder ladd()
    {
        return add(new SimpleInstruction(Instruction.OP_LADD));
    }

    public InstructionSequenceBuilder fadd()
    {
        return add(new SimpleInstruction(Instruction.OP_FADD));
    }

    public InstructionSequenceBuilder dadd()
    {
        return add(new SimpleInstruction(Instruction.OP_DADD));
    }

    public InstructionSequenceBuilder isub()
    {
        return add(new SimpleInstruction(Instruction.OP_ISUB));
    }

    public InstructionSequenceBuilder lsub()
    {
        return add(new SimpleInstruction(Instruction.OP_LSUB));
    }

    public InstructionSequenceBuilder fsub()
    {
        return add(new SimpleInstruction(Instruction.OP_FSUB));
    }

    public InstructionSequenceBuilder dsub()
    {
        return add(new SimpleInstruction(Instruction.OP_DSUB));
    }

    public InstructionSequenceBuilder imul()
    {
        return add(new SimpleInstruction(Instruction.OP_IMUL));
    }

    public InstructionSequenceBuilder lmul()
    {
        return add(new SimpleInstruction(Instruction.OP_LMUL));
    }

    public InstructionSequenceBuilder fmul()
    {
        return add(new SimpleInstruction(Instruction.OP_FMUL));
    }

    public InstructionSequenceBuilder dmul()
    {
        return add(new SimpleInstruction(Instruction.OP_DMUL));
    }

    public InstructionSequenceBuilder idiv()
    {
        return add(new SimpleInstruction(Instruction.OP_IDIV));
    }

    public InstructionSequenceBuilder ldiv()
    {
        return add(new SimpleInstruction(Instruction.OP_LDIV));
    }

    public InstructionSequenceBuilder fdiv()
    {
        return add(new SimpleInstruction(Instruction.OP_FDIV));
    }

    public InstructionSequenceBuilder ddiv()
    {
        return add(new SimpleInstruction(Instruction.OP_DDIV));
    }

    public InstructionSequenceBuilder irem()
    {
        return add(new SimpleInstruction(Instruction.OP_IREM));
    }

    public InstructionSequenceBuilder lrem()
    {
        return add(new SimpleInstruction(Instruction.OP_LREM));
    }

    public InstructionSequenceBuilder frem()
    {
        return add(new SimpleInstruction(Instruction.OP_FREM));
    }

    public InstructionSequenceBuilder drem()
    {
        return add(new SimpleInstruction(Instruction.OP_DREM));
    }

    public InstructionSequenceBuilder ineg()
    {
        return add(new SimpleInstruction(Instruction.OP_INEG));
    }

    public InstructionSequenceBuilder lneg()
    {
        return add(new SimpleInstruction(Instruction.OP_LNEG));
    }

    public InstructionSequenceBuilder fneg()
    {
        return add(new SimpleInstruction(Instruction.OP_FNEG));
    }

    public InstructionSequenceBuilder dneg()
    {
        return add(new SimpleInstruction(Instruction.OP_DNEG));
    }

    public InstructionSequenceBuilder ishl()
    {
        return add(new SimpleInstruction(Instruction.OP_ISHL));
    }

    public InstructionSequenceBuilder lshl()
    {
        return add(new SimpleInstruction(Instruction.OP_LSHL));
    }

    public InstructionSequenceBuilder ishr()
    {
        return add(new SimpleInstruction(Instruction.OP_ISHR));
    }

    public InstructionSequenceBuilder lshr()
    {
        return add(new SimpleInstruction(Instruction.OP_LSHR));
    }

    public InstructionSequenceBuilder iushr()
    {
        return add(new SimpleInstruction(Instruction.OP_IUSHR));
    }

    public InstructionSequenceBuilder lushr()
    {
        return add(new SimpleInstruction(Instruction.OP_LUSHR));
    }

    public InstructionSequenceBuilder iand()
    {
        return add(new SimpleInstruction(Instruction.OP_IAND));
    }

    public InstructionSequenceBuilder land()
    {
        return add(new SimpleInstruction(Instruction.OP_LAND));
    }

    public InstructionSequenceBuilder ior()
    {
        return add(new SimpleInstruction(Instruction.OP_IOR));
    }

    public InstructionSequenceBuilder lor()
    {
        return add(new SimpleInstruction(Instruction.OP_LOR));
    }

    public InstructionSequenceBuilder ixor()
    {
        return add(new SimpleInstruction(Instruction.OP_IXOR));
    }

    public InstructionSequenceBuilder lxor()
    {
        return add(new SimpleInstruction(Instruction.OP_LXOR));
    }

    public InstructionSequenceBuilder iinc(int variableIndex,
                                           int constant)
    {
        return add(new VariableInstruction(Instruction.OP_IINC, variableIndex, constant));
    }

    public InstructionSequenceBuilder i2l()
    {
        return add(new SimpleInstruction(Instruction.OP_I2L));
    }

    public InstructionSequenceBuilder i2f()
    {
        return add(new SimpleInstruction(Instruction.OP_I2F));
    }

    public InstructionSequenceBuilder i2d()
    {
        return add(new SimpleInstruction(Instruction.OP_I2D));
    }

    public InstructionSequenceBuilder l2i()
    {
        return add(new SimpleInstruction(Instruction.OP_L2I));
    }

    public InstructionSequenceBuilder l2f()
    {
        return add(new SimpleInstruction(Instruction.OP_L2F));
    }

    public InstructionSequenceBuilder l2d()
    {
        return add(new SimpleInstruction(Instruction.OP_L2D));
    }

    public InstructionSequenceBuilder f2i()
    {
        return add(new SimpleInstruction(Instruction.OP_F2I));
    }

    public InstructionSequenceBuilder f2l()
    {
        return add(new SimpleInstruction(Instruction.OP_F2L));
    }

    public InstructionSequenceBuilder f2d()
    {
        return add(new SimpleInstruction(Instruction.OP_F2D));
    }

    public InstructionSequenceBuilder d2i()
    {
        return add(new SimpleInstruction(Instruction.OP_D2I));
    }

    public InstructionSequenceBuilder d2l()
    {
        return add(new SimpleInstruction(Instruction.OP_D2L));
    }

    public InstructionSequenceBuilder d2f()
    {
        return add(new SimpleInstruction(Instruction.OP_D2F));
    }

    public InstructionSequenceBuilder i2b()
    {
        return add(new SimpleInstruction(Instruction.OP_I2B));
    }

    public InstructionSequenceBuilder i2c()
    {
        return add(new SimpleInstruction(Instruction.OP_I2C));
    }

    public InstructionSequenceBuilder i2s()
    {
        return add(new SimpleInstruction(Instruction.OP_I2S));
    }

    public InstructionSequenceBuilder lcmp()
    {
        return add(new SimpleInstruction(Instruction.OP_LCMP));
    }

    public InstructionSequenceBuilder fcmpl()
    {
        return add(new SimpleInstruction(Instruction.OP_FCMPL));
    }

    public InstructionSequenceBuilder fcmpg()
    {
        return add(new SimpleInstruction(Instruction.OP_FCMPG));
    }

    public InstructionSequenceBuilder dcmpl()
    {
        return add(new SimpleInstruction(Instruction.OP_DCMPL));
    }

    public InstructionSequenceBuilder dcmpg()
    {
        return add(new SimpleInstruction(Instruction.OP_DCMPG));
    }

    public InstructionSequenceBuilder ifeq(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFEQ, branchOffset));
    }

    public InstructionSequenceBuilder ifne(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFNE, branchOffset));
    }

    public InstructionSequenceBuilder iflt(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFLT, branchOffset));
    }

    public InstructionSequenceBuilder ifge(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFGE, branchOffset));
    }

    public InstructionSequenceBuilder ifgt(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFGT, branchOffset));
    }

    public InstructionSequenceBuilder ifle(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFLE, branchOffset));
    }

    public InstructionSequenceBuilder ificmpeq(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFICMPEQ, branchOffset));
    }

    public InstructionSequenceBuilder ificmpne(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFICMPNE, branchOffset));
    }

    public InstructionSequenceBuilder ificmplt(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFICMPLT, branchOffset));
    }

    public InstructionSequenceBuilder ificmpge(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFICMPGE, branchOffset));
    }

    public InstructionSequenceBuilder ificmpgt(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFICMPGT, branchOffset));
    }

    public InstructionSequenceBuilder ificmple(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFICMPLE, branchOffset));
    }

    public InstructionSequenceBuilder ifacmpeq(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFACMPEQ, branchOffset));
    }

    public InstructionSequenceBuilder ifacmpne(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFACMPNE, branchOffset));
    }

    public InstructionSequenceBuilder goto_(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_GOTO, branchOffset));
    }

    public InstructionSequenceBuilder jsr(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_JSR, branchOffset));
    }

    public InstructionSequenceBuilder ret(int variableIndex)
    {
        return add(new VariableInstruction(Instruction.OP_RET, variableIndex));
    }

    public InstructionSequenceBuilder tableswitch(int   defaultOffset,
                                                    int   lowCase,
                                                    int   highCase,
                                                    int[] jumpOffsets)
    {
        return add(new TableSwitchInstruction(Instruction.OP_TABLESWITCH,
                                                    defaultOffset,
                                                    lowCase,
                                                    highCase,
                                                    jumpOffsets));
    }

    public InstructionSequenceBuilder lookupswitch(int  defaultOffset,
                                                    int[] cases,
                                                    int[] jumpOffsets)
    {
        return add(new LookUpSwitchInstruction(Instruction.OP_LOOKUPSWITCH,
                                                     defaultOffset,
                                                     cases,
                                                     jumpOffsets));
    }

    public InstructionSequenceBuilder ireturn()
    {
        return add(new SimpleInstruction(Instruction.OP_IRETURN));
    }

    public InstructionSequenceBuilder lreturn()
    {
        return add(new SimpleInstruction(Instruction.OP_LRETURN));
    }

    public InstructionSequenceBuilder freturn()
    {
        return add(new SimpleInstruction(Instruction.OP_FRETURN));
    }

    public InstructionSequenceBuilder dreturn()
    {
        return add(new SimpleInstruction(Instruction.OP_DRETURN));
    }

    public InstructionSequenceBuilder areturn()
    {
        return add(new SimpleInstruction(Instruction.OP_ARETURN));
    }

    public InstructionSequenceBuilder return_()
    {
        return add(new SimpleInstruction(Instruction.OP_RETURN));
    }

    public InstructionSequenceBuilder getstatic(Clazz clazz,
                                                Field field)
    {
        return getstatic(clazz.getName(),
                         field.getName(clazz),
                         field.getDescriptor(clazz),
                         clazz,
                         field);
    }

    public InstructionSequenceBuilder getstatic(String className,
                                                String name,
                                                String descriptor)
    {
        return getstatic(className, name, descriptor, null, null);
    }

    public InstructionSequenceBuilder getstatic(String className,
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

    public InstructionSequenceBuilder getstatic(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_GETSTATIC, constantIndex));
    }

    public InstructionSequenceBuilder putstatic(Clazz clazz,
                                                Field field)
    {
        return putstatic(clazz.getName(),
                         field.getName(clazz),
                         field.getDescriptor(clazz),
                         clazz,
                         field);
    }

    public InstructionSequenceBuilder putstatic(String className,
                                                String name,
                                                String descriptor)
    {
        return putstatic(className, name, descriptor, null, null);
    }

    public InstructionSequenceBuilder putstatic(String className,
                                                String name,
                                                String descriptor,
                                                Clazz  referencedClass,
                                                Field  referencedField)
    {
        return putstatic(constantPoolEditor.addFieldrefConstant(className,
                                                                name,
                                                                descriptor,
                                                                referencedClass,
                                                                referencedField));
    }

    public InstructionSequenceBuilder putstatic(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_PUTSTATIC, constantIndex));
    }

    public InstructionSequenceBuilder getfield(Clazz clazz,
                                               Field field)
    {
        return getfield(clazz.getName(),
                        field.getName(clazz),
                        field.getDescriptor(clazz),
                        clazz,
                        field);
    }

    public InstructionSequenceBuilder getfield(String className,
                                               String name,
                                               String descriptor)
    {
        return getfield(className, name, descriptor, null, null);
    }

    public InstructionSequenceBuilder getfield(String className,
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

    public InstructionSequenceBuilder getfield(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_GETFIELD, constantIndex));
    }

    public InstructionSequenceBuilder putfield(Clazz clazz,
                                               Field field)
    {
        return putfield(clazz.getName(),
                        field.getName(clazz),
                        field.getDescriptor(clazz),
                        clazz,
                        field);
    }

    public InstructionSequenceBuilder putfield(String className,
                                               String name,
                                               String descriptor)
    {
        return putfield(className, name, descriptor, null, null);
    }

    public InstructionSequenceBuilder putfield(String className,
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

    public InstructionSequenceBuilder putfield(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_PUTFIELD, constantIndex));
    }

    public InstructionSequenceBuilder invokevirtual(Clazz  clazz,
                                                    Method method)
    {
        return invokevirtual(clazz.getName(),
                             method.getName(clazz),
                             method.getDescriptor(clazz),
                             clazz,
                             method);
    }

    public InstructionSequenceBuilder invokevirtual(String className,
                                                    String name,
                                                    String descriptor)
    {
        return invokevirtual(className, name, descriptor, null, null);
    }

    public InstructionSequenceBuilder invokevirtual(int    classIndex,
                                                    String name,
                                                    String descriptor)
    {
        return invokevirtual(constantPoolEditor.addMethodrefConstant(classIndex,
                                                                     name,
                                                                     descriptor,
                                                                     null,
                                                                     null));
    }

    public InstructionSequenceBuilder invokevirtual(String className,
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

    public InstructionSequenceBuilder invokevirtual(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, constantIndex));
    }

    public InstructionSequenceBuilder invokespecial(Clazz  clazz,
                                                    Method method)
    {
        return invokespecial(clazz.getName(),
                             method.getName(clazz),
                             method.getDescriptor(clazz),
                             clazz,
                             method);
    }

    public InstructionSequenceBuilder invokespecial(String className,
                                                    String name,
                                                    String descriptor)
    {
        return invokespecial(className, name, descriptor, null, null);
    }

    public InstructionSequenceBuilder invokespecial(String className,
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

    public InstructionSequenceBuilder invokespecial_interface(Clazz  clazz,
                                                              Method method)
    {
        return invokespecial_interface(clazz.getName(),
                                       method.getName(clazz),
                                       method.getDescriptor(clazz),
                                       clazz,
                                       method);
    }

    public InstructionSequenceBuilder invokespecial_interface(String className,
                                                              String name,
                                                              String descriptor)
    {
        return invokespecial_interface(className, name, descriptor, null, null);
    }

    public InstructionSequenceBuilder invokespecial_interface(String className,
                                                              String name,
                                                              String descriptor,
                                                              Clazz  referencedClass,
                                                              Method referencedMethod)
    {
        return invokespecial(constantPoolEditor.addInterfaceMethodrefConstant(className,
                                                                              name,
                                                                              descriptor,
                                                                              referencedClass,
                                                                              referencedMethod));
    }

    public InstructionSequenceBuilder invokespecial(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex));
    }

    public InstructionSequenceBuilder invokestatic(Clazz  clazz,
                                                   Method method)
    {
        return invokestatic(clazz.getName(),
                            method.getName(clazz),
                            method.getDescriptor(clazz),
                            clazz,
                            method);
    }

    public InstructionSequenceBuilder invokestatic(String className,
                                                   String name,
                                                   String descriptor)
    {
        return invokestatic(className, name, descriptor, null, null);
    }

    public InstructionSequenceBuilder invokestatic(String className,
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

    public InstructionSequenceBuilder invokestatic_interface(Clazz  clazz,
                                                             Method method)
    {
        return invokestatic_interface(clazz.getName(),
                                      method.getName(clazz),
                                      method.getDescriptor(clazz),
                                      clazz,
                                      method);
    }

    public InstructionSequenceBuilder invokestatic_interface(String className,
                                                             String name,
                                                             String descriptor)
    {
        return invokestatic_interface(className, name, descriptor, null, null);
    }

    public InstructionSequenceBuilder invokestatic_interface(String className,
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

    public InstructionSequenceBuilder invokestatic(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_INVOKESTATIC, constantIndex));
    }

    public InstructionSequenceBuilder invokeinterface(Clazz  clazz,
                                                      Method method)
    {
        return invokeinterface(clazz.getName(),
                               method.getName(clazz),
                               method.getDescriptor(clazz),
                               clazz,
                               method);
    }

    public InstructionSequenceBuilder invokeinterface(String className,
                                                      String name,
                                                      String descriptor)
    {
        return invokeinterface(className, name, descriptor, null, null);
    }

    public InstructionSequenceBuilder invokeinterface(String className,
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

    public InstructionSequenceBuilder invokeinterface(int constantIndex, int constant)
    {
        return add(new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, constantIndex, constant));
    }

    public InstructionSequenceBuilder invokedynamic(int    bootStrapMethodIndex,
                                                    String name,
                                                    String descriptor)
    {
        return invokedynamic(bootStrapMethodIndex, name, descriptor, null);
    }

    public InstructionSequenceBuilder invokedynamic(int     bootStrapMethodIndex,
                                                    String  name,
                                                    String  descriptor,
                                                    Clazz[] referencedClasses)
    {
        return invokedynamic(constantPoolEditor.addInvokeDynamicConstant(bootStrapMethodIndex,
                                                                         name,
                                                                         descriptor,
                                                                         referencedClasses));
    }

    public InstructionSequenceBuilder invokedynamic(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, constantIndex));
    }

    public InstructionSequenceBuilder new_(Clazz clazz)
    {
        return new_(clazz.getName(), clazz);
    }

    public InstructionSequenceBuilder new_(String className)
    {
        return new_(className, null);
    }

    public InstructionSequenceBuilder new_(String className,
                                           Clazz  referencedClass)
    {
        return new_(constantPoolEditor.addClassConstant(className, referencedClass));
    }

    public InstructionSequenceBuilder new_(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_NEW, constantIndex));
    }

    public InstructionSequenceBuilder newarray(int constant)
    {
        return add(new SimpleInstruction(Instruction.OP_NEWARRAY, constant));
    }

    public InstructionSequenceBuilder anewarray(Clazz elementType)
    {
        return anewarray(elementType.getName(), elementType);
    }

    public InstructionSequenceBuilder anewarray(String elementTypeName,
                                                Clazz  referencedClass)
    {
        return anewarray(constantPoolEditor.addClassConstant(elementTypeName, referencedClass));
    }

    public InstructionSequenceBuilder anewarray(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_ANEWARRAY, constantIndex));
    }

    public InstructionSequenceBuilder arraylength()
    {
        return add(new SimpleInstruction(Instruction.OP_ARRAYLENGTH));
    }

    public InstructionSequenceBuilder athrow()
    {
        return add(new SimpleInstruction(Instruction.OP_ATHROW));
    }

    public InstructionSequenceBuilder checkcast(Clazz type)
    {
        return checkcast(type.getName(), type);
    }

    public InstructionSequenceBuilder checkcast(String typeName)
    {
        return checkcast(typeName, null);
    }

    public InstructionSequenceBuilder checkcast(String typeName, Clazz referencedClass)
    {
        return checkcast(constantPoolEditor.addClassConstant(typeName, referencedClass));
    }

    public InstructionSequenceBuilder checkcast(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_CHECKCAST, constantIndex));
    }

    public InstructionSequenceBuilder instanceof_(Clazz type)
    {
        return instanceof_(type.getName(), type);
    }

    public InstructionSequenceBuilder instanceof_(String typeName, Clazz referencedClass)
    {
        return instanceof_(constantPoolEditor.addClassConstant(typeName, referencedClass));
    }

    public InstructionSequenceBuilder instanceof_(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_INSTANCEOF, constantIndex));
    }

    public InstructionSequenceBuilder monitorenter()
    {
        return add(new SimpleInstruction(Instruction.OP_MONITORENTER));
    }

    public InstructionSequenceBuilder monitorexit()
    {
        return add(new SimpleInstruction(Instruction.OP_MONITOREXIT));
    }

    public InstructionSequenceBuilder wide()
    {
        return add(new SimpleInstruction(Instruction.OP_WIDE));
    }

    public InstructionSequenceBuilder multianewarray(Clazz type)
    {
        return multianewarray(type.getName(), type);
    }

    public InstructionSequenceBuilder multianewarray(String typeName)
    {
        return multianewarray(typeName, null);
    }

    public InstructionSequenceBuilder multianewarray(String typeName, Clazz referencedClass)
    {
        return multianewarray(constantPoolEditor.addClassConstant(typeName, referencedClass));
    }

    public InstructionSequenceBuilder multianewarray(int constantIndex)
    {
        return add(new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, constantIndex));
    }

    public InstructionSequenceBuilder ifnull(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFNULL, branchOffset));
    }

    public InstructionSequenceBuilder ifnonnull(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_IFNONNULL, branchOffset));
    }

    public InstructionSequenceBuilder goto_w(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_GOTO_W, branchOffset));
    }

    public InstructionSequenceBuilder jsr_w(int branchOffset)
    {
        return add(new BranchInstruction(Instruction.OP_JSR_W, branchOffset));
    }


    // Additional convenience methods.

    /**
     * Pushes the given primitive value on the stack.
     *
     * Operand stack:
     * ... -> ..., value
     *
     * @param value        the primitive value to be pushed - should never be null.
     * @param type the internal type of the primitive ('Z','B','I',...)
     */
    public InstructionSequenceBuilder pushPrimitive(Object value,
                                                    char   type)
    {
        switch (type)
        {
            case TypeConstants.BOOLEAN: return ((Boolean)value).booleanValue() ? iconst_1() : iconst_0();
            case TypeConstants.BYTE:
            case TypeConstants.SHORT:
            case TypeConstants.INT:     return pushInt(((Number)value).intValue());
            case TypeConstants.CHAR:    return ldc(((Character)value).charValue());
            case TypeConstants.LONG:    return ldc2_w((Long)value);
            case TypeConstants.FLOAT:   return ldc(((Float)value).floatValue());
            case TypeConstants.DOUBLE:  return ldc2_w((Double)value);
            default: throw new IllegalArgumentException("" + type);
        }
    }


    /**
     * Pushes the given primitive int on the stack in the most efficient way
     * (as an iconst, bipush, sipush, or ldc instruction).
     *
     * @param value the int value to be pushed.
     */
    public InstructionSequenceBuilder pushInt(int value)
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
    public InstructionSequenceBuilder pushFloat(float value)
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
    public InstructionSequenceBuilder pushLong(long value)
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
    public InstructionSequenceBuilder pushDouble(double value)
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
     * @param type the array element type (or class name in case of objects).
     * @param size the size of the array to be created.
     */
    public InstructionSequenceBuilder pushNewArray(String type,
                                                   int    size)
    {
        // Create new array.
        pushInt(size);

        return ClassUtil.isInternalPrimitiveType(type) ?
            newarray(InstructionUtil.arrayTypeFromInternalType(type.charAt(0))) :
            anewarray(type, null);
    }


    /**
     * Loads the given variable onto the stack.
     *
     * Operand stack:
     * ... -> ..., value
     *
     * @param variableIndex the index of the variable to be loaded.
     * @param type  the type of the variable to be loaded.
     */
    public InstructionSequenceBuilder load(int    variableIndex,
                                           String type)
    {
        return load(variableIndex, type.charAt(0));
    }


    /**
     * Loads the given variable of primitive type onto the stack.
     *
     * Operand stack:
     * ... -> ..., value
     *
     * @param variableIndex the index of the variable to be loaded.
     * @param type          the type of the variable to be loaded.
     */
    public InstructionSequenceBuilder load(int  variableIndex,
                                           char type)
    {
        switch (type)
        {
            case TypeConstants.BOOLEAN:
            case TypeConstants.BYTE:
            case TypeConstants.CHAR:
            case TypeConstants.SHORT:
            case TypeConstants.INT:    return iload(variableIndex);
            case TypeConstants.LONG:   return lload(variableIndex);
            case TypeConstants.FLOAT:  return fload(variableIndex);
            case TypeConstants.DOUBLE: return dload(variableIndex);
            default:                         return aload(variableIndex);
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
     * @param type          the type of the value to be stored.
     */
    public InstructionSequenceBuilder store(int    variableIndex,
                                            String type)
    {
        return store(variableIndex, type.charAt(0));
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
     * @param type          the type of the value to be stored.
     */
    public InstructionSequenceBuilder store(int  variableIndex,
                                            char type)
    {
        switch (type)
        {
            case TypeConstants.BOOLEAN:
            case TypeConstants.BYTE:
            case TypeConstants.CHAR:
            case TypeConstants.SHORT:
            case TypeConstants.INT:    return istore(variableIndex);
            case TypeConstants.LONG:   return lstore(variableIndex);
            case TypeConstants.FLOAT:  return fstore(variableIndex);
            case TypeConstants.DOUBLE: return dstore(variableIndex);
            default:                         return astore(variableIndex);
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
    public InstructionSequenceBuilder storeToArray(String elementType)
    {
        // Store element on stack in array.
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
            default:                         return aastore();
        }
    }


    /**
     * Loads an element from an array.
     *
     * Operand stack:
     * ..., array, index -> ..., value
     *
     * @param elementType the type of the value to be loaded.
     */
    public InstructionSequenceBuilder loadFromArray(String elementType)
    {
        // Load element from array on stack.
        switch (elementType.charAt(0))
        {
            case TypeConstants.BOOLEAN:
            case TypeConstants.BYTE:   return baload();
            case TypeConstants.CHAR:   return caload();
            case TypeConstants.SHORT:  return saload();
            case TypeConstants.INT:    return iaload();
            case TypeConstants.LONG:   return laload();
            case TypeConstants.FLOAT:  return faload();
            case TypeConstants.DOUBLE: return daload();
            default:                         return aaload();
        }
    }


    // Small utility methods.

    /**
     * Adds the given instruction, shrinking it if necessary.
     */
    private InstructionSequenceBuilder add(Instruction instruction)
    {
        instructions.add(instruction);

        return this;
    }


    /**
     * Small sample application that illustrates the use of this class.
     */
    public static void main(String[] args)
    {
        InstructionSequenceBuilder builder = new InstructionSequenceBuilder();

        Instruction[] instructions = builder
            .iconst_2()
            .istore_0()
            .iinc(0, 2)
            .iload_0()
            .ldc(12)
            .iadd()
            .putstatic("com/example/SomeClass", "someField", "I", null, null)
            .instructions();

        Constant[] constants = builder.constants();

        System.out.println("Instructions:");
        for (Instruction instruction : instructions)
        {
            System.out.println(instruction);
        }

        System.out.println();

        System.out.println("Constants:");
        for (int index = 0; index < constants.length; index++)
        {
            System.out.println("#"+index+": " + constants[index]);
        }
    }


    /**
     * This ProgramClass is a dummy container for a constant pool, with a null name.
     */
    private static class MyDummyClass
    extends              ProgramClass
    {
        public MyDummyClass()
        {
            super(VersionConstants.CLASS_VERSION_1_0, 1, new Constant[256], 0, 0, 0);
        }


        // Overriding methods for Claaz.

        public String getName()
        {
            return null;
        }
    }
}
