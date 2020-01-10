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
import proguard.classfile.attribute.visitor.LocalVariableTypeInfoVisitor;
import proguard.util.ArrayUtil;

/**
 * This {@link LocalVariableTypeInfoVisitor} adds all local variable types that it
 * visits to the given target local variable type attribute.
 */
public class LocalVariableTypeInfoAdder
implements   LocalVariableTypeInfoVisitor
{
    private final ConstantAdder                     constantAdder;
    private final LocalVariableTypeTableAttributeEditor localVariableTypeTableAttributeEditor;


    /**
     * Creates a new LocalVariableTypeInfoAdder that will copy local variable
     * types into the given target local variable type table.
     */
    public LocalVariableTypeInfoAdder(ProgramClass                    targetClass,
                                      LocalVariableTypeTableAttribute targetLocalVariableTypeTableAttribute)
    {
        this.constantAdder                         = new ConstantAdder(targetClass);
        this.localVariableTypeTableAttributeEditor = new LocalVariableTypeTableAttributeEditor(targetLocalVariableTypeTableAttribute);
    }


    // Implementations for LocalVariableTypeInfoVisitor.

    public void visitLocalVariableTypeInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeInfo localVariableTypeInfo)
    {
        // Create a new local variable type.
        LocalVariableTypeInfo newLocalVariableTypeInfo =
            new LocalVariableTypeInfo(localVariableTypeInfo.u2startPC,
                                      localVariableTypeInfo.u2length,
                                      constantAdder.addConstant(clazz, localVariableTypeInfo.u2nameIndex),
                                      constantAdder.addConstant(clazz, localVariableTypeInfo.u2signatureIndex),
                                      localVariableTypeInfo.u2index);

        newLocalVariableTypeInfo.referencedClasses = ArrayUtil.cloneOrNull(localVariableTypeInfo.referencedClasses);

        // Add it to the target.
        localVariableTypeTableAttributeEditor.addLocalVariableTypeInfo(newLocalVariableTypeInfo);
    }
}