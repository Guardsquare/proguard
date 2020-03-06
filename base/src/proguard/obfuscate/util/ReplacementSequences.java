/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */
package proguard.obfuscate.util;

import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.Instruction;

public interface ReplacementSequences
{
    Instruction[][][] getSequences();
    Constant[]        getConstants();
}
