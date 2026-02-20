package proguard.optimize

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.string.shouldContain
import proguard.AppView
import proguard.classfile.ClassPool
import proguard.classfile.Clazz
import proguard.classfile.kotlin.visitor.KotlinMetadataPrinter
import proguard.classfile.util.AllParameterVisitor
import proguard.classfile.visitor.ClassCleaner
import proguard.classfile.visitor.MemberNameFilter
import proguard.classfile.visitor.ParameterVisitor
import proguard.mark.Marker
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.KotlinSource
import testutils.asConfiguration
import java.io.PrintWriter
import java.io.StringWriter

class KotlinContextParameterShrinkingTest : StringSpec({
    // TODO(T18173): These tests currently check that the Context Receivers list
    //               is *not* shrunk and that the JVM method parameter is *not* removed.
    //               Ideally, we would be able to shrink the context receivers list in-sync
    //               with existing optimizations.

    "Given a Kotlin function with ContextParameters" {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                class Unused {
                    fun info(message: String) {
                      println(message)
                    }
                }
                
                class Used {
                    fun used(message: String) { }
                }

                context(used: Used, unused: Unused)
                fun String.foo() {
                    used.used(this) 
                } 
                
                fun main() {
                  with (Unused()) { with (Used()) { "test".foo() } }
                }
                """.trimIndent(),
            ),
            kotlincArguments = listOf(
                "-Xcontext-parameters",
                // Disable generation of instrinsics null checks
                // In real world use-cases, -assumenosideeffects might be used
                "-Xno-param-assertions",
            ),
        )

        optimize(programClassPool, libraryClassPool)

        val testKt = programClassPool.getClass("TestKt")
        val kotlinMetadata = testKt.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTPA] unused"
        kotlinMetadata shouldContain "[CTPA] used"

        verifyParameters(testKt, "foo*")
    }

    "Given a Kotlin function with default parameters with ContextParameters" {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                class Unused {
                    fun info(message: String) {
                      println(message)
                    }
                }
                
                class Used {
                    fun bar(message: String) { }
                }

                context(used: Used, unused: Unused)
                fun String.foo(message: String = "test") {
                    println(this) // use the String receiver
                    used.bar(message) 
                } 

                fun main() {
                  with (Unused()) { with (Used()) { "test".foo() } }
                }
                """.trimIndent(),
            ),
            kotlincArguments = listOf(
                "-Xcontext-parameters",
                // Disable generation of instrinsics null checks
                // In real world use-cases, -assumenosideeffects might be used
                "-Xno-param-assertions",
            ),
        )

        optimize(programClassPool, libraryClassPool)

        val testKt = programClassPool.getClass("TestKt")
        val kotlinMetadata = testKt.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTPA] unused"
        kotlinMetadata shouldContain "[CTPA] used"

        verifyParameters(testKt, "foo*")
    }

    "Given a Kotlin property with ContextParameters used in the getter" {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                class Unused {
                    fun info(message: String) {
                      println(message)
                    }
                }
                
                class Used {
                    fun barfo(message: String) { }
                }

                context(used: Used, unused: Unused)
                val String.property: String
                    get() {
                        println(this)
                        used.barfo("bar") 
                        return "test"
                    } 
                
                fun main() {
                  with (Unused()) { with (Used()) { "test".property } }
                }
                """.trimIndent(),
            ),
            kotlincArguments = listOf(
                "-Xcontext-parameters",
                // Disable generation of instrinsics null checks
                // In real world use-cases, -assumenosideeffects might be used
                "-Xno-param-assertions",
            ),
        )

        optimize(programClassPool, libraryClassPool)

        val testKt = programClassPool.getClass("TestKt")
        val kotlinMetadata = testKt.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTPA] unused"
        kotlinMetadata shouldContain "[CTPA] used"

        verifyParameters(testKt, "get*,set*")
    }

    "Given a Kotlin property with a single ContextParameters used both in the getter and setter" {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                class Used2 {
                    fun used2(message: String) {
                      println(message)
                    }
                }
                
                class Used {
                    fun used(message: String) { }
                }

                context(used: Used, used2: Used2)
                var String.property: String
                    set(value) {
                        used.used(value)
                        used2.used2(value)
                    }
                    get() {
                        used.used("bar") 
                        used2.used2(this)
                        return "test"
                    } 
                
                fun main() {
                  with (Used2()) { with (Used()) { "test".property } }
                }
                """.trimIndent(),
            ),
            kotlincArguments = listOf(
                "-Xcontext-parameters",
                // Disable generation of instrinsics null checks
                // In real world use-cases, -assumenosideeffects might be used
                "-Xno-param-assertions",
            ),
        )

        optimize(programClassPool, libraryClassPool)

        val testKt = programClassPool.getClass("TestKt")
        val kotlinMetadata = testKt.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTPA] used2"
        kotlinMetadata shouldContain "[CTPA] used"

        verifyParameters(testKt, "get*,set*", p1 = "LUsed;", p2 = "LUsed2;")
    }

    "Given a Kotlin property with a single ContextParameter used in only the setter" {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                class Used2 {
                    fun used2(message: String) {
                      println(message)
                    }
                }
                
                class Used {
                    fun used(message: String) { }
                }

                context(used: Used, used2: Used2)
                var String.property: String
                    set(value) {
                        used2.used2(value)
                    }
                    get() {
                        return "test"
                    } 
                
                fun main() {
                  with (Used2()) { with (Used()) { "test".property } }
                }
                """.trimIndent(),
            ),
            kotlincArguments = listOf(
                "-Xcontext-parameters",
                // Disable generation of instrinsics null checks
                // In real world use-cases, -assumenosideeffects might be used
                "-Xno-param-assertions",
            ),
        )

        optimize(programClassPool, libraryClassPool)

        val testKt = programClassPool.getClass("TestKt")
        val kotlinMetadata = testKt.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTPA] used2"
        kotlinMetadata shouldContain "[CTPA] used"
        verifyParameters(testKt, "get*,set*", p1 = "LUsed;", p2 = "LUsed2;")
    }
    "Given a Kotlin property with ContextParameters used in only the getter but also containing a setter" {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                class Unused {
                    fun info(message: String) {
                      println(message)
                    }
                }
                
                class Used {
                    fun used(message: String) { }
                }

                context(used: Used, unused: Unused)
                var String.property: String
                    set(value) { }
                    get() {
                        used.used(this) 
                        return "test"
                    } 
                
                fun main() {
                  with (Unused()) { with (Used()) { "test".property } }
                }
                """.trimIndent(),
            ),
            kotlincArguments = listOf(
                "-Xcontext-parameters",
                // Disable generation of instrinsics null checks
                // In real world use-cases, -assumenosideeffects might be used
                "-Xno-param-assertions",
            ),
        )

        optimize(programClassPool, libraryClassPool)

        val testKt = programClassPool.getClass("TestKt")
        val kotlinMetadata = testKt.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTPA] unused"
        kotlinMetadata shouldContain "[CTPA] used"
        verifyParameters(testKt, "get*,set*")
    }
})

