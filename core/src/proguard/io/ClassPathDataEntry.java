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

import static proguard.classfile.ClassConstants.CLASS_FILE_EXTENSION;
import static proguard.classfile.util.ClassUtil.internalClassName;

/**
 * DataEntry implementation which loads an input stream from the classpath of
 * the running VM.
 *
 * @author Johan Leys
 */
public class ClassPathDataEntry implements DataEntry
{
    private final String name;

    private InputStream inputStream;


    /**
     * Creas an new ClassPathDataEntry for the given class.
     *
     * @param clazz the class for which to create a data entry.
     */
    public ClassPathDataEntry(Class clazz)
    {
        this(internalClassName(clazz.getName()) + CLASS_FILE_EXTENSION);
    }


    /**
     * Creates a new ClassPathDataEntry for the entry with the given name.
     *
     * @param name the name of the class for which to create a data entry.
     */
    public ClassPathDataEntry(String name)
    {
        this.name = name;
    }


    // Implementations for DataEntry.

    @Override
    public String getName()
    {
        return name;
    }


    @Override
    public String getOriginalName()
    {
        return name;
    }


    @Override
    public long getSize()
    {
        return -1;
    }


    @Override
    public boolean isDirectory()
    {
        return false;
    }


    @Override
    public InputStream getInputStream() throws IOException
    {
        if (inputStream == null)
        {
            inputStream = getClass().getClassLoader().getResourceAsStream(name);
        }
        return inputStream;
    }


    @Override
    public void closeInputStream() throws IOException
    {
        inputStream.close();
        inputStream = null;
    }


    @Override
    public DataEntry getParent()
    {
        return null;
    }


    // Implementations for Object.

    @Override
    public String toString()
    {
        return getName();
    }
}
