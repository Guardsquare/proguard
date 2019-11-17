/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
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
package proguard.classfile.editor;

import kotlinx.metadata.*;
import kotlinx.metadata.jvm.*;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.flags.KotlinPropertyAccessorFlags;
import proguard.classfile.kotlin.visitors.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;

import java.util.*;

import static proguard.classfile.ClassConstants.ATTR_RuntimeInvisibleAnnotations;
import static proguard.classfile.kotlin.KotlinConstants.TYPE_KOTLIN_JVM_JVMNAME;

/**
 * This ClassVisitor fixes references of constant pool entries, fields,
 * methods, attributes and kotlin metadata to classes whose names have
 * changed. Descriptors of member references are not updated yet.
 *
 * @see MemberReferenceFixer
 * @author Eric Lafortune
 */
public class ClassReferenceFixer
extends      SimplifiedVisitor
implements   ClassVisitor,

             // Implementation interfaces.
             ConstantVisitor,
             MemberVisitor,
             AttributeVisitor,
             InnerClassesInfoVisitor,
             LocalVariableInfoVisitor,
             LocalVariableTypeInfoVisitor,
             AnnotationVisitor,
             ElementValueVisitor
{
    private final boolean ensureUniqueMemberNames;

    private final KotlinReferenceFixer kotlinReferenceFixer = new KotlinReferenceFixer();

    /**
     * Creates a new ClassReferenceFixer.
     * @param ensureUniqueMemberNames specifies whether class members whose
     *                                descriptor changes should get new, unique
     *                                names, in order to avoid naming conflicts
     *                                with similar methods.
     */
    public ClassReferenceFixer(boolean ensureUniqueMemberNames)
    {
        this.ensureUniqueMemberNames = ensureUniqueMemberNames;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        // Fix the constant pool.
        programClass.constantPoolEntriesAccept(this);

        // Fix the class members.
        programClass.fieldsAccept(this);
        programClass.methodsAccept(this);

        // Fix the attributes.
        programClass.attributesAccept(this);

        // Fix the Kotlin Metadata.
        programClass.kotlinMetadataAccept(kotlinReferenceFixer);
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Fix class members.
        libraryClass.fieldsAccept(this);
        libraryClass.methodsAccept(this);
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        // Has the descriptor changed?
        String descriptor    = programField.getDescriptor(programClass);
        String newDescriptor = newDescriptor(descriptor,
                                             programField.referencedClass);

        visitProgramMember(programClass, programField, descriptor, newDescriptor);
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        String descriptor    = programMethod.getDescriptor(programClass);
        String newDescriptor = newDescriptor(descriptor,
                                             programMethod.referencedClasses);

        visitProgramMember(programClass, programMethod, descriptor, newDescriptor);
    }


    private void visitProgramMember(ProgramClass programClass,
                                    ProgramMember programMember,
                                    String descriptor,
                                    String newDescriptor)
    {
        // Has the descriptor changed?
        if (!descriptor.equals(newDescriptor))
        {
            ConstantPoolEditor constantPoolEditor =
                new ConstantPoolEditor(programClass);

            // Update the descriptor.
            programMember.u2descriptorIndex =
                constantPoolEditor.addUtf8Constant(newDescriptor);

            // Update the name, if requested.
            if (ensureUniqueMemberNames)
            {
                String name    = programMember.getName(programClass);
                String newName = newUniqueMemberName(name, descriptor);
                programMember.u2nameIndex =
                    constantPoolEditor.addUtf8Constant(newName);
            }
        }

        // Fix the attributes.
        programMember.attributesAccept(programClass, this);
    }


    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        // Has the descriptor changed?
        String descriptor    = libraryField.getDescriptor(libraryClass);
        String newDescriptor = newDescriptor(descriptor,
                                             libraryField.referencedClass);

        // Update the descriptor.
        libraryField.descriptor = newDescriptor;
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        // Has the descriptor changed?
        String descriptor    = libraryMethod.getDescriptor(libraryClass);
        String newDescriptor = newDescriptor(descriptor,
                                             libraryMethod.referencedClasses);

        if (!descriptor.equals(newDescriptor))
        {
            // Update the descriptor.
            libraryMethod.descriptor = newDescriptor;
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        // Does the string refer to a class, due to a Class.forName construct?
        Clazz  referencedClass  = stringConstant.referencedClass;
        Member referencedMember = stringConstant.referencedMember;
        if (referencedClass  != null &&
            referencedMember == null)
        {
            // Reconstruct the new class name.
            String externalClassName    = stringConstant.getString(clazz);
            String internalClassName    = ClassUtil.internalClassName(externalClassName);
            String newInternalClassName = newClassName(internalClassName,
                                                       referencedClass);

            // Update the String entry if required.
            if (!newInternalClassName.equals(internalClassName))
            {
                // Only convert to an external class name if the original was
                // an external class name too.
                String newExternalClassName =
                    externalClassName.indexOf(JavaConstants.PACKAGE_SEPARATOR) >= 0 ?
                        ClassUtil.externalClassName(newInternalClassName) :
                        newInternalClassName;

                // Refer to a new Utf8 entry.
                stringConstant.u2stringIndex =
                    new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newExternalClassName);
            }
        }
    }


    public void visitDynamicConstant(Clazz clazz, DynamicConstant dynamicConstant)
    {
        // Has the descriptor changed?
        String descriptor    = dynamicConstant.getType(clazz);
        String newDescriptor = newDescriptor(descriptor,
                                             dynamicConstant.referencedClasses);

        if (!descriptor.equals(newDescriptor))
        {
            String name = dynamicConstant.getName(clazz);

            // Refer to a new NameAndType entry.
            dynamicConstant.u2nameAndTypeIndex =
                new ConstantPoolEditor((ProgramClass)clazz).addNameAndTypeConstant(name, newDescriptor);
        }
    }


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        // Has the descriptor changed?
        String descriptor    = invokeDynamicConstant.getType(clazz);
        String newDescriptor = newDescriptor(descriptor,
                                             invokeDynamicConstant.referencedClasses);

        if (!descriptor.equals(newDescriptor))
        {
            String name = invokeDynamicConstant.getName(clazz);

            // Refer to a new NameAndType entry.
            invokeDynamicConstant.u2nameAndTypeIndex =
                new ConstantPoolEditor((ProgramClass)clazz).addNameAndTypeConstant(name, newDescriptor);
        }
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Do we know the referenced class?
        Clazz referencedClass = classConstant.referencedClass;
        if (referencedClass != null)
        {
            // Has the class name changed?
            String className    = classConstant.getName(clazz);
            String newClassName = newClassName(className, referencedClass);
            if (!className.equals(newClassName))
            {
                // Refer to a new Utf8 entry.
                classConstant.u2nameIndex =
                    new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newClassName);
            }
        }
    }


    public void visitMethodTypeConstant(Clazz clazz, MethodTypeConstant methodTypeConstant)
    {
        // Has the descriptor changed?
        String descriptor    = methodTypeConstant.getType(clazz);
        String newDescriptor = newDescriptor(descriptor,
                                             methodTypeConstant.referencedClasses);

        if (!descriptor.equals(newDescriptor))
        {
            // Update the descriptor.
            methodTypeConstant.u2descriptorIndex =
                new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newDescriptor);
        }
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute)
    {
        // Fix the inner class names.
        innerClassesAttribute.innerClassEntriesAccept(clazz, this);
    }


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // Fix the attributes.
        codeAttribute.attributesAccept(clazz, method, this);
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        // Fix the types of the local variables.
        localVariableTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        // Fix the signatures of the local variables.
        localVariableTypeTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        // Has the signature changed?
        String signature    = signatureAttribute.getSignature(clazz);
        String newSignature = newDescriptor(signature,
                                            signatureAttribute.referencedClasses);

        if (!signature.equals(newSignature))
        {
            // Update the signature.
            signatureAttribute.u2signatureIndex =
                new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newSignature);
        }
    }


    public void visitAnyAnnotationsAttribute(Clazz clazz, AnnotationsAttribute annotationsAttribute)
    {
        // Fix the annotations.
        annotationsAttribute.annotationsAccept(clazz, this);
    }


    public void visitAnyParameterAnnotationsAttribute(Clazz clazz, Method method, ParameterAnnotationsAttribute parameterAnnotationsAttribute)
    {
        // Fix the annotations.
        parameterAnnotationsAttribute.annotationsAccept(clazz, method, this);
    }


    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        // Fix the annotation.
        annotationDefaultAttribute.defaultValueAccept(clazz, this);
    }


    // Implementations for InnerClassesInfoVisitor.

    public void visitInnerClassesInfo(Clazz clazz, InnerClassesInfo innerClassesInfo)
    {
        // Fix the inner class name.
        int innerClassIndex = innerClassesInfo.u2innerClassIndex;
        int innerNameIndex  = innerClassesInfo.u2innerNameIndex;
        if (innerClassIndex != 0 &&
            innerNameIndex  != 0)
        {
            String newInnerName = clazz.getClassName(innerClassIndex);
            int index = newInnerName.lastIndexOf(ClassConstants.INNER_CLASS_SEPARATOR);
            if (index >= 0)
            {
                innerClassesInfo.u2innerNameIndex =
                    new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newInnerName.substring(index + 1));
            }
        }
    }


    // Implementations for LocalVariableInfoVisitor.

    public void visitLocalVariableInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableInfo localVariableInfo)
    {
        // Has the descriptor changed?
        String descriptor    = localVariableInfo.getDescriptor(clazz);
        String newDescriptor = newDescriptor(descriptor,
                                             localVariableInfo.referencedClass);

        if (!descriptor.equals(newDescriptor))
        {
            // Refer to a new Utf8 entry.
            localVariableInfo.u2descriptorIndex =
                new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newDescriptor);
        }
    }

    // Implementations for LocalVariableTypeInfoVisitor.

    public void visitLocalVariableTypeInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeInfo localVariableTypeInfo)
    {
        // Has the signature changed?
        String signature    = localVariableTypeInfo.getSignature(clazz);
        String newSignature = newDescriptor(signature,
                                            localVariableTypeInfo.referencedClasses);

        if (!signature.equals(newSignature))
        {
            // Update the signature.
            localVariableTypeInfo.u2signatureIndex =
                new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newSignature);
        }
    }

    // Implementations for AnnotationVisitor.

    public void visitAnnotation(Clazz clazz, Annotation annotation)
    {
        // Has the type changed?
        String typeName    = annotation.getType(clazz);
        String newTypeName = newDescriptor(typeName,
                                           annotation.referencedClasses);

        if (!typeName.equals(newTypeName))
        {
            // Update the type.
            annotation.u2typeIndex =
                new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newTypeName);
        }

        // Fix the element values.
        annotation.elementValuesAccept(clazz, this);
    }


    // Implementations for ElementValueVisitor.

    public void visitConstantElementValue(Clazz clazz, Annotation annotation, ConstantElementValue constantElementValue)
    {
    }


    public void visitEnumConstantElementValue(Clazz clazz, Annotation annotation, EnumConstantElementValue enumConstantElementValue)
    {
        // Has the type name changed?
        String typeName    = enumConstantElementValue.getTypeName(clazz);
        String newTypeName = newDescriptor(typeName,
                                           enumConstantElementValue.referencedClasses);

        if (!typeName.equals(newTypeName))
        {
            // Update the type name.
            enumConstantElementValue.u2typeNameIndex =
                new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newTypeName);
        }
    }


    public void visitClassElementValue(Clazz clazz, Annotation annotation, ClassElementValue classElementValue)
    {
        // Has the class info changed?
        String className    = classElementValue.getClassName(clazz);
        String newClassName = newDescriptor(className,
                                            classElementValue.referencedClasses);

        if (!className.equals(newClassName))
        {
            // Update the class info.
            classElementValue.u2classInfoIndex =
                new ConstantPoolEditor((ProgramClass)clazz).addUtf8Constant(newClassName);
        }
    }


    public void visitAnnotationElementValue(Clazz clazz, Annotation annotation, AnnotationElementValue annotationElementValue)
    {
        // Fix the annotation.
        annotationElementValue.annotationAccept(clazz, this);
    }


    public void visitArrayElementValue(Clazz clazz, Annotation annotation, ArrayElementValue arrayElementValue)
    {
        // Fix the element values.
        arrayElementValue.elementValuesAccept(clazz, annotation, this);
    }


    private static class KotlinReferenceFixer
    implements           KotlinMetadataVisitor,
                         KotlinPropertyVisitor,
                         KotlinFunctionVisitor,
                         KotlinConstructorVisitor,
                         KotlinTypeVisitor,
                         KotlinTypeAliasVisitor,
                         KotlinValueParameterVisitor,
                         KotlinTypeParameterVisitor
    {
        // Implementations for KotlinMetadataVisitor.
        @Override
        public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

        @Override
        public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                            KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
        {
            kotlinDeclarationContainerMetadata.ownerClassName = newClassName(kotlinDeclarationContainerMetadata.ownerClassName,
                                                                             kotlinDeclarationContainerMetadata.ownerReferencedClass);
            kotlinDeclarationContainerMetadata.propertiesAccept(clazz, this);
            kotlinDeclarationContainerMetadata.delegatedPropertiesAccept(clazz, this);
            kotlinDeclarationContainerMetadata.functionsAccept(clazz, this);
            kotlinDeclarationContainerMetadata.typeAliasesAccept(clazz, this);
        }


        @Override
        public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
        {
            kotlinClassKindMetadata.className = newClassName(kotlinClassKindMetadata.className,
                                                             kotlinClassKindMetadata.referencedClass);

            if (kotlinClassKindMetadata.anonymousObjectOriginName != null)
            {
                kotlinClassKindMetadata.anonymousObjectOriginName =
                    newClassName(kotlinClassKindMetadata.anonymousObjectOriginName,
                                 kotlinClassKindMetadata.anonymousObjectOriginClass);
            }

            if (kotlinClassKindMetadata.companionObjectName != null)
            {
                kotlinClassKindMetadata.companionObjectName = kotlinClassKindMetadata.referencedCompanionField.getName(clazz);
            }

            for (int k = 0; k < kotlinClassKindMetadata.enumEntryNames.size(); k++)
            {
                kotlinClassKindMetadata.enumEntryNames.set(k,
                    kotlinClassKindMetadata.referencedEnumEntries.get(k).getName(clazz));
            }

            for (int k = 0; k < kotlinClassKindMetadata.nestedClassNames.size(); k++)
            {
                kotlinClassKindMetadata.nestedClassNames.set(k,
                     newInnerClassName(clazz.getName(),
                                       kotlinClassKindMetadata.nestedClassNames       .get(k),
                                       kotlinClassKindMetadata.referencedNestedClasses.get(k)));
            }

            for (int k = 0; k < kotlinClassKindMetadata.sealedSubclassNames.size(); k++)
            {
                kotlinClassKindMetadata.sealedSubclassNames.set(k,
                     newClassName(kotlinClassKindMetadata.sealedSubclassNames       .get(k),
                                  kotlinClassKindMetadata.referencedSealedSubClasses.get(k)));
            }

            kotlinClassKindMetadata.typeParametersAccept(clazz, this);
            kotlinClassKindMetadata.superTypesAccept(    clazz, this);
            kotlinClassKindMetadata.constructorsAccept(  clazz, this);

            visitKotlinDeclarationContainerMetadata(clazz, kotlinClassKindMetadata);
        }

        @Override
        public void visitKotlinFileFacadeMetadata(Clazz clazz, KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata)
        {
            visitKotlinDeclarationContainerMetadata(clazz, kotlinFileFacadeKindMetadata);
        }

        @Override
        public void visitKotlinSyntheticClassMetadata(Clazz clazz, KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
        {
            kotlinSyntheticClassKindMetadata.className = newClassName(kotlinSyntheticClassKindMetadata.className,
                                                                      kotlinSyntheticClassKindMetadata.referencedClass);
            kotlinSyntheticClassKindMetadata.functionsAccept(clazz, this);
        }

        @Override
        public void visitKotlinMultiFileFacadeMetadata(Clazz clazz, KotlinMultiFileFacadeKindMetadata kotlinMultiFileFacadeKindMetadata)
        {
            for (int k = 0; k < kotlinMultiFileFacadeKindMetadata.partClassNames.size(); k++)
            {
                kotlinMultiFileFacadeKindMetadata.partClassNames.set(k,
                    newClassName(kotlinMultiFileFacadeKindMetadata.partClassNames.get(k),
                                 kotlinMultiFileFacadeKindMetadata.referencedPartClasses.get(k)));
            }
        }

        @Override
        public void visitKotlinMultiFilePartMetadata(Clazz clazz, KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata)
        {
            kotlinMultiFilePartKindMetadata.xs = newClassName(kotlinMultiFilePartKindMetadata.xs,
                                                              kotlinMultiFilePartKindMetadata.referencedFacade);

            visitKotlinDeclarationContainerMetadata(clazz, kotlinMultiFilePartKindMetadata);
        }


        // Implementations for KotlinPropertyVisitor.
        @Override
        public void visitAnyProperty(Clazz                              clazz,
                                     KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                     KotlinPropertyMetadata             kotlinPropertyMetadata)
        {
            ProgramClass programClass = (ProgramClass)clazz;
            if (kotlinPropertyMetadata.backingFieldSignature != null)
            {
                Clazz backingFieldClass = kotlinPropertyMetadata.referencedBackingFieldClass;
                Field backingField      = kotlinPropertyMetadata.referencedBackingField;
                kotlinPropertyMetadata.backingFieldSignature =
                    new JvmFieldSignature(backingField.getName(backingFieldClass),
                                          backingField.getDescriptor(backingFieldClass));
            }

            kotlinPropertyMetadata.getterSignature =
                fixPropertyMethod(programClass,
                                  kotlinPropertyMetadata.referencedGetterMethod,
                                  kotlinPropertyMetadata.getterFlags,
                                  kotlinPropertyMetadata.getterSignature);

            kotlinPropertyMetadata.setterSignature =
                fixPropertyMethod(programClass,
                                  kotlinPropertyMetadata.referencedSetterMethod,
                                  kotlinPropertyMetadata.setterFlags,
                                  kotlinPropertyMetadata.setterSignature);

            kotlinPropertyMetadata.syntheticMethodForAnnotations =
                fixPropertyMethod(kotlinPropertyMetadata.referencedSyntheticMethodClass,
                                  kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations,
                                  null,
                                  kotlinPropertyMetadata.syntheticMethodForAnnotations);

            kotlinPropertyMetadata.typeParametersAccept(  clazz, kotlinDeclarationContainerMetadata, this);
            kotlinPropertyMetadata.receiverTypeAccept(    clazz, kotlinDeclarationContainerMetadata, this);
            kotlinPropertyMetadata.typeAccept(            clazz, kotlinDeclarationContainerMetadata, this);
            kotlinPropertyMetadata.setterParametersAccept(clazz, kotlinDeclarationContainerMetadata, this);

            //TODO change name, etc.
        }


        // Implementations for KotlinFunctionVisitor.
        @Override
        public void visitAnyFunction(Clazz                  clazz,
                                     KotlinMetadata         kotlinMetadata,
                                     KotlinFunctionMetadata kotlinFunctionMetadata)
        {
            if (kotlinFunctionMetadata.lambdaClassOriginName != null)
            {
                //TODO in future, we could provide configurable option for this.
                kotlinFunctionMetadata.lambdaClassOriginName       = null;
                kotlinFunctionMetadata.referencedLambdaClassOrigin = null;
            }

            // Fix the JVM signatures.
            if (kotlinFunctionMetadata.jvmSignature != null)
            {
                Method jvmMethod = kotlinFunctionMetadata.referencedMethod;
                kotlinFunctionMetadata.jvmSignature =
                    new JvmMethodSignature(jvmMethod.getName(kotlinFunctionMetadata.referencedMethodClass),
                                           jvmMethod.getDescriptor(kotlinFunctionMetadata.referencedMethodClass));
            }

            kotlinFunctionMetadata.typeParametersAccept( clazz, kotlinMetadata, this);
            kotlinFunctionMetadata.receiverTypeAccept(   clazz, kotlinMetadata, this);
            kotlinFunctionMetadata.valueParametersAccept(clazz, kotlinMetadata, this);
            kotlinFunctionMetadata.returnTypeAccept(     clazz, kotlinMetadata, this);
            kotlinFunctionMetadata.contractsAccept(      clazz, kotlinMetadata, new AllTypeVisitor(this));

            //TODO change name, name variants, etc.
            kotlinFunctionMetadata.name = kotlinFunctionMetadata.referencedMethod.getName(kotlinFunctionMetadata.referencedMethodClass);
        }


        // Implementations for KotlinConstructorVisitor.
        @Override
        public void visitConstructor(Clazz                     clazz,
                                     KotlinClassKindMetadata   kotlinClassKindMetadata,
                                     KotlinConstructorMetadata kotlinConstructorMetadata)
        {
            if (kotlinConstructorMetadata.jvmSignature != null)
            {
                Method jvmMethod = kotlinConstructorMetadata.referencedMethod;
                kotlinConstructorMetadata.jvmSignature = new JvmMethodSignature(jvmMethod.getName(clazz),
                                                                                jvmMethod.getDescriptor(clazz));
            }

            kotlinConstructorMetadata.valueParametersAccept(clazz, kotlinClassKindMetadata, this);
        }

        //TODO Fix KotlinTypeMetadata.

        // Implementations for KotlinTypeVisitor.
        @Override
        public void visitAnyType(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata)
        {
            fixKotlinAnnotations(kotlinTypeMetadata.annotations);

            if (kotlinTypeMetadata.className != null)
            {
                kotlinTypeMetadata.className = kotlinTypeMetadata.referencedClass.getName();
            }

            // We fix aliasName using KotlinAliasReferenceFixer after ClassReferenceFixer is finished.

            kotlinTypeMetadata.typeArgumentsAccept(clazz, this);
            kotlinTypeMetadata.outerClassAccept(   clazz, this);
            kotlinTypeMetadata.upperBoundsAccept(  clazz, this);
            kotlinTypeMetadata.abbreviationAccept( clazz, this);
        }


        // Implementations for KotlinTypeAliasVisitor.
        @Override
        public void visitTypeAlias(Clazz                              clazz,
                                   KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                   KotlinTypeAliasMetadata            kotlinTypeAliasMetadata)
        {
            fixKotlinAnnotations(kotlinTypeAliasMetadata.annotations);

            kotlinTypeAliasMetadata.underlyingTypeAccept(clazz, kotlinDeclarationContainerMetadata, this);
            kotlinTypeAliasMetadata.expandedTypeAccept(  clazz, kotlinDeclarationContainerMetadata, this);
            kotlinTypeAliasMetadata.typeParametersAccept(clazz, kotlinDeclarationContainerMetadata, this);
        }


        // Implementations for KotlinValueParameterVisitor
        @Override
        public void visitAnyValueParameter(Clazz                        clazz,
                                           KotlinValueParameterMetadata kotlinValueParameterMetadata) {}

        @Override
        public void visitFunctionValParameter(Clazz                        clazz,
                                              KotlinMetadata               kotlinMetadata,
                                              KotlinFunctionMetadata       kotlinFunctionMetadata,
                                              KotlinValueParameterMetadata kotlinValueParameterMetadata)
        {
            kotlinValueParameterMetadata.typeAccept(clazz, kotlinMetadata, kotlinFunctionMetadata, this);
        }

        @Override
        public void visitConstructorValParameter(Clazz                        clazz,
                                                 KotlinClassKindMetadata      kotlinClassKindMetadata,
                                                 KotlinConstructorMetadata    kotlinConstructorMetadata,
                                                 KotlinValueParameterMetadata kotlinValueParameterMetadata)
        {
            kotlinValueParameterMetadata.typeAccept(clazz, kotlinClassKindMetadata, kotlinConstructorMetadata, this);
        }

        @Override
        public void visitPropertyValParameter(Clazz                              clazz,
                                              KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                              KotlinPropertyMetadata             kotlinPropertyMetadata,
                                              KotlinValueParameterMetadata       kotlinValueParameterMetadata)
        {
            kotlinValueParameterMetadata.typeAccept(clazz, kotlinDeclarationContainerMetadata, kotlinPropertyMetadata, this);
        }


        // Implementations for KotlinTypeParameterVisitor
        @Override
        public void visitAnyTypeParameter(Clazz clazz, KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
        {
            fixKotlinAnnotations(kotlinTypeParameterMetadata.annotations);

            kotlinTypeParameterMetadata.upperBoundsAccept(clazz, this);
        }
    }

    // Small utility methods.

    /**
     * Returns the new descriptor of a field after applying the obfuscation,
     * given the old descriptor and the referenced classes.
     */
    private static String newDescriptor(String descriptor,
                                        Clazz  referencedClass)
    {
        // If there is no referenced class, the descriptor won't change.
        if (referencedClass == null)
        {
            return descriptor;
        }

        // Unravel and reconstruct the class element of the descriptor.
        DescriptorClassEnumeration descriptorClassEnumeration =
            new DescriptorClassEnumeration(descriptor);

        StringBuffer newDescriptorBuffer = new StringBuffer(descriptor.length());
        newDescriptorBuffer.append(descriptorClassEnumeration.nextFluff());

        // Only if the descriptor contains a class name (e.g. with an array of
        // primitive types), the descriptor can change.
        if (descriptorClassEnumeration.hasMoreClassNames())
        {
            String className = descriptorClassEnumeration.nextClassName();
            String fluff     = descriptorClassEnumeration.nextFluff();

            String newClassName = newClassName(className,
                                               referencedClass);

            newDescriptorBuffer.append(newClassName);
            newDescriptorBuffer.append(fluff);
        }

        return newDescriptorBuffer.toString();
    }


    /**
     * Returns the new descriptor of a method after applying the obfuscation,
     * given the old descriptor and the referenced classes.
     */
    public static String newDescriptor(String  descriptor,
                                       Clazz[] referencedClasses)
    {
        // If there are no referenced classes, the descriptor won't change.
        if (referencedClasses == null ||
            referencedClasses.length == 0)
        {
            return descriptor;
        }

        // TODO: Remove the try/catch when the code has stabilized.
        // Catch any unexpected exceptions.
        try
        {
            // Unravel and reconstruct the class elements of the descriptor.
            DescriptorClassEnumeration descriptorClassEnumeration =
                new DescriptorClassEnumeration(descriptor);

            StringBuffer newDescriptorBuffer = new StringBuffer(descriptor.length());
            newDescriptorBuffer.append(descriptorClassEnumeration.nextFluff());

            int index = 0;
            while (descriptorClassEnumeration.hasMoreClassNames())
            {
                String  className        = descriptorClassEnumeration.nextClassName();
                boolean isInnerClassName = descriptorClassEnumeration.isInnerClassName();
                String  fluff            = descriptorClassEnumeration.nextFluff();

                String newClassName = newClassName(className,
                                                   referencedClasses[index++]);

                // Strip the outer class name again, if it's an inner class.
                if (isInnerClassName)
                {
                    newClassName =
                        newClassName.substring(newClassName.lastIndexOf(ClassConstants.INNER_CLASS_SEPARATOR)+1);
                }

                newDescriptorBuffer.append(newClassName);
                newDescriptorBuffer.append(fluff);
            }

            return newDescriptorBuffer.toString();
        }
        catch (RuntimeException e)
        {
            System.err.println("Unexpected error while updating descriptor:");
            System.err.println("  Descriptor = ["+descriptor+"]");
            System.err.println("  Referenced classes: "+referencedClasses.length);
            for (int index = 0; index < referencedClasses.length; index++)
            {
                Clazz referencedClass = referencedClasses[index];
                if (referencedClass != null)
                {
                    System.err.println("    #"+index+": ["+referencedClass.getName()+"]");
                }
            }

            throw e;
        }
    }


    /**
     * Returns a unique class member name, based on the given name and descriptor.
     */
    private String newUniqueMemberName(String name, String descriptor)
    {
        return name.equals(ClassConstants.METHOD_NAME_INIT) ?
            ClassConstants.METHOD_NAME_INIT :
            name + ClassConstants.SPECIAL_MEMBER_SEPARATOR + Long.toHexString(Math.abs((descriptor).hashCode()));
    }


    /**
     * Returns the new class name based on the given class name and the new
     * name of the given referenced class. Class names of array types
     * are handled properly.
     */
    private static String newClassName(String className,
                                       Clazz  referencedClass)
    {
        // If there is no referenced class, the class name won't change.
        if (referencedClass == null)
        {
            return className;
        }

        // Reconstruct the class name.
        String newClassName = referencedClass.getName();

        // Is it an array type?
        if (className.charAt(0) == ClassConstants.TYPE_ARRAY)
        {
            // Add the array prefixes and suffix "[L...;".
            newClassName =
                 className.substring(0, className.indexOf(ClassConstants.TYPE_CLASS_START)+1) +
                 newClassName +
                 ClassConstants.TYPE_CLASS_END;
        }

        return newClassName;
    }


    private static String newInnerClassName(String enclosingClassName,
                                            String shortInnerClassName,
                                            Clazz referencedClass)
    {
        String newFulllName = newClassName(enclosingClassName + "$" + shortInnerClassName,
                                           referencedClass);

        return newFulllName.substring(newFulllName.indexOf('$') + 1);
    }


    // Small utility helper methods for KotlinReferenceFixer.

    private static void fixKotlinAnnotations(List<KotlinMetadataAnnotation> annotations)
    {
        for (KotlinMetadataAnnotation annotation : annotations)
        {
            Map<String, KmAnnotationArgument<?>> newKeys = new HashMap<>();

            for (Map.Entry<String, KmAnnotationArgument<?>> entry : annotation.kmAnnotation.getArguments().entrySet())
            {
                String originalName = entry.getKey();
                Method refMethod    = annotation.referencedArgumentMethods.get(originalName);
                String newName      = refMethod.getName(annotation.referencedAnnotationClass);
                newKeys.put(newName, entry.getValue());
                // Update the new name in the referencedKeys as well.
                annotation.referencedArgumentMethods.remove(originalName);
                annotation.referencedArgumentMethods.put(newName, refMethod);
            }

            annotation.kmAnnotation = new KmAnnotation(annotation.referencedAnnotationClass.getName(), newKeys);
        }
    }

    private static JvmMethodSignature fixPropertyMethod(Clazz                       referencedMethodClass,
                                                        Method                      referencedMethod,
                                                        KotlinPropertyAccessorFlags flags,
                                                        JvmMethodSignature          oldSignature)
    {
        if (oldSignature == null)
        {
            return null;
        }

        JvmMethodSignature newSignature = new JvmMethodSignature(referencedMethod.getName(referencedMethodClass),
                                                                 referencedMethod.getDescriptor(referencedMethodClass));

        if (!oldSignature.equals(newSignature) &&
            flags != null)
        {
            addJvmNameAnnotation((ProgramClass) referencedMethodClass,
                                 (ProgramMethod)referencedMethod);
            flags.common.hasAnnotations = true;
        }

        return newSignature;
    }


    private static void addJvmNameAnnotation(ProgramClass programClass, ProgramMethod programMethod)
    {
        ConstantPoolEditor editor = new ConstantPoolEditor(programClass);

        Annotation jvmName = new Annotation(editor.addUtf8Constant(TYPE_KOTLIN_JVM_JVMNAME),
                                            1,
                                            new ElementValue[]
                                            {
                                                new ConstantElementValue('s',
                                                                         editor.addUtf8Constant("name"),
                                                                         editor.addUtf8Constant(programMethod.getName(programClass)))
                                            });

        RuntimeInvisibleAnnotationsAttribute attribute =
            new RuntimeInvisibleAnnotationsAttribute(editor.addUtf8Constant(ATTR_RuntimeInvisibleAnnotations),
                                                     1,
                                                     new Annotation[] { jvmName });

        new AttributesEditor(programClass, programMethod, false).addAttribute(attribute);
    }
}
