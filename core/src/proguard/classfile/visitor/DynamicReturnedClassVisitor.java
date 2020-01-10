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
package proguard.classfile.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.*;

/**
 * This {@link ConstantVisitor} lets a given {@link ClassVisitor} visit all the referenced
 * classes that are returned by the dynamic constants and invoke dynamic
 * constants that it visits.
 *
 * @author Eric Lafortune
 */
public class DynamicReturnedClassVisitor
extends      SimplifiedVisitor
implements   ConstantVisitor
{
    protected final ClassVisitor classVisitor;


    public DynamicReturnedClassVisitor(ClassVisitor classVisitor)
    {
        this.classVisitor = classVisitor;
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitDynamicConstant(Clazz clazz, DynamicConstant dynamicConstant)
    {
        // Is the method returning a class type?
        Clazz[] referencedClasses = dynamicConstant.referencedClasses;
        if (referencedClasses != null    &&
            referencedClasses.length > 0 &&
            ClassUtil.isInternalClassType(ClassUtil.internalMethodReturnType(dynamicConstant.getType(clazz))))
        {
            // Let the visitor visit the return type class, if any.
            Clazz referencedClass = referencedClasses[referencedClasses.length - 1];
            if (referencedClass != null)
            {
                referencedClass.accept(classVisitor);
            }
        }
    }


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        // Is the method returning a class type?
        Clazz[] referencedClasses = invokeDynamicConstant.referencedClasses;
        if (referencedClasses != null    &&
            referencedClasses.length > 0 &&
            ClassUtil.isInternalClassType(ClassUtil.internalMethodReturnType(invokeDynamicConstant.getType(clazz))))
        {
            // Let the visitor visit the return type class, if any.
            Clazz referencedClass = referencedClasses[referencedClasses.length - 1];
            if (referencedClass != null)
            {
                referencedClass.accept(classVisitor);
            }
        }
    }
}
