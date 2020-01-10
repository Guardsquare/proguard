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

import proguard.classfile.TypeConstants;

/**
 * Representation of a partially evaluated long value.
 *
 * @author Eric Lafortune
 */
public abstract class LongValue extends Category2Value
{
    /**
     * Returns the specific long value, if applicable.
     */
    public long value()
    {
        return 0;
    }


    // Basic unary methods.

    /**
     * Returns the negated value of this LongValue.
     */
    public abstract LongValue negate();


    /**
     * Converts this LongValue to an IntegerValue.
     */
    public abstract IntegerValue convertToInteger();

    /**
     * Converts this LongValue to a FloatValue.
     */
    public abstract FloatValue convertToFloat();

    /**
     * Converts this LongValue to a DoubleValue.
     */
    public abstract DoubleValue convertToDouble();


    // Basic binary methods.

    /**
     * Returns the generalization of this LongValue and the given other
     * LongValue.
     */
    public LongValue generalize(LongValue other)
    {
        return other.generalize(this);
    }

    /**
     * Returns the sum of this LongValue and the given LongValue.
     */
    public LongValue add(LongValue other)
    {
        return other.add(this);
    }

    /**
     * Returns the difference of this LongValue and the given LongValue.
     */
    public LongValue subtract(LongValue other)
    {
        return other.subtractFrom(this);
    }

    /**
     * Returns the difference of the given LongValue and this LongValue.
     */
    public LongValue subtractFrom(LongValue other)
    {
        return other.subtract(this);
    }

    /**
     * Returns the product of this LongValue and the given LongValue.
     */
    public LongValue multiply(LongValue other)
    throws ArithmeticException
    {
        return other.multiply(this);
    }

    /**
     * Returns the quotient of this LongValue and the given LongValue.
     */
    public LongValue divide(LongValue other)
    throws ArithmeticException
    {
        return other.divideOf(this);
    }

    /**
     * Returns the quotient of the given LongValue and this LongValue.
     */
    public LongValue divideOf(LongValue other)
    throws ArithmeticException
    {
        return other.divide(this);
    }

    /**
     * Returns the remainder of this LongValue divided by the given
     * LongValue.
     */
    public LongValue remainder(LongValue other)
    throws ArithmeticException
    {
        return other.remainderOf(this);
    }

    /**
     * Returns the remainder of the given LongValue divided by this
     * LongValue.
     */
    public LongValue remainderOf(LongValue other)
    throws ArithmeticException
    {
        return other.remainder(this);
    }

    /**
     * Returns this LongValue, shifted left by the given IntegerValue.
     */
    public LongValue shiftLeft(IntegerValue other)
    {
        return other.shiftLeftOf(this);
    }

    /**
     * Returns this LongValue, shifted right by the given IntegerValue.
     */
    public LongValue shiftRight(IntegerValue other)
    {
        return other.shiftRightOf(this);
    }

    /**
     * Returns this unsigned LongValue, shifted left by the given
     * IntegerValue.
     */
    public LongValue unsignedShiftRight(IntegerValue other)
    {
        return other.unsignedShiftRightOf(this);
    }

    /**
     * Returns the logical <i>and</i> of this LongValue and the given
     * LongValue.
     */
    public LongValue and(LongValue other)
    {
        return other.and(this);
    }

    /**
     * Returns the logical <i>or</i> of this LongValue and the given
     * LongValue.
     */
    public LongValue or(LongValue other)
    {
        return other.or(this);
    }

    /**
     * Returns the logical <i>xor</i> of this LongValue and the given
     * LongValue.
     */
    public LongValue xor(LongValue other)
    {
        return other.xor(this);
    }

    /**
     * Returns an IntegerValue with value -1, 0, or 1, if this LongValue is
     * less than, equal to, or greater than the given LongValue, respectively.
     */
    public IntegerValue compare(LongValue other)
    {
        return other.compareReverse(this);
    }


    // Derived binary methods.

    /**
     * Returns an IntegerValue with value 1, 0, or -1, if this LongValue is
     * less than, equal to, or greater than the given LongValue, respectively.
     */
    public final IntegerValue compareReverse(LongValue other)
    {
        return compare(other).negate();
    }


    // Similar binary methods, but this time with more specific arguments.

    /**
     * Returns the generalization of this LongValue and the given other
     * SpecificLongValue.
     */
    public LongValue generalize(SpecificLongValue other)
    {
        return this;
    }


