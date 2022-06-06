package testutils

import proguard.classfile.Clazz
import proguard.classfile.Method
import proguard.classfile.attribute.CodeAttribute
import proguard.classfile.constant.Constant
import proguard.classfile.constant.visitor.ConstantVisitor
import proguard.classfile.editor.InstructionSequenceBuilder
import proguard.classfile.instruction.ConstantInstruction
import proguard.classfile.instruction.Instruction
import proguard.classfile.instruction.visitor.InstructionVisitor

class InstructionSequenceCollector(val builder: InstructionSequenceBuilder) : InstructionVisitor {
    override fun visitAnyInstruction(
        clazz: Clazz,
        method: Method,
        codeAttribute: CodeAttribute,
        offset: Int,
        instruction: Instruction
    ) {
        builder.appendInstruction(instruction)
    }
    override fun visitConstantInstruction(
        clazz: Clazz?,
        method: Method?,
        codeAttribute: CodeAttribute?,
        offset: Int,
        constantInstruction: ConstantInstruction?
    ) {
        val constantPoolEditor = builder.constantPoolEditor
        var constantIndex = 0
        assert(constantInstruction != null)
        assert(constantInstruction!!.constantIndex > 0)
        clazz?.constantPoolEntryAccept(
            constantInstruction.constantIndex,
            object : ConstantVisitor {
                override fun visitAnyConstant(clazz: Clazz?, constant: Constant?) {
                    constantIndex = constantPoolEditor.addConstant(constant)
                }
            }
        )
        assert(constantIndex == 0)
        val newConstantInstruction = ConstantInstruction().copy(constantInstruction)
        newConstantInstruction.constantIndex = constantIndex
        visitAnyInstruction(clazz!!, method!!, codeAttribute!!, offset, newConstantInstruction)
    }
}
