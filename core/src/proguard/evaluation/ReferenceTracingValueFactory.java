/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.evaluation;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.evaluation.value.*;

/**
 * This ValueFactory tags newly created reference values so they can be traced
 * throughout the execution of a method.
 *
 * @see TracedReferenceValue
 * @see InstructionOffsetValue
 * @author Eric Lafortune
 */
public class ReferenceTracingValueFactory
extends      SimplifiedVisitor
implements   InstructionVisitor,
             ValueFactory
{
    private final ValueFactory valueFactory;
    private final boolean      preserveTraceValueOnCasts;

    private Value traceValue;


    /**
     * Creates a new ReferenceTracingValueFactory that attaches instruction
     * offset values based on being used as an instruction visitor. This
     * instance preserves trace values in the {@link #cast} method.
     * @param valueFactory the value factory that creates the actual values.
     */
    public ReferenceTracingValueFactory(ValueFactory valueFactory)
    {
        this(valueFactory, true);
    }


    /**
     * Creates a new ReferenceTracingValueFactory that attaches instruction
     * offset values based on being used as an instruction visitor.
     * @param valueFactory              the value factory that creates the
     *                                  actual values.
     * @param preserveTraceValueOnCasts specifies whether to preserve the
     *                                  trace value for reference values that
     *                                  are passed to the {@link #cast} method.
     */
    public ReferenceTracingValueFactory(ValueFactory valueFactory,
                                        boolean      preserveTraceValueOnCasts)
    {
        this.valueFactory              = valueFactory;
        this.preserveTraceValueOnCasts = preserveTraceValueOnCasts;
    }


    public void setTraceValue(Value traceValue)
    {
        this.traceValue = traceValue;
    }


    /**
     * Casts a given traced reference value to the given type, either keeping
     * its trace value or setting a new one.
     */
    public TracedReferenceValue cast(TracedReferenceValue referenceValue,
                                     String               type,
                                     Clazz                referencedClass,
                                     boolean              alwaysCast)
    {
        // Cast the value.
        ReferenceValue castValue =
            referenceValue.getReferenceValue().cast(type,
                                                    referencedClass,
                                                    valueFactory,
                                                    alwaysCast);

        // Trace it.
        return new TracedReferenceValue(castValue,
                                        preserveTraceValueOnCasts ?
                                            referenceValue.getTraceValue() :
                                            traceValue);
    }


   // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
        traceValue = null;
    }


    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
    {
        switch (simpleInstruction.opcode)
        {
            case InstructionConstants.OP_ACONST_NULL:
            case InstructionConstants.OP_NEWARRAY:
            case InstructionConstants.OP_ATHROW:
                traceValue = new InstructionOffsetValue(offset | InstructionOffsetValue.NEW_INSTANCE);
                break;

            case InstructionConstants.OP_AALOAD:
                traceValue = new InstructionOffsetValue(offset);
                break;

            default:
                traceValue = null;
                break;
        }
    }


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        switch (constantInstruction.opcode)
        {
            case InstructionConstants.OP_LDC:
            case InstructionConstants.OP_LDC_W:
            case InstructionConstants.OP_NEW:
            case InstructionConstants.OP_ANEWARRAY:
            case InstructionConstants.OP_MULTIANEWARRAY:
                traceValue = new InstructionOffsetValue(offset | InstructionOffsetValue.NEW_INSTANCE);
                break;

            case InstructionConstants.OP_GETSTATIC:
            case InstructionConstants.OP_GETFIELD:
                traceValue = new InstructionOffsetValue(offset | InstructionOffsetValue.FIELD_VALUE);
                break;

            case InstructionConstants.OP_INVOKEVIRTUAL:
            case InstructionConstants.OP_INVOKESPECIAL:
            case InstructionConstants.OP_INVOKESTATIC:
            case InstructionConstants.OP_INVOKEINTERFACE:
                traceValue = new InstructionOffsetValue(offset | InstructionOffsetValue.METHOD_RETURN_VALUE);
                break;

            case InstructionConstants.OP_CHECKCAST:
                traceValue = new InstructionOffsetValue(offset | InstructionOffsetValue.CAST);
                break;

            default:
                traceValue = null;
                break;
        }
    }


    // Implementations for BasicValueFactory.

    public Value createValue(String  type,
                             Clazz   referencedClass,
                             boolean mayBeExtension,
                             boolean mayBeNull)
    {
        return trace(valueFactory.createValue(type,
                                              referencedClass,
                                              mayBeExtension,
                                              mayBeNull));
    }


    public IntegerValue createIntegerValue()
    {
        return valueFactory.createIntegerValue();
    }


    public IntegerValue createIntegerValue(int value)
    {
        return valueFactory.createIntegerValue(value);
    }


    public IntegerValue createIntegerValue(int min, int max)
    {
        return valueFactory.createIntegerValue(min, max);
    }


    public LongValue createLongValue()
    {
        return valueFactory.createLongValue();
    }


    public LongValue createLongValue(long value)
    {
        return valueFactory.createLongValue(value);
    }


    public FloatValue createFloatValue()
    {
        return valueFactory.createFloatValue();
    }


    public FloatValue createFloatValue(float value)
    {
        return valueFactory.createFloatValue(value);
    }


    public DoubleValue createDoubleValue()
    {
        return valueFactory.createDoubleValue();
    }


    public DoubleValue createDoubleValue(double value)
    {
        return valueFactory.createDoubleValue(value);
    }


    public ReferenceValue createReferenceValue()
    {
        return trace(valueFactory.createReferenceValue());
    }


    public ReferenceValue createReferenceValueNull()
    {
        return trace(valueFactory.createReferenceValueNull());
    }


    public ReferenceValue createReferenceValue(String  type,
                                               Clazz   referencedClass,
                                               boolean mayBeExtension,
                                               boolean mayBeNull)
    {
        return trace(valueFactory.createReferenceValue(type,
                                                       referencedClass,
                                                       mayBeExtension,
                                                       mayBeNull));
    }


    public ReferenceValue createArrayReferenceValue(String       type,
                                                    Clazz        referencedClass,
                                                    IntegerValue arrayLength)
    {
        return trace(valueFactory.createArrayReferenceValue(type, referencedClass, arrayLength));
    }


    /**
     * Creates a new ReferenceValue that represents an array with elements of
     * the given type, with the given length and initial element values.
     */
    public ReferenceValue createArrayReferenceValue(String       type,
                                                    Clazz        referencedClass,
                                                    IntegerValue arrayLength,
                                                    Value        elementValue)
    {
        return trace(valueFactory.createArrayReferenceValue(type, referencedClass, arrayLength, elementValue));
    }


    // Small utility methods.

    /**
     * Attaches the current trace value to given value, if it is a reference
     * value.
     */
    public Value trace(Value value)
    {
        return value.computationalType() == Value.TYPE_REFERENCE ?
            trace(value.referenceValue()) :
            value;
    }

    /**
     * Attaches the current trace value to given reference value.
     */
    public ReferenceValue trace(ReferenceValue referenceValue)
    {
        return traceValue != null ?
            new TracedReferenceValue(referenceValue, traceValue) :
            referenceValue;
    }
}
