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
