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
package proguard.util;

/**
 * This {@link StringFunction} adds a prefix in front of each transformed String.
 *
 * @author Johan Leys
 */
public class PrefixAddingStringFunction implements StringFunction
{
    private final String         prefix;
    private final StringFunction delegateFunction;


    /**
     * Creates a new PrefixAddingStringFunction.
     *
     * @param prefix the prefix to add in front of each string.
     */
    public PrefixAddingStringFunction(String prefix)
    {
        this(prefix, null);
    }


    public PrefixAddingStringFunction(String prefix, StringFunction delegateFunction)
    {
        this.prefix = prefix;
        this.delegateFunction = delegateFunction;
    }


    // Implementations for StringFunction.

    @Override
    public String transform(String string)
    {
        if (delegateFunction != null)
        {
            string = delegateFunction.transform(string);
        }
        return prefix + string;
    }
}
