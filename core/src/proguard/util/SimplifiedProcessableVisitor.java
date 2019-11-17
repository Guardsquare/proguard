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
package proguard.util;

import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.resources.file.ResourceFile;

/**
 * This abstract utility class allows to implement visitor interfaces for various Processable entities.
 * The provided methods all delegate to a single method that allows to visit any Processable in a single implementation.
 *
 * @author Johan Leys
 */
public abstract class SimplifiedProcessableVisitor
extends               SimplifiedVisitor
{
    /**
     * Visits any Processable entity.
     */
    public void visitAnyProcessable(Processable processable)
    {
        throw new UnsupportedOperationException("Method must be overridden in [" + this.getClass().getName() + "] if ever called");
    }


    // Simplifications for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz)
    {
        visitAnyProcessable(clazz);
    }


    // Simplifications for MemberVisitor.

    @Override
    public void visitAnyMember(Clazz clazz, Member member)
    {
        visitAnyProcessable(member);
    }


    // Simplifications for AttributeVisitor.

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute)
    {
        visitAnyProcessable(attribute);
    }


    // Simplifications for ResourceFileVisitor.

    //public void visit...(...)

    public void visitResourceFile(ResourceFile resourceFile)
    {
        visitAnyProcessable(resourceFile);
    }
}
