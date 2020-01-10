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
 * This {@link IntegerValue} represents a specific integer value.
 * <p/>
 * This class handles interactions with:
 * - RangeIntegerValue
 * - SpecificInteger (in general)
 * <p/>
 * It reverses and delegates interactions with:
 * - IntegerValue (in general)
 * <p/>
 * It notably doesn't handle interactions with:
 * - UnknownInteger
 *
 * @author Eric Lafortune
 */
abstract class SpecificIntegerValue extends IntegerValue
{
    // Implementations of unary methods of IntegerValue.

    public IntegerValue negate()
    {
        return new NegatedIntegerValue(this);
    }

    public IntegerValue convertToByte()
    {
        return new ConvertedByteValue(this);
    }

    public IntegerValue convertToCharacter()
    {
        return new ConvertedCharacterValue(this);
    }

    public IntegerValue convertToShort()
    {
        return new ConvertedShortValue(this);
    }

    public LongValue convertToLong()
    {
        return new ConvertedLongValue(this);
    }

    public FloatValue convertToFloat()
    {
        return new ConvertedFloatValue(this);
    }

    public DoubleValue convertToDouble()
    {
        return new ConvertedDoubleValue(this);
    }


    // Implementations of binary methods of IntegerValue.

    public IntegerValue generalize(IntegerValue other)
    {
        return other.generalize(this);
    }

    public IntegerValue add(IntegerValue other)
    {
        return other.add(this);
    }

    public IntegerValue subtract(IntegerValue other)
    {
        return other.subtractFrom(this);
    }

    public IntegerValue subtractFrom(IntegerValue other)
    {
        return other.subtract(this);
    }

    public IntegerValue multiply(IntegerValue other)
    {
        return other.multiply(this);
    }

    public IntegerValue divide(IntegerValue other)
    throws ArithmeticException
    {
        return other.divideOf(this);
    }

    public IntegerValue divideOf(IntegerValue other)
    throws ArithmeticException
    {
        return other.divide(this);
    }

    public IntegerValue remainder(IntegerValue other)
    throws ArithmeticException
    {
        return other.remainderOf(this);
    }

    public IntegerValue remainderOf(IntegerValue other)
    throws ArithmeticException
    {
        return other.remainder(this);
    }

    public IntegerValue shiftLeft(IntegerValue other)
    {
        return other.shiftLeftOf(this);
    }

    public IntegerValue shiftLeftOf(IntegerValue other)
    {
        return other.shiftLeft(this);
    }

    public IntegerValue shiftRight(IntegerValue other)
    {
        return other.shiftRightOf(this);
    }

    public IntegerValue shiftRightOf(IntegerValue other)
    {
        return other.shiftRight(this);
    }

    public IntegerValue unsignedShiftRight(IntegerValue other)
    {
        return other.unsignedShiftRightOf(this);
    }

    public IntegerValue unsignedShiftRightOf(IntegerValue other)
    {
        return other.unsignedShiftRight(this);
    }

    public LongValue shiftLeftOf(LongValue other)
    {
        return other.shiftLeft(this);
    }

    public LongValue shiftRightOf(LongValue other)
    {
        return other.shiftRight(this);
    }

    public LongValue unsignedShiftRightOf(LongValue other)
    {
        return other.unsignedShiftRight(this);
    }

    public IntegerValue and(IntegerValue other)
    {
        return other.and(this);
    }

    public IntegerValue or(IntegerValue other)
    {
        return other.or(this);
    }

    public IntegerValue xor(IntegerValue other)
    {
        return other.xor(this);
    }

    public int equal(IntegerValue other)
    {
        return other.equal(this);
    }

    public int lessThan(IntegerValue other)
    {
        return other.greaterThan(this);
    }

    public int lessThanOrEqual(IntegerValue other)
    {
        return other.greaterThanOrEqual(this);
    }


    // Implementations of binary IntegerValue methods with SpecificIntegerValue
    // arguments.

    public IntegerValue generalize(SpecificIntegerValue other)
    {
        return this.equals(other) ? this : BasicValueFactory.INTEGER_VALUE;
    }

