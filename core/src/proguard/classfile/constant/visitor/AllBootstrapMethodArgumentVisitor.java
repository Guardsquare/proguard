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
import proguard.classfile.attribute.BootstrapMethodInfo;
import proguard.classfile.attribute.visitor.BootstrapMethodInfoVisitor;

/**
 * This {@link BootstrapMethodInfoVisitor} lets a given {@link ConstantVisitor} visit all
 * constant pool entries of the bootstrap methods it visits.
 *
 * @author Eric Lafortune
 */
public class AllBootstrapMethodArgumentVisitor
implements   BootstrapMethodInfoVisitor
{
    private ConstantVisitor constantVisitor;

    /**
     * Creates a new AllBootstrapMethodArgumentVisitor that will delegate to
     * the given constant visitor.
     */
    public AllBootstrapMethodArgumentVisitor(ConstantVisitor constantVisitor)
    {
        this.constantVisitor = constantVisitor;
    }


    // Implementations for BootstrapMethodInfoVisitor.

    public void visitBootstrapMethodInfo(Clazz clazz, BootstrapMethodInfo bootstrapMethodInfo)
    {
        bootstrapMethodInfo.methodArgumentsAccept(clazz, constantVisitor);
    }
}
