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
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.visitor.*;

/**
 * This ElementValue represents an enumeration constant element value.
 *
 * @author Eric Lafortune
 */
public class EnumConstantElementValue extends ElementValue
{
    public int u2typeNameIndex;
    public int u2constantNameIndex;

    /**
     * An extra field pointing to the Clazz objects referenced in the
     * type name string. This field is typically filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer
     * ClassReferenceInitializer}</code>.
     * References to primitive types are ignored.
     */
    public Clazz[] referencedClasses;

    /**
     * An extra field optionally pointing to the referenced enum Field object.
     * This field is typically filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer
     * ClassReferenceInitializer}</code>.
     */
    public Field referencedField;


    /**
     * Creates an uninitialized EnumConstantElementValue.
     */
    public EnumConstantElementValue()
    {
    }


    /**
     * Creates an initialized EnumConstantElementValue.
     */
    public EnumConstantElementValue(int u2elementNameIndex,
                                    int u2typeNameIndex,
                                    int u2constantNameIndex)
    {
        super(u2elementNameIndex);

        this.u2typeNameIndex     = u2typeNameIndex;
        this.u2constantNameIndex = u2constantNameIndex;
    }


    /**
     * Returns the enumeration type name.
     */
    public String getTypeName(Clazz clazz)
    {
        return clazz.getString(u2typeNameIndex);
    }


    /**
     * Returns the constant name.
     */
    public String getConstantName(Clazz clazz)
    {
        return clazz.getString(u2constantNameIndex);
    }


    /**
     * Applies the given visitor to all referenced classes.
     */
    public void referencedClassesAccept(ClassVisitor classVisitor)
    {
        if (referencedClasses != null)
        {
            for (int index = 0; index < referencedClasses.length; index++)
            {
                Clazz referencedClass = referencedClasses[index];
                if (referencedClass != null)
                {
                    referencedClass.accept(classVisitor);
                }
            }
        }
    }


    /**
     * Applies the given visitor to the referenced field.
     */
    public void referencedFieldAccept(MemberVisitor memberVisitor)
    {
        if (referencedField != null)
        {
            referencedField.accept(referencedClasses[0],
                                   memberVisitor);
        }
    }


    // Implementations for ElementValue.

    public char getTag()
    {
        return ElementValue.TAG_ENUM_CONSTANT;
    }

    public void accept(Clazz clazz, Annotation annotation, ElementValueVisitor elementValueVisitor)
    {
        elementValueVisitor.visitEnumConstantElementValue(clazz, annotation, this);
    }
}
