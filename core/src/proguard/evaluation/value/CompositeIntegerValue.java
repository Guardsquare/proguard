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
 * This {@link IntegerValue} represents the result of a binary operation on two integer
 * values.
 *
 * @author Eric Lafortune
 */
final class CompositeIntegerValue extends SpecificIntegerValue
{
    public static final byte ADD                  = '+';
    public static final byte SUBTRACT             = '-';
    public static final byte MULTIPLY             = '*';
    public static final byte DIVIDE               = '/';
    public static final byte REMAINDER            = '%';
    public static final byte SHIFT_LEFT           = '<';
    public static final byte SHIFT_RIGHT          = '>';
    public static final byte UNSIGNED_SHIFT_RIGHT = '}';
    public static final byte AND                  = '&';
    public static final byte OR                   = '|';
    public static final byte XOR                  = '^';


    private final IntegerValue integerValue1;
    private final byte         operation;
    private final IntegerValue integerValue2;


    /**
     * Creates a new composite integer value of the two given integer values
     * and the given operation.
     */
    public CompositeIntegerValue(IntegerValue integerValue1,
                                 byte         operation,
                                 IntegerValue integerValue2)
    {
        this.integerValue1 = integerValue1;
        this.operation     = operation;
        this.integerValue2 = integerValue2;
    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        return this == object ||
               super.equals(object) &&
               this.integerValue1.equals(((CompositeIntegerValue)object).integerValue1) &&
               this.operation         == ((CompositeIntegerValue)object).operation      &&
               this.integerValue2.equals(((CompositeIntegerValue)object).integerValue2);
    }


    public int hashCode()
    {
        return super.hashCode() ^
               integerValue1.hashCode() ^
               integerValue2.hashCode();
    }


    public String toString()
    {
        return "("+integerValue1+((char)operation)+integerValue2+")";
    }
}