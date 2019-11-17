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

import proguard.classfile.Clazz;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.util.SimpleVisitorAccepter;

/**
 * Representation of an annotation.
 *
 * @author Eric Lafortune
 */
public class Annotation extends SimpleVisitorAccepter
{
    public int            u2typeIndex;
    public int            u2elementValuesCount;
    public ElementValue[] elementValues;

    /**
     * An extra field pointing to the Clazz objects referenced in the
     * type string. This field is typically filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer
     * ClassReferenceInitializer}</code>.
     * References to primitive types are ignored.
     */
    public Clazz[] referencedClasses;

    /**
     * Creates an uninitialized Annotation.
     */
    public Annotation()
    {
    }


    /**
     * Creates an initialized Annotation.
     */
    public Annotation(int            u2typeIndex,
                      int            u2elementValuesCount,
                      ElementValue[] elementValues)
    {
        this.u2typeIndex          = u2typeIndex;
        this.u2elementValuesCount = u2elementValuesCount;
        this.elementValues        = elementValues;
    }


    /**
     * Returns the type.
     */
    public String getType(Clazz clazz)
    {
        return clazz.getString(u2typeIndex);
    }


    /**
     * Applies the given visitor to the first referenced class. This is the
     * main annotation class.
     */
    public void referencedClassAccept(ClassVisitor classVisitor)
    {
        if (referencedClasses != null)
        {
            Clazz referencedClass = referencedClasses[0];
            if (referencedClass != null)
            {
                referencedClass.accept(classVisitor);
            }
        }
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
     * Applies the given visitor to the specified element value pair.
     */
    public void elementValueAccept(Clazz clazz, int index, ElementValueVisitor elementValueVisitor)
    {
        elementValues[index].accept(clazz, this, elementValueVisitor);
    }


    /**
     * Applies the given visitor to all element value pairs.
     */
    public void elementValuesAccept(Clazz clazz, ElementValueVisitor elementValueVisitor)
    {
        for (int index = 0; index < u2elementValuesCount; index++)
        {
            elementValues[index].accept(clazz, this, elementValueVisitor);
        }
    }
}
