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
 * {@link FloatValue} that is unknown.
 *
 * @author Eric Lafortune
 */
public class UnknownFloatValue extends FloatValue
{
    // Basic unary methods.

    public FloatValue negate()
    {
        return this;
    }

    public IntegerValue convertToInteger()
    {
        return BasicValueFactory.INTEGER_VALUE;
    }

    public LongValue convertToLong()
    {
        return BasicValueFactory.LONG_VALUE;
    }

    public DoubleValue convertToDouble()
    {
        return BasicValueFactory.DOUBLE_VALUE;
    }


    // Basic binary methods.

    public FloatValue generalize(FloatValue other)
    {
        return this;
    }

    public FloatValue add(FloatValue other)
    {
        return this;
    }

    public FloatValue subtract(FloatValue other)
    {
        return this;
    }

    public FloatValue subtractFrom(FloatValue other)
    {
        return this;
    }

    public FloatValue multiply(FloatValue other)
    {
        return this;
    }

    public FloatValue divide(FloatValue other)
    {
        return this;
    }

    public FloatValue divideOf(FloatValue other)
    {
        return this;
    }

    public FloatValue remainder(FloatValue other)
    {
        return this;
    }

    public FloatValue remainderOf(FloatValue other)
    {
        return this;
    }

    public IntegerValue compare(FloatValue other)
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
        return "f";
    }
}
