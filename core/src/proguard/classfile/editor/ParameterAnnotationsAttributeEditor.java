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
package proguard.classfile.editor;

import proguard.classfile.attribute.annotation.*;
import proguard.util.ArrayUtil;

/**
 * This class can add annotations to a given parameter annotations attribute.
 * Annotations to be added must have been filled out beforehand.
 *
 * @author Eric Lafortune
 */
public class ParameterAnnotationsAttributeEditor
{
    private ParameterAnnotationsAttribute targetParameterAnnotationsAttribute;


    /**
     * Creates a new ParameterAnnotationsAttributeEditor that will edit
     * annotations in the given parameter annotations attribute.
     */
    public ParameterAnnotationsAttributeEditor(ParameterAnnotationsAttribute targetParameterAnnotationsAttribute)
    {
        this.targetParameterAnnotationsAttribute = targetParameterAnnotationsAttribute;
    }


    /**
     * Adds a given annotation to the annotations attribute.
     */
    public void addAnnotation(int parameterIndex, Annotation annotation)
    {
        targetParameterAnnotationsAttribute.parameterAnnotations[parameterIndex] =
            ArrayUtil.add(targetParameterAnnotationsAttribute.parameterAnnotations[parameterIndex],
                          targetParameterAnnotationsAttribute.u2parameterAnnotationsCount[parameterIndex]++,
                          annotation);
    }

    /**
     * Deletes a given annotation from the annotations attribute.
     */
    public void deleteAnnotation(int parameterIndex, Annotation annotation)
    {
        int index = findAnnotationIndex(annotation,
                                        targetParameterAnnotationsAttribute.parameterAnnotations[parameterIndex],
                                        targetParameterAnnotationsAttribute.u2parameterAnnotationsCount[parameterIndex]);
        deleteAnnotation(parameterIndex, index);
    }

    /**
     * Deletes the annotation at the given index from the annotations attribute.
     */
    public void deleteAnnotation(int parameterIndex, int annotationIndex)
    {
        ArrayUtil.remove(targetParameterAnnotationsAttribute.parameterAnnotations[parameterIndex],
                         targetParameterAnnotationsAttribute.u2parameterAnnotationsCount[parameterIndex],
                         annotationIndex);
        targetParameterAnnotationsAttribute.u2parameterAnnotationsCount[parameterIndex]--;
    }

    private int findAnnotationIndex(Annotation annotation, Annotation[] annotations, int annotationCount)
    {
        for (int index = 0; index < annotationCount; index++)
        {
            if (annotation == annotations[index])
            {
                return index;
            }

        }
        return -1;
    }
}