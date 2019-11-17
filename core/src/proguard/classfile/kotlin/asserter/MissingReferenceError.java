/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.asserter;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinMetadata;

public class MissingReferenceError
extends KotlinMetadataError
{
    private final String         parentElement;
    private final String         missingReference;

    public MissingReferenceError(String         parentElement,
                                 String         missingReference,
                                 Clazz          clazz,
                                 KotlinMetadata kotlinMetadata)
    {
        super(clazz, kotlinMetadata);
        this.parentElement    = parentElement;
        this.missingReference = missingReference;
    }


    public String errorDescription()
    {
        return parentElement + " has no reference for its " + missingReference + ".";
    }
}