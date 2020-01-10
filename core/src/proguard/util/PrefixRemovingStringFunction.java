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
 * This {@link StringFunction} removes a given prefix from each transformed String, if present.
 *
 * @author Johan Leys
 */
public class PrefixRemovingStringFunction implements StringFunction
{
    private final String         prefix;
    private final StringFunction successStringFunction;
    private final StringFunction failStringFunction;


    /**
     * Creates a new PrefixRemovingStringFunction.
     *
     * @param prefix the prefix to remove from each string.
     */
    public PrefixRemovingStringFunction(String prefix)
    {
        this(prefix,
             StringFunction.IDENTITY_FUNCTION);
    }


    /**
     * Creates a new PrefixRemovingStringFunction.
     *
     * @param prefix         the prefix to remove from each string.
     * @param stringFunction the function to subsequently apply to all strings.
     */
    public PrefixRemovingStringFunction(String         prefix,
                                        StringFunction stringFunction)
    {
        this(prefix,
             stringFunction,
             stringFunction);
    }


    /**
     * Creates a new PrefixRemovingStringFunction.
     *
     * @param prefix                the prefix to remove from each string.
     * @param successStringFunction the function to subsequently apply to
     *                              prefixed strings.
     * @param failStringFunction    the function to subsequently apply to
     *                              other strings.
     */
    public PrefixRemovingStringFunction(String         prefix,
                                        StringFunction successStringFunction,
                                        StringFunction failStringFunction)
    {
        this.prefix                = prefix;
        this.successStringFunction = successStringFunction;
        this.failStringFunction    = failStringFunction;
    }

    // Implementations for StringFunction.

    public String transform(String string)
    {
        return string.startsWith(prefix) ?
            successStringFunction.transform(string.substring(prefix.length())) :
            failStringFunction.transform(string);
    }
}