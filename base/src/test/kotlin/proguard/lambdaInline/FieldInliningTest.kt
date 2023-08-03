package proguard.lambdaInline

import io.kotest.core.spec.style.FreeSpec
import onlyTestRunning
import proguard.testutils.KotlinSource

class FieldInliningTest: FreeSpec ({
    "call lambda from field in method" {
        //setup
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        class MainKtWithAField {
            val f = { i: Int -> i + 1 }
        }

        fun test(a: (Int) -> Int) {
            println(a(12))
        }
        
        fun main() {
            val instance = MainKtWithAField()
            test(instance.f)
        }
        """
        )

        onlyTestRunning(code)
    }

    "call lambda from a non-final field in method" {
        //setup
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        class MainKtWithAField {
            var f = { i: Int -> i + 1 }
        }

        fun test(a: (Int) -> Int) {
            println(a(12))
        }
        
        fun main() {
            val instance = MainKtWithAField()
            test(instance.f)
        }
        """
        )

        onlyTestRunning(code)
    }
})