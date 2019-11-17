/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.*;
import proguard.classfile.kotlin.*;
import proguard.util.ArrayUtil;

public class MultiKotlinMetadataVisitor
implements   KotlinMetadataVisitor
{
    private KotlinMetadataVisitor[] kotlinMetadataVisitors;

    public MultiKotlinMetadataVisitor()
    {
        this.kotlinMetadataVisitors = new KotlinMetadataVisitor[16];
    }

    public MultiKotlinMetadataVisitor(KotlinMetadataVisitor ... kotlinMetadataVisitors)
    {
        this.kotlinMetadataVisitors = kotlinMetadataVisitors;
    }

    public void addKotlinMetadataVisitor(KotlinMetadataVisitor kotlinMetadataVisitor)
    {
        kotlinMetadataVisitors =
            ArrayUtil.add(kotlinMetadataVisitors,
                          kotlinMetadataVisitors.length + 1,
                          kotlinMetadataVisitor);
    }

    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        for (KotlinMetadataVisitor visitor : kotlinMetadataVisitors)
        {
            kotlinMetadata.accept(clazz, visitor);
        }
    }
}
