package proguard.optimize.kotlin

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import proguard.classfile.AccessConstants
import proguard.classfile.ClassConstants
import proguard.classfile.ClassPool
import proguard.classfile.VersionConstants
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.editor.ClassBuilder
import proguard.classfile.editor.InstructionSequenceBuilder
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.util.InstructionSequenceMatcher
import testutils.MatchDetector

class KotlinLambdaGroupInvokeMethodBuilderTest : FreeSpec({

    val arity = 0
    "Given an invoke method builder, a target class and a method" - {
        val classBuilder = ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            "TargetClass",
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        val method = classBuilder.addAndReturnMethod(AccessConstants.PUBLIC,
            ClassConstants.METHOD_NAME_INIT,
            ClassConstants.METHOD_TYPE_INIT)
        val targetClass = classBuilder.programClass
        val invokeMethodBuilder = KotlinLambdaGroupInvokeMethodBuilder(arity, classBuilder, ClassPool(), ClassPool())
        "When a call to the method is added to the invoke method" - {
            invokeMethodBuilder.addCallTo(method)
            "Then the invoke method contains a call to the method" {
                val invokeMethod = invokeMethodBuilder.build()
                val matchingSequenceBuilder = InstructionSequenceBuilder().invokevirtual(targetClass, method)
                val matchDetector = MatchDetector(InstructionSequenceMatcher(matchingSequenceBuilder.constants(),
                                                                             matchingSequenceBuilder.instructions()))
                invokeMethod.accept(targetClass,
                                    AllAttributeVisitor(
                                    AllInstructionVisitor(
                                    matchDetector)))
                matchDetector.matchIsFound shouldBe true
            }
        }

        "When the invoke method is built" - {
            val invokeMethod = invokeMethodBuilder.build()
            targetClass.methods.forEach {
                println("$it: ${it.getName(targetClass)}${it.getDescriptor(targetClass)}")
            }
            "Then the returned invoke method is not null" {
                invokeMethod shouldNotBe null
            }
            "Then the target class contains an invoke method with the correct descriptor" {
                targetClass.findMethod("invoke", "()Ljava/lang/Object;") shouldNotBe null
            }
            "Then the target class contains the returned invoke method" {
                targetClass.findMethod("invoke", "()Ljava/lang/Object;") shouldBe invokeMethod
            }
        }
    }
}) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf
}