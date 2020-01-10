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
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.attribute.InnerClassesInfo;
import proguard.classfile.attribute.visitor.InnerClassesInfoVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.ClassVisitor;

/**
 * This {@link InnerClassesInfoVisitor} fixes the inner class access flags of the
 * inner classes information that it visits.
 *
 * @author Eric Lafortune
 */
public class InnerClassesAccessFixer
extends      SimplifiedVisitor
implements   InnerClassesInfoVisitor,
             ConstantVisitor,
             ClassVisitor
{
    private int innerClassAccessFlags;


    // Implementations for InnerClassesInfoVisitor.

    public void visitInnerClassesInfo(Clazz clazz, InnerClassesInfo innerClassesInfo)
    {
        // The current access flags are the default.
        innerClassAccessFlags = innerClassesInfo.u2innerClassAccessFlags;

        // See if we can find new access flags.
        innerClassesInfo.innerClassConstantAccept(clazz, this);

        // Update the access flags.
        innerClassesInfo.u2innerClassAccessFlags = innerClassAccessFlags;
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        classConstant.referencedClassAccept(this);
    }


    // Implementations for ClassVisitor.

    public void visitLibraryClass(LibraryClass libraryClass) {}


    public void visitProgramClass(ProgramClass programClass)
    {
        innerClassAccessFlags =
            AccessUtil.replaceAccessFlags(innerClassAccessFlags,
                                          programClass.u2accessFlags);
    }
}