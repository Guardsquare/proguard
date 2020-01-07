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
 * Representation of a parameter, as defined in a method parameters
 * attribute.
 *
 * @author Eric Lafortune
 */
public class ParameterInfo extends SimpleProcessable
{
    public int u2nameIndex;
    public int u2accessFlags;


    /**
     * Creates an uninitialized ParameterInfo.
     */
    public ParameterInfo()
    {
    }


    /**
     * Creates an initialized ParameterInfo.
     */
    public ParameterInfo(int u2nameIndex,
                         int u2accessFlags)
    {
        this.u2nameIndex   = u2nameIndex;
        this.u2accessFlags = u2accessFlags;
    }


    /**
     * Returns the parameter name.
     */
    public String getName(Clazz clazz)
    {
        return clazz.getString(u2nameIndex);
    }


    /**
     * Applies the given constant pool visitor to the Utf8 constant that
     * represents the name of the parameter, if any.
     */
    public void nameConstantAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        if (u2nameIndex != 0)
        {
            clazz.constantPoolEntryAccept(u2nameIndex, constantVisitor);
        }
    }
}
