/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.asserter.constraint;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.asserter.*;
import proguard.classfile.kotlin.visitors.KotlinMetadataVisitor;

/**
 */
public class DeclarationContainerIntegrity
extends    SimpleConstraintChecker
implements ConstraintChecker,
           KotlinMetadataVisitor

{

    public static KotlinMetadataConstraint constraint()
    {
        return KotlinMetadataConstraint.make(new DeclarationContainerIntegrity());
    }


    // Implementations for KotlinMetadataVisitor.
    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        AssertUtil util = new AssertUtil("Declaration container", clazz, kotlinDeclarationContainerMetadata, reporter);
        if (kotlinDeclarationContainerMetadata.ownerClassName != null)
        {
            util.reportIfNullReference(kotlinDeclarationContainerMetadata.ownerReferencedClass, "referenced owner class");
        }
    }
}