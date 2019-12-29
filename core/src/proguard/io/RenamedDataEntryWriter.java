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

import proguard.classfile.TypeConstants;
import proguard.util.StringFunction;

import java.io.*;

/**
 * This DataEntryWriter delegates to another DataEntryWriter, renaming the
 * data entries with the given string function.
 *
 * @author Eric Lafortune
 */
public class RenamedDataEntryWriter implements DataEntryWriter
{
    private final StringFunction  nameFunction;
    private final DataEntryWriter dataEntryWriter;


    /**
     * Creates a new RenamedDataEntryWriter.
     * @param nameFunction    the function from old names to new names.
     * @param dataEntryWriter the DataEntryWriter to which renamed data
     *                        entries will be passed.
     */
    public RenamedDataEntryWriter(StringFunction  nameFunction,
                                  DataEntryWriter dataEntryWriter)
    {
        this.nameFunction    = nameFunction;
        this.dataEntryWriter = dataEntryWriter;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        // Add the directory separator.
        String name    = dataEntry.getName() + TypeConstants.PACKAGE_SEPARATOR;
        String newName = nameFunction.transform(name);

        boolean result = newName != null && newName.length() > 0;
        if (result)
        {
            // Remove the directory separator again.
            dataEntryWriter.createDirectory(new RenamedDataEntry(dataEntry, newName.substring(0, newName.length() - 1)));
        }

        return result;
    }


    @Override
    public boolean sameOutputStream(DataEntry dataEntry1, DataEntry dataEntry2) throws IOException
    {
        DataEntry renamedDataEntry1 = rename(dataEntry1);
        DataEntry renamedDataEntry2 = rename(dataEntry2);

        return renamedDataEntry1 != null &&
               renamedDataEntry2 != null &&
               dataEntryWriter.sameOutputStream(renamedDataEntry1, renamedDataEntry2);
    }


    @Override
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        DataEntry renamedDataEntry = rename(dataEntry);

        return renamedDataEntry == null ? null : dataEntryWriter.createOutputStream(renamedDataEntry);
    }


    @Override
    public void close() throws IOException
    {
        dataEntryWriter.close();
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "RenamedDataEntryWriter");
        dataEntryWriter.println(pw, prefix);
    }


    // Small utility methods.

    /**
     * Returns the suitably renamed data entry, or null.
     */
    private DataEntry rename(DataEntry dataEntry)
    {
        String name    = dataEntry.getName();
        String newName = nameFunction.transform(name);

        return newName == null ? null : new RenamedDataEntry(dataEntry, newName);
    }
}
