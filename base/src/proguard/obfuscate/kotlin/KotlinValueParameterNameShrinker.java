/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */
package proguard.obfuscate.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.*;

/**
 * This KotlinValueParameterVisitor removes the name of ValueParameters that it visits, based on the markings of the
 * {@link KotlinValueParameterUsageMarker}.
 */
public class   KotlinValueParameterNameShrinker
    implements KotlinValueParameterVisitor
{
    private int parameterNumber = 0;

    // Implementations for KotlinValueParameterVisitor.
    @Override
    public void onNewFunctionStart()
    {
        parameterNumber = 0;
    }

    @Override
    public void visitAnyValueParameter(Clazz clazz, KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        if (!KotlinValueParameterUsageMarker.isUsed(kotlinValueParameterMetadata))
        {
            kotlinValueParameterMetadata.parameterName = "p" + parameterNumber++;
        }
    }
}
