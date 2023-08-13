package proguard.lambdainline.correctness

import compareOutputAndMainInstructions
import io.kotest.core.spec.style.FreeSpec
import onlyTestRunning
import proguard.testutils.KotlinSource

class TwoLambdasInArgsTest: FreeSpec({
    "Two lambda in args, basic case" {
        val code = KotlinSource(
            "Main.kt",
            """
        fun test(a: (Int) -> Int, b: (Int, Int) -> Int) {
            println(a(3))
            println(b(5,7))
        }
        
        fun main() {
            test({ a: Int -> a * a }, {a : Int, b : Int -> a+b})
        }
        """
        )

        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "Two lambdas in args and instruction before lambda function calls in main" {
        val code = KotlinSource(
            "Main.kt",
            """
        fun test(a: (Int) -> Int, b: (Int, Int) -> Int) {
            println(a(3))
            println(b(5,7))
        }
        
        fun main() {
            val x=1
            println(x)
            test({ a: Int -> a * a }, {a : Int, b : Int -> a+b})
}
        """
        )

        compareOutputAndMainInstructions(
            code,
            listOf("iconst_1", "istore_0", "getstatic", "iload_0", "invokevirtual", "invokestatic", "return")
        )
    }

    "Twice the same lambda types in args" {
        val code = KotlinSource(
            "Main.kt",
            """
        fun test(a: (Int) -> Int, b: (Int) -> Int) {
            println(a(3))
            println(b(5))
            println((a(12) - b(15)))
        }
        
        fun main() {
            test({ a: Int -> a * a }, {a : Int -> a+a})
        }
        """
        )

        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "Twice the same lambda types in args + one lambda with one arg" {
        val code = KotlinSource(
            "Main.kt",
            """
        fun test(a: (Int) -> Int, b: (Int) -> Int) {
            println(a(3))
            println(b(5))
            println((a(12) - b(15)))
        }

        fun test(f: (Int) -> Int) {
            println(f(3))
        }
        
        fun main() {
            test {it * 2}
        
            test({ a: Int -> a * a }, {a : Int -> a + a})
        }
        """
        )

        compareOutputAndMainInstructions(code, listOf("invokestatic", "invokestatic", "return"))
    }

    "Two lambdas but multiple other arguments before and after lambda arg" {
        val code = KotlinSource(
            "Main.kt",
            """
        
        
        fun test(e:Int, b: Int, a: (Int) -> Int, c: Int, f: (Int) -> Int, d: Int) {
            println(a(3))
            println(b)
            println(c)
            println(d)
            println(e)
            println(f(3))
        }

        fun main() {
            test(12, 1, { a: Int -> a + a }, 123, { a: Int -> a * a }, 122)
        }
        """
        )

        compareOutputAndMainInstructions(
            code,
            listOf("bipush", "iconst_1", "bipush", "bipush", "invokestatic", "return")
        )
    }


    "Twice the same lambda types in args multiple levels" {
        val code = KotlinSource(
            "Main.kt",
            """
        fun level1(a: (Int) -> Int, b: (Int) -> Int) {
            level2(b)
            level2(a)
        }

        fun level2(f: (Int) -> Int) {
            println(f(3))
        }
        
        fun main() {
            level1({ a -> a * a }, {a -> a + a})
        }
        """
        )

        onlyTestRunning(code)
    }

    "Twice the same lambda types in args multiple levels with one non lambda before the lambdas" {
        val code = KotlinSource(
            "Main.kt",
            """
        fun level1(i1: Int, a: (Int) -> Int, b: (Int) -> Int) {
            level2(b)
            level2(a)
        }

        fun level2(f: (Int) -> Int) {
            println(f(3))
        }
        
        fun main() {
            level1(7, { a -> a * a }, {a -> 26})
        }
        """
        )

        onlyTestRunning(code)
    }
})
