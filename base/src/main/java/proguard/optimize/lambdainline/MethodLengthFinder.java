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
package proguard.optimize.lambdainline;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import java.util.Optional;

/**
 * A simple utility class that can be used to easily obtain the length of a method.
 */
public class MethodLengthFinder {
    private static Optional<Integer> codeLength;

    /**
     * @param method     A Method object from which we'll get the length.
     * @param clazz      The class in which the method is.
     * @return           The length of the method, will return an empty optional if there is no code attribute.
     */
    public static Optional<Integer> getMethodCodeLength(Clazz clazz, Method method) {
        // If a method has no codeAttribute we don't want to return the previous method length value.
        codeLength = Optional.empty();
        method.accept(clazz, new AllAttributeVisitor(new AttributeVisitor() {
            @Override
            public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}
            @Override
            public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
                codeLength = Optional.of(codeAttribute.u4codeLength);
            }
        }));
        return codeLength;
    }
}
