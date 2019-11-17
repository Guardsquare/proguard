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
import proguard.classfile.attribute.visitor.AttributeVisitor;

/**
 * This Attribute represents a runtime invisible annotations attribute.
 *
 * @author Eric Lafortune
 */
public class RuntimeInvisibleAnnotationsAttribute extends AnnotationsAttribute
{
    /**
     * Creates an uninitialized RuntimeInvisibleAnnotationsAttribute.
     */
    public RuntimeInvisibleAnnotationsAttribute()
    {
    }


    /**
     * Creates an initialized RuntimeInvisibleAnnotationsAttribute.
     */
    public RuntimeInvisibleAnnotationsAttribute(int          u2attributeNameIndex,
                                                int          u2annotationsCount,
                                                Annotation[] annotations)
    {
        super(u2attributeNameIndex, u2annotationsCount, annotations);
    }


// Implementations for Attribute.

    public void accept(Clazz clazz, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitRuntimeInvisibleAnnotationsAttribute(clazz, this);
    }


    public void accept(Clazz clazz, Field field, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitRuntimeInvisibleAnnotationsAttribute(clazz, field, this);
    }


    public void accept(Clazz clazz, Method method, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitRuntimeInvisibleAnnotationsAttribute(clazz, method, this);
    }
}
