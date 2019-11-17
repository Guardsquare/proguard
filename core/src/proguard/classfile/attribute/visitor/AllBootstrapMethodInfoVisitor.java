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
package proguard.classfile.attribute.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.attribute.*;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This AttributeVisitor lets a given BootstrapMethodInfoVisitor visit all
 * bootstrap method objects of the BootstrapMethodsAttribute objects it visits.
 *
 * @author Eric Lafortune
 */
public class AllBootstrapMethodInfoVisitor
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    private final BootstrapMethodInfoVisitor bootstrapMethodInfoVisitor;


    public AllBootstrapMethodInfoVisitor(BootstrapMethodInfoVisitor bootstrapMethodInfoVisitor)
    {
        this.bootstrapMethodInfoVisitor = bootstrapMethodInfoVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute)
    {
        bootstrapMethodsAttribute.bootstrapMethodEntriesAccept(clazz, bootstrapMethodInfoVisitor);
    }
}
