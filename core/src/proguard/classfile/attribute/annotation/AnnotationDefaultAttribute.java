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
package proguard.classfile.attribute.annotation;

import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;

/**
 * This {@link Attribute} represents an annotation default attribute.
 *
 * @author Eric Lafortune
 */
public class AnnotationDefaultAttribute extends Attribute
{
    public ElementValue defaultValue;


    /**
     * Creates an uninitialized AnnotationDefaultAttribute.
     */
    public AnnotationDefaultAttribute()
    {
    }


    /**
     * Creates an initialized AnnotationDefaultAttribute.
     */
    public AnnotationDefaultAttribute(int          u2attributeNameIndex,
                                      ElementValue defaultValue)
    {
        super(u2attributeNameIndex);

        this.defaultValue = defaultValue;
    }


    /**
     * Applies the given visitor to the default element value.
     */
    public void defaultValueAccept(Clazz clazz, ElementValueVisitor elementValueVisitor)
    {
        defaultValue.accept(clazz, null, elementValueVisitor);
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, Method method, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitAnnotationDefaultAttribute(clazz, method, this);
    }
}
