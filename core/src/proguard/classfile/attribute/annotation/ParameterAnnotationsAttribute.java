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
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;

/**
 * This {@link Attribute} represents a parameter annotations attribute.
 *
 * @author Eric Lafortune
 */
public abstract class ParameterAnnotationsAttribute extends Attribute
{
    // Note that the java compilers of JDK 1.5+ and of Eclipse count the
    // number of parameters of constructors of non-static inner classes and
    // of enum classes, based on the Signature attribute (if any), which
    // lacks the first one or two synthetic parameters. Unresolved issues:
    // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8024694
    // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8024694
    public int            u1parametersCount;
    public int[]          u2parameterAnnotationsCount;
    public Annotation[][] parameterAnnotations;


    /**
     * Creates an uninitialized ParameterAnnotationsAttribute.
     */
    protected ParameterAnnotationsAttribute()
    {
    }


    /**
     * Creates an initialized ParameterAnnotationsAttribute.
     */
    protected ParameterAnnotationsAttribute(int            u2attributeNameIndex,
                                            int            u1parametersCount,
                                            int[]          u2parameterAnnotationsCount,
                                            Annotation[][] parameterAnnotations)
    {
        super(u2attributeNameIndex);

        this.u1parametersCount           = u1parametersCount;
        this.u2parameterAnnotationsCount = u2parameterAnnotationsCount;
        this.parameterAnnotations        = parameterAnnotations;
    }


    /**
     * Applies the given visitor to all annotations.
     */
    public void annotationsAccept(Clazz clazz, Method method, AnnotationVisitor annotationVisitor)
    {
        // Loop over all parameters.
        for (int parameterIndex = 0; parameterIndex < u1parametersCount; parameterIndex++)
        {
            int          annotationsCount = u2parameterAnnotationsCount[parameterIndex];
            Annotation[] annotations      = parameterAnnotations[parameterIndex];

            // Loop over all parameter annotations.
            for (int index = 0; index < annotationsCount; index++)
            {
                // We don't need double dispatching here, since there is only one
                // type of Annotation.
                annotationVisitor.visitAnnotation(clazz, method, parameterIndex, annotations[index]);
            }
        }
    }
}
