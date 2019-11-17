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

import proguard.classfile.attribute.ExceptionsAttribute;

/**
 * This class can add exceptions to a given exceptions attribute.
 * Exceptions to be added must have been added to the constant pool and filled
 * out beforehand.
 *
 * @author Eric Lafortune
 */
public class ExceptionsAttributeEditor
{
    private ExceptionsAttribute targetExceptionsAttribute;


    /**
     * Creates a new ExceptionsAttributeEditor that will edit exceptions in the
     * given exceptions attribute.
     */
    public ExceptionsAttributeEditor(ExceptionsAttribute targetExceptionsAttribute)
    {
        this.targetExceptionsAttribute = targetExceptionsAttribute;
    }


    /**
     * Adds a given exception to the exceptions attribute.
     */
    public void addException(int exceptionIndex)
    {
        int   exceptionIndexTableLength = targetExceptionsAttribute.u2exceptionIndexTableLength;
        int[] exceptionIndexTable       = targetExceptionsAttribute.u2exceptionIndexTable;

        // Make sure there is enough space for the new exception.
        if (exceptionIndexTable.length <= exceptionIndexTableLength)
        {
            targetExceptionsAttribute.u2exceptionIndexTable = new int[exceptionIndexTableLength+1];
            System.arraycopy(exceptionIndexTable, 0,
                             targetExceptionsAttribute.u2exceptionIndexTable, 0,
                             exceptionIndexTableLength);
            exceptionIndexTable = targetExceptionsAttribute.u2exceptionIndexTable;
        }

        // Add the exception.
        exceptionIndexTable[targetExceptionsAttribute.u2exceptionIndexTableLength++] = exceptionIndex;
    }
}
