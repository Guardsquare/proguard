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
package proguard.optimize.peephole;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.ClassPrinter;
import proguard.optimize.info.*;

import java.io.IOException;

/**
 * This InstructionVisitor replaces instance references on classes without
 * constructors in all methods that it visits.
 *
 * @author Joachim Vandersmissen
 */
public class NoConstructorReferenceReplacer
implements   InstructionVisitor,
             ConstantVisitor
{
    private final CodeAttributeEditor codeAttributeEditor;
    private final InstructionVisitor extraReferenceVisitor;

    private boolean containsConstructors;


    /**
     * Creates a new NoConstructorReferenceReplacer.
     * @param codeAttributeEditor a code editor that can be used for
     *                            accumulating changes to the code.
     */
    public NoConstructorReferenceReplacer(CodeAttributeEditor codeAttributeEditor)
    {
        this(codeAttributeEditor, null);
    }


    /**
     * Creates a new GotoGotoReplacer.
     * @param codeAttributeEditor   a code editor that can be used for
     *                              accumulating changes to the code.
     * @param extraReferenceVisitor an optional extra visitor for all replaced
     *                              instance references.
     */
    public NoConstructorReferenceReplacer(CodeAttributeEditor codeAttributeEditor,
                                          InstructionVisitor  extraReferenceVisitor)
    {
        this.codeAttributeEditor   = codeAttributeEditor;
        this.extraReferenceVisitor = extraReferenceVisitor;
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        // Is it a method invocation?
        switch (constantInstruction.opcode)
        {
            case Instruction.OP_INVOKEVIRTUAL:
            case Instruction.OP_INVOKESPECIAL:
            case Instruction.OP_GETFIELD:
            case Instruction.OP_PUTFIELD:
            {
                // Does the referenced class not contain any constructors?
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);

                if (!containsConstructors)
                {
                    // Replace with aconst_null + athrow.
                    codeAttributeEditor.replaceInstruction(offset, new Instruction[]
                    {
                        new SimpleInstruction(Instruction.OP_ACONST_NULL),
                        new SimpleInstruction(Instruction.OP_ATHROW)
                    });

                    // Visit the instruction, if required.
                    if (extraReferenceVisitor != null)
                    {
                        extraReferenceVisitor.visitConstantInstruction(clazz, method, codeAttribute, offset, constantInstruction);
                    }
                }
            }
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant)
    {
    }


    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        Clazz referencedClass = fieldrefConstant.referencedClass;
        containsConstructors =
            // We have to assume an unknown referenced class always has constructors.
            referencedClass == null ||
            ContainsConstructorsMarker.containsConstructors(referencedClass);
    }


    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        Clazz referencedClass = anyMethodrefConstant.referencedClass;
        containsConstructors =
            // We have to assume an unknown referenced class always has constructors.
            referencedClass == null                                                ||
            // Interfaces never have constructors but can still be the target of
            // invokevirtual and invokespecial instructions. We don't want to
            // replace those.
            (referencedClass.getAccessFlags() & AccessConstants.INTERFACE) != 0 ||
            ContainsConstructorsMarker.containsConstructors(referencedClass);
    }
}
