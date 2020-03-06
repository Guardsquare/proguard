/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.util.kotlin.asserter.constraint;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.resources.kotlinmodule.KotlinModule;
import proguard.util.kotlin.asserter.Reporter;

/**
 * Implementations of this class represent a conceptual constraint on KotlinMetadata.
 *
 * A KotlinMetadataConstraint is checked in the context of ClassPools and a specific KotlinMetadata
 * instance, and should report its findings to the passed Reporter.
 */
public interface KotlinAsserterConstraint
{
    void check(Reporter reporter, Clazz clazz, KotlinMetadata kotlinMetadata);

    void check(Reporter reporter, KotlinModule kotlinModule);
}
