/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
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
package proguard.obfuscate;

import proguard.classfile.*;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.ConstantPoolEditor;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.*;

/**
 * This <code>ClassVisitor</code> renames the class names and class member
 * names of the classes it visits, using names previously determined by the
 * obfuscator.
 *
 * @see ClassObfuscator
 * @see MemberObfuscator
 *
 * @author Eric Lafortune
 */
public class ClassRenamer
extends      SimplifiedVisitor
implements   ClassVisitor,
             MemberVisitor,
             ConstantVisitor
{
    private final ClassVisitor  extraClassVisitor;
    private final MemberVisitor extraMemberVisitor;


    /**
     * Creates a new ClassRenamer.
     */
    public ClassRenamer()
    {
        this(null, null);
    }


    /**
     * Creates a new ClassRenamer.
     *
     * @param extraClassVisitor  an optional extra visitor for classes that
     *                           have been renamed.
     * @param extraMemberVisitor an optional extra visitor for class members
     *                           that have been renamed.
     */
    public ClassRenamer(ClassVisitor  extraClassVisitor,
                        MemberVisitor extraMemberVisitor)
    {
        this.extraClassVisitor  = extraClassVisitor;
        this.extraMemberVisitor = extraMemberVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        // Rename this class.
        programClass.thisClassConstantAccept(this);

        // Rename the class members.
        programClass.fieldsAccept(this);
        programClass.methodsAccept(this);
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Has the library class name changed?
        String name    = libraryClass.getName();
        String newName = ClassObfuscator.newClassName(libraryClass);
        if (newName != null && !newName.equals(name))
        {
            libraryClass.thisClassName = newName;

            if (extraClassVisitor != null)
            {
                extraClassVisitor.visitLibraryClass(libraryClass);
            }
        }

        // Rename the class members.
        libraryClass.fieldsAccept(this);
        libraryClass.methodsAccept(this);
    }


    // Implementations for MemberVisitor.

    public void visitProgramMember(ProgramClass  programClass,
                                     ProgramMember programMember)
    {
        // Has the class member name changed?
        String name    = programMember.getName(programClass);
        String newName = MemberObfuscator.newMemberName(programMember);
        if (newName != null && !newName.equals(name))
        {
            programMember.u2nameIndex =
                new ConstantPoolEditor(programClass).addUtf8Constant(newName);

            if (extraMemberVisitor != null)
            {
                programMember.accept(programClass, extraMemberVisitor);
            }
        }
    }

    public void visitLibraryMember(LibraryClass  libraryClass,
                                   LibraryMember libraryMember)
    {
        // Has the library member name changed?
        String name    = libraryMember.getName(libraryClass);
        String newName = MemberObfuscator.newMemberName(libraryMember);
        if (newName != null && !newName.equals(name))
        {
            libraryMember.name = newName;

            if (extraMemberVisitor != null)
            {
                libraryMember.accept(libraryClass, extraMemberVisitor);
            }
        }
    }


    // Implementations for ConstantVisitor.

    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Update the Class entry if required.
        String name    = clazz.getName();
        String newName = ClassObfuscator.newClassName(clazz);
        if (newName != null && !newName.equals(name))
        {
            // Refer to a new Utf8 entry.
            classConstant.u2nameIndex =
                new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newName);

            if (extraClassVisitor != null)
            {
                clazz.accept(extraClassVisitor);
            }
        }
    }
}
