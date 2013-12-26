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
package proguard.optimize.evaluation;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.optimize.info.*;

/**
 * This ClassVisitor simplifies the descriptors that contain simple enums in
 * the program classes that it visits.
 *
 * @see SimpleEnumMarker
 * @see MemberReferenceFixer
 * @author Eric Lafortune
 */
public class SimpleEnumDescriptorSimplifier
extends      SimplifiedVisitor
implements   ClassVisitor,
             ConstantVisitor,
             MemberVisitor,
             AttributeVisitor,
             LocalVariableInfoVisitor,
             LocalVariableTypeInfoVisitor
{
    //*
    private static final boolean DEBUG = false;
    /*/
    private static       boolean DEBUG = System.getProperty("enum") != null;
    //*/

    private final InstructionVisitor extraInstructionVisitor;

    private final PartialEvaluator    partialEvaluator;
    private final CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor(false, true);


    /**
     * Creates a new SimpleEnumDescriptorSimplifier.
     */
    public SimpleEnumDescriptorSimplifier()
    {
        this(new PartialEvaluator(), null);
    }


    /**
     * Creates a new SimpleEnumDescriptorSimplifier.
     * @param partialEvaluator        the partial evaluator that will
     *                                execute the code and provide
     *                                information about the results.
     * @param extraInstructionVisitor an optional extra visitor for all
     *                                simplified instructions.
     */
    public SimpleEnumDescriptorSimplifier(PartialEvaluator   partialEvaluator,
                                          InstructionVisitor extraInstructionVisitor)
    {
        this.partialEvaluator        = partialEvaluator;
        this.extraInstructionVisitor = extraInstructionVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        if (DEBUG)
        {
            System.out.println("SimpleEnumDescriptorSimplifier: "+programClass.getName());
        }

        // Simplify the class members.
        programClass.fieldsAccept(this);
        programClass.methodsAccept(this);

        // Simplify the attributes.
        //programClass.attributesAccept(this);

        // Simplify the simple enum array constants.
        programClass.constantPoolEntriesAccept(this);
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        // Does the constant refer to a simple enum type?
        Clazz referencedClass = stringConstant.referencedClass;
        if (isSimpleEnum(referencedClass))
        {
            // Is it an array type?
            String name = stringConstant.getString(clazz);
            if (ClassUtil.isInternalArrayType(name))
            {
                // Update the type.
                ConstantPoolEditor constantPoolEditor =
                    new ConstantPoolEditor((ProgramClass)clazz);

                String newName = simplifyDescriptor(name, referencedClass);

                stringConstant.u2stringIndex =
                    constantPoolEditor.addUtf8Constant(newName);

                // Clear the referenced class.
                stringConstant.referencedClass = null;
            }
        }
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Does the constant refer to a simple enum type?
        Clazz referencedClass = classConstant.referencedClass;
        if (isSimpleEnum(referencedClass))
        {
            // Is it an array type?
            String name = classConstant.getName(clazz);
            if (ClassUtil.isInternalArrayType(name))
            {
                // Update the type.
                ConstantPoolEditor constantPoolEditor =
                    new ConstantPoolEditor((ProgramClass)clazz);

                String newName = simplifyDescriptor(name, referencedClass);

                classConstant.u2nameIndex =
                    constantPoolEditor.addUtf8Constant(newName);

                // Clear the referenced class.
                classConstant.referencedClass = null;
            }
        }
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        // Update the descriptor if it has a simple enum class.
        String descriptor    = programField.getDescriptor(programClass);
        String newDescriptor = simplifyDescriptor(descriptor, programField.referencedClass);

        if (!descriptor.equals(newDescriptor))
        {
            String name    = programField.getName(programClass);
            String newName = name + ClassConstants.SPECIAL_MEMBER_SEPARATOR + Long.toHexString(Math.abs((descriptor).hashCode()));

            if (DEBUG)
            {
                System.out.println("SimpleEnumDescriptorSimplifier: ["+programClass.getName()+"."+name+" "+descriptor + "] -> ["+newName+" "+newDescriptor+"]");
            }

            ConstantPoolEditor constantPoolEditor =
                new ConstantPoolEditor(programClass);

            // Update the name.
            programField.u2nameIndex =
                constantPoolEditor.addUtf8Constant(newName);

            // Update the descriptor itself.
            programField.u2descriptorIndex =
                constantPoolEditor.addUtf8Constant(newDescriptor);

            // Clear the referenced class.
            programField.referencedClass = null;

            // Clear the field value.
            FieldOptimizationInfo fieldOptimizationInfo =
                FieldOptimizationInfo.getFieldOptimizationInfo(programField);
            if (fieldOptimizationInfo != null)
            {
                fieldOptimizationInfo.resetValue(programClass, programField);
            }

            // Simplify the signature.
            programField.attributesAccept(programClass, this);
        }
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
//        // Skip the valueOf method.
//        if (programMethod.getName(programClass).equals(ClassConstants.INTERNAL_METHOD_NAME_VALUEOF))
//        {
//            return;
//        }

        // Simplify the code, the signature, and the parameter annotations,
        // before simplifying the descriptor.
        programMethod.attributesAccept(programClass, this);

        // Update the descriptor if it has any simple enum classes.
        String descriptor    = programMethod.getDescriptor(programClass);
        String newDescriptor = simplifyDescriptor(descriptor, programMethod.referencedClasses);

        if (!descriptor.equals(newDescriptor))
        {
            String name    = programMethod.getName(programClass);
            String newName = name;

            // Append a code, if the method isn't a class instance initializer.
            if (!name.equals(ClassConstants.INTERNAL_METHOD_NAME_INIT))
            {
                newName += ClassConstants.SPECIAL_MEMBER_SEPARATOR + Long.toHexString(Math.abs((descriptor).hashCode()));
            }

            if (DEBUG)
            {
                System.out.println("SimpleEnumDescriptorSimplifier: ["+programClass.getName()+"."+name+descriptor+"] -> ["+newName+newDescriptor+"]");
            }

            ConstantPoolEditor constantPoolEditor =
                new ConstantPoolEditor(programClass);

            // Update the name, if necessary.
            if (!newName.equals(name))
            {
                programMethod.u2nameIndex =
                    constantPoolEditor.addUtf8Constant(newName);
            }

            // Update the descriptor itself.
            programMethod.u2descriptorIndex =
                constantPoolEditor.addUtf8Constant(newDescriptor);
        }
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // Simplify the local variable descriptors.
        codeAttribute.attributesAccept(clazz, method, this);
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        // Change the references of the local variables.
        localVariableTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        // Change the references of the local variables.
        localVariableTypeTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    public void visitSignatureAttribute(Clazz clazz, Field field, SignatureAttribute signatureAttribute)
    {
        // We're only looking at the base type for now.
        if (signatureAttribute.referencedClasses != null &&
            signatureAttribute.referencedClasses.length > 0)
        {
            // Update the signature if it has any simple enum classes.
            String signature    = clazz.getString(signatureAttribute.u2signatureIndex);
            String newSignature = simplifyDescriptor(signature,
                                                     signatureAttribute.referencedClasses[0]);

            if (!signature.equals(newSignature))
            {
                // Update the signature.
                signatureAttribute.u2signatureIndex =
                    new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newSignature);

                // Clear the referenced class.
                signatureAttribute.referencedClasses[0] = null;
            }
        }
    }


    public void visitSignatureAttribute(Clazz clazz, Method method, SignatureAttribute signatureAttribute)
    {
        // Compute the new signature.
        String signature    = clazz.getString(signatureAttribute.u2signatureIndex);
        String newSignature = simplifyDescriptor(signature,
                                                 signatureAttribute.referencedClasses);

        if (!signature.equals(newSignature))
        {
            // Update the signature.
            signatureAttribute.u2signatureIndex =
                new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newSignature);
        }
    }


   // Implementations for LocalVariableInfoVisitor.

    public void visitLocalVariableInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableInfo localVariableInfo)
    {
        // Update the descriptor if it has a simple enum class.
        String descriptor    = clazz.getString(localVariableInfo.u2descriptorIndex);
        String newDescriptor = simplifyDescriptor(descriptor, localVariableInfo.referencedClass);

        if (!descriptor.equals(newDescriptor))
        {
            // Update the descriptor.
            localVariableInfo.u2descriptorIndex =
                new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newDescriptor);

            // Clear the referenced class.
            localVariableInfo.referencedClass = null;
        }
    }


    // Implementations for LocalVariableTypeInfoVisitor.

    public void visitLocalVariableTypeInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeInfo localVariableTypeInfo)
    {
        // We're only looking at the base type for now.
        if (localVariableTypeInfo.referencedClasses != null &&
            localVariableTypeInfo.referencedClasses.length > 0)
        {
            // Update the signature if it has any simple enum classes.
            String signature    = clazz.getString(localVariableTypeInfo.u2signatureIndex);
            String newSignature = simplifyDescriptor(signature,
                                                     localVariableTypeInfo.referencedClasses[0]);

            if (!signature.equals(newSignature))
            {
                // Update the signature.
                localVariableTypeInfo.u2signatureIndex =
                    new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newSignature);

                // Clear the referenced class.
                localVariableTypeInfo.referencedClasses[0] = null;
            }
        }
    }


    // Small utility methods.

    /**
     * Returns the descriptor with simplified enum type.
     */
    private String simplifyDescriptor(String descriptor,
                                      Clazz  referencedClass)
    {
        return isSimpleEnum(referencedClass) ?
                   descriptor.substring(0, ClassUtil.internalArrayTypeDimensionCount(descriptor)) + ClassConstants.INTERNAL_TYPE_INT :
                   descriptor;
    }


    /**
     * Returns the descriptor with simplified enum types and removes any enum
     * classes that can be simplified from the referenced classes.
     */
    private String simplifyDescriptor(String  descriptor,
                                      Clazz[] referencedClasses)
    {
        if (referencedClasses != null)
        {
            int referencedClassIndex    = 0;
            int newReferencedClassIndex = 0;

            // Go over the parameters.
            InternalTypeEnumeration internalTypeEnumeration =
                new InternalTypeEnumeration(descriptor);

            StringBuffer newDescriptorBuffer = new StringBuffer();

            newDescriptorBuffer.append(internalTypeEnumeration.formalTypeParameters());
            newDescriptorBuffer.append(ClassConstants.INTERNAL_METHOD_ARGUMENTS_OPEN);

            // Also look at the formal type parameters.
            String type  = internalTypeEnumeration.formalTypeParameters();
            int    count = new DescriptorClassEnumeration(type).classCount();
            for (int counter = 0; counter < count; counter++)
            {
                Clazz referencedClass =
                    referencedClasses[referencedClassIndex++];

                if (!isSimpleEnum(referencedClass))
                {
                    referencedClasses[newReferencedClassIndex++] =
                        referencedClass;
                }
            }

            while (internalTypeEnumeration.hasMoreTypes())
            {
                // Consider the classes referenced by this parameter type.
                type  = internalTypeEnumeration.nextType();
                count = new DescriptorClassEnumeration(type).classCount();

                // Copy the referenced classes.
                for (int counter = 0; counter < count; counter++)
                {
                    Clazz referencedClass =
                        referencedClasses[referencedClassIndex++];

                    if (!isSimpleEnum(referencedClass))
                    {
                        referencedClasses[newReferencedClassIndex++] =
                            referencedClass;
                    }
                    else
                    {
                        type =
                            type.substring(0, ClassUtil.internalArrayTypeDimensionCount(type)) +
                            ClassConstants.INTERNAL_TYPE_INT;
                    }
                }

                newDescriptorBuffer.append(type);
            }

            // Also look at the return value.
            type  = internalTypeEnumeration.returnType();
            count = new DescriptorClassEnumeration(type).classCount();

            for (int counter = 0; counter < count; counter++)
            {
                Clazz referencedClass =
                    referencedClasses[referencedClassIndex++];

                if (!isSimpleEnum(referencedClass))
                {
                    referencedClasses[newReferencedClassIndex++] =
                        referencedClass;
                }
                else
                {
                    type =
                        type.substring(0, ClassUtil.internalArrayTypeDimensionCount(
                            type)) +
                        ClassConstants.INTERNAL_TYPE_INT;
                }
            }

            newDescriptorBuffer.append(ClassConstants.INTERNAL_METHOD_ARGUMENTS_CLOSE);
            newDescriptorBuffer.append(type);

            // Clear the unused entries.
            while (newReferencedClassIndex < referencedClassIndex)
            {
                referencedClasses[newReferencedClassIndex++] = null;
            }

            descriptor = newDescriptorBuffer.toString();
        }

        return descriptor;
    }


    /**
     * Returns whether the given class is not null and a simple enum class.
     */
    private boolean isSimpleEnum(Clazz clazz)
    {
        return clazz != null &&
               SimpleEnumMarker.isSimpleEnum(clazz);
    }
}
