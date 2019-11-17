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
 * Representation of a formal parameter annotation target.
 *
 * @author Eric Lafortune
 */
public class FormalParameterTargetInfo extends TargetInfo
{
    public int u1formalParameterIndex;


    /**
     * Creates an uninitialized FormalParameterTargetInfo.
     */
    public FormalParameterTargetInfo()
    {
    }


    /**
     * Creates a partially initialized FormalParameterTargetInfo.
     */
    public FormalParameterTargetInfo(byte u1targetType)
    {
        super(u1targetType);
    }


    /**
     * Creates an initialized FormalParameterTargetInfo.
     */
    public FormalParameterTargetInfo(byte u1targetType,
                                     int  u1formalParameterIndex)
    {
        super(u1targetType);

        this.u1formalParameterIndex = u1formalParameterIndex;
    }


    // Implementations for TargetInfo.

    /**
     * Lets the visitor visit, with Method null.
     */
    public void accept(Clazz clazz, TypeAnnotation typeAnnotation, TargetInfoVisitor targetInfoVisitor)
    {
        targetInfoVisitor.visitFormalParameterTargetInfo(clazz, null, typeAnnotation, this);
    }


    public void accept(Clazz clazz, Method method, TypeAnnotation typeAnnotation, TargetInfoVisitor targetInfoVisitor)
    {
        targetInfoVisitor.visitFormalParameterTargetInfo(clazz, method, typeAnnotation, this);
    }
}
