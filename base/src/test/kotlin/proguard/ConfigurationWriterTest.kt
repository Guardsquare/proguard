package proguard

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Test printing of the configuration (-printconfiguration option).
 */
class ConfigurationWriterTest : FreeSpec({
    fun printConfiguration(rules: String): String {
        val out = StringWriter()
        val configuration = Configuration()

        ConfigurationParser(rules, "", null, System.getProperties()).use {
            it.parse(configuration)
        }

        ConfigurationWriter(PrintWriter(out)).use {
            it.write(configuration)
        }

        return out.toString().trim()
    }

    "Hash character handling tests" - {
        "Comments should not be quoted" {
            printConfiguration("# comment\n-keep class **") shouldBe "# comment\n-keep class **"
        }

        "Hash characters in comments should not be quoted" {
            printConfiguration("# #comment\n-keep class **") shouldBe "# #comment\n-keep class **"
        }
    }

    "Given a -dontnote rule specifying a class name" - {
        val rules = "-dontnote com.example.MyClass"

        "When the rule is parsed and printed out again" - {
            val out = printConfiguration(rules)

            "Then the printed rule should be the same as the given rule" {
                out shouldBe rules
            }
        }
    }

    "Given a -dontnote rule specifying a class name with wildcards" - {
        val rules = "-dontnote com.example.**"

        "When the rule is parsed and printed out again" - {
            val out = printConfiguration(rules)

            "Then the printed rule should be the same as the given rule" {
                out shouldBe rules
            }
        }
    }

    "Given a -addconfigurationdebugging rule" - {
        val rules = "-addconfigurationdebugging"

        "When the rules are parsed and printed out again" - {
            val out = printConfiguration(rules)

            "Then the rules should be present in the output" {
                out shouldContain rules
            }
        }
    }

    "Given an -optimizeaggressively rule" - {
        val rules = "-optimizeaggressively"

        "When the rule is parsed and printed out again" - {
            val out = printConfiguration(rules)

            "Then the rule should be present in the output" {
                out shouldBe rules
            }
        }
    }
})
