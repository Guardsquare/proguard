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

import proguard.util.ArrayUtil;

import java.io.IOException;

/**
 * This DataEntryReader delegates to a given DataEntryReader, each time
 * stripping a possible prefix from the read data entry name.
 *
 * @author Eric Lafortune
 */
public class PrefixStrippingDataEntryReader implements DataEntryReader
{
    private final String          prefix;
    private final DataEntryReader dataEntryReader;


    /**
     * Creates a new PrefixStrippingDataEntryReader.
     */
    public PrefixStrippingDataEntryReader(String          prefix,
                                          DataEntryReader dataEntryReader)
    {
        this.prefix          = prefix;
        this.dataEntryReader = dataEntryReader;
    }


    // Implementation for DataEntryReader.

    @Override
    public void read(DataEntry dataEntry) throws IOException
    {
        // Strip the prefix if necessary.
        String name = dataEntry.getName();
        if (name.startsWith(prefix))
        {
            dataEntry = new RenamedDataEntry(dataEntry,
                                             name.substring(prefix.length()));
        }

        // Read the data entry.
        dataEntryReader.read(dataEntry);
    }
}
