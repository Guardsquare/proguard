package proguard.optimize.evaluation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.value.InstructionOffsetValue;

class InstructionUsageMarkerDiffblueTest {
  /**
   * Test {@link InstructionUsageMarker#InstructionUsageMarker(PartialEvaluator, boolean, boolean)}.
   *
   * <p>Method under test: {@link InstructionUsageMarker#InstructionUsageMarker(PartialEvaluator,
   * boolean, boolean)}
   */
  @Test
  @DisplayName("Test new InstructionUsageMarker(PartialEvaluator, boolean, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InstructionUsageMarker.<init>(PartialEvaluator, boolean, boolean)"})
  void testNewInstructionUsageMarker() {
    // Arrange and Act
    InstructionUsageMarker actualInstructionUsageMarker =
        new InstructionUsageMarker(new PartialEvaluator(), true, true);

    // Assert
    assertNull(actualInstructionUsageMarker.getStackAfter(1));
    assertNull(actualInstructionUsageMarker.getStackBefore(1));
    assertNull(actualInstructionUsageMarker.branchTargets(1));
    assertFalse(actualInstructionUsageMarker.isExtraPushPopInstructionNecessary(1));
  }

  /**
   * Test {@link InstructionUsageMarker#InstructionUsageMarker(boolean)}.
   *
   * <p>Method under test: {@link InstructionUsageMarker#InstructionUsageMarker(boolean)}
   */
  @Test
  @DisplayName("Test new InstructionUsageMarker(boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InstructionUsageMarker.<init>(boolean)"})
  void testNewInstructionUsageMarker2() {
    // Arrange and Act
    InstructionUsageMarker actualInstructionUsageMarker = new InstructionUsageMarker(true);

    // Assert
    assertNull(actualInstructionUsageMarker.getStackAfter(1));
    assertNull(actualInstructionUsageMarker.getStackBefore(1));
    assertNull(actualInstructionUsageMarker.branchTargets(1));
    assertFalse(actualInstructionUsageMarker.isExtraPushPopInstructionNecessary(1));
  }

  /**
   * Test {@link InstructionUsageMarker#InstructionUsageMarker(PartialEvaluator, boolean, boolean,
   * boolean)}.
   *
   * <ul>
   *   <li>When {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#InstructionUsageMarker(PartialEvaluator,
   * boolean, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test new InstructionUsageMarker(PartialEvaluator, boolean, boolean, boolean); when 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void InstructionUsageMarker.<init>(PartialEvaluator, boolean, boolean, boolean)"
  })
  void testNewInstructionUsageMarker_whenFalse() {
    // Arrange and Act
    InstructionUsageMarker actualInstructionUsageMarker =
        new InstructionUsageMarker(new PartialEvaluator(), true, false, true);

    // Assert
    assertNull(actualInstructionUsageMarker.getStackAfter(1));
    assertNull(actualInstructionUsageMarker.getStackBefore(1));
    assertNull(actualInstructionUsageMarker.branchTargets(1));
    assertFalse(actualInstructionUsageMarker.isExtraPushPopInstructionNecessary(1));
  }

  /**
   * Test {@link InstructionUsageMarker#InstructionUsageMarker(PartialEvaluator, boolean, boolean,
   * boolean)}.
   *
   * <ul>
   *   <li>When {@link PartialEvaluator#PartialEvaluator()}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#InstructionUsageMarker(PartialEvaluator,
   * boolean, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test new InstructionUsageMarker(PartialEvaluator, boolean, boolean, boolean); when PartialEvaluator()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void InstructionUsageMarker.<init>(PartialEvaluator, boolean, boolean, boolean)"
  })
  void testNewInstructionUsageMarker_whenPartialEvaluator() {
    // Arrange and Act
    InstructionUsageMarker actualInstructionUsageMarker =
        new InstructionUsageMarker(new PartialEvaluator(), true, true, true);

    // Assert
    assertNull(actualInstructionUsageMarker.getStackAfter(1));
    assertNull(actualInstructionUsageMarker.getStackBefore(1));
    assertNull(actualInstructionUsageMarker.branchTargets(1));
    assertFalse(actualInstructionUsageMarker.isExtraPushPopInstructionNecessary(1));
  }

  /**
   * Test {@link InstructionUsageMarker#isTraced(int)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#isTraced(int)}
   */
  @Test
  @DisplayName("Test isTraced(int); when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InstructionUsageMarker.isTraced(int)"})
  void testIsTraced_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new InstructionUsageMarker(true).isTraced(1));
  }

  /**
   * Test {@link InstructionUsageMarker#isInstructionNecessary(int)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#isInstructionNecessary(int)}
   */
  @Test
  @DisplayName("Test isInstructionNecessary(int); when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InstructionUsageMarker.isInstructionNecessary(int)"})
  void testIsInstructionNecessary_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new InstructionUsageMarker(true).isInstructionNecessary(1));
  }

  /**
   * Test {@link InstructionUsageMarker#isExtraPushPopInstructionNecessary(int)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#isExtraPushPopInstructionNecessary(int)}
   */
  @Test
  @DisplayName("Test isExtraPushPopInstructionNecessary(int); when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InstructionUsageMarker.isExtraPushPopInstructionNecessary(int)"})
  void testIsExtraPushPopInstructionNecessary_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new InstructionUsageMarker(true).isExtraPushPopInstructionNecessary(1));
  }

  /**
   * Test {@link InstructionUsageMarker#getStackBefore(int)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#getStackBefore(int)}
   */
  @Test
  @DisplayName("Test getStackBefore(int); when one; then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"proguard.evaluation.TracedStack InstructionUsageMarker.getStackBefore(int)"})
  void testGetStackBefore_whenOne_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(new InstructionUsageMarker(true).getStackBefore(1));
  }

  /**
   * Test {@link InstructionUsageMarker#getStackAfter(int)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#getStackAfter(int)}
   */
  @Test
  @DisplayName("Test getStackAfter(int); when one; then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"proguard.evaluation.TracedStack InstructionUsageMarker.getStackAfter(int)"})
  void testGetStackAfter_whenOne_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(new InstructionUsageMarker(true).getStackAfter(1));
  }

  /**
   * Test {@link InstructionUsageMarker#isStackEntryUnwantedBefore(int, int)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#isStackEntryUnwantedBefore(int, int)}
   */
  @Test
  @DisplayName("Test isStackEntryUnwantedBefore(int, int); when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InstructionUsageMarker.isStackEntryUnwantedBefore(int, int)"})
  void testIsStackEntryUnwantedBefore_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new InstructionUsageMarker(true).isStackEntryUnwantedBefore(1, 1));
  }

  /**
   * Test {@link InstructionUsageMarker#isStackEntriesNecessaryAfter(int, int, int)}.
   *
   * <ul>
   *   <li>When {@code 536870912}.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#isStackEntriesNecessaryAfter(int, int,
   * int)}
   */
  @Test
  @DisplayName(
      "Test isStackEntriesNecessaryAfter(int, int, int); when '536870912'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InstructionUsageMarker.isStackEntriesNecessaryAfter(int, int, int)"})
  void testIsStackEntriesNecessaryAfter_when536870912_thenReturnTrue() {
    // Arrange, Act and Assert
    assertTrue(new InstructionUsageMarker(true).isStackEntriesNecessaryAfter(536870912, 1, 1));
  }

  /**
   * Test {@link InstructionUsageMarker#isStackEntriesNecessaryAfter(int, int, int)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#isStackEntriesNecessaryAfter(int, int,
   * int)}
   */
  @Test
  @DisplayName("Test isStackEntriesNecessaryAfter(int, int, int); when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InstructionUsageMarker.isStackEntriesNecessaryAfter(int, int, int)"})
  void testIsStackEntriesNecessaryAfter_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new InstructionUsageMarker(true).isStackEntriesNecessaryAfter(1, 1, 1));
  }

  /**
   * Test {@link InstructionUsageMarker#isAnyStackEntryNecessaryAfter(InstructionOffsetValue, int)}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link
   * InstructionUsageMarker#isAnyStackEntryNecessaryAfter(InstructionOffsetValue, int)}
   */
  @Test
  @DisplayName(
      "Test isAnyStackEntryNecessaryAfter(InstructionOffsetValue, int); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean InstructionUsageMarker.isAnyStackEntryNecessaryAfter(InstructionOffsetValue, int)"
  })
  void testIsAnyStackEntryNecessaryAfter_thenReturnFalse() {
    // Arrange
    InstructionUsageMarker instructionUsageMarker = new InstructionUsageMarker(true);

    // Act and Assert
    assertFalse(
        instructionUsageMarker.isAnyStackEntryNecessaryAfter(new InstructionOffsetValue(42), 1));
  }

  /**
   * Test {@link InstructionUsageMarker#isAnyStackEntryNecessaryAfter(InstructionOffsetValue, int)}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link
   * InstructionUsageMarker#isAnyStackEntryNecessaryAfter(InstructionOffsetValue, int)}
   */
  @Test
  @DisplayName(
      "Test isAnyStackEntryNecessaryAfter(InstructionOffsetValue, int); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean InstructionUsageMarker.isAnyStackEntryNecessaryAfter(InstructionOffsetValue, int)"
  })
  void testIsAnyStackEntryNecessaryAfter_thenReturnTrue() {
    // Arrange
    InstructionUsageMarker instructionUsageMarker = new InstructionUsageMarker(true);

    // Act and Assert
    assertTrue(
        instructionUsageMarker.isAnyStackEntryNecessaryAfter(
            new InstructionOffsetValue(InstructionOffsetValue.EXCEPTION_HANDLER), 1));
  }

  /**
   * Test {@link InstructionUsageMarker#isStackEntryNecessaryAfter(int, int)}.
   *
   * <ul>
   *   <li>When {@code 536870912}.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#isStackEntryNecessaryAfter(int, int)}
   */
  @Test
  @DisplayName("Test isStackEntryNecessaryAfter(int, int); when '536870912'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InstructionUsageMarker.isStackEntryNecessaryAfter(int, int)"})
  void testIsStackEntryNecessaryAfter_when536870912_thenReturnTrue() {
    // Arrange, Act and Assert
    assertTrue(new InstructionUsageMarker(true).isStackEntryNecessaryAfter(536870912, 1));
  }

  /**
   * Test {@link InstructionUsageMarker#isStackEntryNecessaryAfter(int, int)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#isStackEntryNecessaryAfter(int, int)}
   */
  @Test
  @DisplayName("Test isStackEntryNecessaryAfter(int, int); when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InstructionUsageMarker.isStackEntryNecessaryAfter(int, int)"})
  void testIsStackEntryNecessaryAfter_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new InstructionUsageMarker(true).isStackEntryNecessaryAfter(1, 1));
  }

  /**
   * Test {@link InstructionUsageMarker#branchTargets(int)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionUsageMarker#branchTargets(int)}
   */
  @Test
  @DisplayName("Test branchTargets(int); when one; then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"InstructionOffsetValue InstructionUsageMarker.branchTargets(int)"})
  void testBranchTargets_whenOne_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(new InstructionUsageMarker(true).branchTargets(1));
  }
}
