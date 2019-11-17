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
import proguard.classfile.visitor.ClassVisitor;

/**
 * This ElementValue represents a class element value.
 *
 * @author Eric Lafortune
 */
public class ClassElementValue extends ElementValue
{
    public int u2classInfoIndex;

    /**
     * An extra field pointing to the Clazz objects referenced in the
     * type name string. This field is filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer ClassReferenceInitializer}</code>.
     * References to primitive types are ignored.
     */
    public Clazz[] referencedClasses;


    /**
     * Creates an uninitialized ClassElementValue.
     */
    public ClassElementValue()
    {
    }


    /**
     * Creates an initialized ClassElementValue.
     */
    public ClassElementValue(int u2elementNameIndex,
                             int u2classInfoIndex)
    {
        super(u2elementNameIndex);

        this.u2classInfoIndex = u2classInfoIndex;
    }


    /**
     * Returns the class info name.
     */
    public String getClassName(Clazz clazz)
    {
        return clazz.getString(u2classInfoIndex);
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


    // Implementations for ElementValue.

    public char getTag()
    {
        return ClassConstants.ELEMENT_VALUE_CLASS;
    }

    public void accept(Clazz clazz, Annotation annotation, ElementValueVisitor elementValueVisitor)
    {
        elementValueVisitor.visitClassElementValue(clazz, annotation, this);
    }
}
