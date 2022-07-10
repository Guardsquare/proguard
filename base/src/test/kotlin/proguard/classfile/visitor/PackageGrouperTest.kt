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

package proguard.classfile.visitor

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import proguard.classfile.ClassPool
import proguard.classfile.util.ClassUtil
import proguard.optimize.kotlin.visitor.PackageGrouper
import testutils.ClassPoolBuilder
import testutils.KotlinSource

class PackageGrouperTest : FreeSpec({

    val (programClassPool, _) = ClassPoolBuilder.fromSource(
        KotlinSource(
            "Test.kt",
            """
            package app.package1
            fun main(index: Int) {
                val lambda1 = { 
                    println("Lambda1")
                }
                lambda1()
            }
            """.trimIndent()
        ),
        KotlinSource(
            "Test2.kt",
            """
            package app.package2
            fun main() {
                val lambda2 = { println("Lambda2") }
                lambda2()
            }
            """.trimIndent()
        )
    )

    "Given a PackageGrouper" - {
        val grouper = PackageGrouper()
        "When the grouper is applied to classes of different packages" - {
            programClassPool.classesAccept(grouper)
            "Then the amount packages of packages found by the grouper equals the amount of packages in the class pool" {
                grouper.size() shouldBe 2
            }
            "Then the grouper has found the packages that are in the class pool" {
                grouper.containsPackage("app/package1") shouldBe true
                grouper.containsPackage("app/package2") shouldBe true
            }
            "Then the grouper does not contain other packages, except for those from the class pool" {
                grouper.packageNames().forEach {
                    it shouldBeIn arrayListOf("app/package2", "app/package1")
                }
            }
            "Then the union of classes in the packages of the package grouper contain equals the class pool" {
                val grouperCompleteClassPool = ClassPool()
                grouper.packagesAccept { packageClassPool ->
                    packageClassPool.classes().forEach { clazz ->
                        programClassPool.contains(clazz)
                        grouperCompleteClassPool.addClass(clazz)
                    }
                }
                programClassPool.classes().forEach { clazz ->
                    grouperCompleteClassPool.contains(clazz)
                }
            }
            "Then the classes of a package class pool belong to the respective package" {
                grouper.packageNames().forEach { packageName ->
                    grouper.packageAccept(packageName) { packageClassPool ->
                        packageClassPool.classesAccept { clazz ->
                            ClassUtil.internalPackageName(clazz.name) shouldBe packageName
                        }
                    }
                }
            }
        }
    }
})
