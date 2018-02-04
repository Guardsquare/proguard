/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 GuardSquare NV
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

import proguard.classfile.*;
import proguard.io.*;
import proguard.util.*;

import java.util.List;

/**
 * This class can create DataEntryWriter instances based on class paths. The
 * writers will wrap the output in the proper apks, jars, wars, ears, jmods,
 * and zips.
 *
 * @author Eric Lafortune
 */
public class DataEntryWriterFactory
{
    private final ClassPool                              programClassPool;
    private final MultiValueMap                          extraClassNameMap;


    /**
     * Creates a new DataEntryWriterFactory with the given parameters.
     * @param programClassPool      the program classpool to process.
     */
    public DataEntryWriterFactory(ClassPool                              programClassPool,
                                  MultiValueMap<String, String>          extraClassNamemap)
    {
        this.programClassPool                 = programClassPool;
        this.extraClassNameMap                = extraClassNamemap;
    }


    /**
     * Creates a DataEntryWriter that can write to the given class path entries.
     *
     * @param classPath the output class path.
     * @param fromIndex the start index in the class path.
     * @param toIndex   the end index in the class path.
     * @return a DataEntryWriter for writing to the given class path entries.
     */
    public DataEntryWriter createDataEntryWriter(ClassPath classPath,
                                                 int       fromIndex,
                                                 int       toIndex)
    {
        DataEntryWriter writer = null;

        // Create a chain of writers, one for each class path entry.
        for (int index = toIndex - 1; index >= fromIndex; index--)
        {
            ClassPathEntry entry = classPath.get(index);

            writer = createClassPathEntryWriter(entry, writer);
        }

        return writer;
    }


    /**
     * Creates a DataEntryWriter that can write to the given class path entry,
     * or delegate to another DataEntryWriter if its filters don't match.
     */
    private DataEntryWriter createClassPathEntryWriter(ClassPathEntry  classPathEntry,
                                                       DataEntryWriter alternativeWriter)
    {
        boolean isApk  = classPathEntry.isApk();
        boolean isJar  = classPathEntry.isJar();
        boolean isAar  = classPathEntry.isAar();
        boolean isWar  = classPathEntry.isWar();
        boolean isEar  = classPathEntry.isEar();
        boolean isJmod = classPathEntry.isJmod();
        boolean isZip  = classPathEntry.isZip();

        List filter     = classPathEntry.getFilter();
        List apkFilter  = classPathEntry.getApkFilter();
        List jarFilter  = classPathEntry.getJarFilter();
        List aarFilter  = classPathEntry.getAarFilter();
        List warFilter  = classPathEntry.getWarFilter();
        List earFilter  = classPathEntry.getEarFilter();
        List jmodFilter = classPathEntry.getJmodFilter();
        List zipFilter  = classPathEntry.getZipFilter();

        System.out.println("Preparing output " +
                           (isApk  ? "apk"  :
                            isJar  ? "jar"  :
                            isAar  ? "aar"  :
                            isWar  ? "war"  :
                            isEar  ? "ear"  :
                            isJmod ? "jmod" :
                            isZip  ? "zip"  :
                                     "directory") +
                           " [" + classPathEntry.getName() + "]" +
                           (filter     != null ||
                            apkFilter  != null ||
                            jarFilter  != null ||
                            aarFilter  != null ||
                            warFilter  != null ||
                            earFilter  != null ||
                            jmodFilter != null ||
                            zipFilter  != null ? " (filtered)" : ""));

        DataEntryWriter writer = new DirectoryWriter(classPathEntry.getFile(),
                                                     isApk  ||
                                                     isJar  ||
                                                     isAar  ||
                                                     isWar  ||
                                                     isEar  ||
                                                     isJmod ||
                                                     isZip);

        // Set up the filtered jar writers.
        writer = wrapInJarWriter(writer, false, null,                       isZip,  zipFilter,  ".zip",  isApk || isJar || isAar || isWar || isEar || isJmod);
        writer = wrapInJarWriter(writer, true,  ClassConstants.JMOD_HEADER, isJmod, jmodFilter, ".jmod", isApk || isJar || isAar || isWar || isEar);
        writer = wrapInJarWriter(writer, false, null,                       isEar,  earFilter,  ".ear",  isApk || isJar || isAar || isWar);
        writer = wrapInJarWriter(writer, true,  null,                       isWar,  warFilter,  ".war",  isApk || isJar || isAar);
        writer = wrapInJarWriter(writer, false, null,                       isAar,  aarFilter,  ".aar",  isApk || isJar);
        writer = wrapInJarWriter(writer, false, null,                       isJar,  jarFilter,  ".jar",  isApk);
        writer = wrapInJarWriter(writer, false, null,                       isApk,  apkFilter,  ".apk",  false);

        // Set up for writing out the program classes.
        writer = new ClassDataEntryWriter(programClassPool, writer);

        // Add a filter, if specified.
        writer = filter != null ?
            new FilteredDataEntryWriter(
                new DataEntryNameFilter(
                    new ListParser(new FileNameParser()).parse(filter)),
                writer) :
            writer;

        // Add a writer for the injected classes.
        writer = new ExtraDataEntryWriter(extraClassNameMap,
                                          writer,
                                          writer,
                                          ClassConstants.CLASS_FILE_EXTENSION);

        // Let the writer cascade, if specified.
        return alternativeWriter != null ?
            new CascadingDataEntryWriter(writer, alternativeWriter) :
            writer;
    }


    /**
     * Wraps the given DataEntryWriter in a JarWriter, filtering if necessary.
     */
    private DataEntryWriter wrapInJarWriter(DataEntryWriter writer,
                                            boolean         addClassesPrefix,
                                            byte[]          header,
                                            boolean         isJar,
                                            List            jarFilter,
                                            String          jarExtension,
                                            boolean         dontWrap)
    {
        // Zip up jars, if necessary.
        DataEntryWriter jarWriter =
            dontWrap ?
                new ParentDataEntryWriter(writer) :
                new JarWriter(header, writer);

        // Add a "classes/" prefix for class files, if specified.
        if (addClassesPrefix)
        {
            writer = new FilteredDataEntryWriter(
                new DataEntryNameFilter(
                new ExtensionMatcher(".class")),
                new PrefixAddingDataEntryWriter("classes/",
                                                writer),
                writer);
        }

        // Add a filter, if specified.
        DataEntryWriter filteredJarWriter = jarFilter != null ?
            new FilteredDataEntryWriter(
            new DataEntryParentFilter(
            new DataEntryNameFilter(
            new ListParser(new FileNameParser()).parse(jarFilter))),
            jarWriter) :

            jarWriter;

        // Only zip up jars, unless the output is a jar file itself.
        return new FilteredDataEntryWriter(
               new DataEntryParentFilter(
               new DataEntryNameFilter(
               new ExtensionMatcher(jarExtension))),
                   filteredJarWriter,
                   isJar ? jarWriter : writer);
    }
}
