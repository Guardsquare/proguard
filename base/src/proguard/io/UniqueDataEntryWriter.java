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
import java.util.HashSet;
import java.util.Set;

/**
 * This DataEntryWriter delegates to a given {@link DataEntryWriter}, but a {@link DataEntry} can be written
 * at most one time (uniqueness is based on {@link DataEntry#getName()}.
 *
 * @author Johan Leys
 * @author James Hamilton
 */
public class UniqueDataEntryWriter implements DataEntryWriter
{
    private final DataEntryWriter dataEntryWriter;
    private final Set<String>     written = new HashSet<>();


    public UniqueDataEntryWriter(DataEntryWriter dataEntryWriter)
    {
        this.dataEntryWriter = dataEntryWriter;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        if (!written.contains(dataEntry.getName()))
        {
            try
            {
                return dataEntryWriter.createDirectory(dataEntry);
            }
            finally
            {
                written.add(dataEntry.getName());
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
        if (!written.contains(dataEntry.getName()))
        {
            try
            {
                return dataEntryWriter.createOutputStream(dataEntry);
            }
            finally
            {
                written.add(dataEntry.getName());
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
