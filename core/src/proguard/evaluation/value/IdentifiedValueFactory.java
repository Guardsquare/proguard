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
package proguard.evaluation.value;

import proguard.classfile.*;

/**
 * This class provides methods to create and reuse Value objects that are
 * identified by unique integer IDs.
 *
 * @author Eric Lafortune
 */
public class IdentifiedValueFactory
extends      ParticularValueFactory
{
    protected int integerID;
    protected int longID;
    protected int floatID;
    protected int doubleID;
    protected int referenceID;


   // Implementations for BasicValueFactory.

    public IntegerValue createIntegerValue()
    {
        return new IdentifiedIntegerValue(this, integerID++);
    }


    public LongValue createLongValue()
    {
        return new IdentifiedLongValue(this, longID++);
    }


    public FloatValue createFloatValue()
    {
        return new IdentifiedFloatValue(this, floatID++);
    }


    public DoubleValue createDoubleValue()
    {
        return new IdentifiedDoubleValue(this, doubleID++);
    }


    public ReferenceValue createReferenceValue(String  type,
                                               Clazz   referencedClass,
                                               boolean mayBeExtension,
                                               boolean mayBeNull)
    {
        return type == null ?
            TypedReferenceValueFactory.REFERENCE_VALUE_NULL :
            new IdentifiedReferenceValue(type,
                                         referencedClass,
                                         mayBeExtension,
                                         mayBeNull,
                                         this,
                                         referenceID++);
    }


    public ReferenceValue createArrayReferenceValue(String       type,
                                                    Clazz        referencedClass,
                                                    IntegerValue arrayLength)
    {
        return type == null ?
            TypedReferenceValueFactory.REFERENCE_VALUE_NULL :
            new IdentifiedArrayReferenceValue(ClassConstants.TYPE_ARRAY + type,
                                              referencedClass,
                                              false,
                                              arrayLength,
                                              this,
                                              referenceID++);
    }
}