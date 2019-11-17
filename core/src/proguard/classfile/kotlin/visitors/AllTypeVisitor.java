/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.*;
import proguard.classfile.kotlin.*;

public class AllTypeVisitor
implements   KotlinMetadataVisitor,
             KotlinPropertyVisitor,
             KotlinFunctionVisitor,
             KotlinTypeAliasVisitor,
             KotlinTypeParameterVisitor,
             KotlinConstructorVisitor,
             KotlinValueParameterVisitor,
             KotlinTypeVisitor,
             KotlinContractVisitor,
             KotlinEffectVisitor,
             KotlinEffectExprVisitor
{
    private final KotlinTypeVisitor delegate;

    public AllTypeVisitor(KotlinTypeVisitor delegate)
    {
        this.delegate = delegate;
    }

    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        kotlinDeclarationContainerMetadata.propertiesAccept(         clazz, this);
        kotlinDeclarationContainerMetadata.delegatedPropertiesAccept(clazz, this);
        kotlinDeclarationContainerMetadata.functionsAccept(          clazz, this);
        kotlinDeclarationContainerMetadata.typeAliasesAccept(        clazz,this);
    }

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        kotlinClassKindMetadata.typeParametersAccept(clazz, this);
        kotlinClassKindMetadata.superTypesAccept(    clazz, this);
        kotlinClassKindMetadata.constructorsAccept(  clazz, this);

        visitKotlinDeclarationContainerMetadata(clazz, kotlinClassKindMetadata);
    }

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz clazz, KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        kotlinSyntheticClassKindMetadata.functionsAccept(clazz, this);
    }

    // Implementations for KotlinPropertyVisitor.

    @Override
    public void visitAnyProperty(Clazz                              clazz,
                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                 KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        kotlinPropertyMetadata.typeParametersAccept(  clazz, kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.receiverTypeAccept(    clazz, kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.typeAccept(            clazz, kotlinDeclarationContainerMetadata, this);
        kotlinPropertyMetadata.setterParametersAccept(clazz, kotlinDeclarationContainerMetadata, this);
    }


    // Implementations for KotlinFunctionVisitor.

    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        kotlinFunctionMetadata.typeParametersAccept( clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.receiverTypeAccept(   clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.valueParametersAccept(clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.returnTypeAccept(     clazz, kotlinMetadata, this);
        kotlinFunctionMetadata.contractsAccept(      clazz, kotlinMetadata, this);
    }

    // Implementations for KotlinContractVisitor.
    @Override
    public void visitContract(Clazz clazz,
                              KotlinMetadata kotlinMetadata,
                              KotlinFunctionMetadata kotlinFunctionMetadata,
                              KotlinContractMetadata kotlinContractMetadata)
    {
        kotlinContractMetadata.effectsAccept(clazz, kotlinMetadata, kotlinFunctionMetadata, this);
    }


    // Implementations for KotlinEffectVisitor.
    public void visitEffect(Clazz                  clazz,
                            KotlinMetadata         kotlinMetadata,
                            KotlinFunctionMetadata kotlinFunctionMetadata,
                            KotlinContractMetadata kotlinContractMetadata,
                            KotlinEffectMetadata   kotlinEffectMetadata)
    {
        kotlinEffectMetadata.conclusionOfConditionalEffectAccept(clazz, this);
    }


    // Implementations for KotlinEffectExprVisitor.
    @Override
    public void visitEffectExpression(Clazz                          clazz,
                                      KotlinEffectMetadata           kotlinEffectMetadata,
                                      KotlinEffectExpressionMetadata kotlinEffectExpressionMetadata)
    {
        kotlinEffectExpressionMetadata.typeOfIsAccept(clazz, this);

        kotlinEffectExpressionMetadata.orRightHandSideAccept( clazz, kotlinEffectMetadata, this);
        kotlinEffectExpressionMetadata.andRightHandSideAccept(clazz, kotlinEffectMetadata, this);
    }

    // Implementations for KotlinConstructorVisitor.

    @Override
    public void visitConstructor(Clazz                     clazz,
                                 KotlinClassKindMetadata   kotlinClassKindMetadata,
                                 KotlinConstructorMetadata kotlinConstructorMetadata)
    {
        kotlinConstructorMetadata.valueParametersAccept(clazz, kotlinClassKindMetadata, this);
    }

    // Implementations for KotlinTypeAliasVisitor.

    @Override
    public void visitTypeAlias(Clazz                              clazz,
                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                               KotlinTypeAliasMetadata            kotlinTypeAliasMetadata)
    {
        kotlinTypeAliasMetadata.underlyingTypeAccept(clazz, kotlinDeclarationContainerMetadata, this);
        kotlinTypeAliasMetadata.expandedTypeAccept(  clazz, kotlinDeclarationContainerMetadata, this);
        kotlinTypeAliasMetadata.typeParametersAccept(clazz, kotlinDeclarationContainerMetadata, this);
    }

    // Implementations for KotlinValueParameterVisitor.
    @Override
    public void visitAnyValueParameter(Clazz                        clazz,
                                       KotlinValueParameterMetadata kotlinValueParameterMetadata) {}

    @Override
    public void visitFunctionValParameter(Clazz                        clazz,
                                          KotlinMetadata               kotlinMetadata,
                                          KotlinFunctionMetadata       kotlinFunctionMetadata,
                                          KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        kotlinValueParameterMetadata.typeAccept(clazz, kotlinMetadata, kotlinFunctionMetadata, this);
    }

    @Override
    public void visitConstructorValParameter(Clazz                        clazz,
                                             KotlinClassKindMetadata      kotlinClassKindMetadata,
                                             KotlinConstructorMetadata    kotlinConstructorMetadata,
                                             KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        kotlinValueParameterMetadata.typeAccept(clazz, kotlinClassKindMetadata, kotlinConstructorMetadata, this);
    }

    @Override
    public void visitPropertyValParameter(Clazz                              clazz,
                                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                          KotlinPropertyMetadata             kotlinPropertyMetadata,
                                          KotlinValueParameterMetadata       kotlinValueParameterMetadata)
    {
        kotlinValueParameterMetadata.typeAccept(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata, this);
    }

    // Implementations for KotlinTypeParameterVisitor.

    @Override
    public void visitAnyTypeParameter(Clazz clazz, KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
    {
        kotlinTypeParameterMetadata.upperBoundsAccept(clazz, this);
    }

    // Implementations for KotlinTypeVisitor.

    @Override
    public void visitAnyType(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata)
    {
        kotlinTypeMetadata.outerClassAccept(   clazz, this);
        kotlinTypeMetadata.upperBoundsAccept(  clazz, this);
        kotlinTypeMetadata.abbreviationAccept( clazz, this);
        kotlinTypeMetadata.typeArgumentsAccept(clazz, this);
    }

    @Override
    public void visitTypeUpperBound(Clazz              clazz,
                                    KotlinTypeMetadata boundedType,
                                    KotlinTypeMetadata upperBound)
    {
        delegate.visitTypeUpperBound(clazz, boundedType, upperBound);

        visitAnyType(clazz, upperBound);
    }

    @Override
    public void visitAbbreviation(Clazz              clazz,
                                  KotlinTypeMetadata abbreviatedType,
                                  KotlinTypeMetadata abbreviation)
    {
        delegate.visitAbbreviation(clazz, abbreviatedType, abbreviation);

        visitAnyType(clazz, abbreviation);
    }

    @Override
    public void visitParameterUpperBound(Clazz                       clazz,
                                         KotlinTypeParameterMetadata boundedTypeParameter,
                                         KotlinTypeMetadata          upperBound)
    {
        delegate.visitParameterUpperBound(clazz, boundedTypeParameter, upperBound);

        visitAnyType(clazz, upperBound);
    }

    @Override
    public void visitEffectExprTypeOfIs(Clazz                          clazz,
                                        KotlinEffectExpressionMetadata kotlinEffectExprMetadata,
                                        KotlinTypeMetadata             typeOfIs)
    {
        delegate.visitEffectExprTypeOfIs(clazz, kotlinEffectExprMetadata, typeOfIs);

        visitAnyType(clazz, typeOfIs);
    }

    @Override
    public void visitTypeArgument(Clazz              clazz,
                                  KotlinTypeMetadata kotlinTypeMetadata,
                                  KotlinTypeMetadata typeArgument)
    {
        delegate.visitTypeArgument(clazz, kotlinTypeMetadata, typeArgument);

        visitAnyType(clazz, typeArgument);
    }

    @Override
    public void visitStarProjection(Clazz              clazz,
                                    KotlinTypeMetadata typeWithStarArg)
    {
        delegate.visitStarProjection(clazz, typeWithStarArg);
    }

    @Override
    public void visitOuterClass(Clazz              clazz,
                                KotlinTypeMetadata innerClass,
                                KotlinTypeMetadata outerClass)
    {
        delegate.visitOuterClass(clazz, innerClass, outerClass);

        visitAnyType(clazz, outerClass);
    }

    @Override
    public void visitSuperType(Clazz clazz,
                               KotlinClassKindMetadata kotlinClassKindMetadata,
                               KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitSuperType(clazz, kotlinClassKindMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }

    @Override
    public void visitConstructorValParamType(Clazz clazz,
                                             KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                             KotlinConstructorMetadata kotlinConstructorMetadata,
                                             KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                             KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitConstructorValParamType(clazz, kotlinDeclarationContainerMetadata, kotlinConstructorMetadata, kotlinValueParameterMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }

    @Override
    public void visitConstructorValParamVarArgType(Clazz clazz,
                                                   KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                                   KotlinConstructorMetadata kotlinConstructorMetadata,
                                                   KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                                   KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitConstructorValParamType(clazz, kotlinDeclarationContainerMetadata, kotlinConstructorMetadata, kotlinValueParameterMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }

    @Override
    public void visitPropertyType(Clazz clazz,
                                  KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                  KotlinPropertyMetadata kotlinPropertyMetadata,
                                  KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitPropertyType(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }

    @Override
    public void visitPropertyReceiverType(Clazz clazz,
                                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                          KotlinPropertyMetadata kotlinPropertyMetadata,
                                          KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitPropertyReceiverType(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }

    @Override
    public void visitPropertyValParamType(Clazz clazz,
                                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                          KotlinPropertyMetadata kotlinPropertyMetadata,
                                          KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                          KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitPropertyValParamType(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata, kotlinValueParameterMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }

    @Override
    public void visitPropertyValParamVarArgType(Clazz clazz,
                                                KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                                KotlinPropertyMetadata kotlinPropertyMetadata,
                                                KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                                KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitPropertyValParamType(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata, kotlinValueParameterMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }

    @Override
    public void visitFunctionReturnType(Clazz clazz,
                                        KotlinMetadata kotlinMetadata,
                                        KotlinFunctionMetadata kotlinFunctionMetadata,
                                        KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitFunctionReturnType(clazz, kotlinMetadata, kotlinFunctionMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }

    @Override
    public void visitFunctionReceiverType(Clazz clazz,
                                          KotlinMetadata kotlinMetadata,
                                          KotlinFunctionMetadata kotlinFunctionMetadata,
                                          KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitFunctionReceiverType(clazz, kotlinMetadata, kotlinFunctionMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }

    @Override
    public void visitFunctionValParamType(Clazz clazz,
                                          KotlinMetadata kotlinMetadata,
                                          KotlinFunctionMetadata kotlinFunctionMetadata,
                                          KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                          KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitFunctionValParamType(clazz, kotlinMetadata, kotlinFunctionMetadata, kotlinValueParameterMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }

    @Override
    public void visitFunctionValParamVarArgType(Clazz clazz,
                                                KotlinMetadata kotlinMetadata,
                                                KotlinFunctionMetadata kotlinFunctionMetadata,
                                                KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                                KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitFunctionValParamVarArgType(clazz, kotlinMetadata, kotlinFunctionMetadata, kotlinValueParameterMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }

    @Override
    public void visitAliasUnderlyingType(Clazz clazz,
                                         KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                         KotlinTypeAliasMetadata kotlinTypeAliasMetadata,
                                         KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitAliasUnderlyingType(clazz, kotlinDeclarationContainerMetadata, kotlinTypeAliasMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }

    @Override
    public void visitAliasExpandedType(Clazz clazz,
                                       KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                       KotlinTypeAliasMetadata kotlinTypeAliasMetadata,
                                       KotlinTypeMetadata kotlinTypeMetadata)
    {
        delegate.visitAliasExpandedType(clazz, kotlinDeclarationContainerMetadata, kotlinTypeAliasMetadata, kotlinTypeMetadata);

        visitAnyType(clazz, kotlinTypeMetadata);
    }
}