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
package proguard.mark;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.AppView;
import proguard.Configuration;
import proguard.KeepClassSpecificationVisitorFactory;
import proguard.classfile.AccessConstants;
import proguard.classfile.ClassConstants;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.attribute.visitor.AttributeNameFilter;
import proguard.classfile.attribute.visitor.AttributeProcessingFlagFilter;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.visitor.KotlinFunctionToDefaultMethodVisitor;
import proguard.classfile.kotlin.visitor.KotlinFunctionToMethodVisitor;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.kotlin.visitor.ReferencedKotlinMetadataVisitor;
import proguard.classfile.util.AllParameterVisitor;
import proguard.classfile.visitor.AllMemberVisitor;
import proguard.classfile.visitor.ClassAccessFilter;
import proguard.classfile.visitor.ClassNameFilter;
import proguard.classfile.visitor.ClassPoolVisitor;
import proguard.classfile.visitor.ClassProcessingFlagFilter;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberAccessFilter;
import proguard.classfile.visitor.MemberDescriptorReferencedClassVisitor;
import proguard.classfile.visitor.MemberNameFilter;
import proguard.classfile.visitor.MemberProcessingFlagFilter;
import proguard.classfile.visitor.MemberToClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.classfile.visitor.MultiClassPoolVisitor;
import proguard.classfile.visitor.MultiClassVisitor;
import proguard.classfile.visitor.MultiMemberVisitor;
import proguard.classfile.visitor.NamedMethodVisitor;
import proguard.pass.Pass;
import proguard.util.ProcessingFlagSetter;
import proguard.util.ProcessingFlags;

import java.util.Arrays;

import static proguard.util.ProcessingFlags.DONT_OBFUSCATE;
import static proguard.util.ProcessingFlags.DONT_OPTIMIZE;
import static proguard.util.ProcessingFlags.DONT_SHRINK;
import static proguard.util.ProcessingFlags.INJECTED;

/**
 * This pass translates the keep rules and other class specifications from the
 * configuration into processing flags on classes and class members.
 *
 * @author Johan Leys
 */
public class Marker implements Pass
{
    private static final Logger logger = LogManager.getLogger(Marker.class);

    private final Configuration configuration;

    public Marker(Configuration configuration)
    {
        this.configuration = configuration;
    }


    /**
     * Marks classes and class members in the given class pools with
     * appropriate access flags, corresponding to the class specifications
     * in the configuration.
     */
    @Override
    public void execute(AppView appView)
    {
        logger.info("Marking classes and class members to be kept...");

        // Create a combined ClassPool visitor for marking classes.
        MultiClassPoolVisitor classPoolVisitor =
            new MultiClassPoolVisitor(
                createShrinkingMarker(configuration),
                createOptimizationMarker(configuration),
                createObfuscationMarker(configuration)
            );

        // Mark the seeds.
        appView.programClassPool.accept(classPoolVisitor);
        appView.libraryClassPool.accept(classPoolVisitor);

        if (configuration.keepKotlinMetadata)
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

        // Mark members that can be safely used for generalization,
        // but only if optimization is enabled.
        if (configuration.optimize)
        {
            markSafeGeneralizationMembers(appView.programClassPool, appView.libraryClassPool);
        }

        if (configuration.keepKotlinMetadata)
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


    private void markSafeGeneralizationMembers(ClassPool programClassPool,
                                               ClassPool libraryClassPool)
    {
        // Program classes are always available and safe to generalize/specialize from/to.
        ClassVisitor isClassAvailableMarker =
                new ProcessingFlagSetter(ProcessingFlags.IS_CLASS_AVAILABLE);

        programClassPool.classesAccept(isClassAvailableMarker);

        if (!configuration.optimizeConservatively)
        {
            libraryClassPool.classesAccept(isClassAvailableMarker);
        }
        // TODO: Mark library class members where appropriate in the conservative case.
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
                kotlinClassKindMetadata.referencedCompanionFieldAccept(MEMBER_AND_CLASS_MARKER);
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
/*              TODO: use this when referencedDefaultImplementationMethodAccept is available in ProGuardCORE
                kotlinFunctionMetadata.referencedDefaultImplementationMethodAccept(
                    new MultiMemberVisitor(
                        new ProcessingFlagSetter(DONT_OPTIMIZE),
                        new MemberToClassVisitor(new ProcessingFlagSetter(DONT_OPTIMIZE))
                    )
                );*/
                if (kotlinFunctionMetadata.referencedDefaultImplementationMethod      != null &&
                    kotlinFunctionMetadata.referencedDefaultImplementationMethodClass != null)
                {
                    kotlinFunctionMetadata.referencedDefaultImplementationMethod
                        .accept(kotlinFunctionMetadata.referencedDefaultImplementationMethodClass,
                                new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE));
                    kotlinFunctionMetadata.referencedDefaultImplementationMethodClass
                        .accept(new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE));
                }
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
