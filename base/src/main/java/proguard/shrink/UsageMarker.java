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

import proguard.Configuration;
import proguard.classfile.ClassPool;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.kotlin.visitor.ReferencedKotlinMetadataVisitor;
import proguard.classfile.visitor.*;
import proguard.optimize.Optimizer;
import proguard.optimize.gson.*;
import proguard.resources.file.*;
import proguard.resources.file.visitor.*;
import proguard.util.*;

/**
 * Marks classes, resources, resource files and native libraries as being used,
 * starting from the given seeds (keep rules, Android Manifest).
 *
 * @author Johan Leys
 */
public class UsageMarker
{
    private final Configuration configuration;


    /**
     * Creates a new UsageMarker.
     */
    public UsageMarker(Configuration configuration)
    {
        this.configuration = configuration;
    }


    /**
     * Marks classes, resources, resource files and native libraries as being
     * used, based on the configuration.
     *
     * @param programClassPool  the program class pool.
     * @param libraryClassPool  the library class pool.
     * @param resourceFilePool  the resource file pool.
     * @param simpleUsageMarker the usage marker for marking any visitor
     *                          accepters.
     */
    public void mark(ClassPool         programClassPool,
                     ClassPool         libraryClassPool,
                     ResourceFilePool  resourceFilePool,
                     SimpleUsageMarker simpleUsageMarker)
    {
        mark(programClassPool,
             libraryClassPool,
             resourceFilePool,
             simpleUsageMarker,
             new ClassUsageMarker(simpleUsageMarker));
    }


    /**
     * Marks classes, resources, resource files and native libraries as being
     * used, based on the configuration.
     *
     * @param programClassPool  the program class pool.
     * @param libraryClassPool  the library class pool.
     * @param resourceFilePool  the resource file pool.
     * @param simpleUsageMarker the usage marker for marking any visitor
     *                          accepters.
     * @param classUsageMarker  the usage marker for recursively marking
     *                          classes.
     */
    public void mark(ClassPool         programClassPool,
                     ClassPool         libraryClassPool,
                     ResourceFilePool  resourceFilePool,
                     SimpleUsageMarker simpleUsageMarker,
                     ClassUsageMarker  classUsageMarker)
    {
        // Mark the seeds.
        libraryClassPool.classesAccept(classUsageMarker);

        // Mark classes that have to be kept.
        programClassPool.classesAccept(
            new MultiClassVisitor(
                new ClassProcessingFlagFilter(ProcessingFlags.DONT_SHRINK, 0,
                                              classUsageMarker),
                new AllMemberVisitor(
                new MemberProcessingFlagFilter(ProcessingFlags.DONT_SHRINK, 0,
                classUsageMarker))
            ));

        // Mark interfaces that have to be kept.
        // THis must be before the NestUsageMarker call right after, see https://github.com/Guardsquare/proguard/issues/501
        programClassPool.classesAccept(new InterfaceUsageMarker(classUsageMarker));


        // Mark the elements of Kotlin metadata that need to be kept.
        if (configuration.keepKotlinMetadata)
        {
            // Mark Kotlin things that refer to classes that have been marked as being kept now.
            programClassPool.classesAccept(
                new ReferencedKotlinMetadataVisitor(
                classUsageMarker));
        }
        // Mark the inner class and annotation information that has to be kept.
        programClassPool.classesAccept(
            new UsedClassFilter(simpleUsageMarker,
            new AllAttributeVisitor(true,
            new MultiAttributeVisitor(
                new InnerUsageMarker(classUsageMarker),
                new NestUsageMarker(classUsageMarker),
                new AnnotationUsageMarker(classUsageMarker),
                new LocalVariableTypeUsageMarker(classUsageMarker)
            ))));


        if (configuration.keepKotlinMetadata)
        {
            // Mark the used Kotlin modules.
            resourceFilePool.resourceFilesAccept(
                new ResourceFileNameFilter(KotlinConstants.MODULE.FILE_EXPRESSION,
                new ResourceFileProcessingFlagFilter(0, ProcessingFlags.DONT_PROCESS_KOTLIN_MODULE,
                new KotlinModuleUsageMarker(simpleUsageMarker))));
        }

        // Mark the record metadata.
        programClassPool.classesAccept(
            new AllAttributeVisitor(
            new AllRecordComponentInfoVisitor(
            new RecordComponentUsageMarker(classUsageMarker))));

        // Check if the Gson optimization is enabled.
        StringMatcher filter = configuration.optimizations != null ?
            new ListParser(new NameParser()).parse(configuration.optimizations) :
            new ConstantMatcher(true);
        boolean libraryGson = filter.matches(Optimizer.LIBRARY_GSON);

        if (configuration.optimize && libraryGson)
        {
            // Setup Gson context that represents how Gson is used in program
            // class pool.
            GsonContext gsonContext = new GsonContext();
            gsonContext.setupFor(programClassPool, libraryClassPool, null);

            // Mark domain classes and fields that are involved in GSON library
            // invocations.
            if (gsonContext.gsonRuntimeSettings.excludeFieldsWithModifiers)
            {
                // When fields are excluded based on modifier, we have to keep all
                // fields.
                gsonContext.gsonDomainClassPool.classesAccept(
                    new MultiClassVisitor(
                        classUsageMarker,
                        new AllFieldVisitor(classUsageMarker)));
            }
            else
            {
                // When fields are not excluded based on modifier, we can keep only
                // the fields for which we have injected (de)serialization code.
                gsonContext.gsonDomainClassPool.classesAccept(
                    new OptimizedJsonFieldVisitor(classUsageMarker,
                                                  classUsageMarker));
            }
        }
    }
}
