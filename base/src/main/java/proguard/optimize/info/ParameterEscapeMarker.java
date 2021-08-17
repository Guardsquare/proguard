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
package proguard.optimize.info;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.evaluation.*;
import proguard.evaluation.value.*;
import proguard.optimize.evaluation.*;

/**
 * This MemberVisitor, AttributeVisitor, and InstructionVisitor marks the
 * reference parameters that are escaping, that are modified, or that are
 * returned.
 *
 * It also marks methods that may modify anything on the heap.
 *
 * The class must be called as a MemberVisitor on all members (to mark the
 * parameters of native methods, without code attributes), then as an
 * AttributeVisitor on their code attributes (so it can run its PartialEvaluator
 * and ReferenceEscapeChecker), and finally as an InstructionVisitor on its
 * instructions (to actually mark the parameters).
 *
 * @see SideEffectClassChecker
 * @see SideEffectClassMarker
 * @author Eric Lafortune
 */
public class ParameterEscapeMarker
implements   MemberVisitor,
             AttributeVisitor,
             InstructionVisitor,
             ConstantVisitor,
             ParameterVisitor
{
    private static final Logger logger = LogManager.getLogger(ParameterEscapeMarker.class);


    private final MemberVisitor          extraMemberVisitor;
    private final PartialEvaluator       partialEvaluator;
    private final boolean                runPartialEvaluator;
    private final ReferenceEscapeChecker referenceEscapeChecker;
    private final boolean                runReferenceEscapeChecker;

    private final MemberVisitor parameterMarker = new AllParameterVisitor(true, this);

    // Parameters and values for visitor methods.
    private Clazz   referencingClass;
    private Method  referencingMethod;
    private int     referencingOffset;
    private int     referencingPopCount;
    private boolean isReturnValueEscaping;
    private boolean isReturnValueModified;


    /**
     * Creates a new ParameterEscapeMarker.
     */
    public ParameterEscapeMarker()
    {
        this(null);
    }

    /**
     * Creates a new ParameterEscapeMarker.
     */
    public ParameterEscapeMarker(MemberVisitor extraMemberVisitor)
    {
        this(new BasicValueFactory(), extraMemberVisitor
        );
    }


    /**
     * Creates a new ParameterEscapeMarker.
     */
    public ParameterEscapeMarker(ValueFactory valueFactory, MemberVisitor extraMemberVisitor)
    {
        this(valueFactory, new ReferenceTracingValueFactory(valueFactory), extraMemberVisitor
        );
    }


    /**
     * Creates a new ParameterEscapeMarker.
     */
    public ParameterEscapeMarker(ValueFactory                 valueFactory,
                                 ReferenceTracingValueFactory tracingValueFactory,
                                 MemberVisitor                extraMemberVisitor)
    {
        this(new PartialEvaluator(tracingValueFactory,
                             new ParameterTracingInvocationUnit(new BasicInvocationUnit(tracingValueFactory)),
                             true,
                             tracingValueFactory), true, extraMemberVisitor
        );
    }


    /**
     * Creates a new ParameterEscapeMarker.
     */
    public ParameterEscapeMarker(PartialEvaluator partialEvaluator,
                                 boolean          runPartialEvaluator,
                                 MemberVisitor    extraMemberVisitor)
    {
        this(partialEvaluator,
             runPartialEvaluator,
             new ReferenceEscapeChecker(partialEvaluator, false),
             true,
             extraMemberVisitor
        );
    }


    /**
     * Creates a new ParameterEscapeMarker.
     */
    public ParameterEscapeMarker(PartialEvaluator       partialEvaluator,
                                 boolean                runPartialEvaluator,
                                 ReferenceEscapeChecker referenceEscapeChecker,
                                 boolean                runReferenceEscapeChecker,
                                 MemberVisitor          extraMemberVisitor)
    {
        this.extraMemberVisitor = extraMemberVisitor;
        this.partialEvaluator          = partialEvaluator;
        this.runPartialEvaluator       = runPartialEvaluator;
        this.referenceEscapeChecker    = referenceEscapeChecker;
        this.runReferenceEscapeChecker = runReferenceEscapeChecker;
    }


    // Implementations for MemberVisitor.

    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        int accessFlags = programMethod.getAccessFlags();

        // Is it a native method?
        if ((accessFlags & AccessConstants.NATIVE) != 0)
        {
            // Mark all parameters.
            markModifiedParameters(programClass, programMethod, -1L);
            markEscapingParameters(programClass, programMethod, -1L);
            markReturnedParameters(programClass, programMethod, -1L);
            markAnythingModified(programClass, programMethod);
        }
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // Evaluate the code.
        if (runPartialEvaluator)
        {
            partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);
        }

        if (runReferenceEscapeChecker)
        {
            referenceEscapeChecker.visitCodeAttribute(clazz, method, codeAttribute);
        }

        // These results are not complete yet, since this class must still
        // be called as an InstructionVisitor.
        logger.debug("ParameterEscapeMarker: [{}.{}{}]",
                     clazz.getName(),
                     method.getName(clazz),
                     method.getDescriptor(clazz)
        );

        int parameterCount =
            ClassUtil.internalMethodParameterCount(method.getDescriptor(clazz),
                                                   method.getAccessFlags());

        if (logger.getLevel().isLessSpecificThan(Level.DEBUG))
        {
            for (int index = 0; index < parameterCount; index++) {
                logger.debug("  {}{}{} P{}",
//                           (hasParameterEscaped(method, index) ? 'e' : '.'),
                             isParameterEscaping(method, index) ? 'E' : '.',
                             isParameterReturned(method, index) ? 'R' : '.',
                             isParameterModified(method, index) ? 'M' : '.',
                             index
                );
            }
        }

        logger.debug("  {}   Return value", returnsExternalValues(method) ? 'X' : '.');
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
    {
        switch (simpleInstruction.opcode)
        {
            case Instruction.OP_AASTORE:
                // Mark array parameters whose element is modified.
                markModifiedParameters(clazz,
                                       method,
                                       offset,
                                       simpleInstruction.stackPopCount(clazz) - 1);

                // Mark reference values that are put in the array.
                markEscapingParameters(clazz, method, offset, 0);
                break;

            case Instruction.OP_IASTORE:
            case Instruction.OP_LASTORE:
            case Instruction.OP_FASTORE:
            case Instruction.OP_DASTORE:
            case Instruction.OP_BASTORE:
            case Instruction.OP_CASTORE:
            case Instruction.OP_SASTORE:
                // Mark array parameters whose element is modified.
                markModifiedParameters(clazz,
                                       method,
                                       offset,
                                       simpleInstruction.stackPopCount(clazz) - 1);
                break;

            case Instruction.OP_ARETURN:
                // Mark returned reference values.
                markReturnedParameters(clazz, method, offset, 0);
                break;

            case Instruction.OP_ATHROW:
                // Mark the escaping reference values.
                markEscapingParameters(clazz, method, offset, 0);
                break;
        }
    }


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        switch (constantInstruction.opcode)
        {
            case Instruction.OP_LDC:
            case Instruction.OP_LDC_W:
            case Instruction.OP_NEW:
            case Instruction.OP_ANEWARRAY:
            case Instruction.OP_MULTIANEWARRAY:
            case Instruction.OP_GETSTATIC:
                // Mark possible modifications due to initializers.
                referencingClass  = clazz;
                referencingMethod = method;
                referencingOffset = offset;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;

            case Instruction.OP_PUTSTATIC:
                // Mark some global modification.
                markAnythingModified(clazz, method);

                // Mark reference values that are put in the field.
                markEscapingParameters(clazz, method, offset, 0);
                break;

            case Instruction.OP_GETFIELD:
                // Mark the owner of the field. The owner sort of escapes when
                // the field is retrieved. [DGD-1298] (test2181)
                markEscapingParameters(clazz, method, offset, 0);
                break;

            case Instruction.OP_PUTFIELD:
                // Mark reference parameters whose field is modified.
                markModifiedParameters(clazz,
                                       method,
                                       offset,
                                       constantInstruction.stackPopCount(clazz) - 1);

                // Mark reference values that are put in the field.
                markEscapingParameters(clazz, method, offset, 0);
                break;

            case Instruction.OP_INVOKEVIRTUAL:
            case Instruction.OP_INVOKESPECIAL:
            case Instruction.OP_INVOKESTATIC:
            case Instruction.OP_INVOKEINTERFACE:
            case Instruction.OP_INVOKEDYNAMIC:
                // Mark reference parameters that are modified as parameters
                // of the invoked method.
                // Mark reference values that are escaping as parameters
                // of the invoked method.
                // Mark escaped reference parameters in the invoked method.
                referencingClass    = clazz;
                referencingMethod   = method;
                referencingOffset   = offset;
                referencingPopCount = constantInstruction.stackPopCount(clazz);
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        Clazz referencedClass = stringConstant.referencedClass;

        // If a static initializer may modify anything, so does the referencing
        // method.
        if (referencedClass == null ||
            SideEffectClassChecker.mayHaveSideEffects(clazz,
                                                      referencedClass))
        {
            markAnythingModified(referencingClass, referencingMethod);
        }
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        Clazz referencedClass = classConstant.referencedClass;

        // If a static initializer may modify anything, so does the referencing
        // method.
        if (referencedClass == null ||
            SideEffectClassChecker.mayHaveSideEffects(clazz,
                                                      referencedClass))
        {
            markAnythingModified(referencingClass, referencingMethod);
        }
    }


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        markAnythingModified(referencingClass, referencingMethod);
    }


    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        clazz.constantPoolEntryAccept(fieldrefConstant.u2classIndex, this);
    }


    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        Method referencedMethod = (Method)anyMethodrefConstant.referencedMethod;

        // If the referenced method or a static initializer may modify anything,
        // so does the referencing method.
        if (referencedMethod == null ||
            modifiesAnything(referencedMethod) ||
            SideEffectClassChecker.mayHaveSideEffects(clazz,
                                                      anyMethodrefConstant.referencedClass,
                                                      referencedMethod))
        {
            markAnythingModified(referencingClass, referencingMethod);
        }

        // Do we know the invoked method?
        if (referencedMethod == null)
        {
            // Mark all parameters of the invoking method that are passed to
            // the invoked method, since they may escape or or be modified
            // there.
            for (int parameterOffset = 0; parameterOffset < referencingPopCount; parameterOffset++)
            {
                int stackEntryIndex = referencingPopCount - parameterOffset - 1;

                markEscapingParameters(referencingClass,
                                       referencingMethod,
                                       referencingOffset,
                                       stackEntryIndex);

                markModifiedParameters(referencingClass,
                                       referencingMethod,
                                       referencingOffset,
                                       stackEntryIndex);
            }
        }
        else
        {
            // Remember whether the return value of the method is escaping or
            // modified later on.
            isReturnValueEscaping =
                referenceEscapeChecker.isInstanceEscaping(referencingOffset);

            isReturnValueModified =
                referenceEscapeChecker.isInstanceModified(referencingOffset);

            // Mark parameters of the invoking method that are passed to the
            // invoked method and escaping or modified there.
            anyMethodrefConstant.referencedMethodAccept(parameterMarker);
        }
    }


    // Implementations for ParameterVisitor.

    public void visitParameter(Clazz clazz, Member member, int parameterIndex, int parameterCount, int parameterOffset, int parameterSize, String parameterType, Clazz referencedClass)
    {
        if (!ClassUtil.isInternalPrimitiveType(parameterType.charAt(0)))
        {
            Method method = (Method)member;

            // Is the parameter escaping from the method,
            // or is it returned and then escaping?
            if (isParameterEscaping(method, parameterIndex) ||
                (isParameterReturned(method, parameterIndex) &&
                 isReturnValueEscaping))
            {
                markEscapingParameters(referencingClass,
                                       referencingMethod,
                                       referencingOffset,
                                       parameterSize - parameterOffset - 1);
            }

            // Is the parameter being modified in the method.
            // or is it returned and then modified?
            if (isParameterModified(method, parameterIndex) ||
                (isParameterReturned(method, parameterIndex) &&
                 isReturnValueModified))
            {
                markModifiedParameters(referencingClass,
                                       referencingMethod,
                                       referencingOffset,
                                       parameterSize - parameterOffset - 1);
            }
        }
    }


    // Small utility methods.

    private void reportSideEffect(Clazz clazz, Method method)
    {
        if (extraMemberVisitor != null)
        {
            method.accept(clazz, extraMemberVisitor);
        }
    }

    /**
     * Marks the producing reference parameters (and the classes) of the
     * specified stack entry at the given instruction offset.
     */
    private void markEscapingParameters(Clazz  clazz,
                                        Method method,
                                        int    consumerOffset,
                                        int    stackEntryIndex)
    {
        TracedStack stackBefore = partialEvaluator.getStackBefore(consumerOffset);
        Value       stackEntry  = stackBefore.getTop(stackEntryIndex);

        if (stackEntry.computationalType() == Value.TYPE_REFERENCE)
        {
            ReferenceValue referenceValue = stackEntry.referenceValue();

            // The null reference value may not have a trace value.
            if (referenceValue.isNull() != Value.ALWAYS)
            {
                markEscapingParameters(clazz, method, referenceValue);
            }
        }
    }


    /**
     * Marks the producing parameters (and the classes) of the given
     * reference value.
     */
    private void markEscapingParameters(Clazz          clazz,
                                        Method         method,
                                        ReferenceValue referenceValue)
    {
        TracedReferenceValue   tracedReferenceValue = (TracedReferenceValue)referenceValue;
        InstructionOffsetValue producers            = tracedReferenceValue.getTraceValue().instructionOffsetValue();

        int producerCount = producers.instructionOffsetCount();
        for (int index = 0; index < producerCount; index++)
        {
            if (producers.isMethodParameter(index))
            {
                // We know exactly which parameter is escaping.
                markParameterEscaping(clazz, method, producers.methodParameter(index));
            }
        }
    }


    /**
     * Marks the given parameter as escaping from the given method.
     */
    private void markParameterEscaping(Clazz clazz, Method method, int parameterIndex)
    {
        MethodOptimizationInfo methodOptimizationInfo =
            MethodOptimizationInfo.getMethodOptimizationInfo(method);

        if (!methodOptimizationInfo.isParameterEscaping(parameterIndex) &&
            methodOptimizationInfo instanceof ProgramMethodOptimizationInfo)
        {
            ((ProgramMethodOptimizationInfo)methodOptimizationInfo).setParameterEscaping(parameterIndex);

            // Trigger the repeater if the setter has changed the value.
            if (methodOptimizationInfo.isParameterEscaping(parameterIndex))
            {
                reportSideEffect(clazz, method);
            }
        }
    }


    /**
     * Marks the given parameters as escaping from the given method.
     */
    private void markEscapingParameters(Clazz clazz, Method method, long escapingParameters)
    {
        MethodOptimizationInfo methodOptimizationInfo =
            MethodOptimizationInfo.getMethodOptimizationInfo(method);

        long oldEscapingParameters =
            methodOptimizationInfo.getEscapingParameters();

        if ((~oldEscapingParameters & escapingParameters) != 0 &&
            methodOptimizationInfo instanceof ProgramMethodOptimizationInfo)
        {
            ((ProgramMethodOptimizationInfo)methodOptimizationInfo).updateEscapingParameters(escapingParameters);

            // Trigger the repeater if the setter has changed the value.
            if (methodOptimizationInfo.getEscapingParameters() != oldEscapingParameters)
            {
                reportSideEffect(clazz, method);
            }
        }
    }


    /**
     * Returns whether the given parameter is escaping from the given method.
     */
    public static boolean isParameterEscaping(Method method, int parameterIndex)
    {
        return MethodOptimizationInfo.getMethodOptimizationInfo(method).isParameterEscaping(parameterIndex);
    }


    /**
     * Returns which parameters are escaping from the given method.
     */
    public static long getEscapingParameters(Method method)
    {
        return MethodOptimizationInfo.getMethodOptimizationInfo(method).getEscapingParameters();
    }


    /**
     * Marks the method and the returned reference parameters of the specified
     * stack entry at the given instruction offset.
     */
    private void markReturnedParameters(Clazz  clazz,
                                        Method method,
                                        int    returnOffset,
                                        int    stackEntryIndex)
    {
        TracedStack stackBefore = partialEvaluator.getStackBefore(returnOffset);
        Value       stackEntry  = stackBefore.getTop(stackEntryIndex);

        if (stackEntry.computationalType() == Value.TYPE_REFERENCE)
        {
            ReferenceValue referenceValue = stackEntry.referenceValue();

            // The null reference value may not have a trace value.
            if (referenceValue.isNull() != Value.ALWAYS &&
                mayReturnType(clazz, method, referenceValue))
            {
                markReturnedParameters(clazz, method, referenceValue);
            }
        }
    }


    /**
     * Marks the method and the producing parameters of the given reference
     * value.
     */
    private void markReturnedParameters(Clazz          clazz,
                                        Method         method,
                                        ReferenceValue referenceValue)
    {
        TracedReferenceValue   tracedReferenceValue = (TracedReferenceValue)referenceValue;
        InstructionOffsetValue producers            = tracedReferenceValue.getTraceValue().instructionOffsetValue();

        int producerCount = producers.instructionOffsetCount();
        for (int index = 0; index < producerCount; index++)
        {
            if (producers.isMethodParameter(index))
            {
                // We know exactly which parameter is returned.
                markParameterReturned(clazz, method, producers.methodParameter(index));
            }
            else if (producers.isFieldValue(index))
            {
                markReturnsExternalValues(clazz, method);
            }
            else if (producers.isNewinstance(index) ||
                     producers.isExceptionHandler(index))
            {
                markReturnsNewInstances(clazz, method);
            }
        }
    }


    /**
     * Marks the given parameter as returned from the given method.
     */
    private void markParameterReturned(Clazz clazz, Method method, int parameterIndex)
    {
        MethodOptimizationInfo methodOptimizationInfo =
            MethodOptimizationInfo.getMethodOptimizationInfo(method);

        if (!methodOptimizationInfo.returnsParameter(parameterIndex) &&
            methodOptimizationInfo instanceof ProgramMethodOptimizationInfo)
        {
            ((ProgramMethodOptimizationInfo)methodOptimizationInfo).setParameterReturned(parameterIndex);

            // Trigger the repeater if the setter has changed the value.
            if (methodOptimizationInfo.returnsParameter(parameterIndex))
            {
                reportSideEffect(clazz, method);
            }
        }
    }


    /**
     * Marks the given parameters as returned from the given method.
     */
    private void markReturnedParameters(Clazz clazz, Method method, long returnedParameters)
    {
        MethodOptimizationInfo methodOptimizationInfo =
            MethodOptimizationInfo.getMethodOptimizationInfo(method);

        long oldReturnedParameters =
            methodOptimizationInfo.getReturnedParameters();

        if ((~oldReturnedParameters & returnedParameters) != 0 &&
            methodOptimizationInfo instanceof ProgramMethodOptimizationInfo)
        {
            ((ProgramMethodOptimizationInfo)methodOptimizationInfo).updateReturnedParameters(returnedParameters);

            // Trigger the repeater if the setter has changed the value.
            if (methodOptimizationInfo.getReturnedParameters() != oldReturnedParameters)
            {
                reportSideEffect(clazz, method);
            }
        }
    }


    /**
     * Returns whether the given parameter is returned from the given method.
     */
    public static boolean isParameterReturned(Method method, int parameterIndex)
    {
        return MethodOptimizationInfo.getMethodOptimizationInfo(method).returnsParameter(parameterIndex);
    }


    /**
     * Returns which parameters are returned from the given method.
     */
    public static long getReturnedParameters(Method method)
    {
        return MethodOptimizationInfo.getMethodOptimizationInfo(method).getReturnedParameters();
    }


    /**
     * Marks that the given method returns new instances (created inside the
     * method).
     */
    private void markReturnsNewInstances(Clazz clazz, Method method)
    {
        MethodOptimizationInfo methodOptimizationInfo =
            MethodOptimizationInfo.getMethodOptimizationInfo(method);

        if (!methodOptimizationInfo.returnsNewInstances() &&
            methodOptimizationInfo instanceof ProgramMethodOptimizationInfo)
        {
            ((ProgramMethodOptimizationInfo)methodOptimizationInfo).setReturnsNewInstances();

            // Trigger the repeater if the setter has changed the value.
            if (methodOptimizationInfo.returnsNewInstances())
            {
                reportSideEffect(clazz, method);
            }
        }
    }


    /**
     * Returns whether the given method returns new instances (created inside
     * the method).
     */
    public static boolean returnsNewInstances(Method method)
    {
        return MethodOptimizationInfo.getMethodOptimizationInfo(method).returnsNewInstances();
    }


    /**
     * Marks that the given method returns external reference values (not
     * parameter or new instance).
     */
    private void markReturnsExternalValues(Clazz clazz, Method method)
    {
        MethodOptimizationInfo methodOptimizationInfo =
            MethodOptimizationInfo.getMethodOptimizationInfo(method);

        if (!methodOptimizationInfo.returnsExternalValues() &&
            methodOptimizationInfo instanceof ProgramMethodOptimizationInfo)
        {
            ((ProgramMethodOptimizationInfo)methodOptimizationInfo).setReturnsExternalValues();

            // Trigger the repeater if the setter has changed the value.
            if (methodOptimizationInfo.returnsExternalValues())
            {
                reportSideEffect(clazz, method);
            }
        }
    }


    /**
     * Returns whether the given method returns external reference values
     * (not parameter or new instance).
     */
    public static boolean returnsExternalValues(Method method)
    {
        return MethodOptimizationInfo.getMethodOptimizationInfo(method).returnsExternalValues();
    }


    /**
     * Returns whether the given method may return the given type of reference
     * value
     */
    private boolean mayReturnType(Clazz          clazz,
                                  Method         method,
                                  ReferenceValue referenceValue)
    {
        String returnType =
            ClassUtil.internalMethodReturnType(method.getDescriptor(clazz));

        Clazz[] referencedClasses = method instanceof ProgramMethod ?
            ((ProgramMethod)method).referencedClasses :
            ((LibraryMethod)method).referencedClasses;

        Clazz referencedClass =
            referencedClasses == null ||
            !ClassUtil.isInternalClassType(returnType) ? null :
                referencedClasses[referencedClasses.length - 1];

        return referenceValue.instanceOf(returnType,
                                         referencedClass) != Value.NEVER;
    }


    /**
     * Marks the producing reference parameters of the specified stack entry at
     * the given instruction offset.
     */
    private void markModifiedParameters(Clazz clazz,
                                        Method method,
                                        int    offset,
                                        int    stackEntryIndex)
    {
        TracedStack stackBefore = partialEvaluator.getStackBefore(offset);
        Value       stackEntry  = stackBefore.getTop(stackEntryIndex);

        if (stackEntry.computationalType() == Value.TYPE_REFERENCE)
        {
            ReferenceValue referenceValue = stackEntry.referenceValue();

            // The null reference value may not have a trace value.
            if (referenceValue.isNull() != Value.ALWAYS)
            {
                markModifiedParameters(clazz, method, referenceValue);
            }
        }
    }


    /**
     * Marks the producing parameters of the given reference value.
     */
    private void markModifiedParameters(Clazz clazz,
                                        Method         method,
                                        ReferenceValue referenceValue)
    {
        TracedReferenceValue   tracedReferenceValue = (TracedReferenceValue)referenceValue;
        InstructionOffsetValue producers            = tracedReferenceValue.getTraceValue().instructionOffsetValue();

        int producerCount = producers.instructionOffsetCount();
        for (int index = 0; index < producerCount; index++)
        {
            if (producers.isMethodParameter(index))
            {
                // We know exactly which parameter is being modified.
                markParameterModified(clazz, method, producers.methodParameter(index));
            }
            else if (!producers.isNewinstance(index) &&
                     !producers.isExceptionHandler(index))
            {
                // If some unknown instance is modified, any escaping parameters
                // may be modified.
                markModifiedParameters(clazz, method, getEscapingParameters(method));
                markAnythingModified(clazz, method);
            }
        }
    }


    /**
     * Marks the given parameter as modified by the given method.
     */
    private void markParameterModified(Clazz clazz, Method method, int parameterIndex)
    {
        MethodOptimizationInfo methodOptimizationInfo =
            MethodOptimizationInfo.getMethodOptimizationInfo(method);

        if (!methodOptimizationInfo.isParameterModified(parameterIndex) &&
            methodOptimizationInfo instanceof ProgramMethodOptimizationInfo)
        {
            ((ProgramMethodOptimizationInfo)methodOptimizationInfo).setParameterModified(parameterIndex);

            // Trigger the repeater if the setter has changed the value.
            if (methodOptimizationInfo.isParameterModified(parameterIndex))
            {
                reportSideEffect(clazz, method);
            }
        }
    }


    /**
     * Marks the given parameters as modified by the given method.
     */
    private void markModifiedParameters(Clazz clazz, Method method, long modifiedParameters)
    {
        MethodOptimizationInfo methodOptimizationInfo =
            MethodOptimizationInfo.getMethodOptimizationInfo(method);

        long oldModifiedParameters =
            methodOptimizationInfo.getModifiedParameters();

        if ((~oldModifiedParameters & modifiedParameters) != 0 &&
            methodOptimizationInfo instanceof ProgramMethodOptimizationInfo)
        {
            ((ProgramMethodOptimizationInfo)methodOptimizationInfo).updateModifiedParameters(modifiedParameters);

            // Trigger the repeater if the setter has changed the value.
            if (methodOptimizationInfo.getModifiedParameters() != oldModifiedParameters)
            {
                reportSideEffect(clazz, method);
            }
        }
    }


    /**
     * Returns whether the given parameter is modified by the given method.
     */
    public static boolean isParameterModified(Method method, int parameterIndex)
    {
        return MethodOptimizationInfo.getMethodOptimizationInfo(method).isParameterModified(parameterIndex);
    }


    /**
     * Returns which parameters are modified by the given method.
     */
    public static long getModifiedParameters(Method method)
    {
        return MethodOptimizationInfo.getMethodOptimizationInfo(method).getModifiedParameters();
    }


    /**
     * Marks that anything may be modified by the given method.
     */
    private void markAnythingModified(Clazz clazz, Method method)
    {
        MethodOptimizationInfo methodOptimizationInfo =
            MethodOptimizationInfo.getMethodOptimizationInfo(method);

        if (!methodOptimizationInfo.modifiesAnything() &&
            methodOptimizationInfo instanceof ProgramMethodOptimizationInfo)
        {
            ((ProgramMethodOptimizationInfo)methodOptimizationInfo).setModifiesAnything();

            // Trigger the repeater if the setter has changed the value.
            if (methodOptimizationInfo.modifiesAnything())
            {
                reportSideEffect(clazz, method);
            }
        }
    }


    /**
     * Returns whether anything may be modified by the given method. This takes
     * into account the side effects of static initializers, except the static
     * initializer of the invoked method (because it is better checked
     * explicitly as a function of the referencing class).
     *
     * @see SideEffectClassChecker#mayHaveSideEffects(Clazz, Clazz, Member)
     */
    public static boolean modifiesAnything(Method method)
    {
        return MethodOptimizationInfo.getMethodOptimizationInfo(method).modifiesAnything();
    }
}
