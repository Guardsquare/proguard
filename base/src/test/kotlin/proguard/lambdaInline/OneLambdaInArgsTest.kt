package proguard.lambdaInline

import compareOutputAndMainInstructions
import io.kotest.core.spec.style.FreeSpec
import onlyTestRunning
import proguard.testutils.KotlinSource

class OneLambdaInArgsTest: FreeSpec({
    "One lambda in args, basic case with int" {
        //setup
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

    "One lambda in args, basic case with boolean" {
        //setup
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun test(a: (Boolean) -> Boolean) {
            println(a(false))
        }
        
        fun main() {
            test { a: Boolean -> a and true}
        }
        """
        )

        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "One lambda in args, basic case with char" {
        //setup
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun test(a: (Char) -> Char) {
            println(a('a'))
        }
        
        fun main() {
            test { a: Char -> a}
        }
        """
        )

        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "One lambda in args and instruction before lambda function calls in main" {
        //setup
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun test(a: (Int) -> Int) {
            println(a(3))
        }
        
        fun main() {
            val x=1
            println(x)
            test { a: Int -> (a * a) + 1 }
        }
        """
        )

        compareOutputAndMainInstructions(
            code,
            listOf("iconst_1", "istore_0", "getstatic", "iload_0", "invokevirtual", "invokestatic", "return")
        )
    }

    "One lambda in args with if" {
        //setup
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun test(a: (Int) -> Boolean) {
            println(a(3))
        }
        
        fun main() {
            test { a: Int -> a == 2 }
        }
        """
        )

        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }

    "One lambda but in a class companion object" {
        //setup
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        
        
        fun main() {
            MainKtTest.test { a: Int -> a == 2 }
        }

        class MainKtTest {
            companion object {
                @JvmStatic
                fun test(a: (Int) -> Boolean) {
                    println(a(2))
                }
            }
        }
        """
        )

        compareOutputAndMainInstructions(code, listOf("getstatic", "invokevirtual", "return"))
    }

    "One lambda but in a different class" {
        //setup
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        
        
        fun main() {
            val instance = MainKtTest()
            instance.test { a: Int -> a == 2 }
        }

        class MainKtTest {
            fun test(a: (Int) -> Boolean) {
                println(a(2))
            }
        }
        """
        )

        compareOutputAndMainInstructions(
            code,
            listOf("new", "dup", "invokespecial", "astore_0", "aload_0", "invokevirtual", "return")
        )
    }

    "One lambda in a class + other arguments" {
        //setup
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun main() {
            val instance = MainKtTest()
            instance.foo { it == 2 }
            instance.bar(2) { it * 3 }
            instance.foo { it == 3 }
        }

        class MainKtTest {
            fun foo(a: (Int) -> Boolean) {
                println(a(2))
            }

            fun bar(i: Int, f: (Int) -> Int) {
                println(f(i))
            }
        }
        """
        )

        compareOutputAndMainInstructions(
            code,
            listOf("new", "dup", "invokespecial", "astore_0", "aload_0", "invokevirtual", "aload_0", "iconst_2", "invokevirtual", "aload_0", "invokevirtual", "return")
        )
    }

    "One lambda in a class + other arguments + nested + private" {
        //setup
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun main() {
            val instance = MainKtTest()
            instance.foo({ it * 2 }, { it + 3 })
            instance.bar(2) { it * 3 }
            instance.foo({ it + 3 }, { it * 2 })
        }
        
        class MainKtTest {
            fun foo(f1: (Int) -> Int, f2: (Int) -> Int) {
                level2(2, f2)
                level2(3, f1)
            }
        
            fun bar(i: Int, f: (Int) -> Int) {
                level2(i, f)
            }
        
            private fun level2(i: Int, f: (Int) -> Int) {
                println(f(i))
            }
        }
        """
        )

        onlyTestRunning(code)
    }

    "One lambda in a class with a call to a function in another class" {
        //setup
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun main() {
            val instance = MainKtTest()
            instance.foo { it * 2 }
        }
        
        class MainKtTest {
            val instance = MainKtTest2()
            fun foo(f: (Int) -> Int) {
                instance.level2(2, f)
            }
        }

        class MainKtTest2 {
            val offset = 5
            fun level2(i: Int, f: (Int) -> Int) {
                println(f(i + offset))
            }
        }
        """
        )

        onlyTestRunning(code)
    }

    "One lambda in a class with a call to a function in another class that calls a function in yet another class" {
        //setup
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun main() {
            val instance = MainKtTest()
            instance.foo { it * 2 }
        }
        
        class MainKtTest {
            val instance = MainKtTest2()
            
            fun foo(f: (Int) -> Int) {
                instance.foo(2, f)
            }
        }

        class MainKtTest2 {
            val instance = MainKtTest3()
            val offset = 5
            
            fun foo(i: Int, f: (Int) -> Int) {
                instance.foo(i + offset, f)
            }
        }

        class MainKtTest3 {
            val offset = 7
            
            fun foo(i: Int, f: (Int) -> Int) {
                println(f(i + offset))
            }
        }
        """
        )

        onlyTestRunning(code)
    }

    "One lambda but one other argument after lambda arg" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        
        
        fun test(a: (Int) -> Int, b: Int) {
            println(a(3))
            println(b)
        }
        
        fun main() {
            test({ a: Int -> a + a }, 1)
        }

        """
        )

        compareOutputAndMainInstructions(code, listOf("iconst_1", "invokestatic", "return"))
    }

    "One lambda but one other argument before lambda arg" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        
        
        fun test(b: Int, a: (Int) -> Int) {
            println(a(3))
            println(b)
        }
        
        fun main() {
            test(1) { a: Int -> a + a }
        }

        """
        )

        compareOutputAndMainInstructions(code, listOf("iconst_1", "invokestatic", "return"))
    }

    "One lambda but one other argument before and after lambda arg" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        
        
        fun test(b: Int, a: (Int) -> Int, c: Int) {
            println(a(3))
            println(b)
            println(c)
        }
        
        fun main() {
            test(1, { a: Int -> a + a }, 123)
        }

        """
        )

        compareOutputAndMainInstructions(code, listOf("iconst_1", "bipush", "invokestatic", "return"))
    }
    "One lambda but multiple other arguments before and after lambda arg" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        
        
        fun test(e:Int, b: Int, a: (Int) -> Int, c: Int, d: Int) {
            println(a(3))
            println(b)
            println(c)
            println(d)
            println(e)
        }
        
        fun main() {
            test(12, 1, { a: Int -> a + a }, 123, 122)
        }

        """
        )

        compareOutputAndMainInstructions(
            code,
            listOf("bipush", "iconst_1", "bipush", "bipush", "invokestatic", "return")
        )
    }

    "One lambda but the lambda is consumed by a function called from the consuming method" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun level1(f: (Int) -> Int) {
            println(level2(f))
        }

        fun level2(f: (Int) -> Int): Int {
            return f(5)
        }
        
        fun main() {
            level1 { it * 2}
        }
        """
        )

        onlyTestRunning(code)
    }

    "One lambda but the lambda is consumed by a function called from the consuming method LONG" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun level1(b: Long, f: (Int) -> Int, a: Long) {
            println(level2(f, 26L, 12L, 16L))
        }

        fun level2(f: (Int) -> Int, c: Long, a: Long, b: Long): Int {
            println(a)
            return f(5)
        }
        
        fun main() {
            level1(26L, { it * 2}, 7L) 
        }
        """
        )

        onlyTestRunning(code)
    }

    "One lambda but the lambda is consumed by multiple other methods from the original consuming method" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun level1(f: (Int) -> Int) {
            println(level2_1(f))
            println(level2_2(f))
        }

        fun level2_1(f: (Int) -> Int): Int {
            return f(5)
        }

        fun level2_2(f: (Int) -> Int): Int {
            return f(7)
        }
        
        fun main() {
            level1 { it * 2}
        }
        """
        )

        onlyTestRunning(code)
    }

    "One lambda but the lambda at even more levels" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun level1(f: (Int) -> Int) {
            println(level2_1(f))
            println(level2_2(f))
        }

        fun level2_1(f: (Int) -> Int): Int {
            return f(5)
        }

        fun level2_2(f: (Int) -> Int): Int {
            return level3(f)
        }

        fun level3(f: (Int) -> Int): Int {
            return f(7)
        }
        
        fun main() {
            level1 { it * 2}
        }
        """
        )

        onlyTestRunning(code)
    }

    "One lambda but the lambda at even more levels + private" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
        fun level1(f: (Int) -> Int) {
            println(level2_1(f))
            println(level2_2(f))
        }

        fun level2_1(f: (Int) -> Int): Int {
            return f(5)
        }

        private fun level2_2(f: (Int) -> Int): Int {
            return level3(f)
        }

        fun level3(f: (Int) -> Int): Int {
            return f(7)
        }
        
        fun main() {
            level1 { it * 2}
        }
        """
        )

        onlyTestRunning(code)
    }

    "Lambda in a variable" {
        val code = KotlinSource(
            "Main.kt",
            """
            fun level1(f: (Int) -> Int) {
                val x = 5 * f(2)
                println(x)
                val renamedFunction = f
                println(level2_2(f))
                println(level2_1(renamedFunction))
            }
            
            fun level2_1(f: (Int) -> Int): Int {
                return f(5)
            }
                
            fun level2_2(f: (Int) -> Int): Int {
                return level3(f)
            }
                        
            fun level3(f: (Int) -> Int): Int {
                return f(7)
            }
            
            
            fun main() {
                println("Hello there!")
                level1 { it * 2}
            }
            """.trimIndent()
        )

        onlyTestRunning(code)
    }

    "Lambda in a variable more advanced" {
        val code = KotlinSource(
            "Main.kt",
            """
            // Note: The lambda x currently does not get inlined because it is not detected in the usage pattern finder
            // but in the future if we do try inlining it, it cannot be inlined if it is used in println but that's very
            // uncommon anyway and only used for debugging purposes.
            fun level1(f: (Int) -> Int) {
                var x : (Int) -> Int = { it * 2 }
                println(x)
                val f2 = f
                println(level2_2(f))
                println(level2_1(f2))
                println(level2_1(x))
            }
            
            fun level2_1(f: (Int) -> Int): Int {
                return f(5)
            }
                
            fun level2_2(f: (Int) -> Int): Int {
                return level3(f)
            }
                        
            fun level3(f: (Int) -> Int): Int {
                return f(7)
            }
            
            
            fun main() {
                level1 { it * 2}
            }
            """.trimIndent()
        )

        onlyTestRunning(code)
    }

    "Lambda in a variable multiple usages" {
        val code = KotlinSource(
            "Main.kt",
            """
            fun foo(i: Int, f: (Int) -> Int) {
                println(f(i))
            }
            
            fun main() {
                val x : (Int) -> Int = { it * 2 }
                foo(1, x)
                foo(2, x)
            }
            """.trimIndent()
        )

        // TODO: Currently broken due to a bug in the proguard-core test utils
        //compareOutputAndMainInstructions(code, listOf("iconst_1", "invokestatic", "iconst_2", "invokestatic", "return"))
    }

    "Lambda in a variable multiple usages with a sum of 2 functions that take lambdas" {
        val code = KotlinSource(
            "Main.kt",
            """
            fun foo(i: Int, f: (Int) -> Int): Int {
                return f(i)
            }
            
            fun main() {
                val x : (Int) -> Int = { it * 2 }
                val y : (Int) -> Int = { it * 2 }
                println(foo(2, y))                
                println(foo(1, x) + foo(2, x))
            }
            """.trimIndent()
        )

        compareOutputAndMainInstructions(code, listOf(
            "iconst_2",
            "invokestatic",
            "istore_2",
            "getstatic",
            "iload_2",
            "invokevirtual",
            "iconst_1",
            "invokestatic",
            "iconst_2",
            "invokestatic",
            "iadd",
            "istore_2",
            "getstatic",
            "iload_2",
            "invokevirtual",
            "return"
        ))
    }

    "Don't inline lambda in a variable that changes depending on an if statement" {
        val code = KotlinSource(
            "Main.kt",
            """
            fun foo(i: Int, f: (Int) -> Int) {
                println(f(i))
            }
            
            fun main() {
                var x : (Int) -> Int = { it * 2 }
            
                var i = 0
                if (i * 5 == 0) {
                    x = { it * 3 }
                }
            
                foo(1, x)
            }
            """.trimIndent()
        )

        compareOutputAndMainInstructions(code, listOf(
            "getstatic",
            "checkcast",
            "astore_0",
            "iconst_0",
            "istore_1",
            "iload_1",
            "iconst_5",
            "imul",
            "ifne",
            "getstatic",
            "checkcast",
            "astore_0",
            "iconst_1",
            "aload_0",
            "invokestatic",
            "return"
        ), false)
    }

    "Not using lambda return value" {
        val code = KotlinSource( //create java file
            "Main.kt",
            """
            fun test(a: (Int) -> Int) {
                a(12)
            }
            
            fun main() {
                test { a: Int -> (a * a) + 1 }
            }
            """
        )
        compareOutputAndMainInstructions(code, listOf("invokestatic", "return"))
    }
})
