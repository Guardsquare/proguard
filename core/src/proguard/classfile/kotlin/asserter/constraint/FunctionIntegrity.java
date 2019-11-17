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
public class FunctionIntegrity
extends      SimpleConstraintChecker
implements   ConstraintChecker,
             KotlinFunctionVisitor,
             KotlinValueParameterVisitor
{
    public static KotlinMetadataConstraint constraint()
    {
        return KotlinMetadataConstraint.makeFromFunction(new FunctionIntegrity());
    }

    // Implementations for KotlinFunctionVisitor.
    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        AssertUtil util = new AssertUtil("Function", clazz, kotlinMetadata, reporter);

        if (kotlinFunctionMetadata.jvmSignature == null)
        {
            reporter.report(new MyMissingMetadataError(clazz,
                                                       kotlinMetadata));
        }

        if (kotlinFunctionMetadata.referencedMethodClass == null)
        {
            reporter.report(new MyMissingReferenceError("method class",
                                                        clazz,
                                                        kotlinMetadata));
        }

        if (kotlinFunctionMetadata.referencedMethod == null)
        {
            reporter.report(new MyMissingReferenceError("method",
                                                        clazz,
                                                        kotlinMetadata));
        }

        if (kotlinFunctionMetadata.referencedMethod      != null &&
            kotlinFunctionMetadata.referencedMethodClass != null)
        {
            util.reportIfMethodDangling(kotlinFunctionMetadata.referencedMethodClass,
                                        kotlinFunctionMetadata.referencedMethod,
                                        "referenced method");
        }

        if (kotlinFunctionMetadata.referencedDefaultMethod      != null &&
            kotlinFunctionMetadata.referencedDefaultMethodClass != null)
        {
            util.reportIfMethodDangling(
                kotlinFunctionMetadata.referencedDefaultMethodClass,
                kotlinFunctionMetadata.referencedDefaultMethod,
                "referenced default method");
        }

        if (kotlinFunctionMetadata.referencedDefaultImplementationMethod      != null &&
            kotlinFunctionMetadata.referencedDefaultImplementationMethodClass != null)
        {
            util.reportIfMethodDangling(
                kotlinFunctionMetadata.referencedDefaultImplementationMethodClass,
                kotlinFunctionMetadata.referencedDefaultImplementationMethod,
                "referenced default implementation method");
        }

        // If any parameter has a hasDefault flag, then there should be a corresponding $default method.
        hasDefaults = false;
        kotlinFunctionMetadata.valueParametersAccept(clazz, kotlinMetadata, this);
        if (hasDefaults)
        {
            boolean hasDefaultMethod =
                kotlinFunctionMetadata.referencedDefaultMethod != null &&
                kotlinFunctionMetadata.referencedDefaultMethod.getName(kotlinFunctionMetadata.referencedDefaultMethodClass)
                    .equals(kotlinFunctionMetadata.referencedMethod.getName(kotlinFunctionMetadata.referencedMethodClass) + "$default");

            if (!hasDefaultMethod)
            {
                reporter.report(new MyMissingReferenceError(
                    kotlinFunctionMetadata.name + "$default method [" + kotlinFunctionMetadata.jvmSignature + "]",
                    clazz,
                    kotlinMetadata));
            }
        }

        // If the function is non-abstract and in an interface,
        // then there must be a default implementation in the $DefaultImpls class.
        if (!kotlinFunctionMetadata.flags.modality.isAbstract &&
            kotlinMetadata.k == KotlinConstants.METADATA_KIND_CLASS)
        {
            KotlinClassKindMetadata kotlinClassKindMetadata = (KotlinClassKindMetadata)kotlinMetadata;
            if (kotlinClassKindMetadata.flags.isInterface)
            {
                util.reportIfNullReference(kotlinFunctionMetadata.referencedDefaultImplementationMethod, "default implementation method");
                util.reportIfNullReference(kotlinFunctionMetadata.referencedDefaultImplementationMethodClass, "default implementation method class");
            }
        }

    }

    // Implementations for KotlinValueParameterVisitor.

    private boolean hasDefaults = false;

    @Override
    public void visitAnyValueParameter(Clazz                         clazz,
                                       KotlinValueParameterMetadata  kotlinValueParameterMetadata)
    {
        hasDefaults |= kotlinValueParameterMetadata.flags.hasDefaultValue;
    }

    private static class MyMissingMetadataError
    extends MissingMetadataError
    {
        MyMissingMetadataError(Clazz          clazz,
                               KotlinMetadata kotlinMetadata)
        {
            super("Function", "JVM signature", clazz, kotlinMetadata);
        }
    }

    private static class MyMissingReferenceError
    extends MissingReferenceError
    {
        MyMissingReferenceError(String         missingPart,
                                Clazz          clazz,
                                KotlinMetadata kotlinMetadata)
        {
            super("Function", missingPart, clazz, kotlinMetadata);
        }
    }
}
