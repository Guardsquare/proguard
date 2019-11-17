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
 * This StringMatcher tests whether strings start with a given fixed string
 * and then match another optional given StringMatcher.
 *
 * @author Eric Lafortune
 */
public class FixedStringMatcher extends StringMatcher
{
    private final String        fixedString;
    private final StringMatcher nextMatcher;


    /**
     * Creates a new FixedStringMatcher.
     *
     * @param fixedString the string to match.
     */
    public FixedStringMatcher(String fixedString)
    {
        this(fixedString, null);
    }


    /**
     * Creates a new FixedStringMatcher.
     *
     * @param fixedString the string prefix to match.
     * @param nextMatcher an optional string matcher to match the remainder of
     *                    the string.
     */
    public FixedStringMatcher(String fixedString, StringMatcher nextMatcher)
    {
        this.fixedString = fixedString;
        this.nextMatcher = nextMatcher;
    }


    // Implementations for StringMatcher.

    @Override
    protected boolean matches(String string, int beginOffset, int endOffset)
    {
        int stringLength      = endOffset - beginOffset;
        int fixedStringLength = fixedString.length();
        return stringLength >= fixedStringLength &&
               string.startsWith(fixedString, beginOffset) &&
               ((nextMatcher == null && stringLength == fixedStringLength) ||
                nextMatcher.matches(string,
                                    beginOffset + fixedStringLength,
                                    endOffset));
    }
}
