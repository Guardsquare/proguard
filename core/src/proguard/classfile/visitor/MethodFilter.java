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
 * This MemberVisitor delegates its visits to one of two other given
 * MemberVisitor instances, depending on whether the visited method
 * is a method or a field.
 *
 * @author Thomas Neidhart
 */
public class MethodFilter implements MemberVisitor
{
    private final MemberVisitor methodMemberVisitor;
    private final MemberVisitor fieldMemberVisitor;


    /**
     * Creates a new MethodFilter.
     * @param methodMemberVisitor the MemberVisitor to which method visits will be delegated.
     */
    public MethodFilter(MemberVisitor methodMemberVisitor)
    {
        this(methodMemberVisitor, null);
    }


    /**
     * Creates a new MethodFilter.
     * @param methodMemberVisitor the MemberVisitor to which method visits will be delegated.
     * @param fieldMemberVisitor  the MemberVisitor to which field visits will be delegated.
     */
    public MethodFilter(MemberVisitor methodMemberVisitor,
                        MemberVisitor fieldMemberVisitor)
    {
        this.methodMemberVisitor = methodMemberVisitor;
        this.fieldMemberVisitor  = fieldMemberVisitor;
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        if (fieldMemberVisitor != null)
        {
            fieldMemberVisitor.visitProgramField(programClass, programField);
        }
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        if (methodMemberVisitor != null)
        {
            methodMemberVisitor.visitProgramMethod(programClass, programMethod);
        }
    }


    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        if (fieldMemberVisitor != null)
        {
            fieldMemberVisitor.visitLibraryField(libraryClass, libraryField);
        }
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        if (methodMemberVisitor != null)
        {
            methodMemberVisitor.visitLibraryMethod(libraryClass, libraryMethod);
        }
    }
}
