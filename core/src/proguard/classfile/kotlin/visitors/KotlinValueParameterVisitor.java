/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;

public interface KotlinValueParameterVisitor
{
    void visitAnyValueParameter(Clazz                        clazz,
                                KotlinValueParameterMetadata kotlinValueParameterMetadata);

    default void visitConstructorValParameter(Clazz                        clazz,
                                              KotlinClassKindMetadata      kotlinClassKindMetadata,
                                              KotlinConstructorMetadata    kotlinConstructorMetadata,
                                              KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        visitAnyValueParameter(clazz, kotlinValueParameterMetadata);
    }

    default void visitFunctionValParameter(Clazz                        clazz,
                                           KotlinMetadata               kotlinMetadata,
                                           KotlinFunctionMetadata       kotlinFunctionMetadata,
                                           KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        visitAnyValueParameter(clazz, kotlinValueParameterMetadata);
    }

    /**
     * Visit a value parameter of the property setter, if it has one.
     */
    default void visitPropertyValParameter(Clazz                              clazz,
                                           KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                           KotlinPropertyMetadata             kotlinPropertyMetadata,
                                           KotlinValueParameterMetadata       kotlinValueParameterMetadata)
    {
        visitAnyValueParameter(clazz, kotlinValueParameterMetadata);
    }

    default void onNewFunctionStart() {}
}
