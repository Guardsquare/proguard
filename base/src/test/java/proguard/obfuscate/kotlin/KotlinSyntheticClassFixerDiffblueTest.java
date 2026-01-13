package proguard.obfuscate.kotlin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata.Flavor;

class KotlinSyntheticClassFixerDiffblueTest {
  /**
   * Test {@link KotlinSyntheticClassFixer#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinSyntheticClassFixer#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinSyntheticClassFixer.visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)"
  })
  void testVisitKotlinSyntheticClassMetadata() {
    // Arrange
    KotlinSyntheticClassFixer kotlinSyntheticClassFixer = new KotlinSyntheticClassFixer();
    LibraryClass clazz = new LibraryClass(1, "$*", "$*");
    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.LAMBDA);

    // Act
    kotlinSyntheticClassFixer.visitKotlinSyntheticClassMetadata(
        clazz, kotlinSyntheticClassKindMetadata);

    // Assert that nothing has changed
    assertNull(clazz.getProcessingInfo());
  }

  /**
   * Test {@link KotlinSyntheticClassFixer#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinSyntheticClassFixer#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinSyntheticClassFixer.visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)"
  })
  void testVisitKotlinSyntheticClassMetadata2() {
    // Arrange
    KotlinSyntheticClassFixer kotlinSyntheticClassFixer = new KotlinSyntheticClassFixer();

    LibraryClass clazz = new LibraryClass();
    clazz.setProcessingInfo("Processing Info");
    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.WHEN_MAPPINGS);

    // Act
    kotlinSyntheticClassFixer.visitKotlinSyntheticClassMetadata(
        clazz, kotlinSyntheticClassKindMetadata);

    // Assert
    assertEquals("Processing Info$WhenMappings", clazz.getProcessingInfo());
  }

  /**
   * Test {@link KotlinSyntheticClassFixer#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}.
   *
   * <p>Method under test: {@link KotlinSyntheticClassFixer#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}
   */
  @Test
  @DisplayName("Test visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinSyntheticClassFixer.visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)"
  })
  void testVisitKotlinSyntheticClassMetadata3() {
    // Arrange
    KotlinSyntheticClassFixer kotlinSyntheticClassFixer = new KotlinSyntheticClassFixer();

    LibraryClass clazz = new LibraryClass();
    clazz.setProcessingInfo("$WhenMappings");
    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.WHEN_MAPPINGS);

    // Act
    kotlinSyntheticClassFixer.visitKotlinSyntheticClassMetadata(
        clazz, kotlinSyntheticClassKindMetadata);

    // Assert that nothing has changed
    assertEquals("$WhenMappings", clazz.getProcessingInfo());
  }

  /**
   * Test {@link KotlinSyntheticClassFixer#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}.
   *
   * <ul>
   *   <li>Then {@link LibraryClass#LibraryClass()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinSyntheticClassFixer#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata); then LibraryClass() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinSyntheticClassFixer.visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)"
  })
  void testVisitKotlinSyntheticClassMetadata_thenLibraryClassProcessingInfoIsNull() {
    // Arrange
    KotlinSyntheticClassFixer kotlinSyntheticClassFixer = new KotlinSyntheticClassFixer();
    LibraryClass clazz = new LibraryClass();
    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.REGULAR);

    // Act
    kotlinSyntheticClassFixer.visitKotlinSyntheticClassMetadata(
        clazz, kotlinSyntheticClassKindMetadata);

    // Assert that nothing has changed
    assertNull(clazz.getProcessingInfo());
  }

  /**
   * Test {@link KotlinSyntheticClassFixer#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}.
   *
   * <ul>
   *   <li>Then {@link ProgramClass#ProgramClass()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link KotlinSyntheticClassFixer#visitKotlinSyntheticClassMetadata(Clazz,
   * KotlinSyntheticClassKindMetadata)}
   */
  @Test
  @DisplayName(
      "Test visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata); then ProgramClass() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinSyntheticClassFixer.visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)"
  })
  void testVisitKotlinSyntheticClassMetadata_thenProgramClassProcessingInfoIsNull() {
    // Arrange
    KotlinSyntheticClassFixer kotlinSyntheticClassFixer = new KotlinSyntheticClassFixer();
    ProgramClass clazz = new ProgramClass();
    KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata =
        new KotlinSyntheticClassKindMetadata(
            new int[] {1, -1, 1, -1}, 1, "Xs", "Pn", Flavor.LAMBDA);

    // Act
    kotlinSyntheticClassFixer.visitKotlinSyntheticClassMetadata(
        clazz, kotlinSyntheticClassKindMetadata);

    // Assert that nothing has changed
    assertNull(clazz.getProcessingInfo());
  }
}
