/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
 */

package proguard

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.string.shouldContain
import proguard.classfile.ProgramClass
import proguard.classfile.VersionConstants.CLASS_VERSION_11
import proguard.classfile.VersionConstants.CLASS_VERSION_12
import proguard.classfile.VersionConstants.CLASS_VERSION_17
import proguard.classfile.VersionConstants.CLASS_VERSION_18
import proguard.classfile.VersionConstants.CLASS_VERSION_1_6
import testutils.asConfiguration
import java.util.UUID

/**
 * Test the after init checks.
 */
class AfterInitConfigurationCheckerTest : FreeSpec({

    // Mock a program class with the given class file version.
    class FakeClass(version: Int) : ProgramClass(version, 1, emptyArray(), 1, -1, -1) {
        override fun getClassName(constantIndex: Int): String {
            return "Fake${UUID.randomUUID()}"
        }
    }

    "Given a configuration with target specified" - {

        val configuration = """
            -verbose
            -target 6
        """.trimIndent().asConfiguration()

        "It should throw an exception if program class pool contains a class with class file version > jdk 11" {
            val view = AppView()
            view.programClassPool.addClass(FakeClass(CLASS_VERSION_12))

            val exception = shouldThrow<RuntimeException> {
                AfterInitConfigurationChecker(configuration).execute(view)
            }
            exception.message shouldContain "-target can only be used with class file versions <= 55 (Java 11)."
            exception.message shouldContain "The input classes contain version 56 class files which cannot be backported to target version (50)."
        }

        "It should not throw an exception if program class pool contains classes with class file version = jdk 11" {
            val view = AppView()
            view.programClassPool.addClass(FakeClass(CLASS_VERSION_11))

            shouldNotThrow<RuntimeException> {
                AfterInitConfigurationChecker(configuration).execute(view)
            }
        }

        "It should not throw an exception if program class pool contains classes with class file version < jdk 11" {
            val view = AppView()
            view.programClassPool.addClass(FakeClass(CLASS_VERSION_1_6))

            shouldNotThrow<RuntimeException> {
                AfterInitConfigurationChecker(configuration).execute(view)
            }
        }
    }

    "Given a configuration with a target specified and classes above Java 11" - {

        val configuration = """
            -target 17
        """.trimIndent().asConfiguration()

        "It should not throw an exception if -target is set but all the classes are already at the targeted version (no backport needed)" {
            val view = AppView()
            with(view.programClassPool) {
                addClass(FakeClass(CLASS_VERSION_17))
                addClass(FakeClass(CLASS_VERSION_17))
            }

            shouldNotThrow<RuntimeException> { AfterInitConfigurationChecker(configuration).execute(view) }
        }

        "It should throw an exception if -target is set and the version of one of the classes version is different from the target version" {
            val view = AppView()
            with(view.programClassPool) {
                addClass(FakeClass(CLASS_VERSION_18))
                addClass(FakeClass(CLASS_VERSION_17))
            }

            val exception = shouldThrow<RuntimeException> {
                AfterInitConfigurationChecker(configuration).execute(view)
            }

            exception.message shouldContain "-target can only be used with class file versions <= 55 (Java 11)."
            exception.message shouldContain "The input classes contain version 62 class files which cannot be backported to target version (61)."
        }
    }

    "Given a configuration with no target specified" - {

        val configuration = """
            -verbose
        """.trimIndent().asConfiguration()

        "It should not throw an exception if program class pool contains classes with class file version > jdk 11" {
            val view = AppView()
            view.programClassPool.addClass(FakeClass(CLASS_VERSION_12))

            shouldNotThrow<RuntimeException> {
                AfterInitConfigurationChecker(configuration).execute(view)
            }
        }

        "It should not throw an exception if program class pool contains classes with class file version = jdk 11" {
            val view = AppView()
            view.programClassPool.addClass(FakeClass(CLASS_VERSION_11))

            shouldNotThrow<RuntimeException> {
                AfterInitConfigurationChecker(configuration).execute(view)
            }
        }

        "It should not throw an exception if program class pool contains classes with class file version < jdk 11" {
            val view = AppView()
            view.programClassPool.addClass(FakeClass(CLASS_VERSION_1_6))

            shouldNotThrow<RuntimeException> {
                AfterInitConfigurationChecker(configuration).execute(view)
            }
        }
    }
})
