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
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.visitor.*;
import proguard.util.*;

import static proguard.util.ProcessingFlags.INJECTED;

/**
 * Translates the keep rules and other class specifications from the
 * configuration into processing flags on classes and class members.
 *
 * @author Johan Leys
 */
public class Marker
{
    private final Configuration configuration;


    /**
     * Creates a new Marker for the given configuration.
     */
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


    // Small utility methods.

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
