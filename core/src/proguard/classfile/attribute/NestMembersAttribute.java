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
import proguard.classfile.constant.visitor.ConstantVisitor;

/**
 * This {@link Attribute} represents a nest host attribute.
 *
 * @author Eric Lafortune
 */
public class NestMembersAttribute extends Attribute
{
    public int   u2classesCount;
    public int[] u2classes;


    /**
     * Creates an uninitialized NestMembersAttribute.
     */
    public NestMembersAttribute()
    {
    }


    /**
     * Creates an initialized NestMembersAttribute.
     */
    public NestMembersAttribute(int   u2attributeNameIndex,
                                int   u2classesCount,
                                int[] u2classes)
    {
        super(u2attributeNameIndex);

        this.u2classesCount = u2classesCount;
        this.u2classes      = u2classes;
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitNestMembersAttribute(clazz, this);
    }


    /**
     * Applies the given visitor to all member class constants.
     */
    public void memberClassConstantsAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        for (int index = 0; index < u2classesCount; index++)
        {
            clazz.constantPoolEntryAccept(u2classes[index], constantVisitor);
        }
    }
}
