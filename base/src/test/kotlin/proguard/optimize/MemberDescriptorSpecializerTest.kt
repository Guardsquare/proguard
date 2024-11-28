package proguard.optimize

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import proguard.classfile.AccessConstants
import proguard.classfile.ClassPool
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

    fun specializeMemberDescriptors(
        programClassPool: ClassPool,
        libraryClassPool: ClassPool,
    ) {
        // Mark all program classes as available.
        programClassPool.classesAccept(ProcessingFlagSetter(IS_CLASS_AVAILABLE))

        // Setup the OptimizationInfo on the classes
        val keepMarker = KeepMarker()
        libraryClassPool.classesAccept(keepMarker)
        libraryClassPool.classesAccept(AllMemberVisitor(keepMarker))

        programClassPool.classesAccept(ProgramClassOptimizationInfoSetter())
        programClassPool.classesAccept(AllMemberVisitor(ProgramMemberOptimizationInfoSetter()))

        // Create the optimization as in Optimizer
        val fillingOutValuesClassVisitor =
            ClassVisitorFactory {
                val valueFactory: ValueFactory = ParticularValueFactory()
                val storingInvocationUnit: InvocationUnit =
                    StoringInvocationUnit(
                        valueFactory,
                        true,
                        true,
                        true,
                    )
                ClassAccessFilter(
                    0,
                    AccessConstants.SYNTHETIC,
                    AllMethodVisitor(
                        AllAttributeVisitor(
                            DebugAttributeVisitor(
                                "Filling out fields, method parameters, and return values",
                                PartialEvaluator(
                                    valueFactory,
                                    storingInvocationUnit,
                                    true,
                                ),
                            ),
                        ),
                    ),
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
                        null,
                    ),
                ),
            ),
        )
    }

    "Given a method with a more general program class pool parameter type than its use" - {
        val (programClassPool, libraryClassPool) =
            ClassPoolBuilder.fromSource(
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
                    """.trimIndent(),
                ),
            )

        "When specializing the member descriptors" - {
            specializeMemberDescriptors(programClassPool, libraryClassPool)

            "Then the member descriptor should be correctly specialised" {
                lateinit var memberDescriptor: String
                programClassPool.classAccept(
                    "Test",
                    AllMemberVisitor(
                        MemberNameFilter(
                            "foo*",
                            object : MemberVisitor {
                                override fun visitAnyMember(
                                    clazz: Clazz,
                                    member: Member,
                                ) {
                                    memberDescriptor = member.getDescriptor(clazz)
                                }
                            },
                        ),
                    ),
                )
                memberDescriptor shouldBe "(LFoo;)V"
            }
        }
    }

    "Given a field with a more general program class pool parameter type than its use" - {
        val (programClassPool, libraryClassPool) =
            ClassPoolBuilder.fromSource(
                JavaSource(
                    "Test.java",
                    """
                    public class Test {
                        static Bar myField = null;
                    
                        public static void main(String[] args) {
                            myField = new Foo();
                        }
                    }
                    
                    class Bar { }
                    
                    class Foo extends Bar { }
                    """.trimIndent(),
                ),
            )

        "When specializing the member descriptors" - {
            specializeMemberDescriptors(programClassPool, libraryClassPool)

            "Then the member descriptor should be correctly specialised" {
                lateinit var memberDescriptor: String
                programClassPool.classAccept(
                    "Test",
                    AllMemberVisitor(
                        MemberNameFilter(
                            "myField*",
                            object : MemberVisitor {
                                override fun visitAnyMember(
                                    clazz: Clazz,
                                    member: Member,
                                ) {
                                    memberDescriptor = member.getDescriptor(clazz)
                                }
                            },
                        ),
                    ),
                )
                memberDescriptor shouldBe "LFoo;"
            }
        }
    }

    "Given a field with a more general library class pool parameter type than its use" - {
        val (programClassPool, libraryClassPool) =
            ClassPoolBuilder.fromSource(
                JavaSource(
                    "Test.java",
                    """
                    public class Test {
                        static java.lang.Object myField = null;
                    
                        public static void main(String[] args) {
                            myField = new java.lang.StringBuffer();
                        }
                    }
                    """.trimIndent(),
                ),
            )

        "When specializing the member descriptors" - {
            specializeMemberDescriptors(programClassPool, libraryClassPool)

            "Then the member descriptor should be correctly specialised" {
                lateinit var memberDescriptor: String
                programClassPool.classAccept(
                    "Test",
                    AllMemberVisitor(
                        MemberNameFilter(
                            "myField*",
                            object : MemberVisitor {
                                override fun visitAnyMember(
                                    clazz: Clazz,
                                    member: Member,
                                ) {
                                    memberDescriptor = member.getDescriptor(clazz)
                                }
                            },
                        ),
                    ),
                )
                // Library classes are not marked as available by default. Therefore, they are not specialized.
                memberDescriptor shouldBe "Ljava/lang/Object;"
            }
        }
    }
})
