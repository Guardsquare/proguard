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
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.attribute.ExceptionsAttribute;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This ConstantVisitor adds all class constants that it visits to the given
 * target exceptions attribute.
 *
 * @author Eric Lafortune
 */
public class ExceptionAdder
extends      SimplifiedVisitor
implements   ConstantVisitor
{
    private final ConstantAdder             constantAdder;
    private final ExceptionsAttributeEditor exceptionsAttributeEditor;


    /**
     * Creates a new ExceptionAdder that will copy classes into the given
     * target exceptions attribute.
     */
    public ExceptionAdder(ProgramClass        targetClass,
                          ExceptionsAttribute targetExceptionsAttribute)
    {
        constantAdder             = new ConstantAdder(targetClass);
        exceptionsAttributeEditor = new ExceptionsAttributeEditor(targetExceptionsAttribute);
    }


    // Implementations for ConstantVisitor.

    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Add a class constant to the constant pool.
        constantAdder.visitClassConstant(clazz, classConstant);

        // Add the index of the class constant to the list of exceptions.
        exceptionsAttributeEditor.addException(constantAdder.getConstantIndex());
    }
}
