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

import java.util.Set;

/**
 * This matcher tests whether strings match with a String in a given Set.
 *
 * @author Johan Leys
 */
public class CollectionMatcher extends StringMatcher
{
    private final Set<String> set;


    public CollectionMatcher(Set<String> set)
    {
        this.set = set;
    }


    // Implementations for StringMatcher.

    @Override
    public boolean matches(String string)
    {
        return set.contains(string);
    }


    @Override
    protected boolean matches(String string, int beginOffset, int endOffset)
    {
        return set.contains(string.substring(beginOffset, endOffset));
    }
}
