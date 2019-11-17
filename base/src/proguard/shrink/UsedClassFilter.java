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
package proguard.shrink;

import proguard.classfile.*;
import proguard.classfile.visitor.ClassVisitor;

/**
 * This ClassVisitor delegates all its method calls to another ClassVisitor,
 * depending on whether the Clazz objects are marked as used.
 *
 * @see ClassUsageMarker
 *
 * @author Eric Lafortune
 */
public class UsedClassFilter
implements   ClassVisitor
{
    private final SimpleUsageMarker usageMarker;
    private final ClassVisitor      usedClassVisitor;
    private final ClassVisitor      unusedClassVisitor;


    /**
     * Creates a new UsedClassFilter.
     * @param usageMarker        the usage marker that is used to mark the
     *                           classes and class members.
     * @param usedClassVisitor   the class visitor to which the visiting
     *                           used classes will be delegated.
     */
    public UsedClassFilter(SimpleUsageMarker usageMarker,
                           ClassVisitor      usedClassVisitor)
    {
        this(usageMarker, usedClassVisitor, null);
    }


    /**
     * Creates a new UsedClassFilter.
     * @param usageMarker        the usage marker that is used to mark the
     *                           classes and class members.
     * @param usedClassVisitor   the class visitor to which the visiting
     *                           used classes will be delegated.
     * @param unusedClassVisitor the class visitor to which the visiting
     *                           unused classes will be delegated.
     */
    public UsedClassFilter(SimpleUsageMarker usageMarker,
                           ClassVisitor      usedClassVisitor,
                           ClassVisitor      unusedClassVisitor)
    {
        this.usageMarker        = usageMarker;
        this.usedClassVisitor   = usedClassVisitor;
        this.unusedClassVisitor = unusedClassVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        // Is the class marked?
        ClassVisitor classVisitor = usageMarker.isUsed(programClass) ?
            usedClassVisitor :
            unusedClassVisitor;

        if (classVisitor != null)
        {
            classVisitor.visitProgramClass(programClass);
        }
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Is the class marked?
        ClassVisitor classVisitor = usageMarker.isUsed(libraryClass) ?
            usedClassVisitor :
            unusedClassVisitor;

        if (classVisitor != null)
        {
            classVisitor.visitLibraryClass(libraryClass);
        }
    }
}
