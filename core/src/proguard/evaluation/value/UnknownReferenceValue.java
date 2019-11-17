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

import proguard.classfile.*;

/**
 * This class represents a partially evaluated reference value.
 *
 * @author Eric Lafortune
 */
public class UnknownReferenceValue extends ReferenceValue
{
    // Implementations of unary methods of ReferenceValue.

    public String getType()
    {
        return ClassConstants.NAME_JAVA_LANG_OBJECT;
    }

    public Clazz getReferencedClass()
    {
        return null;
    }


    public boolean mayBeExtension()
    {
        return true;
    }


    public int isNull()
    {
        return MAYBE;
    }

    public int instanceOf(String otherType, Clazz otherReferencedClass)
    {
        return MAYBE;
    }

    public ReferenceValue cast(String type, Clazz referencedClass, ValueFactory valueFactory, boolean alwaysCast)
    {
        return valueFactory.createReferenceValue(type,
                                                 referencedClass,
                                                 true,
                                                 true);
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
//    // UnknownReferenceValue arguments.
//
//    public ReferenceValue generalize(UnknownReferenceValue other)
//    {
//        return other;
//    }
//
//
//    public int equal(UnknownReferenceValue other)
//    {
//        return MAYBE;
//    }
//
//
//    // Implementations of binary ReferenceValue methods with
//    // TypedReferenceValue arguments.
//
//    public ReferenceValue generalize(TypedReferenceValue other)
//    {
//    }
//
//
//    public int equal(TypedReferenceValue other)
//    {
//    }
//
//
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
//
//
//    // Implementations of binary ReferenceValue methods with
//    // ArrayReferenceValue arguments.
//
//    public ReferenceValue generalize(ArrayReferenceValue other)
//    {
//        return generalize((TypedReferenceValue)other);
//    }
//
//
//    public int equal(ArrayReferenceValue other)
//    {
//        return equal((TypedReferenceValue)other);
//    }
//
//
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
//
//
//    // Implementations of binary ReferenceValue methods with
//    // TracedReferenceValue arguments.
//
//    public ReferenceValue generalize(TracedReferenceValue other)
//    {
//        return other.generalize(this);
//    }
//
//
//    public int equal(TracedReferenceValue other)
//    {
//        return other.equal(this);
//    }


    // Implementations for Value.

    public boolean isParticular()
    {
        return false;
    }


    public final String internalType()
    {
        return ClassConstants.TYPE_JAVA_LANG_OBJECT;
    }


    // Implementations for Object.

    public String toString()
    {
        return "a";
    }
}