package proguard.optimize.peephole

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import proguard.classfile.AccessConstants
import proguard.classfile.ClassConstants
import proguard.classfile.ProgramMethod
import proguard.classfile.VersionConstants
import proguard.classfile.attribute.Attribute
import proguard.classfile.attribute.Attribute.LINE_NUMBER_TABLE
import proguard.classfile.attribute.LineNumberInfo
import proguard.classfile.attribute.LineNumberTableAttribute
import proguard.classfile.attribute.ProGuardOrigin
import proguard.classfile.attribute.StructuredLineNumberInfo
import proguard.classfile.editor.AttributesEditor
import proguard.classfile.editor.ClassBuilder
import proguard.testutils.CodeAttributeFinder

class InlinedMethodLineNumberLinearizerTest : BehaviorSpec({

    Given("A method with two levels of inlined line numbers") {
        val clazzBuilder = ClassBuilder(VersionConstants.CLASS_VERSION_18, AccessConstants.PUBLIC, "A", ClassConstants.NAME_JAVA_LANG_OBJECT)

        val clazz = clazzBuilder.programClass
        val method = clazzBuilder.addAndReturnMethod(
            AccessConstants.PUBLIC or AccessConstants.STATIC,
            "a",
            "()V",
            10,
        ) {
            it.return_()
        } as ProgramMethod

        val codeAttribute = CodeAttributeFinder.findCodeAttribute(method)!!

        val blockB = StructuredLineNumberInfo.Block(ProGuardOrigin.INLINED, "B.b()V", 0, 0)
        val blockC = StructuredLineNumberInfo.Block(ProGuardOrigin.INLINED, "C.c()V", 0, 0)

        val lineNumbers = arrayOf(
            LineNumberInfo(0, 1),
            blockB.line(1, MethodInliner.INLINED_METHOD_START_LINE_NUMBER),
            blockB.line(2, 11),
            blockC.line(3, MethodInliner.INLINED_METHOD_START_LINE_NUMBER),
            blockC.line(4, 21),
            blockC.line(5, MethodInliner.INLINED_METHOD_END_LINE_NUMBER),
            blockB.line(6, 12),
            blockB.line(7, MethodInliner.INLINED_METHOD_END_LINE_NUMBER),
            LineNumberInfo(8, 2)
        )

        val lineNumberTableAttribute = LineNumberTableAttribute(clazzBuilder.constantPoolEditor.addUtf8Constant(LINE_NUMBER_TABLE), lineNumbers.size, lineNumbers)
        AttributesEditor(clazz, method, codeAttribute, true).addAttribute(lineNumberTableAttribute)

        When("Linearizing the line numbers") {
            clazz.accept(LineNumberLinearizer())

            Then("The line number table should look correct") {
                val lineNumberTableAttribute = codeAttribute.getAttribute(clazz, Attribute.LINE_NUMBER_TABLE) as LineNumberTableAttribute
                val table = lineNumberTableAttribute.lineNumberTable
                table[0].u2lineNumber shouldBe 1
                table[0].source.shouldBeNull()

                table[1].u2lineNumber.mod(LineNumberLinearizer.SHIFT_ROUNDING) shouldBe 11
                table[1].source shouldBe "B.b()V:0:0"

                table[2].u2lineNumber.mod(LineNumberLinearizer.SHIFT_ROUNDING) shouldBe 21
                table[2].source shouldBe "C.c()V:0:0"

                table[3].u2lineNumber.mod(LineNumberLinearizer.SHIFT_ROUNDING) shouldBe 11
                table[3].source shouldBe "B.b()V:0:0"

                table[4].u2lineNumber.mod(LineNumberLinearizer.SHIFT_ROUNDING) shouldBe 12
                table[4].source shouldBe "B.b()V:0:0"

                table[5].u2lineNumber shouldBe 1
                table[5].source.shouldBeNull()

                table[6].u2lineNumber shouldBe 2
                table[6].source.shouldBeNull()
            }
        }
    }
})
