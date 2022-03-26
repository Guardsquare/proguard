/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard

import io.kotest.core.spec.style.FreeSpec
import io.mockk.spyk
import io.mockk.verify
import proguard.classfile.visitor.ClassVisitor
import testutils.ClassPoolBuilder
import testutils.JavaSource
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
})
