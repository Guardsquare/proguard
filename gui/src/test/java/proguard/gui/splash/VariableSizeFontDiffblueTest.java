package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.awt.Font;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class VariableSizeFontDiffblueTest {
  /**
   * Test {@link VariableSizeFont#getFont(long)}.
   *
   * <p>Method under test: {@link VariableSizeFont#getFont(long)}
   */
  @Test
  @DisplayName("Test getFont(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Font VariableSizeFont.getFont(long)"})
  void testGetFont() {
    // Arrange
    VariableDouble size = mock(VariableDouble.class);
    when(size.getDouble(anyLong())).thenReturn(10.0d);
    VariableSizeFont variableSizeFont = new VariableSizeFont(Font.decode("Str"), size);

    // Act
    Font actualFont = variableSizeFont.getFont(10L);

    // Assert
    verify(size).getDouble(10L);
    assertEquals("Dialog", actualFont.getFamily());
    assertEquals("Dialog.plain", actualFont.getFontName());
    assertEquals("Dialog.plain", actualFont.getPSName());
    assertEquals("Str", actualFont.getName());
    assertEquals(0, actualFont.getMissingGlyphCode());
    assertEquals(0, actualFont.getStyle());
    assertEquals(0.0f, actualFont.getItalicAngle());
    assertEquals(10, actualFont.getSize());
    assertEquals(10.0f, actualFont.getSize2D());
    assertEquals(22, actualFont.getAvailableAttributes().length);
    assertEquals(4548, actualFont.getNumGlyphs());
    assertEquals(8, actualFont.getAttributes().size());
    assertFalse(actualFont.hasLayoutAttributes());
    assertFalse(actualFont.hasUniformLineMetrics());
    assertFalse(actualFont.isBold());
    assertFalse(actualFont.isItalic());
    assertFalse(actualFont.isTransformed());
    assertTrue(actualFont.isPlain());
  }
}
