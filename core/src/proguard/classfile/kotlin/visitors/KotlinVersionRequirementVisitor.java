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
