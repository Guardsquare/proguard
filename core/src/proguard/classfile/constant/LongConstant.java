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

/**
 * This {@link Constant} represents a long constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public class LongConstant extends Constant
{
    public long u8value;


    /**
     * Creates an uninitialized LongConstant.
     */
    public LongConstant()
    {
    }


    /**
     * Creates a new LongConstant with the given long value.
     */
    public LongConstant(long value)
    {
        u8value = value;
    }


    /**
     * Returns the long value of this LongConstant.
     */
    public long getValue()
    {
        return u8value;
    }


    /**
     * Sets the long value of this LongConstant.
     */
    public void setValue(long value)
    {
        u8value = value;
    }


    // Implementations for Constant.

    public int getTag()
    {
        return Constant.LONG;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        constantVisitor.visitLongConstant(clazz, this);
    }
}
