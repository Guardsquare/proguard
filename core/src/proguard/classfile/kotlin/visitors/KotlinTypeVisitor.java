/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;

//TODO how to visit abbreviated types?
public interface KotlinTypeVisitor
{
    void visitAnyType(Clazz              clazz,
                      KotlinTypeMetadata kotlinTypeMetadata);


    // Type nest.

    default void visitTypeUpperBound(Clazz              clazz,
                                     KotlinTypeMetadata boundedType,
                                     KotlinTypeMetadata upperBound)
    {
        visitAnyType(clazz, upperBound);
    }

    default void visitAbbreviation(Clazz              clazz,
                                   KotlinTypeMetadata kotlinTypeMetadata,
                                   KotlinTypeMetadata abbreviation)
    {
        visitAnyType(clazz, abbreviation);
    }

    default void visitParameterUpperBound(Clazz                       clazz,
                                          KotlinTypeParameterMetadata boundedTypeParameter,
                                          KotlinTypeMetadata          upperBound)
    {
        visitAnyType(clazz, upperBound);
    }

    default void visitEffectExprTypeOfIs(Clazz                          clazz,
                                         KotlinEffectExpressionMetadata kotlinEffectExprMetadata,
                                         KotlinTypeMetadata             typeOfIs)
    {
        visitAnyType(clazz, typeOfIs);
    }

    default void visitTypeArgument(Clazz              clazz,
                                   KotlinTypeMetadata kotlinTypeMetadata,
                                   KotlinTypeMetadata typeArgument)
    {
        visitAnyType(clazz, typeArgument);
    }

    default void visitStarProjection(Clazz              clazz,
                                     KotlinTypeMetadata typeWithStarArg)
    {
        // By default, ignore star projection visits.
    }

    default void visitOuterClass(Clazz              clazz,
                                 KotlinTypeMetadata innerClass,
                                 KotlinTypeMetadata outerClass)
    {
        visitAnyType(clazz, outerClass);
    }


    // Regular Kotlin class.

    default void visitSuperType(Clazz                   clazz,
                                KotlinClassKindMetadata kotlinMetadata,
                                KotlinTypeMetadata      kotlinTypeMetadata)
    {
        visitAnyType(clazz, kotlinTypeMetadata);
    }

    default void visitConstructorValParamType(Clazz                              clazz,
                                              KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                              KotlinConstructorMetadata          kotlinConstructorMetadata,
                                              KotlinValueParameterMetadata       kotlinValueParameterMetadata,
                                              KotlinTypeMetadata                 kotlinTypeMetadata)
    {
        visitAnyType(clazz, kotlinTypeMetadata);
    }

    default void visitConstructorValParamVarArgType(Clazz                              clazz,
                                                    KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                                    KotlinConstructorMetadata          kotlinConstructorMetadata,
                                                    KotlinValueParameterMetadata       kotlinValueParameterMetadata,
                                                    KotlinTypeMetadata                 kotlinTypeMetadata)
    {
        visitAnyType(clazz, kotlinTypeMetadata);
    }


    // Property.

    default void visitPropertyType(Clazz                             clazz,
                                  KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                  KotlinPropertyMetadata             kotlinPropertyMetadata,
                                  KotlinTypeMetadata                 kotlinTypeMetadata)
    {
        visitAnyType(clazz, kotlinTypeMetadata);
    }

    default void visitPropertyReceiverType(Clazz                             clazz,
                                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                          KotlinPropertyMetadata             kotlinPropertyMetadata,
                                          KotlinTypeMetadata                 kotlinTypeMetadata)
    {
        visitAnyType(clazz, kotlinTypeMetadata);
    }

    default void visitPropertyValParamType(Clazz                             clazz,
                                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                          KotlinPropertyMetadata             kotlinPropertyMetadata,
                                          KotlinValueParameterMetadata       kotlinValueParameterMetadata,
                                          KotlinTypeMetadata                 kotlinTypeMetadata)
    {
        visitAnyType(clazz, kotlinTypeMetadata);
    }

    default void visitPropertyValParamVarArgType(Clazz                             clazz,
                                                KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                                KotlinPropertyMetadata             kotlinPropertyMetadata,
                                                KotlinValueParameterMetadata       kotlinValueParameterMetadata,
                                                KotlinTypeMetadata                 kotlinTypeMetadata)
    {
        visitAnyType(clazz, kotlinTypeMetadata);
    }

    // Function.

    default void visitFunctionReturnType(Clazz                 clazz,
                                        KotlinMetadata         kotlinMetadata,
                                        KotlinFunctionMetadata kotlinFunctionMetadata,
                                        KotlinTypeMetadata     kotlinTypeMetadata)
    {
        visitAnyType(clazz, kotlinTypeMetadata);
    }

    default void visitFunctionReceiverType(Clazz                 clazz,
                                          KotlinMetadata         kotlinMetadata,
                                          KotlinFunctionMetadata kotlinFunctionMetadata,
                                          KotlinTypeMetadata     kotlinTypeMetadata)
    {
        visitAnyType(clazz, kotlinTypeMetadata);
    }

    default void visitFunctionValParamType(Clazz                       clazz,
                                          KotlinMetadata               kotlinMetadata,
                                          KotlinFunctionMetadata       kotlinFunctionMetadata,
                                          KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                          KotlinTypeMetadata           kotlinTypeMetadata)
    {
        visitAnyType(clazz, kotlinTypeMetadata);
    }

    default void visitFunctionValParamVarArgType(Clazz                       clazz,
                                                KotlinMetadata               kotlinMetadata,
                                                KotlinFunctionMetadata       kotlinFunctionMetadata,
                                                KotlinValueParameterMetadata kotlinValueParameterMetadata,
                                                KotlinTypeMetadata           kotlinTypeMetadata)
    {
        visitAnyType(clazz, kotlinTypeMetadata);
    }


    // Type Alias.

    default void visitAliasUnderlyingType(Clazz                             clazz,
                                         KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                         KotlinTypeAliasMetadata            kotlinTypeAliasMetadata,
                                         KotlinTypeMetadata                 underlyingType)
    {
        visitAnyType(clazz, underlyingType);
    }

    default void visitAliasExpandedType(Clazz                             clazz,
                                       KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                       KotlinTypeAliasMetadata            kotlinTypeAliasMetadata,
                                       KotlinTypeMetadata                 expandedType)
    {
        visitAnyType(clazz, expandedType);
    }
}
