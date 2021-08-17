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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.ClassEstimates;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.evaluation.*;
import proguard.evaluation.value.*;
import proguard.util.ArrayUtil;

/**
 * This AttributeVisitor finds all the reference types that are expected on the
 * stack, based on their consuming instructions.
 *
 * @author Eric Lafortune
 */
public class ExpectedStackTypeFinder
implements   AttributeVisitor,
             InstructionVisitor,
             ConstantVisitor,
             MemberVisitor
{
    private static final Logger logger = LogManager.getLogger(ExpectedStackTypeFinder.class);


    protected final PartialEvaluator partialEvaluator;
    private   final boolean          runPartialEvaluator;

    private ReferenceValue[] expectedTypes = new ReferenceValue[ClassEstimates.TYPICAL_CODE_LENGTH];

    // Parameters and return values for visitor methods.
    private int     referencingOffset;
    private boolean isPut;
    private boolean isStatic;
    private Clazz   referencedClass;
    private Clazz[] referencedClasses;

    private boolean reRunFromStart;


    /**
     * Creates a new ExpectedStackTypeFinder.
     */
    public ExpectedStackTypeFinder()
    {
        this(new TypedReferenceValueFactory());
    }


    /**
     * Creates a new ExpectedStackTypeFinder.
     */
    public ExpectedStackTypeFinder(ValueFactory valueFactory)
    {
        this(valueFactory,
             new ReferenceTracingValueFactory(valueFactory, false));
    }


    /**
     * Creates a new ExpectedStackTypeFinder.
     */
    public ExpectedStackTypeFinder(ValueFactory                 valueFactory,
                                   ReferenceTracingValueFactory tracingValueFactory)
    {
        this(new PartialEvaluator(tracingValueFactory,
                                  new ReferenceTracingInvocationUnit(new BasicInvocationUnit(tracingValueFactory)),
                                  true,
                                  tracingValueFactory),
             true);
    }


    /**
     * Creates a new ParameterModificationMarker.
     */
    public ExpectedStackTypeFinder(PartialEvaluator partialEvaluator,
                                   boolean          runPartialEvaluator)
    {
        this.partialEvaluator    = partialEvaluator;
        this.runPartialEvaluator = runPartialEvaluator;
    }


    /**
     * Returns the reference type that is expected on the stack from the
     * specified producing instruction, based on all consuming instructions
     * in the code attribute that was visited most recently.
     */
    public ReferenceValue getExpectedTypeAfter(int instructionOffset)
    {
        return expectedTypes[instructionOffset];
    }


    /**
     * Returns the 'new' instruction offset at which the object instance is
     * created that is initialized at the given offset.
     */
    public int creationOffset(int initializationOffset)
    {
        int stackEntryIndex =
            partialEvaluator.getStackAfter(initializationOffset).size();

        TracedReferenceValue tracedReferenceValue =
            (TracedReferenceValue)partialEvaluator.getStackBefore(initializationOffset).getBottom(stackEntryIndex);

        InstructionOffsetValue producerOffsets =
            tracedReferenceValue.getTraceValue().instructionOffsetValue();

        return producerOffsets.instructionOffset(0);
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        logger.debug("ExpectedStackTypeFinder: [{}.{}{}]",
                     clazz.getName(),
                     method.getName(clazz),
                     method.getDescriptor(clazz)
        );

        // Initialized the results.
        expectedTypes =
            ArrayUtil.ensureArraySize(expectedTypes,
                                      codeAttribute.u4codeLength,
                                      null);

        // Evaluate the code.
        if (runPartialEvaluator)
        {
            partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);
        }

        // Check all consuming instructions, to mark the producing instructions.
        // We're traversing the instructions backward, so the types required
        // by subsequent 'aaload' instructions for multidimensional arrays can
        // propagate. In case the instructions are not sequential (e.g. due to
        // code obfuscation), we need to re-run the evaluation to correctly
        // propagate the types.

        do
        {
            reRunFromStart = false;
            for (int offset = codeAttribute.u4codeLength - 1; offset >= 0; offset--)
            {
                if (partialEvaluator.isTraced(offset))
                {
                    codeAttribute.instructionAccept(clazz, method, offset, this);
                }
            }

            if (reRunFromStart)
            {
                logger.debug("ExpectedStackTypeFinder: repeat [{}.{}{}]",
                             clazz.getName(),
                             method.getName(clazz),
                             method.getDescriptor(clazz)
                );
            }
        }
        while (reRunFromStart);

        logger.debug("ExpectedStackTypeFinder: results");

        if (logger.getLevel().isLessSpecificThan(Level.DEBUG))
        {
            int offset = 0;
            do {
                Instruction instruction = InstructionFactory.create(codeAttribute.code,
                        offset);
                ReferenceValue expectedType = expectedTypes[offset];

                if (expectedType == null) {
                    logger.debug(instruction.toString(offset));
                } else {
                    logger.debug("{} -> [{}]", instruction.toString(offset), expectedType);
                }

                offset += instruction.length(offset);
            }
            while (offset < codeAttribute.u4codeLength);
        }
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
    {
        switch (simpleInstruction.opcode)
        {
            case Instruction.OP_IALOAD:
                ensureType("[I",
                           null,
                           offset,
                           1);
                break;

            case Instruction.OP_LALOAD:
                ensureType("[J",
                           null,
                           offset,
                           1);
                break;

            case Instruction.OP_FALOAD:
                ensureType("[F",
                           null,
                           offset,
                           1);
                break;

            case Instruction.OP_DALOAD:
                ensureType("[D",
                           null,
                           offset,
                           1);
                break;

            case Instruction.OP_AALOAD:
                ReferenceValue expectedType = expectedTypes[offset];
                ensureType(expectedType == null  ||
                           expectedType.getType() == null ?
                               TypeConstants.ARRAY + ClassConstants.TYPE_JAVA_LANG_OBJECT :
                               TypeConstants.ARRAY + ClassUtil.internalTypeFromClassType(expectedType.getType()),
                           expectedType == null ?  null : expectedType.getReferencedClass(),
                           offset,
                           1);
                break;

            case Instruction.OP_BALOAD:
                ensureType("[B",
                           null,
                           offset,
                           1);
                break;

            case Instruction.OP_CALOAD:
                ensureType("[C",
                           null,
                           offset,
                           1);
                break;

            case Instruction.OP_SALOAD:
                ensureType("[S",
                           null,
                           offset,
                           1);
                break;

            case Instruction.OP_IASTORE:
                ensureType("[I",
                           null,
                           offset,
                           2);
                break;

            case Instruction.OP_LASTORE:
                ensureType("[J",
                           null,
                           offset,
                           3);
                break;

            case Instruction.OP_FASTORE:
                ensureType("[F",
                           null,
                           offset,
                           2);
                break;

            case Instruction.OP_DASTORE:
                ensureType("[D",
                           null,
                           offset,
                           3);
                break;

            case Instruction.OP_AASTORE:
                ensureType(TypeConstants.ARRAY + ClassConstants.TYPE_JAVA_LANG_OBJECT,
                           null,
                           offset,
                           2);
                break;

            case Instruction.OP_BASTORE:
                ensureType("[B",
                           null,
                           offset,
                           2);
                break;

            case Instruction.OP_CASTORE:
                ensureType("[C",
                           null,
                           offset,
                           2);
                break;

            case Instruction.OP_SASTORE:
                ensureType("[S",
                           null,
                           offset,
                           2);
                break;

            case Instruction.OP_ARETURN:
            {
                String descriptor = method.getDescriptor(clazz);
                Clazz[] referencedClasses = ((ProgramMethod)method).referencedClasses;
                ensureType(ClassUtil.internalClassTypeFromType(ClassUtil.internalMethodReturnType(descriptor)),
                           referencedClasses == null ||
                           !ClassUtil.isInternalClassType(descriptor) ? null :
                               referencedClasses[new DescriptorClassEnumeration(descriptor).classCount() - 1],
                           offset,
                           0);
                break;
            }

            case Instruction.OP_ARRAYLENGTH:
            {
                // We'll need an array type of objects or of primitives.
                String actualType = partialEvaluator.getStackBefore(offset).getTop(0).referenceValue().internalType();
                ensureType(ClassUtil.isInternalClassType(actualType) ?
                               TypeConstants.ARRAY + ClassConstants.TYPE_JAVA_LANG_OBJECT :
                               actualType,
                           null,
                           offset,
                           0);
                break;
            }

            case Instruction.OP_ATHROW:
                // TODO: Pass referenced class.
                ensureType(ClassConstants.NAME_JAVA_LANG_THROWABLE,
                           null,
                           offset,
                           0);
                break;
        }
    }


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        switch (constantInstruction.opcode)
        {
            case Instruction.OP_GETSTATIC:
                referencingOffset = offset;
                isPut             = false;
                isStatic          = true;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;

            case Instruction.OP_PUTSTATIC:
                referencingOffset = offset;
                isPut             = true;
                isStatic          = true;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;

            case Instruction.OP_GETFIELD:
                referencingOffset = offset;
                isPut             = false;
                isStatic          = false;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;

            case Instruction.OP_PUTFIELD:
                referencingOffset = offset;
                isPut             = true;
                isStatic          = false;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;

            case Instruction.OP_INVOKESTATIC:
                referencingOffset = offset;
                isStatic          = true;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;

            case Instruction.OP_INVOKEVIRTUAL:
            case Instruction.OP_INVOKESPECIAL:
            case Instruction.OP_INVOKEINTERFACE:
            case Instruction.OP_INVOKEDYNAMIC:
                referencingOffset = offset;
                isStatic          = false;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                break;
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        // Check dynamically invoking a method.
        String methodDescriptor = invokeDynamicConstant.getType(clazz);

        int stackEntryIndex =
            ClassUtil.internalMethodParameterSize(methodDescriptor);

        // Check the method parameter types.
        Clazz[] referencedClasses = invokeDynamicConstant.referencedClasses;

        InternalTypeEnumeration types =
            new InternalTypeEnumeration(methodDescriptor);

        int index = 0;
        while (types.hasMoreTypes())
        {
            String type = types.nextType();

            stackEntryIndex -= ClassUtil.internalTypeSize(type);

            if (!ClassUtil.isInternalPrimitiveType(type.charAt(0)))
            {
                ensureType(ClassUtil.internalClassTypeFromType(type),
                           referencedClasses == null ? null : referencedClasses[index++],
                           referencingOffset,
                           stackEntryIndex);
            }
        }
    }


    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        // Check loading or storing a value in a field.
        String fieldType = fieldrefConstant.getType(clazz);

        // Check the field's class type.
        if (!isStatic)
        {
            referencedClass = null;
            clazz.constantPoolEntryAccept(fieldrefConstant.u2classIndex, this);

            ensureType(fieldrefConstant.getClassName(clazz),
                       referencedClass,
                       referencingOffset,
                       isPut ? ClassUtil.internalTypeSize(fieldType) : 0);
        }

        // Check the field type.
        if (isPut && !ClassUtil.isInternalPrimitiveType(fieldType.charAt(0)))
        {
            referencedClass = null;
            fieldrefConstant.referencedFieldAccept(this);

            ensureType(ClassUtil.internalClassTypeFromType(fieldType),
                       referencedClass,
                       referencingOffset,
                       0);
        }
    }


    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        // Check invoking a method.
        String methodDescriptor = anyMethodrefConstant.getType(clazz);

        int stackEntryIndex =
            ClassUtil.internalMethodParameterSize(methodDescriptor);

        // Check the method's class type.
        if (!isStatic)
        {
            referencedClass = null;
            clazz.constantPoolEntryAccept(anyMethodrefConstant.u2classIndex, this);

            ensureType(anyMethodrefConstant.getClassName(clazz),
                       referencedClass,
                       referencingOffset,
                       stackEntryIndex);
        }

        // Check the method parameter types.
        referencedClasses = null;
        anyMethodrefConstant.referencedMethodAccept(this);

        InternalTypeEnumeration types =
            new InternalTypeEnumeration(methodDescriptor);

        int index = 0;
        while (types.hasMoreTypes())
        {
            String type = types.nextType();

            stackEntryIndex -= ClassUtil.internalTypeSize(type);

            if (!ClassUtil.isInternalPrimitiveType(type.charAt(0)))
            {
                Clazz referencedClass1 =
                    referencedClasses == null ||
                    !ClassUtil.isInternalClassType(type) ? null :
                        referencedClasses[index++];

                ensureType(ClassUtil.internalClassTypeFromType(type),
                           referencedClass1,
                           referencingOffset,
                           stackEntryIndex);
            }
        }
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        referencedClass = classConstant.referencedClass;
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        referencedClass = programField.referencedClass;
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        referencedClasses = programMethod.referencedClasses;
    }


    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        referencedClass = libraryField.referencedClass;
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        referencedClasses = libraryMethod.referencedClasses;
    }


    // Small utility methods.

    /**
     * Ensures that the specified stack entry at the given consumer
     * instruction offset has the given reference type.
     */
    private void ensureType(String type,
                            Clazz  referencedClass,
                            int    offset,
                            int    stackEntryIndex)
    {
        TracedStack stackBefore = partialEvaluator.getStackBefore(offset);

        TracedReferenceValue stackEntry =
            (TracedReferenceValue)stackBefore.getTop(stackEntryIndex);

        InstructionOffsetValue instructionOffsets =
            stackEntry.getTraceValue().instructionOffsetValue();

        int instructionOffsetCount =
            instructionOffsets.instructionOffsetCount();

        for (int index = 0; index < instructionOffsetCount; index++)
        {
            if (!instructionOffsets.isMethodParameter(index) &&
                !instructionOffsets.isExceptionHandler(index))
            {
                int producerOffset = instructionOffsets.instructionOffset(index);

                ReferenceValue oldExpectedType =
                    expectedTypes[producerOffset];

                String debugMessage = String.format("[%s]: [%s] old [%s] & new [%s]",
                                                    offset,
                                                    producerOffset,
                                                    oldExpectedType,
                                                    type
                );

                ReferenceValue newExpectedType =
                    new TypedReferenceValue(type, referencedClass, false, false);

                // Pick the most specific type, so all consumers are satisfied.
                if (oldExpectedType == null                  ||
                    oldExpectedType.isNull() == Value.ALWAYS ||
                    newExpectedType.instanceOf(oldExpectedType.getType(), oldExpectedType.getReferencedClass()) == Value.ALWAYS)
                {
                    // The new type is more specific than the old type,
                    // so pick the new type.
                    expectedTypes[producerOffset] = newExpectedType;

                    // Check if we need to revisit an earlier producer.
                    if (producerOffset > offset  &&
                        (oldExpectedType == null ||
                        !oldExpectedType.getType().equals(newExpectedType.getType())))
                    {
                        reRunFromStart = true;

                        debugMessage += String.format(" (rerun for higher producer [%s])", producerOffset);
                    }
                }
                else if (oldExpectedType.instanceOf(type, referencedClass) == Value.ALWAYS)
                {
                    // The old type is more specific than the new type,
                    // so leave the old type.
                }
                else
                {
                    // No type is more specific than the other type.
                    // There might be multiple solutions.
                    // Cheat by picking the type that is actually pushed.
                    ReferenceValue actualValue =
                        partialEvaluator.getStackAfter(producerOffset).getTop(0).referenceValue();

                    if (oldExpectedType != null &&
                        actualValue.getType() == null)
                    {
                        // The new type is null, so leave the old type.
                        continue;
                    }

                    newExpectedType =
                        new TypedReferenceValue(actualValue.getType(),
                                                actualValue.getReferencedClass(),
                                                false,
                                                false);

                    expectedTypes[producerOffset] = newExpectedType;

                    // Check if we need to revisit an earlier producer.
                    if (producerOffset > offset  &&
                        (oldExpectedType == null ||
                         !oldExpectedType.getType().equals(newExpectedType.getType())))
                    {
                        reRunFromStart = true;

                        debugMessage += String.format(" (rerun for higher producer [%s])", producerOffset);
                    }
                }

                debugMessage += String.format(" => [%s]", expectedTypes[producerOffset]);
                logger.debug(debugMessage);
            }
        }
    }
}
