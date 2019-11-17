/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
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

import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.InstructionSequenceMatcher;

import static proguard.classfile.kotlin.KotlinConstants.*;

public final class IntrinsicsReplacementSequences implements ReplacementSequences
{
    private static final String REPLACEMENT_STRING = "";

    private static final int CONSTANT_INDEX_1 = InstructionSequenceMatcher.X;
    private static final int CONSTANT_INDEX_2 = InstructionSequenceMatcher.Y;

    private final Instruction[][][] SEQUENCES;
    private final Constant[]        CONSTANTS;

    public IntrinsicsReplacementSequences(ClassPool programClassPool, ClassPool libraryClassPool)
    {
        InstructionSequenceBuilder ____ = new InstructionSequenceBuilder(programClassPool, libraryClassPool);

        SEQUENCES = new Instruction[][][] {
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkNotNull", "(Ljava/lang/Object;Ljava/lang/String;)V").__(),

                ____
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkNotNull", "(Ljava/lang/Object;)V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkExpressionValueIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;)V").__(),

                ____
                    .ldc(REPLACEMENT_STRING)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkExpressionValueIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;)V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkNotNullExpressionValue", "(Ljava/lang/Object;Ljava/lang/String;)V").__(),

                ____
                    .ldc(REPLACEMENT_STRING)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkNotNullExpressionValue", "(Ljava/lang/Object;Ljava/lang/String;)V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkParameterIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;)V").__(),

                ____
                    .ldc(REPLACEMENT_STRING)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkParameterIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;)V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkNotNullParameter", "(Ljava/lang/Object;Ljava/lang/String;)V").__(),

                ____
                    .ldc(REPLACEMENT_STRING)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkNotNullParameter", "(Ljava/lang/Object;Ljava/lang/String;)V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkReturnedValueIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;)V").__(),

                ____
                    .ldc(REPLACEMENT_STRING)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkReturnedValueIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;)V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkFieldIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;)V").__(),

                ____
                    .ldc(REPLACEMENT_STRING)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkFieldIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;)V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .ldc_(CONSTANT_INDEX_2)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkReturnedValueIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V").__(),

                ____
                    .ldc(REPLACEMENT_STRING)
                    .ldc(REPLACEMENT_STRING)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkReturnedValueIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .ldc_(CONSTANT_INDEX_2)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkFieldIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V").__(),

                ____
                    .ldc(REPLACEMENT_STRING)
                    .ldc(REPLACEMENT_STRING)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "checkFieldIsNotNull", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V").__()
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