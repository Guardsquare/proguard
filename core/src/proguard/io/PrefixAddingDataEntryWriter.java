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
 * This {@link DataEntryWriter} delegates to a given {@link DataEntryWriter}, each time
 * adding a prefix of the written data entry name.
 *
 * @author Eric Lafortune
 */
public class PrefixAddingDataEntryWriter implements DataEntryWriter
{
    private final String          prefix;
    private final DataEntryWriter dataEntryWriter;


    /**
     * Creates a new PrefixAddingDataEntryWriter.
     */
    public PrefixAddingDataEntryWriter(String          prefix,
                                       DataEntryWriter dataEntryWriter)
    {
        this.prefix          = prefix;
        this.dataEntryWriter = dataEntryWriter;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry)
    throws IOException
    {
        return dataEntryWriter.createDirectory(renamedDataEntry(dataEntry));
    }


    @Override
    public boolean sameOutputStream(DataEntry dataEntry1,
                                    DataEntry dataEntry2)
    throws IOException
    {
        return dataEntryWriter.sameOutputStream(renamedDataEntry(dataEntry1),
                                                renamedDataEntry(dataEntry2));
    }


    @Override
    public OutputStream createOutputStream(DataEntry dataEntry)
    throws IOException
    {
        return dataEntryWriter.createOutputStream(renamedDataEntry(dataEntry));
    }


    @Override
    public void close() throws IOException
    {
        dataEntryWriter.close();
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "PrefixAddingDataEntryWriter (prefix = "+prefix+")");
        dataEntryWriter.println(pw, prefix + "  ");
    }


    // Small utility methods.

    /**
     * Adds the prefix to the given data entry name.
     */
    private DataEntry renamedDataEntry(DataEntry dataEntry)
    {
        return new RenamedDataEntry(dataEntry, prefix + dataEntry.getName());
    }
}
