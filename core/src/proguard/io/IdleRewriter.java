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

/**
 * This {@link DataEntryReader} reads data entries and requests their corresponding
 * output streams from a given {@link DataEntryWriter}, without actually using the
 * output stream. This approach correctly accounts for any filtering of the
 * classes to output archives.
 *
 * @author Eric Lafortune
 */
public class IdleRewriter implements DataEntryReader
{
    private final DataEntryWriter dataEntryWriter;


    public IdleRewriter(DataEntryWriter dataEntryWriter)
    {
        this.dataEntryWriter = dataEntryWriter;
    }


    // Implementations for DataEntryReader.

    @Override
    public void read(DataEntry dataEntry) throws IOException
    {
        // Get the output entry corresponding to this input entry, but don't
        // even try to close it.
        dataEntryWriter.createOutputStream(dataEntry);
    }
}
