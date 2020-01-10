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
 * This {@link DataEntry} wraps another data entry.
 *
 * @author Thomas Neidhart
 */
public class WrappedDataEntry implements DataEntry
{
    protected final DataEntry wrappedEntry;


    public WrappedDataEntry(DataEntry wrappedEntry)
    {
        this.wrappedEntry = wrappedEntry;
    }


    @Override
    public void closeInputStream() throws IOException
    {
        wrappedEntry.closeInputStream();
    }


    @Override
    public String getName()
    {
        return wrappedEntry.getName();
    }


    @Override
    public String getOriginalName()
    {
        return wrappedEntry.getOriginalName();
    }


    @Override
    public long getSize()
    {
        return wrappedEntry.getSize();
    }


    @Override
    public boolean isDirectory()
    {
        return wrappedEntry.isDirectory();
    }


    @Override
    public InputStream getInputStream() throws IOException
    {
        return wrappedEntry.getInputStream();
    }


    @Override
    public DataEntry getParent()
    {
        return wrappedEntry.getParent();
    }


    @Override
    public String toString()
    {
        return getName();
    }

}
