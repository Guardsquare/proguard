/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2013 Eric Lafortune (eric@graphics.cornell.edu)
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

import java.io.*;
import java.util.zip.*;

/**
 * This <code>DataEntry</code> represents a ZIP entry.
 *
 * @author Eric Lafortune
 */
public class ZipDataEntry implements DataEntry
{
    private final DataEntry      parent;
    private final ZipEntry       zipEntry;
    private       ZipInputStream zipInputStream;


    public ZipDataEntry(DataEntry      parent,
                        ZipEntry       zipEntry,
                        ZipInputStream zipInputStream)
    {
        this.parent         = parent;
        this.zipEntry       = zipEntry;
        this.zipInputStream = zipInputStream;
    }


    // Implementations for DataEntry.

    public String getName()
    {
        // Get the right separators.
        String name = zipEntry.getName()
            .replace(File.separatorChar, ClassConstants.INTERNAL_PACKAGE_SEPARATOR);

        // Chop the trailing directory slash, if any.
        int length = name.length();
        return length > 0 &&
               name.charAt(length-1) == ClassConstants.INTERNAL_PACKAGE_SEPARATOR ?
                   name.substring(0, length -1) :
                   name;
    }


    public boolean isDirectory()
    {
        return zipEntry.isDirectory();
    }


    public InputStream getInputStream() throws IOException
    {
        return zipInputStream;
    }


    public void closeInputStream() throws IOException
    {
        zipInputStream.closeEntry();
        zipInputStream = null;
    }


    public DataEntry getParent()
    {
        return parent;
    }


    // Implementations for Object.

    public String toString()
    {
        return parent.toString() + ':' + getName();
    }
}
