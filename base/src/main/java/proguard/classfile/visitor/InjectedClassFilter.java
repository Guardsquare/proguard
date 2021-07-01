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
package proguard.classfile.visitor;

import proguard.classfile.*;
import proguard.util.ProcessingFlags;


/**
 * This ClassVisitor delegates to one of two other visitors, depending on
 * whether the visited class was injected or not.
 *
 * @author Johan Leys
 */
public class InjectedClassFilter
implements   ClassVisitor
{
    private final ClassVisitor injectedClassVisitor;
    private final ClassVisitor otherClassVisitor;


    public InjectedClassFilter(ClassVisitor injectedClassVisitor,
                               ClassVisitor otherClassVisitor)
    {
        this.injectedClassVisitor = injectedClassVisitor;
        this.otherClassVisitor    = otherClassVisitor;
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz)
    {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support " + clazz.getClass().getName());
    }

    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        ClassVisitor delegate = delegateVisitor(programClass);
        if (delegate != null)
        {
            delegate.visitProgramClass(programClass);
        }
    }


    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        if (otherClassVisitor != null)
        {
            otherClassVisitor.visitLibraryClass(libraryClass);
        }
    }


    // Small utility methods.

    private ClassVisitor delegateVisitor(ProgramClass programClass)
    {
        return (programClass.processingFlags & ProcessingFlags.INJECTED) != 0 ?
            injectedClassVisitor : otherClassVisitor;
    }
}
