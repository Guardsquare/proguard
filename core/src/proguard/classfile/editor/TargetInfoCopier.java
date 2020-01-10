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
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.annotation.TypeAnnotation;
import proguard.classfile.attribute.annotation.target.*;
import proguard.classfile.attribute.annotation.target.visitor.TargetInfoVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This {@link TargetInfoVisitor} copies the target info instances that it visits to
 * the given type annotation (each time overwriting any previous target info).
 *
 * @author Eric Lafortune
 */
public class TargetInfoCopier
extends      SimplifiedVisitor
implements   TargetInfoVisitor
{
    private final ProgramClass   targetClass;
    private final TypeAnnotation targetTypeAnnotation;


    /**
     * Creates a new TargetInfoCopier that will copy target info instances
     * to the given target type annotation.
     */
    public TargetInfoCopier(ProgramClass   targetClass,
                            TypeAnnotation targetTypeAnnotation)
    {
        this.targetClass          = targetClass;
        this.targetTypeAnnotation = targetTypeAnnotation;
    }


    // Implementations for TargetInfoVisitor.

    public void visitTypeParameterTargetInfo(Clazz clazz, TypeAnnotation typeAnnotation, TypeParameterTargetInfo typeParameterTargetInfo)
    {
        targetTypeAnnotation.targetInfo =
            new TypeParameterTargetInfo(typeParameterTargetInfo.u1targetType,
                                        typeParameterTargetInfo.u1typeParameterIndex);
    }


    public void visitSuperTypeTargetInfo(Clazz clazz, TypeAnnotation typeAnnotation, SuperTypeTargetInfo superTypeTargetInfo)
    {
        // TODO: The supertype index (= interface number) is probably different in the target class.
        targetTypeAnnotation.targetInfo =
            new SuperTypeTargetInfo(superTypeTargetInfo.u1targetType,
                                    superTypeTargetInfo.u2superTypeIndex);
    }


    public void visitTypeParameterBoundTargetInfo(Clazz clazz, TypeAnnotation typeAnnotation, TypeParameterBoundTargetInfo typeParameterBoundTargetInfo)
    {
        targetTypeAnnotation.targetInfo =
            new TypeParameterBoundTargetInfo(typeParameterBoundTargetInfo.u1targetType,
                                             typeParameterBoundTargetInfo.u1typeParameterIndex,
                                             typeParameterBoundTargetInfo.u1boundIndex);
    }


    public void visitEmptyTargetInfo(Clazz clazz, Member member, TypeAnnotation typeAnnotation, EmptyTargetInfo emptyTargetInfo)
    {
        targetTypeAnnotation.targetInfo =
            new EmptyTargetInfo(emptyTargetInfo.u1targetType);
    }


    public void visitFormalParameterTargetInfo(Clazz clazz, Method method, TypeAnnotation typeAnnotation, FormalParameterTargetInfo formalParameterTargetInfo)
    {
        targetTypeAnnotation.targetInfo =
            new FormalParameterTargetInfo(formalParameterTargetInfo.u1targetType,
                                          formalParameterTargetInfo.u1formalParameterIndex);
    }


    public void visitThrowsTargetInfo(Clazz clazz, Method method, TypeAnnotation typeAnnotation, ThrowsTargetInfo throwsTargetInfo)
    {
        targetTypeAnnotation.targetInfo =
            new ThrowsTargetInfo(throwsTargetInfo.u1targetType,
                                 throwsTargetInfo.u2throwsTypeIndex);
    }


    public void visitLocalVariableTargetInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, LocalVariableTargetInfo localVariableTargetInfo)
    {
        LocalVariableTargetElement[] table =
            localVariableTargetInfo.table;

        LocalVariableTargetElement[] newTable =
            new LocalVariableTargetElement[localVariableTargetInfo.u2tableLength];

        LocalVariableTargetInfo newLocalVariableTargetInfo =
            new LocalVariableTargetInfo(localVariableTargetInfo.u1targetType,
                                        localVariableTargetInfo.u2tableLength,
                                        newTable);

        for (int index = 0; index < localVariableTargetInfo.u2tableLength; index++)
        {
            LocalVariableTargetElement element =
                localVariableTargetInfo.table[index];

            LocalVariableTargetElement newElement =
                new LocalVariableTargetElement(element.u2startPC,
                                               element.u2length,
                                               element.u2index);

            newTable[index] = newElement;
        }

        newLocalVariableTargetInfo.table = newTable;

        targetTypeAnnotation.targetInfo = newLocalVariableTargetInfo;
    }


    public void visitCatchTargetInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, CatchTargetInfo catchTargetInfo)
    {
        targetTypeAnnotation.targetInfo =
            new CatchTargetInfo(catchTargetInfo.u1targetType,
                                catchTargetInfo.u2exceptionTableIndex);
    }


    public void visitOffsetTargetInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, OffsetTargetInfo offsetTargetInfo)
    {
        targetTypeAnnotation.targetInfo =
            new OffsetTargetInfo(offsetTargetInfo.u1targetType,
                                 offsetTargetInfo.u2offset);
    }


    public void visitTypeArgumentTargetInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, TypeArgumentTargetInfo typeArgumentTargetInfo)
    {
        targetTypeAnnotation.targetInfo =
            new TypeArgumentTargetInfo(typeArgumentTargetInfo.u1targetType,
                                       typeArgumentTargetInfo.u2offset,
                                       typeArgumentTargetInfo.u1typeArgumentIndex);
    }
}