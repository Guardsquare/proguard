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

public class KotlinInterfaceToDefaultImplsClassVisitor
implements   KotlinMetadataVisitor
{

    private final ClassVisitor classVisitor;

    public KotlinInterfaceToDefaultImplsClassVisitor(ClassVisitor classVisitor) {
        this.classVisitor = classVisitor;
    }

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        if (kotlinClassKindMetadata.flags.isInterface && kotlinClassKindMetadata.referencedDefaultImplsClass != null)
        {
            kotlinClassKindMetadata.referencedDefaultImplsClass.accept(this.classVisitor);
        }
    }
}
