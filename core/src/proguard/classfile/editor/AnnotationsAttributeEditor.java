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
 * This class can add annotations to a given annotations attribute.
 * Annotations to be added must have been filled out beforehand.
 *
 * @author Eric Lafortune
 */
public class AnnotationsAttributeEditor
{
    private AnnotationsAttribute targetAnnotationsAttribute;


    /**
     * Creates a new AnnotationsAttributeEditor that will edit annotations in
     * the given annotations attribute.
     */
    public AnnotationsAttributeEditor(AnnotationsAttribute targetAnnotationsAttribute)
    {
        this.targetAnnotationsAttribute = targetAnnotationsAttribute;
    }


    /**
     * Adds a given annotation to the annotations attribute.
     */
    public void addAnnotation(Annotation annotation)
    {
        int          annotationsCount = targetAnnotationsAttribute.u2annotationsCount;
        Annotation[] annotations      = targetAnnotationsAttribute.annotations;

        // Make sure there is enough space for the new annotation.
        if (annotations.length <= annotationsCount)
        {
            targetAnnotationsAttribute.annotations = new Annotation[annotationsCount+1];
            System.arraycopy(annotations, 0,
                             targetAnnotationsAttribute.annotations, 0,
                             annotationsCount);
            annotations = targetAnnotationsAttribute.annotations;
        }

        // Add the annotation.
        annotations[targetAnnotationsAttribute.u2annotationsCount++] = annotation;
    }


    /**
     * Deletes a given annotation from the annotations attribute.
     */
    public void deleteAnnotation(Annotation annotation)
    {
        int index = findAnnotationIndex(annotation,
                                        targetAnnotationsAttribute.annotations,
                                        targetAnnotationsAttribute.u2annotationsCount);
        deleteAnnotation(index);
    }


    /**
     * Deletes the annotation at the given idnex from the annotations attribute.
     */
    public void deleteAnnotation(int index)
    {
        ArrayUtil.remove(targetAnnotationsAttribute.annotations,
                         targetAnnotationsAttribute.u2annotationsCount,
                         index);
        targetAnnotationsAttribute.u2annotationsCount--;
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