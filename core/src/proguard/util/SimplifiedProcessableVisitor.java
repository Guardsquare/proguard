/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
