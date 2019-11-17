/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.asserter;

import proguard.classfile.ClassPool;

/**
 * Interface for classes which need an error reporter, used with SAM convention.
 */
public interface ConstraintChecker
{
    void setReporter(Reporter reporter);

    void setClassPools(ClassPool programClassPool, ClassPool libraryClassPool);
}
