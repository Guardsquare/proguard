/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import kotlinx.metadata.*;
import proguard.classfile.*;
import proguard.classfile.kotlin.flags.KotlinTypeParameterFlags;
import proguard.classfile.kotlin.visitors.*;
import proguard.util.*;

import java.util.*;

public class KotlinTypeParameterMetadata
extends SimpleVisitorAccepter
implements VisitorAccepter
{
    public String name;

    public int id;

    public KmVariance variance;

    public List<KotlinTypeMetadata> upperBounds;

    public KotlinTypeParameterFlags flags;

    // Extensions.
    public List<KotlinMetadataAnnotation> annotations;


    public KotlinTypeParameterMetadata(int flags, String name, int id, KmVariance variance)
    {
        this.name     = name;
        this.id       = id;
        this.variance = variance;
        this.flags    = new KotlinTypeParameterFlags(flags);
    }


    public void accept(Clazz                      clazz,
                       KotlinClassKindMetadata    kotlinClassKindMetadata,
                       KotlinTypeParameterVisitor kotlinTypeParameterVisitor)
    {
        kotlinTypeParameterVisitor.visitClassTypeParameter(clazz, kotlinClassKindMetadata, this);
    }


    public void accept(Clazz                              clazz,
                       KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                       KotlinPropertyMetadata             kotlinPropertyMetadata,
                       KotlinTypeParameterVisitor         kotlinTypeParameterVisitor)
    {
        kotlinTypeParameterVisitor.visitPropertyTypeParameter(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata, this);
    }


    public void accept(Clazz                      clazz,
                       KotlinMetadata             kotlinMetadata,
                       KotlinFunctionMetadata     kotlinFunctionMetadata,
                       KotlinTypeParameterVisitor kotlinTypeParameterVisitor)
    {
        kotlinTypeParameterVisitor.visitFunctionTypeParameter(clazz, kotlinMetadata, kotlinFunctionMetadata, this);
    }


    public void accept(Clazz                              clazz,
                       KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                       KotlinTypeAliasMetadata            kotlinPropertyMetadata,
                       KotlinTypeParameterVisitor         kotlinTypeParameterVisitor)
    {
        kotlinTypeParameterVisitor.visitAliasTypeParameter(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata, this);
    }


    public void upperBoundsAccept(Clazz             clazz,
                                  KotlinTypeVisitor kotlinTypeVisitor)
    {
        for (KotlinTypeMetadata upperBound : upperBounds)
        {
            upperBound.accept(clazz, this, kotlinTypeVisitor);
        }
    }

    public void annotationsAccept(Clazz                   clazz,
                                  KotlinAnnotationVisitor kotlinAnnotationVisitor)
    {
        for (KotlinMetadataAnnotation annotation : annotations)
        {
            kotlinAnnotationVisitor.visitTypeParameterAnnotation(clazz, this, annotation);
        }
    }


    // Implementations for Object.
    @Override
    public String toString()
    {
        return "Kotlin " +
               (flags.isReified ? "primary " : "") +
               "constructor";
    }
}
