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
package proguard.classfile.visitor;

import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;


/**
 * This {@link ClassVisitor} delegates its visits to another given
 * {@link ClassVisitor}, but only when the visited class
 * has the proper processing flags.
 *
 * @author Johan Leys
 */
public class ClassProcessingFlagFilter implements ClassVisitor
{
    private final int          requiredSetProcessingFlags;
    private final int          requiredUnsetProcessingFlags;
    private final ClassVisitor classVisitor;


    /**
     * Creates a new ClassProcessingFlagFilter.
     *
     * @param requiredSetProcessingFlags   the class processing flags that should be set.
     * @param requiredUnsetProcessingFlags the class processing flags that should be unset.
     * @param classVisitor                 the <code>ClassVisitor</code> to which visits will be delegated.
     */
    public ClassProcessingFlagFilter(int          requiredSetProcessingFlags,
                                     int          requiredUnsetProcessingFlags,
                                     ClassVisitor classVisitor)
    {
        this.requiredSetProcessingFlags   = requiredSetProcessingFlags;
        this.requiredUnsetProcessingFlags = requiredUnsetProcessingFlags;
        this.classVisitor                 = classVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        if (accepted(programClass.getProcessingFlags()))
        {
            classVisitor.visitProgramClass(programClass);
        }
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        if (accepted(libraryClass.getProcessingFlags()))
        {
            classVisitor.visitLibraryClass(libraryClass);
        }
    }


    // Small utility methods.

    private boolean accepted(int processingFlags)
    {
        return (requiredSetProcessingFlags & ~processingFlags) == 0 &&
               (requiredUnsetProcessingFlags &  processingFlags) == 0;
    }
}
