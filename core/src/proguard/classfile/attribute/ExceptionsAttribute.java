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
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.visitor.ConstantVisitor;

/**
 * This Attribute represents an exceptions attribute.
 *
 * @author Eric Lafortune
 */
public class ExceptionsAttribute extends Attribute
{
    public int   u2exceptionIndexTableLength;
    public int[] u2exceptionIndexTable;


    /**
     * Creates an uninitialized ExceptionsAttribute.
     */
    public ExceptionsAttribute()
    {
    }


    /**
     * Creates an initialized ExceptionsAttribute.
     */
    public ExceptionsAttribute(int   u2attributeNameIndex,
                               int   u2exceptionIndexTableLength,
                               int[] u2exceptionIndexTable)
    {
        super(u2attributeNameIndex);

        this.u2exceptionIndexTableLength = u2exceptionIndexTableLength;
        this.u2exceptionIndexTable       = u2exceptionIndexTable;
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, Method method, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitExceptionsAttribute(clazz, method, this);
    }


    /**
     * Applies the given constant pool visitor to all exception class pool info
     * entries.
     */
    public void exceptionEntriesAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        for (int index = 0; index < u2exceptionIndexTableLength; index++)
        {
            clazz.constantPoolEntryAccept(u2exceptionIndexTable[index],
                                          constantVisitor);
        }
    }
}
