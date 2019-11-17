/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;

public interface KotlinVersionRequirementVisitor
{
    void visitAnyVersionRequirement(Clazz clazz, KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata);

    default void visitClassVersionRequirement(Clazz                            clazz,
                                              KotlinMetadata                   kotlinMetadata,
                                              KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
    {
        visitAnyVersionRequirement(clazz, kotlinVersionRequirementMetadata);
    }

    default void visitConstructorVersionRequirement(Clazz                            clazz,
                                                    KotlinMetadata                   kotlinMetadata,
                                                    KotlinConstructorMetadata        kotlinConstructorMetadata,
                                                    KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
    {
        visitAnyVersionRequirement(clazz, kotlinVersionRequirementMetadata);
    }

    default void visitFunctionVersionRequirement(Clazz                            clazz,
                                                 KotlinMetadata                   kotlinMetadata,
                                                 KotlinFunctionMetadata           kotlinFunctionMetadata,
                                                 KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
    {
        visitAnyVersionRequirement(clazz, kotlinVersionRequirementMetadata);
    }

    default void visitPropertyVersionRequirement(Clazz                              clazz,
                                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                                 KotlinPropertyMetadata             kotlinPropertyMetadata,
                                                 KotlinVersionRequirementMetadata   kotlinVersionRequirementMetadata)
    {
        visitAnyVersionRequirement(clazz, kotlinVersionRequirementMetadata);
    }

    default void visitTypeAliasVersionRequirement(Clazz                            clazz,
                                                  KotlinMetadata                   kotlinMetadata,
                                                  KotlinTypeAliasMetadata          kotlinTypeAliasMetadata,
                                                  KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
    {
        visitAnyVersionRequirement(clazz, kotlinVersionRequirementMetadata);
    }
}
