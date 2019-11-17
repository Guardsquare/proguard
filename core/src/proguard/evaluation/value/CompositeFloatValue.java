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

/**
 * This FloatValue represents the result of a binary operation on two float
 * values.
 *
 * @author Eric Lafortune
 */
final class CompositeFloatValue extends SpecificFloatValue
{
    public static final byte ADD       = '+';
    public static final byte SUBTRACT  = '-';
    public static final byte MULTIPLY  = '*';
    public static final byte DIVIDE    = '/';
    public static final byte REMAINDER = '%';


    private final FloatValue floatValue1;
    private final byte       operation;
    private final FloatValue floatValue2;


    /**
     * Creates a new composite float value of the two given float values
     * and the given operation.
     */
    public CompositeFloatValue(FloatValue floatValue1,
                               byte       operation,
                               FloatValue floatValue2)
    {
        this.floatValue1 = floatValue1;
        this.operation   = operation;
        this.floatValue2 = floatValue2;
    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        return this == object ||
               super.equals(object) &&
               this.floatValue1.equals(((CompositeFloatValue)object).floatValue1) &&
               this.operation       == ((CompositeFloatValue)object).operation    &&
               this.floatValue2.equals(((CompositeFloatValue)object).floatValue2);
    }


    public int hashCode()
    {
        return super.hashCode() ^
               floatValue1.hashCode() ^
               floatValue2.hashCode();
    }


    public String toString()
    {
        return "("+floatValue1+((char)operation)+floatValue2+")";
    }
}