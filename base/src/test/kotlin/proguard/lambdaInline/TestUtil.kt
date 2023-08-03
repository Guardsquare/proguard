import proguard.optimize.inline.lambda_locator.Util
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import proguard.optimize.inline.LambdaInliner
import proguard.AppView
import proguard.classfile.ClassPool
import proguard.classfile.Clazz
import proguard.classfile.Method
import proguard.classfile.attribute.CodeAttribute
import proguard.classfile.attribute.visitor.AllAttributeVisitor
import proguard.classfile.constant.*
import proguard.classfile.constant.visitor.ConstantVisitor
import proguard.classfile.instruction.ConstantInstruction
import proguard.classfile.instruction.Instruction
import proguard.classfile.instruction.visitor.AllInstructionVisitor
import proguard.classfile.instruction.visitor.InstructionVisitor
import proguard.classfile.util.ClassReferenceInitializer
import proguard.classfile.visitor.AllMethodVisitor
import proguard.classfile.visitor.ClassPoolFiller
import proguard.classfile.visitor.MultiClassVisitor
import proguard.evaluation.BasicInvocationUnit
import proguard.evaluation.PartialEvaluator
import proguard.evaluation.value.TypedReferenceValueFactory
import proguard.io.util.IOUtil
import proguard.optimize.info.ProgramClassOptimizationInfoSetter
import proguard.optimize.info.ProgramMemberOptimizationInfoSetter
import proguard.optimize.peephole.LineNumberLinearizer
import proguard.preverify.CodePreverifier
import proguard.testutils.ClassPoolBuilder
import proguard.testutils.PartialEvaluatorUtil
import proguard.testutils.TestSource
import java.util.regex.Pattern
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

fun compareOutputAndMainInstructions(code: TestSource, correctInstructions: List<String>, checkInvokeRemoved: Boolean=true): ClassPool {
    val mydata = System.getProperty("java.class.path")
    val pattern = Pattern.compile(System.getProperty("user.home") + "/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib/(.*?).jar")
    val matcher = pattern.matcher(mydata)
    matcher.find()
    val libraryClassPool = Util.loadJar(matcher.group(0))
    val (classPool, _) = ClassPoolBuilder.fromSource(code)

    libraryClassPool.classesAccept(ClassPoolFiller(classPool))
    IOUtil.writeJar(classPool, "test-files/original.jar", "MainKt")

    val optimizationInfoInitializer = MultiClassVisitor(
        ProgramClassOptimizationInfoSetter(),
        AllMethodVisitor(
            ProgramMemberOptimizationInfoSetter()
        )
    )
    classPool.classesAccept(optimizationInfoInitializer)

    val appView = AppView(classPool, libraryClassPool)

    appView.programClassPool.classesAccept(
        ClassReferenceInitializer(appView.programClassPool, appView.libraryClassPool)
    )

    val inliner = LambdaInliner("Main**")
    inliner.execute(appView)

    val linearizer = LineNumberLinearizer()
    linearizer.execute(appView)

    classPool.classesAccept("Main**", AllMethodVisitor(AllAttributeVisitor(CodePreverifier(false))))

    IOUtil.writeJar(classPool, "test-files/result.jar", "MainKt")

    val valueFactory = TypedReferenceValueFactory()
    val invocationUnit = BasicInvocationUnit(valueFactory)
    val partialEvaluator = PartialEvaluator(valueFactory, invocationUnit, true)
    //test
    val (instructions, _) = PartialEvaluatorUtil.evaluate(
        "MainKt",
        "main",
        "()V",
        classPool,
        partialEvaluator
    )

    val procOriginal = ProcessBuilder("java", "-jar", "test-files/original.jar")
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    val resultOriginal = procOriginal.waitFor()

    val procGenerated = ProcessBuilder("java", "-jar", "test-files/result.jar")
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    val result = procGenerated.waitFor()

    //clean up
    ProcessBuilder("rm", "-f", "test-files/original.jar")
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor()
    ProcessBuilder("rm", "-f", "test-files/result.jar")
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor()

    val generatedErrorString = procGenerated.errorStream.bufferedReader().readText()
    if (generatedErrorString.isNotEmpty()) {
        println("Error output from running jar:")
        println(generatedErrorString)
    }
    withClue("Exit code should be the same!") {
        result shouldBe resultOriginal
    }
    procGenerated.inputStream.bufferedReader().readText() shouldBe procOriginal.inputStream.bufferedReader().readText()
    generatedErrorString shouldBe procOriginal.errorStream.bufferedReader().readText()

    withClue({ "Instructions: $instructions\nExpected instructions: $correctInstructions" }) {
        instructions.size shouldBe correctInstructions.size
    }

    val lambdaMethods = mutableSetOf<String>("main")


    val c: Clazz = classPool.getClass("MainKt")
    var i = 0
    while (i < correctInstructions.size) {
        instructions[i].second.name shouldBe correctInstructions[i]

        //find all functions called in main method
        if (instructions[i].second.opcode == Instruction.OP_INVOKESTATIC || instructions[i].second.opcode == Instruction.OP_INVOKEVIRTUAL) {
            c.constantPoolEntryAccept(((instructions[i].second) as ConstantInstruction).constantIndex, object: ConstantVisitor {
                override fun visitAnyConstant(clazz: Clazz?, constant: Constant?) {}
                override fun visitMethodrefConstant(clazz: Clazz?, methodrefConstant: MethodrefConstant?) {
                    methodrefConstant?.nameAndTypeIndex?.let {
                        clazz?.constantPoolEntryAccept(it, object: ConstantVisitor {
                            override fun visitAnyConstant(clazz: Clazz?, constant: Constant?) {}
                            override fun visitNameAndTypeConstant(clazz: Clazz?, nameAndTypeConstant: NameAndTypeConstant?) {
                                if (nameAndTypeConstant != null) {
                                    clazz?.constantPoolEntryAccept(nameAndTypeConstant.u2nameIndex, object: ConstantVisitor {
                                        override fun visitAnyConstant(clazz: Clazz?, constant: Constant?) {}
                                        override fun visitUtf8Constant(clazz: Clazz?, utf8Constant: Utf8Constant?) {
                                            if (utf8Constant != null) {
                                                lambdaMethods.add(utf8Constant.string)
                                            }
                                        }
                                    })
                                }
                            }
                        })
                    }
                }
            })
        }
        i++
    }

    if (checkInvokeRemoved) {
        classPool.classesAccept("MainKt", AllMethodVisitor(AllAttributeVisitor(AllInstructionVisitor(object :
            InstructionVisitor {
            override fun visitAnyInstruction(
                clazz: Clazz,
                method: Method,
                codeAttribute: CodeAttribute,
                offset: Int,
                instruction: Instruction
            ) {
            }

            override fun visitConstantInstruction(
                clazz: Clazz,
                method: Method,
                codeAttribute: CodeAttribute,
                offset: Int,
                constantInstruction: ConstantInstruction
            ) {
                //only check functions that are called in main method
                if (constantInstruction.opcode == Instruction.OP_INVOKEINTERFACE && lambdaMethods.contains(method.getName(clazz))) {
                    clazz.constantPoolEntryAccept(constantInstruction.constantIndex, object : ConstantVisitor {
                        override fun visitAnyConstant(clazz: Clazz, constant: Constant) {}

                        override fun visitInterfaceMethodrefConstant(
                            clazz: Clazz,
                            interfaceMethodrefConstant: InterfaceMethodrefConstant
                        ) {
                            if (interfaceMethodrefConstant.referencedClass != null) {
                                withClue("There should be no more invoke usage after inlining.") {
                                    interfaceMethodrefConstant.referencedClass.name.startsWith("kotlin/jvm/functions/Function") shouldBe false
                                }
                            }
                        }
                    })
                }
            }
        }))))
    }

    return classPool
}

