/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.fixer.kotlin;

import proguard.classfile.*;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.*;

public class KotlinAnnotationFlagFixer
implements   KotlinMetadataVisitor,

             // Implementation interfaces.
             KotlinPropertyVisitor,
             KotlinFunctionVisitor,
             KotlinTypeAliasVisitor,
             KotlinTypeVisitor,
             KotlinConstructorVisitor,
             KotlinTypeParameterVisitor,
             KotlinValueParameterVisitor,
             KotlinVersionRequirementVisitor
{
    private final KotlinAnnotationCounter annotationCounter;


    public KotlinAnnotationFlagFixer()
    {
        this.annotationCounter = new KotlinAnnotationCounter();
    }

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}


    // Implementations for KotlinMetadataVisitor.
    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz clazz, KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        kotlinDeclarationContainerMetadata.propertiesAccept(         clazz, this);
        kotlinDeclarationContainerMetadata.functionsAccept(          clazz, this);
        kotlinDeclarationContainerMetadata.typeAliasesAccept(        clazz, this);
        kotlinDeclarationContainerMetadata.delegatedPropertiesAccept(clazz, this);
    }

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        visitKotlinDeclarationContainerMetadata(clazz, kotlinClassKindMetadata);

        kotlinClassKindMetadata.contextReceiverTypesAccept(             clazz, this);
        kotlinClassKindMetadata.superTypesAccept(                       clazz, this);
        kotlinClassKindMetadata.typeParametersAccept(                   clazz, this);
        kotlinClassKindMetadata.versionRequirementAccept(               clazz, this);
        kotlinClassKindMetadata.constructorsAccept(                     clazz, this);
        kotlinClassKindMetadata.inlineClassUnderlyingPropertyTypeAccept(clazz, this);

        kotlinClassKindMetadata.referencedClass.attributesAccept(annotationCounter.reset());
        kotlinClassKindMetadata.flags.common.hasAnnotations = annotationCounter.getCount() > 0;
    }

    @Override
    public void visitKotlinFileFacadeMetadata(Clazz clazz, KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata)
    {
        visitKotlinDeclarationContainerMetadata(clazz, kotlinFileFacadeKindMetadata);
    }

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz clazz, KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        kotlinSyntheticClassKindMetadata.functionsAccept(clazz, this);
    }

    @Override
    public void visitKotlinMultiFileFacadeMetadata(Clazz clazz, KotlinMultiFileFacadeKindMetadata kotlinMultiFileFacadeKindMetadata)
    {
    }

    @Override
    public void visitKotlinMultiFilePartMetadata(Clazz clazz, KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata)
    {
        visitKotlinDeclarationContainerMetadata(clazz, kotlinMultiFilePartKindMetadata);
    }


    // Implementations for KotlinPropertyVisitor.
    @Override
    public void visitAnyProperty(Clazz                              clazz,
                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                 KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        kotlinPropertyMetadata.versionRequirementAccept(clazz,   kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.typeAccept(              clazz,   kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.setterParametersAccept(  clazz,   kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.contextReceiverTypesAccept(clazz, kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.receiverTypeAccept(      clazz,   kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.typeParametersAccept(    clazz,   kotlinDeclarationContainerMetadata, this);

        if (kotlinPropertyMetadata.syntheticMethodForAnnotations != null)
        {
            kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations.accept(
                kotlinPropertyMetadata.referencedSyntheticMethodClass,
                annotationCounter.reset()
            );

            kotlinPropertyMetadata.flags.common.hasAnnotations = annotationCounter.getCount() > 0;
        }
        else if (kotlinPropertyMetadata.referencedBackingField != null)
        {
            kotlinPropertyMetadata.referencedBackingField.accept(kotlinPropertyMetadata.referencedBackingFieldClass, annotationCounter);
            kotlinPropertyMetadata.flags.common.hasAnnotations = annotationCounter.getCount() > 0;
        }
        else
        {
            kotlinPropertyMetadata.flags.common.hasAnnotations = false;
        }

        if (kotlinPropertyMetadata.flags.hasGetter && kotlinPropertyMetadata.referencedGetterMethod != null)
        {
            kotlinPropertyMetadata.referencedGetterMethod.accept(clazz, annotationCounter.reset());
            kotlinPropertyMetadata.getterFlags.common.hasAnnotations = annotationCounter.getCount() > 0;
        }

        if (kotlinPropertyMetadata.flags.hasSetter && kotlinPropertyMetadata.referencedSetterMethod != null)
        {
            kotlinPropertyMetadata.referencedSetterMethod.accept(clazz, annotationCounter.reset());
            kotlinPropertyMetadata.setterFlags.common.hasAnnotations = annotationCounter.getCount() > 0;
        }
    }

    // Implementations for KotlinFunctionVisitor.
    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        kotlinFunctionMetadata.contextReceiverTypesAccept(clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.receiverTypeAccept(        clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.typeParametersAccept(      clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.valueParametersAccept(     clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.returnTypeAccept(          clazz, kotlinMetadata, this);

        kotlinFunctionMetadata.referencedMethodAccept(annotationCounter.reset());
        kotlinFunctionMetadata.flags.common.hasAnnotations = annotationCounter.getCount() != 0;
    }

    // Implementations for KotlinConstructorVisitor.
    @Override
    public void visitConstructor(Clazz                     clazz,
                                 KotlinClassKindMetadata   kotlinClassKindMetadata,
                                 KotlinConstructorMetadata kotlinConstructorMetadata)
    {
        kotlinConstructorMetadata.valueParametersAccept(   clazz, kotlinClassKindMetadata, this);
        kotlinConstructorMetadata.versionRequirementAccept(clazz, kotlinClassKindMetadata, this);

        if (kotlinClassKindMetadata.flags.isAnnotationClass)
        {
            //PROBBUG where are the annotations?
            kotlinConstructorMetadata.flags.common.hasAnnotations = false;
        }
        else
        {
            kotlinConstructorMetadata.referencedMethodAccept(clazz, annotationCounter.reset());
            kotlinConstructorMetadata.flags.common.hasAnnotations = annotationCounter.getCount() != 0;
        }
    }

    // Implementations for KotlinTypeAliasVisitor.
    @Override
    public void visitTypeAlias(Clazz clazz,
                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                               KotlinTypeAliasMetadata kotlinTypeAliasMetadata)
    {
        kotlinTypeAliasMetadata.typeParametersAccept(    clazz, kotlinDeclarationContainerMetadata, this);
        kotlinTypeAliasMetadata.underlyingTypeAccept(    clazz, kotlinDeclarationContainerMetadata, this);
        kotlinTypeAliasMetadata.expandedTypeAccept(      clazz, kotlinDeclarationContainerMetadata, this);
        kotlinTypeAliasMetadata.versionRequirementAccept(clazz, kotlinDeclarationContainerMetadata, this);

        kotlinTypeAliasMetadata.flags.common.hasAnnotations = !kotlinTypeAliasMetadata.annotations.isEmpty();
    }

    // Implementations for KotlinTypeVisitor.
    @Override
    public void visitAnyType(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata)
    {
        kotlinTypeMetadata.typeArgumentsAccept(clazz, this);
        kotlinTypeMetadata.upperBoundsAccept(  clazz, this);
        kotlinTypeMetadata.abbreviationAccept( clazz, this);
    }

    @Override
    public void visitFunctionReceiverType(Clazz clazz,
                                          KotlinMetadata kotlinMetadata,
                                          KotlinFunctionMetadata kotlinFunctionMetadata,
                                          KotlinTypeMetadata kotlinTypeMetadata)
    {
        kotlinFunctionMetadata.referencedMethodAccept(this.annotationCounter.reset());
    }

    // Implementations for KotlinTypeParameterVisitor.
    @Override
    public void visitAnyValueParameter(Clazz                        clazz,
                                       KotlinValueParameterMetadata kotlinValueParameterMetadata) {}

    @Override
    public void visitAnyTypeParameter(Clazz clazz, KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
    {
        kotlinTypeParameterMetadata.upperBoundsAccept(clazz, this);
    }

    // Implementations for KotlinValueParameterVisitor.

    @Override
    public void visitFunctionValParameter(Clazz                        clazz,
                                          KotlinMetadata               kotlinMetadata,
                                          KotlinFunctionMetadata       kotlinFunctionMetadata,
                                          KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        kotlinValueParameterMetadata.typeAccept(clazz,
                                                kotlinMetadata,
                                                kotlinFunctionMetadata,
                                                this);

        if (kotlinValueParameterMetadata.flags.common.hasAnnotations)
        {
            kotlinFunctionMetadata.referencedMethodAccept(annotationCounter.reset());
            kotlinValueParameterMetadata.flags.common.hasAnnotations =
                annotationCounter.getParameterAnnotationCount(kotlinValueParameterMetadata.index) > 0;
        }
    }

    @Override
    public void visitConstructorValParameter(Clazz                        clazz,
                                             KotlinClassKindMetadata      kotlinClassKindMetadata,
                                             KotlinConstructorMetadata    kotlinConstructorMetadata,
                                             KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        kotlinValueParameterMetadata.typeAccept(clazz,
                                                kotlinClassKindMetadata,
                                                kotlinConstructorMetadata,
                                                this);

        if (kotlinValueParameterMetadata.flags.common.hasAnnotations)
        {
            if (!kotlinClassKindMetadata.flags.isAnnotationClass)
            {
                kotlinConstructorMetadata.referencedMethodAccept(clazz, annotationCounter.reset());
                kotlinValueParameterMetadata.flags.common.hasAnnotations =
                    annotationCounter.getParameterAnnotationCount(kotlinValueParameterMetadata.index) > 0;
            }
        }
    }

    @Override
    public void visitPropertyValParameter(Clazz                              clazz,
                                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                          KotlinPropertyMetadata             kotlinPropertyMetadata,
                                          KotlinValueParameterMetadata       kotlinValueParameterMetadata)
    {
        kotlinValueParameterMetadata.typeAccept(clazz,
                                                kotlinDeclarationContainerMetadata,
                                                kotlinPropertyMetadata,
                                                this);

        if (kotlinValueParameterMetadata.flags.common.hasAnnotations)
        {
            kotlinPropertyMetadata.referencedSetterMethod.accept(clazz, annotationCounter.reset());
            kotlinValueParameterMetadata.flags.common.hasAnnotations =
                annotationCounter.getParameterAnnotationCount(kotlinValueParameterMetadata.index) > 0;
        }
    }

    @Override
    public void visitAnyVersionRequirement(Clazz                            clazz,
                                           KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata) {}
}
