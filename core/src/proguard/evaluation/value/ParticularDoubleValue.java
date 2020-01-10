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
 * This {@link DoubleValue} represents a particular double value.
 *
 * @author Eric Lafortune
 */
final class ParticularDoubleValue extends SpecificDoubleValue
{
    private final double value;


    /**
     * Creates a new particular double value.
     */
    public ParticularDoubleValue(double value)
    {
        this.value = value;
    }


    // Implementations for DoubleValue.

    public double value()
    {
        return value;
    }


    // Implementations of unary methods of DoubleValue.

    public DoubleValue negate()
    {
        return new ParticularDoubleValue(-value);
    }

    public IntegerValue convertToInteger()
    {
        return new ParticularIntegerValue((int)value);
    }

    public LongValue convertToLong()
    {
        return new ParticularLongValue((long)value);
    }

    public FloatValue convertToFloat()
    {
        return new ParticularFloatValue((float)value);
    }


    // Implementations of binary methods of DoubleValue.

    public DoubleValue generalize(DoubleValue other)
    {
        return other.generalize(this);
    }

    public DoubleValue add(DoubleValue other)
    {
        // Careful: -0.0 + 0.0 == 0.0
        //return value == 0.0 ? other : other.add(this);
        return other.add(this);
    }

    public DoubleValue subtract(DoubleValue other)
    {
        // Careful: -0.0 + 0.0 == 0.0
        //return value == 0.0 ? other.negate() : other.subtractFrom(this);
        return other.subtractFrom(this);
    }

    public DoubleValue subtractFrom(DoubleValue other)
    {
        // Careful: -0.0 + 0.0 == 0.0
        //return value == 0.0 ? other : other.subtract(this);
        return other.subtract(this);
    }

    public DoubleValue multiply(DoubleValue other)
    {
        return other.multiply(this);
    }

    public DoubleValue divide(DoubleValue other)
    {
        return other.divideOf(this);
    }

    public DoubleValue divideOf(DoubleValue other)
    {
        return other.divide(this);
    }

    public DoubleValue remainder(DoubleValue other)
    {
        return other.remainderOf(this);
    }

    public DoubleValue remainderOf(DoubleValue other)
    {
        return other.remainder(this);
    }

    public IntegerValue compare(DoubleValue other)
    {
        return other.compareReverse(this);
    }


    // Implementations of binary DoubleValue methods with ParticularDoubleValue
    // arguments.

    public DoubleValue generalize(ParticularDoubleValue other)
    {
        // Also handle NaN and Infinity.
        return Double.doubleToRawLongBits(this.value) ==
               Double.doubleToRawLongBits(other.value) ?
                   this : BasicValueFactory.DOUBLE_VALUE;
    }

    public DoubleValue add(ParticularDoubleValue other)
    {
        return new ParticularDoubleValue(this.value + other.value);
    }

    public DoubleValue subtract(ParticularDoubleValue other)
    {
        return new ParticularDoubleValue(this.value - other.value);
    }

    public DoubleValue subtractFrom(ParticularDoubleValue other)
    {
        return new ParticularDoubleValue(other.value - this.value);
    }

    public DoubleValue multiply(ParticularDoubleValue other)
    {
        return new ParticularDoubleValue(this.value * other.value);
    }

    public DoubleValue divide(ParticularDoubleValue other)
    {
        return new ParticularDoubleValue(this.value / other.value);
    }

    public DoubleValue divideOf(ParticularDoubleValue other)
    {
        return new ParticularDoubleValue(other.value / this.value);
    }

    public DoubleValue remainder(ParticularDoubleValue other)
    {
        return new ParticularDoubleValue(this.value % other.value);
    }

    public DoubleValue remainderOf(ParticularDoubleValue other)
    {
        return new ParticularDoubleValue(other.value % this.value);
    }

    public IntegerValue compare(ParticularDoubleValue other)
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
              Double.doubleToLongBits(this.value) ==
              Double.doubleToLongBits(((ParticularDoubleValue)object).value);
    }


    public int hashCode()
    {
        return super.hashCode() ^
               (int)Double.doubleToLongBits(value);
    }


    public String toString()
    {
        return value+"d";
    }
}