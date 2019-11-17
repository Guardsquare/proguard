/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
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
package proguard.classfile.visitor;

import proguard.classfile.*;


/**
 * This <code>ClassVisitor</code> delegates its visits to one of two other given
 * <code>ClassVisitor</code> instances, depending on whether they have any
 * subclasses or not.
 *
 * @author Eric Lafortune
 */
public class BottomClassFilter implements ClassVisitor
{
    private final ClassVisitor bottomClassVisitor;
    private final ClassVisitor otherClassVisitor;

    /**
     * Creates a new BottomClassFilter.
     * @param bottomClassVisitor the <code>ClassVisitor</code> to which visits
     *                           to bottom classes will be delegated.
     */
    public BottomClassFilter(ClassVisitor bottomClassVisitor)
    {
        this(bottomClassVisitor, null);
    }


    /**
     * Creates a new BottomClassFilter.
     * @param bottomClassVisitor the <code>ClassVisitor</code> to which visits
     *                           to bottom classes will be delegated.
     * @param otherClassVisitor  the <code>ClassVisitor</code> to which visits
     *                           to non-bottom classes will be delegated.
     */
    public BottomClassFilter(ClassVisitor bottomClassVisitor,
                             ClassVisitor otherClassVisitor)
    {
        this.bottomClassVisitor = bottomClassVisitor;
        this.otherClassVisitor  = otherClassVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        // Is this a bottom class in the class hierarchy?
        ClassVisitor classVisitor = programClass.subClasses == null ?
            bottomClassVisitor :
            otherClassVisitor;

        if (classVisitor != null)
        {
            classVisitor.visitProgramClass(programClass);
        }
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Is this a bottom class in the class hierarchy?
        ClassVisitor classVisitor = libraryClass.subClasses == null ?
            bottomClassVisitor :
            otherClassVisitor;

        if (classVisitor != null)
        {
            classVisitor.visitLibraryClass(libraryClass);
        }
    }
}
