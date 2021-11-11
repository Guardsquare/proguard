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
import proguard.classfile.visitor.MemberVisitor;

/**
 * This MemberVisitor marks all methods that it visits as not having any
 * escaping parameters (including 'this'). It will make the
 * ParameterEscapeMarker consider them as such without further analysis.
 *
 * @see ParameterEscapeMarker
 * @author Eric Lafortune
 */
public class NoEscapingParametersMethodMarker
implements   MemberVisitor
{
    // Implementations for MemberVisitor.

    public void visitAnyMember(Clazz Clazz, Member member)
    {
        // Ignore any attempts to mark fields.
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        markNoParameterEscaping(programMethod);
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        markNoParameterEscaping(libraryMethod);
    }


    // Small utility methods.

    private static void markNoParameterEscaping(Method method)
    {
        MethodOptimizationInfo.getMethodOptimizationInfo(method).setNoEscapingParameters();
    }


    public static boolean hasNoParameterEscaping(Method method)
    {
        return MethodOptimizationInfo.getMethodOptimizationInfo(method).hasNoEscapingParameters();
    }
}
