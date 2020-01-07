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
import proguard.util.SimpleProcessable;

/**
 * Representation of a Provides entry in a Module attribute.
 *
 * @author Joachim Vandersmissen
 */
public class ProvidesInfo extends SimpleProcessable
{
    public int   u2providesIndex;
    public int   u2providesWithCount;
    public int[] u2providesWithIndex;


    /**
     * Creates an uninitialized ProvidesInfo.
     */
    public ProvidesInfo()
    {
    }


    /**
     * Creates an initialized ProvidesInfo.
     */
    public ProvidesInfo(int   u2providesIndex,
                        int   u2providesWithCount,
                        int[] u2providesWithIndex)
    {
        this.u2providesIndex     = u2providesIndex;
        this.u2providesWithCount = u2providesWithCount;
        this.u2providesWithIndex = u2providesWithIndex;
    }


    /**
     * Applies the given constant pool visitor to the class constant of the
     * provides, if any.
     */
    public void providesAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        if (u2providesIndex != 0)
        {
            clazz.constantPoolEntryAccept(u2providesIndex, constantVisitor);
        }
    }


    /**
     * Applies the given constant pool visitor to all with entries.
     */
    public void withAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        // Loop over all u2providesWithIndex entries.
        for (int index = 0; index < u2providesWithCount; index++)
        {
            clazz.constantPoolEntryAccept(u2providesWithIndex[index], constantVisitor);
        }
    }
}
