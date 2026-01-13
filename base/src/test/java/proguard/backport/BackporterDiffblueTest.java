package proguard.backport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.AppView;
import proguard.ClassPath;
import proguard.Configuration;
import proguard.classfile.ClassPool;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;
import proguard.testutils.cpa.NamedClass;

class BackporterDiffblueTest {
  /**
   * Test {@link Backporter#Backporter(Configuration)}.
   *
   * <p>Method under test: {@link Backporter#Backporter(Configuration)}
   */
  @Test
  @DisplayName("Test new Backporter(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Backporter.<init>(Configuration)"})
  void testNewBackporter() throws MalformedURLException {
    // Arrange
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

    // Act and Assert
    assertEquals("proguard.backport.Backporter", new Backporter(configuration).getName());
  }

  /**
   * Test {@link Backporter#execute(AppView)}.
   *
   * <p>Method under test: {@link Backporter#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Backporter.execute(AppView)"})
  void testExecute() throws IOException {
    // Arrange
    Backporter backporter = new Backporter(mock(Configuration.class));

    ClassPool programClassPool = mock(ClassPool.class);
    LibraryClass libraryClass =
        new LibraryClass(3473408, "Backporting class files...", "Backporting class files...");
    when(programClassPool.getClass(Mockito.<String>any())).thenReturn(libraryClass);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());
    AppView appView = new AppView(programClassPool, new ClassPool());

    // Act
    backporter.execute(appView);

    // Assert
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
    verify(programClassPool, atLeast(1)).getClass(Mockito.<String>any());
  }

  /**
   * Test {@link Backporter#execute(AppView)}.
   *
   * <p>Method under test: {@link Backporter#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Backporter.execute(AppView)"})
  void testExecute2() throws IOException {
    // Arrange
    Backporter backporter = new Backporter(mock(Configuration.class));

    LibraryClass libraryClass = mock(LibraryClass.class);
    doNothing()
        .when(libraryClass)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());

    ClassPool programClassPool = mock(ClassPool.class);
    when(programClassPool.getClass(Mockito.<String>any())).thenReturn(libraryClass);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    ClassPool libraryClassPool = mock(ClassPool.class);
    doNothing().when(libraryClassPool).classesAccept(Mockito.<ClassVisitor>any());

    AppView appView = new AppView(programClassPool, libraryClassPool);

    // Act
    backporter.execute(appView);

    // Assert
    verify(libraryClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
    verify(programClassPool, atLeast(1)).getClass(Mockito.<String>any());
    verify(libraryClass, atLeast(1))
        .hierarchyAccept(eq(true), eq(true), eq(true), eq(false), Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link Backporter#execute(AppView)}.
   *
   * <p>Method under test: {@link Backporter#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Backporter.execute(AppView)"})
  void testExecute3() throws IOException {
    // Arrange
    Backporter backporter = new Backporter(mock(Configuration.class));

    LibraryClass libraryClass = mock(LibraryClass.class);
    doNothing()
        .when(libraryClass)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());

    ClassPool programClassPool = mock(ClassPool.class);
    when(programClassPool.getClass(Mockito.<String>any())).thenReturn(libraryClass);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    ClassPool libraryClassPool = new ClassPool();
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClassPool.addClass("Backporting class files...", clazz);

    AppView appView = new AppView(programClassPool, libraryClassPool);

    // Act
    backporter.execute(appView);

    // Assert
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
    verify(programClassPool, atLeast(1)).getClass(Mockito.<String>any());
    verify(libraryClass, atLeast(1))
        .hierarchyAccept(eq(true), eq(true), eq(true), eq(false), Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link Backporter#execute(AppView)}.
   *
   * <p>Method under test: {@link Backporter#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Backporter.execute(AppView)"})
  void testExecute4() throws IOException {
    // Arrange
    Backporter backporter = new Backporter(mock(Configuration.class));

    LibraryClass libraryClass = mock(LibraryClass.class);
    doNothing()
        .when(libraryClass)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());

    ClassPool programClassPool = mock(ClassPool.class);
    when(programClassPool.getClass(Mockito.<String>any())).thenReturn(libraryClass);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    ClassPool libraryClassPool = new ClassPool();
    LibraryClass clazz = new LibraryClass(1, "java8/**", "Super Class Name");
    libraryClassPool.addClass("Backporting class files...", clazz);

    AppView appView = new AppView(programClassPool, libraryClassPool);

    // Act
    backporter.execute(appView);

    // Assert
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
    verify(programClassPool, atLeast(1)).getClass(Mockito.<String>any());
    verify(libraryClass, atLeast(1))
        .hierarchyAccept(eq(true), eq(true), eq(true), eq(false), Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link Backporter#execute(AppView)}.
   *
   * <ul>
   *   <li>Given {@link LibraryClass} {@link LibraryClass#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link LibraryClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link Backporter#execute(AppView)}
   */
  @Test
  @DisplayName(
      "Test execute(AppView); given LibraryClass accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Backporter.execute(AppView)"})
  void testExecute_givenLibraryClassAcceptDoesNothing_thenCallsAccept() throws IOException {
    // Arrange
    Backporter backporter = new Backporter(mock(Configuration.class));

    LibraryClass libraryClass = mock(LibraryClass.class);
    doNothing()
        .when(libraryClass)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());

    ClassPool programClassPool = mock(ClassPool.class);
    when(programClassPool.getClass(Mockito.<String>any())).thenReturn(libraryClass);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    LibraryClass clazz = mock(LibraryClass.class);
    doNothing().when(clazz).accept(Mockito.<ClassVisitor>any());

    ClassPool libraryClassPool = new ClassPool();
    libraryClassPool.addClass("Backporting class files...", clazz);

    AppView appView = new AppView(programClassPool, libraryClassPool);

    // Act
    backporter.execute(appView);

    // Assert
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
    verify(programClassPool, atLeast(1)).getClass(Mockito.<String>any());
    verify(clazz, atLeast(1)).accept(Mockito.<ClassVisitor>any());
    verify(libraryClass, atLeast(1))
        .hierarchyAccept(eq(true), eq(true), eq(true), eq(false), Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link Backporter#execute(AppView)}.
   *
   * <ul>
   *   <li>Given {@link LibraryClass} {@link LibraryClass#hierarchyAccept(boolean, boolean, boolean,
   *       boolean, ClassVisitor)} does nothing.
   *   <li>Then calls {@link LibraryClass#hierarchyAccept(boolean, boolean, boolean, boolean,
   *       ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link Backporter#execute(AppView)}
   */
  @Test
  @DisplayName(
      "Test execute(AppView); given LibraryClass hierarchyAccept(boolean, boolean, boolean, boolean, ClassVisitor) does nothing; then calls hierarchyAccept(boolean, boolean, boolean, boolean, ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Backporter.execute(AppView)"})
  void testExecute_givenLibraryClassHierarchyAcceptDoesNothing_thenCallsHierarchyAccept()
      throws IOException {
    // Arrange
    Backporter backporter = new Backporter(mock(Configuration.class));

    LibraryClass libraryClass = mock(LibraryClass.class);
    doNothing()
        .when(libraryClass)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());

    ClassPool programClassPool = mock(ClassPool.class);
    when(programClassPool.getClass(Mockito.<String>any())).thenReturn(libraryClass);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());
    AppView appView = new AppView(programClassPool, new ClassPool());

    // Act
    backporter.execute(appView);

    // Assert
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
    verify(programClassPool, atLeast(1)).getClass(Mockito.<String>any());
    verify(libraryClass, atLeast(1))
        .hierarchyAccept(eq(true), eq(true), eq(true), eq(false), Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link Backporter#execute(AppView)}.
   *
   * <ul>
   *   <li>Given {@link NamedClass#NamedClass(String)} with {@code Member Name}.
   * </ul>
   *
   * <p>Method under test: {@link Backporter#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView); given NamedClass(String) with 'Member Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Backporter.execute(AppView)"})
  void testExecute_givenNamedClassWithMemberName() throws IOException {
    // Arrange
    Backporter backporter = new Backporter(mock(Configuration.class));

    LibraryClass libraryClass = mock(LibraryClass.class);
    doNothing()
        .when(libraryClass)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());

    ClassPool programClassPool = mock(ClassPool.class);
    when(programClassPool.getClass(Mockito.<String>any())).thenReturn(libraryClass);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    ClassPool libraryClassPool = new ClassPool();
    libraryClassPool.addClass("Backporting class files...", new NamedClass("Member Name"));

    AppView appView = new AppView(programClassPool, libraryClassPool);

    // Act
    backporter.execute(appView);

    // Assert
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
    verify(programClassPool, atLeast(1)).getClass(Mockito.<String>any());
    verify(libraryClass, atLeast(1))
        .hierarchyAccept(eq(true), eq(true), eq(true), eq(false), Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link Backporter#execute(AppView)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClass#ProgramClass()}.
   *   <li>When {@link ClassPool} {@link ClassPool#getClass(String)} return {@link
   *       ProgramClass#ProgramClass()}.
   * </ul>
   *
   * <p>Method under test: {@link Backporter#execute(AppView)}
   */
  @Test
  @DisplayName(
      "Test execute(AppView); given ProgramClass(); when ClassPool getClass(String) return ProgramClass()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Backporter.execute(AppView)"})
  void testExecute_givenProgramClass_whenClassPoolGetClassReturnProgramClass() throws IOException {
    // Arrange
    Backporter backporter = new Backporter(mock(Configuration.class));

    ClassPool programClassPool = mock(ClassPool.class);
    when(programClassPool.getClass(Mockito.<String>any())).thenReturn(new ProgramClass());
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());
    AppView appView = new AppView(programClassPool, new ClassPool());

    // Act
    backporter.execute(appView);

    // Assert
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
    verify(programClassPool, atLeast(1)).getClass(Mockito.<String>any());
  }
}
