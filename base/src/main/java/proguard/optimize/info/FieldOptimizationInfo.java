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
package proguard.optimize.info;

import proguard.classfile.*;
import proguard.evaluation.value.*;

/**
 * This class stores some optimization information that can be attached to
 * a field.
 *
 * @author Eric Lafortune
 */
public class FieldOptimizationInfo
{
    protected Value value;


    /**
     * Returns whether the method is kept.
     */
    // TODO: This information is now available from the processing flags.
    public boolean isKept()
    {
        return true;
    }


    /**
     * Returns whether the field is ever written to.
     */
    public boolean isWritten()
    {
        return true;
    }


    /**
     * Returns whether the field is ever read.
     */
    public boolean isRead()
    {
        return true;
    }


    /**
     * Returns whether the field can be made private.
     */
    public boolean canBeMadePrivate()
    {
        return false;
    }


    /**
     * Returns a representation of the class through which the field is
     * accessed, or null if it is unknown.
     */
    public ReferenceValue getReferencedClass()
    {
        return null;
    }


    /**
     * Specifies the value of the field.
     */
    public void setValue(Value value)
    {
        this.value = value;
    }


    /**
     * Returns a representation of the value of the field, or null
     * if it is unknown.
     */
    public Value getValue()
    {
        return value;
    }


    /**
     * Creates and sets a FieldOptimizationInfo instance on the given field.
     */
    public static void setFieldOptimizationInfo(Clazz clazz, Field field)
    {
        field.setProcessingInfo(new FieldOptimizationInfo());
    }


    /**
     * Returns the FieldOptimizationInfo instance from the given field.
     */
    public static FieldOptimizationInfo getFieldOptimizationInfo(Field field)
    {
        return (FieldOptimizationInfo)field.getProcessingInfo();
    }
}
