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
    val EOL = System.lineSeparator()
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
        "Option parameters with hash characters should be quoted" {
            printConfiguration("-keystorepassword '#tester'") shouldBe "-keystorepassword '#tester'"
        }

        "Comments should not be quoted" {
            printConfiguration("# comment$EOL-keep class **") shouldBe "# comment$EOL-keep class **"
        }

        "Hash characters in comments should not be quoted" {
            printConfiguration("# #comment$EOL-keep class **") shouldBe "# #comment$EOL-keep class **"
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

    "Given a -alwaysinline rule" - {
        val rules = "-alwaysinline class ** {*;}"

        "When the rule is parsed and printed out again" - {
            val out = printConfiguration(rules)

            "Then the rule should be not be present in the output" {
                out shouldBe ""
            }
        }

        "When the rule does not exist it shouldn't be printed out" - {
            val out = printConfiguration("")

            "Then the rule should not be present in the output" {
                out shouldBe ""
            }
        }
    }

    "Given a -identifiernamestring rule" - {
        val rules = "-identifiernamestring class ** {*;}"

        "When the rule is parsed and printed out again" - {
            val out = printConfiguration(rules)

            "Then the rule should be not be present in the output" {
                out shouldBe ""
            }
        }

        "When the rule does not exist it shouldn't be printed out" - {
            val out = printConfiguration("")

            "Then the rule should not be present in the output" {
                out shouldBe ""
            }
        }
    }
})
