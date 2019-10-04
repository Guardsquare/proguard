/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 GuardSquare NV
 */
package proguard.optimize.info;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This InstructionVisitor marks whether a final field is assigned
 * in the methods whose instructions it visits.
 *
 * @author Thomas Neidhart
 */
public class FinalFieldAssignmentMarker
extends      SimplifiedVisitor
implements   InstructionVisitor,
             ConstantVisitor
{
    private Method referencedMethod;


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        if (constantInstruction.opcode == InstructionConstants.OP_PUTSTATIC ||
            constantInstruction.opcode == InstructionConstants.OP_PUTFIELD)
        {
            referencedMethod = method;
            clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
        }
    }


    // Implementations for ConstantVisitor.

    @Override
    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    @Override
    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        if (fieldrefConstant.referencedMember != null &&
            (fieldrefConstant.referencedMember.getAccessFlags() & ClassConstants.ACC_FINAL) != 0)
        {
            setAssignsFinalField(referencedMethod);
        }
    }


    // Small utility methods.

    private static void setAssignsFinalField(Method method)
    {
        ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method).setAssignsFinalField();
    }


    /**
     * Returns whether the given method assigns a final field.
     */
    public static boolean assignsFinalField(Method method)
    {
        return MethodOptimizationInfo.getMethodOptimizationInfo(method).assignsFinalField();
    }
}
