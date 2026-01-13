package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ConfigurationDiffblueTest {
  /**
   * Test new {@link Configuration} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link Configuration}
   */
  @Test
  @DisplayName("Test new Configuration (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Configuration.<init>()"})
  void testNewConfiguration() {
    // Arrange and Act
    Configuration actualConfiguration = new Configuration();

    // Assert
    assertNull(actualConfiguration.applyMapping);
    assertNull(actualConfiguration.dump);
    assertNull(actualConfiguration.extraJar);
    assertNull(actualConfiguration.printConfiguration);
    assertNull(actualConfiguration.printMapping);
    assertNull(actualConfiguration.printSeeds);
    assertNull(actualConfiguration.printUsage);
    assertNull(actualConfiguration.flattenPackageHierarchy);
    assertNull(actualConfiguration.newSourceFileAttribute);
    assertNull(actualConfiguration.repackageClasses);
    assertNull(actualConfiguration.classObfuscationDictionary);
    assertNull(actualConfiguration.obfuscationDictionary);
    assertNull(actualConfiguration.packageObfuscationDictionary);
    assertNull(actualConfiguration.keepDirectories);
    assertNull(actualConfiguration.keyStores);
    assertNull(actualConfiguration.adaptClassStrings);
    assertNull(actualConfiguration.adaptResourceFileContents);
    assertNull(actualConfiguration.adaptResourceFileNames);
    assertNull(actualConfiguration.dontCompress);
    assertNull(actualConfiguration.keepAttributes);
    assertNull(actualConfiguration.keepPackageNames);
    assertNull(actualConfiguration.keyAliases);
    assertNull(actualConfiguration.keyPasswords);
    assertNull(actualConfiguration.keyStorePasswords);
    assertNull(actualConfiguration.note);
    assertNull(actualConfiguration.optimizations);
    assertNull(actualConfiguration.warn);
    assertNull(actualConfiguration.assumeNoEscapingParameters);
    assertNull(actualConfiguration.assumeNoExternalReturnValues);
    assertNull(actualConfiguration.assumeNoExternalSideEffects);
    assertNull(actualConfiguration.assumeNoSideEffects);
    assertNull(actualConfiguration.assumeValues);
    assertNull(actualConfiguration.whyAreYouKeeping);
    assertNull(actualConfiguration.keep);
    assertNull(actualConfiguration.libraryJars);
    assertNull(actualConfiguration.programJars);
    assertEquals(0, actualConfiguration.targetClassVersion);
    assertEquals(0L, actualConfiguration.lastModified);
    assertEquals(1, actualConfiguration.optimizationPasses);
    assertEquals(1, actualConfiguration.zipAlign);
    assertFalse(actualConfiguration.addConfigurationDebugging);
    assertFalse(actualConfiguration.allowAccessModification);
    assertFalse(actualConfiguration.android);
    assertFalse(actualConfiguration.backport);
    assertFalse(actualConfiguration.dontProcessKotlinMetadata);
    assertFalse(actualConfiguration.ignoreWarnings);
    assertFalse(actualConfiguration.keepKotlinMetadata);
    assertFalse(actualConfiguration.keepParameterNames);
    assertFalse(actualConfiguration.mergeInterfacesAggressively);
    assertFalse(actualConfiguration.microEdition);
    assertFalse(actualConfiguration.overloadAggressively);
    assertFalse(actualConfiguration.skipNonPublicLibraryClasses);
    assertFalse(actualConfiguration.useUniqueClassMemberNames);
    assertFalse(actualConfiguration.verbose);
    assertTrue(actualConfiguration.enableKotlinAsserter);
    assertTrue(actualConfiguration.obfuscate);
    assertTrue(actualConfiguration.optimize);
    assertTrue(actualConfiguration.optimizeConservatively);
    assertTrue(actualConfiguration.preverify);
    assertTrue(actualConfiguration.shrink);
    assertTrue(actualConfiguration.skipNonPublicLibraryClassMembers);
    assertTrue(actualConfiguration.useMixedCaseClassNames);
  }
}
