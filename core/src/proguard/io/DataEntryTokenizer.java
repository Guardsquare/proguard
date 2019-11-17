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
