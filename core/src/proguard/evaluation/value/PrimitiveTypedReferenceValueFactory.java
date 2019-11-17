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
import proguard.classfile.util.ClassUtil;

/**
 * This class provides methods to create and reuse Value objects.
 * Its ReferenceValue objects have types if they represent primitive arrays.
 *
 * @author Eric Lafortune
 */
public class PrimitiveTypedReferenceValueFactory
extends      BasicValueFactory
{
    static final ReferenceValue REFERENCE_VALUE_NULL = new TypedReferenceValue(null, null, false, true);

    // Implementations for BasicValueFactory.


    public ReferenceValue createReferenceValueNull()
    {
        return REFERENCE_VALUE_NULL;
    }


    public ReferenceValue createReferenceValue(String type,
                                               Clazz referencedClass,
                                               boolean mayBeExtension,
                                               boolean mayBeNull)
    {
        return type == null ? REFERENCE_VALUE_NULL :
            !ClassUtil.isInternalArrayType(type) ||
             ClassUtil.isInternalClassType(type) ? REFERENCE_VALUE :
                                                   new TypedReferenceValue(type, referencedClass, mayBeExtension, mayBeNull);
    }


    public ReferenceValue createArrayReferenceValue(String       type,
                                                    Clazz        referencedClass,
                                                    IntegerValue arrayLength)
    {
        return type == null ?
            REFERENCE_VALUE_NULL :
            new ArrayReferenceValue(ClassConstants.TYPE_ARRAY + type,
                                    referencedClass,
                                    false,
                                    arrayLength);
    }
}