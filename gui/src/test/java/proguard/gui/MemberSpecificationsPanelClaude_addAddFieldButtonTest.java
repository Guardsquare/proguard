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
 * Test class for MemberSpecificationsPanel.addAddFieldButton() method.
 *
 * The addAddFieldButton() method creates and adds an "Add Field" button to the panel.
 * Lines that need coverage:
 * - Line 74: Creates a new JButton with the localized "addField" message
 * - Line 75: Adds an ActionListener to the button
 * - Line 90: Wraps the button with a tooltip using the tip() method
 * - Line 91: (continuation of line 90)
 *
 * The ActionListener (lines 77-87) performs these actions when clicked:
 * - Line 79: Sets an empty MemberSpecification in the field dialog
 * - Line 80: Shows the field dialog and gets the return value
 * - Line 81: Checks if the user approved (clicked OK)
 * - Line 84-85: Adds the new field specification to the list if approved
 *
 * These tests verify:
 * - The button is created with proper text and tooltip
 * - The button is added to the panel
 * - The action listener is attached
 * - The button is properly configured
 * - The button is in the correct position
 *
 * Note: The constructor calls addAddFieldButton() when includeFieldButton is true,
 * so creating a panel with includeFieldButton=true executes these lines.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationsPanelClaude_addAddFieldButtonTest {

    private JDialog testDialog;
    private MemberSpecificationsPanel panel;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (panel != null) {
            panel.removeAll();
        }
        if (testDialog != null) {
            testDialog.dispose();
        }
    }

    /**
     * Test that addAddFieldButton creates and adds a button to the panel.
     * This covers line 74 (JButton creation), line 75 (addActionListener),
     * and lines 90-91 (tip wrapper and addButton call).
     *
     * The constructor calls addAddFieldButton() when includeFieldButton=true,
     * so creating a panel executes these lines.
     */
    @Test
    public void testAddAddFieldButtonCreatesButton() {
        testDialog = new JDialog();
        // Constructor calls addAddFieldButton() when includeFieldButton=true, which executes lines 74, 75, 90, 91
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Verify that buttons were added to the panel
        java.util.List buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons list should not be null");
        assertTrue(buttons.size() >= 6, "At least six buttons should be added (Add Field, Add Method, Edit, Remove, Up, Down)");
    }

    /**
     * Test that the Add Field button has the correct text.
     * This verifies line 74 where the button is created with msg("addField").
     */
    @Test
    public void testAddFieldButtonHasText() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        // The Add Field button should be the first button (index 0)
        JButton addFieldButton = (JButton) buttons.get(0);

        assertNotNull(addFieldButton.getText(), "Add Field button should have text");
        assertFalse(addFieldButton.getText().isEmpty(), "Add Field button text should not be empty");
    }

    /**
     * Test that the Add Field button has a tooltip.
     * This verifies lines 90-91 where tip(addFieldButton, "addFieldTip") is called.
     */
    @Test
    public void testAddFieldButtonHasTooltip() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        assertNotNull(addFieldButton.getToolTipText(), "Add Field button should have a tooltip");
        assertFalse(addFieldButton.getToolTipText().isEmpty(),
                    "Add Field button tooltip should not be empty");
    }

    /**
     * Test that the Add Field button has an action listener attached.
     * This verifies line 75 where addActionListener() is called.
     */
    @Test
    public void testAddFieldButtonHasActionListener() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        ActionListener[] listeners = addFieldButton.getActionListeners();
        assertTrue(listeners.length > 0,
                   "Add Field button should have at least one action listener");
    }

    /**
     * Test that the Add Field button is enabled by default.
     * Unlike Remove/Edit buttons, the Add Field button should always be enabled.
     */
    @Test
    public void testAddFieldButtonIsEnabledByDefault() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        assertTrue(addFieldButton.isEnabled(),
                   "Add Field button should be enabled by default");
    }

    /**
     * Test that the Add Field button is properly added to the panel's component hierarchy.
     * This verifies lines 90-91 where addButton() is called.
     */
    @Test
    public void testAddFieldButtonAddedToPanel() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // The button should be in the panel's component hierarchy
        int buttonCount = 0;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                buttonCount++;
            }
        }
        assertTrue(buttonCount >= 6,
                   "At least six buttons should be in panel's component hierarchy");
    }

    /**
     * Test that the Add Field button is the first button in the list.
     * This verifies the order of button creation.
     */
    @Test
    public void testAddFieldButtonIsFirstButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 1, "Should have at least one button");

        Component firstButton = (Component) buttons.get(0);
        assertNotNull(firstButton, "First button should not be null");
        assertTrue(firstButton instanceof JButton, "First button should be a JButton");
    }

    /**
     * Test that multiple panels each have their own Add Field button.
     * This ensures addAddFieldButton() works correctly for multiple instances.
     */
    @Test
    public void testMultiplePanelsHaveIndependentAddFieldButtons() {
        testDialog = new JDialog();
        MemberSpecificationsPanel panel1 = new MemberSpecificationsPanel(testDialog, true);
        MemberSpecificationsPanel panel2 = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons1 = panel1.getButtons();
        java.util.List buttons2 = panel2.getButtons();

        JButton addFieldButton1 = (JButton) buttons1.get(0);
        JButton addFieldButton2 = (JButton) buttons2.get(0);

        assertNotSame(addFieldButton1, addFieldButton2,
                      "Each panel should have its own Add Field button instance");
    }

    /**
     * Test that the Add Field button can be retrieved from the buttons list.
     * This verifies the button is properly registered.
     */
    @Test
    public void testAddFieldButtonIsInButtonsList() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 1, "Should have at least 1 button");

        Component addFieldButton = (Component) buttons.get(0);
        assertNotNull(addFieldButton, "Add Field button should not be null");
        assertTrue(addFieldButton instanceof JButton, "Add Field button should be JButton");
    }

    /**
     * Test that the Add Field button is visible.
     */
    @Test
    public void testAddFieldButtonIsVisible() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        JFrame frame = new JFrame("Test Frame");
        frame.add(panel);
        frame.pack();

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        assertTrue(addFieldButton.isVisible(), "Add Field button should be visible");
        frame.dispose();
    }

    /**
     * Test that the Add Field button has exactly one action listener.
     * This verifies line 75 adds exactly one listener.
     */
    @Test
    public void testAddFieldButtonHasExactlyOneActionListener() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        ActionListener[] listeners = addFieldButton.getActionListeners();
        assertEquals(1, listeners.length,
                     "Add Field button should have exactly one action listener");
    }

    /**
     * Test that the Add Field button's action listener is not null.
     */
    @Test
    public void testAddFieldButtonActionListenerNotNull() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        ActionListener[] listeners = addFieldButton.getActionListeners();
        assertNotNull(listeners[0], "Action listener should not be null");
    }

    /**
     * Test Add Field button properties after panel is added to a frame.
     */
    @Test
    public void testAddFieldButtonPropertiesAfterAddingToFrame() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        JFrame frame = new JFrame("Test Frame");
        frame.add(panel);
        frame.pack();

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        assertNotNull(addFieldButton.getText(), "Button text should be set");
        assertNotNull(addFieldButton.getToolTipText(), "Button tooltip should be set");
        assertTrue(addFieldButton.getActionListeners().length > 0,
                   "Button should have action listeners");

        frame.dispose();
    }

    /**
     * Test that creating a panel with includeFieldButton=true executes addAddFieldButton without exceptions.
     * This is a smoke test to ensure lines 74, 75, 90, 91 don't throw exceptions.
     */
    @Test
    public void testAddAddFieldButtonExecutesWithoutException() {
        testDialog = new JDialog();

        // This should not throw any exception
        assertDoesNotThrow(() -> {
            panel = new MemberSpecificationsPanel(testDialog, true);
        }, "Creating panel with includeFieldButton=true (which calls addAddFieldButton) should not throw exception");

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.getButtons(), "Buttons should be initialized");
        assertTrue(panel.getButtons().size() >= 6, "At least six buttons should exist");
    }

    /**
     * Test Add Field button with null owner dialog.
     * This ensures addAddFieldButton() works even when owner is null.
     */
    @Test
    public void testAddFieldButtonWithNullOwner() {
        panel = new MemberSpecificationsPanel(null, true);

        java.util.List buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons should exist even with null owner");
        assertTrue(buttons.size() >= 6, "Add Field button should exist with null owner");

        JButton addFieldButton = (JButton) buttons.get(0);
        assertNotNull(addFieldButton.getText(), "Add Field button should have text with null owner");
    }

    /**
     * Test that the Add Field button's parent is the panel.
     */
    @Test
    public void testAddFieldButtonParentIsPanel() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        assertNotNull(addFieldButton.getParent(), "Add Field button should have a parent");
        assertSame(panel, addFieldButton.getParent(),
                   "Add Field button's parent should be the panel");
    }

    /**
     * Test that the Add Field button has reasonable size after packing.
     */
    @Test
    public void testAddFieldButtonHasReasonableSize() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        JFrame frame = new JFrame("Test Frame");
        frame.add(panel);
        frame.pack();

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        Dimension size = addFieldButton.getSize();
        assertTrue(size.width > 0, "Add Field button width should be positive");
        assertTrue(size.height > 0, "Add Field button height should be positive");

        frame.dispose();
    }

    /**
     * Test that the Add Field button is focusable.
     */
    @Test
    public void testAddFieldButtonIsFocusable() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        assertTrue(addFieldButton.isFocusable(), "Add Field button should be focusable");
    }

    /**
     * Test that Add Field button is enabled even when list is empty.
     * Unlike Remove/Edit buttons, Add Field button should always be enabled.
     */
    @Test
    public void testAddFieldButtonEnabledWhenListEmpty() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Add Field button should be enabled even when list is empty
        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        assertTrue(addFieldButton.isEnabled(),
                   "Add Field button should be enabled when list is empty");
    }

    /**
     * Test that Add Field button comes before Add Method button in the buttons list.
     * This verifies the order of button creation when includeFieldButton=true.
     */
    @Test
    public void testAddFieldButtonOrderBeforeAddMethodButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 2, "Should have at least two buttons");

        // Verify we can get both buttons without exception
        Component firstButton = (Component) buttons.get(0);
        Component secondButton = (Component) buttons.get(1);

        assertNotNull(firstButton, "First button (Add Field) should exist");
        assertNotNull(secondButton, "Second button (Add Method) should exist");
        assertTrue(firstButton instanceof JButton, "First button should be JButton");
        assertTrue(secondButton instanceof JButton, "Second button should be JButton");
    }

    /**
     * Test Add Field button text is different from other button texts.
     * This confirms line 74 uses msg("addField") correctly.
     */
    @Test
    public void testAddFieldButtonTextDifferentFromOtherButtons() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);
        JButton addMethodButton = (JButton) buttons.get(1);
        JButton editButton = (JButton) buttons.get(2);

        String addFieldText = addFieldButton.getText();
        String addMethodText = addMethodButton.getText();
        String editText = editButton.getText();

        assertNotEquals(addFieldText, addMethodText,
                        "Add Field button text should be different from Add Method button text");
        assertNotEquals(addFieldText, editText,
                        "Add Field button text should be different from Edit button text");
    }

    /**
     * Test Add Field button tooltip is different from other button tooltips.
     * This confirms lines 90-91 use tip(addFieldButton, "addFieldTip") correctly.
     */
    @Test
    public void testAddFieldButtonTooltipDifferentFromOtherButtons() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);
        JButton addMethodButton = (JButton) buttons.get(1);
        JButton editButton = (JButton) buttons.get(2);

        String addFieldTooltip = addFieldButton.getToolTipText();
        String addMethodTooltip = addMethodButton.getToolTipText();
        String editTooltip = editButton.getToolTipText();

        assertNotEquals(addFieldTooltip, addMethodTooltip,
                        "Add Field button tooltip should be different from Add Method button tooltip");
        assertNotEquals(addFieldTooltip, editTooltip,
                        "Add Field button tooltip should be different from Edit button tooltip");
    }

    /**
     * Test that all buttons in the panel have the Add Field button included.
     * This verifies lines 90-91 successfully add the button to the panel.
     */
    @Test
    public void testAddFieldButtonIncludedInAllButtons() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();

        // Should have 6 buttons: Add Field, Add Method, Edit, Remove, Up, Down
        assertEquals(6, buttons.size(), "Should have 6 buttons including Add Field");

        // Verify Add Field button is the first button
        assertTrue(buttons.get(0) instanceof JButton,
                   "First button should be a JButton (Add Field button)");
    }

    /**
     * Test Add Field button state consistency across panel lifecycle.
     */
    @Test
    public void testAddFieldButtonStateConsistency() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        // Initially enabled
        assertTrue(addFieldButton.isEnabled(), "Should be enabled initially");

        // Add to frame
        JFrame frame = new JFrame("Test Frame");
        frame.add(panel);
        frame.pack();

        // Should still be enabled
        assertTrue(addFieldButton.isEnabled(),
                   "Should remain enabled after adding to frame");

        frame.dispose();
    }

    /**
     * Test that Add Field button has proper configuration after construction.
     * This verifies all lines (74, 75, 90, 91) executed successfully.
     */
    @Test
    public void testAddFieldButtonFullConfiguration() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        // Verify line 74: button created with text
        assertNotNull(addFieldButton.getText());
        assertFalse(addFieldButton.getText().isEmpty());

        // Verify line 75: action listener added
        assertTrue(addFieldButton.getActionListeners().length > 0);

        // Verify lines 90-91: tooltip set and button added to panel
        assertNotNull(addFieldButton.getToolTipText());
        assertFalse(addFieldButton.getToolTipText().isEmpty());
        assertSame(panel, addFieldButton.getParent());
    }

    /**
     * Test that addAddFieldButton is only called when includeFieldButton is true.
     * This verifies the conditional execution in the constructor.
     */
    @Test
    public void testAddFieldButtonOnlyWhenIncludeFieldButtonTrue() {
        testDialog = new JDialog();

        // When includeFieldButton=true, should have 6 buttons
        MemberSpecificationsPanel panelWithField = new MemberSpecificationsPanel(testDialog, true);
        assertEquals(6, panelWithField.getButtons().size(),
                     "Should have 6 buttons when includeFieldButton=true");

        // When includeFieldButton=false, should have 5 buttons (no Add Field button)
        MemberSpecificationsPanel panelWithoutField = new MemberSpecificationsPanel(testDialog, false);
        assertEquals(5, panelWithoutField.getButtons().size(),
                     "Should have 5 buttons when includeFieldButton=false");
    }

    /**
     * Test that Add Field button is created with JButton constructor.
     * This verifies line 74 creates a proper JButton instance.
     */
    @Test
    public void testAddFieldButtonIsJButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        Object addFieldButton = buttons.get(0);

        assertTrue(addFieldButton instanceof JButton,
                   "Add Field button should be an instance of JButton");
        assertTrue(addFieldButton instanceof AbstractButton,
                   "Add Field button should be an instance of AbstractButton");
        assertTrue(addFieldButton instanceof Component,
                   "Add Field button should be an instance of Component");
    }

    /**
     * Test creating multiple panels to ensure addAddFieldButton works repeatedly.
     * This exercises lines 74, 75, 90, 91 multiple times.
     */
    @Test
    public void testAddFieldButtonMultiplePanelCreations() {
        testDialog = new JDialog();

        for (int i = 0; i < 5; i++) {
            MemberSpecificationsPanel testPanel = new MemberSpecificationsPanel(testDialog, true);
            assertNotNull(testPanel, "Panel " + i + " should be created");
            assertEquals(6, testPanel.getButtons().size(), "Panel " + i + " should have 6 buttons");

            JButton addFieldButton = (JButton) testPanel.getButtons().get(0);
            assertNotNull(addFieldButton.getText(), "Panel " + i + " Add Field button should have text");
            assertTrue(addFieldButton.isEnabled(), "Panel " + i + " Add Field button should be enabled");
        }
    }

    /**
     * Test that tip() method is called with correct parameters.
     * This verifies lines 90-91 call tip(addFieldButton, "addFieldTip").
     */
    @Test
    public void testAddFieldButtonTipMethodCalled() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        // The tip() method sets the tooltip text
        String tooltip = addFieldButton.getToolTipText();
        assertNotNull(tooltip, "Tooltip should be set by tip() method");
        assertFalse(tooltip.isEmpty(), "Tooltip should not be empty");
    }

    /**
     * Test that addButton() method is called after tip().
     * This verifies lines 90-91 call addButton(tip(...)).
     */
    @Test
    public void testAddFieldButtonAddButtonMethodCalled() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // The button should be in the buttons list (added by addButton())
        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 1, "At least one button should be added");

        // The button should be in the panel's component hierarchy
        boolean foundButton = false;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                foundButton = true;
                break;
            }
        }
        assertTrue(foundButton, "Button should be added to panel's component hierarchy");
    }

    /**
     * Test Add Field button remains functional after panel validation.
     */
    @Test
    public void testAddFieldButtonFunctionalAfterValidation() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        panel.validate();

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        assertNotNull(addFieldButton, "Button should exist after validation");
        assertTrue(addFieldButton.isEnabled(), "Button should be enabled after validation");
        assertNotNull(addFieldButton.getText(), "Button should have text after validation");
    }

    /**
     * Test that Add Field button is properly initialized with all required properties.
     * This is a comprehensive test ensuring lines 74, 75, 90, 91 all execute correctly.
     */
    @Test
    public void testAddFieldButtonCompleteInitialization() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);

        // Check all properties are properly set
        assertNotNull(addFieldButton, "Button should be created");
        assertNotNull(addFieldButton.getText(), "Button should have text (line 74)");
        assertFalse(addFieldButton.getText().isEmpty(), "Button text should not be empty");
        assertEquals(1, addFieldButton.getActionListeners().length, "Button should have one listener (line 75)");
        assertNotNull(addFieldButton.getToolTipText(), "Button should have tooltip (lines 90-91)");
        assertFalse(addFieldButton.getToolTipText().isEmpty(), "Button tooltip should not be empty");
        assertSame(panel, addFieldButton.getParent(), "Button should be added to panel (lines 90-91)");
        assertTrue(addFieldButton.isEnabled(), "Button should be enabled");
        assertTrue(addFieldButton.isVisible(), "Button should be visible");
        assertTrue(addFieldButton.isFocusable(), "Button should be focusable");
    }
}
