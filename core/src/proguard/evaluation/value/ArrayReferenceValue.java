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

import proguard.classfile.Clazz;

/**
 * This {@link ReferenceValue} represents a partially evaluated array. It has an array
 * length and possibly array values (up to a fixed maximum number). It is not
 * immutable.
 *
 * @author Eric Lafortune
 */
class ArrayReferenceValue extends TypedReferenceValue
{
    protected final IntegerValue arrayLength;


    /**
     * Creates a new ArrayReferenceValue.
     */
    public ArrayReferenceValue(String       type,
                               Clazz        referencedClass,
                               boolean      mayBeExtension,
                               IntegerValue arrayLength)
    {
        super(type, referencedClass, mayBeExtension, false);

        this.arrayLength = arrayLength;
    }


    // Implementations for ReferenceValue.

    public IntegerValue arrayLength(ValueFactory valueFactory)
    {
        return arrayLength;
    }


     // Implementations of binary methods of ReferenceValue.

    public ReferenceValue generalize(ReferenceValue other)
    {
        return other.generalize(this);
    }


    public int equal(ReferenceValue other)
    {
        return other.equal(this);
    }


//    // Implementations of binary ReferenceValue methods with
//    // IdentifiedReferenceValue arguments.
//
//    public ReferenceValue generalize(IdentifiedReferenceValue other)
//    {
//        return generalize((TypedReferenceValue)other);
//    }
//
//
//    public int equal(IdentifiedReferenceValue other)
//    {
//        return equal((TypedReferenceValue)other);
//    }


    // Implementations of binary ReferenceValue methods with
    // ArrayReferenceValue arguments.

    public ReferenceValue generalize(ArrayReferenceValue other)
    {
        return
            this.equals(other)                            ? this :
            this.type != null            &&
            this.type.equals(other.type) &&
            this.referencedClass == other.referencedClass ? new ArrayReferenceValue(this.type,
                                                                                    this.referencedClass,
                                                                                    this.mayBeExtension || other.mayBeExtension,
                                                                                    this.arrayLength.generalize(other.arrayLength)) :
                                                            generalize((TypedReferenceValue)other);
    }


    public int equal(ArrayReferenceValue other)
    {
        if (this.arrayLength.equal(other.arrayLength) == NEVER)
        {
            return NEVER;
        }

        return equal((TypedReferenceValue)other);
    }


//    // Implementations of binary ReferenceValue methods with
//    // IdentifiedArrayReferenceValue arguments.
//
//    public ReferenceValue generalize(IdentifiedArrayReferenceValue other)
//    {
//        return generalize((ArrayReferenceValue)other);
//    }
//
//
//    public int equal(IdentifiedArrayReferenceValue other)
//    {
//        return equal((ArrayReferenceValue)other);
//    }
//
//
//    // Implementations of binary ReferenceValue methods with
//    // DetailedArrayReferenceValue arguments.
//
//    public ReferenceValue generalize(DetailedArrayReferenceValue other)
//    {
//        return generalize((IdentifiedArrayReferenceValue)other);
//    }
//
//
//    public int equal(DetailedArrayReferenceValue other)
//    {
//        return equal((IdentifiedArrayReferenceValue)other);
//    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (!super.equals(object))
        {
            return false;
        }

        ArrayReferenceValue other = (ArrayReferenceValue)object;
        return this.arrayLength.equals(other.arrayLength);
    }


    public int hashCode()
    {
        return super.hashCode() ^
               arrayLength.hashCode();
    }


    public String toString()
    {
        return super.toString() + '['+arrayLength+']';
    }
}
