package proguard.preverify;

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
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.ClassVisitor;

class SubroutineInlinerDiffblueTest {
  /**
   * Test {@link SubroutineInliner#SubroutineInliner(Configuration)}.
   *
   * <p>Method under test: {@link SubroutineInliner#SubroutineInliner(Configuration)}
   */
  @Test
  @DisplayName("Test new SubroutineInliner(Configuration)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SubroutineInliner.<init>(Configuration)"})
  void testNewSubroutineInliner() throws MalformedURLException {
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
        "proguard.preverify.SubroutineInliner", new SubroutineInliner(configuration).getName());
  }

  /**
   * Test {@link SubroutineInliner#execute(AppView)}.
   *
   * <ul>
   *   <li>When {@link ClassPool} {@link ClassPool#classesAccept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link ClassPool#classesAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link SubroutineInliner#execute(AppView)}
   */
  @Test
  @DisplayName(
      "Test execute(AppView); when ClassPool classesAccept(ClassVisitor) does nothing; then calls classesAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SubroutineInliner.execute(AppView)"})
  void testExecute_whenClassPoolClassesAcceptDoesNothing_thenCallsClassesAccept() {
    // Arrange
    SubroutineInliner subroutineInliner = new SubroutineInliner(mock(Configuration.class));

    ClassPool programClassPool = mock(ClassPool.class);
    doNothing().when(programClassPool).classesAccept(Mockito.<ClassVisitor>any());

    // Act
    subroutineInliner.execute(new AppView(programClassPool, KotlinConstants.dummyClassPool));

    // Assert
    verify(programClassPool, atLeast(1)).classesAccept(Mockito.<ClassVisitor>any());
  }
}
