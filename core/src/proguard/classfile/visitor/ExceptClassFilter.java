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
 * This {@link ClassVisitor} delegates its visits to another given
 * {@link ClassVisitor}, except for one given class.
 *
 * @author Eric Lafortune
 */
public class ExceptClassFilter implements ClassVisitor
{
    private final Clazz        exceptClass;
    private final ClassVisitor classVisitor;


    /**
     * Creates a new ClassNameFilter.
     * @param exceptClass  the class that will not be visited.
     * @param classVisitor the <code>ClassVisitor</code> to which visits will
     *                     be delegated.
     */
    public ExceptClassFilter(Clazz        exceptClass,
                             ClassVisitor classVisitor)
    {
        this.exceptClass  = exceptClass;
        this.classVisitor = classVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        if (!programClass.equals(exceptClass))
        {
            classVisitor.visitProgramClass(programClass);
        }
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        if (!libraryClass.equals(exceptClass))
        {
            classVisitor.visitLibraryClass(libraryClass);
        }
    }
}