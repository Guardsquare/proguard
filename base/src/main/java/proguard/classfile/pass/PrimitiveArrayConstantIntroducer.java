/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard.classfile.pass;

import proguard.AppView;
import proguard.classfile.util.ArrayInitializationReplacer;
import proguard.pass.Pass;

/**
 * This pass replaces primitive array initialization code by primitive array constants.
 */
public class PrimitiveArrayConstantIntroducer implements Pass
{
    @Override
    public void execute(AppView appView)
    {
        appView.programClassPool.classesAccept(new ArrayInitializationReplacer());
    }
}
