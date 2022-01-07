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

import proguard.*;
import proguard.classfile.*;
import proguard.classfile.kotlin.visitor.ReferencedKotlinMetadataVisitor;
import proguard.fixer.kotlin.KotlinAnnotationFlagFixer;
import proguard.resources.file.visitor.ResourceFileProcessingFlagFilter;
import proguard.classfile.visitor.*;
import proguard.pass.Pass;
import proguard.util.*;

import java.io.*;

/**
 * This pass shrinks class pools according to a given configuration.
 *
 * @author Eric Lafortune
 */
public class Shrinker implements Pass
{
    /**
     * Performs shrinking of the given program class pool.
     */
    @Override
    public void execute(AppView appView) throws IOException
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Shrinking...");

            // We'll print out some explanation, if requested.
            if (appView.configuration.whyAreYouKeeping != null)
            {
                System.out.println("Explaining why classes and class members are being kept...");
            }

            // We'll print out the usage, if requested.
            if (appView.configuration.printUsage != null)
            {
                System.out.println("Printing usage to [" + PrintWriterUtil.fileName(appView.configuration.printUsage) + "]...");
            }
        }

        // Check if we have at least some keep commands.
        if (appView.configuration.keep == null)
        {
            throw new IOException("You have to specify '-keep' options for the shrinking step.");
        }

        // We're using the system's default character encoding for writing to
        // the standard output.
        PrintWriter out = new PrintWriter(System.out, true);

        // Clean up any old processing info.
        appView.programClassPool.classesAccept(new ClassCleaner());
        appView.libraryClassPool.classesAccept(new ClassCleaner());

        // Create a visitor for marking the seeds.
        SimpleUsageMarker simpleUsageMarker = appView.configuration.whyAreYouKeeping == null ?
            new SimpleUsageMarker() :
            new ShortestUsageMarker();

         // Create a usage marker for resources and code, tracing the reasons
         // if specified.
         ClassUsageMarker classUsageMarker = appView.configuration.whyAreYouKeeping == null ?
             new ClassUsageMarker(simpleUsageMarker) :
             new ShortestClassUsageMarker((ShortestUsageMarker) simpleUsageMarker,
                                          "is kept by a directive in the configuration.\n\n");

        // Mark all used code and resources and resource files.
        new UsageMarker(appView.configuration).mark(appView.programClassPool,
                                                    appView.libraryClassPool,
                                                    appView.resourceFilePool,
                                                    simpleUsageMarker,
                                                    classUsageMarker);

        // Should we explain ourselves?
        if (appView.configuration.whyAreYouKeeping != null)
        {
            out.println();

            // Create a visitor for explaining classes and class members.
            ShortestUsagePrinter shortestUsagePrinter =
                new ShortestUsagePrinter((ShortestUsageMarker)classUsageMarker.getUsageMarker(),
                                         appView.configuration.verbose,
                                         out);

            ClassPoolVisitor whyClassPoolvisitor =
                new ClassSpecificationVisitorFactory()
                    .createClassPoolVisitor(appView.configuration.whyAreYouKeeping,
                                            shortestUsagePrinter,
                                            shortestUsagePrinter);

            // Mark the seeds.
            appView.programClassPool.accept(whyClassPoolvisitor);
            appView.libraryClassPool.accept(whyClassPoolvisitor);
        }

        if (appView.configuration.printUsage != null)
        {
            PrintWriter usageWriter =
                PrintWriterUtil.createPrintWriterOut(appView.configuration.printUsage);

            try
            {
                // Print out items that will be removed.
                appView.programClassPool.classesAcceptAlphabetically(
                    new UsagePrinter(simpleUsageMarker, true, usageWriter));
            }
            finally
            {
                PrintWriterUtil.closePrintWriter(appView.configuration.printUsage,
                                                 usageWriter);
            }
        }

        // Clean up used program classes and discard unused program classes.
        ClassPool newProgramClassPool = new ClassPool();
        appView.programClassPool.classesAccept(
            new UsedClassFilter(simpleUsageMarker,
            new MultiClassVisitor(
                new ClassShrinker(simpleUsageMarker),
                new ClassPoolFiller(newProgramClassPool)
            )));

        appView.libraryClassPool.classesAccept(
            new UsedClassFilter(simpleUsageMarker,
            new ClassShrinker(simpleUsageMarker)));

        if (appView.configuration.keepKotlinMetadata)
        {
            // Clean up Kotlin metadata for unused classes/members.
            newProgramClassPool.classesAccept(
                new ReferencedKotlinMetadataVisitor(
                new KotlinShrinker(simpleUsageMarker)));

            newProgramClassPool.classesAccept(
                new ReferencedKotlinMetadataVisitor(
                new KotlinAnnotationFlagFixer()));

            // Shrink the content of the Kotlin module files.
            appView.resourceFilePool.resourceFilesAccept(
                new ResourceFileProcessingFlagFilter(0, ProcessingFlags.DONT_PROCESS_KOTLIN_MODULE,
                                                     new KotlinModuleShrinker(simpleUsageMarker)));
        }

        int newProgramClassPoolSize = newProgramClassPool.size();

        // Collect some statistics.
        if (appView.configuration.verbose)
        {
           ClassCounter originalClassCounter = new ClassCounter();
            appView.programClassPool.classesAccept(
               new ClassProcessingFlagFilter(0, ProcessingFlags.INJECTED,
                                             originalClassCounter));

           ClassCounter newClassCounter = new ClassCounter();
           newProgramClassPool.classesAccept(
               new ClassProcessingFlagFilter(0, ProcessingFlags.INJECTED,
               newClassCounter));

            out.println("Removing unused program classes and class elements...");
            out.println("  Original number of program classes:            " + originalClassCounter.getCount());
            out.println("  Final number of program classes:               " + newClassCounter.getCount());
            if (newClassCounter.getCount() != newProgramClassPoolSize)
            {
                out.println("  Final number of program and injected classes:  " + newProgramClassPoolSize);
            }
        }

        // Check if we have at least some output classes.
        if (newProgramClassPoolSize == 0 &&
            (appView.configuration.warn == null || !appView.configuration.warn.isEmpty()))
        {
            if (appView.configuration.ignoreWarnings)
            {
                System.err.println("Warning: the output jar is empty. Did you specify the proper '-keep' options?");
            }
            else
            {
                throw new IOException("The output jar is empty. Did you specify the proper '-keep' options?");
            }
        }

        appView.programClassPool.clear();
        newProgramClassPool.classesAccept(new ClassPoolFiller(appView.programClassPool));
    }
}
