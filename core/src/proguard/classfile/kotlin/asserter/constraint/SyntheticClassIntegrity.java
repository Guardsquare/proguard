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
package proguard.classfile.kotlin.asserter.constraint;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.asserter.*;
import proguard.classfile.kotlin.visitors.KotlinMetadataVisitor;

import static proguard.classfile.kotlin.KotlinConstants.*;

public class SyntheticClassIntegrity
extends      SimpleConstraintChecker
implements   KotlinMetadataVisitor,
             ConstraintChecker
{

    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    public static KotlinMetadataConstraint constraint()
    {
        return KotlinMetadataConstraint.make(new SyntheticClassIntegrity());
    }

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz                            clazz,
                                                  KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        switch (kotlinSyntheticClassKindMetadata.kind)
        {
            case DEFAULT_IMPLS:
                checkSuffix(clazz, kotlinSyntheticClassKindMetadata, DEFAULT_IMPLEMENTATIONS_SUFFIX, reporter);
                break;
            case WHEN_MAPPINGS:
                checkSuffix(clazz, kotlinSyntheticClassKindMetadata, WHEN_MAPPINGS_SUFFIX, reporter);
                break;
            case LAMBDA:
                // Other synthetic classes are created from lambdas.
                try {
                    Integer.parseInt(
                        kotlinSyntheticClassKindMetadata.referencedClass.getName().substring(
                            kotlinSyntheticClassKindMetadata.referencedClass.getName().lastIndexOf("$") + 1));
                }
                catch (NumberFormatException e)
                {
                    reporter.report(new KotlinMetadataError(clazz, kotlinSyntheticClassKindMetadata)
                    {
                        public String errorDescription()
                        {
                            return "Synthetic lambda inner classname is not an integer.";
                        }
                    });
                }

                if (kotlinSyntheticClassKindMetadata.functions.isEmpty())
                {
                    reporter.report(new KotlinMetadataError(clazz, kotlinSyntheticClassKindMetadata)
                    {
                        public String errorDescription()
                                                            {
                                                               return "Synthetic class has no functions";
                                                                                                         }
                    });
                }
                else if (kotlinSyntheticClassKindMetadata.functions.size() > 1)
                {
                    reporter.report(new KotlinMetadataError(clazz, kotlinSyntheticClassKindMetadata)
                    {
                        public String errorDescription()
                        {
                            return "Synthetic class has multiple functions";
                        }
                    });
                }
                break;
            case UNKNOWN:
        }
    }

    // Helper methods.

    private static void checkSuffix(Clazz                            clazz,
                                    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata,
                                    String                           suffix,
                                    Reporter                         reporter)
    {
        if (!kotlinSyntheticClassKindMetadata.referencedClass.getName().endsWith(suffix))
        {
            reporter.report(new KotlinMetadataError(clazz, kotlinSyntheticClassKindMetadata)
            {
                public String errorDescription()
                {
                    return "Synthetic class name does not end with " + suffix;
                }
            });
        }
    }
}
