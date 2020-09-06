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
package proguard.optimize.peephole;

import java.util.Arrays;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.visitor.*;
import proguard.util.ArrayUtil;

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
    /*
    private static final boolean DEBUG = false;
    /*/
    private        final boolean DEBUG = System.getProperty("tcc") != null;;
    //*/


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

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        // Change the referenced class.
        programField.referencedClass =
            updateReferencedClass(programField.referencedClass);

        // Change the references of the attributes.
        programField.attributesAccept(programClass, this);
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        // Change the referenced classes.
        updateReferencedClasses(programMethod.referencedClasses);

        // Change the references of the attributes.
        programMethod.attributesAccept(programClass, this);
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


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


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        // Change the referenced classes.
        updateReferencedClasses(invokeDynamicConstant.referencedClasses);
    }


    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        Clazz referencedClass    = fieldrefConstant.referencedClass;
        Clazz newReferencedClass = updateReferencedClass(referencedClass);
        if (referencedClass != newReferencedClass)
        {
            if (DEBUG)
            {
                System.out.println("TargetClassChanger:");
                System.out.println("  ["+clazz.getName()+"] changing reference from ["+fieldrefConstant.referencedClass+"."+fieldrefConstant.referencedField.getName(fieldrefConstant.referencedClass)+fieldrefConstant.referencedField.getDescriptor(fieldrefConstant.referencedClass)+"]");
            }

            // Change the referenced class.
            fieldrefConstant.referencedClass  = newReferencedClass;

            // Change the referenced class member.
            fieldrefConstant.referencedField =
                updateReferencedField(fieldrefConstant.referencedField,
                                      fieldrefConstant.getName(clazz),
                                      fieldrefConstant.getType(clazz),
                                      newReferencedClass);

            if (DEBUG)
            {
                System.out.println("  ["+clazz.getName()+"]                    to   ["+fieldrefConstant.referencedClass+"."+fieldrefConstant.referencedField.getName(fieldrefConstant.referencedClass)+fieldrefConstant.referencedField.getDescriptor(fieldrefConstant.referencedClass)+"]");
            }
        }
    }


    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        Clazz referencedClass    = anyMethodrefConstant.referencedClass;
        Clazz newReferencedClass = updateReferencedClass(referencedClass);
        if (referencedClass != newReferencedClass)
        {
            if (DEBUG)
            {
                System.out.println("TargetClassChanger:");
                System.out.println("  ["+clazz.getName()+"] changing reference from ["+anyMethodrefConstant.referencedClass+"."+anyMethodrefConstant.referencedMethod.getName(anyMethodrefConstant.referencedClass)+anyMethodrefConstant.referencedMethod.getDescriptor(anyMethodrefConstant.referencedClass)+"]");
            }

            // Change the referenced class.
            anyMethodrefConstant.referencedClass  = newReferencedClass;

            // Change the referenced class member.
            anyMethodrefConstant.referencedMethod =
                updateReferencedMethod(anyMethodrefConstant.referencedMethod,
                                       anyMethodrefConstant.getName(clazz),
                                       anyMethodrefConstant.getType(clazz),
                                       newReferencedClass);

            if (DEBUG)
            {
                System.out.println("  ["+clazz.getName()+"]                    to   ["+anyMethodrefConstant.referencedClass+"."+anyMethodrefConstant.referencedMethod.getName(anyMethodrefConstant.referencedClass)+anyMethodrefConstant.referencedMethod.getDescriptor(anyMethodrefConstant.referencedClass)+"]");
            }
        }
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Change the referenced class.
        classConstant.referencedClass =
            updateReferencedClass(classConstant.referencedClass);
    }


    public void visitMethodTypeConstant(Clazz clazz, MethodTypeConstant methodTypeConstant)
    {
        updateReferencedClasses(methodTypeConstant.referencedClasses);
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitRecordAttribute(Clazz clazz, RecordAttribute recordAttribute)
    {
        // Fix the record component attributes.
        recordAttribute.componentsAccept(clazz, this);
    }


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // Change the references of the attributes.
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


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        // Change the referenced classes.
        updateReferencedClasses(signatureAttribute.referencedClasses);
    }


    public void visitAnyAnnotationsAttribute(Clazz clazz, AnnotationsAttribute annotationsAttribute)
    {
        // Change the references of the annotations.
        annotationsAttribute.annotationsAccept(clazz, this);
    }


    public void visitAnyParameterAnnotationsAttribute(Clazz clazz, Method method, ParameterAnnotationsAttribute parameterAnnotationsAttribute)
    {
        // Change the references of the annotations.
        parameterAnnotationsAttribute.annotationsAccept(clazz, method, this);
    }


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

    public void visitLocalVariableInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableInfo localVariableInfo)
    {
        // Change the referenced class.
        localVariableInfo.referencedClass =
            updateReferencedClass(localVariableInfo.referencedClass);
    }


    // Implementations for LocalVariableTypeInfoVisitor.

    public void visitLocalVariableTypeInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeInfo localVariableTypeInfo)
    {
        // Change the referenced classes.
        updateReferencedClasses(localVariableTypeInfo.referencedClasses);
    }


    // Implementations for AnnotationVisitor.

    public void visitAnnotation(Clazz clazz, Annotation annotation)
    {
        // Change the referenced classes.
        updateReferencedClasses(annotation.referencedClasses);

        // Change the references of the element values.
        annotation.elementValuesAccept(clazz, this);
    }


    // Implementations for ElementValueVisitor.

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
                updateReferencedMethod(elementValue.referencedMethod,
                                       elementValue.getMethodName(clazz),
                                       null,
                                       newReferencedClass);
        }
    }


    public void visitConstantElementValue(Clazz clazz, Annotation annotation, ConstantElementValue constantElementValue)
    {
        // Change the referenced annotation class and method.
        visitAnyElementValue(clazz, annotation, constantElementValue);
    }


    public void visitEnumConstantElementValue(Clazz clazz, Annotation annotation, EnumConstantElementValue enumConstantElementValue)
    {
        // Change the referenced annotation class and method.
        visitAnyElementValue(clazz, annotation, enumConstantElementValue);

        // Change the referenced classes.
        updateReferencedClasses(enumConstantElementValue.referencedClasses);
    }


    public void visitClassElementValue(Clazz clazz, Annotation annotation, ClassElementValue classElementValue)
    {
        // Change the referenced annotation class and method.
        visitAnyElementValue(clazz, annotation, classElementValue);

        // Change the referenced classes.
        updateReferencedClasses(classElementValue.referencedClasses);
    }


    public void visitAnnotationElementValue(Clazz clazz, Annotation annotation, AnnotationElementValue annotationElementValue)
    {
        // Change the referenced annotation class and method.
        visitAnyElementValue(clazz, annotation, annotationElementValue);

        // Change the references of the annotation.
        annotationElementValue.annotationAccept(clazz, this);
    }


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
        return referencedMember instanceof  Field ?
            updateReferencedField((Field)referencedMember,
                                  name,
                                  type,
                                  newReferencedClass) :
            updateReferencedMethod((Method)referencedMember,
                                   name,
                                   type,
                                   newReferencedClass);
    }



    /**
     * Returns the retargeted field of the given field.
     */
    private Field updateReferencedField(Field  referencedField,
                                        String name,
                                        String type,
                                        Clazz  newReferencedClass)
    {
        if (referencedField == null)
        {
            return null;
        }

        return newReferencedClass.findField(name, type);
    }


    /**
     * Returns the retargeted method of the given method.
     */
    private Method updateReferencedMethod(Method referencedMethod,
                                          String name,
                                          String type,
                                          Clazz  newReferencedClass)
    {
        if (referencedMethod == null)
        {
            return null;
        }

        return newReferencedClass.findMethod(name, type);
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
