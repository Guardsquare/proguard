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
import proguard.classfile.attribute.annotation.visitor.*;

/**
 * This {@link ElementValue} represents an annotation element value.
 *
 * @author Eric Lafortune
 */
public class AnnotationElementValue extends ElementValue
{
    public Annotation annotationValue;


    /**
     * Creates an uninitialized AnnotationElementValue.
     */
    public AnnotationElementValue()
    {
    }


    /**
     * Creates an initialized AnnotationElementValue.
     */
    public AnnotationElementValue(int        u2elementNameIndex,
                                  Annotation annotationValue)
    {
        super(u2elementNameIndex);

        this.annotationValue = annotationValue;
    }


    /**
     * Applies the given visitor to the annotation.
     */
    public void annotationAccept(Clazz clazz, AnnotationVisitor annotationVisitor)
    {
        annotationVisitor.visitAnnotation(clazz, annotationValue);
    }


    // Implementations for ElementValue.

    public char getTag()
    {
        return ElementValue.TAG_ANNOTATION;
    }

    public void accept(Clazz clazz, Annotation annotation, ElementValueVisitor elementValueVisitor)
    {
        elementValueVisitor.visitAnnotationElementValue(clazz, annotation, this);
    }
}
