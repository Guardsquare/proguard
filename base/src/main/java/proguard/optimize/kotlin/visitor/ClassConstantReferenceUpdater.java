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
package proguard.optimize.kotlin.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.visitor.ConstantVisitor;

public class ClassConstantReferenceUpdater implements ConstantVisitor
{
    private final Clazz originalClass;
    private final Clazz replacingClass;
    public ClassConstantReferenceUpdater(Clazz originalClass, Clazz replacingClass)
    {
        this.originalClass  = originalClass;
        this.replacingClass = replacingClass;
    }

    @Override
    public void visitAnyConstant(Clazz clazz, Constant constant) {}

    @Override
    public void visitClassConstant(Clazz clazz, ClassConstant classConstant) {
        if (classConstant.referencedClass == originalClass)
        {
            classConstant.referencedClass = replacingClass;
        }
    }
}
