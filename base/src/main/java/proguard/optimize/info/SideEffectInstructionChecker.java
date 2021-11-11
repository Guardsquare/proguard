/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.optimize.info;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.MemberVisitor;

/**
 * This class can tell whether an instruction has any side effects. This
 * includes invoking methods that have side effects, writing to fields that
 * are not write-only, and throwing exceptions. Return instructions and
 * array store instructions can be included or not.
 *
 * With the environment setting "optimize.conservatively", it also accounts for
 * all possible NullPointerExceptions, ArrayIndexOutOfBoundExceptions, etc.,
 * which are typically accidental, not intentional.
 *
 * @see ReadWriteFieldMarker
 * @see SideEffectClassMarker
 * @see NoSideEffectMethodMarker
 * @see SideEffectMethodMarker
 * @author Eric Lafortune
 */
public class SideEffectInstructionChecker
implements   InstructionVisitor,
             ConstantVisitor,
             MemberVisitor
{
    public static final boolean OPTIMIZE_CONSERVATIVELY = System.getProperty("optimize.conservatively") != null;


    private final boolean includeReturnInstructions;
    private final boolean includeArrayStoreInstructions;

    // Parameters and return values for the visitor methods.
    private boolean writingField;
    private Clazz   referencingClass;
    private boolean hasSideEffects;


    /**
     * Creates a new SideEffectInstructionChecker
     * @param includeReturnInstructions     specifies whether return
     *                                      instructions count as side
     *                                      effects.
     * @param includeArrayStoreInstructions specifies whether storing values
     *                                      in arrays counts as side effects.
     */
    public SideEffectInstructionChecker(boolean includeReturnInstructions,
                                        boolean includeArrayStoreInstructions)
    {
        this.includeReturnInstructions     = includeReturnInstructions;
        this.includeArrayStoreInstructions = includeArrayStoreInstructions;
    }


    public boolean hasSideEffects(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
        hasSideEffects = false;

        instruction.accept(clazz, method, codeAttribute, offset, this);

        return hasSideEffects;
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
    {
        byte opcode = simpleInstruction.opcode;

        // Check for instructions that might cause side effects.
        switch (opcode)
        {
            case Instruction.OP_IDIV:
            case Instruction.OP_LDIV:
            case Instruction.OP_IREM:
            case Instruction.OP_LREM:
            case Instruction.OP_FDIV:
            case Instruction.OP_FREM:
            case Instruction.OP_DDIV:
            case Instruction.OP_DREM:
            case Instruction.OP_IALOAD:
            case Instruction.OP_LALOAD:
            case Instruction.OP_FALOAD:
            case Instruction.OP_DALOAD:
            case Instruction.OP_AALOAD:
            case Instruction.OP_BALOAD:
            case Instruction.OP_CALOAD:
            case Instruction.OP_SALOAD:
            case Instruction.OP_NEWARRAY:
            case Instruction.OP_ARRAYLENGTH:
                // These instructions strictly taken may cause a side effect
                // (ArithmeticException, NullPointerException,
                // ArrayIndexOutOfBoundsException, NegativeArraySizeException).
                hasSideEffects = OPTIMIZE_CONSERVATIVELY;
                break;

            case Instruction.OP_IASTORE:
            case Instruction.OP_LASTORE:
            case Instruction.OP_FASTORE:
            case Instruction.OP_DASTORE:
            case Instruction.OP_AASTORE:
            case Instruction.OP_BASTORE:
            case Instruction.OP_CASTORE:
            case Instruction.OP_SASTORE:
                // These instructions may cause a side effect.
                hasSideEffects = includeArrayStoreInstructions;
                break;

            case Instruction.OP_ATHROW :
            case Instruction.OP_MONITORENTER:
            case Instruction.OP_MONITOREXIT:
                // These instructions always cause a side effect.
                hasSideEffects = true;
                break;

            case Instruction.OP_IRETURN:
            case Instruction.OP_LRETURN:
            case Instruction.OP_FRETURN:
            case Instruction.OP_DRETURN:
            case Instruction.OP_ARETURN:
            case Instruction.OP_RETURN:
                // These instructions may have a side effect.
                hasSideEffects = includeReturnInstructions;
                break;
        }
    }


    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction)
    {
        byte opcode = variableInstruction.opcode;

        // Check for instructions that might cause side effects.
        switch (opcode)
        {
            case Instruction.OP_RET:
                // This instruction may have a side effect.
                hasSideEffects = includeReturnInstructions;
                break;
        }
    }


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        byte opcode = constantInstruction.opcode;

        // Check for instructions that might cause side effects.
        switch (opcode)
        {
            case Instruction.OP_GETSTATIC:
                // Check if accessing the field might cause any side effects.
                writingField = false;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;

            case Instruction.OP_PUTSTATIC:
                // Check if accessing the field might cause any side effects.
                writingField = true;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;

            case Instruction.OP_GETFIELD:
                if (OPTIMIZE_CONSERVATIVELY)
                {
                    // These instructions strictly taken may cause a side effect
                    // (NullPointerException).
                    hasSideEffects = true;
                }
                else
                {
                    // Check if the field is write-only or volatile.
                    writingField = false;
                    clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                }
                break;

            case Instruction.OP_PUTFIELD:
                if (OPTIMIZE_CONSERVATIVELY)
                {
                    // These instructions strictly taken may cause a side effect
                    // (NullPointerException).
                    hasSideEffects = true;
                }
                else
                {
                    // Check if the field is write-only or volatile.
                    writingField = true;
                    clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                }
                break;

            case Instruction.OP_INVOKESPECIAL:
            case Instruction.OP_INVOKESTATIC:
                // Check if the invoked method is causing any side effects.
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;

            case Instruction.OP_INVOKEVIRTUAL:
            case Instruction.OP_INVOKEINTERFACE:
            case Instruction.OP_INVOKEDYNAMIC:
                if (OPTIMIZE_CONSERVATIVELY)
                {
                    // These instructions strictly taken may cause a side effect
                    // (NullPointerException).
                    hasSideEffects = true;
                }
                else
                {
                    // Check if the invoked method is causing any side effects.
                    clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                }
                break;

            case Instruction.OP_ANEWARRAY:
            case Instruction.OP_MULTIANEWARRAY:
            case Instruction.OP_CHECKCAST:
                // This instructions strictly taken may cause a side effect
                // (ClassCastException, NegativeArraySizeException).
                hasSideEffects = OPTIMIZE_CONSERVATIVELY;
                break;
        }
    }


    public void visitBranchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, BranchInstruction branchInstruction)
    {
        byte opcode = branchInstruction.opcode;

        // Check for instructions that might cause side effects.
        switch (opcode)
        {
            case Instruction.OP_JSR:
            case Instruction.OP_JSR_W:
                hasSideEffects = includeReturnInstructions;
                break;
        }
    }


    // Implementations for ConstantVisitor.

    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        // We'll have to assume invoking an unknown method has side effects.
        hasSideEffects = true;
    }


    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        // Pass the referencing class.
        referencingClass = clazz;

        // We'll have to assume accessing an unknown field has side effects.
        hasSideEffects = true;

        // Check the referenced field, if known.
        fieldrefConstant.referencedFieldAccept(this);
    }


    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        // Pass the referencing class.
        referencingClass = clazz;

        // We'll have to assume invoking an unknown method has side effects.
        hasSideEffects = true;

        // Check the referenced method, if known.
        anyMethodrefConstant.referencedMethodAccept(this);
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        hasSideEffects =
            (writingField && ReadWriteFieldMarker.isRead(programField))        ||
            (programField.getAccessFlags() & AccessConstants.VOLATILE) != 0 ||
            SideEffectClassChecker.mayHaveSideEffects(referencingClass,
                                                      programClass,
                                                      programField);
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        // Note that side effects already include synchronization of some
        // implementation of the method.
        hasSideEffects =
            SideEffectMethodMarker.hasSideEffects(programMethod) ||
            SideEffectClassChecker.mayHaveSideEffects(referencingClass,
                                                      programClass,
                                                      programMethod);
    }


    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        hasSideEffects = true;
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        hasSideEffects =
            !NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod);
    }
}
