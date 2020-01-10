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
package proguard.classfile.util;

import proguard.classfile.*;
import proguard.classfile.visitor.ClassVisitor;

/**
 * This {@link ClassVisitor} adds all classes that it visits to the list of subclasses
 * of their superclass. These subclass lists make it more convenient to travel
 *
 * @author Eric Lafortune
 */
public class ClassSubHierarchyInitializer
implements   ClassVisitor
{
    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        // Add this class to the subclasses of its superclass.
        addSubclass(programClass, programClass.getSuperClass());

        // Add this class to the subclasses of its interfaces.
        for (int index = 0; index < programClass.u2interfacesCount; index++)
        {
            addSubclass(programClass, programClass.getInterface(index));
        }
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Add this class to the subclasses of its superclass,
        addSubclass(libraryClass, libraryClass.superClass);

        // Add this class to the subclasses of its interfaces.
        Clazz[] interfaceClasses = libraryClass.interfaceClasses;
        if (interfaceClasses != null)
        {
            for (int index = 0; index < interfaceClasses.length; index++)
            {
                // Add this class to the subclasses of the interface class.
                addSubclass(libraryClass, interfaceClasses[index]);
            }
        }
    }


    // Small utility methods.

    private void addSubclass(Clazz subclass, Clazz clazz)
    {
        if (clazz != null)
        {
            clazz.addSubClass(subclass);
        }
    }
}
