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

import proguard.classfile.ProgramClass;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.ClassVisitor;

/**
 * This {@link ClassVisitor} sorts the various elements of the classes that it visits:
 * interfaces, constants, fields, methods, and attributes.
 *
 * @author Eric Lafortune
 */
public class ClassElementSorter
extends      SimplifiedVisitor
implements   ClassVisitor
{
    private final ClassVisitor interfaceSorter    = new InterfaceSorter();
    private final ClassVisitor constantPoolSorter = new ConstantPoolSorter();
//  private ClassVisitor classMemberSorter  = new ClassMemberSorter();
    private final ClassVisitor attributeSorter    = new AttributeSorter();


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.accept(constantPoolSorter);
        programClass.accept(interfaceSorter);
//      programClass.accept(classMemberSorter);
        programClass.accept(attributeSorter);
    }
}
