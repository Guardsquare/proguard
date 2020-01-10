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
 * This {@link StringFunction} tests whether strings match either given {@link StringFunction}
 * instances, returning the first non-null result.
 *
 * @author Eric Lafortune
 */
public class OrStringFunction implements StringFunction
{
    private final StringFunction stringFunction1;
    private final StringFunction stringFunction2;


    /**
     * Creates a new AndStringFunction with the two given string functions.
     */
    public OrStringFunction(StringFunction stringFunction1,
                            StringFunction stringFunction2)
    {
        this.stringFunction1 = stringFunction1;
        this.stringFunction2 = stringFunction2;
    }


    // Implementations for StringFunction.

    @Override
    public String transform(String string)
    {
        String stringFunction1transform = stringFunction1.transform(string);
        return stringFunction1transform != null ?
            stringFunction1transform :
            stringFunction2.transform(string);
    }
}
