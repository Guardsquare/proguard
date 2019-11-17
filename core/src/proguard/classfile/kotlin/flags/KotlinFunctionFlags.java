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

import java.util.*;

/**
 * Flags for Kotlin functions.
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
public class KotlinFunctionFlags extends KotlinFlags
{

    public KotlinCommonFlags     common     = new KotlinCommonFlags();
    public KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();
    public KotlinModalityFlags   modality   = new KotlinModalityFlags();

    protected List<KotlinFlags> getChildren()
    {
        return Arrays.asList(common,visibility,modality);
    }

    /**
     * A member kind flag, signifying that the corresponding function is explicitly declared in the containing class.
     */
    public boolean isDeclaration;

    /**
     * A member kind flag, signifying that the corresponding function exists in the containing class because a function with a suitable
     * signature exists in a supertype. This flag is not written by the Kotlin compiler and its effects are unspecified.
     */
    public boolean isFakeOverride;

    /**
     * A member kind flag, signifying that the corresponding function exists in the containing class because it has been produced
     * by interface delegation (delegation "by").
     */
    public boolean isDelegation;

    /**
     * A member kind flag, signifying that the corresponding function exists in the containing class because it has been synthesized
     * by the compiler and has no declaration in the source code.
     */
    public boolean isSynthesized;

    /**
     * Signifies that the corresponding function is `operator`.
     */
    public boolean isOperator;

    /**
     * Signifies that the corresponding function is `infix`.
     */
    public boolean isInfix;

    /**
     * Signifies that the corresponding function is `inline`.
     */
    public boolean isInline;

    /**
     * Signifies that the corresponding function is `tailrec`.
     */
    public boolean isTailrec;

    /**
     * Signifies that the corresponding function is `external`.
     */
    public boolean isExternal;

    /**
     * Signifies that the corresponding function is `suspend`.
     */
    public boolean isSuspend;

    /**
     * Signifies that the corresponding function is `expect`.
     */
    public boolean isExpect;

    public KotlinFunctionFlags(int flags)
    {
        setFlags(flags);
    }


    protected Map<Flag, FlagValue> getOwnProperties()
    {
        HashMap<Flag, FlagValue> map = new HashMap<>();
        map.put(Flag.Function.IS_DECLARATION,   new FlagValue(() -> isDeclaration,  newValue -> isDeclaration = newValue));
        map.put(Flag.Function.IS_FAKE_OVERRIDE, new FlagValue(() -> isFakeOverride, newValue -> isFakeOverride = newValue));
        map.put(Flag.Function.IS_DELEGATION,    new FlagValue(() -> isDelegation,   newValue -> isDelegation = newValue));
        map.put(Flag.Function.IS_SYNTHESIZED,   new FlagValue(() -> isSynthesized,  newValue -> isSynthesized = newValue));
        map.put(Flag.Function.IS_OPERATOR,      new FlagValue(() -> isOperator,     newValue -> isOperator = newValue));
        map.put(Flag.Function.IS_INFIX,         new FlagValue(() -> isInfix,        newValue -> isInfix = newValue));
        map.put(Flag.Function.IS_INLINE,        new FlagValue(() -> isInline,       newValue -> isInline = newValue));
        map.put(Flag.Function.IS_TAILREC,       new FlagValue(() -> isTailrec,      newValue -> isTailrec = newValue));
        map.put(Flag.Function.IS_EXTERNAL,      new FlagValue(() -> isExternal,     newValue -> isExternal = newValue));
        map.put(Flag.Function.IS_SUSPEND,       new FlagValue(() -> isSuspend,      newValue -> isSuspend = newValue));
        map.put(Flag.Function.IS_EXPECT,        new FlagValue(() -> isExpect,       newValue -> isExpect = newValue));
        return map;
    }

}
