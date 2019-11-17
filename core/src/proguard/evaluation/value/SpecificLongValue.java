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
 * This LongValue represents a specific long value.
 *
 * @author Eric Lafortune
 */
abstract class SpecificLongValue extends LongValue
{
    // Implementations of unary methods of LongValue.

    public LongValue negate()
    {
        return new NegatedLongValue(this);
    }

    public IntegerValue convertToInteger()
    {
        return new ConvertedIntegerValue(this);
    }

    public FloatValue convertToFloat()
    {
        return new ConvertedFloatValue(this);
    }

    public DoubleValue convertToDouble()
    {
        return new ConvertedDoubleValue(this);
    }


    // Implementations of binary methods of LongValue.

    public LongValue generalize(LongValue other)
    {
        return other.generalize(this);
    }

    public LongValue add(LongValue other)
    {
        return other.add(this);
    }

    public LongValue subtract(LongValue other)
    {
        return other.subtractFrom(this);
    }

    public LongValue subtractFrom(LongValue other)
    {
        return other.subtract(this);
    }

    public LongValue multiply(LongValue other)
    {
        return other.multiply(this);
    }

    public LongValue divide(LongValue other)
    throws ArithmeticException
    {
        return other.divideOf(this);
    }

    public LongValue divideOf(LongValue other)
    throws ArithmeticException
    {
        return other.divide(this);
    }

    public LongValue remainder(LongValue other)
    throws ArithmeticException
    {
        return other.remainderOf(this);
    }

    public LongValue remainderOf(LongValue other)
    throws ArithmeticException
    {
        return other.remainder(this);
    }

    public LongValue shiftLeft(IntegerValue other)
    {
        return other.shiftLeftOf(this);
    }

    public LongValue shiftRight(IntegerValue other)
    {
        return other.shiftRightOf(this);
    }

    public LongValue unsignedShiftRight(IntegerValue other)
    {
        return other.unsignedShiftRightOf(this);
    }

    public LongValue and(LongValue other)
    {
        return other.and(this);
    }

    public LongValue or(LongValue other)
    {
        return other.or(this);
    }

    public LongValue xor(LongValue other)
    {
        return other.xor(this);
    }

    public IntegerValue compare(LongValue other)
    {
        return other.compareReverse(this);
    }


    // Implementations of binary LongValue methods with SpecificLongValue
    // arguments.

    public LongValue generalize(SpecificLongValue other)
    {
        return this.equals(other) ? this : BasicValueFactory.LONG_VALUE;
    }

    public LongValue add(SpecificLongValue other)
    {
        return new CompositeLongValue(this, CompositeLongValue.ADD, other);
    }

    public LongValue subtract(SpecificLongValue other)
    {
        return this.equals(other) ?
            ParticularValueFactory.LONG_VALUE_0 :
            new CompositeLongValue(this, CompositeLongValue.SUBTRACT, other);
    }

    public LongValue subtractFrom(SpecificLongValue other)
    {
        return this.equals(other) ?
            ParticularValueFactory.LONG_VALUE_0 :
            new CompositeLongValue(other, CompositeLongValue.SUBTRACT, this);
    }

    public LongValue multiply(SpecificLongValue other)
    {
        return new CompositeLongValue(this, CompositeLongValue.MULTIPLY, other);
    }

    public LongValue divide(SpecificLongValue other)
    throws ArithmeticException
    {
        return new CompositeLongValue(this, CompositeLongValue.DIVIDE, other);
    }

    public LongValue divideOf(SpecificLongValue other)
    throws ArithmeticException
    {
        return new CompositeLongValue(other, CompositeLongValue.DIVIDE, this);
    }

    public LongValue remainder(SpecificLongValue other)
    throws ArithmeticException
    {
        return new CompositeLongValue(this, CompositeLongValue.REMAINDER, other);
    }

    public LongValue remainderOf(SpecificLongValue other)
    throws ArithmeticException
    {
        return new CompositeLongValue(other, CompositeLongValue.REMAINDER, this);
    }

    public LongValue shiftLeft(SpecificLongValue other)
    {
        return new CompositeLongValue(this, CompositeLongValue.SHIFT_LEFT, other);
    }

    public LongValue shiftRight(SpecificLongValue other)
    {
        return new CompositeLongValue(this, CompositeLongValue.SHIFT_RIGHT, other);
    }

    public LongValue unsignedShiftRight(SpecificLongValue other)
    {
        return new CompositeLongValue(this, CompositeLongValue.UNSIGNED_SHIFT_RIGHT, other);
    }

    public LongValue and(SpecificLongValue other)
    {
        return this.equals(other) ?
            this :
            new CompositeLongValue(other, CompositeLongValue.AND, this);
    }

    public LongValue or(SpecificLongValue other)
    {
        return this.equals(other) ?
            this :
            new CompositeLongValue(other, CompositeLongValue.OR, this);
    }

    public LongValue xor(SpecificLongValue other)
    {
        return this.equals(other) ?
            ParticularValueFactory.LONG_VALUE_0 :
            new CompositeLongValue(other, CompositeLongValue.XOR, this);
    }

    public IntegerValue compare(SpecificLongValue other)
    {
        return new ComparisonValue(this, other);
    }


    // Implementations for Value.

    public boolean isSpecific()
    {
        return true;
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
}
