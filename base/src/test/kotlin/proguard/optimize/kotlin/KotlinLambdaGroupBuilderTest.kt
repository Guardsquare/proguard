package proguard.optimize.kotlin

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import proguard.Configuration
import proguard.classfile.AccessConstants
import proguard.classfile.ClassPool
import proguard.classfile.ProgramClass
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.editor.InstructionSequenceBuilder
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.util.ClassUtil
import proguard.classfile.util.InstructionSequenceMatcher
import proguard.classfile.visitor.AllMemberVisitor
import proguard.classfile.visitor.MemberAccessFilter
import proguard.classfile.visitor.MemberNameFilter
import proguard.io.ExtraDataEntryNameMap
import proguard.optimize.info.ProgramClassOptimizationInfo
import proguard.optimize.info.ProgramClassOptimizationInfoSetter
import testutils.*

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
        val mergedLambdaVisitor = null
        val notMergedLambdaVisitor = null
        val builder = KotlinLambdaGroupBuilder(
            lambdaGroupName,
            configuration,
            programClassPool,
            libraryClassPool,
            entryMapper,
            mergedLambdaVisitor,
            notMergedLambdaVisitor
        )
        val lambdaClassName = "TestKt\$main\$lambda1\$1"
        val arity = 0
        val lambdaClass = programClassPool.getClass(lambdaClassName) as ProgramClass
        lambdaClass.accept(ProgramClassOptimizationInfoSetter())
        val optimizationInfo = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(lambdaClass)
        val capturedExecutionOutputBefore = captureExecutionOutput(programClassPool,
            lambdaClassName) {
                testClassBefore ->
            val instance = testClassBefore.getDeclaredConstructor().newInstance()
            testClassBefore.declaredMethods.single { it.name == "invoke" && it.isBridge }
                .invoke(instance, null)
        }

        "When the builder adds a lambda class to the lambda group under construction" - {
            builder.visitProgramClass(lambdaClass)
            val lambdaGroup = builder.build()

            "Then the optimization info lambda group of the lambda class refers to the lambda group" {
                optimizationInfo.lambdaGroup shouldBe lambdaGroup
            }

            "Then the optimization info target class of the lambda class refers to the lambda group" {
                optimizationInfo.targetClass shouldBe lambdaGroup
            }

            "Then the optimization info class id of the lambda class is 0 or greater" {
                optimizationInfo.lambdaGroupClassId shouldBeGreaterThanOrEqual 0
            }


            "Then the lambda group has the same output on invocation as the original lambda class" {
                val capturedExecutionOutputAfter = captureExecutionOutput(ClassPool(lambdaGroup),
                    lambdaGroupName) {
                        testClassBefore ->
                    val instance = testClassBefore.getDeclaredConstructor().newInstance(
                        optimizationInfo.lambdaGroupClassId, arity)
                    testClassBefore.declaredMethods.single { it.name == "invoke" && it.isBridge }
                        .invoke(instance)
                }
                capturedExecutionOutputAfter shouldBe capturedExecutionOutputBefore
            }
        }
    }
})
