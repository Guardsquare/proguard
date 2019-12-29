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
 * This class provides methods to create and reuse Value objects.
 * Its ReferenceValue objects have types.
 *
 * @author Eric Lafortune
 */
public class TypedReferenceValueFactory
extends      BasicValueFactory
{
    static final ReferenceValue REFERENCE_VALUE_NULL                        = new TypedReferenceValue(null, null, false, true);
    static final ReferenceValue REFERENCE_VALUE_JAVA_LANG_OBJECT_MAYBE_NULL = new TypedReferenceValue(ClassConstants.NAME_JAVA_LANG_OBJECT, null, true, true);
    static final ReferenceValue REFERENCE_VALUE_JAVA_LANG_OBJECT_NOT_NULL   = new TypedReferenceValue(ClassConstants.NAME_JAVA_LANG_OBJECT, null, true, false);


    // Implementations for BasicValueFactory.

    public ReferenceValue createReferenceValueNull()
    {
        return REFERENCE_VALUE_NULL;
    }


    public ReferenceValue createReferenceValue(String  type,
                                               Clazz   referencedClass,
                                               boolean mayBeExtension,
                                               boolean mayBeNull)
    {
        return type == null                                       ? REFERENCE_VALUE_NULL                                                      :
               !type.equals(ClassConstants.NAME_JAVA_LANG_OBJECT) ||
               !mayBeExtension                                    ? new TypedReferenceValue(type, referencedClass, mayBeExtension, mayBeNull) :
               mayBeNull                                          ? REFERENCE_VALUE_JAVA_LANG_OBJECT_MAYBE_NULL                               :
                                                                    REFERENCE_VALUE_JAVA_LANG_OBJECT_NOT_NULL;
    }


    public ReferenceValue createArrayReferenceValue(String       type,
                                                    Clazz        referencedClass,
                                                    IntegerValue arrayLength)
    {
        return createArrayReferenceValue(type,
                                         referencedClass,
                                         arrayLength,
                                         createValue(type,
                                                     referencedClass,
                                                     true,
                                                     true));
    }


    public ReferenceValue createArrayReferenceValue(String       type,
                                                    Clazz        referencedClass,
                                                    IntegerValue arrayLength,
                                                    Value        elementValue)
    {
        return createReferenceValue(TypeConstants.ARRAY + type,
                                    referencedClass,
                                    false,
                                    false);
    }
}