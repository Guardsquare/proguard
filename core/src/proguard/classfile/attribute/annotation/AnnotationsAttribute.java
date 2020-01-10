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
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;

/**
 * This {@link Attribute} represents an annotations attribute.
 *
 * @author Eric Lafortune
 */
public abstract class AnnotationsAttribute extends Attribute
{
    public int          u2annotationsCount;
    public Annotation[] annotations;


    /**
     * Creates an uninitialized AnnotationsAttribute.
     */
    protected AnnotationsAttribute()
    {
    }


    /**
     * Creates an initialized AnnotationsAttribute.
     */
    protected AnnotationsAttribute(int          u2attributeNameIndex,
                                   int          u2annotationsCount,
                                   Annotation[] annotations)
    {
        super(u2attributeNameIndex);

        this.u2annotationsCount = u2annotationsCount;
        this.annotations        = annotations;
    }


    /**
     * Applies the given visitor to the specified class annotation.
     */
    public void annotationAccept(Clazz clazz, int index, AnnotationVisitor annotationVisitor)
    {
        // We don't need double dispatching here, since there is only one
        // type of Annotation.
        annotationVisitor.visitAnnotation(clazz, annotations[index]);
    }


    /**
     * Applies the given visitor to all class annotations.
     */
    public void annotationsAccept(Clazz clazz, AnnotationVisitor annotationVisitor)
    {
        for (int index = 0; index < u2annotationsCount; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of Annotation.
            annotationVisitor.visitAnnotation(clazz, annotations[index]);
        }
    }


    /**
     * Applies the given visitor to the specified field annotation.
     */
    public void annotationAccept(Clazz clazz, Field field, int index, AnnotationVisitor annotationVisitor)
    {
        // We don't need double dispatching here, since there is only one
        // type of Annotation.
        annotationVisitor.visitAnnotation(clazz, field, annotations[index]);
    }


    /**
     * Applies the given visitor to all field annotations.
     */
    public void annotationsAccept(Clazz clazz, Field field, AnnotationVisitor annotationVisitor)
    {
        for (int index = 0; index < u2annotationsCount; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of Annotation.
            annotationVisitor.visitAnnotation(clazz, field, annotations[index]);
        }
    }


    /**
     * Applies the given visitor to the specified method annotation.
     */
    public void annotationAccept(Clazz clazz, Method method, int index, AnnotationVisitor annotationVisitor)
    {
        // We don't need double dispatching here, since there is only one
        // type of Annotation.
        annotationVisitor.visitAnnotation(clazz, method, annotations[index]);
    }


    /**
     * Applies the given visitor to all method annotations.
     */
    public void annotationsAccept(Clazz clazz, Method method, AnnotationVisitor annotationVisitor)
    {
        for (int index = 0; index < u2annotationsCount; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of Annotation.
            annotationVisitor.visitAnnotation(clazz, method, annotations[index]);
        }
    }


    /**
     * Applies the given visitor to all code attribute annotations.
     */
    public void annotationsAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, AnnotationVisitor annotationVisitor)
    {
        for (int index = 0; index < u2annotationsCount; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of Annotation.
            annotationVisitor.visitAnnotation(clazz, method, codeAttribute, annotations[index]);
        }
    }
}
