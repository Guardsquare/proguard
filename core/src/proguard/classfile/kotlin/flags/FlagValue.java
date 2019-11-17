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

import java.util.function.*;

/**
 * A value of a flag, which is a mutable boolean
 */
public class FlagValue
{
    public final Supplier<Boolean> getter;
    public final Consumer<Boolean> setter;


    public FlagValue(Supplier<Boolean> getter, Consumer<Boolean> setter)
    {
        this.getter = getter;
        this.setter = setter;
    }


    public void set(boolean newValue)
    {
        setter.accept(newValue);
    }


    public boolean get()
    {
        return getter.get();
    }


    /**
     * @return a FlagValue which behaves as a transparant negation to the original flag
     */
    public FlagValue negation()
    {
        FlagValue outer = this;
        return new FlagValue(getter, setter)
        {
            public void set(boolean newValue)
            {
                outer.set(!newValue);
            }


            public boolean get()
            {
                return !outer.get();
            }
        };
    }
}
