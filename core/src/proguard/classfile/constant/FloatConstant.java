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
 * This Constant represents a float constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public class FloatConstant extends Constant
{
    public float f4value;


    /**
     * Creates an uninitialized FloatConstant.
     */
    public FloatConstant()
    {
    }


    /**
     * Creates a new FloatConstant with the given float value.
     */
    public FloatConstant(float value)
    {
        f4value = value;
    }


    /**
     * Returns the float value of this FloatConstant.
     */
    public float getValue()
    {
        return f4value;
    }


    /**
     * Sets the float value of this FloatConstant.
     */
    public void setValue(float value)
    {
        f4value = value;
    }


    // Implementations for Constant.

    public int getTag()
    {
        return Constant.FLOAT;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        constantVisitor.visitFloatConstant(clazz, this);
    }
}
