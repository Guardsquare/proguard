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
package proguard.classfile.instruction;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.ClassUtil;

/**
 * This {@link Instruction} represents an instruction that refers to an entry in the
 * constant pool.
 *
 * @author Eric Lafortune
 */
public class ConstantInstruction extends Instruction
implements   ConstantVisitor
{
    public int constantIndex;
    public int constant;


    // Fields acting as return parameters for the ConstantVisitor methods.
    private int parameterStackDelta;
    private int typeStackDelta;


    /**
     * Creates an uninitialized ConstantInstruction.
     */
    public ConstantInstruction() {}


    /**
     * Creates a new ConstantInstruction with the given opcode and constant pool
     * index.
     */
    public ConstantInstruction(byte opcode, int constantIndex)
    {
        this(opcode, constantIndex, 0);
    }


    /**
     * Creates a new ConstantInstruction with the given opcode, constant pool
     * index, and constant.
     */
    public ConstantInstruction(byte opcode, int constantIndex, int constant)
    {
        this.opcode        = opcode;
        this.constantIndex = constantIndex;
        this.constant      = constant;
    }


    /**
     * Copies the given instruction into this instruction.
     * @param constantInstruction the instruction to be copied.
     * @return this instruction.
     */
    public ConstantInstruction copy(ConstantInstruction constantInstruction)
    {
        this.opcode        = constantInstruction.opcode;
        this.constantIndex = constantInstruction.constantIndex;
        this.constant      = constantInstruction.constant;

        return this;
    }


    // Implementations for Instruction.

    public byte canonicalOpcode()
    {
        // Remove the _w extension, if any.
        return
            opcode == Instruction.OP_LDC_W ? Instruction.OP_LDC :
                                                      opcode;
    }

    public Instruction shrink()
    {
        // Do we need a short index or a long index?
        if (requiredConstantIndexSize() == 1)
        {
            // Can we replace the long instruction by a short instruction?
            if (opcode == Instruction.OP_LDC_W)
            {
                opcode = Instruction.OP_LDC;
            }
        }
        else
        {
            // Should we replace the short instruction by a long instruction?
            if (opcode == Instruction.OP_LDC)
            {
                opcode = Instruction.OP_LDC_W;
            }
        }

        return this;
    }

    protected void readInfo(byte[] code, int offset)
    {
        int constantIndexSize = constantIndexSize();
        int constantSize      = constantSize();

        constantIndex = readValue(code, offset, constantIndexSize);  offset += constantIndexSize;
        constant      = readValue(code, offset, constantSize);
    }


    protected void writeInfo(byte[] code, int offset)
    {
        int constantIndexSize = constantIndexSize();
        int constantSize      = constantSize();

        if (requiredConstantIndexSize() > constantIndexSize)
        {
            throw new IllegalArgumentException("Instruction has invalid constant index size ("+this.toString(offset)+")");
        }

        writeValue(code, offset, constantIndex, constantIndexSize); offset += constantIndexSize;
        writeValue(code, offset, constant,      constantSize);
    }


    public int length(int offset)
    {
        return 1 + constantIndexSize() + constantSize();
    }


    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, InstructionVisitor instructionVisitor)
    {
        instructionVisitor.visitConstantInstruction(clazz, method, codeAttribute, offset, this);
    }


    public int stackPopCount(Clazz clazz)
    {
        int stackPopCount = super.stackPopCount(clazz);

        // Some special cases.
        switch (opcode)
        {
            case Instruction.OP_MULTIANEWARRAY:
                // For each dimension, an integer size is popped from the stack.
                stackPopCount += constant;
                break;

            case Instruction.OP_PUTSTATIC:
            case Instruction.OP_PUTFIELD:
                // The field value is be popped from the stack.
                clazz.constantPoolEntryAccept(constantIndex, this);
                stackPopCount += typeStackDelta;
                break;

            case Instruction.OP_INVOKEVIRTUAL:
            case Instruction.OP_INVOKESPECIAL:
            case Instruction.OP_INVOKESTATIC:
            case Instruction.OP_INVOKEINTERFACE:
            case Instruction.OP_INVOKEDYNAMIC:
                // Some parameters may be popped from the stack.
                clazz.constantPoolEntryAccept(constantIndex, this);
                stackPopCount += parameterStackDelta;
                break;
        }

        return stackPopCount;
    }


    public int stackPushCount(Clazz clazz)
    {
        int stackPushCount = super.stackPushCount(clazz);

        // Some special cases.
        switch (opcode)
        {
            case Instruction.OP_GETSTATIC:
            case Instruction.OP_GETFIELD:
            case Instruction.OP_INVOKEVIRTUAL:
            case Instruction.OP_INVOKESPECIAL:
            case Instruction.OP_INVOKESTATIC:
            case Instruction.OP_INVOKEINTERFACE:
            case Instruction.OP_INVOKEDYNAMIC:
                // The field value or a return value may be pushed onto the stack.
                clazz.constantPoolEntryAccept(constantIndex, this);
                stackPushCount += typeStackDelta;
                break;
        }

        return stackPushCount;
    }


    // Implementations for ConstantVisitor.

    public void visitIntegerConstant(Clazz clazz, IntegerConstant integerConstant) {}
    public void visitLongConstant(Clazz clazz, LongConstant longConstant) {}
    public void visitFloatConstant(Clazz clazz, FloatConstant floatConstant) {}
    public void visitDoubleConstant(Clazz clazz, DoubleConstant doubleConstant) {}
    public void visitPrimitiveArrayConstant(Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant) {}
    public void visitStringConstant(Clazz clazz, StringConstant stringConstant) {}
    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant) {}
    public void visitMethodHandleConstant(Clazz clazz, MethodHandleConstant methodHandleConstant) {}
    public void visitClassConstant(Clazz clazz, ClassConstant classConstant) {}
    public void visitMethodTypeConstant(Clazz clazz, MethodTypeConstant methodTypeConstant) {}
    public void visitModuleConstant(Clazz clazz, ModuleConstant moduleConstant) {}
    public void visitPackageConstant(Clazz clazz, PackageConstant packageConstant) {}


    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        String type = fieldrefConstant.getType(clazz);

        typeStackDelta = ClassUtil.internalTypeSize(ClassUtil.internalMethodReturnType(type));
    }


    public void visitDynamicConstant(Clazz clazz, DynamicConstant dynamicConstant)
    {
        clazz.constantPoolEntryAccept(dynamicConstant.u2nameAndTypeIndex, this);
    }


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        clazz.constantPoolEntryAccept(invokeDynamicConstant.u2nameAndTypeIndex, this);
    }


    public void visitInterfaceMethodrefConstant(Clazz clazz, InterfaceMethodrefConstant interfaceMethodrefConstant)
    {
        clazz.constantPoolEntryAccept(interfaceMethodrefConstant.u2nameAndTypeIndex, this);
    }


    public void visitMethodrefConstant(Clazz clazz, MethodrefConstant methodrefConstant)
    {
        clazz.constantPoolEntryAccept(methodrefConstant.u2nameAndTypeIndex, this);
    }


    public void visitNameAndTypeConstant(Clazz clazz, NameAndTypeConstant nameAndTypeConstant)
    {
        String type = nameAndTypeConstant.getType(clazz);

        parameterStackDelta = ClassUtil.internalMethodParameterSize(type);
        typeStackDelta      = ClassUtil.internalTypeSize(ClassUtil.internalMethodReturnType(type));
    }


    // Implementations for Object.

    public String toString()
    {
        return getName()+" #"+constantIndex+(constantSize() == 0 ? "" : ", "+constant);
    }


    // Small utility methods.

    /**
     * Returns the constant pool index size for this instruction.
     */
    private int constantIndexSize()
    {
        return opcode == Instruction.OP_LDC ? 1 :
                                                       2;
    }


    /**
     * Returns the constant size for this instruction.
     */
    private int constantSize()
    {
        return opcode == Instruction.OP_MULTIANEWARRAY  ? 1 :
               opcode == Instruction.OP_INVOKEDYNAMIC ||
               opcode == Instruction.OP_INVOKEINTERFACE ? 2 :
                                                                   0;
    }


    /**
     * Computes the required constant pool index size for this instruction's
     * constant pool index.
     */
    private int requiredConstantIndexSize()
    {
        return (constantIndex &   0xff) == constantIndex ? 1 :
               (constantIndex & 0xffff) == constantIndex ? 2 :
                                                           4;
    }
}
