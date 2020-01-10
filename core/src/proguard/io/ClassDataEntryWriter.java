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

import proguard.classfile.*;
import proguard.classfile.io.ProgramClassWriter;

import java.io.*;

/**
 * This {@link DataEntryWriter} finds received class entries in the given class pool
 * and writes them out to the given data entry writer. For resource entries,
 * it returns valid output streams. For class entries, it returns output
 * streams that must not be used.
 *
 * @see IdleRewriter
 * @author Eric Lafortune
 */
public class ClassDataEntryWriter implements DataEntryWriter
{
    private final ClassPool       classPool;
    private final DataEntryWriter dataEntryWriter;


    /**
     * Creates a new ClassDataEntryWriter.
     * @param classPool       the class pool in which classes are found.
     * @param dataEntryWriter the writer to which the class file is written.
     */
    public ClassDataEntryWriter(ClassPool       classPool,
                                DataEntryWriter dataEntryWriter)
    {
        this.classPool       = classPool;
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
        String inputName = dataEntry.getName();

        // Is it a class entry?
        String name = dataEntry.getName();
        if (name.endsWith(ClassConstants.CLASS_FILE_EXTENSION))
        {
            // Does it still have a corresponding class?
            String className = inputName.substring(0, inputName.length() - ClassConstants.CLASS_FILE_EXTENSION.length());
            Clazz clazz = classPool.getClass(className);
            if (clazz != null)
            {
                // Rename the data entry if necessary.
                String newClassName = clazz.getName();
                if (!className.equals(newClassName))
                {
                    dataEntry = new RenamedDataEntry(dataEntry, newClassName + ClassConstants.CLASS_FILE_EXTENSION);
                }

                // Get the output stream for this input entry.
                OutputStream outputStream = dataEntryWriter.createOutputStream(dataEntry);
                if (outputStream != null)
                {
                    // Write the class to the output stream.
                    DataOutputStream classOutputStream = new DataOutputStream(outputStream);
                    try
                    {
                        clazz.accept(new ProgramClassWriter(classOutputStream));
                    }
                    catch (RuntimeException e)
                    {
                        throw (RuntimeException)new RuntimeException("Unexpected error while writing class ["+className+"] ("+e.getMessage()+")").initCause(e);
                    }
                    finally
                    {
                        classOutputStream.close();
                    }
                }
            }

            // Return a dummy, non-null output stream (to work with cascading
            // output writers).
            return new FilterOutputStream(null);
        }

        // Delegate for resource entries.
        return dataEntryWriter.createOutputStream(dataEntry);
    }


    @Override
    public void close() throws IOException
    {
        // Close the delegate writer.
        dataEntryWriter.close();
    }


    @Override
    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "ClassDataEntryWriter");
        dataEntryWriter.println(pw, prefix + "  ");
    }
}