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
 * This <code>MemberVisitor</code> lets a given <code>MemberVisitor</code>
 * visit all members that have the same name and type as the visited methods
 * in the class hierarchy of the members' classes or of a given target class.
 *
 * @author Eric Lafortune
 */
public class SimilarMemberVisitor
implements   MemberVisitor
{
    private final Clazz         targetClass;
    private final boolean       visitThisMember;
    private final boolean       visitSuperMembers;
    private final boolean       visitInterfaceMembers;
    private final boolean       visitOverridingMembers;
    private final MemberVisitor memberVisitor;


    /**
     * Creates a new SimilarMemberVisitor.
     * @param visitThisMember        specifies whether to visit the class
     *                               members in the members' classes themselves.
     * @param visitSuperMembers      specifies whether to visit the class
     *                               members in the super classes of the
     *                               members' classes.
     * @param visitInterfaceMembers  specifies whether to visit the class
     *                               members in the interface classes of the
     *                               members' classes.
     * @param visitOverridingMembers specifies whether to visit the class
     *                               members in the subclasses of the members'
     *                               classes.
     * @param memberVisitor          the <code>MemberVisitor</code> to which
     *                               visits will be delegated.
     */
    public SimilarMemberVisitor(boolean       visitThisMember,
                                boolean       visitSuperMembers,
                                boolean       visitInterfaceMembers,
                                boolean       visitOverridingMembers,
                                MemberVisitor memberVisitor)
    {
        this(null,
             visitThisMember,
             visitSuperMembers,
             visitInterfaceMembers,
             visitOverridingMembers,
             memberVisitor);
    }


    /**
     * Creates a new SimilarMemberVisitor.
     * @param targetClass            the class in whose hierarchy to look for
     *                               the visited class members.
     * @param visitThisMember        specifies whether to visit the class
     *                               members in the target class itself.
     * @param visitSuperMembers      specifies whether to visit the class
     *                               members in the super classes of the target
     *                               class.
     * @param visitInterfaceMembers  specifies whether to visit the class
     *                               members in the interface classes of the
     *                               target class.
     * @param visitOverridingMembers specifies whether to visit the class
     *                               members in the subclasses of the target
     *                               class.
     * @param memberVisitor          the <code>MemberVisitor</code> to which
     *                               visits will be delegated.
     */
    public SimilarMemberVisitor(Clazz         targetClass,
                                boolean       visitThisMember,
                                boolean       visitSuperMembers,
                                boolean       visitInterfaceMembers,
                                boolean       visitOverridingMembers,
                                MemberVisitor memberVisitor)
    {
        this.targetClass            = targetClass;
        this.visitThisMember        = visitThisMember;
        this.visitSuperMembers      = visitSuperMembers;
        this.visitInterfaceMembers  = visitInterfaceMembers;
        this.visitOverridingMembers = visitOverridingMembers;
        this.memberVisitor          = memberVisitor;
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        Clazz targetClass = targetClass(programClass);

        targetClass.hierarchyAccept(visitThisMember,
                                    visitSuperMembers,
                                    visitInterfaceMembers,
                                    visitOverridingMembers,
                                    new NamedFieldVisitor(programField.getName(programClass),
                                                          programField.getDescriptor(programClass),
                                                          memberVisitor));
    }


    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        Clazz targetClass = targetClass(libraryClass);

        targetClass.hierarchyAccept(visitThisMember,
                                    visitSuperMembers,
                                    visitInterfaceMembers,
                                    visitOverridingMembers,
                                    new NamedFieldVisitor(libraryField.getName(libraryClass),
                                                          libraryField.getDescriptor(libraryClass),
                                                          memberVisitor));
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        Clazz targetClass = targetClass(programClass);

        targetClass.hierarchyAccept(visitThisMember,
                                    visitSuperMembers,
                                    visitInterfaceMembers,
                                    visitOverridingMembers,
                                    new NamedMethodVisitor(programMethod.getName(programClass),
                                                           programMethod.getDescriptor(programClass),
                                                           memberVisitor));
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        Clazz targetClass = targetClass(libraryClass);

        targetClass.hierarchyAccept(visitThisMember,
                                    visitSuperMembers,
                                    visitInterfaceMembers,
                                    visitOverridingMembers,
                                    new NamedMethodVisitor(libraryMethod.getName(libraryClass),
                                                           libraryMethod.getDescriptor(libraryClass),
                                                           memberVisitor));
    }


    /**
     * Returns the target class, or the given class if the target class is
     * null.
     */
    private Clazz targetClass(Clazz clazz)
    {
        return targetClass != null ? targetClass : clazz;
    }
}