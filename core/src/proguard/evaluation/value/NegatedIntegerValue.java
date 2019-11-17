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
 * This IntegerValue represents a integer value that is negated.
 *
 * @author Eric Lafortune
 */
final class NegatedIntegerValue extends SpecificIntegerValue
{
    private final IntegerValue integerValue;


    /**
     * Creates a new negated integer value of the given integer value.
     */
    public NegatedIntegerValue(IntegerValue integerValue)
    {
        this.integerValue = integerValue;
    }


    // Implementations of unary methods of IntegerValue.

    public IntegerValue negate()
    {
        return integerValue;
    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        return this == object ||
               super.equals(object) &&
               this.integerValue.equals(((NegatedIntegerValue)object).integerValue);
    }


    public int hashCode()
    {
        return super.hashCode() ^
               integerValue.hashCode();
    }


    public String toString()
    {
        return "-"+integerValue;
    }
}