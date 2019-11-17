/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.asserter;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinMetadata;

public class InvalidReferenceError
extends KotlinMetadataError
{
    private final String         parentElement;
    private final String         checkedElementName;


    public InvalidReferenceError(String         parentElement,
                                 String         checkedElementName,
                                 Clazz          clazz,
                                 KotlinMetadata kotlinMetadata)
    {
        super(clazz, kotlinMetadata);
        this.parentElement      = parentElement;
        this.checkedElementName = checkedElementName;
    }

    public String errorDescription()
    {
        return parentElement + " has a dangling reference for its " + checkedElementName + ".";
    }
}