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
package proguard.resources.file.io;

import proguard.io.*;
import proguard.resources.file.*;
import proguard.resources.file.visitor.ResourceFileVisitor;

import java.io.*;
import java.util.*;

/**
 * This DataEntryReader creates plain ResourceFile instances for the data
 * entries that it reads, and passes them to the given resource file visitor.
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
