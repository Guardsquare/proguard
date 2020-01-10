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
package proguard.classfile.constant.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.constant.*;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This {@link ConstantVisitor} travels from any method handle constants that it visits
 * to their methodref constants, and applies a given constant visitor.
 *
 * @author Eric Lafortune
 */
public class MethodrefTraveler
extends      SimplifiedVisitor
implements   ConstantVisitor
{
    private ConstantVisitor methodrefConstantVisitor;


    /**
     * Creates a new v that will delegate to the given constant visitor.
     */
    public MethodrefTraveler(ConstantVisitor methodrefConstantVisitor)
    {
        this.methodrefConstantVisitor = methodrefConstantVisitor;
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitMethodHandleConstant(Clazz clazz, MethodHandleConstant methodHandleConstant)
    {
        clazz.constantPoolEntryAccept(methodHandleConstant.u2referenceIndex,
                                      methodrefConstantVisitor);
    }
}
