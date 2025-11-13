package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertNull;
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

class TimeSwitchSpriteDiffblueTest {
  /**
   * Test {@link TimeSwitchSprite#TimeSwitchSprite(long, long, Sprite)}.
   *
   * <ul>
   *   <li>When {@link Long#MAX_VALUE}.
   *   <li>Then {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link TimeSwitchSprite#TimeSwitchSprite(long, long, Sprite)}
   */
  @Test
  @DisplayName("Test new TimeSwitchSprite(long, long, Sprite); when MAX_VALUE; then 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TimeSwitchSprite.<init>(long, long, Sprite)"})
  void testNewTimeSwitchSprite_whenMax_value_thenNull() {
    // Arrange and Act
    new TimeSwitchSprite(Long.MAX_VALUE, 1L, mock(Sprite.class)).paint(null, 10L);

    // Assert that nothing has changed
    assertNull(null);
  }

  /**
   * Test {@link TimeSwitchSprite#TimeSwitchSprite(long, Sprite)}.
   *
   * <ul>
   *   <li>When {@link Long#MAX_VALUE}.
   *   <li>Then {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link TimeSwitchSprite#TimeSwitchSprite(long, Sprite)}
   */
  @Test
  @DisplayName("Test new TimeSwitchSprite(long, Sprite); when MAX_VALUE; then 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TimeSwitchSprite.<init>(long, Sprite)"})
  void testNewTimeSwitchSprite_whenMax_value_thenNull2() {
    // Arrange and Act
    TimeSwitchSprite actualTimeSwitchSprite =
        new TimeSwitchSprite(Long.MAX_VALUE, mock(Sprite.class));
    actualTimeSwitchSprite.paint(null, 10L);

    // Assert that nothing has changed
    assertNull(null);
  }

  /**
   * Test {@link TimeSwitchSprite#TimeSwitchSprite(long, Sprite)}.
   *
   * <ul>
   *   <li>When {@link Sprite} {@link Sprite#paint(Graphics, long)} does nothing.
   *   <li>Then calls {@link Sprite#paint(Graphics, long)}.
   * </ul>
   *
   * <p>Method under test: {@link TimeSwitchSprite#TimeSwitchSprite(long, Sprite)}
   */
  @Test
  @DisplayName(
      "Test new TimeSwitchSprite(long, Sprite); when Sprite paint(Graphics, long) does nothing; then calls paint(Graphics, long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TimeSwitchSprite.<init>(long, Sprite)"})
  void testNewTimeSwitchSprite_whenSpritePaintDoesNothing_thenCallsPaint() {
    // Arrange
    Sprite sprite = mock(Sprite.class);
    doNothing().when(sprite).paint(Mockito.<Graphics>any(), anyLong());

    // Act
    TimeSwitchSprite actualTimeSwitchSprite = new TimeSwitchSprite(1L, sprite);
    actualTimeSwitchSprite.paint(null, 10L);

    // Assert that nothing has changed
    verify(sprite).paint(isNull(), eq(9L));
  }

  /**
   * Test {@link TimeSwitchSprite#TimeSwitchSprite(long, long, Sprite)}.
   *
   * <ul>
   *   <li>When {@link Sprite}.
   *   <li>Then {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link TimeSwitchSprite#TimeSwitchSprite(long, long, Sprite)}
   */
  @Test
  @DisplayName("Test new TimeSwitchSprite(long, long, Sprite); when Sprite; then 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TimeSwitchSprite.<init>(long, long, Sprite)"})
  void testNewTimeSwitchSprite_whenSprite_thenNull() {
    // Arrange and Act
    new TimeSwitchSprite(1L, 1L, mock(Sprite.class)).paint(null, 10L);

    // Assert that nothing has changed
    assertNull(null);
  }

  /**
   * Test {@link TimeSwitchSprite#TimeSwitchSprite(long, long, Sprite)}.
   *
   * <ul>
   *   <li>When ten.
   *   <li>Then calls {@link Sprite#paint(Graphics, long)}.
   * </ul>
   *
   * <p>Method under test: {@link TimeSwitchSprite#TimeSwitchSprite(long, long, Sprite)}
   */
  @Test
  @DisplayName(
      "Test new TimeSwitchSprite(long, long, Sprite); when ten; then calls paint(Graphics, long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TimeSwitchSprite.<init>(long, long, Sprite)"})
  void testNewTimeSwitchSprite_whenTen_thenCallsPaint() {
    // Arrange
    Sprite sprite = mock(Sprite.class);
    doNothing().when(sprite).paint(Mockito.<Graphics>any(), anyLong());

    // Act
    new TimeSwitchSprite(1L, 10L, sprite).paint(null, 10L);

    // Assert that nothing has changed
    verify(sprite).paint(isNull(), eq(9L));
  }

  /**
   * Test {@link TimeSwitchSprite#TimeSwitchSprite(long, long, Sprite)}.
   *
   * <ul>
   *   <li>When zero.
   *   <li>Then calls {@link Sprite#paint(Graphics, long)}.
   * </ul>
   *
   * <p>Method under test: {@link TimeSwitchSprite#TimeSwitchSprite(long, long, Sprite)}
   */
  @Test
  @DisplayName(
      "Test new TimeSwitchSprite(long, long, Sprite); when zero; then calls paint(Graphics, long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TimeSwitchSprite.<init>(long, long, Sprite)"})
  void testNewTimeSwitchSprite_whenZero_thenCallsPaint() {
    // Arrange
    Sprite sprite = mock(Sprite.class);
    doNothing().when(sprite).paint(Mockito.<Graphics>any(), anyLong());

    // Act
    new TimeSwitchSprite(1L, 0L, sprite).paint(null, 10L);

    // Assert that nothing has changed
    verify(sprite).paint(isNull(), eq(9L));
  }

  /**
   * Test {@link TimeSwitchSprite#paint(Graphics, long)}.
   *
   * <ul>
   *   <li>Given {@link TimeSwitchSprite#TimeSwitchSprite(long, long, Sprite)} with onTime is one
   *       and offTime is ten and {@link Sprite}.
   * </ul>
   *
   * <p>Method under test: {@link TimeSwitchSprite#paint(Graphics, long)}
   */
  @Test
  @DisplayName(
      "Test paint(Graphics, long); given TimeSwitchSprite(long, long, Sprite) with onTime is one and offTime is ten and Sprite")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TimeSwitchSprite.paint(Graphics, long)"})
  void testPaint_givenTimeSwitchSpriteWithOnTimeIsOneAndOffTimeIsTenAndSprite() {
    // Arrange
    Sprite sprite = mock(Sprite.class);
    doNothing().when(sprite).paint(Mockito.<Graphics>any(), anyLong());

    // Act
    new TimeSwitchSprite(1L, 10L, sprite).paint(null, 10L);

    // Assert
    verify(sprite).paint(isNull(), eq(9L));
  }

  /**
   * Test {@link TimeSwitchSprite#paint(Graphics, long)}.
   *
   * <ul>
   *   <li>Given {@link TimeSwitchSprite#TimeSwitchSprite(long, Sprite)} with onTime is one and
   *       {@link Sprite}.
   *   <li>When {@code null}.
   *   <li>Then calls {@link Sprite#paint(Graphics, long)}.
   * </ul>
   *
   * <p>Method under test: {@link TimeSwitchSprite#paint(Graphics, long)}
   */
  @Test
  @DisplayName(
      "Test paint(Graphics, long); given TimeSwitchSprite(long, Sprite) with onTime is one and Sprite; when 'null'; then calls paint(Graphics, long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TimeSwitchSprite.paint(Graphics, long)"})
  void testPaint_givenTimeSwitchSpriteWithOnTimeIsOneAndSprite_whenNull_thenCallsPaint() {
    // Arrange
    Sprite sprite = mock(Sprite.class);
    doNothing().when(sprite).paint(Mockito.<Graphics>any(), anyLong());
    TimeSwitchSprite timeSwitchSprite = new TimeSwitchSprite(1L, sprite);

    // Act
    timeSwitchSprite.paint(null, 10L);

    // Assert
    verify(sprite).paint(isNull(), eq(9L));
  }
}
