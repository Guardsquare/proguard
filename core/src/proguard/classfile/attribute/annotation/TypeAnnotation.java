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
package proguard.classfile.attribute.annotation;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.annotation.target.TargetInfo;
import proguard.classfile.attribute.annotation.target.visitor.TargetInfoVisitor;
import proguard.classfile.attribute.annotation.visitor.*;

/**
 * Representation of a type annotation.
 *
 * @author Eric Lafortune
 */
public class TypeAnnotation extends Annotation
{
    public TargetInfo     targetInfo;
    public TypePathInfo[] typePath;


    /**
     * Creates an uninitialized TypeAnnotation.
     */
    public TypeAnnotation()
    {
    }


    /**
     * Creates an initialized TypeAnnotation.
     */
    public TypeAnnotation(int            u2typeIndex,
                          int            u2elementValuesCount,
                          ElementValue[] elementValues,
                          TargetInfo     targetInfo,
                          TypePathInfo[] typePath)
    {
        super(u2typeIndex, u2elementValuesCount, elementValues);

        this.targetInfo = targetInfo;
        this.typePath   = typePath;
    }


    /**
     * Applies the given visitor to the target info.
     */
    public void targetInfoAccept(Clazz clazz, TargetInfoVisitor targetInfoVisitor)
    {
        // We don't need double dispatching here, since there is only one
        // type of TypePathInfo.
        targetInfo.accept(clazz, this, targetInfoVisitor);
    }


    /**
     * Applies the given visitor to the target info.
     */
    public void targetInfoAccept(Clazz clazz, Field field, TargetInfoVisitor targetInfoVisitor)
    {
        // We don't need double dispatching here, since there is only one
        // type of TypePathInfo.
        targetInfo.accept(clazz, field, this, targetInfoVisitor);
    }


    /**
     * Applies the given visitor to the target info.
     */
    public void targetInfoAccept(Clazz clazz, Method method, TargetInfoVisitor targetInfoVisitor)
    {
        // We don't need double dispatching here, since there is only one
        // type of TypePathInfo.
        targetInfo.accept(clazz, method, this, targetInfoVisitor);
    }


    /**
     * Applies the given visitor to the target info.
     */
    public void targetInfoAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, TargetInfoVisitor targetInfoVisitor)
    {
        // We don't need double dispatching here, since there is only one
        // type of TypePathInfo.
        targetInfo.accept(clazz, method, codeAttribute, this, targetInfoVisitor);
    }


    /**
     * Applies the given visitor to all type path elements.
     */
    public void typePathInfosAccept(Clazz clazz, TypePathInfoVisitor typePathVisitor)
    {
        for (int index = 0; index < typePath.length; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of TypePathInfo.
            typePathVisitor.visitTypePathInfo(clazz, this, typePath[index]);
        }
    }


    /**
     * Applies the given visitor to all type path elements.
     */
    public void typePathInfosAccept(Clazz clazz, Field field, TypePathInfoVisitor typePathVisitor)
    {
        for (int index = 0; index < typePath.length; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of TypePathInfo.
            typePathVisitor.visitTypePathInfo(clazz, field, this, typePath[index]);
        }
    }


    /**
     * Applies the given visitor to all type path elements.
     */
    public void typePathInfosAccept(Clazz clazz, Method method, TypePathInfoVisitor typePathVisitor)
    {
        for (int index = 0; index < typePath.length; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of TypePathInfo.
            typePathVisitor.visitTypePathInfo(clazz, method, this, typePath[index]);
        }
    }


    /**
     * Applies the given visitor to all type path elements.
     */
    public void typePathInfosAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, TypePathInfoVisitor typePathVisitor)
    {
        for (int index = 0; index < typePath.length; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of TypePathInfo.
            typePathVisitor.visitTypePathInfo(clazz, method, codeAttribute, typeAnnotation, typePath[index]);
        }
    }
}
