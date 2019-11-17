/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
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
