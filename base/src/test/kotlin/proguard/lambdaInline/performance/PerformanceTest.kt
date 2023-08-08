package performance

import io.kotest.core.spec.style.FreeSpec
import proguard.testutils.KotlinSource
import testPerf

class PerformanceTest: FreeSpec ({
    "basic case" - {
        "10" {
            //setup
            val code = KotlinSource( //create java file
                "Main.kt",
                """
            fun test(a: (Int) -> Int) {
                a(12)        }
            
            fun main() {
                var index = 0
                while (index < 10) {
                    index += 1
                    test { a: Int -> (a * a) + 1 }
                }
            }
            """
            )

            val inlineCode = KotlinSource(
                "Main.kt",
                """
            inline fun test(a: (Int) -> Int) {
                a(12)        }
            
            fun main() {
                var index = 0
                while (index < 10) {
                    index += 1
                    test { a: Int -> (a * a) + 1 }
                }
            }
                    
                """
            )
            println("easy 10")

            testPerf(code, inlineCode, true)
        }
        "1000" {
            //setup
            val code = KotlinSource( //create java file
                "Main.kt",
                """
            fun test(a: (Int) -> Int) {
                a(12)        }
            
            fun main() {
                var index = 0
                while (index < 1000) {
                    index += 1
                    test { a: Int -> (a * a) + 1 }
                }
            }
            """
            )

            val inlineCode = KotlinSource(
                "Main.kt",
                """
            inline fun test(a: (Int) -> Int) {
                a(12)        }
            
            fun main() {
                var index = 0
                while (index < 1000) {
                    index += 1
                    test { a: Int -> (a * a) + 1 }
                }
            }
                    
                """
            )
            println("easy 1000")

            testPerf(code, inlineCode, true)
        }
        "100000" {
            //setup
            val code = KotlinSource( //create java file
                "Main.kt",
                """
            fun test(a: (Int) -> Int) {
                a(12)        }
            
            fun main() {
                var index = 0
                while (index < 100000) {
                    index += 1
                    test { a: Int -> (a * a) + 1 }
                }
            }
            """
            )

            val inlineCode = KotlinSource(
                "Main.kt",
                """
            inline fun test(a: (Int) -> Int) {
                a(12)        }
            
            fun main() {
                var index = 0
                while (index < 100000) {
                    index += 1
                    test { a: Int -> (a * a) + 1 }
                }
            }
                    
                """
            )
            println("easy 100000")

            testPerf(code, inlineCode, true)
        }
    }

    "hard case" - {
        "1" {
            //setup
            val code = KotlinSource( //create java file
                "Main.kt",
                """
            fun test(a: (Int) -> Int) {
                a(12)
            }
            fun a(b: (String) -> Int) {
                b("Hello World!") + 1
            }
            
            fun test1(a: (Int) -> Int, b: (String, Boolean, Int) -> Boolean, c: (Int) -> Int) {
                a(3)
                b("Hello", true, 0)
                c(5)
            }
            
            
            fun main() {
                var index = 0
                while (index < 1) {
                    index += 1
                    test { a: Int -> (a * a) + 1 }
                    a { b: String ->
                            var cnt = 0
                            var i = 0
                            while (i < b.length) {
                                cnt += b[i].code
                                i++
                            }
                            cnt
                    }
                    test1({ a: Int -> a * a }, { a : String, b : Boolean, c : Int -> a.uppercase(); b}, {a : Int -> a+a})
                }
            }
            """
            )

            val inlineCode = KotlinSource(
                "Main.kt",
                """
            inline fun test(a: (Int) -> Int) {
                a(12)
            }
            inline fun a(b: (String) -> Int) {
                b("Hello World!") + 1
            }
            
            inline fun test1(a: (Int) -> Int, b: (String, Boolean, Int) -> Boolean, c: (Int) -> Int) {
                a(3)
                b("Hello", true, 0)
                c(5)
            }
            
            
            fun main() {
                var index = 0
                while (index < 1) {
                    index += 1
                    test { a: Int -> (a * a) + 1 }
                    a { b: String ->
                            var cnt = 0
                            var i = 0
                            while (i < b.length) {
                                cnt += b[i].code
                                i++
                            }
                            cnt
                    }
                    test1({ a: Int -> a * a }, { a : String, b : Boolean, c : Int -> a.uppercase(); b}, {a : Int -> a+a})
                }
            }
                    
                """
            )
            println("hard 1")

            testPerf(code, inlineCode, false)
        }
        "10" {
            //setup
            val code = KotlinSource( //create java file
                "Main.kt",
                """
            fun test(a: (Int) -> Int) {
                a(12)
            }
            fun a(b: (String) -> Int) {
                b("Hello World!") + 1
            }
            
            fun test1(a: (Int) -> Int, b: (String, Boolean, Int) -> Boolean, c: (Int) -> Int) {
                a(3)
                b("Hello", true, 0)
                c(5)
            }
            
            
            fun main() {
                var index = 0
                while (index < 10) {
                    index += 1
                    test { a: Int -> (a * a) + 1 }
                    a { b: String ->
                            var cnt = 0
                            var i = 0
                            while (i < b.length) {
                                cnt += b[i].code
                                i++
                            }
                            cnt
                    }
                    test1({ a: Int -> a * a }, { a : String, b : Boolean, c : Int -> a.uppercase(); b}, {a : Int -> a+a})
                }
            }
            """
            )

            val inlineCode = KotlinSource(
                "Main.kt",
                """
            inline fun test(a: (Int) -> Int) {
                a(12)
            }
            inline fun a(b: (String) -> Int) {
                b("Hello World!") + 1
            }
            
            inline fun test1(a: (Int) -> Int, b: (String, Boolean, Int) -> Boolean, c: (Int) -> Int) {
                a(3)
                b("Hello", true, 0)
                c(5)
            }
            
            
            fun main() {
                var index = 0
                while (index < 10) {
                    index += 1
                    test { a: Int -> (a * a) + 1 }
                    a { b: String ->
                            var cnt = 0
                            var i = 0
                            while (i < b.length) {
                                cnt += b[i].code
                                i++
                            }
                            cnt
                    }
                    test1({ a: Int -> a * a }, { a : String, b : Boolean, c : Int -> a.uppercase(); b}, {a : Int -> a+a})
                }
            }
                    
                """
            )
            println("hard 10")
            testPerf(code, inlineCode, true)
        }
        "100" {
            //setup
            val code = KotlinSource( //create java file
                "Main.kt",
                """
            fun test(a: (Int) -> Int) {
                a(12)
            }
            fun a(b: (String) -> Int) {
                b("Hello World!") + 1
            }
            
            fun test1(a: (Int) -> Int, b: (String, Boolean, Int) -> Boolean, c: (Int) -> Int) {
                a(3)
                b("Hello", true, 0)
                c(5)
            }
            
            
            fun main() {
                var index = 0
                while (index < 100) {
                    index += 1
                    test { a: Int -> (a * a) + 1 }
                    a { b: String ->
                            var cnt = 0
                            var i = 0
                            while (i < b.length) {
                                cnt += b[i].code
                                i++
                            }
                            cnt
                    }
                    test1({ a: Int -> a * a }, { a : String, b : Boolean, c : Int -> a.uppercase(); b}, {a : Int -> a+a})
                }
            }
            """
            )

            val inlineCode = KotlinSource(
                "Main.kt",
                """
            inline fun test(a: (Int) -> Int) {
                a(12)
            }
            inline fun a(b: (String) -> Int) {
                b("Hello World!") + 1
            }
            
            inline fun test1(a: (Int) -> Int, b: (String, Boolean, Int) -> Boolean, c: (Int) -> Int) {
                a(3)
                b("Hello", true, 0)
                c(5)
            }
            
            
            fun main() {
                var index = 0
                while (index < 100) {
                    index += 1
                    test { a: Int -> (a * a) + 1 }
                    a { b: String ->
                            var cnt = 0
                            var i = 0
                            while (i < b.length) {
                                cnt += b[i].code
                                i++
                            }
                            cnt
                    }
                    test1({ a: Int -> a * a }, { a : String, b : Boolean, c : Int -> a.uppercase(); b}, {a : Int -> a+a})
                }
            }
                    
                """
            )

            println("hard 100")
            testPerf(code, inlineCode, true)
        }
    }
})