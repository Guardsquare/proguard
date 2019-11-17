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
 * This StringMatcher tests whether strings match a given list of StringMatcher
 * instances. The instances are considered sequentially. Each instance in the
 * list can optionally be negated, meaning that a match makes the entire
 * remaining match fail.
 *
 * @author Eric Lafortune
 */
public class ListMatcher extends StringMatcher
{
    private final StringMatcher[] matchers;
    private final boolean[]       negate;


    public ListMatcher(StringMatcher... matchers)
    {
        this(matchers, null);
    }


    public ListMatcher(StringMatcher[] matchers, boolean[] negate)
    {
        this.matchers = matchers;
        this.negate   = negate;
    }


    // Implementations for StringMatcher.

    @Override
    protected boolean matches(String string, int beginOffset, int endOffset)
    {
        // Check the list of matchers.
        for (int index = 0; index < matchers.length; index++)
        {
            StringMatcher matcher = matchers[index];
            if (matcher.matches(string, beginOffset, endOffset))
            {
                return negate == null ||
                       !negate[index];
            }
        }

        return negate != null &&
               negate[negate.length - 1];
    }
}
