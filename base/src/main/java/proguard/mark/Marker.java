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
package proguard.mark;

import proguard.*;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.*;
import proguard.classfile.util.AllParameterVisitor;
import proguard.classfile.visitor.*;
import proguard.pass.Pass;
import proguard.util.*;

import java.util.Arrays;

import static proguard.util.ProcessingFlags.*;
import static proguard.util.ProcessingFlags.DONT_OBFUSCATE;

/**
 * This pass translates the keep rules and other class specifications from the
 * configuration into processing flags on classes and class members.
 *
 * @author Johan Leys
 */
public class Marker implements Pass
{
    /**
     * Marks classes and class members in the given class pools with
     * appropriate access flags, corresponding to the class specifications
     * in the configuration.
     */
    @Override
    public void execute(AppView appView)
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Marking classes and class members to be kept...");
        }

        // Create a combined ClassPool visitor for marking classes.
        MultiClassPoolVisitor classPoolVisitor =
            new MultiClassPoolVisitor(
                createShrinkingMarker(appView.configuration),
                createOptimizationMarker(appView.configuration),
                createObfuscationMarker(appView.configuration)
            );

        // Mark the seeds.
        appView.programClassPool.accept(classPoolVisitor);
        appView.libraryClassPool.accept(classPoolVisitor);

        if (appView.configuration.keepKotlinMetadata)
        {
            // Keep the Kotlin metadata annotation.
            ClassVisitor classVisitor =
                new ClassNameFilter(KotlinConstants.NAME_KOTLIN_METADATA,
                new MultiClassVisitor(
                    new ProcessingFlagSetter(ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OPTIMIZE | ProcessingFlags.DONT_OBFUSCATE),
                    new AllMemberVisitor(
                        new ProcessingFlagSetter(ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OPTIMIZE | ProcessingFlags.DONT_OBFUSCATE))));
            appView.programClassPool.classesAccept(classVisitor);
            appView.libraryClassPool.classesAccept(classVisitor);
        }

        if (appView.configuration.keepKotlinMetadata)
        {
            disableOptimizationForKotlinFeatures(appView.programClassPool, appView.libraryClassPool);
        }
    }


    // Small utility methods.

    private ClassPoolVisitor createShrinkingMarker(Configuration configuration)
    {
        // Automatically mark the parameterless constructors of seed classes,
        // mainly for convenience and for backward compatibility.
        ProcessingFlagSetter marker =
            new ProcessingFlagSetter(ProcessingFlags.DONT_SHRINK);

        ClassVisitor classUsageMarker =
            new MultiClassVisitor(
                marker,
                new NamedMethodVisitor(ClassConstants.METHOD_NAME_INIT,
                                       ClassConstants.METHOD_TYPE_INIT,
                                       marker));

        // Create a visitor for marking the seeds.
        return new KeepClassSpecificationVisitorFactory(true, false, false)
            .createClassPoolVisitor(configuration.keep,
                                    classUsageMarker,
                                    marker);
    }


    private ClassPoolVisitor createOptimizationMarker(Configuration configuration)
    {
        ProcessingFlagSetter marker =
            new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE);

        // We'll also mark classes referenced from descriptors of class members
        // that can't be obfuscated. We're excluding enum classes at this time,
        // because they would always be marked, due to "SomeEnum[] values()".
        MemberVisitor descriptorClassMarker =
            new MemberDescriptorReferencedClassVisitor(
            new ClassAccessFilter(0, AccessConstants.ENUM,
            marker));

        return new MultiClassPoolVisitor(
            // Create a visitor for marking the seeds.
            new KeepClassSpecificationVisitorFactory(false, true, false)
                .createClassPoolVisitor(configuration.keep,
                                        marker, // marking classes
                                        marker, // marking fields
                                        marker, // marking methods
                                        marker),

            // Create a visitor for marking the classes referenced from
            // descriptors of obfuscation class member seeds, to avoid
            // merging such classes, to avoid having to rename these class
            // members.
            new KeepClassSpecificationVisitorFactory(false, false, true)
                .createClassPoolVisitor(configuration.keep,
                                        null,
                                        descriptorClassMarker, // for fields
                                        descriptorClassMarker, // for methods
                                        null)
        );
    }


    private ClassPoolVisitor createObfuscationMarker(Configuration configuration)
    {
        // We exclude injected classes from any user-defined pattern
        // that prevents obfuscation.
        ProcessingFlagSetter marker =
            new ProcessingFlagSetter(ProcessingFlags.DONT_OBFUSCATE);

        ClassVisitor classMarker =
            new ClassProcessingFlagFilter(0, ProcessingFlags.INJECTED,
            marker);

        MemberVisitor memberMarker =
            new MemberProcessingFlagFilter(0, INJECTED,
            marker);

        AttributeVisitor attributeMarker =
            new AttributeProcessingFlagFilter(0, INJECTED,
            marker);

        // Create a visitor for marking the seeds.
        return new KeepClassSpecificationVisitorFactory(false, false, true)
            .createClassPoolVisitor(configuration.keep,
                                    classMarker,
                                    memberMarker,
                                    memberMarker,
                                    attributeMarker);
    }


    /**
     * This method will disable optimization for all Kotlin components where required,
     * such as for $default methods.
     */
    private void disableOptimizationForKotlinFeatures(ClassPool programClassPool,
                                                      ClassPool libraryClassPool)
    {
        ClassVisitor classVisitor = new ReferencedKotlinMetadataVisitor(new KotlinDontOptimizeMarker());
        programClassPool.classesAccept(classVisitor);
        libraryClassPool.classesAccept(classVisitor);
    }


    /**
     * This KotlinMetadataVisitor marks classes and members with DONT_OPTIMIZE if it is known
     * that optimizing them would break Kotlin functionality.
     */
    public static class KotlinDontOptimizeMarker
    implements KotlinMetadataVisitor,
            KotlinFunctionVisitor
    {
        private static final MultiMemberVisitor MEMBER_AND_CLASS_MARKER =
            new MultiMemberVisitor(
                new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE),
                new MemberToClassVisitor(
                new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE)));

        // Implementations for KotlinMetadataVisitor.

        @Override
        public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}


        @Override
        public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                            KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
        {
            kotlinDeclarationContainerMetadata.functionsAccept(clazz, this);
        }


        @Override
        public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
        {
            visitKotlinDeclarationContainerMetadata(clazz, kotlinClassKindMetadata);

            // Mark the INSTANCE field for object classes.
            if (kotlinClassKindMetadata.flags.isObject)
            {
                clazz.fieldAccept(KotlinConstants.KOTLIN_OBJECT_INSTANCE_FIELD_NAME, null, MEMBER_AND_CLASS_MARKER);
            }

            // Mark default constructors - these are like $default methods,
            // they're marked by having an unused DefaultConstructorMarker parameter.
            clazz.methodsAccept(
                new MemberNameFilter(ClassConstants.METHOD_NAME_INIT,
                new AllParameterVisitor(false,
                (_clazz, member, parameterIndex, parameterCount, parameterOffset, parameterSize, parameterType, referencedClass) -> {
                    if (parameterType.equals(KotlinConstants.TYPE_KOTLIN_DEFAULT_CONSTRUCTOR_MARKER))
                    {
                        clazz.accept(new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE));
                        member.accept(_clazz, new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE));
                    }
                }))
            );

            // Mark the field that stores the companion object.
            if (kotlinClassKindMetadata.companionObjectName != null)
            {
                clazz.fieldAccept(kotlinClassKindMetadata.companionObjectName, null, MEMBER_AND_CLASS_MARKER);
            }
        }


        @Override
        public void visitKotlinSyntheticClassMetadata(Clazz                            clazz,
                                                      KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
        {
            // Mark all the invoke() methods in lambda classes.
            if (kotlinSyntheticClassKindMetadata.flavor == KotlinSyntheticClassKindMetadata.Flavor.LAMBDA)
            {
                kotlinSyntheticClassKindMetadata
                    .functionsAccept(clazz, new KotlinFunctionToMethodVisitor(MEMBER_AND_CLASS_MARKER));
            }

            // Mark all the code inside the public methods of CallableReferences.
            if (clazz.extendsOrImplements(KotlinConstants.REFLECTION.CALLABLE_REFERENCE_CLASS_NAME))
            {
                clazz.accept(
                    new MultiClassVisitor(
                        new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE),
                        new AllMemberVisitor(
                        new MemberAccessFilter(AccessConstants.PUBLIC, 0,
                        new MultiMemberVisitor(new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE),
                        new AllAttributeVisitor(
                        new AttributeNameFilter(Attribute.CODE,
                                                new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE))))))));
            }
        }


        // Implementations for KotlinFunctionVisitor.

        @Override
        public void visitAnyFunction(Clazz                  clazz,
                                     KotlinMetadata         kotlinMetadata,
                                     KotlinFunctionMetadata kotlinFunctionMetadata) {}


        @Override
        public void visitFunction(Clazz                              clazz,
                                  KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                  KotlinFunctionMetadata             kotlinFunctionMetadata)
        {
            // If there is a corresponding default method and the user specifically kept this method, mark it.
            if (hasAnyOf(kotlinFunctionMetadata.referencedMethod.getProcessingFlags(),
                                 DONT_OPTIMIZE, DONT_SHRINK, DONT_OBFUSCATE))
            {
                kotlinFunctionMetadata.accept(clazz, kotlinDeclarationContainerMetadata,
                                              new KotlinFunctionToDefaultMethodVisitor(MEMBER_AND_CLASS_MARKER));
            }

            // If a method with a default implementation is kept, don't optimize the default implementation.
            if (kotlinDeclarationContainerMetadata.k == KotlinConstants.METADATA_KIND_CLASS &&
                ((KotlinClassKindMetadata)kotlinDeclarationContainerMetadata).flags.isInterface &&
                !kotlinFunctionMetadata.flags.modality.isAbstract &&
                hasAnyOf(kotlinFunctionMetadata.referencedMethod.getProcessingFlags(),
                                 DONT_OPTIMIZE, DONT_SHRINK, DONT_OBFUSCATE))
            {
                kotlinFunctionMetadata.referencedDefaultImplementationMethod
                    .accept(kotlinFunctionMetadata.referencedDefaultImplementationMethodClass,
                            new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE));
                kotlinFunctionMetadata.referencedDefaultImplementationMethodClass
                    .accept(new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE));
            }
        }


        @Override
        public void visitSyntheticFunction(Clazz                            clazz,
                                           KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata,
                                           KotlinFunctionMetadata           kotlinFunctionMetadata)
        {
            // If there is a corresponding default method and the user specifically kept this method, mark it.
            if (hasAnyOf(kotlinFunctionMetadata.referencedMethod.getProcessingFlags(),
                                 DONT_OPTIMIZE, DONT_SHRINK, DONT_OBFUSCATE))
            {
                kotlinFunctionMetadata.accept(clazz, kotlinSyntheticClassKindMetadata,
                                              new KotlinFunctionToDefaultMethodVisitor(MEMBER_AND_CLASS_MARKER));
            }
        }
    }

    /**
     * Checks if any of the given integer bit flags are set in
     * the provided integer.
     */
    private static boolean hasAnyOf(int value, int ... flag)
    {
        return (value & Arrays.stream(flag).reduce((a, b) -> a | b).orElse(0)) != 0;
    }
}
