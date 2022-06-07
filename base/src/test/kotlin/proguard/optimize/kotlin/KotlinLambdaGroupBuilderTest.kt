package proguard.optimize.kotlin

import io.kotest.core.spec.style.FreeSpec
import testutils.ClassPoolBuilder
import testutils.KotlinSource

class KotlinLambdaGroupBuilderTest : FreeSpec({
    val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
        KotlinSource(
            "Test2.kt",
            """
            package app.package2
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

    "Given a lambda group builder" - {
        "When the builder is adds a lambda class to the lambda group under construction" - {

            "Then the lambda class implementation has been added to the lambda group" {
            }

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
        }
    }
})
