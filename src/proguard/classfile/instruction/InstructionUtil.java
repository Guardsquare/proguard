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
package proguard.classfile.instruction;

import proguard.classfile.ClassConstants;

/**
 * Utility methods for converting between representations of names and
 * descriptions.
 *
 * @author Eric Lafortune
 */
public class InstructionUtil
{
    /**
     * Returns the internal type corresponding to the given 'newarray' type.
     * @param arrayType <code>InstructionConstants.ARRAY_T_BOOLEAN</code>,
     *                  <code>InstructionConstants.ARRAY_T_BYTE</code>,
     *                  <code>InstructionConstants.ARRAY_T_CHAR</code>,
     *                  <code>InstructionConstants.ARRAY_T_SHORT</code>,
     *                  <code>InstructionConstants.ARRAY_T_INT</code>,
     *                  <code>InstructionConstants.ARRAY_T_LONG</code>,
     *                  <code>InstructionConstants.ARRAY_T_FLOAT</code>, or
     *                  <code>InstructionConstants.ARRAY_T_DOUBLE</code>.
     * @return <code>ClassConstants.INTERNAL_TYPE_BOOLEAN</code>,
     *         <code>ClassConstants.INTERNAL_TYPE_BYTE</code>,
     *         <code>ClassConstants.INTERNAL_TYPE_CHAR</code>,
     *         <code>ClassConstants.INTERNAL_TYPE_SHORT</code>,
     *         <code>ClassConstants.INTERNAL_TYPE_INT</code>,
     *         <code>ClassConstants.INTERNAL_TYPE_LONG</code>,
     *         <code>ClassConstants.INTERNAL_TYPE_FLOAT</code>, or
     *         <code>ClassConstants.INTERNAL_TYPE_DOUBLE</code>.
     */
    public static char internalTypeFromArrayType(byte arrayType)
    {
        switch (arrayType)
        {
            case InstructionConstants.ARRAY_T_BOOLEAN: return ClassConstants.INTERNAL_TYPE_BOOLEAN;
            case InstructionConstants.ARRAY_T_CHAR:    return ClassConstants.INTERNAL_TYPE_CHAR;
            case InstructionConstants.ARRAY_T_FLOAT:   return ClassConstants.INTERNAL_TYPE_FLOAT;
            case InstructionConstants.ARRAY_T_DOUBLE:  return ClassConstants.INTERNAL_TYPE_DOUBLE;
            case InstructionConstants.ARRAY_T_BYTE:    return ClassConstants.INTERNAL_TYPE_BYTE;
            case InstructionConstants.ARRAY_T_SHORT:   return ClassConstants.INTERNAL_TYPE_SHORT;
            case InstructionConstants.ARRAY_T_INT:     return ClassConstants.INTERNAL_TYPE_INT;
            case InstructionConstants.ARRAY_T_LONG:    return ClassConstants.INTERNAL_TYPE_LONG;
            default: throw new IllegalArgumentException("Unknown array type ["+arrayType+"]");
        }
    }
}
