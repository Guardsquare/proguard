/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import kotlinx.metadata.*;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.visitors.*;
import proguard.util.*;


public class KotlinEffectMetadata
extends SimpleVisitorAccepter
implements VisitorAccepter
{
    public KmEffectType effectType;

    public KmEffectInvocationKind invocationKind;

    public KotlinEffectExpressionMetadata conclusionOfConditionalEffect;

    public KotlinEffectExpressionMetadata constructorArgument;


    public KotlinEffectMetadata(KmEffectType           effectType,
                                KmEffectInvocationKind invocationKind)
    {
        this.effectType = effectType;
        this.invocationKind = invocationKind;
    }


    public void accept(Clazz                       clazz,
                       KotlinMetadata              kotlinMetadata,
                       KotlinFunctionMetadata      kotlinFunctionMetadata,
                       KotlinContractMetadata      kotlinContractMetadata,
                       KotlinEffectVisitor kotlinEffectVisitor)
    {
        kotlinEffectVisitor.visitEffect(clazz,
                                        kotlinMetadata,
                                        kotlinFunctionMetadata,
                                        kotlinContractMetadata,
                                        this);
    }

    public void constructorArgumentAccept(Clazz                   clazz,
                                          KotlinEffectExprVisitor kotlinEffectExprVisitor)
    {
        if (constructorArgument != null)
        {
            constructorArgument.accept(clazz, this, kotlinEffectExprVisitor);
        }
    }

    public void conclusionOfConditionalEffectAccept(Clazz                   clazz,
                                                    KotlinEffectExprVisitor kotlinEffectExprVisitor)
    {
        if (conclusionOfConditionalEffect != null)
        {
            conclusionOfConditionalEffect.accept(clazz, this, kotlinEffectExprVisitor);
        }
    }


    // Implementations for Object.
    @Override
    public String toString()
    {
        return "Kotlin contract effect";
    }
}
