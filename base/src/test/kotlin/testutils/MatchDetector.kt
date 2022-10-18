package testutils

import proguard.classfile.Clazz
import proguard.classfile.Method
import proguard.classfile.attribute.CodeAttribute
import proguard.classfile.instruction.Instruction
import proguard.classfile.instruction.visitor.InstructionVisitor
import proguard.classfile.util.InstructionSequenceMatcher

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
