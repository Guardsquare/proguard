package proguard;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ConfigurationWriterDiffblueTest {
  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName("Test write(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite() throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt())).thenReturn(new ClassPathEntry(Configuration.STD_OUT, true));
    when(classPath.size()).thenReturn(3);
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping = Configuration.STD_OUT;
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "java.text";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = 1L;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration = Configuration.STD_OUT;
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = new ClassPath();
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
  }

  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName("Test write(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite2() throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt())).thenReturn(new ClassPathEntry(Configuration.STD_OUT, false));
    when(classPath.size()).thenReturn(3);
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping = Configuration.STD_OUT;
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "java.text";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = 1L;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration = Configuration.STD_OUT;
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = new ClassPath();
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
  }

  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName("Test write(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite3() throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt()))
        .thenReturn(
            new ClassPathEntry(Configuration.STD_OUT, true, "Printing configuration to [{}]..."));
    when(classPath.size()).thenReturn(3);
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping = Configuration.STD_OUT;
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "java.text";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = 1L;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration = Configuration.STD_OUT;
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = new ClassPath();
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
  }

  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName("Test write(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite4() throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPathEntry classPathEntry = mock(ClassPathEntry.class);
    when(classPathEntry.getAabFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getAarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getApkFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getEarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJmodFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getWarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getZipFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.isOutput()).thenReturn(true);
    when(classPathEntry.getFile()).thenReturn(Configuration.STD_OUT);
    when(classPathEntry.getFeatureName()).thenReturn("Feature Name");

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt())).thenReturn(classPathEntry);
    when(classPath.size()).thenReturn(3);
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "java.text";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = 1L;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration = Configuration.STD_OUT;
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = new ClassPath();
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
    verify(classPathEntry, atLeast(1)).getAabFilter();
    verify(classPathEntry, atLeast(1)).getAarFilter();
    verify(classPathEntry, atLeast(1)).getApkFilter();
    verify(classPathEntry, atLeast(1)).getEarFilter();
    verify(classPathEntry, atLeast(1)).getFeatureName();
    verify(classPathEntry, atLeast(1)).getFile();
    verify(classPathEntry, atLeast(1)).getFilter();
    verify(classPathEntry, atLeast(1)).getJarFilter();
    verify(classPathEntry, atLeast(1)).getJmodFilter();
    verify(classPathEntry, atLeast(1)).getWarFilter();
    verify(classPathEntry, atLeast(1)).getZipFilter();
    verify(classPathEntry, atLeast(1)).isOutput();
  }

  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName("Test write(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite5() throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPathEntry classPathEntry = mock(ClassPathEntry.class);
    when(classPathEntry.getAabFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getAarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getApkFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getEarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJmodFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getWarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getZipFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.isOutput()).thenReturn(true);
    when(classPathEntry.getFile()).thenReturn(Configuration.STD_OUT);
    when(classPathEntry.getFeatureName()).thenReturn("Feature Name");

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt())).thenReturn(classPathEntry);
    when(classPath.size()).thenReturn(3);
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping = Configuration.STD_OUT;
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(
                System.getProperty("java.io.tmpdir"),
                ConfigurationConstants.ARGUMENT_SEPARATOR_KEYWORD)
            .toUri()
            .toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "java.text";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = 1L;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration = Configuration.STD_OUT;
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = new ClassPath();
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
    verify(classPathEntry, atLeast(1)).getAabFilter();
    verify(classPathEntry, atLeast(1)).getAarFilter();
    verify(classPathEntry, atLeast(1)).getApkFilter();
    verify(classPathEntry, atLeast(1)).getEarFilter();
    verify(classPathEntry, atLeast(1)).getFeatureName();
    verify(classPathEntry, atLeast(1)).getFile();
    verify(classPathEntry, atLeast(1)).getFilter();
    verify(classPathEntry, atLeast(1)).getJarFilter();
    verify(classPathEntry, atLeast(1)).getJmodFilter();
    verify(classPathEntry, atLeast(1)).getWarFilter();
    verify(classPathEntry, atLeast(1)).getZipFilter();
    verify(classPathEntry, atLeast(1)).isOutput();
  }

  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName("Test write(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite6() throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPathEntry classPathEntry = mock(ClassPathEntry.class);
    when(classPathEntry.getAabFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getAarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getApkFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getEarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJmodFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getWarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getZipFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.isOutput()).thenReturn(true);
    when(classPathEntry.getFile()).thenReturn(Configuration.STD_OUT);
    when(classPathEntry.getFeatureName()).thenReturn("Feature Name");

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt())).thenReturn(classPathEntry);
    when(classPath.size()).thenReturn(3);
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping = Configuration.STD_OUT;
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(
                System.getProperty("java.io.tmpdir"), ConfigurationConstants.OPEN_ARGUMENTS_KEYWORD)
            .toUri()
            .toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "java.text";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = 1L;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration = Configuration.STD_OUT;
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = new ClassPath();
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
    verify(classPathEntry, atLeast(1)).getAabFilter();
    verify(classPathEntry, atLeast(1)).getAarFilter();
    verify(classPathEntry, atLeast(1)).getApkFilter();
    verify(classPathEntry, atLeast(1)).getEarFilter();
    verify(classPathEntry, atLeast(1)).getFeatureName();
    verify(classPathEntry, atLeast(1)).getFile();
    verify(classPathEntry, atLeast(1)).getFilter();
    verify(classPathEntry, atLeast(1)).getJarFilter();
    verify(classPathEntry, atLeast(1)).getJmodFilter();
    verify(classPathEntry, atLeast(1)).getWarFilter();
    verify(classPathEntry, atLeast(1)).getZipFilter();
    verify(classPathEntry, atLeast(1)).isOutput();
  }

  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName("Test write(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite7() throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPathEntry classPathEntry = mock(ClassPathEntry.class);
    when(classPathEntry.getAabFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getAarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getApkFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getEarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJmodFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getWarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getZipFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.isOutput()).thenReturn(true);
    when(classPathEntry.getFile()).thenReturn(Configuration.STD_OUT);
    when(classPathEntry.getFeatureName()).thenReturn("Feature Name");

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt())).thenReturn(classPathEntry);
    when(classPath.size()).thenReturn(3);
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping = Configuration.STD_OUT;
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), ConfigurationConstants.SEPARATOR_KEYWORD)
            .toUri()
            .toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "java.text";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = 1L;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration = Configuration.STD_OUT;
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = new ClassPath();
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
    verify(classPathEntry, atLeast(1)).getAabFilter();
    verify(classPathEntry, atLeast(1)).getAarFilter();
    verify(classPathEntry, atLeast(1)).getApkFilter();
    verify(classPathEntry, atLeast(1)).getEarFilter();
    verify(classPathEntry, atLeast(1)).getFeatureName();
    verify(classPathEntry, atLeast(1)).getFile();
    verify(classPathEntry, atLeast(1)).getFilter();
    verify(classPathEntry, atLeast(1)).getJarFilter();
    verify(classPathEntry, atLeast(1)).getJmodFilter();
    verify(classPathEntry, atLeast(1)).getWarFilter();
    verify(classPathEntry, atLeast(1)).getZipFilter();
    verify(classPathEntry, atLeast(1)).isOutput();
  }

  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName("Test write(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite8() throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPathEntry classPathEntry = mock(ClassPathEntry.class);
    when(classPathEntry.getAabFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getAarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getApkFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getEarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJmodFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getWarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getZipFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.isOutput()).thenReturn(true);
    when(classPathEntry.getFile()).thenReturn(Configuration.STD_OUT);
    when(classPathEntry.getFeatureName()).thenReturn("Feature Name");

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt())).thenReturn(classPathEntry);
    when(classPath.size()).thenReturn(3);
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping = Configuration.STD_OUT;
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(
                System.getProperty("java.io.tmpdir"),
                ConfigurationConstants.CLOSE_ARGUMENTS_KEYWORD)
            .toUri()
            .toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "java.text";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = 1L;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration = Configuration.STD_OUT;
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = new ClassPath();
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
    verify(classPathEntry, atLeast(1)).getAabFilter();
    verify(classPathEntry, atLeast(1)).getAarFilter();
    verify(classPathEntry, atLeast(1)).getApkFilter();
    verify(classPathEntry, atLeast(1)).getEarFilter();
    verify(classPathEntry, atLeast(1)).getFeatureName();
    verify(classPathEntry, atLeast(1)).getFile();
    verify(classPathEntry, atLeast(1)).getFilter();
    verify(classPathEntry, atLeast(1)).getJarFilter();
    verify(classPathEntry, atLeast(1)).getJmodFilter();
    verify(classPathEntry, atLeast(1)).getWarFilter();
    verify(classPathEntry, atLeast(1)).getZipFilter();
    verify(classPathEntry, atLeast(1)).isOutput();
  }

  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName("Test write(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite9() throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPathEntry classPathEntry = mock(ClassPathEntry.class);
    when(classPathEntry.getAabFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getAarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getApkFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getEarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJmodFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getWarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getZipFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.isOutput()).thenReturn(true);
    when(classPathEntry.getFile()).thenReturn(Configuration.STD_OUT);
    when(classPathEntry.getFeatureName()).thenReturn("Feature Name");

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt())).thenReturn(classPathEntry);
    when(classPath.size()).thenReturn(3);
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping = Configuration.STD_OUT;
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "java.text";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = 1L;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = new ClassPath();
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
    verify(classPathEntry, atLeast(1)).getAabFilter();
    verify(classPathEntry, atLeast(1)).getAarFilter();
    verify(classPathEntry, atLeast(1)).getApkFilter();
    verify(classPathEntry, atLeast(1)).getEarFilter();
    verify(classPathEntry, atLeast(1)).getFeatureName();
    verify(classPathEntry, atLeast(1)).getFile();
    verify(classPathEntry, atLeast(1)).getFilter();
    verify(classPathEntry, atLeast(1)).getJarFilter();
    verify(classPathEntry, atLeast(1)).getJmodFilter();
    verify(classPathEntry, atLeast(1)).getWarFilter();
    verify(classPathEntry, atLeast(1)).getZipFilter();
    verify(classPathEntry, atLeast(1)).isOutput();
  }

  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link ClassPath} (default constructor) add {@link
   *       ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link Configuration#STD_OUT}
   *       and isOutput is {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName(
      "Test write(Configuration); given ClassPath (default constructor) add ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite_givenClassPathAddClassPathEntryWithFileIsStd_outAndIsOutputIsTrue()
      throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPathEntry classPathEntry = mock(ClassPathEntry.class);
    when(classPathEntry.getAabFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getAarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getApkFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getEarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJmodFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getWarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getZipFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.isOutput()).thenReturn(true);
    when(classPathEntry.getFile()).thenReturn(Configuration.STD_OUT);
    when(classPathEntry.getFeatureName()).thenReturn("Feature Name");

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt())).thenReturn(classPathEntry);
    when(classPath.size()).thenReturn(3);

    ClassPath classPath2 = new ClassPath();
    classPath2.add(new ClassPathEntry(Configuration.STD_OUT, true));
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping = Configuration.STD_OUT;
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "java.text";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = 1L;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration = Configuration.STD_OUT;
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = classPath2;
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
    verify(classPathEntry, atLeast(1)).getAabFilter();
    verify(classPathEntry, atLeast(1)).getAarFilter();
    verify(classPathEntry, atLeast(1)).getApkFilter();
    verify(classPathEntry, atLeast(1)).getEarFilter();
    verify(classPathEntry, atLeast(1)).getFeatureName();
    verify(classPathEntry, atLeast(1)).getFile();
    verify(classPathEntry, atLeast(1)).getFilter();
    verify(classPathEntry, atLeast(1)).getJarFilter();
    verify(classPathEntry, atLeast(1)).getJmodFilter();
    verify(classPathEntry, atLeast(1)).getWarFilter();
    verify(classPathEntry, atLeast(1)).getZipFilter();
    verify(classPathEntry, atLeast(1)).isOutput();
  }

  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link ClassPathEntry} {@link ClassPathEntry#getAabFilter()} return {@link
   *       ArrayList#ArrayList()}.
   *   <li>Then calls {@link ClassPathEntry#getAabFilter()}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName(
      "Test write(Configuration); given ClassPathEntry getAabFilter() return ArrayList(); then calls getAabFilter()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite_givenClassPathEntryGetAabFilterReturnArrayList_thenCallsGetAabFilter()
      throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPathEntry classPathEntry = mock(ClassPathEntry.class);
    when(classPathEntry.getAabFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getAarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getApkFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getEarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJmodFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getWarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getZipFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.isOutput()).thenReturn(true);
    when(classPathEntry.getFile()).thenReturn(Configuration.STD_OUT);
    when(classPathEntry.getFeatureName()).thenReturn("Feature Name");

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt())).thenReturn(classPathEntry);
    when(classPath.size()).thenReturn(3);
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping = Configuration.STD_OUT;
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "java.text";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = 1L;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration = Configuration.STD_OUT;
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = new ClassPath();
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
    verify(classPathEntry, atLeast(1)).getAabFilter();
    verify(classPathEntry, atLeast(1)).getAarFilter();
    verify(classPathEntry, atLeast(1)).getApkFilter();
    verify(classPathEntry, atLeast(1)).getEarFilter();
    verify(classPathEntry, atLeast(1)).getFeatureName();
    verify(classPathEntry, atLeast(1)).getFile();
    verify(classPathEntry, atLeast(1)).getFilter();
    verify(classPathEntry, atLeast(1)).getJarFilter();
    verify(classPathEntry, atLeast(1)).getJmodFilter();
    verify(classPathEntry, atLeast(1)).getWarFilter();
    verify(classPathEntry, atLeast(1)).getZipFilter();
    verify(classPathEntry, atLeast(1)).isOutput();
  }

  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <ul>
   *   <li>Given empty string.
   *   <li>When {@link Configuration} (default constructor) {@link
   *       Configuration#flattenPackageHierarchy} is empty string.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName(
      "Test write(Configuration); given empty string; when Configuration (default constructor) flattenPackageHierarchy is empty string")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite_givenEmptyString_whenConfigurationFlattenPackageHierarchyIsEmptyString()
      throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPathEntry classPathEntry = mock(ClassPathEntry.class);
    when(classPathEntry.getAabFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getAarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getApkFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getEarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJmodFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getWarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getZipFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.isOutput()).thenReturn(true);
    when(classPathEntry.getFile()).thenReturn(Configuration.STD_OUT);
    when(classPathEntry.getFeatureName()).thenReturn("Feature Name");

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt())).thenReturn(classPathEntry);
    when(classPath.size()).thenReturn(3);
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping = Configuration.STD_OUT;
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = 1L;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration = Configuration.STD_OUT;
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = new ClassPath();
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
    verify(classPathEntry, atLeast(1)).getAabFilter();
    verify(classPathEntry, atLeast(1)).getAarFilter();
    verify(classPathEntry, atLeast(1)).getApkFilter();
    verify(classPathEntry, atLeast(1)).getEarFilter();
    verify(classPathEntry, atLeast(1)).getFeatureName();
    verify(classPathEntry, atLeast(1)).getFile();
    verify(classPathEntry, atLeast(1)).getFilter();
    verify(classPathEntry, atLeast(1)).getJarFilter();
    verify(classPathEntry, atLeast(1)).getJmodFilter();
    verify(classPathEntry, atLeast(1)).getWarFilter();
    verify(classPathEntry, atLeast(1)).getZipFilter();
    verify(classPathEntry, atLeast(1)).isOutput();
  }

  /**
   * Test {@link ConfigurationWriter#write(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link Long#MAX_VALUE}.
   *   <li>When {@link Configuration} (default constructor) {@link Configuration#lastModified} is
   *       {@link Long#MAX_VALUE}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationWriter#write(Configuration)}
   */
  @Test
  @DisplayName(
      "Test write(Configuration); given MAX_VALUE; when Configuration (default constructor) lastModified is MAX_VALUE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationWriter.write(Configuration)"})
  void testWrite_givenMax_value_whenConfigurationLastModifiedIsMax_value() throws IOException {
    // Arrange
    ConfigurationWriter configurationWriter = new ConfigurationWriter(Configuration.STD_OUT);

    ClassPathEntry classPathEntry = mock(ClassPathEntry.class);
    when(classPathEntry.getAabFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getAarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getApkFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getEarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getJmodFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getWarFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.getZipFilter()).thenReturn(new ArrayList<>());
    when(classPathEntry.isOutput()).thenReturn(true);
    when(classPathEntry.getFile()).thenReturn(Configuration.STD_OUT);
    when(classPathEntry.getFeatureName()).thenReturn("Feature Name");

    ClassPath classPath = mock(ClassPath.class);
    when(classPath.get(anyInt())).thenReturn(classPathEntry);
    when(classPath.size()).thenReturn(3);
    Configuration configuration = new Configuration();
    configuration.adaptClassStrings = new ArrayList<>();
    configuration.adaptResourceFileContents = new ArrayList<>();
    configuration.adaptResourceFileNames = new ArrayList<>();
    configuration.addConfigurationDebugging = true;
    configuration.allowAccessModification = true;
    configuration.android = true;
    configuration.applyMapping = Configuration.STD_OUT;
    configuration.assumeNoEscapingParameters = new ArrayList<>();
    configuration.assumeNoExternalReturnValues = new ArrayList<>();
    configuration.assumeNoExternalSideEffects = new ArrayList<>();
    configuration.assumeNoSideEffects = new ArrayList<>();
    configuration.assumeValues = new ArrayList<>();
    configuration.backport = true;
    configuration.classObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.dontCompress = new ArrayList<>();
    configuration.dontProcessKotlinMetadata = true;
    configuration.dump = Configuration.STD_OUT;
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Configuration.STD_OUT;
    configuration.flattenPackageHierarchy = "java.text";
    configuration.ignoreWarnings = true;
    configuration.keep = new ArrayList<>();
    configuration.keepAttributes = new ArrayList<>();
    configuration.keepDirectories = new ArrayList();
    configuration.keepKotlinMetadata = true;
    configuration.keepPackageNames = new ArrayList<>();
    configuration.keepParameterNames = true;
    configuration.keyAliases = new ArrayList<>();
    configuration.keyPasswords = new ArrayList<>();
    configuration.keyStorePasswords = new ArrayList<>();
    configuration.keyStores = new ArrayList<>();
    configuration.lastModified = Long.MAX_VALUE;
    configuration.libraryJars = classPath;
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = ConfigurationConstants.INJARS_OPTION;
    configuration.note = new ArrayList<>();
    configuration.obfuscate = true;
    configuration.obfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.optimizationPasses = 1;
    configuration.optimizations = new ArrayList<>();
    configuration.optimize = true;
    configuration.optimizeConservatively = true;
    configuration.overloadAggressively = true;
    configuration.packageObfuscationDictionary =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();
    configuration.preverify = true;
    configuration.printConfiguration = Configuration.STD_OUT;
    configuration.printMapping = Configuration.STD_OUT;
    configuration.printSeeds = Configuration.STD_OUT;
    configuration.printUsage = Configuration.STD_OUT;
    configuration.programJars = new ClassPath();
    configuration.repackageClasses = "java.text";
    configuration.shrink = true;
    configuration.skipNonPublicLibraryClassMembers = true;
    configuration.skipNonPublicLibraryClasses = true;
    configuration.targetClassVersion = 1;
    configuration.useMixedCaseClassNames = true;
    configuration.useUniqueClassMemberNames = true;
    configuration.verbose = true;
    configuration.warn = new ArrayList<>();
    configuration.whyAreYouKeeping = new ArrayList<>();
    configuration.zipAlign = 1;

    // Act
    configurationWriter.write(configuration);

    // Assert
    verify(classPath, atLeast(1)).get(anyInt());
    verify(classPath, atLeast(1)).size();
    verify(classPathEntry, atLeast(1)).getAabFilter();
    verify(classPathEntry, atLeast(1)).getAarFilter();
    verify(classPathEntry, atLeast(1)).getApkFilter();
    verify(classPathEntry, atLeast(1)).getEarFilter();
    verify(classPathEntry, atLeast(1)).getFeatureName();
    verify(classPathEntry, atLeast(1)).getFile();
    verify(classPathEntry, atLeast(1)).getFilter();
    verify(classPathEntry, atLeast(1)).getJarFilter();
    verify(classPathEntry, atLeast(1)).getJmodFilter();
    verify(classPathEntry, atLeast(1)).getWarFilter();
    verify(classPathEntry, atLeast(1)).getZipFilter();
    verify(classPathEntry, atLeast(1)).isOutput();
  }
}
