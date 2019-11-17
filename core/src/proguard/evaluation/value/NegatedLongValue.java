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
 * This LongValue represents a long value that is negated.
 *
 * @author Eric Lafortune
 */
final class NegatedLongValue extends SpecificLongValue
{
    private final LongValue longValue;


    /**
     * Creates a new negated long value of the given long value.
     */
    public NegatedLongValue(LongValue longValue)
    {
        this.longValue = longValue;
    }


    // Implementations of unary methods of LongValue.

    public LongValue negate()
    {
        return longValue;
    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        return this == object ||
               super.equals(object) &&
               this.longValue.equals(((NegatedLongValue)object).longValue);
    }


    public int hashCode()
    {
        return super.hashCode() ^
               longValue.hashCode();
    }


    public String toString()
    {
        return "-"+longValue;
    }
}