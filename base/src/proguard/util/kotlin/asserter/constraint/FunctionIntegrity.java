/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.util.kotlin.asserter.constraint;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.*;
import proguard.util.kotlin.asserter.AssertUtil;

/**
 * This class checks the assumption: All functions need a JVM signature
 */
public class FunctionIntegrity
extends      AbstractKotlinMetadataConstraint
    implements KotlinFunctionVisitor
{

    private boolean hasDefaults = false;

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        kotlinMetadata.accept(clazz, new AllFunctionsVisitor(this));
    }

    // Implementations for KotlinFunctionVisitor.
    @Override
    public void visitAnyFunction(Clazz clazz,
                                 KotlinMetadata kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        AssertUtil util = new AssertUtil("Function " + kotlinFunctionMetadata.name, reporter);

        util.reportIfNull("jvmSignature", kotlinFunctionMetadata.jvmSignature);

        util.reportIfNullReference("referencedMethod", kotlinFunctionMetadata.referencedMethod);
        util.reportIfNullReference("referencedMethodClass", kotlinFunctionMetadata.referencedMethodClass);

        util.reportIfMethodDangling("referenced method",
                                    kotlinFunctionMetadata.referencedMethodClass,
                                    kotlinFunctionMetadata.referencedMethod);

        util.reportIfMethodDangling(
            "referenced default method",
            kotlinFunctionMetadata.referencedDefaultMethodClass,
            kotlinFunctionMetadata.referencedDefaultMethod);

        util.reportIfMethodDangling(
            "referenced default implementation method",
            kotlinFunctionMetadata.referencedDefaultImplementationMethodClass,
            kotlinFunctionMetadata.referencedDefaultImplementationMethod);

        // If any parameter has a hasDefault flag, then there should be a corresponding $default method.
        hasDefaults = false;
        kotlinFunctionMetadata.valueParametersAccept(clazz, kotlinMetadata, (_clazz, vp) -> hasDefaults |= vp.flags.hasDefaultValue);
        if (hasDefaults)
        {
            boolean hasDefaultMethod =
                kotlinFunctionMetadata.referencedDefaultMethod != null &&
                kotlinFunctionMetadata.referencedDefaultMethod.getName(kotlinFunctionMetadata.referencedDefaultMethodClass)
                    .equals(kotlinFunctionMetadata.referencedMethod.getName(kotlinFunctionMetadata.referencedMethodClass) + "$default");

            if (!hasDefaultMethod)
            {
                reporter.report(kotlinFunctionMetadata.name + "$default method not found [" + kotlinFunctionMetadata.jvmSignature + "]");
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
                util.reportIfNullReference("default implementation method", kotlinFunctionMetadata.referencedDefaultImplementationMethod);
                util.reportIfNullReference("default implementation method class", kotlinFunctionMetadata.referencedDefaultImplementationMethodClass);
            }
        }

    }
}
