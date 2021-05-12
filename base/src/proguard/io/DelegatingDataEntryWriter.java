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
 * A simple delegating DataEntryWriter.
 */
public class DelegatingDataEntryWriter
implements   DataEntryWriter
{
    private DataEntryWriter delegate;

    public DelegatingDataEntryWriter()
    {
        this(null);
    }

    public DelegatingDataEntryWriter(DataEntryWriter delegate)
    {
        setDelegate(delegate);
    }

    public void setDelegate(DataEntryWriter delegate)
    {
        this.delegate = delegate;
    }


    // Implementations for DataEntryWriter.

    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        return delegate.createDirectory(dataEntry);
    }


    public boolean sameOutputStream(DataEntry dataEntry1, DataEntry dataEntry2) throws IOException
    {
        return delegate.sameOutputStream(dataEntry1, dataEntry2);
    }


    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        return delegate.createOutputStream(dataEntry);
    }


    public void close() throws IOException
    {
        delegate.close();
    }


    public void println(PrintWriter pw, String prefix)
    {
        delegate.println(pw, prefix);
    }
}
