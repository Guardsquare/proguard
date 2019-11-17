/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.asserter;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;

public class UnexpectedFlagError
extends KotlinMetadataError
{
    private final String         unexpectedElement;
    private final String         unexpectedFlag;
    private final boolean        actualValue;


    public UnexpectedFlagError(String         unexpectedElement,
                               String         unexpectedFlag,
                               boolean        actualValue,
                               Clazz          clazz,
                               KotlinMetadata kotlinMetadata)
    {
        super(clazz, kotlinMetadata);
        this.unexpectedElement = unexpectedElement;
        this.unexpectedFlag    = unexpectedFlag;
        this.actualValue       = actualValue;
    }

    public String errorDescription()
    {
        return unexpectedElement+ " has unexpected flag value "+actualValue+" for "+unexpectedFlag+".";
    }
}