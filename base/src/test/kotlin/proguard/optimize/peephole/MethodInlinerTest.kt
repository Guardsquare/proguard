package proguard.optimize.peephole

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldMatch
import proguard.classfile.ClassPool
import proguard.classfile.Clazz
import proguard.classfile.Method
import proguard.classfile.ProgramClass
import proguard.classfile.ProgramMethod
import proguard.classfile.attribute.CodeAttribute
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.visitor.AllMethodVisitor
import proguard.classfile.visitor.ClassPrinter
import proguard.classfile.visitor.ClassVisitor
import proguard.classfile.visitor.MemberVisitor
import proguard.classfile.visitor.MultiClassVisitor
import proguard.classfile.visitor.MultiMemberVisitor
import proguard.optimize.info.BackwardBranchMarker
import proguard.optimize.info.ProgramClassOptimizationInfoSetter
import proguard.optimize.info.ProgramMemberOptimizationInfoSetter
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource
import java.io.ByteArrayOutputStream
import java.io.PrintWriter

class MethodInlinerTest : FreeSpec({
    isolationMode = IsolationMode.InstancePerTest

    "Given two simple functions, one calling the other" - {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            JavaSource(
                "Foo.java",
                """class Foo { 
                static int f1() {
                    return f2() + 1;
                }
                
                static int f2() {
                    return 1;
                }
            }"""
            )
        )

        // Sanity check how the instructions look before.
        val instructionsBefore = printProgramMethodInstructions(programClassPool, "Foo", "f1", "()I")

        instructionsBefore.size shouldBe 4
        instructionsBefore[0] shouldMatch """(.*)invokestatic(.*)Foo.f2\(\)I(.*)""".toRegex()
        instructionsBefore[1] shouldContain "iconst_1"
        instructionsBefore[2] shouldContain "iadd"
        instructionsBefore[3] shouldContain "ireturn"

        "When calling the method inliner, specifying that we should always inline" - {
            // Initialize optimization info (used when inlining).
            val optimizationInfoInitializer: ClassVisitor = MultiClassVisitor(
                ProgramClassOptimizationInfoSetter(),
                AllMethodVisitor(
                    ProgramMemberOptimizationInfoSetter()
                )
            )

            programClassPool.classesAccept(optimizationInfoInitializer)

            // Create a mock method inliner which always returns true.
            val methodInliner = object : MethodInliner(false, true, true) {
                override fun shouldInline(clazz: Clazz, method: Method?, codeAttribute: CodeAttribute?): Boolean = true
            }

            programClassPool.classesAccept(
                AllMethodVisitor(
                    AllAttributeVisitor(
                        methodInliner
                    )
                )
            )

            "Then the called function is inlined as expected" {
                val instructionsAfter = printProgramMethodInstructions(programClassPool, "Foo", "f1", "()I")

                instructionsAfter.size shouldBe 4
                instructionsAfter[0] shouldContain "iconst_1"
                instructionsAfter[1] shouldContain "iconst_1"
                instructionsAfter[2] shouldContain "iadd"
                instructionsAfter[3] shouldContain "ireturn"
            }
        }

        "When calling the method inliner, specifying that we should never inline" - {
            // Initialize optimization info (used when inlining).
            val optimizationInfoInitializer: ClassVisitor = MultiClassVisitor(
                ProgramClassOptimizationInfoSetter(),
                AllMethodVisitor(
                    ProgramMemberOptimizationInfoSetter()
                )
            )

            programClassPool.classesAccept(optimizationInfoInitializer)

            // Create a mock method inliner which always returns true.
            val methodInliner = object : MethodInliner(false, true, true) {
                override fun shouldInline(clazz: Clazz?, method: Method?, codeAttribute: CodeAttribute?): Boolean = false
            }

            programClassPool.classesAccept(
                AllMethodVisitor(
                    AllAttributeVisitor(
                        methodInliner
                    )
                )
            )

            "Then the called function is not inlined" {
                val instructionsAfter = printProgramMethodInstructions(programClassPool, "Foo", "f1", "()I")

                instructionsAfter.size shouldBe 4
                instructionsAfter[0] shouldMatch """(.*)invokestatic(.*)Foo.f2\(\)I(.*)""".toRegex()
                instructionsAfter[1] shouldContain "iconst_1"
                instructionsAfter[2] shouldContain "iadd"
                instructionsAfter[3] shouldContain "ireturn"
            }
        }
    }

    "Given a function calling a big function" - {
        val lotsOfPrints = (1..3000).joinToString("\n") { "System.out.println(\"${it}\");" }

        val (programClassPool, _) = ClassPoolBuilder.fromSource(
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
            }"""
            )
        )

        val clazz = programClassPool.getClass("Foo") as ProgramClass
        val method = clazz.findMethod("f1", "()V") as ProgramMethod
        val codeAttr = method.attributes.filterIsInstance<CodeAttribute>()[0]

        val lengthBefore = codeAttr.u4codeLength

        "When using the default maximum resulting code length parameter" - {
            // Initialize optimization info (used when inlining).
            val optimizationInfoInitializer: ClassVisitor = MultiClassVisitor(
                ProgramClassOptimizationInfoSetter(),
                AllMethodVisitor(
                    ProgramMemberOptimizationInfoSetter()
                )
            )

            programClassPool.classesAccept(optimizationInfoInitializer)

            // Create a mock method inliner which always returns true.
            val methodInliner = object : MethodInliner(false, true, true) {
                override fun shouldInline(clazz: Clazz?, method: Method?, codeAttribute: CodeAttribute?): Boolean = true
            }

            "Then the large method is not inlined" {
                programClassPool.classesAccept(
                    AllMethodVisitor(
                        AllAttributeVisitor(
                            methodInliner
                        )
                    )
                )

                val lengthAfter = codeAttr.u4codeLength

                lengthAfter shouldBeExactly lengthBefore
            }
        }

        "When using the maximum resulting code length parameter" - {
            // Initialize optimization info (used when inlining).
            val optimizationInfoInitializer: ClassVisitor = MultiClassVisitor(
                ProgramClassOptimizationInfoSetter(),
                AllMethodVisitor(
                    ProgramMemberOptimizationInfoSetter()
                )
            )

            programClassPool.classesAccept(optimizationInfoInitializer)

            // Create a mock method inliner with the maximum limit
            val methodInliner = object : MethodInliner(false, true, MAXIMUM_RESULTING_CODE_LENGTH_JVM, true, true, null) {
                override fun shouldInline(clazz: Clazz?, method: Method?, codeAttribute: CodeAttribute?): Boolean = true
            }

            programClassPool.classesAccept(
                AllMethodVisitor(
                    AllAttributeVisitor(
                        methodInliner
                    )
                )
            )

            "Then the large method is inlined" {
                val lengthAfter = codeAttr.u4codeLength

                lengthAfter shouldBeGreaterThan lengthBefore
            }
        }
    }

    "Given a method initializing a library class and calling a method with backwards branching " - {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
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
            }"""
            )
        )

        val clazz = programClassPool.getClass("Foo") as ProgramClass
        val method = clazz.findMethod("f1", "()V") as ProgramMethod
        val codeAttr = method.attributes.filterIsInstance<CodeAttribute>()[0]

        val lengthBefore = codeAttr.u4codeLength

        "When inlining the method call" - {
            // Initialize optimization info (used when inlining).
            // Make sure the backwards branching info is set correctly.
            val optimizationInfoInitializer: ClassVisitor = MultiClassVisitor(
                ProgramClassOptimizationInfoSetter(),
                AllMethodVisitor(
                    MultiMemberVisitor(
                        ProgramMemberOptimizationInfoSetter(),
                        AllAttributeVisitor(
                            AllInstructionVisitor(
                                BackwardBranchMarker()
                            )
                        )
                    )
                )
            )

            programClassPool.classesAccept(optimizationInfoInitializer)

            // Create a mock method inliner which always returns true.
            val methodInliner = object : MethodInliner(false, true, true) {
                override fun shouldInline(clazz: Clazz?, method: Method?, codeAttribute: CodeAttribute?): Boolean = true
            }

            "Then the method is inlined" {
                programClassPool.classesAccept(
                    AllMethodVisitor(
                        AllAttributeVisitor(methodInliner)
                    )
                )

                val lengthAfter = codeAttr.u4codeLength

                lengthAfter shouldBeGreaterThan lengthBefore
            }
        }
    }
})

private fun printProgramMethodInstructions(
    classPool: ClassPool,
    className: String,
    methodName: String,
    methodDescriptor: String
): List<String> {
    val output = ByteArrayOutputStream()
    val pw = PrintWriter(output)

    classPool.classAccept(className) { clazz ->
        clazz.methodAccept(
            methodName,
            methodDescriptor,
            object : MemberVisitor {
                override fun visitProgramMethod(programClass: ProgramClass?, programMethod: ProgramMethod?) {
                    programMethod?.accept(programClass, AllAttributeVisitor(AllInstructionVisitor(ClassPrinter(pw))))
                }
            }
        )
    }

    pw.flush()
    val result = output.toString().trimEnd()
    return result.lines()
}
