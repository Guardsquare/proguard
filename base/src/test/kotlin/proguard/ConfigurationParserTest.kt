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
import proguard.classfile.AccessConstants.PUBLIC
import testutils.asConfiguration

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
