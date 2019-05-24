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
 * This DataEntryWriter writes data entries to a single given file.
 *
 * @see DirectoryWriter
 * @author Eric Lafortune
 */
public class FixedFileWriter implements DataEntryWriter
{
    private final File         file;
    private       OutputStream outputStream;


    /**
     * Creates a new FixedFileWriter.
     * @param file the file to which all data entries will be written.
     */
    public FixedFileWriter(File file)
    {
        this.file = file;
    }


    // Implementations for DataEntryWriter.

    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        File directory = file;
        if (!directory.exists() &&
            !directory.mkdirs())
        {
            throw new IOException("Can't create directory [" + directory.getPath() + "]");
        }

        return true;
    }


    public boolean sameOutputStream(DataEntry dataEntry1,
                                    DataEntry dataEntry2)
    throws IOException
    {
        return true;
    }


    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        File file = this.file;

        // Make sure the parent directories exist.
        File parentDirectory = file.getParentFile();
        if (parentDirectory != null   &&
            !parentDirectory.exists() &&
            !parentDirectory.mkdirs())
        {
            throw new IOException("Can't create directory [" + parentDirectory.getPath() + "]");
        }

        outputStream =
            new BufferedOutputStream(
            new FileOutputStream(file));

        return outputStream;
    }


    public void close() throws IOException
    {
        if (outputStream != null)
        {
            outputStream.close();
            outputStream = null;
        }
    }


    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "FixedFileWriter (file [" + file + "])");
    }
}
