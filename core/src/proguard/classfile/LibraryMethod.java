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
package proguard.classfile;

import proguard.classfile.visitor.*;

/**
 * This {@link Method} represents a method in a {@link LibraryClass}.
 *
 * @author Eric Lafortune
 */
public class LibraryMethod extends LibraryMember implements Method
{
    /**
     * An extra field containing all the classes referenced in the
     * descriptor string. This field is filled out by the {@link
     * proguard.classfile.util.ClassReferenceInitializer ClassReferenceInitializer}.
     * The size of the array is the number of classes in the descriptor.
     * Primitive types and arrays of primitive types are ignored.
     * Unknown classes are represented as null values.
     */
    public Clazz[] referencedClasses;


    /**
     * Creates an uninitialized LibraryMethod.
     */
    public LibraryMethod()
    {
    }


    /**
     * Creates an initialized LibraryMethod.
     */
    public LibraryMethod(int    u2accessFlags,
                         String name,
                         String descriptor)
    {
        super(u2accessFlags, name, descriptor);
    }


    // Implementations for LibraryMember.

    public void accept(LibraryClass libraryClass, MemberVisitor memberVisitor)
    {
        memberVisitor.visitLibraryMethod(libraryClass, this);
    }


    // Implementations for Member.

    public void referencedClassesAccept(ClassVisitor classVisitor)
    {
        if (referencedClasses != null)
        {
            for (int index = 0; index < referencedClasses.length; index++)
            {
                if (referencedClasses[index] != null)
                {
                    referencedClasses[index].accept(classVisitor);
                }
            }
        }
    }
}
