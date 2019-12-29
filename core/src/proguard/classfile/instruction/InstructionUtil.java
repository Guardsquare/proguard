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

import proguard.classfile.TypeConstants;

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
     * @param arrayType <code>Instruction.ARRAY_T_BOOLEAN</code>,
     *                  <code>Instruction.ARRAY_T_BYTE</code>,
     *                  <code>Instruction.ARRAY_T_CHAR</code>,
     *                  <code>Instruction.ARRAY_T_SHORT</code>,
     *                  <code>Instruction.ARRAY_T_INT</code>,
     *                  <code>Instruction.ARRAY_T_LONG</code>,
     *                  <code>Instruction.ARRAY_T_FLOAT</code>, or
     *                  <code>Instruction.ARRAY_T_DOUBLE</code>.
     * @return <code>TypeConstants.BOOLEAN</code>,
     *         <code>TypeConstants.BYTE</code>,
     *         <code>TypeConstants.CHAR</code>,
     *         <code>TypeConstants.SHORT</code>,
     *         <code>TypeConstants.INT</code>,
     *         <code>TypeConstants.LONG</code>,
     *         <code>TypeConstants.FLOAT</code>, or
     *         <code>TypeConstants.DOUBLE</code>.
     */
    public static char internalTypeFromArrayType(byte arrayType)
    {
        switch (arrayType)
        {
            case Instruction.ARRAY_T_BOOLEAN: return TypeConstants.BOOLEAN;
            case Instruction.ARRAY_T_CHAR:    return TypeConstants.CHAR;
            case Instruction.ARRAY_T_FLOAT:   return TypeConstants.FLOAT;
            case Instruction.ARRAY_T_DOUBLE:  return TypeConstants.DOUBLE;
            case Instruction.ARRAY_T_BYTE:    return TypeConstants.BYTE;
            case Instruction.ARRAY_T_SHORT:   return TypeConstants.SHORT;
            case Instruction.ARRAY_T_INT:     return TypeConstants.INT;
            case Instruction.ARRAY_T_LONG:    return TypeConstants.LONG;
            default: throw new IllegalArgumentException("Unknown array type ["+arrayType+"]");
        }
    }

    /**
     * Returns the 'newarray' type constant for the given internal primitive
     * type.
     *
     * @param internalType a primitive type
     *                     <code>TypeConstants.BOOLEAN</code>,
     *                     <code>TypeConstants.BYTE</code>,
     *                     <code>TypeConstants.CHAR</code>,
     *                     <code>TypeConstants.SHORT</code>,
     *                     <code>TypeConstants.INT</code>,
     *                     <code>TypeConstants.LONG</code>,
     *                     <code>TypeConstants.FLOAT</code>, or
     *                     <code>TypeConstants.DOUBLE</code>.
     * @return the array type constant corresponding to the given
     *         primitive type;
     *         <code>TypeConstants.BOOLEAN</code>,
     *         <code>TypeConstants.BYTE</code>,
     *         <code>TypeConstants.CHAR</code>,
     *         <code>TypeConstants.SHORT</code>,
     *         <code>TypeConstants.INT</code>,
     *         <code>TypeConstants.LONG</code>,
     *         <code>TypeConstants.FLOAT</code>, or
     *         <code>TypeConstants.DOUBLE</code>.
     * @see #internalTypeFromArrayType(byte)
     */
    public static byte arrayTypeFromInternalType(char internalType)
    {
        switch (internalType)
        {
            case TypeConstants.BOOLEAN: return Instruction.ARRAY_T_BOOLEAN;
            case TypeConstants.BYTE:    return Instruction.ARRAY_T_BYTE;
            case TypeConstants.CHAR:    return Instruction.ARRAY_T_CHAR;
            case TypeConstants.SHORT:   return Instruction.ARRAY_T_SHORT;
            case TypeConstants.INT:     return Instruction.ARRAY_T_INT;
            case TypeConstants.LONG:    return Instruction.ARRAY_T_LONG;
            case TypeConstants.FLOAT:   return Instruction.ARRAY_T_FLOAT;
            case TypeConstants.DOUBLE:  return Instruction.ARRAY_T_DOUBLE;
            default: throw new IllegalArgumentException("Unknown primitive type ["+internalType+"]");
        }
    }
}
