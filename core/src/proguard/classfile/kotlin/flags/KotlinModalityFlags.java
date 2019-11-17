/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.flags;

import kotlinx.metadata.Flag;

import java.util.*;

public class KotlinModalityFlags extends KotlinFlags
{
    // Valid for: class, constructor, function, synthetic function, property (including getter + setter)

    /**
     * Signifies the declaration is 'final'
     */
    public boolean isFinal;

    /**
     * Signifies the declaration is 'open'
     */
    public boolean isOpen;

    /**
     * Signifies the declaration is 'abstract'
     */
    public boolean isAbstract;

    /**
     * Signifies the declaration is 'sealed'
     */
    public boolean isSealed;


    protected Map<Flag, FlagValue> getOwnProperties()
    {
        HashMap<Flag, FlagValue> map = new HashMap<>();
        map.put(Flag.IS_FINAL,    new FlagValue(() -> isFinal,    newValue -> isFinal = newValue));
        map.put(Flag.IS_OPEN,     new FlagValue(() -> isOpen,     newValue -> isOpen = newValue));
        map.put(Flag.IS_ABSTRACT, new FlagValue(() -> isAbstract, newValue -> isAbstract = newValue));
        map.put(Flag.IS_SEALED,   new FlagValue(() -> isSealed,   newValue -> isSealed = newValue));
        return map;
    }

}
