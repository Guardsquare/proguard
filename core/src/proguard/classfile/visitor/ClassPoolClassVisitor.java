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
 * This {@link ClassPoolVisitor} and {@link ClassVisitor} remembers the {@link ClassPool} instances
 * that it visits and applies the given {@link ClassPoolVisitor} to the most
 * recently remembered one, every time it visits a Clazz instance.
 *
 * @author Eric Lafortune
 */
public class ClassPoolClassVisitor
implements   ClassPoolVisitor,
             ClassVisitor
{
    private ClassPoolVisitor classPoolVisitor;
    private ClassPool classPool;


    /**
     * Creates a new ClassPoolClassVisitor.
     * @param classPoolVisitor
     */
    public ClassPoolClassVisitor(ClassPoolVisitor classPoolVisitor)
    {
        this.classPoolVisitor = classPoolVisitor;
    }


    // Implementations for ClassPoolVisitor.

    public void visitClassPool(ClassPool classPool)
    {
        this.classPool = classPool;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        classPoolVisitor.visitClassPool(classPool);
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        classPoolVisitor.visitClassPool(classPool);
    }
}
