package proguard.optimize.kotlin

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import proguard.classfile.AccessConstants
import proguard.classfile.VersionConstants
import proguard.classfile.editor.ClassBuilder
import proguard.optimize.info.ProgramClassOptimizationInfo
import proguard.optimize.info.ProgramClassOptimizationInfoSetter
import java.io.ByteArrayOutputStream
import java.io.PrintWriter

class LambdaGroupMappingPrinterTest : FreeSpec({
    "Given a lambda class and a lambda group" - {
        val arity = 0
        val lambdaClassId = 0
        val lambdaClassName = "LambdaClass"
        val lambdaGroupName = "LambdaGroup"
        val lambdaClass = ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.FINAL,
            lambdaClassName,
            KotlinLambdaMerger.NAME_KOTLIN_LAMBDA
        ).addInterface("${KotlinLambdaMerger.NAME_KOTLIN_FUNCTION}$arity").programClass
        val lambdaGroup = ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.FINAL,
            lambdaGroupName,
            KotlinLambdaMerger.NAME_KOTLIN_LAMBDA
        ).programClass
        lambdaClass.accept(ProgramClassOptimizationInfoSetter())
        val optimizationInfo = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(lambdaClass)
        optimizationInfo.lambdaGroup = lambdaGroup
        optimizationInfo.lambdaGroupClassId = lambdaClassId
        "When the mapping of the lambda class is printed" - {
            val outputStream = ByteArrayOutputStream()
            val mappingPrinter = LambdaGroupMappingPrinter(PrintWriter(outputStream, true))
            lambdaClass.accept(mappingPrinter)
            "Then the correct lambda group, arity and case are printed" {
                val mappingEntry = outputStream.toString()
                val expectedMappingEntry = "$lambdaClassName -> $lambdaGroupName (arity $arity, case $lambdaClassId)${System.lineSeparator()}"
                mappingEntry shouldBe expectedMappingEntry
            }
        }
    }
})
