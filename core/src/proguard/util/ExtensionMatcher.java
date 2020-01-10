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
 * This {@link StringMatcher} tests whether strings end in a given extension, ignoring
 * its case.
 *
 * @author Eric Lafortune
 */
public class ExtensionMatcher extends StringMatcher
{
    private final String extension;


    /**
     * Creates a new StringMatcher.
     *
     * @param extension the extension against which strings will be matched.
     */
    public ExtensionMatcher(String extension)
    {
        this.extension = extension;
    }


    // Implementations for StringMatcher.

    @Override
    protected boolean matches(String string, int beginOffset, int endOffset)
    {
        return endsWithIgnoreCase(string, beginOffset, endOffset, extension);
    }


    /**
     * Returns whether the given string ends with the given suffix, ignoring its
     * case.
     */
    private static boolean endsWithIgnoreCase(String string,
                                              int    beginOffset,
                                              int    endOffset,
                                              String suffix)
    {
        int suffixLength = suffix.length();

        return string.regionMatches(true, endOffset - suffixLength, suffix, 0, suffixLength);
    }
}
