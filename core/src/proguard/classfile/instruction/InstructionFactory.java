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

/**
 * This class provides methods to create and reuse Instruction instances.
 *
 * @author Eric Lafortune
 */
public class InstructionFactory
{
    /**
     * Creates a new Instruction from the data in the byte array, starting
     * at the given offset.
     */
    public static Instruction create(byte[] code, int offset)
    {
        int  index  = offset;
        byte opcode = code[index++];

        boolean wide = false;
        if (opcode == Instruction.OP_WIDE)
        {
            opcode = code[index++];
            wide   = true;
        }

        Instruction instruction = create(opcode, wide);

        instruction.opcode = opcode;

        instruction.readInfo(code, index);

        return instruction;
    }


    /**
     * Creates a new Instruction corresponding to the given opcode.
     */
    public static Instruction create(byte opcode)
    {
        Instruction instruction = create(opcode, false);

        instruction.opcode = opcode;

        return instruction;
    }


    /**
     * Creates a new Instruction corresponding to the given opcode.
     */
    private static Instruction create(byte opcode, boolean wide)
    {
        switch (opcode)
        {
            // Simple instructions.
            case Instruction.OP_NOP:
            case Instruction.OP_ACONST_NULL:
            case Instruction.OP_ICONST_M1:
            case Instruction.OP_ICONST_0:
            case Instruction.OP_ICONST_1:
            case Instruction.OP_ICONST_2:
            case Instruction.OP_ICONST_3:
            case Instruction.OP_ICONST_4:
            case Instruction.OP_ICONST_5:
            case Instruction.OP_LCONST_0:
            case Instruction.OP_LCONST_1:
            case Instruction.OP_FCONST_0:
            case Instruction.OP_FCONST_1:
            case Instruction.OP_FCONST_2:
            case Instruction.OP_DCONST_0:
            case Instruction.OP_DCONST_1:

            case Instruction.OP_BIPUSH:
            case Instruction.OP_SIPUSH:

            case Instruction.OP_IALOAD:
            case Instruction.OP_LALOAD:
            case Instruction.OP_FALOAD:
            case Instruction.OP_DALOAD:
            case Instruction.OP_AALOAD:
            case Instruction.OP_BALOAD:
            case Instruction.OP_CALOAD:
            case Instruction.OP_SALOAD:

            case Instruction.OP_IASTORE:
            case Instruction.OP_LASTORE:
            case Instruction.OP_FASTORE:
            case Instruction.OP_DASTORE:
            case Instruction.OP_AASTORE:
            case Instruction.OP_BASTORE:
            case Instruction.OP_CASTORE:
            case Instruction.OP_SASTORE:
            case Instruction.OP_POP:
            case Instruction.OP_POP2:
            case Instruction.OP_DUP:
            case Instruction.OP_DUP_X1:
            case Instruction.OP_DUP_X2:
            case Instruction.OP_DUP2:
            case Instruction.OP_DUP2_X1:
            case Instruction.OP_DUP2_X2:
            case Instruction.OP_SWAP:
            case Instruction.OP_IADD:
            case Instruction.OP_LADD:
            case Instruction.OP_FADD:
            case Instruction.OP_DADD:
            case Instruction.OP_ISUB:
            case Instruction.OP_LSUB:
            case Instruction.OP_FSUB:
            case Instruction.OP_DSUB:
            case Instruction.OP_IMUL:
            case Instruction.OP_LMUL:
            case Instruction.OP_FMUL:
            case Instruction.OP_DMUL:
            case Instruction.OP_IDIV:
            case Instruction.OP_LDIV:
            case Instruction.OP_FDIV:
            case Instruction.OP_DDIV:
            case Instruction.OP_IREM:
            case Instruction.OP_LREM:
            case Instruction.OP_FREM:
            case Instruction.OP_DREM:
            case Instruction.OP_INEG:
            case Instruction.OP_LNEG:
            case Instruction.OP_FNEG:
            case Instruction.OP_DNEG:
            case Instruction.OP_ISHL:
            case Instruction.OP_LSHL:
            case Instruction.OP_ISHR:
            case Instruction.OP_LSHR:
            case Instruction.OP_IUSHR:
            case Instruction.OP_LUSHR:
            case Instruction.OP_IAND:
            case Instruction.OP_LAND:
            case Instruction.OP_IOR:
            case Instruction.OP_LOR:
            case Instruction.OP_IXOR:
            case Instruction.OP_LXOR:

            case Instruction.OP_I2L:
            case Instruction.OP_I2F:
            case Instruction.OP_I2D:
            case Instruction.OP_L2I:
            case Instruction.OP_L2F:
            case Instruction.OP_L2D:
            case Instruction.OP_F2I:
            case Instruction.OP_F2L:
            case Instruction.OP_F2D:
            case Instruction.OP_D2I:
            case Instruction.OP_D2L:
            case Instruction.OP_D2F:
            case Instruction.OP_I2B:
            case Instruction.OP_I2C:
            case Instruction.OP_I2S:
            case Instruction.OP_LCMP:
            case Instruction.OP_FCMPL:
            case Instruction.OP_FCMPG:
            case Instruction.OP_DCMPL:
            case Instruction.OP_DCMPG:

            case Instruction.OP_IRETURN:
            case Instruction.OP_LRETURN:
            case Instruction.OP_FRETURN:
            case Instruction.OP_DRETURN:
            case Instruction.OP_ARETURN:
            case Instruction.OP_RETURN:

            case Instruction.OP_NEWARRAY:
            case Instruction.OP_ARRAYLENGTH:
            case Instruction.OP_ATHROW:

            case Instruction.OP_MONITORENTER:
            case Instruction.OP_MONITOREXIT:
                return new SimpleInstruction();

            // Instructions with a contant pool index.
            case Instruction.OP_LDC:
            case Instruction.OP_LDC_W:
            case Instruction.OP_LDC2_W:

            case Instruction.OP_GETSTATIC:
            case Instruction.OP_PUTSTATIC:
            case Instruction.OP_GETFIELD:
            case Instruction.OP_PUTFIELD:

            case Instruction.OP_INVOKEVIRTUAL:
            case Instruction.OP_INVOKESPECIAL:
            case Instruction.OP_INVOKESTATIC:
            case Instruction.OP_INVOKEINTERFACE:
            case Instruction.OP_INVOKEDYNAMIC:

            case Instruction.OP_NEW:
            case Instruction.OP_ANEWARRAY:
            case Instruction.OP_CHECKCAST:
            case Instruction.OP_INSTANCEOF:
            case Instruction.OP_MULTIANEWARRAY:
                return new ConstantInstruction();

            // Instructions with a local variable index.
            case Instruction.OP_ILOAD:
            case Instruction.OP_LLOAD:
            case Instruction.OP_FLOAD:
            case Instruction.OP_DLOAD:
            case Instruction.OP_ALOAD:
            case Instruction.OP_ILOAD_0:
            case Instruction.OP_ILOAD_1:
            case Instruction.OP_ILOAD_2:
            case Instruction.OP_ILOAD_3:
            case Instruction.OP_LLOAD_0:
            case Instruction.OP_LLOAD_1:
            case Instruction.OP_LLOAD_2:
            case Instruction.OP_LLOAD_3:
            case Instruction.OP_FLOAD_0:
            case Instruction.OP_FLOAD_1:
            case Instruction.OP_FLOAD_2:
            case Instruction.OP_FLOAD_3:
            case Instruction.OP_DLOAD_0:
            case Instruction.OP_DLOAD_1:
            case Instruction.OP_DLOAD_2:
            case Instruction.OP_DLOAD_3:
            case Instruction.OP_ALOAD_0:
            case Instruction.OP_ALOAD_1:
            case Instruction.OP_ALOAD_2:
            case Instruction.OP_ALOAD_3:

            case Instruction.OP_ISTORE:
            case Instruction.OP_LSTORE:
            case Instruction.OP_FSTORE:
            case Instruction.OP_DSTORE:
            case Instruction.OP_ASTORE:
            case Instruction.OP_ISTORE_0:
            case Instruction.OP_ISTORE_1:
            case Instruction.OP_ISTORE_2:
            case Instruction.OP_ISTORE_3:
            case Instruction.OP_LSTORE_0:
            case Instruction.OP_LSTORE_1:
            case Instruction.OP_LSTORE_2:
            case Instruction.OP_LSTORE_3:
            case Instruction.OP_FSTORE_0:
            case Instruction.OP_FSTORE_1:
            case Instruction.OP_FSTORE_2:
            case Instruction.OP_FSTORE_3:
            case Instruction.OP_DSTORE_0:
            case Instruction.OP_DSTORE_1:
            case Instruction.OP_DSTORE_2:
            case Instruction.OP_DSTORE_3:
            case Instruction.OP_ASTORE_0:
            case Instruction.OP_ASTORE_1:
            case Instruction.OP_ASTORE_2:
            case Instruction.OP_ASTORE_3:

            case Instruction.OP_IINC:

            case Instruction.OP_RET:
                return new VariableInstruction(wide);

            // Instructions with a branch offset operand.
            case Instruction.OP_IFEQ:
            case Instruction.OP_IFNE:
            case Instruction.OP_IFLT:
            case Instruction.OP_IFGE:
            case Instruction.OP_IFGT:
            case Instruction.OP_IFLE:
            case Instruction.OP_IFICMPEQ:
            case Instruction.OP_IFICMPNE:
            case Instruction.OP_IFICMPLT:
            case Instruction.OP_IFICMPGE:
            case Instruction.OP_IFICMPGT:
            case Instruction.OP_IFICMPLE:
            case Instruction.OP_IFACMPEQ:
            case Instruction.OP_IFACMPNE:
            case Instruction.OP_GOTO:
            case Instruction.OP_JSR:

            case Instruction.OP_IFNULL:
            case Instruction.OP_IFNONNULL:

            case Instruction.OP_GOTO_W:
            case Instruction.OP_JSR_W:
                return new BranchInstruction();

            //  The tableswitch instruction.
            case Instruction.OP_TABLESWITCH:
                return new TableSwitchInstruction();

            //  The lookupswitch instruction.
            case Instruction.OP_LOOKUPSWITCH:
                return new LookUpSwitchInstruction();

            default:
                throw new IllegalArgumentException("Unknown instruction opcode ["+opcode+"]");
        }
    }
}
