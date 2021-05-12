/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
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

import java.nio.charset.StandardCharsets;

/**
 * Hashing functions.
 *
 * @author Johan Leys
 */
public class HashUtil
{
    // Constants for the FNV1-a hashCode algorithm.
    public static final int FNV_HASH_INIT  = 0x811c9dc5;
    public static final int FNV_HASH_PRIME = 0x01000193;


    /**
     * Convience method for computing the FNV-1a hash on the UTF-8 encoded byte representation of the given String.
     */
    public static int hashFnv1a32_UTF8(String string)
    {
        return hash(string, FNV_HASH_INIT);
    }


    /**
     * Convience method for computing the FNV-1a hash on the UTF-8 encoded byte representation of the given String,
     * using the given initial hash value.
     */
    public static int hash(String string, int init)
    {
        return hash(string.getBytes(StandardCharsets.UTF_8), init);
    }


    /**
     * FNV-1a hashing function.
     * <p>
     * https://en.wikipedia.org/wiki/Fowler%E2%80%93Noll%E2%80%93Vo_hash_function
     */
    public static int hashFnv1a32(byte[] data)
    {
        return hash(data, FNV_HASH_INIT);
    }


    /**
     * Computes a hash of the given bytes, using the FNV-1a hashing function, but with a custom inital hash value.
     */
    public static int hash(byte[] data, int init)
    {
        int hash   = init;
        for (byte b : data)
        {
            hash ^= b;
            hash *= FNV_HASH_PRIME;
        }

        return hash;
    }


    // This class should never be instantiated - it contains only static utility methods.
    private HashUtil() {}
}
