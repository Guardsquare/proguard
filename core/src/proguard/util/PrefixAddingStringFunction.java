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
 * This StringFunction adds a prefix in front of each transformed String.
 *
 * @author Johan Leys
 */
public class PrefixAddingStringFunction implements StringFunction
{
    private final String         prefix;
    private final StringFunction delegateFunction;


    /**
     * Creates a new PrefixAddingStringFunction.
     *
     * @param prefix the prefix to add in front of each string.
     */
    public PrefixAddingStringFunction(String prefix)
    {
        this(prefix, null);
    }


    public PrefixAddingStringFunction(String prefix, StringFunction delegateFunction)
    {
        this.prefix = prefix;
        this.delegateFunction = delegateFunction;
    }


    // Implementations for StringFunction.

    @Override
    public String transform(String string)
    {
        if (delegateFunction != null)
        {
            string = delegateFunction.transform(string);
        }
        return prefix + string;
    }
}
