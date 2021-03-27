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

package proguard.gradle.configuration;

import org.gradle.api.Transformer;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectories;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.OutputFiles;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import proguard.ClassPath;
import proguard.ClassPathEntry;
import proguard.ClassSpecification;
import proguard.Configuration;
import proguard.KeepClassSpecification;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * A Gradle-aware wrapper for the ProGuard `Configuration`, allowing all inputs and outputs
 * to be correctly declared. This class simply exposes the properties of `Configuration`, annotated
 * correctly for the Gradle task with return types wrapped as appropriate.
 *
 * For clarity, fields on `Configuration` that are not task inputs or outputs are exposed with `@Internal`.
 * These methods could just as easily be removed from this type.
 */
public class ProGuardTaskConfiguration
{
    protected final Provider<Configuration> configurationProvider;

    private final FileCollection inputJars;
    private final FileCollection outputJars;
    private final FileCollection outputDirs;
    private final Provider<List<ProGuardTaskClassPathEntry>> fileFilters;
    private final Provider<File> applyMapping;
    private final Provider<File> obfuscationDictionary;
    private final Provider<File> classObfuscationDictionary;
    private final Provider<File> packageObfuscationDictionary;

    public ProGuardTaskConfiguration(Provider<Configuration> configurationProvider, ObjectFactory objectFactory)
    {
        this.configurationProvider = configurationProvider;
        this.inputJars = objectFactory.fileCollection().from(inputJars());
        this.outputJars = objectFactory.fileCollection().from(outputs(false));
        this.outputDirs = objectFactory.fileCollection().from(outputs(true));
        this.fileFilters = fileFilters();
        this.applyMapping = mappedProvider(config -> optionalFile(config.applyMapping));
        this.obfuscationDictionary = mappedProvider(config -> optionalFile(config.obfuscationDictionary));
        this.classObfuscationDictionary = mappedProvider(config -> optionalFile(config.classObfuscationDictionary));
        this.packageObfuscationDictionary = mappedProvider(config -> optionalFile(config.packageObfuscationDictionary));
    }

    public static ProGuardTaskConfiguration create(Provider<Configuration> configurationProvider, ObjectFactory objectFactory)
    {
        if (GradleVersionCheck.isAtLeastGradle7())
        {
            return new ProGuardTaskConfiguration(configurationProvider, objectFactory);
        }
        else
        {
            return new ProGuardTaskConfigurationForGradle6(configurationProvider, objectFactory);
        }
    }

    private Configuration getConfiguration()
    {
        return configurationProvider.get();
    }

    protected <T> Provider<T> mappedProvider(Transformer<T, Configuration> transformer)
    {
        return configurationProvider.map(transformer);
    }

    private Provider<List<File>> inputJars()
    {
        return mappedProvider(config ->
        {
            List<File> files = new ArrayList<>();
            ClassPath programJars = config.programJars;
            for (int i = 0; i < programJars.size(); i++)
            {
                ClassPathEntry classPathEntry = programJars.get(i);
                if (!classPathEntry.isOutput())
                {
                    files.add(classPathEntry.getFile());
                }
            }

            ClassPath libraryJars = config.libraryJars;
            for (int i = 0; i < libraryJars.size(); i++)
            {
                ClassPathEntry classPathEntry = libraryJars.get(i);
                if (!classPathEntry.isOutput())
                {
                    files.add(classPathEntry.getFile());
                }
            }
            return files;
        });
    }

    private Callable<List<File>> outputs(boolean directories)
    {
        return () ->
        {
            List<File> files = new ArrayList<>();
            ClassPath programJars = getConfiguration().programJars;
            for (int i = 0; i < programJars.size(); i++)
            {
                ClassPathEntry classPathEntry = programJars.get(i);
                if (classPathEntry.isOutput() && classPathEntry.isDirectory() == directories)
                {
                    files.add(classPathEntry.getFile());
                }
            }
            return files;
        };
    }

