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
 * This DataEntryWriter delegates to one of two other DataEntryWriter instances,
 * depending on whether the data entry passes through a given data entry filter
 * or not.
 *
 * @author Eric Lafortune
 */
public class FilteredDataEntryWriter implements DataEntryWriter
{
    private final DataEntryFilter dataEntryFilter;
    private DataEntryWriter acceptedDataEntryWriter;
    private DataEntryWriter rejectedDataEntryWriter;


    /**
     * Creates a new FilteredDataEntryWriter with only a writer for accepted
     * data entries.
     * @param dataEntryFilter         the data entry filter.
     * @param acceptedDataEntryWriter the DataEntryWriter to which the writing
     *                                will be delegated if the filter accepts
     *                                the data entry. May be <code>null</code>.
     */
    public FilteredDataEntryWriter(DataEntryFilter dataEntryFilter,
                                   DataEntryWriter acceptedDataEntryWriter)
    {
        this(dataEntryFilter, acceptedDataEntryWriter, null);
    }


    /**
     * Creates a new FilteredDataEntryWriter.
     * @param dataEntryFilter         the data entry filter.
     * @param acceptedDataEntryWriter the DataEntryWriter to which the writing
     *                                will be delegated if the filter accepts
     *                                the data entry. May be <code>null</code>.
     * @param rejectedDataEntryWriter the DataEntryWriter to which the writing
     *                                will be delegated if the filter does not
     *                                accept the data entry. May be
     *                                <code>null</code>.
     */
    public FilteredDataEntryWriter(DataEntryFilter dataEntryFilter,
                                   DataEntryWriter acceptedDataEntryWriter,
                                   DataEntryWriter rejectedDataEntryWriter)
    {
        this.dataEntryFilter         = dataEntryFilter;
        this.acceptedDataEntryWriter = acceptedDataEntryWriter;
        this.rejectedDataEntryWriter = rejectedDataEntryWriter;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        // Get the right data entry writer.
        DataEntryWriter dataEntryWriter = dataEntryFilter.accepts(dataEntry) ?
            acceptedDataEntryWriter :
            rejectedDataEntryWriter;

        // Delegate to it, if it's not null.
        return dataEntryWriter != null &&
               dataEntryWriter.createDirectory(dataEntry);
    }


    @Override
    public boolean sameOutputStream(DataEntry dataEntry1,
                                    DataEntry dataEntry2)
    throws IOException
    {
        boolean accepts1 = dataEntryFilter.accepts(dataEntry1);
        boolean accepts2 = dataEntryFilter.accepts(dataEntry2);
        return
            accepts1 ? !accepts2 || acceptedDataEntryWriter == null || acceptedDataEntryWriter.sameOutputStream(dataEntry1, dataEntry2) :
                       accepts2  || rejectedDataEntryWriter == null || rejectedDataEntryWriter.sameOutputStream(dataEntry1, dataEntry2);
    }


    @Override
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        // Get the right data entry writer.
        DataEntryWriter dataEntryWriter = dataEntryFilter.accepts(dataEntry) ?
            acceptedDataEntryWriter :
            rejectedDataEntryWriter;

        // Delegate to it, if it's not null.
        return dataEntryWriter != null ?
            dataEntryWriter.createOutputStream(dataEntry) :
            null;
    }


    @Override
    public void close() throws IOException
    {
        if (acceptedDataEntryWriter != null)
        {
            acceptedDataEntryWriter.close();
            acceptedDataEntryWriter = null;
        }

        if (rejectedDataEntryWriter != null)
        {
            rejectedDataEntryWriter.close();
            rejectedDataEntryWriter = null;
        }
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "FilteredDataEntryWriter (filter = "+dataEntryFilter+")");
        if (acceptedDataEntryWriter != null)
        {
            acceptedDataEntryWriter.println(pw, prefix + "  ");
        }
        if (rejectedDataEntryWriter != null)
        {
            rejectedDataEntryWriter.println(pw, prefix + "  ");
        }
    }
}
