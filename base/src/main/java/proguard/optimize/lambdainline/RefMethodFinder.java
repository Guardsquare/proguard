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
import proguard.classfile.Member;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.MemberVisitor;
import proguard.classfile.visitor.ReferencedMemberVisitor;

/**
 * A utility class that can be used to easily obtain the referenced method when given a constant index.
 */
public class RefMethodFinder {
    private final Clazz clazz;
    private Method foundMethod;
    public RefMethodFinder(Clazz clazz) {
        this.clazz = clazz;
        this.foundMethod = null;
    }

    public Method findReferencedMethod(int constantIndex) {
        clazz.constantPoolEntryAccept(constantIndex, new ReferencedMemberVisitor(new MemberVisitor() {
            @Override
            public void visitAnyMember(Clazz clazz, Member member) {}

            @Override
            public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
                foundMethod = programMethod;
            }
        }));
        return foundMethod;
    }
}