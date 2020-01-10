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
package proguard.classfile.visitor;

import proguard.classfile.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.util.ArrayUtil;


/**
 * This {@link ConstantVisitor} delegates all visits to each {@link ConstantVisitor} in a given list.
 *
 * @author Johan Leys
 */
public class MultiConstantVisitor
extends      SimplifiedVisitor
implements   ConstantVisitor
{
    private ConstantVisitor[] constantVisitors;
    private int               constantVisitorCount;


    public MultiConstantVisitor()
    {
        this.constantVisitors = new ConstantVisitor[16];
    }


    public MultiConstantVisitor(ConstantVisitor... constantVisitors)
    {
        this.constantVisitors     = constantVisitors;
        this.constantVisitorCount = this.constantVisitors.length;
    }


    public void addClassVisitor(ConstantVisitor constantVisitor)
    {
        constantVisitors =
            ArrayUtil.add(constantVisitors,
                          constantVisitorCount++,
                          constantVisitor);
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant)
    {
        for (int index = 0; index < constantVisitorCount; index++)
        {
            constant.accept(clazz, constantVisitors[index]);
        }
    }
}
