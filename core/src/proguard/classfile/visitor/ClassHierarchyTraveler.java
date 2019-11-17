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
 * This <code>ClassVisitor</code> lets a given <code>ClassVisitor</code>
 * optionally travel to the visited class, its superclass, its interfaces, and
 * its subclasses.
 *
 * @author Eric Lafortune
 */
public class ClassHierarchyTraveler implements ClassVisitor
{
    private final boolean visitThisClass;
    private final boolean visitSuperClass;
    private final boolean visitInterfaces;
    private final boolean visitSubclasses;

    private final ClassVisitor classVisitor;


    /**
     * Creates a new ClassHierarchyTraveler.
     * @param visitThisClass  specifies whether to visit the originally visited
     *                        classes.
     * @param visitSuperClass specifies whether to visit the super classes of
     *                        the visited classes.
     * @param visitInterfaces specifies whether to visit the interfaces of
     *                        the visited classes.
     * @param visitSubclasses specifies whether to visit the subclasses of
     *                        the visited classes.
     * @param classVisitor    the <code>ClassVisitor</code> to
     *                        which visits will be delegated.
     */
    public ClassHierarchyTraveler(boolean      visitThisClass,
                                  boolean      visitSuperClass,
                                  boolean      visitInterfaces,
                                  boolean      visitSubclasses,
                                  ClassVisitor classVisitor)
    {
        this.visitThisClass  = visitThisClass;
        this.visitSuperClass = visitSuperClass;
        this.visitInterfaces = visitInterfaces;
        this.visitSubclasses = visitSubclasses;

        this.classVisitor = classVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.hierarchyAccept(visitThisClass,
                                     visitSuperClass,
                                     visitInterfaces,
                                     visitSubclasses,
                                     classVisitor);
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        libraryClass.hierarchyAccept(visitThisClass,
                                     visitSuperClass,
                                     visitInterfaces,
                                     visitSubclasses,
                                     classVisitor);
    }
}
