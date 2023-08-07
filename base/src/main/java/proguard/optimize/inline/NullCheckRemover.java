package proguard.optimize.inline;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.InstructionFactory;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.InstructionSequenceMatcher;

class NullCheckRemover implements InstructionVisitor {
    private final InstructionSequenceMatcher insSeqMatcher;
    private final int X = InstructionSequenceMatcher.X;
    private final int Y = InstructionSequenceMatcher.Y;
    private final int C = InstructionSequenceMatcher.C;
    private final Instruction[] pattern;
    Constant[] constants;
    private final int argumentIndex;
    private final InstructionVisitor extraInstructionVisitor;

    public NullCheckRemover(int argumentIndex, InstructionVisitor extraInstructionVisitor) {
        InstructionSequenceBuilder ____ =
                new InstructionSequenceBuilder();

        pattern = ____.aload(X)
            .ldc_(C)
            .invokestatic(Y).__();

        constants = ____.constants();

        this.insSeqMatcher = new InstructionSequenceMatcher(constants, pattern);
        this.argumentIndex = argumentIndex;
        this.extraInstructionVisitor = extraInstructionVisitor;
    }

    public NullCheckRemover(int argumentIndex) {
        this(argumentIndex, null);
    }

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {
        instruction.accept(clazz, method, codeAttribute, offset, insSeqMatcher);
        if (insSeqMatcher.isMatching() && insSeqMatcher.matchedConstantIndex(X) == argumentIndex) {

            insSeqMatcher.matchedConstantIndex(Y);
            clazz.constantPoolEntryAccept(insSeqMatcher.matchedArgument(Y), new ConstantVisitor() {
                @Override
                public void visitMethodrefConstant(Clazz clazz, MethodrefConstant methodrefConstant) {
                    // Check if the called function matches kotlin.jvm.internal.Intrinsics#void checkNotNullParameter(java.lang.Object,java.lang.String)
                    if (
                        methodrefConstant.getClassName(clazz).equals("kotlin/jvm/internal/Intrinsics") &&
                        methodrefConstant.getName(clazz).equals("checkNotNullParameter") &&
                        methodrefConstant.getType(clazz).equals("(Ljava/lang/Object;Ljava/lang/String;)V")
                    ) {
                        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
                        codeAttributeEditor.reset(codeAttribute.u4codeLength);
                        for (int insIndex = 0; insIndex < pattern.length; insIndex++) {
                            int insOffset = insSeqMatcher.matchedInstructionOffset(insIndex);
                            if (extraInstructionVisitor != null)
                                extraInstructionVisitor.visitAnyInstruction(clazz, method, codeAttribute, insSeqMatcher.matchedInstructionOffset(insIndex), InstructionFactory.create(codeAttribute.code, insOffset));
                            codeAttributeEditor.deleteInstruction(insOffset);
                        }
                        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
                    }
                }
            });
        }
    }
}
