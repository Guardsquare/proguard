package proguard.optimize.kotlin

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import proguard.Configuration
import proguard.classfile.AccessConstants
import proguard.classfile.ProgramClass
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.editor.InstructionSequenceBuilder
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.util.InstructionSequenceMatcher
import proguard.classfile.visitor.AllMemberVisitor
import proguard.classfile.visitor.MemberAccessFilter
import proguard.classfile.visitor.MemberNameFilter
import proguard.io.ExtraDataEntryNameMap
import proguard.optimize.info.ProgramClassOptimizationInfo
import proguard.optimize.info.ProgramClassOptimizationInfoSetter
import testutils.ClassPoolBuilder
import testutils.InstructionSequenceCollector
import testutils.KotlinSource
import testutils.MatchDetector

class KotlinLambdaGroupBuilderTest : FreeSpec({
    val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
        KotlinSource(
            "Test.kt",
            """
            fun main() {
                val lambda1 = { println("Lambda1") }
                val lambda2 = { println("Lambda2") }
                val lambda3 = { println("Lambda3") }
                lambda1()
                lambda2()
                lambda3()
            }
            """.trimIndent()
        )
    )

    "Given a lambda group builder and a lambda class" - {
        val lambdaGroupName = "LambdaGroup"
        val configuration = Configuration()
        val entryMapper = ExtraDataEntryNameMap()
        val notMergedLambdaVisitor = null
        val builder = KotlinLambdaGroupBuilder(
            lambdaGroupName,
            configuration,
            programClassPool,
            libraryClassPool,
            entryMapper,
            notMergedLambdaVisitor
        )
        val lambdaClass = programClassPool.getClass("TestKt\$main\$lambda1\$1") as ProgramClass
        lambdaClass.accept(ProgramClassOptimizationInfoSetter())
        val optimizationInfo = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(lambdaClass)
        val originalInvokeMethodCodeBuilder = InstructionSequenceBuilder(programClassPool, libraryClassPool)
        lambdaClass.accept(
            AllMemberVisitor(
                MemberNameFilter(
                    "invoke",
                    MemberAccessFilter(
                        0, AccessConstants.SYNTHETIC,
                        AllAttributeVisitor(
                            AllInstructionVisitor(
                                InstructionSequenceCollector(
                                    originalInvokeMethodCodeBuilder
                                )
                            )
                        )
                    )
                )
            )
        )
        val originalInstructionsMatcher = InstructionSequenceMatcher(
            originalInvokeMethodCodeBuilder.constants(),
            originalInvokeMethodCodeBuilder.instructions()
        )
        val originalInstructionsMatchDetector = MatchDetector(originalInstructionsMatcher)
        "When the builder adds a lambda class to the lambda group under construction" - {
            builder.visitProgramClass(lambdaClass)
            val lambdaGroup = builder.build()
            "Then the lambda class implementation has been added to the lambda group" {
                lambdaGroup.accept(
                    AllMemberVisitor(
                        AllAttributeVisitor(
                            AllInstructionVisitor(
                                originalInstructionsMatchDetector
                            )
                        )
                    )
                )
                originalInstructionsMatchDetector.matchIsFound shouldBe true
            }

            "Then the optimization info lambda group of the lambda class refers to the lambda group" {
                optimizationInfo.lambdaGroup shouldBe lambdaGroup
            }

            "Then the optimization info target class of the lambda class refers to the lambda group" {
                optimizationInfo.targetClass shouldBe lambdaGroup
            }

            "Then the optimization info class id of the lambda class is 0 or greater" {
                optimizationInfo.lambdaGroupClassId shouldBeGreaterThanOrEqual 0
            }

            /*
            "Then the lambda group has the same output on invocation as the original lambda class" {
                /*val stdOutput = System.out
                val capturedOutputBefore = ByteArrayOutputStream()
                val capturedOutputStreamBefore = PrintStream(capturedOutputBefore)
                //System.setOut(capturedOutputStreamBefore)

                testClassBefore.declaredMethods.single { it.name == "main" && it.isSynthetic}.invoke(null, arrayOf<String>())

                val loaderAfter = ClassPoolClassLoader(newProgramClassPool)
                val testClassAfter = loaderAfter.loadClass("app.package2.Test2Kt")
                val capturedOutputAfter = ByteArrayOutputStream()
                val capturedOutputStreamAfter = PrintStream(capturedOutputBefore)
                //System.setOut(capturedOutputStreamAfter)

                testClassAfter.declaredMethods.single { it.name == "main" && it.isSynthetic}.invoke(null, arrayOf<String>())

                System.setOut(stdOutput)

                capturedOutputAfter.toByteArray().toString() shouldBe capturedOutputBefore.toByteArray().toString()
                println(capturedOutputAfter.toByteArray())
                println()
                println(capturedOutputBefore.toByteArray())
            */
            }
             */
        }
    }
})
