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
 * Flags for Kotlin property accessors (getters/setters for properties).
 *
 * Valid common flags:
 *   - hasAnnotations
 *   - isInternal
 *   - isPrivate
 *   - isProtected
 *   - isPublic
 *   - isPrivateToThis
 *   - isLocal
 *   - isFinal
 *   - isOpen
 *   - isAbstract
 *   - isSealed
 */
public class KotlinPropertyAccessorFlags extends KotlinFlags
{

    public KotlinCommonFlags     common     = new KotlinCommonFlags();
    public KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();
    public KotlinModalityFlags   modality   = new KotlinModalityFlags();

    protected List<KotlinFlags> getChildren()
    {
        return Arrays.asList(common,visibility,modality);
    }


    /**
     * Signifies that the corresponding property is not default, i.e. it has a body and/or annotations in the source code.
     */
    public boolean isDefault;

    /**
     * Signifies that the corresponding property is `external`.
     */
    public boolean isExternal;

    /**
     * Signifies that the corresponding property is `inline`.
     */
    public boolean isInline;

    public KotlinPropertyAccessorFlags(int flags)
    {
        setFlags(flags);
    }


    protected Map<Flag, FlagValue> getOwnProperties()
    {
        HashMap<Flag, FlagValue> map = new HashMap<>();
        map.put(Flag.PropertyAccessor.IS_NOT_DEFAULT, new FlagValue(() -> isDefault,  newValue -> isDefault = newValue).negation());
        map.put(Flag.PropertyAccessor.IS_EXTERNAL,    new FlagValue(() -> isExternal, newValue -> isExternal = newValue));
        map.put(Flag.PropertyAccessor.IS_INLINE,      new FlagValue(() -> isInline,   newValue -> isInline = newValue));
        return map;
    }

}
