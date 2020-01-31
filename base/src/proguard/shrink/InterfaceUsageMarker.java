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
package proguard.shrink;

import proguard.classfile.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.ClassVisitor;

/**
 * This ClassVisitor recursively marks all interface
 * classes that are being used in the visited class.
 *
 * @see ClassUsageMarker
 *
 * @author Eric Lafortune
 */
public class InterfaceUsageMarker
extends      SimplifiedVisitor
implements   ClassVisitor,
             ConstantVisitor
{
    private final ClassUsageMarker classUsageMarker;

    // Fields acting as return parameters for the visitor methods.
    private boolean used;
    private boolean anyUsed;


    /**
     * Creates a new InterfaceUsageMarker.
     * @param classUsageMarker the marker to mark and check the classes and
     *                         class members.
     */
    public InterfaceUsageMarker(ClassUsageMarker classUsageMarker)
    {
        this.classUsageMarker = classUsageMarker;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        boolean classUsed         = classUsageMarker.isUsed(programClass);
        boolean classPossiblyUsed = classUsageMarker.isPossiblyUsed(programClass);

        if (classUsed || classPossiblyUsed)
        {
            // Check if any interfaces are being used.
            boolean oldAnyUsed = anyUsed;
            anyUsed = false;

            programClass.interfaceConstantsAccept(this);

            classUsed |= anyUsed;
            anyUsed = oldAnyUsed;

            // Is this an interface with a preliminary mark?
            if (classPossiblyUsed)
            {
                // Should it be included now?
                if (classUsed)
                {
                    // At least one if this interface's interfaces is being used.
                    // Mark this interface as well.
                    classUsageMarker.markAsUsed(programClass);

                    // Mark this interface's name.
                    programClass.thisClassConstantAccept(this);

                    // Mark the superclass (java/lang/Object).
                    programClass.superClassConstantAccept(this);
                }
                else
                {
                    // Unmark this interface, so we don't bother looking at it again.
                    classUsageMarker.markAsUnused(programClass);
                }
            }
        }

        // The return value.
        used = classUsed;
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // The return values.
        used    = true;
        anyUsed = true;
    }


    // Implementations for ConstantVisitor.

    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        boolean classUsed = classUsageMarker.isUsed(classConstant);

        if (!classUsed)
        {
            // The ClassConstant isn't marked as being used yet. But maybe it
            // should be included as an interface, so check the actual class.
            classConstant.referencedClassAccept(this);
            classUsed = used;

            if (classUsed)
            {
                // The class is being used. Mark the ClassConstant as being used
                // as well.
                classUsageMarker.markAsUsed(classConstant);

                clazz.constantPoolEntryAccept(classConstant.u2nameIndex, this);
            }
        }

        // The return values.
        used    =  classUsed;
        anyUsed |= classUsed;
    }


    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
    {
        if (!classUsageMarker.isUsed(utf8Constant))
        {
            classUsageMarker.markAsUsed(utf8Constant);
        }
    }
}
