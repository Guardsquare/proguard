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
