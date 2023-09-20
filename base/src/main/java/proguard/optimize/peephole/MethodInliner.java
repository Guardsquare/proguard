/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.optimize.*;
import proguard.optimize.info.*;
import proguard.util.ProcessingFlagSetter;
import proguard.util.ProcessingFlags;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Stack;

/**
 * This AttributeVisitor is an abstract class representing a visitor considering to inline each method that it
 * visits in its usage sites. The behavior of whether a class is considered for inlining is controlled
 * by overriding the shouldInline method.
 *
 * There are some additional technical constraints imposed on whether the method is actually inlined
 * (see visitProgramMethod).
 *
 * @see SuperInvocationMarker
 * @see BackwardBranchMarker
 * @see AccessMethodMarker
 * @see SideEffectClassMarker
 * @author Eric Lafortune
 */
abstract public class MethodInliner
implements            AttributeVisitor,
                      InstructionVisitor,
                      ConstantVisitor,
                      MemberVisitor,
                      ExceptionInfoVisitor,
                      LineNumberInfoVisitor
{
    protected static final int MAXIMUM_INLINED_CODE_LENGTH_JVM     = Integer.parseInt(System.getProperty("maximum.inlined.code.length",      "8"));
    protected static final int MAXIMUM_INLINED_CODE_LENGTH_android = Integer.parseInt(System.getProperty("maximum.inlined.code.length",     "32"));
    protected static final int MAXIMUM_RESULTING_CODE_LENGTH_JSE   = Integer.parseInt(System.getProperty("maximum.resulting.code.length", "7000"));
    protected static final int MAXIMUM_RESULTING_CODE_LENGTH_JME   = Integer.parseInt(System.getProperty("maximum.resulting.code.length", "2000"));
    protected static final int MAXIMUM_RESULTING_CODE_LENGTH_JVM   = 65535;

    static final int METHOD_DUMMY_START_LINE_NUMBER = 0;
    static final int INLINED_METHOD_END_LINE_NUMBER = -1;

    private static final Logger logger = LogManager.getLogger(MethodInliner.class);

    protected final boolean            microEdition;
    protected final boolean            android;
    protected final int                maxResultingCodeLength;
    protected final boolean            allowAccessModification;
    protected final boolean            usesOptimizationInfo;
    protected final InstructionVisitor extraInlinedInvocationVisitor;

    private final CodeAttributeComposer codeAttributeComposer  = new CodeAttributeComposer();
    private final MemberVisitor         accessMethodMarker     = new OptimizationInfoMemberFilter(
                                                                 new AllAttributeVisitor(
                                                                 new AllInstructionVisitor(
                                                                 new MultiInstructionVisitor(
                                                                     new SuperInvocationMarker(),
                                                                     new AccessMethodMarker()
                                                                 ))));
    private final AttributeVisitor      methodInvocationMarker = new AllInstructionVisitor(
                                                                 new MethodInvocationMarker());
    private final StackSizeComputer     stackSizeComputer      = new StackSizeComputer();

    private ProgramClass       targetClass;
    private ProgramMethod      targetMethod;
    private ConstantAdder      constantAdder;
    private ExceptionInfoAdder exceptionInfoAdder;
    private int                estimatedResultingCodeLength;
    private boolean            inlining;
    private Stack              inliningMethods              = new Stack();
    private boolean            emptyInvokingStack;
    private boolean            coveredByCatchAllHandler;
    private int                exceptionInfoCount;
    private int                uninitializedObjectCount;
    private int                variableOffset;
    private boolean            inlined;
    private boolean            inlinedAny;
    private boolean            copiedLineNumbers;
    private String             source;
    private int                minimumLineNumberIndex;


    /**
     * Creates a new MethodInliner.
     *
     * @param microEdition            Indicates whether the resulting code is
     *                                targeted at Java Micro Edition.
     * @param android                 Indicates whether the resulting code is
     *                                targeted at the Dalvik VM.
     * @param allowAccessModification Indicates whether the access modifiers of
     *                                classes and class members can be changed
     *                                in order to inline methods.
     */
    public MethodInliner(boolean microEdition,
                         boolean android,
                         boolean allowAccessModification)
    {
        this(microEdition,
             android,
             allowAccessModification,
             null);
    }


    /**
     * Creates a new MethodInliner.
     *
     * @param microEdition                  Indicates whether the resulting code is
     *                                      targeted at Java Micro Edition.
     * @param android                       Indicates whether the resulting code is
     *                                      targeted at the Dalvik VM.
     * @param allowAccessModification       Indicates whether the access modifiers of
     *                                      classes and class members can be changed
     *                                      in order to inline methods.
     * @param extraInlinedInvocationVisitor An optional extra visitor for all
     *                                      inlined invocation instructions.
     */
    public MethodInliner(boolean            microEdition,
                         boolean            android,
                         boolean            allowAccessModification,
                         InstructionVisitor extraInlinedInvocationVisitor)
    {
        this(microEdition,
             android,
             defaultMaxResultingCodeLength(microEdition),
             allowAccessModification,
             true,
             extraInlinedInvocationVisitor);
    }


    /**
     * Creates a new MethodInliner.
     *
     * @param microEdition                  Indicates whether the resulting code is
     *                                      targeted at Java Micro Edition.
     * @param android                       Indicates whether the resulting code is
     *                                      targeted at the Dalvik VM.
     * @param maxResultingCodeLength        Configures the inliner with a max resulting
     *                                      code length.
     * @param allowAccessModification       Indicates whether the access modifiers of
     *                                      classes and class members can be changed
     *                                      in order to inline methods.
     * @param usesOptimizationInfo          Indicates whether this inliner needs to perform checks
     *                                      that require optimization info.
     * @param extraInlinedInvocationVisitor An optional extra visitor for all
     *                                      inlined invocation instructions.
     */
    public MethodInliner(boolean            microEdition,
                         boolean            android,
                         int                maxResultingCodeLength,
                         boolean            allowAccessModification,
                         boolean            usesOptimizationInfo,
                         InstructionVisitor extraInlinedInvocationVisitor)
    {
        if (maxResultingCodeLength > MAXIMUM_RESULTING_CODE_LENGTH_JVM)
        {
            throw new IllegalArgumentException("Maximum resulting code length cannot exceed " + MAXIMUM_RESULTING_CODE_LENGTH_JVM);
        }
        this.microEdition                  = microEdition;
        this.android                       = android;
        this.maxResultingCodeLength        = maxResultingCodeLength;
        this.allowAccessModification       = allowAccessModification;
        this.usesOptimizationInfo          = usesOptimizationInfo;
        this.extraInlinedInvocationVisitor = extraInlinedInvocationVisitor;
    }

    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // TODO: Remove this when the method inliner has stabilized.
        // Catch any unexpected exceptions from the actual visiting method.
        try
        {
            // Process the code.
            visitCodeAttribute0(clazz, method, codeAttribute);
        }
        catch (RuntimeException ex)
        {
            logger.error("Unexpected error while inlining method:");
            logger.error("  Target class   = [{}]", targetClass.getName());
            logger.error("  Target method  = [{}{}]", targetMethod.getName(targetClass), targetMethod.getDescriptor(targetClass));
            if (inlining)
            {
                logger.error("  Inlined class  = [{}]", clazz.getName());
                logger.error("  Inlined method = [{}{}]", method.getName(clazz), method.getDescriptor(clazz));
            }
            logger.error("  Exception      = [{}] ({})", ex.getClass().getName(), ex.getMessage(), ex);

            logger.error("Not inlining this method");

            logger.debug("{}", () -> {
                StringWriter sw = new StringWriter();
                targetMethod.accept(targetClass, new ClassPrinter(new PrintWriter(sw)));
                return sw.toString();
            });
            if (inlining)
            {
                logger.debug("{}", () -> {
                    StringWriter sw = new StringWriter();
                    method.accept(clazz, new ClassPrinter(new PrintWriter(sw)));
                    return sw.toString();
                });
            }

            if (logger.getLevel().isLessSpecificThan(Level.DEBUG))
            {
                throw ex;
            }
        }
    }


    public void visitCodeAttribute0(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        if (!inlining)
        {
//            codeAttributeComposer.DEBUG = DEBUG =
//                clazz.getName().equals("abc/Def") &&
//                method.getName(clazz).equals("abc");

            targetClass                  = (ProgramClass)clazz;
            targetMethod                 = (ProgramMethod)method;
            constantAdder                = new ConstantAdder(targetClass);
            exceptionInfoAdder           = new ExceptionInfoAdder(targetClass, codeAttributeComposer);
            estimatedResultingCodeLength = codeAttribute.u4codeLength;
            inliningMethods.clear();
            uninitializedObjectCount     = method.getName(clazz).equals(ClassConstants.METHOD_NAME_INIT) ? 1 : 0;
            inlinedAny                   = false;
            codeAttributeComposer.reset();
            stackSizeComputer.visitCodeAttribute(clazz, method, codeAttribute);

            // Append the body of the code.
            copyCode(clazz, method, codeAttribute);

            // Update the code attribute if any code has been inlined.
            if (inlinedAny)
            {
                codeAttributeComposer.visitCodeAttribute(clazz, method, codeAttribute);

                // Update the super/private/package/protected accessing flags.

                if (usesOptimizationInfo)
                {
                    method.accept(clazz, accessMethodMarker);
                }
            }

            targetClass   = null;
            targetMethod  = null;
            constantAdder = null;
        }

        // Only inline the method if
        // 1. The shouldInline method returns true AND
        // 2. The resulting estimated code attribute length is below the specified limit
        else if (shouldInline(clazz, method, codeAttribute) &&
                 estimatedResultingCodeLength + codeAttribute.u4codeLength < maxResultingCodeLength)
        {
            logger.debug("MethodInliner: inlining [{}.{}{}] in [{}.{}{}]",
                         clazz.getName(),
                         method.getName(clazz),
                         method.getDescriptor(clazz),
                         targetClass.getName(),
                         targetMethod.getName(targetClass),
                         targetMethod.getDescriptor(targetClass)
            );

            // Ignore the removal of the original method invocation,
            // the addition of the parameter setup, and
            // the modification of a few inlined instructions.
            estimatedResultingCodeLength += codeAttribute.u4codeLength;

            // Append instructions to store the parameters.
            storeParameters(clazz, method);

            // Inline the body of the code.
            copyCode(clazz, method, codeAttribute);

            inlined    = true;
            inlinedAny = true;
        }
    }


    public void visitLineNumberTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute lineNumberTableAttribute)
    {
        // Remember the source if we're inlining a method.
        source = inlining ?
            clazz.getName()                                 + '.' +
            method.getName(clazz)                           +
            method.getDescriptor(clazz)                     + ':' +
            lineNumberTableAttribute.getLowestLineNumber()  + ':' +
            lineNumberTableAttribute.getHighestLineNumber() :
            null;

        // Insert all line numbers, possibly partly before previously inserted
        // line numbers.
        lineNumberTableAttribute.lineNumbersAccept(clazz, method, codeAttribute, this);

        copiedLineNumbers = true;
    }


    /**
     * Appends instructions to pop the parameters for the given method, storing
     * them in new local variables.
     */
    private void storeParameters(Clazz clazz, Method method)
    {
        String descriptor = method.getDescriptor(clazz);

        boolean isStatic =
            (method.getAccessFlags() & AccessConstants.STATIC) != 0;

        // Count the number of parameters, taking into account their categories.
        int parameterSize   = ClassUtil.internalMethodParameterSize(descriptor);
        int parameterOffset = isStatic ? 0 : 1;

        // Store the parameter types.
        String[] parameterTypes = new String[parameterSize];

        InternalTypeEnumeration internalTypeEnumeration =
            new InternalTypeEnumeration(descriptor);

        for (int parameterIndex = 0; parameterIndex < parameterSize; parameterIndex++)
        {
            String parameterType = internalTypeEnumeration.nextType();
            parameterTypes[parameterIndex] = parameterType;
            if (ClassUtil.internalTypeSize(parameterType) == 2)
            {
                parameterIndex++;
            }
        }

        codeAttributeComposer.beginCodeFragment(parameterSize+1);

        // Go over the parameter types backward, storing the stack entries
        // in their corresponding variables.
        for (int parameterIndex = parameterSize-1; parameterIndex >= 0; parameterIndex--)
        {
            String parameterType = parameterTypes[parameterIndex];
            if (parameterType != null)
            {
                byte opcode;
                switch (parameterType.charAt(0))
                {
                    case TypeConstants.BOOLEAN:
                    case TypeConstants.BYTE:
                    case TypeConstants.CHAR:
                    case TypeConstants.SHORT:
                    case TypeConstants.INT:
                        opcode = Instruction.OP_ISTORE;
                        break;

                    case TypeConstants.LONG:
                        opcode = Instruction.OP_LSTORE;
                        break;

                    case TypeConstants.FLOAT:
                        opcode = Instruction.OP_FSTORE;
                        break;

                    case TypeConstants.DOUBLE:
                        opcode = Instruction.OP_DSTORE;
                        break;

                    default:
                        opcode = Instruction.OP_ASTORE;
                        break;
                }

                codeAttributeComposer.appendInstruction(parameterSize-parameterIndex-1,
                                                        new VariableInstruction(opcode, variableOffset + parameterOffset + parameterIndex));
            }
        }

        // Put the 'this' reference in variable 0 (plus offset).
        if (!isStatic)
        {
            codeAttributeComposer.appendInstruction(parameterSize,
                                                    new VariableInstruction(Instruction.OP_ASTORE, variableOffset));
        }

        codeAttributeComposer.endCodeFragment();
    }


    /**
     * Appends the code of the given code attribute.
     */
    private void copyCode(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // The code may expand, due to expanding constant and variable
        // instructions.
        codeAttributeComposer.beginCodeFragment(codeAttribute.u4codeLength);

        // Copy the instructions.
        codeAttribute.instructionsAccept(clazz, method, this);

        // Append a label just after the code.
        codeAttributeComposer.appendLabel(codeAttribute.u4codeLength);

        // Copy the exceptions.
        codeAttribute.exceptionsAccept(clazz, method, exceptionInfoAdder);

        // Copy the processing flags that need to be copied as well.
        targetMethod.accept(targetClass, new ProcessingFlagSetter(method.getProcessingFlags() & ProcessingFlags.COPYABLE_PROCESSING_FLAGS));

        // Copy the line numbers.
        copiedLineNumbers = false;

        // The line numbers need to be inserted sequentially.
        minimumLineNumberIndex = 0;

        codeAttribute.attributesAccept(clazz, method, this);

        // Make sure we at least have some entry at the start of the method.
        if (!copiedLineNumbers)
        {
            String source = inlining ?
                clazz.getName()             + '.' +
                method.getName(clazz)       +
                method.getDescriptor(clazz) +
                ":0:0" :
                null;

            minimumLineNumberIndex =
                codeAttributeComposer.insertLineNumber(minimumLineNumberIndex,
                    new ExtendedLineNumberInfo(0,
                                               METHOD_DUMMY_START_LINE_NUMBER,
                                               source)) + 1;
        }

        // Add a marker at the end of an inlined method.
        // The marker will be corrected in LineNumberLinearizer,
        // so it points to the line of the enclosing method.
        if (inlining)
        {
            String source =
                clazz.getName()             + '.' +
                method.getName(clazz)       +
                method.getDescriptor(clazz) +
                ":0:0";

            minimumLineNumberIndex =
                codeAttributeComposer.insertLineNumber(minimumLineNumberIndex,
                    new ExtendedLineNumberInfo(codeAttribute.u4codeLength,
                                               INLINED_METHOD_END_LINE_NUMBER,
                                               source)) + 1;
        }

        codeAttributeComposer.endCodeFragment();
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
        codeAttributeComposer.appendInstruction(offset, instruction);
    }


    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
    {
        // Are we inlining this instruction?
        if (inlining)
        {
            // Replace any return instructions by branches to the end of the code.
            switch (simpleInstruction.opcode)
            {
                case Instruction.OP_IRETURN:
                case Instruction.OP_LRETURN:
                case Instruction.OP_FRETURN:
                case Instruction.OP_DRETURN:
                case Instruction.OP_ARETURN:
                case Instruction.OP_RETURN:
                    // Are we not at the last instruction?
                    if (offset < codeAttribute.u4codeLength-1)
                    {
                        // Replace the return instruction by a branch instruction.
                        Instruction branchInstruction =
                            new BranchInstruction(Instruction.OP_GOTO_W,
                                                  codeAttribute.u4codeLength - offset);

                        codeAttributeComposer.appendInstruction(offset,
                                                                branchInstruction);
                    }
                    else
                    {
                        // Just leave out the instruction, but put in a label,
                        // for the sake of any other branch instructions.
                        codeAttributeComposer.appendLabel(offset);
                    }

                    return;
            }
        }

        codeAttributeComposer.appendInstruction(offset, simpleInstruction);
    }


    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction)
    {
        // Are we inlining this instruction?
        if (inlining)
        {
            // Update the variable index.
            variableInstruction.variableIndex += variableOffset;
        }

        codeAttributeComposer.appendInstruction(offset, variableInstruction);
    }


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        // Is it a method invocation?
        switch (constantInstruction.opcode)
        {
            case Instruction.OP_NEW:
                uninitializedObjectCount++;
                break;

            case Instruction.OP_INVOKEVIRTUAL:
            case Instruction.OP_INVOKESPECIAL:
            case Instruction.OP_INVOKESTATIC:
            case Instruction.OP_INVOKEINTERFACE:
                // See if we can inline it.
                inlined = false;

                // Append a label, in case the invocation will be inlined.
                codeAttributeComposer.appendLabel(offset);

                emptyInvokingStack =
                    !inlining &&
                    stackSizeComputer.isReachable(offset) &&
                    stackSizeComputer.getStackSizeAfter(offset) == 0;

                variableOffset += codeAttribute.u2maxLocals;

                // Check if the method invocation is covered by a catch-all
                // exception handler.
                coveredByCatchAllHandler = false;
                exceptionInfoCount       = 0;
                codeAttribute.exceptionsAccept(clazz, method, offset, this);
                coveredByCatchAllHandler = exceptionInfoCount <= 0 || coveredByCatchAllHandler;

                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);

                variableOffset -= codeAttribute.u2maxLocals;

                // Was the method inlined?
                if (inlined)
                {
                    if (extraInlinedInvocationVisitor != null)
                    {
                        extraInlinedInvocationVisitor.visitConstantInstruction(clazz, method, codeAttribute, offset, constantInstruction);
                    }

                    // The invocation itself is no longer necessary.
                    return;
                }

                break;
        }

        // Are we inlining this instruction?
        if (inlining)
        {
            // Make sure the constant is present in the constant pool of the
            // target class.
            constantInstruction.constantIndex =
                constantAdder.addConstant(clazz, constantInstruction.constantIndex);
        }

        codeAttributeComposer.appendInstruction(offset, constantInstruction);
    }


    // Implementations for ConstantVisitor.

    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        anyMethodrefConstant.referencedMethodAccept(this);
    }


    // Implementations for MemberVisitor.

    public void visitAnyMember(Clazz Clazz, Member member) {}


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        int accessFlags = programMethod.getAccessFlags();

        logger.trace("MethodInliner: checking [{}.{}{}] in [{}.{}{}]",
                     programClass.getName(),
                     programMethod.getName(programClass),
                     programMethod.getDescriptor(programClass),
                     targetClass.getName(),
                     targetMethod.getName(targetClass),
                     targetMethod.getDescriptor(targetClass)
        );

        if (DEBUG("Access?")                                                                      &&

            // Only inline the method if it is private, static, or final.
            // This currently precludes default interface methods, because
            // they can't be final.
            (accessFlags & (AccessConstants.PRIVATE |
                            AccessConstants.STATIC  |
                            AccessConstants.FINAL)) != 0                                       &&

            DEBUG("Interface?")                                                                   &&

            // Methods in interfaces should not be inlined since this can potentially
            // lead to other methods in the interface needing broadened visibility,
            // which can lead to either compilation errors during output writing
            // or various issues at runtime.
            (programClass.getAccessFlags() & AccessConstants.INTERFACE) == 0                   &&

            DEBUG("Synchronized?")                                                                &&

            // Only inline the method if it is not synchronized, etc.
            (accessFlags & (AccessConstants.SYNCHRONIZED  |
                            AccessConstants.NATIVE        |
                            AccessConstants.ABSTRACT)) == 0                                    &&

            DEBUG("Init?")                                                                        &&

            // Don't inline an <init> method, except in an <init> method in the
            // same class.
//            (!programMethod.getName(programClass).equals(ClassConstants.METHOD_NAME_INIT) ||
//             (programClass.equals(targetClass) &&
//              targetMethod.getName(targetClass).equals(ClassConstants.METHOD_NAME_INIT))) &&
            !programMethod.getName(programClass).equals(ClassConstants.METHOD_NAME_INIT)          &&

            DEBUG("Self?")                                                                        &&

            // Don't inline a method into itself.
            (!programMethod.equals(targetMethod) ||
             !programClass.equals(targetClass))                                                   &&

            DEBUG("Recurse?")                                                                     &&

            // Only inline the method if it isn't recursing.
            !inliningMethods.contains(programMethod)                                              &&

            DEBUG("Version?")                                                                     &&

            // Only inline the method if its target class has at least the
            // same version number as the source class, in order to avoid
            // introducing incompatible constructs.
            targetClass.u4version >= programClass.u4version                                       &&

            DEBUG("Super?")                                                                       &&

            // The below checks require optimization info to be set.
            (!usesOptimizationInfo || (

            // Don't inline methods that must be preserved.
            !KeepMarker.isKept(programMethod)                                                     &&

            // Only inline the method if it doesn't invoke a super method or a
            // dynamic method, or if it is in the same class.
            (!SuperInvocationMarker.invokesSuperMethods(programMethod) &&
             !DynamicInvocationMarker.invokesDynamically(programMethod) ||
             programClass.equals(targetClass))                                                    &&

            DEBUG("Branch?")                                                                      &&

            // Only inline the method if it doesn't branch backward while there
            // are uninitialized objects.
            (!BackwardBranchMarker.branchesBackward(programMethod) ||
             uninitializedObjectCount == 0)                                                       &&

            DEBUG("Access private?")                                                              &&

            // Only inline if the code access of the inlined method allows it.
            (allowAccessModification ||
             ((!AccessMethodMarker.accessesPrivateCode(programMethod) ||
               programClass.equals(targetClass)) &&

              (!AccessMethodMarker.accessesPackageCode(programMethod) ||
               ClassUtil.internalPackageName(programClass.getName()).equals(
               ClassUtil.internalPackageName(targetClass.getName())))))                           &&

            DEBUG("Access private in subclass?")                                                  &&

            // Only inline a method from a superclass if it doesn't access
            // private code (with invokespecial), because we can't fix the
            // invocation. (test2172) [DGD-1258]
            (!AccessMethodMarker.accessesPrivateCode(programMethod) ||
             programClass.equals(targetClass)                       ||
             !targetClass.extendsOrImplements(programClass))                                      &&

            DEBUG("Access protected?")                                                            &&

            // Only inline code that accesses protected code into the same
            // class.
            (!AccessMethodMarker.accessesProtectedCode(programMethod) ||
             programClass.equals(targetClass))                                                    &&

            DEBUG("Synchronization?")                                                             &&

            // if the method to be inlined has a synchronized block only inline it into
            // the target method if its invocation is covered by a catchall handler or
            // none at all. This might happen if the target method has been obfuscated
            // with fake exception handlers.
            (!SynchronizedBlockMethodMarker.hasSynchronizedBlock(programMethod) ||
             coveredByCatchAllHandler)                                                            &&

            DEBUG("Final fields?")                                                                &&

            // Methods assigning final fields cannot be inlined, at least on Android
            // this leads to VerifyErrors at runtime.
            // This should normally not happen anyways, but some tools modify/generate
            // bytecode that would lead to such situations, e.g. jacoco, see DGD-561.
            !FinalFieldAssignmentMarker.assignsFinalField(programMethod)                          &&

            DEBUG("Catch?")                                                                       &&

            // Only inline the method if it doesn't catch exceptions, or if it
            // is invoked with an empty stack.
            (!CatchExceptionMarker.catchesExceptions(programMethod) ||
             emptyInvokingStack)                                                                  &&

            DEBUG("Stack?")                                                                       &&

            // Only inline the method if it always returns with an empty
            // stack.
            !NonEmptyStackReturnMarker.returnsWithNonEmptyStack(programMethod)                    &&

            DEBUG("Side effects?")                                                                &&

            // Only inline the method if its related static initializers don't
            // have any side effects.
            !SideEffectClassChecker.mayHaveSideEffects(targetClass, programClass, programMethod))))
        {
            boolean oldInlining = inlining;

            inlining = true;
            inliningMethods.push(programMethod);

            // Inline the method body.
            programMethod.attributesAccept(programClass, this);

            if (usesOptimizationInfo)
            {
                // Update the optimization information of the target method.
                if (!KeepMarker.isKept(targetMethod))
                {
                    ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(targetMethod)
                        .merge(MethodOptimizationInfo.getMethodOptimizationInfo(programMethod));
                }

                // Increment the invocation count of referenced methods again,
                // since they are now invoked from the inlined code too.
                programMethod.attributesAccept(programClass, methodInvocationMarker);
            }

            inlining = oldInlining;
            inliningMethods.pop();
        }
        else if (programMethod.getName(programClass).equals(ClassConstants.METHOD_NAME_INIT))
        {
            uninitializedObjectCount--;
        }
    }

    @Override
    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        if (libraryMethod.getName(libraryClass).equals(ClassConstants.METHOD_NAME_INIT))
        {
            uninitializedObjectCount--;
        }
    }


    // Implementations for LineNumberInfoVisitor.

    public void visitLineNumberInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberInfo lineNumberInfo)
    {
        try
        {
            String newSource = lineNumberInfo.getSource() != null ?
                lineNumberInfo.getSource() :
                source;

            LineNumberInfo newLineNumberInfo = newSource != null ?
                new ExtendedLineNumberInfo(lineNumberInfo.u2startPC,
                                           lineNumberInfo.u2lineNumber,
                                           newSource) :
                new LineNumberInfo(lineNumberInfo.u2startPC,
                                   lineNumberInfo.u2lineNumber);

            minimumLineNumberIndex =
                codeAttributeComposer.insertLineNumber(minimumLineNumberIndex, newLineNumberInfo) + 1;
        }
        catch (IllegalArgumentException e)
        {
            if (logger.getLevel().isLessSpecificThan(Level.DEBUG))
            {
                logger.error("Invalid line number while inlining method:");
                logger.error("  Target class   = [{}]", targetClass.getName());
                logger.error("  Target method  = [{}{}]", targetMethod.getName(targetClass), targetMethod.getDescriptor(targetClass));
                if (inlining)
                {
                    logger.error("  Inlined class  = [{}]", clazz.getName());
                    logger.error("  Inlined method = [{}{}]", method.getName(clazz), method.getDescriptor(clazz));
                }
                logger.error("  Exception      = [{}] ({})", e.getClass().getName(), e.getMessage());
            }
        }
    }


    // Implementations for ExceptionInfoVisitor.

    public void visitExceptionInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, ExceptionInfo exceptionInfo)
    {
        exceptionInfoCount++;
        coveredByCatchAllHandler |= exceptionInfo.u2catchType == 0;
    }


    // Small helper methods.

    private static int defaultMaxResultingCodeLength(boolean microEdition)
    {
        return microEdition ? MAXIMUM_RESULTING_CODE_LENGTH_JME : MAXIMUM_RESULTING_CODE_LENGTH_JSE;
    }

    /**
     * Returns true, while printing out the given debug message.
     */
    private boolean DEBUG(String string)
    {
        logger.trace("  {}", string);

        return true;
    }


    /**
     * Returns whether the method with the given descriptor returns an int-like (boolean,
     * byte, char or short) value.
     */
    private boolean returnsIntLike(String methodDescriptor)
    {
        char   returnChar =  methodDescriptor.charAt(methodDescriptor.length() - 1);
        return returnChar == TypeConstants.BOOLEAN ||
               returnChar == TypeConstants.BYTE    ||
               returnChar == TypeConstants.CHAR    ||
               returnChar == TypeConstants.SHORT;
    }


    /**
     * Indicates whether this method should be inlined. Subclasses can overwrite
     * this method to change which methods are inlined.
     *
     * Note that the method will always still first be tested on whether they can technically
     * be inlined (see `visitProgramMethod`) and only then will this method be called to decide
     * whether to actually inline or not.
     *
     * @param method The method that is eligible for inlining.
     */
    abstract protected boolean shouldInline(Clazz clazz, Method method, CodeAttribute codeAttribute);
}
