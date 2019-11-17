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
 * This StringFunction delegates a given String to one of two other StringFunctions, depending on
 * whether it matches the given filter.
 *
 * @author Tim Van Den Broecke
 */
public class FilteredStringFunction
implements StringFunction
{
    private final StringMatcher  nameFilter;
    private final StringFunction acceptedFunction;
    private final StringFunction rejectedFunction;


    public FilteredStringFunction(String         nameFilter,
                                  StringFunction acceptedFunction,
                                  StringFunction rejectedFunction)
    {
        this.nameFilter   = new ListParser(new NameParser()).parse(nameFilter);

        this.acceptedFunction = acceptedFunction;
        this.rejectedFunction = rejectedFunction;
    }


    // Implementations for StringFunction.

    @Override
    public String transform(String fileName)
    {
        if (nameFilter.matches(fileName))
        {
            return acceptedFunction.transform(fileName);
        }
        else
        {
            return rejectedFunction.transform(fileName);
        }
    }
}
