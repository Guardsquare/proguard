/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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

package proguard.backport

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.editor.InstructionSequenceBuilder
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.util.InstructionSequenceMatcher
import proguard.classfile.visitor.ClassPrinter
import proguard.io.ExtraDataEntryNameMap
import testutils.ClassPoolBuilder
import testutils.JavaSource

class LambdaExpressionConverterTest : FreeSpec({
    "Given a lambda expression" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            JavaSource(
                "Test.java",
                """
                import java.util.function.Function;
                public class Test {
                    public void test() {
                        foo(s -> s.length());
                    }
                    public void foo(Function<String, Integer> function) {
                        System.out.println(function.apply("Hello")); 
                    }
                }
                """.trimIndent()
            )
        )
        programClassPool.classesAccept(ClassPrinter())
        "When backported" - {
            val lambdaExpressionConvertor = LambdaExpressionConverter(programClassPool, libraryClassPool, ExtraDataEntryNameMap(), null)
            programClassPool.classesAccept("Test", lambdaExpressionConvertor)
            programClassPool.classesAccept(ClassPrinter())
            "Then a new class should be created" {
                programClassPool.getClass("Test\$\$Lambda\$0") shouldNotBe null
            }

            "Then the invocation should call the backported lambda" {
                val clazz = programClassPool.getClass("Test")
                val builder = InstructionSequenceBuilder()
                builder
                    .aload_0()
                    .getstatic("Test\$\$Lambda\$0", "INSTANCE", "LTest\$\$Lambda\$0;")
                    .invokevirtual("Test", "foo", "(Ljava/util/function/Function;)V")
                    .return_()

                val matcher = InstructionSequenceMatcher(builder.constants(), builder.instructions())
                clazz.methodAccept("test", "()V", AllAttributeVisitor(AllInstructionVisitor(matcher)))
                matcher.isMatching shouldBe true
            }
        }
    }
})
