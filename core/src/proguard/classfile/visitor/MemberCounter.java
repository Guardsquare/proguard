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
import proguard.util.Counter;

/**
 * This {@link MemberVisitor} counts the number of class members that have been visited.
 *
 * @author Eric Lafortune
 */
public class MemberCounter
implements   MemberVisitor,
             Counter
{
    private int count;


    // Implementations for Counter.

    /**
     * Returns the number of class members that has been visited so far.
     */
    public int getCount()
    {
        return count;
    }


    // Implementations for MemberVisitor.

    public void visitLibraryField(LibraryClass libraryClass,
                                  LibraryField libraryField)
    {
        count++;
    }


    public void visitLibraryMethod(LibraryClass libraryClass,
                                   LibraryMethod libraryMethod)
    {
        count++;
    }


    public void visitProgramField(ProgramClass programClass,
                                  ProgramField programField)
    {
        count++;
    }


    public void visitProgramMethod(ProgramClass programClass,
                                   ProgramMethod programMethod)
    {
        count++;
    }
}
