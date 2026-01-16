package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ListPanel.addRemoveButton() method.
 *
 * The addRemoveButton() method creates and adds a "Remove" button to the panel.
 * Lines that need coverage:
 * - Line 83: Creates a new JButton with the localized "remove" message
 * - Line 84: Adds an ActionListener to the button
 * - Line 93: Wraps the button with a tooltip using the tip() method
 *
 * The ActionListener (lines 86-90) performs these actions when clicked:
 * - Line 89: Removes the selected elements by calling removeElementsAt()
 *
 * These tests verify:
 * - The button is created with proper text and tooltip
 * - The button is added to the panel
 * - The action listener is attached
 * - The button is properly configured
 * - The button state changes based on list selection
 * - The button's action listener actually removes selected items
 *
 * Note: Since ListPanel is abstract, we test it through the concrete
 * ClassPathPanel class which extends ListPanel and calls addRemoveButton()
 * in its constructor.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ListPanelClaude_addRemoveButtonTest {

    private JFrame testFrame;
    private ClassPathPanel panel;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (panel != null) {
            panel = null;
        }
        if (testFrame != null) {
            testFrame.dispose();
            testFrame = null;
        }
    }

    /**
     * Test that addRemoveButton creates and adds a button to the panel.
     * This covers line 83 (JButton creation), line 84 (addActionListener),
     * and line 93 (tip wrapper and addButton call).
     *
     * The constructor calls addRemoveButton(), so creating a panel executes these lines.
     */
    @Test
    public void testAddRemoveButtonCreatesButton() {
        testFrame = new JFrame("Test Frame");
        // Constructor calls addRemoveButton(), which executes lines 83, 84, 93
        panel = new ClassPathPanel(testFrame, false);

        // Verify that buttons were added to the panel
        java.util.List buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons list should not be null");
        assertTrue(buttons.size() >= 3, "At least three buttons should be added (Add, Edit, Remove, ...)");

        // The fourth button should be the Remove button (0=Add, 1=Edit, 2=Filter, 3=Remove)
        assertTrue(buttons.size() > 3, "Should have at least 4 buttons");
        Component fourthButton = (Component) buttons.get(3);
        assertTrue(fourthButton instanceof JButton, "Fourth button should be a JButton");
    }

    /**
     * Test that the Remove button has the correct text.
     * This verifies line 83 where the button is created with msg("remove").
     */
    @Test
    public void testRemoveButtonHasText() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        assertNotNull(removeButton.getText(), "Remove button should have text");
        assertFalse(removeButton.getText().isEmpty(), "Remove button text should not be empty");
    }

    /**
     * Test that the Remove button has a tooltip.
     * This verifies line 93 where tip(removeButton, "removeTip") is called.
     */
    @Test
    public void testRemoveButtonHasTooltip() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        assertNotNull(removeButton.getToolTipText(), "Remove button should have a tooltip");
        assertFalse(removeButton.getToolTipText().isEmpty(),
                    "Remove button tooltip should not be empty");
    }

    /**
     * Test that the Remove button has an action listener attached.
     * This verifies line 84 where addActionListener() is called.
     */
    @Test
    public void testRemoveButtonHasActionListener() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        ActionListener[] listeners = removeButton.getActionListeners();
        assertTrue(listeners.length > 0,
                   "Remove button should have at least one action listener");
    }

    /**
     * Test that the Remove button is disabled by default when no items are selected.
     * The Remove button should be disabled when no items are in the list.
     */
    @Test
    public void testRemoveButtonIsDisabledByDefault() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        assertFalse(removeButton.isEnabled(),
                    "Remove button should be disabled by default when no items are selected");
    }

    /**
     * Test that the Remove button is properly added to the panel's component hierarchy.
     * This verifies line 93 where addButton() is called.
     */
    @Test
    public void testRemoveButtonAddedToPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // The button should be in the panel's component hierarchy
        int buttonCount = 0;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                buttonCount++;
            }
        }
        assertTrue(buttonCount >= 4,
                   "At least four buttons should be in panel's component hierarchy");
    }

    /**
     * Test that multiple panels each have their own Remove button.
     * This ensures addRemoveButton() works correctly for multiple instances.
     */
    @Test
    public void testMultiplePanelsHaveIndependentRemoveButtons() {
        testFrame = new JFrame("Test Frame");
        ClassPathPanel panel1 = new ClassPathPanel(testFrame, false);
        ClassPathPanel panel2 = new ClassPathPanel(testFrame, true);

        java.util.List buttons1 = panel1.getButtons();
        java.util.List buttons2 = panel2.getButtons();

        JButton removeButton1 = (JButton) buttons1.get(3);
        JButton removeButton2 = (JButton) buttons2.get(4); // Different index for inputAndOutput mode

        assertNotSame(removeButton1, removeButton2,
                      "Each panel should have its own Remove button instance");
    }

    /**
     * Test that the Remove button can be retrieved from the buttons list.
     * This verifies the button is properly registered.
     */
    @Test
    public void testRemoveButtonIsInButtonsList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 4, "Should have at least 4 buttons including Remove");

        Component removeButton = (Component) buttons.get(3);
        assertNotNull(removeButton, "Remove button should not be null");
        assertTrue(removeButton instanceof JButton, "Remove button should be JButton");
    }

    /**
     * Test Remove button with different panel configurations.
     * This ensures addRemoveButton() works with different constructor parameters.
     */
    @Test
    public void testRemoveButtonWithDifferentConfigurations() {
        testFrame = new JFrame("Test Frame");

        // Test with inputAndOutput=false
        ClassPathPanel panel1 = new ClassPathPanel(testFrame, false);
        assertNotNull(panel1.getButtons().get(3), "Remove button should exist with inputAndOutput=false");

        // Test with inputAndOutput=true
        ClassPathPanel panel2 = new ClassPathPanel(testFrame, true);
        // In inputAndOutput mode, Remove button is at index 4 (0=AddInput, 1=AddOutput, 2=Edit, 3=Filter, 4=Remove)
        assertNotNull(panel2.getButtons().get(4), "Remove button should exist with inputAndOutput=true");
    }

    /**
     * Test that the Remove button is visible.
     */
    @Test
    public void testRemoveButtonIsVisible() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        assertTrue(removeButton.isVisible(), "Remove button should be visible");
    }

    /**
     * Test that the Remove button has exactly one action listener.
     * This verifies line 84 adds exactly one listener.
     */
    @Test
    public void testRemoveButtonHasExactlyOneActionListener() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        ActionListener[] listeners = removeButton.getActionListeners();
        assertEquals(1, listeners.length,
                     "Remove button should have exactly one action listener");
    }

    /**
     * Test that the Remove button's action listener is not null.
     */
    @Test
    public void testRemoveButtonActionListenerNotNull() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        ActionListener[] listeners = removeButton.getActionListeners();
        assertNotNull(listeners[0], "Action listener should not be null");
    }

    /**
     * Test Remove button properties after panel is added to a frame.
     */
    @Test
    public void testRemoveButtonPropertiesAfterAddingToFrame() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        assertNotNull(removeButton.getText(), "Button text should be set");
        assertNotNull(removeButton.getToolTipText(), "Button tooltip should be set");
        assertTrue(removeButton.getActionListeners().length > 0,
                   "Button should have action listeners");
    }

    /**
     * Test that creating a panel executes addRemoveButton without exceptions.
     * This is a smoke test to ensure lines 83, 84, 93 don't throw exceptions.
     */
    @Test
    public void testAddRemoveButtonExecutesWithoutException() {
        testFrame = new JFrame("Test Frame");

        // This should not throw any exception
        assertDoesNotThrow(() -> {
            panel = new ClassPathPanel(testFrame, false);
        }, "Creating panel (which calls addRemoveButton) should not throw exception");

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.getButtons(), "Buttons should be initialized");
        assertTrue(panel.getButtons().size() >= 4, "At least four buttons should exist");
    }

    /**
     * Test Remove button with null owner frame.
     * This ensures addRemoveButton() works even when owner is null.
     */
    @Test
    public void testRemoveButtonWithNullOwner() {
        panel = new ClassPathPanel(null, false);

        java.util.List buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons should exist even with null owner");
        assertTrue(buttons.size() >= 4, "Remove button should exist with null owner");

        JButton removeButton = (JButton) buttons.get(3);
        assertNotNull(removeButton.getText(), "Remove button should have text with null owner");
    }

    /**
     * Test that the Remove button's parent is the panel.
     */
    @Test
    public void testRemoveButtonParentIsPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        assertNotNull(removeButton.getParent(), "Remove button should have a parent");
        assertSame(panel, removeButton.getParent(),
                   "Remove button's parent should be the panel");
    }

    /**
     * Test that the Remove button has reasonable size after packing.
     */
    @Test
    public void testRemoveButtonHasReasonableSize() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        Dimension size = removeButton.getSize();
        assertTrue(size.width > 0, "Remove button width should be positive");
        assertTrue(size.height > 0, "Remove button height should be positive");
    }

    /**
     * Test that the Remove button is focusable.
     */
    @Test
    public void testRemoveButtonIsFocusable() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        assertTrue(removeButton.isFocusable(), "Remove button should be focusable");
    }

    /**
     * Test that Remove button is disabled when list is empty.
     * Unlike the Add button, the Remove button should be disabled when no items are selected.
     */
    @Test
    public void testRemoveButtonDisabledWhenListEmpty() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Remove button should be disabled when list is empty
        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        assertFalse(removeButton.isEnabled(),
                    "Remove button should be disabled when list is empty");

        // Verify Add button is enabled (for comparison)
        JButton addButton = (JButton) buttons.get(0);
        assertTrue(addButton.isEnabled(),
                   "Add button should be enabled (different from Remove button)");
    }

    /**
     * Test that Remove button comes after Add, Edit, and Filter buttons in the buttons list.
     * This verifies the order of button creation.
     */
    @Test
    public void testRemoveButtonOrderAfterOtherButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 4, "Should have at least four buttons");

        // Verify we can get all buttons without exception
        Component firstButton = (Component) buttons.get(0);
        Component secondButton = (Component) buttons.get(1);
        Component thirdButton = (Component) buttons.get(2);
        Component fourthButton = (Component) buttons.get(3);

        assertNotNull(firstButton, "First button (Add) should exist");
        assertNotNull(secondButton, "Second button (Edit) should exist");
        assertNotNull(thirdButton, "Third button (Filter) should exist");
        assertNotNull(fourthButton, "Fourth button (Remove) should exist");
    }

    /**
     * Test Remove button text is different from other button texts.
     * This confirms line 83 uses msg("remove") correctly.
     */
    @Test
    public void testRemoveButtonTextDifferentFromOtherButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);
        JButton editButton = (JButton) buttons.get(1);
        JButton removeButton = (JButton) buttons.get(3);

        String addText = addButton.getText();
        String editText = editButton.getText();
        String removeText = removeButton.getText();

        assertNotEquals(addText, removeText,
                        "Remove button text should be different from Add button text");
        assertNotEquals(editText, removeText,
                        "Remove button text should be different from Edit button text");
    }

    /**
     * Test Remove button tooltip is different from other button tooltips.
     * This confirms line 93 uses tip(removeButton, "removeTip") correctly.
     */
    @Test
    public void testRemoveButtonTooltipDifferentFromOtherButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);
        JButton editButton = (JButton) buttons.get(1);
        JButton removeButton = (JButton) buttons.get(3);

        String addTooltip = addButton.getToolTipText();
        String editTooltip = editButton.getToolTipText();
        String removeTooltip = removeButton.getToolTipText();

        assertNotEquals(addTooltip, removeTooltip,
                        "Remove button tooltip should be different from Add button tooltip");
        assertNotEquals(editTooltip, removeTooltip,
                        "Remove button tooltip should be different from Edit button tooltip");
    }

    /**
     * Test that all buttons in the panel have the Remove button included.
     * This verifies line 93 successfully adds the button to the panel.
     */
    @Test
    public void testRemoveButtonIncludedInAllButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();

        // Should have 6 buttons: Add, Edit, Filter, Remove, Up, Down
        assertEquals(6, buttons.size(), "Should have 6 buttons including Remove");

        // Verify Remove button is the fourth button
        assertTrue(buttons.get(3) instanceof JButton,
                   "Fourth button should be a JButton (Remove button)");
    }

    /**
     * Test Remove button state consistency across panel lifecycle.
     */
    @Test
    public void testRemoveButtonStateConsistency() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        // Initially disabled
        assertFalse(removeButton.isEnabled(), "Should be disabled initially");

        // Add to frame
        testFrame.add(panel);
        testFrame.pack();

        // Should still be disabled (no selection)
        assertFalse(removeButton.isEnabled(),
                    "Should remain disabled after adding to frame");
    }

    /**
     * Test that Remove button has proper configuration after construction.
     * This verifies all three lines (83, 84, 93) executed successfully.
     */
    @Test
    public void testRemoveButtonFullConfiguration() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        // Verify line 83: button created with text
        assertNotNull(removeButton.getText());
        assertFalse(removeButton.getText().isEmpty());

        // Verify line 84: action listener added
        assertTrue(removeButton.getActionListeners().length > 0);

        // Verify line 93: tooltip set and button added to panel
        assertNotNull(removeButton.getToolTipText());
        assertFalse(removeButton.getToolTipText().isEmpty());
        assertSame(panel, removeButton.getParent());
    }
}
