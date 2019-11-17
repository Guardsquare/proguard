/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import proguard.classfile.*;
import proguard.classfile.kotlin.visitors.*;
import proguard.util.*;

import java.util.*;


public class KotlinContractMetadata
extends    SimpleVisitorAccepter
implements VisitorAccepter
{
    public List<KotlinEffectMetadata> effects;


    public void accept(Clazz                  clazz,
                       KotlinMetadata         kotlinMetadata,
                       KotlinFunctionMetadata kotlinFunctionMetadata,
                       KotlinContractVisitor  kotlinContractVisitor)
    {
        kotlinContractVisitor.visitContract(clazz,
                                            kotlinMetadata,
                                            kotlinFunctionMetadata,
                                            this);
    }


    public void effectsAccept(Clazz                       clazz,
                             KotlinMetadata              kotlinMetadata,
                             KotlinFunctionMetadata      kotlinFunctionMetadata,
                             KotlinEffectVisitor kotlinEffectVisitor)
    {
        for (KotlinEffectMetadata effect : effects)
        {
            effect.accept(clazz, kotlinMetadata, kotlinFunctionMetadata, this, kotlinEffectVisitor);
        }
    }


    // Implementations for Object.
    @Override
    public String toString()
    {
        return "Kotlin contract";
    }
}
