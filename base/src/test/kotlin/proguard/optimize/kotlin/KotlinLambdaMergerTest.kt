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
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import io.mockk.*
import proguard.Configuration
import proguard.classfile.ClassConstants
import proguard.classfile.ClassPool
import proguard.classfile.constant.ClassConstant
import proguard.classfile.constant.visitor.ConstantVisitor
import proguard.classfile.visitor.ClassMethodFilter
import proguard.classfile.visitor.ClassPoolFiller
import proguard.classfile.visitor.ImplementedClassFilter
import proguard.io.ExtraDataEntryNameMap
import testutils.ClassPoolBuilder
import testutils.KotlinSource

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


    // A class pool where the applicable lambda's will be stored
    val lambdaClassPool = ClassPool()
    val kotlinLambdaClass = libraryClassPool.getClass(KotlinLambdaMerger.NAME_KOTLIN_LAMBDA)
    val kotlinFunction0Interface = libraryClassPool.getClass(KotlinLambdaMerger.NAME_KOTLIN_FUNCTION0)
    programClassPool.classesAccept(ImplementedClassFilter(
                                   kotlinFunction0Interface, false,
                                   ImplementedClassFilter(
                                   kotlinLambdaClass, false,
                                   ClassMethodFilter(
                                   ClassConstants.METHOD_NAME_INIT, ClassConstants.METHOD_TYPE_INIT,
                                   ClassPoolFiller(lambdaClassPool), null), null), null))

    "Given a Kotlin Lambda Package Grouper" {
        val grouper = PackageGrouper()
        "When the grouper is applied to lambda's of different packages" {
            programClassPool.classesAccept(grouper)
            "Then the grouper has found 2 packages" {
                grouper.size() shouldBe 2
            }
            "Then the grouper has found the packages app/package1 and app/package2" {
                grouper.containsPackage("app/package1") shouldBe true
                grouper.containsPackage("app/package2") shouldBe true
            }
            "Then the grouper does not contain other packages" {
                grouper.packageNames().forEach {
                    it shouldBeIn arrayListOf("app/package2", "app/package1")
                }
            }
        }
    }

    "Given a Kotlin Lambda Merger and entry name mapper" - {
        val merger = KotlinLambdaMerger(Configuration())
        val nameMapper = ExtraDataEntryNameMap()
        "When the merger is applied to the class pools" - {
            val newProgramClassPool = merger.execute(programClassPool, libraryClassPool, null, nameMapper)
            val newFullClassPool = newProgramClassPool.classes() union libraryClassPool.classes()
            "Then the resulting program class pool contains less classes" {
                programClassPool.size() shouldBeGreaterThanOrEqual newProgramClassPool.size()
            }

            "Then the program classes should only refer to classes that are longer in the class pool" {
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
        }
    }
})
