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

import java.util.Map;

/**
 * This StringFunction gets its transformed strings from a given map.
 *
 * @author Eric Lafortune
 */
public class MapStringFunction implements StringFunction
{
    private final Map    map;
    private final String defaultString;


    /**
     * Creates a new MapStringFunction based on the given map.
     */
    public MapStringFunction(Map map)
    {
        this(map, null);
    }


    /**
     * Creates a new MapStringFunction based on the given map,
     * with a default string for strings that are not in the map.
     */
    public MapStringFunction(Map map, String defaultString)
    {
        this.map           = map;
        this.defaultString = defaultString;
    }


    // Implementations for StringFunction.

    public String transform(String string)
    {
        String mappedString = (String)map.get(string);

        return mappedString != null ? mappedString : defaultString;
    }
}
