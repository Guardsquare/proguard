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
 * This StringFunction returns the original string if the given StringFunction
 * returns null, or null otherwise.
 *
 * @author Eric Lafortune
 */
public class NotStringFunction implements StringFunction
{
    private final StringFunction stringFunction;


    /**
     * Creates a new NotStringFunction with the two given string functions.
     */
    public NotStringFunction(StringFunction stringFunction)
    {
        this.stringFunction = stringFunction;
    }


    // Implementations for StringFunction.

    @Override
    public String transform(String string)
    {
        return stringFunction.transform(string) == null ? string : null;
    }
}
