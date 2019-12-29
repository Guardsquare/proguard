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
 * This Constant represents a double constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public class DoubleConstant extends Constant
{
    public double f8value;


    /**
     * Creates an uninitialized DoubleConstant.
     */
    public DoubleConstant()
    {
    }


    /**
     * Creates a new DoubleConstant with the given double value.
     */
    public DoubleConstant(double value)
    {
        f8value = value;
    }


    /**
     * Returns the double value of this DoubleConstant.
     */
    public double getValue()
    {
        return f8value;
    }


    /**
     * Sets the double value of this DoubleConstant.
     */
    public void setValue(double value)
    {
        f8value = value;
    }


    // Implementations for Constant.

    public int getTag()
    {
        return Constant.DOUBLE;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        constantVisitor.visitDoubleConstant(clazz, this);
    }
}
