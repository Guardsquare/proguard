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
