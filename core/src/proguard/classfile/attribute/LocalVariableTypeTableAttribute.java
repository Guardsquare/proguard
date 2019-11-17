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
package proguard.classfile.attribute;

import proguard.classfile.*;
import proguard.classfile.attribute.visitor.*;

/**
 * This Attribute represents a local variable table type attribute.
 *
 * @author Eric Lafortune
 */
public class LocalVariableTypeTableAttribute extends Attribute
{
    public int                     u2localVariableTypeTableLength;
    public LocalVariableTypeInfo[] localVariableTypeTable;


    /**
     * Creates an uninitialized LocalVariableTypeTableAttribute.
     */
    public LocalVariableTypeTableAttribute()
    {
    }


    /**
     * Creates an initialized LocalVariableTypeTableAttribute.
     */
    public LocalVariableTypeTableAttribute(int                     u2attributeNameIndex,
                                           int                     u2localVariableTypeTableLength,
                                           LocalVariableTypeInfo[] localVariableTypeTable)
    {
        super(u2attributeNameIndex);

        this.u2localVariableTypeTableLength = u2localVariableTypeTableLength;
        this.localVariableTypeTable         = localVariableTypeTable;
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, this);
    }


    /**
     * Applies the given visitor to all local variable types.
     */
    public void localVariablesAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeInfoVisitor localVariableTypeInfoVisitor)
    {
        for (int index = 0; index < u2localVariableTypeTableLength; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of LocalVariableTypeInfo.
            localVariableTypeInfoVisitor.visitLocalVariableTypeInfo(clazz, method, codeAttribute, localVariableTypeTable[index]);
        }
    }
}
