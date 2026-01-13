package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class SmoothTimingDiffblueTest {
  /**
   * Test {@link SmoothTiming#SmoothTiming(long, long)}.
   *
   * <ul>
   *   <li>When {@link Long#MAX_VALUE}.
   *   <li>Then return Timing is ten is {@code 2.8564512724981586E-36}.
   * </ul>
   *
   * <p>Method under test: {@link SmoothTiming#SmoothTiming(long, long)}
   */
  @Test
  @DisplayName(
      "Test new SmoothTiming(long, long); when MAX_VALUE; then return Timing is ten is '2.8564512724981586E-36'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SmoothTiming.<init>(long, long)"})
  void testNewSmoothTiming_whenMax_value_thenReturnTimingIsTenIs28564512724981586e36() {
    // Arrange, Act and Assert
    assertEquals(2.8564512724981586E-36d, new SmoothTiming(1L, Long.MAX_VALUE).getTiming(10L));
  }

  /**
   * Test {@link SmoothTiming#SmoothTiming(long, long)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return Timing is ten is one.
   * </ul>
   *
   * <p>Method under test: {@link SmoothTiming#SmoothTiming(long, long)}
   */
  @Test
  @DisplayName("Test new SmoothTiming(long, long); when one; then return Timing is ten is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SmoothTiming.<init>(long, long)"})
  void testNewSmoothTiming_whenOne_thenReturnTimingIsTenIsOne() {
    // Arrange, Act and Assert
    assertEquals(1.0d, new SmoothTiming(1L, 1L).getTiming(10L));
  }

  /**
   * Test {@link SmoothTiming#SmoothTiming(long, long)}.
   *
   * <ul>
   *   <li>When ten.
   *   <li>Then return Timing is ten is zero.
   * </ul>
   *
   * <p>Method under test: {@link SmoothTiming#SmoothTiming(long, long)}
   */
  @Test
  @DisplayName("Test new SmoothTiming(long, long); when ten; then return Timing is ten is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SmoothTiming.<init>(long, long)"})
  void testNewSmoothTiming_whenTen_thenReturnTimingIsTenIsZero() {
    // Arrange, Act and Assert
    assertEquals(0.0d, new SmoothTiming(10L, 1L).getTiming(10L));
  }
}
