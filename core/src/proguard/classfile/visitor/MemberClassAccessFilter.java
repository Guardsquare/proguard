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
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.util.*;

/**
 * This {@link MemberVisitor} delegates its visits to another given
 * {@link MemberVisitor}, but only when the visited member is accessible
 * from the given referencing class.
 *
 * @author Eric Lafortune
 */
public class MemberClassAccessFilter
implements   MemberVisitor
{
    private final NestHostFinder nestHostFinder = new NestHostFinder();
    private final Clazz          referencingClass;
    private final String         referencingNestHostClassName;
    private final MemberVisitor  memberVisitor;



    /**
     * Creates a new MemberAccessFilter.
     * @param referencingClass the class that is accessing the member.
     * @param memberVisitor    the <code>MemberVisitor</code> to which visits
     *                         will be delegated.
     */
    public MemberClassAccessFilter(Clazz         referencingClass,
                                   MemberVisitor memberVisitor)
    {
        this.referencingClass             = referencingClass;
        this.referencingNestHostClassName = nestHostFinder.findNestHostClassName(referencingClass);
        this.memberVisitor                = memberVisitor;
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        if (accepted(programClass, programField.getAccessFlags()))
        {
            memberVisitor.visitProgramField(programClass, programField);
        }
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        if (accepted(programClass, programMethod.getAccessFlags()))
        {
            memberVisitor.visitProgramMethod(programClass, programMethod);
        }
    }


    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        if (accepted(libraryClass, libraryField.getAccessFlags()))
        {
            memberVisitor.visitLibraryField(libraryClass, libraryField);
        }
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        if (accepted(libraryClass, libraryMethod.getAccessFlags()))
        {
            memberVisitor.visitLibraryMethod(libraryClass, libraryMethod);
        }
    }


    // Small utility methods.

    private boolean accepted(Clazz clazz, int memberAccessFlags)
    {
        int accessLevel = AccessUtil.accessLevel(memberAccessFlags);

        return
            (accessLevel >= AccessUtil.PUBLIC                                                               ) ||
            (accessLevel >= AccessUtil.PRIVATE         && nestHostFinder.inSameNest(referencingClass, clazz)) ||
            (accessLevel >= AccessUtil.PACKAGE_VISIBLE && (ClassUtil.internalPackageName(referencingClass.getName()).equals(
                                                           ClassUtil.internalPackageName(clazz.getName()))) ) ||
            (accessLevel >= AccessUtil.PROTECTED       && (referencingClass.extends_(clazz) ||
                                                           referencingClass.extendsOrImplements(clazz))     );
    }
}
