package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyFloat;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderableImage;
import java.awt.image.renderable.RenderableImageOp;
import java.io.UnsupportedEncodingException;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import javax.swing.plaf.ColorUIResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OverrideGraphics2DDiffblueTest {
  @Mock private Graphics2D graphics2D;

  @InjectMocks private OverrideGraphics2D overrideGraphics2D;

  /**
   * Test {@link OverrideGraphics2D#setOverrideColor(Color)}.
   *
   * <ul>
   *   <li>When Color {@code foo} is one.
   *   <li>Then calls {@link Graphics2D#setColor(Color)}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setOverrideColor(Color)}
   */
  @Test
  @DisplayName("Test setOverrideColor(Color); when Color 'foo' is one; then calls setColor(Color)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setOverrideColor(Color)"})
  void testSetOverrideColor_whenColorFooIsOne_thenCallsSetColor() {
    // Arrange
    doNothing().when(graphics2D).setColor(Mockito.<Color>any());

    // Act
    overrideGraphics2D.setOverrideColor(Color.getColor("foo", 1));

    // Assert
    verify(graphics2D).setColor(isA(Color.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setOverrideColor(Color)}.
   *
   * <ul>
   *   <li>When {@code null}.
   *   <li>Then calls {@link Graphics2D#setColor(Color)}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setOverrideColor(Color)}
   */
  @Test
  @DisplayName("Test setOverrideColor(Color); when 'null'; then calls setColor(Color)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setOverrideColor(Color)"})
  void testSetOverrideColor_whenNull_thenCallsSetColor() {
    // Arrange
    doNothing().when(graphics2D).setColor(Mockito.<Color>any());

    // Act
    overrideGraphics2D.setOverrideColor(null);

    // Assert
    verify(graphics2D).setColor(isNull());
  }

  /**
   * Test {@link OverrideGraphics2D#setOverrideFont(Font)}.
   *
   * <ul>
   *   <li>When decode {@code foo}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setOverrideFont(Font)}
   */
  @Test
  @DisplayName("Test setOverrideFont(Font); when decode 'foo'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setOverrideFont(Font)"})
  void testSetOverrideFont_whenDecodeFoo() {
    // Arrange
    doNothing().when(graphics2D).setFont(Mockito.<Font>any());

    // Act
    overrideGraphics2D.setOverrideFont(Font.decode("foo"));

    // Assert
    verify(graphics2D).setFont(isA(Font.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setOverrideFont(Font)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setOverrideFont(Font)}
   */
  @Test
  @DisplayName("Test setOverrideFont(Font); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setOverrideFont(Font)"})
  void testSetOverrideFont_whenNull() {
    // Arrange
    doNothing().when(graphics2D).setFont(Mockito.<Font>any());

    // Act
    overrideGraphics2D.setOverrideFont(null);

    // Assert
    verify(graphics2D).setFont(isNull());
  }

  /**
   * Test {@link OverrideGraphics2D#setOverridePaint(Paint)}.
   *
   * <ul>
   *   <li>When Color {@code foo} is one.
   *   <li>Then calls {@link Graphics2D#setPaint(Paint)}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setOverridePaint(Paint)}
   */
  @Test
  @DisplayName("Test setOverridePaint(Paint); when Color 'foo' is one; then calls setPaint(Paint)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setOverridePaint(Paint)"})
  void testSetOverridePaint_whenColorFooIsOne_thenCallsSetPaint() {
    // Arrange
    doNothing().when(graphics2D).setPaint(Mockito.<Paint>any());

    // Act
    overrideGraphics2D.setOverridePaint(Color.getColor("foo", 1));

    // Assert
    verify(graphics2D).setPaint(isA(Paint.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setOverridePaint(Paint)}.
   *
   * <ul>
   *   <li>When {@code null}.
   *   <li>Then calls {@link Graphics2D#setPaint(Paint)}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setOverridePaint(Paint)}
   */
  @Test
  @DisplayName("Test setOverridePaint(Paint); when 'null'; then calls setPaint(Paint)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setOverridePaint(Paint)"})
  void testSetOverridePaint_whenNull_thenCallsSetPaint() {
    // Arrange
    doNothing().when(graphics2D).setPaint(Mockito.<Paint>any());

    // Act
    overrideGraphics2D.setOverridePaint(null);

    // Assert
    verify(graphics2D).setPaint(isNull());
  }

  /**
   * Test {@link OverrideGraphics2D#setOverrideStroke(Stroke)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setOverrideStroke(Stroke)}
   */
  @Test
  @DisplayName("Test setOverrideStroke(Stroke); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setOverrideStroke(Stroke)"})
  void testSetOverrideStroke_whenNull() {
    // Arrange
    doNothing().when(graphics2D).setStroke(Mockito.<Stroke>any());

    // Act
    overrideGraphics2D.setOverrideStroke(null);

    // Assert
    verify(graphics2D).setStroke(isNull());
  }

  /**
   * Test {@link OverrideGraphics2D#setOverrideStroke(Stroke)}.
   *
   * <ul>
   *   <li>When {@link Stroke}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setOverrideStroke(Stroke)}
   */
  @Test
  @DisplayName("Test setOverrideStroke(Stroke); when Stroke")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setOverrideStroke(Stroke)"})
  void testSetOverrideStroke_whenStroke() {
    // Arrange
    doNothing().when(graphics2D).setStroke(Mockito.<Stroke>any());

    // Act
    overrideGraphics2D.setOverrideStroke(mock(Stroke.class));

    // Assert
    verify(graphics2D).setStroke(isA(Stroke.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setOverrideXORMode(Color)}.
   *
   * <ul>
   *   <li>Then calls {@link Graphics2D#setPaintMode()}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setOverrideXORMode(Color)}
   */
  @Test
  @DisplayName("Test setOverrideXORMode(Color); then calls setPaintMode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setOverrideXORMode(Color)"})
  void testSetOverrideXORMode_thenCallsSetPaintMode() {
    // Arrange
    doNothing().when(graphics2D).setPaintMode();

    // Act
    overrideGraphics2D.setOverrideXORMode(null);

    // Assert
    verify(graphics2D).setPaintMode();
  }

  /**
   * Test {@link OverrideGraphics2D#setOverrideXORMode(Color)}.
   *
   * <ul>
   *   <li>When Color {@code foo} is one.
   *   <li>Then calls {@link Graphics2D#setXORMode(Color)}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setOverrideXORMode(Color)}
   */
  @Test
  @DisplayName(
      "Test setOverrideXORMode(Color); when Color 'foo' is one; then calls setXORMode(Color)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setOverrideXORMode(Color)"})
  void testSetOverrideXORMode_whenColorFooIsOne_thenCallsSetXORMode() {
    // Arrange
    doNothing().when(graphics2D).setXORMode(Mockito.<Color>any());

    // Act
    overrideGraphics2D.setOverrideXORMode(Color.getColor("foo", 1));

    // Assert
    verify(graphics2D).setXORMode(isA(Color.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setOverrideXORMode(Color)}.
   *
   * <ul>
   *   <li>When {@link ColorUIResource#ColorUIResource(int, int, int)} with one and one and zero.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setOverrideXORMode(Color)}
   */
  @Test
  @DisplayName(
      "Test setOverrideXORMode(Color); when ColorUIResource(int, int, int) with one and one and zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setOverrideXORMode(Color)"})
  void testSetOverrideXORMode_whenColorUIResourceWithOneAndOneAndZero() {
    // Arrange
    doNothing().when(graphics2D).setXORMode(Mockito.<Color>any());

    // Act
    overrideGraphics2D.setOverrideXORMode(new ColorUIResource(1, 1, 0));

    // Assert
    verify(graphics2D).setXORMode(isA(Color.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setColor(Color)}.
   *
   * <ul>
   *   <li>Given {@link Graphics2D} {@link Graphics2D#setColor(Color)} does nothing.
   *   <li>When decode {@code 42}.
   *   <li>Then calls {@link Graphics2D#setColor(Color)}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setColor(Color)}
   */
  @Test
  @DisplayName(
      "Test setColor(Color); given Graphics2D setColor(Color) does nothing; when decode '42'; then calls setColor(Color)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setColor(Color)"})
  void testSetColor_givenGraphics2DSetColorDoesNothing_whenDecode42_thenCallsSetColor()
      throws NumberFormatException {
    // Arrange
    doNothing().when(graphics2D).setColor(Mockito.<Color>any());

    // Act
    overrideGraphics2D.setColor(Color.decode("42"));

    // Assert
    verify(graphics2D).setColor(isA(Color.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setFont(Font)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#setFont(Font)}
   */
  @Test
  @DisplayName("Test setFont(Font)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setFont(Font)"})
  void testSetFont() {
    // Arrange
    doNothing().when(graphics2D).setFont(Mockito.<Font>any());

    // Act
    overrideGraphics2D.setFont(Font.decode("foo"));

    // Assert
    verify(graphics2D).setFont(isA(Font.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setPaint(Paint)}.
   *
   * <ul>
   *   <li>Given {@link Graphics2D} {@link Graphics2D#setPaint(Paint)} does nothing.
   *   <li>When decode {@code 42}.
   *   <li>Then calls {@link Graphics2D#setPaint(Paint)}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setPaint(Paint)}
   */
  @Test
  @DisplayName(
      "Test setPaint(Paint); given Graphics2D setPaint(Paint) does nothing; when decode '42'; then calls setPaint(Paint)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setPaint(Paint)"})
  void testSetPaint_givenGraphics2DSetPaintDoesNothing_whenDecode42_thenCallsSetPaint()
      throws NumberFormatException {
    // Arrange
    doNothing().when(graphics2D).setPaint(Mockito.<Paint>any());

    // Act
    overrideGraphics2D.setPaint(Color.decode("42"));

    // Assert
    verify(graphics2D).setPaint(isA(Paint.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setStroke(Stroke)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#setStroke(Stroke)}
   */
  @Test
  @DisplayName("Test setStroke(Stroke)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setStroke(Stroke)"})
  void testSetStroke() {
    // Arrange
    doNothing().when(graphics2D).setStroke(Mockito.<Stroke>any());

    // Act
    overrideGraphics2D.setStroke(mock(Stroke.class));

    // Assert
    verify(graphics2D).setStroke(isA(Stroke.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setXORMode(Color)}.
   *
   * <ul>
   *   <li>Given {@link Graphics2D} {@link Graphics2D#setXORMode(Color)} does nothing.
   *   <li>Then calls {@link Graphics2D#setXORMode(Color)}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setXORMode(Color)}
   */
  @Test
  @DisplayName(
      "Test setXORMode(Color); given Graphics2D setXORMode(Color) does nothing; then calls setXORMode(Color)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setXORMode(Color)"})
  void testSetXORMode_givenGraphics2DSetXORModeDoesNothing_thenCallsSetXORMode()
      throws NumberFormatException {
    // Arrange
    doNothing().when(graphics2D).setXORMode(Mockito.<Color>any());

    // Act
    overrideGraphics2D.setXORMode(Color.decode("42"));

    // Assert
    verify(graphics2D).setXORMode(isA(Color.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setPaintMode()}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#setPaintMode()}
   */
  @Test
  @DisplayName("Test setPaintMode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setPaintMode()"})
  void testSetPaintMode() {
    // Arrange
    doNothing().when(graphics2D).setPaintMode();

    // Act
    overrideGraphics2D.setPaintMode();

    // Assert
    verify(graphics2D).setPaintMode();
  }

  /**
   * Test {@link OverrideGraphics2D#addRenderingHints(Map)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#addRenderingHints(Map)}
   */
  @Test
  @DisplayName("Test addRenderingHints(Map)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.addRenderingHints(Map)"})
  void testAddRenderingHints() {
    // Arrange
    doNothing().when(graphics2D).addRenderingHints(Mockito.<Map<?, ?>>any());

    // Act
    overrideGraphics2D.addRenderingHints((Map) new HashMap<>());

    // Assert
    verify(graphics2D).addRenderingHints(isA(Map.class));
  }

  /**
   * Test {@link OverrideGraphics2D#clearRect(int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#clearRect(int, int, int, int)}
   */
  @Test
  @DisplayName("Test clearRect(int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.clearRect(int, int, int, int)"})
  void testClearRect() {
    // Arrange
    doNothing().when(graphics2D).clearRect(anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.clearRect(2, 3, 1, 1);

    // Assert
    verify(graphics2D).clearRect(2, 3, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#clip(Shape)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#clip(Shape)}
   */
  @Test
  @DisplayName("Test clip(Shape)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.clip(Shape)"})
  void testClip() {
    // Arrange
    doNothing().when(graphics2D).clip(Mockito.<Shape>any());

    // Act
    overrideGraphics2D.clip(new Polygon());

    // Assert
    verify(graphics2D).clip(isA(Shape.class));
  }

  /**
   * Test {@link OverrideGraphics2D#clipRect(int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#clipRect(int, int, int, int)}
   */
  @Test
  @DisplayName("Test clipRect(int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.clipRect(int, int, int, int)"})
  void testClipRect() {
    // Arrange
    doNothing().when(graphics2D).clipRect(anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.clipRect(2, 3, 1, 1);

    // Assert
    verify(graphics2D).clipRect(2, 3, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#copyArea(int, int, int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#copyArea(int, int, int, int, int, int)}
   */
  @Test
  @DisplayName("Test copyArea(int, int, int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.copyArea(int, int, int, int, int, int)"})
  void testCopyArea() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .copyArea(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.copyArea(2, 3, 1, 1, 1, 1);

    // Assert
    verify(graphics2D).copyArea(2, 3, 1, 1, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#dispose()}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#dispose()}
   */
  @Test
  @DisplayName("Test dispose()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.dispose()"})
  void testDispose() {
    // Arrange
    doNothing().when(graphics2D).dispose();

    // Act
    overrideGraphics2D.dispose();

    // Assert
    verify(graphics2D).dispose();
  }

  /**
   * Test {@link OverrideGraphics2D#draw(Shape)}.
   *
   * <ul>
   *   <li>When {@link Arc2D.Double#Double(double, double, double, double, double, double, int)}
   *       with {@code -0.5} and ten and ten and ten and ten and ten and one.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#draw(Shape)}
   */
  @Test
  @DisplayName(
      "Test draw(Shape); when Double(double, double, double, double, double, double, int) with '-0.5' and ten and ten and ten and ten and ten and one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.draw(Shape)"})
  void testDraw_whenDoubleWith05AndTenAndTenAndTenAndTenAndTenAndOne() {
    // Arrange
    doNothing().when(graphics2D).draw(Mockito.<Shape>any());
    Arc2D.Double s = new Arc2D.Double(-0.5d, 10.0d, 10.0d, 10.0d, 10.0d, 10.0d, 1);

    // Act
    overrideGraphics2D.draw(s);

    // Assert
    verify(graphics2D).draw(isA(Shape.class));
  }

  /**
   * Test {@link OverrideGraphics2D#draw(Shape)}.
   *
   * <ul>
   *   <li>When {@link Polygon#Polygon()}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#draw(Shape)}
   */
  @Test
  @DisplayName("Test draw(Shape); when Polygon()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.draw(Shape)"})
  void testDraw_whenPolygon() {
    // Arrange
    doNothing().when(graphics2D).draw(Mockito.<Shape>any());

    // Act
    overrideGraphics2D.draw(new Polygon());

    // Assert
    verify(graphics2D).draw(isA(Shape.class));
  }

  /**
   * Test {@link OverrideGraphics2D#draw3DRect(int, int, int, int, boolean)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#draw3DRect(int, int, int, int, boolean)}
   */
  @Test
  @DisplayName("Test draw3DRect(int, int, int, int, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.draw3DRect(int, int, int, int, boolean)"})
  void testDraw3DRect() {
    // Arrange
    doNothing().when(graphics2D).draw3DRect(anyInt(), anyInt(), anyInt(), anyInt(), anyBoolean());

    // Act
    overrideGraphics2D.draw3DRect(2, 3, 1, 1, true);

    // Assert
    verify(graphics2D).draw3DRect(2, 3, 1, 1, true);
  }

  /**
   * Test {@link OverrideGraphics2D#drawArc(int, int, int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawArc(int, int, int, int, int, int)}
   */
  @Test
  @DisplayName("Test drawArc(int, int, int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawArc(int, int, int, int, int, int)"})
  void testDrawArc() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .drawArc(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.drawArc(2, 3, 1, 1, 1, 1);

    // Assert
    verify(graphics2D).drawArc(2, 3, 1, 1, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#drawBytes(byte[], int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawBytes(byte[], int, int, int, int)}
   */
  @Test
  @DisplayName("Test drawBytes(byte[], int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawBytes(byte[], int, int, int, int)"})
  void testDrawBytes() throws UnsupportedEncodingException {
    // Arrange
    doNothing()
        .when(graphics2D)
        .drawBytes(Mockito.<byte[]>any(), anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.drawBytes("AXAXAXAX".getBytes("UTF-8"), 2, 3, 2, 3);

    // Assert
    verify(graphics2D).drawBytes(isA(byte[].class), eq(2), eq(3), eq(2), eq(3));
  }

  /**
   * Test {@link OverrideGraphics2D#drawChars(char[], int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawChars(char[], int, int, int, int)}
   */
  @Test
  @DisplayName("Test drawChars(char[], int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawChars(char[], int, int, int, int)"})
  void testDrawChars() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .drawChars(Mockito.<char[]>any(), anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.drawChars("AZAZ".toCharArray(), 2, 3, 2, 3);

    // Assert
    verify(graphics2D).drawChars(isA(char[].class), eq(2), eq(3), eq(2), eq(3));
  }

  /**
   * Test {@link OverrideGraphics2D#drawGlyphVector(GlyphVector, float, float)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawGlyphVector(GlyphVector, float, float)}
   */
  @Test
  @DisplayName("Test drawGlyphVector(GlyphVector, float, float)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawGlyphVector(GlyphVector, float, float)"})
  void testDrawGlyphVector() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .drawGlyphVector(Mockito.<GlyphVector>any(), anyFloat(), anyFloat());

    // Act
    overrideGraphics2D.drawGlyphVector(null, 10.0f, 10.0f);

    // Assert
    verify(graphics2D).drawGlyphVector(isNull(), eq(10.0f), eq(10.0f));
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, int, int, int, int, Color,
   * ImageObserver)} with {@code img}, {@code dx1}, {@code dy1}, {@code dx2}, {@code dy2}, {@code
   * sx1}, {@code sy1}, {@code sx2}, {@code sy2}, {@code bgcolor}, {@code observer}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, int, int,
   * int, int, Color, ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, int, int, int, int, int, int, int, int, Color, ImageObserver) with 'img', 'dx1', 'dy1', 'dx2', 'dy2', 'sx1', 'sy1', 'sx2', 'sy2', 'bgcolor', 'observer'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean OverrideGraphics2D.drawImage(Image, int, int, int, int, int, int, int, int, Color, ImageObserver)"
  })
  void testDrawImageWithImgDx1Dy1Dx2Dy2Sx1Sy1Sx2Sy2BgcolorObserver_thenReturnFalse()
      throws NumberFormatException {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            Mockito.<Color>any(),
            Mockito.<ImageObserver>any()))
        .thenReturn(false);
    BufferedImage img = new BufferedImage(1, 1, 1);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(
            img, 1, 1, 1, 1, 1, 1, 1, 1, Color.decode("42"), mock(ImageObserver.class));

    // Assert
    verify(graphics2D)
        .drawImage(
            isA(Image.class),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            isA(Color.class),
            isA(ImageObserver.class));
    assertFalse(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, int, int, int, int, Color,
   * ImageObserver)} with {@code img}, {@code dx1}, {@code dy1}, {@code dx2}, {@code dy2}, {@code
   * sx1}, {@code sy1}, {@code sx2}, {@code sy2}, {@code bgcolor}, {@code observer}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, int, int,
   * int, int, Color, ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, int, int, int, int, int, int, int, int, Color, ImageObserver) with 'img', 'dx1', 'dy1', 'dx2', 'dy2', 'sx1', 'sy1', 'sx2', 'sy2', 'bgcolor', 'observer'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean OverrideGraphics2D.drawImage(Image, int, int, int, int, int, int, int, int, Color, ImageObserver)"
  })
  void testDrawImageWithImgDx1Dy1Dx2Dy2Sx1Sy1Sx2Sy2BgcolorObserver_thenReturnTrue()
      throws NumberFormatException {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            Mockito.<Color>any(),
            Mockito.<ImageObserver>any()))
        .thenReturn(true);
    BufferedImage img = new BufferedImage(1, 1, 1);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(
            img, 1, 1, 1, 1, 1, 1, 1, 1, Color.decode("42"), mock(ImageObserver.class));

    // Assert
    verify(graphics2D)
        .drawImage(
            isA(Image.class),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            isA(Color.class),
            isA(ImageObserver.class));
    assertTrue(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, int, int, int, int,
   * ImageObserver)} with {@code img}, {@code dx1}, {@code dy1}, {@code dx2}, {@code dy2}, {@code
   * sx1}, {@code sy1}, {@code sx2}, {@code sy2}, {@code observer}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, int, int,
   * int, int, ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, int, int, int, int, int, int, int, int, ImageObserver) with 'img', 'dx1', 'dy1', 'dx2', 'dy2', 'sx1', 'sy1', 'sx2', 'sy2', 'observer'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean OverrideGraphics2D.drawImage(Image, int, int, int, int, int, int, int, int, ImageObserver)"
  })
  void testDrawImageWithImgDx1Dy1Dx2Dy2Sx1Sy1Sx2Sy2Observer_thenReturnFalse() {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            Mockito.<ImageObserver>any()))
        .thenReturn(false);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(
            new BufferedImage(1, 1, 1), 1, 1, 1, 1, 1, 1, 1, 1, mock(ImageObserver.class));

    // Assert
    verify(graphics2D)
        .drawImage(
            isA(Image.class),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            isA(ImageObserver.class));
    assertFalse(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, int, int, int, int,
   * ImageObserver)} with {@code img}, {@code dx1}, {@code dy1}, {@code dx2}, {@code dy2}, {@code
   * sx1}, {@code sy1}, {@code sx2}, {@code sy2}, {@code observer}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, int, int,
   * int, int, ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, int, int, int, int, int, int, int, int, ImageObserver) with 'img', 'dx1', 'dy1', 'dx2', 'dy2', 'sx1', 'sy1', 'sx2', 'sy2', 'observer'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean OverrideGraphics2D.drawImage(Image, int, int, int, int, int, int, int, int, ImageObserver)"
  })
  void testDrawImageWithImgDx1Dy1Dx2Dy2Sx1Sy1Sx2Sy2Observer_thenReturnTrue() {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            Mockito.<ImageObserver>any()))
        .thenReturn(true);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(
            new BufferedImage(1, 1, 1), 1, 1, 1, 1, 1, 1, 1, 1, mock(ImageObserver.class));

    // Assert
    verify(graphics2D)
        .drawImage(
            isA(Image.class),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            eq(1),
            isA(ImageObserver.class));
    assertTrue(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(BufferedImage, BufferedImageOp, int, int)} with {@code
   * img}, {@code op}, {@code x}, {@code y}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(BufferedImage, BufferedImageOp, int,
   * int)}
   */
  @Test
  @DisplayName(
      "Test drawImage(BufferedImage, BufferedImageOp, int, int) with 'img', 'op', 'x', 'y'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawImage(BufferedImage, BufferedImageOp, int, int)"})
  void testDrawImageWithImgOpXY() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .drawImage(
            Mockito.<BufferedImage>any(), Mockito.<BufferedImageOp>any(), anyInt(), anyInt());
    BufferedImage img = new BufferedImage(1, 1, 1);

    // Act
    overrideGraphics2D.drawImage(img, new ColorConvertOp(null), 2, 3);

    // Assert
    verify(graphics2D)
        .drawImage(isA(BufferedImage.class), isA(BufferedImageOp.class), eq(2), eq(3));
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, int, int, Color, ImageObserver)} with {@code
   * img}, {@code x}, {@code y}, {@code bgcolor}, {@code observer}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, int, int, Color,
   * ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, int, int, Color, ImageObserver) with 'img', 'x', 'y', 'bgcolor', 'observer'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OverrideGraphics2D.drawImage(Image, int, int, Color, ImageObserver)"})
  void testDrawImageWithImgXYBgcolorObserver_thenReturnFalse() throws NumberFormatException {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(),
            anyInt(),
            anyInt(),
            Mockito.<Color>any(),
            Mockito.<ImageObserver>any()))
        .thenReturn(false);
    BufferedImage img = new BufferedImage(1, 1, 1);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(img, 2, 3, Color.decode("42"), mock(ImageObserver.class));

    // Assert
    verify(graphics2D)
        .drawImage(isA(Image.class), eq(2), eq(3), isA(Color.class), isA(ImageObserver.class));
    assertFalse(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, int, int, Color, ImageObserver)} with {@code
   * img}, {@code x}, {@code y}, {@code bgcolor}, {@code observer}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, int, int, Color,
   * ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, int, int, Color, ImageObserver) with 'img', 'x', 'y', 'bgcolor', 'observer'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OverrideGraphics2D.drawImage(Image, int, int, Color, ImageObserver)"})
  void testDrawImageWithImgXYBgcolorObserver_thenReturnTrue() throws NumberFormatException {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(),
            anyInt(),
            anyInt(),
            Mockito.<Color>any(),
            Mockito.<ImageObserver>any()))
        .thenReturn(true);
    BufferedImage img = new BufferedImage(1, 1, 1);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(img, 2, 3, Color.decode("42"), mock(ImageObserver.class));

    // Assert
    verify(graphics2D)
        .drawImage(isA(Image.class), eq(2), eq(3), isA(Color.class), isA(ImageObserver.class));
    assertTrue(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, int, int, ImageObserver)} with {@code img},
   * {@code x}, {@code y}, {@code observer}.
   *
   * <ul>
   *   <li>Given {@link Graphics2D} {@link Graphics2D#drawImage(Image, int, int, ImageObserver)}
   *       return {@code true}.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, int, int, ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, int, int, ImageObserver) with 'img', 'x', 'y', 'observer'; given Graphics2D drawImage(Image, int, int, ImageObserver) return 'true'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OverrideGraphics2D.drawImage(Image, int, int, ImageObserver)"})
  void testDrawImageWithImgXYObserver_givenGraphics2DDrawImageReturnTrue_thenReturnTrue() {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(), anyInt(), anyInt(), Mockito.<ImageObserver>any()))
        .thenReturn(true);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(new BufferedImage(1, 1, 1), 2, 3, mock(ImageObserver.class));

    // Assert
    verify(graphics2D).drawImage(isA(Image.class), eq(2), eq(3), isA(ImageObserver.class));
    assertTrue(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, int, int, ImageObserver)} with {@code img},
   * {@code x}, {@code y}, {@code observer}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, int, int, ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, int, int, ImageObserver) with 'img', 'x', 'y', 'observer'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OverrideGraphics2D.drawImage(Image, int, int, ImageObserver)"})
  void testDrawImageWithImgXYObserver_thenReturnFalse() {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(), anyInt(), anyInt(), Mockito.<ImageObserver>any()))
        .thenReturn(false);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(new BufferedImage(1, 1, 1), 2, 3, mock(ImageObserver.class));

    // Assert
    verify(graphics2D).drawImage(isA(Image.class), eq(2), eq(3), isA(ImageObserver.class));
    assertFalse(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, Color, ImageObserver)} with
   * {@code img}, {@code x}, {@code y}, {@code width}, {@code height}, {@code bgcolor}, {@code
   * observer}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, Color,
   * ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, int, int, int, int, Color, ImageObserver) with 'img', 'x', 'y', 'width', 'height', 'bgcolor', 'observer'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean OverrideGraphics2D.drawImage(Image, int, int, int, int, Color, ImageObserver)"
  })
  void testDrawImageWithImgXYWidthHeightBgcolorObserver_thenReturnFalse()
      throws NumberFormatException {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            Mockito.<Color>any(),
            Mockito.<ImageObserver>any()))
        .thenReturn(false);
    BufferedImage img = new BufferedImage(1, 1, 1);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(
            img, 2, 3, 1, 1, Color.decode("42"), mock(ImageObserver.class));

    // Assert
    verify(graphics2D)
        .drawImage(
            isA(Image.class),
            eq(2),
            eq(3),
            eq(1),
            eq(1),
            isA(Color.class),
            isA(ImageObserver.class));
    assertFalse(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, Color, ImageObserver)} with
   * {@code img}, {@code x}, {@code y}, {@code width}, {@code height}, {@code bgcolor}, {@code
   * observer}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, Color,
   * ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, int, int, int, int, Color, ImageObserver) with 'img', 'x', 'y', 'width', 'height', 'bgcolor', 'observer'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean OverrideGraphics2D.drawImage(Image, int, int, int, int, Color, ImageObserver)"
  })
  void testDrawImageWithImgXYWidthHeightBgcolorObserver_thenReturnTrue()
      throws NumberFormatException {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            Mockito.<Color>any(),
            Mockito.<ImageObserver>any()))
        .thenReturn(true);
    BufferedImage img = new BufferedImage(1, 1, 1);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(
            img, 2, 3, 1, 1, Color.decode("42"), mock(ImageObserver.class));

    // Assert
    verify(graphics2D)
        .drawImage(
            isA(Image.class),
            eq(2),
            eq(3),
            eq(1),
            eq(1),
            isA(Color.class),
            isA(ImageObserver.class));
    assertTrue(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, ImageObserver)} with {@code
   * img}, {@code x}, {@code y}, {@code width}, {@code height}, {@code observer}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, int, int, int, int,
   * ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, int, int, int, int, ImageObserver) with 'img', 'x', 'y', 'width', 'height', 'observer'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean OverrideGraphics2D.drawImage(Image, int, int, int, int, ImageObserver)"
  })
  void testDrawImageWithImgXYWidthHeightObserver_thenReturnFalse() {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            Mockito.<ImageObserver>any()))
        .thenReturn(false);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(
            new BufferedImage(1, 1, 1), 2, 3, 1, 1, mock(ImageObserver.class));

    // Assert
    verify(graphics2D)
        .drawImage(isA(Image.class), eq(2), eq(3), eq(1), eq(1), isA(ImageObserver.class));
    assertFalse(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, int, int, int, int, ImageObserver)} with {@code
   * img}, {@code x}, {@code y}, {@code width}, {@code height}, {@code observer}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, int, int, int, int,
   * ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, int, int, int, int, ImageObserver) with 'img', 'x', 'y', 'width', 'height', 'observer'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean OverrideGraphics2D.drawImage(Image, int, int, int, int, ImageObserver)"
  })
  void testDrawImageWithImgXYWidthHeightObserver_thenReturnTrue() {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(),
            anyInt(),
            anyInt(),
            anyInt(),
            anyInt(),
            Mockito.<ImageObserver>any()))
        .thenReturn(true);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(
            new BufferedImage(1, 1, 1), 2, 3, 1, 1, mock(ImageObserver.class));

    // Assert
    verify(graphics2D)
        .drawImage(isA(Image.class), eq(2), eq(3), eq(1), eq(1), isA(ImageObserver.class));
    assertTrue(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, AffineTransform, ImageObserver)} with {@code
   * img}, {@code xform}, {@code obs}.
   *
   * <ul>
   *   <li>Given {@link Graphics2D} {@link Graphics2D#drawImage(Image, AffineTransform,
   *       ImageObserver)} return {@code false}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, AffineTransform,
   * ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, AffineTransform, ImageObserver) with 'img', 'xform', 'obs'; given Graphics2D drawImage(Image, AffineTransform, ImageObserver) return 'false'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OverrideGraphics2D.drawImage(Image, AffineTransform, ImageObserver)"})
  void testDrawImageWithImgXformObs_givenGraphics2DDrawImageReturnFalse_thenReturnFalse() {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(), Mockito.<AffineTransform>any(), Mockito.<ImageObserver>any()))
        .thenReturn(false);
    BufferedImage img = new BufferedImage(1, 1, 1);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(img, new AffineTransform(), mock(ImageObserver.class));

    // Assert
    verify(graphics2D)
        .drawImage(isA(Image.class), isA(AffineTransform.class), isA(ImageObserver.class));
    assertFalse(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawImage(Image, AffineTransform, ImageObserver)} with {@code
   * img}, {@code xform}, {@code obs}.
   *
   * <ul>
   *   <li>Given {@link Graphics2D} {@link Graphics2D#drawImage(Image, AffineTransform,
   *       ImageObserver)} return {@code true}.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawImage(Image, AffineTransform,
   * ImageObserver)}
   */
  @Test
  @DisplayName(
      "Test drawImage(Image, AffineTransform, ImageObserver) with 'img', 'xform', 'obs'; given Graphics2D drawImage(Image, AffineTransform, ImageObserver) return 'true'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OverrideGraphics2D.drawImage(Image, AffineTransform, ImageObserver)"})
  void testDrawImageWithImgXformObs_givenGraphics2DDrawImageReturnTrue_thenReturnTrue() {
    // Arrange
    when(graphics2D.drawImage(
            Mockito.<Image>any(), Mockito.<AffineTransform>any(), Mockito.<ImageObserver>any()))
        .thenReturn(true);
    BufferedImage img = new BufferedImage(1, 1, 1);

    // Act
    boolean actualDrawImageResult =
        overrideGraphics2D.drawImage(img, new AffineTransform(), mock(ImageObserver.class));

    // Assert
    verify(graphics2D)
        .drawImage(isA(Image.class), isA(AffineTransform.class), isA(ImageObserver.class));
    assertTrue(actualDrawImageResult);
  }

  /**
   * Test {@link OverrideGraphics2D#drawLine(int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawLine(int, int, int, int)}
   */
  @Test
  @DisplayName("Test drawLine(int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawLine(int, int, int, int)"})
  void testDrawLine() {
    // Arrange
    doNothing().when(graphics2D).drawLine(anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.drawLine(1, 3, 1, 1);

    // Assert
    verify(graphics2D).drawLine(1, 3, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#drawOval(int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawOval(int, int, int, int)}
   */
  @Test
  @DisplayName("Test drawOval(int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawOval(int, int, int, int)"})
  void testDrawOval() {
    // Arrange
    doNothing().when(graphics2D).drawOval(anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.drawOval(2, 3, 1, 1);

    // Assert
    verify(graphics2D).drawOval(2, 3, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#drawPolygon(Polygon)} with {@code p}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawPolygon(Polygon)}
   */
  @Test
  @DisplayName("Test drawPolygon(Polygon) with 'p'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawPolygon(Polygon)"})
  void testDrawPolygonWithP() {
    // Arrange
    doNothing().when(graphics2D).drawPolygon(Mockito.<Polygon>any());

    // Act
    overrideGraphics2D.drawPolygon(new Polygon());

    // Assert
    verify(graphics2D).drawPolygon(isA(Polygon.class));
  }

  /**
   * Test {@link OverrideGraphics2D#drawPolygon(int[], int[], int)} with {@code xPoints}, {@code
   * yPoints}, {@code nPoints}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawPolygon(int[], int[], int)}
   */
  @Test
  @DisplayName("Test drawPolygon(int[], int[], int) with 'xPoints', 'yPoints', 'nPoints'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawPolygon(int[], int[], int)"})
  void testDrawPolygonWithXPointsYPointsNPoints() {
    // Arrange
    doNothing().when(graphics2D).drawPolygon(Mockito.<int[]>any(), Mockito.<int[]>any(), anyInt());

    // Act
    overrideGraphics2D.drawPolygon(new int[] {1, -1, 1, -1}, new int[] {1, -1, 1, -1}, 1);

    // Assert
    verify(graphics2D).drawPolygon(isA(int[].class), isA(int[].class), eq(1));
  }

  /**
   * Test {@link OverrideGraphics2D#drawPolyline(int[], int[], int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawPolyline(int[], int[], int)}
   */
  @Test
  @DisplayName("Test drawPolyline(int[], int[], int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawPolyline(int[], int[], int)"})
  void testDrawPolyline() {
    // Arrange
    doNothing().when(graphics2D).drawPolyline(Mockito.<int[]>any(), Mockito.<int[]>any(), anyInt());

    // Act
    overrideGraphics2D.drawPolyline(new int[] {1, -1, 1, -1}, new int[] {1, -1, 1, -1}, 1);

    // Assert
    verify(graphics2D).drawPolyline(isA(int[].class), isA(int[].class), eq(1));
  }

  /**
   * Test {@link OverrideGraphics2D#drawRect(int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawRect(int, int, int, int)}
   */
  @Test
  @DisplayName("Test drawRect(int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawRect(int, int, int, int)"})
  void testDrawRect() {
    // Arrange
    doNothing().when(graphics2D).drawRect(anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.drawRect(2, 3, 1, 1);

    // Assert
    verify(graphics2D).drawRect(2, 3, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#drawRenderableImage(RenderableImage, AffineTransform)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ParameterBlock#ParameterBlock()} Source {@code 42} is one.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawRenderableImage(RenderableImage,
   * AffineTransform)}
   */
  @Test
  @DisplayName(
      "Test drawRenderableImage(RenderableImage, AffineTransform); given '42'; when ParameterBlock() Source '42' is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OverrideGraphics2D.drawRenderableImage(RenderableImage, AffineTransform)"
  })
  void testDrawRenderableImage_given42_whenParameterBlockSource42IsOne() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .drawRenderableImage(Mockito.<RenderableImage>any(), Mockito.<AffineTransform>any());

    ParameterBlock parameterBlock = new ParameterBlock();
    parameterBlock.setSource("42", 1);
    RenderableImageOp img = new RenderableImageOp(null, parameterBlock);

    // Act
    overrideGraphics2D.drawRenderableImage(img, AffineTransform.getQuadrantRotateInstance(1));

    // Assert
    verify(graphics2D).drawRenderableImage(isA(RenderableImage.class), isA(AffineTransform.class));
  }

  /**
   * Test {@link OverrideGraphics2D#drawRenderableImage(RenderableImage, AffineTransform)}.
   *
   * <ul>
   *   <li>When {@link AffineTransform#AffineTransform()}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawRenderableImage(RenderableImage,
   * AffineTransform)}
   */
  @Test
  @DisplayName("Test drawRenderableImage(RenderableImage, AffineTransform); when AffineTransform()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OverrideGraphics2D.drawRenderableImage(RenderableImage, AffineTransform)"
  })
  void testDrawRenderableImage_whenAffineTransform() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .drawRenderableImage(Mockito.<RenderableImage>any(), Mockito.<AffineTransform>any());
    RenderableImageOp img = new RenderableImageOp(null, new ParameterBlock());

    // Act
    overrideGraphics2D.drawRenderableImage(img, new AffineTransform());

    // Assert
    verify(graphics2D).drawRenderableImage(isA(RenderableImage.class), isA(AffineTransform.class));
  }

  /**
   * Test {@link OverrideGraphics2D#drawRenderedImage(RenderedImage, AffineTransform)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawRenderedImage(RenderedImage,
   * AffineTransform)}
   */
  @Test
  @DisplayName("Test drawRenderedImage(RenderedImage, AffineTransform)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawRenderedImage(RenderedImage, AffineTransform)"})
  void testDrawRenderedImage() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .drawRenderedImage(Mockito.<RenderedImage>any(), Mockito.<AffineTransform>any());
    BufferedImage img = new BufferedImage(1, 1, 1);

    // Act
    overrideGraphics2D.drawRenderedImage(img, new AffineTransform());

    // Assert
    verify(graphics2D).drawRenderedImage(isA(RenderedImage.class), isA(AffineTransform.class));
  }

  /**
   * Test {@link OverrideGraphics2D#drawRoundRect(int, int, int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawRoundRect(int, int, int, int, int, int)}
   */
  @Test
  @DisplayName("Test drawRoundRect(int, int, int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawRoundRect(int, int, int, int, int, int)"})
  void testDrawRoundRect() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .drawRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.drawRoundRect(2, 3, 1, 1, 1, 1);

    // Assert
    verify(graphics2D).drawRoundRect(2, 3, 1, 1, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#drawString(AttributedCharacterIterator, float, float)} with
   * {@code AttributedCharacterIterator}, {@code float}, {@code float}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawString(AttributedCharacterIterator, float,
   * float)}
   */
  @Test
  @DisplayName(
      "Test drawString(AttributedCharacterIterator, float, float) with 'AttributedCharacterIterator', 'float', 'float'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OverrideGraphics2D.drawString(AttributedCharacterIterator, float, float)"
  })
  void testDrawStringWithAttributedCharacterIteratorFloatFloat() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .drawString(Mockito.<AttributedCharacterIterator>any(), anyFloat(), anyFloat());

    // Act
    overrideGraphics2D.drawString((AttributedCharacterIterator) null, 10.0f, 10.0f);

    // Assert
    verify(graphics2D).drawString((AttributedCharacterIterator) isNull(), eq(10.0f), eq(10.0f));
  }

  /**
   * Test {@link OverrideGraphics2D#drawString(AttributedCharacterIterator, int, int)} with {@code
   * AttributedCharacterIterator}, {@code int}, {@code int}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawString(AttributedCharacterIterator, int,
   * int)}
   */
  @Test
  @DisplayName(
      "Test drawString(AttributedCharacterIterator, int, int) with 'AttributedCharacterIterator', 'int', 'int'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawString(AttributedCharacterIterator, int, int)"})
  void testDrawStringWithAttributedCharacterIteratorIntInt() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .drawString(Mockito.<AttributedCharacterIterator>any(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.drawString((AttributedCharacterIterator) null, 2, 3);

    // Assert
    verify(graphics2D).drawString((AttributedCharacterIterator) isNull(), eq(2), eq(3));
  }

  /**
   * Test {@link OverrideGraphics2D#drawString(String, float, float)} with {@code String}, {@code
   * float}, {@code float}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawString(String, float, float)}
   */
  @Test
  @DisplayName("Test drawString(String, float, float) with 'String', 'float', 'float'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawString(String, float, float)"})
  void testDrawStringWithStringFloatFloat() {
    // Arrange
    doNothing().when(graphics2D).drawString(Mockito.<String>any(), anyFloat(), anyFloat());

    // Act
    overrideGraphics2D.drawString("foo", 10.0f, 10.0f);

    // Assert
    verify(graphics2D).drawString("foo", 10.0f, 10.0f);
  }

  /**
   * Test {@link OverrideGraphics2D#drawString(String, int, int)} with {@code String}, {@code int},
   * {@code int}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#drawString(String, int, int)}
   */
  @Test
  @DisplayName("Test drawString(String, int, int) with 'String', 'int', 'int'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.drawString(String, int, int)"})
  void testDrawStringWithStringIntInt() {
    // Arrange
    doNothing().when(graphics2D).drawString(Mockito.<String>any(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.drawString("Str", 2, 3);

    // Assert
    verify(graphics2D).drawString("Str", 2, 3);
  }

  /**
   * Test {@link OverrideGraphics2D#fill(Shape)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#fill(Shape)}
   */
  @Test
  @DisplayName("Test fill(Shape)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.fill(Shape)"})
  void testFill() {
    // Arrange
    doNothing().when(graphics2D).fill(Mockito.<Shape>any());

    // Act
    overrideGraphics2D.fill(new Polygon());

    // Assert
    verify(graphics2D).fill(isA(Shape.class));
  }

  /**
   * Test {@link OverrideGraphics2D#fill3DRect(int, int, int, int, boolean)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#fill3DRect(int, int, int, int, boolean)}
   */
  @Test
  @DisplayName("Test fill3DRect(int, int, int, int, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.fill3DRect(int, int, int, int, boolean)"})
  void testFill3DRect() {
    // Arrange
    doNothing().when(graphics2D).fill3DRect(anyInt(), anyInt(), anyInt(), anyInt(), anyBoolean());

    // Act
    overrideGraphics2D.fill3DRect(2, 3, 1, 1, true);

    // Assert
    verify(graphics2D).fill3DRect(2, 3, 1, 1, true);
  }

  /**
   * Test {@link OverrideGraphics2D#fillArc(int, int, int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#fillArc(int, int, int, int, int, int)}
   */
  @Test
  @DisplayName("Test fillArc(int, int, int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.fillArc(int, int, int, int, int, int)"})
  void testFillArc() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .fillArc(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.fillArc(2, 3, 1, 1, 1, 1);

    // Assert
    verify(graphics2D).fillArc(2, 3, 1, 1, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#fillOval(int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#fillOval(int, int, int, int)}
   */
  @Test
  @DisplayName("Test fillOval(int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.fillOval(int, int, int, int)"})
  void testFillOval() {
    // Arrange
    doNothing().when(graphics2D).fillOval(anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.fillOval(2, 3, 1, 1);

    // Assert
    verify(graphics2D).fillOval(2, 3, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#fillPolygon(Polygon)} with {@code p}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#fillPolygon(Polygon)}
   */
  @Test
  @DisplayName("Test fillPolygon(Polygon) with 'p'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.fillPolygon(Polygon)"})
  void testFillPolygonWithP() {
    // Arrange
    doNothing().when(graphics2D).fillPolygon(Mockito.<Polygon>any());

    // Act
    overrideGraphics2D.fillPolygon(new Polygon());

    // Assert
    verify(graphics2D).fillPolygon(isA(Polygon.class));
  }

  /**
   * Test {@link OverrideGraphics2D#fillPolygon(int[], int[], int)} with {@code xPoints}, {@code
   * yPoints}, {@code nPoints}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#fillPolygon(int[], int[], int)}
   */
  @Test
  @DisplayName("Test fillPolygon(int[], int[], int) with 'xPoints', 'yPoints', 'nPoints'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.fillPolygon(int[], int[], int)"})
  void testFillPolygonWithXPointsYPointsNPoints() {
    // Arrange
    doNothing().when(graphics2D).fillPolygon(Mockito.<int[]>any(), Mockito.<int[]>any(), anyInt());

    // Act
    overrideGraphics2D.fillPolygon(new int[] {1, -1, 1, -1}, new int[] {1, -1, 1, -1}, 1);

    // Assert
    verify(graphics2D).fillPolygon(isA(int[].class), isA(int[].class), eq(1));
  }

  /**
   * Test {@link OverrideGraphics2D#fillRect(int, int, int, int)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#fillRect(int, int, int, int)}
   */
  @Test
  @DisplayName("Test fillRect(int, int, int, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.fillRect(int, int, int, int)"})
  void testFillRect() {
    // Arrange
    doNothing().when(graphics2D).fillRect(anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.fillRect(2, 3, 1, 1);

    // Assert
    verify(graphics2D).fillRect(2, 3, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#fillRoundRect(int, int, int, int, int, int)}.
   *
   * <ul>
   *   <li>When minus one.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#fillRoundRect(int, int, int, int, int, int)}
   */
  @Test
  @DisplayName("Test fillRoundRect(int, int, int, int, int, int); when minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.fillRoundRect(int, int, int, int, int, int)"})
  void testFillRoundRect_whenMinusOne() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .fillRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.fillRoundRect(-1, 3, 2, 1, 1, 1);

    // Assert
    verify(graphics2D).fillRoundRect(-1, 3, 2, 1, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#fillRoundRect(int, int, int, int, int, int)}.
   *
   * <ul>
   *   <li>When minus one.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#fillRoundRect(int, int, int, int, int, int)}
   */
  @Test
  @DisplayName("Test fillRoundRect(int, int, int, int, int, int); when minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.fillRoundRect(int, int, int, int, int, int)"})
  void testFillRoundRect_whenMinusOne2() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .fillRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.fillRoundRect(-1, 3, 3, 1, 1, 1);

    // Assert
    verify(graphics2D).fillRoundRect(-1, 3, 3, 1, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#fillRoundRect(int, int, int, int, int, int)}.
   *
   * <ul>
   *   <li>When two.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#fillRoundRect(int, int, int, int, int, int)}
   */
  @Test
  @DisplayName("Test fillRoundRect(int, int, int, int, int, int); when two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.fillRoundRect(int, int, int, int, int, int)"})
  void testFillRoundRect_whenTwo() {
    // Arrange
    doNothing()
        .when(graphics2D)
        .fillRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.fillRoundRect(2, 3, 1, 1, 1, 1);

    // Assert
    verify(graphics2D).fillRoundRect(2, 3, 1, 1, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#getBackground()}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#getBackground()}
   */
  @Test
  @DisplayName("Test getBackground()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Color OverrideGraphics2D.getBackground()"})
  void testGetBackground() throws NumberFormatException {
    // Arrange
    Color decodeResult = Color.decode("42");
    when(graphics2D.getBackground()).thenReturn(decodeResult);

    // Act
    Color actualBackground = overrideGraphics2D.getBackground();

    // Assert
    verify(graphics2D).getBackground();
    assertSame(decodeResult, actualBackground);
  }

  /**
   * Test {@link OverrideGraphics2D#getClip()}.
   *
   * <ul>
   *   <li>Given {@link Graphics2D} {@link Graphics2D#getClip()} return {@link Polygon#Polygon()}.
   *   <li>Then return {@link Polygon#Polygon()}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#getClip()}
   */
  @Test
  @DisplayName("Test getClip(); given Graphics2D getClip() return Polygon(); then return Polygon()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Shape OverrideGraphics2D.getClip()"})
  void testGetClip_givenGraphics2DGetClipReturnPolygon_thenReturnPolygon() {
    // Arrange
    Polygon polygon = new Polygon();
    when(graphics2D.getClip()).thenReturn(polygon);

    // Act
    Shape actualClip = overrideGraphics2D.getClip();

    // Assert
    verify(graphics2D).getClip();
    assertSame(polygon, actualClip);
  }

  /**
   * Test {@link OverrideGraphics2D#getClip()}.
   *
   * <ul>
   *   <li>Then return {@link Arc2D.Double#Double(double, double, double, double, double, double,
   *       int)} with {@code -0.5} and ten and ten and ten and ten and {@link Double#NaN} and one.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#getClip()}
   */
  @Test
  @DisplayName(
      "Test getClip(); then return Double(double, double, double, double, double, double, int) with '-0.5' and ten and ten and ten and ten and NaN and one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Shape OverrideGraphics2D.getClip()"})
  void testGetClip_thenReturnDoubleWith05AndTenAndTenAndTenAndTenAndNaNAndOne() {
    // Arrange
    Arc2D.Double resultDouble = new Arc2D.Double(-0.5d, 10.0d, 10.0d, 10.0d, 10.0d, Double.NaN, 1);
    when(graphics2D.getClip()).thenReturn(resultDouble);

    // Act
    Shape actualClip = overrideGraphics2D.getClip();

    // Assert
    verify(graphics2D).getClip();
    assertSame(resultDouble, actualClip);
  }

  /**
   * Test {@link OverrideGraphics2D#getClip()}.
   *
   * <ul>
   *   <li>Then return {@link Arc2D.Double#Double(double, double, double, double, double, double,
   *       int)} with ten and ten and ten and ten and ten and {@link Double#NaN} and one.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#getClip()}
   */
  @Test
  @DisplayName(
      "Test getClip(); then return Double(double, double, double, double, double, double, int) with ten and ten and ten and ten and ten and NaN and one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Shape OverrideGraphics2D.getClip()"})
  void testGetClip_thenReturnDoubleWithTenAndTenAndTenAndTenAndTenAndNaNAndOne() {
    // Arrange
    Arc2D.Double resultDouble = new Arc2D.Double(10.0d, 10.0d, 10.0d, 10.0d, 10.0d, Double.NaN, 1);
    when(graphics2D.getClip()).thenReturn(resultDouble);

    // Act
    Shape actualClip = overrideGraphics2D.getClip();

    // Assert
    verify(graphics2D).getClip();
    assertSame(resultDouble, actualClip);
  }

  /**
   * Test {@link OverrideGraphics2D#getClipBounds()}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#getClipBounds()}
   */
  @Test
  @DisplayName("Test getClipBounds()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Rectangle OverrideGraphics2D.getClipBounds()"})
  void testGetClipBounds() {
    // Arrange
    Rectangle rectangle = new Rectangle();
    when(graphics2D.getClipBounds()).thenReturn(rectangle);

    // Act
    Rectangle actualClipBounds = overrideGraphics2D.getClipBounds();

    // Assert
    verify(graphics2D).getClipBounds();
    assertSame(rectangle, actualClipBounds);
  }

  /**
   * Test {@link OverrideGraphics2D#getClipBounds(Rectangle)} with {@code Rectangle}.
   *
   * <ul>
   *   <li>Then return {@link Rectangle#Rectangle()}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#getClipBounds(Rectangle)}
   */
  @Test
  @DisplayName("Test getClipBounds(Rectangle) with 'Rectangle'; then return Rectangle()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Rectangle OverrideGraphics2D.getClipBounds(Rectangle)"})
  void testGetClipBoundsWithRectangle_thenReturnRectangle() {
    // Arrange
    Rectangle rectangle = new Rectangle();
    when(graphics2D.getClipBounds(Mockito.<Rectangle>any())).thenReturn(rectangle);

    // Act
    Rectangle actualClipBounds = overrideGraphics2D.getClipBounds(new Rectangle());

    // Assert
    verify(graphics2D).getClipBounds(isA(Rectangle.class));
    assertSame(rectangle, actualClipBounds);
  }

  /**
   * Test {@link OverrideGraphics2D#getClipBounds(Rectangle)} with {@code Rectangle}.
   *
   * <ul>
   *   <li>Then return {@link Rectangle#Rectangle(int, int)} with one and one.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#getClipBounds(Rectangle)}
   */
  @Test
  @DisplayName(
      "Test getClipBounds(Rectangle) with 'Rectangle'; then return Rectangle(int, int) with one and one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Rectangle OverrideGraphics2D.getClipBounds(Rectangle)"})
  void testGetClipBoundsWithRectangle_thenReturnRectangleWithOneAndOne() {
    // Arrange
    Rectangle rectangle = new Rectangle(1, 1);
    rectangle.add((Rectangle2D) new Rectangle());
    when(graphics2D.getClipBounds(Mockito.<Rectangle>any())).thenReturn(rectangle);

    // Act
    Rectangle actualClipBounds = overrideGraphics2D.getClipBounds(new Rectangle());

    // Assert
    verify(graphics2D).getClipBounds(isA(Rectangle.class));
    assertSame(rectangle, actualClipBounds);
  }

  /**
   * Test {@link OverrideGraphics2D#getClipRect()}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#getClipRect()}
   */
  @Test
  @DisplayName("Test getClipRect()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Rectangle OverrideGraphics2D.getClipRect()"})
  void testGetClipRect() {
    // Arrange
    Rectangle rectangle = new Rectangle();
    when(graphics2D.getClipRect()).thenReturn(rectangle);

    // Act
    Rectangle actualClipRect = overrideGraphics2D.getClipRect();

    // Assert
    verify(graphics2D).getClipRect();
    assertSame(rectangle, actualClipRect);
  }

  /**
   * Test {@link OverrideGraphics2D#getDeviceConfiguration()}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#getDeviceConfiguration()}
   */
  @Test
  @DisplayName("Test getDeviceConfiguration()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"GraphicsConfiguration OverrideGraphics2D.getDeviceConfiguration()"})
  void testGetDeviceConfiguration() {
    // Arrange
    when(graphics2D.getDeviceConfiguration()).thenReturn(null);

    // Act
    GraphicsConfiguration actualDeviceConfiguration = overrideGraphics2D.getDeviceConfiguration();

    // Assert
    verify(graphics2D).getDeviceConfiguration();
    assertNull(actualDeviceConfiguration);
  }

  /**
   * Test {@link OverrideGraphics2D#getFontMetrics()}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#getFontMetrics()}
   */
  @Test
  @DisplayName("Test getFontMetrics()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"FontMetrics OverrideGraphics2D.getFontMetrics()"})
  void testGetFontMetrics() {
    // Arrange
    when(graphics2D.getFontMetrics()).thenReturn(null);

    // Act
    FontMetrics actualFontMetrics = overrideGraphics2D.getFontMetrics();

    // Assert
    verify(graphics2D).getFontMetrics();
    assertNull(actualFontMetrics);
  }

  /**
   * Test {@link OverrideGraphics2D#getFontMetrics(Font)} with {@code Font}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#getFontMetrics(Font)}
   */
  @Test
  @DisplayName("Test getFontMetrics(Font) with 'Font'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"FontMetrics OverrideGraphics2D.getFontMetrics(Font)"})
  void testGetFontMetricsWithFont() {
    // Arrange
    when(graphics2D.getFontMetrics(Mockito.<Font>any())).thenReturn(null);

    // Act
    FontMetrics actualFontMetrics = overrideGraphics2D.getFontMetrics(Font.decode("foo"));

    // Assert
    verify(graphics2D).getFontMetrics(isA(Font.class));
    assertNull(actualFontMetrics);
  }

  /**
   * Test {@link OverrideGraphics2D#getFontRenderContext()}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#getFontRenderContext()}
   */
  @Test
  @DisplayName("Test getFontRenderContext()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"FontRenderContext OverrideGraphics2D.getFontRenderContext()"})
  void testGetFontRenderContext() {
    // Arrange
    when(graphics2D.getFontRenderContext()).thenReturn(null);

    // Act
    FontRenderContext actualFontRenderContext = overrideGraphics2D.getFontRenderContext();

    // Assert
    verify(graphics2D).getFontRenderContext();
    assertNull(actualFontRenderContext);
  }

  /**
   * Test {@link OverrideGraphics2D#getRenderingHint(Key)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#getRenderingHint(RenderingHints.Key)}
   */
  @Test
  @DisplayName("Test getRenderingHint(Key)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Object OverrideGraphics2D.getRenderingHint(RenderingHints.Key)"})
  void testGetRenderingHint() {
    // Arrange
    when(graphics2D.getRenderingHint(Mockito.<Key>any())).thenReturn("Rendering Hint");

    // Act
    Object actualRenderingHint = overrideGraphics2D.getRenderingHint(null);

    // Assert
    verify(graphics2D).getRenderingHint(isNull());
    assertEquals("Rendering Hint", actualRenderingHint);
  }

  /**
   * Test {@link OverrideGraphics2D#getRenderingHints()}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#getRenderingHints()}
   */
  @Test
  @DisplayName("Test getRenderingHints()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"RenderingHints OverrideGraphics2D.getRenderingHints()"})
  void testGetRenderingHints() {
    // Arrange
    when(graphics2D.getRenderingHints()).thenReturn(null);

    // Act
    RenderingHints actualRenderingHints = overrideGraphics2D.getRenderingHints();

    // Assert
    verify(graphics2D).getRenderingHints();
    assertNull(actualRenderingHints);
  }

  /**
   * Test {@link OverrideGraphics2D#getTransform()}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#getTransform()}
   */
  @Test
  @DisplayName("Test getTransform()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"AffineTransform OverrideGraphics2D.getTransform()"})
  void testGetTransform() {
    // Arrange
    AffineTransform affineTransform = new AffineTransform();
    when(graphics2D.getTransform()).thenReturn(affineTransform);

    // Act
    AffineTransform actualTransform = overrideGraphics2D.getTransform();

    // Assert
    verify(graphics2D).getTransform();
    assertSame(affineTransform, actualTransform);
  }

  /**
   * Test {@link OverrideGraphics2D#hit(Rectangle, Shape, boolean)}.
   *
   * <ul>
   *   <li>Given {@link Graphics2D} {@link Graphics2D#hit(Rectangle, Shape, boolean)} return {@code
   *       false}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#hit(Rectangle, Shape, boolean)}
   */
  @Test
  @DisplayName(
      "Test hit(Rectangle, Shape, boolean); given Graphics2D hit(Rectangle, Shape, boolean) return 'false'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OverrideGraphics2D.hit(Rectangle, Shape, boolean)"})
  void testHit_givenGraphics2DHitReturnFalse_thenReturnFalse() {
    // Arrange
    when(graphics2D.hit(Mockito.<Rectangle>any(), Mockito.<Shape>any(), anyBoolean()))
        .thenReturn(false);
    Rectangle rect = new Rectangle();

    // Act
    boolean actualHitResult = overrideGraphics2D.hit(rect, new Polygon(), true);

    // Assert
    verify(graphics2D).hit(isA(Rectangle.class), isA(Shape.class), eq(true));
    assertFalse(actualHitResult);
  }

  /**
   * Test {@link OverrideGraphics2D#hit(Rectangle, Shape, boolean)}.
   *
   * <ul>
   *   <li>Given {@link Graphics2D} {@link Graphics2D#hit(Rectangle, Shape, boolean)} return {@code
   *       true}.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#hit(Rectangle, Shape, boolean)}
   */
  @Test
  @DisplayName(
      "Test hit(Rectangle, Shape, boolean); given Graphics2D hit(Rectangle, Shape, boolean) return 'true'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OverrideGraphics2D.hit(Rectangle, Shape, boolean)"})
  void testHit_givenGraphics2DHitReturnTrue_thenReturnTrue() {
    // Arrange
    when(graphics2D.hit(Mockito.<Rectangle>any(), Mockito.<Shape>any(), anyBoolean()))
        .thenReturn(true);
    Rectangle rect = new Rectangle();

    // Act
    boolean actualHitResult = overrideGraphics2D.hit(rect, new Polygon(), true);

    // Assert
    verify(graphics2D).hit(isA(Rectangle.class), isA(Shape.class), eq(true));
    assertTrue(actualHitResult);
  }

  /**
   * Test {@link OverrideGraphics2D#hitClip(int, int, int, int)}.
   *
   * <ul>
   *   <li>Given {@link Graphics2D} {@link Graphics2D#hitClip(int, int, int, int)} return {@code
   *       false}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#hitClip(int, int, int, int)}
   */
  @Test
  @DisplayName(
      "Test hitClip(int, int, int, int); given Graphics2D hitClip(int, int, int, int) return 'false'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OverrideGraphics2D.hitClip(int, int, int, int)"})
  void testHitClip_givenGraphics2DHitClipReturnFalse_thenReturnFalse() {
    // Arrange
    when(graphics2D.hitClip(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);

    // Act
    boolean actualHitClipResult = overrideGraphics2D.hitClip(2, 3, 1, 1);

    // Assert
    verify(graphics2D).hitClip(2, 3, 1, 1);
    assertFalse(actualHitClipResult);
  }

  /**
   * Test {@link OverrideGraphics2D#hitClip(int, int, int, int)}.
   *
   * <ul>
   *   <li>Given {@link Graphics2D} {@link Graphics2D#hitClip(int, int, int, int)} return {@code
   *       true}.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#hitClip(int, int, int, int)}
   */
  @Test
  @DisplayName(
      "Test hitClip(int, int, int, int); given Graphics2D hitClip(int, int, int, int) return 'true'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OverrideGraphics2D.hitClip(int, int, int, int)"})
  void testHitClip_givenGraphics2DHitClipReturnTrue_thenReturnTrue() {
    // Arrange
    when(graphics2D.hitClip(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(true);

    // Act
    boolean actualHitClipResult = overrideGraphics2D.hitClip(2, 3, 1, 1);

    // Assert
    verify(graphics2D).hitClip(2, 3, 1, 1);
    assertTrue(actualHitClipResult);
  }

  /**
   * Test {@link OverrideGraphics2D#rotate(double)} with {@code theta}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#rotate(double)}
   */
  @Test
  @DisplayName("Test rotate(double) with 'theta'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.rotate(double)"})
  void testRotateWithTheta() {
    // Arrange
    doNothing().when(graphics2D).rotate(anyDouble());

    // Act
    overrideGraphics2D.rotate(10.0d);

    // Assert
    verify(graphics2D).rotate(10.0d);
  }

  /**
   * Test {@link OverrideGraphics2D#rotate(double, double, double)} with {@code theta}, {@code x},
   * {@code y}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#rotate(double, double, double)}
   */
  @Test
  @DisplayName("Test rotate(double, double, double) with 'theta', 'x', 'y'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.rotate(double, double, double)"})
  void testRotateWithThetaXY() {
    // Arrange
    doNothing().when(graphics2D).rotate(anyDouble(), anyDouble(), anyDouble());

    // Act
    overrideGraphics2D.rotate(10.0d, 2.0d, 3.0d);

    // Assert
    verify(graphics2D).rotate(10.0d, 2.0d, 3.0d);
  }

  /**
   * Test {@link OverrideGraphics2D#scale(double, double)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#scale(double, double)}
   */
  @Test
  @DisplayName("Test scale(double, double)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.scale(double, double)"})
  void testScale() {
    // Arrange
    doNothing().when(graphics2D).scale(anyDouble(), anyDouble());

    // Act
    overrideGraphics2D.scale(10.0d, 10.0d);

    // Assert
    verify(graphics2D).scale(10.0d, 10.0d);
  }

  /**
   * Test {@link OverrideGraphics2D#setBackground(Color)}.
   *
   * <ul>
   *   <li>Given {@link Graphics2D} {@link Graphics2D#setBackground(Color)} does nothing.
   *   <li>Then calls {@link Graphics2D#setBackground(Color)}.
   * </ul>
   *
   * <p>Method under test: {@link OverrideGraphics2D#setBackground(Color)}
   */
  @Test
  @DisplayName(
      "Test setBackground(Color); given Graphics2D setBackground(Color) does nothing; then calls setBackground(Color)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setBackground(Color)"})
  void testSetBackground_givenGraphics2DSetBackgroundDoesNothing_thenCallsSetBackground()
      throws NumberFormatException {
    // Arrange
    doNothing().when(graphics2D).setBackground(Mockito.<Color>any());

    // Act
    overrideGraphics2D.setBackground(Color.decode("42"));

    // Assert
    verify(graphics2D).setBackground(isA(Color.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setClip(Shape)} with {@code clip}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#setClip(Shape)}
   */
  @Test
  @DisplayName("Test setClip(Shape) with 'clip'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setClip(Shape)"})
  void testSetClipWithClip() {
    // Arrange
    doNothing().when(graphics2D).setClip(Mockito.<Shape>any());

    // Act
    overrideGraphics2D.setClip(new Polygon());

    // Assert
    verify(graphics2D).setClip(isA(Shape.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setClip(int, int, int, int)} with {@code x}, {@code y}, {@code
   * width}, {@code height}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#setClip(int, int, int, int)}
   */
  @Test
  @DisplayName("Test setClip(int, int, int, int) with 'x', 'y', 'width', 'height'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setClip(int, int, int, int)"})
  void testSetClipWithXYWidthHeight() {
    // Arrange
    doNothing().when(graphics2D).setClip(anyInt(), anyInt(), anyInt(), anyInt());

    // Act
    overrideGraphics2D.setClip(2, 3, 1, 1);

    // Assert
    verify(graphics2D).setClip(2, 3, 1, 1);
  }

  /**
   * Test {@link OverrideGraphics2D#setComposite(Composite)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#setComposite(Composite)}
   */
  @Test
  @DisplayName("Test setComposite(Composite)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setComposite(Composite)"})
  void testSetComposite() {
    // Arrange
    doNothing().when(graphics2D).setComposite(Mockito.<Composite>any());

    // Act
    overrideGraphics2D.setComposite(mock(Composite.class));

    // Assert
    verify(graphics2D).setComposite(isA(Composite.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setRenderingHint(Key, Object)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#setRenderingHint(RenderingHints.Key, Object)}
   */
  @Test
  @DisplayName("Test setRenderingHint(Key, Object)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setRenderingHint(RenderingHints.Key, Object)"})
  void testSetRenderingHint() {
    // Arrange
    doNothing().when(graphics2D).setRenderingHint(Mockito.<Key>any(), Mockito.<Object>any());

    // Act
    overrideGraphics2D.setRenderingHint(null, "Hint Value");

    // Assert
    verify(graphics2D).setRenderingHint(isNull(), isA(Object.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setRenderingHints(Map)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#setRenderingHints(Map)}
   */
  @Test
  @DisplayName("Test setRenderingHints(Map)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setRenderingHints(Map)"})
  void testSetRenderingHints() {
    // Arrange
    doNothing().when(graphics2D).setRenderingHints(Mockito.<Map<?, ?>>any());

    // Act
    overrideGraphics2D.setRenderingHints((Map) new HashMap<>());

    // Assert
    verify(graphics2D).setRenderingHints(isA(Map.class));
  }

  /**
   * Test {@link OverrideGraphics2D#setTransform(AffineTransform)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#setTransform(AffineTransform)}
   */
  @Test
  @DisplayName("Test setTransform(AffineTransform)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.setTransform(AffineTransform)"})
  void testSetTransform() {
    // Arrange
    doNothing().when(graphics2D).setTransform(Mockito.<AffineTransform>any());

    // Act
    overrideGraphics2D.setTransform(new AffineTransform());

    // Assert
    verify(graphics2D).setTransform(isA(AffineTransform.class));
  }

  /**
   * Test {@link OverrideGraphics2D#shear(double, double)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#shear(double, double)}
   */
  @Test
  @DisplayName("Test shear(double, double)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.shear(double, double)"})
  void testShear() {
    // Arrange
    doNothing().when(graphics2D).shear(anyDouble(), anyDouble());

    // Act
    overrideGraphics2D.shear(10.0d, 10.0d);

    // Assert
    verify(graphics2D).shear(10.0d, 10.0d);
  }

  /**
   * Test {@link OverrideGraphics2D#transform(AffineTransform)}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#transform(AffineTransform)}
   */
  @Test
  @DisplayName("Test transform(AffineTransform)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.transform(AffineTransform)"})
  void testTransform() {
    // Arrange
    doNothing().when(graphics2D).transform(Mockito.<AffineTransform>any());

    // Act
    overrideGraphics2D.transform(new AffineTransform());

    // Assert
    verify(graphics2D).transform(isA(AffineTransform.class));
  }

  /**
   * Test {@link OverrideGraphics2D#translate(double, double)} with {@code tx}, {@code ty}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#translate(double, double)}
   */
  @Test
  @DisplayName("Test translate(double, double) with 'tx', 'ty'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.translate(double, double)"})
  void testTranslateWithTxTy() {
    // Arrange
    doNothing().when(graphics2D).translate(anyDouble(), anyDouble());

    // Act
    overrideGraphics2D.translate(10.0d, 10.0d);

    // Assert
    verify(graphics2D).translate(10.0d, 10.0d);
  }

  /**
   * Test {@link OverrideGraphics2D#translate(int, int)} with {@code x}, {@code y}.
   *
   * <p>Method under test: {@link OverrideGraphics2D#translate(int, int)}
   */
  @Test
  @DisplayName("Test translate(int, int) with 'x', 'y'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OverrideGraphics2D.translate(int, int)"})
  void testTranslateWithXY() {
    // Arrange
    doNothing().when(graphics2D).translate(anyInt(), anyInt());

    // Act
    overrideGraphics2D.translate(2, 3);

    // Assert
    verify(graphics2D).translate(2, 3);
  }
}
