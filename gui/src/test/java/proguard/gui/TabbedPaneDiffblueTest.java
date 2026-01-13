package proguard.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Component.BaselineResizeBehavior;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import javax.swing.DefaultButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.BorderUIResource.CompoundBorderUIResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TabbedPaneDiffblueTest {
  /**
   * Test new {@link TabbedPane} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link TabbedPane}
   */
  @Test
  @DisplayName("Test new TabbedPane (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TabbedPane.<init>()"})
  void testNewTabbedPane() {
    // Arrange and Act
    TabbedPane actualTabbedPane = new TabbedPane();

    // Assert
    assertTrue(actualTabbedPane.getLayout() instanceof GridBagLayout);
    assertTrue(actualTabbedPane.getColorModel() instanceof DirectColorModel);
    assertEquals("PanelUI", actualTabbedPane.getUIClassID());
    assertNull(actualTabbedPane.getNextFocusableComponent());
    assertNull(actualTabbedPane.getFocusCycleRootAncestor());
    assertNull(actualTabbedPane.getParent());
    assertNull(actualTabbedPane.getTopLevelAncestor());
    assertNull(actualTabbedPane.getFocusTraversalPolicy());
    assertNull(actualTabbedPane.getGraphics());
    assertNull(actualTabbedPane.getGraphicsConfiguration());
    assertNull(actualTabbedPane.getDropTarget());
    assertNull(actualTabbedPane.getInputContext());
    assertNull(actualTabbedPane.getInputMethodRequests());
    assertNull(actualTabbedPane.getName());
    assertNull(actualTabbedPane.getToolTipText());
    assertNull(actualTabbedPane.getInputVerifier());
    assertNull(actualTabbedPane.getComponentPopupMenu());
    assertNull(actualTabbedPane.getRootPane());
    assertNull(actualTabbedPane.getTransferHandler());
    assertNull(actualTabbedPane.getBorder());
    assertEquals(0, actualTabbedPane.getDebugGraphicsOptions());
    assertEquals(0, actualTabbedPane.getHeight());
    assertEquals(0, actualTabbedPane.getWidth());
    assertEquals(0, actualTabbedPane.getX());
    assertEquals(0, actualTabbedPane.getY());
    assertEquals(0, actualTabbedPane.getComponentListeners().length);
    assertEquals(0, actualTabbedPane.getFocusListeners().length);
    assertEquals(0, actualTabbedPane.getHierarchyBoundsListeners().length);
    assertEquals(0, actualTabbedPane.getHierarchyListeners().length);
    assertEquals(0, actualTabbedPane.getInputMethodListeners().length);
    assertEquals(0, actualTabbedPane.getKeyListeners().length);
    assertEquals(0, actualTabbedPane.getMouseListeners().length);
    assertEquals(0, actualTabbedPane.getMouseMotionListeners().length);
    assertEquals(0, actualTabbedPane.getMouseWheelListeners().length);
    assertEquals(0, actualTabbedPane.getPropertyChangeListeners().length);
    assertEquals(0, actualTabbedPane.getContainerListeners().length);
    assertEquals(0, actualTabbedPane.getAncestorListeners().length);
    assertEquals(0, actualTabbedPane.getRegisteredKeyStrokes().length);
    assertEquals(0, actualTabbedPane.getVetoableChangeListeners().length);
    assertEquals(0.5f, actualTabbedPane.getAlignmentX());
    assertEquals(0.5f, actualTabbedPane.getAlignmentY());
    assertEquals(1, actualTabbedPane.getComponentCount());
    assertEquals(1, actualTabbedPane.getComponents().length);
    assertEquals(BaselineResizeBehavior.OTHER, actualTabbedPane.getBaselineResizeBehavior());
    assertFalse(actualTabbedPane.getIgnoreRepaint());
    assertFalse(actualTabbedPane.hasFocus());
    assertFalse(actualTabbedPane.isCursorSet());
    assertFalse(actualTabbedPane.isDisplayable());
    assertFalse(actualTabbedPane.isFocusOwner());
    assertFalse(actualTabbedPane.isLightweight());
    assertFalse(actualTabbedPane.isMaximumSizeSet());
    assertFalse(actualTabbedPane.isMinimumSizeSet());
    assertFalse(actualTabbedPane.isPreferredSizeSet());
    assertFalse(actualTabbedPane.isShowing());
    assertFalse(actualTabbedPane.isValid());
    assertFalse(actualTabbedPane.isFocusCycleRoot());
    assertFalse(actualTabbedPane.isFocusTraversalPolicyProvider());
    assertFalse(actualTabbedPane.isFocusTraversalPolicySet());
    assertFalse(actualTabbedPane.getAutoscrolls());
    assertFalse(actualTabbedPane.getInheritsPopupMenu());
    assertFalse(actualTabbedPane.isManagingFocus());
    assertFalse(actualTabbedPane.isPaintingForPrint());
    assertFalse(actualTabbedPane.isPaintingTile());
    assertFalse(actualTabbedPane.isValidateRoot());
    assertTrue(actualTabbedPane.getFocusTraversalKeysEnabled());
    assertTrue(actualTabbedPane.isBackgroundSet());
    assertTrue(actualTabbedPane.isEnabled());
    assertTrue(actualTabbedPane.isFocusable());
    assertTrue(actualTabbedPane.isFontSet());
    assertTrue(actualTabbedPane.isForegroundSet());
    assertTrue(actualTabbedPane.isVisible());
    assertTrue(actualTabbedPane.getVerifyInputWhenFocusTarget());
    assertTrue(actualTabbedPane.isDoubleBuffered());
    assertTrue(actualTabbedPane.isOpaque());
    assertTrue(actualTabbedPane.isOptimizedDrawingEnabled());
    assertTrue(actualTabbedPane.isRequestFocusEnabled());
  }

  /**
   * Test {@link TabbedPane#add(String, Component)} with {@code String}, {@code Component}.
   *
   * <ul>
   *   <li>Given {@link TabbedPane} (default constructor).
   *   <li>When {@link Canvas#Canvas()}.
   *   <li>Then return {@link Canvas#Canvas()}.
   * </ul>
   *
   * <p>Method under test: {@link TabbedPane#add(String, Component)}
   */
  @Test
  @DisplayName(
      "Test add(String, Component) with 'String', 'Component'; given TabbedPane (default constructor); when Canvas(); then return Canvas()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Component TabbedPane.add(String, Component)"})
  void testAddWithStringComponent_givenTabbedPane_whenCanvas_thenReturnCanvas() {
    // Arrange
    TabbedPane tabbedPane = new TabbedPane();
    Canvas component = new Canvas();

    // Act
    Component actualAddResult = tabbedPane.add("Dr", component);

    // Assert
    assertSame(component, actualAddResult);
  }

  /**
   * Test {@link TabbedPane#addImage(Image)}.
   *
   * <ul>
   *   <li>Given {@link TabbedPane} (default constructor).
   *   <li>Then ColorModel return {@link DirectColorModel}.
   * </ul>
   *
   * <p>Method under test: {@link TabbedPane#addImage(Image)}
   */
  @Test
  @DisplayName(
      "Test addImage(Image); given TabbedPane (default constructor); then ColorModel return DirectColorModel")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Component TabbedPane.addImage(Image)"})
  void testAddImage_givenTabbedPane_thenColorModelReturnDirectColorModel() {
    // Arrange
    TabbedPane tabbedPane = new TabbedPane();

    // Act
    Component actualAddImageResult = tabbedPane.addImage(new BufferedImage(1, 1, 1));

    // Assert
    assertTrue(actualAddImageResult.getColorModel() instanceof DirectColorModel);
    assertTrue(((JButton) actualAddImageResult).getModel() instanceof DefaultButtonModel);
    assertTrue(((JButton) actualAddImageResult).getIcon() instanceof ImageIcon);
    assertTrue(actualAddImageResult instanceof JButton);
    assertTrue(((JButton) actualAddImageResult).getBorder() instanceof CompoundBorderUIResource);
    Container parent = actualAddImageResult.getParent();
    assertTrue(parent instanceof TabbedPane);
    assertEquals("", ((JButton) actualAddImageResult).getActionCommand());
    assertEquals("", ((JButton) actualAddImageResult).getLabel());
    assertEquals("", ((JButton) actualAddImageResult).getText());
    assertEquals("ButtonUI", ((JButton) actualAddImageResult).getUIClassID());
    assertNull(((JButton) actualAddImageResult).getNextFocusableComponent());
    assertNull(actualAddImageResult.getFocusCycleRootAncestor());
    assertNull(((JButton) actualAddImageResult).getTopLevelAncestor());
    assertNull(((JButton) actualAddImageResult).getFocusTraversalPolicy());
    assertNull(actualAddImageResult.getGraphics());
    assertNull(actualAddImageResult.getGraphicsConfiguration());
    assertNull(((JButton) actualAddImageResult).getLayout());
    assertNull(actualAddImageResult.getDropTarget());
    assertNull(actualAddImageResult.getInputContext());
    assertNull(actualAddImageResult.getInputMethodRequests());
    assertNull(((JButton) actualAddImageResult).getSelectedObjects());
    assertNull(actualAddImageResult.getName());
    assertNull(((JButton) actualAddImageResult).getToolTipText());
    assertNull(((JButton) actualAddImageResult).getAction());
    assertNull(((JButton) actualAddImageResult).getPressedIcon());
    assertNull(((JButton) actualAddImageResult).getRolloverIcon());
    assertNull(((JButton) actualAddImageResult).getRolloverSelectedIcon());
    assertNull(((JButton) actualAddImageResult).getSelectedIcon());
    assertNull(((JButton) actualAddImageResult).getInputVerifier());
    assertNull(((JButton) actualAddImageResult).getComponentPopupMenu());
    assertNull(((JButton) actualAddImageResult).getRootPane());
    assertNull(((JButton) actualAddImageResult).getTransferHandler());
    assertEquals(-1, ((JButton) actualAddImageResult).getDisplayedMnemonicIndex());
    assertEquals(0, actualAddImageResult.getHeight());
    assertEquals(0, actualAddImageResult.getWidth());
    assertEquals(0, actualAddImageResult.getX());
    assertEquals(0, actualAddImageResult.getY());
    assertEquals(0, ((JButton) actualAddImageResult).getComponentCount());
    assertEquals(0, ((JButton) actualAddImageResult).getMnemonic());
    assertEquals(0, ((JButton) actualAddImageResult).getVerticalTextPosition());
    assertEquals(0, ((JButton) actualAddImageResult).getDebugGraphicsOptions());
    assertEquals(0, actualAddImageResult.getComponentListeners().length);
    assertEquals(0, actualAddImageResult.getHierarchyBoundsListeners().length);
    assertEquals(0, actualAddImageResult.getHierarchyListeners().length);
    assertEquals(0, actualAddImageResult.getInputMethodListeners().length);
    assertEquals(0, actualAddImageResult.getKeyListeners().length);
    assertEquals(0, actualAddImageResult.getMouseWheelListeners().length);
    assertEquals(0, ((JButton) actualAddImageResult).getComponents().length);
    assertEquals(0, ((JButton) actualAddImageResult).getContainerListeners().length);
    assertEquals(0, ((JButton) actualAddImageResult).getActionListeners().length);
    assertEquals(0, ((JButton) actualAddImageResult).getItemListeners().length);
    assertEquals(0, ((JButton) actualAddImageResult).getAncestorListeners().length);
    assertEquals(0, ((JButton) actualAddImageResult).getVetoableChangeListeners().length);
    assertEquals(0.0f, actualAddImageResult.getAlignmentX());
    assertEquals(0.5f, actualAddImageResult.getAlignmentY());
    assertEquals(0L, ((JButton) actualAddImageResult).getMultiClickThreshhold());
    assertEquals(1, actualAddImageResult.getFocusListeners().length);
    assertEquals(1, actualAddImageResult.getMouseListeners().length);
    assertEquals(1, actualAddImageResult.getMouseMotionListeners().length);
    assertEquals(1, actualAddImageResult.getPropertyChangeListeners().length);
    assertEquals(1, ((JButton) actualAddImageResult).getChangeListeners().length);
    assertEquals(11, ((JButton) actualAddImageResult).getHorizontalTextPosition());
    assertEquals(2, ((JButton) actualAddImageResult).getHorizontalAlignment());
    assertEquals(2, ((JButton) actualAddImageResult).getRegisteredKeyStrokes().length);
    assertEquals(3, ((JButton) actualAddImageResult).getVerticalAlignment());
    assertEquals(4, ((JButton) actualAddImageResult).getIconTextGap());
    assertEquals(
        BaselineResizeBehavior.CONSTANT_DESCENT, actualAddImageResult.getBaselineResizeBehavior());
    assertFalse(actualAddImageResult.getIgnoreRepaint());
    assertFalse(actualAddImageResult.hasFocus());
    assertFalse(actualAddImageResult.isCursorSet());
    assertFalse(actualAddImageResult.isDisplayable());
    assertFalse(actualAddImageResult.isDoubleBuffered());
    assertFalse(actualAddImageResult.isFocusOwner());
    assertFalse(actualAddImageResult.isFocusable());
    assertFalse(actualAddImageResult.isLightweight());
    assertFalse(actualAddImageResult.isMaximumSizeSet());
    assertFalse(actualAddImageResult.isMinimumSizeSet());
    assertFalse(actualAddImageResult.isShowing());
    assertFalse(actualAddImageResult.isValid());
    assertFalse(((JButton) actualAddImageResult).isFocusCycleRoot());
    assertFalse(((JButton) actualAddImageResult).isFocusTraversalPolicyProvider());
    assertFalse(((JButton) actualAddImageResult).isFocusTraversalPolicySet());
    assertFalse(((JButton) actualAddImageResult).getHideActionText());
    assertFalse(((JButton) actualAddImageResult).isFocusPainted());
    assertFalse(((JButton) actualAddImageResult).isRolloverEnabled());
    assertFalse(((JButton) actualAddImageResult).isSelected());
    assertFalse(((JButton) actualAddImageResult).isDefaultButton());
    assertFalse(((JButton) actualAddImageResult).getAutoscrolls());
    assertFalse(((JButton) actualAddImageResult).getInheritsPopupMenu());
    assertFalse(((JButton) actualAddImageResult).isManagingFocus());
    assertFalse(((JButton) actualAddImageResult).isPaintingForPrint());
    assertFalse(((JButton) actualAddImageResult).isPaintingTile());
    assertFalse(((JButton) actualAddImageResult).isRequestFocusEnabled());
    assertFalse(((JButton) actualAddImageResult).isValidateRoot());
    assertTrue(actualAddImageResult.getFocusTraversalKeysEnabled());
    assertTrue(actualAddImageResult.isBackgroundSet());
    assertTrue(actualAddImageResult.isEnabled());
    assertTrue(actualAddImageResult.isFontSet());
    assertTrue(actualAddImageResult.isForegroundSet());
    assertTrue(actualAddImageResult.isOpaque());
    assertTrue(actualAddImageResult.isPreferredSizeSet());
    assertTrue(actualAddImageResult.isVisible());
    assertTrue(((JButton) actualAddImageResult).isBorderPainted());
    assertTrue(((JButton) actualAddImageResult).isContentAreaFilled());
    assertTrue(((JButton) actualAddImageResult).isDefaultCapable());
    assertTrue(((JButton) actualAddImageResult).getVerifyInputWhenFocusTarget());
    assertTrue(((JButton) actualAddImageResult).isOptimizedDrawingEnabled());
    assertSame(tabbedPane, parent);
  }
}
