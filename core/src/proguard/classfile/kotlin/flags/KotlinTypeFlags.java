/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.flags;

import kotlinx.metadata.Flag;

import java.util.*;

/**
 * Flags for Kotlin types.
 *
 * No valid common visibility or modality flags.
 *
 * hasAnnotation is valid.
 */
public class KotlinTypeFlags extends KotlinFlags
{

    public KotlinCommonFlags common = new KotlinCommonFlags();


    protected List<KotlinFlags> getChildren()
    {
        return Arrays.asList(common);
    }


    /**
     * Signifies that the corresponding type is marked as nullable, i.e. has a question mark at the end of its notation.
     */
    public boolean isNullable;

    /**
     * Signifies that the corresponding type is `suspend`.
     */
    public boolean isSuspend;

    public KotlinTypeFlags(int flags)
    {
        setFlags(flags);
    }


    protected Map<Flag, FlagValue> getOwnProperties()
    {
        HashMap<Flag, FlagValue> map = new HashMap<>();
        map.put(Flag.Type.IS_NULLABLE, new FlagValue(() -> isNullable, newValue -> isNullable = newValue));
        map.put(Flag.Type.IS_SUSPEND,  new FlagValue(() -> isSuspend,  newValue -> isSuspend = newValue));
        return map;
    }
}
