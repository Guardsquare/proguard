/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;

public interface KotlinPropertyVisitor
{
    void visitAnyProperty(Clazz                              clazz,
                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                          KotlinPropertyMetadata             kotlinPropertyMetadata);


    default void visitProperty(Clazz                              clazz,
                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                               KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        visitAnyProperty(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);
    }

    default void visitDelegatedProperty(Clazz                              clazz,
                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                        KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        visitAnyProperty(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata);
    }
}
