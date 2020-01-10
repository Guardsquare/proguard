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
 * This {@link StringMatcher} matches any string or no string at all.
 *
 * @author Eric Lafortune
 */
public class ConstantMatcher extends StringMatcher
{
    private boolean matches;


    /**
     * Creates a new ConstantMatcher that always returns the given result.
     */
    public ConstantMatcher(boolean matches)
    {
        this.matches = matches;
    }


    // Implementations for StringMatcher.

    @Override
    protected boolean matches(String string, int beginOffset, int endOffset)
    {
        return matches;
    }
}