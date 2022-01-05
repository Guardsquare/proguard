/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
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
import proguard.classfile.editor.*;
import proguard.classfile.pass.PrimitiveArrayConstantIntroducer;
import proguard.classfile.util.*;
import proguard.configuration.ConfigurationLoggingAdder;
import proguard.evaluation.IncompleteClassHierarchyException;
import proguard.configuration.InitialStateInfo;
import proguard.mark.Marker;
import proguard.obfuscate.ObfuscationPreparation;
import proguard.obfuscate.Obfuscator;
import proguard.optimize.LineNumberTrimmer;
import proguard.optimize.Optimizer;
import proguard.optimize.gson.GsonOptimizer;
import proguard.optimize.peephole.LineNumberLinearizer;
import proguard.preverify.*;
import proguard.shrink.Shrinker;
import proguard.strip.KotlinAnnotationStripper;
import proguard.util.*;

import java.io.*;

/**
 * Tool for shrinking, optimizing, obfuscating, and preverifying Java classes.
 *
 * @author Eric Lafortune
 */
public class ProGuard
{
    public static final String VERSION = "ProGuard, version " + getVersion();

    /**
     * A data object containing pass inputs in a centralized location. Passes can access and update the information
     * at any point in the pipeline.
     */
    private final AppView appView;

    /**
     * Creates a new ProGuard object to process jars as specified by the given
     * configuration.
     */
    public ProGuard(Configuration configuration)
    {
        this.appView = new AppView(configuration);
    }

    /**
     * Performs all subsequent ProGuard operations.
     */
    public void execute() throws Exception
    {
        System.out.println(VERSION);

        Configuration configuration = appView.configuration;

        try
        {
            checkGpl();

            if (configuration.printConfiguration != null)
            {
                printConfiguration();
            }

            checkConfiguration();

            if (configuration.programJars.hasOutput())
            {
                checkUpToDate();
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
                configuration.keepKotlinMetadata)
            {
                initialize();
                mark();
            }

            if (configuration.addConfigurationDebugging)
            {
                // Remember the initial state of the program classpool and resource filepool
                // before shrinking / obfuscation / optimization.
                appView.initialStateInfo = new InitialStateInfo(appView.programClassPool);
            }

            if (configuration.keepKotlinMetadata)
            {
                stripKotlinMetadataAnnotations();
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

            // Create a matcher for filtering optimizations.
            StringMatcher filter = configuration.optimizations != null ?
                new ListParser(new NameParser()).parse(configuration.optimizations) :
                new ConstantMatcher(true);

            if (configuration.optimize &&
                filter.matches(Optimizer.LIBRARY_GSON))
            {
                optimizeGson();
            }

            if (configuration.optimize)
            {
                optimize();
                linearizeLineNumbers();
            }

            if (configuration.obfuscate)
            {
                obfuscate();
            }

            if (configuration.keepKotlinMetadata)
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
        catch (UpToDateChecker.UpToDateException ignore) {}
        catch (IncompleteClassHierarchyException e)
        {
            throw new RuntimeException(
                System.lineSeparator() + System.lineSeparator() +
                "It appears you are missing some classes resulting in an incomplete class hierarchy, " + System.lineSeparator() +
                "please refer to the troubleshooting page in the manual: " + System.lineSeparator() +
                "https://www.guardsquare.com/en/products/proguard/manual/troubleshooting#superclass" + System.lineSeparator()
            );
        }
    }


    /**
     * Checks the GPL.
     */
    private void checkGpl()
    {
        GPL.check();
    }


    /**
     * Prints out the configuration that ProGuard is using.
     */
    private void printConfiguration() throws IOException
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Printing configuration to [" +
                               PrintWriterUtil.fileName(appView.configuration.printConfiguration) +
                               "]...");
        }

        PrintWriter pw = PrintWriterUtil.createPrintWriterOut(appView.configuration.printConfiguration);

        try (ConfigurationWriter configurationWriter = new ConfigurationWriter(pw))
        {
            configurationWriter.write(appView.configuration);
        }
    }

    /**
     * Checks the configuration for conflicts and inconsistencies.
     */
    private void checkConfiguration() throws IOException
    {
        new ConfigurationChecker(appView.configuration).check();
    }

    /**
     * Checks whether the output is up-to-date.
     */
    private void checkUpToDate()
    {
        new UpToDateChecker(appView.configuration).check();
    }



