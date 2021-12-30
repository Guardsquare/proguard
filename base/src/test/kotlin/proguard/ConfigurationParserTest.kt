/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec

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
})
