/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.util.kotlin.asserter.constraint;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.*;
import proguard.util.kotlin.asserter.AssertUtil;

public class ConstructorIntegrity
extends      AbstractKotlinMetadataConstraint
    implements KotlinConstructorVisitor
{

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        kotlinMetadata.accept(clazz, new AllConstructorsVisitor(this));
    }

    @Override
    public void visitConstructor(Clazz clazz,
                                 KotlinClassKindMetadata kotlinClassKindMetadata,
                                 KotlinConstructorMetadata kotlinConstructorMetadata)
    {
        if (!kotlinClassKindMetadata.flags.isAnnotationClass)
        {
            AssertUtil util = new AssertUtil("Constructor", reporter);
            util.reportIfNullReference("constructor referencedMethod", kotlinConstructorMetadata.referencedMethod);
            util.reportIfMethodDangling("constructor referencedMethod",
                                        clazz, kotlinConstructorMetadata.referencedMethod);
        }
    }
}
