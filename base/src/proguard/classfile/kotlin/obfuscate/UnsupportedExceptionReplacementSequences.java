/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.classfile.kotlin.obfuscate;

import proguard.classfile.ClassPool;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.InstructionSequenceMatcher;

import static proguard.classfile.ClassConstants.NAME_JAVA_LANG_UNSUPPORTED_OP_EXCEPTION;

/**
 * The Kotlin compiler automatically inserts a null check into $default methods,
 * checking the last parameter but the string contains the original name of the
 * method. So here we remove the string.
 */
public final class UnsupportedExceptionReplacementSequences implements ReplacementSequences
{
    private static final int CONSTANT_INDEX_1 = InstructionSequenceMatcher.X;
    private static final int ALOAD_INDEX      = InstructionSequenceMatcher.A;

    private final Instruction[][][] SEQUENCES;
    private final Constant[]        CONSTANTS;

    public UnsupportedExceptionReplacementSequences(ClassPool programClassPool, ClassPool libraryClassPool)
    {
        InstructionSequenceBuilder ____ = new InstructionSequenceBuilder(programClassPool, libraryClassPool);

        SEQUENCES = new Instruction[][][] {
            {
                ____
                    .aload(ALOAD_INDEX)
                    .ifnull(InstructionSequenceMatcher.Y)
                    .new_(NAME_JAVA_LANG_UNSUPPORTED_OP_EXCEPTION)
                    .dup()
                    .ldc_(CONSTANT_INDEX_1)
                    .invokespecial(NAME_JAVA_LANG_UNSUPPORTED_OP_EXCEPTION, "<init>", "(Ljava/lang/String;)V").__(),

                ____
                    .aload(ALOAD_INDEX)
                    .ifnull(InstructionSequenceMatcher.Y)
                    .new_(NAME_JAVA_LANG_UNSUPPORTED_OP_EXCEPTION)
                    .dup()
                    .invokespecial(NAME_JAVA_LANG_UNSUPPORTED_OP_EXCEPTION, "<init>", "()V").__()
            },
        };

        CONSTANTS = ____.constants();
    }

    @Override
    public Instruction[][][] getSequences()
    {
        return SEQUENCES;
    }

    @Override
    public Constant[] getConstants()
    {
        return CONSTANTS;
    }
}