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
 * This StringFunction removes a given prefix from each transformed String, if present.
 *
 * @author Johan Leys
 */
public class PrefixRemovingStringFunction implements StringFunction
{
    private final String         prefix;
    private final StringFunction successStringFunction;
    private final StringFunction failStringFunction;


    /**
     * Creates a new PrefixRemovingStringFunction.
     *
     * @param prefix the prefix to remove from each string.
     */
    public PrefixRemovingStringFunction(String prefix)
    {
        this(prefix,
             StringFunction.IDENTITY_FUNCTION);
    }


    /**
     * Creates a new PrefixRemovingStringFunction.
     *
     * @param prefix         the prefix to remove from each string.
     * @param stringFunction the function to subsequently apply to all strings.
     */
    public PrefixRemovingStringFunction(String         prefix,
                                        StringFunction stringFunction)
    {
        this(prefix,
             stringFunction,
             stringFunction);
    }


    /**
     * Creates a new PrefixRemovingStringFunction.
     *
     * @param prefix                the prefix to remove from each string.
     * @param successStringFunction the function to subsequently apply to
     *                              prefixed strings.
     * @param failStringFunction    the function to subsequently apply to
     *                              other strings.
     */
    public PrefixRemovingStringFunction(String         prefix,
                                        StringFunction successStringFunction,
                                        StringFunction failStringFunction)
    {
        this.prefix                = prefix;
        this.successStringFunction = successStringFunction;
        this.failStringFunction    = failStringFunction;
    }

    // Implementations for StringFunction.

    public String transform(String string)
    {
        return string.startsWith(prefix) ?
            successStringFunction.transform(string.substring(prefix.length())) :
            failStringFunction.transform(string);
    }
}