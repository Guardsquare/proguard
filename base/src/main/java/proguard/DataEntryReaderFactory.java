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

import proguard.io.DataEntryNameFilter;
import proguard.io.DataEntryReader;
import proguard.io.FilteredDataEntryReader;
import proguard.io.JarReader;
import proguard.io.PrefixStrippingDataEntryReader;
import proguard.io.RenamedDataEntryReader;
import proguard.util.AndMatcher;
import proguard.util.ExtensionMatcher;
import proguard.util.FileNameParser;
import proguard.util.ListFunctionParser;
import proguard.util.ListParser;
import proguard.util.NotMatcher;
import proguard.util.SingleFunctionParser;
import proguard.util.StringFunction;
import proguard.util.StringMatcher;
import proguard.util.WildcardManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static proguard.classfile.ClassConstants.CLASS_FILE_EXTENSION;

/**
 * This class can create DataEntryReader instances based on class path entries.
 * The readers will unwrap the input data entries from any jars, wars, ears,
 * jmods, and zips before passing them to a given reader.
 *
 * @author Eric Lafortune
 */
public class DataEntryReaderFactory
{

    private static final String VERSIONS_PATTERN = "META-INF/versions";
    private static final String VERSIONS_EXCLUDE = "!META-INF/versions/**";

    private static final String JMOD_CLASS_FILE_PREFIX = "classes/";


    private final boolean android;


    /**
     * Creates a new DataEntryReaderFactory.
     *
     * @param android Specifies whether the packaging is targeted at the
     *                Android platform. Archives inside the assets directory
     *                then aren't unpacked but simply read as data files.
     */
    public DataEntryReaderFactory(boolean android)
    {
        this.android = android;
    }

    /**
     * Creates a DataEntryReader that can read the given class path entry.
     *
     * @param classPathEntry the input class path entry.
     * @param reader         a data entry reader to which the reading of actual
     *                       classes and resource files can be delegated.
     * @return a DataEntryReader for reading the given class path entry.
     */
    public DataEntryReader createDataEntryReader(ClassPathEntry classPathEntry, DataEntryReader reader)
    {
        boolean isApk  = classPathEntry.isApk();
        boolean isAab  = classPathEntry.isAab();
        boolean isJar  = classPathEntry.isJar();
        boolean isAar  = classPathEntry.isAar();
        boolean isWar  = classPathEntry.isWar();
        boolean isEar  = classPathEntry.isEar();
        boolean isJmod = classPathEntry.isJmod();
        boolean isZip  = classPathEntry.isZip();

        List<String> filter     = getFilterExcludingVersionedClasses(classPathEntry);
        List<String> apkFilter  = classPathEntry.getApkFilter();
        List<String> aabFilter  = classPathEntry.getAabFilter();
        List<String> jarFilter  = classPathEntry.getJarFilter();
        List<String> aarFilter  = classPathEntry.getAarFilter();
        List<String> warFilter  = classPathEntry.getWarFilter();
        List<String> earFilter  = classPathEntry.getEarFilter();
        List<String> jmodFilter = classPathEntry.getJmodFilter();
        List<String> zipFilter  = classPathEntry.getZipFilter();

        // Add a renaming filter, if specified.
        if (filter != null)
        {
            WildcardManager wildcardManager = new WildcardManager();

            StringFunction nameFunction =
                new ListFunctionParser(
                new SingleFunctionParser(
                new FileNameParser(wildcardManager), wildcardManager)).parse(filter);

            reader = new RenamedDataEntryReader(nameFunction, reader);
        }

        // Unzip any apks, if necessary.
        reader = wrapInJarReader(reader, false, false, isApk, apkFilter, ".apk");
        if (!isApk)
        {
            // Unzip any aabs, if necessary.
            reader = wrapInJarReader(reader, false, false, isAab, aabFilter, ".aab");
            if (!isAab)
            {
                // Unzip any jars, if necessary.
                reader = wrapInJarReader(reader, false, false, isJar, jarFilter, ".jar");
                if (!isJar)
                {
                    // Unzip any aars, if necessary.
                    reader = wrapInJarReader(reader, false, false, isAar, aarFilter, ".aar");
                    if (!isAar)
                    {
                        // Unzip any wars, if necessary.
                        reader = wrapInJarReader(reader, true, false, isWar, warFilter, ".war");
                        if (!isWar)
                        {
                            // Unzip any ears, if necessary.
                            reader = wrapInJarReader(reader, false, false, isEar, earFilter, ".ear");
                            if (!isEar)
                            {
                                // Unzip any jmods, if necessary.
                                reader = wrapInJarReader(reader, true, true, isJmod, jmodFilter, ".jmod");
                                if (!isJmod)
                                {
                                    // Unzip any zips, if necessary.
                                    reader = wrapInJarReader(reader, false, false, isZip, zipFilter, ".zip");
                                }
                            }
                        }
                    }
                }
            }
        }

        return reader;
    }


