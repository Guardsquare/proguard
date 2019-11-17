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
import proguard.util.StringMatcher;

import java.io.*;

/**
 * This DataEntryWriter sends data entries to the zip files specified by
 * their parents.
 *
 * @author Eric Lafortune
 */
public class ZipWriter implements DataEntryWriter
{
    private final StringMatcher   uncompressedFilter;
    private final int             uncompressedAlignment;
    private final StringMatcher   extraUncompressedAlignmentFilter;
    private final int             extraUncompressedAlignment;
    private final int             modificationTime;
    private final byte[]          header;
    private final DataEntryWriter dataEntryWriter;

    private DataEntry currentParentEntry;
    private ZipOutput currentZipOutput;


    /**
     * Creates a new ZipWriter.
     * @param uncompressedFilter    an optional filter for files that should not
     *                              be compressed.
     * @param uncompressedAlignment the desired alignment for the data of
     *                              uncompressed entries.
     * @param modificationTime      the modification date and time of the zip
     *                              entries, in DOS format.
     * @param dataEntryWriter       the data entry writer that can provide
     *                              output streams for the zip archives.
     */
    public ZipWriter(StringMatcher   uncompressedFilter,
                     int             uncompressedAlignment,
                     int             modificationTime,
                     DataEntryWriter dataEntryWriter)
    {
        this(uncompressedFilter,
             uncompressedAlignment,
             null,
             1,
             modificationTime,
             dataEntryWriter);
    }


    /**
     * Creates a new ZipWriter.
     * @param uncompressedFilter                an optional filter for files that should not
     *                                          be compressed.
     * @param uncompressedAlignment             the desired alignment for the data of
     *                                          uncompressed entries.
     * @param extraUncompressedAlignmentFilter  an optional filter for files that should not
     *                                          be compressed and use a different alignment.
     * @param extraUncompressedAlignment        the desired alignment for the data of
     *                                          entries matching extraAlignmentFilter.
     * @param modificationTime                  the modification date and time of the zip
     *                                          entries, in DOS format.
     * @param dataEntryWriter                   the data entry writer that can provide
     *                                          output streams for the zip archives.
     */
    public ZipWriter(StringMatcher   uncompressedFilter,
                     int             uncompressedAlignment,
                     StringMatcher   extraUncompressedAlignmentFilter,
                     int             extraUncompressedAlignment,
                     int             modificationTime,
                     DataEntryWriter dataEntryWriter)
    {
        this.uncompressedFilter               = uncompressedFilter;
        this.uncompressedAlignment            = uncompressedAlignment;
        this.extraUncompressedAlignmentFilter = extraUncompressedAlignmentFilter;
        this.extraUncompressedAlignment       = extraUncompressedAlignment;
        this.modificationTime                 = modificationTime;
        this.header                           = null;
        this.dataEntryWriter                  = dataEntryWriter;
    }


    /**
     * Creates a new ZipWriter.
     * @param uncompressedFilter    an optional filter for files that should not
     *                              be compressed.
     * @param uncompressedAlignment the desired alignment for the data of
     *                              uncompressed entries.
     * @param modificationTime      the modification date and time of the zip
     *                              entries, in DOS format.
     * @param dataEntryWriter       the data entry writer that can provide
     *                              output streams for the zip archives.
     */
    public ZipWriter(StringMatcher   uncompressedFilter,
                     int             uncompressedAlignment,
                     int             modificationTime,
                     byte[]          header,
                     DataEntryWriter dataEntryWriter)
    {
        this(uncompressedFilter,
             uncompressedAlignment,
             null,
             1,
             modificationTime,
             header,
             dataEntryWriter);
    }


