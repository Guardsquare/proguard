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
 * This interface describes a data entry, for exanple a ZIP entry, a file, or
 * a directory.
 *
 * @author Eric Lafortune
 */
public interface DataEntry
{
    /**
     * Returns the name of this data entry.
     */
    public String getName();


    /**
     * Returns the original name of this data entry, i.e. the name of the
     * data entry before any renaming or obfuscation.
     */
    public String getOriginalName();


    /**
     * Returns the size of this data entry, in bytes, or -1 if unknown.
     */
    public long getSize();


    /**
     * Returns whether the data entry represents a directory.
     */
    public boolean isDirectory();


    /**
     * Returns an input stream for reading the content of this data entry.
     * The data entry may not represent a directory.
     */
    public InputStream getInputStream() throws IOException;


    /**
     * Closes the previously retrieved InputStream.
     */
    public void closeInputStream() throws IOException;


    /**
     * Returns the parent of this data entry, or <code>null</null> if it doesn't
     * have one.
     */
    public DataEntry getParent();
}
