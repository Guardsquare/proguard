package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class SawToothTimingDiffblueTest {
  /**
   * Test {@link SawToothTiming#SawToothTiming(long, long)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return Timing is ten is zero.
   * </ul>
   *
   * <p>Method under test: {@link SawToothTiming#SawToothTiming(long, long)}
   */
  @Test
  @DisplayName("Test new SawToothTiming(long, long); when one; then return Timing is ten is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SawToothTiming.<init>(long, long)"})
  void testNewSawToothTiming_whenOne_thenReturnTimingIsTenIsZero() {
    // Arrange, Act and Assert
    assertEquals(0.0d, new SawToothTiming(1L, 1L).getTiming(10L));
  }
}
