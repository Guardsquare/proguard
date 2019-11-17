/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.flags;

import kotlinx.metadata.*;

import java.util.*;

/**
 * Flags for Kotlin type parameters.
 *
 * No valid visibility or modality flags.
 *
 * hasAnnotation is valid.
 */
public class KotlinTypeParameterFlags extends KotlinFlags
{
    public KotlinCommonFlags common = new KotlinCommonFlags();


    protected List<KotlinFlags> getChildren()
    {
        return Arrays.asList(common);
    }


    /**
     * Signifies that the corresponding type parameter is `reified`.
     */
    public boolean isReified;

    public KotlinTypeParameterFlags(int flags)
    {
        setFlags(flags);
    }


    protected Map<Flag, FlagValue> getOwnProperties()
    {
        HashMap<Flag, FlagValue> map = new HashMap<>();
        map.put(Flag.TypeParameter.IS_REIFIED, new FlagValue(() -> isReified, newValue -> isReified = newValue));
        return map;
    }
}
