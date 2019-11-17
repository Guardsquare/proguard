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
import proguard.classfile.instruction.ConstantInstruction;
import proguard.evaluation.value.Value;

/**
 * This interface sets up the variables for entering a method,
 * and it updates the stack for the invocation of a class member.
 *
 * @author Eric Lafortune
 */
public interface InvocationUnit
{
    /**
     * Sets up the given variables for entering the given method.
     */
    public void enterMethod(Clazz     clazz,
                            Method    method,
                            Variables variables);


    /**
     * Exits the given method with the given return value.
     */
    public void exitMethod(Clazz  clazz,
                           Method method,
                           Value  returnValue);


    /**
     * Sets up the given stack for entering the given exception handler.
     */
    public void enterExceptionHandler(Clazz         clazz,
                                      Method        method,
                                      CodeAttribute codeAttribute,
                                      int           offset,
                                      int           catchType,
                                      Stack         stack);


    /**
     * Updates the given stack corresponding to the execution of the given
     * field or method reference instruction.
     */
    public void invokeMember(Clazz               clazz,
                             Method              method,
                             CodeAttribute       codeAttribute,
                             int                 offset,
                             ConstantInstruction constantInstruction,
                             Stack               stack);
}
