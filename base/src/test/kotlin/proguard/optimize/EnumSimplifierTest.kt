package proguard.optimize

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import proguard.classfile.ClassPool
import proguard.classfile.ProgramClass
import proguard.classfile.ProgramMember
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.editor.MemberReferenceFixer
import proguard.classfile.util.ClassPoolClassLoader
import proguard.classfile.visitor.AllMemberVisitor
import proguard.classfile.visitor.AllMethodVisitor
import proguard.classfile.visitor.MemberNameFilter
import proguard.classfile.visitor.MemberVisitor
import proguard.optimize.evaluation.SimpleEnumClassSimplifier
import proguard.optimize.evaluation.SimpleEnumDescriptorSimplifier
import proguard.optimize.evaluation.SimpleEnumUseSimplifier
import proguard.optimize.info.ProgramClassOptimizationInfoSetter
import proguard.optimize.info.ProgramMemberOptimizationInfoSetter
import proguard.optimize.info.SimpleEnumFilter
import proguard.optimize.info.SimpleEnumMarker
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.JavaSource
import proguard.testutils.KotlinSource
import proguard.testutils.and
import proguard.testutils.match

class EnumSimplifierTest : FreeSpec({
    "Test simplification of simple Kotlin enum (#349)" {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            KotlinSource(
                "TestEnum.kt", "enum class TestEnum { A, B, C }"
            )
        )

        simplifyEnum(libraryClassPool, programClassPool)
    }

    "Test simplification of simple Java enum" {
        val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
            JavaSource(
                "TestEnum.java", "public enum TestEnum { A, B, C, }"
            )
        )

        simplifyEnum(libraryClassPool, programClassPool)
    }
})

private fun simplifyEnum(libraryClassPool: ClassPool, programClassPool: ClassPool) {
    // Setup the OptimizationInfo on the classes
    val keepMarker = KeepMarker()
    libraryClassPool.classesAccept(keepMarker)
    libraryClassPool.classesAccept(AllMemberVisitor(keepMarker))
    programClassPool.classesAccept(ProgramClassOptimizationInfoSetter())
    programClassPool.classesAccept(AllMemberVisitor(ProgramMemberOptimizationInfoSetter()))

    programClassPool.classAccept("TestEnum", SimpleEnumMarker(true))

    val enumClazz = programClassPool.getClass("TestEnum")

    SimpleEnumMarker.isSimpleEnum(enumClazz) shouldBe true

    // Application of enum simplification similar to the Optimizer.

    // Simplify the use of the enum classes in code.
    programClassPool.classesAccept(
        AllMethodVisitor(
            AllAttributeVisitor(
                SimpleEnumUseSimplifier()
            )
        )
    )

    // Simplify the static initializers of simple enum classes.
    programClassPool.classesAccept(
        SimpleEnumFilter(
            SimpleEnumClassSimplifier()
        )
    )

    // Simplify the use of the enum classes in descriptors.
    programClassPool.classesAccept(
        SimpleEnumDescriptorSimplifier()
    )

    // Update references to class members with simple enum classes.
    programClassPool.classesAccept(MemberReferenceFixer(false))

    // Trigger class loading, that would previously result in a verify error.
    val classPoolClassLoader = ClassPoolClassLoader(programClassPool)
    val clazz = classPoolClassLoader.findClass("TestEnum")
    clazz.fields.single { it.name.startsWith("A") }.get(null) shouldBe 1

    enumClazz.methodsAccept(
        MemberNameFilter(
            "values*",
            object : MemberVisitor {
                override fun visitProgramMember(programClass: ProgramClass, programMember: ProgramMember) {
                    with(programClass and programMember) {
                        match {
                            invokevirtual("[I", "clone", "()Ljava/lang/Object;")
                        } shouldBe true
                    }
                }
            }
        )
    )
}
