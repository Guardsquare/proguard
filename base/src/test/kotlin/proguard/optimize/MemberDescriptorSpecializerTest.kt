package proguard.optimize

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import proguard.classfile.AccessConstants
import proguard.classfile.Clazz
import proguard.classfile.Member
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.attribute.visitor.DebugAttributeVisitor
import proguard.classfile.visitor.AllMemberVisitor
import proguard.classfile.visitor.AllMethodVisitor
import proguard.classfile.visitor.ClassAccessFilter
import proguard.classfile.visitor.MemberNameFilter
import proguard.classfile.visitor.MemberVisitor
import proguard.classfile.visitor.ParallelAllClassVisitor.ClassVisitorFactory
import proguard.evaluation.InvocationUnit
import proguard.evaluation.PartialEvaluator
import proguard.evaluation.value.ParticularValueFactory
import proguard.evaluation.value.ValueFactory
import proguard.optimize.evaluation.StoringInvocationUnit
import proguard.optimize.info.ProgramClassOptimizationInfoSetter
import proguard.optimize.info.ProgramMemberOptimizationInfoSetter
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource
import proguard.util.ProcessingFlagSetter
import proguard.util.ProcessingFlags.IS_CLASS_AVAILABLE

class MemberDescriptorSpecializerTest : FreeSpec({
    "Given a method with a more general parameter type than its use" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            JavaSource(
                "Test.java",
                """
                public class Test {
                    public static void main(String[] args) {
                        foo(new Foo());
                    }
                    public static void foo(Bar foo) {
                        System.out.println(foo);
                    }
                }
                
                class Bar { }
                
                class Foo extends Bar { }
                """.trimIndent()
            )
        )

        "When specializing the member descriptors" - {

            // Mark all members as available.
            programClassPool.classesAccept(AllMemberVisitor(ProcessingFlagSetter(IS_CLASS_AVAILABLE)))

            // Setup the OptimizationInfo on the classes
            val keepMarker = KeepMarker()
            libraryClassPool.classesAccept(keepMarker)
            libraryClassPool.classesAccept(AllMemberVisitor(keepMarker))

            programClassPool.classesAccept(ProgramClassOptimizationInfoSetter())
            programClassPool.classesAccept(AllMemberVisitor(ProgramMemberOptimizationInfoSetter()))

            // Create the optimization as in Optimizer
            val fillingOutValuesClassVisitor = ClassVisitorFactory {
                val valueFactory: ValueFactory = ParticularValueFactory()
                val storingInvocationUnit: InvocationUnit = StoringInvocationUnit(
                    valueFactory,
                    true,
                    true,
                    true
                )
                ClassAccessFilter(
                    0, AccessConstants.SYNTHETIC,
                    AllMethodVisitor(
                        AllAttributeVisitor(
                            DebugAttributeVisitor(
                                "Filling out fields, method parameters, and return values",
                                PartialEvaluator(
                                    valueFactory, storingInvocationUnit,
                                    true
                                )
                            )
                        )
                    )
                )
            }

            programClassPool.classesAccept(fillingOutValuesClassVisitor.createClassVisitor())

            // Specialize class member descriptors, based on partial evaluation.
            programClassPool.classesAccept(
                AllMemberVisitor(
                    OptimizationInfoMemberFilter(
                        MemberDescriptorSpecializer(
                            true,
                            true,
                            true,
                            null,
                            null,
                            null
                        )
                    )
                )
            )

            "Then the member descriptor should be correctly specialised" {
                lateinit var memberDescriptor: String
                programClassPool.classAccept(
                    "Test",
                    AllMemberVisitor(
                        MemberNameFilter(
                            "foo*",
                            object : MemberVisitor {
                                override fun visitAnyMember(clazz: Clazz, member: Member) {
                                    memberDescriptor = member.getDescriptor(clazz)
                                }
                            }
                        )
                    )
                )
                memberDescriptor shouldBe "(LFoo;)V"
            }
        }
    }
})
