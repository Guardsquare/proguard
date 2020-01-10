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
package proguard.classfile.attribute.module.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.module.ModuleAttribute;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This {@link AttributeVisitor} lets a given {@link RequiresInfoVisitor} visit all
 * {@link RequiresInfo} instances of the {@link ModuleAttribute} instances it visits.
 *
 * @author Joachim Vandersmissen
 */
public class AllRequiresInfoVisitor
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    private final RequiresInfoVisitor requiresInfoVisitor;


    public AllRequiresInfoVisitor(RequiresInfoVisitor requiresInfoVisitor)
    {
        this.requiresInfoVisitor = requiresInfoVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitModuleAttribute(Clazz clazz, ModuleAttribute moduleAttribute)
    {
        moduleAttribute.requiresAccept(clazz, requiresInfoVisitor);
    }
}
