package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassSpecificationsPanel.addEditButton() method.
 *
 * The addEditButton() method creates and adds an "Edit" button to the panel.
 * Lines that need coverage:
 * - Line 90: Creates a new JButton with the localized "edit" message
 * - Line 91: Adds an ActionListener to the button
 * - Line 109: Wraps the button with a tooltip using the tip() method
 * - Line 110: Adds the button to the panel using addButton()
 *
 * The ActionListener (lines 93-106) performs these actions when clicked:
 * - Lines 95-96: Gets the selected ClassSpecification from the list
 * - Line 98: Sets the specification in the dialog
 * - Line 99: Shows the dialog and captures the return value
 * - Lines 100-105: If user approves, replaces the element at the selected index
 *
 * These tests verify:
 * - The button is created with proper text and tooltip
 * - The button is added to the panel
 * - The action listener is attached
 * - The button is properly configured
 * - The button state changes based on list selection
 *
 * Note: Testing the action listener's behavior (lines 93-106) requires simulating
 * user interaction with the dialog, which is complex. These tests focus on
 * verifying that the button creation and setup code (lines 90, 91, 109, 110) is
 * executed by checking the button's properties and presence.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassSpecificationsPanelClaude_addEditButtonTest {

    private JFrame testFrame;
    private ClassSpecificationsPanel panel;

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
     * Test that addEditButton creates and adds a button to the panel.
     * This covers line 90 (JButton creation), line 91 (addActionListener),
     * line 109 (tip wrapper), and line 110 (addButton call).
     *
     * The constructor calls addEditButton(), so creating a panel executes these lines.
     */
    @Test
    public void testAddEditButtonCreatesButton() {
        testFrame = new JFrame("Test Frame");
        // Constructor calls addEditButton(), which executes lines 90, 91, 109, 110
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Verify that buttons were added to the panel
        java.util.List<Component> buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons list should not be null");
        assertTrue(buttons.size() >= 2, "At least two buttons should be added (Add and Edit)");

        // The second button should be the Edit button
        Component secondButton = buttons.get(1);
        assertTrue(secondButton instanceof JButton, "Second button should be a JButton");
    }

    /**
     * Test that the Edit button has the correct text.
     * This verifies line 90 where the button is created with msg("edit").
     */
    @Test
    public void testEditButtonHasText() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertNotNull(editButton.getText(), "Edit button should have text");
        assertFalse(editButton.getText().isEmpty(), "Edit button text should not be empty");
    }

    /**
     * Test that the Edit button has a tooltip.
     * This verifies line 109 where tip(editButton, "editTip") is called.
     */
    @Test
    public void testEditButtonHasTooltip() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertNotNull(editButton.getToolTipText(), "Edit button should have a tooltip");
        assertFalse(editButton.getToolTipText().isEmpty(),
                    "Edit button tooltip should not be empty");
    }

    /**
     * Test that the Edit button has an action listener attached.
     * This verifies line 91 where addActionListener() is called.
     */
    @Test
    public void testEditButtonHasActionListener() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        ActionListener[] listeners = editButton.getActionListeners();
        assertTrue(listeners.length > 0,
                   "Edit button should have at least one action listener");
    }

    /**
     * Test that the Edit button is disabled by default when no items are selected.
     * The Edit button should be disabled when no items are in the list.
     */
    @Test
    public void testEditButtonIsDisabledByDefault() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertFalse(editButton.isEnabled(),
                    "Edit button should be disabled by default when no items are selected");
    }

    /**
     * Test that the Edit button is properly added to the panel's component hierarchy.
     * This verifies line 110 where addButton() is called.
     */
    @Test
    public void testEditButtonAddedToPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // The button should be in the panel's component hierarchy
        int buttonCount = 0;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                buttonCount++;
            }
        }
        assertTrue(buttonCount >= 2,
                   "At least two buttons (Add and Edit) should be in panel's component hierarchy");
    }

    /**
     * Test that multiple panels each have their own Edit button.
     * This ensures addEditButton() works correctly for multiple instances.
     */
    @Test
    public void testMultiplePanelsHaveIndependentEditButtons() {
        testFrame = new JFrame("Test Frame");
        ClassSpecificationsPanel panel1 = new ClassSpecificationsPanel(testFrame, false, true);
        ClassSpecificationsPanel panel2 = new ClassSpecificationsPanel(testFrame, true, false);

        java.util.List<Component> buttons1 = panel1.getButtons();
        java.util.List<Component> buttons2 = panel2.getButtons();

        JButton editButton1 = (JButton) buttons1.get(1);
        JButton editButton2 = (JButton) buttons2.get(1);

        assertNotSame(editButton1, editButton2,
                      "Each panel should have its own Edit button instance");
    }

    /**
     * Test that the Edit button can be retrieved from the buttons list.
     * This verifies the button is properly registered.
     */
    @Test
    public void testEditButtonIsSecondInButtonsList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        assertTrue(buttons.size() >= 2, "Should have at least Add and Edit buttons");

        Component secondButton = buttons.get(1);
        assertNotNull(secondButton, "Second button should not be null");
        assertTrue(secondButton instanceof JButton, "Second button should be JButton");
    }

    /**
     * Test Edit button with different panel configurations.
     * This ensures addEditButton() works with different constructor parameters.
     */
    @Test
    public void testEditButtonWithDifferentConfigurations() {
        testFrame = new JFrame("Test Frame");

        // Test with includeKeepSettings=false, includeFieldButton=false
        ClassSpecificationsPanel panel1 = new ClassSpecificationsPanel(testFrame, false, false);
        assertNotNull(panel1.getButtons().get(1), "Edit button should exist with (false, false)");

        // Test with includeKeepSettings=true, includeFieldButton=true
        ClassSpecificationsPanel panel2 = new ClassSpecificationsPanel(testFrame, true, true);
        assertNotNull(panel2.getButtons().get(1), "Edit button should exist with (true, true)");

        // Test with includeKeepSettings=true, includeFieldButton=false
        ClassSpecificationsPanel panel3 = new ClassSpecificationsPanel(testFrame, true, false);
        assertNotNull(panel3.getButtons().get(1), "Edit button should exist with (true, false)");
    }

    /**
     * Test that the Edit button is visible.
     */
    @Test
    public void testEditButtonIsVisible() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertTrue(editButton.isVisible(), "Edit button should be visible");
    }

    /**
     * Test that the Edit button has exactly one action listener.
     * This verifies line 91 adds exactly one listener.
     */
    @Test
    public void testEditButtonHasExactlyOneActionListener() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        ActionListener[] listeners = editButton.getActionListeners();
        assertEquals(1, listeners.length,
                     "Edit button should have exactly one action listener");
    }

    /**
     * Test that the Edit button's action listener is not null.
     */
    @Test
    public void testEditButtonActionListenerNotNull() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        ActionListener[] listeners = editButton.getActionListeners();
        assertNotNull(listeners[0], "Action listener should not be null");
    }

    /**
     * Test Edit button properties after panel is added to a frame.
     */
    @Test
    public void testEditButtonPropertiesAfterAddingToFrame() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertNotNull(editButton.getText(), "Button text should be set");
        assertNotNull(editButton.getToolTipText(), "Button tooltip should be set");
        assertTrue(editButton.getActionListeners().length > 0,
                   "Button should have action listeners");
    }

    /**
     * Test that creating a panel executes addEditButton without exceptions.
     * This is a smoke test to ensure lines 90, 91, 109, 110 don't throw exceptions.
     */
    @Test
    public void testAddEditButtonExecutesWithoutException() {
        testFrame = new JFrame("Test Frame");

        // This should not throw any exception
        assertDoesNotThrow(() -> {
            panel = new ClassSpecificationsPanel(testFrame, false, true);
        }, "Creating panel (which calls addEditButton) should not throw exception");

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.getButtons(), "Buttons should be initialized");
        assertTrue(panel.getButtons().size() >= 2, "At least two buttons should exist");
    }

    /**
     * Test Edit button with null owner frame.
     * This ensures addEditButton() works even when owner is null.
     */
    @Test
    public void testEditButtonWithNullOwner() {
        panel = new ClassSpecificationsPanel(null, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons should exist even with null owner");
        assertTrue(buttons.size() >= 2, "Edit button should exist with null owner");

        JButton editButton = (JButton) buttons.get(1);
        assertNotNull(editButton.getText(), "Edit button should have text with null owner");
    }

    /**
     * Test that the Edit button's parent is the panel.
     */
    @Test
    public void testEditButtonParentIsPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertNotNull(editButton.getParent(), "Edit button should have a parent");
        assertSame(panel, editButton.getParent(),
                   "Edit button's parent should be the panel");
    }

    /**
     * Test that the Edit button has reasonable size after packing.
     */
    @Test
    public void testEditButtonHasReasonableSize() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        Dimension size = editButton.getSize();
        assertTrue(size.width > 0, "Edit button width should be positive");
        assertTrue(size.height > 0, "Edit button height should be positive");
    }

    /**
     * Test that the Edit button is focusable.
     */
    @Test
    public void testEditButtonIsFocusable() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertTrue(editButton.isFocusable(), "Edit button should be focusable");
    }

    /**
     * Test that the Edit button is disabled after panel initialization when list is empty.
     * Unlike the Add button, the Edit button should be disabled when no items are selected.
     */
    @Test
    public void testEditButtonDisabledWhenListEmpty() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Edit button should be disabled when list is empty
        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertFalse(editButton.isEnabled(),
                    "Edit button should be disabled when list is empty");

        // Verify Add button is enabled (for comparison)
        JButton addButton = (JButton) buttons.get(0);
        assertTrue(addButton.isEnabled(),
                   "Add button should be enabled (different from Edit button)");
    }

    /**
     * Test that Edit button comes after Add button in the buttons list.
     * This verifies the order of button creation.
     */
    @Test
    public void testEditButtonOrderAfterAddButton() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        assertTrue(buttons.size() >= 2, "Should have at least two buttons");

        // Verify we can get both buttons without exception
        Component firstButton = buttons.get(0);
        Component secondButton = buttons.get(1);

        assertNotNull(firstButton, "First button (Add) should exist");
        assertNotNull(secondButton, "Second button (Edit) should exist");
        assertNotSame(firstButton, secondButton, "Add and Edit should be different buttons");
    }

    /**
     * Test Edit button text is different from Add button text.
     * This confirms line 90 uses msg("edit") and not msg("add").
     */
    @Test
    public void testEditButtonTextDifferentFromAddButton() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);
        JButton editButton = (JButton) buttons.get(1);

        String addText = addButton.getText();
        String editText = editButton.getText();

        assertNotEquals(addText, editText,
                        "Edit button text should be different from Add button text");
    }

    /**
     * Test Edit button tooltip is different from Add button tooltip.
     * This confirms line 109 uses tip(editButton, "editTip") correctly.
     */
    @Test
    public void testEditButtonTooltipDifferentFromAddButton() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);
        JButton editButton = (JButton) buttons.get(1);

        String addTooltip = addButton.getToolTipText();
        String editTooltip = editButton.getToolTipText();

        assertNotEquals(addTooltip, editTooltip,
                        "Edit button tooltip should be different from Add button tooltip");
    }

    /**
     * Test that all buttons in the panel have the Edit button included.
     * This verifies line 110 successfully adds the button to the panel.
     */
    @Test
    public void testEditButtonIncludedInAllButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();

        // Should have 5 buttons: Add, Edit, Remove, Up, Down
        assertEquals(5, buttons.size(), "Should have 5 buttons including Edit");

        // Verify Edit button is the second button
        assertTrue(buttons.get(1) instanceof JButton,
                   "Second button should be a JButton (Edit button)");
    }

    /**
     * Test Edit button state consistency across panel lifecycle.
     */
    @Test
    public void testEditButtonStateConsistency() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        // Initially disabled
        assertFalse(editButton.isEnabled(), "Should be disabled initially");

        // Add to frame
        testFrame.add(panel);
        testFrame.pack();

        // Should still be disabled (no selection)
        assertFalse(editButton.isEnabled(),
                    "Should remain disabled after adding to frame");
    }

    /**
     * Test that Edit button has proper configuration after construction.
     * This verifies all four lines (90, 91, 109, 110) executed successfully.
     */
    @Test
    public void testEditButtonFullConfiguration() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        // Verify line 90: button created with text
        assertNotNull(editButton.getText());
        assertFalse(editButton.getText().isEmpty());

        // Verify line 91: action listener added
        assertTrue(editButton.getActionListeners().length > 0);

        // Verify line 109: tooltip set
        assertNotNull(editButton.getToolTipText());
        assertFalse(editButton.getToolTipText().isEmpty());

        // Verify line 110: button added to panel
        assertSame(panel, editButton.getParent());
    }
}
