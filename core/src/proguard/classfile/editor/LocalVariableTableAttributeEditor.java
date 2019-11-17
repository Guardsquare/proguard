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
 * This class can add local variables to a given local variable table attribute.
 * Local variables to be added must have been filled out beforehand.
 *
 * @author Eric Lafortune
 */
public class LocalVariableTableAttributeEditor
{
    private final LocalVariableTableAttribute targetLocalVariableTableAttribute;


    /**
     * Creates a new LocalVariableTableAttributeEditor that will edit local
     * variables in the given local variable table attribute.
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
        targetLocalVariableTableAttribute.localVariableTable =
            ArrayUtil.add(targetLocalVariableTableAttribute.localVariableTable,
                          targetLocalVariableTableAttribute.u2localVariableTableLength++,
                          localVariableInfo);
    }
}