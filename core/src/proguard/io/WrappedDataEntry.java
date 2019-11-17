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

import java.io.*;

/**
 * This DataEntry wraps another data entry.
 *
 * @author Thomas Neidhart
 */
public class WrappedDataEntry implements DataEntry
{
    protected final DataEntry wrappedEntry;


    public WrappedDataEntry(DataEntry wrappedEntry)
    {
        this.wrappedEntry = wrappedEntry;
    }


    @Override
    public void closeInputStream() throws IOException
    {
        wrappedEntry.closeInputStream();
    }


    @Override
    public String getName()
    {
        return wrappedEntry.getName();
    }


    @Override
    public String getOriginalName()
    {
        return wrappedEntry.getOriginalName();
    }


    @Override
    public long getSize()
    {
        return wrappedEntry.getSize();
    }


    @Override
    public boolean isDirectory()
    {
        return wrappedEntry.isDirectory();
    }


    @Override
    public InputStream getInputStream() throws IOException
    {
        return wrappedEntry.getInputStream();
    }


    @Override
    public DataEntry getParent()
    {
        return wrappedEntry.getParent();
    }


    @Override
    public String toString()
    {
        return getName();
    }

}
