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
 * This class contains utility methods for strings.
 */
public class StringUtil
{
    private static final char TWO_BYTE_LIMIT     = 0x80;
    private static final int  TWO_BYTE_CONSTANT1 = 0xc0;
    private static final int  TWO_BYTE_CONSTANT2 = 0x80;
    private static final int  TWO_BYTE_SHIFT1    = 6;
    private static final int  TWO_BYTE_MASK1     = 0x1f;
    private static final int  TWO_BYTE_MASK2     = 0x3f;

    private static final char THREE_BYTE_LIMIT     = 0x800;
    private static final int  THREE_BYTE_CONSTANT1 = 0xe0;
    private static final int  THREE_BYTE_CONSTANT2 = 0x80;
    private static final int  THREE_BYTE_CONSTANT3 = 0x80;
    private static final int  THREE_BYTE_SHIFT1    = 12;
    private static final int  THREE_BYTE_SHIFT2    = 6;
    private static final int  THREE_BYTE_MASK1     = 0x0f;
    private static final int  THREE_BYTE_MASK2     = 0x3f;
    private static final int  THREE_BYTE_MASK3     = 0x3f;


    /**
     * Returns the length of the modified UTF-8 byte array representation of the given string.
     */
    public static int getModifiedUtf8Length(String string)
    {
        int byteLength   = 0;
        int stringLength = string.length();
        for (int stringIndex = 0; stringIndex < stringLength; stringIndex++)
        {
            char c = string.charAt(stringIndex);

            // The character is represented by one, two, or three bytes.
            byteLength += c == 0                ? 2 :
                          c <  TWO_BYTE_LIMIT   ? 1 :
                          c <  THREE_BYTE_LIMIT ? 2 :
                                                  3;
        }

        return byteLength;
    }


    /**
     * Returns the modified UTF-8 byte array representation of the given string.
     * <p>
     * Note: surrogate pairs are encoded separately as three byte values as requested
     * by the modified UTF-8 specification. This method is suited for encoding strings
     * stored in class files, dex files or native libraries.
     *
     * @link https://www.oracle.com/technetwork/articles/java/supplementary-142654.html
     * @link https://source.android.com/devices/tech/dalvik/dex-format#mutf-8
     */
    public static byte[] getModifiedUtf8Bytes(String string)
    {
        // We're computing the byte array ourselves, because the implementation
        // of String.getBytes("UTF-8") has a bug, at least up to JRE 1.4.2.
        // Also note the special treatment of the 0 character.

        int byteLength   = getModifiedUtf8Length(string);
        int stringLength = string.length();

        // Allocate the byte array with the computed length.
        byte[] bytes  = new byte[byteLength];

        // Fill out the array.
        int byteIndex = 0;
        for (int stringIndex = 0; stringIndex < stringLength; stringIndex++)
        {
            char c = string.charAt(stringIndex);
            if (c == 0)
            {
                // The 0 character gets a two-byte representation in classes.
                bytes[byteIndex++] = (byte)TWO_BYTE_CONSTANT1;
                bytes[byteIndex++] = (byte)TWO_BYTE_CONSTANT2;
            }
            else if (c < TWO_BYTE_LIMIT)
            {
                // The character is represented by a single byte.
                bytes[byteIndex++] = (byte)c;
            }
            else if (c < THREE_BYTE_LIMIT)
            {
                // The character is represented by two bytes.
                bytes[byteIndex++] = (byte)(TWO_BYTE_CONSTANT1 | ((c >>> TWO_BYTE_SHIFT1) & TWO_BYTE_MASK1));
                bytes[byteIndex++] = (byte)(TWO_BYTE_CONSTANT2 | ( c                      & TWO_BYTE_MASK2));
            }
            else
            {
                // The character is represented by three bytes.
                bytes[byteIndex++] = (byte)(THREE_BYTE_CONSTANT1 | ((c >>> THREE_BYTE_SHIFT1) & THREE_BYTE_MASK1));
                bytes[byteIndex++] = (byte)(THREE_BYTE_CONSTANT2 | ((c >>> THREE_BYTE_SHIFT2) & THREE_BYTE_MASK2));
                bytes[byteIndex++] = (byte)(THREE_BYTE_CONSTANT3 | ( c                        & THREE_BYTE_MASK3));
            }
        }

        return bytes;
    }


