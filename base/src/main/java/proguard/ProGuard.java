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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.backport.Backporter;
import proguard.classfile.editor.ClassElementSorter;
import proguard.classfile.pass.PrimitiveArrayConstantIntroducer;
import proguard.classfile.util.PrimitiveArrayConstantReplacer;
import proguard.configuration.ConfigurationLoggingAdder;
import proguard.configuration.InitialStateInfo;
import proguard.evaluation.IncompleteClassHierarchyException;
import proguard.logging.Logging;
import proguard.mark.Marker;
import proguard.obfuscate.NameObfuscationReferenceFixer;
import proguard.obfuscate.ObfuscationPreparation;
import proguard.obfuscate.Obfuscator;
import proguard.obfuscate.ResourceFileNameAdapter;
import proguard.optimize.LineNumberTrimmer;
import proguard.optimize.Optimizer;
import proguard.optimize.gson.GsonOptimizer;
import proguard.optimize.peephole.LineNumberLinearizer;
import proguard.pass.PassRunner;
import proguard.preverify.PreverificationClearer;
import proguard.preverify.Preverifier;
import proguard.preverify.SubroutineInliner;
import proguard.shrink.Shrinker;
import proguard.strip.KotlinAnnotationStripper;
import proguard.util.ConstantMatcher;
import proguard.util.ListParser;
import proguard.util.NameParser;
import proguard.util.StringMatcher;
import proguard.util.kotlin.KotlinUnsupportedVersionChecker;
import proguard.util.kotlin.asserter.KotlinMetadataVerifier;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Tool for shrinking, optimizing, obfuscating, and preverifying Java classes.
 *
 * @author Eric Lafortune
 */
public class ProGuard
{
    private static final Logger logger = LogManager.getLogger(ProGuard.class);
    public static final String VERSION = "ProGuard, version " + getVersion();

    /**
     * A data object containing pass inputs in a centralized location. Passes can access and update the information
     * at any point in the pipeline.
     */
    private final AppView       appView;
    private final PassRunner    passRunner;
    private final Configuration configuration;

    /**
     * Creates a new ProGuard object to process jars as specified by the given
     * configuration.
     */
    public ProGuard(Configuration configuration)
    {
        this.appView       = new AppView();
        this.passRunner    = new PassRunner();
        this.configuration = configuration;
    }

