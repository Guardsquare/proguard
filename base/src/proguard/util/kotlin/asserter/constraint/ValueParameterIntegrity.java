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
 */
public class ValueParameterIntegrity
extends      AbstractKotlinMetadataConstraint
    implements KotlinValueParameterVisitor
{

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        kotlinMetadata.accept(clazz, new AllValueParameterVisitor(this));
    }


    // Implementations for KotlinValueParameterVisitor.
    @Override
    public void visitAnyValueParameter(Clazz clazz,
                                       KotlinValueParameterMetadata kotlinValueParameterMetadata) {}
    @Override
    public void visitConstructorValParameter(Clazz clazz,
                                             KotlinClassKindMetadata kotlinClassKindMetadata,
                                             KotlinConstructorMetadata kotlinConstructorMetadata,
                                             KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        new AssertUtil("Constructor value parameter", reporter)
            .reportIfNullReference("type", kotlinValueParameterMetadata.type);
    }

    @Override
    public void visitFunctionValParameter(Clazz clazz,
                                          KotlinMetadata kotlinMetadata,
                                          KotlinFunctionMetadata kotlinFunctionMetadata,
                                          KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        new AssertUtil("Function value parameter", reporter)
            .reportIfNullReference("type", kotlinValueParameterMetadata.type);
    }

    @Override
    public void visitPropertyValParameter(Clazz clazz,
                                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                          KotlinPropertyMetadata kotlinPropertyMetadata,
                                          KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        new AssertUtil("Property value parameter", reporter)
            .reportIfNullReference("type", kotlinValueParameterMetadata.type);
    }
}
