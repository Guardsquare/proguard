/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.AccessConstants;
import proguard.classfile.ClassConstants;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMember;
import proguard.classfile.ProgramMethod;
import proguard.classfile.TypeConstants;
import proguard.classfile.editor.ConstantPoolEditor;
import proguard.classfile.editor.MemberReferenceFixer;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.util.InternalTypeEnumeration;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.value.Value;
import proguard.optimize.evaluation.StoringInvocationUnit;
import proguard.util.ProcessingFlags;

/**
 * This MemberVisitor specializes class types in the descriptors of the fields
 * and methods that it visits.
 *
 * Note that ClassReferenceFixer isn't necessary (and wouldn't work, because
 * of specialized array types), but MemberReferenceFixer is.
 *
 * @see StoringInvocationUnit
 * @see MemberReferenceFixer
 * @author Eric Lafortune
 */
public class MemberDescriptorSpecializer
implements   MemberVisitor
{
    private static final Logger logger = LogManager.getLogger(MemberDescriptorSpecializer.class);

    private final boolean       specializeFieldTypes;
    private final boolean       specializeMethodParameterTypes;
    private final boolean       specializeMethodReturnTypes;
    private final MemberVisitor extraTypeFieldVisitor;
    private final MemberVisitor extraParameterTypeMethodVisitor;
    private final MemberVisitor extraReturnTypeMethodVisitor;


    /**
     * Creates a new MemberDescriptorSpecializer.
     * @param specializeFieldTypes           specifies whether field types
     *                                       should be specialized.
     * @param specializeMethodParameterTypes specifies whether method parameter
     *                                       types should be specialized.
     * @param specializeMethodReturnTypes    specifies whether method return
     *                                       types should be specialized.
     */
    public MemberDescriptorSpecializer(boolean specializeFieldTypes,
                                       boolean specializeMethodParameterTypes,
                                       boolean specializeMethodReturnTypes)
    {
        this(specializeFieldTypes,
             specializeMethodParameterTypes,
             specializeMethodReturnTypes,
             null,
             null,
             null);
    }


    /**
     * Creates a new MemberDescriptorSpecializer with extra visitors.
     * @param specializeFieldTypes            specifies whether field types
     *                                        should be specialized.
     * @param specializeMethodParameterTypes  specifies whether method parameter
     *                                        types should be specialized.
     * @param specializeMethodReturnTypes     specifies whether method return
     *                                        types should be specialized.
     * @param extraTypeFieldVisitor           an optional extra visitor for all
     *                                        fields whose types have been
     *                                        specialized.
     * @param extraParameterTypeMethodVisitor an optional extra visitor for all
     *                                        methods whose parameter types have
     *                                        been specialized.
     * @param extraReturnTypeMethodVisitor    an optional extra visitor for all
     *                                        methods whose return types have
     *                                        been specialized.
     */
    public MemberDescriptorSpecializer(boolean       specializeFieldTypes,
                                       boolean       specializeMethodParameterTypes,
                                       boolean       specializeMethodReturnTypes,
                                       MemberVisitor extraTypeFieldVisitor,
                                       MemberVisitor extraParameterTypeMethodVisitor,
                                       MemberVisitor extraReturnTypeMethodVisitor)
    {
        this.specializeFieldTypes            = specializeFieldTypes;
        this.specializeMethodParameterTypes  = specializeMethodParameterTypes;
        this.specializeMethodReturnTypes     = specializeMethodReturnTypes;
        this.extraTypeFieldVisitor           = extraTypeFieldVisitor;
        this.extraParameterTypeMethodVisitor = extraParameterTypeMethodVisitor;
        this.extraReturnTypeMethodVisitor    = extraReturnTypeMethodVisitor;
    }


    // Implementations for MemberVisitor.

    @Override
    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        String fieldType = programField.getDescriptor(programClass);
        if (specializeFieldTypes &&
            ClassUtil.isInternalClassType(fieldType))
        {
            // Is the value's type different from the declared type?
            Value  value     = StoringInvocationUnit.getFieldValue(programField);
            String valueType = valueType(fieldType, value);

            if (!valueType.equals(fieldType))
            {
                // Is the value's class really more specific than the declared
                // type? Sometimes, it's an unrelated class or interface, which
                // can cause problems.
                Clazz valueClass = value.referenceValue().getReferencedClass();

                if (valueClass != null &&
                    valueClass.extendsOrImplements(ClassUtil.internalClassNameFromClassType(fieldType)) &&
                    (valueClass.getProcessingFlags() & ProcessingFlags.IS_CLASS_AVAILABLE) != 0)
                {
                    logger.debug("MemberDescriptorSpecializer [{}.{} {}] -> {}",
                                 programClass.getName(),
                                 programField.getName(programClass),
                                 programField.getDescriptor(programClass),
                                 valueType
                    );

                    // Set the specialized referenced class.
                    programField.referencedClass = value.referenceValue().getReferencedClass();

                    // Update the descriptor (and name).
                    updateDescriptor(programClass, programField, valueType);

                    // Visit the field, if required.
                    if (extraTypeFieldVisitor != null)
                    {
                        extraTypeFieldVisitor.visitProgramField(programClass, programField);
                    }
                }
            }
        }
    }


    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        // Only bother if there are referenced classes.
        Clazz[] referencedClasses = programMethod.referencedClasses;
        if (referencedClasses != null)
        {
            String descriptor = programMethod.getDescriptor(programClass);

            // All parameters of non-static methods are shifted by one in the local
            // variable frame.
            boolean isStatic =
                (programMethod.getAccessFlags() & AccessConstants.STATIC) != 0;

            int parameterIndex = isStatic ? 0 : 1;
            int classIndex     = 0;

            // Go over the parameters.
            InternalTypeEnumeration parameterTypeEnumeration =
                new InternalTypeEnumeration(descriptor);

            StringBuilder newDescriptorBuffer = new StringBuilder(descriptor.length());
            newDescriptorBuffer.append(TypeConstants.METHOD_ARGUMENTS_OPEN);

            while (parameterTypeEnumeration.hasMoreTypes())
            {
                String parameterType = parameterTypeEnumeration.nextType();
                if (specializeMethodParameterTypes &&
                    ClassUtil.isInternalClassType(parameterType))
                {
                    // Is the value's type different from the declared type?
                    Value  value     = StoringInvocationUnit.getMethodParameterValue(programMethod, parameterIndex);
                    String valueType = valueType(parameterType, value);

                    if (!valueType.equals(parameterType))
                    {
                        // Is the value's class really more specific than the
                        // declared type? Sometimes, it's an unrelated class or
                        // interface, which can cause problems.
                        Clazz valueClass = value.referenceValue().getReferencedClass();

                        if (valueClass != null &&
                            valueClass.extendsOrImplements(ClassUtil.internalClassNameFromClassType(parameterType)) &&
                            (valueClass.getProcessingFlags() & ProcessingFlags.IS_CLASS_AVAILABLE) != 0)
                        {
                            logger.debug("MemberDescriptorSpecializer [{}.{}{}]: parameter #{}: {} -> {}",
                                    programClass.getName(),
                                    programMethod.getName(programClass),
                                    descriptor,
                                    parameterIndex,
                                    parameterType,
                                    valueType
                            );

                            // Set the specialized type.
                            referencedClasses[classIndex] = valueClass;

                            // Visit the method, if required.
                            if (extraParameterTypeMethodVisitor != null)
                            {
                                extraParameterTypeMethodVisitor.visitProgramMethod(programClass, programMethod);
                            }

                            parameterType = valueType;
                        }
                    }
                }

                // Compose the new descriptor.
                newDescriptorBuffer.append(parameterType);

                // Note that the specialized type may no longer be a class type,
                // for instance when specializing java.lang.Object to int[].
                if (ClassUtil.isInternalClassType(parameterType))
                {
                    classIndex++;
                }

                parameterIndex++;
            }

            newDescriptorBuffer.append(TypeConstants.METHOD_ARGUMENTS_CLOSE);

            // Check the return type.
            String returnType = parameterTypeEnumeration.returnType();
            if (specializeMethodReturnTypes &&
                ClassUtil.isInternalClassType(returnType))
            {
                // Is the value's type more specific than the declared type?
                Value  value     = StoringInvocationUnit.getMethodReturnValue(programMethod);
                String valueType = valueType(returnType, value);

                if (!valueType.equals(returnType))
                {
                    // Is the value's class really more specific than the
                    // declared type? Sometimes, it's an unrelated class or
                    // interface, which can cause problems.
                    Clazz valueClass = value.referenceValue().getReferencedClass();

                    if (valueClass != null &&
                        valueClass.extendsOrImplements(ClassUtil.internalClassNameFromClassType(returnType)) &&
                        (valueClass.getProcessingFlags() & ProcessingFlags.IS_CLASS_AVAILABLE) != 0)
                    {
                        logger.debug("MemberDescriptorSpecializer [{}.{}{}]: return value: {} ->  {}",
                                     programClass.getName(),
                                     programMethod.getName(programClass),
                                     descriptor,
                                     returnType,
                                     valueType
                        );

                        // Set the specialized type.
                        referencedClasses[classIndex] = value.referenceValue().getReferencedClass();

                        // Visit the method, if required.
                        if (extraReturnTypeMethodVisitor != null)
                        {
                            extraReturnTypeMethodVisitor.visitProgramMethod(programClass, programMethod);
                        }

                        returnType = valueType;
                    }
                }
            }

            // Compose the new descriptor.
            newDescriptorBuffer.append(returnType);

            // Is the new descriptor different from the old one?
            String newDescriptor = newDescriptorBuffer.toString();
            if (!newDescriptor.equals(descriptor))
            {
                // Update the descriptor (and name).
                updateDescriptor(programClass, programMethod, newDescriptor);
            }
        }
    }


    // Small utility methods.

    /**
     * Returns the specialized type, if possible, based on the given value.
     */
    private String valueType(String type, Value value)
    {
        // The type always remains unchanged if it's not a reference value.
        if (value == null ||
            value.computationalType() != Value.TYPE_REFERENCE)
        {
            return type;
        }

        // The type may be null if the reference value is always null.
        String valueType = value.referenceValue().getType();
        if (valueType == null)
        {
            return type;
        }

        return valueType;
    }


    /**
     * Sets the given new descriptor on the given class member, and ensures that
     * it gets a unique name.
     */
    private void updateDescriptor(ProgramClass  programClass,
                                  ProgramMember programMember,
                                  String        newDescriptor)
    {
        String descriptor = programMember.getDescriptor(programClass);

        ConstantPoolEditor constantPoolEditor =
            new ConstantPoolEditor(programClass);

        // Update the descriptor.
        programMember.u2descriptorIndex =
            constantPoolEditor.addUtf8Constant(newDescriptor);

        // Update the name.
        String name    = programMember.getName(programClass);
        String newName = newUniqueMemberName(name, descriptor);
        programMember.u2nameIndex =
            constantPoolEditor.addUtf8Constant(newName);
    }


    /**
     * Returns a unique class member name, based on the given name and descriptor.
     */
    private String newUniqueMemberName(String name, String descriptor)
    {
        return name.equals(ClassConstants.METHOD_NAME_INIT) ?
            ClassConstants.METHOD_NAME_INIT :
            name + TypeConstants.SPECIAL_MEMBER_SEPARATOR + Long.toHexString(Math.abs((descriptor).hashCode()));
    }
}
