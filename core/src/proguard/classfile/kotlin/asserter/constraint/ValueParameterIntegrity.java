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

import proguard.classfile.*;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.asserter.*;
import proguard.classfile.kotlin.visitors.*;

/**
 * This class checks the assumption: All functions need a JVM signature
 */
public class ValueParameterIntegrity
extends      SimpleConstraintChecker
implements   ConstraintChecker,
             KotlinValueParameterVisitor
{
    public static KotlinMetadataConstraint constraint()
    {
        return KotlinMetadataConstraint.makeFromValueParameter(new ValueParameterIntegrity());
    }


    // Implementations for KotlinValueParameterVisitor.
    @Override
    public void visitAnyValueParameter(Clazz                        clazz,
                                       KotlinValueParameterMetadata kotlinValueParameterMetadata) {}
    @Override
    public void visitConstructorValParameter(Clazz                        clazz,
                                             KotlinClassKindMetadata      kotlinClassKindMetadata,
                                             KotlinConstructorMetadata    kotlinConstructorMetadata,
                                             KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        check(clazz, kotlinClassKindMetadata, kotlinValueParameterMetadata);
    }

    @Override
    public void visitFunctionValParameter(Clazz                        clazz,
                                          KotlinMetadata               kotlinMetadata,
                                          KotlinFunctionMetadata       kotlinFunctionMetadata,
                                          KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        check(clazz, kotlinMetadata, kotlinValueParameterMetadata);
    }

    @Override
    public void visitPropertyValParameter(Clazz                              clazz,
                                          KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                          KotlinPropertyMetadata             kotlinPropertyMetadata,
                                          KotlinValueParameterMetadata       kotlinValueParameterMetadata)
    {
        check(clazz, kotlinDeclarationContainerMetadata, kotlinValueParameterMetadata);
    }


    // Small helper methods.

    private void check(Clazz                        clazz,
                       KotlinMetadata               kotlinMetadata,
                       KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        AssertUtil util = new AssertUtil("ValueParameter", clazz, kotlinMetadata, reporter);
        util.reportIfNullReference(kotlinValueParameterMetadata.type, "type");
    }
}
