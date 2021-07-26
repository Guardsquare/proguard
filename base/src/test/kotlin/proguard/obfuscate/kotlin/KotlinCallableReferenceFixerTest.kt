/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
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

package proguard.obfuscate.kotlin

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotContainIgnoringCase
import io.mockk.spyk
import io.mockk.verify
import proguard.classfile.ClassPool
import proguard.classfile.Clazz
import proguard.classfile.constant.Constant
import proguard.classfile.constant.visitor.AllConstantVisitor
import proguard.classfile.constant.visitor.ConstantVisitor
import proguard.classfile.editor.ClassReferenceFixer
import proguard.classfile.editor.ConstantPoolShrinker
import proguard.classfile.editor.MemberReferenceFixer
import proguard.classfile.kotlin.KotlinClassKindMetadata
import proguard.classfile.kotlin.KotlinFileFacadeKindMetadata
import proguard.classfile.kotlin.KotlinMetadata
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata
import proguard.classfile.kotlin.reflect.visitor.CallableReferenceInfoVisitor
import proguard.classfile.kotlin.visitor.AllPropertyVisitor
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor
import proguard.classfile.kotlin.visitor.ReferencedKotlinMetadataVisitor
import proguard.classfile.util.ClassRenamer
import proguard.classfile.visitor.ClassNameFilter
import proguard.classfile.visitor.MultiClassVisitor
import testutils.ClassPoolBuilder
import testutils.JavaSource
import testutils.KotlinSource

