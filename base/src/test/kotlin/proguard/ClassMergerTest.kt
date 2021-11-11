/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import proguard.classfile.ProgramClass
import proguard.optimize.info.ClassOptimizationInfo
import proguard.optimize.peephole.ClassMerger
import testutils.AssemblerSource
import testutils.ClassPoolBuilder
import testutils.JavaSource

class ClassMergerTest : FreeSpec({
    val classB = ClassPoolBuilder.fromSource(
        JavaSource(
            "B.java",
            """
            public class B {}
            """.trimIndent()
        )
    ).programClassPool.getClass("B") as ProgramClass

    "Given a non-nested class" - {
        val classA = ClassPoolBuilder.fromSource(
            AssemblerSource(
                "A.jbc",
                """
                version 1.8;
                public class A extends java.lang.Object [
                    SourceFile "A.java";
                    ] {
                }
                """.trimIndent()
            )
        ).programClassPool.getClass("A") as ProgramClass
        classA.setProcessingInfo(ClassOptimizationInfo())
        classB.setProcessingInfo(ClassOptimizationInfo())

        "the check should indicate so" {
            ClassMerger.isNestHostOrMember(classA) shouldBe false
        }
        "it should be mergeable" {
            ClassMerger(classB, true, true, true).isMergeable(classA) shouldBe false
        }
    }

    "Given a nested class" - {
        val classA = ClassPoolBuilder.fromSource(
            AssemblerSource(
                "A.jbc",
                """
                version 1.8;
                public class A extends java.lang.Object [
                    NestHost java.lang.Class;
                    SourceFile "A.java";
                    ] {
                }
                """.trimIndent()
            )
        ).programClassPool.getClass("A") as ProgramClass
        classA.setProcessingInfo(ClassOptimizationInfo())
        classB.setProcessingInfo(ClassOptimizationInfo())

        "the check should indicate so" {
            ClassMerger.isNestHostOrMember(classA) shouldBe true
        }
        "it should not be mergeable" {
            ClassMerger(classB, true, true, true).isMergeable(classA) shouldBe false
        }
    }
})
