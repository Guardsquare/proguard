package proguard.shrink;

import static org.junit.jupiter.api.Assertions.assertFalse;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.util.Processable;
import proguard.util.SimpleProcessable;

class SimpleUsageMarkerDiffblueTest {
  /**
   * Test {@link SimpleUsageMarker#isPossiblyUsed(Processable)}.
   *
   * <ul>
   *   <li>When {@link SimpleProcessable#SimpleProcessable()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleUsageMarker#isPossiblyUsed(Processable)}
   */
  @Test
  @DisplayName("Test isPossiblyUsed(Processable); when SimpleProcessable(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean SimpleUsageMarker.isPossiblyUsed(Processable)"})
  void testIsPossiblyUsed_whenSimpleProcessable_thenReturnFalse() {
    // Arrange
    SimpleUsageMarker simpleUsageMarker = new SimpleUsageMarker();

    // Act and Assert
    assertFalse(simpleUsageMarker.isPossiblyUsed(new SimpleProcessable()));
  }

  /**
   * Test {@link SimpleUsageMarker#isUsed(Processable)}.
   *
   * <ul>
   *   <li>When {@link SimpleProcessable#SimpleProcessable()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleUsageMarker#isUsed(Processable)}
   */
  @Test
  @DisplayName("Test isUsed(Processable); when SimpleProcessable(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean SimpleUsageMarker.isUsed(Processable)"})
  void testIsUsed_whenSimpleProcessable_thenReturnFalse() {
    // Arrange
    SimpleUsageMarker simpleUsageMarker = new SimpleUsageMarker();

    // Act and Assert
    assertFalse(simpleUsageMarker.isUsed(new SimpleProcessable()));
  }
}
