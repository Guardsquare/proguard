package proguard.optimize.info

import io.kotest.core.annotation.Tags
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.visitor.AllMemberVisitor
import proguard.optimize.info.ProgramClassOptimizationInfo.getProgramClassOptimizationInfo
import proguard.optimize.info.ProgramClassOptimizationInfo.setProgramClassOptimizationInfo
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource

class InstantiationClassMarkerTest : FreeSpec({
    "A class should be marked as instantiated" - {
        "when it is instantiated with a `new` instruction" {
            val (classPool, _) = ClassPoolBuilder.fromSource(
                JavaSource(
                    "Main.java",
                    """
                class A { }
                public class Main {
                    public static void main(String[] args) {
                        System.out.println(new A());
                    }
                }
                    """.trimIndent()
                )
            )

            val classA = classPool.getClass("A")

            classPool.classesAccept { clazz -> setProgramClassOptimizationInfo(clazz) }

            classPool.classAccept(
                "Main",
                AllMemberVisitor(
                    AllAttributeVisitor(
                        AllInstructionVisitor(
                            InstantiationClassMarker()
                        )
                    )
                )
            )

            getProgramClassOptimizationInfo(classA).isInstantiated shouldBe true
        }

        "when one of its subclasses is instantiated with a `new` instruction" {
            val (classPool, _) = ClassPoolBuilder.fromSource(
                JavaSource(
                    "Main.java",
                    """
                class A { }
                class B extends A { }
                public class Main {
                    public static void main(String[] args) {
                        System.out.println(new B());
                    }
                }
                    """.trimIndent()
                )
            )

            val classA = classPool.getClass("A")
            val classB = classPool.getClass("B")

            classPool.classesAccept { clazz -> setProgramClassOptimizationInfo(clazz) }

            classPool.classAccept(
                "Main",
                AllMemberVisitor(
                    AllAttributeVisitor(
                        AllInstructionVisitor(
                            InstantiationClassMarker()
                        )
                    )
                )
            )

            getProgramClassOptimizationInfo(classA).isInstantiated shouldBe true
            getProgramClassOptimizationInfo(classB).isInstantiated shouldBe true
        }
    }
})
