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
package proguard.classfile.kotlin.visitors.filter;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitors.KotlinMetadataVisitor;

import java.util.function.Predicate;

/**
 * Delegate to another {@link KotlinMetadataVisitor} if the predicate returns true.
 *
 */
public class KotlinSyntheticClassKindFilter
implements   KotlinMetadataVisitor
{
    private final Predicate<KotlinSyntheticClassKindMetadata> predicate;
    private final KotlinMetadataVisitor                       kotlinMetadataVisitor;

    public KotlinSyntheticClassKindFilter(KotlinMetadataVisitor kotlinMetadataVisitor)
    {
        this(__ -> true, kotlinMetadataVisitor);
    }

    public KotlinSyntheticClassKindFilter(Predicate<KotlinSyntheticClassKindMetadata> predicate, KotlinMetadataVisitor kotlinMetadataVisitor) {
        this.predicate             = predicate;
        this.kotlinMetadataVisitor = kotlinMetadataVisitor;
    }


    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz                            clazz,
                                                  KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        if (this.predicate.test(kotlinSyntheticClassKindMetadata))
        {
            this.kotlinMetadataVisitor.visitKotlinSyntheticClassMetadata(clazz, kotlinSyntheticClassKindMetadata);
        }
    }

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    // Helper methods.

    public static boolean isLambda(KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        return kotlinSyntheticClassKindMetadata.kind == KotlinSyntheticClassKindMetadata.Kind.LAMBDA;
    }

    public static boolean isWhenMappings(KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        return kotlinSyntheticClassKindMetadata.kind == KotlinSyntheticClassKindMetadata.Kind.WHEN_MAPPINGS;
    }

    public static boolean isDefaultImpls(KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        return kotlinSyntheticClassKindMetadata.kind == KotlinSyntheticClassKindMetadata.Kind.DEFAULT_IMPLS;
    }
}
