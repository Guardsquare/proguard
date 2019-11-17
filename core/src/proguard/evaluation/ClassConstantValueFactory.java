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
import proguard.classfile.constant.ClassConstant;
import proguard.evaluation.value.ValueFactory;

/**
 * This class creates java.lang.Class ReferenceValue instances that correspond
 * to specified constant pool entries.
 *
 * @author Eric Lafortune
 */
public class ClassConstantValueFactory
extends      ConstantValueFactory
{
    public ClassConstantValueFactory(ValueFactory valueFactory)
    {
        super(valueFactory);
    }


    // Implementations for ConstantVisitor.

    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Create a Class reference instead of a reference to the class.
        value = valueFactory.createReferenceValue(ClassConstants.NAME_JAVA_LANG_CLASS,
                                                  classConstant.javaLangClassClass,
                                                  false,
                                                  false);
    }
}
