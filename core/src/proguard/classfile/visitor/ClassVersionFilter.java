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

import proguard.classfile.*;

/**
 * This {@link ClassVisitor} delegates its visits to program classes to
 * another given {@link ClassVisitor}, but only when the class version
 * number of the visited program class lies in a given range.
 *
 * @author Eric Lafortune
 */
public class ClassVersionFilter implements ClassVisitor
{
    private final int          minimumClassVersion;
    private final int          maximumClassVersion;
    private final ClassVisitor classVisitor;


    /**
     * Creates a new ClassVersionFilter.
     * @param minimumClassVersion the minimum class version number.
     * @param classVisitor        the <code>ClassVisitor</code> to which visits
     *                            will be delegated.
     */
    public ClassVersionFilter(int          minimumClassVersion,
                              ClassVisitor classVisitor)
    {
        this(minimumClassVersion, Integer.MAX_VALUE, classVisitor);
    }


    /**
     * Creates a new ClassVersionFilter.
     * @param minimumClassVersion the minimum class version number.
     * @param maximumClassVersion the maximum class version number.
     * @param classVisitor        the <code>ClassVisitor</code> to which visits
     *                            will be delegated.
     */
    public ClassVersionFilter(int          minimumClassVersion,
                              int          maximumClassVersion,
                              ClassVisitor classVisitor)
    {
        this.minimumClassVersion = minimumClassVersion;
        this.maximumClassVersion = maximumClassVersion;
        this.classVisitor        = classVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        if (programClass.u4version >= minimumClassVersion &&
            programClass.u4version <= maximumClassVersion)
        {
            classVisitor.visitProgramClass(programClass);
        }
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Library classes don't have version numbers.
    }
}
