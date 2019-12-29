/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.io;

import proguard.classfile.TypeConstants;

import java.io.*;

/**
 * This DataEntryWriter writes data entries to individual files in a given
 * directory.
 *
 * @author Eric Lafortune
 */
public class DirectoryWriter implements DataEntryWriter
{
    private final File baseDirectory;


    /**
     * Creates a new DirectoryWriter.
     * @param baseDirectory the base directory to which all files will be written.
     */
    public DirectoryWriter(File baseDirectory)
    {
        this.baseDirectory = baseDirectory;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        File directory = getFile(dataEntry);
        if (!directory.exists() &&
            !directory.mkdirs())
        {
            throw new IOException("Can't create directory [" + directory.getPath() + "]");
        }

        return true;
    }


    @Override
    public boolean sameOutputStream(DataEntry dataEntry1,
                                    DataEntry dataEntry2) throws IOException
    {
        return getFile(dataEntry1).equals(getFile(dataEntry2));
    }


    @Override
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        File file = getFile(dataEntry);

        // Make sure the parent directories exist.
        File parentDirectory = file.getParentFile();
        if (parentDirectory != null   &&
            !parentDirectory.exists() &&
            !parentDirectory.mkdirs())
        {
            throw new IOException("Can't create directory [" + parentDirectory.getPath() + "]");
        }

        return
            new BufferedOutputStream(
            new FileOutputStream(file));
    }


    @Override
    public void close() throws IOException
    {
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "DirectoryWriter (base directory ["+baseDirectory+"])");
    }


    // Small utility methods.

    /**
     * Returns the file for the given data entry.
     */
    private File getFile(DataEntry dataEntry)
    {
        // Use the specified file, or construct a new file.
        return new File(baseDirectory,
                        dataEntry.getName().replace(TypeConstants.PACKAGE_SEPARATOR,
                                                    File.separatorChar));
    }
}
