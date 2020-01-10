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
 * {@link KotlinFlags} represent a collection of flags in the metadata.
 * When implementing this interface, it is only expected that you override getOwnProperties().
 * If you have any children you also need to implement getChildren()
 */
public abstract class KotlinFlags
{

    /**
     * @return The mapping between the flags specific to this KotlinFlags instance and their value
     */
    protected abstract Map<Flag,FlagValue> getOwnProperties();

    /**
     * @return The list of children KotlinFlag instances (such as common or visibility)
     */
    protected List<KotlinFlags> getChildren() {
        return Collections.emptyList();
    }

    protected final void setFlags(int flags){
        getChildren().forEach(it -> it.setFlags(flags));
        getOwnProperties().forEach((flag, flagValue) -> flagValue.set(flag.invoke(flags)));
    }

    protected final List<Flag> getFlags(){
        ArrayList<Flag> flags = new ArrayList<>();
        getChildren().forEach(it -> flags.addAll(it.getFlags()));
        getOwnProperties().forEach((flag, flagValue) -> {
            if (flagValue.get()) {flags.add(flag);}
        });
        return flags;
    }

    public final int asInt()
    {
        return FlagsKt.flagsOf(getFlags().toArray(new Flag[0]));
    }

}
