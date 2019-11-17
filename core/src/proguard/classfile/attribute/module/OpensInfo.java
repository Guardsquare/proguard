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
 * Representation of a Opens entry in a Module attribute.
 *
 * @author Joachim Vandersmissen
 */
public class OpensInfo extends SimpleVisitorAccepter
{
    public int   u2opensIndex;
    public int   u2opensFlags;
    public int   u2opensToCount;
    public int[] u2opensToIndex;


    /**
     * Creates an uninitialized OpensInfo.
     */
    public OpensInfo()
    {
    }


    /**
     * Creates an initialized OpensInfo.
     */
    public OpensInfo(int   u2opensIndex,
                     int   u2opensFlags,
                     int   u2opensToCount,
                     int[] u2opensToIndex)
    {
        this.u2opensIndex   = u2opensIndex;
        this.u2opensFlags   = u2opensFlags;
        this.u2opensToCount = u2opensToCount;
        this.u2opensToIndex = u2opensToIndex;
    }


    /**
     * Applies the given constant pool visitor to the package constant of the
     * package, if any.
     */
    public void packageAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        if (u2opensIndex != 0)
        {
            clazz.constantPoolEntryAccept(u2opensIndex, constantVisitor);
        }
    }


    /**
     * Applies the given constant pool visitor to all targets.
     */
    public void targetsAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        // Loop over all targets.
        for (int index = 0; index < u2opensToCount; index++)
        {
            clazz.constantPoolEntryAccept(u2opensToIndex[index], constantVisitor);
        }
    }
}
