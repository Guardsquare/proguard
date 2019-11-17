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
package proguard.classfile.attribute;

import proguard.classfile.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.*;

/**
 * This Attribute represents an enclosing method attribute.
 *
 * @author Eric Lafortune
 */
public class EnclosingMethodAttribute extends Attribute
{
    public int u2classIndex;
    public int u2nameAndTypeIndex;

    /**
     * An extra field pointing to the referenced Clazz object.
     * This field is typically filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer
     * ClassReferenceInitializer}</code>.
     */
    public Clazz referencedClass;

    /**
     * An extra field optionally pointing to the referenced Method object.
     * This field is typically filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer
     * ClassReferenceInitializer}</code>.
     */
    public Method referencedMethod;


    /**
     * Creates an uninitialized EnclosingMethodAttribute.
     */
    public EnclosingMethodAttribute()
    {
    }


    /**
     * Creates an initialized EnclosingMethodAttribute.
     */
    public EnclosingMethodAttribute(int u2attributeNameIndex,
                                    int u2classIndex,
                                    int u2nameAndTypeIndex)
    {
        super(u2attributeNameIndex);

        this.u2classIndex       = u2classIndex;
        this.u2nameAndTypeIndex = u2nameAndTypeIndex;
    }


    /**
     * Returns the class name.
     */
    public String getClassName(Clazz clazz)
    {
        return clazz.getClassName(u2classIndex);
    }

    /**
     * Returns the method/field name.
     */
    public String getName(Clazz clazz)
    {
        return clazz.getName(u2nameAndTypeIndex);
    }

    /**
     * Returns the type.
     */
    public String getType(Clazz clazz)
    {
        return clazz.getType(u2nameAndTypeIndex);
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


    /**
     * Lets the referenced class member accept the given visitor.
     */
    public void referencedMethodAccept(MemberVisitor memberVisitor)
    {
        if (referencedMethod != null)
        {
            referencedMethod.accept(referencedClass,
                                    memberVisitor);
        }
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitEnclosingMethodAttribute(clazz, this);
    }
}
