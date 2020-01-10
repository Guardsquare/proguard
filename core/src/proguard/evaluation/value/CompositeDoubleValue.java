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
 * This {@link DoubleValue} represents the result of a binary operation on two double
 * values.
 *
 * @author Eric Lafortune
 */
final class CompositeDoubleValue extends SpecificDoubleValue
{
    public static final byte ADD       = '+';
    public static final byte SUBTRACT  = '-';
    public static final byte MULTIPLY  = '*';
    public static final byte DIVIDE    = '/';
    public static final byte REMAINDER = '%';


    private final DoubleValue doubleValue1;
    private final byte        operation;
    private final DoubleValue doubleValue2;


    /**
     * Creates a new composite double value of the two given double values
     * and the given operation.
     */
    public CompositeDoubleValue(DoubleValue doubleValue1,
                                byte        operation,
                                DoubleValue doubleValue2)
    {
        this.doubleValue1 = doubleValue1;
        this.operation    = operation;
        this.doubleValue2 = doubleValue2;
    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        return this == object ||
               super.equals(object) &&
               this.doubleValue1.equals(((CompositeDoubleValue)object).doubleValue1) &&
               this.operation        == ((CompositeDoubleValue)object).operation     &&
               this.doubleValue2.equals(((CompositeDoubleValue)object).doubleValue2);
    }


    public int hashCode()
    {
        return super.hashCode() ^
               doubleValue1.hashCode() ^
               doubleValue2.hashCode();
    }


    public String toString()
    {
        return "("+doubleValue1+((char)operation)+doubleValue2+")";
    }
}