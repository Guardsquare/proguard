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
import proguard.util.ArrayUtil;

/**
 * This class can add exceptions to the exception table of a given code
 * attribute. The exceptions must have been filled out beforehand.
 *
 * @author Eric Lafortune
 */
public class ExceptionInfoEditor
{
    private final CodeAttribute codeAttribute;


    /**
     * Creates a new ExceptionInfoEditor that can add exceptions to the
     * given code attribute.
     */
    public ExceptionInfoEditor(CodeAttribute codeAttribute)
    {
        this.codeAttribute = codeAttribute;
    }


    /**
     * Prepends the given exception to the exception table.
     */
    void prependException(ExceptionInfo exceptionInfo)
    {
        ExceptionInfo[] exceptionTable       = codeAttribute.exceptionTable;
        int             exceptionTableLength = codeAttribute.u2exceptionTableLength;

        int newExceptionTableLength = exceptionTableLength + 1;

        // Is the exception table large enough?
        if (exceptionTable.length < newExceptionTableLength)
        {
            ExceptionInfo[] newExceptionTable =
                new ExceptionInfo[newExceptionTableLength];

            System.arraycopy(exceptionTable, 0,
                             newExceptionTable, 1,
                             exceptionTableLength);
            newExceptionTable[0] = exceptionInfo;

            codeAttribute.exceptionTable = newExceptionTable;
        }
        else
        {
            System.arraycopy(exceptionTable, 0,
                             exceptionTable, 1,
                             exceptionTableLength);
            exceptionTable[0] = exceptionInfo;
        }

        codeAttribute.u2exceptionTableLength = newExceptionTableLength;
    }


    /**
     * Appends the given exception to the exception table.
     */
    void appendException(ExceptionInfo exceptionInfo)
    {
        codeAttribute.exceptionTable =
            ArrayUtil.add(codeAttribute.exceptionTable,
                          codeAttribute.u2exceptionTableLength++,
                          exceptionInfo);
    }
}