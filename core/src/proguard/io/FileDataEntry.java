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

import proguard.classfile.ClassConstants;

import java.io.*;

/**
 * This <code>DataEntry</code> represents a file.
 *
 * @author Eric Lafortune
 */
public class FileDataEntry implements DataEntry
{
    private final File        directory;
    private final File        file;
    private       InputStream inputStream;


    public FileDataEntry(File directory,
                         File file)
    {
        this.directory = directory;
        this.file      = file;
    }


    /**
     * Returns the complete file, including its directory.
     */
    public File getFile()
    {
        return file.equals(directory) ?
            file :
            new File(directory, getRelativeFilePath());
    }


    // Implementations for DataEntry.

    @Override
    public String getName()
    {
        // Chop the directory name from the file name and get the right separators.
        return file.equals(directory) ?
            file.getName() :
            getRelativeFilePath();
    }


    /**
     * Returns the file path of this data entry, relative to the base directory.
     * If the file equals the base directory, an empty string is returned.
     */
    private String getRelativeFilePath()
    {
        return file.equals(directory) ?
            "" :
            file.getPath()
                .substring(directory.getPath().length() + File.separator.length())
                .replace(File.separatorChar, ClassConstants.PACKAGE_SEPARATOR);
    }


    @Override
    public String getOriginalName()
    {
        return getName();
    }


    @Override
    public long getSize()
    {
        return file.length();
    }


    @Override
    public boolean isDirectory()
    {
        return file.isDirectory();
    }


    @Override
    public InputStream getInputStream() throws IOException
    {
        if (inputStream == null)
        {
            inputStream = new BufferedInputStream(new FileInputStream(file));
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
