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
 * This identified value factory creates array reference values that also
 * represent their elements, in as far as possible.
 *
 * @author Eric Lafortune
 */
public class DetailedArrayValueFactory
extends      IdentifiedValueFactory
{
    // Implementations for ReferenceValue.

    public ReferenceValue createArrayReferenceValue(String       type,
                                                    Clazz        referencedClass,
                                                    IntegerValue arrayLength)
    {
        return type == null ?
            TypedReferenceValueFactory.REFERENCE_VALUE_NULL :
            new DetailedArrayReferenceValue(TypeConstants.ARRAY + type,
                                            referencedClass,
                                            false,
                                            arrayLength,
                                            this,
                                            referenceID++);
    }
}