/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
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
package proguard.optimize.peephole;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.visitor.*;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.util.ArrayUtil;

import java.util.Arrays;

/**
 * This ClassVisitor replaces references to classes and class members if the
 * classes have targets that are intended to replace them.
 *
 * @see VerticalClassMerger
 * @see ClassReferenceFixer
 * @see MemberReferenceFixer
 * @author Eric Lafortune
 */
public class TargetClassChanger
implements   ClassVisitor,

             // Implementation interfaces.
             ConstantVisitor,
             MemberVisitor,
             AttributeVisitor,
             RecordComponentInfoVisitor,
             LocalVariableInfoVisitor,
             LocalVariableTypeInfoVisitor,
             AnnotationVisitor,
             ElementValueVisitor
{
    private static final Logger logger = LogManager.getLogger(TargetClassChanger.class);


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz)
    {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support " + clazz.getClass().getName());
    }


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        // We're only making changes locally in the class.
        // Not all other classes may have been retargeted yet.

        // Change the references of the constant pool.
        programClass.constantPoolEntriesAccept(this);

        // Change the references of the class members.
        programClass.fieldsAccept(this);
        programClass.methodsAccept(this);

        // Change the references of the attributes.
        programClass.attributesAccept(this);

        // Remove duplicate interfaces and interface classes that have ended
        // up pointing to the class itself.
        boolean[] delete = null;
        for (int index = 0; index < programClass.u2interfacesCount; index++)
        {
            Clazz interfaceClass = programClass.getInterface(index);
            if (interfaceClass != null &&
                (programClass.equals(interfaceClass) ||
                 containsInterfaceClass(programClass,
                                        index,
                                        interfaceClass)))
            {
                // Lazily create the array.
                if (delete == null)
                {
                    delete = new boolean[programClass.u2interfacesCount];
                }

                delete[index] = true;
            }
        }

        if (delete != null)
        {
            new InterfaceDeleter(delete, false).visitProgramClass(programClass);
        }

        // Is the class being retargeted?
        Clazz targetClass = ClassMerger.getTargetClass(programClass);
        if (targetClass != null)
        {
            // We're not changing anything special in the superclass and
            // interface hierarchy of the retargeted class. The shrinking
            // step will remove the class for us.

            // Restore the class name. We have to add a new class entry
            // to avoid an existing entry with the same name being reused. The
            // names have to be fixed later, based on their referenced classes.
            programClass.u2thisClass =
                addNewClassConstant(programClass,
                                    programClass.getName(),
                                    programClass);

            // This class will no longer have any subclasses, because their
            // subclasses and interfaces will be retargeted.
            programClass.subClassCount = 0;

            // Clear all elements.
            Arrays.fill(programClass.subClasses, null);
        }
        else
        {
            // Retarget subclasses, avoiding duplicates and the class itself.
            programClass.subClassCount =
                updateUniqueReferencedClasses(programClass.subClasses,
                                              programClass.subClassCount,
                                              programClass);

            // TODO: Maybe restore private method references.
        }

        // The class merger already made sure that any new interfaces got this
        // class as a subclass. Now the superclass and interfaces have been
        // retargeted, also make sure that they have this class as a subclass.
        // Retargeting subclasses may not be sufficient, for example if a class
        // didn't have subclasses before.
        ConstantVisitor subclassAdder =
            new ReferencedClassVisitor(
            new SubclassFilter(programClass,
            new SubclassAdder(programClass)));

        programClass.superClassConstantAccept(subclassAdder);
        programClass.interfaceConstantsAccept(subclassAdder);
    }


    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Retarget subclasses, avoiding duplicates.
        libraryClass.subClassCount =
            updateUniqueReferencedClasses(libraryClass.subClasses,
                                          libraryClass.subClassCount,
                                          libraryClass);
    }


    // Implementations for MemberVisitor.

    @Override
    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        // Change the referenced class.
        programField.referencedClass =
            updateReferencedClass(programField.referencedClass);

        // Change the references of the attributes.
        programField.attributesAccept(programClass, this);
    }


    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        // Change the referenced classes.
        updateReferencedClasses(programMethod.referencedClasses);

        // Change the references of the attributes.
        programMethod.attributesAccept(programClass, this);
    }


    // Implementations for ConstantVisitor.

    @Override
    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    @Override
    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        // Does the string refer to a class, due to a Class.forName construct?
        Clazz referencedClass    = stringConstant.referencedClass;
        Clazz newReferencedClass = updateReferencedClass(referencedClass);
        if (referencedClass != newReferencedClass)
        {
            // Change the referenced class.
            stringConstant.referencedClass = newReferencedClass;

            // Change the referenced class member, if applicable.
            stringConstant.referencedMember =
                updateReferencedMember(stringConstant.referencedMember,
                                       stringConstant.getString(clazz),
                                       null,
                                       newReferencedClass);
        }
    }


    @Override
    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        // Change the referenced classes.
        updateReferencedClasses(invokeDynamicConstant.referencedClasses);
    }


    @Override
    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant refConstant)
    {
        Clazz referencedClass    = refConstant.referencedClass;
        Clazz newReferencedClass = updateReferencedClass(referencedClass);
        if (referencedClass != newReferencedClass)
        {
            logger.debug("TargetClassChanger:");
            logger.debug("  [{}] changing reference from [{}.{}{}]",
                         clazz.getName(),
                         refConstant.referencedClass.getName(),
                         refConstant.referencedMethod.getName(refConstant.referencedClass),
                         refConstant.referencedMethod.getDescriptor(refConstant.referencedClass)
            );

            // Change the referenced class.
            refConstant.referencedClass  = newReferencedClass;

            // Change the referenced class member.
            refConstant.referencedMethod = (Method)updateReferencedMember(refConstant.referencedMethod,
                                                                          refConstant.getName(clazz),
                                                                          refConstant.getType(clazz),
                                                                          newReferencedClass);

            logger.debug("  [{}]                    to   [{}.{}]",
                         clazz.getName(),
                         refConstant.referencedClass,
                         refConstant.referencedMethod
            );
        }
    }


    @Override
    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant refConstant)
    {
        Clazz referencedClass    = refConstant.referencedClass;
        Clazz newReferencedClass = updateReferencedClass(referencedClass);
        if (referencedClass != newReferencedClass)
        {
            logger.debug("TargetClassChanger:");
            logger.debug("  [{}] changing reference from [{}.{}{}]",
                         clazz.getName(),
                         refConstant.referencedClass.getName(),
                         refConstant.referencedField.getName(refConstant.referencedClass),
                         refConstant.referencedField.getDescriptor(refConstant.referencedClass)
            );

            // Change the referenced class.
            refConstant.referencedClass  = newReferencedClass;

            // Change the referenced class member.
            refConstant.referencedField = (Field)updateReferencedMember(refConstant.referencedField,
                                                                        refConstant.getName(clazz),
                                                                        refConstant.getType(clazz),
                                                                        newReferencedClass);

            logger.debug("  [{}]                    to   [{}.{}]",
                         clazz.getName(),
                         refConstant.referencedClass,
                         refConstant.referencedField
            );
        }
    }


    @Override
    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Change the referenced class.
        classConstant.referencedClass =
            updateReferencedClass(classConstant.referencedClass);
    }


    @Override
    public void visitMethodTypeConstant(Clazz clazz, MethodTypeConstant methodTypeConstant)
    {
        updateReferencedClasses(methodTypeConstant.referencedClasses);
    }


    // Implementations for AttributeVisitor.

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // Change the references of the attributes.
        codeAttribute.attributesAccept(clazz, method, this);
    }


    @Override
    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        // Change the references of the local variables.
        localVariableTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    @Override
    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        // Change the references of the local variables.
        localVariableTypeTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    @Override
    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        try
        {
                // Change the referenced classes.
                updateReferencedClasses(signatureAttribute.referencedClasses);
        }
        catch (RuntimeException e)
        {
            logger.error("Unexpected error while adapting signatures for merged classes:");
            logger.error("  Class     = [{}]", clazz.getName());
            logger.error("  Signature = [{}]", signatureAttribute.getSignature(clazz));
            Clazz[] referencedClasses = signatureAttribute.referencedClasses;
            if (referencedClasses != null)
            {
                for (int index = 0; index < referencedClasses.length; index++)
                {
                    Clazz referencedClass = referencedClasses[index];
                    logger.error("  Referenced class #{} = {}", index, referencedClass);
                    if (referencedClass != null)
                    {
                        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(referencedClass);
                        logger.error("                         info        = {}", info);
                        if (info != null)
                        {
                            logger.error("                         target      = {}", info.getTargetClass());
                        }
                    }
                }
            }
            logger.error("  Exception = [{}] ({})", e.getClass().getName(), e.getMessage());
            throw e;
        }
    }


    @Override
    public void visitAnyAnnotationsAttribute(Clazz clazz, AnnotationsAttribute annotationsAttribute)
    {
        // Change the references of the annotations.
        annotationsAttribute.annotationsAccept(clazz, this);
    }


    @Override
    public void visitAnyParameterAnnotationsAttribute(Clazz clazz, Method method, ParameterAnnotationsAttribute parameterAnnotationsAttribute)
    {
        // Change the references of the annotations.
        parameterAnnotationsAttribute.annotationsAccept(clazz, method, this);
    }


    @Override
    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        // Change the references of the annotation.
        annotationDefaultAttribute.defaultValueAccept(clazz, this);
    }


    // Implementations for RecordComponentInfoVisitor.

    public void visitRecordComponentInfo(Clazz clazz, RecordComponentInfo recordComponentInfo)
    {
        // Don't change the referenced field; it's still the original one
        // in this class.

        // Change the references of the attributes.
        recordComponentInfo.attributesAccept(clazz, this);
    }


    // Implementations for LocalVariableInfoVisitor.

    @Override
    public void visitLocalVariableInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableInfo localVariableInfo)
    {
        // Change the referenced class.
        localVariableInfo.referencedClass =
            updateReferencedClass(localVariableInfo.referencedClass);
    }


    // Implementations for LocalVariableTypeInfoVisitor.

    @Override
    public void visitLocalVariableTypeInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeInfo localVariableTypeInfo)
    {
        // Change the referenced classes.
        updateReferencedClasses(localVariableTypeInfo.referencedClasses);
    }


    // Implementations for AnnotationVisitor.

    @Override
    public void visitAnnotation(Clazz clazz, Annotation annotation)
    {
        // Change the referenced classes.
        updateReferencedClasses(annotation.referencedClasses);

        // Change the references of the element values.
        annotation.elementValuesAccept(clazz, this);
    }


    // Implementations for ElementValueVisitor.

    @Override
    public void visitAnyElementValue(Clazz clazz, Annotation annotation, ElementValue elementValue)
    {
        Clazz referencedClass    = elementValue.referencedClass;
        Clazz newReferencedClass = updateReferencedClass(referencedClass);
        if (referencedClass != newReferencedClass)
        {
            // Change the referenced annotation class.
            elementValue.referencedClass  = newReferencedClass;

            // Change the referenced method.
            elementValue.referencedMethod =
                (Method)updateReferencedMember(elementValue.referencedMethod,
                                               elementValue.getMethodName(clazz),
                                               null,
                                               newReferencedClass);
        }
    }


    @Override
    public void visitConstantElementValue(Clazz clazz, Annotation annotation, ConstantElementValue constantElementValue)
    {
        // Change the referenced annotation class and method.
        visitAnyElementValue(clazz, annotation, constantElementValue);
    }


    @Override
    public void visitEnumConstantElementValue(Clazz clazz, Annotation annotation, EnumConstantElementValue enumConstantElementValue)
    {
        // Change the referenced annotation class and method.
        visitAnyElementValue(clazz, annotation, enumConstantElementValue);

        // Change the referenced classes.
        updateReferencedClasses(enumConstantElementValue.referencedClasses);
    }


    @Override
    public void visitClassElementValue(Clazz clazz, Annotation annotation, ClassElementValue classElementValue)
    {
        // Change the referenced annotation class and method.
        visitAnyElementValue(clazz, annotation, classElementValue);

        // Change the referenced classes.
        updateReferencedClasses(classElementValue.referencedClasses);
    }


    @Override
    public void visitAnnotationElementValue(Clazz clazz, Annotation annotation, AnnotationElementValue annotationElementValue)
    {
        // Change the referenced annotation class and method.
        visitAnyElementValue(clazz, annotation, annotationElementValue);

        // Change the references of the annotation.
        annotationElementValue.annotationAccept(clazz, this);
    }


    @Override
    public void visitArrayElementValue(Clazz clazz, Annotation annotation, ArrayElementValue arrayElementValue)
    {
        // Change the referenced annotation class and method.
        visitAnyElementValue(clazz, annotation, arrayElementValue);

        // Change the references of the element values.
        arrayElementValue.elementValuesAccept(clazz, annotation, this);
    }


    // Small utility methods.

     /**
     * Returns whether the given class contains the given interface
     * class in its first given number of interfaces.
     */
    private boolean containsInterfaceClass(Clazz clazz,
                                           int   interfaceCount,
                                           Clazz interfaceClass)
    {
        for (int index = 0; index < interfaceCount; index++)
        {
            if (interfaceClass.equals(clazz.getInterface(index)))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Updates the retargeted classes in the given array of unique classes.
     * Optionally gets a class to avoid in the results.
     */
    private int updateUniqueReferencedClasses(Clazz[] referencedClasses,
                                              int     referencedClassCount,
                                              Clazz   avoidClass)
    {
        int newIndex = 0;
        for (int index = 0; index < referencedClassCount; index++)
        {
            Clazz referencedClass = referencedClasses[index];

            // Isn't the subclass being retargeted?
            Clazz targetClass = ClassMerger.getTargetClass(referencedClasses[index]);
            if (targetClass == null)
            {
                // Keep the original class.
                referencedClasses[newIndex++] = referencedClass;
            }
            // Isn't the targeted class present yet?
            else if (!targetClass.equals(avoidClass) &&
                     ArrayUtil.indexOf(referencedClasses, referencedClassCount, targetClass) < 0)
            {
                // Replace the original class by its targeted class.
                referencedClasses[newIndex++] = targetClass;
            }
        }

        // Clear the remaining elements.
        Arrays.fill(referencedClasses, newIndex, referencedClassCount, null);

        return newIndex;
    }


    /**
     * Updates the retargeted classes in the given array of classes.
     */
    private void updateReferencedClasses(Clazz[] referencedClasses)
    {
        if (referencedClasses == null)
        {
            return;
        }

        for (int index = 0; index < referencedClasses.length; index++)
        {
            referencedClasses[index] =
                updateReferencedClass(referencedClasses[index]);
        }
    }


    /**
     * Returns the retargeted class of the given class.
     */
    private Clazz updateReferencedClass(Clazz referencedClass)
    {
        if (referencedClass == null)
        {
            return null;
        }

        Clazz targetClazz = ClassMerger.getTargetClass(referencedClass);
        return targetClazz != null ?
            targetClazz :
            referencedClass;
    }


    /**
     * Returns the retargeted class member of the given class member.
     */
    private Member updateReferencedMember(Member referencedMember,
                                          String name,
                                          String type,
                                          Clazz  newReferencedClass)
    {
        if (referencedMember == null)
        {
            return null;
        }

        return referencedMember instanceof Field ?
            newReferencedClass.findField(name, type) :
            newReferencedClass.findMethod(name, type);
    }


    /**
     * Explicitly adds a new class constant for the given class in the given
     * program class.
     */
    private int addNewClassConstant(ProgramClass programClass,
                                    String       className,
                                    Clazz        referencedClass)
    {
        ConstantPoolEditor constantPoolEditor =
            new ConstantPoolEditor(programClass);

        int nameIndex =
            constantPoolEditor.addUtf8Constant(className);

        int classConstantIndex =
            constantPoolEditor.addConstant(new ClassConstant(nameIndex,
                                                             referencedClass));
        return classConstantIndex;
    }
}
