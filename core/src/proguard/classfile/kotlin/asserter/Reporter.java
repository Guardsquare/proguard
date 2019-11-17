/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.asserter;

/**
 * Interface for reporting errors, used with SAM convention.
 */
public interface Reporter
{
    void report(KotlinMetadataError error);

    void resetCounter();

    int getCount();
}
