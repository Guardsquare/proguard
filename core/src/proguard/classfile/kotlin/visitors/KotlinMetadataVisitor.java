/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
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
