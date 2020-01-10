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
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.util.SimplifiedVisitor;

import java.util.Arrays;

/**
 * This {@link AttributeVisitor} cleans up variable tables in all code attributes that
 * it visits. It trims overlapping local variables. It removes empty local
 * variables and empty local variable tables.
 *
 * @author Eric Lafortune
 */
public class VariableCleaner
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    private LocalVariableTableAttribute     localVariableTableAttribute;
    private LocalVariableTypeTableAttribute localVariableTypeTableAttribute;
    private boolean                         deleteLocalVariableTableAttribute;
    private boolean                         deleteLocalVariableTypeTableAttribute;


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        localVariableTableAttribute           = null;
        localVariableTypeTableAttribute       = null;
        deleteLocalVariableTableAttribute     = false;
        deleteLocalVariableTypeTableAttribute = false;

        // Trim the local variable table and the local variable type table.
        codeAttribute.attributesAccept(clazz, method, this);

        // Finally, still trim the code blocks of the local variable types,
        // based on the code blocks of the local variables, if we have found
        // both. The local variable type table may contain fewer entries,
        // but the JVM preverifier complains if variables and corresponding
        // variable types are inconsistent.
        if (localVariableTableAttribute     != null &&
            localVariableTypeTableAttribute != null)
        {
            trimLocalVariableTypes(localVariableTableAttribute.localVariableTable,
                                   localVariableTableAttribute.u2localVariableTableLength,
                                   localVariableTypeTableAttribute.localVariableTypeTable,
                                   localVariableTypeTableAttribute.u2localVariableTypeTableLength);
        }

        // Delete the local variable table if it ended up empty.
        if (deleteLocalVariableTableAttribute)
        {
            AttributesEditor editor =
                new AttributesEditor((ProgramClass)clazz,
                                     (ProgramMember)method,
                                     codeAttribute,
                                     true);

            editor.deleteAttribute(Attribute.LOCAL_VARIABLE_TABLE);
        }

        // Delete the local variable type table if it ended up empty.
        if (deleteLocalVariableTypeTableAttribute)
        {
            AttributesEditor editor =
                new AttributesEditor((ProgramClass)clazz,
                                     (ProgramMember)method,
                                     codeAttribute,
                                     true);

            editor.deleteAttribute(Attribute.LOCAL_VARIABLE_TYPETable);
        }
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        // Clean up local variables that aren't used.
        localVariableTableAttribute.u2localVariableTableLength =
            removeUnusedLocalVariables(localVariableTableAttribute.localVariableTable,
                                       localVariableTableAttribute.u2localVariableTableLength,
                                       codeAttribute.u2maxLocals);

        // Trim the code blocks of the local variables.
        trimLocalVariables(localVariableTableAttribute.localVariableTable,
                           localVariableTableAttribute.u2localVariableTableLength,
                           codeAttribute.u2maxLocals);

        // Delete the attribute in a moment, if it is empty.
        if (localVariableTableAttribute.u2localVariableTableLength == 0)
        {
            deleteLocalVariableTableAttribute = true;
        }

        // We still need to remember the attribute.
        this.localVariableTableAttribute = localVariableTableAttribute;
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        // Clean up local variable types that aren't used.
        localVariableTypeTableAttribute.u2localVariableTypeTableLength =
            removeUnusedLocalVariableTypes(localVariableTypeTableAttribute.localVariableTypeTable,
                                           localVariableTypeTableAttribute.u2localVariableTypeTableLength,
                                           codeAttribute.u2maxLocals);

        // Trim the code blocks of the local variable types.
        trimLocalVariableTypes(localVariableTypeTableAttribute.localVariableTypeTable,
                               localVariableTypeTableAttribute.u2localVariableTypeTableLength,
                               codeAttribute.u2maxLocals);

        // Delete the attribute in a moment, if it is empty.
        if (localVariableTypeTableAttribute.u2localVariableTypeTableLength == 0)
        {
            deleteLocalVariableTypeTableAttribute = true;
        }

        // We still need to remember the attribute.
        this.localVariableTypeTableAttribute = localVariableTypeTableAttribute;
    }


    // Small utility methods.

    /**
     * Returns the given list of local variables, without the ones that aren't
     * used.
     */
    private int removeUnusedLocalVariables(LocalVariableInfo[] localVariableInfos,
                                           int                 localVariableInfoCount,
                                           int                 maxLocals)
    {
        // Overwrite all empty local variable entries.
        // Do keep parameter entries.
        int newIndex = 0;
        for (int index = 0; index < localVariableInfoCount; index++)
        {
            LocalVariableInfo localVariableInfo = localVariableInfos[index];

            if (localVariableInfo.u2index >= 0        &&
                localVariableInfo.u2index < maxLocals &&
                (localVariableInfo.u2startPC == 0 ||
                 localVariableInfo.u2length > 0))
            {
                localVariableInfos[newIndex++] = localVariableInfos[index];
            }
        }

        // Clean up any remaining array elements.
        Arrays.fill(localVariableInfos, newIndex, localVariableInfoCount, null);

        return newIndex;
    }


    /**
     * Returns the given list of local variable types, without the ones that
     * aren't used.
     */
    private int removeUnusedLocalVariableTypes(LocalVariableTypeInfo[] localVariableTypeInfos,
                                               int                     localVariableTypeInfoCount,
                                               int                     maxLocals)
    {
        // Overwrite all empty local variable type entries.
        // Do keep parameter entries.
        int newIndex = 0;
        for (int index = 0; index < localVariableTypeInfoCount; index++)
        {
            LocalVariableTypeInfo localVariableTypeInfo = localVariableTypeInfos[index];

            if (localVariableTypeInfo.u2index >= 0        &&
                localVariableTypeInfo.u2index < maxLocals &&
                (localVariableTypeInfo.u2startPC == 0 ||
                 localVariableTypeInfo.u2length > 0))
            {
                localVariableTypeInfos[newIndex++] = localVariableTypeInfos[index];
            }
        }

        // Clean up any remaining array elements.
        Arrays.fill(localVariableTypeInfos,  newIndex, localVariableTypeInfoCount, null);

        return newIndex;
    }


    /**
     * Sorts the given list of local variables and trims their code blocks
     * when necessary. The block is trimmed at the end, which is a bit
     * arbitrary.
     */
    private void trimLocalVariables(LocalVariableInfo[] localVariableInfos,
                                    int                 localVariableInfoCount,
                                    int                 maxLocals)
    {
        // Sort the local variable entries.
        Arrays.sort(localVariableInfos, 0, localVariableInfoCount);

        int[] startPCs = createMaxArray(maxLocals);

        // Trim the local variable entries, starting at the last one.
        for (int index = localVariableInfoCount-1; index >= 0; index--)
        {
            LocalVariableInfo localVariableInfo = localVariableInfos[index];

            // Make sure the variable's code block doesn't overlap with the
            // next one for the same variable.
            int maxLength = startPCs[localVariableInfo.u2index] -
                            localVariableInfo.u2startPC;

            if (localVariableInfo.u2length > maxLength)
            {
                localVariableInfo.u2length = maxLength;
            }

            startPCs[localVariableInfo.u2index] = localVariableInfo.u2startPC;
        }
    }


    /**
     * Sorts the given list of local variable types and trims their code blocks
     * when necessary. The block is trimmed at the end, which is a bit
     * arbitrary.
     */
    private void trimLocalVariableTypes(LocalVariableTypeInfo[] localVariableTypeInfos,
                                        int                     localVariableTypeInfoCount,
                                        int                     maxLocals)
    {
        // Sort the local variable entries.
        Arrays.sort(localVariableTypeInfos, 0, localVariableTypeInfoCount);

        int[] startPCs = createMaxArray(maxLocals);

        // Trim the local variable type entries, starting at the last one.
        for (int index = localVariableTypeInfoCount-1; index >= 0; index--)
        {
            LocalVariableTypeInfo localVariableTypeInfo = localVariableTypeInfos[index];

            // Make sure the variable type's code block doesn't overlap with
            // the next one for the same variable.
            int maxLength = startPCs[localVariableTypeInfo.u2index] -
                            localVariableTypeInfo.u2startPC;

            if (localVariableTypeInfo.u2length > maxLength)
            {
                localVariableTypeInfo.u2length = maxLength;
            }

            startPCs[localVariableTypeInfo.u2index] = localVariableTypeInfo.u2startPC;
        }
    }


    /**
     * Trims the code blocks of the given list of local variable types, based
     * on the given list of local variables.
     */
    private void trimLocalVariableTypes(LocalVariableInfo[]     localVariableInfos,
                                        int                     localVariableInfoCount,
                                        LocalVariableTypeInfo[] localVariableTypeInfos,
                                        int                     localVariableTypeInfoCount)
    {
        int typeIndex = 0;

        // Go over the sorted list of local variables.
        for (int index = 0;
             index     < localVariableInfoCount &&
             typeIndex < localVariableTypeInfoCount;
             index++)
        {
            LocalVariableInfo     localVariableInfo     = localVariableInfos[index];
            LocalVariableTypeInfo localVariableTypeInfo = localVariableTypeInfos[typeIndex];

            // Do we have a corresponding variable type?
            if (localVariableTypeInfo.u2index   == localVariableInfo.u2index &&
                localVariableTypeInfo.u2startPC == localVariableInfo.u2startPC)
            {
                // Just copy the length.
                localVariableTypeInfo.u2length = localVariableInfo.u2length;

                // Continue with the next local variable type.
                typeIndex++;
            }
        }
    }


    /**
     * Creates an integer array of the given length, initialized with
     * Integer.MAX_VALUE.
     */
    private int[] createMaxArray(int length)
    {
        int[] startPCs = new int[length];
        for (int index = 0; index < length; index++)
        {
            startPCs[index] = Integer.MAX_VALUE;
        }
        return startPCs;
    }
}