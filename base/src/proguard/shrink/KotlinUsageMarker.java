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
import proguard.classfile.util.*;
import proguard.classfile.kotlin.visitors.KotlinAnnotationCounter;
import proguard.util.*;

import java.util.*;

public class KotlinUsageMarker
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
             KotlinVersionRequirementVisitor,
             KotlinContractVisitor,
             KotlinEffectVisitor,
             KotlinEffectExprVisitor,
             KotlinAnnotationVisitor
{
    private final SimpleUsageMarker javaUsageMarker;
    private final ClassUsageMarker  javaClassUsageMarker;

    KotlinUsageMarker(SimpleUsageMarker javaUsageMarker,
                      ClassUsageMarker  javaClassUsageMarker)
    {
        this.javaUsageMarker      = javaUsageMarker;
        this.javaClassUsageMarker = javaClassUsageMarker;
    }


    // Implementations for KotlinMetadataVisitor.
    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz clazz, KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        if (!isUsed(kotlinDeclarationContainerMetadata))
        {
            if (isJavaClassUsed(kotlinDeclarationContainerMetadata.ownerReferencedClass))
            {
                markAsUsed(kotlinDeclarationContainerMetadata);
            }
        }

        if (isUsed(kotlinDeclarationContainerMetadata))
        {
            kotlinDeclarationContainerMetadata.propertiesAccept(         clazz, this);
            kotlinDeclarationContainerMetadata.functionsAccept(          clazz, this);
            kotlinDeclarationContainerMetadata.typeAliasesAccept(        clazz, this);
            kotlinDeclarationContainerMetadata.delegatedPropertiesAccept(clazz, this);
        }
    }

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        visitKotlinDeclarationContainerMetadata(clazz, kotlinClassKindMetadata);

        if (isUsed(kotlinClassKindMetadata))
        {
            markAsUsed(kotlinClassKindMetadata.superTypes);
            markAsUsed(kotlinClassKindMetadata.typeParameters);

            if (kotlinClassKindMetadata.flags.isAnnotationClass)
            {
                // Annotation classes have constructors in the metadata but
                // no corresponding Java constructors.
                markAsUsed(kotlinClassKindMetadata.constructors);
            }
            else
            {
                kotlinClassKindMetadata.constructorsAccept(clazz, this);
            }

            kotlinClassKindMetadata.superTypesAccept(        clazz, this);
            kotlinClassKindMetadata.typeParametersAccept(    clazz, this);
            kotlinClassKindMetadata.versionRequirementAccept(clazz, this);
        }
    }

    @Override
    public void visitKotlinFileFacadeMetadata(Clazz clazz, KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata)
    {
        visitKotlinDeclarationContainerMetadata(clazz, kotlinFileFacadeKindMetadata);
    }


    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz                            clazz,
                                                  KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        markAsUsed(kotlinSyntheticClassKindMetadata);
        kotlinSyntheticClassKindMetadata.functionsAccept(clazz, this);
    }

    @Override
    public void visitKotlinMultiFileFacadeMetadata(Clazz                             clazz,
                                                   KotlinMultiFileFacadeKindMetadata kotlinMultiFileFacadeKindMetadata)
    {
    }

    @Override
    public void visitKotlinMultiFilePartMetadata(Clazz                           clazz,
                                                 KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata)
    {
        visitKotlinDeclarationContainerMetadata(clazz, kotlinMultiFilePartKindMetadata);

        if (isUsed(kotlinMultiFilePartKindMetadata))
        {
            if (!isJavaClassUsed(kotlinMultiFilePartKindMetadata.referencedFacade))
            {
                //TODO Shouldn't happen?
                kotlinMultiFilePartKindMetadata.referencedFacade.accept(javaClassUsageMarker);
            }
        }
    }

    // Implementations for KotlinPropertyVisitor.

    @Override
    public void visitAnyProperty(Clazz                              clazz,
                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                 KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        if (!isUsed(kotlinPropertyMetadata))
        {
            boolean backingFieldUsed =
                kotlinPropertyMetadata.referencedBackingField != null &&
                isUsed(kotlinPropertyMetadata.referencedBackingField);
            boolean getterUsed =
                kotlinPropertyMetadata.referencedGetterMethod != null &&
                isUsed(kotlinPropertyMetadata.referencedGetterMethod);
            boolean setterUsed =
                kotlinPropertyMetadata.referencedSetterMethod != null &&
                isUsed(kotlinPropertyMetadata.referencedSetterMethod);

            if (backingFieldUsed || getterUsed || setterUsed)
            {
                markAsUsed(kotlinPropertyMetadata);
            }
        }

        if (isUsed(kotlinPropertyMetadata))
        {
            // TODO: Don't use JvmMethodSignature in ProGuard API.
            if (kotlinPropertyMetadata.flags.common.hasAnnotations &&
                true)//kotlinPropertyMetadata.syntheticMethodForAnnotations != null)
            {
                // Annotations are placed on a synthetic method (e.g. myProperty$annotations())
                // so we must ensure that the synthetic method is marked as used, if there
                // are any used annotations there.

                // TODO: Don't use SimpleUsageMarker in ProGuard API.
                KotlinAnnotationCounter annotationCounter = new KotlinAnnotationCounter();//javaUsageMarker);
                kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations.accept(
                    kotlinPropertyMetadata.referencedSyntheticMethodClass,
                    annotationCounter
                );

                if (annotationCounter.getCount() != 0)
                {
                    kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations.accept(
                        kotlinPropertyMetadata.referencedSyntheticMethodClass,
                        javaClassUsageMarker
                    );
                }
            }

            kotlinPropertyMetadata.versionRequirementAccept(clazz,
                                                            kotlinDeclarationContainerMetadata,
                                                            this);
        }
    }

    // Implementations for KotlinFunctionVisitor.

    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        if (!isUsed(kotlinFunctionMetadata))
        {
            if (isUsed(kotlinFunctionMetadata.referencedMethod))
            {
                markAsUsed(kotlinFunctionMetadata);
            }
        }

        if (isUsed(kotlinFunctionMetadata))
        {
            // Mark the required elements.
            markAsUsed(kotlinFunctionMetadata.receiverType);
            markAsUsed(kotlinFunctionMetadata.typeParameters);
            markAsUsed(kotlinFunctionMetadata.valueParameters);
            markAsUsed(kotlinFunctionMetadata.returnType);

            // If there is a corresponding default method and the user specifically kept this method, keep it as well.
            if (kotlinFunctionMetadata.referencedDefaultMethod != null &&
                (kotlinFunctionMetadata.referencedMethod.getProcessingFlags() & ProcessingFlags.DONT_SHRINK) != 0 &&
                !javaUsageMarker.isUsed(kotlinFunctionMetadata.referencedDefaultMethod))
            {
                kotlinFunctionMetadata.referencedDefaultMethod.accept(kotlinFunctionMetadata.referencedDefaultMethodClass, javaClassUsageMarker);
            }

            kotlinFunctionMetadata.receiverTypeAccept(   clazz, kotlinMetadata, this);
            kotlinFunctionMetadata.typeParametersAccept( clazz, kotlinMetadata, this);
            kotlinFunctionMetadata.valueParametersAccept(clazz, kotlinMetadata, this);
            kotlinFunctionMetadata.returnTypeAccept(     clazz, kotlinMetadata, this);
        }
    }

    @Override
    public void visitFunction(Clazz clazz,
                              KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                              KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        visitAnyFunction(clazz, kotlinDeclarationContainerMetadata, kotlinFunctionMetadata);

        // Non-abstract functions in interfaces should have default implementations, so keep it if the
        // user kept the original function.
        if (isUsed(kotlinFunctionMetadata))
        {
            if (kotlinDeclarationContainerMetadata.k == KotlinConstants.METADATA_KIND_CLASS &&
                ((KotlinClassKindMetadata)kotlinDeclarationContainerMetadata).flags.isInterface &&
                !kotlinFunctionMetadata.flags.modality.isAbstract &&
                (kotlinFunctionMetadata.referencedMethod.getProcessingFlags() & ProcessingFlags.DONT_SHRINK) != 0)
            {
                if (!isJavaClassUsed(kotlinFunctionMetadata.referencedDefaultImplementationMethodClass))
                {
                    kotlinFunctionMetadata.referencedDefaultImplementationMethodClass.accept(javaClassUsageMarker);
                }

                kotlinFunctionMetadata.referencedDefaultImplementationMethod.accept(kotlinFunctionMetadata.referencedDefaultImplementationMethodClass, javaClassUsageMarker);
            }
        }
    }

    // Implementations for KotlinTypeAliasVisitor.

    @Override
    public void visitTypeAlias(Clazz                              clazz,
                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                               KotlinTypeAliasMetadata            kotlinTypeAliasMetadata)
    {
        if (!isUsed(kotlinTypeAliasMetadata))
        {
            // Mark a type alias if its expandedType is used.
            kotlinTypeAliasMetadata.expandedTypeAccept(clazz, kotlinDeclarationContainerMetadata, this);

            if (isUsed(kotlinTypeAliasMetadata.expandedType))
            {
                markAsUsed(kotlinTypeAliasMetadata);
            }
        }

        if (isUsed(kotlinTypeAliasMetadata))
        {
            if (!isJavaClassUsed(clazz))
            {
                // Mark the class as used, using the javaClassUsageMarker
                // - this will also mark entries within the class like constant pool entries.
                clazz.accept(javaClassUsageMarker);
                clazz.accept(new ReferencedKotlinMetadataVisitor(this));
            }

            markAsUsed(kotlinTypeAliasMetadata.typeParameters);
            markAsUsed(kotlinTypeAliasMetadata.underlyingType);
            markAsUsed(kotlinTypeAliasMetadata.expandedType);

            kotlinTypeAliasMetadata.typeParametersAccept(    clazz, kotlinDeclarationContainerMetadata, this);
            kotlinTypeAliasMetadata.underlyingTypeAccept(    clazz, kotlinDeclarationContainerMetadata, this);
            kotlinTypeAliasMetadata.expandedTypeAccept(      clazz, kotlinDeclarationContainerMetadata, this);
            kotlinTypeAliasMetadata.versionRequirementAccept(clazz, kotlinDeclarationContainerMetadata, this);
        }
    }

    // Implementations for KotlinTypeVisitor.

    @Override
    public void visitAnyType(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata)
    {
        if (!isUsed(kotlinTypeMetadata))
        {
            if (kotlinTypeMetadata.className != null)
            {
                if (isJavaClassUsed(kotlinTypeMetadata.referencedClass))
                {
                    markAsUsed(kotlinTypeMetadata);
                }
            }
            else if (kotlinTypeMetadata.aliasName != null)
            {
                kotlinTypeMetadata.referencedTypeAlias.accept(kotlinTypeMetadata.referencedTypeAlias.referencedDeclarationContainer.ownerReferencedClass,
                                                              kotlinTypeMetadata.referencedTypeAlias.referencedDeclarationContainer,
                                                              this);

                if (isUsed(kotlinTypeMetadata.referencedTypeAlias))
                {
                    markAsUsed(kotlinTypeMetadata);
                }
            }
            else
            {
                markAsUsed(kotlinTypeMetadata);
            }
        }

        if (isUsed(kotlinTypeMetadata))
        {
            if (kotlinTypeMetadata.className != null && !isJavaClassUsed(kotlinTypeMetadata.referencedClass))
            {
                kotlinTypeMetadata.referencedClass.accept(javaClassUsageMarker);
            }
            else if (kotlinTypeMetadata.aliasName != null && !isUsed(kotlinTypeMetadata.referencedTypeAlias))
            {
                markAsUsed(kotlinTypeMetadata.referencedTypeAlias);
                kotlinTypeMetadata.referencedTypeAlias.accept(null, null, this);
            }

            markAsUsed(kotlinTypeMetadata.typeArguments);
            markAsUsed(kotlinTypeMetadata.upperBounds);
            markAsUsed(kotlinTypeMetadata.outerClassType);

            kotlinTypeMetadata.typeArgumentsAccept(clazz, this);
            kotlinTypeMetadata.upperBoundsAccept(  clazz, this);
            kotlinTypeMetadata.abbreviationAccept( clazz, this);
            kotlinTypeMetadata.annotationsAccept(  clazz, this);
        }
    }

    //Implementations for KotlinConstructorVisitor.

    @Override
    public void visitConstructor(Clazz                     clazz,
                                 KotlinClassKindMetadata   kotlinClassKindMetadata,
                                 KotlinConstructorMetadata kotlinConstructorMetadata)
    {
        if (!isUsed(kotlinConstructorMetadata))
        {
            if (isUsed(kotlinConstructorMetadata.referencedMethod))
            {
                markAsUsed(kotlinConstructorMetadata);
            }
        }

        if (isUsed(kotlinConstructorMetadata))
        {
            markAsUsed(kotlinConstructorMetadata.valueParameters);

            kotlinConstructorMetadata.valueParametersAccept(  clazz,  kotlinClassKindMetadata, this);
            kotlinConstructorMetadata.versionRequirementAccept(clazz, kotlinClassKindMetadata, this);
        }
    }

    //Implementations for KotlinTypeParameterVisitor.

    @Override
    public void visitAnyTypeParameter(Clazz clazz, KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
    {
        if (isUsed(kotlinTypeParameterMetadata))
        {
            markAsUsed(kotlinTypeParameterMetadata.upperBounds);
            kotlinTypeParameterMetadata.upperBoundsAccept(clazz, this);
            kotlinTypeParameterMetadata.annotationsAccept(clazz, this);
        }
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
        if (isUsed(kotlinValueParameterMetadata))
        {
            kotlinValueParameterMetadata.typeAccept(clazz,
                                                    kotlinMetadata,
                                                    kotlinFunctionMetadata,
                                                    this);
        }
    }

    @Override
    public void visitConstructorValParameter(Clazz                        clazz,
                                             KotlinClassKindMetadata      kotlinClassKindMetadata,
                                             KotlinConstructorMetadata    kotlinConstructorMetadata,
                                             KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        if (isUsed(kotlinValueParameterMetadata))
        {
            kotlinValueParameterMetadata.typeAccept(clazz,
                                                    kotlinClassKindMetadata,
                                                    kotlinConstructorMetadata,
                                                    this);
        }
    }

    @Override
    public void visitPropertyValParameter(Clazz                              clazz,
                                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                          KotlinPropertyMetadata             kotlinPropertyMetadata,
                                          KotlinValueParameterMetadata       kotlinValueParameterMetadata)
    {
        if (isUsed(kotlinValueParameterMetadata))
        {
            kotlinValueParameterMetadata.typeAccept(clazz,
                                                    kotlinDeclarationContainerMetadata,
                                                    kotlinPropertyMetadata,
                                                    this);
        }
    }

    // Implementations for KotlinVersionRequirementVisitor

    @Override
    public void visitAnyVersionRequirement(Clazz                            clazz,
                                           KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
    {
        markAsUsed(kotlinVersionRequirementMetadata);
    }

    // Implementations for KotlinAnnotationVisitor.

    @Override
    public void visitAnyAnnotation(Clazz                    clazz,
                                   KotlinMetadataAnnotation annotation)
    {
        if (!isUsed(annotation))
        {
            if (isJavaClassUsed(annotation.referencedAnnotationClass))
            {
                markAsUsed(annotation);
            }
        }
    }

    // Implementations for KotlinContractVisitor.

    @Override
    public void visitContract(Clazz clazz,
                              KotlinMetadata kotlinMetadata,
                              KotlinFunctionMetadata kotlinFunctionMetadata,
                              KotlinContractMetadata kotlinContractMetadata)
    {
        //TODO
        markAsUsed(kotlinContractMetadata);
        kotlinContractMetadata.effectsAccept(clazz, kotlinMetadata, kotlinFunctionMetadata, this);
    }

    // Implementations for KotlinEffectVisitor.

    @Override
    public void visitEffect(Clazz clazz,
                            KotlinMetadata kotlinMetadata,
                            KotlinFunctionMetadata kotlinFunctionMetadata,
                            KotlinContractMetadata kotlinContractMetadata,
                            KotlinEffectMetadata kotlinEffectMetadata)
    {
        //TODO
        markAsUsed(kotlinEffectMetadata);
        kotlinEffectMetadata.conclusionOfConditionalEffectAccept(clazz, this);
        kotlinEffectMetadata.constructorArgumentAccept(clazz, this);
    }

    // Implementations for KotlinEffectExpressionVisitor.

    @Override
    public void visitEffectExpression(Clazz                          clazz,
                                      KotlinEffectMetadata           kotlinEffectMetadata,
                                      KotlinEffectExpressionMetadata kotlinEffectExpressionMetadata)
    {
        //TODO
        markAsUsed(kotlinEffectExpressionMetadata);
        kotlinEffectExpressionMetadata.orRightHandSideAccept( clazz, kotlinEffectMetadata,this);
        kotlinEffectExpressionMetadata.andRightHandSideAccept(clazz, kotlinEffectMetadata, this);
        kotlinEffectExpressionMetadata.typeOfIsAccept(clazz, this);
    }

    // Small helper methods.

    private void markAsUsed(Processable metadataElement)
    {
        if (metadataElement != null)
        {
            javaUsageMarker.markAsUsed(metadataElement);
        }
    }

    private boolean isJavaClassUsed(Clazz clazz)
    {
        // Because Kotlin dummy classes (see ClassReferenceInitializer) won't be marked as used
        // we must also check the DONT_SHRINK flag.
        return isUsed(clazz) || ((clazz.getProcessingFlags() & ProcessingFlags.DONT_SHRINK) != 0);
    }

    private void markAsUsed(List<? extends Processable> metadataElements)
    {
        metadataElements.forEach(this::markAsUsed);
    }

    public boolean isUsed(Processable metadataElement)
    {
        return javaUsageMarker.isUsed(metadataElement);
    }
}
