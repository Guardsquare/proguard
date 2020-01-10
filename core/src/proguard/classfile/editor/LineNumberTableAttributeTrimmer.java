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
 * This {@link AttributeVisitor} trims the line number table attributes that it visits.
 *
 * @author Eric Lafortune
 */
public class LineNumberTableAttributeTrimmer
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitLineNumberTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute lineNumberTableAttribute)
    {
        LineNumberInfo[] lineNumberTable       = lineNumberTableAttribute.lineNumberTable;
        int              lineNumberTableLength = lineNumberTableAttribute.u2lineNumberTableLength;

        // Overwrite all empty line number entries.
        int newIndex = 0;
        for (int index = 0; index < lineNumberTableLength; index++)
        {
            LineNumberInfo lineNumberInfo = lineNumberTable[index];

            int startPC    = lineNumberInfo.u2startPC;
            int lineNumber = lineNumberInfo.u2lineNumber;

            // The offset must lie inside the code.
            // The offset must be smaller than the next one.
            // The line number should be different from the previous one.
            if (startPC < codeAttribute.u4codeLength             &&

                (index == lineNumberTableLength - 1 ||
                 startPC < lineNumberTable[index + 1].u2startPC) &&

                (index == 0 ||
                 lineNumber != lineNumberTable[index - 1].u2lineNumber))
            {
                lineNumberTable[newIndex++] = lineNumberInfo;
            }
        }

        // Clear the unused array entries.
        Arrays.fill(lineNumberTable, newIndex, lineNumberTableAttribute.u2lineNumberTableLength, null);

        lineNumberTableAttribute.u2lineNumberTableLength = newIndex;
    }
}
