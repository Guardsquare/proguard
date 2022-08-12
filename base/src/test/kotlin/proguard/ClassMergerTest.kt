/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard.optimize.peephole

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import proguard.classfile.ProgramClass
import proguard.classfile.VersionConstants.CLASS_VERSION_1_8
import proguard.classfile.visitor.ClassVersionSetter
import proguard.classfile.visitor.ProcessingInfoSetter
import proguard.optimize.info.ClassOptimizationInfo
import proguard.optimize.info.ProgramClassOptimizationInfo
import proguard.testutils.AssemblerSource
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource

class ClassMergerTest : FreeSpec({
    val classBPools = ClassPoolBuilder.fromSource(
        JavaSource(
            "B.java",
            """
            public class B {}
            """.trimIndent()
        )
    )

    val classB = classBPools.programClassPool.getClass("B") as ProgramClass
    classB.accept(ClassVersionSetter(CLASS_VERSION_1_8))
    classBPools.libraryClassPool.classesAccept(ProcessingInfoSetter(ClassOptimizationInfo()))
    classBPools.programClassPool.classesAccept(ProcessingInfoSetter(ProgramClassOptimizationInfo()))

    "Given a non-nested class" - {
        val classAPools = ClassPoolBuilder.fromSource(
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
        )
        val classA = classAPools.programClassPool.getClass("A") as ProgramClass

        "the check should indicate so" {
            ClassMerger.isNestHostOrMember(classA) shouldBe false
        }
        "it should be mergeable" {
            classAPools.libraryClassPool.classesAccept(ProcessingInfoSetter(ClassOptimizationInfo()))
            classAPools.programClassPool.classesAccept(ProcessingInfoSetter(ProgramClassOptimizationInfo()))
            ClassMerger(classB, true, true, true).isMergeable(classA) shouldBe true
        }
    }

    "Given a nested class" - {
        val classAPools = ClassPoolBuilder.fromSource(
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
        )

        val classA = classAPools.programClassPool.getClass("A") as ProgramClass

        "the check should indicate so" {
            ClassMerger.isNestHostOrMember(classA) shouldBe true
        }
        "it should not be mergeable" {
            classAPools.libraryClassPool.classesAccept(ProcessingInfoSetter(ClassOptimizationInfo()))
            classAPools.programClassPool.classesAccept(ProcessingInfoSetter(ProgramClassOptimizationInfo()))
            ClassMerger(classB, true, true, true).isMergeable(classA) shouldBe false
        }
    }

    "Given a class with no native methods" - {
        val targetPools = ClassPoolBuilder.fromSource(
            JavaSource(
                "Target.java",
                """
            public class Target {
                private boolean value;
                
                public Target(boolean value)
                {
                    this.value = value;
                }
                
                public void setValue(boolean value)
                {
                    this.value = value;
                }
            }
                """.trimIndent()
            )
        )

        targetPools.libraryClassPool.classesAccept(ProcessingInfoSetter(ClassOptimizationInfo()))
        targetPools.programClassPool.classesAccept(ProcessingInfoSetter(ProgramClassOptimizationInfo()))

        val target = targetPools.programClassPool.getClass("Target") as ProgramClass

        val merger = ClassMerger(target, true, true, true)

        "When merging in a class with no native methods" - {
            val sourcePools = ClassPoolBuilder.fromSource(
                JavaSource(
                    "Source.java",
                    """
                public class Source {
                    private String name;
                    
                    public String getName()
                    {
                        return name;
                    }
                }
                    """.trimIndent()
                )
            )

            "Then it should be possible to merge the classes" {
                sourcePools.libraryClassPool.classesAccept(ProcessingInfoSetter(ClassOptimizationInfo()))
                sourcePools.programClassPool.classesAccept(ProcessingInfoSetter(ProgramClassOptimizationInfo()))
                merger.isMergeable(sourcePools.programClassPool.getClass("Source") as ProgramClass) shouldBe true
            }
        }

        "When merging in a class with native methods" - {
            val sourcePools = ClassPoolBuilder.fromSource(
                JavaSource(
                    "Source.java",
                    """
                public class Source {
                    private String name;
                    
                    private native void method();
                    
                    public String getName()
                    {
                        return name;
                    }
                }
                    """.trimIndent()
                )
            )

            "Then it should not be possible to merge the classes" {
                sourcePools.libraryClassPool.classesAccept(ProcessingInfoSetter(ClassOptimizationInfo()))
                sourcePools.programClassPool.classesAccept(ProcessingInfoSetter(ProgramClassOptimizationInfo()))
                merger.isMergeable(sourcePools.programClassPool.getClass("Source") as ProgramClass) shouldBe false
            }
        }
    }

    "Given a class with native methods" - {
        // Same as above but checking if merging in a class with native methods is still possible.
        val targetPools = ClassPoolBuilder.fromSource(
            JavaSource(
                "Target.java",
                """
            public class Target {
                private boolean value;
                
                public static native void foo();
                
                public Target(boolean value)
                {
                    this.value = value;
                }
                
                public void setValue(boolean value)
                {
                    this.value = value;
                }
            }
                """.trimIndent()
            )
        )

        targetPools.libraryClassPool.classesAccept(ProcessingInfoSetter(ClassOptimizationInfo()))
        targetPools.programClassPool.classesAccept(ProcessingInfoSetter(ProgramClassOptimizationInfo()))

        val target = targetPools.programClassPool.getClass("Target") as ProgramClass

        val merger = ClassMerger(target, true, true, true)

        "When merging in a class with no native methods" - {
            val sourcePools = ClassPoolBuilder.fromSource(
                JavaSource(
                    "Source.java",
                    """
                public class Source {
                    private String name;
                    
                    public String getName()
                    {
                        return name;
                    }
                }
                    """.trimIndent()
                )
            )

            "Then it should be possible to merge the classes" {
                sourcePools.libraryClassPool.classesAccept(ProcessingInfoSetter(ClassOptimizationInfo()))
                sourcePools.programClassPool.classesAccept(ProcessingInfoSetter(ProgramClassOptimizationInfo()))
                merger.isMergeable(sourcePools.programClassPool.getClass("Source") as ProgramClass) shouldBe true
            }
        }

        "When merging in a class with native methods" - {
            val sourcePools = ClassPoolBuilder.fromSource(
                JavaSource(
                    "Source.java",
                    """
                public class Source {
                    private String name;
                    
                    private native void method();
                    
                    public String getName()
                    {
                        return name;
                    }
                }
                    """.trimIndent()
                )
            )

            "Then it should not be possible to merge the classes" {
                sourcePools.libraryClassPool.classesAccept(ProcessingInfoSetter(ClassOptimizationInfo()))
                sourcePools.programClassPool.classesAccept(ProcessingInfoSetter(ProgramClassOptimizationInfo()))
                merger.isMergeable(sourcePools.programClassPool.getClass("Source") as ProgramClass) shouldBe false
            }
        }
    }
})
