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
import proguard.classfile.visitor.ClassVisitor;

/**
 * This Constant represents a method handle constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public class MethodTypeConstant extends Constant
{
    public int u2descriptorIndex;


    /**
     * An extra field pointing to the java.lang.invoke.MethodType Clazz object.
     * This field is typically filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer
     * ClassReferenceInitializer}</code>..
     */
    public Clazz javaLangInvokeMethodTypeClass;


    /**
     * An extra field pointing to the Clazz objects referenced in the
     * descriptor string. This field is filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer ClassReferenceInitializer}</code>.
     * References to primitive types are ignored.
     */
    public Clazz[] referencedClasses;


    /**
     * Creates an uninitialized MethodTypeConstant.
     */
    public MethodTypeConstant()
    {
    }


    /**
     * Creates a new MethodTypeConstant with the given descriptor index.
     * @param u2descriptorIndex the index of the descriptor in the constant
     *                          pool.
     * @param referencedClasses the classes referenced by the descriptor.
     */
    public MethodTypeConstant(int     u2descriptorIndex,
                              Clazz[] referencedClasses)
    {
        this.u2descriptorIndex = u2descriptorIndex;
        this.referencedClasses = referencedClasses;
    }


    /**
     * Returns the descriptor index.
     */
    public int getDescriptorIndex()
    {
        return u2descriptorIndex;
    }


    /**
     * Returns the type.
     */
    public String getType(Clazz clazz)
    {
        return clazz.getString(u2descriptorIndex);
    }


    /**
     * Lets the Clazz objects referenced in the descriptor string
     * accept the given visitor.
     */
    public void referencedClassesAccept(ClassVisitor classVisitor)
    {
        if (referencedClasses != null)
        {
            for (int index = 0; index < referencedClasses.length; index++)
            {
                if (referencedClasses[index] != null)
                {
                    referencedClasses[index].accept(classVisitor);
                }
            }
        }
    }


    // Implementations for Constant.

    public int getTag()
    {
        return ClassConstants.CONSTANT_MethodType;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        constantVisitor.visitMethodTypeConstant(clazz, this);
    }
}
