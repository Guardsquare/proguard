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
import proguard.classfile.Clazz
import proguard.classfile.attribute.annotation.Annotation
import proguard.classfile.attribute.annotation.visitor.AllAnnotationVisitor
import proguard.classfile.attribute.annotation.visitor.AnnotationTypeFilter
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.visitor.ProcessingInfoSetter
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource

class MarkedAnnotationDeleterTest : FreeSpec({
    "Given a class with two annotations on its field" - {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            JavaSource("Ann1.java", "public @interface Ann1 {}"),
            JavaSource("Ann2.java", "public @interface Ann2 {}"),
            JavaSource(
                "A.java",
                """class A {
                @Ann1
                @Ann2
                public int a;
            }"""
            )
        )

        val classA = programClassPool.getClass("A")

        "When we mark and delete the annotations" - {
            val mark = Object()

            classA.fieldsAccept(
                AllAttributeVisitor(
                    AllAnnotationVisitor(
                        ProcessingInfoSetter(mark)
                    )
                )
            )

            classA.fieldsAccept(
                AllAttributeVisitor(
                    MarkedAnnotationDeleter(mark)
                )
            )

            "Then no annotations should remain" {
                var count = 0

                classA.fieldsAccept(
                    AllAttributeVisitor(
                        AllAnnotationVisitor(object : AnnotationVisitor {
                            override fun visitAnnotation(clazz: Clazz?, annotation: Annotation?) {
                                count += 1
                            }
                        }
                        )
                    )
                )

                count shouldBe 0
            }
        }
    }

    "Given a class with two A and B annotations on its field" - {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            JavaSource("AnnA1.java", "public @interface AnnA1 {}"),
            JavaSource("AnnA2.java", "public @interface AnnA2 {}"),
            JavaSource("AnnB1.java", "public @interface AnnB1 {}"),
            JavaSource("AnnB2.java", "public @interface AnnB2 {}"),
            JavaSource("AnnB3.java", "public @interface AnnB3 {}"),
            JavaSource(
                "A.java",
                """class A {
                @AnnB1
                @AnnA1
                @AnnB2
                @AnnA2
                @AnnB3
                public int a;
            }"""
            )
        )

        val classA = programClassPool.getClass("A")

        "When we mark and delete only the A annotations" - {
            val mark = Object()

            classA.fieldsAccept(
                AllAttributeVisitor(
                    AllAnnotationVisitor(
                        AnnotationTypeFilter(
                            "LAnnA*;",
                            ProcessingInfoSetter(mark)
                        )
                    )
                )
            )

            classA.fieldsAccept(
                AllAttributeVisitor(
                    MarkedAnnotationDeleter(mark)
                )
            )

            "Then only the B annotations should remain" {
                val list = mutableListOf<String>()

                classA.fieldsAccept(
                    AllAttributeVisitor(
                        AllAnnotationVisitor(object : AnnotationVisitor {
                            override fun visitAnnotation(clazz: Clazz, annotation: Annotation) {
                                list.add(annotation.getType(clazz))
                            }
                        }
                        )
                    )
                )

                list shouldContainExactly listOf("LAnnB1;", "LAnnB2;", "LAnnB3;")
            }
        }
    }

    "Given a class with two annotations on its method parameter" - {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            JavaSource("Ann1.java", "public @interface Ann1 {}"),
            JavaSource("Ann2.java", "public @interface Ann2 {}"),
            JavaSource(
                "A.java",
                """class A {
                public void a(@Ann1 @Ann2 int x) {}
            }"""
            )
        )

        val classA = programClassPool.getClass("A")

        "When we mark and delete the annotations" - {
            val mark = Object()

            classA.methodsAccept(
                AllAttributeVisitor(
                    AllAnnotationVisitor(
                        ProcessingInfoSetter(mark)
                    )
                )
            )

            classA.methodsAccept(
                AllAttributeVisitor(
                    MarkedAnnotationDeleter(mark)
                )
            )

            "Then no annotations should remain" {
                var count = 0

                classA.methodsAccept(
                    AllAttributeVisitor(
                        AllAnnotationVisitor(object : AnnotationVisitor {
                            override fun visitAnnotation(clazz: Clazz?, annotation: Annotation?) {
                                count += 1
                            }
                        }
                        )
                    )
                )

                count shouldBe 0
            }
        }
    }

    "Given a class with two A and B annotations on its method parameter" - {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            JavaSource("AnnA1.java", "public @interface AnnA1 {}"),
            JavaSource("AnnA2.java", "public @interface AnnA2 {}"),
            JavaSource("AnnB1.java", "public @interface AnnB1 {}"),
            JavaSource("AnnB2.java", "public @interface AnnB2 {}"),
            JavaSource("AnnB3.java", "public @interface AnnB3 {}"),
            JavaSource(
                "A.java",
                """class A {
                public void a(@AnnB1 @AnnA1 @AnnB2 @AnnA2 @AnnB3 int x) {}
            }"""
            )
        )

        val classA = programClassPool.getClass("A")

        "When we mark and delete only the A annotations" - {
            val mark = Object()

            classA.methodsAccept(
                AllAttributeVisitor(
                    AllAnnotationVisitor(
                        AnnotationTypeFilter(
                            "LAnnA*;",
                            ProcessingInfoSetter(mark)
                        )
                    )
                )
            )

            classA.methodsAccept(
                AllAttributeVisitor(
                    MarkedAnnotationDeleter(mark)
                )
            )

            "Then only the B annotations should remain" {
                val list = mutableListOf<String>()

                classA.methodsAccept(
                    AllAttributeVisitor(
                        AllAnnotationVisitor(object : AnnotationVisitor {
                            override fun visitAnnotation(clazz: Clazz, annotation: Annotation) {
                                list.add(annotation.getType(clazz))
                            }
                        }
                        )
                    )
                )

                list shouldContainExactly listOf("LAnnB1;", "LAnnB2;", "LAnnB3;")
            }
        }
    }
})
