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


/**
 * This <code>ClassVisitor</code> delegates its visits to another given
 * <code>ClassVisitor</code>, but only when visiting library classes.
 *
 * @author Eric Lafortune
 */
public class LibraryClassFilter implements ClassVisitor
{
    private final ClassVisitor classVisitor;


    /**
     * Creates a new LibraryClassFilter.
     * @param classVisitor     the <code>ClassVisitor</code> to which visits
     *                         will be delegated.
     */
    public LibraryClassFilter(ClassVisitor classVisitor)
    {
        this.classVisitor = classVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        // Don't delegate visits to program classes.
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        classVisitor.visitLibraryClass(libraryClass);
    }
}
