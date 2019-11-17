/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.visitors.*;

import java.util.*;

/**
 * This class is named after Kotlin's own naming scheme. A declaration container is a type that
 * can define functions, properties and delegated properties, and that can also define type aliases.
 */
public abstract class KotlinDeclarationContainerMetadata
extends KotlinMetadata
{
    public List<KotlinPropertyMetadata> properties;

    public List<KotlinFunctionMetadata> functions;

    public List<KotlinTypeAliasMetadata> typeAliases;

    public String ownerClassName;
    public Clazz  ownerReferencedClass;

    // Extensions.
    public List<KotlinPropertyMetadata> localDelegatedProperties;


    public KotlinDeclarationContainerMetadata(int    k,
                                              int[]  mv,
                                              int[]  bv,
                                              int    xi,
                                              String xs,
                                              String pn)
    {
        super(k, mv, bv, xi, xs, pn);
    }


    public void propertiesAccept(Clazz clazz, KotlinPropertyVisitor kotlinPropertyVisitor)
    {
        for (KotlinPropertyMetadata property : properties)
        {
            property.accept(clazz, this, kotlinPropertyVisitor);
        }
    }


    //TODO currently unclear whether these are separate from `properties` or if they are additional info about some of them
    public void delegatedPropertiesAccept(Clazz clazz, KotlinPropertyVisitor kotlinPropertyVisitor)
    {
        for (KotlinPropertyMetadata localDelegatedProperty : localDelegatedProperties)
        {
            localDelegatedProperty.acceptAsDelegated(clazz, this, kotlinPropertyVisitor);
        }
    }


    public void functionsAccept(Clazz clazz, KotlinFunctionVisitor kotlinFunctionVisitor)
    {
        for (KotlinFunctionMetadata function : functions)
        {
            function.accept(clazz, this, kotlinFunctionVisitor);
        }
    }


    public void typeAliasesAccept(Clazz clazz, KotlinTypeAliasVisitor kotlinTypeAliasVisitor)
    {
        for (KotlinTypeAliasMetadata typeAlias : typeAliases)
        {
            typeAlias.accept(clazz, this, kotlinTypeAliasVisitor);
        }
    }
}
