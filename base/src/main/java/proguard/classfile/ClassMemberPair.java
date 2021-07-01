/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
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
package proguard.classfile;

import proguard.classfile.visitor.MemberVisitor;

import java.util.Objects;

/**
 * Container class for a pair of class + member.
 *
 * @author James Hamilton
 */
public class ClassMemberPair
{
    public final Clazz  clazz;
    public final Member member;


    public ClassMemberPair(Clazz clazz, Member member)
    {
        this.clazz  = clazz;
        this.member = member;
    }


    public void accept(MemberVisitor memberVisitor)
    {
        this.member.accept(this.clazz, memberVisitor);
    }


    public String getName()
    {
        return this.member.getName(this.clazz);
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassMemberPair that = (ClassMemberPair)o;
        return Objects.equals(clazz, that.clazz) &&
               Objects.equals(member, that.member);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(clazz, member);
    }


    @Override
    public String toString()
    {
        return clazz.getName() + "." + this.member.getName(this.clazz) + this.member.getDescriptor(this.clazz);
    }
}
