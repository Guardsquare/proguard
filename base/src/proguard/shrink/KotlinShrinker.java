/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
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
package proguard.shrink;

import proguard.classfile.*;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitors.*;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.util.VisitorAccepter;

import java.util.*;

public class KotlinShrinker
extends      SimplifiedVisitor
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
    private final SimpleUsageMarker usageMarker;


    KotlinShrinker(SimpleUsageMarker usageMarker)
    {
        this.usageMarker = usageMarker;
    }


    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    // Implementations for KotlinMetadataVisitor.
    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz clazz, KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        // Compact the metadata's lists of properties and functions.
        shrinkMetadataArray(kotlinDeclarationContainerMetadata.properties);
        shrinkMetadataArray(kotlinDeclarationContainerMetadata.functions);
        shrinkMetadataArray(kotlinDeclarationContainerMetadata.typeAliases);
        shrinkMetadataArray(kotlinDeclarationContainerMetadata.localDelegatedProperties);

        // Compact each remaining property and function.
        kotlinDeclarationContainerMetadata.propertiesAccept(         clazz, this);
        kotlinDeclarationContainerMetadata.functionsAccept(          clazz, this);
        kotlinDeclarationContainerMetadata.typeAliasesAccept(        clazz, this);
        kotlinDeclarationContainerMetadata.delegatedPropertiesAccept(clazz, this);
    }

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        //TODO kotlinc creates an array of KProperty objects for properties. We need to update that array as well. Maybe cache the array in the KotlinMetadata instance?

        // Compact the metadata's own fields.
        if (shouldShrinkMetadata(kotlinClassKindMetadata.companionObjectName,
                                 kotlinClassKindMetadata.referencedCompanionClass))
        {
            kotlinClassKindMetadata.companionObjectName      = null;
            kotlinClassKindMetadata.referencedCompanionClass = null;
        }

        shrinkMetadataArray(kotlinClassKindMetadata.constructors);

        shrinkArray(kotlinClassKindMetadata.enumEntryNames,
                    kotlinClassKindMetadata.referencedEnumEntries);

        shrinkArray(kotlinClassKindMetadata.nestedClassNames,
                    kotlinClassKindMetadata.referencedNestedClasses);

        shrinkArray(kotlinClassKindMetadata.sealedSubclassNames,
                    kotlinClassKindMetadata.referencedSealedSubClasses);

        visitKotlinDeclarationContainerMetadata(clazz, kotlinClassKindMetadata);

        kotlinClassKindMetadata.superTypesAccept(        clazz, this);
        kotlinClassKindMetadata.typeParametersAccept(    clazz, this);
        kotlinClassKindMetadata.versionRequirementAccept(clazz, this);
        kotlinClassKindMetadata.constructorsAccept(      clazz, this);
    }

    @Override
    public void visitKotlinFileFacadeMetadata(Clazz clazz, KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata)
    {
        visitKotlinDeclarationContainerMetadata(clazz, kotlinFileFacadeKindMetadata);
    }

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz clazz, KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        // Compact the metadata's lists of functions.
        shrinkMetadataArray(kotlinSyntheticClassKindMetadata.functions);

        // Compact each remaining property and function.
        kotlinSyntheticClassKindMetadata.functionsAccept(clazz, this);
    }

    @Override
    public void visitKotlinMultiFileFacadeMetadata(Clazz clazz, KotlinMultiFileFacadeKindMetadata kotlinMultiFileFacadeKindMetadata)
    {
        shrinkArray(kotlinMultiFileFacadeKindMetadata.partClassNames,
                    kotlinMultiFileFacadeKindMetadata.referencedPartClasses);
    }

    @Override
    public void visitKotlinMultiFilePartMetadata(Clazz clazz, KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata)
    {
        //TODO what if the facade is shrunk? It's not really part of a multi-file class anymore then...

        visitKotlinDeclarationContainerMetadata(clazz, kotlinMultiFilePartKindMetadata);
    }


    // Implementations for KotlinPropertyVisitor.
    @Override
    public void visitAnyProperty(Clazz                              clazz,
                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                 KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        kotlinPropertyMetadata.versionRequirementAccept(clazz, kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.typeAccept(              clazz, kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.setterParametersAccept(  clazz, kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.receiverTypeAccept(      clazz, kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.typeParametersAccept(    clazz, kotlinDeclarationContainerMetadata, this);

        // TODO: Don't use JvmMethodSignature in ProGuard API.
        if (shouldShrinkMetadata(null,//kotlinPropertyMetadata.backingFieldSignature,
                                 kotlinPropertyMetadata.referencedBackingField))
        {
            //kotlinPropertyMetadata.backingFieldSignature  = null;
            kotlinPropertyMetadata.referencedBackingField = null;
        }

        // TODO: Don't use JvmMethodSignature in ProGuard API.
        if (shouldShrinkMetadata(null,//kotlinPropertyMetadata.getterSignature,
                                 kotlinPropertyMetadata.referencedGetterMethod))
        {
            //kotlinPropertyMetadata.getterSignature        = null;
            kotlinPropertyMetadata.referencedGetterMethod = null;
            kotlinPropertyMetadata.flags.hasGetter        = false;
        }

        // TODO: Don't use JvmMethodSignature in ProGuard API.
        if (shouldShrinkMetadata(null,//kotlinPropertyMetadata.setterSignature,
                                 kotlinPropertyMetadata.referencedSetterMethod))
        {
            //kotlinPropertyMetadata.setterSignature        = null;
            kotlinPropertyMetadata.referencedSetterMethod = null;
            kotlinPropertyMetadata.flags.hasSetter        = false;
            kotlinPropertyMetadata.setterParameters.clear();
        }

        kotlinPropertyMetadata.versionRequirementAccept(clazz,
                                                        kotlinDeclarationContainerMetadata,
                                                        this);

        // TODO: Don't use JvmMethodSignature in ProGuard API.
        if (//kotlinPropertyMetadata.syntheticMethodForAnnotations != null &&
            !usageMarker.isUsed(kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations))
        {
            // TODO: Don't use JvmMethodSignature in ProGuard API.
            //kotlinPropertyMetadata.syntheticMethodForAnnotations           = null;
            kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations = null;
            kotlinPropertyMetadata.referencedSyntheticMethodClass          = null;
            kotlinPropertyMetadata.flags.common.hasAnnotations             = false;
        }
    }

    // Implementations for KotlinFunctionVisitor.
    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        if (kotlinFunctionMetadata.referencedLambdaClassOrigin != null &&
            shouldShrinkMetadata(kotlinFunctionMetadata.lambdaClassOriginName,
                                 kotlinFunctionMetadata.referencedLambdaClassOrigin))
        {
            kotlinFunctionMetadata.lambdaClassOriginName       = null;
            kotlinFunctionMetadata.referencedLambdaClassOrigin = null;
        }

        kotlinFunctionMetadata.receiverTypeAccept(   clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.typeParametersAccept( clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.valueParametersAccept(clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.returnTypeAccept(     clazz, kotlinMetadata, this);

        if (!kotlinFunctionMetadata.flags.modality.isAbstract &&
            kotlinMetadata.k == KotlinConstants.METADATA_KIND_CLASS &&
            ((KotlinClassKindMetadata)kotlinMetadata).flags.isInterface &&
            kotlinFunctionMetadata.referencedDefaultImplementationMethod == null)
        {
            //TODO not sure if this can actually happen?
            kotlinFunctionMetadata.flags.modality.isAbstract = true;
        }

        if (kotlinFunctionMetadata.referencedDefaultMethod != null &&
            !usageMarker.isUsed(kotlinFunctionMetadata.referencedDefaultMethod))
        {
            kotlinFunctionMetadata.referencedDefaultMethod      = null;
            kotlinFunctionMetadata.referencedDefaultMethodClass = null;
        }

        if (kotlinFunctionMetadata.referencedDefaultImplementationMethod != null &&
            !usageMarker.isUsed(kotlinFunctionMetadata.referencedDefaultImplementationMethod))
        {
            kotlinFunctionMetadata.referencedDefaultImplementationMethod      = null;
            kotlinFunctionMetadata.referencedDefaultImplementationMethodClass = null;
        }
    }

    // Implementations for KotlinConstructorVisitor.

    @Override
    public void visitConstructor(Clazz                     clazz,
                                 KotlinClassKindMetadata   kotlinClassKindMetadata,
                                 KotlinConstructorMetadata kotlinConstructorMetadata)
    {
        kotlinConstructorMetadata.valueParametersAccept(   clazz, kotlinClassKindMetadata, this);
        kotlinConstructorMetadata.versionRequirementAccept(clazz, kotlinClassKindMetadata, this);
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

        shrinkMetadataArray(kotlinTypeAliasMetadata.annotations);
    }

    // Implementations for KotlinTypeVisitor.

    @Override
    public void visitAnyType(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata)
    {
        kotlinTypeMetadata.typeArgumentsAccept(clazz, this);
        kotlinTypeMetadata.upperBoundsAccept(  clazz, this);
        kotlinTypeMetadata.abbreviationAccept( clazz, this);

        shrinkMetadataArray(kotlinTypeMetadata.annotations);
    }

    // Implementations for KotlinTypeParameterVisitor.

    @Override
    public void visitAnyTypeParameter(Clazz clazz, KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
    {
        kotlinTypeParameterMetadata.upperBoundsAccept(clazz, this);

        shrinkMetadataArray(kotlinTypeParameterMetadata.annotations);
    }

    // Implementations for KotlinValueParameterVisitor.
    @Override
    public void visitAnyValueParameter(Clazz clazz,
                                       KotlinValueParameterMetadata kotlinValueParameterMetadata) {}

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

        if (kotlinValueParameterMetadata.flags.hasDefaultValue &&
            !usageMarker.isUsed(kotlinFunctionMetadata.referencedDefaultMethod))
        {
            kotlinValueParameterMetadata.flags.hasDefaultValue = false;
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
    }

    @Override
    public void visitAnyVersionRequirement(Clazz                            clazz,
                                           KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
    {
    }

    // Small helper methods.

    /**
     * Returns whether the given metadata element has its corresponding jvm
     * implementation element shrunk, and thus should be shrunk itself.
     */
    private boolean shouldShrinkMetadata(Object metadataElement, VisitorAccepter jvmElement)
    {
        // If this method throws a NPE, there is a kotlin metadata
        // element for which we could not find the corresponding
        // jvm element to cache yet (see ClassReferenceFixer.visitKotlinMetadata).
        return metadataElement != null &&
               !usageMarker.isUsed(jvmElement);
    }


    /**
     * Shrinks elements and their corresponding referenced element, based on
     * markings on the referenced element.
     *
     * List is modified - must be a modifiable list!
     */
    private void shrinkArray(List<?>                         elements,
                             List<? extends VisitorAccepter> referencedJavaElements)
    {
        for (int k = elements.size() - 1; k >= 0; k--)
        {
            if (!usageMarker.isUsed(referencedJavaElements.get(k)))
            {
                elements              .remove(k);
                referencedJavaElements.remove(k);
            }
        }
    }


    /**
     * Shrinks elements based on their markings.
     */
    private void shrinkMetadataArray(List<? extends VisitorAccepter> elements)
    {
        for (int k = elements.size() - 1; k >= 0; k--)
        {
            if (!usageMarker.isUsed(elements.get(k)))
            {
                elements.remove(k);
            }
        }
    }
}
