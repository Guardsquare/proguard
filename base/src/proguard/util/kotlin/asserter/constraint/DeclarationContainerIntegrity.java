/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.util.kotlin.asserter.constraint;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.util.kotlin.asserter.AssertUtil;

/**
 */
public class DeclarationContainerIntegrity
extends      AbstractKotlinMetadataConstraint
    implements KotlinMetadataVisitor
{
    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        if (kotlinDeclarationContainerMetadata.ownerClassName != null)
        {
            new AssertUtil("Declaration container " + kotlinDeclarationContainerMetadata.ownerClassName, reporter)
                .reportIfNullReference("referenced owner class", kotlinDeclarationContainerMetadata.ownerReferencedClass);
        }
    }
}