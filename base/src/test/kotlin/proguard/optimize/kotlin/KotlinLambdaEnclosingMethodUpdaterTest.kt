package proguard.optimize.kotlin

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import proguard.classfile.AccessConstants
import proguard.classfile.ClassConstants
import proguard.classfile.Clazz
import proguard.classfile.Method
import proguard.classfile.ProgramClass
import proguard.classfile.ProgramMethod
import proguard.classfile.VersionConstants
import proguard.classfile.attribute.Attribute
import proguard.classfile.attribute.CodeAttribute
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.attribute.visitor.AttributeVisitor
import proguard.classfile.attribute.visitor.MultiAttributeVisitor
import proguard.classfile.constant.AnyMethodrefConstant
import proguard.classfile.constant.ClassConstant
import proguard.classfile.constant.Constant
import proguard.classfile.constant.visitor.ConstantVisitor
import proguard.classfile.editor.ClassBuilder
import proguard.classfile.editor.InstructionSequenceBuilder
import proguard.classfile.instruction.ConstantInstruction
import proguard.classfile.instruction.Instruction
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.instruction.visitor.InstructionVisitor
import proguard.classfile.util.InstructionSequenceMatcher
import proguard.classfile.visitor.AllMemberVisitor
import proguard.io.ExtraDataEntryNameMap
import testutils.ClassPoolBuilder
import testutils.KotlinSource
import testutils.MatchDetector

