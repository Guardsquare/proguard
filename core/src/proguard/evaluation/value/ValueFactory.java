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

import proguard.classfile.Clazz;

/**
 * This interface provides methods to create Value objects.
 *
 * @author Eric Lafortune
 */
public interface ValueFactory
{
    /**
     * Creates a new Value of the given type.
     * The type must be a fully specified internal type for primitives, classes,
     * or arrays.
     */
    public Value createValue(String  type,
                             Clazz   referencedClass,
                             boolean mayBeExtension,
                             boolean mayBeNull);


    /**
     * Creates a new IntegerValue with an undefined value.
     */
    public IntegerValue createIntegerValue();


    /**
     * Creates a new IntegerValue with a given particular value.
     */
    public IntegerValue createIntegerValue(int value);


    /**
     * Creates a new IntegerValue with a given possible range.
     */
    public IntegerValue createIntegerValue(int min, int max);


    /**
     * Creates a new LongValue with an undefined value.
     */
    public LongValue createLongValue();


    /**
     * Creates a new LongValue with a given particular value.
     */
    public LongValue createLongValue(long value);


    /**
     * Creates a new FloatValue with an undefined value.
     */
    public FloatValue createFloatValue();


    /**
     * Creates a new FloatValue with a given particular value.
     */
    public FloatValue createFloatValue(float value);


    /**
     * Creates a new DoubleValue with an undefined value.
     */
    public DoubleValue createDoubleValue();


    /**
     * Creates a new DoubleValue with a given particular value.
     */
    public DoubleValue createDoubleValue(double value);


    /**
     * Creates a new ReferenceValue of an undefined type.
     */
    public ReferenceValue createReferenceValue();


    /**
     * Creates a new ReferenceValue that represents <code>null</code>.
     */
    public ReferenceValue createReferenceValueNull();


    /**
     * Creates a new ReferenceValue that represents the given type. The type
     * must be an internal class name or an array type. If the type is
     * <code>null</code>, the ReferenceValue represents <code>null</code>.
     */
    public ReferenceValue createReferenceValue(String  type,
                                               Clazz   referencedClass,
                                               boolean mayBeExtension,
                                               boolean mayBeNull);


    /**
     * Creates a new ReferenceValue that represents a non-null array with
     * elements of the given type, with the given length.
     */
    public ReferenceValue createArrayReferenceValue(String       type,
                                                    Clazz        referencedClass,
                                                    IntegerValue arrayLength);


    /**
     * Creates a new ReferenceValue that represents a non-null array with
     * elements of the given type, with the given length and initial element
     * values.
     */
    public ReferenceValue createArrayReferenceValue(String       type,
                                                    Clazz        referencedClass,
                                                    IntegerValue arrayLength,
                                                    Value        elementValue);
}
