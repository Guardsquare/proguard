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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.*;
import proguard.classfile.*;
import proguard.classfile.kotlin.visitor.ReferencedKotlinMetadataVisitor;
import proguard.classfile.util.WarningLogger;
import proguard.fixer.kotlin.KotlinAnnotationFlagFixer;
import proguard.resources.file.visitor.ResourceFileProcessingFlagFilter;
import proguard.util.kotlin.asserter.KotlinMetadataAsserter;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.*;
import proguard.resources.file.ResourceFilePool;
import proguard.util.*;

import java.io.*;

/**
 * This class shrinks class pools according to a given configuration.
 *
 * @author Eric Lafortune
 */
public class Shrinker
{
    private static final Logger logger = LogManager.getLogger(Shrinker.class);
    private final Configuration configuration;


    /**
     * Creates a new Shrinker.
     */
    public Shrinker(Configuration configuration)
    {
        this.configuration = configuration;
    }


    /**
     * Performs shrinking of the given program class pool.
     */
    public ClassPool execute(ClassPool        programClassPool,
                             ClassPool        libraryClassPool,
                             ResourceFilePool resourceFilePool) throws IOException
    {
        // Check if we have at least some keep commands.
        if (configuration.keep == null)
        {
            throw new IOException("You have to specify '-keep' options for the shrinking step.");
        }

        // We're using the system's default character encoding for writing to
        // the standard output.
        PrintWriter out = new PrintWriter(System.out, true);

        // Clean up any old processing info.
        programClassPool.classesAccept(new ClassCleaner());
        libraryClassPool.classesAccept(new ClassCleaner());

        // Create a visitor for marking the seeds.
        SimpleUsageMarker simpleUsageMarker = configuration.whyAreYouKeeping == null ?
            new SimpleUsageMarker() :
            new ShortestUsageMarker();

         // Create a usage marker for resources and code, tracing the reasons
         // if specified.
         ClassUsageMarker classUsageMarker = configuration.whyAreYouKeeping == null ?
             new ClassUsageMarker(simpleUsageMarker) :
             new ShortestClassUsageMarker((ShortestUsageMarker) simpleUsageMarker,
                                          "is kept by a directive in the configuration.\n\n");

        // Mark all used code and resources and resource files.
        new UsageMarker(configuration).mark(programClassPool,
                                            libraryClassPool,
                                            resourceFilePool,
                                            simpleUsageMarker,
                                            classUsageMarker);

        // Should we explain ourselves?
        if (configuration.whyAreYouKeeping != null)
        {

            // Create a visitor for explaining classes and class members.
            ShortestUsagePrinter shortestUsagePrinter =
                new ShortestUsagePrinter((ShortestUsageMarker)classUsageMarker.getUsageMarker(),
                                         configuration.verbose,
                                         out);

            ClassPoolVisitor whyClassPoolvisitor =
                new ClassSpecificationVisitorFactory()
                    .createClassPoolVisitor(configuration.whyAreYouKeeping,
                                            shortestUsagePrinter,
                                            shortestUsagePrinter);

            // Mark the seeds.
            programClassPool.accept(whyClassPoolvisitor);
            libraryClassPool.accept(whyClassPoolvisitor);
        }

        if (configuration.printUsage != null)
        {
            PrintWriter usageWriter =
                PrintWriterUtil.createPrintWriterOut(configuration.printUsage);

            try
            {
                // Print out items that will be removed.
                programClassPool.classesAcceptAlphabetically(
                    new UsagePrinter(simpleUsageMarker, true, usageWriter));
            }
            finally
            {
                PrintWriterUtil.closePrintWriter(configuration.printUsage,
                                                 usageWriter);
            }
        }

        // Clean up used program classes and discard unused program classes.
        ClassPool newProgramClassPool = new ClassPool();
        programClassPool.classesAccept(
            new UsedClassFilter(simpleUsageMarker,
            new MultiClassVisitor(
                new ClassShrinker(simpleUsageMarker),
                new ClassPoolFiller(newProgramClassPool)
            )));

        libraryClassPool.classesAccept(
            new UsedClassFilter(simpleUsageMarker,
            new ClassShrinker(simpleUsageMarker)));

        if (configuration.keepKotlinMetadata)
        {
            // Clean up Kotlin metadata for unused classes/members.
            newProgramClassPool.classesAccept(
                new ReferencedKotlinMetadataVisitor(
                new KotlinShrinker(simpleUsageMarker)));

            newProgramClassPool.classesAccept(
                new ReferencedKotlinMetadataVisitor(
                new KotlinAnnotationFlagFixer()));

            // Shrink the content of the Kotlin module files.
            resourceFilePool.resourceFilesAccept(
                new ResourceFileProcessingFlagFilter(0, ProcessingFlags.DONT_PROCESS_KOTLIN_MODULE,
                                                     new KotlinModuleShrinker(simpleUsageMarker)));

            if (configuration.enableKotlinAsserter)
            {
                WarningPrinter warningPrinter = new WarningLogger(logger, configuration.warn);
                new KotlinMetadataAsserter()
                    .execute(programClassPool,
                             libraryClassPool,
                             resourceFilePool,
                             warningPrinter);
            }
        }

        int newProgramClassPoolSize = newProgramClassPool.size();

        // Collect some statistics.
       ClassCounter originalClassCounter = new ClassCounter();
       programClassPool.classesAccept(
           new ClassProcessingFlagFilter(0, ProcessingFlags.INJECTED,
                                         originalClassCounter));

       ClassCounter newClassCounter = new ClassCounter();
       newProgramClassPool.classesAccept(
           new ClassProcessingFlagFilter(0, ProcessingFlags.INJECTED,
           newClassCounter));

        logger.info("Removing unused program classes and class elements...");
        logger.info("  Original number of program classes:            {}", originalClassCounter.getCount());
        logger.info("  Final number of program classes:               {}", newClassCounter.getCount());
        if (newClassCounter.getCount() != newProgramClassPoolSize)
        {
            logger.info("  Final number of program and injected classes:  {}", newProgramClassPoolSize);
        }

        // Check if we have at least some output classes.
        if (newProgramClassPoolSize == 0 &&
            (configuration.warn == null || !configuration.warn.isEmpty()))
        {
            if (configuration.ignoreWarnings)
            {
                logger.warn("Warning: the output jar is empty. Did you specify the proper '-keep' options?");
            }
            else
            {
                throw new IOException("The output jar is empty. Did you specify the proper '-keep' options?");
            }
        }

        return newProgramClassPool;
    }
}
