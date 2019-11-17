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

/**
 * This Attribute represents an unknown attribute.
 *
 * @author Eric Lafortune
 */
public class UnknownAttribute extends Attribute
{
    public final int    u4attributeLength;
    public       byte[] info;


    /**
     * Creates an uninitialized UnknownAttribute with the specified name and
     * length.
     */
    public UnknownAttribute(int u2attributeNameIndex,
                            int attributeLength)
    {
        this(u2attributeNameIndex, attributeLength, null);
    }


    /**
     * Creates an initialized UnknownAttribute.
     */
    public UnknownAttribute(int    u2attributeNameIndex,
                            int    u4attributeLength,
                            byte[] info)
    {
        super(u2attributeNameIndex);

        this.u4attributeLength = u4attributeLength;
        this.info              = info;
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitUnknownAttribute(clazz, this);
    }

    public void accept(Clazz clazz, Field field, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitUnknownAttribute(clazz, this);
    }

    public void accept(Clazz clazz, Method method, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitUnknownAttribute(clazz, this);
    }

    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitUnknownAttribute(clazz, this);
    }
}
