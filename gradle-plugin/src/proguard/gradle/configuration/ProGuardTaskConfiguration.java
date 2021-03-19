package proguard.gradle.configuration;

import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputFile;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A Gradle-aware wrapper for the ProGuard `Configuration`, allowing all inputs and outputs
 * to be correctly declared. This class simply exposes the properties of `Configuration`, annotated
 * correctly for the Gradle task with return types wrapped as appropriate.
 *
 * For clarity, fields on `Configuration` that are not task inputs or outputs are exposed with `@Internal`.
 * These methods could just as easily be removed from this type.
 */
public class ProGuardTaskConfiguration {
    private final Provider<Configuration> configurationProvider;

    public ProGuardTaskConfiguration(Provider<Configuration> configurationProvider) {
        this.configurationProvider = configurationProvider;
    }

    private Configuration getConfiguration() {
        return configurationProvider.get();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Input and output options.
    ///////////////////////////////////////////////////////////////////////////

    @Nested
    public Collection<ProGuardTaskClassPathEntry> getProgramJars() {
        return wrapClassPath(getConfiguration().programJars);
    }

    @Nested
    public Collection<ProGuardTaskClassPathEntry> getLibraryJars() {
        return wrapClassPath(getConfiguration().libraryJars);
    }

    @Input
    public boolean isSkipNonPublicLibraryClasses() {
        return getConfiguration().skipNonPublicLibraryClasses;
    }

    @Input
    public boolean isSkipNonPublicLibraryClassMembers() {
        return getConfiguration().skipNonPublicLibraryClassMembers;
    }

    @Optional @Input
    public List<String> getKeepDirectories() {
        return getConfiguration().keepDirectories;
    }

    @Optional @Input
    public List<String> getDontCompress() {
        return getConfiguration().dontCompress;
    }

    @Input
    public int getZipAlign() {
        return getConfiguration().zipAlign;
    }

    @Input
    public int getTargetClassVersion() {
        return getConfiguration().targetClassVersion;
    }

    @Internal
    public long getLastModified() {
        return getConfiguration().lastModified;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Keep options for code.
    ///////////////////////////////////////////////////////////////////////////

    @Optional @Input
    public List<KeepClassSpecification> getKeep() {
        return getConfiguration().keep;
    }

    @Optional @OutputFile
    public File getPrintSeeds() {
        return optionalFile(getConfiguration().printSeeds);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Shrinking options.
    ///////////////////////////////////////////////////////////////////////////

    @Input
    public boolean isShrink() {
        return getConfiguration().shrink;
    }

    @Optional @OutputFile
    public File getPrintUsage() {
        return optionalFile(getConfiguration().printUsage);
    }

    @Optional @Input
    public List<ClassSpecification> getWhyAreYouKeeping() {
        return getConfiguration().whyAreYouKeeping;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Optimization options.
    ///////////////////////////////////////////////////////////////////////////

    @Input
    public boolean isOptimize() {
        return getConfiguration().optimize;
    }

    @Optional @Input
    public List<String> getOptimizations() {
        return getConfiguration().optimizations;
    }

    @Input
    public int getOptimizationPasses() {
        return getConfiguration().optimizationPasses;
    }

    @Optional @Input
    public List<ClassSpecification> getAssumeNoSideEffects() {
        return getConfiguration().assumeNoSideEffects;
    }

    @Optional @Input
    public List<ClassSpecification> getAssumeNoExternalSideEffects() {
        return getConfiguration().assumeNoExternalSideEffects;
    }

    @Optional @Input
    public List<ClassSpecification> getAssumeNoEscapingParameters() {
        return getConfiguration().assumeNoEscapingParameters;
    }

    @Optional @Input
    public List<ClassSpecification> getAssumeNoExternalReturnValues() {
        return getConfiguration().assumeNoExternalReturnValues;
    }

    @Optional @Input
    public List<ClassSpecification> getAssumeValues() {
        return getConfiguration().assumeValues;
    }

    @Input
    public boolean isAllowAccessModification() {
        return getConfiguration().allowAccessModification;
    }

    @Input
    public boolean isMergeInterfacesAggressively() {
        return getConfiguration().mergeInterfacesAggressively;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Obfuscation options.
    ///////////////////////////////////////////////////////////////////////////

    @Input
    public boolean isObfuscate() {
        return getConfiguration().obfuscate;
    }

    @Optional @OutputFile
    public File getPrintMapping() {
        return optionalFile(getConfiguration().printMapping);
    }

    @Optional @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getApplyMapping() {
        return optionalFile(getConfiguration().applyMapping);
    }

    @Optional @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getObfuscationDictionary() {
        return optionalFile(getConfiguration().obfuscationDictionary);
    }

    @Optional @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getClassObfuscationDictionary() {
        return optionalFile(getConfiguration().classObfuscationDictionary);
    }

    @Optional @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getPackageObfuscationDictionary() {
        return optionalFile(getConfiguration().packageObfuscationDictionary);
    }

    @Input
    public boolean isOverloadAggressively() {
        return getConfiguration().overloadAggressively;
    }

    @Input
    public boolean isUseUniqueClassMemberNames() {
        return getConfiguration().useUniqueClassMemberNames;
    }

    @Input
    public boolean isUseMixedCaseClassNames() {
        return getConfiguration().useMixedCaseClassNames;
    }

    @Optional @Input
    public List<String> getKeepPackageNames() {
        return getConfiguration().keepPackageNames;
    }

    @Optional @Input
    public String getFlattenPackageHierarchy() {
        return getConfiguration().flattenPackageHierarchy;
    }

    @Optional @Input
    public String getRepackageClasses() {
        return getConfiguration().repackageClasses;
    }

    @Optional @Input
    public List<String> getKeepAttributes() {
        return getConfiguration().keepAttributes;
    }

    @Input
    public boolean isKeepParameterNames() {
        return getConfiguration().keepParameterNames;
    }

    @Optional @Input
    public String getNewSourceFileAttribute() {
        return getConfiguration().newSourceFileAttribute;
    }

    @Optional @Input
    public List<String> getAdaptClassStrings() {
        return getConfiguration().adaptClassStrings;
    }

    @Optional @Input
    public List<String> getAdaptResourceFileNames() {
        return getConfiguration().adaptResourceFileNames;
    }

    @Optional @Input
    public List<String> getAdaptResourceFileContents() {
        return getConfiguration().adaptResourceFileContents;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Preverification options.
    ///////////////////////////////////////////////////////////////////////////

    @Input
    public boolean isPreverify() {
        return getConfiguration().preverify;
    }

    @Input
    public boolean isMicroEdition() {
        return getConfiguration().microEdition;
    }

    @Input
    public boolean isAndroid() {
        return getConfiguration().android;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Jar signing options.
    ///////////////////////////////////////////////////////////////////////////

    @Internal
    public List<File> getKeyStores() {
        return getConfiguration().keyStores;
    }

    @Internal
    public List<String> getKeyStorePasswords() {
        return getConfiguration().keyStorePasswords;
    }

    @Internal
    public List<String> getKeyAliases() {
        return getConfiguration().keyAliases;
    }

    @Internal
    public List<String> getKeyPasswords() {
        return getConfiguration().keyPasswords;
    }

    ///////////////////////////////////////////////////////////////////////////
    // General options.
    ///////////////////////////////////////////////////////////////////////////

    @Internal
    public boolean isVerbose() {
        return getConfiguration().verbose;
    }

    @Optional @Input
    public List<String> getNote() {
        return getConfiguration().note;
    }

    @Optional @Input
    public List<String> getWarn() {
        return getConfiguration().warn;
    }

    @Input
    public boolean isIgnoreWarnings() {
        return getConfiguration().ignoreWarnings;
    }

    @Optional @OutputFile
    public File getPrintConfiguration() {
        return optionalFile(getConfiguration().printConfiguration);
    }

    @Optional @OutputFile
    public File getDump() {
        return optionalFile(getConfiguration().dump);
    }

    @Input
    public boolean isAddConfigurationDebugging() {
        return getConfiguration().addConfigurationDebugging;
    }

    @Input
    public boolean isBackport() {
        return getConfiguration().backport;
    }

    @Input
    public boolean isKeepKotlinMetadata() {
        return getConfiguration().keepKotlinMetadata;
    }

    @Input
    public boolean isEnableKotlinAsserter() {
        return getConfiguration().enableKotlinAsserter;
    }


    ///////////////////////////////////////////////////////////////////////////
    // Utility methods.
    ///////////////////////////////////////////////////////////////////////////
    private static File optionalFile(File input) {
        if (input == null || input == Configuration.STD_OUT) {
            return null;
        }
        return input;
    }

    private static File optionalFile(URL input) {
        if (input == null || input.getFile().isEmpty()) {
            return null;
        }
        return new File(input.getFile());
    }

    private static List<ProGuardTaskClassPathEntry> wrapClassPath(ClassPath classPath) {
        if (classPath == null) {
            return Collections.emptyList();
        }
        List<ProGuardTaskClassPathEntry> entries = new ArrayList<>();
        for (int i = 0; i < classPath.size(); i++) {
            ClassPathEntry classPathEntry = classPath.get(i);
            entries.add(wrapClassPathEntry(classPathEntry));
        }
        return entries;
    }

    private static ProGuardTaskClassPathEntry wrapClassPathEntry(ClassPathEntry entry) {
        File file = entry.getFile();
        if (file.isDirectory()) {
            return entry.isOutput() ? new OutputDirectoryClassPathEntry(entry) : new InputDirectoryClassPathEntry(entry);
        } else {
            return entry.isOutput() ? new OutputFileClassPathEntry(entry) : new InputFileClassPathEntry(entry);
        }
    }
}
