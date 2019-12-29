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
 * This AttributeVisitor accumulates specified changes to local variables, and
 * then applies these accumulated changes to the code attributes that it visits.
 *
 * @author Eric  Lafortune
 */
public class VariableEditor
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    private boolean   modified;

    private boolean[] deleted     = new boolean[ClassEstimates.TYPICAL_VARIABLES_SIZE];
    private int[]     variableMap = new int[ClassEstimates.TYPICAL_VARIABLES_SIZE];

    private final VariableRemapper variableRemapper = new VariableRemapper();


    /**
     * Resets the accumulated code changes.
     * @param maxLocals the length of the local variable frame that will be
     *                  edited next.
     */
    public void reset(int maxLocals)
    {
        // Try to reuse the previous array.
        if (deleted.length < maxLocals)
        {
            // Create a new array.
            deleted = new boolean[maxLocals];
        }
        else
        {
            // Reset the array.
            Arrays.fill(deleted, 0, maxLocals, false);
        }

        modified = false;
    }


    /**
     * Remembers to delete the given variable.
     * @param variableIndex the index of the variable to be deleted.
     */
    public void deleteVariable(int variableIndex)
    {
        deleted[variableIndex] = true;

        modified = true;
    }


    /**
     * Returns whether the given variable at the given offset will be deleted.
     */
    public boolean isDeleted(int instructionOffset)
    {
        return deleted[instructionOffset];
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // Avoid doing any work if nothing is changing anyway.
        if (!modified)
        {
            return;
        }

        int oldMaxLocals = codeAttribute.u2maxLocals;

        // Make sure there is a sufficiently large variable map.
        if (variableMap.length < oldMaxLocals)
        {
            variableMap = new int[oldMaxLocals];
        }

        // Fill out the variable map.
        int newVariableIndex = 0;
        for (int oldVariableIndex = 0; oldVariableIndex < oldMaxLocals; oldVariableIndex++)
        {
            variableMap[oldVariableIndex] = deleted[oldVariableIndex] ?
                -1 : newVariableIndex++;
        }

        // Set the map.
        variableRemapper.setVariableMap(variableMap);

        // Remap the variables.
        variableRemapper.visitCodeAttribute(clazz, method, codeAttribute);

        // Update the length of local variable frame.
        codeAttribute.u2maxLocals = newVariableIndex;
    }
}
