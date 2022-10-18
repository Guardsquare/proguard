/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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
package proguard.optimize.kotlin.attribute.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.attribute.InnerClassesAttribute;
import proguard.classfile.attribute.visitor.AllInnerClassesInfoVisitor;
import proguard.classfile.attribute.visitor.InnerClassesInfoVisitor;

/**
 * This {@link AllInnerClassesInfoVisitor} revisits each {@link InnerClassesAttribute} everytime its amount of
 * referenced classes has been modified in the meantime.
 */
public class ModifiedAllInnerClassesInfoVisitor extends AllInnerClassesInfoVisitor {

    public ModifiedAllInnerClassesInfoVisitor(InnerClassesInfoVisitor innerClassesInfoVisitor) {
        super(innerClassesInfoVisitor);
    }

    public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute)
    {
        int originalClassesCount = -1;
        while (originalClassesCount != innerClassesAttribute.u2classesCount)
        {
            originalClassesCount = innerClassesAttribute.u2classesCount;
            super.visitInnerClassesAttribute(clazz, innerClassesAttribute);
        }
    }
}
