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
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.util.*;

import java.util.*;

/**
 * This ClassVisitor removes constant pool entries, class members, and other
 * class elements that are not marked as being used.
 *
 * @see ClassUsageMarker
 *
 * @author Eric Lafortune
 */
public class ClassShrinker
implements   ClassVisitor,
             MemberVisitor,
             AttributeVisitor,
             RecordComponentInfoVisitor,
             AnnotationVisitor,
             ElementValueVisitor
{
    private final SimpleUsageMarker usageMarker;

    private       int[]                   constantIndexMap        = new int[ClassEstimates.TYPICAL_CONSTANT_POOL_SIZE];
    private       int[]                   bootstrapMethodIndexMap = new int[ClassEstimates.TYPICAL_CONSTANT_POOL_SIZE];
    private final MyAttributeShrinker     attributeShrinker       = new MyAttributeShrinker();
    private final ConstantPoolRemapper    constantPoolRemapper    = new ConstantPoolRemapper();
    private final BootstrapMethodRemapper bootstrapMethodRemapper = new BootstrapMethodRemapper();
    private final MySignatureCleaner      signatureCleaner        = new MySignatureCleaner();



    /**
     * Creates a new ClassShrinker.
     * @param usageMarker the usage marker that is used to mark the classes
     *                    and class members.
     */
    public ClassShrinker(SimpleUsageMarker usageMarker)
    {
        this.usageMarker = usageMarker;
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
        // Mark the classes with processing flags if fields or methods are removed
        // (used for configuration debugging).
        programClass.fieldsAccept(
            new UsedMemberFilter(usageMarker, null,
            new MemberAccessFilter(AccessConstants.PUBLIC, 0,
            new MemberToClassVisitor(
            new MultiClassVisitor(
                new ProcessingFlagSetter(ProcessingFlags.REMOVED_FIELDS),
                new ProcessingFlagSetter(ProcessingFlags.REMOVED_PUBLIC_FIELDS)
            )),
            new MemberToClassVisitor(
            new ProcessingFlagSetter(ProcessingFlags.REMOVED_FIELDS)))));

        programClass.methodsAccept(
            new UsedMemberFilter(usageMarker, null,
            new ConstructorMethodFilter(

            new MemberAccessFilter(AccessConstants.PUBLIC, 0,
            new MemberToClassVisitor(
            new MultiClassVisitor(
                new ProcessingFlagSetter(ProcessingFlags.REMOVED_CONSTRUCTORS),
                new ProcessingFlagSetter(ProcessingFlags.REMOVED_PUBLIC_CONSTRUCTORS)
            )),
            new MemberToClassVisitor(
            new ProcessingFlagSetter(ProcessingFlags.REMOVED_CONSTRUCTORS))),

            new MemberAccessFilter(AccessConstants.PUBLIC, 0,
            new MemberToClassVisitor(
            new MultiClassVisitor(
                new ProcessingFlagSetter(ProcessingFlags.REMOVED_METHODS),
                new ProcessingFlagSetter(ProcessingFlags.REMOVED_PUBLIC_METHODS)
            )),
            new MemberToClassVisitor(
            new ProcessingFlagSetter(ProcessingFlags.REMOVED_METHODS))))));

        // Shrink the arrays for constant pool, interfaces, fields, methods,
        // and class attributes.
        if (programClass.u2interfacesCount > 0)
        {
            new InterfaceDeleter(shrinkInterfaceFlags(programClass), true)
                .visitProgramClass(programClass);
        }

        // Shrink the arrays for nest members and permitted subclasses.
        programClass.attributesAccept(attributeShrinker);

        // Shrink the constant pool, also setting up an index map.
        int newConstantPoolCount =
            shrinkConstantPool(programClass.constantPool,
                               programClass.u2constantPoolCount);

        int oldFieldsCount = programClass.u2fieldsCount;
        programClass.u2fieldsCount =
            shrinkArray(programClass.fields,
                        programClass.u2fieldsCount);
        if (programClass.u2fieldsCount < oldFieldsCount)
        {
            programClass.processingFlags |= ProcessingFlags.REMOVED_FIELDS;
        }

        int oldMethodsCount = programClass.u2methodsCount;
        programClass.u2methodsCount =
            shrinkArray(programClass.methods,
                        programClass.u2methodsCount);
        if (programClass.u2methodsCount < oldMethodsCount)
        {
            programClass.processingFlags |= ProcessingFlags.REMOVED_METHODS;
        }

        programClass.u2attributesCount =
            shrinkArray(programClass.attributes,
                        programClass.u2attributesCount);

        // Compact the remaining fields, methods, and attributes,
        // and remap their references to the constant pool.
        programClass.fieldsAccept(this);
        programClass.methodsAccept(this);
        programClass.attributesAccept(this);

        // Remap the references to the constant pool if it has shrunk.
        if (newConstantPoolCount < programClass.u2constantPoolCount)
        {
            programClass.u2constantPoolCount = newConstantPoolCount;

            // Remap all constant pool references.
            constantPoolRemapper.setConstantIndexMap(constantIndexMap);
            constantPoolRemapper.visitProgramClass(programClass);
        }

        // Replace any unused classes in the signatures.
        programClass.fieldsAccept(new AllAttributeVisitor(signatureCleaner));
        programClass.methodsAccept(new AllAttributeVisitor(signatureCleaner));
        programClass.attributesAccept(signatureCleaner);

        // Compact the extra field pointing to the subclasses of this class.
        programClass.subClassCount =
            shrinkArray(programClass.subClasses,
                        programClass.subClassCount);
    }


    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Library classes are left unchanged.

        // Compact the extra field pointing to the subclasses of this class.
        libraryClass.subClassCount =
            shrinkArray(libraryClass.subClasses,
                        libraryClass.subClassCount);
    }


    // Implementations for MemberVisitor.

    public void visitProgramMember(ProgramClass programClass, ProgramMember programMember)
    {
        // Shrink the attributes array.
        programMember.u2attributesCount =
            shrinkArray(programMember.attributes,
                        programMember.u2attributesCount);

        // Shrink any attributes.
        programMember.attributesAccept(programClass, this);
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute)
    {
        // Shrink the array of BootstrapMethodInfo objects.
        int newBootstrapMethodsCount =
            shrinkBootstrapMethodArray(bootstrapMethodsAttribute.bootstrapMethods,
                                       bootstrapMethodsAttribute.u2bootstrapMethodsCount);

        if (newBootstrapMethodsCount < bootstrapMethodsAttribute.u2bootstrapMethodsCount)
        {
            bootstrapMethodsAttribute.u2bootstrapMethodsCount = newBootstrapMethodsCount;

            // Remap all bootstrap method references.
            bootstrapMethodRemapper.setBootstrapMethodIndexMap(bootstrapMethodIndexMap);
            clazz.constantPoolEntriesAccept(bootstrapMethodRemapper);
        }
    }


    public void visitRecordAttribute(Clazz clazz, RecordAttribute recordAttribute)
    {
        // Shrink the array of RecordComponentInfo objects.
        recordAttribute.u2componentsCount =
            shrinkArray(recordAttribute.components,
                        recordAttribute.u2componentsCount);

        // Shrink the attributes of the remaining components.
        recordAttribute.componentsAccept(clazz, this);
    }


    public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute)
    {
        // Shrink the array of InnerClassesInfo objects.
        innerClassesAttribute.u2classesCount =
            shrinkArray(innerClassesAttribute.classes,
                        innerClassesAttribute.u2classesCount);
    }


    public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute)
    {
        // Sometimes, a class is still referenced (apparently as a dummy class),
        // but its enclosing method is not. Then remove the reference to
        // the enclosing method.
        // E.g. the anonymous inner class javax.swing.JList$1 is defined inside
        // a constructor of javax.swing.JList, but it is also referenced as a
        // dummy argument in a constructor of javax.swing.JList$ListSelectionHandler.
        if (enclosingMethodAttribute.referencedMethod != null &&
            !usageMarker.isUsed(enclosingMethodAttribute.referencedMethod))
        {
            enclosingMethodAttribute.u2nameAndTypeIndex = 0;

            enclosingMethodAttribute.referencedMethod = null;
        }
    }


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // Shrink the attributes array.
        codeAttribute.u2attributesCount =
            shrinkArray(codeAttribute.attributes,
                        codeAttribute.u2attributesCount);

        // Shrink the attributes themselves.
        codeAttribute.attributesAccept(clazz, method, this);
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        // Shrink the local variable info array.
        localVariableTableAttribute.u2localVariableTableLength =
            shrinkArray(localVariableTableAttribute.localVariableTable,
                        localVariableTableAttribute.u2localVariableTableLength);
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        // Shrink the local variable type info array.
        localVariableTypeTableAttribute.u2localVariableTypeTableLength =
            shrinkArray(localVariableTypeTableAttribute.localVariableTypeTable,
                        localVariableTypeTableAttribute.u2localVariableTypeTableLength);
    }


    public void visitAnyAnnotationsAttribute(Clazz clazz, AnnotationsAttribute annotationsAttribute)
    {
        // Shrink the annotations array.
        annotationsAttribute.u2annotationsCount =
            shrinkArray(annotationsAttribute.annotations,
                        annotationsAttribute.u2annotationsCount);

        // Shrink the annotations themselves.
        annotationsAttribute.annotationsAccept(clazz, this);
    }


    public void visitAnyParameterAnnotationsAttribute(Clazz clazz, Method method, ParameterAnnotationsAttribute parameterAnnotationsAttribute)
    {
        // Loop over all parameters.
        for (int parameterIndex = 0; parameterIndex < parameterAnnotationsAttribute.u1parametersCount; parameterIndex++)
        {
            // Shrink the parameter annotations array.
            parameterAnnotationsAttribute.u2parameterAnnotationsCount[parameterIndex] =
                shrinkArray(parameterAnnotationsAttribute.parameterAnnotations[parameterIndex],
                            parameterAnnotationsAttribute.u2parameterAnnotationsCount[parameterIndex]);
        }

        // Shrink the annotations themselves.
        parameterAnnotationsAttribute.annotationsAccept(clazz, method, this);
    }


    // Implementations for RecordComponentInfoVisitor.

    public void visitRecordComponentInfo(Clazz clazz, RecordComponentInfo recordComponentInfo)
    {
        // Shrink the attributes array.
        recordComponentInfo.u2attributesCount =
            shrinkArray(recordComponentInfo.attributes,
                        recordComponentInfo.u2attributesCount);

        // Shrink the remaining attributes.
        recordComponentInfo.attributesAccept(clazz, this);
    }


    // Implementations for AnnotationVisitor.

    public void visitAnnotation(Clazz clazz, Annotation annotation)
    {
        // Shrink the element values array.
        annotation.u2elementValuesCount =
            shrinkArray(annotation.elementValues,
                        annotation.u2elementValuesCount);

        // Shrink the element values themselves.
        annotation.elementValuesAccept(clazz, this);
    }


    /**
     * This AttributeVisitor shrinks the nest members in the nest member
     * attributes and the permitted subclasses in the permitted subclasses
     * attributes that it visits.
     */
    private class MyAttributeShrinker
    implements    AttributeVisitor
    {
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


        public void visitNestMembersAttribute(Clazz clazz, NestMembersAttribute nestMembersAttribute)
        {
            // Shrink the array of nest member indices.
            // We must do this before the corresponding constants are remapped.
            nestMembersAttribute.u2classesCount =
                shrinkConstantIndexArray(((ProgramClass)clazz).constantPool,
                                         nestMembersAttribute.u2classes,
                                         nestMembersAttribute.u2classesCount);
        }


        public void visitPermittedSubclassesAttribute(Clazz clazz, PermittedSubclassesAttribute permittedSubclassesAttribute)
        {
            // Shrink the array of nest member indices.
            // We must do this before the corresponding constants are remapped.
            permittedSubclassesAttribute.u2classesCount =
                shrinkConstantIndexArray(((ProgramClass)clazz).constantPool,
                                         permittedSubclassesAttribute.u2classes,
                                         permittedSubclassesAttribute.u2classesCount);
        }
    }


    /**
     * This AttributeVisitor updates the Utf8 constants of signatures
     * of classes, fields, and methods.
     */
    private class MySignatureCleaner
    implements    AttributeVisitor
    {
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


        public void visitSignatureAttribute(Clazz clazz, SignatureAttribute  signatureAttribute)
        {
            Clazz[] referencedClasses = signatureAttribute.referencedClasses;
            if (referencedClasses != null)
            {
                // Go over the classes in the signature.
                String signature = signatureAttribute.getSignature(clazz);

                DescriptorClassEnumeration classEnumeration =
                    new DescriptorClassEnumeration(signature);

                int referencedClassIndex = 0;

                // Start construction a new signature.
                StringBuffer newSignatureBuffer = new StringBuffer();

                newSignatureBuffer.append(classEnumeration.nextFluff());

                while (classEnumeration.hasMoreClassNames())
                {
                    String className = classEnumeration.nextClassName();

                    // Replace the class name if it is unused.
                    Clazz referencedClass = referencedClasses[referencedClassIndex];
                    if (referencedClass != null &&
                        !usageMarker.isUsed(referencedClass))
                    {
                        className = ClassConstants.NAME_JAVA_LANG_OBJECT;

                        referencedClasses[referencedClassIndex] = null;
                    }

                    // Use a short name if it's an inner class after a '.'
                    // separator.
                    else if (classEnumeration.isInnerClassName())
                    {
                        className = className.substring(className.lastIndexOf(TypeConstants.INNER_CLASS_SEPARATOR)+1);
                    }

                    referencedClassIndex++;

                    newSignatureBuffer.append(className);
                    newSignatureBuffer.append(classEnumeration.nextFluff());
                }

                // Update the signature.
                ((Utf8Constant)((ProgramClass)clazz).constantPool[signatureAttribute.u2signatureIndex]).setString(newSignatureBuffer.toString());
            }
        }
    }


    // Implementations for ElementValueVisitor.

    public void visitAnyElementValue(Clazz clazz, Annotation annotation, ElementValue elementValue) {}


    public void visitAnnotationElementValue(Clazz clazz, Annotation annotation, AnnotationElementValue annotationElementValue)
    {
        // Shrink the contained annotation.
        annotationElementValue.annotationAccept(clazz, this);
    }


    public void visitArrayElementValue(Clazz clazz, Annotation annotation, ArrayElementValue arrayElementValue)
    {
        // Shrink the element values array.
        arrayElementValue.u2elementValuesCount =
            shrinkArray(arrayElementValue.elementValues,
                        arrayElementValue.u2elementValuesCount);

        // Shrink the element values themselves.
        arrayElementValue.elementValuesAccept(clazz, annotation, this);
    }


    // Small utility methods.

    /**
     * Removes all entries that are not marked as being used from the given
     * constant pool. Creates a map from the old indices to the new indices
     * as a side effect.
     * @return the new number of entries.
     */
    private int shrinkConstantPool(Constant[] constantPool, int length)
    {
        if (constantIndexMap.length < length)
        {
            constantIndexMap = new int[length];
        }

        int     counter = 1;
        boolean isUsed  = false;

        // Shift the used constant pool entries together.
        for (int index = 1; index < length; index++)
        {
            constantIndexMap[index] = counter;

            Constant constant = constantPool[index];

            // Is the constant being used? Don't update the flag if this is the
            // second half of a long entry.
            if (constant != null)
            {
                isUsed = usageMarker.isUsed(constant);
            }

            if (isUsed)
            {
                // Remember the new index.
                constantIndexMap[index] = counter;

                // Shift the constant pool entry.
                constantPool[counter++] = constant;
            }
            else
            {
                // Remember an invalid index.
                constantIndexMap[index] = -1;
            }
        }

        // Clear the remaining constant pool elements.
        Arrays.fill(constantPool, counter, length, null);

        return counter;
    }


    /**
     * Creates an array marking unused constant pool entries for all the
     * elements in the given array of constant pool indices.
     * @return an array of flags indicating unused elements.
     */
    private boolean[] shrinkFlags(Constant[] constantPool, int[] array, int length)
    {
        boolean[] unused = new boolean[length];

        // Remember the unused constants.
        for (int index = 0; index < length; index++)
        {
            if (!usageMarker.isUsed(constantPool[array[index]]))
            {
                unused[index] = true;
            }
        }

        return unused;
    }


    /**
     * Creates an array marking unused constant pool entries for all the
     * elements in the given array of constant pool indices, pointing to
     * class constants of interfaces.
     * @return an array of flags indicating unused elements.
     */
    private boolean[] shrinkInterfaceFlags(ProgramClass programClass)
    {
        Constant[] constantPool    = programClass.constantPool;
        int[]      interfaces      = programClass.u2interfaces;
        int        interfacesCount = programClass.u2interfacesCount;

        // Collect the names of all indirectly implemented interfaces, unless
        // they are kept or the class itself is kept. That avoids problems if
        // some code applies reflection to the list of interfaces.
        Set indirectlyImplementedInterfaces = new HashSet();

        if ((programClass.getProcessingFlags() & ProcessingFlags.DONT_SHRINK) == 0)
        {
            ConstantVisitor interfaceNameCollector =
                new ReferencedClassVisitor(
                new UsedClassFilter(usageMarker,
                new ClassHierarchyTraveler(false, true, true, false,
                new ProgramClassFilter(
                new UsedClassFilter(usageMarker,
                new ClassAccessFilter(AccessConstants.INTERFACE, 0,
                new ClassProcessingFlagFilter(0, ProcessingFlags.DONT_SHRINK,
                new ClassNameCollector(indirectlyImplementedInterfaces))))))));

            programClass.superClassConstantAccept(interfaceNameCollector);
            programClass.interfaceConstantsAccept(interfaceNameCollector);
        }

        boolean[] unused = new boolean[interfacesCount];

        // Remember the unused or unnecessary constants.
        for (int index = 0; index < interfacesCount; index++)
        {
            // The interface may be unused, or it may be unnecessary in the
            // list of implemented interfaces, if a superclass/interface
            // already implements it.
            ClassConstant interfaceClassConstant =
                (ClassConstant)constantPool[interfaces[index]];
            String interfaceClassName =
                interfaceClassConstant.getName(programClass);

            if (!usageMarker.isUsed(interfaceClassConstant) ||
                indirectlyImplementedInterfaces.contains(interfaceClassName))
            {
                unused[index] = true;
            }
        }

        return unused;
    }


    /**
     * Removes all indices that point to unused constant pool entries
     * from the given array.
     * @return the new number of indices.
     */
    private int shrinkConstantIndexArray(Constant[] constantPool, int[] array, int length)
    {
        int counter = 0;

        // Shift the used objects together.
        for (int index = 0; index < length; index++)
        {
            if (usageMarker.isUsed(constantPool[array[index]]))
            {
                array[counter++] = array[index];
            }
        }

        // Clear the remaining array elements.
        Arrays.fill(array, counter, length, 0);

        return counter;
    }


    /**
     * Removes all entries that are not marked as being used from the given
     * array of bootstrap methods. Creates a map from the old indices to the
     * new indices as a side effect.
     * @return the new number of entries.
     */
    private int shrinkBootstrapMethodArray(BootstrapMethodInfo[] bootstrapMethods, int length)
    {
        if (bootstrapMethodIndexMap.length < length)
        {
            bootstrapMethodIndexMap = new int[length];
        }

        int counter = 0;

        // Shift the used bootstrap methods together.
        for (int index = 0; index < length; index++)
        {
            BootstrapMethodInfo bootstrapMethod = bootstrapMethods[index];

            // Is the entry being used?
            if (usageMarker.isUsed(bootstrapMethod))
            {
                // Remember the new index.
                bootstrapMethodIndexMap[index] = counter;

                // Shift the entry.
                bootstrapMethods[counter++] = bootstrapMethod;
            }
            else
            {
                // Remember an invalid index.
                bootstrapMethodIndexMap[index] = -1;
            }
        }

        // Clear the remaining bootstrap methods.
        Arrays.fill(bootstrapMethods, counter, length, null);

        return counter;
    }


    /**
     * Removes all Processable objects that are not marked as being used
     * from the given array.
     * @return the new number of Processable objects.
     */
    private int shrinkArray(Processable[] array, int length)
    {
        int counter = 0;

        // Shift the used objects together.
        for (int index = 0; index < length; index++)
        {
            Processable processable = array[index];

            if (usageMarker.isUsed(processable))
            {
                array[counter++] = processable;
            }
        }

        // Clear any remaining array elements.
        if (counter < length)
        {
            Arrays.fill(array, counter, length, null);
        }

        return counter;
    }
}
