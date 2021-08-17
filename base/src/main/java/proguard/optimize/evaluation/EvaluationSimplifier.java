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
package proguard.optimize.evaluation;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.ClassPrinter;
import proguard.evaluation.*;
import proguard.evaluation.value.*;
import proguard.optimize.info.SideEffectInstructionChecker;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * This AttributeVisitor simplifies the code attributes that it visits, based
 * on partial evaluation.
 *
 * @author Eric Lafortune
 */
public class EvaluationSimplifier
implements   AttributeVisitor,
             InstructionVisitor
{
    private static final int  POS_ZERO_FLOAT_BITS  = Float.floatToIntBits(0.0f);
    private static final long POS_ZERO_DOUBLE_BITS = Double.doubleToLongBits(0.0);

    private static final Logger logger = LogManager.getLogger(EvaluationSimplifier.class);

    private final InstructionVisitor extraInstructionVisitor;

    private final PartialEvaluator             partialEvaluator;
    private final SideEffectInstructionChecker sideEffectInstructionChecker = new SideEffectInstructionChecker(true, true);
    private final CodeAttributeEditor          codeAttributeEditor          = new CodeAttributeEditor(true, true);


    /**
     * Creates a new EvaluationSimplifier.
     */
    public EvaluationSimplifier()
    {
        this(new PartialEvaluator(), null);
    }


    /**
     * Creates a new EvaluationSimplifier.
     * @param partialEvaluator        the partial evaluator that will
     *                                execute the code and provide
     *                                information about the results.
     * @param extraInstructionVisitor an optional extra visitor for all
     *                                simplified instructions.
     */
    public EvaluationSimplifier(PartialEvaluator partialEvaluator,
                                InstructionVisitor extraInstructionVisitor)
    {
        this.partialEvaluator        = partialEvaluator;
        this.extraInstructionVisitor = extraInstructionVisitor;
    }


    // Implementations for AttributeVisitor.


    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
//        DEBUG =
//            clazz.getName().equals("abc/Def") &&
//            method.getName(clazz).equals("abc");

        // TODO: Remove this when the evaluation simplifier has stabilized.
        // Catch any unexpected exceptions from the actual visiting method.
        try
        {
            // Process the code.
            visitCodeAttribute0(clazz, method, codeAttribute);
        }
        catch (RuntimeException ex)
        {
            logger.error("Unexpected error while simplifying instructions after partial evaluation:");
            logger.error("  Class       = [{}]", clazz.getName());
            logger.error("  Method      = [{}{}]", method.getName(clazz), method.getDescriptor(clazz));
            logger.error("  Exception   = [{}] ({})", ex.getClass().getName(), ex.getMessage());
            logger.error("Not optimizing this method");

            logger.debug("{}", () -> {
                StringWriter sw = new StringWriter();
                method.accept(clazz, new ClassPrinter(new PrintWriter(sw)));
                return sw.toString();
            });
            if (logger.getLevel().isLessSpecificThan(Level.DEBUG))
            {
                throw ex;
            }
        }
    }


    public void visitCodeAttribute0(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        logger.debug("EvaluationSimplifier [{}.{}{}]", clazz.getName(), method.getName(clazz), method.getDescriptor(clazz));

        // Evaluate the method.
        partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);

        int codeLength = codeAttribute.u4codeLength;

        // Reset the code changes.
        codeAttributeEditor.reset(codeLength);

        // Replace any instructions that can be simplified.
        for (int offset = 0; offset < codeLength; offset++)
        {
            if (partialEvaluator.isTraced(offset))
            {
                Instruction instruction = InstructionFactory.create(codeAttribute.code,
                                                                    offset);

                instruction.accept(clazz, method, codeAttribute, offset, this);
            }
        }

        // Apply all accumulated changes to the code.
        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
    }


    // Implementations for InstructionVisitor.

    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
    {
        switch (simpleInstruction.opcode)
        {
            case Instruction.OP_IDIV:
            case Instruction.OP_IREM:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceIntegerPushInstruction(clazz, offset, simpleInstruction);
                }
                else if (isDivisionByZero(offset, Value.TYPE_INTEGER))
                {
                    // In case we detected a certain division by zero, and OPTIMIZE.CONSERVATIVELY
                    // is enabled, replace the instruction by the explicit exception.
                    replaceByException(clazz, offset, simpleInstruction, "java/lang/ArithmeticException");
                }
                break;

            case Instruction.OP_LDIV:
            case Instruction.OP_LREM:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceLongPushInstruction(clazz, offset, simpleInstruction);
                }
                else if (isDivisionByZero(offset, Value.TYPE_LONG))
                {
                    // In case we detected a certain division by zero, and OPTIMIZE.CONSERVATIVELY
                    // is enabled, replace the instruction by the explicit exception.
                    replaceByException(clazz, offset, simpleInstruction, "java/lang/ArithmeticException");
                }
                break;

            case Instruction.OP_FDIV:
            case Instruction.OP_FREM:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceFloatPushInstruction(clazz, offset, simpleInstruction);
                }
                else if (isDivisionByZero(offset, Value.TYPE_FLOAT))
                {
                    // In case we detected a certain division by zero, and OPTIMIZE.CONSERVATIVELY
                    // is enabled, replace the instruction by the explicit exception.
                    replaceByException(clazz, offset, simpleInstruction, "java/lang/ArithmeticException");
                }
                break;

            case Instruction.OP_DDIV:
            case Instruction.OP_DREM:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceDoublePushInstruction(clazz, offset, simpleInstruction);
                }
                else if (isDivisionByZero(offset, Value.TYPE_DOUBLE))
                {
                    // In case we detected a certain division by zero, and OPTIMIZE.CONSERVATIVELY
                    // is enabled, replace the instruction by the explicit exception.
                    replaceByException(clazz, offset, simpleInstruction, "java/lang/ArithmeticException");
                }
                break;

            case Instruction.OP_IALOAD:
            case Instruction.OP_BALOAD:
            case Instruction.OP_CALOAD:
            case Instruction.OP_SALOAD:
            case Instruction.OP_ARRAYLENGTH:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceIntegerPushInstruction(clazz, offset, simpleInstruction);
                }
                else if (isNullReference(offset, simpleInstruction.stackPopCount(clazz) - 1))
                {
                    // In case we detected a certain access to a null array, and OPTIMIZE.CONSERVATIVELY
                    // is enabled, replace the instruction by the explicit exception.
                    replaceByException(clazz, offset, simpleInstruction, "java/lang/NullPointerException");
                }
                break;

            case Instruction.OP_IADD:
            case Instruction.OP_ISUB:
            case Instruction.OP_IMUL:
            case Instruction.OP_INEG:
            case Instruction.OP_ISHL:
            case Instruction.OP_ISHR:
            case Instruction.OP_IUSHR:
            case Instruction.OP_IAND:
            case Instruction.OP_IOR:
            case Instruction.OP_IXOR:
            case Instruction.OP_L2I:
            case Instruction.OP_F2I:
            case Instruction.OP_D2I:
            case Instruction.OP_I2B:
            case Instruction.OP_I2C:
            case Instruction.OP_I2S:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceIntegerPushInstruction(clazz, offset, simpleInstruction);
                }
                break;

            case Instruction.OP_LALOAD:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceLongPushInstruction(clazz, offset, simpleInstruction);
                }
                else if (isNullReference(offset, simpleInstruction.stackPopCount(clazz) - 1))
                {
                    // In case we detected a certain access to a null array, and OPTIMIZE.CONSERVATIVELY
                    // is enabled, replace the instruction by the explicit exception.
                    replaceByException(clazz, offset, simpleInstruction, "java/lang/NullPointerException");
                }
                break;

            case Instruction.OP_LADD:
            case Instruction.OP_LSUB:
            case Instruction.OP_LMUL:
            case Instruction.OP_LNEG:
            case Instruction.OP_LSHL:
            case Instruction.OP_LSHR:
            case Instruction.OP_LUSHR:
            case Instruction.OP_LAND:
            case Instruction.OP_LOR:
            case Instruction.OP_LXOR:
            case Instruction.OP_I2L:
            case Instruction.OP_F2L:
            case Instruction.OP_D2L:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceLongPushInstruction(clazz, offset, simpleInstruction);
                }
                break;

            case Instruction.OP_FALOAD:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceFloatPushInstruction(clazz, offset, simpleInstruction);
                }
                else if (isNullReference(offset, simpleInstruction.stackPopCount(clazz) - 1))
                {
                    // In case we detected a certain access to a null array, and OPTIMIZE.CONSERVATIVELY
                    // is enabled, replace the instruction by the explicit exception.
                    replaceByException(clazz, offset, simpleInstruction, "java/lang/NullPointerException");
                }
                break;

            case Instruction.OP_FADD:
            case Instruction.OP_FSUB:
            case Instruction.OP_FMUL:
            case Instruction.OP_FNEG:
            case Instruction.OP_I2F:
            case Instruction.OP_L2F:
            case Instruction.OP_D2F:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceFloatPushInstruction(clazz, offset, simpleInstruction);
                }
                break;

            case Instruction.OP_DALOAD:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceDoublePushInstruction(clazz, offset, simpleInstruction);
                }
                else if (isNullReference(offset, simpleInstruction.stackPopCount(clazz) - 1))
                {
                    // In case we detected a certain access to a null array, and OPTIMIZE.CONSERVATIVELY
                    // is enabled, replace the instruction by the explicit exception.
                    replaceByException(clazz, offset, simpleInstruction, "java/lang/NullPointerException");
                }
                break;

            case Instruction.OP_DADD:
            case Instruction.OP_DSUB:
            case Instruction.OP_DMUL:
            case Instruction.OP_DNEG:
            case Instruction.OP_I2D:
            case Instruction.OP_L2D:
            case Instruction.OP_F2D:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceDoublePushInstruction(clazz, offset, simpleInstruction);
                }
                break;

            case Instruction.OP_AALOAD:
                if (!sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 simpleInstruction))
                {
                    replaceReferencePushInstruction(clazz, offset, simpleInstruction);
                }
                else if (isNullReference(offset, simpleInstruction.stackPopCount(clazz) - 1))
                {
                    // In case we detected a certain access to a null array, and OPTIMIZE.CONSERVATIVELY
                    // is enabled, replace the instruction by the explicit exception.
                    replaceByException(clazz, offset, simpleInstruction, "java/lang/NullPointerException");
                }
                break;

            case Instruction.OP_IASTORE:
            case Instruction.OP_BASTORE:
            case Instruction.OP_CASTORE:
            case Instruction.OP_SASTORE:
            case Instruction.OP_LASTORE:
            case Instruction.OP_FASTORE:
            case Instruction.OP_DASTORE:
            case Instruction.OP_AASTORE:
                if (SideEffectInstructionChecker.OPTIMIZE_CONSERVATIVELY &&
                    isNullReference(offset, simpleInstruction.stackPopCount(clazz) - 1))
                {
                    // In case we detected a certain access to a null array, and OPTIMIZE.CONSERVATIVELY
                    // is enabled, replace the instruction by the explicit exception.
                    replaceByException(clazz, offset, simpleInstruction, "java/lang/NullPointerException");
                }
                break;
        }
    }


    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction)
    {
        int variableIndex = variableInstruction.variableIndex;

        switch (variableInstruction.opcode)
        {
            case Instruction.OP_ILOAD:
            case Instruction.OP_ILOAD_0:
            case Instruction.OP_ILOAD_1:
            case Instruction.OP_ILOAD_2:
            case Instruction.OP_ILOAD_3:
                replaceIntegerPushInstruction(clazz, offset, variableInstruction, variableIndex);
                break;

            case Instruction.OP_LLOAD:
            case Instruction.OP_LLOAD_0:
            case Instruction.OP_LLOAD_1:
            case Instruction.OP_LLOAD_2:
            case Instruction.OP_LLOAD_3:
                replaceLongPushInstruction(clazz, offset, variableInstruction, variableIndex);
                break;

            case Instruction.OP_FLOAD:
            case Instruction.OP_FLOAD_0:
            case Instruction.OP_FLOAD_1:
            case Instruction.OP_FLOAD_2:
            case Instruction.OP_FLOAD_3:
                replaceFloatPushInstruction(clazz, offset, variableInstruction, variableIndex);
                break;

            case Instruction.OP_DLOAD:
            case Instruction.OP_DLOAD_0:
            case Instruction.OP_DLOAD_1:
            case Instruction.OP_DLOAD_2:
            case Instruction.OP_DLOAD_3:
                replaceDoublePushInstruction(clazz, offset, variableInstruction, variableIndex);
                break;

            case Instruction.OP_ALOAD:
            case Instruction.OP_ALOAD_0:
            case Instruction.OP_ALOAD_1:
            case Instruction.OP_ALOAD_2:
            case Instruction.OP_ALOAD_3:
                replaceReferencePushInstruction(clazz, offset, variableInstruction);
                break;

            case Instruction.OP_ASTORE:
            case Instruction.OP_ASTORE_0:
            case Instruction.OP_ASTORE_1:
            case Instruction.OP_ASTORE_2:
            case Instruction.OP_ASTORE_3:
                deleteReferencePopInstruction(clazz, offset, variableInstruction);
                break;

            case Instruction.OP_RET:
                replaceBranchInstruction(clazz, offset, variableInstruction);
                break;
        }
    }


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        switch (constantInstruction.opcode)
        {
            case Instruction.OP_INVOKEVIRTUAL:
            case Instruction.OP_INVOKESPECIAL:
            case Instruction.OP_INVOKEINTERFACE:
                if (SideEffectInstructionChecker.OPTIMIZE_CONSERVATIVELY &&
                    isNullReference(offset, constantInstruction.stackPopCount(clazz) - 1))
                {
                    // In case a method is invoked on a null reference
                    // replace the instruction with an explicit NullPointerException.
                    // This is mainly needed to counter obfuscated code that might
                    // use exceptions to change the control flow. This is especially
                    // problematic if it happens with methods that are explicitly marked
                    // as having no side-effect (e.g. String#length()) as they might get
                    // removed otherwise.
                    replaceByException(clazz, offset, constantInstruction, "java/lang/NullPointerException");
                    break;
                }
                // intended fallthrough

            case Instruction.OP_GETSTATIC:
            case Instruction.OP_GETFIELD:
            case Instruction.OP_INVOKESTATIC:
                if (constantInstruction.stackPushCount(clazz) > 0 &&
                    !sideEffectInstructionChecker.hasSideEffects(clazz,
                                                                 method,
                                                                 codeAttribute,
                                                                 offset,
                                                                 constantInstruction))
                {
                    replaceAnyPushInstruction(clazz, offset, constantInstruction);
                }

                break;

            case Instruction.OP_CHECKCAST:
                replaceReferencePushInstruction(clazz, offset, constantInstruction);
                break;
        }
    }


    public void visitBranchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, BranchInstruction branchInstruction)
    {
        switch (branchInstruction.opcode)
        {
            case Instruction.OP_GOTO:
            case Instruction.OP_GOTO_W:
                // Don't replace unconditional branches.
                break;

            case Instruction.OP_JSR:
            case Instruction.OP_JSR_W:
                replaceJsrInstruction(clazz, offset, branchInstruction);
                break;

            default:
                replaceBranchInstruction(clazz, offset, branchInstruction);
                break;
        }
    }


    public void visitTableSwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, TableSwitchInstruction tableSwitchInstruction)
    {
        // First try to simplify it to a simple branch.
        replaceBranchInstruction(clazz, offset, tableSwitchInstruction);

        // Otherwise try to simplify simple enum switches.
        if (!codeAttributeEditor.isModified(offset))
        {
            replaceSimpleEnumSwitchInstruction(clazz,
                                               codeAttribute,
                                               offset,
                                               tableSwitchInstruction);

            // Otherwise make sure all branch targets are valid.
            if (!codeAttributeEditor.isModified(offset))
            {
                cleanUpSwitchInstruction(clazz, offset, tableSwitchInstruction);

                trimSwitchInstruction(clazz, offset, tableSwitchInstruction);
            }
        }
    }


    public void visitLookUpSwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, LookUpSwitchInstruction lookUpSwitchInstruction)
    {
        // First try to simplify it to a simple branch.
        replaceBranchInstruction(clazz, offset, lookUpSwitchInstruction);

        // Otherwise try to simplify simple enum switches.
        if (!codeAttributeEditor.isModified(offset))
        {
            replaceSimpleEnumSwitchInstruction(clazz,
                                               codeAttribute,
                                               offset,
                                               lookUpSwitchInstruction);

            // Otherwise make sure all branch targets are valid.
            if (!codeAttributeEditor.isModified(offset))
            {
                cleanUpSwitchInstruction(clazz, offset, lookUpSwitchInstruction);

                trimSwitchInstruction(clazz, offset, lookUpSwitchInstruction);
            }
        }
    }


    // Small utility methods.

    /**
     * Replaces the push instruction at the given offset by a simpler push
     * instruction, if possible.
     */
    private void replaceAnyPushInstruction(Clazz       clazz,
                                           int         offset,
                                           Instruction instruction)
    {
        Value pushedValue = partialEvaluator.getStackAfter(offset).getTop(0);
        if (pushedValue.isParticular())
        {
            switch (pushedValue.computationalType())
            {
                case Value.TYPE_INTEGER:
                    replaceIntegerPushInstruction(clazz, offset, instruction);
                    break;
                case Value.TYPE_LONG:
                    replaceLongPushInstruction(clazz, offset, instruction);
                    break;
                case Value.TYPE_FLOAT:
                    replaceFloatPushInstruction(clazz, offset, instruction);
                    break;
                case Value.TYPE_DOUBLE:
                    replaceDoublePushInstruction(clazz, offset, instruction);
                    break;
                case Value.TYPE_REFERENCE:
                    replaceReferencePushInstruction(clazz, offset, instruction);
                    break;
            }
        }
    }


    /**
     * Replaces the integer pushing instruction at the given offset by a simpler
     * push instruction, if possible.
     */
    private void replaceIntegerPushInstruction(Clazz       clazz,
                                               int         offset,
                                               Instruction instruction)
    {
        replaceIntegerPushInstruction(clazz,
                                      offset,
                                      instruction,
                                      partialEvaluator.getVariablesBefore(offset).size());
    }


    /**
     * Replaces the integer pushing instruction at the given offset by a simpler
     * push instruction, if possible.
     */
    private void replaceIntegerPushInstruction(Clazz       clazz,
                                               int         offset,
                                               Instruction instruction,
                                               int         maxVariableIndex)
    {
        Value pushedValue = partialEvaluator.getStackAfter(offset).getTop(0);
        if (pushedValue.isParticular())
        {
            // Push a constant instead.
            int value = pushedValue.integerValue().value();
            if ((short)value == value)
            {
                replaceConstantPushInstruction(clazz,
                                               offset,
                                               instruction,
                                               Instruction.OP_SIPUSH,
                                               value);
            }
            else
            {
                ConstantPoolEditor constantPoolEditor =
                    new ConstantPoolEditor((ProgramClass)clazz);

                Instruction replacementInstruction =
                    new ConstantInstruction(Instruction.OP_LDC,
                                            constantPoolEditor.addIntegerConstant(value));

                replaceInstruction(clazz, offset, instruction, replacementInstruction);
            }
        }
        else if (pushedValue.isSpecific())
        {
            // Load an equivalent lower-numbered variable instead, if any.
            TracedVariables variables = partialEvaluator.getVariablesBefore(offset);
            for (int variableIndex = 0; variableIndex < maxVariableIndex; variableIndex++)
            {
                if (pushedValue.equals(variables.load(variableIndex)))
                {
                    replaceVariablePushInstruction(clazz,
                                                   offset,
                                                   instruction,
                                                   Instruction.OP_ILOAD,
                                                   variableIndex);
                    break;
                }
            }
        }
    }


    /**
     * Replaces the long pushing instruction at the given offset by a simpler
     * push instruction, if possible.
     */
    private void replaceLongPushInstruction(Clazz       clazz,
                                            int         offset,
                                            Instruction instruction)
    {
        replaceLongPushInstruction(clazz,
                                   offset,
                                   instruction,
                                   partialEvaluator.getVariablesBefore(offset).size());
    }


    /**
     * Replaces the long pushing instruction at the given offset by a simpler
     * push instruction, if possible.
     */
    private void replaceLongPushInstruction(Clazz       clazz,
                                            int         offset,
                                            Instruction instruction,
                                            int         maxVariableIndex)
    {
        Value pushedValue = partialEvaluator.getStackAfter(offset).getTop(0);
        if (pushedValue.isParticular())
        {
            // Push a constant instead.
            long value = pushedValue.longValue().value();
            if (value == 0L ||
                value == 1L)
            {
                replaceConstantPushInstruction(clazz,
                                       offset,
                                       instruction,
                                       Instruction.OP_LCONST_0,
                                       (int)value);
            }
            else
            {
                ConstantPoolEditor constantPoolEditor =
                    new ConstantPoolEditor((ProgramClass)clazz);

                Instruction replacementInstruction =
                    new ConstantInstruction(Instruction.OP_LDC2_W,
                                            constantPoolEditor.addLongConstant(value));

                replaceInstruction(clazz, offset, instruction, replacementInstruction);
            }
        }
        else if (pushedValue.isSpecific())
        {
            // Load an equivalent lower-numbered variable instead, if any.
            TracedVariables variables = partialEvaluator.getVariablesBefore(offset);
            for (int variableIndex = 0; variableIndex < maxVariableIndex; variableIndex++)
            {
                // Note that we have to check the second part as well.
                if (pushedValue.equals(variables.load(variableIndex)) &&
                    variables.load(variableIndex + 1) != null         &&
                    variables.load(variableIndex + 1).computationalType() == Value.TYPE_TOP)
                {
                    replaceVariablePushInstruction(clazz,
                                                   offset,
                                                   instruction,
                                                   Instruction.OP_LLOAD,
                                                   variableIndex);
                }
            }
        }
    }


    /**
     * Replaces the float pushing instruction at the given offset by a simpler
     * push instruction, if possible.
     */
    private void replaceFloatPushInstruction(Clazz       clazz,
                                             int         offset,
                                             Instruction instruction)
    {
        replaceFloatPushInstruction(clazz,
                                    offset,
                                    instruction,
                                    partialEvaluator.getVariablesBefore(offset).size());
    }


    /**
     * Replaces the float pushing instruction at the given offset by a simpler
     * push instruction, if possible.
     */
    private void replaceFloatPushInstruction(Clazz       clazz,
                                             int         offset,
                                             Instruction instruction,
                                             int         maxVariableIndex)
    {
        Value pushedValue = partialEvaluator.getStackAfter(offset).getTop(0);
        if (pushedValue.isParticular())
        {
            // Push a constant instead.
            // Make sure to distinguish between +0.0 and -0.0.
            float value = pushedValue.floatValue().value();
            if (value == 0.0f && Float.floatToIntBits(value) == POS_ZERO_FLOAT_BITS ||
                value == 1.0f ||
                value == 2.0f)
            {
                replaceConstantPushInstruction(clazz,
                                               offset,
                                               instruction,
                                               Instruction.OP_FCONST_0,
                                               (int)value);
            }
            else
            {
                ConstantPoolEditor constantPoolEditor =
                    new ConstantPoolEditor((ProgramClass)clazz);

                Instruction replacementInstruction =
                    new ConstantInstruction(Instruction.OP_LDC,
                                            constantPoolEditor.addFloatConstant(value));

                replaceInstruction(clazz, offset, instruction, replacementInstruction);
            }
        }
        else if (pushedValue.isSpecific())
        {
            // Load an equivalent lower-numbered variable instead, if any.
            TracedVariables variables = partialEvaluator.getVariablesBefore(offset);
            for (int variableIndex = 0; variableIndex < maxVariableIndex; variableIndex++)
            {
                if (pushedValue.equals(variables.load(variableIndex)))
                {
                    replaceVariablePushInstruction(clazz,
                                                   offset,
                                                   instruction,
                                                   Instruction.OP_FLOAD,
                                                   variableIndex);
                }
            }
        }
    }


    /**
     * Replaces the double pushing instruction at the given offset by a simpler
     * push instruction, if possible.
     */
    private void replaceDoublePushInstruction(Clazz       clazz,
                                              int         offset,
                                              Instruction instruction)
    {
        replaceDoublePushInstruction(clazz,
                                     offset,
                                     instruction,
                                     partialEvaluator.getVariablesBefore(offset).size());
    }


    /**
     * Replaces the double pushing instruction at the given offset by a simpler
     * push instruction, if possible.
     */
    private void replaceDoublePushInstruction(Clazz       clazz,
                                              int         offset,
                                              Instruction instruction,
                                              int         maxVariableIndex)
    {
        Value pushedValue = partialEvaluator.getStackAfter(offset).getTop(0);
        if (pushedValue.isParticular())
        {
            // Push a constant instead.
            // Make sure to distinguish between +0.0 and -0.0.
            double value = pushedValue.doubleValue().value();
            if (value == 0.0 && Double.doubleToLongBits(value) == POS_ZERO_DOUBLE_BITS ||
                value == 1.0)
            {
                replaceConstantPushInstruction(clazz,
                                               offset,
                                               instruction,
                                               Instruction.OP_DCONST_0,
                                               (int)value);
            }
            else
            {
                ConstantPoolEditor constantPoolEditor =
                    new ConstantPoolEditor((ProgramClass)clazz);

                Instruction replacementInstruction =
                    new ConstantInstruction(Instruction.OP_LDC2_W,
                                            constantPoolEditor.addDoubleConstant(value));

                replaceInstruction(clazz, offset, instruction, replacementInstruction);
            }
        }
        else if (pushedValue.isSpecific())
        {
            // Load an equivalent lower-numbered variable instead, if any.
            TracedVariables variables = partialEvaluator.getVariablesBefore(offset);
            for (int variableIndex = 0; variableIndex < maxVariableIndex; variableIndex++)
            {
                // Note that we have to check the second part as well.
                if (pushedValue.equals(variables.load(variableIndex)) &&
                    variables.load(variableIndex + 1) != null         &&
                    variables.load(variableIndex + 1).computationalType() == Value.TYPE_TOP)
                {
                    replaceVariablePushInstruction(clazz,
                                                   offset,
                                                   instruction,
                                                   Instruction.OP_DLOAD,
                                                   variableIndex);
                }
            }
        }
    }


    /**
     * Replaces the reference pushing instruction at the given offset by a
     * simpler push instruction, if possible.
     */
    private void replaceReferencePushInstruction(Clazz       clazz,
                                                 int         offset,
                                                 Instruction instruction)
    {
        ReferenceValue pushedValue = partialEvaluator.getStackAfter(offset).getTop(0).referenceValue();
        if (pushedValue.isNull() == Value.ALWAYS)
        {
            // A reference value can only be specific if it is null.
            replaceConstantPushInstruction(clazz,
                                           offset,
                                           instruction,
                                           Instruction.OP_ACONST_NULL,
                                           0);
        }
    }


    /**
     * Replaces the instruction at a given offset by a given push instruction
     * of a constant.
     */
    private void replaceConstantPushInstruction(Clazz       clazz,
                                                int         offset,
                                                Instruction instruction,
                                                byte        replacementOpcode,
                                                int         value)
    {
        Instruction replacementInstruction =
            new SimpleInstruction(replacementOpcode, value);

        replaceInstruction(clazz, offset, instruction, replacementInstruction);
    }


    /**
     * Replaces the instruction at a given offset by a given push instruction
     * of a variable.
     */
    private void replaceVariablePushInstruction(Clazz       clazz,
                                                int         offset,
                                                Instruction instruction,
                                                byte        replacementOpcode,
                                                int         variableIndex)
    {
        Instruction replacementInstruction =
            new VariableInstruction(replacementOpcode, variableIndex);

        replaceInstruction(clazz, offset, instruction, replacementInstruction);
    }


    /**
     * Replaces the given 'jsr' instruction by a simpler branch instruction,
     * if it jumps to a subroutine that doesn't return or a subroutine that
     * is only called from one place.
     */
    private void replaceJsrInstruction(Clazz             clazz,
                                       int               offset,
                                       BranchInstruction branchInstruction)
    {
        // Is the subroutine ever returning?
        int subroutineStart = offset + branchInstruction.branchOffset;
        if (!partialEvaluator.isSubroutineReturning(subroutineStart) ||
            partialEvaluator.branchOrigins(subroutineStart).instructionOffsetCount() == 1)
        {
            // All 'jsr' instructions to this subroutine can be replaced
            // by unconditional branch instructions.
            replaceBranchInstruction(clazz, offset, branchInstruction);
        }
        else if (!partialEvaluator.isTraced(offset + branchInstruction.length(offset)))
        {
            // We have to make sure the instruction after this 'jsr'
            // instruction is valid, even if it is never reached.
            replaceByInfiniteLoop(clazz, offset + branchInstruction.length(offset), branchInstruction);
        }
    }


    /**
     * Deletes the reference popping instruction at the given offset, if
     * it is at the start of a subroutine that doesn't return or a subroutine
     * that is only called from one place.
     */
    private void deleteReferencePopInstruction(Clazz       clazz,
                                               int         offset,
                                               Instruction instruction)
    {
        if (partialEvaluator.isSubroutineStart(offset) &&
            (!partialEvaluator.isSubroutineReturning(offset) ||
             partialEvaluator.branchOrigins(offset).instructionOffsetCount() == 1))
        {
            logger.debug("  Deleting store of subroutine return address {}", instruction.toString(offset));

            // A reference value can only be specific if it is null.
            codeAttributeEditor.deleteInstruction(offset);
        }
    }


    /**
     * Deletes the given branch instruction, or replaces it by a simpler branch
     * instruction, if possible.
     */
    private void replaceBranchInstruction(Clazz       clazz,
                                          int         offset,
                                          Instruction instruction)
    {
        InstructionOffsetValue branchTargets = partialEvaluator.branchTargets(offset);

        // Is there exactly one branch target (not from a goto or jsr)?
        if (branchTargets != null &&
            branchTargets.instructionOffsetCount() == 1)
        {
            // Is it branching to the next instruction?
            int branchOffset = branchTargets.instructionOffset(0) - offset;
            if (branchOffset == instruction.length(offset))
            {
                logger.debug("  Ignoring zero branch instruction at [{}]", offset);
            }
            else
            {
                // Replace the branch instruction by a simple branch instruction.
                Instruction replacementInstruction =
                    new BranchInstruction(Instruction.OP_GOTO,
                                          branchOffset);

                replaceInstruction(clazz, offset, instruction, replacementInstruction);
            }
        }
    }


    /**
     * Replaces the given table switch instruction, if it is based on the value
     * of a fixed array. This is typical for switches on simple enums.
     */
    private void replaceSimpleEnumSwitchInstruction(Clazz                  clazz,
                                                    CodeAttribute          codeAttribute,
                                                    int                    offset,
                                                    TableSwitchInstruction tableSwitchInstruction)
    {
        // Check if the switch instruction is consuming a single value loaded
        // from a fully specified array.
        InstructionOffsetValue producerOffsets =
            partialEvaluator.getStackBefore(offset).getTopProducerValue(0).instructionOffsetValue();

        if (producerOffsets.instructionOffsetCount() == 1)
        {
            int producerOffset = producerOffsets.instructionOffset(0);

            if (codeAttribute.code[producerOffset] == Instruction.OP_IALOAD &&
                !codeAttributeEditor.isModified(producerOffset))
            {
                ReferenceValue referenceValue =
                    partialEvaluator.getStackBefore(producerOffset).getTop(1).referenceValue();

                if (referenceValue.isParticular())
                {
                    // Simplify the entire construct.
                    replaceSimpleEnumSwitchInstruction(clazz,
                                                       codeAttribute,
                                                       producerOffset,
                                                       offset,
                                                       tableSwitchInstruction,
                                                       referenceValue);
                }
            }
        }
    }


    /**
     * Replaces the given table switch instruction that is based on a value of
     * the given fixed array.
     */
    private void replaceSimpleEnumSwitchInstruction(Clazz                  clazz,
                                                    CodeAttribute          codeAttribute,
                                                    int                    loadOffset,
                                                    int                    switchOffset,
                                                    TableSwitchInstruction tableSwitchInstruction,
                                                    ReferenceValue         mappingValue)
    {
        ValueFactory valueFactory = new ParticularValueFactory();

        // Transform the jump offsets.
        int[] jumpOffsets    = tableSwitchInstruction.jumpOffsets;
        int[] newJumpOffsets = new int[mappingValue.arrayLength(valueFactory).value()];

        for (int index = 0; index < newJumpOffsets.length; index++)
        {
            int switchCase =
                mappingValue.integerArrayLoad(valueFactory.createIntegerValue(index),
                                              valueFactory).value();

            newJumpOffsets[index] =
                switchCase >= tableSwitchInstruction.lowCase &&
                switchCase <= tableSwitchInstruction.highCase ?
                    jumpOffsets[switchCase - tableSwitchInstruction.lowCase] :
                    tableSwitchInstruction.defaultOffset;
        }

        // Update the instruction.
        tableSwitchInstruction.lowCase     = 0;
        tableSwitchInstruction.highCase    = newJumpOffsets.length - 1;
        tableSwitchInstruction.jumpOffsets = newJumpOffsets;

        // Replace the original one with the new version.
        replaceSimpleEnumSwitchInstruction(clazz,
                                           loadOffset,
                                           switchOffset,
                                           tableSwitchInstruction);

        cleanUpSwitchInstruction(clazz, switchOffset, tableSwitchInstruction);

        trimSwitchInstruction(clazz, switchOffset, tableSwitchInstruction);
    }


    /**
     * Replaces the given look up switch instruction, if it is based on the
     * value of a fixed array. This is typical for switches on simple enums.
     */
    private void replaceSimpleEnumSwitchInstruction(Clazz                   clazz,
                                                    CodeAttribute           codeAttribute,
                                                    int                     offset,
                                                    LookUpSwitchInstruction lookupSwitchInstruction)
    {
        // Check if the switch instruction is consuming a single value loaded
        // from a fully specified array.
        InstructionOffsetValue producerOffsets =
            partialEvaluator.getStackBefore(offset).getTopProducerValue(0).instructionOffsetValue();

        if (producerOffsets.instructionOffsetCount() == 1)
        {
            int producerOffset = producerOffsets.instructionOffset(0);

            if (codeAttribute.code[producerOffset] == Instruction.OP_IALOAD &&
                !codeAttributeEditor.isModified(producerOffset))
            {
                ReferenceValue referenceValue =
                    partialEvaluator.getStackBefore(producerOffset).getTop(1).referenceValue();

                if (referenceValue.isParticular())
                {
                    // Simplify the entire construct.
                    replaceSimpleEnumSwitchInstruction(clazz,
                                                       codeAttribute,
                                                       producerOffset,
                                                       offset,
                                                       lookupSwitchInstruction,
                                                       referenceValue);
                }
            }
        }
    }


    /**
     * Replaces the given look up switch instruction that is based on a value of
     * the given fixed array. This is typical for switches on simple enums.
     */
    private void replaceSimpleEnumSwitchInstruction(Clazz                   clazz,
                                                    CodeAttribute           codeAttribute,
                                                    int                     loadOffset,
                                                    int                     switchOffset,
                                                    LookUpSwitchInstruction lookupSwitchInstruction,
                                                    ReferenceValue          mappingValue)
    {
        ValueFactory valueFactory = new ParticularValueFactory();

        // Transform the jump offsets.
        int[] cases          = lookupSwitchInstruction.cases;
        int[] jumpOffsets    = lookupSwitchInstruction.jumpOffsets;
        int[] newJumpOffsets = new int[mappingValue.arrayLength(valueFactory).value()];

        for (int index = 0; index < newJumpOffsets.length; index++)
        {
            int switchCase =
                mappingValue.integerArrayLoad(valueFactory.createIntegerValue(index),
                                              valueFactory).value();

            int caseIndex = Arrays.binarySearch(cases, switchCase);

            newJumpOffsets[index] = caseIndex >= 0 ?
                jumpOffsets[caseIndex] :
                lookupSwitchInstruction.defaultOffset;
        }

        // Replace the original lookup switch with a table switch.
        TableSwitchInstruction replacementSwitchInstruction =
            new TableSwitchInstruction(Instruction.OP_TABLESWITCH,
                                       lookupSwitchInstruction.defaultOffset,
                                       0,
                                       newJumpOffsets.length - 1,
                                       newJumpOffsets);

        replaceSimpleEnumSwitchInstruction(clazz,
                                           loadOffset,
                                           switchOffset,
                                           replacementSwitchInstruction);

        cleanUpSwitchInstruction(clazz, switchOffset, replacementSwitchInstruction);

        trimSwitchInstruction(clazz, switchOffset, replacementSwitchInstruction);
    }


    /**
     * Makes sure all branch targets of the given switch instruction are valid.
     */
    private void cleanUpSwitchInstruction(Clazz             clazz,
                                          int               offset,
                                          SwitchInstruction switchInstruction)
    {
        // Get the actual branch targets.
        InstructionOffsetValue branchTargets = partialEvaluator.branchTargets(offset);

        // Get an offset that can serve as a valid default offset.
        int defaultOffset =
            branchTargets.instructionOffset(branchTargets.instructionOffsetCount()-1) -
            offset;

        Instruction replacementInstruction = null;

        // Check the jump offsets.
        int[] jumpOffsets = switchInstruction.jumpOffsets;
        for (int index = 0; index < jumpOffsets.length; index++)
        {
            if (!branchTargets.contains(offset + jumpOffsets[index]))
            {
                // Replace the unused offset.
                jumpOffsets[index] = defaultOffset;

                // Remember to replace the instruction.
                replacementInstruction = switchInstruction;
            }
        }

        // Check the default offset.
        if (!branchTargets.contains(offset + switchInstruction.defaultOffset))
        {
            // Replace the unused offset.
            switchInstruction.defaultOffset = defaultOffset;

            // Remember to replace the instruction.
            replacementInstruction = switchInstruction;
        }

        if (replacementInstruction != null)
        {
            replaceInstruction(clazz, offset, switchInstruction, replacementInstruction);
        }
    }


    /**
     * Trims redundant offsets from the given switch instruction.
     */
    private void trimSwitchInstruction(Clazz                  clazz,
                                       int                    offset,
                                       TableSwitchInstruction tableSwitchInstruction)
    {
        // Get an offset that can serve as a valid default offset.
        int   defaultOffset = tableSwitchInstruction.defaultOffset;
        int[] jumpOffsets   = tableSwitchInstruction.jumpOffsets;
        int   length        = jumpOffsets.length;

        // Find the lowest index with a non-default jump offset.
        int lowIndex = 0;
        while (lowIndex < length &&
               jumpOffsets[lowIndex] == defaultOffset)
        {
            lowIndex++;
        }

        // Find the highest index with a non-default jump offset.
        int highIndex = length - 1;
        while (highIndex >= 0 &&
               jumpOffsets[highIndex] == defaultOffset)
        {
            highIndex--;
        }

        // Can we use a shorter array?
        int newLength = highIndex - lowIndex + 1;
        if (newLength < length)
        {
            if (newLength <= 0)
            {
                // Replace the switch instruction by a simple branch instruction.
                Instruction replacementInstruction =
                    new BranchInstruction(Instruction.OP_GOTO,
                                          defaultOffset);

                replaceInstruction(clazz, offset, tableSwitchInstruction,
                                   replacementInstruction);
            }
            else
            {
                // Trim the array.
                int[] newJumpOffsets = new int[newLength];

                System.arraycopy(jumpOffsets, lowIndex, newJumpOffsets, 0, newLength);

                tableSwitchInstruction.jumpOffsets = newJumpOffsets;
                tableSwitchInstruction.lowCase    += lowIndex;
                tableSwitchInstruction.highCase   -= length - newLength - lowIndex;

                replaceInstruction(clazz, offset, tableSwitchInstruction,
                                   tableSwitchInstruction);
            }
        }
    }


    /**
     * Trims redundant offsets from the given switch instruction.
     */
    private void trimSwitchInstruction(Clazz                   clazz,
                                       int                     offset,
                                       LookUpSwitchInstruction lookUpSwitchInstruction)
    {
        // Get an offset that can serve as a valid default offset.
        int   defaultOffset = lookUpSwitchInstruction.defaultOffset;
        int[] jumpOffsets   = lookUpSwitchInstruction.jumpOffsets;
        int   length        = jumpOffsets.length;
        int   newLength     = length;

        // Count the default jump offsets.
        for (int index = 0; index < length; index++)
        {
            if (jumpOffsets[index] == defaultOffset)
            {
                newLength--;
            }
        }

        // Can we use shorter arrays?
        if (newLength < length)
        {
            if (newLength <= 0)
            {
                // Replace the switch instruction by a simple branch instruction.
                Instruction replacementInstruction =
                    new BranchInstruction(Instruction.OP_GOTO,
                                          defaultOffset);

                replaceInstruction(clazz, offset, lookUpSwitchInstruction,
                                   replacementInstruction);
            }
            else
            {
                // Remove redundant entries from the arrays.
                int[] cases          = lookUpSwitchInstruction.cases;
                int[] newJumpOffsets = new int[newLength];
                int[] newCases       = new int[newLength];

                int newIndex = 0;

                for (int index = 0; index < length; index++)
                {
                    if (jumpOffsets[index] != defaultOffset)
                    {
                        newJumpOffsets[newIndex] = jumpOffsets[index];
                        newCases[newIndex++]     = cases[index];
                    }
                }

                lookUpSwitchInstruction.jumpOffsets = newJumpOffsets;
                lookUpSwitchInstruction.cases       = newCases;

                replaceInstruction(clazz, offset, lookUpSwitchInstruction,
                                   lookUpSwitchInstruction);
            }
        }
    }


    /**
     * Checks whether if the current top value on the stack is a divisor
     * leading to a certain division by zero for the given computation type.
     */
    private boolean isDivisionByZero(int offset, int computationType)
    {
        TracedStack tracedStack = partialEvaluator.getStackBefore(offset);
        Value divisor = tracedStack.getTop(0);
        switch (computationType)
        {
            case Value.TYPE_INTEGER:
                return divisor.computationalType() == Value.TYPE_INTEGER &&
                       divisor.isParticular() &&
                       divisor.integerValue().value() == 0;

            case Value.TYPE_LONG:
                return divisor.computationalType() == Value.TYPE_LONG &&
                       divisor.isParticular() &&
                       divisor.longValue().value() == 0L;

            case Value.TYPE_FLOAT:
                return divisor.computationalType() == Value.TYPE_FLOAT &&
                       divisor.isParticular() &&
                       divisor.floatValue().value() == 0f;

            case Value.TYPE_DOUBLE:
                return divisor.computationalType() == Value.TYPE_DOUBLE &&
                       divisor.isParticular() &&
                       divisor.doubleValue().value() == 0d;

            default:
                return false;
        }
    }


    /**
     * Checks whether the value at the given stack entry index is always a null reference.
     */
    private boolean isNullReference(int offset, int popStackEntryIndex)
    {
        TracedStack tracedStack = partialEvaluator.getStackBefore(offset);
        Value objectRef = tracedStack.getTop(popStackEntryIndex);

        return objectRef.computationalType() == Value.TYPE_REFERENCE &&
               objectRef.isParticular() &&
               objectRef.referenceValue().isNull() == Value.ALWAYS;
    }


    /**
     * Replaces the given instruction by an explicit exception.
     */
    private void replaceByException(Clazz       clazz,
                                    int         offset,
                                    Instruction instruction,
                                    String      exceptionClass)
    {
        ConstantPoolEditor constantPoolEditor =
            new ConstantPoolEditor((ProgramClass)clazz);

        // Replace the instruction by an infinite loop.
        Instruction[] replacementInstructions = new Instruction[]
            {
                new ConstantInstruction(Instruction.OP_NEW,
                                        constantPoolEditor.addClassConstant(exceptionClass, null)),
                new SimpleInstruction(Instruction.OP_DUP),
                new ConstantInstruction(Instruction.OP_INVOKESPECIAL,
                                        constantPoolEditor.addMethodrefConstant(exceptionClass, "<init>", "()V", null, null)),
                new SimpleInstruction(Instruction.OP_ATHROW)
            };

        logger.debug("  Replacing instruction by explicit exception {}", exceptionClass);

        codeAttributeEditor.replaceInstruction(offset, replacementInstructions);

        // Visit the instruction, if required.
        if (extraInstructionVisitor != null)
        {
            // Note: we're not passing the right arguments for now, knowing that
            // they aren't used anyway.
            instruction.accept(clazz,
                               null,
                               null,
                               offset,
                               extraInstructionVisitor);
        }
    }


    /**
     * Replaces the given instruction by an infinite loop.
     */
    private void replaceByInfiniteLoop(Clazz       clazz,
                                       int         offset,
                                       Instruction instruction)
    {
        // Replace the instruction by an infinite loop.
        Instruction replacementInstruction =
            new BranchInstruction(Instruction.OP_GOTO, 0);

        logger.debug("  Replacing unreachable instruction by infinite loop {}", replacementInstruction.toString(offset));

        codeAttributeEditor.replaceInstruction(offset, replacementInstruction);

        // Visit the instruction, if required.
        if (extraInstructionVisitor != null)
        {
            // Note: we're not passing the right arguments for now, knowing that
            // they aren't used anyway.
            instruction.accept(clazz,
                               null,
                               null,
                               offset,
                               extraInstructionVisitor);
        }
    }


    /**
     * Replaces the instruction at a given offset by a given push instruction.
     */
    private void replaceInstruction(Clazz       clazz,
                                    int         offset,
                                    Instruction instruction,
                                    Instruction replacementInstruction)
    {
        // Pop unneeded stack entries if necessary.
        int popCount =
            instruction.stackPopCount(clazz) -
            replacementInstruction.stackPopCount(clazz);

        insertPopInstructions(offset, popCount);

        logger.debug("  Replacing instruction {} -> {}{}",
                     instruction.toString(offset),
                     replacementInstruction.toString(),
                     popCount == 0 ? "" : " ("+popCount+" pops)"
        );

        codeAttributeEditor.replaceInstruction(offset, replacementInstruction);

        // Visit the instruction, if required.
        if (extraInstructionVisitor != null)
        {
            // Note: we're not passing the right arguments for now, knowing that
            // they aren't used anyway.
            instruction.accept(clazz, null, null, offset, extraInstructionVisitor);
        }
    }


    /**
     * Pops the given number of stack entries before the instruction at the
     * given offset.
     */
    private void insertPopInstructions(int offset, int popCount)
    {
        switch (popCount)
        {
            case 0:
            {
                break;
            }
            case 1:
            {
                // Insert a single pop instruction.
                Instruction popInstruction =
                    new SimpleInstruction(Instruction.OP_POP);

                codeAttributeEditor.insertBeforeInstruction(offset,
                                                            popInstruction);
                break;
            }
            case 2:
            {
                // Insert a single pop2 instruction.
                Instruction popInstruction =
                    new SimpleInstruction(Instruction.OP_POP2);

                codeAttributeEditor.insertBeforeInstruction(offset,
                                                            popInstruction);
                break;
            }
            default:
            {
                // Insert the specified number of pop instructions.
                Instruction[] popInstructions =
                    new Instruction[popCount / 2 + popCount % 2];

                Instruction popInstruction =
                    new SimpleInstruction(Instruction.OP_POP2);

                for (int index = 0; index < popCount / 2; index++)
                {
                      popInstructions[index] = popInstruction;
                }

                if (popCount % 2 == 1)
                {
                    popInstruction =
                        new SimpleInstruction(Instruction.OP_POP);

                    popInstructions[popCount / 2] = popInstruction;
                }

                codeAttributeEditor.insertBeforeInstruction(offset,
                                                            popInstructions);
                break;
            }
        }
    }


    /**
     * Replaces the simple enum switch instructions at a given offsets by a
     * given replacement instruction.
     */
    private void replaceSimpleEnumSwitchInstruction(Clazz             clazz,
                                                    int               loadOffset,
                                                    int               switchOffset,
                                                    SwitchInstruction replacementSwitchInstruction)
    {
        logger.debug("  Replacing switch instruction at [{}] -> [{}] swap + pop, {})",
                     switchOffset,
                     loadOffset,
                     replacementSwitchInstruction.toString(switchOffset)
        );

        // Remove the array load instruction.
        codeAttributeEditor.replaceInstruction(loadOffset, new Instruction[]
            {
                new SimpleInstruction(Instruction.OP_SWAP),
                new SimpleInstruction(Instruction.OP_POP),
            });

        // Replace the switch instruction.
        codeAttributeEditor.replaceInstruction(switchOffset, replacementSwitchInstruction);

        // Visit the instruction, if required.
        if (extraInstructionVisitor != null)
        {
            // Note: we're not passing the right arguments for now, knowing that
            // they aren't used anyway.
            replacementSwitchInstruction.accept(clazz,
                                                null,
                                                null,
                                                switchOffset,
                                                extraInstructionVisitor);
        }
    }
}
