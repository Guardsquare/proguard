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
import proguard.classfile.util.ClassUtil;

/**
 * This {@link MemberVisitor} delegates its visits to one of two other given
 * {@link MemberVisitor} instances, depending on whether the visited method
 * is a static initializer or instance initializer, or not.
 *
 * @author Eric Lafortune
 */
public class InitializerMethodFilter
implements   MemberVisitor
{
    private final MemberVisitor initializerMemberVisitor;
    private final MemberVisitor otherMemberVisitor;


    /**
     * Creates a new InitializerMethodFilter.
     * @param initializerMemberVisitor the member visitor to which visits to
     *                                 initializers will be delegated.
     */
    public InitializerMethodFilter(MemberVisitor initializerMemberVisitor)
    {
        this(initializerMemberVisitor, null);
    }


    /**
     * Creates a new InitializerMethodFilter.
     * @param initializerMemberVisitor the member visitor to which visits to
     *                                 initializers will be delegated.
     * @param otherMemberVisitor       the member visitor to which visits to
     *                                 non-initializer methods will be delegated.
     */
    public InitializerMethodFilter(MemberVisitor initializerMemberVisitor,
                                   MemberVisitor otherMemberVisitor)
    {
        this.initializerMemberVisitor = initializerMemberVisitor;
        this.otherMemberVisitor       = otherMemberVisitor;
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField) {}
    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {}


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        MemberVisitor memberVisitor =
            applicableMemberVisitor(programClass, programMethod);

        if (memberVisitor != null)
        {
            memberVisitor.visitProgramMethod(programClass, programMethod);
        }
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        MemberVisitor memberVisitor =
            applicableMemberVisitor(libraryClass, libraryMethod);

        if (memberVisitor != null)
        {
            memberVisitor.visitLibraryMethod(libraryClass, libraryMethod);
        }
    }


    // Small utility methods.

    /**
     * Returns the appropriate member visitor, depending on whether the
     * given method is an initializer or not.
     */
    private MemberVisitor applicableMemberVisitor(Clazz clazz, Member method)
    {
        return ClassUtil.isInitializer(method.getName(clazz)) ?
            initializerMemberVisitor :
            otherMemberVisitor;
    }
}
