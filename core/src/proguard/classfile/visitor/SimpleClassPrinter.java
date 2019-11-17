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

import java.io.PrintWriter;


/**
 * This <code>ClassVisitor</code> and <code>MemberVisitor</code>
 * prints out the class names of the classes it visits, and the full class
 * member descriptions of the class members it visits. The names are printed
 * in a readable, Java-like format. The access modifiers can be included or not.
 *
 * @author Eric Lafortune
 */
public class SimpleClassPrinter
implements   ClassVisitor,
             MemberVisitor
{
    private final boolean     printAccessModifiers;
    private final PrintWriter pw;


    /**
     * Creates a new SimpleClassPrinter that prints to the standard output, with
     * or without the access modifiers.
     */
    public SimpleClassPrinter(boolean printAccessModifiers)
    {
        // We're using the system's default character encoding for writing to
        // the standard output.
        this(printAccessModifiers, new PrintWriter(System.out, true));
    }


    /**
     * Creates a new SimpleClassPrinter that prints to the given writer, with
     * or without the access modifiers.
     */
    public SimpleClassPrinter(boolean     printAccessModifiers,
                              PrintWriter printWriter)
    {
        this.printAccessModifiers = printAccessModifiers;
        this.pw                   = printWriter;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        pw.println(ClassUtil.externalFullClassDescription(
                       printAccessModifiers ?
                           programClass.getAccessFlags() :
                           0,
                       programClass.getName()));
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        pw.println(ClassUtil.externalFullClassDescription(
                       printAccessModifiers ?
                           libraryClass.getAccessFlags() :
                           0,
                       libraryClass.getName()));
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        pw.println(ClassUtil.externalFullClassDescription(
                       printAccessModifiers ?
                           programClass.getAccessFlags() :
                           0,
                       programClass.getName()) +
                   ": " +
                   ClassUtil.externalFullFieldDescription(
                       printAccessModifiers ?
                           programField.getAccessFlags() :
                           0,
                       programField.getName(programClass),
                       programField.getDescriptor(programClass)));
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        pw.println(ClassUtil.externalFullClassDescription(
                       printAccessModifiers ?
                           programClass.getAccessFlags() :
                           0,
                       programClass.getName()) +
                   ": " +
                   ClassUtil.externalFullMethodDescription(
                       programClass.getName(),
                       printAccessModifiers ?
                           programMethod.getAccessFlags() :
                           0,
                       programMethod.getName(programClass),
                       programMethod.getDescriptor(programClass)));
    }


    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        pw.println(ClassUtil.externalFullClassDescription(
                       printAccessModifiers ?
                           libraryClass.getAccessFlags() :
                           0,
                       libraryClass.getName()) +
                   ": " +
                   ClassUtil.externalFullFieldDescription(
                       printAccessModifiers ?
                           libraryField.getAccessFlags() :
                           0,
                       libraryField.getName(libraryClass),
                       libraryField.getDescriptor(libraryClass)));
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        pw.println(ClassUtil.externalFullClassDescription(
                       printAccessModifiers ?
                           libraryClass.getAccessFlags() :
                           0,
                       libraryClass.getName()) +
                   ": " +
                   ClassUtil.externalFullMethodDescription(
                       libraryClass.getName(),
                       printAccessModifiers ?
                           libraryMethod.getAccessFlags() :
                           0,
                       libraryMethod.getName(libraryClass),
                       libraryMethod.getDescriptor(libraryClass)));
    }
}
