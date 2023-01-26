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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.util.WarningLogger;
import proguard.classfile.util.WarningPrinter;

import java.io.*;

/**
 * This class performs sanity checks on a given configurations.
 *
 * @author Eric Lafortune
 */
public class ConfigurationVerifier
{
    private static final Logger logger = LogManager.getLogger(ConfigurationVerifier.class);

    private final Configuration configuration;


    /**
     * Creates a new ConfigurationVerifier with the given configuration.
     */
    public ConfigurationVerifier(Configuration configuration)
    {
        this.configuration = configuration;
    }


    /**
     * Checks the given configuration for potential problems.
     */
    public void check() throws IOException
    {
        ClassPath programJars = configuration.programJars;
        ClassPath libraryJars = configuration.libraryJars;

        // Check that the input isn't empty.
        if (programJars == null)
        {
            throw new IOException("The input is empty. You have to specify one or more '-injars' options.");
        }

        checkInputJarFirst(programJars);

        checkOutputJarFilter(programJars);

        // Check for conflicts between input/output entries of the class paths.
        checkConflicts(programJars, programJars);
        checkConflicts(programJars, libraryJars);
        checkConflicts(libraryJars, libraryJars);

        printNotes(configuration, programJars, logger);
    }


    /**
     * Checks that the input application is specified before any input libraries.
     */
    private void checkInputJarFirst(ClassPath programJars) throws IOException
    {
        // Check that the first jar is an input jar.
        ClassPathEntry firstEntry = programJars.get(0);
        if (firstEntry.isOutput())
        {
            throw new IOException("The output jar [" + firstEntry.getName() +
                                    "] must be specified after an input jar, or it will be empty.");
        }
    }


     /**
     * Checks that the first of two subsequent output jars has a filter.
     */
    private void checkOutputJarFilter(ClassPath programJars) throws IOException
    {
        // Check that the first of two subsequent the output jars has a filter.
        for (int index = 0; index < programJars.size() - 1; index++)
        {
            ClassPathEntry entry = programJars.get(index);
            if (entry.isOutput()    &&
                !entry.isFiltered() &&
                programJars.get(index + 1).isOutput())
            {
                throw new IOException("The output jar [" + entry.getName() +
                                      "] must have a filter, or all subsequent output jars will be empty.");
            }
        }
    }


    /**
     * Performs some sanity checks on the class paths.
     */
    private void checkConflicts(ClassPath classPath1,
                                ClassPath classPath2)
    throws IOException
    {
        if (classPath1 == null ||
            classPath2 == null)
        {
            return;
        }

        for (int index1 = 0; index1 < classPath1.size(); index1++)
        {
            ClassPathEntry entry1 = classPath1.get(index1);

            for (int index2 = 0; index2 < classPath2.size(); index2++)
            {
                if (classPath1 != classPath2 || index1 != index2)
                {
                    ClassPathEntry entry2 = classPath2.get(index2);

                    if (entry2.getName().equals(entry1.getName()))
                    {
                        if (entry1.isOutput())
                        {
                            if (entry2.isOutput())
                            {
                                // Output / output.
                                throw new IOException("The same output jar ["+entry1.getName()+"] is specified twice.");
                            }
                            else
                            {
                                // Output / input.
                                throw new IOException("Input jars and output jars must be different ["+entry1.getName()+"].");
                            }
                        }
                        else
                        {
                            if (entry2.isOutput())
                            {
                                // Input / output.
                                throw new IOException("Input jars and output jars must be different ["+entry1.getName()+"].");
                            }
                            else if (!entry1.isFiltered() ||
                                     !entry2.isFiltered())
                            {
                                // Input / input.
                                throw new IOException("The same input jar ["+entry1.getName()+"] is specified twice.");
                            }
                        }
                    }
                }
            }
        }
    }


    private void printNotes(Configuration configuration, ClassPath programJars, Logger logger) throws IOException
    {
        // Print out some general notes if necessary.
        if ((configuration.note == null ||
             !configuration.note.isEmpty()))
        {
            // Check for potential problems with mixed-case class names on
            // case-insensitive file systems.
            if (configuration.obfuscate &&
                configuration.useMixedCaseClassNames &&
                configuration.classObfuscationDictionary == null)
            {
                String os = System.getProperty("os.name").toLowerCase();
                if (os.startsWith("windows") ||
                    os.startsWith("mac os"))
                {
                    // Go over all program class path entries.
                    for (int index = 0; index < programJars.size(); index++)
                    {
                        // Is it an output directory?
                        ClassPathEntry entry = programJars.get(index);
                        if (entry.isOutput() &&
                            !entry.isApk()   &&
                            !entry.isJar()   &&
                            !entry.isAar()   &&
                            !entry.isWar()   &&
                            !entry.isEar()   &&
                            !entry.isJmod()  &&
                            !entry.isZip())
                        {
                            logger.info("Note: you're writing the processed class files to a directory [{}].", entry.getName());
                            logger.info("      This will likely cause problems with obfuscated mixed-case class names.");
                            logger.info("      You should consider writing the output to a jar file, or otherwise");
                            logger.info("      specify '-dontusemixedcaseclassnames'.");

                            break;
                        }
                    }
                }
            }

            // Check if -adaptresourcefilecontents has a proper filter.
            if (configuration.adaptResourceFileContents != null &&
                (configuration.adaptResourceFileContents.isEmpty() ||
                 configuration.adaptResourceFileContents.get(0).equals(ConfigurationConstants.ANY_FILE_KEYWORD)))
            {
                logger.info("Note: you're specifying '-adaptresourcefilecontents' for all resource files.");
                logger.info("      This will most likely cause problems with binary files.");
            }

            // Check if all -keepclassmembers options indeed have class members.
            WarningPrinter keepClassMemberNotePrinter = new WarningLogger(logger, configuration.note);

            new KeepClassMemberChecker(keepClassMemberNotePrinter).checkClassSpecifications(configuration.keep);

            // Check if -assumenosideffects options don't specify all methods.
            WarningPrinter assumeNoSideEffectsNotePrinter = new WarningLogger(logger, configuration.note);

            new AssumeNoSideEffectsChecker(assumeNoSideEffectsNotePrinter).checkClassSpecifications(configuration.assumeNoSideEffects);

            // Print out a summary of the notes, if necessary.
            int keepClassMemberNoteCount = keepClassMemberNotePrinter.getWarningCount();
            if (keepClassMemberNoteCount > 0)
            {
                logger.info("Note: there were {} '-keepclassmembers' options that didn't specify class", keepClassMemberNoteCount);
                logger.info("      members. You should specify at least some class members or consider");
                logger.info("      if you just need '-keep'.");
                logger.info("      (https://www.guardsquare.com/proguard/manual/troubleshooting#classmembers)");
            }

            int assumeNoSideEffectsNoteCount = assumeNoSideEffectsNotePrinter.getWarningCount();
            if (assumeNoSideEffectsNoteCount > 0)
            {
                logger.info("Note: there were {} '-assumenosideeffects' options that try to match all", assumeNoSideEffectsNoteCount);
                logger.info("      methods with wildcards. This will likely cause problems with methods like");
                logger.info("      'wait()' and 'notify()'. You should specify the methods more precisely.");
                logger.info("      (https://www.guardsquare.com/proguard/manual/troubleshooting#nosideeffects)");
            }
        }
    }
}
