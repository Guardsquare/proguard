/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.util.kotlin.asserter.constraint;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.*;
import proguard.util.kotlin.asserter.AssertUtil;

/**
 * This class checks the assumption: All properties need a JVM signature for their getter
 */
public class KmAnnotationIntegrity
extends      AbstractKotlinMetadataConstraint
    implements KotlinTypeVisitor,
               KotlinTypeAliasVisitor,
               KotlinTypeParameterVisitor,
               KotlinAnnotationVisitor
{

    private AssertUtil util;


    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        kotlinMetadata.accept(clazz, new MultiKotlinMetadataVisitor(
            new AllTypeVisitor(this),
            new AllTypeAliasVisitor(this),
            new AllTypeParameterVisitor(this)
        ));
    }

    // Implementations for KotlinTypeVisitor.
    @Override
    public void visitAnyType(Clazz clazz, KotlinTypeMetadata type)
    {
        util = new AssertUtil("Type", reporter);
        type.annotationsAccept(clazz, this);

    }

    // Implementations for KotlinTypeAliasVisitor.
    @Override
    public void visitTypeAlias(Clazz clazz,
                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                               KotlinTypeAliasMetadata kotlinTypeAliasMetadata)
    {
        util = new AssertUtil("Type alias", reporter);
        kotlinTypeAliasMetadata.annotationsAccept(clazz, this);
    }

    // Implementations for KotlinTypeParameterVisitor.
    @Override
    public void visitAnyTypeParameter(Clazz clazz,
                                      KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
    {
        util = new AssertUtil("Type parameter", reporter);
        kotlinTypeParameterMetadata.annotationsAccept(clazz, this);
    }

    @Override
    public void visitAnyAnnotation(Clazz clazz, KotlinMetadataAnnotation antn)
    {
        util.reportIfNullReference("annotation class", antn.referencedAnnotationClass);
        antn.referencedArgumentMethods
            .values()
            .forEach(annotation -> util.reportIfNullReference("annotation method", annotation));
    }
}
