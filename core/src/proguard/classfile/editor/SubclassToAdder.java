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
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.visitor.ClassVisitor;

/**
 * This {@link ClassVisitor} adds all classes that it visits to the list of subclasses
 * of the given target class.
 *
 * @author Eric Lafortune
 */
public class SubclassToAdder
implements   ClassVisitor
{
    private final Clazz targetClass;


    /**
     * Creates a new SubclassAdder that will add subclasses to the given
     * target class.
     */
    public SubclassToAdder(Clazz targetClass)
    {
        this.targetClass = targetClass;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        targetClass.addSubClass(programClass);
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        targetClass.addSubClass(libraryClass);
    }
}