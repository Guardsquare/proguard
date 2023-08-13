/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.optimize.lambdainline;

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

/**
 * This class removes Kotlin null checks that make use of the <code>checkNotNullParameter</code> this is useful for
 * when removing an argument of a method, in that case you also want to remove the null check for that argument.
 * <p>
 * Example bytecode:
 * <pre>{@code
 * [0] aload_0 v0
 * [1] ldc #10 = String("argumentName")
 * [3] invokestatic #16 = Methodref(kotlin/jvm/internal/Intrinsics.checkNotNullParameter(Ljava/lang/Object;Ljava/lang/String;)V)
 * }</pre>
 * If the argumentIndex is set to 0 then these 3 instructions will be removed.
 */
public class NullCheckRemover implements InstructionVisitor {
    private final InstructionSequenceMatcher insSeqMatcher;
    private final int X = InstructionSequenceMatcher.X;
    private final int Y = InstructionSequenceMatcher.Y;
    private final int C = InstructionSequenceMatcher.C;
    private final Instruction[] pattern;
    Constant[] constants;
    private final int argumentIndex;
    private final InstructionVisitor extraInstructionVisitor;
    private final CodeAttributeEditor codeAttributeEditor;

    public NullCheckRemover(int argumentIndex, CodeAttributeEditor codeAttributeEditor, InstructionVisitor extraInstructionVisitor) {
        InstructionSequenceBuilder ____ =
                new InstructionSequenceBuilder();

        pattern =
            ____.aload(X)
                .ldc_(C)
                .invokestatic(Y).__();

        constants = ____.constants();

        this.insSeqMatcher = new InstructionSequenceMatcher(constants, pattern);
        this.argumentIndex = argumentIndex;
        this.extraInstructionVisitor = extraInstructionVisitor;
        this.codeAttributeEditor = codeAttributeEditor;
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
                        (methodrefConstant.getName(clazz).equals("checkNotNullParameter") || methodrefConstant.getName(clazz).equals("checkParameterIsNotNull")) &&
                        methodrefConstant.getType(clazz).equals("(Ljava/lang/Object;Ljava/lang/String;)V")
                    ) {
                        for (int insIndex = 0; insIndex < pattern.length; insIndex++) {
                            int insOffset = insSeqMatcher.matchedInstructionOffset(insIndex);
                            if (extraInstructionVisitor != null)
                                extraInstructionVisitor.visitAnyInstruction(clazz, method, codeAttribute, insSeqMatcher.matchedInstructionOffset(insIndex), InstructionFactory.create(codeAttribute.code, insOffset));
                            codeAttributeEditor.deleteInstruction(insOffset);
                        }
                    }
                }
            });
        }
    }
}
