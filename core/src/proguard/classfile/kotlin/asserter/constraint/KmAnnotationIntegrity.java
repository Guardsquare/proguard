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
public class KmAnnotationIntegrity
extends    SimpleConstraintChecker
implements ConstraintChecker,
           KotlinTypeVisitor,
           KotlinTypeAliasVisitor,
           KotlinTypeParameterVisitor
{
    public static KotlinMetadataConstraint constraint()
    {
        KmAnnotationIntegrity kmAnnotationIntegrity = new KmAnnotationIntegrity();

        return KotlinMetadataConstraint.makeConstraint(
            kmAnnotationIntegrity,

            new MultiKotlinMetadataVisitor(
                new AllTypeVisitor(         kmAnnotationIntegrity),
                new AllTypeAliasVisitor(    kmAnnotationIntegrity),
                new AllTypeParameterVisitor(kmAnnotationIntegrity)
            ));
    }


    // Implementations for KotlinTypeVisitor.
    @Override
    public void visitAnyType(Clazz              clazz,
                             KotlinTypeMetadata type)
    {
        AssertUtil util = new AssertUtil("Type", clazz, null, reporter);

        if (!type.annotations.isEmpty())
        {
            type.annotations.forEach(
                antn ->
                {
                    util.reportIfNullReference(antn.referencedAnnotationClass,
                                               "annotation class");
                    antn.referencedArgumentMethods.values().forEach(util.reportIfNullReference("annotation method"));
                });
        }
    }


    // Implementations for KotlinTypeAliasVisitor.
    @Override
    public void visitTypeAlias(Clazz                              clazz,
                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                               KotlinTypeAliasMetadata            kotlinTypeAliasMetadata)
    {
        AssertUtil util = new AssertUtil("Type alias", clazz, null, reporter);

        if (!kotlinTypeAliasMetadata.annotations.isEmpty())
        {
            kotlinTypeAliasMetadata.annotations.forEach(
                antn ->
                {
                    util.reportIfNullReference(antn.referencedAnnotationClass,
                                               "annotation class");
                    antn.referencedArgumentMethods.values().forEach(util.reportIfNullReference("annotation method"));
                });

        }
    }


    // Implementations for KotlinTypeParameterVisitor.
    @Override
    public void visitAnyTypeParameter(Clazz                       clazz,
                                      KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
    {
        AssertUtil util = new AssertUtil("Type alias", clazz, null, reporter);

        if (!kotlinTypeParameterMetadata.annotations.isEmpty())
        {
            kotlinTypeParameterMetadata.annotations.forEach(
                antn ->
                {
                    util.reportIfNullReference(antn.referencedAnnotationClass,
                                               "annotation class");
                    antn.referencedArgumentMethods.values().forEach(util.reportIfNullReference("annotation method"));
                });

        }
    }
}
