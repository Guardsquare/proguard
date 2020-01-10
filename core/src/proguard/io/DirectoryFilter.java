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

/**
 * This {@link DataEntryReader} delegates to one of two other {@link DataEntryReader} instances,
 * depending on whether the data entry represents a directory or not.
 *
 * @author Eric Lafortune
 */
public class DirectoryFilter extends FilteredDataEntryReader
{
    /**
     * Creates a new ClassFilter that delegates reading directories to the
     * given reader.
     */
    public DirectoryFilter(DataEntryReader directoryReader)
    {
        this (directoryReader, null);
    }


    /**
     * Creates a new ClassFilter that delegates to either of the two given
     * readers.
     */
    public DirectoryFilter(DataEntryReader directoryReader,
                           DataEntryReader otherReader)
    {
        super(new DataEntryDirectoryFilter(),
              directoryReader,
              otherReader);
    }
}