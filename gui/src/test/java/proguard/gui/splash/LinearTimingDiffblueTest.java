package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class LinearTimingDiffblueTest {
  /**
   * Test {@link LinearTiming#LinearTiming(long, long)}.
   *
   * <ul>
   *   <li>When {@link Long#MAX_VALUE}.
   *   <li>Then return Timing is ten is {@code 9.75781955236954E-19}.
   * </ul>
   *
   * <p>Method under test: {@link LinearTiming#LinearTiming(long, long)}
   */
  @Test
  @DisplayName(
      "Test new LinearTiming(long, long); when MAX_VALUE; then return Timing is ten is '9.75781955236954E-19'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LinearTiming.<init>(long, long)"})
  void testNewLinearTiming_whenMax_value_thenReturnTimingIsTenIs975781955236954e19() {
    // Arrange, Act and Assert
    assertEquals(9.75781955236954E-19d, new LinearTiming(1L, Long.MAX_VALUE).getTiming(10L));
  }

  /**
   * Test {@link LinearTiming#LinearTiming(long, long)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return Timing is ten is one.
   * </ul>
   *
   * <p>Method under test: {@link LinearTiming#LinearTiming(long, long)}
   */
  @Test
  @DisplayName("Test new LinearTiming(long, long); when one; then return Timing is ten is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LinearTiming.<init>(long, long)"})
  void testNewLinearTiming_whenOne_thenReturnTimingIsTenIsOne() {
    // Arrange, Act and Assert
    assertEquals(1.0d, new LinearTiming(1L, 1L).getTiming(10L));
  }

  /**
   * Test {@link LinearTiming#LinearTiming(long, long)}.
   *
   * <ul>
   *   <li>When ten.
   *   <li>Then return Timing is ten is zero.
   * </ul>
   *
   * <p>Method under test: {@link LinearTiming#LinearTiming(long, long)}
   */
  @Test
  @DisplayName("Test new LinearTiming(long, long); when ten; then return Timing is ten is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LinearTiming.<init>(long, long)"})
  void testNewLinearTiming_whenTen_thenReturnTimingIsTenIsZero() {
    // Arrange, Act and Assert
    assertEquals(0.0d, new LinearTiming(10L, 1L).getTiming(10L));
  }
}
