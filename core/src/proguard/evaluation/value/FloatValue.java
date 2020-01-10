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

import proguard.classfile.*;

/**
 * Representation of a partially evaluated float value.
 *
 * @author Eric Lafortune
 */
public abstract class FloatValue extends Category1Value
{
    /**
     * Returns the specific float value, if applicable.
     */
    public float value()
    {
        return 0f;
    }


    // Basic unary methods.

    /**
     * Returns the negated value of this FloatValue.
     */
    public abstract FloatValue negate();

    /**
     * Converts this FloatValue to an IntegerValue.
     */
    public abstract IntegerValue convertToInteger();

    /**
     * Converts this FloatValue to a LongValue.
     */
    public abstract LongValue convertToLong();

    /**
     * Converts this FloatValue to a DoubleValue.
     */
    public abstract DoubleValue convertToDouble();


    // Basic binary methods.

    /**
     * Returns the generalization of this FloatValue and the given other
     * FloatValue.
     */
    public abstract FloatValue generalize(FloatValue other);


    /**
     * Returns the sum of this FloatValue and the given FloatValue.
     */
    public abstract FloatValue add(FloatValue other);

    /**
     * Returns the difference of this FloatValue and the given FloatValue.
     */
    public abstract FloatValue subtract(FloatValue other);

    /**
     * Returns the difference of the given FloatValue and this FloatValue.
     */
    public abstract FloatValue subtractFrom(FloatValue other);

    /**
     * Returns the product of this FloatValue and the given FloatValue.
     */
    public abstract FloatValue multiply(FloatValue other);

    /**
     * Returns the quotient of this FloatValue and the given FloatValue.
     */
    public abstract FloatValue divide(FloatValue other);

    /**
     * Returns the quotient of the given FloatValue and this FloatValue.
     */
    public abstract FloatValue divideOf(FloatValue other);

    /**
     * Returns the remainder of this FloatValue divided by the given FloatValue.
     */
    public abstract FloatValue remainder(FloatValue other);

    /**
     * Returns the remainder of the given FloatValue divided by this FloatValue.
     */
    public abstract FloatValue remainderOf(FloatValue other);

    /**
     * Returns an IntegerValue with value -1, 0, or 1, if this FloatValue is
     * less than, equal to, or greater than the given FloatValue, respectively.
     */
    public abstract IntegerValue compare(FloatValue other);


    // Derived binary methods.

    /**
     * Returns an IntegerValue with value 1, 0, or -1, if this FloatValue is
     * less than, equal to, or greater than the given FloatValue, respectively.
     */
    public final IntegerValue compareReverse(FloatValue other)
    {
        return compare(other).negate();
    }


    // Similar binary methods, but this time with more specific arguments.

    /**
     * Returns the generalization of this FloatValue and the given other
     * SpecificFloatValue.
     */
    public FloatValue generalize(SpecificFloatValue other)
    {
        return generalize((FloatValue)other);
    }


    /**
     * Returns the sum of this FloatValue and the given SpecificFloatValue.
     */
    public FloatValue add(SpecificFloatValue other)
    {
        return add((FloatValue)other);
    }

    /**
     * Returns the difference of this FloatValue and the given SpecificFloatValue.
     */
    public FloatValue subtract(SpecificFloatValue other)
    {
        return subtract((FloatValue)other);
    }

    /**
     * Returns the difference of the given SpecificFloatValue and this FloatValue.
     */
    public FloatValue subtractFrom(SpecificFloatValue other)
    {
        return subtractFrom((FloatValue)other);
    }

    /**
     * Returns the product of this FloatValue and the given SpecificFloatValue.
     */
    public FloatValue multiply(SpecificFloatValue other)
    {
        return multiply((FloatValue)other);
    }

    /**
     * Returns the quotient of this FloatValue and the given SpecificFloatValue.
     */
    public FloatValue divide(SpecificFloatValue other)
    {
        return divide((FloatValue)other);
    }

    /**
     * Returns the quotient of the given SpecificFloatValue and this
     * FloatValue.
     */
    public FloatValue divideOf(SpecificFloatValue other)
    {
        return divideOf((FloatValue)other);
    }

