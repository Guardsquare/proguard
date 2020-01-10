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
 * This {@link DataEntryWriter} delegates to another {@link DataEntryWriter}, except for
 * any {@link #close()} calls.
 * <p/>
 * For example:
 * <pre>
 *     DataEntryWriter writer = ...
 *
 *     NonClosingDataEntryWriter nonClosingDataEntryWriter =
 *         new NonClosingDataEntryWriter(writer);
 *
 *     DataEntryWriter writer1 = new NonClosingDataEntryWriter(writer);
 *     DataEntryWriter writer2 = writer;
 *
 *     ...
 *
 *     writer1.close();
 *
 *     ...
 *
 *     writer2.close();
 * </pre>
 *
 * @author Eric Lafortune
 */
public class NonClosingDataEntryWriter implements DataEntryWriter
{
    private final DataEntryWriter dataEntryWriter;


    /**
     * Creates a new NonClosingDataEntryWriter that won't close its delegate.
     * @param dataEntryWriter the DataEntryWriter to which the writing will be
     *                        delegated.
     */
    public NonClosingDataEntryWriter(DataEntryWriter dataEntryWriter)
    {
        this.dataEntryWriter = dataEntryWriter;
    }


    // Implementations for DataEntryWriter.

    @Override
    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        return dataEntryWriter.createDirectory(dataEntry);
    }


    @Override
    public boolean sameOutputStream(DataEntry dataEntry1,
                                    DataEntry dataEntry2)
    throws IOException
    {
        return dataEntryWriter.sameOutputStream(dataEntry1, dataEntry2);
    }


    @Override
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        return dataEntryWriter.createOutputStream(dataEntry);
    }


    @Override
    public void close() throws IOException
    {
        // Don't close.
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "NonClosingDataEntryWriter");
        dataEntryWriter.println(pw, prefix + "  ");
    }
}