    /**
     * Reads the input class files.
     */
    private void readInput() throws IOException
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Reading input...");
        }

        // Fill the program class pool and the library class pool.
        new InputReader(appView.configuration).execute(appView);
    }


    /**
     * Clears any JSE preverification information from the program classes.
     */
    private void clearPreverification()
    {
        new PreverificationClearer().execute(appView);
    }


    /**
     * Initializes the cross-references between all classes, performs some
     * basic checks, and shrinks the library class pool.
     */
    private void initialize() throws IOException
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Initializing...");
        }

        new Initializer().execute(appView);
    }


    /**
     * Marks the classes, class members and attributes to be kept or encrypted,
     * by setting the appropriate access flags.
     */
    private void mark()
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Marking classes and class members to be kept...");
        }

        new Marker().execute(appView);
    }


    /**
     * Strips the Kotlin metadata annotation where possible.
     */
    private void stripKotlinMetadataAnnotations()
    {
        new KotlinAnnotationStripper().execute(appView);
    }


    /**
     * Replaces primitive array initialization code by primitive array constants.
     */
    private void introducePrimitiveArrayConstants()
    {
        new PrimitiveArrayConstantIntroducer().execute(appView);
    }


    /**
     * Backports java language features to the specified target version.
     */
    private void backport() throws Exception
    {
        new Backporter().execute(appView);
    }


    /**
     * Adds configuration logging code, providing suggestions on improving
     * the ProGuard configuration.
     */
    private void addConfigurationLogging() throws IOException
    {
        new ConfigurationLoggingAdder().execute(appView);
    }


    /**
     * Prints out classes and class members that are used as seeds in the
     * shrinking and obfuscation steps.
     */
    private void printSeeds() throws IOException
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Printing kept classes, fields, and methods...");
        }

        new SeedPrinter().execute(appView);
    }


    /**
     * Performs the subroutine inlining step.
     */
    private void inlineSubroutines()
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Inlining subroutines...");
        }

        // Perform the actual inlining.
        new SubroutineInliner().execute(appView);
    }


    /**
     * Performs the shrinking step.
     */
    private void shrink() throws IOException
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

        // Perform the actual shrinking.
        new Shrinker().execute(appView);
    }


    /**
     * Optimizes usages of the Gson library.
     */
    private void optimizeGson() throws IOException
    {
        // Do we have Gson code?
        // Is Gson optimization enabled?
        if (appView.programClassPool.getClass("com/google/gson/Gson") != null)
        {
            if (appView.configuration.verbose)
            {
                System.out.println("Optimizing usages of Gson library...");
            }

            // Perform the Gson optimization.
            new GsonOptimizer().execute(appView);
        }
    }


    /**
     * Performs the optimization step.
     */
    private void optimize() throws IOException
    {
        Optimizer optimizer = new Optimizer();

        for (int optimizationPass = 0; optimizationPass < appView.configuration.optimizationPasses; optimizationPass++)
        {
            if (appView.configuration.verbose)
            {
                System.out.println("Optimizing (pass " + (optimizationPass + 1) + "/" + appView.configuration.optimizationPasses + ")...");
            }

            // Perform the actual optimization.
            optimizer.execute(appView);

            // Shrink again, if we may.
            if (appView.configuration.shrink)
            {
                shrink();
            }
        }
    }


    /**
     * Disambiguates the line numbers of all program classes, after
     * optimizations like method inlining and class merging.
     */
    private void linearizeLineNumbers()
    {
        new LineNumberLinearizer().execute(appView);
    }


    /**
     * Performs the obfuscation step.
     */
    private void obfuscate() throws IOException
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Obfuscating...");
        }

        new ObfuscationPreparation().execute(appView);

        // Perform the actual obfuscation.
        new Obfuscator().execute(appView);
    }


    /**
     * Adapts Kotlin Metadata annotations.
     */
    private void adaptKotlinMetadata()
    {
        new KotlinMetadataAdapter().execute(appView);
    }


    /**
     * Expands primitive array constants back to traditional primitive array
     * initialization code.
     */
    private void expandPrimitiveArrayConstants()
    {
        appView.programClassPool.classesAccept(new PrimitiveArrayConstantReplacer());
    }


    /**
     * Sets that target versions of the program classes.
     */
    private void target() throws IOException
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Setting target versions...");
        }

        new Targeter().execute(appView);
    }


    /**
     * Performs the preverification step.
     */
    private void preverify()
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Preverifying...");
        }

        // Perform the actual preverification.
        new Preverifier().execute(appView);
    }


    /**
     * Trims the line number table attributes of all program classes.
     */
    private void trimLineNumbers()
    {
        new LineNumberTrimmer().execute(appView);
    }


    /**
     * Sorts the elements of all program classes.
     */
    private void sortClassElements()
    {
        appView.programClassPool.classesAccept(new ClassElementSorter());
    }


    /**
     * Writes the output class files.
     */
    private void writeOutput() throws IOException
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Writing output...");
        }

        // Write out the program class pool.
        new OutputWriter().execute(appView);
    }


    /**
     * Prints out the contents of the program classes.
     */
    private void dump() throws Exception
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Printing classes to [" + PrintWriterUtil.fileName(appView.configuration.dump) + "]...");
        }

        new Dumper().execute(appView);
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
            try (ConfigurationParser parser = new ConfigurationParser(args, System.getProperties()))
            {
                parser.parse(configuration);
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
