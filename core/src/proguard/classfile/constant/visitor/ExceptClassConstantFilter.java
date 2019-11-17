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
package proguard.classfile.constant.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This <code>ConstantVisitor</code> delegates its visits to class constants
 * to another given <code>ConstantVisitor</code>, except for one given class.
 *
 * @author Eric Lafortune
 */
public class ExceptClassConstantFilter
extends      SimplifiedVisitor
implements   ConstantVisitor
{
    private final String           exceptClassName;
    private final ConstantVisitor constantVisitor;


    /**
     * Creates a new ExceptClassConstantFilter.
     * @param exceptClassName the name of the class that will not be visited.
     * @param constantVisitor the <code>ConstantVisitor</code> to which visits
     *                        will be delegated.
     */
    public ExceptClassConstantFilter(String          exceptClassName,
                                     ConstantVisitor constantVisitor)
    {
        this.exceptClassName = exceptClassName;
        this.constantVisitor = constantVisitor;
    }


    // Implementations for ConstantVisitor.

    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        if (!classConstant.getName(clazz).equals(exceptClassName))
        {
            constantVisitor.visitClassConstant(clazz, classConstant);
        }
    }
}