package proguard.optimize

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.spyk
import io.mockk.verify
import proguard.AppView
import proguard.classfile.ClassPool
import proguard.classfile.Clazz
import proguard.classfile.Member
import proguard.classfile.kotlin.visitor.KotlinMetadataPrinter
import proguard.classfile.util.AllParameterVisitor
import proguard.classfile.visitor.ClassCleaner
import proguard.classfile.visitor.ClassPrinter
import proguard.classfile.visitor.MemberNameFilter
import proguard.classfile.visitor.ParameterVisitor
import proguard.mark.Marker
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.KotlinSource
import testutils.asConfiguration
import java.io.PrintWriter
import java.io.StringWriter

class KotlinContextReceiverParameterShrinkingTest : StringSpec({
    // TODO(T18173): These tests currently check that the Context Receivers list
    //               is *not* shrunk and that the JVM method parameter is *not* removed.
    //               Ideally, we would be able to shrink the context receivers list in-sync
    //               with existing optimizations.

    "Given a Kotlin function with ContextReceivers" {
        val (programClassPool, libraryClassPool) =
            ClassPoolBuilder.fromSource(
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

                    context(Used, Unused)
                    fun String.foo() {
                        used(this) 
                    } 
                    
                    fun main() {
                      with (Unused()) { with (Used()) { "test".foo() } }
                    }
                    """.trimIndent(),
                ),
                kotlincArguments =
                    listOf(
                        "-Xcontext-receivers",
                        // Disable generation of instrinsics null checks
                        // In real world use-cases, -assumenosideeffects might be used
                        "-Xno-param-assertions",
                    ),
            )

        optimize(programClassPool, libraryClassPool)

        val testKt = programClassPool.getClass("TestKt")
        val kotlinMetadata = testKt.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTRE] Unused"
        kotlinMetadata shouldContain "[CTRE] Used"

        verifyParameters(testKt, "foo*")
    }

    "Given a Kotlin function with default parameters with ContextReceivers" {
        val (programClassPool, libraryClassPool) =
            ClassPoolBuilder.fromSource(
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

                    context(Used, Unused)
                    fun String.foo(message: String = "test") {
                        println(this) // use the String receiver
                        bar(message) 
                    } 

                    fun main() {
                      with (Unused()) { with (Used()) { "test".foo() } }
                    }
                    """.trimIndent(),
                ),
                kotlincArguments =
                    listOf(
                        "-Xcontext-receivers",
                        // Disable generation of instrinsics null checks
                        // In real world use-cases, -assumenosideeffects might be used
                        "-Xno-param-assertions",
                    ),
            )

        optimize(programClassPool, libraryClassPool)

        val testKt = programClassPool.getClass("TestKt")
        val kotlinMetadata = testKt.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTRE] Unused"
        kotlinMetadata shouldContain "[CTRE] Used"
        testKt.accept(ClassPrinter())

        verifyParameters(testKt, "foo*")
    }

    "Given a Kotlin property with ContextReceivers used in the getter" {
        val (programClassPool, libraryClassPool) =
            ClassPoolBuilder.fromSource(
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

                    context(Used, Unused)
                    val String.property: String
                        get() {
                            println(this)
                            barfo("bar") 
                            return "test"
                        } 
                    
                    fun main() {
                      with (Unused()) { with (Used()) { "test".property } }
                    }
                    """.trimIndent(),
                ),
                kotlincArguments =
                    listOf(
                        "-Xcontext-receivers",
                        // Disable generation of instrinsics null checks
                        // In real world use-cases, -assumenosideeffects might be used
                        "-Xno-param-assertions",
                    ),
            )

        optimize(programClassPool, libraryClassPool)

        val testKt = programClassPool.getClass("TestKt")
        val kotlinMetadata = testKt.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTRE] Unused"
        kotlinMetadata shouldContain "[CTRE] Used"

        verifyParameters(testKt, "get*,set*")
    }

    "Given a Kotlin property with a single ContextReceivers used both in the getter and setter" {
        val (programClassPool, libraryClassPool) =
            ClassPoolBuilder.fromSource(
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

                    context(Used, Used2)
                    var String.property: String
                        set(value) {
                            used(value)
                            used2(value)
                        }
                        get() {
                            used("bar") 
                            used2(this)
                            return "test"
                        } 
                    
                    fun main() {
                      with (Used2()) { with (Used()) { "test".property } }
                    }
                    """.trimIndent(),
                ),
                kotlincArguments =
                    listOf(
                        "-Xcontext-receivers",
                        // Disable generation of instrinsics null checks
                        // In real world use-cases, -assumenosideeffects might be used
                        "-Xno-param-assertions",
                    ),
            )

        optimize(programClassPool, libraryClassPool)

        val testKt = programClassPool.getClass("TestKt")
        val kotlinMetadata = testKt.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTRE] Used2"
        kotlinMetadata shouldContain "[CTRE] Used"

        verifyParameters(testKt, "get*,set*", p1 = "LUsed;", p2 = "LUsed2;")
    }

    "Given a Kotlin property with a single ContextReceivers used in only the setter" {
        val (programClassPool, libraryClassPool) =
            ClassPoolBuilder.fromSource(
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

                    context(Used, Used2)
                    var String.property: String
                        set(value) {
                            used2(value)
                        }
                        get() {
                            return "test"
                        } 
                    
                    fun main() {
                      with (Used2()) { with (Used()) { "test".property } }
                    }
                    """.trimIndent(),
                ),
                kotlincArguments =
                    listOf(
                        "-Xcontext-receivers",
                        // Disable generation of instrinsics null checks
                        // In real world use-cases, -assumenosideeffects might be used
                        "-Xno-param-assertions",
                    ),
            )

        optimize(programClassPool, libraryClassPool)

        val testKt = programClassPool.getClass("TestKt")
        val kotlinMetadata = testKt.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTRE] Used2"
        kotlinMetadata shouldContain "[CTRE] Used"
        verifyParameters(testKt, "get*,set*", p1 = "LUsed;", p2 = "LUsed2;")
    }
    "Given a Kotlin property with ContextReceivers used in only the getter but also containing a setter" {
        val (programClassPool, libraryClassPool) =
            ClassPoolBuilder.fromSource(
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

                    context(Used, Unused)
                    var String.property: String
                        set(value) { }
                        get() {
                            used(this) 
                            return "test"
                        } 
                    
                    fun main() {
                      with (Unused()) { with (Used()) { "test".property } }
                    }
                    """.trimIndent(),
                ),
                kotlincArguments =
                    listOf(
                        "-Xcontext-receivers",
                        // Disable generation of instrinsics null checks
                        // In real world use-cases, -assumenosideeffects might be used
                        "-Xno-param-assertions",
                    ),
            )

        optimize(programClassPool, libraryClassPool)

        val testKt = programClassPool.getClass("TestKt")
        val kotlinMetadata = testKt.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTRE] Unused"
        kotlinMetadata shouldContain "[CTRE] Used"
        verifyParameters(testKt, "get*,set*")
    }

    "Given a Kotlin property with ContextReceivers with no getter or setter" {
        val (programClassPool, libraryClassPool) =
            ClassPoolBuilder.fromSource(
                KotlinSource(
                    "Test.kt",
                    """
                    class Unused1
                    class Unused2

                    class ClassWithProperty {
                        context(Unused1, Unused2)
                        val property: String by lazy { "foo" }
                    }
                    
                    fun main() {
                      with (Unused2()) { with (Unused1()) { ClassWithProperty().property } }
                    }
                    """.trimIndent(),
                ),
                kotlincArguments =
                    listOf(
                        "-Xcontext-receivers",
                        // Disable generation of instrinsics null checks
                        // In real world use-cases, -assumenosideeffects might be used
                        "-Xno-param-assertions",
                    ),
            )

        val classWithProperty = programClassPool.getClass("ClassWithProperty")

        optimize(programClassPool, libraryClassPool)

        val kotlinMetadata = classWithProperty.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTRE] Unused1"
        kotlinMetadata shouldContain "[CTRE] Unused2"
        verifyParameters(classWithProperty, "get*", p1 = "LUnused1;", p2 = "LUnused2;")
    }

    "Given a Kotlin class with ContextReceivers" {
        val (programClassPool, libraryClassPool) =
            ClassPoolBuilder.fromSource(
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

                    context(Used, Unused)
                    class MyClass {
                        fun foo() {
                            barfo("test")
                        } 
                    } 
                    
                    fun main() {
                      with (Unused()) { with (Used()) { MyClass().foo() } }
                    }
                    """.trimIndent(),
                ),
                kotlincArguments =
                    listOf(
                        "-Xcontext-receivers",
                        // Disable generation of instrinsics null checks
                        // In real world use-cases, -assumenosideeffects might be used
                        "-Xno-param-assertions",
                    ),
            )

        optimize(programClassPool, libraryClassPool)

        val myClass = programClassPool.getClass("MyClass")
        val kotlinMetadata = myClass.kotlinMetadataAsString
        kotlinMetadata shouldContain "[CTRE] Unused"
        kotlinMetadata shouldContain "[CTRE] Used"

        verifyParameters(myClass, "<init>")
    }
})

private fun verifyParameters(
    testKt: Clazz,
    s: String,
    p1: String = "LUsed;",
    p2: String = "LUnused;",
) {
    val parameterVisitor = spyk<ParameterVisitor>()
    testKt.methodsAccept(MemberNameFilter(s, AllParameterVisitor(false, parameterVisitor)))
    verify {
        parameterVisitor.visitParameter(
            testKt,
            ofType<Member>(),
            ofType<Int>(),
            ofType<Int>(),
            ofType<Int>(),
            ofType<Int>(),
            withArg {
                it shouldBe p1
            },
            ofType<Clazz>(),
        )

        parameterVisitor.visitParameter(
            testKt,
            ofType<Member>(),
            ofType<Int>(),
            ofType<Int>(),
            ofType<Int>(),
            ofType<Int>(),
            withArg {
                it shouldBe p2
            },
            ofType<Clazz>(),
        )
    }
}

private fun optimize(
    programClassPool: ClassPool,
    libraryClassPool: ClassPool,
) {
    val config =
        """
        -keep,allowoptimization,allowshrinking class TestKt,ClassWithProperty,MyClass { *; }
        -optimizations **
    """.asConfiguration()
    val appView = AppView(programClassPool, libraryClassPool)
    programClassPool.classesAccept { it.accept(ClassCleaner()) }
    Marker(config).execute(appView)
    val optimizer = Optimizer(config)
    // Run two passes, two ensure that e.g. unread fields are removed,
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
