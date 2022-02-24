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

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * The ProGuard configuration.
 *
 * @see ProGuard
 *
 * @author Eric Lafortune
 */
public class Configuration
{
    public static final File STD_OUT = new File("");


    ///////////////////////////////////////////////////////////////////////////
    // Input and output options.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * A list of input and output entries (jars, wars, ears, jmods, zips, and directories).
     */
    public ClassPath                    programJars;

    /**
     * A list of library entries (jars, wars, ears, jmods, zips, and directories).
     */
    public ClassPath                    libraryJars;

    /**
     * Specifies whether to skip non-public library classes while reading
     * library jars.
     */
    public boolean                      skipNonPublicLibraryClasses      = false;

    /**
     * Specifies whether to skip non-public library class members while reading
     * library classes.
     */
    public boolean                      skipNonPublicLibraryClassMembers = true;

    /**
     * A list of String instances specifying directories to be kept in
     * the output directories or the output jars. A <code>null</code> list
     * means no directories. An empty list means all directories. The directory
     * names may contain "**", "*", or "?" wildcards, and they may be preceded
     * by the "!" negator.
     */
    public List                         keepDirectories;

    /**
     * A list of String instances specifying a filter for files that should
     * not be compressed in output jars.
     */
    public List<String>                 dontCompress;

    /**
     * Specifies the desired alignment of uncompressed data in output jars.
     * Uncompressed data will start at a multiple of the given number of bytes,
     * relative to the start of the jar file.
     */
    public int                          zipAlign                         = 1;

    /**
     * Specifies the version number of the output classes, or 0 if the version
     * number can be left unchanged.
     */
    public int                          targetClassVersion;

    /**
     * Specifies the last modification time of this configuration. This time
     * is necessary to check whether the input has to be processed. Setting it
     * to Long.MAX_VALUE forces processing, even if the modification times
     * of the output appear more recent than the modification times of the
     * input.
     */
    public long                         lastModified                     = 0L;

    ///////////////////////////////////////////////////////////////////////////
    // Keep options for code.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * A list of {@link KeepClassSpecification} instances, whose class names and
     * class member names are to be kept from shrinking, optimization, and/or
     * obfuscation.
     */
    public List<KeepClassSpecification> keep;

    /**
     * An optional output file for listing the kept seeds.
     * An empty file name means the standard output.
     */
    public File                         printSeeds;

    ///////////////////////////////////////////////////////////////////////////
    // Shrinking options.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Specifies whether the code should be shrunk.
     */
    public boolean                      shrink                           = true;

    /**
     * An optional output file for listing the unused classes and class
     * members. An empty file name means the standard output.
     */
    public File                         printUsage;

    /**
     * A list of {@link ClassSpecification} instances, for which an explanation
     * is to be printed, why they are kept in the shrinking step.
     */
    public List<ClassSpecification>     whyAreYouKeeping;

    ///////////////////////////////////////////////////////////////////////////
    // Optimization options.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Specifies whether the code should be optimized.
     */
    public boolean                      optimize                         = true;

    /**
     * A list of String instances specifying the optimizations to be
     * performed. A <code>null</code> list means all optimizations. The
     * optimization names may contain "*" or "?" wildcards, and they may
     * be preceded by the "!" negator.
     */
    public List<String>                 optimizations;

    /**
     * Specifies the number of optimization passes.
     */
    public int                          optimizationPasses               = 1;

    /**
     * A list of {@link ClassSpecification} instances, whose methods are
     * assumed to have no side effects.
     */
    public List<ClassSpecification>     assumeNoSideEffects;

    /**
     * A list of {@link ClassSpecification} instances, whose methods are
     * assumed to have no side external effects (that is, outside of 'this').
     */
    public List<ClassSpecification>     assumeNoExternalSideEffects;

    /**
     * A list of {@link ClassSpecification} instances, whose methods are
     * assumed not to let any reference parameters escape (including 'this').
     */
    public List<ClassSpecification>     assumeNoEscapingParameters;

    /**
     * A list of {@link ClassSpecification} instances, whose methods are
     * assumed not to return any external references (only parameters and new
     * instances).
     */
    public List<ClassSpecification>     assumeNoExternalReturnValues;

    /**
     * A list of {@link ClassSpecification} instances, with fields and methods
     * that have specified fixed primitive values.
     */
    public List<ClassSpecification>     assumeValues;

    /**
     * Specifies whether the access of class members can be modified.
     */
    public boolean                      allowAccessModification          = false;

    /**
     * Specifies whether interfaces may be merged aggressively.
     */
    public boolean                      mergeInterfacesAggressively      = false;

    /**
     * An optional output file for listing the lambda to lambdagroup mapping.
     * An empty file name means the standard output.
     */
    public File                         printLambdaGroupMapping;

    /**
     * Specifies whether Kotlin lambda classes can be merged into lambda groups.
     */
    public boolean                      mergeKotlinLambdaClasses;

    ///////////////////////////////////////////////////////////////////////////
    // Obfuscation options.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Specifies whether the code should be obfuscated.
     */
    public boolean                      obfuscate                        = true;

    /**
     * An optional output file for listing the obfuscation mapping.
     * An empty file name means the standard output.
     */
    public File                         printMapping;

    /**
     * An optional input file for reading an obfuscation mapping.
     */
    public File                         applyMapping;

    /**
     * An optional name of a file containing obfuscated class member names.
     */
    public URL                          obfuscationDictionary;

