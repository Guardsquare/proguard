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
 * This {@link Attribute} represents a runtime invisible parameter annotations attribute.
 *
 * @author Eric Lafortune
 */
public class RuntimeInvisibleParameterAnnotationsAttribute extends ParameterAnnotationsAttribute
{
    /**
     * Creates an uninitialized RuntimeInvisibleParameterAnnotationsAttribute.
     */
    public RuntimeInvisibleParameterAnnotationsAttribute()
    {
    }


    /**
     * Creates an initialized RuntimeInvisibleParameterAnnotationsAttribute.
     */
    public RuntimeInvisibleParameterAnnotationsAttribute(int            u2attributeNameIndex,
                                                         int            u1parametersCount,
                                                         int[]          u2parameterAnnotationsCount,
                                                         Annotation[][] parameterAnnotations)
    {
        super(u2attributeNameIndex,
              u1parametersCount,
              u2parameterAnnotationsCount,
              parameterAnnotations);
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, Method method, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, this);
    }
}
