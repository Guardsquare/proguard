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
package proguard.classfile.constant;

import proguard.classfile.*;
import proguard.classfile.constant.visitor.ConstantVisitor;

/**
 * This Constant represents a name and type constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public class NameAndTypeConstant extends Constant
{
    public int u2nameIndex;
    public int u2descriptorIndex;


    /**
     * Creates an uninitialized NameAndTypeConstant.
     */
    public NameAndTypeConstant()
    {
    }


    /**
     * Creates a new NameAndTypeConstant with the given name and type indices.
     * @param u2nameIndex       the index of the name in the constant pool.
     * @param u2descriptorIndex the index of the descriptor in the constant
     *                          pool.
     */
    public NameAndTypeConstant(int u2nameIndex,
                               int u2descriptorIndex)
    {
        this.u2nameIndex       = u2nameIndex;
        this.u2descriptorIndex = u2descriptorIndex;
    }


    /**
     * Returns the name index.
     */
    protected int getNameIndex()
    {
        return u2nameIndex;
    }

    /**
     * Sets the name index.
     */
    protected void setNameIndex(int index)
    {
        u2nameIndex = index;
    }

    /**
     * Returns the descriptor index.
     */
    protected int getDescriptorIndex()
    {
        return u2descriptorIndex;
    }

    /**
     * Sets the descriptor index.
     */
    protected void setDescriptorIndex(int index)
    {
        u2descriptorIndex = index;
    }

    /**
     * Returns the name.
     */
    public String getName(Clazz clazz)
    {
        return clazz.getString(u2nameIndex);
    }

    /**
     * Returns the type.
     */
    public String getType(Clazz clazz)
    {
        return clazz.getString(u2descriptorIndex);
    }


    // Implementations for Constant.

    public int getTag()
    {
        return ClassConstants.CONSTANT_NameAndType;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        constantVisitor.visitNameAndTypeConstant(clazz, this);
    }
}
