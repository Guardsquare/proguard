/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.kotlin.flags;


import kotlinx.metadata.*;
import kotlinx.metadata.jvm.JvmFlag;

import java.util.*;

/**
 * Flags for Kotlin properties.
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
public class KotlinPropertyFlags extends KotlinFlags
{

    public KotlinCommonFlags     common     = new KotlinCommonFlags();
    public KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();
    public KotlinModalityFlags   modality   = new KotlinModalityFlags();

    protected List<KotlinFlags> getChildren()
    {
        return Arrays.asList(common,visibility,modality);
    }


    /**
     * A member kind flag, signifying that the corresponding property is explicitly declared in the containing class.
     */
    public boolean isDeclared;

    /**
     * A member kind flag, signifying that the corresponding property exists in the containing class because a property with a suitable
     * signature exists in a supertype. This flag is not written by the Kotlin compiler and its effects are unspecified.
     */
    public boolean isFakeOverride;

    /**
     * A member kind flag, signifying that the corresponding property exists in the containing class because it has been produced
     * by interface delegation (delegation "by"). Kotlinc never writes this flag.
     */
    //TODO It is unclear when this flag is written. Delegated properties have the IS_DELEGATED flag set but not this one.
    public boolean isDelegation;

    /**
     * A member kind flag, signifying that the corresponding property exists in the containing class because it has been synthesized
     * by the compiler and has no declaration in the source code.
     */
    public boolean isSynthesized;

    /**
     * Signifies that the corresponding property is `var`.
     */
    public boolean isVar;

    /**
     * Signifies that the corresponding property has a getter.
     */
    public boolean hasGetter;

    /**
     * Signifies that the corresponding property has a setter.
     */
    public boolean hasSetter;

    /**
     * Signifies that the corresponding property is `const`.
     */
    public boolean isConst;

    /**
     * Signifies that the corresponding property is `lateinit`.
     */
    public boolean isLateinit;

    /**
     * Signifies that the corresponding property has a constant value. On JVM, this flag allows an optimization similarly to
     * [F.HAS_ANNOTATIONS]: constant values of properties are written to the bytecode directly, and this flag can be used to avoid
     * reading the value from the bytecode in case there isn't one.
     */
    public boolean hasConstant;

    /**
     * Signifies that the corresponding property is `external`.
     */
    public boolean isExternal;

    /**
     * Signifies that the corresponding property is a delegated property.
     */
    public boolean isDelegated;

    /**
     * Signifies that the corresponding property is `expect`.
     */
    public boolean isExpect;

    //JVM Specific flags

    /**
     * Signifies that its backing field is declared as a static field in an interface,
     * usually happens when @JvmField annotation is used e.g.
     * <pre>
     * interface A {
     *     companion object {
     *          @JvmField
     *          val s:String = "string"
     *     }
     * }
     * </pre>
     */
    public boolean isMovedFromInterfaceCompanion;

    public KotlinPropertyFlags(int flags)
    {
        setFlags(flags);
    }


    protected Map<Flag, FlagValue> getOwnProperties()
    {
        HashMap<Flag, FlagValue> map = new HashMap<>();
        map.put(Flag.Property.IS_DECLARATION,   new FlagValue(() -> isDeclared,     newValue -> isDeclared = newValue));
        map.put(Flag.Property.IS_FAKE_OVERRIDE, new FlagValue(() -> isFakeOverride, newValue -> isFakeOverride = newValue));
        map.put(Flag.Property.IS_DELEGATION,    new FlagValue(() -> isDelegation,   newValue -> isDelegation = newValue));
        map.put(Flag.Property.IS_SYNTHESIZED,   new FlagValue(() -> isSynthesized,  newValue -> isSynthesized = newValue));
        map.put(Flag.Property.IS_VAR,           new FlagValue(() -> isVar,          newValue -> isVar = newValue));
        map.put(Flag.Property.HAS_GETTER,       new FlagValue(() -> hasGetter,      newValue -> hasGetter = newValue));
        map.put(Flag.Property.HAS_SETTER,       new FlagValue(() -> hasSetter,      newValue -> hasSetter = newValue));
        map.put(Flag.Property.IS_CONST,         new FlagValue(() -> isConst,        newValue -> isConst = newValue));
        map.put(Flag.Property.IS_LATEINIT,      new FlagValue(() -> isLateinit,     newValue -> isLateinit = newValue));
        map.put(Flag.Property.HAS_CONSTANT,     new FlagValue(() -> hasConstant,    newValue -> hasConstant = newValue));
        map.put(Flag.Property.IS_EXTERNAL,      new FlagValue(() -> isExternal,     newValue -> isExternal = newValue));
        map.put(Flag.Property.IS_DELEGATED,     new FlagValue(() -> isDelegated,    newValue -> isDelegated = newValue));
        map.put(Flag.Property.IS_EXPECT,        new FlagValue(() -> isExpect,       newValue -> isExpect = newValue));
        map.put(JvmFlag.Property.IS_MOVED_FROM_INTERFACE_COMPANION,
                new FlagValue(() -> isMovedFromInterfaceCompanion, newValue -> {/*not allowed for JVM property*/}));
        return map;
    }


    public void setJvmFlags(int jvmFlags)
    {
        isMovedFromInterfaceCompanion = JvmFlag.Property.IS_MOVED_FROM_INTERFACE_COMPANION.invoke(jvmFlags);
    }
}
