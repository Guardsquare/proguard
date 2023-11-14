/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import proguard.classfile.AccessConstants.PUBLIC
import testutils.asConfiguration
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 * Some simple testcases to catch special cases when parsing the Configuration.
 *
 * @author Thomas Neidhart
 */
class ConfigurationParserTest : FreeSpec({
    fun parseConfiguration(rules: String): Configuration {
        val configuration = Configuration()
        ConfigurationParser(rules, "", null, System.getProperties()).use {
            it.parse(configuration)
        }
        return configuration
    }

    fun parseConfiguration(reader: WordReader): Configuration {
        val configuration = Configuration()
        ConfigurationParser(reader, System.getProperties()).use {
            it.parse(configuration)
        }
        return configuration
    }

    "Keep rule tests" - {
        "Keep rule with <fields> wildcard should be valid" {
            parseConfiguration("-keep class * { <fields>; }")
        }

        "Keep rule with <fields> wildcard and public access modifier should be valid" {
            parseConfiguration("-keep class * { public <fields>; }")
        }

        "Keep rule with <fields> wildcard and public + protected access modifiers should be valid" {
            parseConfiguration("-keep class * { public protected <fields>; }")
        }

        "Keep rule with <methods> wildcard should be valid" {
            parseConfiguration("-keep class * { <methods>; }")
        }

        "Keep rule with <methods> wildcard and public access modifier should be valid" {
            parseConfiguration("-keep class * { public <methods>; }")
        }

        "Keep rule with <methods> wildcard and public + protected access modifier should be valid" {
            parseConfiguration("-keep class * { public protected <methods>; }")
        }

        "Keep rule with ClassName should be valid" {
            val configuration = parseConfiguration("-keep class ClassName { ClassName(); }")
            val keep = configuration.keep.single().methodSpecifications.single()
            keep.name shouldBe "<init>"
            keep.descriptor shouldBe "()V"
        }

        "Keep rule with ClassName and external class com.example.ClassName should be valid" {
            val configuration = parseConfiguration("-keep class com.example.ClassName { ClassName(); }")
            val keep = configuration.keep.single().methodSpecifications.single()
            keep.name shouldBe "<init>"
            keep.descriptor shouldBe "()V"
        }

        "Keep rule with <clinit> should be valid" {
            val configuration = parseConfiguration("-keep class ** { <clinit>(); }")
            val keep = configuration.keep.single().methodSpecifications.single()
            keep.name shouldBe "<clinit>"
            keep.descriptor shouldBe "()V"
        }

        "Keep rule with <clinit> and non-empty argument list should throw ParseException" {
            shouldThrow<ParseException> { parseConfiguration("-keep class * { void <clinit>(int) }") }
        }

        "Keep rule with * member wildcard and return type should be valid" {
            parseConfiguration("-keep class * { java.lang.String *; }")
        }

        "Keep rule with * member wildcard, return type and empty argument list should be valid" {
            parseConfiguration("-keep class * { int *(); }")
        }

        "Keep rule with * member wildcard, return type and non-empty argument list should be valid" {
            parseConfiguration("-keep class * { int *(int); }")
        }

        "Keep rule with <fields> wildcard and explicit type should throw ParseException" {
            shouldThrow<ParseException> { parseConfiguration("-keep class * { java.lang.String <fields>; }") }
        }

        "Keep rule with <methods> wildcard and explicit argument list should throw ParseException" {
            shouldThrow<ParseException> { parseConfiguration("-keep class * { <methods>(); }") }
        }
    }

    "A ParseException should be thrown with invalid annotation config at the end of the file" - {
        // This is a parse error without any further config after it.
        val configStr = ("-keep @MyAnnotation @ThisShouldBeInterfaceKeyword")

        "Then the option should throw a ParseException" {
            shouldThrow<ParseException> {
                configStr.asConfiguration()
            }
        }
    }

    "Testing -alwaysinline parsing" - {
        "Given an empty configuration" - {
            val savedPrintStream = System.out
            val customOutputStream = ByteArrayOutputStream()
            System.setOut(PrintStream(customOutputStream))

            parseConfiguration("")

            "The option does not print anything" {
                customOutputStream.toString() shouldContain ""
                System.setOut(savedPrintStream)
            }
        }

        "Given a configuration with -alwaysinline" - {
            val savedPrintStream = System.out
            val customOutputStream = ByteArrayOutputStream()
            System.setOut(PrintStream(customOutputStream))

            parseConfiguration(
                """-alwaysinline class * {
                        @org.chromium.build.annotations.AlwaysInline *;
                    }
                    """
            )

            "The option prints out a warning" {
                customOutputStream.toString() shouldContain "Warning: The R8 option -alwaysinline is currently not supported by ProGuard.\n" +
                    "This option will have no effect on the optimized artifact."
                System.setOut(savedPrintStream)
            }
        }

        "Given a configuration with -alwaysinline with no class specification" - {
            "The parsing should throw an exception" {
                shouldThrow<ParseException> { parseConfiguration("-alwaysinline") }
            }
        }
    }

    "Testing -identifiernamestring parsing" - {
        "Given an empty configuration" - {
            val savedPrintStream = System.out
            val customOutputStream = ByteArrayOutputStream()
            System.setOut(PrintStream(customOutputStream))

            parseConfiguration("")

            "The option does not print anything" {
                customOutputStream.toString() shouldContain ""
                System.setOut(savedPrintStream)
            }
        }

        "Given a configuration with -identifiernamestring" - {
            val savedPrintStream = System.out
            val customOutputStream = ByteArrayOutputStream()
            System.setOut(PrintStream(customOutputStream))

            parseConfiguration(
                """-identifiernamestring class * {
                        @org.chromium.build.annotations.IdentifierNameString *;
                    }
                    """
            )

            "The option prints out a warning" {
                customOutputStream.toString() shouldContain "Warning: The R8 option -identifiernamestring is currently not supported by ProGuard.\n" +
                    "This option will have no effect on the optimized artifact."
                System.setOut(savedPrintStream)
            }
        }

        "Given a configuration with -identifiernamestring with no class specification" - {
            "The parsing should throw an exception" {
                shouldThrow<ParseException> { parseConfiguration("-identifiernamestring") }
            }
        }
    }

    "Wildcard type tests" - {
        class TestConfig(
            val configOption: String,
            classSpecificationConfig: String,
            private val classSpecificationGetter: Configuration.() -> List<ClassSpecification>?
        ) {
            private val configuration: Configuration by lazy {
                "$configOption $classSpecificationConfig".asConfiguration()
            }
            val classSpecifications: List<ClassSpecification>? get() = classSpecificationGetter.invoke(configuration)
        }

        fun generateTestCases(clSpec: String): List<TestConfig> = listOf(
            TestConfig("-keep", clSpec) { keep },
            TestConfig("-assumenosideeffects", clSpec) { assumeNoSideEffects },
            TestConfig("-assumenoexternalsideeffects", clSpec) { assumeNoExternalSideEffects },
            TestConfig("-assumenoescapingparameters", clSpec) { assumeNoEscapingParameters },
            TestConfig("-assumenoexternalreturnvalues", clSpec) { assumeNoExternalReturnValues },
            TestConfig("-assumevalues", clSpec) { assumeValues },
        )

        "Test wildcard matches all methods and fields" {
            val testConfigurations = generateTestCases("class Foo { *; }") + generateTestCases("class Foo { <fields>; <methods>; }")

            for (testConfig in testConfigurations) {
                val classSpecifications = testConfig.classSpecifications
                val methodSpecification = classSpecifications?.single()?.methodSpecifications?.single()
                methodSpecification shouldNotBe null
                methodSpecification?.requiredSetAccessFlags shouldBe 0
                methodSpecification?.name shouldBe null
                methodSpecification?.descriptor shouldBe null
                val fieldSpecification = classSpecifications?.single()?.fieldSpecifications?.single()
                fieldSpecification shouldNotBe null
                fieldSpecification?.requiredSetAccessFlags shouldBe 0
                fieldSpecification?.name shouldBe null
                fieldSpecification?.descriptor shouldBe null
            }
        }

        "Test wildcard method return type" {
            val testConfigurations = generateTestCases("class Foo { * bar(); }")

            for (testConfig in testConfigurations) {
                val classSpecifications = testConfig.classSpecifications
                val methodSpecification = classSpecifications?.single()?.methodSpecifications?.single()
                methodSpecification?.requiredSetAccessFlags shouldBe 0
                methodSpecification?.name shouldBe "bar"
                methodSpecification?.descriptor shouldBe "()L*;"
                val fieldSpecification = classSpecifications?.single()?.fieldSpecifications
                fieldSpecification shouldBe null
            }
        }

        "Test wildcard method return type with access modifier" {
            val testConfigurations = generateTestCases("class Foo { public * bar(); }")

            for (testConfig in testConfigurations) {
                val classSpecifications = testConfig.classSpecifications
                val methodSpecification = classSpecifications?.single()?.methodSpecifications?.single()
                methodSpecification?.requiredSetAccessFlags shouldBe PUBLIC
                methodSpecification?.name shouldBe "bar"
                methodSpecification?.descriptor shouldBe "()L*;"
                val fieldSpecification = classSpecifications?.single()?.fieldSpecifications
                fieldSpecification shouldBe null
            }
        }

        "Test wildcard field type" {
            val testConfigurations = generateTestCases("class Foo { * bar; }")

            for (testConfig in testConfigurations) {
                val classSpecifications = testConfig.classSpecifications
                val methodSpecification = classSpecifications?.single()?.methodSpecifications
                methodSpecification shouldBe null
                val fieldSpecification = classSpecifications?.single()?.fieldSpecifications?.single()
                fieldSpecification?.requiredSetAccessFlags shouldBe 0
                fieldSpecification?.name shouldBe "bar"
                fieldSpecification?.descriptor shouldBe "L*;"
            }
        }

        "Test wildcard field type with access modifier" {
            val testConfigurations = generateTestCases("class Foo { public * bar; }")

            for (testConfig in testConfigurations) {
                val classSpecifications = testConfig.classSpecifications
                val methodSpecification = classSpecifications?.single()?.methodSpecifications
                methodSpecification shouldBe null
                val fieldSpecification = classSpecifications?.single()?.fieldSpecifications?.single()
                fieldSpecification?.requiredSetAccessFlags shouldBe PUBLIC
                fieldSpecification?.name shouldBe "bar"
                fieldSpecification?.descriptor shouldBe "L*;"
            }
        }

        "Test all type wildcard field" {
            val testConfigurations = generateTestCases("class Foo { *** bar; }")

            for (testConfig in testConfigurations) {
                val classSpecifications = testConfig.classSpecifications
                val methodSpecification = classSpecifications?.single()?.methodSpecifications
                methodSpecification shouldBe null
                val fieldSpecification = classSpecifications?.single()?.fieldSpecifications?.single()
                fieldSpecification?.requiredSetAccessFlags shouldBe 0
                fieldSpecification?.name shouldBe "bar"
                fieldSpecification?.descriptor shouldBe "L***;"
            }
        }

        "Test all type wildcard field type with access modifier" {
            val testConfigurations = generateTestCases("class Foo { public *** bar; }")

            for (testConfig in testConfigurations) {
                val classSpecifications = testConfig.classSpecifications
                val methodSpecification = classSpecifications?.single()?.methodSpecifications
                methodSpecification shouldBe null
                val fieldSpecification = classSpecifications?.single()?.fieldSpecifications?.single()
                fieldSpecification?.requiredSetAccessFlags shouldBe PUBLIC
                fieldSpecification?.name shouldBe "bar"
                fieldSpecification?.descriptor shouldBe "L***;"
            }
        }

        "Test all type wildcard method return type" {
            val testConfigurations = generateTestCases("class Foo { *** bar(); }")

            for (testConfig in testConfigurations) {
                val classSpecifications = testConfig.classSpecifications
                val methodSpecification = classSpecifications?.single()?.methodSpecifications?.single()
                methodSpecification?.requiredSetAccessFlags shouldBe 0
                methodSpecification?.name shouldBe "bar"
                methodSpecification?.descriptor shouldBe "()L***;"
                val fieldSpecification = classSpecifications?.single()?.fieldSpecifications
                fieldSpecification shouldBe null
            }
        }

        "Test all type wildcard method return type with access modifier" {
            val testConfigurations = generateTestCases("class Foo { public *** bar(); }")

            for (testConfig in testConfigurations) {
                val classSpecifications = testConfig.classSpecifications
                val methodSpecification = classSpecifications?.single()?.methodSpecifications?.single()
                methodSpecification?.requiredSetAccessFlags shouldBe PUBLIC
                methodSpecification?.name shouldBe "bar"
                methodSpecification?.descriptor shouldBe "()L***;"
                val fieldSpecification = classSpecifications?.single()?.fieldSpecifications
                fieldSpecification shouldBe null
            }
        }

        "Test concrete wildcard field type" {
            val testConfigurations = generateTestCases("class Foo { java.lang.String bar; }")

            for (testConfig in testConfigurations) {
                val classSpecifications = testConfig.classSpecifications
                val methodSpecification = classSpecifications?.single()?.methodSpecifications
                methodSpecification shouldBe null
                val fieldSpecification = classSpecifications?.single()?.fieldSpecifications?.single()
                fieldSpecification?.requiredSetAccessFlags shouldBe 0
                fieldSpecification?.name shouldBe "bar"
                fieldSpecification?.descriptor shouldBe "Ljava/lang/String;"
            }
        }

        "Test concrete wildcard method return type" {
            val testConfigurations = generateTestCases("class Foo { java.lang.String bar(); }")

            for (testConfig in testConfigurations) {
                val classSpecifications = testConfig.classSpecifications
                val methodSpecification = classSpecifications?.single()?.methodSpecifications?.single()
                methodSpecification?.requiredSetAccessFlags shouldBe 0
                methodSpecification?.name shouldBe "bar"
                methodSpecification?.descriptor shouldBe "()Ljava/lang/String;"
                val fieldSpecification = classSpecifications?.single()?.fieldSpecifications
                fieldSpecification shouldBe null
            }
        }
    }
})
