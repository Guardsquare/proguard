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

import proguard.classfile.*;

import java.io.*;
import java.nio.charset.Charset;

/**
 * This DataEntryReader writes the resource data entries that it reads to a
 * given DataEntryWriter, updating their contents based on the renamed classes
 * in the given ClassPool.
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
            word.replace('.', ClassConstants.PACKAGE_SEPARATOR) :
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
                    newClassName.replace(ClassConstants.PACKAGE_SEPARATOR, '.') :
                    newClassName;
            }
        }

        return word;
    }
}
