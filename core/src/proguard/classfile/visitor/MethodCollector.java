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
package proguard.classfile.visitor;

import proguard.classfile.*;
import proguard.classfile.util.SimplifiedVisitor;

import java.util.Collection;

/**
 * This {@link MemberVisitor} collects the methods that it visits in the
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
