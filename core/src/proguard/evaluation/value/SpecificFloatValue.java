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
 * This {@link FloatValue} represents a specific float value.
 *
 * @author Eric Lafortune
 */
abstract class SpecificFloatValue extends FloatValue
{
    // Implementations of unary methods of FloatValue.

    public FloatValue negate()
    {
        return new NegatedFloatValue(this);
    }

    public IntegerValue convertToInteger()
    {
        return new ConvertedIntegerValue(this);
    }

    public LongValue convertToLong()
    {
        return new ConvertedLongValue(this);
    }

    public DoubleValue convertToDouble()
    {
        return new ConvertedDoubleValue(this);
    }


    // Implementations of binary methods of FloatValue.

    public FloatValue generalize(FloatValue other)
    {
        return other.generalize(this);
    }

    public FloatValue add(FloatValue other)
    {
        return other.add(this);
    }

    public FloatValue subtract(FloatValue other)
    {
        return other.subtractFrom(this);
    }

    public FloatValue subtractFrom(FloatValue other)
    {
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


    // Implementations of binary FloatValue methods with SpecificFloatValue
    // arguments.

    public FloatValue generalize(SpecificFloatValue other)
    {
        return this.equals(other) ? this : BasicValueFactory.FLOAT_VALUE;
    }

    public FloatValue add(SpecificFloatValue other)
    {
        return new CompositeFloatValue(this, CompositeFloatValue.ADD, other);
    }

    public FloatValue subtract(SpecificFloatValue other)
    {
        return new CompositeFloatValue(this, CompositeFloatValue.SUBTRACT, other);
    }

    public FloatValue subtractFrom(SpecificFloatValue other)
    {
        return new CompositeFloatValue(other, CompositeFloatValue.SUBTRACT, this);
    }

    public FloatValue multiply(SpecificFloatValue other)
    {
        return new CompositeFloatValue(this, CompositeFloatValue.MULTIPLY, other);
    }

    public FloatValue divide(SpecificFloatValue other)
    {
        return new CompositeFloatValue(this, CompositeFloatValue.DIVIDE, other);
    }

    public FloatValue divideOf(SpecificFloatValue other)
    {
        return new CompositeFloatValue(other, CompositeFloatValue.DIVIDE, this);
    }

    public FloatValue remainder(SpecificFloatValue other)
    {
        return new CompositeFloatValue(this, CompositeFloatValue.REMAINDER, other);
    }

    public FloatValue remainderOf(SpecificFloatValue other)
    {
        return new CompositeFloatValue(other, CompositeFloatValue.REMAINDER, this);
    }

    public IntegerValue compare(SpecificFloatValue other)
    {
        return BasicValueFactory.INTEGER_VALUE;

        // Not handling NaN properly.
        //return this.equals(other) ?
        //    ParticularValueFactory.INTEGER_VALUE_0 :
        //    new ComparisonValue(this, other);
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