    /**
     * Performs all subsequent ProGuard operations.
     */
    public void execute() throws Exception
    {
        Logging.configureVerbosity(configuration.verbose);

        logger.always().log(VERSION);

        try
        {
            checkGpl();

            // Set the -keepkotlinmetadata option if necessary.
            if (!configuration.dontProcessKotlinMetadata)
            {
                configuration.keepKotlinMetadata = requiresKotlinMetadata();
            }

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

            checkConfigurationAfterInitialization();

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
                shrink(false);
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

    private boolean requiresKotlinMetadata()
    {
        return configuration.keepKotlinMetadata ||
            (configuration.keep != null &&
                configuration.keep.stream().anyMatch(
                    keepClassSpecification -> ! keepClassSpecification.allowObfuscation &&
                                              ! keepClassSpecification.allowShrinking   &&
                                              "kotlin/Metadata".equals(keepClassSpecification.className)
                ));
    }


    /**
     * Prints out the configuration that ProGuard is using.
     */
    private void printConfiguration() throws IOException
    {
        try (ConfigurationWriter configurationWriter = new ConfigurationWriter(configuration.printConfiguration))
        {
            configurationWriter.write(configuration);
        }
    }


    /**
     * Checks the configuration for conflicts and inconsistencies.
     */
    private void checkConfiguration() throws IOException
    {
        new ConfigurationVerifier(configuration).check();
    }


    /**
     * Checks whether the output is up-to-date.
     */
    private void checkUpToDate()
    {
        new UpToDateChecker(configuration).check();
    }


    /**
     * Reads the input class files.
     */
    private void readInput() throws Exception
    {
        // Fill the program class pool and the library class pool.
        passRunner.run(new InputReader(configuration), appView);
    }


    /**
     * Clears any JSE preverification information from the program classes.
     */
    private void clearPreverification() throws Exception
    {
        passRunner.run(new PreverificationClearer(), appView);
    }


    /**
     * Initializes the cross-references between all classes, performs some
     * basic checks, and shrinks the library class pool.
     */
    private void initialize() throws Exception
    {
        if (configuration.keepKotlinMetadata)
        {
            passRunner.run(new KotlinUnsupportedVersionChecker(), appView);
        }
        passRunner.run(new Initializer(configuration), appView);

        if (configuration.keepKotlinMetadata &&
            configuration.enableKotlinAsserter)
        {
            passRunner.run(new KotlinMetadataVerifier(configuration), appView);
        }
    }


    /**
     * Marks the classes, class members and attributes to be kept or encrypted,
     * by setting the appropriate access flags.
     */
    private void mark() throws Exception
    {
        passRunner.run(new Marker(configuration), appView);
    }


    /**
     * Strips the Kotlin metadata annotation where possible.
     */
    private void stripKotlinMetadataAnnotations() throws Exception
    {
        passRunner.run(new KotlinAnnotationStripper(configuration), appView);
    }

    /**
     * Checks the configuration after it has been initialized.
     */
    private void checkConfigurationAfterInitialization() throws Exception
    {
        passRunner.run(new AfterInitConfigurationVerifier(configuration), appView);
    }

    /**
     * Replaces primitive array initialization code by primitive array constants.
     */
    private void introducePrimitiveArrayConstants() throws Exception
    {
        passRunner.run(new PrimitiveArrayConstantIntroducer(), appView);
    }


    /**
     * Backports java language features to the specified target version.
     */
    private void backport() throws Exception
    {
        passRunner.run(new Backporter(configuration), appView);
    }


    /**
     * Adds configuration logging code, providing suggestions on improving
     * the ProGuard configuration.
     */
    private void addConfigurationLogging() throws Exception
    {
        passRunner.run(new ConfigurationLoggingAdder(), appView);
    }


    /**
     * Prints out classes and class members that are used as seeds in the
     * shrinking and obfuscation steps.
     */
    private void printSeeds() throws Exception
    {
        passRunner.run(new SeedPrinter(configuration), appView);
    }


    /**
     * Performs the subroutine inlining step.
     */
    private void inlineSubroutines() throws Exception
    {
        // Perform the actual inlining.
        passRunner.run(new SubroutineInliner(configuration), appView);
    }


    /**
     * Performs the shrinking step.
     */
    private void shrink(boolean afterOptimizer) throws Exception
    {
        // Perform the actual shrinking.
        passRunner.run(new Shrinker(configuration, afterOptimizer), appView);

        if (configuration.keepKotlinMetadata &&
            configuration.enableKotlinAsserter)
        {
            passRunner.run(new KotlinMetadataVerifier(configuration), appView);
        }
    }


    /**
     * Optimizes usages of the Gson library.
     */
    private void optimizeGson() throws Exception
    {
        // Perform the Gson optimization.
        passRunner.run(new GsonOptimizer(configuration), appView);
    }


    /**
     * Performs the optimization step.
     */
    private void optimize() throws Exception
    {
        Optimizer optimizer = new Optimizer(configuration);

        for (int optimizationPass = 0; optimizationPass < configuration.optimizationPasses; optimizationPass++)
        {
            // Perform the actual optimization.
            passRunner.run(optimizer, appView);

            // Shrink again, if we may.
            if (configuration.shrink)
            {
                shrink(true);
            }
        }
    }


    /**
     * Disambiguates the line numbers of all program classes, after
     * optimizations like method inlining and class merging.
     */
    private void linearizeLineNumbers() throws Exception
    {
        passRunner.run(new LineNumberLinearizer(), appView);
    }


    /**
     * Performs the obfuscation step.
     */
    private void obfuscate() throws Exception
    {
        passRunner.run(new ObfuscationPreparation(configuration), appView);

        // Perform the actual obfuscation.
        passRunner.run(new Obfuscator(configuration), appView);

        // Adapt resource file names that correspond to class names, if necessary.
        if (configuration.adaptResourceFileNames != null)
        {
            passRunner.run(new ResourceFileNameAdapter(configuration), appView);
        }

        // Fix the Kotlin modules so the filename matches and the class names match.
        passRunner.run(new NameObfuscationReferenceFixer(configuration), appView);

        if (configuration.keepKotlinMetadata &&
            configuration.enableKotlinAsserter)
        {
            passRunner.run(new KotlinMetadataVerifier(configuration), appView);
        }
    }


    /**
     * Adapts Kotlin Metadata annotations.
     */
    private void adaptKotlinMetadata() throws Exception
    {
        passRunner.run(new KotlinMetadataAdapter(), appView);
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
    private void target() throws Exception
    {
        passRunner.run(new Targeter(configuration), appView);
    }


    /**
     * Performs the preverification step.
     */
    private void preverify() throws Exception
    {
        // Perform the actual preverification.
        passRunner.run(new Preverifier(configuration), appView);
    }


    /**
     * Trims the line number table attributes of all program classes.
     */
    private void trimLineNumbers() throws Exception
    {
        passRunner.run(new LineNumberTrimmer(), appView);
    }


    /**
     * Sorts the elements of all program classes.
     */
    private void sortClassElements()
    {
        appView.programClassPool.classesAccept(
            new ClassElementSorter(
                /* sortInterfaces = */ true,
                /* sortConstants = */ true,
                // Sorting members can cause problems with code such as clazz.getMethods()[1]
                /* sortMembers = */ false,
                // PGD-192: Sorting attributes can cause problems for some compilers
                /* sortAttributes = */ false
            )
        );
    }


    /**
     * Writes the output class files.
     */
    private void writeOutput() throws Exception
    {
        // Write out the program class pool.
        passRunner.run(new OutputWriter(configuration), appView);
    }


    /**
     * Prints out the contents of the program classes.
     */
    private void dump() throws Exception
    {
        passRunner.run(new Dumper(configuration), appView);
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
            logger.warn(VERSION);
            logger.warn("Usage: java proguard.ProGuard [options ...]");
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
            logger.error("Unexpected error", ex);

            System.exit(1);
        }

        System.exit(0);
    }
}
