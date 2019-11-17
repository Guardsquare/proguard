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

import proguard.classfile.attribute.*;

/**
 * This class can add line numbers to a given line number table attribute.
 * Line numbers to be added must have been filled out beforehand.
 *
 * @author Eric Lafortune
 */
public class LineNumberTableAttributeEditor
{
    private LineNumberTableAttribute targetLineNumberTableAttribute;


    /**
     * Creates a new LineNumberTableAttributeEditor that will edit line numbers
     * in the given line number table attribute.
     */
    public LineNumberTableAttributeEditor(LineNumberTableAttribute targetLineNumberTableAttribute)
    {
        this.targetLineNumberTableAttribute = targetLineNumberTableAttribute;
    }


    /**
     * Adds a given line number to the line number table attribute.
     */
    public void addLineNumberInfo(LineNumberInfo lineNumberInfo)
    {
        int              lineNumberTableLength = targetLineNumberTableAttribute.u2lineNumberTableLength;
        LineNumberInfo[] lineNumberTable       = targetLineNumberTableAttribute.lineNumberTable;

        // Make sure there is enough space for the new lineNumberInfo.
        if (lineNumberTable.length <= lineNumberTableLength)
        {
            targetLineNumberTableAttribute.lineNumberTable = new LineNumberInfo[lineNumberTableLength+1];
            System.arraycopy(lineNumberTable, 0,
                             targetLineNumberTableAttribute.lineNumberTable, 0,
                             lineNumberTableLength);
            lineNumberTable = targetLineNumberTableAttribute.lineNumberTable;
        }

        // Add the lineNumberInfo.
        lineNumberTable[targetLineNumberTableAttribute.u2lineNumberTableLength++] = lineNumberInfo;
    }
}