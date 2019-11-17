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
import proguard.classfile.kotlin.visitors.*;

/**
 * This class checks the assumption: All properties need a JVM signature for their getter
 */
public class TypeIntegrity
extends    SimpleConstraintChecker
implements ConstraintChecker,
           KotlinTypeVisitor
{
    public static KotlinMetadataConstraint constraint()
    {
        return KotlinMetadataConstraint.makeFromType(new TypeIntegrity());
    }


    // Implementations for KotlinPropertyVisitor.
    @Override
    public void visitAnyType(Clazz              clazz,
                             KotlinTypeMetadata type)
    {
        AssertUtil util = new AssertUtil("Type", clazz, null, reporter);

        if (type.className != null)
        {
            util.reportIfNullReference(type.referencedClass,
                                       "class \"" + type.className + "\"");

            if (type.aliasName != null)
            {
                reporter.report(new AmbiguousTypeError(clazz, "class name", "alias name"));
            }

            if (type.typeParamID >= 0)
            {
                reporter.report(new AmbiguousTypeError(clazz, "class name", "param ID"));
            }
        }

        if (type.aliasName != null)
        {
            util.reportIfNullReference(type.referencedTypeAlias,
                                       "type alias");

            if (type.className != null)
            {
                reporter.report(new AmbiguousTypeError(clazz, "class name", "alias name"));
            }

            if (type.typeParamID >= 0)
            {
                reporter.report(new AmbiguousTypeError(clazz, "class name", "param ID"));
            }
        }
    }


    // Small helper classes.

    private static class AmbiguousTypeError
    extends KotlinMetadataError
    {
        private final String variant1;
        private final String variant2;

        AmbiguousTypeError(Clazz clazz,
                           String variant1,
                           String variant2)
        {
            super(clazz,null);
            this.variant1 = variant1;
            this.variant2 = variant2;
        }

        public String errorDescription()
        {
            return "Type is both "+variant1+" and "+variant2+".";
        }
    }
}
