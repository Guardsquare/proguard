package proguard.gradle.configuration;

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
    private final Configuration configuration;

    public ProGuardTaskConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Input and output options.
    ///////////////////////////////////////////////////////////////////////////

    @Nested
    public Collection<ProGuardTaskClassPathEntry> getProgramJars() {
        return wrapClassPath(configuration.programJars);
    }

    @Nested
    public Collection<ProGuardTaskClassPathEntry> getLibraryJars() {
        return wrapClassPath(configuration.libraryJars);
    }

    @Input
    public boolean isSkipNonPublicLibraryClasses() {
        return configuration.skipNonPublicLibraryClasses;
    }

    @Input
    public boolean isSkipNonPublicLibraryClassMembers() {
        return configuration.skipNonPublicLibraryClassMembers;
    }

    @Optional @Input
    public List<String> getKeepDirectories() {
        return configuration.keepDirectories;
    }

    @Optional @Input
    public List<String> getDontCompress() {
        return configuration.dontCompress;
    }

    @Input
    public int getZipAlign() {
        return configuration.zipAlign;
    }

    @Input
    public int getTargetClassVersion() {
        return configuration.targetClassVersion;
    }

    @Internal
    public long getLastModified() {
        return configuration.lastModified;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Keep options for code.
    ///////////////////////////////////////////////////////////////////////////

    @Optional @Input
    public List<KeepClassSpecification> getKeep() {
        return configuration.keep;
    }

    @Optional @OutputFile
    public File getPrintSeeds() {
        return optionalFile(configuration.printSeeds);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Shrinking options.
    ///////////////////////////////////////////////////////////////////////////

    @Input
    public boolean isShrink() {
        return configuration.shrink;
    }

    @Optional @OutputFile
    public File getPrintUsage() {
        return optionalFile(configuration.printUsage);
    }

    @Optional @Input
    public List<ClassSpecification> getWhyAreYouKeeping() {
        return configuration.whyAreYouKeeping;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Optimization options.
    ///////////////////////////////////////////////////////////////////////////

    @Input
    public boolean isOptimize() {
        return configuration.optimize;
    }

    @Optional @Input
    public List<String> getOptimizations() {
        return configuration.optimizations;
    }

    @Input
    public int getOptimizationPasses() {
        return configuration.optimizationPasses;
    }

    @Optional @Input
    public List<ClassSpecification> getAssumeNoSideEffects() {
        return configuration.assumeNoSideEffects;
    }

    @Optional @Input
    public List<ClassSpecification> getAssumeNoExternalSideEffects() {
        return configuration.assumeNoExternalSideEffects;
    }

    @Optional @Input
    public List<ClassSpecification> getAssumeNoEscapingParameters() {
        return configuration.assumeNoEscapingParameters;
    }

    @Optional @Input
    public List<ClassSpecification> getAssumeNoExternalReturnValues() {
        return configuration.assumeNoExternalReturnValues;
    }

    @Optional @Input
    public List<ClassSpecification> getAssumeValues() {
        return configuration.assumeValues;
    }

    @Input
    public boolean isAllowAccessModification() {
        return configuration.allowAccessModification;
    }

    @Input
    public boolean isMergeInterfacesAggressively() {
        return configuration.mergeInterfacesAggressively;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Obfuscation options.
    ///////////////////////////////////////////////////////////////////////////

    @Input
    public boolean isObfuscate() {
        return configuration.obfuscate;
    }

    @Optional @OutputFile
    public File getPrintMapping() {
        return optionalFile(configuration.printMapping);
    }

    @Optional @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getApplyMapping() {
        return optionalFile(configuration.applyMapping);
    }

    @Optional @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getObfuscationDictionary() {
        return optionalFile(configuration.obfuscationDictionary);
    }

    @Optional @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getClassObfuscationDictionary() {
        return optionalFile(configuration.classObfuscationDictionary);
    }

    @Optional @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getPackageObfuscationDictionary() {
        return optionalFile(configuration.packageObfuscationDictionary);
    }

    @Input
    public boolean isOverloadAggressively() {
        return configuration.overloadAggressively;
    }

    @Input
    public boolean isUseUniqueClassMemberNames() {
        return configuration.useUniqueClassMemberNames;
    }

    @Input
    public boolean isUseMixedCaseClassNames() {
        return configuration.useMixedCaseClassNames;
    }

    @Optional @Input
    public List<String> getKeepPackageNames() {
        return configuration.keepPackageNames;
    }

    @Optional @Input
    public String getFlattenPackageHierarchy() {
        return configuration.flattenPackageHierarchy;
    }

    @Optional @Input
    public String getRepackageClasses() {
        return configuration.repackageClasses;
    }

    @Optional @Input
    public List<String> getKeepAttributes() {
        return configuration.keepAttributes;
    }

    @Input
    public boolean isKeepParameterNames() {
        return configuration.keepParameterNames;
    }

    @Optional @Input
    public String getNewSourceFileAttribute() {
        return configuration.newSourceFileAttribute;
    }

    @Optional @Input
    public List<String> getAdaptClassStrings() {
        return configuration.adaptClassStrings;
    }

    @Optional @Input
    public List<String> getAdaptResourceFileNames() {
        return configuration.adaptResourceFileNames;
    }

    @Optional @Input
    public List<String> getAdaptResourceFileContents() {
        return configuration.adaptResourceFileContents;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Preverification options.
    ///////////////////////////////////////////////////////////////////////////

    @Input
    public boolean isPreverify() {
        return configuration.preverify;
    }

    @Input
    public boolean isMicroEdition() {
        return configuration.microEdition;
    }

    @Input
    public boolean isAndroid() {
        return configuration.android;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Jar signing options.
    ///////////////////////////////////////////////////////////////////////////

    @Internal
    public List<File> getKeyStores() {
        return configuration.keyStores;
    }

    @Internal
    public List<String> getKeyStorePasswords() {
        return configuration.keyStorePasswords;
    }

    @Internal
    public List<String> getKeyAliases() {
        return configuration.keyAliases;
    }

    @Internal
    public List<String> getKeyPasswords() {
        return configuration.keyPasswords;
    }

    ///////////////////////////////////////////////////////////////////////////
    // General options.
    ///////////////////////////////////////////////////////////////////////////

    @Internal
    public boolean isVerbose() {
        return configuration.verbose;
    }

    @Optional @Input
    public List<String> getNote() {
        return configuration.note;
    }

    @Optional @Input
    public List<String> getWarn() {
        return configuration.warn;
    }

    @Input
    public boolean isIgnoreWarnings() {
        return configuration.ignoreWarnings;
    }

    @Optional @OutputFile
    public File getPrintConfiguration() {
        return optionalFile(configuration.printConfiguration);
    }

    @Optional @OutputFile
    public File getDump() {
        return optionalFile(configuration.dump);
    }

    @Input
    public boolean isAddConfigurationDebugging() {
        return configuration.addConfigurationDebugging;
    }

    @Input
    public boolean isBackport() {
        return configuration.backport;
    }

    @Input
    public boolean isKeepKotlinMetadata() {
        return configuration.keepKotlinMetadata;
    }

    @Input
    public boolean isEnableKotlinAsserter() {
        return configuration.enableKotlinAsserter;
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
