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

package proguard.obfuscate.kotlin

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldExistInOrder
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import proguard.classfile.kotlin.KotlinClassKindMetadata
import proguard.classfile.kotlin.KotlinConstructorMetadata
import proguard.classfile.kotlin.KotlinFunctionMetadata
import proguard.classfile.kotlin.KotlinPropertyMetadata
import proguard.classfile.kotlin.visitor.AllConstructorVisitor
import proguard.classfile.kotlin.visitor.AllFunctionVisitor
import proguard.classfile.kotlin.visitor.AllPropertyVisitor
import proguard.classfile.kotlin.visitor.AllValueParameterVisitor
import proguard.classfile.kotlin.visitor.KotlinValueParameterVisitor
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.KotlinSource
import proguard.classfile.kotlin.KotlinValueParameterMetadata as ValueParameter
import proguard.obfuscate.kotlin.KotlinValueParameterUsageMarker as VpUsageMarker

class KotlinValueParameterNameShrinkerTest : FreeSpec({

    // InstancePerTest so the names are reset before every test
    isolationMode = IsolationMode.InstancePerTest

    val (programClassPool, _) =
        ClassPoolBuilder.fromSource(
            KotlinSource(
                "Test.kt",
                """
                @Suppress("UNUSED_PARAMETER")
                class Foo(param1: String, param2: String, param3: String) {
                    var property: String = "foo"
                        set(param1) { }
                    fun foo(param1: String, param2: String, param3: String) {}
                }
                """.trimIndent(),
            ),
        )

    "Given a class with value parameters" - {
        val clazz = programClassPool.getClass("Foo")

        "When none are marked unused" - {
            "Then they should keep their names" {

                val valueParameterVisitor = spyk<KotlinValueParameterVisitor>()

                mockkStatic(VpUsageMarker::class)
                every { VpUsageMarker.isUsed(any()) } returns true
                clazz.kotlinMetadataAccept(KotlinValueParameterNameShrinker())

                clazz.kotlinMetadataAccept(
                    AllConstructorVisitor(
                        AllValueParameterVisitor(
                            valueParameterVisitor,
                        ),
                    ),
                )

                val valueParameters = mutableListOf<ValueParameter>()

                verify {
                    valueParameterVisitor.visitConstructorValParameter(
                        clazz,
                        ofType(KotlinClassKindMetadata::class),
                        ofType(KotlinConstructorMetadata::class),
                        capture(valueParameters),
                    )
                }

                valueParameters shouldExistInOrder
                    listOf<(ValueParameter) -> Boolean>(
                        { it.parameterName == "param1" },
                        { it.parameterName == "param2" },
                        { it.parameterName == "param3" },
                    )
            }
        }

        "When all are marked unused" - {
            "Then they should be renamed" {

                val valueParameterVisitor = spyk<KotlinValueParameterVisitor>()

                mockkStatic(VpUsageMarker::class)
                every { VpUsageMarker.isUsed(any()) } returns false
                clazz.kotlinMetadataAccept(KotlinValueParameterNameShrinker())

                clazz.kotlinMetadataAccept(
                    AllConstructorVisitor(
                        AllValueParameterVisitor(
                            valueParameterVisitor,
                        ),
                    ),
                )

                val valueParameters = mutableListOf<ValueParameter>()

                verify {
                    valueParameterVisitor.visitConstructorValParameter(
                        clazz,
                        ofType(KotlinClassKindMetadata::class),
                        ofType(KotlinConstructorMetadata::class),
                        capture(valueParameters),
                    )
                }

                valueParameters shouldExistInOrder
                    listOf<(ValueParameter) -> Boolean>(
                        { it.parameterName == "p0" },
                        { it.parameterName == "p1" },
                        { it.parameterName == "p2" },
                    )
            }
        }

        "When some are marked used" - {
            "Then only the unused ones should be renamed" {
                val valueParameterVisitor = spyk<KotlinValueParameterVisitor>()
                val valueParameters = mutableListOf<ValueParameter>()

                mockkStatic(VpUsageMarker::class)
                every {
                    VpUsageMarker.isUsed(match { it is ValueParameter && it.parameterName == "param2" })
                } returns true

                clazz.kotlinMetadataAccept(KotlinValueParameterNameShrinker())

                clazz.kotlinMetadataAccept(
                    AllConstructorVisitor(
                        AllValueParameterVisitor(
                            valueParameterVisitor,
                        ),
                    ),
                )

                verify(exactly = 3) {
                    valueParameterVisitor.visitConstructorValParameter(
                        clazz,
                        ofType(KotlinClassKindMetadata::class),
                        ofType(KotlinConstructorMetadata::class),
                        capture(valueParameters),
                    )
                }

                valueParameters shouldExistInOrder
                    listOf<(ValueParameter) -> Boolean>(
                        { it.parameterName == "p0" },
                        { it.parameterName == "param2" },
                        { it.parameterName == "p1" },
                    )
            }
        }
    }

    "Given a function with value parameters" - {
        val clazz = programClassPool.getClass("Foo")

        "When none are marked unused" - {
            "Then they should keep their names" {

                val valueParameterVisitor = spyk<KotlinValueParameterVisitor>()

                mockkStatic(VpUsageMarker::class)
                every { VpUsageMarker.isUsed(any()) } returns true
                clazz.kotlinMetadataAccept(KotlinValueParameterNameShrinker())

                clazz.kotlinMetadataAccept(
                    AllFunctionVisitor(
                        AllValueParameterVisitor(
                            valueParameterVisitor,
                        ),
                    ),
                )

                val valueParameters = mutableListOf<ValueParameter>()

                verify {
                    valueParameterVisitor.visitFunctionValParameter(
                        clazz,
                        ofType(KotlinClassKindMetadata::class),
                        ofType(KotlinFunctionMetadata::class),
                        capture(valueParameters),
                    )
                }

                valueParameters shouldExistInOrder
                    listOf<(ValueParameter) -> Boolean>(
                        { it.parameterName == "param1" },
                        { it.parameterName == "param2" },
                        { it.parameterName == "param3" },
                    )
            }
        }

        "When all are marked unused" - {
            "Then they should be renamed" {

                val valueParameterVisitor = spyk<KotlinValueParameterVisitor>()

                mockkStatic(VpUsageMarker::class)
                every { VpUsageMarker.isUsed(any()) } returns false
                clazz.kotlinMetadataAccept(KotlinValueParameterNameShrinker())

                clazz.kotlinMetadataAccept(
                    AllFunctionVisitor(
                        AllValueParameterVisitor(
                            valueParameterVisitor,
                        ),
                    ),
                )

                val valueParameters = mutableListOf<ValueParameter>()

                verify {
                    valueParameterVisitor.visitFunctionValParameter(
                        clazz,
                        ofType(KotlinClassKindMetadata::class),
                        ofType(KotlinFunctionMetadata::class),
                        capture(valueParameters),
                    )
                }

                valueParameters shouldExistInOrder
                    listOf<(ValueParameter) -> Boolean>(
                        { it.parameterName == "p0" },
                        { it.parameterName == "p1" },
                        { it.parameterName == "p2" },
                    )
            }
        }

        "When some are marked used" - {
            "Then only the unused ones should be renamed" {
                val valueParameterVisitor = spyk<KotlinValueParameterVisitor>()
                val valueParameters = mutableListOf<ValueParameter>()

                mockkStatic(VpUsageMarker::class)
                every {
                    VpUsageMarker.isUsed(match { it is ValueParameter && it.parameterName == "param2" })
                } returns true

                clazz.kotlinMetadataAccept(KotlinValueParameterNameShrinker())

                clazz.kotlinMetadataAccept(
                    AllFunctionVisitor(
                        AllValueParameterVisitor(
                            valueParameterVisitor,
                        ),
                    ),
                )

                verify(exactly = 3) {
                    valueParameterVisitor.visitFunctionValParameter(
                        clazz,
                        ofType(KotlinClassKindMetadata::class),
                        ofType(KotlinFunctionMetadata::class),
                        capture(valueParameters),
                    )
                }

                valueParameters shouldExistInOrder
                    listOf<(ValueParameter) -> Boolean>(
                        { it.parameterName == "p0" },
                        { it.parameterName == "param2" },
                        { it.parameterName == "p1" },
                    )
            }
        }
    }

    "Given a property with value parameters" - {
        val clazz = programClassPool.getClass("Foo")

        "When none are marked unused" - {
            "Then they should keep their names" {

                val valueParameterVisitor = spyk<KotlinValueParameterVisitor>()

                mockkStatic(VpUsageMarker::class)
                every { VpUsageMarker.isUsed(any()) } returns true
                clazz.kotlinMetadataAccept(KotlinValueParameterNameShrinker())

                clazz.kotlinMetadataAccept(
                    AllPropertyVisitor(
                        AllValueParameterVisitor(
                            valueParameterVisitor,
                        ),
                    ),
                )

                verify {
                    valueParameterVisitor.visitPropertyValParameter(
                        clazz,
                        ofType(KotlinClassKindMetadata::class),
                        ofType(KotlinPropertyMetadata::class),
                        withArg { it.parameterName shouldBe "param1" },
                    )
                }
            }
        }

        "When all are marked unused" - {
            "Then they should be renamed" {

                val valueParameterVisitor = spyk<KotlinValueParameterVisitor>()

                mockkStatic(VpUsageMarker::class)
                every { VpUsageMarker.isUsed(any()) } returns false
                clazz.kotlinMetadataAccept(KotlinValueParameterNameShrinker())

                clazz.kotlinMetadataAccept(
                    AllPropertyVisitor(
                        AllValueParameterVisitor(
                            valueParameterVisitor,
                        ),
                    ),
                )

                verify {
                    valueParameterVisitor.visitPropertyValParameter(
                        clazz,
                        ofType(KotlinClassKindMetadata::class),
                        ofType(KotlinPropertyMetadata::class),
                        withArg { it.parameterName shouldBe "p0" },
                    )
                }
            }
        }
    }
})
