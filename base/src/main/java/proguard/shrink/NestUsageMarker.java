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
import proguard.classfile.visitor.ClassVisitor;

/**
 * This AttributeVisitor marks all necessary nest host attributes, nest
 * members attributes, and permitted subclasses attributes that it visits.
 *
 * @see UsageMarker
 *
 * @author Eric Lafortune
 */
public class NestUsageMarker
implements   AttributeVisitor,
             ConstantVisitor,
             ClassVisitor
{
    private final ClassUsageMarker classUsageMarker;

    // Fields acting as return parameters for several methods.
    private boolean attributeUsed;
    private boolean classUsed;


    /**
     * Creates a new NestUsageMarker.
     * @param classUsageMarker the marker to mark and check the classes and
     *                         class members.
     */
    public NestUsageMarker(ClassUsageMarker classUsageMarker)
    {
        this.classUsageMarker = classUsageMarker;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitNestHostAttribute(Clazz clazz, NestHostAttribute nestHostAttribute)
    {
        // Mark the necessary nest host class constant.
        attributeUsed = false;
        clazz.constantPoolEntryAccept(nestHostAttribute.u2hostClassIndex, this);

        if (attributeUsed)
        {
            // We got a positive used flag, so the nest host class is being used.
            // Mark this attribute as being used as well.
            classUsageMarker.markAsUsed(nestHostAttribute);

            markConstant(clazz, nestHostAttribute.u2attributeNameIndex);
        }
    }


    public void visitNestMembersAttribute(Clazz clazz, NestMembersAttribute nestMembersAttribute)
    {
        // Mark the necessary nest member information.
        attributeUsed = false;
        nestMembersAttribute.memberClassConstantsAccept(clazz, this);

        if (attributeUsed)
        {
            // We got a positive used flag, so at least one of the nest members
            // is being used. Mark this attribute as being used as well.
            classUsageMarker.markAsUsed(nestMembersAttribute);

            markConstant(clazz, nestMembersAttribute.u2attributeNameIndex);
        }
    }


    public void visitPermittedSubclassesAttribute(Clazz clazz, PermittedSubclassesAttribute permittedSubclassesAttribute)
    {
        // Mark the necessary permitted subclasses information.
        attributeUsed = false;
        permittedSubclassesAttribute.permittedSubclassConstantsAccept(clazz, this);

        if (attributeUsed)
        {
            // We got a positive used flag, so at least one of the permitted
            // subclasses class is being used. Mark this attribute as being
            // used as well.
            classUsageMarker.markAsUsed(permittedSubclassesAttribute);

            markConstant(clazz, permittedSubclassesAttribute.u2attributeNameIndex);
        }
    }


    // Implementations for ConstantVisitor.

    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        classUsed = classUsageMarker.isUsed(classConstant);

        // Is the class constant marked as being used?
        if (!classUsed)
        {
            // Check the referenced class.
            classUsed = true;
            classConstant.referencedClassAccept(this);

            // Is the referenced class marked as being used?
            if (classUsed)
            {
                // Mark the class constant and its Utf8 constant.
                classUsageMarker.markAsUsed(classConstant);

                markConstant(clazz, classConstant.u2nameIndex);
            }
        }

        // The return value.
        attributeUsed |= classUsed;
    }


    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
    {
        classUsageMarker.markAsUsed(utf8Constant);
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
        classUsed = classUsageMarker.isUsed(programClass);
    }


    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        classUsed = true;
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
