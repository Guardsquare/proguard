package proguard.gui.splash;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.awt.Graphics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TextSpriteDiffblueTest {
  /**
   * Test {@link TextSprite#paint(Graphics, long)}.
   *
   * <ul>
   *   <li>Then calls {@link VariableInt#getInt(long)}.
   * </ul>
   *
   * <p>Method under test: {@link TextSprite#paint(Graphics, long)}
   */
  @Test
  @DisplayName("Test paint(Graphics, long); then calls getInt(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TextSprite.paint(Graphics, long)"})
  void testPaint_thenCallsGetInt() {
    // Arrange
    VariableInt spacing = mock(VariableInt.class);
    when(spacing.getInt(anyLong())).thenReturn(1);

    VariableInt x = mock(VariableInt.class);
    when(x.getInt(anyLong())).thenReturn(1);

    VariableInt y = mock(VariableInt.class);
    when(y.getInt(anyLong())).thenReturn(1);

    TextSprite textSprite = new TextSprite(new VariableString[] {}, spacing, x, y);

    // Act
    textSprite.paint(null, 10L);

    // Assert
    verify(spacing).getInt(10L);
    verify(x).getInt(10L);
    verify(y).getInt(10L);
  }
}
