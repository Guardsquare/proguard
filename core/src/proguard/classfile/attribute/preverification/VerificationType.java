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
import proguard.util.SimpleVisitorAccepter;

/**
 * This abstract class represents a verification type of a local variable or
 * a stack element. Specific verification types are subclassed from it.
 *
 * @author Eric Lafortune
 */
public abstract class VerificationType extends SimpleVisitorAccepter
{
    public static final int TOP_TYPE                = 0;
    public static final int INTEGER_TYPE            = 1;
    public static final int FLOAT_TYPE              = 2;
    public static final int DOUBLE_TYPE             = 3;
    public static final int LONG_TYPE               = 4;
    public static final int NULL_TYPE               = 5;
    public static final int UNINITIALIZED_THIS_TYPE = 6;
    public static final int OBJECT_TYPE             = 7;
    public static final int UNINITIALIZED_TYPE      = 8;


    /**
     * Returns the tag of the verification type.
     */
    public abstract int getTag();


    /**
     * Accepts the given visitor in the context of a method's code, either on
     * a stack or as a variable.
     */
    public abstract void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, int instructionOffset, VerificationTypeVisitor verificationTypeVisitor);


    /**
     * Accepts the given visitor in the context of a stack in a method's code .
     */
    public abstract void stackAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, int instructionOffset, int stackIndex, VerificationTypeVisitor verificationTypeVisitor);


    /**
     * Accepts the given visitor in the context of a variable in a method's code.
     */
    public abstract void variablesAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, int instructionOffset, int variableIndex, VerificationTypeVisitor verificationTypeVisitor);


    // Implementations for Object.

    public boolean equals(Object object)
    {
        return object != null &&
               this.getClass() == object.getClass();
    }


    public int hashCode()
    {
        return this.getClass().hashCode();
    }
}
