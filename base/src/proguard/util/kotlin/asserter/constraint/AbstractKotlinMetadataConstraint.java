/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.util.kotlin.asserter.constraint;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.resources.kotlinmodule.KotlinModule;
import proguard.util.kotlin.asserter.Reporter;

public abstract class AbstractKotlinMetadataConstraint
implements KotlinAsserterConstraint,
           KotlinMetadataVisitor
{
    protected Reporter  reporter;


    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void check(Reporter reporter, Clazz clazz, KotlinMetadata metadata)
    {
        this.reporter = reporter;

        try
        {
            metadata.accept(clazz, this);
        }
        catch (Exception e)
        {
            reporter.report( "Encountered unexpected Exception when checking constraint: " + e.getMessage());
        }
    }

    @Override
    public void check(Reporter reporter, KotlinModule kotlinModule)
    {

    }
}
