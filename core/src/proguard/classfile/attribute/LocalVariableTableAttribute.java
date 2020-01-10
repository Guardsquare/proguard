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
 * This {@link Attribute} represents a local variable table attribute.
 *
 * @author Eric Lafortune
 */
public class LocalVariableTableAttribute extends Attribute
{
    public int                 u2localVariableTableLength;
    public LocalVariableInfo[] localVariableTable;


    /**
     * Creates an uninitialized LocalVariableTableAttribute.
     */
    public LocalVariableTableAttribute()
    {
    }


    /**
     * Creates an initialized LocalVariableTableAttribute.
     */
    public LocalVariableTableAttribute(int                 u2attributeNameIndex,
                                       int                 u2localVariableTableLength,
                                       LocalVariableInfo[] localVariableTable)
    {
        super(u2attributeNameIndex);

        this.u2localVariableTableLength = u2localVariableTableLength;
        this.localVariableTable         = localVariableTable;
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitLocalVariableTableAttribute(clazz, method, codeAttribute, this);
    }


    /**
     * Applies the given visitor to all local variables.
     */
    public void localVariablesAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableInfoVisitor localVariableInfoVisitor)
    {
        for (int index = 0; index < u2localVariableTableLength; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of LocalVariableInfo.
            localVariableInfoVisitor.visitLocalVariableInfo(clazz, method, codeAttribute, localVariableTable[index]);
        }
    }
}
