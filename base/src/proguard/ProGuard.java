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
package proguard;

import proguard.backport.Backporter;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.io.kotlin.KotlinMetadataWriter;
import proguard.classfile.kotlin.visitor.ReferencedKotlinMetadataVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.configuration.ConfigurationLoggingAdder;
import proguard.io.ExtraDataEntryNameMap;
import proguard.mark.Marker;
import proguard.obfuscate.Obfuscator;
import proguard.optimize.Optimizer;
import proguard.optimize.gson.GsonOptimizer;
import proguard.optimize.peephole.LineNumberLinearizer;
import proguard.preverify.*;
import proguard.resources.file.ResourceFilePool;
import proguard.shrink.Shrinker;
import proguard.util.*;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Tool for shrinking, optimizing, obfuscating, and preverifying Java classes.
 *
 * @author Eric Lafortune
 */
public class ProGuard
{
    public static final String VERSION = "ProGuard, version " + getVersion();


    private final Configuration    configuration;

    private       ClassPool        programClassPool = new ClassPool();
    private final ClassPool        libraryClassPool = new ClassPool();
    private final ResourceFilePool resourceFilePool = new ResourceFilePool();

    // All injected data entries.
    private final ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();


    /**
     * Creates a new ProGuard object to process jars as specified by the given
     * configuration.
     */
    public ProGuard(Configuration configuration)
    {
        this.configuration = configuration;
    }


    /**
     * Performs all subsequent ProGuard operations.
     */
    public void execute() throws IOException
    {
        System.out.println(VERSION);

        GPL.check();

        if (configuration.printConfiguration != null)
        {
            printConfiguration();
        }

        new ConfigurationChecker(configuration).check();

        if (configuration.programJars != null     &&
            configuration.programJars.hasOutput() &&
            new UpToDateChecker(configuration).check())
        {
            return;
        }

        if (configuration.targetClassVersion != 0)
        {
            configuration.backport = true;
        }

        readInput();

        if (configuration.shrink    ||
            configuration.optimize  ||
            configuration.obfuscate ||
            configuration.preverify)
        {
            clearPreverification();
        }

        if (configuration.printSeeds != null        ||
            configuration.backport                  ||
            configuration.shrink                    ||
            configuration.optimize                  ||
            configuration.obfuscate                 ||
            configuration.preverify                 ||
            configuration.addConfigurationDebugging ||
            configuration.adaptKotlinMetadata)
        {
            initialize();
            mark();
        }

        if (configuration.optimize ||
            configuration.obfuscate)
        {
            introducePrimitiveArrayConstants();
        }

        if (configuration.backport)
        {
            backport();
        }

        if (configuration.addConfigurationDebugging)
        {
            addConfigurationLogging();
        }

        if (configuration.printSeeds != null)
        {
            printSeeds();
        }

        if (configuration.preverify ||
            configuration.android)
        {
            inlineSubroutines();
        }

        if (configuration.shrink)
        {
            shrink();
        }

        if (configuration.optimize)
        {
            optimizeGson();
        }

        if (configuration.optimize)
        {
            for (int optimizationPass = 0;
                 optimizationPass < configuration.optimizationPasses;
                 optimizationPass++)
            {
                if (!optimize(optimizationPass+1, configuration.optimizationPasses))
                {
                    // Stop optimizing if the code doesn't improve any further.
                    break;
                }

                // Shrink again, if we may.
                if (configuration.shrink)
                {
                    // Don't print any usage this time around.
                    configuration.printUsage       = null;
                    configuration.whyAreYouKeeping = null;

                    shrink();
                }
            }

            linearizeLineNumbers();
        }

        if (configuration.obfuscate)
        {
            obfuscate();
        }

        if (configuration.adaptKotlinMetadata)
        {
            adaptKotlinMetadata();
        }

        if (configuration.optimize ||
            configuration.obfuscate)
        {
            expandPrimitiveArrayConstants();
        }

        if (configuration.targetClassVersion != 0)
        {
            target();
        }

        if (configuration.preverify)
        {
            preverify();
        }

        // Trim line numbers after preverification as this might
        // also remove some instructions.
        if (configuration.optimize ||
            configuration.preverify)
        {
            trimLineNumbers();
        }

        if (configuration.shrink    ||
            configuration.optimize  ||
            configuration.obfuscate ||
            configuration.preverify)
        {
            sortClassElements();
        }

        if (configuration.programJars.hasOutput())
        {
            writeOutput();
        }

        if (configuration.dump != null)
        {
            dump();
        }
    }


