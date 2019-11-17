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

public interface KotlinMetadataVisitor
{
    void visitAnyKotlinMetadata(Clazz          clazz,
                                KotlinMetadata kotlinMetadata);


    default void visitKotlinDeclarationContainerMetadata(Clazz clazz, KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        visitAnyKotlinMetadata(clazz, kotlinDeclarationContainerMetadata);
    }

    default void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        visitKotlinDeclarationContainerMetadata(clazz, kotlinClassKindMetadata);
    }

    default void visitKotlinFileFacadeMetadata(Clazz clazz, KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata)
    {
        visitKotlinDeclarationContainerMetadata(clazz, kotlinFileFacadeKindMetadata);
    }

    default void visitKotlinSyntheticClassMetadata(Clazz clazz, KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        visitAnyKotlinMetadata(clazz, kotlinSyntheticClassKindMetadata);
    }

    default void visitKotlinMultiFileFacadeMetadata(Clazz clazz, KotlinMultiFileFacadeKindMetadata kotlinMultiFileFacadeKindMetadata)
    {
        visitAnyKotlinMetadata(clazz, kotlinMultiFileFacadeKindMetadata);
    }

    default void visitKotlinMultiFilePartMetadata(Clazz clazz, KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata)
    {
        visitKotlinDeclarationContainerMetadata(clazz, kotlinMultiFilePartKindMetadata);
    }
}
