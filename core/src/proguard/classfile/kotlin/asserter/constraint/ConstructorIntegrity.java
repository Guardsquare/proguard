/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
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
