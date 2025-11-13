package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.awt.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ConstantColorDiffblueTest {
  /**
   * Test {@link ConstantColor#getColor(long)}.
   *
   * <ul>
   *   <li>Given {@link ConstantColor#ConstantColor(Color)} with value is decode {@code 42}.
   *   <li>Then return decode {@code 42}.
   * </ul>
   *
   * <p>Method under test: {@link ConstantColor#getColor(long)}
   */
  @Test
  @DisplayName(
      "Test getColor(long); given ConstantColor(Color) with value is decode '42'; then return decode '42'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Color ConstantColor.getColor(long)"})
  void testGetColor_givenConstantColorWithValueIsDecode42_thenReturnDecode42()
      throws NumberFormatException {
    // Arrange
    Color value = Color.decode("42");

    // Act and Assert
    assertSame(value, new ConstantColor(value).getColor(10L));
  }
}
