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
 * This {@link IntegerValue} represents the result of a comparisons of two scalar
 * values.
 *
 * @author Eric Lafortune
 */
final class ComparisonValue extends SpecificIntegerValue
{
    private final Value value1;
    private final Value value2;


    /**
     * Creates a new comparison integer value of the two given scalar values.
     */
    public ComparisonValue(Value value1,
                           Value value2)
    {
        this.value1 = value1;
        this.value2 = value2;
    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        return this == object ||
               super.equals(object) &&
               this.value1.equals(((ComparisonValue)object).value1) &&
               this.value2.equals(((ComparisonValue)object).value2);
    }


    public int hashCode()
    {
        return super.hashCode() ^
               value1.hashCode() ^
               value2.hashCode();
    }


    public String toString()
    {
        return "("+value1+"~"+ value2 +")";
    }
}