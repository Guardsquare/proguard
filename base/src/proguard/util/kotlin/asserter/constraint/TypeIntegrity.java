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

/**
 * This class checks the assumption: All properties need a JVM signature for their getter
 */
public class TypeIntegrity
extends    AbstractKotlinMetadataConstraint
    implements KotlinTypeVisitor
{

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        kotlinMetadata.accept(clazz, new AllTypeVisitor(this));
    }


    // Implementations for KotlinPropertyVisitor.
    @Override
    public void visitAnyType(Clazz clazz,
                             KotlinTypeMetadata type)
    {
        AssertUtil util = new AssertUtil("Type", reporter);

        if (type.className != null)
        {
            util.reportIfNullReference("class \"" + type.className + "\"", type.referencedClass);

            if (type.aliasName != null)
            {
                reporter.report("Type cannot have both className (" + type.className + ") and aliasName (" + type.aliasName + ")");
            }

            if (type.typeParamID >= 0)
            {
                reporter.report("Type cannot have both className (" + type.className + ") and typeParamID (" + type.typeParamID + ")");
            }
        }

        if (type.aliasName != null)
        {
            util.reportIfNullReference("type alias \"" + type.aliasName + "\"", type.referencedTypeAlias);

            if (type.className != null)
            {
                reporter.report("Type cannot have both className (" + type.className + ") and aliasName (" + type.aliasName + ")");
            }

            if (type.typeParamID >= 0)
            {
                reporter.report("Type cannot have both aliasName (" + type.aliasName + ") and typeParamID (" + type.typeParamID + ")");
            }
        }
    }
}
