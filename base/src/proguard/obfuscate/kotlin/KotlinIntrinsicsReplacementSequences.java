/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */
package proguard.obfuscate.kotlin;

import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.InstructionSequenceMatcher;
import proguard.obfuscate.util.ReplacementSequences;

import static proguard.classfile.kotlin.KotlinConstants.*;

public final class KotlinIntrinsicsReplacementSequences
implements         ReplacementSequences
{
    private static final String REPLACEMENT_STRING = "";

    private static final int CONSTANT_INDEX_1 = InstructionSequenceMatcher.X;
    private static final int CONSTANT_INDEX_2 = InstructionSequenceMatcher.Y;

    private final Instruction[][][] SEQUENCES;
    private final Constant[]        CONSTANTS;

    public KotlinIntrinsicsReplacementSequences(ClassPool programClassPool, ClassPool libraryClassPool)
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
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "throwUninitializedPropertyAccessException", "(Ljava/lang/String;)V").__(),

                ____
                    .ldc(REPLACEMENT_STRING)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "throwUninitializedPropertyAccessException", "(Ljava/lang/String;)V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "throwNpe", "(Ljava/lang/String;)V").__(),

                ____
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "throwNpe", "()V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "throwJavaNpe", "(Ljava/lang/String;)V").__(),

                ____
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "throwJavaNpe", "()V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "throwAssert", "(Ljava/lang/String;)V").__(),

                ____
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "throwAssert", "()V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "throwIllegalArgument", "(Ljava/lang/String;)V").__(),

                ____
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "throwIllegalArgument", "()V").__()
            },
            {
                ____
                    .ldc_(CONSTANT_INDEX_1)
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "throwIllegalState", "(Ljava/lang/String;)V").__(),

                ____
                    .invokestatic(KOTLIN_INTRINSICS_CLASS, "throwIllegalState", "()V").__()
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