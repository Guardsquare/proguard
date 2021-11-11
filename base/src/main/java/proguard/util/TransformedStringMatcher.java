/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
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
 * This StringMatcher delegates its tests to another given StringMatcher,
 * with strings that have been transformed with a given function.
 *
 * @author Eric Lafortune
 */
public class TransformedStringMatcher extends StringMatcher
{
    private final StringFunction stringFunction;
    private final StringMatcher  stringMatcher;


    /**
     * Creates a new TransformedStringMatcher.
     * @param stringFunction the function to transform strings.
     * @param stringMatcher  the string matcher to test the transformed
     *                       strings.
     */
    public TransformedStringMatcher(StringFunction stringFunction,
                                    StringMatcher  stringMatcher)
    {
        this.stringFunction = stringFunction;
        this.stringMatcher  = stringMatcher;
    }


    // Implementations for StringMatcher.

    @Override
    public boolean matches(String string)
    {
        return stringMatcher.matches(stringFunction.transform(string));
    }


    @Override
    protected boolean matches(String string, int beginOffset, int endOffset)
    {
        return stringMatcher.matches(stringFunction.transform(string.substring(beginOffset, endOffset)));
    }
}
