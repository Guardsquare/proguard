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
 * This {@link MemberVisitor} delegates all method calls to a {@link MemberVisitor}
 * that can be changed at any time.
 *
 * @author Eric Lafortune
 */
public class VariableMemberVisitor implements MemberVisitor
{
    private MemberVisitor memberVisitor;


    public VariableMemberVisitor()
    {
        this(null);
    }


    public VariableMemberVisitor(MemberVisitor memberVisitor)
    {
        this.memberVisitor = memberVisitor;
    }


    public void setMemberVisitor(MemberVisitor memberVisitor)
    {
        this.memberVisitor = memberVisitor;
    }

    public MemberVisitor getMemberVisitor()
    {
        return memberVisitor;
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        if (memberVisitor != null)
        {
            memberVisitor.visitProgramField(programClass, programField);
        }
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        if (memberVisitor != null)
        {
            memberVisitor.visitProgramMethod(programClass, programMethod);
        }
    }


    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        if (memberVisitor != null)
        {
            memberVisitor.visitLibraryField(libraryClass, libraryField);
        }
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        if (memberVisitor != null)
        {
            memberVisitor.visitLibraryMethod(libraryClass, libraryMethod);
        }
    }
}
