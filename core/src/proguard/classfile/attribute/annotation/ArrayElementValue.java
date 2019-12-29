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
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;

/**
 * This ElementValue represents an array element value.
 *
 * @author Eric Lafortune
 */
public class ArrayElementValue extends ElementValue
{
    public int            u2elementValuesCount;
    public ElementValue[] elementValues;


    /**
     * Creates an uninitialized ArrayElementValue.
     */
    public ArrayElementValue()
    {
    }


    /**
     * Creates an initialized ArrayElementValue.
     */
    public ArrayElementValue(int            u2elementNameIndex,
                             int            u2elementValuesCount,
                             ElementValue[] elementValues)
    {
        super(u2elementNameIndex);

        this.u2elementValuesCount = u2elementValuesCount;
        this.elementValues        = elementValues;
    }


    // Implementations for ElementValue.

    public char getTag()
    {
        return ElementValue.TAG_ARRAY;
    }

    public void accept(Clazz clazz, Annotation annotation, ElementValueVisitor elementValueVisitor)
    {
        elementValueVisitor.visitArrayElementValue(clazz, annotation, this);
    }


    /**
     * Applies the given visitor to the specified nested element value.
     */
    public void elementValueAccept(Clazz clazz, Annotation annotation, int index, ElementValueVisitor elementValueVisitor)
    {
        elementValues[index].accept(clazz, annotation, elementValueVisitor);
    }


    /**
     * Applies the given visitor to all nested element values.
     */
    public void elementValuesAccept(Clazz clazz, Annotation annotation, ElementValueVisitor elementValueVisitor)
    {
        for (int index = 0; index < u2elementValuesCount; index++)
        {
            elementValues[index].accept(clazz, annotation, elementValueVisitor);
        }
    }
}
