/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.asserter;

import proguard.classfile.ClassPool;

public abstract class SimpleConstraintChecker
{
    protected Reporter  reporter;
    protected ClassPool programClassPool;
    protected ClassPool libraryClassPool;

    public void setReporter(Reporter reporter)
    {
        this.reporter = reporter;
    }


    public void setClassPools(ClassPool programClassPool, ClassPool libraryClassPool)
    {
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;
    }
}
