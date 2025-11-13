package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.awt.Color;
import java.awt.color.ICC_ColorSpace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LinearColorDiffblueTest {
  @Mock private Color color;

  @InjectMocks private LinearColor linearColor;

  @Mock private Timing timing;

  /**
   * Test {@link LinearColor#getColor(long)}.
   *
   * <ul>
   *   <li>Given {@link Color} {@link Color#getBlue()} return minus one.
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link LinearColor#getColor(long)}
   */
  @Test
  @DisplayName(
      "Test getColor(long); given Color getBlue() return minus one; then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Color LinearColor.getColor(long)"})
  void testGetColor_givenColorGetBlueReturnMinusOne_thenThrowIllegalArgumentException() {
    // Arrange
    when(color.getBlue()).thenReturn(-1);
    when(color.getGreen()).thenReturn(1);
    when(color.getRed()).thenReturn(1);
    when(timing.getTiming(anyLong())).thenReturn(10.0d);

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> linearColor.getColor(10L));
    verify(color, atLeast(1)).getBlue();
    verify(color, atLeast(1)).getGreen();
    verify(color, atLeast(1)).getRed();
    verify(timing).getTiming(10L);
  }

  /**
   * Test {@link LinearColor#getColor(long)}.
   *
   * <ul>
   *   <li>Given {@link Color} {@link Color#getBlue()} return one.
   *   <li>Then ColorSpace return {@link ICC_ColorSpace}.
   * </ul>
   *
   * <p>Method under test: {@link LinearColor#getColor(long)}
   */
  @Test
  @DisplayName(
      "Test getColor(long); given Color getBlue() return one; then ColorSpace return ICC_ColorSpace")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Color LinearColor.getColor(long)"})
  void testGetColor_givenColorGetBlueReturnOne_thenColorSpaceReturnICC_ColorSpace() {
    // Arrange
    when(color.getBlue()).thenReturn(1);
    when(color.getGreen()).thenReturn(1);
    when(color.getRed()).thenReturn(1);
    when(timing.getTiming(anyLong())).thenReturn(10.0d);

    // Act
    Color actualColor = linearColor.getColor(10L);

    // Assert
    verify(color, atLeast(1)).getBlue();
    verify(color, atLeast(1)).getGreen();
    verify(color, atLeast(1)).getRed();
    verify(timing).getTiming(10L);
    assertTrue(actualColor.getColorSpace() instanceof ICC_ColorSpace);
    assertEquals(-16711423, actualColor.getRGB());
    assertEquals(1, actualColor.getBlue());
    assertEquals(1, actualColor.getGreen());
    assertEquals(1, actualColor.getRed());
    assertEquals(1, actualColor.getTransparency());
    assertEquals(255, actualColor.getAlpha());
    assertEquals(Color.black, actualColor.darker());
  }

  /**
   * Test {@link LinearColor#getColor(long)}.
   *
   * <ul>
   *   <li>Given {@link Timing} {@link Timing#getTiming(long)} return minus one.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link LinearColor#getColor(long)}
   */
  @Test
  @DisplayName(
      "Test getColor(long); given Timing getTiming(long) return minus one; then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Color LinearColor.getColor(long)"})
  void testGetColor_givenTimingGetTimingReturnMinusOne_thenReturnNull() {
    // Arrange
    when(timing.getTiming(anyLong())).thenReturn(-1.0d);

    // Act
    Color actualColor = linearColor.getColor(10L);

    // Assert
    verify(timing).getTiming(10L);
    assertNull(actualColor);
  }

  /**
   * Test {@link LinearColor#getColor(long)}.
   *
   * <ul>
   *   <li>Given {@link Timing} {@link Timing#getTiming(long)} return one.
   * </ul>
   *
   * <p>Method under test: {@link LinearColor#getColor(long)}
   */
  @Test
  @DisplayName("Test getColor(long); given Timing getTiming(long) return one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Color LinearColor.getColor(long)"})
  void testGetColor_givenTimingGetTimingReturnOne() {
    // Arrange
    when(timing.getTiming(anyLong())).thenReturn(1.0d);

    // Act
    linearColor.getColor(10L);

    // Assert
    verify(timing).getTiming(10L);
  }

  /**
   * Test {@link LinearColor#getColor(long)}.
   *
   * <ul>
   *   <li>Given {@link Timing} {@link Timing#getTiming(long)} return zero.
   * </ul>
   *
   * <p>Method under test: {@link LinearColor#getColor(long)}
   */
  @Test
  @DisplayName("Test getColor(long); given Timing getTiming(long) return zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Color LinearColor.getColor(long)"})
  void testGetColor_givenTimingGetTimingReturnZero() {
    // Arrange
    when(timing.getTiming(anyLong())).thenReturn(0.0d);

    // Act
    linearColor.getColor(10L);

    // Assert
    verify(timing).getTiming(10L);
  }
}
