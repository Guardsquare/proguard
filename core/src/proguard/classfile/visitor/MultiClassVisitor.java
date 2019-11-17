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
 * This ClassVisitor delegates all visits to each ClassVisitor
 * in a given list.
 *
 * @author Eric Lafortune
 */
public class MultiClassVisitor implements ClassVisitor
{
    private ClassVisitor[] classVisitors;
    private int            classVisitorCount;


    public MultiClassVisitor()
    {
        this.classVisitors = new ClassVisitor[16];
    }


    public MultiClassVisitor(ClassVisitor... classVisitors)
    {
        this.classVisitors     = classVisitors;
        this.classVisitorCount = classVisitors.length;
    }


    public void addClassVisitor(ClassVisitor classVisitor)
    {
        classVisitors =
            ArrayUtil.add(classVisitors,
                          classVisitorCount++,
                          classVisitor);
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        for (int index = 0; index < classVisitorCount; index++)
        {
            classVisitors[index].visitProgramClass(programClass);
        }
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        for (int index = 0; index < classVisitorCount; index++)
        {
            classVisitors[index].visitLibraryClass(libraryClass);
        }
    }
}
