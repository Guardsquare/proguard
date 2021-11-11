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

/**
 * This InstructionVisitor marks all methods that invoke super methods (other
 * than initializers) from the instructions that it visits.
 *
 * @author Eric Lafortune
 */
public class SuperInvocationMarker
implements   InstructionVisitor,
             ConstantVisitor
{
    private boolean invokesSuperMethods;


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        if (constantInstruction.opcode == Instruction.OP_INVOKESPECIAL)
        {
            invokesSuperMethods = false;

            clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);

            if (invokesSuperMethods)
            {
                setInvokesSuperMethods(method);
            }
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        invokesSuperMethods =
            !clazz.equals(anyMethodrefConstant.referencedClass) &&
            !anyMethodrefConstant.getName(clazz).equals(ClassConstants.METHOD_NAME_INIT);
    }


    // Small utility methods.

    private static void setInvokesSuperMethods(Method method)
    {
        ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method).setInvokesSuperMethods();
    }


    public static boolean invokesSuperMethods(Method method)
    {
        return MethodOptimizationInfo.getMethodOptimizationInfo(method).invokesSuperMethods();
    }
}
