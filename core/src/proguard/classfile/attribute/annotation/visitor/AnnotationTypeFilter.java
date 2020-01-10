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
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.util.*;

import java.util.List;

/**
 * This {@link AnnotationVisitor} delegates its visits to another given
 * {@link AnnotationVisitor}, but only when the visited annotation has
 * a type that matches a given regular expression.
 *
 * @author Eric Lafortune
 */
public class AnnotationTypeFilter
implements   AnnotationVisitor
{
    private final StringMatcher     stringMatcher;
    private final AnnotationVisitor annotationVisitor;


    /**
     * Creates a new AnnotationTypeFilter.
     * @param regularExpression the regular expression against which
     *                          annotation type names will be matched.
     * @param annotationVisitor the annotation visitor to which visits
     *                          will be delegated.
     */
    public AnnotationTypeFilter(String            regularExpression,
                                AnnotationVisitor annotationVisitor)
    {
        this(regularExpression, null, annotationVisitor);
    }


    /**
     * Creates a new AnnotationTypeFilter.
     * @param regularExpression the regular expression against which
     *                          annotation type names will be matched.
     * @param wildcardManager   an optional scope for StringMatcher instances
     *                          that match wildcards.
     * @param annotationVisitor the annotation visitor to which visits
     *                          will be delegated.
     */
    public AnnotationTypeFilter(String            regularExpression,
                                WildcardManager   wildcardManager,
                                AnnotationVisitor annotationVisitor)
    {
        this(new ListParser(new ClassNameParser(wildcardManager)).parse(regularExpression),
             annotationVisitor);
    }


    /**
     * Creates a new AnnotationTypeFilter.
     * @param stringMatcher     the matcher against which annotation type names
     *                          will be matched.
     * @param annotationVisitor the <code>annotationVisitor</code> to which
     *                          visits will be delegated.
     */
    public AnnotationTypeFilter(StringMatcher     stringMatcher,
                                AnnotationVisitor annotationVisitor)
    {
        this.stringMatcher     = stringMatcher;
        this.annotationVisitor = annotationVisitor;
    }


    // Implementations for AnnotationVisitor.

    public void visitAnnotation(Clazz clazz, Annotation annotation)
    {
        if (accepted(annotation.getType(clazz)))
        {
            annotationVisitor.visitAnnotation(clazz, annotation);
        }
    }


    public void visitAnnotation(Clazz clazz, Field field, Annotation annotation)
    {
        if (accepted(annotation.getType(clazz)))
        {
            annotationVisitor.visitAnnotation(clazz, field, annotation);
        }
    }


    public void visitAnnotation(Clazz clazz, Method method, Annotation annotation)
    {
        if (accepted(annotation.getType(clazz)))
        {
            annotationVisitor.visitAnnotation(clazz, method, annotation);
        }
    }


    public void visitAnnotation(Clazz clazz, Method method, int parameterIndex, Annotation annotation)
    {
        if (accepted(annotation.getType(clazz)))
        {
            annotationVisitor.visitAnnotation(clazz, method, parameterIndex, annotation);
        }
    }


    public void visitAnnotation(Clazz clazz, Method method, CodeAttribute codeAttribute, Annotation annotation)
    {
        if (accepted(annotation.getType(clazz)))
        {
            annotationVisitor.visitAnnotation(clazz, method, codeAttribute, annotation);
        }
    }


    // Small utility methods.

    private boolean accepted(String name)
    {
        return stringMatcher.matches(name);
    }
}
