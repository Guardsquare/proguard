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
 * This FloatValue represents a particular float value.
 *
 * @author Eric Lafortune
 */
final class ParticularFloatValue extends SpecificFloatValue
{
    private final float value;


    /**
     * Creates a new particular float value.
     */
    public ParticularFloatValue(float value)
    {
        this.value = value;
    }


    // Implementations for FloatValue.

    public float value()
    {
        return value;
    }


    // Implementations of unary methods of FloatValue.

    public FloatValue negate()
    {
        return new ParticularFloatValue(-value);
    }

    public IntegerValue convertToInteger()
    {
        return new ParticularIntegerValue((int)value);
    }

    public LongValue convertToLong()
    {
        return new ParticularLongValue((long)value);
    }

    public DoubleValue convertToDouble()
    {
        return new ParticularDoubleValue((float)value);
    }


    // Implementations of binary methods of FloatValue.

    public FloatValue generalize(FloatValue other)
    {
        return other.generalize(this);
    }

    public FloatValue add(FloatValue other)
    {
        // Careful: -0.0 + 0.0 == 0.0
        //return value == 0.0 ? other : other.add(this);
        return other.add(this);
    }

    public FloatValue subtract(FloatValue other)
    {
        // Careful: -0.0 + 0.0 == 0.0
        //return value == 0.0 ? other.negate() : other.subtractFrom(this);
        return other.subtractFrom(this);
    }

    public FloatValue subtractFrom(FloatValue other)
    {
        // Careful: -0.0 + 0.0 == 0.0
        //return value == 0.0 ? other : other.subtract(this);
        return other.subtract(this);
    }

    public FloatValue multiply(FloatValue other)
    {
        return other.multiply(this);
    }

    public FloatValue divide(FloatValue other)
    {
        return other.divideOf(this);
    }

    public FloatValue divideOf(FloatValue other)
    {
        return other.divide(this);
    }

    public FloatValue remainder(FloatValue other)
    {
        return other.remainderOf(this);
    }

    public FloatValue remainderOf(FloatValue other)
    {
        return other.remainder(this);
    }

    public IntegerValue compare(FloatValue other)
    {
        return other.compareReverse(this);
    }


    // Implementations of binary FloatValue methods with ParticularFloatValue
    // arguments.

    public FloatValue generalize(ParticularFloatValue other)
    {
        // Also handle NaN and Infinity.
        return Float.floatToRawIntBits(this.value) ==
               Float.floatToRawIntBits(other.value) ?
                   this : BasicValueFactory.FLOAT_VALUE;
    }

    public FloatValue add(ParticularFloatValue other)
    {
        return new ParticularFloatValue(this.value + other.value);
    }

    public FloatValue subtract(ParticularFloatValue other)
    {
        return new ParticularFloatValue(this.value - other.value);
    }

    public FloatValue subtractFrom(ParticularFloatValue other)
    {
        return new ParticularFloatValue(other.value - this.value);
    }

    public FloatValue multiply(ParticularFloatValue other)
    {
        return new ParticularFloatValue(this.value * other.value);
    }

    public FloatValue divide(ParticularFloatValue other)
    {
        return new ParticularFloatValue(this.value / other.value);
    }

    public FloatValue divideOf(ParticularFloatValue other)
    {
        return new ParticularFloatValue(other.value / this.value);
    }

    public FloatValue remainder(ParticularFloatValue other)
    {
        return new ParticularFloatValue(this.value % other.value);
    }

    public FloatValue remainderOf(ParticularFloatValue other)
    {
        return new ParticularFloatValue(other.value % this.value);
    }

    public IntegerValue compare(ParticularFloatValue other)
    {
        return this.value <  other.value ? ParticularValueFactory.INTEGER_VALUE_M1 :
               this.value == other.value ? ParticularValueFactory.INTEGER_VALUE_0  :
                                           ParticularValueFactory.INTEGER_VALUE_1;
    }


    // Implementations for Value.

    public boolean isParticular()
    {
        return true;
    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        // Also handle NaN and Infinity.
        return super.equals(object) &&
               Float.floatToIntBits(this.value) ==
               Float.floatToIntBits(((ParticularFloatValue)object).value);
    }


    public int hashCode()
    {
        return super.hashCode() ^
               Float.floatToIntBits(value);
    }


    public String toString()
    {
        return value+"f";
    }
}