/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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
import org.jetbrains.annotations.Nullable;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static proguard.DataEntryReaderFactory.getFilterExcludingVersionedClasses;

/**
 * This pass reads the input class files.
 *
 * @author Eric Lafortune
 */
public class InputReader implements Pass
{
    private static final Logger logger = LogManager.getLogger(InputReader.class);

    private static final boolean DONT_READ_LIBRARY_KOTLIN_METADATA = System.getProperty("proguard.dontreadlibrarykotlinmetadata") != null;


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
        logger.info("Reading input...");

        WarningPrinter notePrinter    = new WarningLogger(logger, configuration.note);
        WarningPrinter warningPrinter = new WarningLogger(logger, configuration.warn);

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
                            configuration.keepKotlinMetadata,
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
                                      !DONT_READ_LIBRARY_KOTLIN_METADATA && configuration.keepKotlinMetadata,
                                      warningPrinter,
                      new ClassPresenceFilter(appView.programClassPool, duplicateClassPrinter,
                      new ClassPresenceFilter(appView.libraryClassPool, duplicateClassPrinter,
                      new ClassPoolFiller(appView.libraryClassPool))))));
        }

        // Print out a summary of the notes, if necessary.
        int noteCount = notePrinter.getWarningCount();
        if (noteCount > 0)
        {
            logger.warn("Note: there were {} duplicate class definitions.", noteCount);
            logger.warn("      (https://www.guardsquare.com/proguard/manual/troubleshooting#duplicateclass)");
        }

        // Print out a summary of the warnings, if necessary.
        int warningCount = warningPrinter.getWarningCount();
        if (warningCount > 0)
        {
            logger.warn("Warning: there were {} classes in incorrectly named files.", warningCount);
            logger.warn("         You should make sure all file names correspond to their class names.");
            logger.warn("         The directory hierarchies must correspond to the package hierarchies.");
            logger.warn("         (https://www.guardsquare.com/proguard/manual/troubleshooting#unexpectedclass)");

            if (!configuration.ignoreWarnings)
            {
                logger.warn("         If you don't mind the mentioned classes not being written out,");
                logger.warn("         you could try your luck using the '-ignorewarnings' option.");
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

    // TODO: For debugging
    public static void main(String[] args) throws IOException {
        Set<String> withJmods = new HashSet<>();
        File file = new File("jdk-25-with-jmods/jmods");
        new InputReader(new Configuration()).readInput("message", new ClassPathEntry(file, false), dataEntry -> withJmods.add(dataEntry.getName()));

        file = new File("jdk-25-without-jmods/jmods");
        Set<String> withoutJmods = new HashSet<>();
        new InputReader(new Configuration()).readInput("message", new ClassPathEntry(file, false), dataEntry -> withoutJmods.add(dataEntry.getName()));

        Set<String> missing = new HashSet<>(withJmods);
        missing.removeAll(withoutJmods);
        Set<String> unexpected = new HashSet<>(withoutJmods);
        unexpected.removeAll(withJmods);

        System.out.println("=== MISSING ===");
        missing.forEach(System.out::println);

        System.out.println("=== UNEXPECTED ===");
        unexpected.forEach(System.out::println);
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
            List<String> filter = getFilterExcludingVersionedClasses(classPathEntry);

            logger.info("{}{} [{}]{}",
                messagePrefix,
                classPathEntry.isDex()  ? "dex"  :
                classPathEntry.isApk()  ? "apk"  :
                classPathEntry.isAab()  ? "aab"  :
                classPathEntry.isJar()  ? "jar"  :
                classPathEntry.isAar()  ? "aar"  :
                classPathEntry.isWar()  ? "war"  :
                classPathEntry.isEar()  ? "ear"  :
                classPathEntry.isJmod() ? "jmod" :
                classPathEntry.isZip()  ? "zip"  :
                                          "directory",
                classPathEntry.getName(),
                filter != null || classPathEntry.isFiltered() ? " (filtered)" : ""
            );

            File classPathFile = classPathEntry.getFile();
            DataEntryReader reader;
            DataEntrySource source = maybeGetJrtFallback(classPathFile, classPathEntry.isJmod());
            if (source != null) {
                reader = dataEntryReader;
                logger.info("Using jrt:/ file system fallback due to missing jmods directory: {}", classPathFile);
            } else {
                // Create a reader that can unwrap jars, wars, ears, jmods and zips.
                reader =
                    new DataEntryReaderFactory(configuration.android)
                        .createDataEntryReader(classPathEntry,
                                dataEntryReader);

                // Create the data entry source.
                source =
                    new DirectorySource(classPathFile);
            }

            // Set the feature name for the class files and resource files
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
     * Fallback for JDK >= 24 without {@code jmods} directory, see <a href="https://github.com/Guardsquare/proguard/issues/473">
     * issue 473</a>.
     *
     * <p>If the class path file is detected to be the non-existent {@code JAVA_HOME/jmods} directory or a jmod file
     * within it, creates a {@link DataEntrySource} which uses the {@code jrt:/} file system to read JDK classes.
     *
     * @return a data entry source if the class path file refers to the non-existent {@code JAVA_HOME/jmods} directory,
     *      or {@code null} otherwise (e.g. does not refer to jmods directory, or jmods directory exists)
     */
    @Nullable
    private static JrtDataEntrySource maybeGetJrtFallback(File classPathFile, boolean isJmodFile) {
        File jmodsDir = classPathFile;
        String moduleName = null;

        // Handle `jmods/<module>.jmod`
        if (isJmodFile) {
            jmodsDir = classPathFile.getParentFile();
            if (jmodsDir == null) {
                return null;
            }

            String fileName = classPathFile.getName();
            moduleName = fileName.substring(0, fileName.lastIndexOf('.'));
        }

        // First check if this is really a non-existent `JAVA_HOME/jmods` directory
        if (!jmodsDir.getName().equals("jmods")) {
            return null;
        }
        if (jmodsDir.exists()) {
            return null;
        }

        Path javaHome = jmodsDir.toPath().getParent();
        if (javaHome == null) {
            return null;
        }

        Path jrtFsJarPath = JrtDataEntrySource.getJrtFsJarPath(javaHome);
        if (!Files.isRegularFile(jrtFsJarPath)) {
            // Not actually a JDK JAVA_HOME (or the `jrt-fs.jar` is missing for whatever reason)
            return null;
        }

        // Now we are sure that this is the jmods directory of a JDK, and that the `jrt:/` file system can be used
        return new JrtDataEntrySource(javaHome, moduleName);
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
