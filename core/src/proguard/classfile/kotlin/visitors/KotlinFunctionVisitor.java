/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;

public interface KotlinFunctionVisitor
{
    void visitAnyFunction(Clazz                  clazz,
                          KotlinMetadata         kotlinMetadata,
                          KotlinFunctionMetadata kotlinFunctionMetadata);


    default void visitFunction(Clazz                              clazz,
                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                               KotlinFunctionMetadata             kotlinFunctionMetadata)
    {
        visitAnyFunction(clazz, kotlinDeclarationContainerMetadata, kotlinFunctionMetadata);
    }

    default void visitSyntheticFunction(Clazz                            clazz,
                                        KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata,
                                        KotlinFunctionMetadata           kotlinFunctionMetadata)
    {
        visitAnyFunction(clazz, kotlinSyntheticClassKindMetadata, kotlinFunctionMetadata);
    }
}
