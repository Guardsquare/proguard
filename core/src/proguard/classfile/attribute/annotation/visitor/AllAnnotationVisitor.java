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
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This {@link AttributeVisitor} lets a given {@link AnnotationVisitor} visit all Annotation
 * instances of the attributes it visits.
 *
 * @author Eric Lafortune
 */
public class AllAnnotationVisitor
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    private final AnnotationVisitor annotationVisitor;


    /**
     * Creates a new AllAnnotationVisitor.
     * @param annotationVisitor the AnnotationVisitor to which visits will be
     *                          delegated.
     */
    public AllAnnotationVisitor(AnnotationVisitor annotationVisitor)
    {
        this.annotationVisitor = annotationVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeVisibleAnnotationsAttribute.annotationsAccept(clazz, annotationVisitor);
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeVisibleAnnotationsAttribute.annotationsAccept(clazz, field, annotationVisitor);
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeVisibleAnnotationsAttribute.annotationsAccept(clazz, method, annotationVisitor);
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeInvisibleAnnotationsAttribute.annotationsAccept(clazz, annotationVisitor);
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeInvisibleAnnotationsAttribute.annotationsAccept(clazz, field, annotationVisitor);
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeInvisibleAnnotationsAttribute.annotationsAccept(clazz, method, annotationVisitor);
    }


    public void visitAnyParameterAnnotationsAttribute(Clazz clazz, Method method, ParameterAnnotationsAttribute parameterAnnotationsAttribute)
    {
        // Visit the annotations.
        parameterAnnotationsAttribute.annotationsAccept(clazz, method, annotationVisitor);
    }


    public void visitRuntimeVisibleTypeAnnotationsAttribute(Clazz clazz, RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeVisibleTypeAnnotationsAttribute.annotationsAccept(clazz, annotationVisitor);
    }


    public void visitRuntimeVisibleTypeAnnotationsAttribute(Clazz clazz, Field field, RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeVisibleTypeAnnotationsAttribute.annotationsAccept(clazz, field, annotationVisitor);
    }


    public void visitRuntimeVisibleTypeAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeVisibleTypeAnnotationsAttribute.annotationsAccept(clazz, method, annotationVisitor);
    }


    public void visitRuntimeVisibleTypeAnnotationsAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeVisibleTypeAnnotationsAttribute.annotationsAccept(clazz, method, codeAttribute, annotationVisitor);
    }


    public void visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz clazz, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeInvisibleTypeAnnotationsAttribute.annotationsAccept(clazz, annotationVisitor);
    }


    public void visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz clazz, Field field, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeInvisibleTypeAnnotationsAttribute.annotationsAccept(clazz, field, annotationVisitor);
    }


    public void visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeInvisibleTypeAnnotationsAttribute.annotationsAccept(clazz, method, annotationVisitor);
    }


    public void visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeInvisibleTypeAnnotationsAttribute.annotationsAccept(clazz, method, codeAttribute, annotationVisitor);
    }
}
