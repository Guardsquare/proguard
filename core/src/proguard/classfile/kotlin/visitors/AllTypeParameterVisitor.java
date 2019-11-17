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
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;

public class AllTypeParameterVisitor
implements   KotlinMetadataVisitor,
             KotlinPropertyVisitor,
             KotlinFunctionVisitor,
             KotlinTypeAliasVisitor
{
    private final KotlinTypeParameterVisitor delegate;

    public AllTypeParameterVisitor(KotlinTypeParameterVisitor delegate)
    {
        this.delegate = delegate;
    }

    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        kotlinDeclarationContainerMetadata.propertiesAccept(         clazz, this);
        kotlinDeclarationContainerMetadata.delegatedPropertiesAccept(clazz, this);
        kotlinDeclarationContainerMetadata.functionsAccept(          clazz, this);
    }

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        kotlinClassKindMetadata.typeParametersAccept(clazz, delegate);
    }

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz clazz, KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        kotlinSyntheticClassKindMetadata.functionsAccept(clazz, this);
    }

    // Implementations for KotlinPropertyVisitor.

    @Override
    public void visitAnyProperty(Clazz                              clazz,
                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                 KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        kotlinPropertyMetadata.typeParametersAccept(  clazz, kotlinDeclarationContainerMetadata, delegate);
    }


    // Implementations for KotlinFunctionVisitor.

    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        kotlinFunctionMetadata.typeParametersAccept( clazz, kotlinMetadata, delegate);
    }


    // Implementations for KotlinTypeAliasVisitor

    @Override
    public void visitTypeAlias(Clazz                              clazz,
                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                               KotlinTypeAliasMetadata            kotlinTypeAliasMetadata)
    {
        kotlinTypeAliasMetadata.typeParametersAccept(clazz, kotlinDeclarationContainerMetadata, delegate);
    }
}