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

import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;


/**
 * This <code>ClassVisitor</code> delegates its visits to another given
 * <code>ClassVisitor</code>, but only when the visited class
 * has the proper processing flags.
 *
 * @see proguard.util.ProcessingFlags
 *
 * @author Johan Leys
 */
public class ClassProcessingFlagFilter implements ClassVisitor
{
    private final int          requiredSetProcessingFlags;
    private final int          requiredUnsetProcessingFlags;
    private final ClassVisitor classVisitor;


    /**
     * Creates a new ClassProcessingFlagFilter.
     *
     * @param requiredSetProcessingFlags   the class processing flags that should be set.
     * @param requiredUnsetProcessingFlags the class processing flags that should be unset.
     * @param classVisitor                 the <code>ClassVisitor</code> to which visits will be delegated.
     */
    public ClassProcessingFlagFilter(int          requiredSetProcessingFlags,
                                     int          requiredUnsetProcessingFlags,
                                     ClassVisitor classVisitor)
    {
        this.requiredSetProcessingFlags   = requiredSetProcessingFlags;
        this.requiredUnsetProcessingFlags = requiredUnsetProcessingFlags;
        this.classVisitor                 = classVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        if (accepted(programClass.getProcessingFlags()))
        {
            classVisitor.visitProgramClass(programClass);
        }
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        if (accepted(libraryClass.getProcessingFlags()))
        {
            classVisitor.visitLibraryClass(libraryClass);
        }
    }


    // Small utility methods.

    private boolean accepted(int processingFlags)
    {
        return (requiredSetProcessingFlags & ~processingFlags) == 0 &&
               (requiredUnsetProcessingFlags &  processingFlags) == 0;
    }
}
