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
 * This {@link LongValue} represents the result of a binary operation on two long
 * values.
 *
 * @author Eric Lafortune
 */
final class CompositeLongValue extends SpecificLongValue
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


    private final LongValue longValue1;
    private final byte      operation;
    private final Value     longValue2;


    /**
     * Creates a new composite long value of the two given long values
     * and the given operation.
     */
    public CompositeLongValue(LongValue longValue1,
                              byte      operation,
                              Value     longValue2)
    {
        this.longValue1 = longValue1;
        this.operation  = operation;
        this.longValue2 = longValue2;
    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        return this == object ||
               super.equals(object) &&
               this.longValue1.equals(((CompositeLongValue)object).longValue1) &&
               this.operation      == ((CompositeLongValue)object).operation   &&
               this.longValue2.equals(((CompositeLongValue)object).longValue2);
    }


    public int hashCode()
    {
        return super.hashCode() ^
               longValue1.hashCode() ^
               longValue2.hashCode();
    }


    public String toString()
    {
        return "("+longValue1+((char)operation)+longValue2+")";
    }
}