fun onlyTestRunning(code: TestSource) {
    val mydata = System.getProperty("java.class.path")
    val pattern = Pattern.compile(System.getProperty("user.home") + "/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib/(.*?).jar")
    val matcher = pattern.matcher(mydata)
    matcher.find()
    val libraryClassPool = Util.loadJar(matcher.group(0))
    val (classPool, _) = ClassPoolBuilder.fromSource(code)

    libraryClassPool.classesAccept(ClassPoolFiller(classPool))
    IOUtil.writeJar(classPool, "test-files/original.jar", "MainKt")

    val optimizationInfoInitializer = MultiClassVisitor(
        ProgramClassOptimizationInfoSetter(),
        AllMethodVisitor(
            ProgramMemberOptimizationInfoSetter()
        )
    )
    classPool.classesAccept(optimizationInfoInitializer)

    val appView = AppView(classPool, libraryClassPool)

    appView.programClassPool.classesAccept(
        ClassReferenceInitializer(appView.programClassPool, appView.libraryClassPool)
    )

    val inliner = LambdaInliner("Main**")
    inliner.execute(appView)

    val linearizer = LineNumberLinearizer()
    linearizer.execute(appView)

    classPool.classesAccept("Main**", AllMethodVisitor(AllAttributeVisitor(CodePreverifier(false))))

    IOUtil.writeJar(classPool, "test-files/result.jar", "MainKt")

    val valueFactory = TypedReferenceValueFactory()
    val invocationUnit = BasicInvocationUnit(valueFactory)
    val partialEvaluator = PartialEvaluator(valueFactory, invocationUnit, true)
    //test
    val (instructions, _) = PartialEvaluatorUtil.evaluate(
        "MainKt",
        "main",
        "()V",
        classPool,
        partialEvaluator
    )

    val procOriginal = ProcessBuilder("java", "-jar", "test-files/original.jar")
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    val resultOriginal = procOriginal.waitFor()

    val procGenerated = ProcessBuilder("java", "-jar", "test-files/result.jar")
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    val result = procGenerated.waitFor()

    //clean up
    ProcessBuilder("rm", "-f", "test-files/original.jar")
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor()
    ProcessBuilder("rm", "-f", "test-files/result.jar")
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor()

    val generatedErrorString = procGenerated.errorStream.bufferedReader().readText()
    if (generatedErrorString.isNotEmpty()) {
        println("Error output from running jar:")
        println(generatedErrorString)
    }
    withClue("Exit code should be the same!") {
        result shouldBe resultOriginal
    }
    procGenerated.inputStream.bufferedReader().readText() shouldBe procOriginal.inputStream.bufferedReader().readText()
    generatedErrorString shouldBe procOriginal.errorStream.bufferedReader().readText()
}

