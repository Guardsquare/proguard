package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ConstantTimingDiffblueTest {
  /**
   * Test {@link ConstantTiming#getTiming(long)}.
   *
   * <p>Method under test: {@link ConstantTiming#getTiming(long)}
   */
  @Test
  @DisplayName("Test getTiming(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"double ConstantTiming.getTiming(long)"})
  void testGetTiming() {
    // Arrange, Act and Assert
    assertEquals(10.0d, new ConstantTiming(10.0d).getTiming(10L));
  }
}
