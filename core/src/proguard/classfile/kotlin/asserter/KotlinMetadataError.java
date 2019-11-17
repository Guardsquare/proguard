/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.asserter;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinMetadata;

/**
 * A class to represent errors in the Kotlin Metadata
 */
public abstract class KotlinMetadataError
{
    public final Clazz          clazz;
    public final KotlinMetadata kotlinMetadata;


    public KotlinMetadataError(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        this.clazz          = clazz;
        this.kotlinMetadata = kotlinMetadata;
    }


    public abstract String errorDescription();


    public String toString()
    {
        return "Kotlin Metadata Error in class " + clazz.getName() + ": " + errorDescription();
    }
}
