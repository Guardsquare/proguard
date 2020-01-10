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
package proguard.classfile.attribute.visitor;

import proguard.classfile.*;
import proguard.classfile.attribute.*;

/**
 * This {@link LineNumberInfoVisitor} remembers the lowest and the highest line
 * numbers that it finds in all the line numbers that it visits. It
 * ignores the sources of the line numbers.
 */
public class LineNumberRangeFinder
implements   LineNumberInfoVisitor
{
    private int     lowestLineNumber  = Integer.MAX_VALUE;
    private int     highestLineNumber = 0;
    private boolean hasSource;


    /**
     * Returns the lowest line number that has been visited so far.
     */
    public int getLowestLineNumber()
    {
        return lowestLineNumber;
    }


    /**
     * Returns the highest line number that has been visited so far.
     */
    public int getHighestLineNumber()
    {
        return highestLineNumber;
    }


    /**
     * Returns whether any of the visited line numbers has a non-null source.
     */
    public boolean hasSource()
    {
        return hasSource;
    }


    // Implementations for LineNumberInfoVisitor.

    public void visitLineNumberInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberInfo lineNumberInfo)
    {
        int lineNumber = lineNumberInfo.u2lineNumber;

        // Remember the lowest line number.
        if (lowestLineNumber > lineNumber)
        {
            lowestLineNumber = lineNumber;
        }

        // Remember the highest line number.
        if (highestLineNumber < lineNumber)
        {
            highestLineNumber = lineNumber;
        }

        if (lineNumberInfo.getSource() != null)
        {
            hasSource = true;
        }
    }
}
