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

import proguard.classfile.Clazz;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;

/**
 * This ElementValue represents a constant element value.
 *
 * @author Eric Lafortune
 */
public class ConstantElementValue extends ElementValue
{
    public final char u1tag;
    public       int  u2constantValueIndex;


    /**
     * Creates an uninitialized ConstantElementValue.
     */
    public ConstantElementValue(char u1tag)
    {
        this.u1tag = u1tag;
    }


    /**
     * Creates an initialized ConstantElementValue.
     */
    public ConstantElementValue(char u1tag,
                                int  u2elementNameIndex,
                                int  u2constantValueIndex)
    {
        super(u2elementNameIndex);

        this.u1tag                = u1tag;
        this.u2constantValueIndex = u2constantValueIndex;
    }


    // Implementations for ElementValue.

    public char getTag()
    {
        return u1tag;
    }

    public void accept(Clazz clazz, Annotation annotation, ElementValueVisitor elementValueVisitor)
    {
        elementValueVisitor.visitConstantElementValue(clazz, annotation, this);
    }
}
