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
import proguard.classfile.visitor.*;

/**
 * This MemberVisitor marks all classes that contain any kind of constructors.
 *
 * @author Joachim Vandersmissen
 */
public class ContainsConstructorsMarker
implements   MemberVisitor
{
    // Implementations for MemberVisitor.

    public void visitAnyMember(Clazz clazz, Member member) {}


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        if (programMethod.getName(programClass).equals(ClassConstants.METHOD_NAME_INIT))
        {
            ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setContainsConstructors();
        }
    }


    public static boolean containsConstructors(Clazz clazz)
    {
        return ClassOptimizationInfo.getClassOptimizationInfo(clazz).containsConstructors();
    }
}
