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
 * This {@link Attribute} represents a method parameters attribute.
 *
 * @author Eric Lafortune
 */
public class MethodParametersAttribute extends Attribute
{
    public int             u1parametersCount;
    public ParameterInfo[] parameters;


    /**
     * Creates an uninitialized MethodParametersAttribute.
     */
    public MethodParametersAttribute()
    {
    }


    /**
     * Creates an initialized MethodParametersAttribute.
     */
    public MethodParametersAttribute(int             u2attributeNameIndex,
                                     int             u1parametersCount,
                                     ParameterInfo[] parameters)
    {
        super(u2attributeNameIndex);

        this.u1parametersCount = u1parametersCount;
        this.parameters        = parameters;
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, Method method, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitMethodParametersAttribute(clazz, method, this);
    }


    /**
     * Applies the given visitor to all parameters.
     */
    public void parametersAccept(Clazz clazz, Method method, ParameterInfoVisitor parameterInfoVisitor)
    {
        // Loop over all parameters.
        for (int index = 0; index < u1parametersCount; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of ParameterInfo.
            parameterInfoVisitor.visitParameterInfo(clazz, method, index, parameters[index]);
        }
    }
}