    /**
     * Returns the remainder of this FloatValue divided by the given
     * SpecificFloatValue.
     */
    public FloatValue remainder(SpecificFloatValue other)
    {
        return remainder((FloatValue)other);
    }

    /**
     * Returns the remainder of the given SpecificFloatValue and this
     * FloatValue.
     */
    public FloatValue remainderOf(SpecificFloatValue other)
    {
        return remainderOf((FloatValue)other);
    }

    /**
     * Returns an IntegerValue with value -1, 0, or 1, if this FloatValue is
     * less than, equal to, or greater than the given SpecificFloatValue,
     * respectively.
     */
    public IntegerValue compare(SpecificFloatValue other)
    {
        return compare((FloatValue)other);
    }


    // Derived binary methods.

    /**
     * Returns an IntegerValue with value 1, 0, or -1, if this FloatValue is
     * less than, equal to, or greater than the given SpecificFloatValue,
     * respectively.
     */
    public final IntegerValue compareReverse(SpecificFloatValue other)
    {
        return compare(other).negate();
    }


    // Similar binary methods, but this time with particular arguments.

    /**
     * Returns the generalization of this FloatValue and the given other
     * ParticularFloatValue.
     */
    public FloatValue generalize(ParticularFloatValue other)
    {
        return generalize((SpecificFloatValue)other);
    }


    /**
     * Returns the sum of this FloatValue and the given ParticularFloatValue.
     */
    public FloatValue add(ParticularFloatValue other)
    {
        return add((SpecificFloatValue)other);
    }

    /**
     * Returns the difference of this FloatValue and the given ParticularFloatValue.
     */
    public FloatValue subtract(ParticularFloatValue other)
    {
        return subtract((SpecificFloatValue)other);
    }

    /**
     * Returns the difference of the given ParticularFloatValue and this FloatValue.
     */
    public FloatValue subtractFrom(ParticularFloatValue other)
    {
        return subtractFrom((SpecificFloatValue)other);
    }

    /**
     * Returns the product of this FloatValue and the given ParticularFloatValue.
     */
    public FloatValue multiply(ParticularFloatValue other)
    {
        return multiply((SpecificFloatValue)other);
    }

    /**
     * Returns the quotient of this FloatValue and the given ParticularFloatValue.
     */
    public FloatValue divide(ParticularFloatValue other)
    {
        return divide((SpecificFloatValue)other);
    }

    /**
     * Returns the quotient of the given ParticularFloatValue and this
     * FloatValue.
     */
    public FloatValue divideOf(ParticularFloatValue other)
    {
        return divideOf((SpecificFloatValue)other);
    }

    /**
     * Returns the remainder of this FloatValue divided by the given
     * ParticularFloatValue.
     */
    public FloatValue remainder(ParticularFloatValue other)
    {
        return remainder((SpecificFloatValue)other);
    }

    /**
     * Returns the remainder of the given ParticularFloatValue and this
     * FloatValue.
     */
    public FloatValue remainderOf(ParticularFloatValue other)
    {
        return remainderOf((SpecificFloatValue)other);
    }

    /**
     * Returns an IntegerValue with value -1, 0, or 1, if this FloatValue is
     * less than, equal to, or greater than the given ParticularFloatValue,
     * respectively.
     */
    public IntegerValue compare(ParticularFloatValue other)
    {
        return compare((SpecificFloatValue)other);
    }


    // Derived binary methods.

    /**
     * Returns an IntegerValue with value 1, 0, or -1, if this FloatValue is
     * less than, equal to, or greater than the given ParticularFloatValue,
     * respectively.
     */
    public final IntegerValue compareReverse(ParticularFloatValue other)
    {
        return compare(other).negate();
    }


    // Implementations for Value.

    public final FloatValue floatValue()
    {
        return this;
    }

    public final Value generalize(Value other)
    {
        return this.generalize(other.floatValue());
    }

    public final int computationalType()
    {
        return TYPE_FLOAT;
    }

    public final String internalType()
    {
        return String.valueOf(TypeConstants.FLOAT);
    }
}
