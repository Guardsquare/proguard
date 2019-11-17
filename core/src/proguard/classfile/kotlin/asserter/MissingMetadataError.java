/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.asserter;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinMetadata;

public class MissingMetadataError
extends KotlinMetadataError
{
    private final String         parentElement;
    private final String         missingMetadata;


    public MissingMetadataError(String         parentElement,
                                String         missingMetadata,
                                Clazz          clazz,
                                KotlinMetadata kotlinMetadata)
    {
        super(clazz, kotlinMetadata);
        this.parentElement   = parentElement;
        this.missingMetadata = missingMetadata;
    }


    public String errorDescription()
    {
        return parentElement + " has no " + missingMetadata + ".";
    }
}