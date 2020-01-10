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
 * This {@link VerificationType} represents an <code>Object</code> type.
 *
 * @author Eric Lafortune
 */
public class ObjectType extends VerificationType
{
    public int u2classIndex;



    /**
     * Creates an uninitialized ObjectType.
     */
    public ObjectType()
    {
    }


    /**
     * Creates an ObjectType that points to the given class constant.
     */
    public ObjectType(int u2classIndex)
    {
        this.u2classIndex = u2classIndex;
    }


    // Implementations for VerificationType.

    public int getTag()
    {
        return OBJECT_TYPE;
    }


    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, int instructionOffset, VerificationTypeVisitor verificationTypeVisitor)
    {
        verificationTypeVisitor.visitObjectType(clazz, method, codeAttribute, instructionOffset, this);
    }


    public void stackAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, int instructionOffset, int stackIndex, VerificationTypeVisitor verificationTypeVisitor)
    {
        verificationTypeVisitor.visitStackObjectType(clazz, method, codeAttribute, instructionOffset, stackIndex, this);
    }


    public void variablesAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, int instructionOffset, int variableIndex, VerificationTypeVisitor verificationTypeVisitor)
    {
        verificationTypeVisitor.visitVariablesObjectType(clazz, method, codeAttribute, instructionOffset, variableIndex, this);
    }


    // Implementations for Object.

    public boolean equals(Object object)
    {
        if (!super.equals(object))
        {
            return false;
        }

        ObjectType other = (ObjectType)object;

        return this.u2classIndex == other.u2classIndex;
    }


    public int hashCode()
    {
        return super.hashCode() ^
               u2classIndex;
    }


    public String toString()
    {
        return "a:" + u2classIndex;
    }
}
