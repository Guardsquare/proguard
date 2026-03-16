package proguard

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldNotBe
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource
import testutils.RequiresJavaExtension

class Java19RecordPatternTest : FreeSpec({
    extension(RequiresJavaExtension(from = 19, to = 19))
    "Given a class with Java record pattern" - {
        val (programClassPool, _) =
            ClassPoolBuilder.fromSource(
                JavaSource(
                    "Test.java",
                    """
                    public class Test {
                        public static void main(String[] args) {
                            printPoint(new Point(1, 2));
                        }
                    
                        private static void printPoint(Object o) {
                            if (o instanceof (Point(int x, int y) p)) {
                                System.out.println(p + " = " + (x + y));
                            }
                        }
                    }
                    record Point(int x, int y) {}
                    """.trimIndent(),
                ),
                javacArguments = listOf("--enable-preview", "--release", "19"),
            )

        "Then ProGuard should parse the class correctly" {
            programClassPool.getClass("Test") shouldNotBe null
            programClassPool.getClass("Point") shouldNotBe null
        }
    }
})
