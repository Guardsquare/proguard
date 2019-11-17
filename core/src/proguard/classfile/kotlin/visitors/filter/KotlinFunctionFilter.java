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
package proguard.classfile.kotlin.visitors.filter;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitors.KotlinFunctionVisitor;

import java.util.function.Predicate;

/**
 * Delegate to another {@link KotlinFunctionVisitor} if the predicate returns true.
 *
 * For example, visit only abstract functions:
 *
 * kotlinMetadata.functionsAccept(clazz,
 *     new KotlinFunctionFilter(fun -> fun.flags.isAbstract,
 *                              new MyOtherKotlinFunctionVisitor()));
 */
public class KotlinFunctionFilter
implements   KotlinFunctionVisitor
{
    private final KotlinFunctionVisitor             kotlinFunctionVisitor;
    private final Predicate<KotlinFunctionMetadata> predicate;

    public KotlinFunctionFilter(Predicate<KotlinFunctionMetadata> predicate,
                                KotlinFunctionVisitor             kotlinFunctionVisitor)
    {
        this.kotlinFunctionVisitor = kotlinFunctionVisitor;
        this.predicate             = predicate;
    }


    // Implementations for KotlinFunctionVisitor.
    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {}

    @Override
    public void visitFunction(Clazz                              clazz,
                              KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                              KotlinFunctionMetadata             kotlinFunctionMetadata)
    {
        if (predicate.test(kotlinFunctionMetadata))
        {
            kotlinFunctionMetadata.accept(clazz, kotlinDeclarationContainerMetadata, kotlinFunctionVisitor);
        }
    }

    @Override
    public void visitSyntheticFunction(Clazz                            clazz,
                                       KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata,
                                       KotlinFunctionMetadata           kotlinFunctionMetadata)
    {
        if (predicate.test(kotlinFunctionMetadata))
        {
            kotlinFunctionMetadata.accept(clazz, kotlinSyntheticClassKindMetadata, kotlinFunctionVisitor);
        }
    }
}
