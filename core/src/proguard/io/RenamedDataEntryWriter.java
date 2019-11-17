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
package proguard.io;

import proguard.classfile.ClassConstants;
import proguard.util.*;

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
        String name    = dataEntry.getName() + ClassConstants.PACKAGE_SEPARATOR;
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
