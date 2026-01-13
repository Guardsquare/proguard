package proguard.ant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileList.FileName;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.selectors.SelectSelector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.ClassPath;
import proguard.ClassSpecification;
import proguard.Configuration;
import proguard.KeepClassSpecification;

@ExtendWith(MockitoExtension.class)
class ConfigurationTaskDiffblueTest {
  @InjectMocks private ConfigurationTask configurationTask;

  /**
   * Test {@link ConfigurationTask#appendTo(Configuration)}.
   *
   * <p>Method under test: {@link ConfigurationTask#appendTo(Configuration)}
   */
  @Test
  @DisplayName("Test appendTo(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.appendTo(Configuration)"})
  void testAppendTo() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredInjar(new ClassPathElement(new Project()));

    // Act
    configurationTask.appendTo(mock(Configuration.class));

    // Assert that nothing has changed
    Configuration configuration = configurationTask.configuration;
    assertNull(configuration.keepDirectories);
    assertNull(configuration.keep);
  }

  /**
   * Test {@link ConfigurationTask#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link ConfigurationTask} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#appendTo(Configuration)}
   */
  @Test
  @DisplayName("Test appendTo(Configuration); given ConfigurationTask (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.appendTo(Configuration)"})
  void testAppendTo_givenConfigurationTask() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.appendTo(mock(Configuration.class));

    // Assert that nothing has changed
    Configuration configuration = configurationTask.configuration;
    assertNull(configuration.keepDirectories);
    assertNull(configuration.keep);
  }

  /**
   * Test {@link ConfigurationTask#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Then {@link Configuration} (default constructor) {@link Configuration#keep} Empty.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#appendTo(Configuration)}
   */
  @Test
  @DisplayName("Test appendTo(Configuration); then Configuration (default constructor) keep Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.appendTo(Configuration)"})
  void testAppendTo_thenConfigurationKeepEmpty() throws MalformedURLException {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredInjar(new ClassPathElement(new Project()));
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
    configuration.dump = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
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
    configuration.libraryJars = new ClassPath();
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = "New Source File Attribute";
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
    configuration.printMapping =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    configuration.printSeeds = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    configuration.printUsage = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
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
    configurationTask.appendTo(configuration);

    // Assert that nothing has changed
    assertTrue(configuration.keep.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Then {@link Configuration} (default constructor) {@link Configuration#keep} size is one.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#appendTo(Configuration)}
   */
  @Test
  @DisplayName(
      "Test appendTo(Configuration); then Configuration (default constructor) keep size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.appendTo(Configuration)"})
  void testAppendTo_thenConfigurationKeepSizeIsOne() throws MalformedURLException {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeep(new KeepSpecificationElement());
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
    configuration.dump = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
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
    configuration.libraryJars = new ClassPath();
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = "New Source File Attribute";
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
    configuration.printMapping =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    configuration.printSeeds = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    configuration.printUsage = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
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
    configurationTask.appendTo(configuration);

    // Assert
    assertEquals(1, configuration.keep.size());
    assertEquals(1, configurationTask.configuration.keep.size());
  }

  /**
   * Test {@link ConfigurationTask#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} (default constructor) {@link
   *       ConfigurationTask#configuration} {@link Configuration#keepDirectories} Empty.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#appendTo(Configuration)}
   */
  @Test
  @DisplayName(
      "Test appendTo(Configuration); then ConfigurationTask (default constructor) configuration keepDirectories Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.appendTo(Configuration)"})
  void testAppendTo_thenConfigurationTaskConfigurationKeepDirectoriesEmpty() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeepdirectory(new FilterElement());

    // Act
    configurationTask.appendTo(mock(Configuration.class));

    // Assert
    Configuration configuration = configurationTask.configuration;
    assertNull(configuration.keep);
    assertTrue(configuration.keepDirectories.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} (default constructor) {@link
   *       ConfigurationTask#configuration} {@link Configuration#keepDirectories} Empty.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#appendTo(Configuration)}
   */
  @Test
  @DisplayName(
      "Test appendTo(Configuration); then ConfigurationTask (default constructor) configuration keepDirectories Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.appendTo(Configuration)"})
  void testAppendTo_thenConfigurationTaskConfigurationKeepDirectoriesEmpty2()
      throws MalformedURLException {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeepdirectory(new FilterElement());
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
    configuration.dump = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    configuration.enableKotlinAsserter = true;
    configuration.extraJar = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
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
    configuration.libraryJars = new ClassPath();
    configuration.mergeInterfacesAggressively = true;
    configuration.microEdition = true;
    configuration.newSourceFileAttribute = "New Source File Attribute";
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
    configuration.printMapping =
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    configuration.printSeeds = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
    configuration.printUsage = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
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
    configurationTask.appendTo(configuration);

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.keepDirectories.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} (default constructor) {@link
   *       ConfigurationTask#configuration} {@link Configuration#keep} size is one.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#appendTo(Configuration)}
   */
  @Test
  @DisplayName(
      "Test appendTo(Configuration); then ConfigurationTask (default constructor) configuration keep size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.appendTo(Configuration)"})
  void testAppendTo_thenConfigurationTaskConfigurationKeepSizeIsOne() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeep(new KeepSpecificationElement());

    // Act
    configurationTask.appendTo(mock(Configuration.class));

    // Assert
    Configuration configuration = configurationTask.configuration;
    assertNull(configuration.keepDirectories);
    assertEquals(1, configuration.keep.size());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}
   */
  @Test
  @DisplayName("Test addConfiguredInjar(ClassPathElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredInjar(ClassPathElement)"})
  void testAddConfiguredInjar() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredInjar(new ClassPathElement(new Project()));

    // Act
    configurationTask.addConfiguredInjar(new ClassPathElement(new Project()));

    // Assert that nothing has changed
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(0, classPath.size());
    assertFalse(classPath.hasOutput());
    assertTrue(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}
   */
  @Test
  @DisplayName("Test addConfiguredInjar(ClassPathElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredInjar(ClassPathElement)"})
  void testAddConfiguredInjar2() throws BuildException {
    // Arrange
    FileName name = new FileName();
    name.setName("Name");

    FileName name2 = new FileName();
    name2.setName("42");

    FileName name3 = new FileName();
    name3.setName("");

    FileName name4 = new FileName();
    name4.setName("Name");

    FileList fl = new FileList();
    fl.setDir(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    fl.addConfiguredFile(name4);
    fl.addConfiguredFile(name3);
    fl.addConfiguredFile(name2);
    fl.addConfiguredFile(name);

    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFilelist(fl);

    // Act
    configurationTask.addConfiguredInjar(classPathElement);

    // Assert
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(3, classPath.size());
    assertFalse(classPath.hasOutput());
    assertFalse(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}
   */
  @Test
  @DisplayName("Test addConfiguredInjar(ClassPathElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredInjar(ClassPathElement)"})
  void testAddConfiguredInjar3() throws BuildException {
    // Arrange
    FileSet fs = new FileSet();
    fs.setFile(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFileset(fs);

    // Act
    configurationTask.addConfiguredInjar(classPathElement);

    // Assert
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(1, classPath.size());
    assertFalse(classPath.hasOutput());
    assertFalse(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link Project} (default constructor) addDataTypeDefinition {@code 42} and {@link
   *       Object}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredInjar(ClassPathElement); given '42'; when Project (default constructor) addDataTypeDefinition '42' and Object")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredInjar(ClassPathElement)"})
  void testAddConfiguredInjar_given42_whenProjectAddDataTypeDefinition42AndObject() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    Project project = new Project();
    Class<Object> typeClass = Object.class;
    project.addDataTypeDefinition("42", typeClass);
    project.addBuildListener(new AntClassLoader());

    // Act
    configurationTask.addConfiguredInjar(new ClassPathElement(project));

    // Assert
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(0, classPath.size());
    assertFalse(classPath.hasOutput());
    assertTrue(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>Given {@link AntClassLoader#AntClassLoader()}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}
   */
  @Test
  @DisplayName("Test addConfiguredInjar(ClassPathElement); given AntClassLoader()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredInjar(ClassPathElement)"})
  void testAddConfiguredInjar_givenAntClassLoader() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    Project project = new Project();
    project.addBuildListener(new AntClassLoader());

    // Act
    configurationTask.addConfiguredInjar(new ClassPathElement(project));

    // Assert
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(0, classPath.size());
    assertFalse(classPath.hasOutput());
    assertTrue(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>Given {@link FileList#FileList()}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}
   */
  @Test
  @DisplayName("Test addConfiguredInjar(ClassPathElement); given FileList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredInjar(ClassPathElement)"})
  void testAddConfiguredInjar_givenFileList() throws BuildException {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFilelist(new FileList());

    // Act
    configurationTask.addConfiguredInjar(classPathElement);

    // Assert
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(0, classPath.size());
    assertFalse(classPath.hasOutput());
    assertTrue(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} {@link ConfigurationTask#configuration} {@link
   *       Configuration#programJars} size is one.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredInjar(ClassPathElement); then ConfigurationTask configuration programJars size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredInjar(ClassPathElement)"})
  void testAddConfiguredInjar_thenConfigurationTaskConfigurationProgramJarsSizeIsOne()
      throws BuildException {
    // Arrange
    FileName name = new FileName();
    name.setName("Name");

    FileList fl = new FileList();
    fl.addConfiguredFile(name);

    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFilelist(fl);

    // Act
    configurationTask.addConfiguredInjar(classPathElement);

    // Assert
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(1, classPath.size());
    assertFalse(classPath.hasOutput());
    assertFalse(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} {@link ConfigurationTask#configuration} {@link
   *       Configuration#programJars} size is three.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredInjar(ClassPathElement); then ConfigurationTask configuration programJars size is three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredInjar(ClassPathElement)"})
  void testAddConfiguredInjar_thenConfigurationTaskConfigurationProgramJarsSizeIsThree()
      throws BuildException {
    // Arrange
    FileName name = new FileName();
    name.setName("Name");

    FileName name2 = new FileName();
    name2.setName("42");

    FileName name3 = new FileName();
    name3.setName("");

    FileList fl = new FileList();
    fl.addConfiguredFile(name3);
    fl.addConfiguredFile(name2);
    fl.addConfiguredFile(name);

    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFilelist(fl);

    // Act
    configurationTask.addConfiguredInjar(classPathElement);

    // Assert
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(3, classPath.size());
    assertFalse(classPath.hasOutput());
    assertFalse(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} {@link ConfigurationTask#configuration} {@link
   *       Configuration#programJars} size is three.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredInjar(ClassPathElement); then ConfigurationTask configuration programJars size is three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredInjar(ClassPathElement)"})
  void testAddConfiguredInjar_thenConfigurationTaskConfigurationProgramJarsSizeIsThree2()
      throws BuildException {
    // Arrange
    FileName name = new FileName();
    name.setName("Name");

    FileName name2 = new FileName();
    name2.setName("42");

    FileName name3 = new FileName();
    name3.setName("");

    FileName name4 = new FileName();
    name4.setName("Name");

    FileList fl = new FileList();
    fl.addConfiguredFile(name4);
    fl.addConfiguredFile(name3);
    fl.addConfiguredFile(name2);
    fl.addConfiguredFile(name);

    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFilelist(fl);

    // Act
    configurationTask.addConfiguredInjar(classPathElement);

    // Assert
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(3, classPath.size());
    assertFalse(classPath.hasOutput());
    assertFalse(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} {@link ConfigurationTask#configuration} {@link
   *       Configuration#programJars} size is two.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredInjar(ClassPathElement); then ConfigurationTask configuration programJars size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredInjar(ClassPathElement)"})
  void testAddConfiguredInjar_thenConfigurationTaskConfigurationProgramJarsSizeIsTwo()
      throws BuildException {
    // Arrange
    FileName name = new FileName();
    name.setName("Name");

    FileName name2 = new FileName();
    name2.setName("42");

    FileList fl = new FileList();
    fl.addConfiguredFile(name2);
    fl.addConfiguredFile(name);

    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFilelist(fl);

    // Act
    configurationTask.addConfiguredInjar(classPathElement);

    // Assert
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(2, classPath.size());
    assertFalse(classPath.hasOutput());
    assertFalse(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>When {@link ClassPathElement#ClassPathElement(Project)} with project is {@link Project}
   *       (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredInjar(ClassPathElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredInjar(ClassPathElement); when ClassPathElement(Project) with project is Project (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredInjar(ClassPathElement)"})
  void testAddConfiguredInjar_whenClassPathElementWithProjectIsProject() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredInjar(new ClassPathElement(new Project()));

    // Assert
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(0, classPath.size());
    assertFalse(classPath.hasOutput());
    assertTrue(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredOutjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>Given {@code ]}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredOutjar(ClassPathElement)}
   */
  @Test
  @DisplayName("Test addConfiguredOutjar(ClassPathElement); given ']'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredOutjar(ClassPathElement)"})
  void testAddConfiguredOutjar_givenRightSquareBracket() throws BuildException {
    // Arrange
    Project project = new Project();
    Class<Object> typeClass = Object.class;
    project.addDataTypeDefinition("]", typeClass);
    project.addBuildListener(new AntClassLoader());

    FileSet fs = new FileSet();
    fs.setFile(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    ClassPathElement classPathElement = new ClassPathElement(project);
    classPathElement.addFileset(fs);

    // Act
    configurationTask.addConfiguredOutjar(classPathElement);

    // Assert
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(1, classPath.size());
    assertFalse(classPath.isEmpty());
    assertTrue(classPath.hasOutput());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredOutjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} {@link ConfigurationTask#configuration} {@link
   *       Configuration#programJars} size is one.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredOutjar(ClassPathElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredOutjar(ClassPathElement); then ConfigurationTask configuration programJars size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredOutjar(ClassPathElement)"})
  void testAddConfiguredOutjar_thenConfigurationTaskConfigurationProgramJarsSizeIsOne()
      throws BuildException {
    // Arrange
    FileSet fs = new FileSet();
    fs.setFile(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFileset(fs);

    // Act
    configurationTask.addConfiguredOutjar(classPathElement);

    // Assert
    ClassPath classPath = configurationTask.configuration.programJars;
    assertEquals(1, classPath.size());
    assertFalse(classPath.isEmpty());
    assertTrue(classPath.hasOutput());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredLibraryjar(ClassPathElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredLibraryjar(ClassPathElement)}
   */
  @Test
  @DisplayName("Test addConfiguredLibraryjar(ClassPathElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredLibraryjar(ClassPathElement)"})
  void testAddConfiguredLibraryjar() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredLibraryjar(new ClassPathElement(new Project()));

    // Act
    configurationTask.addConfiguredLibraryjar(new ClassPathElement(new Project()));

    // Assert that nothing has changed
    ClassPath classPath = configurationTask.configuration.libraryJars;
    assertEquals(0, classPath.size());
    assertFalse(classPath.hasOutput());
    assertTrue(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredLibraryjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link Project} (default constructor) addDataTypeDefinition {@code 42} and {@link
   *       Object}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredLibraryjar(ClassPathElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredLibraryjar(ClassPathElement); given '42'; when Project (default constructor) addDataTypeDefinition '42' and Object")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredLibraryjar(ClassPathElement)"})
  void testAddConfiguredLibraryjar_given42_whenProjectAddDataTypeDefinition42AndObject() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    Project project = new Project();
    Class<Object> typeClass = Object.class;
    project.addDataTypeDefinition("42", typeClass);
    project.addBuildListener(new AntClassLoader());

    // Act
    configurationTask.addConfiguredLibraryjar(new ClassPathElement(project));

    // Assert
    ClassPath classPath = configurationTask.configuration.libraryJars;
    assertEquals(0, classPath.size());
    assertFalse(classPath.hasOutput());
    assertTrue(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredLibraryjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>Given {@link AntClassLoader#AntClassLoader()}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredLibraryjar(ClassPathElement)}
   */
  @Test
  @DisplayName("Test addConfiguredLibraryjar(ClassPathElement); given AntClassLoader()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredLibraryjar(ClassPathElement)"})
  void testAddConfiguredLibraryjar_givenAntClassLoader() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    Project project = new Project();
    project.addBuildListener(new AntClassLoader());

    // Act
    configurationTask.addConfiguredLibraryjar(new ClassPathElement(project));

    // Assert
    ClassPath classPath = configurationTask.configuration.libraryJars;
    assertEquals(0, classPath.size());
    assertFalse(classPath.hasOutput());
    assertTrue(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredLibraryjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>Given {@link FileList#FileList()}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredLibraryjar(ClassPathElement)}
   */
  @Test
  @DisplayName("Test addConfiguredLibraryjar(ClassPathElement); given FileList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredLibraryjar(ClassPathElement)"})
  void testAddConfiguredLibraryjar_givenFileList() throws BuildException {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFilelist(new FileList());

    // Act
    configurationTask.addConfiguredLibraryjar(classPathElement);

    // Assert
    ClassPath classPath = configurationTask.configuration.libraryJars;
    assertEquals(0, classPath.size());
    assertFalse(classPath.hasOutput());
    assertTrue(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredLibraryjar(ClassPathElement)}.
   *
   * <ul>
   *   <li>When {@link ClassPathElement#ClassPathElement(Project)} with project is {@link Project}
   *       (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredLibraryjar(ClassPathElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredLibraryjar(ClassPathElement); when ClassPathElement(Project) with project is Project (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredLibraryjar(ClassPathElement)"})
  void testAddConfiguredLibraryjar_whenClassPathElementWithProjectIsProject() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredLibraryjar(new ClassPathElement(new Project()));

    // Assert
    ClassPath classPath = configurationTask.configuration.libraryJars;
    assertEquals(0, classPath.size());
    assertFalse(classPath.hasOutput());
    assertTrue(classPath.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepdirectory(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeepdirectory(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepdirectory(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepdirectory(FilterElement)"})
  void testAddConfiguredKeepdirectory() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredKeepdirectory(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.keepDirectories.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepdirectory(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeepdirectory(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepdirectory(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepdirectory(FilterElement)"})
  void testAddConfiguredKeepdirectory2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeepdirectory(new FilterElement());

    // Act
    configurationTask.addConfiguredKeepdirectory(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.keepDirectories.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepdirectory(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeepdirectory(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepdirectory(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepdirectory(FilterElement)"})
  void testAddConfiguredKeepdirectory3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredKeepdirectory(filterElement);

    // Assert
    List list = configurationTask.configuration.keepDirectories;
    assertEquals(1, list.size());
    assertEquals("Name", list.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepdirectories(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeepdirectories(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepdirectories(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepdirectories(FilterElement)"})
  void testAddConfiguredKeepdirectories() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredKeepdirectories(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.keepDirectories.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepdirectories(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeepdirectories(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepdirectories(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepdirectories(FilterElement)"})
  void testAddConfiguredKeepdirectories2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeepdirectory(new FilterElement());

    // Act
    configurationTask.addConfiguredKeepdirectories(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.keepDirectories.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepdirectories(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeepdirectories(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepdirectories(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepdirectories(FilterElement)"})
  void testAddConfiguredKeepdirectories3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredKeepdirectories(filterElement);

    // Assert
    List list = configurationTask.configuration.keepDirectories;
    assertEquals(1, list.size());
    assertEquals("Name", list.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeep(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeep(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeep(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeep(KeepSpecificationElement)"})
  void testAddConfiguredKeep() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredKeep(new KeepSpecificationElement());

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeep(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeep(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeep(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeep(KeepSpecificationElement)"})
  void testAddConfiguredKeep2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeep(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeep(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeep(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeep(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeep(KeepSpecificationElement)"})
  void testAddConfiguredKeep3() {
    // Arrange
    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setExtendsannotation("Extends Annotation");
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeep(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("LExtends Annotation;", getResult.extendsAnnotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeep(KeepSpecificationElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} (default constructor) {@link
   *       ConfigurationTask#configuration} {@link Configuration#keep} first {@link
   *       ClassSpecification#className} is {@code Name}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeep(KeepSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredKeep(KeepSpecificationElement); then ConfigurationTask (default constructor) configuration keep first className is 'Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeep(KeepSpecificationElement)"})
  void testAddConfiguredKeep_thenConfigurationTaskConfigurationKeepFirstClassNameIsName() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setName("Name");

    // Act
    configurationTask.addConfiguredKeep(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("Name", getResult.className);
    assertNull(getResult.annotationType);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeep(KeepSpecificationElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} {@link ConfigurationTask#configuration} {@link
   *       Configuration#keep} first {@link ClassSpecification#className} is {@code Name}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeep(KeepSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredKeep(KeepSpecificationElement); then ConfigurationTask configuration keep first className is 'Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeep(KeepSpecificationElement)"})
  void testAddConfiguredKeep_thenConfigurationTaskConfigurationKeepFirstClassNameIsName2() {
    // Arrange
    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setName("Name");
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeep(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("Name", getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeep(KeepSpecificationElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} (default constructor) {@link
   *       ConfigurationTask#configuration} {@link Configuration#keep} size is two.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeep(KeepSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredKeep(KeepSpecificationElement); then ConfigurationTask (default constructor) configuration keep size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeep(KeepSpecificationElement)"})
  void testAddConfiguredKeep_thenConfigurationTaskConfigurationKeepSizeIsTwo() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeep(new KeepSpecificationElement());

    // Act
    configurationTask.addConfiguredKeep(new KeepSpecificationElement());

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(2, keepClassSpecificationList.size());
    KeepClassSpecification expectedGetResult = keepClassSpecificationList.get(0);
    assertEquals(expectedGetResult, keepClassSpecificationList.get(1));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclassmembers(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclassmembers(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclassmembers(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclassmembers(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclassmembers() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredKeepclassmembers(new KeepSpecificationElement());

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markClasses);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclassmembers(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclassmembers(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclassmembers(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclassmembers(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclassmembers2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeep(new KeepSpecificationElement());

    // Act
    configurationTask.addConfiguredKeepclassmembers(new KeepSpecificationElement());

    // Assert
    assertEquals(2, configurationTask.configuration.keep.size());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclassmembers(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclassmembers(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclassmembers(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclassmembers(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclassmembers3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepclassmembers(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markClasses);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclassmembers(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclassmembers(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclassmembers(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclassmembers(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclassmembers4() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setName("Name");

    // Act
    configurationTask.addConfiguredKeepclassmembers(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("Name", getResult.className);
    assertNull(getResult.annotationType);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markClasses);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclassmembers(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclassmembers(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclassmembers(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclassmembers(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclassmembers5() {
    // Arrange
    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setName("Name");
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepclassmembers(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("Name", getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markClasses);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclassmembers(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclassmembers(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclassmembers(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclassmembers(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclassmembers6() {
    // Arrange
    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setExtendsannotation("Extends Annotation");
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepclassmembers(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("LExtends Annotation;", getResult.extendsAnnotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markClasses);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclasseswithmembers(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembers(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclasseswithmembers(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclasseswithmembers(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclasseswithmembers() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredKeepclasseswithmembers(new KeepSpecificationElement());

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
    assertTrue(getResult.markConditionally);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclasseswithmembers(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembers(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclasseswithmembers(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclasseswithmembers(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclasseswithmembers2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeep(new KeepSpecificationElement());

    // Act
    configurationTask.addConfiguredKeepclasseswithmembers(new KeepSpecificationElement());

    // Assert
    assertEquals(2, configurationTask.configuration.keep.size());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclasseswithmembers(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembers(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclasseswithmembers(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclasseswithmembers(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclasseswithmembers3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepclasseswithmembers(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
    assertTrue(getResult.markConditionally);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclasseswithmembers(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembers(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclasseswithmembers(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclasseswithmembers(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclasseswithmembers4() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setName("Name");

    // Act
    configurationTask.addConfiguredKeepclasseswithmembers(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("Name", getResult.className);
    assertNull(getResult.annotationType);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
    assertTrue(getResult.markConditionally);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclasseswithmembers(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembers(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclasseswithmembers(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclasseswithmembers(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclasseswithmembers5() {
    // Arrange
    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setName("Name");
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepclasseswithmembers(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("Name", getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
    assertTrue(getResult.markConditionally);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclasseswithmembers(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembers(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclasseswithmembers(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclasseswithmembers(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclasseswithmembers6() {
    // Arrange
    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setExtendsannotation("Extends Annotation");
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepclasseswithmembers(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("LExtends Annotation;", getResult.extendsAnnotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.allowShrinking);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
    assertTrue(getResult.markConditionally);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepnames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepnames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepnames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepnames(KeepSpecificationElement)"})
  void testAddConfiguredKeepnames() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredKeepnames(new KeepSpecificationElement());

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepnames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepnames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepnames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepnames(KeepSpecificationElement)"})
  void testAddConfiguredKeepnames2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepnames(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepnames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepnames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepnames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepnames(KeepSpecificationElement)"})
  void testAddConfiguredKeepnames3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setName("Name");

    // Act
    configurationTask.addConfiguredKeepnames(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("Name", getResult.className);
    assertNull(getResult.annotationType);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepnames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepnames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepnames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepnames(KeepSpecificationElement)"})
  void testAddConfiguredKeepnames4() {
    // Arrange
    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setName("Name");
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepnames(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("Name", getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepnames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepnames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepnames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepnames(KeepSpecificationElement)"})
  void testAddConfiguredKeepnames5() {
    // Arrange
    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setExtendsannotation("Extends Annotation");
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepnames(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("LExtends Annotation;", getResult.extendsAnnotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepnames(KeepSpecificationElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} (default constructor) {@link
   *       ConfigurationTask#configuration} {@link Configuration#keep} size is two.
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepnames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredKeepnames(KeepSpecificationElement); then ConfigurationTask (default constructor) configuration keep size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepnames(KeepSpecificationElement)"})
  void testAddConfiguredKeepnames_thenConfigurationTaskConfigurationKeepSizeIsTwo() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeep(new KeepSpecificationElement());

    // Act
    configurationTask.addConfiguredKeepnames(new KeepSpecificationElement());

    // Assert
    assertEquals(2, configurationTask.configuration.keep.size());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclassmembernames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclassmembernames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclassmembernames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclassmembernames(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclassmembernames() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredKeepclassmembernames(new KeepSpecificationElement());

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markClasses);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclassmembernames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclassmembernames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclassmembernames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclassmembernames(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclassmembernames2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeep(new KeepSpecificationElement());

    // Act
    configurationTask.addConfiguredKeepclassmembernames(new KeepSpecificationElement());

    // Assert
    assertEquals(2, configurationTask.configuration.keep.size());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclassmembernames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclassmembernames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclassmembernames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclassmembernames(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclassmembernames3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepclassmembernames(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markClasses);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclassmembernames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclassmembernames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclassmembernames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclassmembernames(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclassmembernames4() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setName("Name");

    // Act
    configurationTask.addConfiguredKeepclassmembernames(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("Name", getResult.className);
    assertNull(getResult.annotationType);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markClasses);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclassmembernames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclassmembernames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclassmembernames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclassmembernames(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclassmembernames5() {
    // Arrange
    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setName("Name");
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepclassmembernames(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("Name", getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markClasses);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepclassmembernames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclassmembernames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclassmembernames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclassmembernames(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclassmembernames6() {
    // Arrange
    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setExtendsannotation("Extends Annotation");
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepclassmembernames(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("LExtends Annotation;", getResult.extendsAnnotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markClasses);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markConditionally);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclasseswithmembernames() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredKeepclasseswithmembernames(new KeepSpecificationElement());

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
    assertTrue(getResult.markConditionally);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclasseswithmembernames2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeep(new KeepSpecificationElement());

    // Act
    configurationTask.addConfiguredKeepclasseswithmembernames(new KeepSpecificationElement());

    // Assert
    assertEquals(2, configurationTask.configuration.keep.size());
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclasseswithmembernames3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepclasseswithmembernames(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
    assertTrue(getResult.markConditionally);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclasseswithmembernames4() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setName("Name");

    // Act
    configurationTask.addConfiguredKeepclasseswithmembernames(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("Name", getResult.className);
    assertNull(getResult.annotationType);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
    assertTrue(getResult.markConditionally);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclasseswithmembernames5() {
    // Arrange
    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setName("Name");
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepclasseswithmembernames(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("Name", getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
    assertTrue(getResult.markConditionally);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredKeepclasseswithmembernames(KeepSpecificationElement)"
  })
  void testAddConfiguredKeepclasseswithmembernames6() {
    // Arrange
    KeepSpecificationElement keepSpecificationElement = new KeepSpecificationElement();
    keepSpecificationElement.setExtendsannotation("Extends Annotation");
    keepSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredKeepclasseswithmembernames(keepSpecificationElement);

    // Assert
    List<KeepClassSpecification> keepClassSpecificationList = configurationTask.configuration.keep;
    assertEquals(1, keepClassSpecificationList.size());
    KeepClassSpecification getResult = keepClassSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("LExtends Annotation;", getResult.extendsAnnotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertNull(getResult.condition);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertFalse(getResult.allowObfuscation);
    assertFalse(getResult.allowOptimization);
    assertFalse(getResult.markCodeAttributes);
    assertFalse(getResult.markDescriptorClasses);
    assertTrue(getResult.allowShrinking);
    assertTrue(getResult.markClassMembers);
    assertTrue(getResult.markClasses);
    assertTrue(getResult.markConditionally);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredWhyareyoukeeping(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredWhyareyoukeeping(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredWhyareyoukeeping(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredWhyareyoukeeping(ClassSpecificationElement)"
  })
  void testAddConfiguredWhyareyoukeeping() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredWhyareyoukeeping(new ClassSpecificationElement());

    // Act
    configurationTask.addConfiguredWhyareyoukeeping(new ClassSpecificationElement());

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.whyAreYouKeeping;
    assertEquals(2, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertEquals(getResult, classSpecificationList.get(1));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredWhyareyoukeeping(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredWhyareyoukeeping(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredWhyareyoukeeping(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredWhyareyoukeeping(ClassSpecificationElement)"
  })
  void testAddConfiguredWhyareyoukeeping2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredWhyareyoukeeping(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.whyAreYouKeeping;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredWhyareyoukeeping(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredWhyareyoukeeping(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredWhyareyoukeeping(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredWhyareyoukeeping(ClassSpecificationElement)"
  })
  void testAddConfiguredWhyareyoukeeping3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");

    // Act
    configurationTask.addConfiguredWhyareyoukeeping(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.whyAreYouKeeping;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("Name", getResult.className);
    assertNull(getResult.annotationType);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredWhyareyoukeeping(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredWhyareyoukeeping(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredWhyareyoukeeping(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredWhyareyoukeeping(ClassSpecificationElement)"
  })
  void testAddConfiguredWhyareyoukeeping4() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredWhyareyoukeeping(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.whyAreYouKeeping;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("Name", getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredWhyareyoukeeping(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredWhyareyoukeeping(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredWhyareyoukeeping(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredWhyareyoukeeping(ClassSpecificationElement)"
  })
  void testAddConfiguredWhyareyoukeeping5() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setExtendsannotation("Extends Annotation");
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredWhyareyoukeeping(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.whyAreYouKeeping;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("LExtends Annotation;", getResult.extendsAnnotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredWhyareyoukeeping(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>When {@link ClassSpecificationElement} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredWhyareyoukeeping(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredWhyareyoukeeping(ClassSpecificationElement); when ClassSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredWhyareyoukeeping(ClassSpecificationElement)"
  })
  void testAddConfiguredWhyareyoukeeping_whenClassSpecificationElement() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredWhyareyoukeeping(new ClassSpecificationElement());

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.whyAreYouKeeping;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAssumenosideeffects(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenosideeffects(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenosideeffects(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenosideeffects(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenosideeffects() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredAssumenosideeffects(new ClassSpecificationElement());

    // Act
    configurationTask.addConfiguredAssumenosideeffects(new ClassSpecificationElement());

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoSideEffects;
    assertEquals(2, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertEquals(getResult, classSpecificationList.get(1));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAssumenosideeffects(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenosideeffects(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenosideeffects(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenosideeffects(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenosideeffects2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumenosideeffects(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoSideEffects;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAssumenosideeffects(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenosideeffects(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenosideeffects(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenosideeffects(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenosideeffects3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");

    // Act
    configurationTask.addConfiguredAssumenosideeffects(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoSideEffects;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("Name", getResult.className);
    assertNull(getResult.annotationType);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAssumenosideeffects(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenosideeffects(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenosideeffects(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenosideeffects(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenosideeffects4() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumenosideeffects(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoSideEffects;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("Name", getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAssumenosideeffects(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenosideeffects(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenosideeffects(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenosideeffects(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenosideeffects5() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setExtendsannotation("Extends Annotation");
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumenosideeffects(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoSideEffects;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("LExtends Annotation;", getResult.extendsAnnotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAssumenosideeffects(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>When {@link ClassSpecificationElement} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenosideeffects(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredAssumenosideeffects(ClassSpecificationElement); when ClassSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenosideeffects(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenosideeffects_whenClassSpecificationElement() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredAssumenosideeffects(new ClassSpecificationElement());

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoSideEffects;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoexternalsideeffects() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredAssumenoexternalsideeffects(new ClassSpecificationElement());

    // Act
    configurationTask.addConfiguredAssumenoexternalsideeffects(new ClassSpecificationElement());

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoExternalSideEffects;
    assertEquals(2, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertEquals(getResult, classSpecificationList.get(1));
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoexternalsideeffects2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumenoexternalsideeffects(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoExternalSideEffects;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoexternalsideeffects3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");

    // Act
    configurationTask.addConfiguredAssumenoexternalsideeffects(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoExternalSideEffects;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("Name", getResult.className);
    assertNull(getResult.annotationType);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoexternalsideeffects4() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumenoexternalsideeffects(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoExternalSideEffects;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("Name", getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoexternalsideeffects5() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setExtendsannotation("Extends Annotation");
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumenoexternalsideeffects(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoExternalSideEffects;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("LExtends Annotation;", getResult.extendsAnnotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>When {@link ClassSpecificationElement} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement); when ClassSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoexternalsideeffects(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoexternalsideeffects_whenClassSpecificationElement() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredAssumenoexternalsideeffects(new ClassSpecificationElement());

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoExternalSideEffects;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoescapingparameters(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoescapingparameters(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoescapingparameters(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoescapingparameters(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoescapingparameters() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredAssumenoescapingparameters(new ClassSpecificationElement());

    // Act
    configurationTask.addConfiguredAssumenoescapingparameters(new ClassSpecificationElement());

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoEscapingParameters;
    assertEquals(2, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertEquals(getResult, classSpecificationList.get(1));
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoescapingparameters(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoescapingparameters(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoescapingparameters(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoescapingparameters(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoescapingparameters2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumenoescapingparameters(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoEscapingParameters;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoescapingparameters(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoescapingparameters(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoescapingparameters(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoescapingparameters(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoescapingparameters3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");

    // Act
    configurationTask.addConfiguredAssumenoescapingparameters(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoEscapingParameters;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("Name", getResult.className);
    assertNull(getResult.annotationType);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoescapingparameters(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoescapingparameters(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoescapingparameters(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoescapingparameters(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoescapingparameters4() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumenoescapingparameters(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoEscapingParameters;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("Name", getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoescapingparameters(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoescapingparameters(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoescapingparameters(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoescapingparameters(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoescapingparameters5() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setExtendsannotation("Extends Annotation");
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumenoescapingparameters(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoEscapingParameters;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("LExtends Annotation;", getResult.extendsAnnotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoescapingparameters(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>When {@link ClassSpecificationElement} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoescapingparameters(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredAssumenoescapingparameters(ClassSpecificationElement); when ClassSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoescapingparameters(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoescapingparameters_whenClassSpecificationElement() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredAssumenoescapingparameters(new ClassSpecificationElement());

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoEscapingParameters;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoexternalreturnvalues() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredAssumenoexternalreturnvalues(new ClassSpecificationElement());

    // Act
    configurationTask.addConfiguredAssumenoexternalreturnvalues(new ClassSpecificationElement());

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoExternalReturnValues;
    assertEquals(2, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertEquals(getResult, classSpecificationList.get(1));
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoexternalreturnvalues2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumenoexternalreturnvalues(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoExternalReturnValues;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoexternalreturnvalues3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");

    // Act
    configurationTask.addConfiguredAssumenoexternalreturnvalues(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoExternalReturnValues;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("Name", getResult.className);
    assertNull(getResult.annotationType);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoexternalreturnvalues4() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumenoexternalreturnvalues(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoExternalReturnValues;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("Name", getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoexternalreturnvalues5() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setExtendsannotation("Extends Annotation");
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumenoexternalreturnvalues(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoExternalReturnValues;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("LExtends Annotation;", getResult.extendsAnnotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link
   * ConfigurationTask#addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>When {@link ClassSpecificationElement} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement); when ClassSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAssumenoexternalreturnvalues(ClassSpecificationElement)"
  })
  void testAddConfiguredAssumenoexternalreturnvalues_whenClassSpecificationElement() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredAssumenoexternalreturnvalues(new ClassSpecificationElement());

    // Assert
    List<ClassSpecification> classSpecificationList =
        configurationTask.configuration.assumeNoExternalReturnValues;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAssumevalues(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumevalues(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumevalues(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredAssumevalues(ClassSpecificationElement)"})
  void testAddConfiguredAssumevalues() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredAssumevalues(new ClassSpecificationElement());

    // Act
    configurationTask.addConfiguredAssumevalues(new ClassSpecificationElement());

    // Assert
    List<ClassSpecification> classSpecificationList = configurationTask.configuration.assumeValues;
    assertEquals(2, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
    assertEquals(getResult, classSpecificationList.get(1));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAssumevalues(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumevalues(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumevalues(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredAssumevalues(ClassSpecificationElement)"})
  void testAddConfiguredAssumevalues2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumevalues(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList = configurationTask.configuration.assumeValues;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAssumevalues(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumevalues(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumevalues(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredAssumevalues(ClassSpecificationElement)"})
  void testAddConfiguredAssumevalues3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");

    // Act
    configurationTask.addConfiguredAssumevalues(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList = configurationTask.configuration.assumeValues;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("Name", getResult.className);
    assertNull(getResult.annotationType);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAssumevalues(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumevalues(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumevalues(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredAssumevalues(ClassSpecificationElement)"})
  void testAddConfiguredAssumevalues4() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setName("Name");
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumevalues(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList = configurationTask.configuration.assumeValues;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("Name", getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAssumevalues(ClassSpecificationElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumevalues(ClassSpecificationElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAssumevalues(ClassSpecificationElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredAssumevalues(ClassSpecificationElement)"})
  void testAddConfiguredAssumevalues5() {
    // Arrange
    ClassSpecificationElement classSpecificationElement = new ClassSpecificationElement();
    classSpecificationElement.setExtendsannotation("Extends Annotation");
    classSpecificationElement.setAnnotation("Annotation");

    // Act
    configurationTask.addConfiguredAssumevalues(classSpecificationElement);

    // Assert
    List<ClassSpecification> classSpecificationList = configurationTask.configuration.assumeValues;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertEquals("LAnnotation;", getResult.annotationType);
    assertEquals("LExtends Annotation;", getResult.extendsAnnotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAssumevalues(ClassSpecificationElement)}.
   *
   * <ul>
   *   <li>When {@link ClassSpecificationElement} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAssumevalues(ClassSpecificationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredAssumevalues(ClassSpecificationElement); when ClassSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredAssumevalues(ClassSpecificationElement)"})
  void testAddConfiguredAssumevalues_whenClassSpecificationElement() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredAssumevalues(new ClassSpecificationElement());

    // Assert
    List<ClassSpecification> classSpecificationList = configurationTask.configuration.assumeValues;
    assertEquals(1, classSpecificationList.size());
    ClassSpecification getResult = classSpecificationList.get(0);
    assertNull(getResult.annotationType);
    assertNull(getResult.className);
    assertNull(getResult.comments);
    assertNull(getResult.extendsAnnotationType);
    assertNull(getResult.extendsClassName);
    assertNull(getResult.memberComments);
    assertNull(getResult.attributeNames);
    assertNull(getResult.fieldSpecifications);
    assertNull(getResult.methodSpecifications);
    assertEquals(0, getResult.requiredSetAccessFlags);
    assertEquals(0, getResult.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredOptimizations(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredOptimizations(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredOptimizations(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredOptimizations(FilterElement)"})
  void testAddConfiguredOptimizations() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredOptimizations(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.optimizations.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredOptimizations(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredOptimizations(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredOptimizations(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredOptimizations(FilterElement)"})
  void testAddConfiguredOptimizations2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredOptimizations(new FilterElement());

    // Act
    configurationTask.addConfiguredOptimizations(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.optimizations.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredOptimizations(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredOptimizations(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredOptimizations(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredOptimizations(FilterElement)"})
  void testAddConfiguredOptimizations3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredOptimizations(filterElement);

    // Assert
    List<String> stringList = configurationTask.configuration.optimizations;
    assertEquals(1, stringList.size());
    assertEquals("Name", stringList.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredOptimization(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredOptimization(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredOptimization(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredOptimization(FilterElement)"})
  void testAddConfiguredOptimization() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredOptimization(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.optimizations.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredOptimization(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredOptimization(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredOptimization(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredOptimization(FilterElement)"})
  void testAddConfiguredOptimization2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredOptimizations(new FilterElement());

    // Act
    configurationTask.addConfiguredOptimization(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.optimizations.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredOptimization(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredOptimization(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredOptimization(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredOptimization(FilterElement)"})
  void testAddConfiguredOptimization3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredOptimization(filterElement);

    // Assert
    List<String> stringList = configurationTask.configuration.optimizations;
    assertEquals(1, stringList.size());
    assertEquals("Name", stringList.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeeppackagename(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeeppackagename(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeeppackagename(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeeppackagename(FilterElement)"})
  void testAddConfiguredKeeppackagename() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredKeeppackagename(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.keepPackageNames.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeeppackagename(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeeppackagename(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeeppackagename(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeeppackagename(FilterElement)"})
  void testAddConfiguredKeeppackagename2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeeppackagename(new FilterElement());

    // Act
    configurationTask.addConfiguredKeeppackagename(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.keepPackageNames.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeeppackagename(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeeppackagename(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeeppackagename(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeeppackagename(FilterElement)"})
  void testAddConfiguredKeeppackagename3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredKeeppackagename(filterElement);

    // Assert
    List<String> stringList = configurationTask.configuration.keepPackageNames;
    assertEquals(1, stringList.size());
    assertEquals("Name", stringList.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeeppackagenames(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeeppackagenames(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeeppackagenames(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeeppackagenames(FilterElement)"})
  void testAddConfiguredKeeppackagenames() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredKeeppackagenames(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.keepPackageNames.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeeppackagenames(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeeppackagenames(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeeppackagenames(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeeppackagenames(FilterElement)"})
  void testAddConfiguredKeeppackagenames2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeeppackagename(new FilterElement());

    // Act
    configurationTask.addConfiguredKeeppackagenames(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.keepPackageNames.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeeppackagenames(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeeppackagenames(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeeppackagenames(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeeppackagenames(FilterElement)"})
  void testAddConfiguredKeeppackagenames3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredKeeppackagenames(filterElement);

    // Assert
    List<String> stringList = configurationTask.configuration.keepPackageNames;
    assertEquals(1, stringList.size());
    assertEquals("Name", stringList.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepattributes(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeepattributes(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepattributes(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepattributes(FilterElement)"})
  void testAddConfiguredKeepattributes() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredKeepattributes(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.keepAttributes.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepattributes(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeepattributes(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepattributes(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepattributes(FilterElement)"})
  void testAddConfiguredKeepattributes2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeepattributes(new FilterElement());

    // Act
    configurationTask.addConfiguredKeepattributes(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.keepAttributes.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepattributes(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeepattributes(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepattributes(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepattributes(FilterElement)"})
  void testAddConfiguredKeepattributes3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredKeepattributes(filterElement);

    // Assert
    List<String> stringList = configurationTask.configuration.keepAttributes;
    assertEquals(1, stringList.size());
    assertEquals("Name", stringList.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepattribute(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeepattribute(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepattribute(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepattribute(FilterElement)"})
  void testAddConfiguredKeepattribute() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredKeepattribute(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.keepAttributes.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepattribute(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeepattribute(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepattribute(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepattribute(FilterElement)"})
  void testAddConfiguredKeepattribute2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredKeepattributes(new FilterElement());

    // Act
    configurationTask.addConfiguredKeepattribute(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.keepAttributes.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredKeepattribute(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredKeepattribute(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredKeepattribute(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredKeepattribute(FilterElement)"})
  void testAddConfiguredKeepattribute3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredKeepattribute(filterElement);

    // Assert
    List<String> stringList = configurationTask.configuration.keepAttributes;
    assertEquals(1, stringList.size());
    assertEquals("Name", stringList.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAdaptclassstrings(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredAdaptclassstrings(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAdaptclassstrings(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredAdaptclassstrings(FilterElement)"})
  void testAddConfiguredAdaptclassstrings() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredAdaptclassstrings(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.adaptClassStrings.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAdaptclassstrings(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredAdaptclassstrings(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAdaptclassstrings(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredAdaptclassstrings(FilterElement)"})
  void testAddConfiguredAdaptclassstrings2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredAdaptclassstrings(new FilterElement());

    // Act
    configurationTask.addConfiguredAdaptclassstrings(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.adaptClassStrings.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAdaptclassstrings(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredAdaptclassstrings(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAdaptclassstrings(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredAdaptclassstrings(FilterElement)"})
  void testAddConfiguredAdaptclassstrings3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredAdaptclassstrings(filterElement);

    // Assert
    List<String> stringList = configurationTask.configuration.adaptClassStrings;
    assertEquals(1, stringList.size());
    assertEquals("Name", stringList.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAdaptresourcefilenames(FilterElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAdaptresourcefilenames(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAdaptresourcefilenames(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredAdaptresourcefilenames(FilterElement)"})
  void testAddConfiguredAdaptresourcefilenames() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredAdaptresourcefilenames(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.adaptResourceFileNames.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAdaptresourcefilenames(FilterElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAdaptresourcefilenames(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAdaptresourcefilenames(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredAdaptresourcefilenames(FilterElement)"})
  void testAddConfiguredAdaptresourcefilenames2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredAdaptresourcefilenames(new FilterElement());

    // Act
    configurationTask.addConfiguredAdaptresourcefilenames(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.adaptResourceFileNames.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAdaptresourcefilenames(FilterElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAdaptresourcefilenames(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAdaptresourcefilenames(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredAdaptresourcefilenames(FilterElement)"})
  void testAddConfiguredAdaptresourcefilenames3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredAdaptresourcefilenames(filterElement);

    // Assert
    List<String> stringList = configurationTask.configuration.adaptResourceFileNames;
    assertEquals(1, stringList.size());
    assertEquals("Name", stringList.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAdaptresourcefilecontents(FilterElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAdaptresourcefilecontents(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAdaptresourcefilecontents(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAdaptresourcefilecontents(FilterElement)"
  })
  void testAddConfiguredAdaptresourcefilecontents() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredAdaptresourcefilecontents(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.adaptResourceFileContents.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAdaptresourcefilecontents(FilterElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAdaptresourcefilecontents(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAdaptresourcefilecontents(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAdaptresourcefilecontents(FilterElement)"
  })
  void testAddConfiguredAdaptresourcefilecontents2() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredAdaptresourcefilecontents(new FilterElement());

    // Act
    configurationTask.addConfiguredAdaptresourcefilecontents(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.adaptResourceFileContents.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredAdaptresourcefilecontents(FilterElement)}.
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredAdaptresourcefilecontents(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredAdaptresourcefilecontents(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ConfigurationTask.addConfiguredAdaptresourcefilecontents(FilterElement)"
  })
  void testAddConfiguredAdaptresourcefilecontents3() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredAdaptresourcefilecontents(filterElement);

    // Assert
    List<String> stringList = configurationTask.configuration.adaptResourceFileContents;
    assertEquals(1, stringList.size());
    assertEquals("Name", stringList.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredDontnote(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredDontnote(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredDontnote(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredDontnote(FilterElement)"})
  void testAddConfiguredDontnote() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredDontnote(new FilterElement());

    // Act
    configurationTask.addConfiguredDontnote(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.note.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredDontnote(FilterElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} (default constructor) {@link
   *       ConfigurationTask#configuration} {@link Configuration#note} Empty.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredDontnote(FilterElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredDontnote(FilterElement); then ConfigurationTask (default constructor) configuration note Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredDontnote(FilterElement)"})
  void testAddConfiguredDontnote_thenConfigurationTaskConfigurationNoteEmpty() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredDontnote(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.note.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredDontnote(FilterElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} (default constructor) {@link
   *       ConfigurationTask#configuration} {@link Configuration#note} size is one.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredDontnote(FilterElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredDontnote(FilterElement); then ConfigurationTask (default constructor) configuration note size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredDontnote(FilterElement)"})
  void testAddConfiguredDontnote_thenConfigurationTaskConfigurationNoteSizeIsOne() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredDontnote(filterElement);

    // Assert
    List<String> stringList = configurationTask.configuration.note;
    assertEquals(1, stringList.size());
    assertEquals("Name", stringList.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredDontwarn(FilterElement)}.
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredDontwarn(FilterElement)}
   */
  @Test
  @DisplayName("Test addConfiguredDontwarn(FilterElement)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredDontwarn(FilterElement)"})
  void testAddConfiguredDontwarn() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.addConfiguredDontwarn(new FilterElement());

    // Act
    configurationTask.addConfiguredDontwarn(new FilterElement());

    // Assert that nothing has changed
    assertTrue(configurationTask.configuration.warn.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredDontwarn(FilterElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} (default constructor) {@link
   *       ConfigurationTask#configuration} {@link Configuration#warn} Empty.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredDontwarn(FilterElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredDontwarn(FilterElement); then ConfigurationTask (default constructor) configuration warn Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredDontwarn(FilterElement)"})
  void testAddConfiguredDontwarn_thenConfigurationTaskConfigurationWarnEmpty() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    // Act
    configurationTask.addConfiguredDontwarn(new FilterElement());

    // Assert
    assertTrue(configurationTask.configuration.warn.isEmpty());
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredDontwarn(FilterElement)}.
   *
   * <ul>
   *   <li>Then {@link ConfigurationTask} (default constructor) {@link
   *       ConfigurationTask#configuration} {@link Configuration#warn} size is one.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addConfiguredDontwarn(FilterElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredDontwarn(FilterElement); then ConfigurationTask (default constructor) configuration warn size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredDontwarn(FilterElement)"})
  void testAddConfiguredDontwarn_thenConfigurationTaskConfigurationWarnSizeIsOne() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    // Act
    configurationTask.addConfiguredDontwarn(filterElement);

    // Assert
    List<String> stringList = configurationTask.configuration.warn;
    assertEquals(1, stringList.size());
    assertEquals("Name", stringList.get(0));
  }

  /**
   * Test {@link ConfigurationTask#addConfiguredConfiguration(ConfigurationElement)}.
   *
   * <ul>
   *   <li>Given {@link ConfigurationTask} (default constructor).
   *   <li>Then calls {@link ConfigurationElement#appendTo(Configuration)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationTask#addConfiguredConfiguration(ConfigurationElement)}
   */
  @Test
  @DisplayName(
      "Test addConfiguredConfiguration(ConfigurationElement); given ConfigurationTask (default constructor); then calls appendTo(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addConfiguredConfiguration(ConfigurationElement)"})
  void testAddConfiguredConfiguration_givenConfigurationTask_thenCallsAppendTo() {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();

    ConfigurationElement configurationElement = mock(ConfigurationElement.class);
    doNothing().when(configurationElement).appendTo(Mockito.<Configuration>any());

    // Act
    configurationTask.addConfiguredConfiguration(configurationElement);

    // Assert
    verify(configurationElement).appendTo(isA(Configuration.class));
  }

  /**
   * Test {@link ConfigurationTask#addText(String)}.
   *
   * <ul>
   *   <li>Given {@link ConfigurationTask} (default constructor) Project is {@link Project} (default
   *       constructor).
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addText(String)}
   */
  @Test
  @DisplayName(
      "Test addText(String); given ConfigurationTask (default constructor) Project is Project (default constructor); then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addText(String)"})
  void testAddText_givenConfigurationTaskProjectIsProject_thenThrowBuildException()
      throws BuildException {
    // Arrange
    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.setProject(new Project());

    // Act and Assert
    assertThrows(BuildException.class, () -> configurationTask.addText("Text"));
  }

  /**
   * Test {@link ConfigurationTask#addText(String)}.
   *
   * <ul>
   *   <li>Given {@link Project} (default constructor) addBuildListener {@link
   *       AntClassLoader#AntClassLoader()}.
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addText(String)}
   */
  @Test
  @DisplayName(
      "Test addText(String); given Project (default constructor) addBuildListener AntClassLoader(); then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addText(String)"})
  void testAddText_givenProjectAddBuildListenerAntClassLoader_thenThrowBuildException()
      throws BuildException {
    // Arrange
    Project project = new Project();
    project.addBuildListener(new AntClassLoader());

    ConfigurationTask configurationTask = new ConfigurationTask();
    configurationTask.setProject(project);

    // Act and Assert
    assertThrows(BuildException.class, () -> configurationTask.addText("Text"));
  }

  /**
   * Test {@link ConfigurationTask#addText(String)}.
   *
   * <ul>
   *   <li>Given {@link Project} (default constructor) addBuildListener {@link DefaultLogger}
   *       (default constructor).
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addText(String)}
   */
  @Test
  @DisplayName(
      "Test addText(String); given Project (default constructor) addBuildListener DefaultLogger (default constructor); then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addText(String)"})
  void testAddText_givenProjectAddBuildListenerDefaultLogger_thenThrowBuildException()
      throws BuildException {
    // Arrange
    Project project = new Project();
    project.addBuildListener(new DefaultLogger());
    configurationTask.setProject(project);

    // Act and Assert
    assertThrows(BuildException.class, () -> configurationTask.addText("Text"));
  }

  /**
   * Test {@link ConfigurationTask#addText(String)}.
   *
   * <ul>
   *   <li>When {@code embedded configuration}.
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addText(String)}
   */
  @Test
  @DisplayName("Test addText(String); when 'embedded configuration'; then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addText(String)"})
  void testAddText_whenEmbeddedConfiguration_thenThrowBuildException() throws BuildException {
    // Arrange
    configurationTask.setProject(new Project());

    // Act and Assert
    assertThrows(BuildException.class, () -> configurationTask.addText("embedded configuration"));
  }

  /**
   * Test {@link ConfigurationTask#addText(String)}.
   *
   * <ul>
   *   <li>When empty string.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationTask#addText(String)}
   */
  @Test
  @DisplayName("Test addText(String); when empty string; then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.addText(String)"})
  void testAddText_whenEmptyString_thenDoesNotThrow() throws BuildException {
    // Arrange
    configurationTask.setProject(new Project());

    // Act and Assert
    assertDoesNotThrow(() -> configurationTask.addText(""));
  }

  /**
   * Test new {@link ConfigurationTask} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link ConfigurationTask}
   */
  @Test
  @DisplayName("Test new ConfigurationTask (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationTask.<init>()"})
  void testNewConfigurationTask() {
    // Arrange and Act
    ConfigurationTask actualConfigurationTask = new ConfigurationTask();

    // Assert
    assertNull(actualConfigurationTask.getDescription());
    assertNull(actualConfigurationTask.getTaskName());
    assertNull(actualConfigurationTask.getTaskType());
    assertNull(actualConfigurationTask.getProject());
    assertNull(actualConfigurationTask.getOwningTarget());
  }
}
