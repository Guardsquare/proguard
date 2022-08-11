/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard

import io.kotest.core.spec.style.FreeSpec
import proguard.io.ExtraDataEntryNameMap
import proguard.mark.Marker
import proguard.resources.file.ResourceFilePool
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.KotlinSource
import proguard.util.ProcessingFlags.DONT_OBFUSCATE
import proguard.util.ProcessingFlags.DONT_OPTIMIZE
import proguard.util.ProcessingFlags.DONT_SHRINK
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
})
