/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
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
package proguard.optimize.gson;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.visitor.*;

/**
 * Checks whether a visited class contains fields that have generic type
 * parameters.
 *
 * For example:
 *
 * <pre>
 * public class Foo<T>
 * {
 *     private T field;
 * }
 * </pre>
 *
 * @author Lars Vandenbergh
 */
class      TypeParameterClassChecker
implements ClassVisitor,
           AttributeVisitor
{
    public boolean hasFieldWithTypeParameter;


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) {}


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.fieldsAccept(new AllAttributeVisitor(this));
    }


    // Implementations for AttributeVisitor.

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    @Override
    public void visitSignatureAttribute(Clazz clazz, Field field, SignatureAttribute signatureAttribute)
    {
        String fieldSignature = signatureAttribute.getSignature(clazz);
        hasFieldWithTypeParameter = hasFieldWithTypeParameter ||
                                    fieldSignature.startsWith("T") ||
                                    fieldSignature.contains("<T")  ||
                                    fieldSignature.contains(";T")  ||
                                    fieldSignature.contains("+L")  ||
                                    fieldSignature.contains("+T")  ||
                                    fieldSignature.contains("[T");
    }
}
