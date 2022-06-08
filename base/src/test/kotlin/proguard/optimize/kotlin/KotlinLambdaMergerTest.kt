/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package proguard.optimize.kotlin

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import proguard.AppView
import proguard.Configuration
import proguard.classfile.*
import proguard.classfile.attribute.CodeAttribute
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.constant.ClassConstant
import proguard.classfile.constant.Constant
import proguard.classfile.constant.visitor.ConstantVisitor
import proguard.classfile.editor.InstructionSequenceBuilder
import proguard.classfile.instruction.Instruction
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.instruction.visitor.InstructionVisitor
import proguard.classfile.instruction.visitor.MultiInstructionVisitor
import proguard.classfile.util.InstructionSequenceMatcher
import proguard.classfile.visitor.AllMemberVisitor
import proguard.classfile.visitor.MemberAccessFilter
import proguard.classfile.visitor.MemberNameFilter
import proguard.io.ExtraDataEntryNameMap
import testutils.*
import java.io.*

class KotlinLambdaMergerTest : FreeSpec({

    val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
        KotlinSource(
            "Test.kt",
            """
            package app.package1
            fun main(index: Int) {
                val lambda1 = { 
                    println("Lambda1")
                    val lambda1a = {
                        println("Lambda1a")
                        val lambda1a1 = {
                            println("Lambda1a1")
                        }
                        lambda1a1()
                    }
                    lambda1a()
                }
                val lambda2 = { println("Lambda2") }
                val lambda3 = { println("Lambda3") }
                when (index) {
                    0 -> lambda1()
                    1 -> lambda2()
                    else -> lambda3()
                }
            }
            """.trimIndent()
        ),
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

    "Given a Kotlin Lambda Merger and entry name mapper" - {
        val merger = KotlinLambdaMerger(Configuration())
        val nameMapper = ExtraDataEntryNameMap()

        class InstructionPrinter : InstructionVisitor {
            override fun visitAnyInstruction(
                clazz: Clazz,
                method: Method,
                codeAttribute: CodeAttribute,
                offset: Int,
                instruction: Instruction
            ) {
                println(instruction.toString(clazz, offset))
            }
        }

        "When the merger is applied to the class pools" - {
            val oldProgramClassPool = ClassPool(programClassPool)
            val loaderBefore = ClassPoolClassLoader(oldProgramClassPool)
            val testClassBefore = loaderBefore.loadClass("app.package2.Test2Kt")
            val stdOutput = System.out
            val capturedOutputBefore = ByteArrayOutputStream()
            val capturedOutputStreamBefore = PrintStream(capturedOutputBefore)
            System.setOut(capturedOutputStreamBefore)
            try {
                testClassBefore.declaredMethods.single { it.name == "main" && it.isSynthetic }
                    .invoke(null, arrayOf<String>())
            } catch (e: Exception) {
                System.setOut(stdOutput)
                println()
                println("Exception: $e")
                println("Output of method call:")
                println(capturedOutputBefore)
            }
            System.setOut(stdOutput)

            val appView = AppView(programClassPool, libraryClassPool, null, nameMapper)
            merger.execute(appView)
            val newProgramClassPool = appView.programClassPool
            val newFullClassPool = appView.programClassPool.classes() union libraryClassPool.classes()
            "Then the resulting program class pool contains less classes" {
                oldProgramClassPool.size() shouldBeGreaterThan newProgramClassPool.size()
            }

            "Then the program classes should only refer to classes that are in the class pool" {
                val visitor = mockk<ConstantVisitor>()
                val slot = slot<ClassConstant>()
                every {
                    visitor.visitAnyConstant(any(), any())
                    visitor.visitDoubleConstant(any(), any())
                    visitor.visitDynamicConstant(any(), any())
                    visitor.visitAnyRefConstant(any(), any())
                    visitor.visitAnyMethodrefConstant(any(), any())
                    visitor.visitFieldrefConstant(any(), any())
                    visitor.visitFloatConstant(any(), any())
                    visitor.visitIntegerConstant(any(), any())
                    visitor.visitInterfaceMethodrefConstant(any(), any())
                    visitor.visitInvokeDynamicConstant(any(), any())
                    visitor.visitLongConstant(any(), any())
                    visitor.visitMethodHandleConstant(any(), any())
                    visitor.visitMethodrefConstant(any(), any())
                    visitor.visitMethodTypeConstant(any(), any())
                    visitor.visitModuleConstant(any(), any())
                    visitor.visitNameAndTypeConstant(any(), any())
                    visitor.visitPackageConstant(any(), any())
                    visitor.visitPrimitiveArrayConstant(any(), any())
                    visitor.visitStringConstant(any(), any())
                    visitor.visitUtf8Constant(any(), any())
                } just Runs
                every {
                    visitor.visitClassConstant(any(), capture(slot))
                } answers {
                    println("Visit class constant referring to ${slot.captured.referencedClass}")
                    newFullClassPool shouldContain slot.captured.referencedClass
                }
                newProgramClassPool.classes().forEach {
                    it.constantPoolEntriesAccept(visitor)
                }
            }

            "Then for each package with lambda's a lambda group has been created." {
                newProgramClassPool.getClass("app/package1/LambdaGroup") shouldNotBe null
                newProgramClassPool.getClass("app/package2/LambdaGroup") shouldNotBe null
            }

            "Then the program output has not changed after optimisation" {
                val loaderAfter = ClassPoolClassLoader(newProgramClassPool)
                val testClassAfter = loaderAfter.loadClass("app.package2.Test2Kt")
                val capturedOutputAfter = ByteArrayOutputStream()
                val capturedOutputStreamAfter = PrintStream(capturedOutputAfter)
                System.setOut(capturedOutputStreamAfter)

                try {
                    testClassAfter.declaredMethods.single { it.name == "main" && it.isSynthetic }
                        .invoke(null, arrayOf<String>())
                } catch (e: Exception) {
                    System.setOut(stdOutput)
                    println("Exception while executing test class after lambda merging.")
                    println(e)
                    e.printStackTrace()
                    val lambdaGroup = newProgramClassPool.getClass("app/package2/LambdaGroup")
                    lambdaGroup.accept(AllMemberVisitor(
                                       MemberNameFilter("invoke",
                                       AllAttributeVisitor(
                                       AllInstructionVisitor(
                                       InstructionPrinter()))))
                    )
                    throw e
                }
                System.setOut(stdOutput)
                capturedOutputAfter.toString() shouldBe capturedOutputBefore.toString()
            }
        }
    }
})
