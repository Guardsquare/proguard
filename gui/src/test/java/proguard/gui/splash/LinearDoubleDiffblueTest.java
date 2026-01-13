package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class LinearDoubleDiffblueTest {
  /**
   * Test {@link LinearDouble#LinearDouble(double, double, Timing)}.
   *
   * <ul>
   *   <li>Given ten.
   *   <li>When {@link Timing} {@link Timing#getTiming(long)} return ten.
   *   <li>Then calls {@link Timing#getTiming(long)}.
   * </ul>
   *
   * <p>Method under test: {@link LinearDouble#LinearDouble(double, double, Timing)}
   */
  @Test
  @DisplayName(
      "Test new LinearDouble(double, double, Timing); given ten; when Timing getTiming(long) return ten; then calls getTiming(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LinearDouble.<init>(double, double, Timing)"})
  void testNewLinearDouble_givenTen_whenTimingGetTimingReturnTen_thenCallsGetTiming() {
    // Arrange
    Timing timing = mock(Timing.class);
    when(timing.getTiming(anyLong())).thenReturn(10.0d);

    // Act
    double actualDouble = new LinearDouble(10.0d, 10.0d, timing).getDouble(10L);

    // Assert
    verify(timing).getTiming(10L);
    assertEquals(10.0d, actualDouble);
  }

  /**
   * Test {@link LinearDouble#LinearDouble(double, double, Timing)}.
   *
   * <ul>
   *   <li>When {@link Timing}.
   * </ul>
   *
   * <p>Method under test: {@link LinearDouble#LinearDouble(double, double, Timing)}
   */
  @Test
  @DisplayName("Test new LinearDouble(double, double, Timing); when Timing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LinearDouble.<init>(double, double, Timing)"})
  void testNewLinearDouble_whenTiming() {
    // Arrange, Act and Assert
    assertEquals(10.0d, new LinearDouble(10.0d, 10.0d, mock(Timing.class)).getDouble(10L));
  }
}
