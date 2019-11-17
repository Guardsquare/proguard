/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.visitor.ClassVisitor;

public class KotlinMetadataToClazzVisitor
implements   KotlinMetadataVisitor
{
    private final ClassVisitor classVisitor;

    public KotlinMetadataToClazzVisitor(ClassVisitor classVisitor) {
        this.classVisitor = classVisitor;
    }

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        kotlinDeclarationContainerMetadata.ownerReferencedClass.accept(this.classVisitor);
    }

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz                            clazz,
                                                  KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        kotlinSyntheticClassKindMetadata.referencedClass.accept(this.classVisitor);
    }
}
