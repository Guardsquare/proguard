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
package proguard.classfile.constant;

import proguard.classfile.*;
import proguard.classfile.constant.visitor.ConstantVisitor;

/**
 * This Constant represents a method handle constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public class MethodTypeConstant extends Constant
{
    public int u2descriptorIndex;


    /**
     * An extra field pointing to the java.lang.invoke.MethodType Clazz object.
     * This field is typically filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer
     * ClassReferenceInitializer}</code>..
     */
    public Clazz javaLangInvokeMethodTypeClass;


    /**
     * Creates an uninitialized MethodTypeConstant.
     */
    public MethodTypeConstant()
    {
    }


    /**
     * Creates a new MethodTypeConstant with the given descriptor index.
     * @param u2descriptorIndex the index of the descriptor in the constant
     *                          pool.
     */
    public MethodTypeConstant(int u2descriptorIndex)
    {
        this.u2descriptorIndex = u2descriptorIndex;
    }


    /**
     * Returns the descriptor index.
     */
    public int getDescriptorIndex()
    {
        return u2descriptorIndex;
    }


    /**
     * Returns the type.
     */
    public String getType(Clazz clazz)
    {
        return clazz.getString(u2descriptorIndex);
    }


    // Implementations for Constant.

    public int getTag()
    {
        return ClassConstants.CONSTANT_MethodType;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        constantVisitor.visitMethodTypeConstant(clazz, this);
    }
}
