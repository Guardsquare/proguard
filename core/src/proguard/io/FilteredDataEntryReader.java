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

import java.io.IOException;


/**
 * This DataEntryReader delegates to one of two other DataEntryReader instances,
 * depending on whether the data entry passes through a given data entry filter
 * or not.
 *
 * @author Eric Lafortune
 */
public class FilteredDataEntryReader implements DataEntryReader
{
    private final DataEntryFilter dataEntryFilter;
    private final DataEntryReader acceptedDataEntryReader;
    private final DataEntryReader rejectedDataEntryReader;


    /**
     * Creates a new FilteredDataEntryReader with only a reader for accepted
     * data entries.
     * @param dataEntryFilter         the data entry filter.
     * @param acceptedDataEntryReader the DataEntryReader to which the reading
     *                                will be delegated if the filter accepts
     *                                the data entry. May be <code>null</code>.
     */
    public FilteredDataEntryReader(DataEntryFilter dataEntryFilter,
                                   DataEntryReader acceptedDataEntryReader)
    {
        this(dataEntryFilter, acceptedDataEntryReader, null);
    }


    /**
     * Creates a new FilteredDataEntryReader.
     * @param dataEntryFilter         the data entry filter.
     * @param acceptedDataEntryReader the DataEntryReader to which the reading
     *                                will be delegated if the filter accepts
     *                                the data entry. May be <code>null</code>.
     * @param rejectedDataEntryReader the DataEntryReader to which the reading
     *                                will be delegated if the filter does not
     *                                accept the data entry. May be
     *                                <code>null</code>.
     */
    public FilteredDataEntryReader(DataEntryFilter dataEntryFilter,
                                   DataEntryReader acceptedDataEntryReader,
                                   DataEntryReader rejectedDataEntryReader)
    {
        this.dataEntryFilter         = dataEntryFilter;
        this.acceptedDataEntryReader = acceptedDataEntryReader;
        this.rejectedDataEntryReader = rejectedDataEntryReader;
    }


    // Implementations for DataEntryReader.

    @Override
    public void read(DataEntry dataEntry)
    throws IOException
    {
        DataEntryReader dataEntryReader = dataEntryFilter.accepts(dataEntry) ?
            acceptedDataEntryReader :
            rejectedDataEntryReader;

        if (dataEntryReader != null)
        {
            dataEntryReader.read(dataEntry);
        }
    }
}
