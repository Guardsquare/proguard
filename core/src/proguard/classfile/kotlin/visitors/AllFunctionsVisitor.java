/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;

/**
 * This KotlinMetadataVisitor lets a given KotlinFunctionVisitor visit all functions
 * of visited KotlinMetadata.
 */
public class AllFunctionsVisitor
implements   KotlinMetadataVisitor
{
    private final KotlinFunctionVisitor[] delegateFunctionVisitors;

    public AllFunctionsVisitor(KotlinFunctionVisitor ... kotlinFunctionVisitor)
    {
        this.delegateFunctionVisitors = kotlinFunctionVisitor;
    }

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
    }

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        for (KotlinFunctionVisitor functionVisitor : delegateFunctionVisitors)
        {
            kotlinDeclarationContainerMetadata.functionsAccept(clazz, functionVisitor);
        }
    }

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz                            clazz,
                                                  KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        for (KotlinFunctionVisitor functionVisitor : delegateFunctionVisitors)
        {
            kotlinSyntheticClassKindMetadata.functionsAccept(clazz, functionVisitor);
        }
    }
}
