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
 * Utility class for Base64 encoding.
 */
public class Base64Util
{
    private static final char[] BASE64 = new char[]
    {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '+', '/'
    };


    /**
     * Encodes the given array of bytes as a Base64 string.
     */
    public static String encode(byte[] bytes)
    {
        int len = bytes.length;

        StringBuffer buffer = new StringBuffer((len + 3) * 4 / 3);

        for (int index = 0; index < len; )
        {
            // Combine 3 bytes into 4 characters.
            char c4 = '=';
            char c3 = '=';

            // First byte.
            int triplet  = (bytes[index++] & 0xff) << 16;
            if (index < len)
            {
                // Second byte, if any.
                triplet |= (bytes[index++] & 0xff) << 8;
                if (index < len)
                {
                    // Third byte, if any.
                    triplet |= bytes[index++] & 0xff;

                    // Three valid bytes => no filler characters.
                    c4 = BASE64[triplet & 0x3f];
                }

                c3 = BASE64[(triplet >>> 6) & 0x3f];
            }

            char c2 = BASE64[(triplet >>> 2 * 6) & 0x3f];
            char c1 = BASE64[(triplet >>> 3 * 6) & 0x3f];

            buffer.append(c1)
                  .append(c2)
                  .append(c3)
                  .append(c4);
        }

        return buffer.toString();
    }
}
