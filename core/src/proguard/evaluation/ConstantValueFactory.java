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
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.*;
import proguard.evaluation.value.*;

/**
 * This class creates {@link Value} instances that correspond to specified
 * constant pool entries.
 *
 * @author Eric Lafortune
 */
public class ConstantValueFactory
extends      SimplifiedVisitor
implements   ConstantVisitor
{
    protected final ValueFactory valueFactory;

    // Field acting as a parameter for the ConstantVisitor methods.
    protected Value value;


    public ConstantValueFactory(ValueFactory valueFactory)
    {
        this.valueFactory = valueFactory;
    }


    /**
     * Returns the Value of the constant pool element at the given index.
     */
    public Value constantValue(Clazz clazz,
                               int   constantIndex)
    {
        // Visit the constant pool entry to get its return value.
        clazz.constantPoolEntryAccept(constantIndex, this);

        return value;
    }


    // Implementations for ConstantVisitor.

    public void visitIntegerConstant(Clazz clazz, IntegerConstant integerConstant)
    {
        value = valueFactory.createIntegerValue(integerConstant.getValue());
    }

    public void visitLongConstant(Clazz clazz, LongConstant longConstant)
    {
        value = valueFactory.createLongValue(longConstant.getValue());
    }

    public void visitFloatConstant(Clazz clazz, FloatConstant floatConstant)
    {
        value = valueFactory.createFloatValue(floatConstant.getValue());
    }

    public void visitDoubleConstant(Clazz clazz, DoubleConstant doubleConstant)
    {
        value = valueFactory.createDoubleValue(doubleConstant.getValue());
    }

    public void visitPrimitiveArrayConstant(Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant)
    {
        value = valueFactory.createArrayReferenceValue(""+primitiveArrayConstant.getPrimitiveType(),
                                                       null,
                                                       valueFactory.createIntegerValue(primitiveArrayConstant.getLength()));
    }

    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        value = valueFactory.createReferenceValue(ClassConstants.NAME_JAVA_LANG_STRING,
                                                  stringConstant.javaLangStringClass,
                                                  false,
                                                  false);
    }

    public void visitDynamicConstant(Clazz clazz, DynamicConstant dynamicConstant)
    {
        String type = ClassUtil.internalMethodReturnType(dynamicConstant.getType(clazz));

        // Is the method returning a class type?
        Clazz[] referencedClasses = dynamicConstant.referencedClasses;
        Clazz   referencedClass =
            referencedClasses != null    &&
            referencedClasses.length > 0 &&
            ClassUtil.isInternalClassType(type) ?
                referencedClasses[referencedClasses.length - 1] :
                null;

        value = valueFactory.createValue(type,
                                         referencedClass,
                                         true,
                                         true);
    }

    public void visitMethodHandleConstant(Clazz clazz, MethodHandleConstant methodHandleConstant)
    {
        value = valueFactory.createReferenceValue(ClassConstants.NAME_JAVA_LANG_INVOKE_METHOD_HANDLE,
                                                  methodHandleConstant.javaLangInvokeMethodHandleClass,
                                                  false,
                                                  false);
    }

    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        value = valueFactory.createReferenceValue(classConstant.getName(clazz),
                                                  classConstant.referencedClass,
                                                  false,
                                                  false);
    }


    public void visitMethodTypeConstant(Clazz clazz, MethodTypeConstant methodTypeConstant)
    {
        value = valueFactory.createReferenceValue(ClassConstants.NAME_JAVA_LANG_INVOKE_METHOD_TYPE,
                                                  methodTypeConstant.javaLangInvokeMethodTypeClass,
                                                  false,
                                                  false);
    }
}
