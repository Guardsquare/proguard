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
package proguard.resources.file.io;

import proguard.io.*;
import proguard.resources.file.*;
import proguard.resources.file.visitor.ResourceFileVisitor;

import java.io.*;
import java.util.*;

/**
 * This {@link DataEntryReader} creates plain {@link ResourceFile} instances for the data
 * entries that it reads, and passes them to the given {@link ResourceFileVisitor}.
 *
 * @author Eric Lafortune
 */
public class ResourceFileDataEntryReader implements DataEntryReader
{
    private final ResourceFileVisitor resourceFileVisitor;
    private final DataEntryFilter     adaptedDataEntryFilter;


    /**
     * Creates a new ResourceFileDataEntryReader
     */
    public ResourceFileDataEntryReader(ResourceFileVisitor resourceFileVisitor)
    {
        this(resourceFileVisitor, null);
    }


    /**
     * Creates a new ResourceFileDataEntryReader with the given filter that
     * accepts data entries for resource files that need to be adapted.
     */
    public ResourceFileDataEntryReader(ResourceFileVisitor resourceFileVisitor,
                                       DataEntryFilter     adaptedDataEntryFilter)
    {
        this.resourceFileVisitor    = resourceFileVisitor;
        this.adaptedDataEntryFilter = adaptedDataEntryFilter;
    }


    // Implementations for DataEntryReader.

    @Override
    public void read(DataEntry dataEntry) throws IOException
    {
        if (!dataEntry.isDirectory())
        {
            ResourceFile resourceFile = new ResourceFile(dataEntry.getName(), 0L);

            // Collect references to Java tokens, if specified.
            if (adaptedDataEntryFilter != null &&
                adaptedDataEntryFilter.accepts(dataEntry))
            {
                resourceFile.references = collectJavaReferences(dataEntry);
            }

            // Pass the resource file to the visitor.
            resourceFileVisitor.visitResourceFile(resourceFile);
        }
    }


    // Small utility methods.

    /**
     * Collects Java tokens in the contents of the given data entry. The tokens
     * are not linked to possible classes yet.
     */
    private Set<ResourceJavaReference> collectJavaReferences(DataEntry dataEntry)
    throws IOException
    {
        Set<ResourceJavaReference> set = new HashSet<>();

        Reader reader =
            new BufferedReader(
            new InputStreamReader(dataEntry.getInputStream()));

        try
        {
            DataEntryTokenizer tokenizer = new DataEntryTokenizer(reader);
            DataEntryToken     token;
            while ((token = tokenizer.nextToken()) != null)
            {
                if (token.type == DataEntryTokenType.JAVA_IDENTIFIER)
                {
                    set.add(new ResourceJavaReference(token.string));
                }
            }
        }
        finally
        {
            dataEntry.closeInputStream();
        }

        return set;
    }
}
