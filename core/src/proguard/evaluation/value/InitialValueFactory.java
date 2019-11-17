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

import proguard.classfile.ClassConstants;

/**
 * This value factory creates initial values for fields and array elements,
 * with the help of a given value factory. Note that this class itself doesn't
 * implement ValueFactory.
 *
 * @author Eric Lafortune
 */
public class InitialValueFactory
{
    private final ValueFactory valueFactory;


    /**
     * Creates a new InitialValueFactory.
     * @param valueFactory the value factory that will actually create the
     *                     values.
     */
    public InitialValueFactory(ValueFactory valueFactory)
    {
        this.valueFactory = valueFactory;
    }


    /**
     * Creates an initial value (0, 0L, 0.0f, 0.0, null) of the given type.
     */
    public Value createValue(String type)
    {
        switch (type.charAt(0))
        {
            case ClassConstants.TYPE_BOOLEAN:
            case ClassConstants.TYPE_BYTE:
            case ClassConstants.TYPE_CHAR:
            case ClassConstants.TYPE_SHORT:
            case ClassConstants.TYPE_INT:
                return valueFactory.createIntegerValue(0);

            case ClassConstants.TYPE_LONG:
                return valueFactory.createLongValue(0L);

            case ClassConstants.TYPE_FLOAT:
                return valueFactory.createFloatValue(0.0f);

            case ClassConstants.TYPE_DOUBLE:
                return valueFactory.createDoubleValue(0.0);

            case ClassConstants.TYPE_CLASS_START:
            case ClassConstants.TYPE_ARRAY:
                return valueFactory.createReferenceValueNull();

            default:
                throw new IllegalArgumentException("Invalid type ["+type+"]");
        }
    }
}