    public IntegerValue add(SpecificIntegerValue other)
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.ADD, other);
    }

    public IntegerValue subtract(SpecificIntegerValue other)
    {
        return this.equals(other) ?
            ParticularValueFactory.INTEGER_VALUE_0 :
            new CompositeIntegerValue(this, CompositeIntegerValue.SUBTRACT, other);
    }

    public IntegerValue subtractFrom(SpecificIntegerValue other)
    {
        return this.equals(other) ?
            ParticularValueFactory.INTEGER_VALUE_0 :
            new CompositeIntegerValue(other, CompositeIntegerValue.SUBTRACT, this);
    }

    public IntegerValue multiply(SpecificIntegerValue other)
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.MULTIPLY, other);
    }

    public IntegerValue divide(SpecificIntegerValue other)
    throws ArithmeticException
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.DIVIDE, other);
    }

    public IntegerValue divideOf(SpecificIntegerValue other)
    throws ArithmeticException
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.DIVIDE, this);
    }

    public IntegerValue remainder(SpecificIntegerValue other)
    throws ArithmeticException
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.REMAINDER, other);
    }

    public IntegerValue remainderOf(SpecificIntegerValue other)
    throws ArithmeticException
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.REMAINDER, this);
    }

    public IntegerValue shiftLeft(SpecificIntegerValue other)
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.SHIFT_LEFT, other);
    }

    public IntegerValue shiftRight(SpecificIntegerValue other)
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.SHIFT_RIGHT, other);
    }

    public IntegerValue unsignedShiftRight(SpecificIntegerValue other)
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.UNSIGNED_SHIFT_RIGHT, other);
    }

    public IntegerValue shiftLeftOf(SpecificIntegerValue other)
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.SHIFT_LEFT, this);
    }

    public IntegerValue shiftRightOf(SpecificIntegerValue other)
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.SHIFT_RIGHT, this);
    }

    public IntegerValue unsignedShiftRightOf(SpecificIntegerValue other)
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.UNSIGNED_SHIFT_RIGHT, this);
    }

    public LongValue shiftLeftOf(SpecificLongValue other)
    {
        return new CompositeLongValue(other, CompositeLongValue.SHIFT_LEFT, this);
    }

    public LongValue shiftRightOf(SpecificLongValue other)
    {
        return new CompositeLongValue(other, CompositeLongValue.SHIFT_RIGHT, this);
    }

    public LongValue unsignedShiftRightOf(SpecificLongValue other)
    {
        return new CompositeLongValue(other, CompositeLongValue.UNSIGNED_SHIFT_RIGHT, this);
    }

    public IntegerValue and(SpecificIntegerValue other)
    {
        return this.equals(other) ?
            this :
            new CompositeIntegerValue(other, CompositeIntegerValue.AND, this);
    }

    public IntegerValue or(SpecificIntegerValue other)
    {
        return this.equals(other) ?
            this :
            new CompositeIntegerValue(other, CompositeIntegerValue.OR, this);
    }

    public IntegerValue xor(SpecificIntegerValue other)
    {
        return this.equals(other) ?
            ParticularValueFactory.INTEGER_VALUE_0 :
            new CompositeIntegerValue(other, CompositeIntegerValue.XOR, this);
    }

    public int equal(SpecificIntegerValue other)
    {
        return this.equals(other) ? ALWAYS : MAYBE;
    }

    public int lessThan(SpecificIntegerValue other)
    {
        return this.equals(other) ? NEVER : MAYBE;
    }

    public int lessThanOrEqual(SpecificIntegerValue other)
    {
        return this.equals(other) ? ALWAYS : MAYBE;
    }


    // Implementations of binary IntegerValue methods with RangeIntegerValue
    // arguments.

    public IntegerValue generalize(RangeIntegerValue other)
    {
        return BasicValueFactory.INTEGER_VALUE;
    }

    public IntegerValue add(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.ADD, other);
    }

    public IntegerValue subtract(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.SUBTRACT, other);
    }

    public IntegerValue subtractFrom(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.SUBTRACT, this);
    }

    public IntegerValue multiply(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.MULTIPLY, other);
    }

    public IntegerValue divide(RangeIntegerValue other)
    throws ArithmeticException
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.DIVIDE, other);
    }

    public IntegerValue divideOf(RangeIntegerValue other)
    throws ArithmeticException
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.DIVIDE, this);
    }

    public IntegerValue remainder(RangeIntegerValue other)
    throws ArithmeticException
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.REMAINDER, other);
    }

    public IntegerValue remainderOf(RangeIntegerValue other)
    throws ArithmeticException
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.REMAINDER, this);
    }

    public IntegerValue shiftLeft(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.SHIFT_LEFT, other);
    }

    public IntegerValue shiftRight(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.SHIFT_RIGHT, other);
    }

    public IntegerValue unsignedShiftRight(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(this, CompositeIntegerValue.UNSIGNED_SHIFT_RIGHT, other);
    }

    public IntegerValue shiftLeftOf(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.SHIFT_LEFT, this);
    }

    public IntegerValue shiftRightOf(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.SHIFT_RIGHT, this);
    }

    public IntegerValue unsignedShiftRightOf(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.UNSIGNED_SHIFT_RIGHT, this);
    }

    public IntegerValue and(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.AND, this);
    }

    public IntegerValue or(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.OR, this);
    }

    public IntegerValue xor(RangeIntegerValue other)
    {
        return new CompositeIntegerValue(other, CompositeIntegerValue.XOR, this);
    }

    public int equal(RangeIntegerValue other)
    {
        return MAYBE;
    }

    public int lessThan(RangeIntegerValue other)
    {
        return MAYBE;
    }

    public int lessThanOrEqual(RangeIntegerValue other)
    {
        return MAYBE;
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
