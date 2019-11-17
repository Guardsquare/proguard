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
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.visitor.ClassVisitor;

import java.util.*;

/**
 * This ClassVisitor sorts the class members of the classes that it visits.
 * The sorting order is based on the access flags, the names, and the
 * descriptors.
 *
 * @author Eric Lafortune
 */
public class ClassMemberSorter implements ClassVisitor, Comparator
{
    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        // Sort the fields.
        Arrays.sort(programClass.fields, 0, programClass.u2fieldsCount, this);

        // Sort the methods.
        Arrays.sort(programClass.methods, 0, programClass.u2methodsCount, this);
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
    }


    // Implementations for Comparator.

    public int compare(Object object1, Object object2)
    {
        ProgramMember member1 = (ProgramMember)object1;
        ProgramMember member2 = (ProgramMember)object2;

        return member1.u2accessFlags     < member2.u2accessFlags     ? -1 :
               member1.u2accessFlags     > member2.u2accessFlags     ?  1 :
               member1.u2nameIndex       < member2.u2nameIndex       ? -1 :
               member1.u2nameIndex       > member2.u2nameIndex       ?  1 :
               member1.u2descriptorIndex < member2.u2descriptorIndex ? -1 :
               member1.u2descriptorIndex > member2.u2descriptorIndex ?  1 :
                                                                        0;
    }
}
