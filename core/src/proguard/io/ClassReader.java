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
import proguard.classfile.io.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.ClassVisitor;

import java.io.*;

/**
 * This DataEntryReader applies a given ClassVisitor to the class
 * definitions that it reads.
 * <p>
 * Class files are read as ProgramClass objects or LibraryClass objects,
 * depending on the <code>isLibrary</code> flag.
 * <p>
 * In case of libraries, only public classes are considered, if the
 * <code>skipNonPublicLibraryClasses</code> flag is set.
 *
 * @author Eric Lafortune
 */
public class ClassReader implements DataEntryReader
{
    private final boolean        isLibrary;
    private final boolean        skipNonPublicLibraryClasses;
    private final boolean        skipNonPublicLibraryClassMembers;
    private final boolean        ignoreStackMapAttributes;
    private final WarningPrinter warningPrinter;
    private final ClassVisitor   classVisitor;


    /**
     * Creates a new DataEntryClassFilter for reading the specified
     * Clazz objects.
     */
    public ClassReader(boolean        isLibrary,
                       boolean        skipNonPublicLibraryClasses,
                       boolean        skipNonPublicLibraryClassMembers,
                       boolean        ignoreStackMapAttributes,
                       WarningPrinter warningPrinter,
                       ClassVisitor   classVisitor)
    {
        this.isLibrary                        = isLibrary;
        this.skipNonPublicLibraryClasses      = skipNonPublicLibraryClasses;
        this.skipNonPublicLibraryClassMembers = skipNonPublicLibraryClassMembers;
        this.ignoreStackMapAttributes         = ignoreStackMapAttributes;
        this.warningPrinter                   = warningPrinter;
        this.classVisitor                     = classVisitor;
    }


    // Implementations for DataEntryReader.

    @Override
    public void read(DataEntry dataEntry) throws IOException
    {
        try
        {
            // Get the input stream.
            InputStream inputStream = dataEntry.getInputStream();

            // Wrap it into a data input stream.
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            // Create a Clazz representation.
            Clazz clazz;
            if (isLibrary)
            {
                clazz = new LibraryClass();
                clazz.accept(new LibraryClassReader(dataInputStream, skipNonPublicLibraryClasses, skipNonPublicLibraryClassMembers));
            }
            else
            {
                clazz = new ProgramClass();
                clazz.accept(new ProgramClassReader(dataInputStream, ignoreStackMapAttributes));
            }

            // Apply the visitor, if we have a real class.
            String className = clazz.getName();
            if (className != null)
            {
                String dataEntryName = dataEntry.getName();
                if (!dataEntryName.equals(ClassConstants.MODULE_INFO_CLASS) &&
                    !dataEntryName.replace(File.pathSeparatorChar, ClassConstants.PACKAGE_SEPARATOR).equals(className + ClassConstants.CLASS_FILE_EXTENSION) &&
                    warningPrinter != null)
                {
                    warningPrinter.print(className,
                                         "Warning: class [" + dataEntry.getName() + "] unexpectedly contains class [" + ClassUtil.externalClassName(className) + "]");
                }

                clazz.accept(classVisitor);
            }

            dataEntry.closeInputStream();
        }
        catch (Exception ex)
        {
            throw (IOException)new IOException("Can't process class ["+dataEntry.getName()+"] ("+ex.getMessage()+")").initCause(ex);
        }
    }
}
