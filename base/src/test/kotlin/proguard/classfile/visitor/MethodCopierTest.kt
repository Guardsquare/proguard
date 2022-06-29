package proguard.classfile.visitor

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import proguard.classfile.AccessConstants
import proguard.classfile.ClassConstants
import proguard.classfile.VersionConstants
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.editor.ClassBuilder
import proguard.classfile.editor.InstructionSequenceBuilder
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.util.InstructionSequenceMatcher
import testutils.MatchDetector

class MethodCopierTest : FreeSpec({
    "Given a method and a target class" - {
        val classBuilder = ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.FINAL or AccessConstants.SUPER,
            "Test",
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        val testClass = classBuilder.programClass
        val method = classBuilder.addAndReturnMethod(
            AccessConstants.PUBLIC,
            ClassConstants.METHOD_NAME_INIT,
            ClassConstants.METHOD_TYPE_INIT,
            50
        ) {
            it
                .aload_0()
                .iconst(0)
                .invokespecial(
                    ClassConstants.NAME_JAVA_LANG_OBJECT,
                    ClassConstants.METHOD_NAME_INIT,
                    ClassConstants.METHOD_TYPE_INIT
                )
                .return_()
        }
        val methodDescriptor = method.getDescriptor(testClass)
        val methodName = method.getName(testClass)
        val methodAccessFlags = AccessConstants.PUBLIC
        val targetClass = ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            "TargetClass",
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).programClass
        "When the method is copied to the target class" - {
            val methodCopier = MethodCopier(targetClass, ClassConstants.METHOD_NAME_INIT, methodAccessFlags)
            method.accept(testClass, methodCopier)
            "Then the target class contains a method with the correct name and descriptor" {
                targetClass.findMethod(methodName, methodDescriptor) shouldNotBe null
            }

            "Then the copied method has the correct access modifiers" {
                val copiedMethod = targetClass.findMethod(methodName, methodDescriptor)
                copiedMethod.accessFlags shouldBe methodAccessFlags
            }

            "Then the instructions of the copied method match those of the original method" {
                val builder = InstructionSequenceBuilder()
                builder
                    .aload_0()
                    .iconst(0)
                    .invokespecial(
                        ClassConstants.NAME_JAVA_LANG_OBJECT,
                        ClassConstants.METHOD_NAME_INIT,
                        ClassConstants.METHOD_TYPE_INIT
                    )
                    .return_()
                val matchDetector = MatchDetector(InstructionSequenceMatcher(builder.constants(), builder.instructions()))
                val copiedMethod = targetClass.findMethod(methodName, methodDescriptor)
                copiedMethod.accept(
                    targetClass,
                    AllAttributeVisitor(
                        AllInstructionVisitor(
                            matchDetector
                        )
                    )
                )
                matchDetector.matchIsFound shouldBe true
            }
        }

        "When the method is copied to the target class with a new descriptor" - {
            val newDescriptor = "(I)V"
            val methodCopier = MethodCopier(targetClass, methodName, newDescriptor, methodAccessFlags)
            method.accept(testClass, methodCopier)
            "Then the target class contains a method with the correct name and descriptor" {
                targetClass.findMethod(methodName, newDescriptor) shouldNotBe null
            }
        }

        "When the method is copied to the target class with a new name prefix" - {
            val newNamePrefix = "copiedMethod"
            val methodCopier = MethodCopier(targetClass, newNamePrefix, methodAccessFlags)
            method.accept(testClass, methodCopier)
            "Then the target class contains a method with the correct name and descriptor" {
                targetClass.findMethod(newNamePrefix, methodDescriptor) shouldNotBe null
            }
        }
    }
})
