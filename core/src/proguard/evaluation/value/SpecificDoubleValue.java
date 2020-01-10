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
 * This {@link DoubleValue} represents a specific double value.
 *
 * @author Eric Lafortune
 */
abstract class SpecificDoubleValue extends DoubleValue
{
    // Implementations of unary methods of DoubleValue.

    public DoubleValue negate()
    {
        return new NegatedDoubleValue(this);
    }

    public IntegerValue convertToInteger()
    {
        return new ConvertedIntegerValue(this);
    }

    public LongValue convertToLong()
    {
        return new ConvertedLongValue(this);
    }

    public FloatValue convertToFloat()
    {
        return new ConvertedFloatValue(this);
    }


    // Implementations of binary methods of DoubleValue.

    public DoubleValue generalize(DoubleValue other)
    {
        return other.generalize(this);
    }

    public DoubleValue add(DoubleValue other)
    {
        return other.add(this);
    }

    public DoubleValue subtract(DoubleValue other)
    {
        return other.subtractFrom(this);
    }

    public DoubleValue subtractFrom(DoubleValue other)
    {
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


    // Implementations of binary DoubleValue methods with SpecificDoubleValue
    // arguments.

    public DoubleValue generalize(SpecificDoubleValue other)
    {
        return this.equals(other) ? this : BasicValueFactory.DOUBLE_VALUE;
    }

    public DoubleValue add(SpecificDoubleValue other)
    {
        return new CompositeDoubleValue(this, CompositeDoubleValue.ADD, other);
    }

    public DoubleValue subtract(SpecificDoubleValue other)
    {
        return new CompositeDoubleValue(this, CompositeDoubleValue.SUBTRACT, other);
    }

    public DoubleValue subtractFrom(SpecificDoubleValue other)
    {
        return new CompositeDoubleValue(other, CompositeDoubleValue.SUBTRACT, this);
    }

    public DoubleValue multiply(SpecificDoubleValue other)
    {
        return new CompositeDoubleValue(this, CompositeDoubleValue.MULTIPLY, other);
    }

    public DoubleValue divide(SpecificDoubleValue other)
    {
        return new CompositeDoubleValue(this, CompositeDoubleValue.DIVIDE, other);
    }

    public DoubleValue divideOf(SpecificDoubleValue other)
    {
        return new CompositeDoubleValue(other, CompositeDoubleValue.DIVIDE, this);
    }

    public DoubleValue remainder(SpecificDoubleValue other)
    {
        return new CompositeDoubleValue(this, CompositeDoubleValue.REMAINDER, other);
    }

    public DoubleValue remainderOf(SpecificDoubleValue other)
    {
        return new CompositeDoubleValue(other, CompositeDoubleValue.REMAINDER, this);
    }

    public IntegerValue compare(SpecificDoubleValue other)
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
