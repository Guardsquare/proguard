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
package proguard.classfile.constant.visitor;

import proguard.classfile.*;
import proguard.classfile.visitor.ClassVisitor;


/**
 * This {@link ClassVisitor} lets a given {@link ConstantVisitor} visit all constant pool
 * entries of the program classes it visits.
 *
 * @author Eric Lafortune
 */
public class AllConstantVisitor implements ClassVisitor
{
    private final ConstantVisitor constantVisitor;


    public AllConstantVisitor(ConstantVisitor constantVisitor)
    {
        this.constantVisitor = constantVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.constantPoolEntriesAccept(constantVisitor);
    }


    public void visitLibraryClass(LibraryClass libraryClass) {}
}
