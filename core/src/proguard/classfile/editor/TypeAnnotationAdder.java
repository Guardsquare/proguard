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

import proguard.classfile.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.util.ArrayUtil;

/**
 * This {@link TypeAnnotationVisitor} adds all type annotations that it visits to the given
 * target annotation element value, target annotation attribute, or target
 * parameter annotation attribute.
 *
 * @author Eric Lafortune
 */
public class TypeAnnotationAdder
extends      SimplifiedVisitor
implements   TypeAnnotationVisitor
{
    private static final ElementValue[] EMPTY_ELEMENT_VALUES = new ElementValue[0];


    private final ProgramClass               targetClass;
    private final AnnotationElementValue     targetAnnotationElementValue;
    private final AnnotationsAttributeEditor annotationsAttributeEditor;

    private final ConstantAdder constantAdder;


    /**
     * Creates a new TypeAnnotationAdder that will copy annotations into the given
     * target annotation element value.
     */
    public TypeAnnotationAdder(ProgramClass           targetClass,
                               AnnotationElementValue targetAnnotationElementValue)
    {
        this.targetClass                         = targetClass;
        this.targetAnnotationElementValue        = targetAnnotationElementValue;
        this.annotationsAttributeEditor          = null;

        constantAdder = new ConstantAdder(targetClass);
    }


    /**
     * Creates a new TypeAnnotationAdder that will copy annotations into the given
     * target annotations attribute.
     */
    public TypeAnnotationAdder(ProgramClass         targetClass,
                               AnnotationsAttribute targetAnnotationsAttribute)
    {
        this.targetClass                         = targetClass;
        this.targetAnnotationElementValue        = null;
        this.annotationsAttributeEditor          = new AnnotationsAttributeEditor(targetAnnotationsAttribute);

        constantAdder = new ConstantAdder(targetClass);
    }


    // Implementations for AnnotationVisitor.

    public void visitTypeAnnotation(Clazz clazz, TypeAnnotation typeAnnotation)
    {
        TypePathInfo[] typePath    = typeAnnotation.typePath;
        TypePathInfo[] newTypePath = new TypePathInfo[typePath.length];

        TypeAnnotation newTypeAnnotation =
            new TypeAnnotation(constantAdder.addConstant(clazz, typeAnnotation.u2typeIndex),
                               0,
                               typeAnnotation.u2elementValuesCount > 0 ?
                                   new ElementValue[typeAnnotation.u2elementValuesCount] :
                                   EMPTY_ELEMENT_VALUES,
                               null,
                               newTypePath);

        newTypeAnnotation.referencedClasses = ArrayUtil.cloneOrNull(typeAnnotation.referencedClasses);

        // Add the element values.
        typeAnnotation.elementValuesAccept(clazz,
                                           new ElementValueAdder(targetClass,
                                                                 newTypeAnnotation,
                                                                 false));

        // Set the target info.
        typeAnnotation.targetInfo.accept(clazz,
                                         typeAnnotation,
                                         new TargetInfoCopier(targetClass, newTypeAnnotation));

        // Copy the type path.
        for (int index = 0; index < typePath.length; index++)
        {
            TypePathInfo typePathInfo    = typePath[index];
            TypePathInfo newTypePathInfo = new TypePathInfo(typePathInfo.u1typePathKind,
                                                            typePathInfo.u1typeArgumentIndex);

            newTypePath[index] = newTypePathInfo;
        }

        // What's the target?
        if (targetAnnotationElementValue != null)
        {
            // Simply set the completed annotation.
            targetAnnotationElementValue.annotationValue = newTypeAnnotation;
        }
        else
        {
            // Add the completed annotation.
            annotationsAttributeEditor.addAnnotation(newTypeAnnotation);
        }
    }
}