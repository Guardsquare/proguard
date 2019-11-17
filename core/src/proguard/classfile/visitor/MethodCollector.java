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
import proguard.classfile.util.SimplifiedVisitor;

import java.util.Collection;

/**
 * This <code>MemberVisitor</code> collects the methods that it visits in the
 * given collection.
 *
 * @author Johan Leys
 */
public class MethodCollector
extends      SimplifiedVisitor
implements   MemberVisitor
{
    private final Collection<Method> collection;


    /**
     * Creates a new MethodCollector.
     * @param collection the <code>Collection</code> in which all methods will be collected.
     */
    public MethodCollector(Collection<Method> collection)
    {
        this.collection = collection;
    }


    // Implementations for MethodCollector.

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        collection.add(programMethod);
    }


    @Override
    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        collection.add(libraryMethod);
    }
}
