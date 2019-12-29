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
import proguard.util.VisitorAccepter;

/**
 * Representation of a bootstrap method.
 *
 * @author Eric Lafortune
 */
public class BootstrapMethodInfo implements VisitorAccepter
{
    public static final int FLAG_BRIDGES      = 4;
    public static final int FLAG_MARKERS      = 2;
    public static final int FLAG_SERIALIZABLE = 1;


    public int   u2methodHandleIndex;
    public int   u2methodArgumentCount;
    public int[] u2methodArguments;

    /**
     * An extra field in which visitors can store information.
     */
    public Object visitorInfo;


    /**
     * Creates an uninitialized BootstrapMethodInfo.
     */
    public BootstrapMethodInfo()
    {
    }


    /**
     * Creates an initialized BootstrapMethodInfo.
     */
    public BootstrapMethodInfo(int   u2methodHandleIndex,
                               int   u2methodArgumentCount,
                               int[] u2methodArguments)
    {
        this.u2methodHandleIndex   = u2methodHandleIndex;
        this.u2methodArgumentCount = u2methodArgumentCount;
        this.u2methodArguments     = u2methodArguments;
    }


    /**
     * Applies the given constant pool visitor to the method handle of the
     * bootstrap method.
     */
    public void methodHandleAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        clazz.constantPoolEntryAccept(u2methodHandleIndex,
                                      constantVisitor);
    }


    /**
     * Applies the given constant pool visitor to the argument constants of the
     * bootstrap method.
     */
    public void methodArgumentsAccept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        for (int index = 0; index < u2methodArgumentCount; index++)
        {
            clazz.constantPoolEntryAccept(u2methodArguments[index],
                                          constantVisitor);
        }
    }


    // Implementations for VisitorAccepter.

    public Object getVisitorInfo()
    {
        return visitorInfo;
    }

    public void setVisitorInfo(Object visitorInfo)
    {
        this.visitorInfo = visitorInfo;
    }
}
