/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 Eric Lafortune (eric@graphics.cornell.edu)
 */
package proguard.io;

import java.io.*;

/**
 * This DataEntryWriter delegates to another DataEntryWriter, except for
 * any {@link #close()} calls.
 *
 * For example:
 * <pre>
 *     DataEntryWriter writer = ...
 *
 *     NonClosingDataEntryWriter nonClosingDataEntryWriter =
 *         new NonClosingDataEntryWriter(writer);
 *
 *     DataEntryWriter writer1 = new NonClosingDataEntryWriter(writer);
 *     DataEntryWriter writer2 = writer;
 *
 *     ...
 *
 *     writer1.close();
 *
 *     ...
 *
 *     writer2.close();
 * </pre>
 *
 * @author Eric Lafortune
 */
public class NonClosingDataEntryWriter implements DataEntryWriter
{
    private final DataEntryWriter dataEntryWriter;


    /**
     * Creates a new NonClosingDataEntryWriter that won't close its delegate.
     * @param dataEntryWriter the DataEntryWriter to which the writing will be
     *                        delegated.
     */
    public NonClosingDataEntryWriter(DataEntryWriter dataEntryWriter)
    {
        this.dataEntryWriter = dataEntryWriter;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        return dataEntryWriter.createDirectory(dataEntry);
    }


    @Override
    public boolean sameOutputStream(DataEntry dataEntry1,
                                    DataEntry dataEntry2)
    throws IOException
    {
        return dataEntryWriter.sameOutputStream(dataEntry1, dataEntry2);
    }


    @Override
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        return dataEntryWriter.createOutputStream(dataEntry);
    }


    @Override
    public void close() throws IOException
    {
        // Don't close.
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "NonClosingDataEntryWriter");
        dataEntryWriter.println(pw, prefix + "  ");
    }
}
