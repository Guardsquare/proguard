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
import proguard.classfile.visitor.MemberVisitor;

/**
 * This {@link Constant} represents a method reference constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public abstract class AnyMethodrefConstant extends RefConstant
{
    /**
     * An extra field optionally pointing to the referenced Method object.
     * This field is typically filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer
     * ClassReferenceInitializer}</code>.
     */
    public Method referencedMethod;


    /**
     * Creates an uninitialized AnyMethodrefConstant.
     */
    public AnyMethodrefConstant()
    {
    }


    /**
     * Creates a new AnyMethodrefConstant with the given name and type indices.
     * @param u2classIndex       the index of the class in the constant pool.
     * @param u2nameAndTypeIndex the index of the name and type entry in the constant pool.
     * @param referencedClass    the referenced class.
     * @param referencedMethod   the referenced method.
     */
    public AnyMethodrefConstant(int    u2classIndex,
                                int    u2nameAndTypeIndex,
                                Clazz  referencedClass,
                                Method referencedMethod)
    {
        this.u2classIndex       = u2classIndex;
        this.u2nameAndTypeIndex = u2nameAndTypeIndex;
        this.referencedClass    = referencedClass;
        this.referencedMethod   = referencedMethod;
    }


    /**
     * Lets the referenced class method accept the given visitor.
     */
    public void referencedMethodAccept(MemberVisitor memberVisitor)
    {
        if (referencedMethod != null)
        {
            referencedMethod.accept(referencedClass,
                                    memberVisitor);
        }
    }


    // Implementations for RefConstant.

    /**
     * Lets the referenced class member accept the given visitor.
     */
    public void referencedMemberAccept(MemberVisitor memberVisitor)
    {
        if (referencedMethod != null)
        {
            referencedMethod.accept(referencedClass,
                                    memberVisitor);
        }
    }
}
