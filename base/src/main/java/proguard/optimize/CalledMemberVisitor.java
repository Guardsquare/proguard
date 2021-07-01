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
package proguard.optimize;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.*;

/**
 * This InstructionVisitor visits all members that can be executed because of an instruction.
 * Explicitly (invoke instructions) or implicitly (class initialisation methods)
 */
public class CalledMemberVisitor
implements   InstructionVisitor
{
    private final MemberVisitor        memberVisitor;
    private final MemberToClassVisitor staticClassInitializer;


    public CalledMemberVisitor(MemberVisitor memberVisitor)
    {
        this.memberVisitor = memberVisitor;
        this.staticClassInitializer =
            new MemberToClassVisitor(
            new NamedMethodVisitor(ClassConstants.METHOD_NAME_CLINIT, null, memberVisitor));
    }


    // Implementations for InstructionVisitor

    public void visitAnyInstruction(Clazz clazz,
                                    Method method,
                                    CodeAttribute codeAttribute,
                                    int offset,
                                    Instruction instruction)
    {
    }


    public void visitConstantInstruction(Clazz clazz,
                                         Method method,
                                         CodeAttribute codeAttribute,
                                         int offset,
                                         ConstantInstruction constantInstruction)
    {

        switch (constantInstruction.opcode)
        {
            case Instruction.OP_INVOKEVIRTUAL:
            case Instruction.OP_INVOKESPECIAL:
            case Instruction.OP_INVOKESTATIC:
            case Instruction.OP_INVOKEINTERFACE:
            case Instruction.OP_INVOKEDYNAMIC:
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex,
                                              new ReferencedMemberVisitor(
                                              new MultiMemberVisitor(memberVisitor,staticClassInitializer)));
                break;

            case Instruction.OP_GETSTATIC:
            case Instruction.OP_PUTSTATIC:
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex,
                                              new ReferencedMemberVisitor(staticClassInitializer));
                break;
        }
    }
}
