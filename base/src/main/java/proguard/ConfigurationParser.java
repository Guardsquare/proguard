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

import proguard.classfile.*;
import proguard.classfile.util.ClassUtil;
import proguard.util.*;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 * This class parses ProGuard configurations. Configurations can be read from an
 * array of arguments or from a configuration file or URL. External references
 * in file names ('&lt;...&gt;') can be resolved against a given set of properties.
 *
 * @author Eric Lafortune
 */
public class ConfigurationParser implements AutoCloseable
{
    private final WordReader reader;
    private final Properties properties;

    private String     nextWord;
    private String     lastComments;


    /**
     * Creates a new ConfigurationParser for the given String arguments and
     * the given Properties.
     */
    public ConfigurationParser(String[]   args,
                               Properties properties) throws IOException
    {
        this(args, null, properties);
    }


    /**
     * Creates a new ConfigurationParser for the given String arguments,
     * with the given base directory and the given Properties.
     */
    public ConfigurationParser(String[]   args,
                               File       baseDir,
                               Properties properties) throws IOException
    {
        this(new ArgumentWordReader(args, baseDir), properties);
    }


    /**
     * Creates a new ConfigurationParser for the given lines,
     * with the given base directory and the given Properties.
     */
    public ConfigurationParser(String     lines,
                               String     description,
                               File       baseDir,
                               Properties properties) throws IOException
    {
        this(new LineWordReader(new LineNumberReader(new StringReader(lines)),
                                description,
                                baseDir),
             properties);
    }


    /**
     * Creates a new ConfigurationParser for the given file, with the system
     * Properties.
     * @deprecated Temporary code for backward compatibility in Obclipse.
     */
    public ConfigurationParser(File file) throws IOException
    {
        this(file, System.getProperties());
    }


    /**
     * Creates a new ConfigurationParser for the given file and the given
     * Properties.
     */
    public ConfigurationParser(File       file,
                               Properties properties) throws IOException
    {
        this(new FileWordReader(file), properties);
    }


    /**
     * Creates a new ConfigurationParser for the given URL and the given
     * Properties.
     */
    public ConfigurationParser(URL        url,
                               Properties properties) throws IOException
    {
        this(new FileWordReader(url), properties);
    }


    /**
     * Creates a new ConfigurationParser for the given word reader and the
     * given Properties.
     */
    public ConfigurationParser(WordReader reader,
                               Properties properties) throws IOException
    {
        this.reader     = reader;
        this.properties = properties;

        readNextWord();
    }


