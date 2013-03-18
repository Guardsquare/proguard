/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2013 Eric Lafortune (eric@graphics.cornell.edu)
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
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;

/**
 * This ConstantVisitor fixes the access modifiers of all classes and class
 * members that are referenced by the constants that it visits.
 *
 * @author Eric Lafortune
 */
public class AccessFixer
extends      SimplifiedVisitor
implements   ConstantVisitor,
             ClassVisitor,
             MemberVisitor
{
    private MyReferencedClassFinder referencedClassFinder = new MyReferencedClassFinder();

    private Clazz referencingClass;
    private Clazz referencedClass;


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        referencingClass = clazz;
        referencedClass  = stringConstant.referencedClass;

        // Make sure the access flags of the referenced class or class member,
        // if any, are acceptable.
        stringConstant.referencedClassAccept(this);
        stringConstant.referencedMemberAccept(this);
    }


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        // Check the bootstrap method.
        invokeDynamicConstant.bootstrapMethodHandleAccept(clazz, this);
    }


    public void visitMethodHandleConstant(Clazz clazz, MethodHandleConstant methodHandleConstant)
    {
        // Check the method reference.
        clazz.constantPoolEntryAccept(methodHandleConstant.u2referenceIndex, this);
    }


    public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant)
    {
        referencingClass = clazz;

        // Remember the specified class, since it might be different from
        // the referenced class that actually contains the class member.
        clazz.constantPoolEntryAccept(refConstant.u2classIndex, referencedClassFinder);

        // Make sure the access flags of the referenced class member are
        // acceptable.
        refConstant.referencedMemberAccept(this);
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        referencingClass = clazz;

        // Make sure the access flags of the referenced class are acceptable.
        classConstant.referencedClassAccept(this);
    }


    // Implementations for ClassVisitor.

    public void visitLibraryClass(LibraryClass libraryClass) {}


    public void visitProgramClass(ProgramClass programClass)
    {
        int currentAccessFlags = programClass.getAccessFlags();
        int currentAccessLevel = AccessUtil.accessLevel(currentAccessFlags);

        // Compute the required access level.
        Clazz referencingClass = this.referencingClass;
        int requiredAccessLevel =
            inSamePackage(programClass, referencingClass) ? AccessUtil.PACKAGE_VISIBLE :
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
            referencedClass.extends_(referencingClass) &&
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


    /**
     * This ConstantVisitor returns the referenced class of the class constant
     * that it visits.
     */
    private class MyReferencedClassFinder
    extends       SimplifiedVisitor
    implements    ConstantVisitor
    {
        // Implementations for ConstantVisitor.
        public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
        {
            referencedClass = classConstant.referencedClass;
        }
    }


    // Small utility methods.

    private boolean inSamePackage(ProgramClass class1, Clazz class2)
    {
        return ClassUtil.internalPackageName(class1.getName()).equals(
               ClassUtil.internalPackageName(class2.getName()));
    }
}
