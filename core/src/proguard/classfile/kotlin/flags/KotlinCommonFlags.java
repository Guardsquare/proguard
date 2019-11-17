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
 * Common flags.
 */
public final class KotlinCommonFlags extends KotlinFlags
{
    /**
     * Valid for: class, constructor, function, synthetic function, property, property accessor, typeAlias, type, type
     * parameter, value parameter
     */
    public boolean hasAnnotations;


    protected Map<Flag, FlagValue> getOwnProperties()
    {
        HashMap<Flag, FlagValue> map = new HashMap<>();
        map.put(Flag.HAS_ANNOTATIONS, new FlagValue(() -> hasAnnotations, newValue -> hasAnnotations = newValue));
        return map;
    }

}
