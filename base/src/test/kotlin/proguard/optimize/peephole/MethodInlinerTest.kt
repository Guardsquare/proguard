package proguard.optimize.peephole

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import proguard.classfile.Clazz
import proguard.classfile.Method
import proguard.classfile.ProgramClass
import proguard.classfile.ProgramMethod
import proguard.classfile.attribute.Attribute.LINE_NUMBER_TABLE
import proguard.classfile.attribute.CodeAttribute
import proguard.classfile.attribute.LineNumberTableAttribute
import proguard.classfile.attribute.ProGuardOrigin
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.visitor.AllMethodVisitor
import proguard.classfile.visitor.ClassVisitor
import proguard.classfile.visitor.MultiClassVisitor
import proguard.classfile.visitor.MultiMemberVisitor
import proguard.optimize.info.BackwardBranchMarker
import proguard.optimize.info.ProgramClassOptimizationInfoSetter
import proguard.optimize.info.ProgramMemberOptimizationInfoSetter
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.CodeAttributeFinder
import proguard.testutils.JavaSource
import proguard.testutils.classfile.extensions.get
import proguard.testutils.classfile.extensions.shouldMatch

class MethodInlinerTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerTest

    Given("two simple functions, one calling the other") {
        val (programClassPool, _) =
            ClassPoolBuilder.fromSource(
                JavaSource(
                    "Foo.java",
                    """class Foo { 
                static int f1() {
                    return f2() + 1;
                }
                
                static int f2() {
                    return 1;
                }
            }""",
                ),
            )

        // Sanity check how the instructions look before.
        val clazz = programClassPool.getClass("Foo")
        val f1 = clazz.findMethod("f1","()I") as ProgramMethod
        val f2 = clazz.findMethod("f2","()I") as ProgramMethod
        clazz[f1].shouldMatch {
            invokestatic(clazz,f2)
            iconst_1()
            iadd()
            ireturn()
        }
        When("calling the method inliner, specifying that we should always inline") {
            // Initialize optimization info (used when inlining).
            val optimizationInfoInitializer: ClassVisitor =
                MultiClassVisitor(
                    ProgramClassOptimizationInfoSetter(),
                    AllMethodVisitor(
                        ProgramMemberOptimizationInfoSetter(),
                    ),
                )

            programClassPool.classesAccept(optimizationInfoInitializer)

            // Create a mock method inliner which always returns true.
            val methodInliner =
                object : MethodInliner(false, true, true) {
                    override fun shouldInline(
                        clazz: Clazz,
                        method: Method?,
                        codeAttribute: CodeAttribute?,
                    ): Boolean = true
                }

            programClassPool.classesAccept(
                AllMethodVisitor(
                    AllAttributeVisitor(
                        methodInliner,
                    ),
                ),
            )

            Then("the called function is inlined as expected") {
                clazz[f1].shouldMatch {
                    iconst_1()
                    iconst_1()
                    iadd()
                    ireturn()
                }
            }
            Then("The line number table should look correct") {
                val codeAttribute = CodeAttributeFinder.findCodeAttribute(f1)!!
                val lineNumberTableAttribute = codeAttribute.getAttribute(clazz, LINE_NUMBER_TABLE) as LineNumberTableAttribute
                val table = lineNumberTableAttribute.lineNumberTable
                table.size shouldBe 4
                table[0].u2lineNumber shouldBe MethodInliner.INLINED_METHOD_START_LINE_NUMBER
                table[0].origin.shouldContainOnly(ProGuardOrigin.INLINED)
                table[0].source shouldBe "Foo.f2()I:7:7"
                table[1].u2lineNumber shouldBe 3
                table[1].origin.shouldBeEmpty()
                table[1].source.shouldBeNull()
                table[2].u2lineNumber shouldBe 7
                table[2].origin.shouldContainOnly(ProGuardOrigin.INLINED)
                table[2].source shouldBe "Foo.f2()I:7:7"
                table[3].u2lineNumber shouldBe MethodInliner.INLINED_METHOD_END_LINE_NUMBER
                table[3].origin.shouldContainOnly(ProGuardOrigin.INLINED)
                table[3].source shouldBe "Foo.f2()I:7:7"
            }
        }

        When("calling the method inliner, specifying that we should never inline") {
            // Initialize optimization info (used when inlining).
            val optimizationInfoInitializer: ClassVisitor =
                MultiClassVisitor(
                    ProgramClassOptimizationInfoSetter(),
                    AllMethodVisitor(
                        ProgramMemberOptimizationInfoSetter(),
                    ),
                )

            programClassPool.classesAccept(optimizationInfoInitializer)

            // Create a mock method inliner which always returns true.
            val methodInliner =
                object : MethodInliner(false, true, true) {
                    override fun shouldInline(
                        clazz: Clazz?,
                        method: Method?,
                        codeAttribute: CodeAttribute?,
                    ): Boolean = false
                }

            programClassPool.classesAccept(
                AllMethodVisitor(
                    AllAttributeVisitor(
                        methodInliner,
                    ),
                ),
            )

            Then("the called function is not inlined") {
                val clazz = programClassPool.getClass("Foo")
                val f1 = clazz.findMethod("f1","()I")
                val f2 = clazz.findMethod("f2","()I")
                clazz[f1].shouldMatch {
                    invokestatic(clazz,f2)
                    iconst_1()
                    iadd()
                    ireturn()
                }
            }
        }
    }

    Given("a function calling a big function") {
        val lotsOfPrints = (1..3000).joinToString("\n") { "System.out.println(\"${it}\");" }

        val (programClassPool, _) =
            ClassPoolBuilder.fromSource(
                JavaSource(
                    "Foo.java",
                    """class Foo { 
                static void f1() {
                    f2();
                }
                
                static void f2() {
                """ +
                        lotsOfPrints +
                        """
                }
            }""",
                ),
            )

        val clazz = programClassPool.getClass("Foo") as ProgramClass
        val method = clazz.findMethod("f1", "()V") as ProgramMethod
        val codeAttr = method.attributes.filterIsInstance<CodeAttribute>()[0]

        val lengthBefore = codeAttr.u4codeLength

        When("using the default maximum resulting code length parameter") {
            // Initialize optimization info (used when inlining).
            val optimizationInfoInitializer: ClassVisitor =
                MultiClassVisitor(
                    ProgramClassOptimizationInfoSetter(),
                    AllMethodVisitor(
                        ProgramMemberOptimizationInfoSetter(),
                    ),
                )

            programClassPool.classesAccept(optimizationInfoInitializer)

            // Create a mock method inliner which always returns true.
            val methodInliner =
                object : MethodInliner(false, true, true) {
                    override fun shouldInline(
                        clazz: Clazz?,
                        method: Method?,
                        codeAttribute: CodeAttribute?,
                    ): Boolean = true
                }

            Then("the large method is not inlined") {
                programClassPool.classesAccept(
                    AllMethodVisitor(
                        AllAttributeVisitor(
                            methodInliner,
                        ),
                    ),
                )

                val lengthAfter = codeAttr.u4codeLength

                lengthAfter shouldBeExactly lengthBefore
            }
        }

        When("using the maximum resulting code length parameter") {
            // Initialize optimization info (used when inlining).
            val optimizationInfoInitializer: ClassVisitor =
                MultiClassVisitor(
                    ProgramClassOptimizationInfoSetter(),
                    AllMethodVisitor(
                        ProgramMemberOptimizationInfoSetter(),
                    ),
                )

            programClassPool.classesAccept(optimizationInfoInitializer)

            // Create a mock method inliner with the maximum limit
            val methodInliner =
                object : MethodInliner(false, true, MAXIMUM_RESULTING_CODE_LENGTH_JVM, true, true, null) {
                    override fun shouldInline(
                        clazz: Clazz?,
                        method: Method?,
                        codeAttribute: CodeAttribute?,
                    ): Boolean = true
                }

            programClassPool.classesAccept(
                AllMethodVisitor(
                    AllAttributeVisitor(
                        methodInliner,
                    ),
                ),
            )

            Then("the large method is inlined") {
                val lengthAfter = codeAttr.u4codeLength

                lengthAfter shouldBeGreaterThan lengthBefore
            }
        }
    }

    Given("a method initializing a library class and calling a method with backwards branching") {
        val (programClassPool, _) =
            ClassPoolBuilder.fromSource(
                JavaSource(
                    "Foo.java",
                    """class Foo { 
                static void f1() {
                    StringBuilder sb = new StringBuilder();
                    sb.append(System.currentTimeMillis());
                    System.out.println(sb.toString());
                    f2();
                }
                
                static void f2() {
                    for (int i = 0; i < 1000; i++)
                    {
                        System.out.println(i);
                    }
                }
            }""",
                ),
            )

        val clazz = programClassPool.getClass("Foo") as ProgramClass
        val method = clazz.findMethod("f1", "()V") as ProgramMethod
        val codeAttr = method.attributes.filterIsInstance<CodeAttribute>()[0]

        val lengthBefore = codeAttr.u4codeLength

        When("inlining the method call") {
            // Initialize optimization info (used when inlining).
            // Make sure the backwards branching info is set correctly.
            val optimizationInfoInitializer: ClassVisitor =
                MultiClassVisitor(
                    ProgramClassOptimizationInfoSetter(),
                    AllMethodVisitor(
                        MultiMemberVisitor(
                            ProgramMemberOptimizationInfoSetter(),
                            AllAttributeVisitor(
                                AllInstructionVisitor(
                                    BackwardBranchMarker(),
                                ),
                            ),
                        ),
                    ),
                )

            programClassPool.classesAccept(optimizationInfoInitializer)

            // Create a mock method inliner which always returns true.
            val methodInliner =
                object : MethodInliner(false, true, true) {
                    override fun shouldInline(
                        clazz: Clazz?,
                        method: Method?,
                        codeAttribute: CodeAttribute?,
                    ): Boolean = true
                }

            Then("the method is inlined") {
                programClassPool.classesAccept(
                    AllMethodVisitor(
                        AllAttributeVisitor(methodInliner),
                    ),
                )

                val lengthAfter = codeAttr.u4codeLength

                lengthAfter shouldBeGreaterThan lengthBefore
            }
        }
    }

    Given("a method calling another non-private method in an interface") {
        val (programClassPool, _) =
            ClassPoolBuilder.fromSource(
                JavaSource(
                    "Foo.java",
                    """interface Foo {
                default void f1() {
                    f2();
                }

                static void f2() {
                    StringBuilder sb = new StringBuilder();
                    sb.append(System.currentTimeMillis());
                    System.out.println(sb.toString());
               }
            }""",
                ),
            )

        val clazz = programClassPool.getClass("Foo") as ProgramClass
        val method = clazz.findMethod("f1", "()V") as ProgramMethod
        val codeAttr = method.attributes.filterIsInstance<CodeAttribute>()[0]

        val lengthBefore = codeAttr.u4codeLength

        // Initialize optimization info (used when inlining).
        val optimizationInfoInitializer: ClassVisitor =
            MultiClassVisitor(
                ProgramClassOptimizationInfoSetter(),
                AllMethodVisitor(
                    ProgramMemberOptimizationInfoSetter(),
                ),
            )

        programClassPool.classesAccept(optimizationInfoInitializer)

        // Create a mock method inliner which always returns true.
        val methodInliner =
            object : MethodInliner(false, true, true) {
                override fun shouldInline(
                    clazz: Clazz?,
                    method: Method?,
                    codeAttribute: CodeAttribute?,
                ): Boolean = true
            }

        Then("the interface method is not inlined") {
            programClassPool.classesAccept(
                AllMethodVisitor(
                    AllAttributeVisitor(
                        methodInliner,
                    ),
                ),
            )

            val lengthAfter = codeAttr.u4codeLength

            lengthAfter shouldBeExactly lengthBefore
        }
    }
})
