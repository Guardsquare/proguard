/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors.filter;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitors.KotlinMetadataVisitor;

import java.util.function.Predicate;

/**
 * Delegate to another {@link KotlinMetadataVisitor} if the predicate returns true,
 * or if there's no predicate.
 *
 * Note: only for KotlinClassKindMetadata i.e. does not visit synthetic classes.
 *
 * For example, visit only abstract classes:
 *
 * programClassPool.classesAccept(
 *     new ClazzToKotlinMetadataVisitor(
 *     new KotlinClassKindFilter(
 *         clazz -> clazz.flags.isAbstract,
 *         new MyOtherKotlinMetadataVisitor())));
 */
public class KotlinClassKindFilter
implements   KotlinMetadataVisitor
{
    private final Predicate<KotlinClassKindMetadata> predicate;
    private final KotlinMetadataVisitor              kotlinMetadataVisitor;

    public KotlinClassKindFilter(KotlinMetadataVisitor kotlinMetadataVisitor)
    {
        this(__ -> true, kotlinMetadataVisitor);
    }

    public KotlinClassKindFilter(Predicate<KotlinClassKindMetadata> predicate, KotlinMetadataVisitor kotlinMetadataVisitor) {
        this.predicate             = predicate;
        this.kotlinMetadataVisitor = kotlinMetadataVisitor;
    }

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        if (this.predicate.test(kotlinClassKindMetadata))
        {
            this.kotlinMetadataVisitor.visitKotlinClassMetadata(clazz, kotlinClassKindMetadata);
        }
    }

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}
}
