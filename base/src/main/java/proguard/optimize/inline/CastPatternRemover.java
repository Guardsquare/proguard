package proguard.optimize.inline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.InstructionSequenceMatcher;
import proguard.classfile.visitor.ClassPrinter;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class removes the casting at the end of a lambda invoke method, it does this through pattern matching. It
 * replaces the pattern invokestatic valueOf, return with just return.
 */
public class CastPatternRemover implements InstructionVisitor {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final InstructionSequenceMatcher insSeqMatcher;
    private final CodeAttributeEditor codeAttributeEditor;

    public CastPatternRemover(CodeAttributeEditor codeAttributeEditor) {
        InstructionSequenceBuilder ____ = new InstructionSequenceBuilder();
        Constant[] constants = ____.constants();
        Instruction[] pattern = ____.invokestatic(InstructionSequenceMatcher.X).areturn().__();
        this.insSeqMatcher = new InstructionSequenceMatcher(constants, pattern);
        this.codeAttributeEditor = codeAttributeEditor;
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {
        instruction.accept(clazz, method, codeAttribute, offset, insSeqMatcher);
        if (insSeqMatcher.isMatching()) {
            int constantIndex = insSeqMatcher.matchedConstantIndex(InstructionSequenceMatcher.X);
            clazz.constantPoolEntryAccept(constantIndex, new ConstantVisitor() {
                @Override
                public void visitMethodrefConstant(Clazz clazz, MethodrefConstant methodrefConstant) {
                    if (methodrefConstant.getName(clazz).equals("valueOf")) {
                        if (logger.isDebugEnabled()) {
                            StringWriter stringWriter = new StringWriter();
                            instruction.accept(clazz, method, codeAttribute, offset, new ClassPrinter(new PrintWriter(stringWriter)));
                            logger.debug("Removing " + stringWriter);
                        }

                        codeAttributeEditor.deleteInstruction(insSeqMatcher.matchedInstructionOffset(0));
                    }
                }
            });
        }
    }
}
