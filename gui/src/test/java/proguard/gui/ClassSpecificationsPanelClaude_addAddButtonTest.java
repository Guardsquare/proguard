package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassSpecificationsPanel.addAddButton() method.
 *
 * The addAddButton() method creates and adds an "Add" button to the panel.
 * Lines that need coverage:
 * - Line 68: Creates a new JButton with the localized "add" message
 * - Line 69: Adds an ActionListener to the button
 * - Line 84: Wraps the button with a tooltip using the tip() method
 * - Line 85: Adds the button to the panel using addButton()
 *
 * The ActionListener (lines 71-81) performs these actions when clicked:
 * - Line 73: Sets a new ClassSpecification in the dialog
 * - Line 75: Shows the dialog and captures the return value
 * - Lines 76-80: If user approves, adds the specification to the list
 *
 * These tests verify:
 * - The button is created with proper text and tooltip
 * - The button is added to the panel
 * - The action listener is attached
 * - The button is properly configured
 *
 * Note: Testing the action listener's behavior (lines 71-81) requires simulating
 * user interaction with the dialog, which is complex. These tests focus on
 * verifying that the button creation and setup code (lines 68, 69, 84, 85) is
 * executed by checking the button's properties and presence.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassSpecificationsPanelClaude_addAddButtonTest {

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
     * Test that addAddButton creates and adds a button to the panel.
     * This covers line 68 (JButton creation), line 69 (addActionListener),
     * line 84 (tip wrapper), and line 85 (addButton call).
     *
     * The constructor calls addAddButton(), so creating a panel executes these lines.
     */
    @Test
    public void testAddAddButtonCreatesButton() {
        testFrame = new JFrame("Test Frame");
        // Constructor calls addAddButton(), which executes lines 68, 69, 84, 85
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Verify that buttons were added to the panel
        java.util.List<Component> buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons list should not be null");
        assertTrue(buttons.size() > 0, "At least one button should be added");

        // The first button should be the Add button
        Component firstButton = buttons.get(0);
        assertTrue(firstButton instanceof JButton, "First button should be a JButton");
    }

    /**
     * Test that the Add button has the correct text.
     * This verifies line 68 where the button is created with msg("add").
     */
    @Test
    public void testAddButtonHasText() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        assertNotNull(addButton.getText(), "Add button should have text");
        assertFalse(addButton.getText().isEmpty(), "Add button text should not be empty");
    }

    /**
     * Test that the Add button has a tooltip.
     * This verifies line 84 where tip(addButton, "addTip") is called.
     */
    @Test
    public void testAddButtonHasTooltip() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        assertNotNull(addButton.getToolTipText(), "Add button should have a tooltip");
        assertFalse(addButton.getToolTipText().isEmpty(),
                    "Add button tooltip should not be empty");
    }

    /**
     * Test that the Add button has an action listener attached.
     * This verifies line 69 where addActionListener() is called.
     */
    @Test
    public void testAddButtonHasActionListener() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        ActionListener[] listeners = addButton.getActionListeners();
        assertTrue(listeners.length > 0,
                   "Add button should have at least one action listener");
    }

    /**
     * Test that the Add button is enabled by default.
     * The Add button should be enabled even when no items are selected.
     */
    @Test
    public void testAddButtonIsEnabledByDefault() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        assertTrue(addButton.isEnabled(),
                   "Add button should be enabled by default");
    }

    /**
     * Test that the Add button is properly added to the panel's component hierarchy.
     * This verifies line 85 where addButton() is called.
     */
    @Test
    public void testAddButtonAddedToPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // The button should be in the panel's component hierarchy
        boolean foundButton = false;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                foundButton = true;
                break;
            }
        }
        assertTrue(foundButton,
                   "Add button should be in panel's component hierarchy");
    }

    /**
     * Test that multiple panels each have their own Add button.
     * This ensures addAddButton() works correctly for multiple instances.
     */
    @Test
    public void testMultiplePanelsHaveIndependentAddButtons() {
        testFrame = new JFrame("Test Frame");
        ClassSpecificationsPanel panel1 = new ClassSpecificationsPanel(testFrame, false, true);
        ClassSpecificationsPanel panel2 = new ClassSpecificationsPanel(testFrame, true, false);

        java.util.List<Component> buttons1 = panel1.getButtons();
        java.util.List<Component> buttons2 = panel2.getButtons();

        JButton addButton1 = (JButton) buttons1.get(0);
        JButton addButton2 = (JButton) buttons2.get(0);

        assertNotSame(addButton1, addButton2,
                      "Each panel should have its own Add button instance");
    }

    /**
     * Test that the Add button can be retrieved from the buttons list.
     * This verifies the button is properly registered.
     */
    @Test
    public void testAddButtonIsFirstInButtonsList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        assertTrue(buttons.size() >= 1, "Should have at least the Add button");

        Component firstButton = buttons.get(0);
        assertNotNull(firstButton, "First button should not be null");
        assertTrue(firstButton instanceof JButton, "First button should be JButton");
    }

    /**
     * Test Add button with different panel configurations.
     * This ensures addAddButton() works with different constructor parameters.
     */
    @Test
    public void testAddButtonWithDifferentConfigurations() {
        testFrame = new JFrame("Test Frame");

        // Test with includeKeepSettings=false, includeFieldButton=false
        ClassSpecificationsPanel panel1 = new ClassSpecificationsPanel(testFrame, false, false);
        assertNotNull(panel1.getButtons().get(0), "Add button should exist with (false, false)");

        // Test with includeKeepSettings=true, includeFieldButton=true
        ClassSpecificationsPanel panel2 = new ClassSpecificationsPanel(testFrame, true, true);
        assertNotNull(panel2.getButtons().get(0), "Add button should exist with (true, true)");

        // Test with includeKeepSettings=true, includeFieldButton=false
        ClassSpecificationsPanel panel3 = new ClassSpecificationsPanel(testFrame, true, false);
        assertNotNull(panel3.getButtons().get(0), "Add button should exist with (true, false)");
    }

    /**
     * Test that the Add button is visible.
     */
    @Test
    public void testAddButtonIsVisible() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        assertTrue(addButton.isVisible(), "Add button should be visible");
    }

    /**
     * Test that the Add button has exactly one action listener.
     * This verifies line 69 adds exactly one listener.
     */
    @Test
    public void testAddButtonHasExactlyOneActionListener() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        ActionListener[] listeners = addButton.getActionListeners();
        assertEquals(1, listeners.length,
                     "Add button should have exactly one action listener");
    }

    /**
     * Test that the Add button's action listener is not null.
     */
    @Test
    public void testAddButtonActionListenerNotNull() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        ActionListener[] listeners = addButton.getActionListeners();
        assertNotNull(listeners[0], "Action listener should not be null");
    }

    /**
     * Test Add button properties after panel is added to a frame.
     */
    @Test
    public void testAddButtonPropertiesAfterAddingToFrame() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        assertNotNull(addButton.getText(), "Button text should be set");
        assertNotNull(addButton.getToolTipText(), "Button tooltip should be set");
        assertTrue(addButton.isEnabled(), "Button should be enabled");
        assertTrue(addButton.getActionListeners().length > 0,
                   "Button should have action listeners");
    }

    /**
     * Test that creating a panel executes addAddButton without exceptions.
     * This is a smoke test to ensure lines 68, 69, 84, 85 don't throw exceptions.
     */
    @Test
    public void testAddAddButtonExecutesWithoutException() {
        testFrame = new JFrame("Test Frame");

        // This should not throw any exception
        assertDoesNotThrow(() -> {
            panel = new ClassSpecificationsPanel(testFrame, false, true);
        }, "Creating panel (which calls addAddButton) should not throw exception");

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.getButtons(), "Buttons should be initialized");
        assertTrue(panel.getButtons().size() > 0, "At least one button should exist");
    }

    /**
     * Test Add button with null owner frame.
     * This ensures addAddButton() works even when owner is null.
     */
    @Test
    public void testAddButtonWithNullOwner() {
        panel = new ClassSpecificationsPanel(null, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons should exist even with null owner");
        assertTrue(buttons.size() > 0, "Add button should exist with null owner");

        JButton addButton = (JButton) buttons.get(0);
        assertNotNull(addButton.getText(), "Add button should have text with null owner");
        assertTrue(addButton.isEnabled(), "Add button should be enabled with null owner");
    }

    /**
     * Test that the Add button's parent is the panel.
     */
    @Test
    public void testAddButtonParentIsPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        assertNotNull(addButton.getParent(), "Add button should have a parent");
        assertSame(panel, addButton.getParent(),
                   "Add button's parent should be the panel");
    }

    /**
     * Test that the Add button has reasonable size after packing.
     */
    @Test
    public void testAddButtonHasReasonableSize() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        Dimension size = addButton.getSize();
        assertTrue(size.width > 0, "Add button width should be positive");
        assertTrue(size.height > 0, "Add button height should be positive");
    }

    /**
     * Test that the Add button is focusable.
     */
    @Test
    public void testAddButtonIsFocusable() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        assertTrue(addButton.isFocusable(), "Add button should be focusable");
    }

    /**
     * Test that the Add button remains enabled after panel initialization.
     * Unlike edit/remove buttons, the Add button should always be enabled.
     */
    @Test
    public void testAddButtonRemainsEnabledAfterInitialization() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Add button should be enabled even though list is empty
        java.util.List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        assertTrue(addButton.isEnabled(),
                   "Add button should remain enabled after initialization");

        // Verify other buttons are disabled (for comparison)
        if (buttons.size() > 1) {
            JButton secondButton = (JButton) buttons.get(1);
            assertFalse(secondButton.isEnabled(),
                        "Other buttons should be disabled when list is empty");
        }
    }
}
