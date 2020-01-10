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
import proguard.util.ArrayUtil;


/**
 * This {@link MemberVisitor} delegates all visits to each {@link MemberVisitor}
 * in a given list.
 *
 * @author Eric Lafortune
 */
public class MultiMemberVisitor implements MemberVisitor
{
    private MemberVisitor[] memberVisitors;
    private int             memberVisitorCount;


    public MultiMemberVisitor()
    {
        this.memberVisitors = new MemberVisitor[16];
    }


    public MultiMemberVisitor(MemberVisitor... memberVisitors)
    {
        this.memberVisitors     = memberVisitors;
        this.memberVisitorCount = memberVisitors.length;
    }


    public void addMemberVisitor(MemberVisitor memberVisitor)
    {
        memberVisitors =
            ArrayUtil.add(memberVisitors,
                          memberVisitorCount++,
                          memberVisitor);
    }


    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        for (int index = 0; index < memberVisitorCount; index++)
        {
            memberVisitors[index].visitProgramField(programClass, programField);
        }
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        for (int index = 0; index < memberVisitorCount; index++)
        {
            memberVisitors[index].visitProgramMethod(programClass, programMethod);
        }
    }


    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        for (int index = 0; index < memberVisitorCount; index++)
        {
            memberVisitors[index].visitLibraryField(libraryClass, libraryField);
        }
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        for (int index = 0; index < memberVisitorCount; index++)
        {
            memberVisitors[index].visitLibraryMethod(libraryClass, libraryMethod);
        }
    }
}
