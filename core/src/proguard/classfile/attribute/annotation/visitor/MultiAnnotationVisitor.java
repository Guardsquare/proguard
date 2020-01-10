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
package proguard.classfile.attribute.annotation.visitor;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.util.ArrayUtil;

/**
 * This {@link AnnotationVisitor} delegates all visits to each {@link AnnotationVisitor}
 * in a given list.
 *
 * @author Thomas Neidhart
 */
public class MultiAnnotationVisitor implements AnnotationVisitor
{
    private AnnotationVisitor[] annotationVisitors;
    private int                 annotationVisitorCount;


    public MultiAnnotationVisitor()
    {
        this.annotationVisitors = new AnnotationVisitor[16];
    }


    public MultiAnnotationVisitor(AnnotationVisitor... annotationVisitors)
    {
        this.annotationVisitors     = annotationVisitors;
        this.annotationVisitorCount = annotationVisitors.length;
    }


    public void addAnnotationVisitor(AnnotationVisitor annotationVisitor)
    {
        annotationVisitors =
            ArrayUtil.add(annotationVisitors,
                          annotationVisitorCount++,
                          annotationVisitor);
    }


    // Implementations for AnnotationVisitor.


    public void visitAnnotation(Clazz clazz, Annotation annotation)
    {
        for (int index = 0; index < annotationVisitorCount; index++)
        {
            annotationVisitors[index].visitAnnotation(clazz, annotation);
        }
    }


    public void visitAnnotation(Clazz clazz, Field field, Annotation annotation)
    {
        for (int index = 0; index < annotationVisitorCount; index++)
        {
            annotationVisitors[index].visitAnnotation(clazz, field, annotation);
        }
    }


    public void visitAnnotation(Clazz clazz, Method method, Annotation annotation)
    {
        for (int index = 0; index < annotationVisitorCount; index++)
        {
            annotationVisitors[index].visitAnnotation(clazz, method, annotation);
        }
    }


    public void visitAnnotation(Clazz clazz, Method method, int parameterIndex, Annotation annotation)
    {
        for (int index = 0; index < annotationVisitorCount; index++)
        {
            annotationVisitors[index].visitAnnotation(clazz, method, parameterIndex, annotation);
        }
    }


    public void visitAnnotation(Clazz clazz, Method method, CodeAttribute codeAttribute, Annotation annotation)
    {
        for (int index = 0; index < annotationVisitorCount; index++)
        {
            annotationVisitors[index].visitAnnotation(clazz, method, codeAttribute, annotation);
        }
    }
}
