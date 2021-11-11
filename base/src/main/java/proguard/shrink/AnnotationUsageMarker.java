/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.shrink;

import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.*;
import proguard.util.ProcessingFlags;

/**
 * This AttributeVisitor recursively marks all necessary annotation information
 * in the attributes that it visits.
 *
 * @see ClassUsageMarker
 *
 * @author Eric Lafortune
 */
public class AnnotationUsageMarker
implements   AttributeVisitor,
             AnnotationVisitor,
             ElementValueVisitor,
             ConstantVisitor,
             ClassVisitor,
             MemberVisitor
{
    private final ClassUsageMarker classUsageMarker;

    // Fields acting as a return parameters for several methods.
    private boolean attributeUsed;
    private boolean annotationUsed;
    private boolean allClassesUsed;
    private boolean methodUsed;


    /**
     * Creates a new AnnotationUsageMarker.
     * @param classUsageMarker the marker to mark and check the classes and
     *                         class members.
     */
    public AnnotationUsageMarker(ClassUsageMarker classUsageMarker)
    {
        this.classUsageMarker = classUsageMarker;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitAnyAnnotationsAttribute(Clazz clazz, AnnotationsAttribute annotationsAttribute)
    {
        // Mark the necessary annotation information.
        attributeUsed = false;
        annotationsAttribute.annotationsAccept(clazz, this);

        if (attributeUsed)
        {
            // We got a positive used flag, so some annotation is being used.
            // Mark this attribute as being used as well.
            classUsageMarker.markAsUsed(annotationsAttribute);

            markConstant(clazz, annotationsAttribute.u2attributeNameIndex);
        }
    }


    public void visitAnyParameterAnnotationsAttribute(Clazz clazz, Method method, ParameterAnnotationsAttribute parameterAnnotationsAttribute)
    {
        // Mark the necessary annotation information.
        attributeUsed = false;
        parameterAnnotationsAttribute.annotationsAccept(clazz, method, this);

        if (attributeUsed)
        {
            // We got a positive used flag, so some annotation is being used.
            // Mark this attribute as being used as well.
            classUsageMarker.markAsUsed(parameterAnnotationsAttribute);

            markConstant(clazz, parameterAnnotationsAttribute.u2attributeNameIndex);
        }
    }


    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        // Mark the necessary annotation information in any annotation elements.
        annotationDefaultAttribute.defaultValueAccept(clazz, this);

        // Always mark annotation defaults.
        classUsageMarker.markAsUsed(annotationDefaultAttribute);

        markConstant(clazz, annotationDefaultAttribute.u2attributeNameIndex);
    }


    // Implementations for AnnotationVisitor.

    public void visitAnnotation(Clazz clazz, Annotation annotation)
    {
        if (isReferencedClassUsed(annotation))
        {
            // Mark the annotation as being used.
            classUsageMarker.markAsUsed(annotation);

            markConstant(clazz, annotation.u2typeIndex);

            // Mark the necessary element values.
            annotation.elementValuesAccept(clazz, this);

            // The return values.
            annotationUsed = true;
            attributeUsed  = true;
        }
    }


    // Implementations for ElementValueVisitor.

    public void visitConstantElementValue(Clazz clazz, Annotation annotation, ConstantElementValue constantElementValue)
    {
        if (isReferencedMethodUsed(constantElementValue))
        {
            // Mark the element value as being used.
            classUsageMarker.markAsUsed(constantElementValue);

            markConstant(clazz, constantElementValue.u2elementNameIndex);
            markConstant(clazz, constantElementValue.u2constantValueIndex);
        }
    }


    public void visitEnumConstantElementValue(Clazz clazz, Annotation annotation, EnumConstantElementValue enumConstantElementValue)
    {
        if (isReferencedMethodUsed(enumConstantElementValue))
        {
            // Check the referenced classes.
            allClassesUsed = true;
            enumConstantElementValue.referencedClassesAccept(this);

            if (allClassesUsed)
            {
                // Mark the element value as being used.
                classUsageMarker.markAsUsed(enumConstantElementValue);

                markConstant(clazz, enumConstantElementValue.u2elementNameIndex);
                markConstant(clazz, enumConstantElementValue.u2typeNameIndex);
                markConstant(clazz, enumConstantElementValue.u2constantNameIndex);
            }
        }
    }


    public void visitClassElementValue(Clazz clazz, Annotation annotation, ClassElementValue classElementValue)
    {
        if (isReferencedMethodUsed(classElementValue))
        {
            // Mark the element value as being used.
            classUsageMarker.markAsUsed(classElementValue);

            markConstant(clazz, classElementValue.u2elementNameIndex);
            markConstant(clazz, classElementValue.u2classInfoIndex);

            // Mark the referenced classes, since they can be retrieved from
            // the annotation and then used.
            // TODO: This could mark more annotation methods, affecting other annotations.
            classElementValue.referencedClassesAccept(classUsageMarker);
        }
    }


    public void visitAnnotationElementValue(Clazz clazz, Annotation annotation, AnnotationElementValue annotationElementValue)
    {
        if (isReferencedMethodUsed(annotationElementValue))
        {
            boolean oldAnnotationUsed = annotationUsed;

            // Check and mark the contained annotation.
            annotationUsed = false;
            annotationElementValue.annotationAccept(clazz, this);

            if (annotationUsed)
            {
                // Mark the element value as being used.
                classUsageMarker.markAsUsed(annotationElementValue);

                markConstant(clazz, annotationElementValue.u2elementNameIndex);
            }

            annotationUsed = oldAnnotationUsed;
        }
    }


    public void visitArrayElementValue(Clazz clazz, Annotation annotation, ArrayElementValue arrayElementValue)
    {
        if (isReferencedMethodUsed(arrayElementValue))
        {
            // Check and mark the contained element values.
            arrayElementValue.elementValuesAccept(clazz, annotation, this);

            // Mark the element value as being used.
            classUsageMarker.markAsUsed(arrayElementValue);

            markConstant(clazz, arrayElementValue.u2elementNameIndex);
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant)
    {
        // Mark the constant (and apply the optional extra visitor).
        constant.accept(clazz, classUsageMarker);
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Is the class constant marked as being used?
        if (!classUsageMarker.isUsed(classConstant))
        {
            // Check the referenced class.
            allClassesUsed = true;
            classConstant.referencedClassAccept(this);

            // Is the referenced class marked as being used?
            if (allClassesUsed)
            {
                // Mark the class constant and its Utf8 constant.
                visitAnyConstant(clazz, classConstant);
            }
        }
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz)
    {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support " + clazz.getClass().getName());
    }


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        allClassesUsed &= classUsageMarker.isUsed(programClass);

        // Add this special case for kotlin DebugMetadata annotation.
        // Unless it is explicitly kept for shrinking, remove it as it exposes
        // information about the original class / member names, see DGD-1361.
        if (programClass.getName().equals(KotlinConstants.NAME_KOTLIN_COROUTINES_DEBUG_METADATA))
        {
            allClassesUsed &= (programClass.getProcessingFlags() & ProcessingFlags.DONT_SHRINK) != 0;
        }
    }


    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Add this special case for kotlin Metadata / DebugMetadata annotation
        // in library projects. We want to strip it (default is to keep -libraryjar
        // annotations). You can override this by keeping kotlin.Metadata in configuration.
        if (libraryClass.getName().equals(KotlinConstants.NAME_KOTLIN_METADATA) ||
            libraryClass.getName().equals(KotlinConstants.NAME_KOTLIN_COROUTINES_DEBUG_METADATA))
        {
            allClassesUsed &= (libraryClass.getProcessingFlags() & ProcessingFlags.DONT_SHRINK) != 0;
        }
    }


    // Implementations for MemberVisitor.

    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        methodUsed = classUsageMarker.isUsed(programMethod);
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
    }


    // Small utility methods.

    /**
     * Returns whether the annotation class has been marked as being used.
     */
    private boolean isReferencedClassUsed(Annotation annotation)
    {
        // Check if the referenced class is being used.
        allClassesUsed = true;
        annotation.referencedClassAccept(this);

        return allClassesUsed;
    }


    /**
     * Returns whether the annotation method has been marked as being used.
     */
    private boolean isReferencedMethodUsed(ElementValue elementValue)
    {
        // Check if the referenced method is being used.
        methodUsed = true;
        elementValue.referencedMethodAccept(this);

        return methodUsed;
    }


    /**
     * Marks the specified constant pool entry.
     */
    private void markConstant(Clazz clazz, int index)
    {
        if (index > 0)
        {
            clazz.constantPoolEntryAccept(index, this);
        }
    }

}
