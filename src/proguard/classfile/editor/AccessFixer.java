/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2015 Eric Lafortune @ GuardSquare
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.constant.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;

/**
 * This ClassVisitor fixes the access modifiers of all classes and class
 * members that are referenced by the classes that it visits.
 *
 * @author Eric Lafortune
 */
public class AccessFixer
extends      ReferencedClassVisitor
implements   ClassVisitor
{
    /**
     * Creates a new AccessFixer.
     */
    public AccessFixer()
    {
        // Unfortunately, the inner class must be static to be passed to the
        // super constructor. We therefore can't let it refer to this class;
        // we'll let this class refer to the inner class instead.
        super(new MyAccessFixer());
    }


    // Overridden methods for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        // Remember the referencing class.
        ((MyAccessFixer)classVisitor).referencingClass = programClass;

        // Start visiting and fixing the referenced classes and class members.
        super.visitProgramClass(programClass);
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Remember the referencing class.
        ((MyAccessFixer)classVisitor).referencingClass = libraryClass;

        // Start visiting and fixing the referenced classes and class members.
        super.visitLibraryClass(libraryClass);
    }


    // Overridden methods for MemberVisitor.

    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        // Fix the referenced classes and class members.
        super.visitProgramMember(programClass, programMethod);

        // Fix overridden or implemented methods higher up the hierarchy.
        // We can ignore private and static methods and initializers.
        if ((programMethod.getAccessFlags() & (ClassConstants.ACC_PRIVATE |
                                               ClassConstants.ACC_STATIC)) == 0 &&
            !ClassUtil.isInitializer(programMethod.getName(programClass)))
        {
            programClass.hierarchyAccept(false, true, false, false,
                new NamedMethodVisitor(programMethod.getName(programClass),
                                       programMethod.getDescriptor(programClass),
                new MemberAccessFilter(0, ClassConstants.ACC_PRIVATE |
                                          ClassConstants.ACC_STATIC,
                                       (MemberVisitor)classVisitor)));
        }
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        // Fix the referenced classes and class members.
        super.visitLibraryMember(libraryClass, libraryMethod);

        // Fix overridden or implemented methods higher up the hierarchy.
        // We can ignore private and static methods and initializers.
        if ((libraryMethod.getAccessFlags() & (ClassConstants.ACC_PRIVATE |
                                               ClassConstants.ACC_STATIC)) == 0 &&
            !ClassUtil.isInitializer(libraryMethod.getName(libraryClass)))
        {
            libraryClass.hierarchyAccept(false, true, false, false,
                new NamedMethodVisitor(libraryMethod.getName(libraryClass),
                                       libraryMethod.getDescriptor(libraryClass),
                new MemberAccessFilter(0, ClassConstants.ACC_PRIVATE |
                                          ClassConstants.ACC_STATIC,
                                       (MemberVisitor)classVisitor)));
        }
    }


    // Overridden methods for ConstantVisitor.

    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        // Fix the access flags of the referenced class, if any.
        super.visitStringConstant(clazz, stringConstant);

        // Fix the access flags of the referenced class member, if any.
        stringConstant.referencedMemberAccept((MemberVisitor)classVisitor);
    }


    public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant)
    {
        // Fix the access flags of the referenced class.
        super.visitAnyRefConstant(clazz, refConstant);

        // Fix the access flags of the referenced class member.
        refConstant.referencedMemberAccept((MemberVisitor)classVisitor);
    }


    /**
     * This ClassVisitor and MemberVisitor fixes the access flags of the
     * classes and class members that it visits, relative to the referencing
     * class.
     */
    private static class MyAccessFixer
    extends              SimplifiedVisitor
    implements           ClassVisitor,
                         MemberVisitor
    {
        private Clazz referencingClass;


        // Implementations for ClassVisitor.

        public void visitLibraryClass(LibraryClass libraryClass) {}


        public void visitProgramClass(ProgramClass programClass)
        {
            int currentAccessFlags = programClass.getAccessFlags();
            int currentAccessLevel = AccessUtil.accessLevel(currentAccessFlags);

            // Compute the required access level.
            int requiredAccessLevel =
                inSamePackage(programClass, referencingClass) ?
                    AccessUtil.PACKAGE_VISIBLE :
                    AccessUtil.PUBLIC;

            // Fix the class access flags if necessary.
            if (currentAccessLevel < requiredAccessLevel)
            {
                programClass.u2accessFlags =
                    AccessUtil.replaceAccessFlags(currentAccessFlags,
                                                  AccessUtil.accessFlags(requiredAccessLevel));
            }
        }


        // Implementations for MemberVisitor.

        public void visitLibraryMember(LibraryClass libraryClass, LibraryMember libraryMember) {}


        public void visitProgramMember(ProgramClass programClass, ProgramMember programMember)
        {
            int currentAccessFlags = programMember.getAccessFlags();
            int currentAccessLevel = AccessUtil.accessLevel(currentAccessFlags);

            // Compute the required access level.
            int requiredAccessLevel =
                programClass.equals(referencingClass)         ? AccessUtil.PRIVATE         :
                inSamePackage(programClass, referencingClass) ? AccessUtil.PACKAGE_VISIBLE :
                programClass.extends_(referencingClass) &&
                referencingClass.extends_(programClass)       ? AccessUtil.PROTECTED       :
                                                                AccessUtil.PUBLIC;

            // Fix the class member access flags if necessary.
            if (currentAccessLevel < requiredAccessLevel)
            {
                programMember.u2accessFlags =
                    AccessUtil.replaceAccessFlags(currentAccessFlags,
                                                  AccessUtil.accessFlags(requiredAccessLevel));
            }
        }


        // Small utility methods.

        /**
         * Returns whether the two given classes are in the same package.
         */
        private boolean inSamePackage(ProgramClass class1, Clazz class2)
        {
            return ClassUtil.internalPackageName(class1.getName()).equals(
                   ClassUtil.internalPackageName(class2.getName()));
        }
    }
}
