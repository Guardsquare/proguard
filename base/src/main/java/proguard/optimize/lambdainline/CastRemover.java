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
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.NameAndTypeConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A general cast remover class used for removing the casts before and after a bridge method invoke call. It has a
 * keepList which is a list of argument indexes for which it should keep the cast. The argIndex indicates the current
 * argument it is processing.
 */
public class CastRemover implements InstructionVisitor {
    private final CodeAttributeEditor codeAttributeEditor;
    private final List<Integer> keepList;
    private final int argIndex;

    // The names of all methods taking a boxed type variable and returning the variable with the unboxed type
    private final Set<String> castingMethodNames = new HashSet<>(Arrays.asList("intValue", "booleanValue", "byteValue", "shortValue", "longValue", "floatValue", "doubleValue", "charValue"));

    public CastRemover(CodeAttributeEditor codeAttributeEditor, List<Integer> keepList, int argIndex) {
        this.codeAttributeEditor = codeAttributeEditor;
        this.argIndex = argIndex;
        this.keepList = keepList;
    }

    public CastRemover(CodeAttributeEditor codeAttributeEditor) {
        this(codeAttributeEditor, new ArrayList<>(), -1);
    }

    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
        /* Casting on a lambda invoke call looks like this :
         * iload
         * invokestatic valueOf
         * invokeinterface invoke   //lambda call
         * checkast
         * invokevirtual intValue
         * istore
         *
         * We remove valueOf, checkast and intValue
         */
        if (constantInstruction.opcode == Instruction.OP_CHECKCAST) {
            codeAttributeEditor.deleteInstruction(offset);
        } else if (constantInstruction.opcode == Instruction.OP_INVOKESTATIC) {
            if (getInvokedMethodName(clazz, constantInstruction).equals("valueOf")) {
                // Don't remove valueOf call when the lambda takes an object as argument
                if (!keepList.contains(argIndex)) {
                    codeAttributeEditor.deleteInstruction(offset);
                }
            }
        } else if (constantInstruction.opcode == Instruction.OP_INVOKEVIRTUAL) {
            if (castingMethodNames.contains(getInvokedMethodName(clazz, constantInstruction))) {
                codeAttributeEditor.deleteInstruction(offset);
            }
        }
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}

    private String getInvokedMethodName(Clazz clazz, ConstantInstruction constantInstruction) {
        final String[] invokedMethodName = new String[1];
        clazz.constantPoolEntryAccept(constantInstruction.constantIndex, new ConstantVisitor() {
            @Override public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant) {
                clazz.constantPoolEntryAccept(anyMethodrefConstant.u2nameAndTypeIndex, new ConstantVisitor() {
                    @Override
                    public void visitNameAndTypeConstant(Clazz clazz, NameAndTypeConstant nameAndTypeConstant) {
                        invokedMethodName[0] = nameAndTypeConstant.getName(clazz);
                    }
                });
            }
        });
        return invokedMethodName[0];
    }
}
