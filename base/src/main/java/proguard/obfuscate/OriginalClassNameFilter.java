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
package proguard.obfuscate;

import proguard.classfile.*;
import proguard.classfile.visitor.ClassVisitor;

/**
 * This ClassVisitor delegates its visits to one of two other ClassVisitor's,
 * depending on whether the visited class still has its original name or not.
 *
 * @see ClassObfuscator
 *
 * @author Johan Leys
 */
public class OriginalClassNameFilter
implements   ClassVisitor
{
    private final ClassVisitor acceptedClassVisitor;
    private final ClassVisitor rejectedClassVisitor;


    /**
     * Creates a new OriginalClassNameFilter.
     *
     * @param acceptedClassVisitor the class visitor to which accepted classes
     *                             (classes that still have their original
     *                             name) will be delegated.
     * @param rejectedClassVisitor the class visitor to which rejected classes
     *                             (classes that have a changed name) will be
     *                             delegated.
     */
    public OriginalClassNameFilter(ClassVisitor acceptedClassVisitor,
                                   ClassVisitor rejectedClassVisitor)
    {
        this.acceptedClassVisitor = acceptedClassVisitor;
        this.rejectedClassVisitor = rejectedClassVisitor;
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz)
    {
        ClassVisitor delegateVisitor = selectVisitor(clazz);
        if (delegateVisitor != null)
        {
            clazz.accept(delegateVisitor);
        }
    }

    // Small utility methods.

    private ClassVisitor selectVisitor(Clazz clazz)
    {
        return ClassObfuscator.hasOriginalClassName(clazz) ?
            acceptedClassVisitor : rejectedClassVisitor;
    }
}