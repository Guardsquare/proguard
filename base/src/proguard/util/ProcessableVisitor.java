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
package proguard.util;

import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.*;
import proguard.resources.arsc.*;
import proguard.resources.arsc.visitor.ResourceTableTypeEntryVisitor;
import proguard.resources.file.ResourceFile;
import proguard.resources.file.visitor.ResourceFileVisitor;

/**
 *  This interface defines visitor methods for the main Processable implementations.
 */
public interface ProcessableVisitor
extends          ClassVisitor,
                 MemberVisitor,
                 AttributeVisitor,
                 ResourceFileVisitor,
                 ResourceTableTypeEntryVisitor
{
    /**
     * Visits any Processable instance. The more specific default implementations of
     * this interface delegate to this method.
     */
    default void visitAnyProcessable(Processable processable)
    {
        throw new UnsupportedOperationException(this.getClass().getName()+" does not support "+processable.getClass().getName());
    }


    // Implementations for ClassVisitor.

    default void visitAnyClass(Clazz clazz)
    {
        visitAnyProcessable(clazz);
    }


    // Implementations for MemberVisitor.

    default void visitAnyMember(Clazz clazz, Member member)
    {
        visitAnyProcessable(member);
    }


    // Implementations for AttributeVisitor.

    default void visitAnyAttribute(Clazz clazz, Attribute attribute)
    {
        visitAnyProcessable(attribute);
    }


    // Implementations for ResourceFileVisitor.

    default void visitAnyResourceFile(ResourceFile resourceFile)
    {
        visitAnyProcessable(resourceFile);
    }


    // Implementations for ResourceFileVisitor.

    default void visitAnyResourceTableTypeEntry(ResourceTable resourceTable, ResourceTablePackage resourceTablePackage, ResourceTableType resourceTableType, int entryIndex, ResourceTableTypeEntry resourceTableTypeEntry)
    {
        visitAnyProcessable(resourceTableTypeEntry);
    }
}
