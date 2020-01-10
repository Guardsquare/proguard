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

import java.io.IOException;
import java.nio.charset.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * This {@link DataEntryReader} lets a given {@link DataEntryReader} read all data entries of
 * the read archive data entries.
 *
 * @author Eric Lafortune
 */
public class JarReader implements DataEntryReader
{
    private final boolean         jmod;
    private final DataEntryReader dataEntryReader;


    /**
     * Creates a new JarReader.
     * @param dataEntryReader the reader that can process the jar entries.
     */
    public JarReader(DataEntryReader dataEntryReader)
    {
        this(false, dataEntryReader);
    }


    /**
     * Creates a new JarReader that optionally reads jmod files.
     * @param jmod            specifies whether the input jar is actually a
     *                        jmod file.
     * @param dataEntryReader the reader that can process the jar entries.
     */
    public JarReader(boolean         jmod,
                     DataEntryReader dataEntryReader)
    {
        this.jmod            = jmod;
        this.dataEntryReader = dataEntryReader;
    }


    // Implementation for DataEntryReader.

    @Override
    public void read(DataEntry dataEntry) throws IOException
    {
        // Can we parse the jar entries more robustly from a file?
        if (dataEntry instanceof FileDataEntry)
        {
            // Read the data entry using its file.
            FileDataEntry fileDataEntry = (FileDataEntry)dataEntry;

            ZipFile zipFile = new ZipFile(fileDataEntry.getFile(), StandardCharsets.UTF_8);

            try
            {
                Enumeration entries = zipFile.entries();

                // Get all entries from the input jar.
                while (entries.hasMoreElements())
                {
                    ZipEntry zipEntry = (ZipEntry)entries.nextElement();

                    // Delegate the actual reading to the data entry reader.
                    dataEntryReader.read(new ZipFileDataEntry(dataEntry,
                                                              zipEntry,
                                                              zipFile));
                }
            }
            finally
            {
                zipFile.close();
            }
        }
        else
        {
            if (jmod)
            {
                // Eat the magic bytes
                dataEntry.getInputStream().read(new byte[4]);
            }

            // Read the data entry using its stream.
            ZipInputStream zipInputStream = new ZipInputStream(dataEntry.getInputStream(), StandardCharsets.UTF_8);

            try
            {
                // Get all entries from the input jar.
                while (true)
                {
                    // Can we get another entry?
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    if (zipEntry == null)
                    {
                        break;
                    }

                    // Delegate the actual reading to the data entry reader.
                    dataEntryReader.read(new ZipDataEntry(dataEntry,
                                                          zipEntry,
                                                          zipInputStream));
                }
            }
            finally
            {
                dataEntry.closeInputStream();
            }
        }
    }
}
