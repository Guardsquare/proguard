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

import proguard.classfile.*;

import java.io.*;
import java.nio.charset.Charset;

/**
 * This {@link DataEntryReader} writes the resource data entries that it reads to a
 * given {@link DataEntryWriter}, updating their contents based on the renamed classes
 * in the given {@link ClassPool}.
 *
 * @author Eric Lafortune
 */
public class DataEntryRewriter extends DataEntryCopier
{
    private final ClassPool classPool;
    private final Charset   charset;


    /**
     * Creates a new DataEntryRewriter.
     */
    public DataEntryRewriter(ClassPool       classPool,
                             Charset         charset,
                             DataEntryWriter dataEntryWriter)
    {
        super(dataEntryWriter);

        this.classPool = classPool;
        this.charset   = charset;
    }


    // Implementations for DataEntryCopier.

    @Override
    protected void copyData(InputStream  inputStream,
                            OutputStream outputStream)
    throws IOException
    {
        Reader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
        Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset));

        copyData(reader, writer);

        writer.flush();
    }


    /**
     * Copies all data that it can read from the given reader to the given
     * writer.
     */
    protected void copyData(Reader reader,
                            Writer writer)
    throws IOException
    {
        DataEntryTokenizer tokenizer = new DataEntryTokenizer(reader);
        DataEntryToken     token;

        // Read all tokens.
        while ((token = tokenizer.nextToken()) != null)
        {
            String word = token.string;

            // Adapt the word if it is a valid Java identifier.
            if (token.type == DataEntryTokenType.JAVA_IDENTIFIER)
            {
                word = adaptedWord(word);
            }

            // Write the word.
            writer.write(word);
        }
    }


    // Small utility methods.

    /**
     * Adapts the given word if possible, based on the renamed class names.
     */
    private String adaptedWord(String word)
    {
        boolean containsDots = word.indexOf('.') >= 0;

        // Replace dots by forward slashes.
        String className = containsDots ?
            word.replace('.', TypeConstants.PACKAGE_SEPARATOR) :
            word;

        // Find the class corresponding to the word.
        Clazz clazz = classPool.getClass(className);
        if (clazz != null)
        {
            // Update the word if necessary.
            String newClassName = clazz.getName();
            if (!className.equals(newClassName))
            {
                // Replace forward slashes by dots.
                word = containsDots ?
                    newClassName.replace(TypeConstants.PACKAGE_SEPARATOR, '.') :
                    newClassName;
            }
        }

        return word;
    }
}
