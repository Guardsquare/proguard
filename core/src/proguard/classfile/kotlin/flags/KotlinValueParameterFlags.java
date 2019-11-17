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
 * Flags for Kotlin value parameters.
 *
 * No valid visibility or modality flags.
 *
 * hasAnnotations is valid.
 */
public class KotlinValueParameterFlags extends KotlinFlags
{
    public KotlinCommonFlags common = new KotlinCommonFlags();


    protected List<KotlinFlags> getChildren()
    {
        return Arrays.asList(common);
    }

    /**
     * Signifies that the corresponding value parameter declares a default value. Note that the default value itself can be a complex
     * expression and is not available via metadata. Also note that in case of an override of a parameter with default value, the
     * parameter in the derived method does _not_ declare the default value ([DECLARES_DEFAULT_VALUE] == false), but the parameter is
     * still optional at the call site because the default value from the base method is used.
     */
    public boolean hasDefaultValue;

    /**
     * Signifies that the corresponding value parameter is `crossinline`.
     */
    public boolean isCrossInline;

    /**
     * Signifies that the corresponding value parameter is `noinline`.
     */
    public boolean isNoInline;

    public KotlinValueParameterFlags(int flags)
    {
        setFlags(flags);
    }


    protected Map<Flag, FlagValue> getOwnProperties()
    {
        HashMap<Flag, FlagValue> map = new HashMap<>();
        map.put(Flag.ValueParameter.DECLARES_DEFAULT_VALUE, new FlagValue(() -> hasDefaultValue, newValue -> hasDefaultValue = newValue));
        map.put(Flag.ValueParameter.IS_CROSSINLINE,         new FlagValue(() -> isCrossInline,   newValue -> isCrossInline = newValue));
        map.put(Flag.ValueParameter.IS_NOINLINE,            new FlagValue(() -> isNoInline,      newValue -> isNoInline = newValue));
        return map;
    }
}
