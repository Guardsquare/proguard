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
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This {@link ConstantVisitor} remaps all possible indices of bootstrap methods
 * of the constants that it visits, based on a given index map.
 *
 * @author Eric Lafortune
 */
public class BootstrapMethodRemapper
extends      SimplifiedVisitor
implements   ConstantVisitor
{
    private int[] bootstrapMethodIndexMap;

    // Ignore (skip) lingering InvokeDynamic constants that
    // refer to removed bootstrap methods.
    private final boolean ignoreDanglingConstants;

    public BootstrapMethodRemapper()
    {
        this(false);
    }

    public BootstrapMethodRemapper(boolean ignoreDanglingConstants)
    {
        this.ignoreDanglingConstants = ignoreDanglingConstants;
    }


    /**
     * Sets the given mapping of old constant pool entry indexes to their new
     * indexes.
     */
    public void setBootstrapMethodIndexMap(int[] bootstrapMethodIndexMap)
    {
        this.bootstrapMethodIndexMap = bootstrapMethodIndexMap;
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitDynamicConstant(Clazz clazz, DynamicConstant dynamicConstant)
    {
        dynamicConstant.u2bootstrapMethodAttributeIndex =
            remapConstantIndex(dynamicConstant.u2bootstrapMethodAttributeIndex);
    }


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        invokeDynamicConstant.u2bootstrapMethodAttributeIndex =
            remapConstantIndex(invokeDynamicConstant.u2bootstrapMethodAttributeIndex);
    }


    // Small utility methods.

    /**
     * Returns the latest bootstrap method index of the entry at the
     * given index.
     */
    private int remapConstantIndex(int constantIndex)
    {
        int remappedConstantIndex = bootstrapMethodIndexMap[constantIndex];
        if (remappedConstantIndex < 0)
        {
            if (ignoreDanglingConstants)
            {
                return constantIndex;
            }
            else
            {
                throw new IllegalArgumentException("Can't remap bootstrap method index ["+constantIndex+"]");
            }
        }

        return remappedConstantIndex;
    }
}