    /**
     * Parses and returns the configuration.
     * @param configuration the configuration that is updated as a side-effect.
     * @throws ParseException if the any of the configuration settings contains
     *                        a syntax error.
     * @throws IOException if an IO error occurs while reading a configuration.
     */
    public void parse(Configuration configuration)
    throws ParseException, IOException
    {
        while (nextWord != null)
        {
            lastComments = reader.lastComments();

            // First include directives.
            if      (ConfigurationConstants.AT_DIRECTIVE                                     .startsWith(nextWord) ||
                     ConfigurationConstants.INCLUDE_DIRECTIVE                                .startsWith(nextWord)) configuration.lastModified                          = parseIncludeArgument(configuration.lastModified);
            else if (ConfigurationConstants.BASE_DIRECTORY_DIRECTIVE                         .startsWith(nextWord)) parseBaseDirectoryArgument();

            // Then configuration options with or without arguments.
            else if (ConfigurationConstants.INJARS_OPTION                                    .startsWith(nextWord)) configuration.programJars                           = parseClassPathArgument(configuration.programJars, false, true);
            else if (ConfigurationConstants.OUTJARS_OPTION                                   .startsWith(nextWord)) configuration.programJars                           = parseClassPathArgument(configuration.programJars, true, false);
            else if (ConfigurationConstants.LIBRARYJARS_OPTION                               .startsWith(nextWord)) configuration.libraryJars                           = parseClassPathArgument(configuration.libraryJars, false, false);
            else if (ConfigurationConstants.RESOURCEJARS_OPTION                              .startsWith(nextWord)) throw new ParseException("The '-resourcejars' option is no longer supported. Please use the '-injars' option for all input");
            else if (ConfigurationConstants.SKIP_NON_PUBLIC_LIBRARY_CLASSES_OPTION           .startsWith(nextWord)) configuration.skipNonPublicLibraryClasses           = parseNoArgument(true);
            else if (ConfigurationConstants.DONT_SKIP_NON_PUBLIC_LIBRARY_CLASSES_OPTION      .startsWith(nextWord)) configuration.skipNonPublicLibraryClasses           = parseNoArgument(false);
            else if (ConfigurationConstants.DONT_SKIP_NON_PUBLIC_LIBRARY_CLASS_MEMBERS_OPTION.startsWith(nextWord)) configuration.skipNonPublicLibraryClassMembers      = parseNoArgument(false);
            else if (ConfigurationConstants.TARGET_OPTION                                    .startsWith(nextWord)) configuration.targetClassVersion                    = parseClassVersion();
            else if (ConfigurationConstants.DONT_COMPRESS_OPTION                             .startsWith(nextWord)) configuration.dontCompress                          = parseCommaSeparatedList("file name", true, true, false, true, false, true, false, false, false, configuration.dontCompress);
            else if (ConfigurationConstants.ZIP_ALIGN_OPTION                                 .startsWith(nextWord)) configuration.zipAlign                              = parseIntegerArgument();
            else if (ConfigurationConstants.FORCE_PROCESSING_OPTION                          .startsWith(nextWord)) configuration.lastModified                          = parseNoArgument(Long.MAX_VALUE);

            else if (ConfigurationConstants.IF_OPTION                                        .startsWith(nextWord)) configuration.keep                                  = parseIfCondition(configuration.keep);
            else if (ConfigurationConstants.KEEP_OPTION                                      .startsWith(nextWord)) configuration.keep                                  = parseKeepClassSpecificationArguments(configuration.keep, true,  true,  false, false, false, null);
            else if (ConfigurationConstants.KEEP_CLASS_MEMBERS_OPTION                        .startsWith(nextWord)) configuration.keep                                  = parseKeepClassSpecificationArguments(configuration.keep, false, true,  false, false, false, null);
            else if (ConfigurationConstants.KEEP_CLASSES_WITH_MEMBERS_OPTION                 .startsWith(nextWord)) configuration.keep                                  = parseKeepClassSpecificationArguments(configuration.keep, false, true,  false, true,  false, null);
            else if (ConfigurationConstants.KEEP_NAMES_OPTION                                .startsWith(nextWord)) configuration.keep                                  = parseKeepClassSpecificationArguments(configuration.keep, true,  true,  false, false, true,  null);
            else if (ConfigurationConstants.KEEP_CLASS_MEMBER_NAMES_OPTION                   .startsWith(nextWord)) configuration.keep                                  = parseKeepClassSpecificationArguments(configuration.keep, false, true,  false, false, true,  null);
            else if (ConfigurationConstants.KEEP_CLASSES_WITH_MEMBER_NAMES_OPTION            .startsWith(nextWord)) configuration.keep                                  = parseKeepClassSpecificationArguments(configuration.keep, false, true,  false, true,  true,  null);
            else if (ConfigurationConstants.KEEP_CODE_OPTION                                 .startsWith(nextWord)) configuration.keep                                  = parseKeepClassSpecificationArguments(configuration.keep, false, false, true,  false, false, null);
            else if (ConfigurationConstants.PRINT_SEEDS_OPTION                               .startsWith(nextWord)) configuration.printSeeds                            = parseOptionalFile();

            // After '-keep'.
            else if (ConfigurationConstants.KEEP_DIRECTORIES_OPTION                          .startsWith(nextWord)) configuration.keepDirectories                       = parseCommaSeparatedList("directory name", true, true, false, true, false, true, true, false, false, configuration.keepDirectories);

            else if (ConfigurationConstants.DONT_SHRINK_OPTION                               .startsWith(nextWord)) configuration.shrink                                = parseNoArgument(false);
            else if (ConfigurationConstants.PRINT_USAGE_OPTION                               .startsWith(nextWord)) configuration.printUsage                            = parseOptionalFile();
            else if (ConfigurationConstants.WHY_ARE_YOU_KEEPING_OPTION                       .startsWith(nextWord)) configuration.whyAreYouKeeping                      = parseClassSpecificationArguments(configuration.whyAreYouKeeping);

            else if (ConfigurationConstants.DONT_OPTIMIZE_OPTION                             .startsWith(nextWord)) configuration.optimize                              = parseNoArgument(false);
            else if (ConfigurationConstants.OPTIMIZATION_PASSES                              .startsWith(nextWord)) configuration.optimizationPasses                    = parseIntegerArgument();
            else if (ConfigurationConstants.OPTIMIZATIONS                                    .startsWith(nextWord)) configuration.optimizations                         = parseCommaSeparatedList("optimization name", true, false, false, false, false, true, false, false, false, configuration.optimizations);
            else if (ConfigurationConstants.ASSUME_NO_SIDE_EFFECTS_OPTION                    .startsWith(nextWord)) configuration.assumeNoSideEffects                   = parseAssumeClassSpecificationArguments(configuration.assumeNoSideEffects);
            else if (ConfigurationConstants.ASSUME_NO_EXTERNAL_SIDE_EFFECTS_OPTION           .startsWith(nextWord)) configuration.assumeNoExternalSideEffects           = parseAssumeClassSpecificationArguments(configuration.assumeNoExternalSideEffects);
            else if (ConfigurationConstants.ASSUME_NO_ESCAPING_PARAMETERS_OPTION             .startsWith(nextWord)) configuration.assumeNoEscapingParameters            = parseAssumeClassSpecificationArguments(configuration.assumeNoEscapingParameters);
            else if (ConfigurationConstants.ASSUME_NO_EXTERNAL_RETURN_VALUES_OPTION          .startsWith(nextWord)) configuration.assumeNoExternalReturnValues          = parseAssumeClassSpecificationArguments(configuration.assumeNoExternalReturnValues);
            else if (ConfigurationConstants.ASSUME_VALUES_OPTION                             .startsWith(nextWord)) configuration.assumeValues                          = parseAssumeClassSpecificationArguments(configuration.assumeValues);
            else if (ConfigurationConstants.ALLOW_ACCESS_MODIFICATION_OPTION                 .startsWith(nextWord)) configuration.allowAccessModification               = parseNoArgument(true);
            else if (ConfigurationConstants.MERGE_INTERFACES_AGGRESSIVELY_OPTION             .startsWith(nextWord)) configuration.mergeInterfacesAggressively           = parseNoArgument(true);

            else if (ConfigurationConstants.DONT_OBFUSCATE_OPTION                            .startsWith(nextWord)) configuration.obfuscate                             = parseNoArgument(false);
            else if (ConfigurationConstants.PRINT_MAPPING_OPTION                             .startsWith(nextWord)) configuration.printMapping                          = parseOptionalFile();
            else if (ConfigurationConstants.APPLY_MAPPING_OPTION                             .startsWith(nextWord)) configuration.applyMapping                          = parseFile();
            else if (ConfigurationConstants.OBFUSCATION_DICTIONARY_OPTION                    .startsWith(nextWord)) configuration.obfuscationDictionary                 = parseURL();
            else if (ConfigurationConstants.CLASS_OBFUSCATION_DICTIONARY_OPTION              .startsWith(nextWord)) configuration.classObfuscationDictionary            = parseURL();
            else if (ConfigurationConstants.PACKAGE_OBFUSCATION_DICTIONARY_OPTION            .startsWith(nextWord)) configuration.packageObfuscationDictionary          = parseURL();
            else if (ConfigurationConstants.OVERLOAD_AGGRESSIVELY_OPTION                     .startsWith(nextWord)) configuration.overloadAggressively                  = parseNoArgument(true);
            else if (ConfigurationConstants.USE_UNIQUE_CLASS_MEMBER_NAMES_OPTION             .startsWith(nextWord)) configuration.useUniqueClassMemberNames             = parseNoArgument(true);
            else if (ConfigurationConstants.DONT_USE_MIXED_CASE_CLASS_NAMES_OPTION           .startsWith(nextWord)) configuration.useMixedCaseClassNames                = parseNoArgument(false);
            else if (ConfigurationConstants.KEEP_PACKAGE_NAMES_OPTION                        .startsWith(nextWord)) configuration.keepPackageNames                      = parseCommaSeparatedList("package name", true, true, false, false, true, false, false, true, false, configuration.keepPackageNames);
            else if (ConfigurationConstants.FLATTEN_PACKAGE_HIERARCHY_OPTION                 .startsWith(nextWord)) configuration.flattenPackageHierarchy               = ClassUtil.internalClassName(parseOptionalArgument());
            else if (ConfigurationConstants.REPACKAGE_CLASSES_OPTION                         .startsWith(nextWord) ||
                     ConfigurationConstants.DEFAULT_PACKAGE_OPTION                           .startsWith(nextWord)) configuration.repackageClasses                      = ClassUtil.internalClassName(parseOptionalArgument());
            else if (ConfigurationConstants.KEEP_ATTRIBUTES_OPTION                           .startsWith(nextWord)) configuration.keepAttributes                        = parseCommaSeparatedList("attribute name", true, true, false, false, true, false, false, false, false, configuration.keepAttributes);
            else if (ConfigurationConstants.KEEP_PARAMETER_NAMES_OPTION                      .startsWith(nextWord)) configuration.keepParameterNames                    = parseNoArgument(true);
            else if (ConfigurationConstants.RENAME_SOURCE_FILE_ATTRIBUTE_OPTION              .startsWith(nextWord)) configuration.newSourceFileAttribute                = parseOptionalArgument();
            else if (ConfigurationConstants.ADAPT_CLASS_STRINGS_OPTION                       .startsWith(nextWord)) configuration.adaptClassStrings                     = parseCommaSeparatedList("class name", true, true, false, false, true, false, false, true, false, configuration.adaptClassStrings);
            else if (ConfigurationConstants.ADAPT_RESOURCE_FILE_NAMES_OPTION                 .startsWith(nextWord)) configuration.adaptResourceFileNames                = parseCommaSeparatedList("resource file name", true, true, false, true, false, true, false, false, false, configuration.adaptResourceFileNames);
            else if (ConfigurationConstants.ADAPT_RESOURCE_FILE_CONTENTS_OPTION              .startsWith(nextWord)) configuration.adaptResourceFileContents             = parseCommaSeparatedList("resource file name", true, true, false, true, false, true, false, false, false, configuration.adaptResourceFileContents);
            else if (ConfigurationConstants.DONT_PROCESS_KOTLIN_METADATA                     .startsWith(nextWord)) configuration.dontProcessKotlinMetadata             = parseNoArgument(true);
            else if (ConfigurationConstants.KEEP_KOTLIN_METADATA                             .startsWith(nextWord)) configuration.keepKotlinMetadata                    = parseKeepKotlinMetadata();

            else if (ConfigurationConstants.DONT_PREVERIFY_OPTION                            .startsWith(nextWord)) configuration.preverify                             = parseNoArgument(false);
            else if (ConfigurationConstants.MICRO_EDITION_OPTION                             .startsWith(nextWord)) configuration.microEdition                          = parseNoArgument(true);
            else if (ConfigurationConstants.ANDROID_OPTION                                   .startsWith(nextWord)) configuration.android                               = parseNoArgument(true);

            else if (ConfigurationConstants.KEY_STORE_OPTION                                 .startsWith(nextWord)) configuration.keyStores                             = parseFiles(configuration.keyStores);
            else if (ConfigurationConstants.KEY_STORE_PASSWORD_OPTION                        .startsWith(nextWord)) configuration.keyStorePasswords                     = parseCommaSeparatedList("keystore password", true, false, false, false, false, false, true, false, false, configuration.keyStorePasswords);
            else if (ConfigurationConstants.KEY_ALIAS_OPTION                                 .startsWith(nextWord)) configuration.keyAliases                            = parseCommaSeparatedList("key", true, false, false, false, false, false, true, false, false, configuration.keyAliases);
            else if (ConfigurationConstants.KEY_PASSWORD_OPTION                              .startsWith(nextWord)) configuration.keyPasswords                          = parseCommaSeparatedList("key password", true, false, false, false, false, false, true, false, false, configuration.keyPasswords);

            else if (ConfigurationConstants.VERBOSE_OPTION                                   .startsWith(nextWord)) configuration.verbose                               = parseNoArgument(true);
            else if (ConfigurationConstants.DONT_NOTE_OPTION                                 .startsWith(nextWord)) configuration.note                                  = parseCommaSeparatedList("class name", true, true, false, false, true, false, false, true, false, configuration.note);
            else if (ConfigurationConstants.DONT_WARN_OPTION                                 .startsWith(nextWord)) configuration.warn                                  = parseCommaSeparatedList("class name", true, true, false, false, true, false, false, true, false, configuration.warn);
            else if (ConfigurationConstants.IGNORE_WARNINGS_OPTION                           .startsWith(nextWord)) configuration.ignoreWarnings                        = parseNoArgument(true);
            else if (ConfigurationConstants.PRINT_CONFIGURATION_OPTION                       .startsWith(nextWord)) configuration.printConfiguration                    = parseOptionalFile();
            else if (ConfigurationConstants.DUMP_OPTION                                      .startsWith(nextWord)) configuration.dump                                  = parseOptionalFile();
            else if (ConfigurationConstants.ADD_CONFIGURATION_DEBUGGING_OPTION               .startsWith(nextWord)) configuration.addConfigurationDebugging             = parseNoArgument(true);
            else
            {
                throw new ParseException("Unknown option " + reader.locationDescription());
            }
        }
    }


    /**
     * Closes the configuration.
     * @throws IOException if an IO error occurs while closing the configuration.
     */
    @Override
    public void close() throws IOException
    {
        if (reader != null)
        {
            reader.close();
        }
    }


    private boolean parseKeepKotlinMetadata() throws IOException
    {
        System.err.println("The `-keepkotlinmetadata` option is deprecated and will be removed in a future ProGuard release." +
                           "Please use `-keep class kotlin.Metadata` instead.");
        return parseNoArgument(true);
    }


