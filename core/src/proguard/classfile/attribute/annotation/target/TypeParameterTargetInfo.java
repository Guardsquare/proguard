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
import proguard.classfile.attribute.annotation.TypeAnnotation;
import proguard.classfile.attribute.annotation.target.visitor.TargetInfoVisitor;

/**
 * Representation of a type parameter annotation target.
 *
 * @author Eric Lafortune
 */
public class TypeParameterTargetInfo extends TargetInfo
{
    public int u1typeParameterIndex;


    /**
     * Creates an uninitialized TypeParameterTargetInfo.
     */
    public TypeParameterTargetInfo()
    {
    }


    /**
     * Creates a partially initialized TypeParameterTargetInfo.
     */
    public TypeParameterTargetInfo(byte u1targetType)
    {
        super(u1targetType);
    }


    /**
     * Creates an initialized TypeParameterTargetInfo.
     */
    public TypeParameterTargetInfo(byte u1targetType,
                                   int  u1typeParameterIndex)
    {
        super(u1targetType);

        this.u1typeParameterIndex = u1typeParameterIndex;
    }


    // Implementations for TargetInfo.

    public void accept(Clazz clazz, TypeAnnotation typeAnnotation, TargetInfoVisitor targetInfoVisitor)
    {
        targetInfoVisitor.visitTypeParameterTargetInfo(clazz, typeAnnotation, this);
    }


    public void accept(Clazz clazz, Method method, TypeAnnotation typeAnnotation, TargetInfoVisitor targetInfoVisitor)
    {
        targetInfoVisitor.visitTypeParameterTargetInfo(clazz, method, typeAnnotation, this);
    }
}
