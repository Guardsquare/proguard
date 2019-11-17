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
 * This StringFunction tests whether the first given StringFunction
 * returns not null, returning the result of the latter function if so,
 * or null otherwise.
 *
 * @author Eric Lafortune
 */
public class AndStringFunction implements StringFunction
{
    private final StringFunction stringFunction1;
    private final StringFunction stringFunction2;


    /**
     * Creates a new AndStringFunction with the two given string functions.
     */
    public AndStringFunction(StringFunction stringFunction1,
                             StringFunction stringFunction2)
    {
        this.stringFunction1 = stringFunction1;
        this.stringFunction2 = stringFunction2;
    }


    // Implementations for StringFunction.

    @Override
    public String transform(String string)
    {
        return stringFunction1.transform(string) == null ? null :
               stringFunction2.transform(string);
    }
}