    private Provider<List<ProGuardTaskClassPathEntry>> fileFilters()
    {
        return mappedProvider(config ->
        {
            List<ProGuardTaskClassPathEntry> entries = new ArrayList<>();
            ClassPath programJars = config.programJars;
            for (int i = 0; i < programJars.size(); i++)
            {
                ClassPathEntry classPathEntry = programJars.get(i);
                entries.add(new ProGuardTaskClassPathEntry(classPathEntry));
            }

            ClassPath libraryJars = config.libraryJars;
            for (int i = 0; i < libraryJars.size(); i++)
            {
                ClassPathEntry classPathEntry = libraryJars.get(i);
                entries.add(new ProGuardTaskClassPathEntry(classPathEntry));
            }
            return entries;
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // Input and output options.
    ///////////////////////////////////////////////////////////////////////////

    @Classpath
    public FileCollection getInputJars()
    {
        return inputJars;
    }

    @OutputFiles
    public FileCollection getOutputFiles()
    {
        return outputJars;
    }

    @OutputDirectories
    public FileCollection getOutputDirectories()
    {
        return outputDirs;
    }

    @Input
    public Provider<List<ProGuardTaskClassPathEntry>> getJarFilters()
    {
        return fileFilters;
    }

    @Input
    public boolean isSkipNonPublicLibraryClasses()
    {
        return getConfiguration().skipNonPublicLibraryClasses;
    }

    @Input
    public boolean isSkipNonPublicLibraryClassMembers()
    {
        return getConfiguration().skipNonPublicLibraryClassMembers;
    }

    @Optional
    @Input
    public List<String> getKeepDirectories()
    {
        return getConfiguration().keepDirectories;
    }

    @Optional
    @Input
    public List<String> getDontCompress()
    {
        return getConfiguration().dontCompress;
    }

    @Input
    public int getZipAlign()
    {
        return getConfiguration().zipAlign;
    }

    @Input
    public int getTargetClassVersion()
    {
        return getConfiguration().targetClassVersion;
    }

    @Internal
    public long getLastModified()
    {
        return getConfiguration().lastModified;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Keep options for code.
    ///////////////////////////////////////////////////////////////////////////

    @Optional
    @Input
    public List<KeepClassSpecification> getKeep()
    {
        return getConfiguration().keep;
    }

    @Optional
    @OutputFile
    public File getPrintSeeds()
    {
        return optionalFile(getConfiguration().printSeeds);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Shrinking options.
    ///////////////////////////////////////////////////////////////////////////

    @Input
    public boolean isShrink()
    {
        return getConfiguration().shrink;
    }

    @Optional
    @OutputFile
    public File getPrintUsage()
    {
        return optionalFile(getConfiguration().printUsage);
    }

    @Optional
    @Input
    public List<ClassSpecification> getWhyAreYouKeeping()
    {
        return getConfiguration().whyAreYouKeeping;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Optimization options.
    ///////////////////////////////////////////////////////////////////////////

    @Input
    public boolean isOptimize()
    {
        return getConfiguration().optimize;
    }

    @Optional
    @Input
    public List<String> getOptimizations()
    {
        return getConfiguration().optimizations;
    }

    @Input
    public int getOptimizationPasses()
    {
        return getConfiguration().optimizationPasses;
    }

    @Optional
    @Input
    public List<ClassSpecification> getAssumeNoSideEffects()
    {
        return getConfiguration().assumeNoSideEffects;
    }

    @Optional
    @Input
    public List<ClassSpecification> getAssumeNoExternalSideEffects()
    {
        return getConfiguration().assumeNoExternalSideEffects;
    }

    @Optional
    @Input
    public List<ClassSpecification> getAssumeNoEscapingParameters()
    {
        return getConfiguration().assumeNoEscapingParameters;
    }

    @Optional
    @Input
    public List<ClassSpecification> getAssumeNoExternalReturnValues()
    {
        return getConfiguration().assumeNoExternalReturnValues;
    }

    @Optional
    @Input
    public List<ClassSpecification> getAssumeValues()
    {
        return getConfiguration().assumeValues;
    }

    @Input
    public boolean isAllowAccessModification()
    {
        return getConfiguration().allowAccessModification;
    }

    @Input
    public boolean isMergeInterfacesAggressively()
    {
        return getConfiguration().mergeInterfacesAggressively;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Obfuscation options.
    ///////////////////////////////////////////////////////////////////////////

    @Input
    public boolean isObfuscate()
    {
        return getConfiguration().obfuscate;
    }

    @Optional
    @OutputFile
    public File getPrintMapping()
    {
        return optionalFile(getConfiguration().printMapping);
    }

    @Optional
    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public Provider<File> getApplyMapping()
    {
        return applyMapping;
    }

    @Optional
    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public Provider<File> getObfuscationDictionary()
    {
        return obfuscationDictionary;
    }

    @Optional
    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public Provider<File> getClassObfuscationDictionary()
    {
        return classObfuscationDictionary;
    }

    @Optional
    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public Provider<File> getPackageObfuscationDictionary()
    {
        return packageObfuscationDictionary;
    }

    @Input
    public boolean isOverloadAggressively()
    {
        return getConfiguration().overloadAggressively;
    }

    @Input
    public boolean isUseUniqueClassMemberNames()
    {
        return getConfiguration().useUniqueClassMemberNames;
    }

    @Input
    public boolean isUseMixedCaseClassNames()
    {
        return getConfiguration().useMixedCaseClassNames;
    }

    @Optional
    @Input
    public List<String> getKeepPackageNames()
    {
        return getConfiguration().keepPackageNames;
    }

    @Optional
    @Input
    public String getFlattenPackageHierarchy()
    {
        return getConfiguration().flattenPackageHierarchy;
    }

    @Optional
    @Input
    public String getRepackageClasses()
    {
        return getConfiguration().repackageClasses;
    }

    @Optional
    @Input
    public List<String> getKeepAttributes()
    {
        return getConfiguration().keepAttributes;
    }

    @Input
    public boolean isKeepParameterNames()
    {
        return getConfiguration().keepParameterNames;
    }

    @Optional
    @Input
    public String getNewSourceFileAttribute()
    {
        return getConfiguration().newSourceFileAttribute;
    }

    @Optional
    @Input
    public List<String> getAdaptClassStrings()
    {
        return getConfiguration().adaptClassStrings;
    }

    @Optional
    @Input
    public List<String> getAdaptResourceFileNames()
    {
        return getConfiguration().adaptResourceFileNames;
    }

    @Optional
    @Input
    public List<String> getAdaptResourceFileContents()
    {
        return getConfiguration().adaptResourceFileContents;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Preverification options.
    ///////////////////////////////////////////////////////////////////////////

    @Input
    public boolean isPreverify()
    {
        return getConfiguration().preverify;
    }

    @Input
    public boolean isMicroEdition()
    {
        return getConfiguration().microEdition;
    }

    @Input
    public boolean isAndroid()
    {
        return getConfiguration().android;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Jar signing options.
    ///////////////////////////////////////////////////////////////////////////

    @Internal
    public List<File> getKeyStores()
    {
        return getConfiguration().keyStores;
    }

    @Internal
    public List<String> getKeyStorePasswords()
    {
        return getConfiguration().keyStorePasswords;
    }

    @Internal
    public List<String> getKeyAliases()
    {
        return getConfiguration().keyAliases;
    }

    @Internal
    public List<String> getKeyPasswords()
    {
        return getConfiguration().keyPasswords;
    }

    ///////////////////////////////////////////////////////////////////////////
    // General options.
    ///////////////////////////////////////////////////////////////////////////

    @Internal
    public boolean isVerbose()
    {
        return getConfiguration().verbose;
    }

    @Optional
    @Input
    public List<String> getNote()
    {
        return getConfiguration().note;
    }

    @Optional
    @Input
    public List<String> getWarn()
    {
        return getConfiguration().warn;
    }

    @Input
    public boolean isIgnoreWarnings()
    {
        return getConfiguration().ignoreWarnings;
    }

    @Optional
    @OutputFile
    public File getPrintConfiguration()
    {
        return optionalFile(getConfiguration().printConfiguration);
    }

    @Optional
    @OutputFile
    public File getDump()
    {
        return optionalFile(getConfiguration().dump);
    }

    @Input
    public boolean isAddConfigurationDebugging()
    {
        return getConfiguration().addConfigurationDebugging;
    }

    @Input
    public boolean isBackport()
    {
        return getConfiguration().backport;
    }

    @Input
    public boolean isKeepKotlinMetadata()
    {
        return getConfiguration().keepKotlinMetadata;
    }

    @Input
    public boolean isEnableKotlinAsserter()
    {
        return getConfiguration().enableKotlinAsserter;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Utility methods.
    ///////////////////////////////////////////////////////////////////////////

    private static File optionalFile(File input)
    {
        if (input == null ||
            input == Configuration.STD_OUT)
        {
            return null;
        }
        return input;
    }

    private static File optionalFile(URL input)
    {
        if (input == null ||
            input.getFile().isEmpty())
        {
            return null;
        }
        return new File(input.getFile());
    }
}
