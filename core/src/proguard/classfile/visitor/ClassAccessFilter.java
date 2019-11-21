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
 * This <code>ClassVisitor</code> delegates its visits to another given
 * <code>ClassVisitor</code>, but only when the visited class
 * has the proper access flags.
 *
 * @see ClassConstants
 *
 * @author Eric Lafortune
 */
public class ClassAccessFilter implements ClassVisitor
{
    private final int          requiredSetAccessFlags;
    private final int          requiredUnsetAccessFlags;
    private final ClassVisitor classVisitor;


    /**
     * Creates a new ClassAccessFilter.
     * @param requiredSetAccessFlags   the class access flags that should be
     *                                 set.
     * @param requiredUnsetAccessFlags the class access flags that should be
     *                                 unset.
     * @param classVisitor             the <code>ClassVisitor</code> to
     *                                 which visits will be delegated.
     */
    public ClassAccessFilter(int          requiredSetAccessFlags,
                             int          requiredUnsetAccessFlags,
                             ClassVisitor classVisitor)
    {
        this.requiredSetAccessFlags   = requiredSetAccessFlags;
        this.requiredUnsetAccessFlags = requiredUnsetAccessFlags;
        this.classVisitor             = classVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        if (accepted(programClass.getAccessFlags()))
        {
            classVisitor.visitProgramClass(programClass);
        }
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        if (accepted(libraryClass.getAccessFlags()))
        {
            classVisitor.visitLibraryClass(libraryClass);
        }
    }


    // Small utility methods.

    private boolean accepted(int accessFlags)
    {
        return (requiredSetAccessFlags   & ~accessFlags) == 0 &&
               (requiredUnsetAccessFlags &  accessFlags) == 0;
    }
}
