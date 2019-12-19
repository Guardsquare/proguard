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
import proguard.classfile.constant.*;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.evaluation.value.*;

/**
 * This InvocationUnit tags reference values of retrieved fields, passed method
 * parameters, method return values, and caught exceptions, so they can be
 * traced throughout the execution of a method. The tags are instruction offsets
 * or parameter indices (not parameter offsets).
 *
 * @see TracedReferenceValue
 * @see InstructionOffsetValue
 * @author Eric Lafortune
 */
public class ReferenceTracingInvocationUnit
extends      SimplifiedInvocationUnit
{
    private final SimplifiedInvocationUnit invocationUnit;

    private int offset;


    /**
     * Creates a new ReferenceTracingInvocationUnit.
     * @param invocationUnit the invocation unit to which invocations will
     *                       be delegated.
     */
    public ReferenceTracingInvocationUnit(SimplifiedInvocationUnit invocationUnit)
    {
        this.invocationUnit = invocationUnit;
    }


    // Implementations for InvocationUnit.

    public void enterExceptionHandler(Clazz         clazz,
                                      Method        method,
                                      CodeAttribute codeAttribute,
                                      int           offset,
                                      int           catchType,
                                      Stack         stack)
    {
        this.offset = offset;

        super.enterExceptionHandler(clazz,
                                    method,
                                    codeAttribute,
                                    offset,
                                    catchType,
                                    stack);
    }


    public void invokeMember(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction, Stack stack)
    {
        this.offset = offset;

        super.invokeMember(clazz,
                           method,
                           codeAttribute,
                           offset,
                           constantInstruction,
                           stack);
    }


    // Implementations for SimplifiedInvocationUnit.

    public Value getExceptionValue(Clazz clazz, ClassConstant catchClassConstant)
    {
        return trace(invocationUnit.getExceptionValue(clazz, catchClassConstant),
                     offset | InstructionOffsetValue.EXCEPTION_HANDLER);
    }


    public void setFieldClassValue(Clazz clazz, FieldrefConstant fieldrefConstant, ReferenceValue value)
    {
        invocationUnit.setFieldClassValue(clazz, fieldrefConstant, value);
    }


    public Value getFieldClassValue(Clazz clazz, FieldrefConstant fieldrefConstant, String type)
    {
        return trace(invocationUnit.getFieldClassValue(clazz, fieldrefConstant, type),
                     offset | InstructionOffsetValue.FIELD_VALUE);
    }


    public void setFieldValue(Clazz clazz, FieldrefConstant fieldrefConstant, Value value)
    {
        invocationUnit.setFieldValue(clazz, fieldrefConstant, value);
    }


    public Value getFieldValue(Clazz clazz, FieldrefConstant fieldrefConstant, String type)
    {
        return trace(invocationUnit.getFieldValue(clazz, fieldrefConstant, type),
                     offset | InstructionOffsetValue.FIELD_VALUE);
    }


    public void setMethodParameterValue(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant, int parameterIndex, Value value)
    {
        invocationUnit.setMethodParameterValue(clazz, anyMethodrefConstant, parameterIndex, value);
    }


    public Value getMethodParameterValue(Clazz clazz, Method method, int parameterIndex, String type, Clazz referencedClass)
    {
        Value parameterValue =
            invocationUnit.getMethodParameterValue(clazz, method, parameterIndex, type, referencedClass);

        // We're attaching the parameter index as a trace value. It doesn't
        // take into account Category 2 values, so it is not compatible with
        // variable indices.
        return trace(parameterValue,
                     parameterIndex | InstructionOffsetValue.METHOD_PARAMETER);
    }


    public void setMethodReturnValue(Clazz clazz, Method method, Value value)
    {
        invocationUnit.setMethodReturnValue(clazz, method, value);
    }


    public Value getMethodReturnValue(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant, String type)
    {
        Value returnValue =
            invocationUnit.getMethodReturnValue(clazz, anyMethodrefConstant, type);

        return trace(returnValue,
                     offset | InstructionOffsetValue.METHOD_RETURN_VALUE);
    }


    public Value getMethodReturnValue(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant, String type)
    {
        Value returnValue =
            invocationUnit.getMethodReturnValue(clazz, invokeDynamicConstant, type);

        return trace(returnValue,
                     offset | InstructionOffsetValue.METHOD_RETURN_VALUE);
    }


    // Small utility methods.

    /**
     * Sets or replaces the trace value on a given value, if it's a
     * reference value, returning the result.
     */
    protected Value trace(Value value, int trace)
    {
        if (value.computationalType() != Value.TYPE_REFERENCE)
        {
            return value;
        }

        return trace(value, new InstructionOffsetValue(trace));
    }


    /**
     * Sets or replaces the trace value on a given value, returning the result.
     */
    protected Value trace(Value value, InstructionOffsetValue traceValue)
    {
        return new TracedReferenceValue(untrace(value).referenceValue(),
                                        traceValue);
    }


    /**
     * Removes the trace value from a given value, if present, returning the
     * result.
     */
    private Value untrace(Value value)
    {
        return value instanceof TracedReferenceValue ?
            ((TracedReferenceValue)value).getReferenceValue() :
            value;
    }
}