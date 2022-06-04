package proguard.optimize.kotlin

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import proguard.classfile.*
import proguard.classfile.attribute.CodeAttribute
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.attribute.visitor.MultiAttributeVisitor
import proguard.classfile.editor.ClassBuilder
import proguard.classfile.editor.InstructionSequenceBuilder
import proguard.classfile.instruction.Instruction
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.instruction.visitor.InstructionVisitor
import proguard.classfile.util.ClassUtil
import proguard.classfile.util.InstructionSequenceMatcher

class KotlinLambdaGroupInitUpdaterTest : FreeSpec({
    val lambdaGroupName = "LambdaGroup"
    val classBuilder = ClassBuilder(
        VersionConstants.CLASS_VERSION_1_8,
        AccessConstants.FINAL or AccessConstants.SUPER,
        lambdaGroupName,
        KotlinLambdaMerger.NAME_KOTLIN_LAMBDA
    )
    val initMethod   = classBuilder.addAndReturnMethod(AccessConstants.PUBLIC,
                                                       ClassConstants.METHOD_NAME_INIT,
                                                       ClassConstants.METHOD_TYPE_INIT,
                                                       50) {
        it.aload_0()
          .iconst(0)
          .invokespecial(KotlinLambdaMerger.NAME_KOTLIN_LAMBDA, ClassConstants.METHOD_NAME_INIT, "(I)V")
          .return_()
    }
    val classIdField = classBuilder.addAndReturnField(AccessConstants.PRIVATE, KotlinLambdaGroupBuilder.FIELD_NAME_ID, KotlinLambdaGroupBuilder.FIELD_TYPE_ID)
    val lambdaGroup  = classBuilder.programClass

    "Given a Kotlin Lambda Group Init Updater" - {
        val initUpdater = KotlinLambdaGroupInitUpdater(ClassPool(lambdaGroup), ClassPool())
        "When the updater is applied to the <init> method of a lambda group" - {
            val initMethodDescriptor    = initMethod.getDescriptor(lambdaGroup)
            val argumentCountBefore     = ClassUtil.internalMethodParameterCount(initMethodDescriptor, false)
            val argumentSizeBefore      = ClassUtil.internalMethodParameterSize(initMethodDescriptor, false)
            initMethod.accept(lambdaGroup, initUpdater)
            val newInitMethodDescriptor = initMethod.getDescriptor(lambdaGroup)
            val argumentCountAfter      = ClassUtil.internalMethodParameterCount(newInitMethodDescriptor, false)
            val argumentSizeAfter       = ClassUtil.internalMethodParameterSize(newInitMethodDescriptor, false)
            "Then the init method contains two additional arguments" - {
                argumentCountAfter shouldBe argumentCountBefore + 2
            }
            "Then the two additional arguments are each of size 1 byte" - {
                argumentSizeAfter shouldBe argumentSizeBefore + 2
            }
            "Then the two additional arguments are of type int" - {
                ClassUtil.internalMethodParameterType(newInitMethodDescriptor, argumentCountAfter - 3) shouldBe "I"
                ClassUtil.internalMethodParameterType(newInitMethodDescriptor, argumentCountAfter - 2) shouldBe "I"
            }

            // Find the match in the code and print it out.
            class MatchDetector(val matcher: InstructionSequenceMatcher, vararg val arguments: Int) : InstructionVisitor {
                var matchIsFound = false
                var matchedArguments = IntArray(arguments.size)

                override fun visitAnyInstruction(
                    clazz: Clazz,
                    method: Method,
                    codeAttribute: CodeAttribute,
                    offset: Int,
                    instruction: Instruction
                ) {
                    println(instruction.toString(clazz, offset))
                    instruction.accept(clazz, method, codeAttribute, offset, matcher)
                    if (matcher.isMatching()) {
                        matchIsFound = true
                        matchedArguments = matcher.matchedArguments(arguments)
                    }
                }
            }

            "Then the code of the <init> method calls the super constructor with the arity argument" - {
                val classIdBuilder = InstructionSequenceBuilder(ClassPool(lambdaGroup), ClassPool())
                classIdBuilder
                    .aload_0()
                    .iload(InstructionSequenceMatcher.A)
                    .putfield(lambdaGroup, classIdField)
                val classIdInstructionMatcher = InstructionSequenceMatcher(classIdBuilder.constants(), classIdBuilder.instructions())
                val classIdMatchDetector      = MatchDetector(classIdInstructionMatcher, InstructionSequenceMatcher.A)
                initMethod.accept(lambdaGroup,
                                  AllAttributeVisitor(
                                  AllInstructionVisitor(
                                  classIdMatchDetector)))
                classIdMatchDetector.matchIsFound shouldBe true
                classIdMatchDetector.matchedArguments[0] shouldBe argumentSizeAfter - 2
            }
            "Then the code of the <init> method stores the classId argument in the classId field" - {
                val callBuilder = InstructionSequenceBuilder(ClassPool(lambdaGroup), ClassPool())
                callBuilder
                    .aload_0()
                    .iload(InstructionSequenceMatcher.A)
                    .invokespecial(KotlinLambdaMerger.NAME_KOTLIN_LAMBDA,
                                   ClassConstants.METHOD_NAME_INIT,
                                   "(I)V")
                val callInstructionMatcher = InstructionSequenceMatcher(callBuilder.constants(), callBuilder.instructions())
                val callMatchDetector      = MatchDetector(callInstructionMatcher, InstructionSequenceMatcher.A)
                initMethod.accept(lambdaGroup,
                                  AllAttributeVisitor(
                                  AllInstructionVisitor(
                                  callMatchDetector)))
                callMatchDetector.matchIsFound shouldBe true
                callMatchDetector.matchedArguments[0] shouldBe argumentSizeAfter - 1
            }
        }
    }
})