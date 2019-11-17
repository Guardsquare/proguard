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
 * This Constant represents a method reference constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public class MethodrefConstant extends RefConstant
{
    /**
     * Creates an uninitialized MethodrefConstant.
     */
    public MethodrefConstant()
    {
    }


    /**
     * Creates a new MethodrefConstant with the given name and type indices.
     * @param u2classIndex         the index of the class in the constant pool.
     * @param u2nameAndTypeIndex   the index of the name and type entry in the constant pool.
     * @param referencedClass      the referenced class.
     * @param referencedMember     the referenced member info.
     */
    public MethodrefConstant(int    u2classIndex,
                             int    u2nameAndTypeIndex,
                             Clazz  referencedClass,
                             Member referencedMember)
    {
        this.u2classIndex       = u2classIndex;
        this.u2nameAndTypeIndex = u2nameAndTypeIndex;
        this.referencedClass    = referencedClass;
        this.referencedMember   = referencedMember;
    }


    // Implementations for Constant.

    public int getTag()
    {
        return ClassConstants.CONSTANT_Methodref;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        constantVisitor.visitMethodrefConstant(clazz, this);
    }
}
