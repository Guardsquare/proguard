/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 Eric Lafortune (eric@graphics.cornell.edu)
 */
package proguard.io;

import java.io.IOException;
import java.util.*;

/**
 * This DataEntryReader delegates to another DataEntryReader, inserting
 * additional data entries that are attached to the read data entry.
 *
 * @author Eric Lafortune
 */
public class ExtraDataEntryReader implements DataEntryReader
{
    private final ExtraDataEntryNameMap extraEntryNameMap;
    private final DataEntryReader       dataEntryReader;
    private final DataEntryReader       extraDataEntryReader;

    private final Set<String> extraEntryNamesRead = new HashSet<String>();


    /**
     * Creates a new ExtraDataEntryReader that reads one given extra data entry
     * together with the first data entry that is read.
     *
     * @param extraEntryName  the name of the extra data entry.
     * @param dataEntryReader the reader from which the entries are
     *                        read, including the extra data entry.
     */
    public ExtraDataEntryReader(String          extraEntryName,
                                DataEntryReader dataEntryReader)
    {
        this(extraEntryName, dataEntryReader, dataEntryReader);
    }


    /**
     * Creates a new ExtraDataEntryReader that reads one given extra data entry
     * together with the first data entry that is read.
     *
     * @param extraEntryName       the name of the extra data entry.
     * @param dataEntryReader      the reader from which the entries are
     *                             read.
     * @param extraDataEntryReader the reader from which the extra data entry
     *                             will be read.
     */
    public ExtraDataEntryReader(String          extraEntryName,
                                DataEntryReader dataEntryReader,
                                DataEntryReader extraDataEntryReader)
    {
        this(new ExtraDataEntryNameMap(),
             dataEntryReader,
             extraDataEntryReader);
        extraEntryNameMap.addExtraDataEntry(extraEntryName);
    }

    /**
     * Creates a new ExtraDataEntryReader.
     *
     * @param extraEntryNameMap    a map with data entry names and their
     *                             associated extra data entries. An extra
     *                             data entry that is associated with multiple
     *                             entries is only read once.
     * @param dataEntryReader      the reader from which the entries are
     *                             read.
     */
    public ExtraDataEntryReader(ExtraDataEntryNameMap extraEntryNameMap,
                                DataEntryReader       dataEntryReader)
    {
        this(extraEntryNameMap, dataEntryReader, dataEntryReader);
    }


    /**
     * Creates a new ExtraDataEntryReader.
     *
     * @param extraEntryNameMap    a map with data entry names and their
     *                             associated extra data entries. An extra
     *                             data entry that is associated with multiple
     *                             entries is only read once.
     * @param dataEntryReader      the reader from which the entries are
     *                             read.
     * @param extraDataEntryReader the reader from which the extra data entry
     *                             will be read.
     */
    public ExtraDataEntryReader(ExtraDataEntryNameMap extraEntryNameMap,
                                DataEntryReader       dataEntryReader,
                                DataEntryReader       extraDataEntryReader)
    {
        this.extraEntryNameMap    = extraEntryNameMap;
        this.dataEntryReader      = dataEntryReader;
        this.extraDataEntryReader = extraDataEntryReader;
    }


    // Implementations for DataEntryReader.

    @Override
    public void read(DataEntry dataEntry) throws IOException
    {
        DataEntry parentEntry = dataEntry.getParent();

        // Read all default extra entries.
        readExtraEntries(parentEntry, extraEntryNameMap.getDefaultExtraDataEntryNames());

        // Read all extra entries attached to the current data entry.
        readExtraEntries(parentEntry, extraEntryNameMap.getExtraDataEntryNames(dataEntry.getName()));

        // Read the actual entry.
        dataEntryReader.read(dataEntry);
    }


    // Small utility methods.

    private void readExtraEntries(DataEntry   parentEntry,
                                  Set<String> extraEntryNames) throws IOException
    {
        if (extraEntryNames != null)
        {
            for (String extraEntryName : extraEntryNames)
            {
                // Haven't we read the extra entry yet?
                if (extraEntryNamesRead.add(extraEntryName))
                {
                    // Create a content-less extra entry.
                    DataEntry extraEntry =
                        new DummyDataEntry(parentEntry, extraEntryName, 0, false);

                    // Read it. The reader is supposed to handle it properly.
                    extraDataEntryReader.read(extraEntry);

                    // Recursively read extra entries attached to this extra
                    // entry.
                    readExtraEntries(parentEntry, extraEntryNameMap.getExtraDataEntryNames(extraEntryName));
                }
            }
        }
    }
}