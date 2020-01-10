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

import java.io.*;

/**
 * This {@link DataEntryWriter} writes data entries to a single given file.
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
