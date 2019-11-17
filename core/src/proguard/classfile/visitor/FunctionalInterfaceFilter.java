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

import java.util.*;

import java.util.*;

/**
 * This <code>ClassVisitor</code> delegates its visits to another given
 * <code>ClassVisitor</code>, but only for functional interfaces, that
 * is, interface classes that have exactly one abstract method.
 *
 * @author Eric Lafortune
 */
public class FunctionalInterfaceFilter implements ClassVisitor
{
    private final ClassVisitor classVisitor;


    /**
     * Creates a new ProgramClassFilter.
     * @param classVisitor the <code>ClassVisitor</code> to which visits
     *                     will be delegated.
     */
    public FunctionalInterfaceFilter(ClassVisitor classVisitor)
    {
        this.classVisitor = classVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        if (isFunctionalInterface(programClass))
        {
            classVisitor.visitProgramClass(programClass);
        }
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        if (isFunctionalInterface(libraryClass))
        {
            classVisitor.visitLibraryClass(libraryClass);
        }
    }


    // Small utility methods.

    private boolean isFunctionalInterface(Clazz clazz)
    {
        // Is it an interface?
        if ((clazz.getAccessFlags() & ClassConstants.ACC_INTERFACE) == 0)
        {
            return false;
        }

        // Count the abstract methods and the default methods in the
        // interface hierarchy.
        Set abstractMethods = new HashSet();
        Set defaultMethods  = new HashSet();

        clazz.hierarchyAccept(true, false, true, false,
                              new AllMethodVisitor(
                              new MultiMemberVisitor(
                                  new MemberAccessFilter(ClassConstants.ACC_ABSTRACT, 0,
                                  new MemberCollector(false, true, true, abstractMethods)),

                                  new MemberAccessFilter(0, ClassConstants.ACC_ABSTRACT,
                                  new MemberCollector(false, true, true, defaultMethods))
                              )));

        // Ignore marker interfaces.
        if (abstractMethods.size() == 0)
        {
            return false;
        }

        // Subtract default methods, since only abstract methods that don't
        // have default implementations count.
        abstractMethods.removeAll(defaultMethods);

        // Also consider this a functional interface if all abstract methods
        // have default implementations. Dynamic invocations may explicitly
        // specify the intended single abstract method. [PGD-756]
        return abstractMethods.size() <= 1;
    }
}
