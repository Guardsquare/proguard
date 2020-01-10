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
 * This {@link DataEntryWriter} delegates to a given {@link DataEntryWriter}, or failing that,
 * to another given {@link DataEntryWriter}.
 *
 * @author Eric Lafortune
 */
public class CascadingDataEntryWriter implements DataEntryWriter
{
    private DataEntryWriter dataEntryWriter1;
    private DataEntryWriter dataEntryWriter2;


    /**
     * Creates a new CascadingDataEntryWriter.
     * @param dataEntryWriter1 the DataEntryWriter to which the writing will be
     *                         delegated first.
     * @param dataEntryWriter2 the DataEntryWriter to which the writing will be
     *                         delegated, if the first one can't provide an
     *                         output stream.
     */
    public CascadingDataEntryWriter(DataEntryWriter dataEntryWriter1,
                                    DataEntryWriter dataEntryWriter2)
    {
        this.dataEntryWriter1 = dataEntryWriter1;
        this.dataEntryWriter2 = dataEntryWriter2;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        // Try to create a directory with the first data entry writer, or
        // otherwise with the second data entry writer.
        return dataEntryWriter1.createDirectory(dataEntry) ||
               dataEntryWriter2.createDirectory(dataEntry);
    }


    @Override
    public boolean sameOutputStream(DataEntry dataEntry1,
                                    DataEntry dataEntry2)
    throws IOException
    {
        return dataEntryWriter1.sameOutputStream(dataEntry1, dataEntry2) ||
               dataEntryWriter2.sameOutputStream(dataEntry1, dataEntry2);
    }


    @Override
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        // Try to get an output stream from the first data entry writer.
        OutputStream outputStream =
            dataEntryWriter1.createOutputStream(dataEntry);

        // Return it, if it's not null. Otherwise try to get an output stream
        // from the second data entry writer.
        return outputStream != null ?
            outputStream :
            dataEntryWriter2.createOutputStream(dataEntry);
    }


    @Override
    public void close() throws IOException
    {
        if (dataEntryWriter1 != null)
        {
            dataEntryWriter1.close();
            dataEntryWriter1 = null;
        }

        if (dataEntryWriter2 != null)
        {
            dataEntryWriter2.close();
            dataEntryWriter2 = null;
        }
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "CascadingDataEntryWriter");
        dataEntryWriter1.println(pw, prefix + "  ");
        dataEntryWriter2.println(pw, prefix + "  ");
    }
}
