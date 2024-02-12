package proguard.shrink

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import proguard.AppView
import proguard.Configuration
import proguard.classfile.Clazz
import proguard.classfile.Member
import proguard.classfile.attribute.annotation.visitor.AllElementValueVisitor
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.kotlin.visitor.ReferencedKotlinMetadataVisitor
import proguard.classfile.util.EnumFieldReferenceInitializer
import proguard.classfile.visitor.AllMethodVisitor
import proguard.classfile.visitor.MemberVisitor
import proguard.classfile.visitor.MultiClassVisitor
import proguard.classfile.visitor.NamedClassVisitor
import proguard.classfile.visitor.NamedMethodVisitor
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource
import proguard.testutils.KotlinSource
import proguard.util.Processable
import proguard.util.ProcessingFlagSetter
import proguard.util.ProcessingFlags.DONT_SHRINK
import proguard.util.kotlin.asserter.KotlinMetadataVerifier

private fun beMarkedWith(simpleUsageMarker: SimpleUsageMarker) = object : Matcher<Processable> {
    override fun test(value: Processable) =
        MatcherResult(
            simpleUsageMarker.isUsed(value),
            { "$value should be marked" },
            { "$value should not be used" }
        )
}

class ClassUsageMarkerTest : StringSpec({
    "Class Usage Marking should mark methods invoked in the method body" {
        val (classPool, _) = ClassPoolBuilder.fromSource(
            JavaSource(
                "A.java",
                """
            public class A {
                public void method1() {
                    this.method2();
                }
                public void method2() {
                    this.method2();
                }
                public void method3() {
                    B.method4();
                }
            }
                """.trimIndent()
            ),
            JavaSource(
                "B.java",
                """            
            public class B {
                public static void method4() {
                    new A().method2();
                }
            }
                """.trimIndent()
            )
        )

        val classA = classPool.getClass("A")
        val method1 = classA.findMethod("method1", null)
        val method2 = classA.findMethod("method2", null)
        val method3 = classA.findMethod("method3", null)

        val classB = classPool.getClass("B")
        val method4 = classB.findMethod("method4", null)

        val usageMarker = SimpleUsageMarker()

        val classUsageMarker = ClassUsageMarker(usageMarker)

        // Visiting the class does not imply visiting the methods
        classA.accept(classUsageMarker)
        method1 shouldNot beMarkedWith(usageMarker)
        method2 shouldNot beMarkedWith(usageMarker)
        method3 shouldNot beMarkedWith(usageMarker)
        method4 shouldNot beMarkedWith(usageMarker)

        // Visiting one method also marks the called method, but not the others
        method1.accept(classA, classUsageMarker)
        method1 should beMarkedWith(usageMarker)
        method2 should beMarkedWith(usageMarker)
        method3 shouldNot beMarkedWith(usageMarker)
        method4 shouldNot beMarkedWith(usageMarker)

        // Visiting the other methods, marks the last one as well
        method3.accept(classA, classUsageMarker)
        method1 should beMarkedWith(usageMarker)
        method2 should beMarkedWith(usageMarker)
        method3 should beMarkedWith(usageMarker)
        method4 should beMarkedWith(usageMarker)
    }

    "The comparable interface should induce additional marking" {
        val (classPool, _) = ClassPoolBuilder.fromSource(
            JavaSource(
                "Application.java",
                """
            public class Application {
                Other attribute;
                public Application() {
                    attribute = new Other();
                }
            }
                """.trimIndent()
            ),
            JavaSource(
                "Other.java",
                """
            public class Other implements Comparable<Other> {
                public void foo() {}
                public void bar() {}
                public int compareTo(Other o) { foo(); return 0; }
            }
                """.trimIndent()
            )
        )

        val classUsageMarker = ClassUsageMarker()
        val applicationClazz = classPool.getClass("Application")
        val applicationInit = applicationClazz.findMethod("<init>", null)

        applicationClazz.accept(classUsageMarker)
        applicationInit.accept(applicationClazz, classUsageMarker)

        val otherClazz = classPool.getClass("Other")
        val otherInit = otherClazz.findMethod("<init>", null)
        val otherFoo = otherClazz.findMethod("foo", null)
        val otherBar = otherClazz.findMethod("bar", null)

        otherInit should beMarkedWith(classUsageMarker.usageMarker)
        otherFoo should beMarkedWith(classUsageMarker.usageMarker)
        otherBar shouldNot beMarkedWith(classUsageMarker.usageMarker)
    }

    "Using an enum as default value in an annotation field should mark the enum value as used" {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            JavaSource(
                "A.java",
                """
                public @interface A {
                    B b() default B.Y;
                }
                """.trimIndent()
            ),
            JavaSource(
                "B.java",
                """
                public enum B {
                    X, Y, Z
                }
                """.trimIndent()
            )
        )

        // Make sure the field references in enum fields are updated.
        programClassPool.classesAccept(
            AllAttributeVisitor(
                true,
                AllElementValueVisitor(
                    true,
                    EnumFieldReferenceInitializer()
                )
            )
        )

        // Visit attributes to mark usage processing info.
        programClassPool.getClass("A").accept(
            AllAttributeVisitor(
                true,
                ClassUsageMarker()
            )
        )

        val bClass = programClassPool.getClass("B")
        val xField = bClass.findField("X", null)
        val yField = bClass.findField("Y", null)
        val zField = bClass.findField("Z", null)

        xField.processingInfo shouldBe null
        yField.processingInfo shouldNotBe null
        zField.processingInfo shouldBe null
    }

    "Given a Kotlin interface with default method implementation in compatibility mode" {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
        interface Test {
            fun foo() {
                TODO()
            }
        }
                """.trimIndent()
            ),
            kotlincArguments = listOf("-Xjvm-default=all")
        )

        // Run the asserter to ensure any metadata that isn't initialized correctly is thrown away
        KotlinMetadataVerifier(Configuration()).execute(AppView(programClassPool, libraryClassPool))

        programClassPool.classAccept("Test", AllMethodVisitor(ProcessingFlagSetter(DONT_SHRINK)))
        val classUsageMarker = ClassUsageMarker(SimpleUsageMarker())
        shouldNotThrowAny {
            programClassPool.classAccept("Test", MultiClassVisitor(classUsageMarker, AllMethodVisitor(classUsageMarker)))
        }
    }
    "Given a Kotlin interface with default method implementation" {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "Interface.kt",
                """
        package test;
        interface Interface {
            fun foo() : Int {
                return 42;
            }
        }
                """.trimIndent(),
            ),
        )

        // Necessary to force marking methods that are not actually used and have not been processed by the Marker.
        class CustomMarker(var marker: SimpleUsageMarker) : MemberVisitor {
            override fun visitAnyMember(clazz: Clazz, member: Member) {
                marker.markAsUsed(member)
            }
        }

        val usageMarker = SimpleUsageMarker()
        val classUsageMarker = ClassUsageMarker(usageMarker)

        // Mark the classes as used.
        programClassPool.classesAccept(classUsageMarker)

        // Mark the default implementation as used.
        programClassPool.accept(NamedClassVisitor(NamedMethodVisitor("foo", null, CustomMarker(usageMarker)), "test/Interface\$DefaultImpls"))

        // Process Kotlin metadata: this should cause the interface method to be kept as well.
        programClassPool.classesAccept(ReferencedKotlinMetadataVisitor(classUsageMarker))

        val fooInterface = programClassPool.getClass("test/Interface").findMethod("foo", null)
        fooInterface should beMarkedWith(usageMarker)
    }
})
