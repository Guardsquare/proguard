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

import java.util.Set;

/**
 * This <code>ClassVisitor</code> sets the version number of the program classes
 * that it visits.
 *
 * @author Eric Lafortune
 */
public class ClassVersionSetter implements ClassVisitor
{
    private final int classVersion;

    private final Set newerClassVersions;


    /**
     * Creates a new ClassVersionSetter.
     * @param classVersion the class version number.
     */
    public ClassVersionSetter(int classVersion)
    {
        this(classVersion, null);
    }


    /**
     * Creates a new ClassVersionSetter that also stores any newer class version
     * numbers that it encounters while visiting program classes.
     * @param classVersion       the class version number.
     * @param newerClassVersions the <code>Set</code> in which newer class
     *                           version numbers can be collected.
     */
    public ClassVersionSetter(int classVersion,
                              Set newerClassVersions)
    {
        this.classVersion       = classVersion;
        this.newerClassVersions = newerClassVersions;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        if (programClass.u4version > classVersion &&
            newerClassVersions != null)
        {
            newerClassVersions.add(new Integer(programClass.u4version));
        }

        programClass.u4version = classVersion;
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Library classes don't have version numbers.
    }
}
