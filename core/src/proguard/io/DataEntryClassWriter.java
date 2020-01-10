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
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.ClassVisitor;

import java.io.*;

/**
 * This {@link ClassVisitor} writes out the {@link ProgramClass} instances that it visits to the
 * given {@link DataEntry}, modified to have the correct name.
 *
 * @author Eric Lafortune
 */
public class DataEntryClassWriter
extends      SimplifiedVisitor
implements   ClassVisitor
{
    private final DataEntry       templateDataEntry;
    private final DataEntryWriter dataEntryWriter;


    /**
     * Creates a new DataEntryClassWriter for writing to the given
     * DataEntryWriter.
     */
    public DataEntryClassWriter(DataEntryWriter dataEntryWriter)
    {
        this(new FileDataEntry(new File(""), new File("")), dataEntryWriter);
    }


    /**
     * Creates a new DataEntryClassWriter for writing to the given
     * DataEntryWriter, based on the given template DataEntry.
     */
    public DataEntryClassWriter(DataEntry       templateDataEntry,
                                DataEntryWriter dataEntryWriter)
    {
        this.templateDataEntry = templateDataEntry;
        this.dataEntryWriter   = dataEntryWriter;
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        // Rename the data entry if necessary.
        String    actualClassName = programClass.getName();
        DataEntry actualDataEntry =
            new RenamedDataEntry(templateDataEntry,
                                 actualClassName + ClassConstants.CLASS_FILE_EXTENSION);

        try
        {
            // Get the output entry corresponding to this input entry.
            OutputStream outputStream = dataEntryWriter.createOutputStream(actualDataEntry);
            if (outputStream != null)
            {
                // Write the class to the output entry.
                DataOutputStream classOutputStream = new DataOutputStream(outputStream);
                try
                {
                    new ProgramClassWriter(classOutputStream).visitProgramClass(programClass);
                }
                finally
                {
                    classOutputStream.close();
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Can't write program class ["+actualClassName+"] to ["+actualDataEntry+"] ("+e.getMessage()+")", e);
        }
    }
}
