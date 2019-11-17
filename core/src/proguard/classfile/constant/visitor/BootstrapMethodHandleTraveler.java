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
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This ConstantVisitor and BootstrapMethodInfoVisitor travels from any invoke
 * dynamic constants or bootstrap method info entries that it visits to their
 * bootstrap method handle constants, and applies a given constant visitor.
 *
 * @author Eric Lafortune
 */
public class BootstrapMethodHandleTraveler
extends      SimplifiedVisitor
implements   ConstantVisitor,
             AttributeVisitor,
             BootstrapMethodInfoVisitor
{
    private ConstantVisitor bootstrapMethodHandleVisitor;

    // Field serving as a method argument.
    int bootstrapMethodAttributeIndex;


    /**
     * Creates a new BootstrapMethodHandleVisitor that will delegate to the
     * given constant visitor.
     */
    public BootstrapMethodHandleTraveler(ConstantVisitor bootstrapMethodHandleVisitor)
    {
        this.bootstrapMethodHandleVisitor = bootstrapMethodHandleVisitor;
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitDynamicConstant(Clazz clazz, DynamicConstant dynamicConstant)
    {
        // Pass the method index.
        bootstrapMethodAttributeIndex =
            dynamicConstant.u2bootstrapMethodAttributeIndex;

        // Delegate to the bootstrap method.
        clazz.attributesAccept(this);
    }


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        // Pass the method index.
        bootstrapMethodAttributeIndex =
            invokeDynamicConstant.u2bootstrapMethodAttributeIndex;

        // Delegate to the bootstrap method.
        clazz.attributesAccept(this);
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute)
    {
        // Check bootstrap methods.
        bootstrapMethodsAttribute.bootstrapMethodEntryAccept(clazz,
                                                             bootstrapMethodAttributeIndex,
                                                             this);
    }


    // Implementations for BootstrapMethodInfoVisitor.

    public void visitBootstrapMethodInfo(Clazz clazz, BootstrapMethodInfo bootstrapMethodInfo)
    {
        // Check bootstrap method.
        clazz.constantPoolEntryAccept(bootstrapMethodInfo.u2methodHandleIndex,
                                      bootstrapMethodHandleVisitor);
    }
}
