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
 * This class represents a partially evaluated integer value.
 *
 * This class handles interactions with:
 * - IntegerValue (in general)
 *
 * @author Eric Lafortune
 */
public class UnknownIntegerValue extends IntegerValue
{
    // Basic unary methods.

    public IntegerValue negate()
    {
        return this;
    }

    public IntegerValue convertToByte()
    {
        return this;
    }

    public IntegerValue convertToCharacter()
    {
        return this;
    }

    public IntegerValue convertToShort()
    {
        return this;
    }

    public LongValue convertToLong()
    {
        return BasicValueFactory.LONG_VALUE;
    }

    public FloatValue convertToFloat()
    {
        return BasicValueFactory.FLOAT_VALUE;
    }

    public DoubleValue convertToDouble()
    {
        return BasicValueFactory.DOUBLE_VALUE;
    }


    // Basic binary methods.

    public IntegerValue generalize(IntegerValue other)
    {
        return this;
    }


    public IntegerValue add(IntegerValue other)
    {
        return this;
    }

    public IntegerValue subtract(IntegerValue other)
    {
        return this;
    }

    public IntegerValue subtractFrom(IntegerValue other)
    {
        return this;
    }

    public IntegerValue multiply(IntegerValue other)
    throws ArithmeticException
    {
        return this;
    }

    public IntegerValue divide(IntegerValue other)
    throws ArithmeticException
    {
        return this;
    }

    public IntegerValue divideOf(IntegerValue other)
    throws ArithmeticException
    {
        return this;
    }

    public IntegerValue remainder(IntegerValue other)
    throws ArithmeticException
    {
        return this;
    }

    public IntegerValue remainderOf(IntegerValue other)
    throws ArithmeticException
    {
        return this;
    }

    public IntegerValue shiftLeft(IntegerValue other)
    {
        return this;
    }

    public IntegerValue shiftLeftOf(IntegerValue other)
    {
        return this;
    }

    public IntegerValue shiftRight(IntegerValue other)
    {
        return this;
    }

    public IntegerValue shiftRightOf(IntegerValue other)
    {
        return this;
    }

    public IntegerValue unsignedShiftRight(IntegerValue other)
    {
        return this;
    }

    public IntegerValue unsignedShiftRightOf(IntegerValue other)
    {
        return this;
    }

    public LongValue shiftLeftOf(LongValue other)
    {
        return BasicValueFactory.LONG_VALUE;
    }

    public LongValue shiftRightOf(LongValue other)
    {
        return BasicValueFactory.LONG_VALUE;
    }

    public LongValue unsignedShiftRightOf(LongValue other)
    {
        return BasicValueFactory.LONG_VALUE;
    }

    public IntegerValue and(IntegerValue other)
    {
        return this;
    }

    public IntegerValue or(IntegerValue other)
    {
        return this;
    }

    public IntegerValue xor(IntegerValue other)
    {
        return this;
    }

    public int equal(IntegerValue other)
    {
        return MAYBE;
    }

    public int lessThan(IntegerValue other)
    {
        return MAYBE;
    }

    public int lessThanOrEqual(IntegerValue other)
    {
        return MAYBE;
    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        return object != null &&
               this.getClass() == object.getClass();
    }


    public int hashCode()
    {
        return this.getClass().hashCode();
    }


    public String toString()
    {
        return "i";
    }
}