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
package proguard.classfile.attribute.module;

import proguard.classfile.Clazz;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.visitor.ConstantVisitor;

/**
 * This {@link Attribute} represents a main class attribute.
 *
 * @author Joachim Vandersmissen
 */
public class ModuleMainClassAttribute extends Attribute
{
    public int u2mainClass;


    /**
     * Creates an uninitialized ModuleMainClassAttribute.
     */
    public ModuleMainClassAttribute()
    {
    }


    /**
     * Creates an initialized ModuleMainClassAttribute.
     */
    public ModuleMainClassAttribute(int u2attributeNameIndex, int u2mainClass)
    {
        super(u2attributeNameIndex);
        this.u2mainClass = u2mainClass;
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitModuleMainClassAttribute(clazz, this);
    }


    /**
     * Applies the given constant pool visitor to the class constant of the
     * main class, if any.
     */
    public void mainClassAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        if (u2mainClass != 0)
        {
            clazz.constantPoolEntryAccept(u2mainClass, constantVisitor);
        }
    }
}
