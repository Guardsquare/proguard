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
 * This class represents a partially evaluated long value.
 *
 * @author Eric Lafortune
 */
public class UnknownLongValue extends LongValue
{
    // Basic unary methods.

    public LongValue negate()
    {
        return this;
    }

    public IntegerValue convertToInteger()
    {
        return BasicValueFactory.INTEGER_VALUE;
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

    public LongValue generalize(LongValue other)
    {
        return this;
    }

    public LongValue add(LongValue other)
    {
        return this;
    }

    public LongValue subtract(LongValue other)
    {
        return this;
    }

    public LongValue subtractFrom(LongValue other)
    {
        return this;
    }

    public LongValue multiply(LongValue other)
    throws ArithmeticException
    {
        return this;
    }

    public LongValue divide(LongValue other)
    throws ArithmeticException
    {
        return this;
    }

    public LongValue divideOf(LongValue other)
    throws ArithmeticException
    {
        return this;
    }

    public LongValue remainder(LongValue other)
    throws ArithmeticException
    {
        return this;
    }

    public LongValue remainderOf(LongValue other)
    throws ArithmeticException
    {
        return this;
    }

    public LongValue shiftLeft(IntegerValue other)
    {
        return this;
    }

    public LongValue shiftRight(IntegerValue other)
    {
        return this;
    }

    public LongValue unsignedShiftRight(IntegerValue other)
    {
        return this;
    }

    public LongValue and(LongValue other)
    {
        return this;
    }

    public LongValue or(LongValue other)
    {
        return this;
    }

    public LongValue xor(LongValue other)
    {
        return this;
    }

    public IntegerValue compare(LongValue other)
    {
        return BasicValueFactory.INTEGER_VALUE;
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
        return "l";
    }
}