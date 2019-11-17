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
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This MemberVisitor lets a given ClassVisitor visit all the classes
 * referenced by the descriptors of the class members that it visits.
 *
 * @author Eric Lafortune
 */
public class MemberDescriptorReferencedClassVisitor
extends      SimplifiedVisitor
implements   MemberVisitor
{
    private final ClassVisitor classVisitor;


    public MemberDescriptorReferencedClassVisitor(ClassVisitor classVisitor)
    {
        this.classVisitor = classVisitor;
    }


    // Implementations for MemberVisitor.

    public void visitProgramMember(ProgramClass programClass, ProgramMember programMember)
    {
        // Let the visitor visit the classes referenced in the descriptor string.
        programMember.referencedClassesAccept(classVisitor);
    }


    public void visitLibraryMember(LibraryClass programClass, LibraryMember libraryMember)
    {
        // Let the visitor visit the classes referenced in the descriptor string.
        libraryMember.referencedClassesAccept(classVisitor);
    }
}
