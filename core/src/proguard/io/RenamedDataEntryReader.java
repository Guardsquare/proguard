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
import proguard.util.StringFunction;

import java.io.IOException;
import java.util.Map;

/**
 * This DataEntryReader delegates to another DataEntryReader, renaming the
 * data entries based on the given string function. Entries whose name are
 * transformed to null or an empty string are passed to an optional
 * alternative DataEntryReader.
 *
 * @author Eric Lafortune
 */
public class RenamedDataEntryReader implements DataEntryReader
{
    private final StringFunction  nameFunction;
    private final DataEntryReader dataEntryReader;
    private final DataEntryReader missingDataEntryReader;


    /**
     * Creates a new RenamedDataEntryReader.
     * @param nameFunction    the function from old names to new names.
     * @param dataEntryReader the DataEntryReader to which renamed data
     *                        entries will be passed.
     */
    public RenamedDataEntryReader(StringFunction  nameFunction,
                                  DataEntryReader dataEntryReader)
    {
        this(nameFunction, dataEntryReader, null);
    }


    /**
     * Creates a new RenamedDataEntryReader.
     * @param nameFunction           the function from old names to new names.
     * @param dataEntryReader        the DataEntryReader to which renamed data
     *                               entries will be passed.
     * @param missingDataEntryReader the optional DataEntryReader to which data
     *                               entries that can't be renamed will be
     *                               passed.
     */
    public RenamedDataEntryReader(StringFunction  nameFunction,
                                  DataEntryReader dataEntryReader,
                                  DataEntryReader missingDataEntryReader)
    {
        this.nameFunction           = nameFunction;
        this.dataEntryReader        = dataEntryReader;
        this.missingDataEntryReader = missingDataEntryReader;
    }


    // Implementations for DataEntryReader.

    @Override
    public void read(DataEntry dataEntry) throws IOException
    {
        String name = dataEntry.getName();

        // Add a directory separator if necessary.
        if (dataEntry.isDirectory() &&
            name.length() > 0)
        {
            name += TypeConstants.PACKAGE_SEPARATOR;
        }

        String newName = nameFunction.transform(name);
        if (newName != null &&
            newName.length() > 0)
        {
            // Remove the directory separator if necessary.
            if (dataEntry.isDirectory())
            {
                newName = newName.substring(0, newName.length() -  1);
            }

            dataEntryReader.read(new RenamedDataEntry(dataEntry, newName));
        }
        else if (missingDataEntryReader != null)
        {
            missingDataEntryReader.read(dataEntry);
        }
    }
}