    private long parseIncludeArgument(long lastModified) throws ParseException, IOException
    {
        // Read the configuration file name.
        readNextWord("configuration file name", true, true, false);

        URL baseURL = reader.getBaseURL();
        URL url     = null;

        try
        {
            // Check if the file name is a valid URL.
            url = new URL(nextWord);
        }
        catch (MalformedURLException ex) {}

        if (url != null)
        {
            reader.includeWordReader(new FileWordReader(url));
        }
        // Is it relative to a URL or to a file?
        else if (baseURL != null)
        {
            url = new URL(baseURL, nextWord);
            reader.includeWordReader(new FileWordReader(url));
        }
        else
        {
            // Is the file a valid resource URL?
            url = ConfigurationParser.class.getResource(nextWord);
            if (url != null)
            {
                reader.includeWordReader(new FileWordReader(url));
            }
            else
            {
                File file = file(nextWord);
                reader.includeWordReader(new FileWordReader(file));

                long fileLastModified = file.lastModified();
                if (fileLastModified > lastModified)
                {
                    lastModified = fileLastModified;
                }
            }
        }

        readNextWord();

        return lastModified;
    }


    private void parseBaseDirectoryArgument() throws ParseException, IOException
    {
        // Read the base directory name.
        readNextWord("base directory name", true, true, false);

        reader.setBaseDir(file(nextWord));

        readNextWord();
    }


