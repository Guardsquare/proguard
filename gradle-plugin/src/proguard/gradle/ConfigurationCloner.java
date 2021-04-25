package proguard.gradle;

import proguard.Configuration;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationCloner
{
    public static Configuration cloneConfiguration(Configuration from)
    {
        Configuration copy = new Configuration();

        // Input and output options.
        copy.programJars                      = from.programJars;
        copy.libraryJars                      = from.libraryJars;
        copy.skipNonPublicLibraryClasses      = from.skipNonPublicLibraryClasses;
        copy.skipNonPublicLibraryClassMembers = from.skipNonPublicLibraryClassMembers;
        copy.keepDirectories                  = copyList(from.keepDirectories);
        copy.dontCompress                     = copyList(from.dontCompress);
        copy.zipAlign                         = from.zipAlign;
        copy.targetClassVersion               = from.targetClassVersion;
        copy.lastModified                     = from.lastModified;

        // Keep options for code.
        copy.keep       = copyList(from.keep);
        copy.printSeeds = from.printSeeds;

        // Shrinking options.
        copy.shrink           = from.shrink;
        copy.printUsage       = from.printUsage;
        copy.whyAreYouKeeping = copyList(from.whyAreYouKeeping);

        // Optimization options.
        copy.optimize                     = from.optimize;
        copy.optimizations                = copyList(from.optimizations);
        copy.optimizationPasses           = from.optimizationPasses;
        copy.assumeNoSideEffects          = copyList(from.assumeNoSideEffects);
        copy.assumeNoExternalSideEffects  = copyList(from.assumeNoExternalSideEffects);
        copy.assumeNoEscapingParameters   = copyList(from.assumeNoEscapingParameters);
        copy.assumeNoExternalReturnValues = copyList(from.assumeNoExternalReturnValues);
        copy.assumeValues                 = copyList(from.assumeValues);
        copy.allowAccessModification      = from.allowAccessModification;
        copy.mergeInterfacesAggressively  = from.mergeInterfacesAggressively;

        // Obfuscation options.
        copy.obfuscate                    = from.obfuscate;
        copy.printMapping                 = from.printMapping;
        copy.applyMapping                 = from.applyMapping;
        copy.obfuscationDictionary        = from.obfuscationDictionary;
        copy.classObfuscationDictionary   = from.classObfuscationDictionary;
        copy.packageObfuscationDictionary = from.packageObfuscationDictionary;
        copy.overloadAggressively         = from.overloadAggressively;
        copy.useUniqueClassMemberNames    = from.useUniqueClassMemberNames;
        copy.useMixedCaseClassNames       = from.useMixedCaseClassNames;
        copy.keepPackageNames             = copyList(from.keepPackageNames);

        copy.flattenPackageHierarchy   = from.flattenPackageHierarchy;
        copy.repackageClasses          = from.repackageClasses;
        copy.keepAttributes            = copyList(from.keepAttributes);
        copy.keepParameterNames        = from.keepParameterNames;
        copy.newSourceFileAttribute    = from.newSourceFileAttribute;
        copy.adaptClassStrings         = copyList(from.adaptClassStrings);
        copy.adaptResourceFileNames    = copyList(from.adaptResourceFileNames);
        copy.adaptResourceFileContents = copyList(from.adaptResourceFileContents);

        // Preverification options.
        copy.preverify    = from.preverify;
        copy.microEdition = from.microEdition;
        copy.android      = from.android;

        // Jar signing options.
        copy.keyStores         = copyList(from.keyStores);
        copy.keyStorePasswords = copyList(from.keyStorePasswords);
        copy.keyAliases        = copyList(from.keyAliases);
        copy.keyPasswords      = copyList(from.keyPasswords);

        // General options.
        copy.verbose                   = from.verbose;
        copy.note                      = copyList(from.note);
        copy.warn                      = copyList(from.warn);
        copy.ignoreWarnings            = from.ignoreWarnings;
        copy.printConfiguration        = from.printConfiguration;
        copy.dump                      = from.dump;
        copy.addConfigurationDebugging = from.addConfigurationDebugging;
        copy.backport                  = from.backport;
        copy.keepKotlinMetadata        = from.keepKotlinMetadata;
        copy.enableKotlinAsserter      = from.enableKotlinAsserter;

        return copy;
    }

    private static <T> List<T> copyList(List<T> from)
    {
        if (from == null)
        {
            return null;
        }
        return new ArrayList<>(from);
    }

}
