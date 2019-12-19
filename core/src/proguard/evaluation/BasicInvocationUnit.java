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
import proguard.classfile.constant.*;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.value.*;

/**
 * This InvocationUnit sets up the variables for entering a method,
 * and it updates the stack for the invocation of a class member,
 * using simple values.
 *
 * @author Eric Lafortune
 */
public class BasicInvocationUnit
extends      SimplifiedInvocationUnit
implements   InvocationUnit,
             MemberVisitor
{
    protected final ValueFactory valueFactory;

    // Field acting as parameter between the visitor methods.
    private Clazz returnTypeClass;


    /**
     * Creates a new BasicInvocationUnit with the given value factory.
     */
    public BasicInvocationUnit(ValueFactory valueFactory)
    {
        this.valueFactory = valueFactory;
    }


    // Implementations for SimplifiedInvocationUnit.

    public Value getExceptionValue(Clazz         clazz,
                                   ClassConstant catchClassConstant)
    {
        String catchClassName = catchClassConstant != null ?
            catchClassConstant.getName(clazz) :
            ClassConstants.NAME_JAVA_LANG_THROWABLE;

        Clazz catchClass = catchClassConstant != null ?
            catchClassConstant.referencedClass :
            null;

        return valueFactory.createReferenceValue(catchClassName,
                                                 catchClass,
                                                 true,
                                                 false);
    }


    public void setFieldClassValue(Clazz            clazz,
                                   FieldrefConstant fieldrefConstant,
                                   ReferenceValue   value)
    {
        // We don't care about the new value.
    }


    public Value getFieldClassValue(Clazz            clazz,
                                    FieldrefConstant fieldrefConstant,
                                    String           type)
    {
        // Try to figure out the class of the return type.
        returnTypeClass = null;
        fieldrefConstant.referencedFieldAccept(this);

        return valueFactory.createValue(type,
                                        returnTypeClass,
                                        true,
                                        true);
    }


    public void setFieldValue(Clazz            clazz,
                              FieldrefConstant fieldrefConstant,
                              Value            value)
    {
        // We don't care about the new field value.
    }


    public Value getFieldValue(Clazz            clazz,
                               FieldrefConstant fieldrefConstant,
                               String           type)
    {
        // Try to figure out the class of the return type.
        returnTypeClass = null;
        fieldrefConstant.referencedFieldAccept(this);

        return valueFactory.createValue(type,
                                        returnTypeClass,
                                        true,
                                        true);
    }


    public void setMethodParameterValue(Clazz                clazz,
                                        AnyMethodrefConstant anyMethodrefConstant,
                                        int                  parameterIndex,
                                        Value                value)
    {
        // We don't care about the parameter value.
    }


    public Value getMethodParameterValue(Clazz  clazz,
                                         Method method,
                                         int    parameterIndex,
                                         String type,
                                         Clazz  referencedClass)
    {
        // A "this" parameter can never be null.
        boolean isThis =
            parameterIndex == 0 &&
            (method.getAccessFlags() & ClassConstants.ACC_STATIC) == 0;

        return valueFactory.createValue(type,
                                        referencedClass,
                                        true,
                                        !isThis);
    }


    public void setMethodReturnValue(Clazz  clazz,
                                     Method method,
                                     Value  value)
    {
        // We don't care about the return value.
    }


    public Value getMethodReturnValue(Clazz                clazz,
                                      AnyMethodrefConstant anyMethodrefConstant,
                                      String               type)
    {
        // Try to figure out the class of the return type.
        returnTypeClass = null;
        anyMethodrefConstant.referencedMethodAccept(this);

        return valueFactory.createValue(type,
                                        returnTypeClass,
                                        true,
                                        true);
    }


    /**
     * Returns the return value of the specified method.
     */
    public Value getMethodReturnValue(Clazz                 clazz,
                                      InvokeDynamicConstant invokeDynamicConstant,
                                      String                type)
    {
        // Try to figure out the class of the return type.
        Clazz[] referencedClasses = invokeDynamicConstant.referencedClasses;

        Clazz referencedClass =
            referencedClasses != null &&
            ClassUtil.isInternalClassType(type) ?
                referencedClasses[referencedClasses.length - 1] :
                null;

        return valueFactory.createValue(type,
                                        referencedClass,
                                        true,
                                        true);
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        returnTypeClass = programField.referencedClass;
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        Clazz[] referencedClasses = programMethod.referencedClasses;
        if (referencedClasses != null &&
            ClassUtil.isInternalClassType(programMethod.getDescriptor(programClass)))
        {
            returnTypeClass = referencedClasses[referencedClasses.length - 1];
        }
    }


    public void visitLibraryField(LibraryClass programClass, LibraryField libraryField)
    {
        returnTypeClass = libraryField.referencedClass;
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        Clazz[] referencedClasses = libraryMethod.referencedClasses;
        if (referencedClasses != null &&
            ClassUtil.isInternalClassType(libraryMethod.getDescriptor(libraryClass)))
        {
            returnTypeClass = referencedClasses[referencedClasses.length - 1];
        }
    }
}
