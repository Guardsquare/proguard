package proguard.optimize

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import proguard.optimize.evaluation.SimpleEnumClassChecker
import proguard.optimize.info.ProgramClassOptimizationInfo
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource

class SimpleEnumClassCheckerTest : FreeSpec({
    "Given a enum class without instance methods" - {
        val (programClassPool, _) =
            ClassPoolBuilder.fromSource(
                JavaSource(
                    "Enum.java",
                    """
                    public enum Enum { FOO, BAR }
                    """.trimIndent(),
                ),
            )

        "Then when checked with SimpleEnumClassChecker" - {
            val enumClass = programClassPool.getClass("Enum")
            enumClass.processingInfo = ProgramClassOptimizationInfo()
            enumClass.accept(SimpleEnumClassChecker())

            "It should be marked as simple" {
                (enumClass.processingInfo as ProgramClassOptimizationInfo).isSimpleEnum shouldBe true
            }
        }
    }

    "Given a enum class with a public instance method" - {
        val (programClassPool, _) =
            ClassPoolBuilder.fromSource(
                JavaSource(
                    "Enum.java",
                    """
                    public enum Enum { 
                        FOO, BAR;
                        public void foo() {
                        
                        }
                    }
                    """.trimIndent(),
                ),
            )

        "Then when checked with SimpleEnumClassChecker" - {
            val enumClass = programClassPool.getClass("Enum")
            enumClass.processingInfo = ProgramClassOptimizationInfo()
            enumClass.accept(SimpleEnumClassChecker())

            "It should not be marked as simple" {
                (enumClass.processingInfo as ProgramClassOptimizationInfo).isSimpleEnum shouldBe false
            }
        }
    }

    "Given a enum class with a private instance method" - {
        val (programClassPool, _) =
            ClassPoolBuilder.fromSource(
                JavaSource(
                    "Enum.java",
                    """
                    public enum Enum { 
                        FOO, BAR;
                        private void foo() {
                        
                        }
                    }
                    """.trimIndent(),
                ),
            )

        "Then when checked with SimpleEnumClassChecker" - {
            val enumClass = programClassPool.getClass("Enum")
            enumClass.processingInfo = ProgramClassOptimizationInfo()
            enumClass.accept(SimpleEnumClassChecker())

            "It should not be marked as simple" {
                (enumClass.processingInfo as ProgramClassOptimizationInfo).isSimpleEnum shouldBe false
            }
        }
    }
})
