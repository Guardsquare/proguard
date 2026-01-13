package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.util.Processable;
import proguard.util.SimpleProcessable;

class AttributeUsageMarkerDiffblueTest {
  /**
   * Test {@link AttributeUsageMarker#isUsed(Processable)}.
   *
   * <ul>
   *   <li>When {@link SimpleProcessable#SimpleProcessable()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link AttributeUsageMarker#isUsed(Processable)}
   */
  @Test
  @DisplayName("Test isUsed(Processable); when SimpleProcessable(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean AttributeUsageMarker.isUsed(Processable)"})
  void testIsUsed_whenSimpleProcessable_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(AttributeUsageMarker.isUsed(new SimpleProcessable()));
  }
}
