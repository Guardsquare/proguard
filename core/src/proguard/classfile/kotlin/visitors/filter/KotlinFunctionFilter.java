/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors.filter;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitors.KotlinFunctionVisitor;

import java.util.function.Predicate;

/**
 * Delegate to another {@link KotlinFunctionVisitor} if the predicate returns true.
 *
 * For example, visit only abstract functions:
 *
 * kotlinMetadata.functionsAccept(clazz,
 *     new KotlinFunctionFilter(fun -> fun.flags.isAbstract,
 *                              new MyOtherKotlinFunctionVisitor()));
 */
public class KotlinFunctionFilter
implements   KotlinFunctionVisitor
{
    private final KotlinFunctionVisitor             kotlinFunctionVisitor;
    private final Predicate<KotlinFunctionMetadata> predicate;

    public KotlinFunctionFilter(Predicate<KotlinFunctionMetadata> predicate,
                                KotlinFunctionVisitor             kotlinFunctionVisitor)
    {
        this.kotlinFunctionVisitor = kotlinFunctionVisitor;
        this.predicate             = predicate;
    }


    // Implementations for KotlinFunctionVisitor.
    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {}

    @Override
    public void visitFunction(Clazz                              clazz,
                              KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                              KotlinFunctionMetadata             kotlinFunctionMetadata)
    {
        if (predicate.test(kotlinFunctionMetadata))
        {
            kotlinFunctionMetadata.accept(clazz, kotlinDeclarationContainerMetadata, kotlinFunctionVisitor);
        }
    }

    @Override
    public void visitSyntheticFunction(Clazz                            clazz,
                                       KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata,
                                       KotlinFunctionMetadata           kotlinFunctionMetadata)
    {
        if (predicate.test(kotlinFunctionMetadata))
        {
            kotlinFunctionMetadata.accept(clazz, kotlinSyntheticClassKindMetadata, kotlinFunctionVisitor);
        }
    }
}
