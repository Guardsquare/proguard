/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import proguard.classfile.*;
import proguard.classfile.kotlin.flags.*;
import proguard.classfile.kotlin.visitors.*;
import proguard.util.*;

import java.util.*;

public class KotlinTypeAliasMetadata
extends SimpleVisitorAccepter
implements VisitorAccepter
{
    public String name;

    public KotlinTypeAliasFlags flags;

    public List<KotlinTypeParameterMetadata> typeParameters;

    // Right-hand side of alias declaration.
    public KotlinTypeMetadata underlyingType;

    // Core type.
    public KotlinTypeMetadata expandedType;

    public KotlinVersionRequirementMetadata versionRequirement;

    // The container where the alias is declared.
    public KotlinDeclarationContainerMetadata referencedDeclarationContainer;

    public List<KotlinMetadataAnnotation> annotations;


    public KotlinTypeAliasMetadata(int flags, String name)
    {
        this.name = name;
        this.flags = new KotlinTypeAliasFlags(flags);
    }


    public void accept(Clazz                              clazz,
                       KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                       KotlinTypeAliasVisitor             kotlinTypeAliasVisitor)
    {
        kotlinTypeAliasVisitor.visitTypeAlias(clazz,
                                              kotlinDeclarationContainerMetadata,
                                              this);
    }


    public void typeParametersAccept(Clazz                              clazz,
                                     KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                     KotlinTypeParameterVisitor         kotlinTypeParameterVisitor)
    {
        for (KotlinTypeParameterMetadata typeParameter : typeParameters)
        {
            typeParameter.accept(clazz, kotlinDeclarationContainerMetadata, this, kotlinTypeParameterVisitor);
        }
    }


    public void underlyingTypeAccept(Clazz                              clazz,
                                     KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                     KotlinTypeVisitor                  kotlinTypeVisitor)
    {

            //TODO unusual?
//            underlyingType.accept(clazz, kotlinDeclarationContainerMetadata, this, kotlinTypeVisitor);
            kotlinTypeVisitor.visitAliasUnderlyingType(clazz, kotlinDeclarationContainerMetadata, this, underlyingType);
    }


    public void expandedTypeAccept(Clazz                              clazz,
                                   KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                   KotlinTypeVisitor                  kotlinTypeVisitor)
    {
            //TODO unusual?
//            underlyingType.accept(clazz, kotlinDeclarationContainerMetadata, this, kotlinTypeVisitor);
            kotlinTypeVisitor.visitAliasExpandedType(clazz, kotlinDeclarationContainerMetadata, this, expandedType);
    }


    public void versionRequirementAccept(Clazz                           clazz,
                                         KotlinMetadata                  kotlinMetadata,
                                         KotlinVersionRequirementVisitor kotlinVersionRequirementVisitor)
    {
        if (versionRequirement != null)
        {
            versionRequirement.accept(clazz, kotlinMetadata, this, kotlinVersionRequirementVisitor);
        }
    }

    public void annotationsAccept(Clazz                   clazz,
                                  KotlinAnnotationVisitor kotlinAnnotationVisitor)
    {
        for (KotlinMetadataAnnotation annotation : annotations)
        {
            kotlinAnnotationVisitor.visitTypeAliasAnnotation(clazz, this, annotation);
        }
    }


    // Implementations for Object.
    @Override
    public String toString()
    {
        return "Kotlin type alias (" + name + ")"; //TODO "(name -> underlying/exapanded)"
    }
}
