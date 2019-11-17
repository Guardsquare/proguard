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
 * This Attribute represents a module packages attribute.
 *
 * @author Joachim Vandersmissen
 */
public class ModulePackagesAttribute extends Attribute
{
    public int   u2packagesCount;
    public int[] u2packages;


    /**
     * Creates an uninitialized ModulePackagesAttribute.
     */
    public ModulePackagesAttribute()
    {
    }


    /**
     * Creates an initialized ModulePackagesAttribute.
     */
    public ModulePackagesAttribute(int   u2attributeNameIndex,
                                   int   u2packagesCount,
                                   int[] u2packages)
    {
        super(u2attributeNameIndex);
        this.u2packagesCount = u2packagesCount;
        this.u2packages      = u2packages;
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitModulePackagesAttribute(clazz, this);
    }


    /**
     * Applies the given constant pool visitor to all packages.
     */
    public void packagesAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        // Loop over all packages.
        for (int index = 0; index < u2packagesCount; index++)
        {
            clazz.constantPoolEntryAccept(u2packages[index], constantVisitor);
        }
    }
}
