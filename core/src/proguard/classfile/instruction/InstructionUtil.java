/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.instruction;

import proguard.classfile.ClassConstants;

import static proguard.classfile.ClassConstants.*;
import static proguard.classfile.instruction.InstructionConstants.*;

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
     * @return <code>ClassConstants.TYPE_BOOLEAN</code>,
     *         <code>ClassConstants.TYPE_BYTE</code>,
     *         <code>ClassConstants.TYPE_CHAR</code>,
     *         <code>ClassConstants.TYPE_SHORT</code>,
     *         <code>ClassConstants.TYPE_INT</code>,
     *         <code>ClassConstants.TYPE_LONG</code>,
     *         <code>ClassConstants.TYPE_FLOAT</code>, or
     *         <code>ClassConstants.TYPE_DOUBLE</code>.
     */
    public static char internalTypeFromArrayType(byte arrayType)
    {
        switch (arrayType)
        {
            case InstructionConstants.ARRAY_T_BOOLEAN: return ClassConstants.TYPE_BOOLEAN;
            case InstructionConstants.ARRAY_T_CHAR:    return ClassConstants.TYPE_CHAR;
            case InstructionConstants.ARRAY_T_FLOAT:   return ClassConstants.TYPE_FLOAT;
            case InstructionConstants.ARRAY_T_DOUBLE:  return ClassConstants.TYPE_DOUBLE;
            case InstructionConstants.ARRAY_T_BYTE:    return ClassConstants.TYPE_BYTE;
            case InstructionConstants.ARRAY_T_SHORT:   return ClassConstants.TYPE_SHORT;
            case InstructionConstants.ARRAY_T_INT:     return ClassConstants.TYPE_INT;
            case InstructionConstants.ARRAY_T_LONG:    return ClassConstants.TYPE_LONG;
            default: throw new IllegalArgumentException("Unknown array type ["+arrayType+"]");
        }
    }

    /**
     * Returns the newarray type constant for the given internal primitive
     * type.
     *
     * @param internalType a primitive type ('Z','B','I',...)
     * @return the array type constant corresponding to the given
     *         primitive type.
     * @see #internalTypeFromArrayType(byte)
     */
    public static byte arrayTypeFromInternalType(char internalType)
    {
        switch (internalType)
        {
            case TYPE_BOOLEAN: return ARRAY_T_BOOLEAN;
            case TYPE_BYTE:    return ARRAY_T_BYTE;
            case TYPE_CHAR:    return ARRAY_T_CHAR;
            case TYPE_SHORT:   return ARRAY_T_SHORT;
            case TYPE_INT:     return ARRAY_T_INT;
            case TYPE_LONG:    return ARRAY_T_LONG;
            case TYPE_FLOAT:   return ARRAY_T_FLOAT;
            case TYPE_DOUBLE:  return ARRAY_T_DOUBLE;
            default:           throw new IllegalArgumentException("Unknown primitive: " + internalType);
        }
    }
}
