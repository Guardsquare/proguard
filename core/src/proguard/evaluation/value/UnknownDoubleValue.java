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
 * This class represents a partially evaluated double value.
 *
 * @author Eric Lafortune
 */
public class UnknownDoubleValue extends DoubleValue
{
    // Basic unary methods.

    public DoubleValue negate()
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

    public FloatValue convertToFloat()
    {
        return BasicValueFactory.FLOAT_VALUE;
    }


    // Basic binary methods.

    public DoubleValue generalize(DoubleValue other)
    {
        return this;
    }

    public DoubleValue add(DoubleValue other)
    {
        return this;
    }

    public DoubleValue subtract(DoubleValue other)
    {
        return this;
    }

    public DoubleValue subtractFrom(DoubleValue other)
    {
        return this;
    }

    public DoubleValue multiply(DoubleValue other)
    {
        return this;
    }

    public DoubleValue divide(DoubleValue other)
    {
        return this;
    }

    public DoubleValue divideOf(DoubleValue other)
    {
        return this;
    }

    public DoubleValue remainder(DoubleValue other)
    {
        return this;
    }

    public DoubleValue remainderOf(DoubleValue other)
    {
        return this;
    }

    public IntegerValue compare(DoubleValue other)
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
        return "d";
    }
}