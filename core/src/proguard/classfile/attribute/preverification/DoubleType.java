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
package proguard.classfile.attribute.preverification;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.preverification.visitor.VerificationTypeVisitor;

/**
 * This {@link VerificationType} represents a <code>Double</code> type.
 *
 * @author Eric Lafortune
 */
public class DoubleType extends VerificationType
{
    // Implementations for VerificationType.

    public int getTag()
    {
        return DOUBLE_TYPE;
    }


    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, int instructionOffset, VerificationTypeVisitor verificationTypeVisitor)
    {
        verificationTypeVisitor.visitDoubleType(clazz, method, codeAttribute, instructionOffset, this);
    }


    public void stackAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, int instructionOffset, int stackIndex, VerificationTypeVisitor verificationTypeVisitor)
    {
        verificationTypeVisitor.visitStackDoubleType(clazz, method, codeAttribute, instructionOffset, stackIndex, this);
    }


    public void variablesAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, int instructionOffset, int variableIndex, VerificationTypeVisitor verificationTypeVisitor)
    {
        verificationTypeVisitor.visitVariablesDoubleType(clazz, method, codeAttribute, instructionOffset, variableIndex, this);
    }


    // Implementations for Object.

    public String toString()
    {
        return "d";
    }
}
