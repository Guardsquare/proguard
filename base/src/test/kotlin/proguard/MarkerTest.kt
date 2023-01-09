/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import proguard.io.ExtraDataEntryNameMap
import proguard.mark.Marker
import proguard.resources.file.ResourceFilePool
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.KotlinSource
import proguard.util.ProcessingFlags.DONT_OBFUSCATE
import proguard.util.ProcessingFlags.DONT_OPTIMIZE
import proguard.util.ProcessingFlags.DONT_SHRINK
import proguard.util.kotlin.asserter.KotlinMetadataVerifier
import testutils.asConfiguration
import testutils.shouldHaveFlag
import testutils.shouldNotHaveFlag

class MarkerTest : FreeSpec({

    "Given a Kotlin inline class" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                @JvmInline
                value class Password(val s: String)

                // The underlying JVM method descriptor will reference the
                // underlying property of `Password`, rather than `Password` itself.

                fun login(password: Password) {
                    println(password)
                }
                """.trimIndent()
            )
        )

        beforeEach {
            programClassPool.classesAccept {
                it.processingFlags = 0
            }
        }

        "Then when using includedescriptorclasses modifier" - {
            val config = """
            -keep,includedescriptorclasses class TestKt {
                <methods>;
            }
            """.asConfiguration()

            "The inline class should be marked" {
                val marker = Marker(config)
                val appView = AppView(programClassPool, libraryClassPool, ResourceFilePool(), ExtraDataEntryNameMap())
                marker.execute(appView)

                with(programClassPool.getClass("Password").processingFlags) {
                    this shouldHaveFlag DONT_OBFUSCATE
                    this shouldHaveFlag DONT_OPTIMIZE
                    this shouldHaveFlag DONT_SHRINK
                }
            }
        }

        "Then when not using includedescriptorclasses modifier" - {
            val config = """
            -keep class TestKt {
                <methods>;
            }
            """.asConfiguration()

            "The inline class should not be marked" {
                val marker = Marker(config)
                val appView = AppView(programClassPool, libraryClassPool, ResourceFilePool(), ExtraDataEntryNameMap())
                marker.execute(appView)

                with(programClassPool.getClass("Password").processingFlags) {
                    this shouldNotHaveFlag DONT_OBFUSCATE
                    this shouldNotHaveFlag DONT_OPTIMIZE
                    this shouldNotHaveFlag DONT_SHRINK
                }
            }
        }
    }

    "Given a Kotlin interface with default method implementation in the interface itself" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                interface Test {
                    fun foo() {
                        TODO()
                    }
                }
                """.trimIndent()
            ),
            kotlincArguments = listOf("-Xjvm-default=all")
        )

        // Run the asserter to ensure any metadata that isn't initialized correctly is thrown away
        KotlinMetadataVerifier(Configuration()).execute(AppView(programClassPool, libraryClassPool))

        "Then when marking" - {
            val config = """
            -keep class Test {
                <methods>;
            }
            # This option is deprecated and one should use '-keep class kotlin.Metadata' instead.
            # In this test we use the deprecated option, as its value is set in ProGuard.java and not in
            # the ConfigurationParser which is used by this unit test.
            -keepkotlinmetadata
            """.asConfiguration()
            val marker = Marker(config)

            "There should be no DefaultImpls class" {
                programClassPool.getClass("Test\$DefaultImpls") shouldBe null
            }

            "An exception should not be thrown" {
                shouldNotThrowAny {
                    marker.execute(AppView(programClassPool, libraryClassPool))
                }
            }
        }
    }

    "Given a Kotlin interface with default method implementation in compatibility mode" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                interface Test {
                    fun foo() {
                        TODO()
                    }
                }
                """.trimIndent()
            ),
            kotlincArguments = listOf("-Xjvm-default=all-compatibility")
        )

        // Run the asserter to ensure any metadata that isn't initialized correctly is thrown away
        KotlinMetadataVerifier(Configuration()).execute(AppView(programClassPool, libraryClassPool))

        "Then when marking" - {
            val config = """
            -keep class Test {
                <methods>;
            }
            # This option is deprecated and one should use '-keep class kotlin.Metadata' instead.
            # In this test we use the deprecated option, as its value is set in ProGuard.java and not in
            # the ConfigurationParser which is used by this unit test.
            -keepkotlinmetadata
            """.asConfiguration()
            val marker = Marker(config)

            "An exception should not be thrown" {
                shouldNotThrowAny {
                    marker.execute(AppView(programClassPool, libraryClassPool))
                }
            }

            "There should be no DefaultImpls class" {
                programClassPool.getClass("Test\$DefaultImpls") shouldNotBe null
            }

            val defaultImpls = programClassPool.getClass("Test\$DefaultImpls")

            "The DefaultImpls class should be marked DONT_OPTIMIZE" {
                defaultImpls.processingFlags shouldHaveFlag DONT_OPTIMIZE
            }

            "The default method should be marked DONT_OPTIMIZE" {
                defaultImpls.findMethod("foo", null).processingFlags shouldHaveFlag DONT_OPTIMIZE
            }
        }
    }
})