fun testPerf(code: TestSource, inlineCode: TestSource, cleanUp: Boolean): Triple<MutableList<Long>, MutableList<Long>, MutableList<Long>> {
    val mydata = System.getProperty("java.class.path")
    val pattern = Pattern.compile(System.getProperty("user.home") + "/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib/(.*?).jar")
    val matcher = pattern.matcher(mydata)
    matcher.find()
    val libraryClassPool = Util.loadJar(matcher.group(0))
    val (classPool, _) = ClassPoolBuilder.fromSource(code)
    val (inlinedClassPool, _) = ClassPoolBuilder.fromSource(inlineCode)

    libraryClassPool.classesAccept(ClassPoolFiller(classPool))
    libraryClassPool.classesAccept(ClassPoolFiller(inlinedClassPool))
    IOUtil.writeJar(classPool, "test-files/original.jar", "MainKt")
    IOUtil.writeJar(inlinedClassPool, "test-files/originalInlined.jar", "MainKt")

    val optimizationInfoInitializer = MultiClassVisitor(
        ProgramClassOptimizationInfoSetter(),
        AllMethodVisitor(
            ProgramMemberOptimizationInfoSetter()
        )
    )
    classPool.classesAccept(optimizationInfoInitializer)

    val inliner = LambdaInliner("Main**")
    val appView = AppView(classPool, libraryClassPool)
    inliner.execute(appView)

    val linearizer = LineNumberLinearizer()
    linearizer.execute(appView)

    classPool.classesAccept("Main**", AllMethodVisitor(AllAttributeVisitor(CodePreverifier(false))))

    IOUtil.writeJar(classPool, "test-files/result.jar", "MainKt")

    //test
    val originalTimes = mutableListOf<Long>()
    var i = 0
    while (i < 10000) {
        i++
        originalTimes.add(measureTimeMillis {
            ProcessBuilder("java", "-jar", "test-files/original.jar")
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
                .waitFor()
        })
    }

    val originalInlinedTimes = mutableListOf<Long>()
    i = 0
    while (i < 10000) {
        i++
        originalInlinedTimes.add(measureTimeMillis {
            ProcessBuilder("java", "-jar", "test-files/originalInlined.jar")
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
                .waitFor()
        })
    }

    val generatedTimes = mutableListOf<Long>()
    i = 0
    while (i < 10000) {
        i++
        generatedTimes.add(measureTimeMillis {
            ProcessBuilder("java", "-jar", "test-files/result.jar")
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
                .waitFor()
        })
    }


    //clean up
    if (cleanUp) {
        ProcessBuilder("rm", "-f", "test-files/original.jar")
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor()
        ProcessBuilder("rm", "-f", "test-files/originalInlined.jar")
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor()
        ProcessBuilder("rm", "-f", "test-files/result.jar")
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor()
    }

    val originalMean = originalTimes.average()
    val originalInlinedMean = originalInlinedTimes.average()
    val generatedMean = generatedTimes.average()
    println("Original code : ${originalMean}ms  (stdev = ${stdev(originalTimes)})")
    println("Original inlined code : ${originalInlinedMean}ms  (stdev = ${stdev(originalInlinedTimes)})")
    println("Generated code : ${generatedMean}ms  (stdev = ${stdev(generatedTimes)})")
    if (originalMean < generatedMean) {
        println("Generated is slower by ${generatedMean - originalMean}ms")
    } else {
        println("Original is slower by ${originalMean - generatedMean}ms")
    }


    println("ORIGINAL : ")
    println(originalTimes)
    println("ORIGINAL INLINED : ")
    println(originalInlinedTimes)
    println("GENERATED : ")
    println(generatedTimes)
    return Triple(originalTimes, originalInlinedTimes, generatedTimes)
}

fun stdev(numArray: MutableList<Long>): Double {
    var sum = 0.0
    var standardDeviation = 0.0

    for (num in numArray) {
        sum += num
    }

    val mean = sum / numArray.size

    for (num in numArray) {
        standardDeviation += (num - mean).pow(2.0)
    }

    return sqrt(standardDeviation / numArray.size)
}
