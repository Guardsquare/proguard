package proguard.util.kotlin.asserter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
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
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata.Flavor;
import proguard.classfile.visitor.ClassVisitor;

class KotlinMetadataVerifierDiffblueTest {
  /**
   * Test {@link KotlinMetadataVerifier#KotlinMetadataVerifier(Configuration)}.
   *
   * <p>Method under test: {@link KotlinMetadataVerifier#KotlinMetadataVerifier(Configuration)}
   */
  @Test
  @DisplayName("Test new KotlinMetadataVerifier(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinMetadataVerifier.<init>(Configuration)"})
  void testNewKotlinMetadataVerifier() throws MalformedURLException {
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
    assertEquals(
        "proguard.util.kotlin.asserter.KotlinMetadataVerifier",
        new KotlinMetadataVerifier(configuration).getName());
  }

  /**
   * Test {@link KotlinMetadataVerifier#execute(AppView)}.
   *
   * <p>Method under test: {@link KotlinMetadataVerifier#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinMetadataVerifier.execute(AppView)"})
  void testExecute() {
    // Arrange
    KotlinMetadataVerifier kotlinMetadataVerifier =
        new KotlinMetadataVerifier(mock(Configuration.class));

    ClassPool programClassPool = mock(ClassPool.class);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    // Act
    kotlinMetadataVerifier.execute(new AppView(programClassPool, KotlinConstants.dummyClassPool));

    // Assert
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link KotlinMetadataVerifier#execute(AppView)}.
   *
   * <p>Method under test: {@link KotlinMetadataVerifier#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinMetadataVerifier.execute(AppView)"})
  void testExecute2() {
    // Arrange
    KotlinMetadataVerifier kotlinMetadataVerifier =
        new KotlinMetadataVerifier(mock(Configuration.class));

    ClassPool programClassPool = mock(ClassPool.class);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    ClassPool libraryClassPool = mock(ClassPool.class);
    doNothing().when(libraryClassPool).classesAccept(Mockito.<ClassVisitor>any());

    AppView appView = new AppView(programClassPool, libraryClassPool);

    // Act
    kotlinMetadataVerifier.execute(appView);

    // Assert
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
    verify(libraryClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link KotlinMetadataVerifier#execute(AppView)}.
   *
   * <p>Method under test: {@link KotlinMetadataVerifier#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinMetadataVerifier.execute(AppView)"})
  void testExecute3() {
    // Arrange
    KotlinMetadataVerifier kotlinMetadataVerifier =
        new KotlinMetadataVerifier(mock(Configuration.class));

    ClassPool programClassPool = mock(ClassPool.class);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());
    KotlinSyntheticClassKindMetadata kotlinMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.REGULAR);
    LibraryClass libraryClass =
        new LibraryClass(1, "This Class Name", "Super Class Name", kotlinMetadata);
    AppView appView = new AppView(programClassPool, new ClassPool(libraryClass));

    // Act
    kotlinMetadataVerifier.execute(appView);

    // Assert
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
  }

  /**
   * Test {@link KotlinMetadataVerifier#execute(AppView)}.
   *
   * <p>Method under test: {@link KotlinMetadataVerifier#execute(AppView)}
   */
  @Test
  @DisplayName("Test execute(AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KotlinMetadataVerifier.execute(AppView)"})
  void testExecute4() {
    // Arrange
    KotlinMetadataVerifier kotlinMetadataVerifier =
        new KotlinMetadataVerifier(mock(Configuration.class));

    ClassPool programClassPool = mock(ClassPool.class);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());
    KotlinClassKindMetadata kotlinMetadata =
        new KotlinClassKindMetadata(
            new int[] {1, -1, 1, -1},
            1,
            "Warning: Kotlin metadata errors encountered in %s. Not processing the metadata for this class.",
            "Warning: Kotlin metadata errors encountered in %s. Not processing the metadata for this class.");
    LibraryClass libraryClass =
        new LibraryClass(1, "This Class Name", "Super Class Name", kotlinMetadata);
    AppView appView = new AppView(programClassPool, new ClassPool(libraryClass));

    // Act
    kotlinMetadataVerifier.execute(appView);

    // Assert
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
  }
}
