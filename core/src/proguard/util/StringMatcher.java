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
 * This abstract class provides methods to determine whether strings match a
 * given criterion, which is specified by the implementation.
 *
 * @author Eric Lafortune
 */
public abstract class StringMatcher
{
    /**
     * Checks whether the given string matches.
     * @param string the string to match.
     * @return a boolean indicating whether the string matches the criterion.
     */
    public boolean matches(String string)
    {
        return matches(string, 0, string.length());
    }


    /**
     * Checks whether the given substring matches.
     * @param string the string to match.
     * @param beginOffset the start offset of the substring (inclusive).
     * @param endOffset the end offset of the substring (exclusive).
     * @return a boolean indicating whether the substring matches the criterion.
     */
    protected abstract boolean matches(String string,
                                       int    beginOffset,
                                       int    endOffset);
}
