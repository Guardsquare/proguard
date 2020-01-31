/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
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

import java.io.*;
import java.util.*;

/**
 * This DataEntryWriter writes out all data entries to a delegate
 * DataEntryWriter, inserting additional data entries that are attached
 * to the written data entry. The output stream of the additional data
 * entry is not used.
 *
 * @author Eric Lafortune
 */
public class ExtraDataEntryWriter implements DataEntryWriter
{
    private final ExtraDataEntryNameMap extraEntryNameMap;
    private final DataEntryWriter       dataEntryWriter;
    private final DataEntryWriter       extraDataEntryWriter;

    private final Set<String> extraEntryNamesWritten = new HashSet<String>();


    /**
     * Creates a new ExtraDataEntryWriter that writes one given extra data entry
     * together with the first data entry that is written.
     *
     * @param extraEntryName  the name of the extra data entry.
     * @param dataEntryWriter the writer to which the entries are
     *                        written, including the extra data entry.
     */
    public ExtraDataEntryWriter(String          extraEntryName,
                                DataEntryWriter dataEntryWriter)
    {
        this(extraEntryName, dataEntryWriter, dataEntryWriter);
    }


    /**
     * Creates a new ExtraDataEntryWriter that writes one given extra data entry
     * together with the first data entry that is written.
     *
     * @param extraEntryName       the name of the extra data entry.
     * @param dataEntryWriter      the writer to which the entries are
     *                             written.
     * @param extraDataEntryWriter the writer to which the extra data entry
     *                             will be written.
     */
    public ExtraDataEntryWriter(String          extraEntryName,
                                DataEntryWriter dataEntryWriter,
                                DataEntryWriter extraDataEntryWriter)
    {
        this(new ExtraDataEntryNameMap(),
             dataEntryWriter,
             extraDataEntryWriter);

        extraEntryNameMap.addExtraDataEntry(extraEntryName);
    }

    /**
     * Creates a new ExtraDataEntryWriter.
     *
     * @param extraEntryNameMap    a map with data entry names and their
     *                             associated extra data entries. An extra
     *                             data entry that is associated with multiple
     *                             entries is only written once.
     * @param dataEntryWriter      the writer to which the entries are
     *                             written.
     * @param extraDataEntryWriter the writer to which the extra data entry
     *                             will be written.
     */
    public ExtraDataEntryWriter(ExtraDataEntryNameMap extraEntryNameMap,
                                DataEntryWriter       dataEntryWriter,
                                DataEntryWriter       extraDataEntryWriter)
    {
        this.extraEntryNameMap    = extraEntryNameMap;
        this.dataEntryWriter      = dataEntryWriter;
        this.extraDataEntryWriter = extraDataEntryWriter;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        return dataEntryWriter.createDirectory(dataEntry);
    }


    @Override
    public boolean sameOutputStream(DataEntry dataEntry1,
                                    DataEntry dataEntry2) throws IOException
    {
        return dataEntryWriter.sameOutputStream(dataEntry1, dataEntry2);
    }


    @Override
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        DataEntry parentEntry = dataEntry.getParent();

        // Write all default extra entries.
        writeExtraEntries(parentEntry, extraEntryNameMap.getDefaultExtraDataEntryNames());

        // Write all extra entries attached to the current data entry.
        writeExtraEntries(parentEntry, extraEntryNameMap.getExtraDataEntryNames(dataEntry.getName()));

        // Delegate to write out the actual entry.
        return dataEntryWriter.createOutputStream(dataEntry);
    }


    @Override
    public void close() throws IOException
    {
        dataEntryWriter.close();
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "ExtraDataEntryWriter");
        dataEntryWriter.println(pw, prefix + "  ");
    }


    // Small utility methods.

    private void writeExtraEntries(DataEntry   parentEntry,
                                   Set<String> extraEntryNames)
    throws IOException
    {
        if (extraEntryNames != null)
        {
            for (String extraEntryName : extraEntryNames)
            {
                if (!extraEntryNamesWritten.contains(extraEntryName))
                {
                    extraEntryNamesWritten.add(extraEntryName);

                    DataEntry extraEntry = new DummyDataEntry(parentEntry, extraEntryName, 0, false);
                    extraDataEntryWriter.createOutputStream(extraEntry);
                    writeExtraEntries(parentEntry, extraEntryNameMap.getExtraDataEntryNames(extraEntryName));
                }
            }
        }
    }
}