class KotlinCallableReferenceFixerTest : FreeSpec({

    "Given a function callable reference" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                fun original() = "bar"
                fun ref() = ::original
                """.trimIndent()
            )
        )

        programClassPool.classesAccept(
            MultiClassVisitor(
                ClassNameFilter(
                    "TestKt",
                    ClassRenamer(
                        { "Obfuscated" },
                        { clazz, member -> if (member.getName(clazz) == "original") "obfuscated" else member.getName(clazz) }
                    )
                ),
                createFixer(programClassPool, libraryClassPool)
            )
        )

        val callableRefInfoVisitor = spyk<CallableReferenceInfoVisitor>()
        val ownerVisitor = spyk< KotlinMetadataVisitor>()
        val testVisitor = createVisitor(callableRefInfoVisitor, ownerVisitor)

        programClassPool.classesAccept("TestKt\$ref\$1", testVisitor)

        "Then the callableReferenceInfo should be initialized" {
            verify(exactly = 1) {
                callableRefInfoVisitor.visitFunctionReferenceInfo(
                    withArg {
                        it.name shouldBe "obfuscated"
                        it.signature shouldBe "obfuscated()Ljava/lang/String;"
                        it.owner shouldNotBe null
                    }
                )
            }

            verify(exactly = 1) {
                ownerVisitor.visitKotlinFileFacadeMetadata(
                    withArg {
                        it.name shouldBe "Obfuscated"
                    },
                    ofType(KotlinFileFacadeKindMetadata::class)
                )
            }
        }

        "Then the callable reference class should not mention the original name" {
            programClassPool.classesAccept(
                "TestKt\$ref\$1",
                AllConstantVisitor(
                    object : ConstantVisitor {
                        override fun visitAnyConstant(clazz: Clazz, constant: Constant) {
                            constant.toString() shouldNotContainIgnoringCase "original"
                        }
                    }
                )
            )
        }
    }

    "Given a property callable reference" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                class Foo {
                    var original = "bar"
                }

                fun ref() = Foo()::original
                """.trimIndent()
            )
        )

        programClassPool.classesAccept(
            MultiClassVisitor(
                ClassNameFilter(
                    "Foo",
                    MultiClassVisitor(
                        ClassRenamer(
                            { "Obfuscated" },
                            { clazz, member ->
                                when {
                                    member.getName(clazz) == "getOriginal" -> "getObfuscated"
                                    member.getName(clazz) == "original" -> "obfuscated"
                                    member.getName(clazz) == "setOriginal" -> "setObfuscated"
                                    else -> member.getName(clazz)
                                }
                            }
                        ),
                        ReferencedKotlinMetadataVisitor(
                            AllPropertyVisitor
                            { _, _, property ->
                                if (property.name == "original") property.name = "obfuscated"
                            }
                        )
                    )
                ),
                createFixer(programClassPool, libraryClassPool)
            )
        )

        val callableRefInfoVisitor = spyk<CallableReferenceInfoVisitor>()
        val ownerVisitor = spyk< KotlinMetadataVisitor>()
        val testVisitor = createVisitor(callableRefInfoVisitor, ownerVisitor)

        programClassPool.classesAccept("TestKt\$ref\$1", testVisitor)

        "Then the callableReferenceInfo should be initialized" {
            verify(exactly = 1) {
                callableRefInfoVisitor.visitPropertyReferenceInfo(
                    withArg {
                        it.name shouldBe "obfuscated"
                        it.signature shouldBe "getObfuscated()Ljava/lang/String;"
                        it.owner shouldNotBe null
                    }
                )
            }

            verify(exactly = 1) {
                ownerVisitor.visitKotlinClassMetadata(
                    withArg {
                        it.name shouldBe "Obfuscated"
                    },
                    ofType(KotlinClassKindMetadata::class)
                )
            }
        }

        "Then the callable reference class should not mention the original name" {
            programClassPool.classesAccept(
                "TestKt\$ref\$1",
                AllConstantVisitor(
                    object : ConstantVisitor {
                        override fun visitAnyConstant(clazz: Clazz, constant: Constant) {
                            constant.toString() shouldNotContainIgnoringCase "original"
                        }
                    }
                )
            )
        }
    }

    "Given a Java method callable reference" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                val javaClassInstance = Original()
                fun ref() = javaClassInstance::original
                """.trimIndent()
            ),
            JavaSource(
                "Original.java",
                """
                public class Original {
                    public String original() { return "bar"; }
                }
                """.trimIndent()
            )
        )

        programClassPool.classesAccept(
            MultiClassVisitor(
                ClassNameFilter(
                    "Original",
                    ClassRenamer(
                        { "Obfuscated" },
                        { clazz, member -> if (member.getName(clazz) == "original") "obfuscated" else member.getName(clazz) }
                    )
                ),
                createFixer(programClassPool, libraryClassPool)
            )
        )

        val callableRefInfoVisitor = spyk<CallableReferenceInfoVisitor>()
        val ownerVisitor = spyk< KotlinMetadataVisitor>()
        val testVisitor = createVisitor(callableRefInfoVisitor, ownerVisitor)

        programClassPool.classesAccept("TestKt\$ref\$1", testVisitor)

        "Then the callableReferenceInfo should be initialized" {
            verify(exactly = 1) {
                callableRefInfoVisitor.visitJavaReferenceInfo(
                    withArg {
                        it.name shouldBe "obfuscated"
                        it.signature shouldBe "obfuscated()Ljava/lang/String;"
                        it.owner shouldBe null
                    }
                )
            }

            verify(exactly = 0) {
                ownerVisitor.visitAnyKotlinMetadata(
                    withArg {
                        it.name shouldBe "Obfuscated"
                    },
                    ofType(KotlinMetadata::class)
                )
            }
        }

        "Then the callable reference class should not mention the original name" {
            programClassPool.classesAccept(
                "TestKt\$ref\$1",
                AllConstantVisitor(
                    object : ConstantVisitor {
                        override fun visitAnyConstant(clazz: Clazz, constant: Constant) {
                            constant.toString() shouldNotContainIgnoringCase "original"
                        }
                    }
                )
            )
        }
    }

    "Given a Java field callable reference" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                val javaClassInstance = Original()
                fun ref() = javaClassInstance::original
                """.trimIndent()
            ),
            JavaSource(
                "Original.java",
                """
                public class Original {
                    public String original = "bar";
                }
                """.trimIndent()
            )
        )

        programClassPool.classesAccept(
            MultiClassVisitor(
                ClassNameFilter(
                    "Original",
                    ClassRenamer(
                        { "Obfuscated" },
                        { clazz, member -> if (member.getName(clazz) == "original") "obfuscated" else member.getName(clazz) }
                    )
                ),
                createFixer(programClassPool, libraryClassPool)
            )
        )

        val callableRefInfoVisitor = spyk<CallableReferenceInfoVisitor>()
        val ownerVisitor = spyk< KotlinMetadataVisitor>()
        val testVisitor = createVisitor(callableRefInfoVisitor, ownerVisitor)

        programClassPool.classesAccept("TestKt\$ref\$1", testVisitor)

        "Then the callableReferenceInfo should be initialized" {
            verify(exactly = 1) {
                callableRefInfoVisitor.visitJavaReferenceInfo(
                    withArg {
                        it.name shouldBe "obfuscated"
                        it.signature shouldBe "getObfuscated()Ljava/lang/String;"
                        it.owner shouldBe null
                    }
                )
            }

            verify(exactly = 0) {
                ownerVisitor.visitAnyKotlinMetadata(
                    withArg {
                        it.name shouldBe "Obfuscated"
                    },
                    ofType(KotlinMetadata::class)
                )
            }
        }

        "Then the callable reference class should not mention the original name" {
            programClassPool.classesAccept(
                "TestKt\$ref\$1",
                AllConstantVisitor(
                    object : ConstantVisitor {
                        override fun visitAnyConstant(clazz: Clazz, constant: Constant) {
                            constant.toString() shouldNotContainIgnoringCase "original"
                        }
                    }
                )
            )
        }
    }
})

private fun createVisitor(callableRefInfoVisitor: CallableReferenceInfoVisitor, ownerVisitor: KotlinMetadataVisitor) = ReferencedKotlinMetadataVisitor(
    object : KotlinMetadataVisitor {
        override fun visitAnyKotlinMetadata(clazz: Clazz, kotlinMetadata: KotlinMetadata) {}
        override fun visitKotlinSyntheticClassMetadata(clazz: Clazz, classMetadata: KotlinSyntheticClassKindMetadata) {
            classMetadata.callableReferenceInfoAccept(callableRefInfoVisitor)
            classMetadata.callableReferenceInfoAccept { it.ownerAccept(ownerVisitor) }
        }
    }
)

private fun createFixer(programClassPool: ClassPool, libraryClassPool: ClassPool) = MultiClassVisitor(
    MemberReferenceFixer(false),
    ClassReferenceFixer(true),
    ReferencedKotlinMetadataVisitor(KotlinCallableReferenceFixer(programClassPool, libraryClassPool)),
    ConstantPoolShrinker()
)
