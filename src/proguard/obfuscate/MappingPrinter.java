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
package proguard.obfuscate;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;

import java.io.PrintStream;


/**
 * This ClassVisitor prints out the renamed classes and class members with
 * their old names and new names.
 *
 * @see ClassRenamer
 *
 * @author Eric Lafortune
 */
public class MappingPrinter
extends      SimplifiedVisitor
implements   ClassVisitor,
             MemberVisitor,
             AttributeVisitor
{
    private final PrintStream ps;


    /**
     * Creates a new MappingPrinter that prints to <code>System.out</code>.
     */
    public MappingPrinter()
    {
        this(System.out);
    }


    /**
     * Creates a new MappingPrinter that prints to the given stream.
     * @param printStream the stream to which to print
     */
    public MappingPrinter(PrintStream printStream)
    {
        this.ps = printStream;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        String name    = programClass.getName();
        String newName = ClassObfuscator.newClassName(programClass);

        ps.println(ClassUtil.externalClassName(name) +
                   " -> " +
                   ClassUtil.externalClassName(newName) +
                   ":");

        // Print out the class members.
        programClass.fieldsAccept(this);
        programClass.methodsAccept(this);
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        String newName = MemberObfuscator.newMemberName(programField);
        if (newName != null)
        {
            ps.println("    " +
                       ClassUtil.externalFullFieldDescription(
                           0,
                           programField.getName(programClass),
                           programField.getDescriptor(programClass)) +
                       " -> " +
                       newName);
        }
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        // Special cases: <clinit> and <init> are always kept unchanged.
        // We can ignore them here.
        String name = programMethod.getName(programClass);
        if (name.equals(ClassConstants.INTERNAL_METHOD_NAME_CLINIT) ||
            name.equals(ClassConstants.INTERNAL_METHOD_NAME_INIT))
        {
            return;
        }

        String newName = MemberObfuscator.newMemberName(programMethod);
        if (newName != null)
        {
            ps.print("    ");
            programMethod.attributesAccept(programClass, this);
            ps.println(ClassUtil.externalFullMethodDescription(
                           programClass.getName(),
                           0,
                           programMethod.getName(programClass),
                           programMethod.getDescriptor(programClass)) +
                       " -> " +
                       newName);
        }
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        codeAttribute.attributesAccept(clazz, method, this);
    }


    public void visitLineNumberTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute lineNumberTableAttribute)
    {
        ps.print(lineNumberTableAttribute.getLowestLineNumber() + ":" +
                 lineNumberTableAttribute.getHighestLineNumber() + ":");
    }
}
