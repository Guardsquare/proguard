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
package proguard.classfile.attribute;

import proguard.classfile.*;
import proguard.classfile.attribute.visitor.*;

/**
 * This Attribute represents a line number table attribute.
 *
 * @author Eric Lafortune
 */
public class LineNumberTableAttribute extends Attribute
{
    public int              u2lineNumberTableLength;
    public LineNumberInfo[] lineNumberTable;


    /**
     * Creates an uninitialized LineNumberTableAttribute.
     */
    public LineNumberTableAttribute()
    {
    }


    /**
     * Creates an initialized LineNumberTableAttribute.
     */
    public LineNumberTableAttribute(int              u2attributeNameIndex,
                                    int              u2lineNumberTableLength,
                                    LineNumberInfo[] lineNumberTable)
    {
        super(u2attributeNameIndex);

        this.u2lineNumberTableLength = u2lineNumberTableLength;
        this.lineNumberTable         = lineNumberTable;
    }


    /**
     * Returns the line number corresponding to the given byte code program
     * counter.
     */
    public int getLineNumber(int pc)
    {
        LineNumberInfo info = getLineNumberInfo(pc);

        return info == null ? 0 : info.u2lineNumber;
    }


    /**
     * Returns the source corresponding to the given byte code program
     * counter.
     */
    public String getSource(int pc)
    {
        LineNumberInfo info = getLineNumberInfo(pc);

        return info == null ? null : info.getSource();
    }


    /**
     * Returns the line number info corresponding to the given byte code
     * program counter.
     */
    public LineNumberInfo getLineNumberInfo(int pc)
    {
        for (int index = u2lineNumberTableLength-1; index >= 0; index--)
        {
            LineNumberInfo info = lineNumberTable[index];
            if (pc >= info.u2startPC)
            {
                return info;
            }
        }

        return u2lineNumberTableLength > 0 ?
            lineNumberTable[0] :
            null;
    }


    /**
     * Returns the lowest line number with the default null source,
     * or 0 if there aren't any such line numbers.
     */
    public int getLowestLineNumber()
    {
        int lowestLineNumber = Integer.MAX_VALUE;

        for (int index = 0; index < u2lineNumberTableLength; index++)
        {
            LineNumberInfo info = lineNumberTable[index];
            if (info.getSource() == null)
            {
                int lineNumber = info.u2lineNumber;
                if (lineNumber < lowestLineNumber)
                {
                    lowestLineNumber = lineNumber;
                }
            }
        }

        return lowestLineNumber == Integer.MAX_VALUE ? 0 : lowestLineNumber;
    }


    /**
     * Returns the highest line number with the default null source,
     * or 0 if there aren't any such line numbers.
     */
    public int getHighestLineNumber()
    {
        int highestLineNumber = 0;

        for (int index = 0; index < u2lineNumberTableLength; index++)
        {
            LineNumberInfo info = lineNumberTable[index];
            if (info.getSource() == null)
            {
                int lineNumber = info.u2lineNumber;
                if (lineNumber > highestLineNumber)
                {
                    highestLineNumber = lineNumber;
                }
            }
        }

        return highestLineNumber;
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitLineNumberTableAttribute(clazz, method, codeAttribute, this);
    }


    /**
     * Applies the given visitor to all line numbers.
     */
    public void lineNumbersAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberInfoVisitor lineNumberInfoVisitor)
    {
        for (int index = 0; index < u2lineNumberTableLength; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of LineNumberInfo.
            lineNumberInfoVisitor.visitLineNumberInfo(clazz, method, codeAttribute, lineNumberTable[index]);
        }
    }
}
