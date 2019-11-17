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
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This <code>ConstantVisitor</code> delegates its visits to class constants
 * to another given <code>ConstantVisitor</code>, except for classes that
 * are extended or implemented by a given class. This exception includes the
 * class itself.
 *
 * @author Eric Lafortune
 */
public class ImplementingClassConstantFilter
extends      SimplifiedVisitor
implements   ConstantVisitor
{
    private final Clazz           implementingClass;
    private final ConstantVisitor constantVisitor;


    /**
     * Creates a new ImplementingClassConstantFilter.
     * @param implementingClass the class whose superclasses and interfaces will
     *                          not be visited.
     * @param constantVisitor   the <code>ConstantVisitor</code> to which visits
     *                          will be delegated.
     */
    public ImplementingClassConstantFilter(Clazz           implementingClass,
                                           ConstantVisitor constantVisitor)
    {
        this.implementingClass = implementingClass;
        this.constantVisitor   = constantVisitor;
    }


    // Implementations for ConstantVisitor.

    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        Clazz referencedClass = classConstant.referencedClass;
        if (referencedClass == null ||
            !implementingClass.extendsOrImplements(referencedClass))
        {
            constantVisitor.visitClassConstant(clazz, classConstant);
        }
    }
}