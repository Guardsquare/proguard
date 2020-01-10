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

import proguard.util.StringMatcher;

import java.io.*;

/**
 * This {@link DataEntryWriter} delegates to another {@link DataEntryWriter}, renaming
 * parent data entries based on the given matcher.
 *
 * @author Thomas Neidhart
 */
public class RenamedParentDataEntryWriter implements DataEntryWriter
{
    private final StringMatcher   matcher;
    private final String          newParentName;
    private final DataEntryWriter dataEntryWriter;


    /**
     * Creates a new RenamedParentDataEntryWriter.
     *
     * @param matcher                the string matcher to match parent entries.
     * @param newParentName          the new parent name to use.
     * @param dataEntryWriter        the DataEntryWriter to which the writing will
     *                               be delegated.
     */
    public RenamedParentDataEntryWriter(StringMatcher   matcher,
                                        String          newParentName,
                                        DataEntryWriter dataEntryWriter)
    {
        this.matcher         = matcher;
        this.newParentName   = newParentName;
        this.dataEntryWriter = dataEntryWriter;
    }


    // Implementations for DataEntryWriter.

    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        return dataEntryWriter.createDirectory(getRedirectedEntry(dataEntry));
    }


    public boolean sameOutputStream(DataEntry dataEntry1, DataEntry dataEntry2)
        throws IOException
    {
        return dataEntryWriter.sameOutputStream(getRedirectedEntry(dataEntry1),
                                                getRedirectedEntry(dataEntry2));
    }


    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        return dataEntryWriter.createOutputStream(getRedirectedEntry(dataEntry));
    }


    public void close() throws IOException
    {
        dataEntryWriter.close();
    }


    public void println(PrintWriter pw, String prefix)
    {
        dataEntryWriter.println(pw, prefix);
    }

    private DataEntry getRedirectedEntry(DataEntry dataEntry)
    {
        if (dataEntry == null)
        {
            return null;
        }

        final DataEntry parentEntry = dataEntry.getParent();
        if (parentEntry != null &&
            matcher.matches(parentEntry.getName()))
        {
            final DataEntry renamedParentEntry =
                new RenamedDataEntry(parentEntry, newParentName);

            return new WrappedDataEntry(dataEntry) {
                public DataEntry getParent()
                {
                    return renamedParentEntry;
                }
            };
        }

        return dataEntry;
    }

}