class KotlinLambdaEnclosingMethodUpdaterTest : FreeSpec({
    val (programClassPool, libraryClassPool) = ClassPoolBuilder.fromSource(
        KotlinSource(
            "Test.kt",
            """
            fun main() {
                val lambda1 = { println("Lambda1") }
                val lambda2 = { println("Lambda2") }
                val lambda3 = { println("Lambda3") }
                lambda1()
                lambda2()
                lambda3()
            }
            """.trimIndent()
        )
    )
    "Given a lambda class, a lambda group and an enclosing method updater" - {
        val lambdaClass = programClassPool.getClass("TestKt\$main\$lambda1\$1") as ProgramClass
        val enclosingClass = programClassPool.getClass("TestKt") as ProgramClass
        println("Lambda class: $lambdaClass")
        val lambdaGroupName = "LambdaGroup"
        val lambdaGroup = ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.FINAL or AccessConstants.SUPER,
            lambdaGroupName,
            KotlinLambdaMerger.NAME_KOTLIN_LAMBDA
        ).programClass
        val initMethod = ClassBuilder(lambdaGroup).addAndReturnMethod(
            AccessConstants.PUBLIC,
            ClassConstants.METHOD_NAME_INIT,
            "(II)V"
        )
        val classId = 0
        val arity = 1
        val constructorDescriptor = "(II)V"
        val nameMapper = ExtraDataEntryNameMap()
        val enclosingMethodUpdater = KotlinLambdaEnclosingMethodUpdater(
            programClassPool,
            libraryClassPool,
            lambdaClass,
            lambdaGroup,
            classId,
            arity,
            constructorDescriptor,
            nameMapper
        )
        "When the enclosing method of the lambda class is updated" - {
            lambdaClass.accept(
                AllAttributeVisitor(
                    enclosingMethodUpdater
                )
            )
            val referencedClasses = ArrayList<Clazz>()
            val referencedClassNames = ArrayList<String>()
            enclosingClass.constantPoolEntriesAccept(object : ConstantVisitor {
                override fun visitAnyConstant(clazz: Clazz?, constant: Constant?) {}
                override fun visitClassConstant(clazz: Clazz?, classConstant: ClassConstant?) {
                    println("$clazz contains a ClassConstant referencing ${classConstant!!.referencedClass}")
                    referencedClasses.add(classConstant.referencedClass)
                    referencedClassNames.add(classConstant.getName(clazz))
                }
            })
            "Then the enclosing class no longer references the lambda class" {
                referencedClasses shouldNotContain lambdaClass
                referencedClassNames shouldNotContain lambdaClass.name
            }
            "Then the enclosing class references the lambda group" {
                referencedClassNames shouldContain lambdaGroupName
            }
            "Then the enclosing method initialises the lambda group with the correct arity and class id" {
                val instructionSequenceBuilder = InstructionSequenceBuilder(programClassPool, libraryClassPool)
                instructionSequenceBuilder
                    .new_(lambdaGroup)
                    .dup()
                    .iconst(classId)
                    .iconst(arity)
                    .invokespecial(lambdaGroup, initMethod)
                val matchDetector = MatchDetector(
                    InstructionSequenceMatcher(instructionSequenceBuilder.constants(), instructionSequenceBuilder.instructions())
                )
                enclosingClass.accept(
                    AllMemberVisitor(
                        AllAttributeVisitor(
                            AllInstructionVisitor(
                                matchDetector
                            )
                        )
                    )
                )
                matchDetector.matchIsFound shouldBe true
            }
        }
        "When a method is visited that does not use the lambda class" - {
            val nonEnclosingMethod = enclosingClass.findMethod("main", "([Ljava/lang/String;)V") as ProgramMethod
            val (originalInstructions, originalAttributes) = nonEnclosingMethod.getInstructionsAndAttributes(enclosingClass)
            nonEnclosingMethod.accept(enclosingClass, enclosingMethodUpdater)
            "Then the enclosing method updater does not modify this method" {
                val (resultingInstructions, resultingAttributes) = nonEnclosingMethod.getInstructionsAndAttributes(enclosingClass)
                resultingAttributes.forEach { originalAttributes shouldContain it }
                originalAttributes.forEach { resultingAttributes shouldContain it }
                resultingInstructions.forEach { originalInstructions shouldContain it }
                originalInstructions.forEach { resultingInstructions shouldContain it }
            }
            "Then none of the instructions of this method references a method of the lambda group" {
                nonEnclosingMethod.accept(
                    enclosingClass,
                    AllAttributeVisitor(
                        AllInstructionVisitor(
                            object : InstructionVisitor {
                                override fun visitAnyInstruction(
                                    clazz: Clazz?,
                                    method: Method?,
                                    codeAttribute: CodeAttribute?,
                                    offset: Int,
                                    instruction: Instruction?
                                ) {}
                                override fun visitConstantInstruction(
                                    clazz: Clazz?,
                                    method: Method?,
                                    codeAttribute: CodeAttribute?,
                                    offset: Int,
                                    constantInstruction: ConstantInstruction?
                                ) {
                                    enclosingClass.constantPoolEntryAccept(
                                        constantInstruction!!.constantIndex,
                                        object : ConstantVisitor {
                                            override fun visitAnyConstant(clazz: Clazz?, constant: Constant?) {}
                                            override fun visitAnyMethodrefConstant(
                                                clazz: Clazz?,
                                                anyMethodrefConstant: AnyMethodrefConstant?
                                            ) {
                                                anyMethodrefConstant?.referencedClass shouldNotBe lambdaGroup
                                            }
                                        }
                                    )
                                }
                            }
                        )
                    )
                )
            }
        }
    }
}) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf
}

fun ProgramMethod.getInstructionsAndAttributes(programClass: ProgramClass): Pair<List<Instruction>, List<Attribute>> {
    val instructions = ArrayList<Instruction>()
    val attributes = ArrayList<Attribute>()
    this.accept(
        programClass,
        AllAttributeVisitor(
            MultiAttributeVisitor(
                object : AttributeVisitor {
                    override fun visitAnyAttribute(clazz: Clazz?, attribute: Attribute?) {
                        attributes.add(attribute!!)
                    }
                },
                AllInstructionVisitor(object : InstructionVisitor {
                    override fun visitAnyInstruction(
                        clazz: Clazz?,
                        method: Method?,
                        codeAttribute: CodeAttribute?,
                        offset: Int,
                        instruction: Instruction?
                    ) {
                        instructions.add(instruction!!)
                    }
                })
            )
        )
    )
    return Pair(instructions, attributes)
}
