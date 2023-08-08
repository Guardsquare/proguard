package proguard.lambdaInline.correctness

import compareOutputAndMainInstructions
import io.kotest.core.spec.style.FreeSpec
import proguard.testutils.KotlinSource

class TypeTest: FreeSpec ({
    "Byte" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (Byte) -> Byte) {
                println(a(12))
            }
            
            fun main() {
                test { a: Byte -> (a + 1).toByte() }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "Short" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (Short) ->Short) {
                println(a(12))
            }
            
            fun main() {
                test { a: Short -> (a + 1).toShort() }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "Int" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (Int) -> Int) {
                println(a(12))
            }
            
            fun main() {
                test { a: Int -> (a * a) + 1 }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }


    "Long" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (Long) -> Long) {
                println(a(12L))
            }
            
            fun main() {
                test { a: Long -> a + 2L }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "Long in consumingMethod args" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(c:Long, a: (Long) -> Long, b: Long) {
                println(b)
                println(c)
                println(a(b))
            }
            
            fun main() {
                test(42L, { a: Long -> a + 2L }, 12L)
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("ldc2_w", "ldc2_w", "invokestatic", "return"))
    }

    "Float" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (Float) -> Float) {
                println(a(12f))
            }
            
            fun main() {
                test { a: Float -> a + 2.3f }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "Double" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (Double) -> Double) {
                println(a(3.14))
            }
            
            fun main() {
                test { a: Double -> a + 2.3 }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "Boolean" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (Boolean) -> Boolean) {
                println(a(true))
            }
            
            fun main() {
                test { a: Boolean -> a and true }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "Char" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (Char) -> Char) {
                println(a('j'))
            }
            
            fun main() {
                test { a: Char -> a.uppercaseChar() }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "String" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (String) -> String) {
                println(a("Hello World!"))
            }
            
            fun main() {
                test { a: String -> a + " And you!" }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "ByteArray" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (ByteArray) -> ByteArray) {
                println(a(byteArrayOf(1, 5, 3)).contentToString())
            }
            
            fun main() {
                test { a: ByteArray -> byteArrayOf(1) + a }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "ShortArray" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (ShortArray) -> ShortArray) {
                println(a(shortArrayOf(1, 5, 3)).contentToString())
            }
            
            fun main() {
                test { a: ShortArray -> shortArrayOf(1) + a }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "IntArray" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (IntArray) -> IntArray) {
                println(a(intArrayOf(1, 5, 3)).contentToString())
            }
            
            fun main() {
                test { a: IntArray -> intArrayOf(1) + a }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "LongArray" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (LongArray) -> LongArray) {
                println(a(longArrayOf(1, 5, 3)).contentToString())
            }
            
            fun main() {
                test { a: LongArray -> longArrayOf(1) + a }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "FloatArray" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (FloatArray) -> FloatArray) {
                println(a(floatArrayOf(1.12f, 5f, 3.89f)).contentToString())
            }
            
            fun main() {
                test { a: FloatArray -> floatArrayOf(1.2f) + a }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "DoubleArray" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (DoubleArray) -> DoubleArray) {
                println(a(doubleArrayOf(1.12, 5.0, 3.89)).contentToString())
            }
            
            fun main() {
                test { a: DoubleArray -> doubleArrayOf(1.2) + a }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "CharArray" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (CharArray) -> CharArray) {
                println(a(charArrayOf('a', 't', '%')).contentToString())
            }
            
            fun main() {
                test { a: CharArray -> charArrayOf('5') + a }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "StringArray" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (Array<String>) -> Array<String>) {
                println(a(arrayOf("raspberry", "pear", "banana")).contentToString())
            }
            
            fun main() {
                test { a: Array<String> -> arrayOf("apple") + a }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "Any" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (Any) -> Any) {
                println(a(arrayOf("raspberry", "pear", "banana"))::class)
            }
            
            fun main() {
                test { a: Any -> a }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

})