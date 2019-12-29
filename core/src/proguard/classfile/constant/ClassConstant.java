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
package proguard.classfile.constant;

import proguard.classfile.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.ClassVisitor;

/**
 * This Constant represents a class constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public class ClassConstant extends Constant
{
    public int u2nameIndex;

    /**
     * An extra field pointing to the referenced Clazz object.
     * This field is filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer ClassReferenceInitializer}</code>.
     */
    public Clazz referencedClass;

    /**
     * An extra field pointing to the java.lang.Class Clazz object.
     * This field is typically filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer
     * ClassReferenceInitializer}</code>..
     */
    public Clazz javaLangClassClass;


    /**
     * Creates an uninitialized ClassConstant.
     */
    public ClassConstant()
    {
    }


    /**
     * Creates a new ClassConstant with the given name index.
     * @param u2nameIndex     the index of the name in the constant pool.
     * @param referencedClass the referenced class.
     */
    public ClassConstant(int   u2nameIndex,
                         Clazz referencedClass)
    {
        this.u2nameIndex     = u2nameIndex;
        this.referencedClass = referencedClass;
    }


    /**
     * Returns the name.
     */
    public String getName(Clazz clazz)
    {
        return clazz.getString(u2nameIndex);
    }


    // Implementations for Constant.

    public int getTag()
    {
        return Constant.CLASS;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        constantVisitor.visitClassConstant(clazz, this);
    }


    /**
     * Lets the referenced class accept the given visitor.
     */
    public void referencedClassAccept(ClassVisitor classVisitor)
    {
        if (referencedClass != null)
        {
            referencedClass.accept(classVisitor);
        }
    }
}
