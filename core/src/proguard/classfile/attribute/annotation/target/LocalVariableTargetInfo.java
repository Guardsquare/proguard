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
package proguard.classfile.attribute.annotation.target;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.annotation.TypeAnnotation;
import proguard.classfile.attribute.annotation.target.visitor.*;

/**
 * Representation of a local variable annotation target.
 *
 * @author Eric Lafortune
 */
public class LocalVariableTargetInfo extends TargetInfo
{
    public int                          u2tableLength;
    public LocalVariableTargetElement[] table;


    /**
     * Creates an uninitialized LocalVariableTargetInfo.
     */
    public LocalVariableTargetInfo()
    {
    }


    /**
     * Creates a partially initialized LocalVariableTargetInfo.
     */
    public LocalVariableTargetInfo(byte u1targetType)
    {
        super(u1targetType);
    }


    /**
     * Creates an initialized LocalVariableTargetInfo.
     */
    public LocalVariableTargetInfo(byte                         u1targetType,
                                   int                          u2tableLength,
                                   LocalVariableTargetElement[] table)
    {
        super(u1targetType);

        this.u2tableLength = u2tableLength;
        this.table         = table;
    }


    /**
     * Applies the given visitor to all target elements.
     */
    public void targetElementsAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, LocalVariableTargetElementVisitor localVariableTargetElementVisitor)
    {
        for (int index = 0; index < u2tableLength; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of TypePathInfo.
            localVariableTargetElementVisitor.visitLocalVariableTargetElement(clazz, method, codeAttribute, typeAnnotation, this, table[index]);
        }
    }


    // Implementations for TargetInfo.

    /**
     * Lets the visitor visit, with Method and CodeAttribute null.
     */
    public void accept(Clazz clazz, TypeAnnotation typeAnnotation, TargetInfoVisitor targetInfoVisitor)
    {
        targetInfoVisitor.visitLocalVariableTargetInfo(clazz, null, null, typeAnnotation, this);
    }


    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, TargetInfoVisitor targetInfoVisitor)
    {
        targetInfoVisitor.visitLocalVariableTargetInfo(clazz, method, codeAttribute, typeAnnotation, this);
    }
}