    /**
     * Returns the sum of this LongValue and the given SpecificLongValue.
     */
    public LongValue add(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns the difference of this LongValue and the given SpecificLongValue.
     */
    public LongValue subtract(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns the difference of the given SpecificLongValue and this LongValue.
     */
    public LongValue subtractFrom(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns the product of this LongValue and the given SpecificLongValue.
     */
    public LongValue multiply(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns the quotient of this LongValue and the given
     * SpecificLongValue.
     */
    public LongValue divide(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns the quotient of the given SpecificLongValue and this
     * LongValue.
     */
    public LongValue divideOf(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns the remainder of this LongValue divided by the given
     * SpecificLongValue.
     */
    public LongValue remainder(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns the remainder of the given SpecificLongValue divided by this
     * LongValue.
     */
    public LongValue remainderOf(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns this LongValue, shifted left by the given SpecificLongValue.
     */
    public LongValue shiftLeft(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns this LongValue, shifted right by the given SpecificLongValue.
     */
    public LongValue shiftRight(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns this unsigned LongValue, shifted right by the given
     * SpecificLongValue.
     */
    public LongValue unsignedShiftRight(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns the logical <i>and</i> of this LongValue and the given
     * SpecificLongValue.
     */
    public LongValue and(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns the logical <i>or</i> of this LongValue and the given
     * SpecificLongValue.
     */
    public LongValue or(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns the logical <i>xor</i> of this LongValue and the given
     * SpecificLongValue.
     */
    public LongValue xor(SpecificLongValue other)
    {
        return this;
    }

    /**
     * Returns an IntegerValue with value -1, 0, or 1, if this LongValue is
     * less than, equal to, or greater than the given SpecificLongValue,
     * respectively.
     */
    public IntegerValue compare(SpecificLongValue other)
    {
        return new ComparisonValue(this, other);
    }


    // Derived binary methods.

    /**
     * Returns an IntegerValue with value 1, 0, or -1, if this LongValue is
     * less than, equal to, or greater than the given SpecificLongValue,
     * respectively.
     */
    public final IntegerValue compareReverse(SpecificLongValue other)
    {
        return compare(other).negate();
    }


    // Similar binary methods, but this time with particular arguments.

    /**
     * Returns the generalization of this LongValue and the given other
     * ParticularLongValue.
     */
    public LongValue generalize(ParticularLongValue other)
    {
        return generalize((SpecificLongValue)other);
    }


    /**
     * Returns the sum of this LongValue and the given ParticularLongValue.
     */
    public LongValue add(ParticularLongValue other)
    {
        return add((SpecificLongValue)other);
    }

    /**
     * Returns the difference of this LongValue and the given ParticularLongValue.
     */
    public LongValue subtract(ParticularLongValue other)
    {
        return subtract((SpecificLongValue)other);
    }

    /**
     * Returns the difference of the given ParticularLongValue and this LongValue.
     */
    public LongValue subtractFrom(ParticularLongValue other)
    {
        return subtractFrom((SpecificLongValue)other);
    }

    /**
     * Returns the product of this LongValue and the given ParticularLongValue.
     */
    public LongValue multiply(ParticularLongValue other)
    {
        return multiply((SpecificLongValue)other);
    }

    /**
     * Returns the quotient of this LongValue and the given
     * ParticularLongValue.
     */
    public LongValue divide(ParticularLongValue other)
    {
        return divide((SpecificLongValue)other);
    }

    /**
     * Returns the quotient of the given ParticularLongValue and this
     * LongValue.
     */
    public LongValue divideOf(ParticularLongValue other)
    {
        return divideOf((SpecificLongValue)other);
    }

    /**
     * Returns the remainder of this LongValue divided by the given
     * ParticularLongValue.
     */
    public LongValue remainder(ParticularLongValue other)
    {
        return remainder((SpecificLongValue)other);
    }

    /**
     * Returns the remainder of the given ParticularLongValue divided by this
     * LongValue.
     */
    public LongValue remainderOf(ParticularLongValue other)
    {
        return remainderOf((SpecificLongValue)other);
    }

    /**
     * Returns this LongValue, shifted left by the given ParticularIntegerValue.
     */
    public LongValue shiftLeft(ParticularIntegerValue other)
    {
        return shiftLeft((SpecificIntegerValue)other);
    }

    /**
     * Returns this LongValue, shifted right by the given ParticularIntegerValue.
     */
    public LongValue shiftRight(ParticularIntegerValue other)
    {
        return shiftRight((SpecificIntegerValue)other);
    }

    /**
     * Returns this unsigned LongValue, shifted right by the given
     * ParticularIntegerValue.
     */
    public LongValue unsignedShiftRight(ParticularIntegerValue other)
    {
        return unsignedShiftRight((SpecificIntegerValue)other);
    }

    /**
     * Returns the logical <i>and</i> of this LongValue and the given
     * ParticularLongValue.
     */
    public LongValue and(ParticularLongValue other)
    {
        return and((SpecificLongValue)other);
    }

    /**
     * Returns the logical <i>or</i> of this LongValue and the given
     * ParticularLongValue.
     */
    public LongValue or(ParticularLongValue other)
    {
        return or((SpecificLongValue)other);
    }

    /**
     * Returns the logical <i>xor</i> of this LongValue and the given
     * ParticularLongValue.
     */
    public LongValue xor(ParticularLongValue other)
    {
        return xor((SpecificLongValue)other);
    }

    /**
     * Returns an IntegerValue with value -1, 0, or 1, if this LongValue is
     * less than, equal to, or greater than the given ParticularLongValue,
     * respectively.
     */
    public IntegerValue compare(ParticularLongValue other)
    {
        return compare((SpecificLongValue)other);
    }


    // Derived binary methods.

    /**
     * Returns an IntegerValue with value 1, 0, or -1, if this LongValue is
     * less than, equal to, or greater than the given ParticularLongValue,
     * respectively.
     */
    public final IntegerValue compareReverse(ParticularLongValue other)
    {
        return compare(other).negate();
    }


    // Implementations for Value.

    public final LongValue longValue()
    {
        return this;
    }

    public final Value generalize(Value other)
    {
        return this.generalize(other.longValue());
    }

    public final int computationalType()
    {
        return TYPE_LONG;
    }

    public final String internalType()
    {
        return String.valueOf(TypeConstants.LONG);
    }
}
