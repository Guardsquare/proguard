/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;

public interface KotlinAnnotationVisitor
{
    void visitAnyAnnotation(Clazz                    clazz,
                            KotlinMetadataAnnotation annotation);


    default void visitTypeAnnotation(Clazz                    clazz,
                                     KotlinTypeMetadata       kotlinTypeMetadata,
                                     KotlinMetadataAnnotation annotation)
    {
        visitAnyAnnotation(clazz, annotation);
    }

    default void visitTypeParameterAnnotation(Clazz                       clazz,
                                              KotlinTypeParameterMetadata kotlinTypeParameterMetadata,
                                              KotlinMetadataAnnotation    annotation)
    {
        visitAnyAnnotation(clazz, annotation);
    }

    default void visitTypeAliasAnnotation(Clazz                    clazz,
                                          KotlinTypeAliasMetadata  kotlinTypeAliasMetadata,
                                          KotlinMetadataAnnotation annotation)
    {
        visitAnyAnnotation(clazz, annotation);
    }
}
