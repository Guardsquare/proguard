package proguard.io;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ClassMapDataEntryReplacerDiffblueTest {
  /**
   * Test {@link ClassMapDataEntryReplacer#isKept(int)}.
   *
   * <ul>
   *   <li>When {@code 4194304}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassMapDataEntryReplacer#isKept(int)}
   */
  @Test
  @DisplayName("Test isKept(int); when '4194304'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMapDataEntryReplacer.isKept(int)"})
  void testIsKept_when4194304_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(ClassMapDataEntryReplacer.isKept(4194304));
  }

  /**
   * Test {@link ClassMapDataEntryReplacer#isKept(int)}.
   *
   * <ul>
   *   <li>When minus one.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ClassMapDataEntryReplacer#isKept(int)}
   */
  @Test
  @DisplayName("Test isKept(int); when minus one; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMapDataEntryReplacer.isKept(int)"})
  void testIsKept_whenMinusOne_thenReturnTrue() {
    // Arrange, Act and Assert
    assertTrue(ClassMapDataEntryReplacer.isKept(-1));
  }

  /**
   * Test {@link ClassMapDataEntryReplacer#isKept(int)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassMapDataEntryReplacer#isKept(int)}
   */
  @Test
  @DisplayName("Test isKept(int); when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMapDataEntryReplacer.isKept(int)"})
  void testIsKept_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(ClassMapDataEntryReplacer.isKept(1));
  }
}
