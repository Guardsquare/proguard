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

import proguard.classfile.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.util.SimpleVisitorAccepter;

/**
 * Representation of a Requires entry in a Module attribute.
 *
 * @author Joachim Vandersmissen
 */
public class RequiresInfo extends SimpleVisitorAccepter
{
    public int u2requiresIndex;
    public int u2requiresFlags;
    public int u2requiresVersionIndex;


    /**
     * Creates an uninitialized RequiresInfo.
     */
    public RequiresInfo()
    {
    }


    /**
     * Creates an uninitialized RequiresInfo.
     */
    public RequiresInfo(int u2requiresIndex,
                        int u2requiresFlags,
                        int u2requiresVersionIndex)
    {
        this.u2requiresIndex        = u2requiresIndex;
        this.u2requiresFlags        = u2requiresFlags;
        this.u2requiresVersionIndex = u2requiresVersionIndex;
    }


    /**
     * Applies the given constant pool visitor to the module constant of the
     * module, if any.
     */
    public void moduleAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        if (u2requiresIndex != 0)
        {
            clazz.constantPoolEntryAccept(u2requiresIndex, constantVisitor);
        }
    }

    /**
     * Applies the given constant pool visitor to the Utf8 constant of the
     * version, if any.
     */
    public void versionAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        if (u2requiresVersionIndex != 0)
        {
            clazz.constantPoolEntryAccept(u2requiresVersionIndex, constantVisitor);
        }
    }
}
