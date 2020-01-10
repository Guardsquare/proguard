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
 * This class can read a given file or directory, recursively, applying a given
 * {@link DataEntryReader} to all files it comes across.
 *
 * @author Eric Lafortune
 */
public class DirectoryPump implements DataEntryPump
{
    private final File directory;


    public DirectoryPump(File directory)
    {
        this.directory = directory;
    }


    // Implementations for DataEntryPump.

    @Override
    public void pumpDataEntries(DataEntryReader dataEntryReader)
    throws IOException
    {
        if (!directory.exists())
        {
            throw new IOException("No such file or directory: " + directory);
        }

        readFiles(directory, dataEntryReader);
    }


    /**
     * Reads the given subdirectory recursively, applying the given DataEntryReader
     * to all files that are encountered.
     */
    private void readFiles(File file, DataEntryReader dataEntryReader)
    throws IOException
    {
        // Pass the file data entry to the reader.
        dataEntryReader.read(new FileDataEntry(directory, file));

        if (file.isDirectory())
        {
            // Recurse into the subdirectory.
            File[] listedFiles = file.listFiles();

            for (int index = 0; index < listedFiles.length; index++)
            {
                File listedFile = listedFiles[index];
                try
                {
                    readFiles(listedFile, dataEntryReader);
                }
                catch (IOException e)
                {
                    throw new IOException("Can't read ["+listedFile.getName()+"] ("+e.getMessage()+")", e);
                }
            }
        }
    }
}
