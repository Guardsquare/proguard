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
import proguard.classfile.attribute.visitor.*;

/**
 * This Attribute represents an inner classes attribute.
 *
 * @author Eric Lafortune
 */
public class InnerClassesAttribute extends Attribute
{
    public int                u2classesCount;
    public InnerClassesInfo[] classes;


    /**
     * Creates an uninitialized InnerClassesAttribute.
     */
    public InnerClassesAttribute()
    {
    }


    /**
     * Creates an initialized InnerClassesAttribute.
     */
    public InnerClassesAttribute(int                u2attributeNameIndex,
                                 int                u2classesCount,
                                 InnerClassesInfo[] classes)
    {
        super(u2attributeNameIndex);

        this.u2classesCount = u2classesCount;
        this.classes        = classes;
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitInnerClassesAttribute(clazz, this);
    }


    /**
     * Applies the given visitor to all inner classes.
     */
    public void innerClassEntriesAccept(Clazz clazz, InnerClassesInfoVisitor innerClassesInfoVisitor)
    {
        for (int index = 0; index < u2classesCount; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of InnerClassesInfo.
            innerClassesInfoVisitor.visitInnerClassesInfo(clazz, classes[index]);
        }
    }
}
