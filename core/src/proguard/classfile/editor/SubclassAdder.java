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
 * This {@link ClassVisitor} adds the given class to the list of subclasses of the
 * classes that it visits.
 *
 * @author Eric Lafortune
 */
public class SubclassAdder
implements   ClassVisitor
{
    private final Clazz subclass;


    /**
     * Creates a new SubclassAdder that will add the given subclass.
     */
    public SubclassAdder(Clazz subclass)
    {
        this.subclass = subclass;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.addSubClass(subclass);
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        libraryClass.addSubClass(subclass);
    }
}