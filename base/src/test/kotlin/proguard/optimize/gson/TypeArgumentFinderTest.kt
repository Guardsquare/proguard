/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
 */

package proguard.optimize.gson

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.instruction.Instruction
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.instruction.visitor.InstructionOpCodeFilter
import proguard.classfile.visitor.AllMethodVisitor
import proguard.classfile.visitor.MemberNameFilter
import proguard.evaluation.PartialEvaluator
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource

class TypeArgumentFinderTest : FreeSpec({
    "Given an aload instruction with TypeToken" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            JavaSource(
                "Type.java",
                """
                    package java.lang.reflect;

                    public class Type {}
                """.trimIndent()
            ),
            JavaSource(
                "TypeToken.java",
                """
                    package com.google.gson.reflect;
                    
                    import java.lang.reflect.Type;
    
                    public class TypeToken<T> {
                        Type type;
                    
                        public final Type getType() {
                            return type;
                        }
                    }
                """.trimIndent()
            ),
            JavaSource(
                "A.java",
                """
                    import com.google.gson.reflect.TypeToken;
                    import java.lang.reflect.Type;
                    
                    public class A {
                        public void a() {
                            Type x = new TypeToken<String>() {}.getType();
                            System.out.println(x);
                        }
                    }
                """.trimIndent()
            )
        )

        "When retrieving the return type with TypeArgumentFinder" - {
            val partialEvaluator = PartialEvaluator()

            programClassPool.classAccept(
                "A",
                AllMethodVisitor(
                    MemberNameFilter(
                        "a",
                        AllAttributeVisitor(partialEvaluator)
                    )
                )
            )

            val typeArgumentFinder = TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator)

            programClassPool.classAccept(
                "A",
                AllMethodVisitor(
                    MemberNameFilter(
                        "a",
                        AllAttributeVisitor(
                            AllInstructionVisitor(
                                InstructionOpCodeFilter(
                                    intArrayOf(
                                        Instruction.OP_ALOAD.toInt(),
                                        Instruction.OP_ALOAD_0.toInt(),
                                        Instruction.OP_ALOAD_1.toInt(),
                                        Instruction.OP_ALOAD_2.toInt(),
                                        Instruction.OP_ALOAD_3.toInt()
                                    ),
                                    typeArgumentFinder
                                )
                            )
                        )
                    )
                )
            )

            "Then we obtain the return type java.lang.String" {
                typeArgumentFinder.typeArgumentClasses.shouldContainExactly("java/lang/String")
            }
        }
    }

    "Given an invokevirtual instruction with TypeToken" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            JavaSource(
                "Type.java",
                """
                    package java.lang.reflect;
    
                    public class Type {}
                """.trimIndent()
            ),
            JavaSource(
                "TypeToken.java",
                """
                    package com.google.gson.reflect;
                    
                    import java.lang.reflect.Type;
    
                    public class TypeToken<T> {
                        Type type;
                    
                        public final Type getType() {
                            return type;
                        }
                    }
                """.trimIndent()
            ),
            JavaSource(
                "A.java",
                """
                    import com.google.gson.reflect.TypeToken;
                    import java.lang.reflect.Type;
                    
                    public class A {
                        public void a() {
                            new TypeToken<String>() {}.getType();
                        }
                    }
                """.trimIndent()
            )
        )

        "When retrieving the return type with TypeArgumentFinder" - {
            val typeArgumentFinder = TypeArgumentFinder(programClassPool, libraryClassPool, null)

            programClassPool.classAccept(
                "A",
                AllMethodVisitor(
                    MemberNameFilter(
                        "a",
                        AllAttributeVisitor(
                            AllInstructionVisitor(
                                InstructionOpCodeFilter(
                                    intArrayOf(Instruction.OP_INVOKEVIRTUAL.toInt()),
                                    typeArgumentFinder
                                )
                            )
                        )
                    )
                )
            )

            "Then we obtain null (Note: this case is not implemented like for aload instructions)" {
                typeArgumentFinder.typeArgumentClasses shouldBe null
            }
        }
    }

    "Given an ldc instruction" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            JavaSource(
                "Z.java",
                """
                    package a.b;
                    
                    public class Z {
                        public void a() {
                            x(Z.class);
                        }
                        
                        public void x(Class<?> c) {}
                    }
                """.trimIndent()
            )
        )

        "When retrieving the return type with TypeArgumentFinder" - {
            val typeArgumentFinder = TypeArgumentFinder(programClassPool, libraryClassPool, null)

            programClassPool.classAccept(
                "a/b/Z",
                AllMethodVisitor(
                    MemberNameFilter(
                        "a",
                        AllAttributeVisitor(
                            AllInstructionVisitor(
                                InstructionOpCodeFilter(
                                    intArrayOf(Instruction.OP_LDC.toInt()),
                                    typeArgumentFinder
                                )
                            )
                        )
                    )
                )
            )

            "Then we obtain the return type a.b.Z" {
                typeArgumentFinder.typeArgumentClasses.shouldContainExactly("a/b/Z")
            }
        }
    }

    "Given an aload instruction" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            JavaSource(
                "A.java",
                """
                    public class A {
                        public void a() {
                            String x = "text";
                            System.out.println(x);
                        }
                    }
                """.trimIndent()
            )
        )

        "When retrieving the return type with TypeArgumentFinder" - {
            val partialEvaluator = PartialEvaluator()

            programClassPool.classAccept(
                "A",
                AllMethodVisitor(
                    MemberNameFilter(
                        "a",
                        AllAttributeVisitor(partialEvaluator)
                    )
                )
            )

            val typeArgumentFinder = TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator)

            programClassPool.classAccept(
                "A",
                AllMethodVisitor(
                    MemberNameFilter(
                        "a",
                        AllAttributeVisitor(
                            AllInstructionVisitor(
                                InstructionOpCodeFilter(
                                    intArrayOf(
                                        Instruction.OP_ALOAD.toInt(),
                                        Instruction.OP_ALOAD_0.toInt(),
                                        Instruction.OP_ALOAD_1.toInt(),
                                        Instruction.OP_ALOAD_2.toInt(),
                                        Instruction.OP_ALOAD_3.toInt()
                                    ),
                                    typeArgumentFinder
                                )
                            )
                        )
                    )
                )
            )

            // This is not an intended use case of the TypeArgumentFinder
            // so it returns null
            "Then we obtain null" {
                typeArgumentFinder.typeArgumentClasses shouldBe null
            }
        }
    }

    "Given a new instruction" - {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            JavaSource(
                "Z.java",
                """
                    package a.b;
                    
                    public class Z {
                        public void a() {
                            new Z();
                        }
                    }
                """.trimIndent()
            )
        )

        "When retrieving the return type with TypeArgumentFinder" - {
            val typeArgumentFinder = TypeArgumentFinder(programClassPool, libraryClassPool, null)

            programClassPool.classAccept(
                "a/b/Z",
                AllMethodVisitor(
                    MemberNameFilter(
                        "a",
                        AllAttributeVisitor(
                            AllInstructionVisitor(
                                InstructionOpCodeFilter(
                                    intArrayOf(Instruction.OP_NEW.toInt()),
                                    typeArgumentFinder
                                )
                            )
                        )
                    )
                )
            )

            "Then we obtain the return type a.b.Z" {
                typeArgumentFinder.typeArgumentClasses.shouldContainExactly("a/b/Z")
            }
        }
    }
})
