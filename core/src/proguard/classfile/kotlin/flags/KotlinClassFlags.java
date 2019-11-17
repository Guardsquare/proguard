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

import kotlinx.metadata.Flag;

import java.util.*;

/**
 * Flags for Kotlin classes.
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
public class KotlinClassFlags extends KotlinFlags
{

    public KotlinVisibilityFlags visibility = new KotlinVisibilityFlags();
    public KotlinModalityFlags   modality   = new KotlinModalityFlags();
    public KotlinCommonFlags     common     = new KotlinCommonFlags();

    /**
     * A class kind flag, signifying that the corresponding class is a usual `class`.
     */
    public boolean isUsualClass;

    /**
     * A class kind flag, signifying that the corresponding class is an `interface`.
     */
    public boolean isInterface;

    /**
     * A class kind flag, signifying that the corresponding class is an `enum class`.
     */
    public boolean isEnumClass;

    /**
     * A class kind flag, signifying that the corresponding class is an enum entry.
     */
    public boolean isEnumEntry;

    /**
     * A class kind flag, signifying that the corresponding class is an `annotation class`.
     */
    public boolean isAnnotationClass;

    /**
     * A class kind flag, signifying that the corresponding class is a non-companion `object`.
     */
    public boolean isObject;

    /**
     * A class kind flag, signifying that the corresponding class is a `companion object`.
     */
    public boolean isCompanionObject;

    /**
     * Signifies that the corresponding class is `inner`.
     */
    public boolean isInner;

    /**
     * Signifies that the corresponding class is `data`.
     */
    public boolean isData;

    /**
     * Signifies that the corresponding class is `external`.
     */
    public boolean isExternal;

    /**
     * Signifies that the corresponding class is `expect`.
     */
    public boolean isExpect;

    /**
     * Signifies that the corresponding class is `inline`.
     */
    public boolean isInline;

    public KotlinClassFlags(int flags)
    {
        setFlags(flags);
    }


    protected Map<Flag, FlagValue> getOwnProperties()
    {
        HashMap<Flag,  FlagValue> map = new HashMap<>();
        map.put(Flag.Class.IS_CLASS,            new FlagValue(() -> isUsualClass,      newValue -> isUsualClass = newValue));
        map.put(Flag.Class.IS_INTERFACE,        new FlagValue(() -> isInterface,       newValue -> isInterface = newValue));
        map.put(Flag.Class.IS_ENUM_CLASS,       new FlagValue(() -> isEnumClass,       newValue -> isEnumClass = newValue));
        map.put(Flag.Class.IS_ENUM_ENTRY,       new FlagValue(() -> isEnumEntry,       newValue -> isEnumEntry = newValue));
        map.put(Flag.Class.IS_ANNOTATION_CLASS, new FlagValue(() -> isAnnotationClass, newValue -> isAnnotationClass = newValue));
        map.put(Flag.Class.IS_OBJECT,           new FlagValue(() -> isObject,          newValue -> isObject = newValue));
        map.put(Flag.Class.IS_COMPANION_OBJECT, new FlagValue(() -> isCompanionObject, newValue -> isCompanionObject = newValue));
        map.put(Flag.Class.IS_INNER,            new FlagValue(() -> isInner,           newValue -> isInner = newValue));
        map.put(Flag.Class.IS_DATA,             new FlagValue(() -> isData,            newValue -> isData = newValue));
        map.put(Flag.Class.IS_EXTERNAL,         new FlagValue(() -> isExternal,        newValue -> isExternal = newValue));
        map.put(Flag.Class.IS_EXPECT,           new FlagValue(() -> isExpect,          newValue -> isExpect = newValue));
        map.put(Flag.Class.IS_INLINE,           new FlagValue(() -> isInline,          newValue -> isInline = newValue));
        return map;
    }



    protected List<KotlinFlags> getChildren()
    {
        return Arrays.asList(common, visibility,modality);
    }


}
