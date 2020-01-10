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
 * This {@link ClassVisitor} delegates its visits to one of two given
 * {@link ClassVisitor}s, depending on whether the visited classes
 * extend/implement a given class or not.
 * <p/>
 * Filter:
 * - accepted: the visited class extends/implements the given class.
 * - rejected: the visited class does not extend/implement the given class.
 *
 * @author Eric Lafortune
 */
public class ImplementedClassFilter implements ClassVisitor
{
    private final Clazz        implementedClass;
    private final boolean      includeImplementedClass;
    private final ClassVisitor acceptedClassVisitor;
    private final ClassVisitor rejectedClassVisitor;


    /**
     * Creates a new ImplementedClassFilter.
     *
     * @param implementedClass        the class whose implementations will
     *                                be accepted.
     * @param includeImplementedClass if true, the implemented class itself
     *                                will also be accepted, otherwise it
     *                                will be rejected.
     * @param acceptedClassVisitor    the <code>ClassVisitor</code> to which
     *                                visits of classes implementing the given
     *                                class will be delegated.
     * @param rejectedClassVisistor   the <code>ClassVisitor</code> to which
     *                                visits of classes not implementing the
     *                                given class will be delegated.
     */
    public ImplementedClassFilter(Clazz        implementedClass,
                                  boolean      includeImplementedClass,
                                  ClassVisitor acceptedClassVisitor,
                                  ClassVisitor rejectedClassVisistor)
    {
        this.implementedClass = implementedClass;
        this.includeImplementedClass = includeImplementedClass;
        this.acceptedClassVisitor = acceptedClassVisitor;
        this.rejectedClassVisitor = rejectedClassVisistor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        ClassVisitor visitor = delegateVisitor(programClass);
        if (visitor != null)
        {
            visitor.visitProgramClass(programClass);
        }
    }

    public void visitLibraryClass(LibraryClass libraryClass)
    {
        ClassVisitor visitor = delegateVisitor(libraryClass);
        if (visitor != null)
        {
            visitor.visitLibraryClass(libraryClass);
        }
    }


    // Small utility methods.

    private ClassVisitor delegateVisitor(Clazz clazz)
    {
        return clazz.extendsOrImplements(implementedClass) &&
               (clazz != implementedClass || includeImplementedClass) ?
            acceptedClassVisitor : rejectedClassVisitor;
    }
}