private fun verifyParameters(testKt: Clazz, s: String, p1: String = "LUsed;", p2: String = "LUnused;") {
    val parameterTypes = mutableListOf<String>()
    val parameterVisitor = ParameterVisitor { _, _, _, _, _, _, parameterType, _ -> parameterTypes += parameterType }
    testKt.methodsAccept(MemberNameFilter(s, AllParameterVisitor(false, parameterVisitor)))
    parameterTypes shouldContainInOrder listOf(p1, p2)
}

private fun optimize(programClassPool: ClassPool, libraryClassPool: ClassPool) {
    val config = """
        -keep,allowoptimization,allowshrinking class TestKt,ClassWithProperty,MyClass { *; }
        -optimizations **
    """.asConfiguration()
    val appView = AppView(programClassPool, libraryClassPool)
    programClassPool.classesAccept { it.accept(ClassCleaner()) }
    Marker(config).execute(appView)
    val optimizer = Optimizer(config)
    // Run two passes to ensure that e.g. unread fields are removed,
    // which can then allow parameters to then be removed.
    optimizer.execute(appView)
    optimizer.execute(appView)
}

private val Clazz.kotlinMetadataAsString: String
    get() {
        val stringWriter = StringWriter()
        kotlinMetadataAccept(KotlinMetadataPrinter(PrintWriter(stringWriter)))
        return stringWriter.toString()
    }