    /**
     * An optional name of a file containing obfuscated class names.
     */
    public URL                          classObfuscationDictionary;

    /**
     * An optional name of a file containing obfuscated package names.
     */
    public URL                          packageObfuscationDictionary;

    /**
     * Specifies whether to apply aggressive name overloading on class members.
     */
    public boolean                      overloadAggressively             = false;

    /**
     * Specifies whether to generate globally unique class member names.
     */
    public boolean                      useUniqueClassMemberNames        = false;

    /**
     * Specifies whether obfuscated packages and classes can get mixed-case names.
     */
    public boolean                      useMixedCaseClassNames           = true;

    /**
     * A list of String instances specifying package names to be kept.
     * A <code>null</code> list means no names. An empty list means all
     * names. The package names may contain "**", "*", or "?" wildcards, and
     * they may be preceded by the "!" negator.
     */
    public List<String>                 keepPackageNames;

    /**
     * An optional base package if the obfuscated package hierarchy is to be
     * flattened, <code>null</code> otherwise.
     */
    public String                       flattenPackageHierarchy;

    /**
     * An optional base package if the obfuscated classes are to be repackaged
     * into a single package, <code>null</code> otherwise.
     */
    public String                       repackageClasses;

    /**
     * A list of String instances specifying optional attributes to be kept.
     * A <code>null</code> list means no attributes. An empty list means all
     * attributes. The attribute names may contain "*" or "?" wildcards, and
     * they may be preceded by the "!" negator.
     */
    public List<String>                 keepAttributes;

    /**
     * Specifies whether method parameter names and types should be kept for
     * methods that are not obfuscated. This is achieved by keeping partial
     * "LocalVariableTable" and "LocalVariableTypeTable" attributes.
     */
    public boolean                      keepParameterNames               = false;

    /**
     * An optional replacement for all SourceFile attributes.
     */
    public String                       newSourceFileAttribute;

    /**
     * A list of String instances specifying a filter for classes whose
     * string constants are to be adapted, based on corresponding obfuscated
     * class names.
     */
    public List<String>                 adaptClassStrings;

    /**
     * A list of String instances specifying a filter for files whose
     * names are to be adapted, based on corresponding obfuscated class names.
     */
    public List<String>                 adaptResourceFileNames;

    /**
     * A list of String instances specifying a filter for files whose
     * contents are to be adapted, based on obfuscated class names.
     */
    public List<String>                 adaptResourceFileContents;

    ///////////////////////////////////////////////////////////////////////////
    // Preverification options.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Specifies whether the code should be preverified.
     */
    public boolean                      preverify                        = true;

    /**
     * Specifies whether the code should be preverified for Java Micro Edition
     * (creating StackMap attributes) instead of for Java Standard Edition
     * (creating StackMapTable attributes).
     */
    public boolean                      microEdition                     = false;

    /**
     * Specifies whether the code should be targeted at the Android platform.
     */
    public boolean                      android                          = false;

    ///////////////////////////////////////////////////////////////////////////
    // Jar signing options.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * A list of File instances specifying the key stores to be used when
     * signing jars.
     */
    public List<File>                   keyStores;

    /**
     * A list of String instances specifying the passwords of the key stores
     * to be used when signing jars.
     */
    public List<String>                 keyStorePasswords;

    /**
     * A list of String instances specifying the names of the keys to be used
     * when signing jars.
     */
    public List<String>                 keyAliases;

    /**
     * A list of String instances specifying the passwords of the keys to be
     * used when signing jars.
     */
    public List<String>                 keyPasswords;

    ///////////////////////////////////////////////////////////////////////////
    // General options.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Specifies whether to print verbose messages.
     */
    public boolean                      verbose                          = false;

    /**
     * A list of String instances specifying a filter for the classes for
     * which not to print notes, if there are noteworthy potential problems.
     * A <code>null</code> list means all classes. The class names may contain
     * "**", "*", or "?" wildcards, and they may be preceded by the "!" negator.
     */
    public List<String>                 note                             = null;

    /**
     * A list of String instances specifying a filter for the classes for
     * which not to print warnings, if there are any problems.
     * A <code>null</code> list means all classes. The class names may contain
     * "**", "*", or "?" wildcards, and they may be preceded by the "!" negator.
     */
    public List<String>                 warn                             = null;

    /**
     * Specifies whether to ignore any warnings.
     */
    public boolean                      ignoreWarnings                   = false;

    /**
     * An optional output file for printing out the configuration that ProGuard
     * is using (with included files and replaced variables).
     * An empty file name means the standard output.
     */
    public File                         printConfiguration;

    /**
     * An optional output file for printing out the processed code in a more
     * or less readable form. An empty file name means the standard output.
     */
    public File                         dump;

    /**
     * Specifies whether to add logging to reflection code, providing suggestions
     * on the ProGuard configuration.
     */
    public boolean                      addConfigurationDebugging;

    /**
     * Specifies whether to backporting of class files to another
     * targetClassVersion shall be enabled.
     */
    public boolean                      backport                         = false;

    /**
     * Specifies whether to process Kotlin metadata or not.
     */
    public boolean                      keepKotlinMetadata               = false;

    // INTERNAL OPTIONS

    /**
     * Enables of disables the Kotlin asserter. This is a hidden option,
     * not available via the configuration file.
     */
    public boolean                      enableKotlinAsserter             = true;

    /**
     * File to write extra data entries to; instead of writing them to
     * their respective jars. See {@link proguard.io.ExtraDataEntryNameMap}.
     */
    public File extraJar;
}
