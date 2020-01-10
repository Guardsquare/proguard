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
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This {@link ConstantVisitor} adds all interfaces that it visits to the given
 * target class.
 *
 * @author Eric Lafortune
 */
public class InterfaceAdder
extends      SimplifiedVisitor
implements   ConstantVisitor
{
    private final ConstantAdder    constantAdder;
    private final InterfacesEditor interfacesEditor;


    /**
     * Creates a new InterfaceAdder that will add interfaces to the given
     * target class.
     */
    public InterfaceAdder(ProgramClass targetClass)
    {
        constantAdder    = new ConstantAdder(targetClass);
        interfacesEditor = new InterfacesEditor(targetClass);
    }


    // Implementations for ConstantVisitor.

    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        interfacesEditor.addInterface(constantAdder.addConstant(clazz, classConstant));
    }
}