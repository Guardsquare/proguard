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
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.ExceptionInfoVisitor;

/**
 * This {@link ExceptionInfoVisitor} adds all exception information that it visits to
 * the given target code attribute.
 *
 * @author Eric Lafortune
 */
public class ExceptionInfoAdder
implements   ExceptionInfoVisitor
{
    private final ConstantAdder         constantAdder;
    private final CodeAttributeComposer codeAttributeComposer;


    /**
     * Creates a new ExceptionAdder that will copy exceptions into the given
     * target code attribute.
     */
    public ExceptionInfoAdder(ProgramClass          targetClass,
                              CodeAttributeComposer targetComposer)
    {
        constantAdder         = new ConstantAdder(targetClass);
        codeAttributeComposer = targetComposer;
    }


    // Implementations for ExceptionInfoVisitor.

    public void visitExceptionInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, ExceptionInfo exceptionInfo)
    {
        // Create a copy of the exception info.
        ExceptionInfo newExceptionInfo =
            new ExceptionInfo(exceptionInfo.u2startPC,
                              exceptionInfo.u2endPC,
                              exceptionInfo.u2handlerPC,
                              exceptionInfo.u2catchType == 0 ? 0 :
                                  constantAdder.addConstant(clazz, exceptionInfo.u2catchType));

        // Add the completed exception info.
        codeAttributeComposer.appendException(newExceptionInfo);
    }
}