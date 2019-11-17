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
 * Flags for Kotlin types.
 *
 * No valid common visibility or modality flags.
 *
 * hasAnnotation is valid.
 */
public class KotlinTypeFlags extends KotlinFlags
{

    public KotlinCommonFlags common = new KotlinCommonFlags();


    protected List<KotlinFlags> getChildren()
    {
        return Arrays.asList(common);
    }


    /**
     * Signifies that the corresponding type is marked as nullable, i.e. has a question mark at the end of its notation.
     */
    public boolean isNullable;

    /**
     * Signifies that the corresponding type is `suspend`.
     */
    public boolean isSuspend;

    public KotlinTypeFlags(int flags)
    {
        setFlags(flags);
    }


    protected Map<Flag, FlagValue> getOwnProperties()
    {
        HashMap<Flag, FlagValue> map = new HashMap<>();
        map.put(Flag.Type.IS_NULLABLE, new FlagValue(() -> isNullable, newValue -> isNullable = newValue));
        map.put(Flag.Type.IS_SUSPEND,  new FlagValue(() -> isSuspend,  newValue -> isSuspend = newValue));
        return map;
    }
}
