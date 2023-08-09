package proguard.lambdaInline.correctness

import compareOutputAndMainInstructions
import io.kotest.core.spec.style.FreeSpec
import proguard.testutils.KotlinSource

class ThreeLambdasInArgsTest: FreeSpec({
    "Three lambda in args, basic case" {
        val code = KotlinSource(
            "Main.kt",
            """
        fun test(a: (Int) -> Int, b: (Int, Int) -> Int, c: (String, Boolean, Int) -> Boolean) {
            println(a(3))
            println(b(5,7))
            println(c("Hello", true, 0))
        }
        
        fun main() {
            test({ a: Int -> a * a }, {a : Int, b : Int -> a+b}, { a : String, b : Boolean, c : Int -> println(a); b })
        }
        """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "Three lambdas in args and instruction before lambda function calls in main" {
        val code = KotlinSource(
            "Main.kt",
            """
        fun test(a: (Int) -> Int, b: (Int, Int) -> Int, c: (String, Boolean, Int) -> Boolean) {
            println(a(3))
            println(b(5,7))
            println(c("Hello", true, 0))
        }
        
        fun main() {
            val x = 1
            val y = 5
            println(x + y)
            test({ a: Int -> a * a }, {a : Int, b : Int -> a+b}, { a : String, b : Boolean, c : Int -> println(a); b})
        }
        """
        )

        compareOutputAndMainInstructions(
            code,
            listOf(
                "iconst_1",
                "istore_0",
                "iconst_5",
                "istore_1",
                "iload_0",
                "iload_1",
                "iadd",
                "istore_2",
                "getstatic",
                "iload_2",
                "invokevirtual",
                "invokestatic",
                "return"
            )
        )
    }
    "Three lambdas in args and two are the same types" {
        val code = KotlinSource(
            "Main.kt",
            """
        fun test(a: (Int) -> Int, b: (Int) -> Int, c: (String, Boolean, Int) -> Boolean) {
            println(a(3))
            println(b(5))
            println(c("Hello", true, 0))
        }
        
        fun main() {
            test({ a: Int -> a * a }, {a : Int -> a+a}, { a : String, b : Boolean, c : Int -> println(a); b})
        }
        """
        )

        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "Three lambdas in args and two are the same types shuffled" {
        val code = KotlinSource(
            "Main.kt",
            """
        fun test(a: (Int) -> Int, b: (String, Boolean, Int) -> Boolean, c: (Int) -> Int) {
            println(a(3))
            println(b("Hello", true, 0))
            println(c(5))
        }
        
        fun main() {
            test({ a: Int -> a * a }, { a : String, b : Boolean, c : Int -> println(a); b}, {a : Int -> a+a})
        }
        """
        )

        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }
})
