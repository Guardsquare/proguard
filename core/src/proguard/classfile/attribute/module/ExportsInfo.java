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
 * Representation of a Exports entry in a Module attribute.
 *
 * @author Joachim Vandersmissen
 */
public class ExportsInfo extends SimpleProcessable
{
    public int   u2exportsIndex;
    public int   u2exportsFlags;
    public int   u2exportsToCount;
    public int[] u2exportsToIndex;


    /**
     * Creates an uninitialized ExportsInfo.
     */
    public ExportsInfo()
    {
    }


    /**
     * Creates an initialized ExportsInfo.
     */
    public ExportsInfo(int   u2exportsIndex,
                       int   u2exportsFlags,
                       int   u2exportsToCount,
                       int[] u2exportsToIndex)
    {
        this.u2exportsIndex   = u2exportsIndex;
        this.u2exportsFlags   = u2exportsFlags;
        this.u2exportsToCount = u2exportsToCount;
        this.u2exportsToIndex = u2exportsToIndex;
    }


    /**
     * Applies the given constant pool visitor to the package constant of the
     * package, if any.
     */
    public void packageAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        if (u2exportsIndex != 0)
        {
            clazz.constantPoolEntryAccept(u2exportsIndex, constantVisitor);
        }
    }


    /**
     * Applies the given constant pool visitor to all exportsToIndex.
     */
    public void exportsToIndexAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        // Loop over all exportsToIndex.
        for (int index = 0; index < u2exportsToCount; index++)
        {
            clazz.constantPoolEntryAccept(u2exportsToIndex[index], constantVisitor);
        }
    }
}
