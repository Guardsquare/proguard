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
package proguard.classfile.attribute.annotation.target.visitor;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.target.*;

/**
 * This interface specifies the methods for a visitor of <code>TargetInfo</code>
 * objects.
 *
 * @author Eric Lafortune
 */
public interface TargetInfoVisitor
{
    public void visitTypeParameterTargetInfo(     Clazz clazz,                                             TypeAnnotation typeAnnotation, TypeParameterTargetInfo      typeParameterTargetInfo);
    public void visitTypeParameterTargetInfo(     Clazz clazz, Method method,                              TypeAnnotation typeAnnotation, TypeParameterTargetInfo      typeParameterTargetInfo);
    public void visitSuperTypeTargetInfo(         Clazz clazz,                                             TypeAnnotation typeAnnotation, SuperTypeTargetInfo          superTypeTargetInfo);
    public void visitTypeParameterBoundTargetInfo(Clazz clazz,                                             TypeAnnotation typeAnnotation, TypeParameterBoundTargetInfo typeParameterBoundTargetInfo);
    public void visitTypeParameterBoundTargetInfo(Clazz clazz, Field field,                                TypeAnnotation typeAnnotation, TypeParameterBoundTargetInfo typeParameterBoundTargetInfo);
    public void visitTypeParameterBoundTargetInfo(Clazz clazz, Method method,                              TypeAnnotation typeAnnotation, TypeParameterBoundTargetInfo typeParameterBoundTargetInfo);
    public void visitEmptyTargetInfo(             Clazz clazz, Field field,                                TypeAnnotation typeAnnotation, EmptyTargetInfo              emptyTargetInfo);
    public void visitEmptyTargetInfo(             Clazz clazz, Method method,                              TypeAnnotation typeAnnotation, EmptyTargetInfo              emptyTargetInfo);
    public void visitFormalParameterTargetInfo(   Clazz clazz, Method method,                              TypeAnnotation typeAnnotation, FormalParameterTargetInfo    formalParameterTargetInfo);
    public void visitThrowsTargetInfo(            Clazz clazz, Method method,                              TypeAnnotation typeAnnotation, ThrowsTargetInfo             throwsTargetInfo);
    public void visitLocalVariableTargetInfo(     Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, LocalVariableTargetInfo      localVariableTargetInfo);
    public void visitCatchTargetInfo(             Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, CatchTargetInfo              catchTargetInfo);
    public void visitOffsetTargetInfo(            Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, OffsetTargetInfo             offsetTargetInfo);
    public void visitTypeArgumentTargetInfo(      Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, TypeArgumentTargetInfo       typeArgumentTargetInfo);
}
