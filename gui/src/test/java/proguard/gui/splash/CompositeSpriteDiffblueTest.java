package proguard.gui.splash;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.awt.Graphics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CompositeSpriteDiffblueTest {
  /**
   * Test {@link CompositeSprite#paint(Graphics, long)}.
   *
   * <ul>
   *   <li>Given {@link Sprite} {@link Sprite#paint(Graphics, long)} does nothing.
   *   <li>When {@code null}.
   *   <li>Then calls {@link Sprite#paint(Graphics, long)}.
   * </ul>
   *
   * <p>Method under test: {@link CompositeSprite#paint(Graphics, long)}
   */
  @Test
  @DisplayName(
      "Test paint(Graphics, long); given Sprite paint(Graphics, long) does nothing; when 'null'; then calls paint(Graphics, long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CompositeSprite.paint(Graphics, long)"})
  void testPaint_givenSpritePaintDoesNothing_whenNull_thenCallsPaint() {
    // Arrange
    Sprite sprite = mock(Sprite.class);
    doNothing().when(sprite).paint(Mockito.<Graphics>any(), anyLong());
    Sprite[] sprites = new Sprite[] {sprite};
    CompositeSprite compositeSprite = new CompositeSprite(sprites);

    // Act
    compositeSprite.paint(null, 10L);

    // Assert
    verify(sprite).paint(isNull(), eq(10L));
  }
}
