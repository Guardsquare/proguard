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
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.LocalVariableInfoVisitor;

/**
 * This {@link LocalVariableInfoVisitor} adds all local variables that it visits to the
 * given target local variable table attribute.
 */
public class LocalVariableInfoAdder
implements   LocalVariableInfoVisitor
{
    private final ConstantAdder                     constantAdder;
    private final LocalVariableTableAttributeEditor localVariableTableAttributeEditor;


    /**
     * Creates a new LocalVariableInfoAdder that will copy local variables
     * into the given target local variable table.
     */
    public LocalVariableInfoAdder(ProgramClass                targetClass,
                                  LocalVariableTableAttribute targetLocalVariableTableAttribute)
    {
        this.constantAdder                     = new ConstantAdder(targetClass);
        this.localVariableTableAttributeEditor = new LocalVariableTableAttributeEditor(targetLocalVariableTableAttribute);
    }


    // Implementations for LocalVariableInfoVisitor.

    public void visitLocalVariableInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableInfo localVariableInfo)
    {
        // Create a new local variable.
        LocalVariableInfo newLocalVariableInfo =
            new LocalVariableInfo(localVariableInfo.u2startPC,
                                  localVariableInfo.u2length,
                                  constantAdder.addConstant(clazz, localVariableInfo.u2nameIndex),
                                  constantAdder.addConstant(clazz, localVariableInfo.u2descriptorIndex),
                                  localVariableInfo.u2index);

        newLocalVariableInfo.referencedClass = localVariableInfo.referencedClass;

        // Add it to the target.
        localVariableTableAttributeEditor.addLocalVariableInfo(newLocalVariableInfo);
    }
}