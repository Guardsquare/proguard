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
 * This KotlinMetadataVisitor lets a given KotlinConstructorVisitor visit all constructors of visited KotlinMetadata.
 */
public class   AllConstructorsVisitor
    implements KotlinMetadataVisitor
{
    private final KotlinConstructorVisitor delegateConstructorVisitor;


    public AllConstructorsVisitor(KotlinConstructorVisitor delegateConstructorVisitor)
    {
        this.delegateConstructorVisitor = delegateConstructorVisitor;
    }


    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
    }


    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        kotlinClassKindMetadata.constructorsAccept(clazz, delegateConstructorVisitor);
    }
}
