/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
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
 * This InstructionVisitor marks all fields that are write-only.
 *
 * @author Eric Lafortune
 */
public class ReadWriteFieldMarker
implements   InstructionVisitor,
             ConstantVisitor,
             MemberVisitor
{
    //*
    private static final boolean DEBUG = false;
    /*/
    private static       boolean DEBUG = System.getProperty("rwfm") != null;
    //*/


    private final MutableBoolean repeatTrigger;
    private final boolean        markReading;
    private final boolean        markWriting;


    // Parameters for the visitor methods.
    // We'll set them to true by default, in case this class is being used
    // as a field visitor.
    private boolean reading = true;
    private boolean writing = true;


    /**
     * Creates a new ReadWriteFieldMarker that marks fields that are read and
     * fields that are written.
     * @param repeatTrigger a mutable boolean flag that is set whenever a field
     *                      gets a mark that it didn't have before.
     */
    public ReadWriteFieldMarker(MutableBoolean repeatTrigger)
    {
        this(repeatTrigger, true, true);
    }


    /**
     * Creates a new ReadWriteFieldMarker that marks fields that are read and
     * fields that are written, as specified.
     * @param repeatTrigger a mutable boolean flag that is set whenever a field
     *                      gets a mark that it didn't have before.
     * @param markReading   specifies whether fields may be marked as read.
     * @param markWriting   specifies whether fields may be marked as written.
     */
    public ReadWriteFieldMarker(MutableBoolean repeatTrigger,
                                boolean        markReading,
                                boolean        markWriting)
    {
        this.repeatTrigger = repeatTrigger;
        this.markReading   = markReading;
        this.markWriting   = markWriting;
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        byte opcode = constantInstruction.opcode;

        // Check for instructions that involve fields.
        switch (opcode)
        {
            case Instruction.OP_LDC:
            case Instruction.OP_LDC_W:
                // Mark the field, if any, as being read from and written to.
                reading = true;
                writing = true;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;

            case Instruction.OP_GETSTATIC:
            case Instruction.OP_GETFIELD:
                // Mark the field as being read from.
                reading = true;
                writing = false;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;

            case Instruction.OP_PUTSTATIC:
            case Instruction.OP_PUTFIELD:
                // Mark the field as being written to.
                reading = false;
                writing = true;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        // Mark the referenced field, if any.
        stringConstant.referencedMemberAccept(this);
    }


    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        // Mark the referenced field.
        fieldrefConstant.referencedFieldAccept(this);
    }


    // Implementations for MemberVisitor.

    public void visitAnyMember(Clazz Clazz, Member member) {}


    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        // Mark the field if it is being read from.
        if (markReading && reading)
        {
            markAsRead(programClass, programField);
        }

        // Mark the field if it is being written to.
        if (markWriting && writing)
        {
            markAsWritten(programClass, programField);
        }
    }


    // Small utility methods.

    private void markAsRead(Clazz clazz, Field field)
    {
        FieldOptimizationInfo fieldOptimizationInfo =
            FieldOptimizationInfo.getFieldOptimizationInfo(field);

        if (!fieldOptimizationInfo.isRead() &&
            fieldOptimizationInfo instanceof ProgramFieldOptimizationInfo)
        {
            if (DEBUG)
            {
                System.out.println("ReadWriteFieldMarker: marking as read: "+clazz.getName()+"."+field.getName(clazz));
            }

            ((ProgramFieldOptimizationInfo)fieldOptimizationInfo).setRead();

            repeatTrigger.set();
        }
    }


    public static boolean isRead(Field field)
    {
        return FieldOptimizationInfo.getFieldOptimizationInfo(field).isRead();
    }


    private void markAsWritten(Clazz clazz, Field field)
    {
        FieldOptimizationInfo fieldOptimizationInfo =
            FieldOptimizationInfo.getFieldOptimizationInfo(field);

        if (!fieldOptimizationInfo.isWritten() &&
            fieldOptimizationInfo instanceof ProgramFieldOptimizationInfo)
        {
            if (DEBUG)
            {
                System.out.println("ReadWriteFieldMarker: marking as written: "+clazz.getName()+"."+field.getName(clazz));
            }

            ((ProgramFieldOptimizationInfo)fieldOptimizationInfo).setWritten();

            repeatTrigger.set();
        }
    }


    public static boolean isWritten(Field field)
    {
        return FieldOptimizationInfo.getFieldOptimizationInfo(field).isWritten();
    }
}
