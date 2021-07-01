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
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.*;

/**
 * This RecordComponentInfoVisitor marks all record components that
 * it visits and whose corresponding fields have been marked before.
 *
 * @see ClassUsageMarker
 *
 * @author Eric Lafortune
 */
public class RecordComponentUsageMarker
implements   RecordComponentInfoVisitor,
             MemberVisitor,
             ConstantVisitor
{
    private final ClassUsageMarker classUsageMarker;

    // Field acting as a return parameter.
    private boolean fieldUsed;


    /**
     * Creates a new InnerUsageMarker.
     * @param classUsageMarker the marker to mark and check the classes and
     *                         class members.
     */
    public RecordComponentUsageMarker(ClassUsageMarker classUsageMarker)
    {
        this.classUsageMarker = classUsageMarker;
    }


    // Implementations for RecordComponentInfoVisitor.

    public void visitRecordComponentInfo(Clazz clazz, RecordComponentInfo recordComponentInfo)
    {
        // Is the field that corresponds to the component used?
        fieldUsed = false;
        recordComponentInfo.referencedFieldAccept(clazz, this);
        if (fieldUsed)
        {
            // Mark the component.
            classUsageMarker.markAsUsed(recordComponentInfo);

            // Mark its name and descriptor.
            markConstant(clazz, recordComponentInfo.u2nameIndex);
            markConstant(clazz, recordComponentInfo.u2descriptorIndex);

            // Mark its attributes.
            recordComponentInfo.attributesAccept(clazz, classUsageMarker);
        }
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        fieldUsed = classUsageMarker.isUsed(programField);
    }


    // Implementations for ConstantVisitor.

    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
    {
        classUsageMarker.markAsUsed(utf8Constant);
    }


    // Small utility methods.

    /**
     * Marks the given constant pool entry of the given class. This includes
     * visiting any other referenced constant pool entries.
     */
    private void markConstant(Clazz clazz, int index)
    {
         clazz.constantPoolEntryAccept(index, this);
    }
}
