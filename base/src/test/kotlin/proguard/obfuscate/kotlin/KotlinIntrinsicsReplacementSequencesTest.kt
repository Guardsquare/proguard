/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
 */

package proguard.obfuscate.kotlin

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import proguard.classfile.Clazz
import proguard.classfile.Method
import proguard.classfile.attribute.CodeAttribute
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.constant.StringConstant
import proguard.classfile.constant.visitor.ConstantVisitor
import proguard.classfile.editor.InstructionSequenceBuilder
import proguard.classfile.instruction.Instruction
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.instruction.visitor.InstructionVisitor
import proguard.classfile.kotlin.KotlinConstants.KOTLIN_INTRINSICS_CLASS
import proguard.classfile.util.InstructionSequenceMatcher
import proguard.classfile.util.InstructionSequenceMatcher.X
import proguard.classfile.visitor.AllMemberVisitor
import proguard.classfile.visitor.MultiClassVisitor
import proguard.obfuscate.util.InstructionSequenceObfuscator
import proguard.util.ProcessingFlagSetter
import proguard.util.ProcessingFlags
import testutils.ClassPoolBuilder
import testutils.KotlinSource

class KotlinIntrinsicsReplacementSequencesTest : FreeSpec({

    "Given a class with a lateinit variable" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                    lateinit var lateVar : String
                    """
            )
        )

        val builder = InstructionSequenceBuilder(programClassPool, libraryClassPool)
        val intrinsicsSequence = builder.ldc_(X)
            .invokestatic(
                KOTLIN_INTRINSICS_CLASS,
                "throwUninitializedPropertyAccessException",
                "(Ljava/lang/String;)V"
            ).instructions()
        val constants = builder.constants()
        val intrinsicsSequenceMatcher = InstructionSequenceMatcher(constants, intrinsicsSequence)

        "Then the compiler generates an Intrinsics.throwUninitializedPropertyAccessException call" {

            var hasMatched = false
            programClassPool.classesAccept(
                AllAttributeVisitor(
                    true,
                    AllInstructionVisitor(
                        object : InstructionVisitor {
                            override fun visitAnyInstruction(clazz: Clazz, method: Method, codeAttribute: CodeAttribute, offset: Int, instruction: Instruction) {
                                instruction.accept(clazz, method, codeAttribute, offset, intrinsicsSequenceMatcher)
                                if (intrinsicsSequenceMatcher.isMatching) hasMatched = true
                            }
                        }
                    )
                )
            )
            hasMatched shouldBe true
        }
    }

    "Given a class with a lateinit variable without the DONT_OBFUSCATE flag" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                    lateinit var lateVar : String
                    """
            )
        )

        val builder = InstructionSequenceBuilder(programClassPool, libraryClassPool)
        val intrinsicsSequence = builder.ldc_(X)
            .invokestatic(
                KOTLIN_INTRINSICS_CLASS,
                "throwUninitializedPropertyAccessException",
                "(Ljava/lang/String;)V"
            ).instructions()
        val constants = builder.constants()
        val intrinsicsSequenceMatcher = InstructionSequenceMatcher(constants, intrinsicsSequence)

        "Then InstructionSequenceObfuscator replaces the 'lateVar' with an empty string" {

            lateinit var matchedConstant: String
            programClassPool.classesAccept(
                MultiClassVisitor(
                    InstructionSequenceObfuscator(KotlinIntrinsicsReplacementSequences(programClassPool, libraryClassPool)),
                    AllAttributeVisitor(
                        true,
                        AllInstructionVisitor(
                            object : InstructionVisitor {
                                override fun visitAnyInstruction(clazz: Clazz, method: Method, codeAttribute: CodeAttribute, offset: Int, instruction: Instruction) {
                                    instruction.accept(clazz, method, codeAttribute, offset, intrinsicsSequenceMatcher)
                                    if (intrinsicsSequenceMatcher.isMatching) {
                                        var constantIndex = intrinsicsSequenceMatcher.matchedConstantIndex(X)
                                        clazz.constantPoolEntryAccept(
                                            constantIndex,
                                            object : ConstantVisitor {
                                                override fun visitStringConstant(clazz: Clazz, stringConstant: StringConstant) {
                                                    matchedConstant = stringConstant.getString(clazz)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        )
                    )
                )
            )

            matchedConstant shouldBe ""
        }
    }

    "Given a class with a lateinit variable and the DONT_OBFUSCATE flag" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                    lateinit var lateVar : String
                    """
            )
        )

        val builder = InstructionSequenceBuilder(programClassPool, libraryClassPool)
        val intrinsicsSequence = builder.ldc_(X)
            .invokestatic(
                KOTLIN_INTRINSICS_CLASS,
                "throwUninitializedPropertyAccessException",
                "(Ljava/lang/String;)V"
            ).instructions()
        val constants = builder.constants()
        val intrinsicsSequenceMatcher = InstructionSequenceMatcher(constants, intrinsicsSequence)

        programClassPool.classesAccept(AllMemberVisitor(AllAttributeVisitor(ProcessingFlagSetter(ProcessingFlags.DONT_OBFUSCATE))))

        "Then InstructionSequenceObfuscator doesn't replace the 'lateVar' if DONT_OBFUSCATE is set" {

            lateinit var matchedConstant: String
            programClassPool.classesAccept(
                MultiClassVisitor(
                    InstructionSequenceObfuscator(KotlinIntrinsicsReplacementSequences(programClassPool, libraryClassPool)),
                    AllAttributeVisitor(
                        true,
                        AllInstructionVisitor(
                            object : InstructionVisitor {
                                override fun visitAnyInstruction(clazz: Clazz, method: Method, codeAttribute: CodeAttribute, offset: Int, instruction: Instruction) {
                                    instruction.accept(clazz, method, codeAttribute, offset, intrinsicsSequenceMatcher)
                                    if (intrinsicsSequenceMatcher.isMatching) {
                                        var constantIndex = intrinsicsSequenceMatcher.matchedConstantIndex(X)
                                        clazz.constantPoolEntryAccept(
                                            constantIndex,
                                            object : ConstantVisitor {
                                                override fun visitStringConstant(clazz: Clazz, stringConstant: StringConstant) {
                                                    matchedConstant = stringConstant.getString(clazz)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        )
                    )
                )
            )

            matchedConstant shouldBe "lateVar"
        }
    }
})
