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
package proguard.classfile.util;

import proguard.classfile.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;

/**
 * This ConstantVisitor initializes any class references of all string constants
 * it visits. More specifically, it fills out the references of string constant
 * pool entries that happen to refer to a class in the program class pool or in
 * the library class pool.
 *
 * @author Eric Lafortune
 */
public class StringReferenceInitializer
extends      SimplifiedVisitor
implements   ConstantVisitor
{
    private final ClassPool programClassPool;
    private final ClassPool libraryClassPool;


    /**
     * Creates a new StringReferenceInitializer.
     */
    public StringReferenceInitializer(ClassPool programClassPool,
                                      ClassPool libraryClassPool)
    {
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        if (stringConstant.referencedClass == null)
        {
            // See if we can find the referenced class.
            stringConstant.referencedClass =
                findClass(ClassUtil.internalClassName(
                          ClassUtil.externalBaseType(stringConstant.getString(clazz))));
        }
    }


    // Small utility methods.

    /**
     * Returns the class with the given name, either for the program class pool
     * or from the library class pool, or <code>null</code> if it can't be found.
     */
    private Clazz findClass(String name)
    {
        // First look for the class in the program class pool.
        Clazz clazz = programClassPool.getClass(name);

        // Otherwise look for the class in the library class pool.
        if (clazz == null)
        {
            clazz = libraryClassPool.getClass(name);
        }

        return clazz;
    }
}