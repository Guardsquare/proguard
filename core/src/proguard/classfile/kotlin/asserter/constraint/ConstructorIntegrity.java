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
import proguard.classfile.kotlin.visitors.KotlinConstructorVisitor;

public class   ConstructorIntegrity
    extends    SimpleConstraintChecker
    implements KotlinConstructorVisitor,
               ConstraintChecker
{
    public static KotlinMetadataConstraint constraint()
    {
        return KotlinMetadataConstraint.makeFromConstructor(new ConstructorIntegrity());
    }


    public void visitConstructor(Clazz                     clazz,
                                 KotlinClassKindMetadata   kotlinClassKindMetadata,
                                 KotlinConstructorMetadata kotlinConstructorMetadata)
    {
        AssertUtil util = new AssertUtil("Constructor", clazz, kotlinClassKindMetadata, reporter);

        if (!kotlinClassKindMetadata.flags.isAnnotationClass)
        {
            util.reportIfMethodDangling(clazz, kotlinConstructorMetadata.referencedMethod, "constructor method");
        }
    }
}
