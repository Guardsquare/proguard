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
 * This <code>DataEntry</code> represents a named output entry that doesn't
 * return an input stream. It is mostly useful for testing data entry writers,
 * explicitly creating and using output streams.
 *
 * @author Eric Lafortune
 */
public class DummyDataEntry implements DataEntry
{
    private final DataEntry parent;
    private final String    name;
    private final long      size;
    private final boolean   isDirectory;


    /**
     * Creates a new NamedDataEntry with the given name, parent, and size.
     */
    public DummyDataEntry(DataEntry parent,
                          String    name,
                          long      size,
                          boolean   isDirectory)
    {
        this.parent      = parent;
        this.name        = name;
        this.size        = size;
        this.isDirectory = isDirectory;
    }


    // Implementations for DataEntry.

    @Override
    public String getName()
    {
        return name;
    }


    @Override
    public String getOriginalName()
    {
        return getName();
    }


    @Override
    public long getSize()
    {
        return size;
    }


    @Override
    public boolean isDirectory()
    {
        return isDirectory;
    }


    @Override
    public InputStream getInputStream() throws IOException
    {
        throw new UnsupportedOperationException("Can't retrieve input stream for output entry ["+name+"]");
    }


    @Override
    public void closeInputStream() throws IOException
    {
    }


    @Override
    public DataEntry getParent()
    {
        return parent;
    }


    // Implementations for Object.

    @Override
    public String toString()
    {
        return String.valueOf(parent) + ':' + name;
    }
}
