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
 * Representation of an empty annotation target.
 *
 * @author Eric Lafortune
 */
public class EmptyTargetInfo extends TargetInfo
{
    /**
     * Creates an uninitialized EmptyTargetInfo.
     */
    public EmptyTargetInfo()
    {
    }


    /**
     * Creates an initialized EmptyTargetInfo.
     */
    public EmptyTargetInfo(byte u1targetType)
    {
        super(u1targetType);
    }


    // Implementations for TargetInfo.

    /**
     * Lets the visitor visit, with Field null.
     */
    public void accept(Clazz clazz, TypeAnnotation typeAnnotation, TargetInfoVisitor targetInfoVisitor)
    {
        targetInfoVisitor.visitEmptyTargetInfo(clazz, (Field)null, typeAnnotation, this);
    }


    public void accept(Clazz clazz, Field field, TypeAnnotation typeAnnotation, TargetInfoVisitor targetInfoVisitor)
    {
        targetInfoVisitor.visitEmptyTargetInfo(clazz, field, typeAnnotation, this);
    }


    public void accept(Clazz clazz, Method method, TypeAnnotation typeAnnotation, TargetInfoVisitor targetInfoVisitor)
    {
        targetInfoVisitor.visitEmptyTargetInfo(clazz, method, typeAnnotation, this);
    }
}
