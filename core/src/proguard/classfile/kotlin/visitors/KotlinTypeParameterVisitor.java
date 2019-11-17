/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;

public interface KotlinTypeParameterVisitor
{
    void visitAnyTypeParameter(Clazz                       clazz,
                               KotlinTypeParameterMetadata kotlinTypeParameterMetadata);


    default void visitClassTypeParameter(Clazz                       clazz,
                                         KotlinMetadata              kotlinMetadata,
                                         KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
    {
        visitAnyTypeParameter(clazz, kotlinTypeParameterMetadata);
    }

    default void visitPropertyTypeParameter(Clazz                              clazz,
                                            KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                            KotlinPropertyMetadata             kotlinPropertyMetadata,
                                            KotlinTypeParameterMetadata        kotlinTypeParameterMetadata)
    {
        visitAnyTypeParameter(clazz, kotlinTypeParameterMetadata);
    }


    default void visitFunctionTypeParameter(Clazz                       clazz,
                                            KotlinMetadata              kotlinMetadata,
                                            KotlinFunctionMetadata      kotlinFunctionMetadata,
                                            KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
    {
        visitAnyTypeParameter(clazz, kotlinTypeParameterMetadata);
    }


    default void visitAliasTypeParameter(Clazz                              clazz,
                                         KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                         KotlinTypeAliasMetadata            kotlinTypeAliasMetadata,
                                         KotlinTypeParameterMetadata        kotlinTypeParameterMetadata)
    {
        visitAnyTypeParameter(clazz, kotlinTypeParameterMetadata);
    }
}
