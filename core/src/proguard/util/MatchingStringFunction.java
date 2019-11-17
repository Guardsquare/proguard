/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.util;

/**
 * This StringFunction returns unchanged strings or null, depending on whether
 * a given string matcher matches the strings. It can be seen as an adapter
 * from StringMatcher or StringFunction.
 *
 * @author Eric Lafortune
 */
public class MatchingStringFunction implements StringFunction
{
    private final StringMatcher stringMatcher;


    /**
     * Creates a new MatchingStringFunction with the given string matcher.
     */
    public MatchingStringFunction(StringMatcher stringMatcher)
    {
        this.stringMatcher = stringMatcher;
    }


    // Implementations for StringFunction.

    @Override
    public String transform(String string)
    {
        return stringMatcher.matches(string) ? string : null;
    }
}
