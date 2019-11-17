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
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.*;

import java.util.*;


/**
 * This visitor removes all members it visits in a ProgramClass.
 *
 * It should be used in two steps:
 * - in the first step, the collection step, all program fields to be removed
 *   should be visited.
 * - in the second step, the removal step, the program class containing the
 *   program fields should be visited. This will actually delete all
 *   collected fields.
 *
 * For example, to remove all fields in a program class:
 *
 *   MemberRemover remover = new MemberRemover();
 *   programClass.fieldsAccept(remover);
 *   programClass.accept(remover);
 *
 * @author Johan Leys
 */
public class MemberRemover
extends      SimplifiedVisitor
implements   ClassVisitor,
             MemberVisitor
{
    private Set<Method> methodsToRemove = new HashSet<Method>();
    private Set<Field>  fieldsToRemove  = new HashSet<Field>();


    // Implementations for ClassVisitor.

    public void visitAnyClass(Clazz clazz) {}


    public void visitProgramClass(ProgramClass programClass)
    {
        ClassEditor classEditor = new ClassEditor(programClass);

        // Remove all collected methods.
        for (Method method : methodsToRemove)
        {
            classEditor.removeMethod(method);
        }
        methodsToRemove.clear();

        // Remove all collected fields.
        for (Field field : fieldsToRemove)
        {
            classEditor.removeField(field);
        }
        fieldsToRemove.clear();
    }


    // Implementations for MemberVisitor.

    public void visitAnyMember(Clazz clazz, Member member) {}


    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        fieldsToRemove.add(programField);
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        methodsToRemove.add(programMethod);
    }
}
