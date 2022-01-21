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

import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.io.*;
import proguard.pass.Pass;
import proguard.resources.file.*;
import proguard.resources.file.io.ResourceFileDataEntryReader;
import proguard.resources.file.visitor.*;
import proguard.resources.kotlinmodule.io.KotlinModuleDataEntryReader;
import proguard.util.*;

import java.io.*;

/**
 * This pass reads the input class files.
 *
 * @author Eric Lafortune
 */
public class InputReader implements Pass
{
    private final Configuration configuration;

    // Field that acts as a parameter to the visitors that attach
    // feature names to classes and resource files.
    private String featureName;

    /**
     * Creates a new InputReader to read input class files as specified by the
     * given configuration.
     */
    public InputReader(Configuration configuration)
    {
        this.configuration = configuration;
    }


    /**
     * Fills the program class pool, library class pool, and resource file
     * pool by reading files based on the current configuration.
     */
    @Override
    public void execute(AppView appView) throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Reading input...");
        }

        // We're using the system's default character encoding for writing to
        // the standard output and error output.
        PrintWriter out = new PrintWriter(System.out, true);
        PrintWriter err = new PrintWriter(System.err, true);

        WarningPrinter notePrinter    = new WarningPrinter(out, configuration.note);
        WarningPrinter warningPrinter = new WarningPrinter(err, configuration.warn);

        DuplicateClassPrinter        duplicateClassPrinter        = new DuplicateClassPrinter(notePrinter);
        DuplicateResourceFilePrinter duplicateResourceFilePrinter = new DuplicateResourceFilePrinter(notePrinter);

        ClassVisitor classPoolFiller =
            new ClassPresenceFilter(appView.programClassPool, duplicateClassPrinter,
            new MultiClassVisitor(
                new ClassPoolFiller(appView.programClassPool),
                // Attach the current resource name, if any, to any program classes that it visits.
                new ProgramClassFilter(clazz -> clazz.setFeatureName(featureName))));

        // Create a reader to fill the program class pool (while checking for
        // duplicates).
        DataEntryReader classReader =
            new ClassReader(false,
                            configuration.skipNonPublicLibraryClasses,
                            configuration.skipNonPublicLibraryClassMembers,
                            configuration.shrink   ||
                            configuration.optimize ||
                            configuration.obfuscate,
                            warningPrinter,
                            classPoolFiller);

        // Create a visitor that initializes the references from resource files
        // to Java classes.
        DataEntryNameFilter adaptedDataEntryFilter =
            configuration.adaptResourceFileContents != null ?
                new DataEntryNameFilter(
                new ListParser(
                new FileNameParser()).parse(configuration.adaptResourceFileContents)) :
                null;

        // Create a visitor and a reader to fill the resource file pool with
        // plain resource file instances (while checking for duplicates).
        ResourceFileVisitor resourceFilePoolFiller =
            new ResourceFilePresenceFilter(appView.resourceFilePool, duplicateResourceFilePrinter,
            new MultiResourceFileVisitor(
                new ResourceFilePoolFiller(appView.resourceFilePool),
                new MyResourceFileFeatureNameSetter()));

        DataEntryReader resourceReader =
            new ResourceFileDataEntryReader(resourceFilePoolFiller,
                                            adaptedDataEntryFilter);

        if (configuration.keepKotlinMetadata)
        {
            resourceReader =
                new NameFilteredDataEntryReader(KotlinConstants.MODULE.FILE_EXPRESSION,
                    new KotlinModuleDataEntryReader(resourceFilePoolFiller),
                    resourceReader);
        }

        // Read the program class files and resource files and put them in the
        // program class pool and resource file pool.
        readInput("Reading program ",
                  configuration.programJars,
                  new ClassFilter(classReader,
                                  resourceReader));

        // Check if we have at least some input classes.
        if (appView.programClassPool.size() == 0)
        {
            throw new IOException("The input doesn't contain any classes. Did you specify the proper '-injars' options?");
        }

        // Read the library class files, if any.
        if (configuration.libraryJars != null &&
            (configuration.printSeeds != null ||
             configuration.shrink    ||
             configuration.optimize  ||
             configuration.obfuscate ||
             configuration.preverify ||
             configuration.backport))
        {
            // Read the library class files and put then in the library class
            // pool.
            readInput("Reading library ",
                      configuration.libraryJars,
                      new ClassFilter(
                      new ClassReader(true,
                                      configuration.skipNonPublicLibraryClasses,
                                      configuration.skipNonPublicLibraryClassMembers,
                                      true,
                                      warningPrinter,
                      new ClassPresenceFilter(appView.programClassPool, duplicateClassPrinter,
                      new ClassPresenceFilter(appView.libraryClassPool, duplicateClassPrinter,
                      new ClassPoolFiller(appView.libraryClassPool))))));
        }

        // Print out a summary of the notes, if necessary.
        int noteCount = notePrinter.getWarningCount();
        if (noteCount > 0)
        {
            err.println("Note: there were " + noteCount +
                        " duplicate class definitions.");
            err.println("      (https://www.guardsquare.com/proguard/manual/troubleshooting#duplicateclass)");
        }

        // Print out a summary of the warnings, if necessary.
        int warningCount = warningPrinter.getWarningCount();
        if (warningCount > 0)
        {
            err.println("Warning: there were " + warningCount +
                        " classes in incorrectly named files.");
            err.println("         You should make sure all file names correspond to their class names.");
            err.println("         The directory hierarchies must correspond to the package hierarchies.");
            err.println("         (https://www.guardsquare.com/proguard/manual/troubleshooting#unexpectedclass)");

            if (!configuration.ignoreWarnings)
            {
                err.println("         If you don't mind the mentioned classes not being written out,");
                err.println("         you could try your luck using the '-ignorewarnings' option.");
                throw new IOException("Please correct the above warnings first.");
            }
        }
    }


    /**
     * Reads all input entries from the given class path.
     */
    private void readInput(String          messagePrefix,
                           ClassPath       classPath,
                           DataEntryReader reader)
    throws IOException
    {
        readInput(messagePrefix,
                  classPath,
                  0,
                  classPath.size(),
                  reader);
    }


    /**
     * Reads all input entries from the given section of the given class path.
     */
    public void readInput(String          messagePrefix,
                          ClassPath       classPath,
                          int             fromIndex,
                          int             toIndex,
                          DataEntryReader reader)
    throws IOException
    {
        for (int index = fromIndex; index < toIndex; index++)
        {
            ClassPathEntry entry = classPath.get(index);
            if (!entry.isOutput())
            {
                readInput(messagePrefix, entry, reader);
            }
        }
    }


    /**
     * Reads the given input class path entry.
     */
    private void readInput(String          messagePrefix,
                           ClassPathEntry  classPathEntry,
                           DataEntryReader dataEntryReader)
    throws IOException
    {
        try
        {
            // Create a reader that can unwrap jars, wars, ears, jmods and zips.
            DataEntryReader reader =
                new DataEntryReaderFactory(configuration.android, configuration.verbose)
                    .createDataEntryReader(messagePrefix,
                                           classPathEntry,
                                           dataEntryReader);

            // Create the data entry source.
            DataEntrySource source =
                new DirectorySource(classPathEntry.getFile());

            // Set he feature name for the class files and resource files
            // that we'll read.
            featureName = classPathEntry.getFeatureName();

            // Pump the data entries into the reader.
            source.pumpDataEntries(reader);
        }
        catch (IOException ex)
        {
            throw new IOException("Can't read [" + classPathEntry + "] (" + ex.getMessage() + ")", ex);
        }
    }


    /**
     * This resource file visitor attaches the current resource name, if any,
     * to any resource files that it visits.
     */
    private class MyResourceFileFeatureNameSetter
    implements    ResourceFileVisitor
    {
        // Implementations for ResourceFileVisitor.

        public void visitAnyResourceFile(ResourceFile resourceFile)
        {
            resourceFile.setFeatureName(featureName);
        }
    }
}
