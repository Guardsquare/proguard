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
package proguard.optimize;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.util.ProcessingFlags;

/**
 * This InstructionVisitor generalizes the classes referenced by the
 * field/method invocation instructions that it visits.
 *
 * For example, it replaces invocations
 *   StringBuilder#toString()    by Object#toString(),
 *   Integer#intValue()          by Number#intValue(),
 *   Exception#printStackTrace() by Throwable#printStackTrace().
 *
 * @author Eric Lafortune
 */
public class MemberReferenceGeneralizer
implements   InstructionVisitor,
             ConstantVisitor,
             ClassVisitor
{
    private static final Logger logger = LogManager.getLogger(MemberReferenceGeneralizer.class);

    private final boolean             fieldGeneralizationClass;
    private final boolean             methodGeneralizationClass;
    private final CodeAttributeEditor codeAttributeEditor;
    private final InstructionVisitor  extraFieldInstructionVisitor;
    private final InstructionVisitor  extraMethodInstructionVisitor;

    // Fields acting as parameters and return values for the visitor methods.
    private int    invocationOffset;
    private byte   invocationOpcode;
    private String memberName;
    private String memberType;
    private Clazz  generalizedClass;
    private Member generalizedMember;


    /**
     * Creates a new ReferenceGeneralizer.
     * @param fieldGeneralizationClass  specifies whether field classes should
     *                                  be generalized.
     * @param methodGeneralizationClass specifies whether method classes
     *                                  should be generalized.
     * @param codeAttributeEditor       a code editor that can be used to
     *                                  accumulate changes to the code.
     */
    public MemberReferenceGeneralizer(boolean             fieldGeneralizationClass,
                                      boolean             methodGeneralizationClass,
                                      CodeAttributeEditor codeAttributeEditor)
    {
        this(fieldGeneralizationClass,
             methodGeneralizationClass,
             codeAttributeEditor,
             null,
             null);
    }


    /**
     * Creates a new ReferenceGeneralizer.
     * @param fieldGeneralizationClass      specifies whether field classes
     *                                      should be generalized.
     * @param methodGeneralizationClass     specifies whether method classes
     *                                      should be generalized.
     * @param codeAttributeEditor           a code editor that can be used to
     *                                      accumulate changes to the code.
     * @param extraFieldInstructionVisitor  an extra visitor for all field
     *                                      access instructions whose
     *                                      referenced classes have been
     *                                      generalized.
     * @param extraMethodInstructionVisitor an extra visitor for all method
     *                                      invocations instructions whose
     *                                      referenced classes have been
     *                                      generalized.
     */
    public MemberReferenceGeneralizer(boolean             fieldGeneralizationClass,
                                      boolean             methodGeneralizationClass,
                                      CodeAttributeEditor codeAttributeEditor,
                                      InstructionVisitor  extraFieldInstructionVisitor,
                                      InstructionVisitor  extraMethodInstructionVisitor)
    {
        this.fieldGeneralizationClass      = fieldGeneralizationClass;
        this.methodGeneralizationClass     = methodGeneralizationClass;
        this.codeAttributeEditor           = codeAttributeEditor;
        this.extraFieldInstructionVisitor  = extraFieldInstructionVisitor;
        this.extraMethodInstructionVisitor = extraMethodInstructionVisitor;
    }


    // Implementations for InstructionVisitor.

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        // Is it a virtual method invocation?
        byte opcode = constantInstruction.opcode;
        switch (opcode)
        {
            case Instruction.OP_GETFIELD:
            case Instruction.OP_PUTFIELD:
            {
                if (fieldGeneralizationClass &&
                    !codeAttributeEditor.isModified(offset))
                {
                    // Try to generalize the field reference.
                    invocationOffset = offset;
                    invocationOpcode = opcode;
                    clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);

                    // Did we find a class with the class member?
                    if (generalizedClass != null &&
                        extraFieldInstructionVisitor != null)
                    {
                        extraFieldInstructionVisitor.visitConstantInstruction(clazz,
                                                                              method,
                                                                              codeAttribute,
                                                                              offset,
                                                                              constantInstruction);
                    }
                }

                break;
            }
            case Instruction.OP_INVOKEVIRTUAL:
            {
                if (methodGeneralizationClass &&
                    !codeAttributeEditor.isModified(offset))
                {
                    // Try to generalize the method invocation.
                    invocationOffset = offset;
                    invocationOpcode = opcode;
                    clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);

                    // Did we find a class with the class member?
                    if (generalizedClass != null &&
                        extraMethodInstructionVisitor != null)
                    {
                        extraMethodInstructionVisitor.visitConstantInstruction(clazz,
                                                                               method,
                                                                               codeAttribute,
                                                                               offset,
                                                                               constantInstruction);
                    }
                }

                break;
            }
        }
    }


    // Implementations for ConstantVisitor.

    @Override
    public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant)
    {
        // Try to find the class member in the super class (or higher).
        memberName        = refConstant.getName(clazz);
        memberType        = refConstant.getType(clazz);
        generalizedClass  = null;
        generalizedMember = null;

        Member referencedMember = refConstant instanceof FieldrefConstant ?
            ((FieldrefConstant)refConstant).referencedField : ((AnyMethodrefConstant)refConstant).referencedMethod;

        // DGD-486: Only generalize members which are always available. Partial replacement of a class that is not
        // available on all platforms may result in a VerifyError at runtime.
        if (referencedMember != null &&
            (referencedMember.getProcessingFlags() & ProcessingFlags.IS_CLASS_AVAILABLE) != 0)
        {
            clazz.constantPoolEntryAccept(refConstant.u2classIndex, this);

            // Did we find a class with the class member?
            if (generalizedClass != null &&
                // only update the RefConstant if the generalized class is different
                // from the existing one. This can happen for fields as we need to first
                // look in the current class. Use the actual class name to compare as the
                // ClassReferenceInitializer might have found the proper class in the
                // hierarchy.
                !generalizedClass.getName().equals(refConstant.getClassName(clazz)))
            {
                logger.debug("ReferenceGeneralizer: [{}] invocation [{}.{}{}] -> [{}. ...]",
                             clazz.getName(),
                             refConstant.getClassName(clazz),
                             memberName,
                             memberType,
                             generalizedClass.getName()
                );

                int refConstantIndex;

                // Prevent concurrent modification of the constant pool from multiple threads.
                synchronized (clazz)
                {
                    ConstantPoolEditor constantPoolEditor =
                        new ConstantPoolEditor((ProgramClass)clazz);

                    // Replace the invocation instruction.
                    int generalizedClassConstantIndex =
                        constantPoolEditor.addClassConstant(generalizedClass);

                    refConstantIndex =
                        invocationOpcode != Instruction.OP_INVOKEVIRTUAL ?
                            constantPoolEditor.addFieldrefConstant(generalizedClassConstantIndex,
                                                                   memberName,
                                                                   memberType,
                                                                   generalizedClass,
                                                                   (Field)generalizedMember) :
                            constantPoolEditor.addMethodrefConstant(generalizedClassConstantIndex,
                                                                    memberName,
                                                                    memberType,
                                                                    generalizedClass,
                                                                    (Method)generalizedMember);
                }

                ConstantInstruction replacementInstruction =
                    new ConstantInstruction(invocationOpcode, refConstantIndex);

                codeAttributeEditor.replaceInstruction(invocationOffset,
                                                       replacementInstruction);
            }
            else
            {
                generalizedClass = null;
            }
        }
    }


    @Override
    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Try to find the method in the super class (or higher).
        classConstant.referencedClassAccept(this);
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz)
    {
        if (invocationOpcode == Instruction.OP_INVOKEVIRTUAL)
        {
            // Do we have a super class?
            Clazz superClass = clazz.getSuperClass();

            if (superClass != null)
            {
                // First look higher up in the hierarchy.
                superClass.accept(this);

                // Otherwise, look in the super class itself.
                // Only consider public classes and methods, to avoid any
                // access problems.
                if (generalizedClass == null &&
                    (superClass.getAccessFlags() & AccessConstants.PUBLIC) != 0)
                {
                    Method method = superClass.findMethod(memberName, memberType);
                    if (method != null &&
                        (method.getAccessFlags() & AccessConstants.PUBLIC) != 0 &&
                        (method.getProcessingFlags() & ProcessingFlags.IS_CLASS_AVAILABLE) != 0)
                    {
                        // Remember the generalized class and class member.
                        generalizedClass = superClass;
                        generalizedMember = method;
                    }
                }
            }
        }
        else
        {
            // First look for the field in the class itself.
            Field field = clazz.findField(memberName, memberType);

            if (field != null &&
                (field.getProcessingFlags() & ProcessingFlags.IS_CLASS_AVAILABLE) != 0)
            {
                // Remember the generalized class and class member.
                generalizedClass = clazz;
                generalizedMember = field;
            }
            else
            {
                // Do we have a super class?
                Clazz superClass = clazz.getSuperClass();

                if (superClass != null)
                {
                    // Look higher up in the hierarchy.
                    superClass.accept(this);
                }
            }
        }
    }
}
