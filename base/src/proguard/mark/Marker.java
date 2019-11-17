/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 GuardSquare NV
 */
package proguard.mark;

import proguard.*;
import proguard.classfile.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.visitor.*;
import proguard.util.*;

import static proguard.util.ProcessingFlags.INJECTED;

/**
 * Translates the keep rules and encryption class specifications from the
 * configuration into processing flags on classes and class members.
 *
 * @author Johan Leys
 */
public class Marker
{
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
    public void mark(ClassPool programClassPool,
                     ClassPool libraryClassPool)
    {
        // Create a combined ClassPool visitor for marking classes.
        MultiClassPoolVisitor classPoolVisitor =
            new MultiClassPoolVisitor(
                createShrinkingMarker(),
                createOptimizationMarker(),
                createObfuscationMarker()
            );

        // Mark the seeds.
        programClassPool.accept(classPoolVisitor);
        libraryClassPool.accept(classPoolVisitor);
    }


    private ClassPoolVisitor createShrinkingMarker()
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


    private ClassPoolVisitor createOptimizationMarker()
    {
        ProcessingFlagSetter marker =
            new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE);

        // We'll also mark classes referenced from descriptors of class members
        // that can't be obfuscated. We're excluding enum classes at this time,
        // because they would always be marked, due to "SomeEnum[] values()".
        MemberVisitor descriptorClassMarker =
            new MemberDescriptorReferencedClassVisitor(
            new ClassAccessFilter(0, ClassConstants.ACC_ENUM,
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


    private ClassPoolVisitor createObfuscationMarker()
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
}
