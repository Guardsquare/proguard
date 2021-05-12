/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * This DataEntryWriter delegates to another DataEntryWriter, after having
 * printed out a message and the data entry name.
 *
 * @author Eric Lafortune
 */
public class DebugDataEntryWriter implements DataEntryWriter
{
    private final String          message;
    private final DataEntryWriter dataEntryWriter;


    /**
     * Creates a new DebugDataEntryWriter with the given message,
     * delegating to the given other writer.
     */
    public DebugDataEntryWriter(String message, DataEntryWriter dataEntryWriter)
    {
        this.message         = message;
        this.dataEntryWriter = dataEntryWriter;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        System.err.println(message + " directory ["+dataEntry+"]");

        return dataEntryWriter.createDirectory(dataEntry);
    }

    @Override
    public boolean sameOutputStream(DataEntry dataEntry1, DataEntry dataEntry2)
    throws IOException
    {
        return dataEntryWriter.sameOutputStream(dataEntry1, dataEntry2);
    }

    @Override
    public OutputStream createOutputStream(DataEntry dataEntry)
    throws IOException
    {
        System.err.println(message + " ["+dataEntry+"]");

        return dataEntryWriter.createOutputStream(dataEntry);
    }

    @Override
    public void close() throws IOException
    {
        System.err.println(message + " (closing)");

        dataEntryWriter.close();
    }

    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "DebugDataEntryWriter");
        dataEntryWriter.println(pw, prefix);
    }
}