    /**
     * Prints out the configuration that ProGuard is using.
     */
    private void printConfiguration() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Printing configuration to [" +
                               PrintWriterUtil.fileName(configuration.printConfiguration) +
                               "]...");
        }

        PrintWriter pw = PrintWriterUtil.createPrintWriterOut(configuration.printConfiguration);
        try
        {
            new ConfigurationWriter(pw).write(configuration);
        }
        finally
        {
            PrintWriterUtil.closePrintWriter(configuration.printConfiguration, pw);
        }
    }


    /**
     * Reads the input class files.
     */
    private void readInput() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Reading input...");
        }

        // Fill the program class pool and the library class pool.
        new InputReader(configuration).execute(programClassPool,
                                               libraryClassPool,
                                               resourceFilePool);
    }


    /**
     * Initializes the cross-references between all classes, performs some
     * basic checks, and shrinks the library class pool.
     */
    private void initialize() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Initializing...");
        }

        new Initializer(configuration).execute(programClassPool,
                                               libraryClassPool,
                                               resourceFilePool);
    }


    /**
     * Marks the classes, class members and attributes to be kept or encrypted,
     * by setting the appropriate access flags.
     */
    private void mark()
    {
        if (configuration.verbose)
        {
            System.out.println("Marking classes and class members to be kept...");
        }

        new Marker(configuration).mark(programClassPool,
                                       libraryClassPool);
    }


    /**
     * Replaces primitive array initialization code by primitive array constants.
     */
    private void introducePrimitiveArrayConstants()
    {
        programClassPool.classesAccept(new ArrayInitializationReplacer());
    }


    /**
     * Expands primitive array constants back to traditional primitive array
     * initialization code.
     */
    private void expandPrimitiveArrayConstants()
    {
        programClassPool.classesAccept(new PrimitiveArrayConstantReplacer());
    }


    /**
     * Backports java language features to the specified target version.
     */
    private void backport()
    {
        new Backporter(configuration).execute(programClassPool,
                                              libraryClassPool,
                                              extraDataEntryNameMap);
    }


    /**
     * Adds configuration logging code, providing suggestions on improving
     * the ProGuard configuration.
     */
    private void addConfigurationLogging()
    {
        new ConfigurationLoggingAdder(configuration).execute(programClassPool,
                                                             libraryClassPool,
                                                             extraDataEntryNameMap);
    }


    /**
     * Adapts Kotlin Metadata annotations.
     */
    private void adaptKotlinMetadata()
    {
        if (configuration.verbose)
        {
            System.out.println("Adapting Kotlin metadata...");
        }

        WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(System.out, true));

        ClassCounter counter = new ClassCounter();
        programClassPool.classesAccept(
            new ReferencedKotlinMetadataVisitor(
            new KotlinMetadataWriter(warningPrinter, counter)));

        if (configuration.verbose)
        {
            System.out.println("  Number of Kotlin classes adapted:              " + counter.getCount());
        }
    }


    /**
     * Sets that target versions of the program classes.
     */
    private void target() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Setting target versions...");
        }

        new Targeter(configuration).execute(programClassPool);
    }


    /**
     * Prints out classes and class members that are used as seeds in the
     * shrinking and obfuscation steps.
     */
    private void printSeeds() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Printing kept classes, fields, and methods...");
        }

        PrintWriter pw = PrintWriterUtil.createPrintWriterOut(configuration.printSeeds);
        try
        {
            new SeedPrinter(pw).write(configuration,
                                      programClassPool,
                                      libraryClassPool);
        }
        finally
        {
            PrintWriterUtil.closePrintWriter(configuration.printSeeds, pw);
        }
    }


    /**
     * Performs the shrinking step.
     */
    private void shrink() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Shrinking...");

            // We'll print out some explanation, if requested.
            if (configuration.whyAreYouKeeping != null)
            {
                System.out.println("Explaining why classes and class members are being kept...");
            }

            // We'll print out the usage, if requested.
            if (configuration.printUsage != null)
            {
                System.out.println("Printing usage to [" + PrintWriterUtil.fileName(configuration.printUsage) + "]...");
            }
        }

        // Perform the actual shrinking.
        programClassPool =
            new Shrinker(configuration).execute(programClassPool,
                                                libraryClassPool,
                                                resourceFilePool);
    }


    /**
     * Performs the subroutine inlining step.
     */
    private void inlineSubroutines()
    {
        if (configuration.verbose)
        {
            System.out.println("Inlining subroutines...");
        }

        // Perform the actual inlining.
        new SubroutineInliner(configuration).execute(programClassPool);
    }


    /**
     * Optimizes usages of the Gson library.
     */
    private void optimizeGson() throws IOException
    {
        // Do we have Gson code?
        // Is Gson optimization enabled?
        if (programClassPool.getClass("com/google/gson/Gson") != null &&
            (configuration.optimizations == null ||
             new ListParser(new NameParser()).parse(configuration.optimizations)
                 .matches(Optimizer.LIBRARY_GSON)))
        {
            if (configuration.verbose)
            {
                System.out.println("Optimizing usages of Gson library...");
            }

            // Perform the Gson optimization.
            new GsonOptimizer(configuration).execute(programClassPool,
                                                     libraryClassPool,
                                                     extraDataEntryNameMap);
        }
    }


    /**
     * Performs the optimization step.
     */
    private boolean optimize(int currentPass,
                             int maxPasses) throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Optimizing (pass " + currentPass + "/" + maxPasses + ")...");
        }

        // Perform the actual optimization.
        return new Optimizer(configuration).execute(programClassPool,
                                                    libraryClassPool,
                                                    extraDataEntryNameMap);
    }


    /**
     * Performs the obfuscation step.
     */
    private void obfuscate() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Obfuscating...");
        }

        // Perform the actual obfuscation.
        new Obfuscator(configuration).execute(programClassPool,
                                              libraryClassPool,
                                              resourceFilePool);
    }


    /**
     * Disambiguates the line numbers of all program classes, after
     * optimizations like method inlining and class merging.
     */
    private void linearizeLineNumbers()
    {
        programClassPool.classesAccept(new LineNumberLinearizer());
    }


    /**
     * Trims the line number table attributes of all program classes.
     */
    private void trimLineNumbers()
    {
        programClassPool.classesAccept(new AllAttributeVisitor(true,
                                       new LineNumberTableAttributeTrimmer()));
    }


    /**
     * Clears any JSE preverification information from the program classes.
     */
    private void clearPreverification()
    {
        programClassPool.classesAccept(
            new ClassVersionFilter(VersionConstants.CLASS_VERSION_1_6,
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new NamedAttributeDeleter(Attribute.STACK_MAP_TABLE)))));
    }


    /**
     * Performs the preverification step.
     */
    private void preverify()
    {
        if (configuration.verbose)
        {
            System.out.println("Preverifying...");
        }

        // Perform the actual preverification.
        new Preverifier(configuration).execute(programClassPool);
    }


    /**
     * Sorts the elements of all program classes.
     */
    private void sortClassElements()
    {
        programClassPool.classesAccept(new ClassElementSorter());
    }


    /**
     * Writes the output class files.
     */
    private void writeOutput() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Writing output...");
        }

        // Write out the program class pool.
        new OutputWriter(configuration).execute(programClassPool,
                                                resourceFilePool,
                                                extraDataEntryNameMap);
    }


    /**
     * Prints out the contents of the program classes.
     */
    private void dump() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Printing classes to [" + PrintWriterUtil.fileName(configuration.dump) + "]...");
        }

        PrintWriter pw = PrintWriterUtil.createPrintWriterOut(configuration.dump);
        try
        {
            programClassPool.classesAccept(new ClassPrinter(pw));
        }
        finally
        {
            PrintWriterUtil.closePrintWriter(configuration.dump, pw);
        }
    }


    /**
     * Returns the implementation version from the manifest.
     */
    public static String getVersion()
    {
        Package pack = ProGuard.class.getPackage();
        if (pack != null)
        {
            String version = pack.getImplementationVersion();
            if (version != null)
            {
                return version;
            }
        }

        return "undefined";
    }


    /**
     * The main method for ProGuard.
     */
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println(VERSION);
            System.out.println("Usage: java proguard.ProGuard [options ...]");
            System.exit(1);
        }

        // Create the default options.
        Configuration configuration = new Configuration();

        try
        {
            // Parse the options specified in the command line arguments.
            ConfigurationParser parser = new ConfigurationParser(args,
                                                                 System.getProperties());
            try
            {
                parser.parse(configuration);
            }
            finally
            {
                parser.close();
            }

            // Execute ProGuard with these options.
            new ProGuard(configuration).execute();
        }
        catch (Exception ex)
        {
            if (configuration.verbose)
            {
                // Print a verbose stack trace.
                ex.printStackTrace();
            }
            else
            {
                // Print just the stack trace message.
                System.err.println("Error: "+ex.getMessage());
            }

            System.exit(1);
        }

        System.exit(0);
    }
}
