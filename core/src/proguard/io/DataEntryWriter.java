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
 * This interface provides methods for writing data entries, such as ZIP entries
 * or files. The implementation determines to which type of data entry the
 * data will be written.
 *
 * @author Eric Lafortune
 */
public interface DataEntryWriter
{
    /**
     * Creates a directory.
     * @param dataEntry the data entry for which the directory is to be created.
     * @return whether the directory has been created.
     */
    public boolean createDirectory(DataEntry dataEntry) throws IOException;


    /**
     * Returns whether the two given data entries would result in the same
     * output stream.
     * @param dataEntry1 the first data entry.
     * @param dataEntry2 the second data entry.
     */
    public boolean sameOutputStream(DataEntry dataEntry1,
                                    DataEntry dataEntry2) throws IOException;


    /**
     * Creates a new output stream for writing data. The caller is responsible
     * for closing the stream.
     * @param dataEntry the data entry for which the output stream is to be
     *                  created.
     * @return the output stream. The stream may be <code>null</code> to
     *         indicate that the data entry should not be written.
     */
    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException;


    /**
     * Finishes writing all data entries.
     * <p>
     * Implementations typically create graphs of writers that can split and
     * merge again, possibly even with cycles.
     * <p>
     * For splits and merges, implementations need to be idempotent; once
     * closed, subsequent attempts to close a writer have no effect. If
     * needed, the wrapper {@link NonClosingDataEntryWriter} can avoid closing
     * a branch prematurely.
     * <p>
     * For cycles, implementations must perform any custom behavior, then
     * delegate {@link #close()} invocations, and only finally clean up. It is
     * possible that delegates call {@link #createOutputStream(DataEntry)}
     * while {@link #close()} is in progress.
     */
    public void close() throws IOException;


    /**
     * Prints out the structure of the data entry writer.
     * @param pw     the print stream to which the structure should be printed.
     * @param prefix a prefix for every printed line.
     */
    public void println(PrintWriter pw, String prefix);
}
