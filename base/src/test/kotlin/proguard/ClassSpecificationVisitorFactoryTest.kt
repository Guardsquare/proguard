/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.spyk
import io.mockk.verify
import proguard.classfile.visitor.ClassPoolVisitor
import proguard.classfile.visitor.ClassVisitor
import proguard.classfile.visitor.MemberCounter
import proguard.classfile.visitor.MemberVisitor
import proguard.testutils.AssemblerSource
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource
import testutils.asConfiguration

class ClassSpecificationVisitorFactoryTest : FreeSpec({
    "Given a class specification with an extending class" - {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            JavaSource("Foo.java", "class Foo extends Bar3 { }"),
            JavaSource("Bar3.java", "class Bar3 { }")
        )
        val spec = "-keep class F** extends Bar* { public *; }".asConfiguration().keep.first()
        val classVisitor = spyk<ClassVisitor>()
        val visitor = KeepClassSpecificationVisitorFactory(true, false, false).createClassPoolVisitor(
            spec, classVisitor, null, null, null
        )
        "Then the visitor should visit the sub-class" {
            programClassPool.accept(visitor)
            verify(exactly = 1) {
                classVisitor.visitAnyClass(programClassPool.getClass("Foo"))
            }
        }
    }

    "Given a class specification with an implementing class" - {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            JavaSource("Foo.java", "class Foo implements Bar3 { }"),
            JavaSource("Bar3.java", "interface Bar3 { }")
        )
        val spec = "-keep class F** implements Bar* { public *; }".asConfiguration().keep.first()
        val classVisitor = spyk<ClassVisitor>()
        val visitor = KeepClassSpecificationVisitorFactory(true, false, false).createClassPoolVisitor(
            spec, classVisitor, null, null, null
        )
        "Then the visitor should visit the implementing class" {
            programClassPool.accept(visitor)
            verify(exactly = 1) {
                classVisitor.visitAnyClass(programClassPool.getClass("Foo"))
            }
        }
    }

    "Given a class specification with a negation" - {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            JavaSource("Foo.java", "class Foo implements Bar3 { }"),
            JavaSource("Bar3.java", "interface Bar3 { }")
        )
        val spec = "-keep !interface B** { public *; }".asConfiguration().keep.first()
        val classVisitor = spyk<ClassVisitor>()
        val visitor = KeepClassSpecificationVisitorFactory(true, false, false).createClassPoolVisitor(
            spec, classVisitor, null, null, null
        )
        "Then the visitor should not visit the interface of which the name starts with 'B'" {
            programClassPool.accept(visitor)
            verify(exactly = 0) {
                classVisitor.visitAnyClass(programClassPool.getClass("Bar3"))
            }
        }
    }

    "Given a conditional class specification containing a wildcard" - {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            JavaSource("FooBar.java", "class FooBar extends Bar { }"),
            JavaSource("Bar.java", "class Bar { }")
        )
        val spec = "-if class * -keep class <1> { public *; }".asConfiguration().keep.first()
        val classVisitor = spyk<ClassVisitor>()
        val visitor = KeepClassSpecificationVisitorFactory(true, false, false).createClassPoolVisitor(
            spec, classVisitor, null, null, null
        )
        "Then the visitor should visit the matched class" {
            programClassPool.accept(visitor)
            verify(exactly = 1) {
                classVisitor.visitAnyClass(programClassPool.getClass("FooBar"))
            }
        }
    }

    "Given a class specification extending an annotation class" - {
        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            JavaSource("Annotation.java", "@interface Annotation { }"),
            JavaSource("FooBar.java", "class FooBar extends Bar { }"),
            JavaSource("Bar.java", "@Annotation class Bar { }")
        )
        val spec = "-keep class * extends @Annotation *".asConfiguration().keep.first()
        val classVisitor = spyk<ClassVisitor>()
        val visitor = KeepClassSpecificationVisitorFactory(true, false, false).createClassPoolVisitor(
            spec, classVisitor, null, null, null
        )
        "Then the visitor should visit the matched class" {
            programClassPool.accept(visitor)
            verify(exactly = 1) {
                classVisitor.visitAnyClass(programClassPool.getClass("FooBar"))
            }
        }
    }

    "Given a class with fields of different types" - {
        fun createFieldVisitor(config: String, fieldVisitor: MemberVisitor): ClassPoolVisitor =
            KeepClassSpecificationVisitorFactory(true, false, false).createClassPoolVisitor(
                config.asConfiguration().keep.first(), null, fieldVisitor, null, null
            )

        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            AssemblerSource(
                "ClassWithFields.jbc",
                """
                public class ClassWithFields {
                    java.lang.String myField;
                    int myField;
                    long myField;
                    com.example.ClassInAPackage myField;
                    java.lang.String[] myField;
                    int[] myField;
                    com.example.ClassInAPackage[] myField;
                    ClassWithFields myField;
                }                
                """.trimIndent()
            )
        )

        "Then * should visit any non-primitive, non-array without package separator type fields" {
            with(MemberCounter()) {
                programClassPool.accept(createFieldVisitor("-keep class ClassWithFields { * myField; }", this))
                count shouldBe 1
            }
        }

        "Then ** should visit any non-primitive, non-array separator type fields" {
            with(MemberCounter()) {
                programClassPool.accept(createFieldVisitor("-keep class ClassWithFields { ** myField; }", this))
                count shouldBe 3
            }
        }

        "Then *** should visit all fields" {
            with(MemberCounter()) {
                programClassPool.accept(createFieldVisitor("-keep class ClassWithFields { *** myField; }", this))
                count shouldBe 8
            }
        }

        "Then % should only visit primitive, non-array fields" {
            with(MemberCounter()) {
                programClassPool.accept(createFieldVisitor("-keep class ClassWithFields { % myField; }", this))
                count shouldBe 2
            }
        }
    }

    "Given a class with methods of different types" - {
        fun createMethodVisitor(config: String, methodVisitor: MemberVisitor): ClassPoolVisitor =
            KeepClassSpecificationVisitorFactory(true, false, false).createClassPoolVisitor(
                config.asConfiguration().keep.first(), null, null, methodVisitor, null
            )

        val (programClassPool, _) = ClassPoolBuilder.fromSource(
            AssemblerSource(
                "ClassWithFields.jbc",
                """
                public class ClassWithFields {
                    public void myMethod(java.lang.String);
                    public void myMethod(int);
                    public void myMethod(long);
                    public void myMethod(com.example.ClassInAPackage);
                    public void myMethod(java.lang.String[]);
                    public void myMethod(int[]);
                    public void myMethod(com.example.ClassInAPackage[]);
                    public void myMethod(ClassWithFields);
                }                
                """.trimIndent()
            )
        )

        "Then * should visit any non-primitive, non-array without package separator type methods" {
            with(MemberCounter()) {
                programClassPool.accept(createMethodVisitor("-keep class ClassWithFields { void myMethod(*); }", this))
                count shouldBe 1
            }
        }

        "Then ** should visit any non-primitive, non-array separator type methods" {
            with(MemberCounter()) {
                programClassPool.accept(createMethodVisitor("-keep class ClassWithFields { void myMethod(**); }", this))
                count shouldBe 3
            }
        }

        "Then *** should visit all methods" {
            with(MemberCounter()) {
                programClassPool.accept(createMethodVisitor("-keep class ClassWithFields { void myMethod(***); }", this))
                count shouldBe 8
            }
        }

        "Then % should only visit primitive, non-array methods" {
            with(MemberCounter()) {
                programClassPool.accept(createMethodVisitor("-keep class ClassWithFields { void myMethod(%); }", this))
                count shouldBe 2
            }
        }
    }
})