    /**
     * Creates a new ZipWriter.
     * @param uncompressedFilter                an optional filter for files that should not
     *                                          be compressed.
     * @param uncompressedAlignment             the desired alignment for the data of
     *                                          uncompressed entries.
     * @param extraUncompressedAlignmentFilter  an optional filter for files that should not
     *                                          be compressed and use a different alignment.
     * @param extraUncompressedAlignment        the desired alignment for the data of
     *                                          entries matching extraAlignmentFilter.
     * @param modificationTime                  the modification date and time of the zip
     *                                          entries, in DOS format.
     * @param dataEntryWriter                   the data entry writer that can provide
     *                                          output streams for the zip archives.
     */
    public ZipWriter(StringMatcher   uncompressedFilter,
                     int             uncompressedAlignment,
                     StringMatcher   extraUncompressedAlignmentFilter,
                     int             extraUncompressedAlignment,
                     int             modificationTime,
                     byte[]          header,
                     DataEntryWriter dataEntryWriter)
    {
        this.uncompressedFilter               = uncompressedFilter;
        this.uncompressedAlignment            = uncompressedAlignment;
        this.extraUncompressedAlignmentFilter = extraUncompressedAlignmentFilter;
        this.extraUncompressedAlignment       = extraUncompressedAlignment;
        this.modificationTime                 = modificationTime;
        this.header                           = header;
        this.dataEntryWriter                  = dataEntryWriter;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        finishIfNecessary(dataEntry);
        setUp(dataEntry);

        // Did we get a zip output?
        if (currentZipOutput == null)
        {
            return false;
        }

        // Get the directory entry name.
        String name = dataEntry.getName() + ClassConstants.PACKAGE_SEPARATOR;

        // Create a new directory entry.
        OutputStream outputStream =
            currentZipOutput.createOutputStream(name,
                                                false,
                                                modificationTime);
        outputStream.close();

        return true;
    }


    @Override
    public boolean sameOutputStream(DataEntry dataEntry1,
                                    DataEntry dataEntry2)
    throws IOException
    {
        return dataEntry1 != null &&
               dataEntry2 != null &&
               dataEntry1.getName().equals(dataEntry2.getName()) &&
               dataEntryWriter.sameOutputStream(dataEntry1.getParent(),
                                                dataEntry2.getParent());
    }


    @Override
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        finishIfNecessary(dataEntry);
        setUp(dataEntry);

        // Did we get a zip output?
        if (currentZipOutput == null)
        {
            return null;
        }

        // Create a new zip entry.
        String name         = dataEntry.getName();
        String originalName = dataEntry.getOriginalName();

        boolean compress =
            (uncompressedFilter               == null || !uncompressedFilter.matches(originalName)) &&
            (extraUncompressedAlignmentFilter == null || !extraUncompressedAlignmentFilter.matches(originalName));

        return
            currentZipOutput.createOutputStream(name,
                                                compress,
                                                modificationTime);
    }


    @Override
    public void close() throws IOException
    {
        finish();

        // Close the delegate writer.
        dataEntryWriter.close();
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "ZipWriter (uncompressed filter = "+uncompressedFilter+", alignment = "+uncompressedAlignment+
                   ", extraAlignmentFilter = "+extraUncompressedAlignmentFilter+", extraAlignment = "+extraUncompressedAlignment+")");
        dataEntryWriter.println(pw, prefix + "  ");
    }


    // Small utility methods.

    /**
     * Sets up the zip output for the parent of the given entry.
     */
    private void setUp(DataEntry dataEntry) throws IOException
    {
        if (currentZipOutput == null)
        {
            // Create a new zip output.
            currentParentEntry = dataEntry.getParent();
            currentZipOutput   =
                createZipOutput(dataEntryWriter.createOutputStream(currentParentEntry),
                                header,
                                uncompressedAlignment,
                                extraUncompressedAlignmentFilter,
                                extraUncompressedAlignment,
                                null);
        }
    }


    /**
     * Creates a zip output with the given header and parameters.
     */
    protected ZipOutput createZipOutput(OutputStream  outputStream,
                                        byte[]        header,
                                        int           uncompressedAlignment,
                                        StringMatcher extraUncompressedAlignmentFilter,
                                        int           extraUncompressedAlignment,
                                        String        comment)
    throws IOException
    {
        return new ZipOutput(outputStream,
                             header,
                             uncompressedAlignment,
                             extraUncompressedAlignmentFilter,
                             extraUncompressedAlignment,
                             comment);
    }


    private void finishIfNecessary(DataEntry dataEntry) throws IOException
    {
        // Would the new data entry end up in a different zip?
        if (currentParentEntry != null &&
            !dataEntryWriter.sameOutputStream(currentParentEntry, dataEntry.getParent()))
        {
            finish();
        }
    }


    /**
     * Closes the zip output, if any.
     */
    private void finish() throws IOException
    {
        // Finish the zip output, if any.
        if (currentZipOutput != null)
        {
            // Close the zip output and its underlying output stream.
            currentZipOutput.close();

            currentParentEntry = null;
            currentZipOutput   = null;
        }
    }
}
