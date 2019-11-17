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
 * This ClassVisitor removes the given class from the list of subclasses of the
 * classes that it visits.
 *
 * @author Eric Lafortune
 */
public class SubclassRemover
implements   ClassVisitor
{
    private final Clazz subclass;


    /**
     * Creates a new SubclassRemover that will remove the given subclass.
     */
    public SubclassRemover(Clazz subclass)
    {
        this.subclass = subclass;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.removeSubClass(subclass);
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        libraryClass.removeSubClass(subclass);
    }
}