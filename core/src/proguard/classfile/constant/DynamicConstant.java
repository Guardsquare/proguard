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
import proguard.classfile.constant.visitor.*;
import proguard.classfile.visitor.ClassVisitor;

/**
 * This Constant represents a dynamic constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public class DynamicConstant extends Constant
{
    public int u2bootstrapMethodAttributeIndex;
    public int u2nameAndTypeIndex;

    /**
     * An extra field pointing to the Clazz objects referenced in the
     * descriptor string. This field is filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer ClassReferenceInitializer}</code>.
     * References to primitive types are ignored.
     */
    public Clazz[] referencedClasses;


    /**
     * Creates an uninitialized InvokeDynamicConstant.
     */
    public DynamicConstant()
    {
    }


    /**
     * Creates a new InvokeDynamicConstant with the given bootstrap method
     * and name-and-type indices.
     * @param u2bootstrapMethodAttributeIndex the index of the bootstrap method
     *                                        entry in the bootstrap methods
     *                                        attribute.
     * @param u2nameAndTypeIndex              the index of the name and type
     *                                        entry in the constant pool.
     * @param referencedClasses               the classes referenced by the
     *                                        type.
     */
    public DynamicConstant(int     u2bootstrapMethodAttributeIndex,
                           int     u2nameAndTypeIndex,
                           Clazz[] referencedClasses)
    {
        this.u2bootstrapMethodAttributeIndex = u2bootstrapMethodAttributeIndex;
        this.u2nameAndTypeIndex              = u2nameAndTypeIndex;
        this.referencedClasses               = referencedClasses;
    }


    /**
     * Returns the index of the bootstrap method in the bootstrap methods
     * attribute of the class.
     */
    public int getBootstrapMethodAttributeIndex()
    {
        return u2bootstrapMethodAttributeIndex;
    }

    /**
     * Returns the name-and-type index.
     */
    public int getNameAndTypeIndex()
    {
        return u2nameAndTypeIndex;
    }

    /**
     * Returns the method name.
     */
    public String getName(Clazz clazz)
    {
        return clazz.getName(u2nameAndTypeIndex);
    }

    /**
     * Returns the method type.
     */
    public String getType(Clazz clazz)
    {
        return clazz.getType(u2nameAndTypeIndex);
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


    /**
     * Lets the bootstrap method handle constant accept the given visitor.
     */
    public void bootstrapMethodHandleAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        new BootstrapMethodHandleTraveler(constantVisitor).visitDynamicConstant(clazz, this);
    }


    // Implementations for Constant.

    public int getTag()
    {
        return ClassConstants.CONSTANT_Dynamic;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        constantVisitor.visitDynamicConstant(clazz, this);
    }
}
