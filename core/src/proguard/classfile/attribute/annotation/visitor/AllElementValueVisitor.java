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
 * This AttributeVisitor and AnnotationVisitor lets a given ElementValueVisitor
 * visit all ElementValue objects of the attributes or annotations that it
 * visits.
 *
 * @author Eric Lafortune
 */
public class AllElementValueVisitor
extends      SimplifiedVisitor
implements   AttributeVisitor,
             AnnotationVisitor,
             ElementValueVisitor
{
    private final boolean             deep;
    private final ElementValueVisitor elementValueVisitor;


    /**
     * Creates a new AllElementValueVisitor.
     * @param elementValueVisitor the AllElementValueVisitor to which visits
     *                            will be delegated.
     */
    public AllElementValueVisitor(ElementValueVisitor elementValueVisitor)
    {
        this(false, elementValueVisitor);
    }


    /**
     * Creates a new AllElementValueVisitor.
     * @param deep                specifies whether the element values
     *                            further down the hierarchy should be
     *                            visited too.
     * @param elementValueVisitor the AllElementValueVisitor to which visits
     *                            will be delegated.
     */
    public AllElementValueVisitor(boolean             deep,
                                  ElementValueVisitor elementValueVisitor)
    {
        this.deep                = deep;
        this.elementValueVisitor = elementValueVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeVisibleAnnotationsAttribute.annotationsAccept(clazz, this);
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeVisibleAnnotationsAttribute.annotationsAccept(clazz, field, this);
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeVisibleAnnotationsAttribute.annotationsAccept(clazz, method, this);
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeInvisibleAnnotationsAttribute.annotationsAccept(clazz, this);
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeInvisibleAnnotationsAttribute.annotationsAccept(clazz, field, this);
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        // Visit the annotations.
        runtimeInvisibleAnnotationsAttribute.annotationsAccept(clazz, method, this);
    }


    public void visitAnyParameterAnnotationsAttribute(Clazz clazz, Method method, ParameterAnnotationsAttribute parameterAnnotationsAttribute)
    {
        // Visit the annotations.
        parameterAnnotationsAttribute.annotationsAccept(clazz, method, this);
    }


    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        // Visit the default element value.
        annotationDefaultAttribute.defaultValueAccept(clazz, this);
    }


    // Implementations for AnnotationVisitor.

    public void visitAnnotation(Clazz clazz, Annotation annotation)
    {
        annotation.elementValuesAccept(clazz, this);
    }


    public void visitAnnotation(Clazz clazz, Field field, Annotation annotation)
    {
        annotation.elementValuesAccept(clazz, this);
    }


    public void visitAnnotation(Clazz clazz, Method method, Annotation annotation)
    {
        annotation.elementValuesAccept(clazz, this);
    }


    public void visitAnnotation(Clazz clazz, Method method, int parameterIndex, Annotation annotation)
    {
        annotation.elementValuesAccept(clazz, this);
    }


    public void visitAnnotation(Clazz clazz, Method method, CodeAttribute codeAttribute, Annotation annotation)
    {
        annotation.elementValuesAccept(clazz, this);
    }


    // Implementations for ElementValueVisitor.

    public void visitConstantElementValue(Clazz clazz, Annotation annotation, ConstantElementValue constantElementValue)
    {
        elementValueVisitor.visitConstantElementValue(clazz, annotation, constantElementValue);
    }


    public void visitEnumConstantElementValue(Clazz clazz, Annotation annotation, EnumConstantElementValue enumConstantElementValue)
    {
        elementValueVisitor.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
    }


    public void visitClassElementValue(Clazz clazz, Annotation annotation, ClassElementValue classElementValue)
    {
        elementValueVisitor.visitClassElementValue(clazz, annotation, classElementValue);
    }


    public void visitAnnotationElementValue(Clazz clazz, Annotation annotation, AnnotationElementValue annotationElementValue)
    {
        elementValueVisitor.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        if (deep)
        {
            annotationElementValue.annotationAccept(clazz, this);
        }
    }


    public void visitArrayElementValue(Clazz clazz, Annotation annotation, ArrayElementValue arrayElementValue)
    {
        elementValueVisitor.visitArrayElementValue(clazz, annotation, arrayElementValue);

        if (deep)
        {
            arrayElementValue.elementValuesAccept(clazz, annotation, elementValueVisitor);
        }
    }
}
