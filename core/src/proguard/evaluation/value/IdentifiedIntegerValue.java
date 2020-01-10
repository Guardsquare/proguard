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
 * This {@link IntegerValue} represents a integer value that is identified by a unique ID.
 *
 * @author Eric Lafortune
 */
final class IdentifiedIntegerValue extends SpecificIntegerValue
{
    private final ValueFactory valuefactory;
    private final int          id;


    /**
     * Creates a new integer value with the given ID.
     */
    public IdentifiedIntegerValue(ValueFactory valuefactory, int id)
    {
        this.valuefactory = valuefactory;
        this.id           = id;
    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        return this == object ||
               super.equals(object) &&
               this.valuefactory.equals(((IdentifiedIntegerValue)object).valuefactory) &&
               this.id == ((IdentifiedIntegerValue)object).id;
    }


    public int hashCode()
    {
        return super.hashCode() ^
               valuefactory.hashCode() ^
               id;
    }


    public String toString()
    {
        return "i"+id;
    }
}