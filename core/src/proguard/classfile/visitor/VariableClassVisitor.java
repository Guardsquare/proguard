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
 * This ClassVisitor delegates all method calls to a ClassVisitor
 * that can be changed at any time.
 *
 * @author Eric Lafortune
 */
public class VariableClassVisitor implements ClassVisitor
{
    private ClassVisitor classVisitor;


    public VariableClassVisitor()
    {
        this(null);
    }


    public VariableClassVisitor(ClassVisitor classVisitor)
    {
        this.classVisitor = classVisitor;
    }


    public void setClassVisitor(ClassVisitor classVisitor)
    {
        this.classVisitor = classVisitor;
    }

    public ClassVisitor getClassVisitor()
    {
        return classVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        if (classVisitor != null)
        {
            classVisitor.visitProgramClass(programClass);
        }
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        if (classVisitor != null)
        {
            classVisitor.visitLibraryClass(libraryClass);
        }
    }
}
