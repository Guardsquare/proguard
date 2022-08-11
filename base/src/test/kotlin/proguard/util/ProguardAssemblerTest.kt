/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard.util

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import proguard.classfile.ProgramClass
import proguard.classfile.constant.Utf8Constant
import proguard.testutils.AssemblerSource
import proguard.testutils.ClassPoolBuilder

class ProguardAssemblerTest : FreeSpec({
    "Given Java bytecode" - {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            AssemblerSource(
                "A.jbc",
                """
        version 1.8;
        public class A extends java.lang.Object [
            SourceFile "A.java";
            ] {

            public void <init>() {
                 line 1
                 aload_0
                 invokespecial java.lang.Object#void <init>()
                 return
            }

            public static void main(java.lang.String[]) {
                line 3
                getstatic java.lang.System#java.io.PrintStream out
                ldc "hello"
                invokevirtual java.io.PrintStream#void println(java.lang.String)
                line 4
                return
            }

        }
        """
            )
        )
        "When the ClassPool object is created" - {
            programClassPool.shouldNotBeNull()
            "Then the count and name of the methods should match the bytecode" {
                val classA = programClassPool.getClass("A") as ProgramClass
                classA.methods.size shouldBe 2
                (classA.constantPool[classA.methods[0].u2nameIndex] as Utf8Constant).string shouldBe "<init>"
                (classA.constantPool[classA.methods[1].u2nameIndex] as Utf8Constant).string shouldBe "main"
            }
        }
    }
})
