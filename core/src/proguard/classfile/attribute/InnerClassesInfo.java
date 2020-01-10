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
package proguard.classfile.attribute;

import proguard.classfile.Clazz;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.util.SimpleProcessable;

/**
 * Representation of an inner classes table entry.
 *
 * @author Eric Lafortune
 */
public class InnerClassesInfo extends SimpleProcessable
{
    public int u2innerClassIndex;
    public int u2outerClassIndex;
    public int u2innerNameIndex;
    public int u2innerClassAccessFlags;


    /**
     * Applies the given constant pool visitor to the class constant of the
     * inner class, if any.
     */
    public void innerClassConstantAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        if (u2innerClassIndex != 0)
        {
            clazz.constantPoolEntryAccept(u2innerClassIndex, constantVisitor);
        }
    }


    /**
     * Applies the given constant pool visitor to the class constant of the
     * outer class, if any.
     */
    public void outerClassConstantAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        if (u2outerClassIndex != 0)
        {
            clazz.constantPoolEntryAccept(u2outerClassIndex, constantVisitor);
        }
    }


    /**
     * Applies the given constant pool visitor to the Utf8 constant of the
     * inner name, if any.
     */
    public void innerNameConstantAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        if (u2innerNameIndex != 0)
        {
            clazz.constantPoolEntryAccept(u2innerNameIndex, constantVisitor);
        }
    }
}
