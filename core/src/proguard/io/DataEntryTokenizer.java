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
package proguard.io;

import java.io.*;

/**
 * This class breaks up the character data read from a given Reader into
 * {@link DataEntryToken}s. It distinguishes character sequences that can be
 * interpreted as valid Java identifiers from other character sequences.
 *
 * @author Lars Vandenbergh
 */
public class DataEntryTokenizer
{
    private Reader       reader;
    private StringBuffer buffer;


    public DataEntryTokenizer(Reader reader)
    {
        this.reader = reader;
        this.buffer = new StringBuffer();
    }


    public DataEntryToken nextToken() throws IOException
    {
        StringBuffer tokenString = new StringBuffer(buffer.toString());
        buffer.setLength(0);

        while (true)
        {
            int i = reader.read();
            if (i < 0)
            {
                break;
            }

            // Is it part if the current token or the start of a new token?
            char c = (char)i;
            if(tokenString.length() == 0 ||
               isJavaIdentifierChar(c) ==
               isJavaIdentifierChar(tokenString.charAt(0)))
            {
                tokenString.append(c);
            }
            else
            {
                buffer.append(c);
                break;
            }
        }

        if (tokenString.length() == 0)
        {
            return null;
        }
        else
        {
            DataEntryTokenType tokenType =
                isJavaIdentifierChar(tokenString.charAt(0)) ?
                    DataEntryTokenType.JAVA_IDENTIFIER :
                    DataEntryTokenType.OTHER;

            return new DataEntryToken(tokenString.toString(),
                                      tokenType);
        }
    }


    private boolean isJavaIdentifierChar(char c)
    {
        return Character.isJavaIdentifierPart(c) ||
               c == '.' ||
               c == '-';
    }
}
