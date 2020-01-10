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

import proguard.classfile.Clazz;
import proguard.classfile.attribute.visitor.AttributeVisitor;

/**
 * This {@link Attribute} represents a source directory attribute.
 *
 * @author Eric Lafortune
 */
public class SourceDirAttribute extends Attribute
{
    public int u2sourceDirIndex;


    /**
     * Creates an uninitialized SourceDirAttribute.
     */
    public SourceDirAttribute()
    {
    }


    /**
     * Creates an initialized SourceDirAttribute.
     */
    public SourceDirAttribute(int u2attributeNameIndex,
                              int u2sourceDirIndex)
    {
        super(u2attributeNameIndex);

        this.u2sourceDirIndex = u2sourceDirIndex;
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitSourceDirAttribute(clazz, this);
    }
}
