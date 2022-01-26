/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.info.WrapperClassMarker;

/**
 * This AttributeVisitor simplifies the use of retargeted wrapper classes in
 * the code attributes that it visits. More specifically, it replaces
 *     "new Wrapper(targetClass)" by "targetClass", and
 *     "wrapper.field" by "wrapper".
 * You still need to retarget all class references, replacing references to
 * the wrapper class by references to the target class.
 *
 * @see WrapperClassMarker
 * @see ClassMerger
 * @see RetargetedClassFilter
 * @see TargetClassChanger
 * @author Eric Lafortune
 */
public class WrapperClassUseSimplifier
implements   AttributeVisitor,
             InstructionVisitor,
             ConstantVisitor,
             ClassVisitor
{
    private static final Logger logger = LogManager.getLogger(WrapperClassUseSimplifier.class);


    private final InstructionVisitor extraInstructionVisitor;

    private final CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor(true, true);

    // Fields acting as parameters and return values for the visitor methods.
    private Clazz       wrappedClass;
    private boolean     isDupedOnStack;
    private Instruction storeInstruction;


    /**
     * Creates a new WrapperClassUseSimplifier.
     */
    public WrapperClassUseSimplifier()
    {
        this(null);
    }


    /**
     * Creates a new WrapperClassUseSimplifier.
     * @param extraInstructionVisitor an optional extra visitor for all
     *                                simplified instructions.
     */
    public WrapperClassUseSimplifier(InstructionVisitor extraInstructionVisitor)
    {
        this.extraInstructionVisitor = extraInstructionVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        logger.debug("WrapperClassUseSimplifier: {}.{}{}",
                     clazz.getName(),
                     method.getName(clazz),
                     method.getDescriptor(clazz));

        int codeLength = codeAttribute.u4codeLength;

        // Reset the code changes.
        codeAttributeEditor.reset(codeLength);

        // Edit the instructions.
        codeAttribute.instructionsAccept(clazz, method, this);

        // Apply all accumulated changes to the code.
        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        switch (constantInstruction.opcode)
        {
            case Instruction.OP_NEW:
            {
                // Is it instantiating a wrapper class?
                if (isReferencingWrapperClass(clazz, constantInstruction.constantIndex))
                {
                    // We at least need to handle any of these sequences:
                    //     new                  -> (nothing)
                    //     new / dup            -> (nothing)
                    //     new / astore / aload -> (nothing)
                    //     new / dup / astore   -> (nothing)

                    // Replace the new instance by a dummy null.
                    codeAttributeEditor.deleteInstruction(offset);

                    // Look at the next instruction.
                    offset += constantInstruction.length(offset);
                    Instruction nextInstruction = InstructionFactory.create(codeAttribute.code, offset);

                    // Is the next instruction a (typical) dup instruction?
                    isDupedOnStack = nextInstruction.opcode == Instruction.OP_DUP;
                    if (isDupedOnStack)
                    {
                        // Delete the duplicate.
                        codeAttributeEditor.deleteInstruction(offset);

                        // Look at the next instruction.
                        offset += nextInstruction.length(offset);
                        nextInstruction = InstructionFactory.create(codeAttribute.code, offset);
                    }

                    // Is the next instruction a (less common) store instruction?
                    if (nextInstruction.canonicalOpcode() ==  Instruction.OP_ASTORE)
                    {
                        // Delete the store.
                        codeAttributeEditor.deleteInstruction(offset);

                        // Remember the store instruction, so we can use it to
                        // store the wrapped class later on.
                        storeInstruction = nextInstruction;

                        // Look at the next instruction.
                        offset += nextInstruction.length(offset);
                        nextInstruction = InstructionFactory.create(codeAttribute.code, offset);

                        // Is the next instruction the corresponding load
                        // instruction?
                        if (nextInstruction.canonicalOpcode() == Instruction.OP_ALOAD &&
                            ((VariableInstruction)storeInstruction).variableIndex ==
                            ((VariableInstruction)nextInstruction ).variableIndex)
                        {
                            // Delete the load.
                            codeAttributeEditor.deleteInstruction(offset);

                            // Remember that the original code had a duplicate
                            // of the wrapper on the stack.
                            isDupedOnStack = true;
                        }
                    }
                    else
                    {
                        storeInstruction = null;
                    }

                    if (extraInstructionVisitor != null)
                    {
                        extraInstructionVisitor.visitConstantInstruction(clazz, method, codeAttribute, offset, constantInstruction);
                    }
                }
                break;
            }
            case Instruction.OP_INVOKESPECIAL:
            {
                // Is it initializing a wrapper class?
                if (isReferencingWrapperClass(clazz, constantInstruction.constantIndex))
                {
                    // Replace the initializer invocation, popping or storing
                    // the wrapped instance from the stack.
                    // TODO: May still fail with nested initializers for different wrapper classes.

                    // Do we have a store instruction?
                    if (storeInstruction != null)
                    {
                        // The wrapper was stored beforehand. Store the wrapped
                        // instance now.
                        //     wrapper.<init>(target) -> astore
                        codeAttributeEditor.replaceInstruction(offset, storeInstruction);
                    }
                    else if (isDupedOnStack)
                    {
                        // The wrapper was originally duplicated on the stack.
                        // Delete the initializer invocation, leaving just the
                        // wrapped instance on the stack:
                        //     wrapper.<init>(target) -> (target)
                        // A subsequent astore/putfield/... should consume it.
                        codeAttributeEditor.deleteInstruction(offset);
                    }
                    else
                    {
                        // The wrapper wasn't duplicated on the stack or
                        // stored in a variable. Just pop the wrapped instance.
                        //     wrapper.<init>(target) -> pop
                        codeAttributeEditor.replaceInstruction(offset, new SimpleInstruction(Instruction.OP_POP));
                    }
                }
                break;
            }
            case Instruction.OP_GETFIELD:
            {
                // Is it retrieving the field of the wrapper class?
                if (isReferencingWrapperClass(clazz, constantInstruction.constantIndex))
                {
                    // Delete the retrieval:
                    //     wrapper.field -> wrapper
                    codeAttributeEditor.deleteInstruction(offset);
                }
                break;
            }
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        // Is the constant retrieving from a wrapper class?
        fieldrefConstant.referencedClassAccept(this);
    }


    public void visitMethodrefConstant(Clazz clazz, MethodrefConstant methodrefConstant)
    {
        if (methodrefConstant.getName(clazz).equals(ClassConstants.METHOD_NAME_INIT))
        {
            // Is the constant referring to a wrapper class?
            methodrefConstant.referencedClassAccept(this);
        }
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Is the constant referring to a wrapper class?
        classConstant.referencedClassAccept(this);
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) { }


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        wrappedClass = ClassMerger.getTargetClass(programClass);
    }


    // Small utility methods.

    /**
     * Returns whether the constant at the given offset is referencing a
     * wrapper class (different from the given class) that is being retargeted.
     */
    private boolean isReferencingWrapperClass(Clazz clazz, int constantIndex)
    {
        wrappedClass = null;

        clazz.constantPoolEntryAccept(constantIndex, this);

        return wrappedClass != null;
    }
}
