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
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.InnerClassesInfo;
import proguard.classfile.attribute.visitor.InnerClassesInfoVisitor;
import proguard.classfile.constant.visitor.ConstantVisitor;

public class InnerClassInfoClassConstantVisitor implements InnerClassesInfoVisitor {

    private final ConstantVisitor innerClassConstantVisitor;
    private final ConstantVisitor outerClassConstantVisitor;

    public InnerClassInfoClassConstantVisitor(ConstantVisitor innerClassConstantVisitor, ConstantVisitor outerClassConstantVisitor)
    {
        this.innerClassConstantVisitor = innerClassConstantVisitor;
        this.outerClassConstantVisitor = outerClassConstantVisitor;
    }

    @Override
    public void visitInnerClassesInfo(Clazz clazz, InnerClassesInfo innerClassesInfo) {
        if (this.innerClassConstantVisitor != null)
        {
            innerClassesInfo.innerClassConstantAccept(clazz, this.innerClassConstantVisitor);
        }
        if (this.outerClassConstantVisitor != null)
        {
            innerClassesInfo.outerClassConstantAccept(clazz, this.outerClassConstantVisitor);
        }
    }
}
