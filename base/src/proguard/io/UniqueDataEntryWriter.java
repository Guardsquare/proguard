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
 * This DataEntryWriter delegates to a given {@link DataEntryWriter}, but a directory or outputstream can be created
 * at most a single time.
 *
 * @author Johan Leys
 */
public class UniqueDataEntryWriter implements DataEntryWriter
{
    private final DataEntryWriter dataEntryWriter;

    private boolean written = false;

    public UniqueDataEntryWriter(DataEntryWriter dataEntryWriter)
    {
        this.dataEntryWriter = dataEntryWriter;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        if (!written)
        {
            try
            {
                return dataEntryWriter.createDirectory(dataEntry);
            }
            finally
            {
                written = true;
            }
        }
        return false;
    }


    @Override
    public boolean sameOutputStream(DataEntry dataEntry1, DataEntry dataEntry2) throws IOException
    {
        return dataEntryWriter.sameOutputStream(dataEntry1, dataEntry2);
    }


    @Override
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        if (!written)
        {
            try
            {
                return dataEntryWriter.createOutputStream(dataEntry);
            }
            finally
            {
                written = true;
            }
        }
        return null;
    }


    @Override
    public void close() throws IOException
    {
        dataEntryWriter.close();
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "UniqueDataEntryWriter");
        dataEntryWriter.println(pw, prefix + "  ");
    }
}
