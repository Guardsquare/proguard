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
 * This class can add local variables to a given local variable table attribute.
 * Local variables to be added must have been filled out beforehand.
 *
 * @author Eric Lafortune
 */
public class LocalVariableTableAttributeEditor
{
    private LocalVariableTableAttribute targetLocalVariableTableAttribute;


    /**
     * Creates a new LocalVariableTableAttributeEditor that will edit line numbers
     * in the given line number table attribute.
     */
    public LocalVariableTableAttributeEditor(LocalVariableTableAttribute targetLocalVariableTableAttribute)
    {
        this.targetLocalVariableTableAttribute = targetLocalVariableTableAttribute;
    }


    /**
     * Adds a given line number to the line number table attribute.
     */
    public void addLocalVariableInfo(LocalVariableInfo localVariableInfo)
    {
        int                 localVariableTableLength = targetLocalVariableTableAttribute.u2localVariableTableLength;
        LocalVariableInfo[] localVariableTable       = targetLocalVariableTableAttribute.localVariableTable;

        // Make sure there is enough space for the new localVariableInfo.
        if (localVariableTable.length <= localVariableTableLength)
        {
            targetLocalVariableTableAttribute.localVariableTable = new LocalVariableInfo[localVariableTableLength+1];
            System.arraycopy(localVariableTable, 0,
                             targetLocalVariableTableAttribute.localVariableTable, 0,
                             localVariableTableLength);
            localVariableTable = targetLocalVariableTableAttribute.localVariableTable;
        }

        // Add the localVariableInfo.
        localVariableTable[targetLocalVariableTableAttribute.u2localVariableTableLength++] = localVariableInfo;
    }
}