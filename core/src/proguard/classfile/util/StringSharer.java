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
package proguard.classfile.util;

import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.ClassVisitor;

/**
 * This {@link ClassVisitor} shares strings in the class files that it visits.
 *
 * @author Eric Lafortune
 */
public class StringSharer
extends      SimplifiedVisitor
implements   ClassVisitor,
             ConstantVisitor,
             AttributeVisitor
{
    // A fields acting as an argument for the visitor methods.
    private String name;
    private String type;


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        // Replace name strings in the constant pool by shared strings.
        programClass.constantPoolEntriesAccept(this);

        // Replace attribute name strings in the constant pool by internalized
        // strings.
        programClass.attributesAccept(this);
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Replace the super class name string by the shared name string.
        Clazz superClass = libraryClass.superClass;
        if (superClass != null)
        {
            libraryClass.superClassName = superClass.getName();
        }

        // Replace the interface name strings by the shared name strings.
        if (libraryClass.interfaceNames != null)
        {
            String[] interfaceNames   = libraryClass.interfaceNames;
            Clazz[]  interfaceClasses = libraryClass.interfaceClasses;

            for (int index = 0; index < interfaceNames.length; index++)
            {
                // Keep a reference to the interface class.
                Clazz interfaceClass = interfaceClasses[index];
                if (interfaceClass != null)
                {
                    interfaceNames[index] = interfaceClass.getName();
                }
            }
        }
    }


    // Implementations for ConstantVisitor.


    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        Member referencedMember = stringConstant.referencedMember;
        if (referencedMember != null)
        {
            Clazz referencedClass = stringConstant.referencedClass;

            // Put the actual class member's name in the class pool.
            name = referencedMember.getName(referencedClass);
            clazz.constantPoolEntryAccept(stringConstant.u2stringIndex, this);
        }
    }


    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        Field referencedField = fieldrefConstant.referencedField;
        if (referencedField != null)
        {
            Clazz referencedClass = fieldrefConstant.referencedClass;

            // Put the actual class field's name and type strings in the class
            // pool.
            name = referencedField.getName(referencedClass);
            type = referencedField.getDescriptor(referencedClass);
            clazz.constantPoolEntryAccept(fieldrefConstant.u2nameAndTypeIndex, this);
        }
    }


    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        Method referencedMethod = anyMethodrefConstant.referencedMethod;
        if (referencedMethod != null)
        {
            Clazz referencedClass = anyMethodrefConstant.referencedClass;

            // Put the actual class method's name and type strings in the class
            // pool.
            name = referencedMethod.getName(referencedClass);
            type = referencedMethod.getDescriptor(referencedClass);
            clazz.constantPoolEntryAccept(anyMethodrefConstant.u2nameAndTypeIndex, this);
        }
    }


    public void visitNameAndTypeConstant(Clazz clazz, NameAndTypeConstant nameAndTypeConstant)
    {
        if (name != null)
        {
            // Put the actual class member's name and type strings in the class
            // pool.
            clazz.constantPoolEntryAccept(nameAndTypeConstant.u2nameIndex, this);
            name = type;
            clazz.constantPoolEntryAccept(nameAndTypeConstant.u2descriptorIndex, this);
        }
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        Clazz referencedClass = classConstant.referencedClass;
        if (referencedClass != null)
        {
            // Put the actual class's name string in the class pool.
            name = referencedClass.getName();
            clazz.constantPoolEntryAccept(classConstant.u2nameIndex, this);
        }
    }


    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
    {
        // Do we have a new string to put into this constant?
        if (name != null)
        {
            // Replace the string, if it's actually the same.
            if (name.equals(utf8Constant.getString()))
            {
                utf8Constant.setString(name);
            }

            name = null;
        }
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute)
    {
        // Put the internalized attribute's name string in the class pool.
        name = attribute.getAttributeName(clazz).intern();
        clazz.constantPoolEntryAccept(attribute.u2attributeNameIndex, this);
    }
}
