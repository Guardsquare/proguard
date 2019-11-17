/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.asserter.constraint;

import proguard.classfile.*;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.asserter.*;
import proguard.classfile.kotlin.visitors.*;

/**
 * This class checks the assumption: All functions need a JVM signature
 */
public class ValueParameterIntegrity
extends      SimpleConstraintChecker
implements   ConstraintChecker,
             KotlinValueParameterVisitor
{
    public static KotlinMetadataConstraint constraint()
    {
        return KotlinMetadataConstraint.makeFromValueParameter(new ValueParameterIntegrity());
    }


    // Implementations for KotlinValueParameterVisitor.
    @Override
    public void visitAnyValueParameter(Clazz                        clazz,
                                       KotlinValueParameterMetadata kotlinValueParameterMetadata) {}
    @Override
    public void visitConstructorValParameter(Clazz                        clazz,
                                             KotlinClassKindMetadata      kotlinClassKindMetadata,
                                             KotlinConstructorMetadata    kotlinConstructorMetadata,
                                             KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        check(clazz, kotlinClassKindMetadata, kotlinValueParameterMetadata);
    }

    @Override
    public void visitFunctionValParameter(Clazz                        clazz,
                                          KotlinMetadata               kotlinMetadata,
                                          KotlinFunctionMetadata       kotlinFunctionMetadata,
                                          KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        check(clazz, kotlinMetadata, kotlinValueParameterMetadata);
    }

    @Override
    public void visitPropertyValParameter(Clazz                              clazz,
                                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                          KotlinPropertyMetadata             kotlinPropertyMetadata,
                                          KotlinValueParameterMetadata       kotlinValueParameterMetadata)
    {
        check(clazz, kotlinDeclarationContainerMetadata, kotlinValueParameterMetadata);
    }


    // Small helper methods.

    private void check(Clazz                        clazz,
                       KotlinMetadata               kotlinMetadata,
                       KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        AssertUtil util = new AssertUtil("ValueParameter", clazz, kotlinMetadata, reporter);
        util.reportIfNullReference(kotlinValueParameterMetadata.type, "type");
    }
}
