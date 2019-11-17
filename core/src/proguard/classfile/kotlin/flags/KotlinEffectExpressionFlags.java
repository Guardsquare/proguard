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
