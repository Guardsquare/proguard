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
 * This {@link StringFunction} delegates a given String to one of two other {@link StringFunction}
 * instances, depending on whether it matches the given filter.
 *
 * @author Tim Van Den Broecke
 */
public class FilteredStringFunction
implements StringFunction
{
    private final StringMatcher  nameFilter;
    private final StringFunction acceptedFunction;
    private final StringFunction rejectedFunction;


    public FilteredStringFunction(String         nameFilter,
                                  StringFunction acceptedFunction,
                                  StringFunction rejectedFunction)
    {
        this.nameFilter   = new ListParser(new NameParser()).parse(nameFilter);

        this.acceptedFunction = acceptedFunction;
        this.rejectedFunction = rejectedFunction;
    }


    // Implementations for StringFunction.

    @Override
    public String transform(String fileName)
    {
        if (nameFilter.matches(fileName))
        {
            return acceptedFunction.transform(fileName);
        }
        else
        {
            return rejectedFunction.transform(fileName);
        }
    }
}
