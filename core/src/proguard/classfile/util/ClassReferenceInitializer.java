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
package proguard.classfile.util;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitors.*;
import proguard.classfile.visitor.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This ClassVisitor initializes the references of all classes that
 * it visits.
 * <p>
 * All class constant pool entries get direct references to the corresponding
 * classes. These references make it more convenient to travel up and across
 * the class hierarchy.
 * <p>
 * All field and method reference constant pool entries get direct references
 * to the corresponding classes, fields, and methods.
 * <p>
 * All name and type constant pool entries get a list of direct references to
 * the classes listed in the type.
 * <p>
 * All Kotlin metadata elements get references to their corresponding Java
 * implementation elements.
 * <p>
 * This visitor optionally prints warnings if some items can't be found.
 * <p>
 * The class hierarchy must be initialized before using this visitor.
 *
 * @author Eric Lafortune
 */
public class ClassReferenceInitializer
extends      SimplifiedVisitor
implements   ClassVisitor,

             // Implementation interfaces.
             MemberVisitor,
             ConstantVisitor,
             AttributeVisitor,
             LocalVariableInfoVisitor,
             LocalVariableTypeInfoVisitor,
             AnnotationVisitor,
             ElementValueVisitor
{
    private final ClassPool      programClassPool;
    private final ClassPool      libraryClassPool;
    private final boolean        checkAccessRules;
    private       WarningPrinter missingClassWarningPrinter;
    private final WarningPrinter missingProgramMemberWarningPrinter;
    private final WarningPrinter missingLibraryMemberWarningPrinter;
    private final WarningPrinter dependencyWarningPrinter;

    private final MemberFinder memberFinder       = new MemberFinder();
    private final MemberFinder strictMemberFinder = new MemberFinder(false);

    private final KotlinReferenceInitializer kotlinReferenceInitializer = new KotlinReferenceInitializer();


    /**
     * Creates a new ClassReferenceInitializer that initializes the references
     * of all visited class files.
     */
    public ClassReferenceInitializer(ClassPool programClassPool,
                                     ClassPool libraryClassPool)
    {
        this(programClassPool,
             libraryClassPool,
             true);
    }


    /**
     * Creates a new ClassReferenceInitializer that initializes the references
     * of all visited class files.
     */
    public ClassReferenceInitializer(ClassPool programClassPool,
                                     ClassPool libraryClassPool,
                                     boolean   checkAccessRules)
    {
        this(programClassPool,
             libraryClassPool,
             checkAccessRules,
             null,
             null,
             null,
             null);
    }


    /**
     * Creates a new ClassReferenceInitializer that initializes the references
     * of all visited class files, optionally printing warnings if some classes
     * or class members can't be found or if they are in the program class pool.
     */
    public ClassReferenceInitializer(ClassPool      programClassPool,
                                     ClassPool      libraryClassPool,
                                     WarningPrinter missingClassWarningPrinter,
                                     WarningPrinter missingProgramMemberWarningPrinter,
                                     WarningPrinter missingLibraryMemberWarningPrinter,
                                     WarningPrinter dependencyWarningPrinter)
    {
        this(programClassPool,
             libraryClassPool,
             true,
             missingClassWarningPrinter,
             missingProgramMemberWarningPrinter,
             missingLibraryMemberWarningPrinter,
             dependencyWarningPrinter);
    }


    /**
     * Creates a new ClassReferenceInitializer that initializes the references
     * of all visited class files, optionally printing warnings if some classes
     * or class members can't be found or if they are in the program class pool.
     */
    public ClassReferenceInitializer(ClassPool      programClassPool,
                                     ClassPool      libraryClassPool,
                                     boolean        checkAccessRules,
                                     WarningPrinter missingClassWarningPrinter,
                                     WarningPrinter missingProgramMemberWarningPrinter,
                                     WarningPrinter missingLibraryMemberWarningPrinter,
                                     WarningPrinter dependencyWarningPrinter)
    {
        this.programClassPool                   = programClassPool;
        this.libraryClassPool                   = libraryClassPool;
        this.checkAccessRules                   = checkAccessRules;
        this.missingClassWarningPrinter         = missingClassWarningPrinter;
        this.missingProgramMemberWarningPrinter = missingProgramMemberWarningPrinter;
        this.missingLibraryMemberWarningPrinter = missingLibraryMemberWarningPrinter;
        this.dependencyWarningPrinter           = dependencyWarningPrinter;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        // Initialize the constant pool entries.
        programClass.constantPoolEntriesAccept(this);

        // Initialize all fields and methods.
        programClass.fieldsAccept(this);
        programClass.methodsAccept(this);

        // Initialize the attributes.
        programClass.attributesAccept(this);

        // Initialize the Kotlin metadata.
        programClass.kotlinMetadataAccept(kotlinReferenceInitializer);
    }


    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Initialize all fields and methods.
        libraryClass.fieldsAccept(this);
        libraryClass.methodsAccept(this);

        // Initialize the Kotlin metadata.
        libraryClass.kotlinMetadataAccept(kotlinReferenceInitializer);
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        programField.referencedClass =
            findReferencedClass(programClass,
                                programField.getDescriptor(programClass));

        // Initialize the attributes.
        programField.attributesAccept(programClass, this);
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        programMethod.referencedClasses =
            findReferencedClasses(programClass,
                                  programMethod.getDescriptor(programClass));

        // Initialize the attributes.
        programMethod.attributesAccept(programClass, this);
    }


    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        libraryField.referencedClass =
            findReferencedClass(libraryClass,
                                libraryField.getDescriptor(libraryClass));
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        libraryMethod.referencedClasses =
            findReferencedClasses(libraryClass,
                                  libraryMethod.getDescriptor(libraryClass));
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        // Fill out the String class.
        stringConstant.javaLangStringClass =
            findClass(clazz, ClassConstants.NAME_JAVA_LANG_STRING);
    }


    public void visitDynamicConstant(Clazz clazz, DynamicConstant dynamicConstant)
    {
        dynamicConstant.referencedClasses =
            findReferencedClasses(clazz,
                                  dynamicConstant.getType(clazz));
    }


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        invokeDynamicConstant.referencedClasses =
            findReferencedClasses(clazz,
                                  invokeDynamicConstant.getType(clazz));
    }


    public void visitMethodHandleConstant(Clazz clazz, MethodHandleConstant methodHandleConstant)
    {
        // Fill out the MethodHandle class.
        methodHandleConstant.javaLangInvokeMethodHandleClass =
            findClass(clazz, ClassConstants.NAME_JAVA_LANG_INVOKE_METHOD_HANDLE);
    }


    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        String className = fieldrefConstant.getClassName(clazz);

        // Methods for array types should be found in the Object class.
        if (ClassUtil.isInternalArrayType(className))
        {
            className = ClassConstants.NAME_JAVA_LANG_OBJECT;
        }

        // See if we can find the referenced class.
        // Unresolved references are assumed to refer to library classes
        // that will not change anyway.
        Clazz referencedClass = findClass(clazz, className);

        if (referencedClass != null)
        {
            String name = fieldrefConstant.getName(clazz);
            String type = fieldrefConstant.getType(clazz);

            // See if we can find the referenced class member somewhere in the
            // hierarchy.
            Clazz referencingClass = checkAccessRules ? clazz : null;

            fieldrefConstant.referencedField = memberFinder.findField(referencingClass,
                                                                      referencedClass,
                                                                      name,
                                                                      type);
            fieldrefConstant.referencedClass = memberFinder.correspondingClass();

            if (fieldrefConstant.referencedField == null)
            {
                // We haven't found the class member anywhere in the hierarchy.
                boolean isProgramClass = referencedClass instanceof ProgramClass;

                WarningPrinter missingMemberWarningPrinter = isProgramClass ?
                    missingProgramMemberWarningPrinter :
                    missingLibraryMemberWarningPrinter;

                if (missingMemberWarningPrinter != null)
                {
                    missingMemberWarningPrinter.print(clazz.getName(),
                                                      className,
                                                      "Warning: " +
                                                      ClassUtil.externalClassName(clazz.getName()) +
                                                      ": can't find referenced field '"  +
                                                      ClassUtil.externalFullFieldDescription(0, name, type) +
                                                      "' in " +
                                                      (isProgramClass ?
                                                          "program" :
                                                          "library") +
                                                      " class " +
                                                      ClassUtil.externalClassName(className));
                }
            }
        }
    }


    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        String className = anyMethodrefConstant.getClassName(clazz);

        // Methods for array types should be found in the Object class.
        if (ClassUtil.isInternalArrayType(className))
        {
            className = ClassConstants.NAME_JAVA_LANG_OBJECT;
        }

        // See if we can find the referenced class.
        // Unresolved references are assumed to refer to library classes
        // that will not change anyway.
        Clazz referencedClass = findClass(clazz, className);

        if (referencedClass != null)
        {
            String name = anyMethodrefConstant.getName(clazz);
            String type = anyMethodrefConstant.getType(clazz);

            boolean isFieldRef = anyMethodrefConstant.getTag() == Constant.FIELDREF;

            // See if we can find the referenced class member somewhere in the
            // hierarchy.
            Clazz referencingClass = checkAccessRules ? clazz : null;

            anyMethodrefConstant.referencedMethod = memberFinder.findMethod(referencingClass,
                                                                            referencedClass,
                                                                            name,
                                                                            type);
            anyMethodrefConstant.referencedClass  = memberFinder.correspondingClass();

            if (anyMethodrefConstant.referencedMethod == null)
            {
                // We haven't found the class member anywhere in the hierarchy.
                boolean isProgramClass = referencedClass instanceof ProgramClass;

                WarningPrinter missingMemberWarningPrinter = isProgramClass ?
                    missingProgramMemberWarningPrinter :
                    missingLibraryMemberWarningPrinter;

                if (missingMemberWarningPrinter != null)
                {
                    missingMemberWarningPrinter.print(clazz.getName(),
                                                      className,
                                                      "Warning: " +
                                                      ClassUtil.externalClassName(clazz.getName()) +
                                                      ": can't find referenced method '" +
                                                      ClassUtil.externalFullMethodDescription(className, 0, name, type) +
                                                      "' in " +
                                                      (isProgramClass ?
                                                          "program" :
                                                          "library") +
                                                      " class " +
                                                      ClassUtil.externalClassName(className));
                }
            }
        }
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Fill out the referenced class.
        classConstant.referencedClass =
            findClass(clazz, ClassUtil.internalClassNameFromClassType(classConstant.getName(clazz)));

        // Fill out the Class class.
        classConstant.javaLangClassClass =
            findClass(clazz, ClassConstants.NAME_JAVA_LANG_CLASS);
    }


    public void visitMethodTypeConstant(Clazz clazz, MethodTypeConstant methodTypeConstant)
    {
        // Fill out the MethodType class.
        methodTypeConstant.javaLangInvokeMethodTypeClass =
            findClass(clazz, ClassConstants.NAME_JAVA_LANG_INVOKE_METHOD_TYPE);

        methodTypeConstant.referencedClasses =
            findReferencedClasses(clazz,
                                  methodTypeConstant.getType(clazz));
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute)
    {
        String enclosingClassName = enclosingMethodAttribute.getClassName(clazz);

        // See if we can find the referenced class.
        enclosingMethodAttribute.referencedClass =
            findClass(clazz, enclosingClassName);

        if (enclosingMethodAttribute.referencedClass != null)
        {
            // Is there an enclosing method? Otherwise it's just initialization
            // code outside of the constructors.
            if (enclosingMethodAttribute.u2nameAndTypeIndex != 0)
            {
                String name = enclosingMethodAttribute.getName(clazz);
                String type = enclosingMethodAttribute.getType(clazz);

                // See if we can find the method in the referenced class.
                enclosingMethodAttribute.referencedMethod =
                    enclosingMethodAttribute.referencedClass.findMethod(name, type);

                if (enclosingMethodAttribute.referencedMethod == null &&
                    missingProgramMemberWarningPrinter != null)
                {
                    // We couldn't find the enclosing method.
                    String className = clazz.getName();

                    missingProgramMemberWarningPrinter.print(className,
                                                             enclosingClassName,
                                                             "Warning: " +
                                                             ClassUtil.externalClassName(className) +
                                                             ": can't find enclosing method '" +
                                                             ClassUtil.externalFullMethodDescription(enclosingClassName, 0, name, type) +
                                                             "' in program class " +
                                                             ClassUtil.externalClassName(enclosingClassName));
                }
            }
        }
    }


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // Initialize the nested attributes.
        codeAttribute.attributesAccept(clazz, method, this);
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        // Initialize the local variables.
        localVariableTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        // Initialize the local variable types.
        localVariableTypeTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        signatureAttribute.referencedClasses =
            findReferencedClasses(clazz,
                                  signatureAttribute.getSignature(clazz));
    }


    public void visitAnyAnnotationsAttribute(Clazz clazz, AnnotationsAttribute annotationsAttribute)
    {
        // Initialize the annotations.
        annotationsAttribute.annotationsAccept(clazz, this);
    }


    public void visitAnyParameterAnnotationsAttribute(Clazz clazz, Method method, ParameterAnnotationsAttribute parameterAnnotationsAttribute)
    {
        // Initialize the annotations.
        parameterAnnotationsAttribute.annotationsAccept(clazz, method, this);
    }


    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        // Initialize the annotation.
        annotationDefaultAttribute.defaultValueAccept(clazz, this);
    }


    // Implementations for LocalVariableInfoVisitor.

    public void visitLocalVariableInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableInfo localVariableInfo)
    {
        localVariableInfo.referencedClass =
            findReferencedClass(clazz,
                                localVariableInfo.getDescriptor(clazz));
    }


    // Implementations for LocalVariableTypeInfoVisitor.

    public void visitLocalVariableTypeInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeInfo localVariableTypeInfo)
    {
        localVariableTypeInfo.referencedClasses =
            findReferencedClasses(clazz,
                                  localVariableTypeInfo.getSignature(clazz));
    }


    // Implementations for AnnotationVisitor.

    public void visitAnnotation(Clazz clazz, Annotation annotation)
    {
        annotation.referencedClasses =
            findReferencedClasses(clazz,
                                  annotation.getType(clazz));

        // Initialize the element values.
        annotation.elementValuesAccept(clazz, this);
    }


    // Implementations for ElementValueVisitor.

    public void visitConstantElementValue(Clazz clazz, Annotation annotation, ConstantElementValue constantElementValue)
    {
        initializeElementValue(clazz, annotation, constantElementValue);
    }


    public void visitEnumConstantElementValue(Clazz clazz, Annotation annotation, EnumConstantElementValue enumConstantElementValue)
    {
        initializeElementValue(clazz, annotation, enumConstantElementValue);

        enumConstantElementValue.referencedClasses =
            findReferencedClasses(clazz,
                                  enumConstantElementValue.getTypeName(clazz));
    }


    public void visitClassElementValue(Clazz clazz, Annotation annotation, ClassElementValue classElementValue)
    {
        initializeElementValue(clazz, annotation, classElementValue);

        classElementValue.referencedClasses =
            findReferencedClasses(clazz,
                                  classElementValue.getClassName(clazz));
    }


    public void visitAnnotationElementValue(Clazz clazz, Annotation annotation, AnnotationElementValue annotationElementValue)
    {
        initializeElementValue(clazz, annotation, annotationElementValue);

        // Initialize the annotation.
        annotationElementValue.annotationAccept(clazz, this);
    }


    public void visitArrayElementValue(Clazz clazz, Annotation annotation, ArrayElementValue arrayElementValue)
    {
        initializeElementValue(clazz, annotation, arrayElementValue);

        // Initialize the element values.
        arrayElementValue.elementValuesAccept(clazz, annotation, this);
    }


    /**
     * Initializes the referenced method of an element value, if any.
     */
    private void initializeElementValue(Clazz clazz, Annotation annotation, ElementValue elementValue)
    {
        // See if we have a referenced class.
        if (annotation                      != null &&
            annotation.referencedClasses    != null &&
            elementValue.u2elementNameIndex != 0)
        {
            // See if we can find the method in the referenced class
            // (ignoring the descriptor).
            String name = elementValue.getMethodName(clazz);

            Clazz referencedClass = annotation.referencedClasses[0];
            elementValue.referencedClass  = referencedClass;
            elementValue.referencedMethod = referencedClass.findMethod(name, null);
        }
    }

    // Kotlin initializer class, used to initialize Kotlin metadata references.

    private class KotlinReferenceInitializer
    implements    KotlinMetadataVisitor,
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
            kotlinDeclarationContainerMetadata.ownerClassName = clazz.getName();
            kotlinDeclarationContainerMetadata.ownerReferencedClass = findClass(clazz, kotlinDeclarationContainerMetadata.ownerClassName);

            kotlinDeclarationContainerMetadata.propertiesAccept(         clazz, this);
            kotlinDeclarationContainerMetadata.delegatedPropertiesAccept(clazz, this);
            kotlinDeclarationContainerMetadata.functionsAccept(          clazz, this);
            kotlinDeclarationContainerMetadata.typeAliasesAccept(        clazz,this);
        }


        @Override
        public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
        {
            kotlinClassKindMetadata.referencedClass = findClass(clazz, kotlinClassKindMetadata.className);

            if (kotlinClassKindMetadata.anonymousObjectOriginName != null)
            {
                kotlinClassKindMetadata.anonymousObjectOriginClass = findClass(clazz,
                                                                               kotlinClassKindMetadata.anonymousObjectOriginName);
            }

            //TODO strip prefix from companion and nested classes.

            if (kotlinClassKindMetadata.companionObjectName != null)
            {
                String name = clazz.getName() + "$" + kotlinClassKindMetadata.companionObjectName;
                kotlinClassKindMetadata.referencedCompanionClass = findClass(clazz, name);
                kotlinClassKindMetadata.referencedCompanionField = memberFinder.findField(clazz,
                                                                                          kotlinClassKindMetadata.companionObjectName,
                                                                                          ClassUtil.internalTypeFromClassName(name));
            }

            kotlinClassKindMetadata.referencedEnumEntries =
                kotlinClassKindMetadata.enumEntryNames
                    .stream()
                    .map(enumEntry ->
                         strictMemberFinder.findField(clazz, enumEntry, null))
                    .collect(Collectors.toList());

            kotlinClassKindMetadata.referencedNestedClasses =
                kotlinClassKindMetadata.nestedClassNames
                    .stream()
                    .map(nestedName ->
                         findClass(clazz, clazz.getName() + "$" + nestedName))
                    .collect(Collectors.toList());

            kotlinClassKindMetadata.referencedSealedSubClasses=
                kotlinClassKindMetadata.sealedSubclassNames
                    .stream()
                    .map(sealedSubName ->
                         findClass(clazz, sealedSubName))
                    .collect(Collectors.toList());


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
            kotlinSyntheticClassKindMetadata.className       = clazz.getName();
            kotlinSyntheticClassKindMetadata.referencedClass = clazz;
            kotlinSyntheticClassKindMetadata.functionsAccept(clazz, this);
        }

        @Override
        public void visitKotlinMultiFileFacadeMetadata(Clazz clazz, KotlinMultiFileFacadeKindMetadata kotlinMultiFileFacadeKindMetadata)
        {
            kotlinMultiFileFacadeKindMetadata.referencedPartClasses =
                kotlinMultiFileFacadeKindMetadata.partClassNames
                    .stream()
                    .map(partName ->
                         findClass(clazz, partName))
                    .collect(Collectors.toList());
        }

        @Override
        public void visitKotlinMultiFilePartMetadata(Clazz clazz, KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata)
        {
            kotlinMultiFilePartKindMetadata.referencedFacade = findClass(clazz,
                                                                         kotlinMultiFilePartKindMetadata.xs);

            visitKotlinDeclarationContainerMetadata(clazz, kotlinMultiFilePartKindMetadata);
        }

        // Implementations for KotlinPropertyVisitor.
        @Override
        public void visitAnyProperty(Clazz                              clazz,
                                     KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                     KotlinPropertyMetadata             kotlinPropertyMetadata)
        {
            if (kotlinPropertyMetadata.backingFieldSignature != null)
            {
                kotlinPropertyMetadata.referencedBackingField =
                    strictMemberFinder.findField(clazz,
                                                 kotlinPropertyMetadata.backingFieldSignature.getName(),
                                                 kotlinPropertyMetadata.backingFieldSignature.getDesc());

                kotlinPropertyMetadata.referencedBackingFieldClass = strictMemberFinder.correspondingClass();
            }

            if (kotlinPropertyMetadata.getterSignature != null)
                //TODO kotlinPropertyMetadata.hasGetter()
            {
                kotlinPropertyMetadata.referencedGetterMethod =
                    strictMemberFinder.findMethod(clazz,
                                                  kotlinPropertyMetadata.getterSignature.getName(),
                                                  kotlinPropertyMetadata.getterSignature.getDesc());
            }

            if (kotlinPropertyMetadata.setterSignature != null)
            //TODO kotlinPropertyMetadata.hasSetter()
            {
                kotlinPropertyMetadata.referencedSetterMethod =
                    strictMemberFinder.findMethod(clazz,
                                                  kotlinPropertyMetadata.setterSignature.getName(),
                                                  kotlinPropertyMetadata.setterSignature.getDesc());
            }

            if (kotlinPropertyMetadata.syntheticMethodForAnnotations != null)
            {
                kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations =
                    strictMemberFinder.findMethod(clazz,
                                                  kotlinPropertyMetadata.syntheticMethodForAnnotations.getName(),
                                                  kotlinPropertyMetadata.syntheticMethodForAnnotations.getDesc());

                kotlinPropertyMetadata.referencedSyntheticMethodClass = strictMemberFinder.correspondingClass();
            }

            kotlinPropertyMetadata.typeParametersAccept(  clazz, kotlinDeclarationContainerMetadata, this);
            kotlinPropertyMetadata.receiverTypeAccept(    clazz, kotlinDeclarationContainerMetadata, this);
            kotlinPropertyMetadata.typeAccept(            clazz, kotlinDeclarationContainerMetadata, this);
            kotlinPropertyMetadata.setterParametersAccept(clazz, kotlinDeclarationContainerMetadata, this);
        }


        // Implementations for KotlinFunctionVisitor.
        @Override
        public void visitAnyFunction(Clazz                  clazz,
                                     KotlinMetadata         kotlinMetadata,
                                     KotlinFunctionMetadata kotlinFunctionMetadata)
        {
            kotlinFunctionMetadata.referencedMethodClass = clazz;

            if (kotlinFunctionMetadata.jvmSignature != null)
            {
                kotlinFunctionMetadata.referencedMethod =
                    strictMemberFinder.findMethod(kotlinFunctionMetadata.referencedMethodClass,
                                                  kotlinFunctionMetadata.jvmSignature.getName(),
                                                  kotlinFunctionMetadata.jvmSignature.getDesc());
            }

            if (kotlinFunctionMetadata.lambdaClassOriginName != null)
            {
                // Have to temporarily remove the warning printer because there might not be an actual class.
                WarningPrinter tmpWarningPrinter = missingClassWarningPrinter;
                missingClassWarningPrinter = null;

                kotlinFunctionMetadata.referencedLambdaClassOrigin =
                    findClass(clazz, kotlinFunctionMetadata.lambdaClassOriginName);

                missingClassWarningPrinter = tmpWarningPrinter;

                if (kotlinFunctionMetadata.referencedLambdaClassOrigin == null)
                {
                    kotlinFunctionMetadata.lambdaClassOriginName = null;
                }
            }

            kotlinFunctionMetadata.contractsAccept(      clazz, kotlinMetadata, new AllTypeVisitor(this));
            kotlinFunctionMetadata.typeParametersAccept( clazz, kotlinMetadata, this);
            kotlinFunctionMetadata.receiverTypeAccept(   clazz, kotlinMetadata, this);
            kotlinFunctionMetadata.valueParametersAccept(clazz, kotlinMetadata, this);
            kotlinFunctionMetadata.returnTypeAccept(     clazz, kotlinMetadata, this);
        }

        // Implementations for KotlinConstructorVisitor
        @Override
        public void visitConstructor(Clazz                     clazz,
                                     KotlinClassKindMetadata   kotlinClassKindMetadata,
                                     KotlinConstructorMetadata kotlinConstructorMetadata)
        {
            // Annotation constructors don't have a corresponding constructor method.
            if (kotlinConstructorMetadata.jvmSignature != null)
            {
                kotlinConstructorMetadata.referencedMethod =
                    strictMemberFinder.findMethod(clazz,
                                                  kotlinConstructorMetadata.jvmSignature.getName(),
                                                  kotlinConstructorMetadata.jvmSignature.getDesc());
            }

            kotlinConstructorMetadata.valueParametersAccept(clazz, kotlinClassKindMetadata, this);
        }

        // Implementations for KotlinTypeVisitor.
        @Override
        public void visitAnyType(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata)
        {
            String className = kotlinTypeMetadata.className;

            if (className != null)
            {
                    if (className.startsWith("."))
                    {
                        className = className.substring(1);
                    }

                    if (className.contains("."))
                    {
                        className = className.replace('.', '$');
                    }

                    // Have to temporarily remove the warning printer because clazz could be null
                    WarningPrinter missingClassWarningPrinter = ClassReferenceInitializer.this.missingClassWarningPrinter;
                    ClassReferenceInitializer.this.missingClassWarningPrinter = null;

                    kotlinTypeMetadata.referencedClass = findClass(clazz, className);

                    if (kotlinTypeMetadata.referencedClass == null)
                    {
                        // Assign dummy Kotlin class.
                        kotlinTypeMetadata.referencedClass = KotlinConstants.dummyClassPool.getClass(className);

                        if (kotlinTypeMetadata.referencedClass == null &&
                            !className.equals(""))
                        {
                            missingClassWarningPrinter.print(className,
                                                             "Warning: can't find referenced class in programClassPool or dummyKotlinClassPool - " +
                                                             ClassUtil.externalClassName(className));
                        }
                    }

                    ClassReferenceInitializer.this.missingClassWarningPrinter = missingClassWarningPrinter;
            }
            else if (kotlinTypeMetadata.aliasName != null)
            {
                // This type is an alias, that refers to an actual type (or another alias).
                // We search for the alias definition to create a reference for this use.

                String aliasName = kotlinTypeMetadata.aliasName;

                int innerClassMarkerIndex = aliasName.lastIndexOf('.');
                String classNameFilter, simpleName;

                if (innerClassMarkerIndex != -1)
                {
                    // Declared inside a class - we know exactly which one.
                    classNameFilter = aliasName.substring(0, innerClassMarkerIndex);
                    simpleName      = aliasName.substring(innerClassMarkerIndex + 1);
                }
                else
                {
                    // Declared in a file facade - we know which package only.
                    int lastPackageMarkerIndex = aliasName.lastIndexOf('/');
                    classNameFilter            = aliasName.substring(0, lastPackageMarkerIndex) + "/*";
                    simpleName                 = aliasName.substring(lastPackageMarkerIndex + 1);
                }

                programClassPool.classesAccept(
                    classNameFilter,
                    new ReferencedKotlinMetadataVisitor(
                    new KotlinTypeAliasReferenceInitializer(kotlinTypeMetadata, simpleName))
                );
            }

            initializeAnnotations(clazz, kotlinTypeMetadata.annotations);

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
            initializeAnnotations(clazz, kotlinTypeAliasMetadata.annotations);

            kotlinTypeAliasMetadata.referencedDeclarationContainer = kotlinDeclarationContainerMetadata;

            kotlinTypeAliasMetadata.underlyingTypeAccept(clazz, kotlinDeclarationContainerMetadata, this);
            kotlinTypeAliasMetadata.expandedTypeAccept(  clazz, kotlinDeclarationContainerMetadata, this);
            kotlinTypeAliasMetadata.typeParametersAccept(clazz, kotlinDeclarationContainerMetadata, this);
        }


        // Implementations for KotlinValueParameterVisitor.
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
            initializeAnnotations(clazz, kotlinTypeParameterMetadata.annotations);

            kotlinTypeParameterMetadata.upperBoundsAccept(clazz, this);
        }

        /**
         * Initialize Kotlin annotations.
         */
        private void initializeAnnotations(Clazz clazz, List<KotlinMetadataAnnotation> annotations)
        {
            for (KotlinMetadataAnnotation annotation : annotations)
            {
                annotation.referencedAnnotationClass = findClass(clazz, annotation.kmAnnotation.getClassName());
                if (annotation.referencedAnnotationClass != null)
                {
                    Set<String> argumentNames            = annotation.kmAnnotation.getArguments().keySet();
                    Map<String, Method> referencedKeys   = new HashMap<String, Method>();
                    for (String argumentName : argumentNames)
                    {
                        referencedKeys.put(argumentName, strictMemberFinder.findMethod(annotation.referencedAnnotationClass, argumentName, null));
                    }
                    annotation.referencedArgumentMethods = referencedKeys;
                }
                else
                {
                    annotation.referencedArgumentMethods = new HashMap<>();
                }
            }
        }
    }

    // Small utility methods.

    /**
     * Returns the single class referenced by the given descriptor, or
     * <code>null</code> if there isn't any useful reference.
     */
    private Clazz findReferencedClass(Clazz  referencingClass,
                                      String descriptor)
    {
        DescriptorClassEnumeration enumeration =
            new DescriptorClassEnumeration(descriptor);

        enumeration.nextFluff();

        if (enumeration.hasMoreClassNames())
        {
            return findClass(referencingClass, enumeration.nextClassName());
        }

        return null;
    }


    /**
     * Returns an array of classes referenced by the given descriptor, or
     * <code>null</code> if there aren't any useful references.
     */
    private Clazz[] findReferencedClasses(Clazz  referencingClass,
                                          String descriptor)
    {
        DescriptorClassEnumeration enumeration =
            new DescriptorClassEnumeration(descriptor);

        int classCount = enumeration.classCount();
        if (classCount > 0)
        {
            Clazz[] referencedClasses = new Clazz[classCount];

            boolean foundReferencedClasses = false;

            for (int index = 0; index < classCount; index++)
            {
                String fluff = enumeration.nextFluff();
                String name  = enumeration.nextClassName();

                Clazz referencedClass = findClass(referencingClass, name);

                if (referencedClass != null)
                {
                    referencedClasses[index] = referencedClass;
                    foundReferencedClasses = true;
                }
            }

            if (foundReferencedClasses)
            {
                return referencedClasses;
            }
        }

        return null;
    }


    /**
     * Returns the class with the given name, either for the program class pool
     * or from the library class pool, or <code>null</code> if it can't be found.
     */
    private Clazz findClass(Clazz referencingClass, String name)
    {
        // Is it an array type?
        if (ClassUtil.isInternalArrayType(name))
        {
            // Ignore any primitive array types.
            if (!ClassUtil.isInternalClassType(name))
            {
                return null;
            }

            // Strip the array part.
            name = ClassUtil.internalClassNameFromClassType(name);
        }

        // First look for the class in the program class pool.
        Clazz clazz = programClassPool.getClass(name);

        // Otherwise look for the class in the library class pool.
        if (clazz == null)
        {
            clazz = libraryClassPool.getClass(name);

            if (clazz == null &&
                missingClassWarningPrinter != null)
            {
                // We didn't find the superclass or interface. Print a warning.
                String referencingClassName = referencingClass.getName();

                missingClassWarningPrinter.print(referencingClassName,
                                                 name,
                                                 "Warning: " +
                                                 ClassUtil.externalClassName(referencingClassName) +
                                                 ": can't find referenced class " +
                                                 ClassUtil.externalClassName(name));
            }
        }
        else if (dependencyWarningPrinter != null)
        {
            // The superclass or interface was found in the program class pool.
            // Print a warning.
            String referencingClassName = referencingClass.getName();

            dependencyWarningPrinter.print(referencingClassName,
                                           name,
                                           "Warning: library class " +
                                           ClassUtil.externalClassName(referencingClassName) +
                                           " depends on program class " +
                                           ClassUtil.externalClassName(name));
        }

        return clazz;
    }


    // Helper class for KotlinReferenceInitializer.

    public static class KotlinTypeAliasReferenceInitializer
    implements          KotlinMetadataVisitor,
                        KotlinTypeAliasVisitor
    {
        private final KotlinTypeMetadata kotlinTypeMetadata;
        private final String             simpleName;

        KotlinTypeAliasReferenceInitializer(KotlinTypeMetadata kotlinTypeMetadata, String simpleName)
        {
            this.simpleName         = simpleName;
            this.kotlinTypeMetadata = kotlinTypeMetadata;
        }

        // Implementations for KotlinMetadataVisitor.

        @Override
        public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

        @Override
        public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                            KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
        {
            kotlinDeclarationContainerMetadata.typeAliasesAccept(clazz, this);
        }

        @Override
        public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
        {
            // Only if the alias was declared inside this class.
            if (kotlinTypeMetadata.aliasName.equals(clazz.getName() + "." + simpleName))
            {
                kotlinClassKindMetadata.typeAliasesAccept(clazz, this);
            }
        }

        // Implementations for KotlinTypeAliasVisitor.

        @Override
        public void visitTypeAlias(Clazz                              clazz,
                                   KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                   KotlinTypeAliasMetadata            kotlinTypeAliasMetadata)
        {
            if (this.simpleName.equals(kotlinTypeAliasMetadata.name))
            {
                this.kotlinTypeMetadata.referencedTypeAlias = kotlinTypeAliasMetadata;
            }
        }
    }
}
