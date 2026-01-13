package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.awt.Font;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ConstantFontDiffblueTest {
  /**
   * Test {@link ConstantFont#getFont(long)}.
   *
   * <p>Method under test: {@link ConstantFont#getFont(long)}
   */
  @Test
  @DisplayName("Test getFont(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Font ConstantFont.getFont(long)"})
  void testGetFont() {
    // Arrange
    Font value = Font.decode("Str");

    // Act and Assert
    assertSame(value, new ConstantFont(value).getFont(10L));
  }
}
