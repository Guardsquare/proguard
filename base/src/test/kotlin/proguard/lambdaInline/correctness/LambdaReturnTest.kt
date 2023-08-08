package proguard.lambdaInline.correctness

import io.kotest.core.spec.style.FreeSpec
import onlyTestRunning
import proguard.testutils.KotlinSource

class LambdaReturnTest : FreeSpec({
    "Inline a lambda which is returned from a method" {
        val code = KotlinSource(
            "Main.kt",
            """
            fun test(): (Int) -> Int {
                return { it * 2 }
            }

            fun consumer(f: (Int) -> Int) {
                println(f(7))
            }

            fun main() {
                consumer(test())    
            }
            """
        )

        onlyTestRunning(code)
    }
})
