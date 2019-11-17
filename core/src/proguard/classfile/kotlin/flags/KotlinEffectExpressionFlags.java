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
 * No valid common flags.
 */
public class KotlinEffectExpressionFlags extends KotlinFlags
{
    /**
     * Signifies that the corresponding effect expression should be negated to compute the proposition or the conclusion of an effect.
     */
    public boolean isNegated;

    /**
     * Signifies that the corresponding effect expression checks whether a value of some variable is `null`.
     */
    public boolean isNullCheckPredicate;

    public KotlinEffectExpressionFlags(int flags)
    {
        setFlags(flags);
    }


    protected Map<Flag, FlagValue> getOwnProperties()
    {
        HashMap<Flag, FlagValue> map = new HashMap<>();
        map.put(Flag.EffectExpression.IS_NEGATED,              new FlagValue(() -> isNegated,            newValue -> isNegated = newValue));
        map.put(Flag.EffectExpression.IS_NULL_CHECK_PREDICATE, new FlagValue(() -> isNullCheckPredicate, newValue -> isNullCheckPredicate = newValue));
        return map;
    }

}
