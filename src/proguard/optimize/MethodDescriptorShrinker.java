/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2013 Eric Lafortune (eric@graphics.cornell.edu)
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
package proguard.optimize;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.ConstantPoolEditor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.MemberVisitor;
import proguard.optimize.info.*;
import proguard.optimize.peephole.VariableShrinker;

/**
 * This MemberVisitor removes unused parameters in the descriptors of the
 * methods that it visits.
 *
 * @see ParameterUsageMarker
 * @see VariableUsageMarker
 * @see VariableShrinker
 * @author Eric Lafortune
 */
public class MethodDescriptorShrinker
extends      SimplifiedVisitor
implements   MemberVisitor,
             AttributeVisitor
{
    private static final boolean DEBUG = false;


    private final MemberVisitor extraMemberVisitor;


    /**
     * Creates a new MethodDescriptorShrinker.
     */
    public MethodDescriptorShrinker()
    {
        this(null);
    }


    /**
     * Creates a new MethodDescriptorShrinker with an extra visitor.
     * @param extraMemberVisitor an optional extra visitor for all methods whose
     *                           parameters have been simplified.
     */
    public MethodDescriptorShrinker(MemberVisitor extraMemberVisitor)
    {
        this.extraMemberVisitor = extraMemberVisitor;
    }


    // Implementations for MemberVisitor.

    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        // Update the descriptor if it has any unused parameters.
        String descriptor    = programMethod.getDescriptor(programClass);
        String newDescriptor = shrinkDescriptor(programMethod, descriptor);

        if (!descriptor.equals(newDescriptor))
        {
            // Shrink the signature and parameter annotations.
            programMethod.attributesAccept(programClass, this);

            String name    = programMethod.getName(programClass);
            String newName = name;

            // Append a code, if the method isn't a class instance initializer.
            if (!name.equals(ClassConstants.INTERNAL_METHOD_NAME_INIT))
            {
                newName += ClassConstants.SPECIAL_MEMBER_SEPARATOR + Long.toHexString(Math.abs((descriptor).hashCode()));
            }

            if (DEBUG)
            {
                System.out.println("MethodDescriptorShrinker:");
                System.out.println("  ["+programClass.getName()+"."+
                                   name+descriptor+"] -> ["+
                                   newName+newDescriptor+"]");
            }

            ConstantPoolEditor constantPoolEditor =
                new ConstantPoolEditor(programClass);

            // Update the name, if necessary.
            if (!newName.equals(name))
            {
                programMethod.u2nameIndex =
                    constantPoolEditor.addUtf8Constant(newName);
            }

            // Update the referenced classes.
            programMethod.referencedClasses =
                shrinkReferencedClasses(programMethod,
                                        descriptor,
                                        programMethod.referencedClasses);

            // Finally, update the descriptor itself.
            programMethod.u2descriptorIndex =
                constantPoolEditor.addUtf8Constant(newDescriptor);

            // Visit the method, if required.
            if (extraMemberVisitor != null)
            {
                extraMemberVisitor.visitProgramMethod(programClass, programMethod);
            }
        }
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitSignatureAttribute(Clazz clazz, Method method, SignatureAttribute signatureAttribute)
    {
        // Compute the new signature.
        String signature    = clazz.getString(signatureAttribute.u2signatureIndex);
        String newSignature = shrinkDescriptor(method, signature);

        // Update the signature.
        signatureAttribute.u2signatureIndex =
            new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newSignature);

        // Update the referenced classes.
        signatureAttribute.referencedClasses =
            shrinkReferencedClasses(method,
                                    signature,
                                    signatureAttribute.referencedClasses);
    }


    public void visitAnyParameterAnnotationsAttribute(Clazz clazz, Method method, ParameterAnnotationsAttribute parameterAnnotationsAttribute)
    {
        int[]          annotationsCounts = parameterAnnotationsAttribute.u2parameterAnnotationsCount;
        Annotation[][] annotations       = parameterAnnotationsAttribute.parameterAnnotations;

        // All parameters of non-static methods are shifted by one in the local
        // variable frame.
        int parameterIndex =
            (method.getAccessFlags() & ClassConstants.INTERNAL_ACC_STATIC) != 0 ?
                0 : 1;

        int annotationIndex    = 0;
        int newAnnotationIndex = 0;

        // Go over the parameters.
        String descriptor = method.getDescriptor(clazz);
        InternalTypeEnumeration internalTypeEnumeration =
            new InternalTypeEnumeration(descriptor);

        while (internalTypeEnumeration.hasMoreTypes())
        {
            String type = internalTypeEnumeration.nextType();
            if (ParameterUsageMarker.isParameterUsed(method, parameterIndex))
            {
                annotationsCounts[newAnnotationIndex] = annotationsCounts[annotationIndex];
                annotations[newAnnotationIndex++]     = annotations[annotationIndex];
            }

            annotationIndex++;

            parameterIndex += ClassUtil.isInternalCategory2Type(type) ? 2 : 1;
        }

        // Update the number of parameters.
        parameterAnnotationsAttribute.u2parametersCount = newAnnotationIndex;

        // Clear the unused entries.
        while (newAnnotationIndex < annotationIndex)
        {
            annotationsCounts[newAnnotationIndex] = 0;
            annotations[newAnnotationIndex++]     = null;
        }
    }


    // Small utility methods.

    /**
     * Returns a shrunk descriptor or signature of the given method.
     */
    private String shrinkDescriptor(Method method, String descriptor)
    {
        // All parameters of non-static methods are shifted by one in the local
        // variable frame.
        int parameterIndex =
            (method.getAccessFlags() & ClassConstants.INTERNAL_ACC_STATIC) != 0 ?
                0 : 1;

        // Go over the parameters.
        InternalTypeEnumeration internalTypeEnumeration =
            new InternalTypeEnumeration(descriptor);

        StringBuffer newDescriptorBuffer = new StringBuffer();

        newDescriptorBuffer.append(internalTypeEnumeration.formalTypeParameters());
        newDescriptorBuffer.append(ClassConstants.INTERNAL_METHOD_ARGUMENTS_OPEN);

        while (internalTypeEnumeration.hasMoreTypes())
        {
            String type = internalTypeEnumeration.nextType();
            if (ParameterUsageMarker.isParameterUsed(method, parameterIndex))
            {
                newDescriptorBuffer.append(type);
            }
            else if (DEBUG)
            {
                System.out.println("  Deleting parameter #"+parameterIndex+" ["+type+"]");
            }

            parameterIndex += ClassUtil.isInternalCategory2Type(type) ? 2 : 1;
        }

        newDescriptorBuffer.append(ClassConstants.INTERNAL_METHOD_ARGUMENTS_CLOSE);
        newDescriptorBuffer.append(internalTypeEnumeration.returnType());

        return newDescriptorBuffer.toString();
    }


    /**
     * Shrinks the array of referenced classes of the given method.
     */
    private Clazz[] shrinkReferencedClasses(Method  method,
                                            String  descriptor,
                                            Clazz[] referencedClasses)
    {
        if (referencedClasses != null)
        {
            // All parameters of non-static methods are shifted by one in the local
            // variable frame.
            int parameterIndex =
                (method.getAccessFlags() & ClassConstants.INTERNAL_ACC_STATIC) != 0 ?
                    0 : 1;

            int referencedClassIndex    = 0;
            int newReferencedClassIndex = 0;

            // Go over the parameters.
            InternalTypeEnumeration internalTypeEnumeration =
                new InternalTypeEnumeration(descriptor);

            // Also look at the formal type parameters.
            String type  = internalTypeEnumeration.formalTypeParameters();
            int    count = new DescriptorClassEnumeration(type).classCount();
            for (int counter = 0; counter < count; counter++)
            {
                referencedClasses[newReferencedClassIndex++] =
                    referencedClasses[referencedClassIndex++];
            }

            while (internalTypeEnumeration.hasMoreTypes())
            {
                // Consider the classes referenced by this parameter type.
                type  = internalTypeEnumeration.nextType();
                count = new DescriptorClassEnumeration(type).classCount();

                if (ParameterUsageMarker.isParameterUsed(method, parameterIndex))
                {
                    // Copy the referenced classes.
                    for (int counter = 0; counter < count; counter++)
                    {
                        referencedClasses[newReferencedClassIndex++] =
                            referencedClasses[referencedClassIndex++];
                    }
                }
                else
                {
                    // Skip the referenced classes.
                    referencedClassIndex += count;
                }

                parameterIndex += ClassUtil.isInternalCategory2Type(type) ? 2 : 1;
            }

            // Also look at the return value.
            type  = internalTypeEnumeration.returnType();
            count = new DescriptorClassEnumeration(type).classCount();
            for (int counter = 0; counter < count; counter++)
            {
                referencedClasses[newReferencedClassIndex++] =
                    referencedClasses[referencedClassIndex++];
            }

            // Clear the unused entries.
            while (newReferencedClassIndex < referencedClassIndex)
            {
                referencedClasses[newReferencedClassIndex++] = null;
            }
        }

        return referencedClasses;
    }
}
