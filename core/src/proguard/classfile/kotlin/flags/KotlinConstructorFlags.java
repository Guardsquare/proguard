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
 * Flags for Kotlin constructors.
 *
 * Valid common flags:
 *   - hasAnnotations
 *   - isInternal
 *   - isPrivate
 *   - isProtected
 *   - isPublic
 *   - isPrivateToThis
 *   - isLocal
 */
public class KotlinConstructorFlags extends KotlinFlags
{

    public KotlinCommonFlags     common     = new KotlinCommonFlags();
    public KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();

    protected List<KotlinFlags> getChildren()
    {
        return Arrays.asList(visibility, common);
    }


    /**
     * Signifies that the corresponding constructor is the primary constructor.
     */
    public boolean isPrimary;

    public KotlinConstructorFlags(int flags)
    {
        setFlags(flags);
    }


    protected Map<Flag, FlagValue> getOwnProperties()
    {
        HashMap<Flag, FlagValue> map = new HashMap<>();
        map.put(Flag.Constructor.IS_PRIMARY, new FlagValue(() -> isPrimary, newValue -> isPrimary = newValue));
        return map;
    }
}
