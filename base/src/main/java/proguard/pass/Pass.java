/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard.pass;

import proguard.AppView;

/**
 * The interface each ProGuard pass should implement.
 */
public interface Pass
{
    /**
     * Returns the name of the pass.
     */
    default String getName()
    {
        return getClass().getName();
    }

    /**
     * Executes the pass.
     */
    void execute(AppView appView) throws Exception;
}
