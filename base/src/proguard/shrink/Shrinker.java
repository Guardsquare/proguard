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
package proguard.shrink;

import proguard.*;
import proguard.classfile.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.kotlin.ReferencedKotlinMetadataVisitor;
import proguard.classfile.kotlin.asserter.KotlinMetadataAsserter;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.*;
import proguard.util.*;

import java.io.*;

/**
 * This class shrinks class pools according to a given configuration.
 *
 * @author Eric Lafortune
 */
public class Shrinker
{
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
    public ClassPool execute(ClassPool programClassPool,
                             ClassPool libraryClassPool) throws IOException
    {
        // Check if we have at least some keep commands.
        if (configuration.keep == null)
        {
            throw new IOException("You have to specify '-keep' options for the shrinking step.");
        }

        // We're using the system's default character encoding for writing to
        // the standard output.
        PrintWriter out = new PrintWriter(System.out, true);

        // Clean up any old visitor info.
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
                                            simpleUsageMarker,
                                            classUsageMarker);

        // Should we explain ourselves?
        if (configuration.whyAreYouKeeping != null)
        {
            out.println();

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

        if (configuration.adaptKotlinMetadata)
        {
            // Clean up Kotlin metadata.
            newProgramClassPool.classesAccept(
                new ReferencedKotlinMetadataVisitor(
                new KotlinShrinker(simpleUsageMarker)));

            if (configuration.enableKotlinAsserter)
            {
                WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(System.err, true));
                programClassPool.classesAccept(
                    new ReferencedKotlinMetadataVisitor(
                    new KotlinMetadataAsserter(programClassPool, libraryClassPool, warningPrinter)));
            }
        }

        int newProgramClassPoolSize = newProgramClassPool.size();

        // Collect some statistics.
        if (configuration.verbose)
        {
           ClassCounter originalClassCounter = new ClassCounter();
           programClassPool.classesAccept(
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
            (configuration.warn == null || !configuration.warn.isEmpty()))
        {
            if (configuration.ignoreWarnings)
            {
                System.err.println("Warning: the output jar is empty. Did you specify the proper '-keep' options?");
            }
            else
            {
                throw new IOException("The output jar is empty. Did you specify the proper '-keep' options?");
            }
        }

        return newProgramClassPool;
    }
}
