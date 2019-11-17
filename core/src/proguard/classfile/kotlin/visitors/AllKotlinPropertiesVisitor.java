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
 * This KotlinMetadataVisitor lets a given KotlinPropertyVisitor visit all properties
 * (regular and delegated) of visited KotlinDeclarationContainerMetadata.
 */
public class AllKotlinPropertiesVisitor
implements KotlinMetadataVisitor
{
    private final KotlinPropertyVisitor delegatePropertyVisitor;

    public AllKotlinPropertiesVisitor(KotlinPropertyVisitor kotlinPropertyVisitor)
    {
        this.delegatePropertyVisitor = kotlinPropertyVisitor;
    }

    // Implementations for KotlinMetadataVisitor.
    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        kotlinDeclarationContainerMetadata.propertiesAccept(         clazz, delegatePropertyVisitor);
        kotlinDeclarationContainerMetadata.delegatedPropertiesAccept(clazz, delegatePropertyVisitor);
    }
}
