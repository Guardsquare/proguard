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
package proguard.classfile.editor;

import proguard.classfile.attribute.*;

/**
 * This class can add local variables to a given local variable type table
 * attribute.
 * Local variable types to be added must have been filled out beforehand.
 *
 * @author Eric Lafortune
 */
public class LocalVariableTypeTableAttributeEditor
{
    private LocalVariableTypeTableAttribute targetLocalVariableTypeTableAttribute;


    /**
     * Creates a new LocalVariableTypeTableAttributeEditor that will edit line numbers
     * in the given line number table attribute.
     */
    public LocalVariableTypeTableAttributeEditor(LocalVariableTypeTableAttribute targetLocalVariableTypeTableAttribute)
    {
        this.targetLocalVariableTypeTableAttribute = targetLocalVariableTypeTableAttribute;
    }


    /**
     * Adds a given line number to the line number table attribute.
     */
    public void addLocalVariableTypeInfo(LocalVariableTypeInfo localVariableTypeInfo)
    {
        int                     localVariableTypeTableLength = targetLocalVariableTypeTableAttribute.u2localVariableTypeTableLength;
        LocalVariableTypeInfo[] localVariableTypeTable       = targetLocalVariableTypeTableAttribute.localVariableTypeTable;

        // Make sure there is enough space for the new localVariableTypeInfo.
        if (localVariableTypeTable.length <= localVariableTypeTableLength)
        {
            targetLocalVariableTypeTableAttribute.localVariableTypeTable = new LocalVariableTypeInfo[localVariableTypeTableLength+1];
            System.arraycopy(localVariableTypeTable, 0,
                             targetLocalVariableTypeTableAttribute.localVariableTypeTable, 0,
                             localVariableTypeTableLength);
            localVariableTypeTable = targetLocalVariableTypeTableAttribute.localVariableTypeTable;
        }

        // Add the localVariableTypeInfo.
        localVariableTypeTable[targetLocalVariableTypeTableAttribute.u2localVariableTypeTableLength++] = localVariableTypeInfo;
    }
}