    private ClassPath parseClassPathArgument(ClassPath classPath,
                                             boolean   isOutput,
                                             boolean   allowFeatureName)
    throws ParseException, IOException
    {
        // Create a new List if necessary.
        if (classPath == null)
        {
            classPath = new ClassPath();
        }

        // Read the first jar name.
        readNextWord("jar or directory name", true, false, false);

        // Do we have a suboption with a feature name instead?
        // The file name then comes out as an empty string.
        String featureName = null;

        if (allowFeatureName &&
            nextWord.length() == 0)
        {
            // Read the separator.
            readNextWord("separating '" + ConfigurationConstants.ARGUMENT_SEPARATOR_KEYWORD + "'");

            if (!ConfigurationConstants.ARGUMENT_SEPARATOR_KEYWORD.equals(nextWord))
            {
                throw new ParseException("Expecting separating '" + ConfigurationConstants.ARGUMENT_SEPARATOR_KEYWORD +
                                         "' or jar or directory name" +
                                         "' before " + reader.locationDescription());
            }

            // Read and remember the feature name.
            readNextWord("feature name", false, false, false);

            featureName = nextWord;

            // Read the first jar name.
            readNextWord("jar or directory name", true, false, false);
        }

        while (true)
        {
            // Create a new class path entry.
            ClassPathEntry entry = new ClassPathEntry(file(nextWord),
                                                      isOutput,
                                                      featureName);

            // Read the opening parenthesis or the separator, if any.
            readNextWord();

            // Read the optional filters.
            if (!configurationEnd() &&
                ConfigurationConstants.OPEN_ARGUMENTS_KEYWORD.equals(nextWord))
            {
                // Read all filters in an array.
                List[] filters = new List[7];

                int counter = 0;
                do
                {
                    // Read the filter.
                    filters[counter++] =
                        parseCommaSeparatedList("filter", true, true, true, true, false, true, true, false, false, null);
                }
                while (counter < filters.length &&
                       ConfigurationConstants.SEPARATOR_KEYWORD.equals(nextWord));

                // Make sure there is a closing parenthesis.
                if (!ConfigurationConstants.CLOSE_ARGUMENTS_KEYWORD.equals(nextWord))
                {
                    throw new ParseException("Expecting separating '" + ConfigurationConstants.ARGUMENT_SEPARATOR_KEYWORD +
                                             "' or '" + ConfigurationConstants.SEPARATOR_KEYWORD +
                                             "', or closing '" + ConfigurationConstants.CLOSE_ARGUMENTS_KEYWORD +
                                             "' before " + reader.locationDescription());
                }

                // Set all filters from the array on the entry.
                entry.setFilter(filters[--counter]);
                if (counter > 0)
                {
                    entry.setJarFilter(filters[--counter]);
                    if (counter > 0)
                    {
                        entry.setWarFilter(filters[--counter]);
                        if (counter > 0)
                        {
                            entry.setEarFilter(filters[--counter]);
                            if (counter > 0)
                            {
                                entry.setJmodFilter(filters[--counter]);
                                if (counter > 0)
                                {
                                    entry.setZipFilter(filters[--counter]);
                                    if (counter > 0)
                                    {
                                        // For backward compatibility, the
                                        // jmod/aar/aab/apk filters come first
                                        // in the list.
                                        entry.setApkFilter(filters[--counter]);
                                        if (counter > 0)
                                        {
                                            entry.setAabFilter(filters[--counter]);
                                            if (counter > 0)
                                            {
                                                entry.setAarFilter(filters[--counter]);
                                                if (counter > 0)
                                                {
                                                    entry.setJmodFilter(filters[--counter]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Read the separator, if any.
                readNextWord();
            }

            // Add the entry to the list.
            classPath.add(entry);

            if (configurationEnd())
            {
                return classPath;
            }

            if (!nextWord.equals(ConfigurationConstants.JAR_SEPARATOR_KEYWORD))
            {
                throw new ParseException("Expecting class path separator '" + ConfigurationConstants.JAR_SEPARATOR_KEYWORD +
                                         "' before " + reader.locationDescription());
            }

            // Read the next jar name.
            readNextWord("jar or directory name", true, false, false);
        }
    }


    private int parseClassVersion()
    throws ParseException, IOException
    {
        // Read the obligatory target.
        readNextWord("java version");

        int classVersion = ClassUtil.internalClassVersion(nextWord);
        if (classVersion == 0)
        {
            throw new ParseException("Unsupported java version " + reader.locationDescription());
        }

        readNextWord();

        return classVersion;
    }


    private int parseIntegerArgument()
    throws ParseException, IOException
    {
        try
        {
            // Read the obligatory integer.
            readNextWord("integer");

            int integer = Integer.parseInt(nextWord);
            if (integer <= 0)
            {
                throw new ParseException("Expecting value larger than 0, instead of '" + nextWord +
                                         "' before " + reader.locationDescription());
            }

            readNextWord();

            return integer;
        }
        catch (NumberFormatException e)
        {
            throw new ParseException("Expecting integer argument instead of '" + nextWord +
                                     "' before " + reader.locationDescription());
        }
    }


    private URL parseURL()
    throws ParseException, IOException
    {
        // Read the obligatory file name.
        readNextWord("file name", true, true, false);

        // Make sure the file is properly resolved.
        URL url = url(nextWord);

        readNextWord();

        return url;
    }


    private List parseFiles(List files)
    throws ParseException, IOException
    {
        if (files == null)
        {
            files = new ArrayList();
        }

        files.add(parseFile());

        return files;
    }


    private File parseFile()
    throws ParseException, IOException
    {
        // Read the obligatory file name.
        readNextWord("file name", true, true, false);

        // Make sure the file is properly resolved.
        File file = file(nextWord);

        readNextWord();

        return file;
    }


    private File parseOptionalFile()
    throws ParseException, IOException
    {
        // Read the optional file name.
        readNextWord(true, true);

        // Didn't the user specify a file name?
        if (configurationEnd())
        {
            return Configuration.STD_OUT;
        }

        // Make sure the file is properly resolved.
        File file = file(nextWord);

        readNextWord();

        return file;
    }


    private String parseOptionalArgument() throws IOException
    {
        // Read the optional argument.
        readNextWord();

        // Didn't the user specify an argument?
        if (configurationEnd())
        {
            return "";
        }

        String argument = nextWord;

        readNextWord();

        return argument;
    }


    private boolean parseNoArgument(boolean value) throws IOException
    {
        readNextWord();

        return value;
    }


    private long parseNoArgument(long value) throws IOException
    {
        readNextWord();

        return value;
    }


    /**
     * Parses and adds a conditional class specification to keep other classes
     * and class members.
     * For example: -if "public class SomeClass { void someMethod(); } -keep"
     * @throws ParseException if the class specification contains a syntax error.
     * @throws IOException    if an IO error occurs while reading the class
     *                        specification.
     */
    private List parseIfCondition(List keepClassSpecifications)
    throws ParseException, IOException
    {
        // Read the condition.
        ClassSpecification condition =
            parseClassSpecificationArguments(true, true, false);

        // Read the corresponding keep option.
        if (nextWord == null)
        {
            throw new ParseException("Expecting '-keep' option after '-if' option, before " + reader.locationDescription());
        }

        if      (ConfigurationConstants.KEEP_OPTION                          .startsWith(nextWord)) keepClassSpecifications = parseKeepClassSpecificationArguments(keepClassSpecifications, true,  true,  false, false, false, condition);
        else if (ConfigurationConstants.KEEP_CLASS_MEMBERS_OPTION            .startsWith(nextWord)) keepClassSpecifications = parseKeepClassSpecificationArguments(keepClassSpecifications, false, true,  false, false, false, condition);
        else if (ConfigurationConstants.KEEP_CLASSES_WITH_MEMBERS_OPTION     .startsWith(nextWord)) keepClassSpecifications = parseKeepClassSpecificationArguments(keepClassSpecifications, false, true,  false, true,  false, condition);
        else if (ConfigurationConstants.KEEP_NAMES_OPTION                    .startsWith(nextWord)) keepClassSpecifications = parseKeepClassSpecificationArguments(keepClassSpecifications, true,  true,  false, false, true,  condition);
        else if (ConfigurationConstants.KEEP_CLASS_MEMBER_NAMES_OPTION       .startsWith(nextWord)) keepClassSpecifications = parseKeepClassSpecificationArguments(keepClassSpecifications, false, true,  false, false, true,  condition);
        else if (ConfigurationConstants.KEEP_CLASSES_WITH_MEMBER_NAMES_OPTION.startsWith(nextWord)) keepClassSpecifications = parseKeepClassSpecificationArguments(keepClassSpecifications, false, true,  false, true,  true,  condition);
        else if (ConfigurationConstants.KEEP_CODE_OPTION                     .startsWith(nextWord)) keepClassSpecifications = parseKeepClassSpecificationArguments(keepClassSpecifications, false, false, true,  false, false, condition);
        else
        {
            throw new ParseException("Expecting '-keep' option after '-if' option, before " + reader.locationDescription());
        }

        return keepClassSpecifications;
    }


    /**
     * Parses and adds a class specification to keep classes and class members.
     * For example: -keep "public class SomeClass { void someMethod(); }"
     * @throws ParseException if the class specification contains a syntax error.
     * @throws IOException    if an IO error occurs while reading the class
     *                        specification.
     */
    private List parseKeepClassSpecificationArguments(List               keepClassSpecifications,
                                                      boolean            markClasses,
                                                      boolean            markMembers,
                                                      boolean            markCodeAttributes,
                                                      boolean            markConditionally,
                                                      boolean            allowShrinking,
                                                      ClassSpecification condition)
    throws ParseException, IOException
    {
        // Create a new List if necessary.
        if (keepClassSpecifications == null)
        {
            keepClassSpecifications = new ArrayList();
        }

        // Read and add the keep configuration.
        keepClassSpecifications.add(parseKeepClassSpecificationArguments(markClasses,
                                                                         markMembers,
                                                                         markCodeAttributes,
                                                                         markConditionally,
                                                                         allowShrinking,
                                                                         condition));
        return keepClassSpecifications;
    }


    /**
     * Parses and returns a class specification to keep classes and class
     * members.
     * For example: -keep "public class SomeClass { void someMethod(); }"
     * @throws ParseException if the class specification contains a syntax error.
     * @throws IOException    if an IO error occurs while reading the class
     *                        specification.
     */
    private KeepClassSpecification parseKeepClassSpecificationArguments(boolean            markClasses,
                                                                        boolean            markMembers,
                                                                        boolean            markCodeAttributes,
                                                                        boolean            markConditionally,
                                                                        boolean            allowShrinking,
                                                                        ClassSpecification condition)
    throws ParseException, IOException
    {
        boolean markDescriptorClasses = false;
        //boolean allowShrinking        = false;
        boolean allowOptimization     = false;
        boolean allowObfuscation      = false;

        // Read the keep modifiers.
        while (true)
        {
            readNextWord("keyword '" + ConfigurationConstants.CLASS_KEYWORD +
                         "', '"      + JavaAccessConstants.INTERFACE +
                         "', or '"   + JavaAccessConstants.ENUM + "'",
                         false, false, true);

            if (!ConfigurationConstants.ARGUMENT_SEPARATOR_KEYWORD.equals(nextWord))
            {
                // Not a comma. Stop parsing the keep modifiers.
                break;
            }

            readNextWord("keyword '" + ConfigurationConstants.ALLOW_SHRINKING_SUBOPTION +
                         "', '"      + ConfigurationConstants.ALLOW_OPTIMIZATION_SUBOPTION +
                         "', or '"   + ConfigurationConstants.ALLOW_OBFUSCATION_SUBOPTION + "'");

            if      (ConfigurationConstants.INCLUDE_DESCRIPTOR_CLASSES_SUBOPTION.startsWith(nextWord))
            {
                markDescriptorClasses = true;
            }
            else if (ConfigurationConstants.INCLUDE_CODE_SUBOPTION              .startsWith(nextWord))
            {
                markCodeAttributes    = true;
            }
            else if (ConfigurationConstants.ALLOW_SHRINKING_SUBOPTION           .startsWith(nextWord))
            {
                allowShrinking        = true;
            }
            else if (ConfigurationConstants.ALLOW_OPTIMIZATION_SUBOPTION        .startsWith(nextWord))
            {
                allowOptimization     = true;
            }
            else if (ConfigurationConstants.ALLOW_OBFUSCATION_SUBOPTION         .startsWith(nextWord))
            {
                allowObfuscation      = true;
            }
            else
            {
                throw new ParseException("Expecting keyword '" + ConfigurationConstants.INCLUDE_DESCRIPTOR_CLASSES_SUBOPTION +
                                         "', '"                + ConfigurationConstants.INCLUDE_CODE_SUBOPTION +
                                         "', '"                + ConfigurationConstants.ALLOW_SHRINKING_SUBOPTION +
                                         "', '"                + ConfigurationConstants.ALLOW_OPTIMIZATION_SUBOPTION +
                                         "', or '"             + ConfigurationConstants.ALLOW_OBFUSCATION_SUBOPTION +
                                         "' before " + reader.locationDescription());
            }
        }

        // Read the class configuration.
        ClassSpecification classSpecification =
            parseClassSpecificationArguments(false, true, false);

        // Create and return the keep configuration.
        return new KeepClassSpecification(markClasses,
                                          markMembers,
                                          markConditionally,
                                          markDescriptorClasses,
                                          markCodeAttributes,
                                          allowShrinking,
                                          allowOptimization,
                                          allowObfuscation,
                                          condition,
                                          classSpecification);
    }


    /**
     * Parses and adds a class specification for optimization assumptions.
     * For example: -assumenosideeffects "public class Simple { void simple(); }"
     * @throws ParseException if the class specification contains a syntax error.
     * @throws IOException    if an IO error occurs while reading the class
     *                        specification.
     */
    private List parseAssumeClassSpecificationArguments(List classSpecifications)
    throws ParseException, IOException
    {
        // Create a new List if necessary.
        if (classSpecifications == null)
        {
            classSpecifications = new ArrayList();
        }

        // Read and add the class configuration.
        classSpecifications.add(parseClassSpecificationArguments(true, true, true));

        return classSpecifications;
    }


    /**
     * Parses and adds a class specification.
     * For example: "class SomeClass { void someMethod(); }"
     * @throws ParseException if the class specification contains a syntax error.
     * @throws IOException    if an IO error occurs while reading the class
     *                        specification.
     */
    private List<ClassSpecification> parseClassSpecificationArguments(List<ClassSpecification> classSpecifications)
    throws ParseException, IOException
    {
        // Create a new List if necessary.
        if (classSpecifications == null)
        {
            classSpecifications = new ArrayList();
        }

        // Read and add the keep configuration.
        classSpecifications.add(parseClassSpecificationArguments(true, true, false));

        return classSpecifications;
    }


    // Added for compatibility with older versions of ProGuard (see PGD-755).
    public ClassSpecification parseClassSpecificationArguments()
    throws ParseException, IOException
    {
        return parseClassSpecificationArguments(false, true, false);
    }


    /**
     * Parses and returns a class specification.
     * For example: "public class SomeClass { public void someMethod(); }"
     * @throws ParseException if the class specification contains a syntax error.
     * @throws IOException    if an IO error occurs while reading the class
     *                        specification.
     */
    public ClassSpecification parseClassSpecificationArguments(boolean readFirstWord,
                                                               boolean allowClassMembers,
                                                               boolean allowValues)
    throws ParseException, IOException
    {
        if (readFirstWord)
        {
            readNextWord("keyword '" + ConfigurationConstants.CLASS_KEYWORD +
                         "', '"      + JavaAccessConstants.INTERFACE +
                         "', or '"   + JavaAccessConstants.ENUM + "'",
                         false, false, true);
        }

        // Clear the annotation type.
        String annotationType = null;

        // Clear the class access modifiers.
        int requiredSetClassAccessFlags   = 0;
        int requiredUnsetClassAccessFlags = 0;

        // Parse the class annotations and access modifiers until the class keyword.
        while (!ConfigurationConstants.CLASS_KEYWORD.equals(nextWord))
        {
            // Strip the negating sign, if any.
            boolean negated =
                nextWord.startsWith(ConfigurationConstants.NEGATOR_KEYWORD);

            String strippedWord = negated ?
                nextWord.substring(1) :
                nextWord;

            // Parse the class access modifiers.
            int accessFlag =
                strippedWord.equals(JavaAccessConstants.PUBLIC)     ? AccessConstants.PUBLIC      :
                strippedWord.equals(JavaAccessConstants.FINAL)      ? AccessConstants.FINAL       :
                strippedWord.equals(JavaAccessConstants.INTERFACE)  ? AccessConstants.INTERFACE   :
                strippedWord.equals(JavaAccessConstants.ABSTRACT)   ? AccessConstants.ABSTRACT    :
                strippedWord.equals(JavaAccessConstants.SYNTHETIC)  ? AccessConstants.SYNTHETIC   :
                strippedWord.equals(JavaAccessConstants.ANNOTATION) ? AccessConstants.ANNOTATION  :
                strippedWord.equals(JavaAccessConstants.ENUM)       ? AccessConstants.ENUM        :
                                                                      unknownAccessFlag();

            // Is it an annotation modifier?
            if (accessFlag == AccessConstants.ANNOTATION)
            {
                readNextWord("annotation type or keyword '" + JavaAccessConstants.INTERFACE + "'",
                             false, false, false);

                // Is the next word actually an annotation type?
                if (!nextWord.equals(JavaAccessConstants.INTERFACE) &&
                    !nextWord.equals(JavaAccessConstants.ENUM)      &&
                    !nextWord.equals(ConfigurationConstants.CLASS_KEYWORD))
                {
                    // Parse the annotation type.
                    annotationType =
                        ListUtil.commaSeparatedString(
                        parseCommaSeparatedList("annotation type",
                                                false, false, false, false, true, false, false, false, true, null), false);

                    // Continue parsing the access modifier that we just read
                    // in the next cycle.
                    continue;
                }

                // Otherwise just handle the annotation modifier.
            }

            if (!negated)
            {
                requiredSetClassAccessFlags   |= accessFlag;
            }
            else
            {
                requiredUnsetClassAccessFlags |= accessFlag;
            }

            if ((requiredSetClassAccessFlags &
                 requiredUnsetClassAccessFlags) != 0)
            {
                throw new ParseException("Conflicting class access modifiers for '" + strippedWord +
                                         "' before " + reader.locationDescription());
            }

            if (strippedWord.equals(JavaAccessConstants.INTERFACE) ||
                strippedWord.equals(JavaAccessConstants.ENUM)      ||
                strippedWord.equals(ConfigurationConstants.CLASS_KEYWORD))
            {
                // The interface or enum keyword. Stop parsing the class flags.
                break;
            }

            // Should we read the next word?
            if (accessFlag != AccessConstants.ANNOTATION)
            {
                readNextWord("keyword '" + ConfigurationConstants.CLASS_KEYWORD +
                             "', '"      + JavaAccessConstants.INTERFACE +
                             "', or '"   + JavaAccessConstants.ENUM + "'",
                             false, false, true);
            }
        }

       // Parse the class name part.
        String externalClassName =
            ListUtil.commaSeparatedString(
            parseCommaSeparatedList("class name or interface name",
                                    true, false, false, false, true, false, false, false, false, null), false);

        // For backward compatibility, allow a single "*" wildcard to match any
        // class.
        String className = ConfigurationConstants.ANY_CLASS_KEYWORD.equals(externalClassName) ?
            null :
            ClassUtil.internalClassName(externalClassName);

        // Clear the annotation type and the class name of the extends part.
        String extendsAnnotationType = null;
        String extendsClassName      = null;

        if (allowClassMembers && !configurationEnd())
        {
            // Parse 'implements ...' or 'extends ...' part, if any.
            if (ConfigurationConstants.IMPLEMENTS_KEYWORD.equals(nextWord) ||
                ConfigurationConstants.EXTENDS_KEYWORD.equals(nextWord))
            {
                readNextWord("class name or interface name", false, false, true);

                // Parse the annotation type, if any.
                if (ConfigurationConstants.ANNOTATION_KEYWORD.equals(nextWord))
                {
                    extendsAnnotationType =
                        ListUtil.commaSeparatedString(
                        parseCommaSeparatedList("annotation type",
                                                true, false, false, false, true, false, false, false, true, null), false);
                }

                String externalExtendsClassName =
                    ListUtil.commaSeparatedString(
                    parseCommaSeparatedList("class name or interface name",
                                            false, false, false, false, true, false, false, false, false, null), false);

                extendsClassName = ConfigurationConstants.ANY_CLASS_KEYWORD.equals(externalExtendsClassName) ?
                    null :
                    ClassUtil.internalClassName(externalExtendsClassName);
            }
        }

        // Create the basic class specification.
        ClassSpecification classSpecification =
            new ClassSpecification(lastComments,
                                   requiredSetClassAccessFlags,
                                   requiredUnsetClassAccessFlags,
                                   annotationType,
                                   className,
                                   extendsAnnotationType,
                                   extendsClassName);


        // Now add any class members to this class specification.
        if (allowClassMembers && !configurationEnd())
        {
            // Check the class member opening part.
            if (!ConfigurationConstants.OPEN_KEYWORD.equals(nextWord))
            {
                throw new ParseException("Expecting opening '" + ConfigurationConstants.OPEN_KEYWORD +
                                         "' at " + reader.locationDescription());
            }

            // Parse all class members.
            while (true)
            {
                readNextWord("class member description" +
                             " or closing '" + ConfigurationConstants.CLOSE_KEYWORD + "'",
                             false, false, true);

                if (nextWord.equals(ConfigurationConstants.CLOSE_KEYWORD))
                {
                    lastComments = reader.lastComments();
                    classSpecification.memberComments = lastComments;

                    // The closing brace. Stop parsing the class members.
                    readNextWord();

                    break;
                }

                parseMemberSpecificationArguments(externalClassName,
                                                  allowValues,
                                                  classSpecification);
            }
        }

        return classSpecification;
    }


    /**
     * Parses and adds a class member specification.
     * @throws ParseException if the class specification contains a syntax error.
     * @throws IOException    if an IO error occurs while reading the class
     *                        specification.
     */
    private void parseMemberSpecificationArguments(String             externalClassName,
                                                   boolean            allowValues,
                                                   ClassSpecification classSpecification)
    throws ParseException, IOException
    {
        // Clear the annotation name.
        String annotationType = null;

        // Parse the class member access modifiers, if any.
        int requiredSetMemberAccessFlags   = 0;
        int requiredUnsetMemberAccessFlags = 0;

        while (!configurationEnd(true))
        {
            // Parse the annotation type, if any.
            if (ConfigurationConstants.ANNOTATION_KEYWORD.equals(nextWord))
            {
                annotationType =
                    ListUtil.commaSeparatedString(
                    parseCommaSeparatedList("annotation type",
                                            true, false, false, false, true, false, false, false, true, null), false);
                continue;
            }

            String strippedWord = nextWord.startsWith("!") ?
                nextWord.substring(1) :
                nextWord;

            // Parse the class member access modifiers.
            int accessFlag =
                strippedWord.equals(JavaAccessConstants.PUBLIC)       ? AccessConstants.PUBLIC       :
                strippedWord.equals(JavaAccessConstants.PRIVATE)      ? AccessConstants.PRIVATE      :
                strippedWord.equals(JavaAccessConstants.PROTECTED)    ? AccessConstants.PROTECTED    :
                strippedWord.equals(JavaAccessConstants.STATIC)       ? AccessConstants.STATIC       :
                strippedWord.equals(JavaAccessConstants.FINAL)        ? AccessConstants.FINAL        :
                strippedWord.equals(JavaAccessConstants.SYNCHRONIZED) ? AccessConstants.SYNCHRONIZED :
                strippedWord.equals(JavaAccessConstants.VOLATILE)     ? AccessConstants.VOLATILE     :
                strippedWord.equals(JavaAccessConstants.TRANSIENT)    ? AccessConstants.TRANSIENT    :
                strippedWord.equals(JavaAccessConstants.BRIDGE)       ? AccessConstants.BRIDGE       :
                strippedWord.equals(JavaAccessConstants.VARARGS)      ? AccessConstants.VARARGS      :
                strippedWord.equals(JavaAccessConstants.NATIVE)       ? AccessConstants.NATIVE       :
                strippedWord.equals(JavaAccessConstants.ABSTRACT)     ? AccessConstants.ABSTRACT     :
                strippedWord.equals(JavaAccessConstants.STRICT)       ? AccessConstants.STRICT       :
                strippedWord.equals(JavaAccessConstants.SYNTHETIC)    ? AccessConstants.SYNTHETIC    :
                                                                        0;
            if (accessFlag == 0)
            {
                // Not a class member access modifier. Stop parsing them.
                break;
            }

            if (strippedWord.equals(nextWord))
            {
                requiredSetMemberAccessFlags   |= accessFlag;
            }
            else
            {
                requiredUnsetMemberAccessFlags |= accessFlag;
            }

            // Make sure the user doesn't try to set and unset the same
            // access flags simultaneously.
            if ((requiredSetMemberAccessFlags &
                 requiredUnsetMemberAccessFlags) != 0)
            {
                throw new ParseException("Conflicting class member access modifiers for " +
                                         reader.locationDescription());
            }

            readNextWord("class member description");
        }

        // Parse the class member type and name part.

        // Did we get a special wildcard?
        if (ConfigurationConstants.ANY_CLASS_MEMBER_KEYWORD.equals(nextWord) ||
            ConfigurationConstants.ANY_FIELD_KEYWORD       .equals(nextWord) ||
            ConfigurationConstants.ANY_METHOD_KEYWORD      .equals(nextWord))
        {
            // Act according to the type of wildcard.
            if (ConfigurationConstants.ANY_CLASS_MEMBER_KEYWORD.equals(nextWord))
            {
                checkFieldAccessFlags(requiredSetMemberAccessFlags,
                                      requiredUnsetMemberAccessFlags);
                checkMethodAccessFlags(requiredSetMemberAccessFlags,
                                       requiredUnsetMemberAccessFlags);

                classSpecification.addField(
                    new MemberSpecification(requiredSetMemberAccessFlags,
                                            requiredUnsetMemberAccessFlags,
                                            annotationType,
                                            null,
                                            null));
                classSpecification.addMethod(
                    new MemberSpecification(requiredSetMemberAccessFlags,
                                            requiredUnsetMemberAccessFlags,
                                            annotationType,
                                            null,
                                            null));
            }
            else if (ConfigurationConstants.ANY_FIELD_KEYWORD.equals(nextWord))
            {
                checkFieldAccessFlags(requiredSetMemberAccessFlags,
                                      requiredUnsetMemberAccessFlags);

                classSpecification.addField(
                    new MemberSpecification(requiredSetMemberAccessFlags,
                                            requiredUnsetMemberAccessFlags,
                                            annotationType,
                                            null,
                                            null));
            }
            else if (ConfigurationConstants.ANY_METHOD_KEYWORD.equals(nextWord))
            {
                checkMethodAccessFlags(requiredSetMemberAccessFlags,
                                       requiredUnsetMemberAccessFlags);

                classSpecification.addMethod(
                    new MemberSpecification(requiredSetMemberAccessFlags,
                                            requiredUnsetMemberAccessFlags,
                                            annotationType,
                                            null,
                                            null));
            }

            // We still have to read the closing separator.
            readNextWord("separator '" + ConfigurationConstants.SEPARATOR_KEYWORD + "'");

            if (!ConfigurationConstants.SEPARATOR_KEYWORD.equals(nextWord))
            {
                throw new ParseException("Expecting separator '" + ConfigurationConstants.SEPARATOR_KEYWORD +
                                         "' before " + reader.locationDescription());
            }
        }
        else
        {
            // Make sure we have a proper type.
            checkJavaIdentifier("java type");
            String type         = nextWord;
            String typeLocation = reader.locationDescription();

            readNextWord("class member name");
            String name = nextWord;

            // Did we get just one word before the opening parenthesis?
            if (ConfigurationConstants.OPEN_ARGUMENTS_KEYWORD.equals(name))
            {
                // This must be a constructor then.
                // Make sure the type is a proper constructor name.
                if (!(type.equals(ClassConstants.METHOD_NAME_INIT) ||
                      type.equals(externalClassName) ||
                      type.equals(ClassUtil.externalShortClassName(externalClassName))))
                {
                    throw new ParseException("Expecting type and name " +
                                             "instead of just '" + type +
                                             "' before " + reader.locationDescription());
                }

                // Assign the fixed constructor type and name.
                type = JavaTypeConstants.VOID;
                name = ClassConstants.METHOD_NAME_INIT;
            }
            else
            {
                // It's not a constructor.
                // Make sure we have a proper name.
                checkJavaIdentifier("class member name");

                // Read the opening parenthesis or the separating
                // semi-colon.
                readNextWord("opening '" + ConfigurationConstants.OPEN_ARGUMENTS_KEYWORD +
                             "' or separator '" + ConfigurationConstants.SEPARATOR_KEYWORD + "'");
            }

            // Check if the type actually contains the use of generics.
            // Can not do it right away as we also support "<init>" as a type (see case above).
            if (containsGenerics(type))
            {
                throw new ParseException("Generics are not allowed (erased) for java type" + typeLocation);
            }

            // Are we looking at a field, a method, or something else?
            if (ConfigurationConstants.SEPARATOR_KEYWORD.equals(nextWord))
            {
                // It's a field.
                checkFieldAccessFlags(requiredSetMemberAccessFlags,
                                      requiredUnsetMemberAccessFlags);

                // We already have a field descriptor.
                String descriptor = ClassUtil.internalType(type);

                if (ConfigurationConstants.ANY_FIELD_KEYWORD.equals(name))
                {
                    throw new ParseException("Not expecting field type before with wildcard '" + ConfigurationConstants.ANY_FIELD_KEYWORD +
                                             "before " + reader.locationDescription() +
                                             " (use '" + ConfigurationConstants.ANY_CLASS_MEMBER_KEYWORD + "' instead)");
                }

                // Add the field.
                classSpecification.addField(
                    new MemberSpecification(requiredSetMemberAccessFlags,
                                            requiredUnsetMemberAccessFlags,
                                            annotationType,
                                            name,
                                            descriptor));
            }
            else if (allowValues &&
                     (ConfigurationConstants.EQUAL_KEYWORD.equals(nextWord) ||
                      ConfigurationConstants.RETURN_KEYWORD.equals(nextWord)))
            {
                // It's a field with a value.
                checkFieldAccessFlags(requiredSetMemberAccessFlags,
                                      requiredUnsetMemberAccessFlags);

                // We already have a field descriptor.
                String descriptor = ClassUtil.internalType(type);

                // Read the constant.
                Number[] values = parseValues(type, descriptor);

                // Read the separator after the constant.
                readNextWord("separator '" + ConfigurationConstants.SEPARATOR_KEYWORD + "'");

                if (!ConfigurationConstants.SEPARATOR_KEYWORD.equals(nextWord))
                {
                    throw new ParseException("Expecting separator '" + ConfigurationConstants.SEPARATOR_KEYWORD +
                                             "' before " + reader.locationDescription());
                }

                // Add the field.
                classSpecification.addField(
                    new MemberValueSpecification(requiredSetMemberAccessFlags,
                                                 requiredUnsetMemberAccessFlags,
                                                 annotationType,
                                                 name,
                                                 descriptor,
                                                 values));
            }
            else if (ConfigurationConstants.OPEN_ARGUMENTS_KEYWORD.equals(nextWord))
            {
                // It's a method.
                checkMethodAccessFlags(requiredSetMemberAccessFlags,
                                       requiredUnsetMemberAccessFlags);

                // Parse the method arguments.
                String descriptor =
                    ClassUtil.internalMethodDescriptor(type,
                                                       parseCommaSeparatedList("argument", true, true, true, false, true, false, false, false, false, null));

                if (!ConfigurationConstants.CLOSE_ARGUMENTS_KEYWORD.equals(nextWord))
                {
                    throw new ParseException("Expecting separating '" + ConfigurationConstants.ARGUMENT_SEPARATOR_KEYWORD +
                                             "' or closing '" + ConfigurationConstants.CLOSE_ARGUMENTS_KEYWORD +
                                             "' before " + reader.locationDescription());
                }

                // Read the separator after the closing parenthesis.
                readNextWord("separator '" + ConfigurationConstants.SEPARATOR_KEYWORD + "'");

                if (ConfigurationConstants.SEPARATOR_KEYWORD.equals(nextWord))
                {
                    if (ConfigurationConstants.ANY_METHOD_KEYWORD.equals(name))
                    {
                        throw new ParseException("Not expecting method descriptor with wildcard '" + ConfigurationConstants.ANY_METHOD_KEYWORD +
                                                 "before " + reader.locationDescription() +
                                                 " (use '" + ConfigurationConstants.ANY_CLASS_MEMBER_KEYWORD + "' instead)");
                    }

                    // Add the plain method.
                    classSpecification.addMethod(
                        new MemberSpecification(requiredSetMemberAccessFlags,
                                                requiredUnsetMemberAccessFlags,
                                                annotationType,
                                                name,
                                                descriptor));
                }
                else if (allowValues &&
                         (ConfigurationConstants.EQUAL_KEYWORD.equals(nextWord) ||
                          ConfigurationConstants.RETURN_KEYWORD.equals(nextWord)))
                {
                    // It's a method with a value.
                    checkFieldAccessFlags(requiredSetMemberAccessFlags,
                                          requiredUnsetMemberAccessFlags);

                    // Read the constant.
                    Number[] values = parseValues(type, ClassUtil.internalType(type));

                    // Read the separator after the constant.
                    readNextWord("separator '" + ConfigurationConstants.SEPARATOR_KEYWORD + "'");

                    if (!ConfigurationConstants.SEPARATOR_KEYWORD.equals(nextWord))
                    {
                        throw new ParseException("Expecting separator '" + ConfigurationConstants.SEPARATOR_KEYWORD +
                                                 "' before " + reader.locationDescription());
                    }

                    // Add the method.
                    classSpecification.addMethod(
                        new MemberValueSpecification(requiredSetMemberAccessFlags,
                                                     requiredUnsetMemberAccessFlags,
                                                     annotationType,
                                                     name,
                                                     descriptor,
                                                     values));
                }
                else
                {
                    throw new ParseException("Expecting separator '" + ConfigurationConstants.SEPARATOR_KEYWORD +
                                             "' before " + reader.locationDescription());

                }
            }
            else
            {
                // It doesn't look like a field or a method.
                throw new ParseException("Expecting opening '" + ConfigurationConstants.OPEN_ARGUMENTS_KEYWORD +
                                         "' or separator '" + ConfigurationConstants.SEPARATOR_KEYWORD +
                                         "' before " + reader.locationDescription());
            }
        }
    }


    /**
     * Reads a value or value range of the given primitive type.
     * For example, values "123" or "100..199" of type "int" ("I").
     */
    private Number[] parseValues(String externalType,
                                 String internalType)
    throws ParseException, IOException
    {
        readNextWord(externalType + " constant");

        int rangeIndex = nextWord.indexOf(ConfigurationConstants.RANGE_KEYWORD);
        return rangeIndex >= 0 ?
            new Number[]
            {
                parseValue(externalType, internalType, nextWord.substring(0, rangeIndex)),
                parseValue(externalType, internalType, nextWord.substring(rangeIndex + ConfigurationConstants.RANGE_KEYWORD.length()))
            } :
            new Number[]
            {
                parseValue(externalType, internalType, nextWord)
            };
    }


    /**
     * Parses the given string as a value of the given primitive type.
     * For example, value "123" of type "int" ("I").
     * For example, value "true" of type "boolean" ("Z"), returned as 1.
     */
    private Number parseValue(String externalType,
                              String internalType,
                              String string)
    throws ParseException
    {
        try
        {
            string = replaceSystemProperties(string);

            switch (internalType.charAt(0))
            {
                case TypeConstants.BOOLEAN:
                {
                    return parseBoolean(string);
                }
                case TypeConstants.BYTE:
                case TypeConstants.CHAR:
                case TypeConstants.SHORT:
                case TypeConstants.INT:
                {
                    return Integer.decode(string);
                }
                //case TypeConstants.LONG:
                //{
                //    return Long.decode(string);
                //}
                //case TypeConstants.FLOAT:
                //{
                //    return Float.valueOf(string);
                //}
                //case TypeConstants.DOUBLE:
                //{
                //    return Double.valueOf(string);
                //}
                default:
                {
                    throw new ParseException("Can't handle '" + externalType +
                                             "' constant " + reader.locationDescription());
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new ParseException("Can't parse " + externalType +
                                     " constant " + reader.locationDescription());
        }
    }


    /**
     * Parses the given boolean string as an integer (0 or 1).
     */
    private Integer parseBoolean(String string)
    throws ParseException
    {
        if      (ConfigurationConstants.FALSE_KEYWORD.equals(nextWord))
        {
            return Integer.valueOf(0);
        }
        else if (ConfigurationConstants.TRUE_KEYWORD.equals(nextWord))
        {
            return Integer.valueOf(1);
        }
        else
        {
            throw new ParseException("Unknown boolean constant " + reader.locationDescription());
        }
    }


    /**
     * Reads a comma-separated list of Lists of java identifiers or of file
     * names.
     */
    private List parseCommaSeparatedLists(String  expectedDescription,
                                          boolean readFirstWord,
                                          boolean allowEmptyList,
                                          boolean expectClosingParenthesis,
                                          boolean isFileName,
                                          boolean checkJavaIdentifiers,
                                          boolean allowGenerics,
                                          boolean replaceSystemProperties,
                                          boolean replaceExternalClassNames,
                                          boolean replaceExternalTypes,
                                          List    list)
    throws ParseException, IOException
    {
        if (list == null)
        {
            list = new ArrayList();
        }

        // Parse a new list and add it to our list.
        list.add(parseCommaSeparatedList(expectedDescription,
                                         readFirstWord,
                                         allowEmptyList,
                                         expectClosingParenthesis,
                                         isFileName,
                                         checkJavaIdentifiers,
                                         allowGenerics,
                                         replaceSystemProperties,
                                         replaceExternalClassNames,
                                         replaceExternalTypes,
                                         null));
        return list;
    }


    /**
     * Reads a comma-separated list of java identifiers or of file names.
     * Examples of invocation arguments:
     *
     *   expected           read   allow  expect  is     check  allow   replace replace replace
     *   description        First  empty  Closing File   Java   Generic System  Extern  Extern
     *                      Word   List   Paren   Name   Id             Prop    Class   Types
     *   ----------------------------------------------------------------------------------
     *   ("directory name", true,  true,  false,  true,  false, true,   true,   false,  false, ...)
     *   ("optimization",   true,  false, false,  false, false, true,   false,  false,  false, ...)
     *   ("package name",   true,  true,  false,  false, true,  false,  false,  true,   false, ...)
     *   ("attribute name", true,  true,  false,  false, true,  false,  false,  false,  false, ...)
     *   ("class name",     true,  true,  false,  false, true,  false,  false,  true,   false, ...)
     *   ("filter",         true,  true,  true,   true,  false, true,   true,   false,  false, ...)
     *   ("annotation ",    false, false, false,  false, true,  false,  false,  false,  true,  ...)
     *   ("class name ",    true,  false, false,  false, true,  false,  false,  false,  false, ...)
     *   ("annotation ",    true,  false, false,  false, true,  false,  false,  false,  true,  ...)
     *   ("class name ",    false, false, false,  false, true,  false,  false,  false,  false, ...)
     *   ("annotation ",    true,  false, false,  false, true,  false,  false,  false,  true,  ...)
     *   ("argument",       true,  true,  true,   false, true,  false,  false,  false,  false, ...)
     */
    private List parseCommaSeparatedList(String  expectedDescription,
                                         boolean readFirstWord,
                                         boolean allowEmptyList,
                                         boolean expectClosingParenthesis,
                                         boolean isFileName,
                                         boolean checkJavaIdentifiers,
                                         boolean allowGenerics,
                                         boolean replaceSystemProperties,
                                         boolean replaceExternalClassNames,
                                         boolean replaceExternalTypes,
                                         List    list)
    throws ParseException, IOException
    {
        return parseCommaSeparatedList(expectedDescription,
                                       readFirstWord,
                                       allowEmptyList,
                                       null,
                                       expectClosingParenthesis,
                                       isFileName,
                                       checkJavaIdentifiers,
                                       allowGenerics,
                                       replaceSystemProperties,
                                       replaceExternalClassNames,
                                       replaceExternalTypes,
                                       list);
    }


    private List parseCommaSeparatedList(String  expectedDescription,
                                         boolean readFirstWord,
                                         boolean allowEmptyList,
                                         String  defaultIfEmpty,
                                         boolean expectClosingParenthesis,
                                         boolean isFileName,
                                         boolean checkJavaIdentifiers,
                                         boolean allowGenerics,
                                         boolean replaceSystemProperties,
                                         boolean replaceExternalClassNames,
                                         boolean replaceExternalTypes,
                                         List    list)
    throws ParseException, IOException
    {
        if (list == null)
        {
            list = new ArrayList();
        }

        if (readFirstWord)
        {
            if (!allowEmptyList)
            {
                // Read the first list entry.
                readNextWord(expectedDescription, isFileName, true, false);
            }
            else if (expectClosingParenthesis)
            {
                // Read the first list entry.
                readNextWord(expectedDescription, isFileName, true, false);

                // Return if the entry is actually empty (an empty file name or
                // a closing parenthesis).
                if (nextWord.length() == 0)
                {
                    // Read the closing parenthesis
                    readNextWord("closing '" + ConfigurationConstants.CLOSE_ARGUMENTS_KEYWORD +
                                 "'");

                    if (defaultIfEmpty != null)
                    {
                        list.add(defaultIfEmpty);
                    }
                    return list;
                }
                else if (nextWord.equals(ConfigurationConstants.CLOSE_ARGUMENTS_KEYWORD))
                {
                    if (defaultIfEmpty != null)
                    {
                        list.add(defaultIfEmpty);
                    }
                    return list;
                }
            }
            else
            {
                // Read the first list entry, if there is any.
                readNextWord(isFileName, true);

                // Check if the list is empty.
                if (configurationEnd())
                {
                    if (defaultIfEmpty != null)
                    {
                        list.add(defaultIfEmpty);
                    }
                    return list;
                }
            }
        }

        while (true)
        {
            if (checkJavaIdentifiers)
            {
                checkJavaIdentifier("java type", allowGenerics);
            }

            if (replaceSystemProperties)
            {
                nextWord = replaceSystemProperties(nextWord);
            }

            if (replaceExternalClassNames)
            {
                nextWord = ClassUtil.internalClassName(nextWord);
            }

            if (replaceExternalTypes)
            {
                nextWord = ClassUtil.internalType(nextWord);
            }

            list.add(nextWord);

            if (expectClosingParenthesis)
            {
                // Read a comma (or a closing parenthesis, or a different word).
                readNextWord("separating '" + ConfigurationConstants.ARGUMENT_SEPARATOR_KEYWORD +
                             "' or closing '" + ConfigurationConstants.CLOSE_ARGUMENTS_KEYWORD +
                             "'");
            }
            else
            {
                // Read a comma (or a different word).
                readNextWord();
            }

            if (!ConfigurationConstants.ARGUMENT_SEPARATOR_KEYWORD.equals(nextWord))
            {
                return list;
            }

            // Read the next list entry.
            readNextWord(expectedDescription, isFileName, true, false);
        }
    }


    /**
     * Converts a list of class name matchers to a class specification.
     */
    private ClassSpecification convertToClassSpecification(List<String> list)
    {
        return new ClassSpecification(null,
                                      0,
                                      0,
                                      null,
                                      StringUtil.join(",", list.toArray(new String[list.size()])),
                                      null,
                                      null);
    }


    /**
     * Removes any present member specification from the provided
     * class specification.
     */
    private ClassSpecification removeMemberSpecification(ClassSpecification classSpecification)
    {
        classSpecification.fieldSpecifications  = null;
        classSpecification.methodSpecifications = null;

        return classSpecification;
    }


    /**
     * Throws a ParseException for an unexpected keyword.
     */
    private int unknownAccessFlag() throws ParseException
    {
        throw new ParseException("Unexpected keyword " + reader.locationDescription());
    }


    /**
     * Creates a properly resolved URL, based on the given word.
     */
    private URL url(String word) throws ParseException, MalformedURLException
    {
        String fileName = replaceSystemProperties(word);
        URL url;

        try
        {
            // Check if the file name is a valid URL.
            url = new URL(fileName);
            return url;
        }
        catch (MalformedURLException ex) {}

        // Is it relative to a URL or to a file?
        URL baseURL = reader.getBaseURL();
        if (baseURL != null)
        {
            url = new URL(baseURL, fileName);
        }
        else
        {
            // Is the file a valid resource URL?
            url = ConfigurationParser.class.getResource(fileName);
            if (url == null)
            {
                File file = new File(fileName);

                // Try to get an absolute file.
                if (!file.isAbsolute())
                {
                    file = new File(reader.getBaseDir(), fileName);
                }

                url = file.toURI().toURL();
            }
        }

        return url;
    }


    /**
     * Creates a properly resolved File, based on the given word.
     */
    private File file(String word) throws ParseException
    {
        String fileName = replaceSystemProperties(word);
        File   file     = new File(fileName);

        // Try to get an absolute file.
        if (!file.isAbsolute())
        {
            file = new File(reader.getBaseDir(), fileName);
        }

        return file;
    }


    /**
     * Replaces any properties in the given word by their values.
     * For instance, the substring "<java.home>" is replaced by its value.
     */
    private String replaceSystemProperties(String word) throws ParseException
    {
        int fromIndex = 0;
        while (true)
        {
            fromIndex = word.indexOf(ConfigurationConstants.OPEN_SYSTEM_PROPERTY, fromIndex);
            if (fromIndex < 0)
            {
                break;
            }

            int toIndex = word.indexOf(ConfigurationConstants.CLOSE_SYSTEM_PROPERTY, fromIndex+1);
            if (toIndex < 0)
            {
                break;
            }

            String propertyName  = word.substring(fromIndex+1, toIndex);
            String propertyValue = properties.getProperty(propertyName);
            if (propertyValue == null)
            {
                try
                {
                    // Allow integer names, since they may be references
                    // to wildcards.
                    Integer.parseInt(propertyName);

                    fromIndex = toIndex + 1;
                    continue;
                }
                catch (NumberFormatException e)
                {
                    throw new ParseException("Value of system property '" + propertyName +
                                             "' is undefined in " + reader.locationDescription());
                }
            }

            word = word.substring(0, fromIndex) + propertyValue + word.substring(toIndex+1);

            fromIndex += propertyValue.length();
        }

        return word;
    }


    /**
     * Reads the next word of the configuration in the 'nextWord' field,
     * throwing an exception if there is no next word.
     */
    private void readNextWord(String expectedDescription)
    throws ParseException, IOException
    {
        readNextWord(expectedDescription, false, false, false);
    }


    /**
     * Reads the next word of the configuration in the 'nextWord' field,
     * throwing an exception if there is no next word.
     */
    private void readNextWord(String  expectedDescription,
                              boolean isFileName,
                              boolean expectSingleFile,
                              boolean expectingAtCharacter)
    throws ParseException, IOException
    {
        readNextWord(isFileName, expectSingleFile);
        if (configurationEnd(expectingAtCharacter))
        {
            throw new ParseException("Expecting " + expectedDescription +
                                     " before " + reader.locationDescription());
        }
    }


    /**
     * Reads the next word of the configuration in the 'nextWord' field.
     */
    private void readNextWord() throws IOException
    {
        readNextWord(false, false);
    }


    /**
     * Reads the next word of the configuration in the 'nextWord' field.
     */
    private void readNextWord(boolean isFileName,
                              boolean expectSingleFile) throws IOException
    {
        nextWord = reader.nextWord(isFileName, expectSingleFile);
    }


    /**
     * Returns whether the end of the configuration has been reached.
     */
    private boolean configurationEnd()
    {
        return configurationEnd(false);
    }


    /**
     * Returns whether the end of the configuration has been reached.
     */
    private boolean configurationEnd(boolean expectingAtCharacter)
    {
        return nextWord == null ||
               nextWord.startsWith(ConfigurationConstants.OPTION_PREFIX) ||
               (!expectingAtCharacter &&
                nextWord.equals(ConfigurationConstants.AT_DIRECTIVE));
    }


    /**
     * Checks whether the given word is a valid Java identifier and throws
     * a ParseException if it isn't. Wildcard characters are accepted.
     */
    private void checkJavaIdentifier(String expectedDescription)
        throws ParseException
    {
        checkJavaIdentifier(expectedDescription, true);
    }


    /**
     * Checks whether the given word is a valid Java identifier and throws
     * a ParseException if it isn't. Wildcard characters are accepted.
     */
    private void checkJavaIdentifier(String expectedDescription, boolean allowGenerics)
    throws ParseException
    {
        if (!isJavaIdentifier(nextWord))
        {
            throw new ParseException("Expecting " + expectedDescription +
                                     " before " + reader.locationDescription());
        }

        if (!allowGenerics && containsGenerics(nextWord))
        {
            throw new ParseException("Generics are not allowed (erased) in " + expectedDescription +
                                     " " + reader.locationDescription());
        }
    }


    /**
     * Returns whether the given word is a valid Java identifier.
     * Wildcard characters are accepted.
     */
    private boolean isJavaIdentifier(String word)
    {
        if (word.length() == 0)
        {
            return false;
        }

        for (int index = 0; index < word.length(); index++)
        {
            char c = word.charAt(index);
            if (!(Character.isJavaIdentifierPart(c) ||
                  c == '.' ||
                  c == '[' ||
                  c == ']' ||
                  c == '<' ||
                  c == '>' ||
                  c == '-' ||
                  c == '!' ||
                  c == '*' ||
                  c == '?' ||
                  c == '%'))
            {
                return false;
            }
        }

        return true;
    }


    /**
     * Returns whether the given word contains angle brackets around
     * a non-digit string.
     */
    private boolean containsGenerics(String word)
    {
        int index = 0;

        while (true)
        {
            // Can we find an opening angular bracket?
            int openIndex = word.indexOf(TypeConstants.GENERIC_START, index);
            if (openIndex < 0)
            {
                return false;
            }

            // Can we find a corresponding closing angular bracket?
            int closeIndex = word.indexOf(TypeConstants.GENERIC_END, openIndex + 1);
            if (closeIndex < 0)
            {
                return false;
            }

            try
            {
                // Is it just a reference to a wildcard?
                Integer.parseInt(word.substring(openIndex + 1, closeIndex));
            }
            catch (NumberFormatException e)
            {
                // It's not; it's really a generic type.
                return true;
            }

            index = closeIndex + 1;
        }
    }


    /**
     * Checks whether the given access flags are valid field access flags,
     * throwing a ParseException if they aren't.
     */
    private void checkFieldAccessFlags(int requiredSetMemberAccessFlags,
                                       int requiredUnsetMemberAccessFlags)
    throws ParseException
    {
        if (((requiredSetMemberAccessFlags |
              requiredUnsetMemberAccessFlags) &
            ~AccessConstants.VALID_FLAGS_FIELD) != 0)
        {
            throw new ParseException("Invalid method access modifier for field before " +
                                     reader.locationDescription());
        }
    }


    /**
     * Checks whether the given access flags are valid method access flags,
     * throwing a ParseException if they aren't.
     */
    private void checkMethodAccessFlags(int requiredSetMemberAccessFlags,
                                        int requiredUnsetMemberAccessFlags)
    throws ParseException
    {
        if (((requiredSetMemberAccessFlags |
              requiredUnsetMemberAccessFlags) &
            ~AccessConstants.VALID_FLAGS_METHOD) != 0)
        {
            throw new ParseException("Invalid field access modifier for method before " +
                                     reader.locationDescription());
        }
    }


    /**
     * A main method for testing configuration parsing.
     */
    public static void main(String[] args)
    {
        try
        {
            try (ConfigurationParser parser = new ConfigurationParser(args, System.getProperties()))
            {
                parser.parse(new Configuration());
            }
            catch (ParseException ex)
            {
                ex.printStackTrace();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