    /**
     * Returns the String representation of the given modified UTF-8 byte array.
     */
    public static String getString(byte[] modifiedUtf8Bytes)
    {
        return getString(modifiedUtf8Bytes, 0, modifiedUtf8Bytes.length);
    }


    /**
     * Returns the String representation of the given modified UTF-8 byte array.
     */
    public static String getString(byte[] modifiedUtf8Bytes,
                                   int    startIndex,
                                   int    endIndex)
    {
        // We're computing the string ourselves, because the implementation
        // of "new String(bytes)" doesn't honor the special treatment of
        // the 0 character in JRE 1.6_u11 and higher.

        StringBuilder builder = new StringBuilder(endIndex - startIndex);

        // Fill out the array.
        int byteIndex = startIndex;
        while (byteIndex < endIndex)
        {
            int b = modifiedUtf8Bytes[byteIndex++] & 0xff;

            // Depending on the flag bits in the first byte, the character
            // is represented by a single byte, by two bytes, or by three
            // bytes. We're not checking the redundant flag bits in the
            // second byte and the third byte.
            try
            {
                char c =
                    (char)(b < TWO_BYTE_CONSTANT1   ? b                                                          :

                           b < THREE_BYTE_CONSTANT1 ? ((b                              & TWO_BYTE_MASK1) << TWO_BYTE_SHIFT1) |
                                                      ((modifiedUtf8Bytes[byteIndex++] & TWO_BYTE_MASK2)                   ) :

                                                      ((b                              & THREE_BYTE_MASK1) << THREE_BYTE_SHIFT1) |
                                                      ((modifiedUtf8Bytes[byteIndex++] & THREE_BYTE_MASK2) << THREE_BYTE_SHIFT2) |
                                                      ((modifiedUtf8Bytes[byteIndex++] & THREE_BYTE_MASK3)                     ));
                builder.append(c);
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                throw new IllegalArgumentException("Missing UTF-8 bytes after byte [0x"+Integer.toHexString(b)+"] in string ["+builder.toString()+"]");
            }
        }

        return builder.toString();
    }


    /**
     * Joins the given strings using the provided separator.
     *
     * @param separator    The separator to use.
     * @param strings      The strings to join.
     * @return The input strings, concatenated together using the separator
     */
    public static String join(String separator, String... strings)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++)
        {
            sb.append(strings[i]);
            if (i + 1 < strings.length)
            {
                sb.append(separator);
            }
        }
        return sb.toString();
    }


    /**
     * Returns the hexadecimal representation of the given byte array.
     */
    public static String toHexString(byte[] bytes)
    {
        return toHexString(bytes, null, true);
    }


    /**
     * Returns the hexadecimal representation of the given byte array.
     */
    public static String toHexString(byte[]  bytes,
                                     String  separator,
                                     boolean upperCase)
    {
        if (bytes == null)
        {
            return null;
        }

        StringBuilder builder = new StringBuilder(2 * bytes.length +
                                                  (separator == null ? 0 : separator.length() * (bytes.length-1)));

        for (int index = 0; index < bytes.length; index++)
        {
            byte b = bytes[index];

            // Append the two nibbles of the byte.
            builder.append(hexNibble(b >> 4, upperCase))
                   .append(hexNibble(b,      upperCase));

            // Append the separator, if any.
            if (separator != null && index < bytes.length - 1)
            {
                builder.append(separator);
            }
        }

        return builder.toString();
    }


    /**
     * Returns the hexadecimal representation of the given nibble.
     */
    private static char hexNibble(int nibble, boolean upperCase)
    {
        nibble &= 0xf;
        return (char)(nibble < 10 ?
                          '0'                     + nibble :
                          (upperCase ? 'A' : 'a') + nibble - 10);
    }


    /**
     * Escapes control characters (\n, \r, \b, \t, \f).
     */
    public static String escapeControlCharacters(String input)
    {
        String result = input;

        result = result.replaceAll("\n", "\\\\n");
        result = result.replaceAll("\r", "\\\\r");
        result = result.replaceAll("\t", "\\\\t");
        result = result.replaceAll("\f", "\\\\f");
        result = result.replaceAll("\b", "\\\\b");

        return result;
    }
}