    /**
     * Wraps the given DataEntryReader in a JarReader, filtering it if
     * necessary.
     * @param reader             the data entry reader that can read the
     *                           entries contained in the jar file.
     * @param stripClassesPrefix specifies whether to strip the ""classes/"
     *                           prefix from contained .class data entries.
     *@param stripJmodHeader     specifies whether to strip the jmod magic
     *                           bytes from the zip.
     * @param isJar              specifies whether the data entries should
     *                           always be unzipped.
     * @param jarFilter          otherwise, an optional filter on the data
     *                           entry names.
     * @param jarExtension       also otherwise, a required data entry name
     *                           extension.
     * @return a DataEntryReader for reading the entries of jar file data
     *         entries.
     */
    private DataEntryReader wrapInJarReader(DataEntryReader reader,
                                            boolean         stripClassesPrefix,
                                            boolean         stripJmodHeader,
                                            boolean         isJar,
                                            List<String>    jarFilter,
                                            String          jarExtension)
    {
        if (stripClassesPrefix)
        {
            reader = new FilteredDataEntryReader(
                new DataEntryNameFilter(new ExtensionMatcher(CLASS_FILE_EXTENSION)),
                new PrefixStrippingDataEntryReader(JMOD_CLASS_FILE_PREFIX, reader),
                reader);
        }

        // Unzip any jars, if necessary.
        DataEntryReader jarReader = new JarReader(stripJmodHeader, reader);

        if (isJar)
        {
            // Always unzip.
            return jarReader;
        }
        else
        {
            // Add a filter, if specified.
            if (jarFilter != null)
            {
                jarReader = new FilteredDataEntryReader(
                            new DataEntryNameFilter(
                            new ListParser(new FileNameParser()).parse(jarFilter)),
                                jarReader);
            }

            StringMatcher jarMatcher =
                new ExtensionMatcher(jarExtension);

            // Don't unzip archives in Android assets directories.
            if (android)
            {
                jarMatcher =
                    new AndMatcher(
                        new AndMatcher(
                            new NotMatcher(
                            new ListParser(new FileNameParser()).parse("assets/**,*/assets/**")),

                            new NotMatcher(
                            new ListParser(new FileNameParser()).parse("res/**,*/res/**"))),

                            jarMatcher);
            }

            // Only unzip the right type of jars.
            return new FilteredDataEntryReader(
                   new DataEntryNameFilter(jarMatcher),
                       jarReader,
                       reader);
        }
    }


    /**
     * Method to return an augmented filter for supported features.
     * <p>
     * Currently versioned class files (a feature introduced in Java 9) are not fully
     * supported by ProGuard. Only 1 version of a class can be read and processed.
     * If no custom filter targeting a specific version is used, exclude such classes
     * from being read.
     */
    public static List<String> getFilterExcludingVersionedClasses(ClassPathEntry classPathEntry)
    {
        List<String> originalFilter = classPathEntry.getFilter();
        if (originalFilter == null)
        {
            return Collections.singletonList(VERSIONS_EXCLUDE);
        }
        else
        {
            // If there is already a custom filter for versioned classes
            // assume that the filter is properly setup.
            for (String element : originalFilter) {
                if (element.contains(VERSIONS_PATTERN)) {
                    return originalFilter;
                }
            }

            // Otherwise, exclude all versioned classes.
            List<String>  filter = new ArrayList<>();
            filter.add(VERSIONS_EXCLUDE);
            filter.addAll(originalFilter);
            return filter;
        }
    }
}
