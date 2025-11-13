package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class SineTimingDiffblueTest {
  /**
   * Test {@link SineTiming#SineTiming(long, long)}.
   *
   * <p>Method under test: {@link SineTiming#SineTiming(long, long)}
   */
  @Test
  @DisplayName("Test new SineTiming(long, long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SineTiming.<init>(long, long)"})
  void testNewSineTiming() {
    // Arrange, Act and Assert
    assertEquals(0.4999999999999951d, new SineTiming(1L, 1L).getTiming(10L));
  }

  /**
   * Test {@link SineTiming#getTiming(long)}.
   *
   * <p>Method under test: {@link SineTiming#getTiming(long)}
   */
  @Test
  @DisplayName("Test getTiming(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"double SineTiming.getTiming(long)"})
  void testGetTiming() {
    // Arrange, Act and Assert
    assertEquals(0.4999999999999951d, new SineTiming(1L, 1L).getTiming(10L));
  }
}
