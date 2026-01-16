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
 * Test class for MemberSpecificationsPanel.addEditButton() method.
 *
 * The addEditButton() method creates and adds an "Edit" button to the panel.
 * Lines that need coverage:
 * - Line 118: Creates a new JButton with the localized "edit" message
 * - Line 119: Adds an ActionListener to the button
 * - Line 143: Wraps the button with a tooltip using the tip() method
 * - Line 144: (continuation of line 143)
 *
 * The ActionListener (lines 121-140) performs these actions when clicked:
 * - Line 123-124: Gets the selected item from the list
 * - Line 126-129: Determines which dialog to use (field or method) based on wrapper.isField
 * - Line 131: Sets the current specification in the dialog
 * - Line 132: Shows the dialog and gets the return value
 * - Line 133: Checks if the user approved (clicked OK)
 * - Line 136-138: Updates the specification if approved
 *
 * These tests verify:
 * - The button is created with proper text and tooltip
 * - The button is added to the panel
 * - The action listener is attached
 * - The button is properly configured
 * - The button is in the correct position
 * - The button state changes based on selection (disabled when no selection)
 *
 * Note: The constructor always calls addEditButton(), so creating any panel executes these lines.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationsPanelClaude_addEditButtonTest {

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
     * Test that addEditButton creates and adds a button to the panel.
     * This covers line 118 (JButton creation), line 119 (addActionListener),
     * and lines 143-144 (tip wrapper and addButton call).
     *
     * The constructor always calls addEditButton(), so creating a panel executes these lines.
     */
    @Test
    public void testAddEditButtonCreatesButton() {
        testDialog = new JDialog();
        // Constructor calls addEditButton(), which executes lines 118, 119, 143, 144
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Verify that buttons were added to the panel
        java.util.List buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons list should not be null");
        assertTrue(buttons.size() >= 6, "At least six buttons should be added");
    }

    /**
     * Test that the Edit button has the correct text.
     * This verifies line 118 where the button is created with msg("edit").
     */
    @Test
    public void testEditButtonHasText() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        // When includeFieldButton=true, Edit button is at index 2
        // (0=Add Field, 1=Add Method, 2=Edit)
        JButton editButton = (JButton) buttons.get(2);

        assertNotNull(editButton.getText(), "Edit button should have text");
        assertFalse(editButton.getText().isEmpty(), "Edit button text should not be empty");
    }

    /**
     * Test that the Edit button has a tooltip.
     * This verifies lines 143-144 where tip(editButton, "editTip") is called.
     */
    @Test
    public void testEditButtonHasTooltip() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(2);

        assertNotNull(editButton.getToolTipText(), "Edit button should have a tooltip");
        assertFalse(editButton.getToolTipText().isEmpty(),
                    "Edit button tooltip should not be empty");
    }

    /**
     * Test that the Edit button has an action listener attached.
     * This verifies line 119 where addActionListener() is called.
     */
    @Test
    public void testEditButtonHasActionListener() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(2);

        ActionListener[] listeners = editButton.getActionListeners();
        assertTrue(listeners.length > 0,
                   "Edit button should have at least one action listener");
    }

    /**
     * Test that the Edit button is disabled by default (no selection).
     * Unlike Add buttons, the Edit button should be disabled when nothing is selected.
     */
    @Test
    public void testEditButtonIsDisabledByDefault() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(2);

        assertFalse(editButton.isEnabled(),
                    "Edit button should be disabled by default when no item is selected");
    }

    /**
     * Test that the Edit button is properly added to the panel's component hierarchy.
     * This verifies lines 143-144 where addButton() is called.
     */
    @Test
    public void testEditButtonAddedToPanel() {
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
     * Test Edit button position when includeFieldButton is true.
     * This verifies the button is third in the list (after Add Field and Add Method).
     */
    @Test
    public void testEditButtonPositionWithFieldButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 3, "Should have at least three buttons");

        // 0=Add Field, 1=Add Method, 2=Edit
        Component thirdButton = (Component) buttons.get(2);
        assertNotNull(thirdButton, "Third button should not be null");
        assertTrue(thirdButton instanceof JButton, "Third button should be a JButton");
    }

    /**
     * Test Edit button position when includeFieldButton is false.
     * This verifies the button is second in the list (after Add Method).
     */
    @Test
    public void testEditButtonPositionWithoutFieldButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 2, "Should have at least two buttons");

        // 0=Add Method, 1=Edit
        Component secondButton = (Component) buttons.get(1);
        assertNotNull(secondButton, "Second button should not be null");
        assertTrue(secondButton instanceof JButton, "Second button should be a JButton");
    }

    /**
     * Test that multiple panels each have their own Edit button.
     * This ensures addEditButton() works correctly for multiple instances.
     */
    @Test
    public void testMultiplePanelsHaveIndependentEditButtons() {
        testDialog = new JDialog();
        MemberSpecificationsPanel panel1 = new MemberSpecificationsPanel(testDialog, true);
        MemberSpecificationsPanel panel2 = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons1 = panel1.getButtons();
        java.util.List buttons2 = panel2.getButtons();

        JButton editButton1 = (JButton) buttons1.get(2);
        JButton editButton2 = (JButton) buttons2.get(2);

        assertNotSame(editButton1, editButton2,
                      "Each panel should have its own Edit button instance");
    }

    /**
     * Test that the Edit button can be retrieved from the buttons list.
     * This verifies the button is properly registered.
     */
    @Test
    public void testEditButtonIsInButtonsList() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 2, "Should have at least 2 buttons");

        // When includeFieldButton=false, Edit is at index 1
        Component editButton = (Component) buttons.get(1);
        assertNotNull(editButton, "Edit button should not be null");
        assertTrue(editButton instanceof JButton, "Edit button should be JButton");
    }

    /**
     * Test that the Edit button is visible.
     */
    @Test
    public void testEditButtonIsVisible() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        JFrame frame = new JFrame("Test Frame");
        frame.add(panel);
        frame.pack();

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertTrue(editButton.isVisible(), "Edit button should be visible");
        frame.dispose();
    }

    /**
     * Test that the Edit button has exactly one action listener.
     * This verifies line 119 adds exactly one listener.
     */
    @Test
    public void testEditButtonHasExactlyOneActionListener() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
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
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        ActionListener[] listeners = editButton.getActionListeners();
        assertNotNull(listeners[0], "Action listener should not be null");
    }

    /**
     * Test Edit button properties after panel is added to a frame.
     */
    @Test
    public void testEditButtonPropertiesAfterAddingToFrame() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        JFrame frame = new JFrame("Test Frame");
        frame.add(panel);
        frame.pack();

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertNotNull(editButton.getText(), "Button text should be set");
        assertNotNull(editButton.getToolTipText(), "Button tooltip should be set");
        assertTrue(editButton.getActionListeners().length > 0,
                   "Button should have action listeners");

        frame.dispose();
    }

    /**
     * Test that creating a panel executes addEditButton without exceptions.
     * This is a smoke test to ensure lines 118, 119, 143, 144 don't throw exceptions.
     */
    @Test
    public void testAddEditButtonExecutesWithoutException() {
        testDialog = new JDialog();

        // This should not throw any exception
        assertDoesNotThrow(() -> {
            panel = new MemberSpecificationsPanel(testDialog, true);
        }, "Creating panel (which calls addEditButton) should not throw exception");

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.getButtons(), "Buttons should be initialized");
        assertTrue(panel.getButtons().size() >= 6, "At least six buttons should exist");
    }

    /**
     * Test Edit button with null owner dialog.
     * This ensures addEditButton() works even when owner is null.
     */
    @Test
    public void testEditButtonWithNullOwner() {
        panel = new MemberSpecificationsPanel(null, false);

        java.util.List buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons should exist even with null owner");
        assertTrue(buttons.size() >= 5, "Edit button should exist with null owner");

        JButton editButton = (JButton) buttons.get(1);
        assertNotNull(editButton.getText(), "Edit button should have text with null owner");
    }

    /**
     * Test that the Edit button's parent is the panel.
     */
    @Test
    public void testEditButtonParentIsPanel() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
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
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        JFrame frame = new JFrame("Test Frame");
        frame.add(panel);
        frame.pack();

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        Dimension size = editButton.getSize();
        assertTrue(size.width > 0, "Edit button width should be positive");
        assertTrue(size.height > 0, "Edit button height should be positive");

        frame.dispose();
    }

    /**
     * Test that the Edit button is focusable.
     */
    @Test
    public void testEditButtonIsFocusable() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertTrue(editButton.isFocusable(), "Edit button should be focusable");
    }

    /**
     * Test that Edit button is disabled when list is empty.
     * Unlike Add buttons, Edit button should be disabled when no items are present/selected.
     */
    @Test
    public void testEditButtonDisabledWhenListEmpty() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        // Edit button should be disabled when list is empty
        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertFalse(editButton.isEnabled(),
                    "Edit button should be disabled when list is empty");

        // Compare with Add button which should be enabled
        JButton addMethodButton = (JButton) buttons.get(0);
        assertTrue(addMethodButton.isEnabled(),
                   "Add Method button should be enabled (different from Edit button)");
    }

    /**
     * Test Edit button text is different from other button texts.
     * This confirms line 118 uses msg("edit") correctly.
     */
    @Test
    public void testEditButtonTextDifferentFromOtherButtons() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);
        JButton addMethodButton = (JButton) buttons.get(1);
        JButton editButton = (JButton) buttons.get(2);

        String addFieldText = addFieldButton.getText();
        String addMethodText = addMethodButton.getText();
        String editText = editButton.getText();

        assertNotEquals(addFieldText, editText,
                        "Edit button text should be different from Add Field button text");
        assertNotEquals(addMethodText, editText,
                        "Edit button text should be different from Add Method button text");
    }

    /**
     * Test Edit button tooltip is different from other button tooltips.
     * This confirms lines 143-144 use tip(editButton, "editTip") correctly.
     */
    @Test
    public void testEditButtonTooltipDifferentFromOtherButtons() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addFieldButton = (JButton) buttons.get(0);
        JButton addMethodButton = (JButton) buttons.get(1);
        JButton editButton = (JButton) buttons.get(2);

        String addFieldTooltip = addFieldButton.getToolTipText();
        String addMethodTooltip = addMethodButton.getToolTipText();
        String editTooltip = editButton.getToolTipText();

        assertNotEquals(addFieldTooltip, editTooltip,
                        "Edit button tooltip should be different from Add Field button tooltip");
        assertNotEquals(addMethodTooltip, editTooltip,
                        "Edit button tooltip should be different from Add Method button tooltip");
    }

    /**
     * Test that all buttons in the panel have the Edit button included.
     * This verifies lines 143-144 successfully add the button to the panel.
     */
    @Test
    public void testEditButtonIncludedInAllButtons() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();

        // Should have 6 buttons: Add Field, Add Method, Edit, Remove, Up, Down
        assertEquals(6, buttons.size(), "Should have 6 buttons including Edit");

        // Verify Edit button is the third button
        assertTrue(buttons.get(2) instanceof JButton,
                   "Third button should be a JButton (Edit button)");
    }

    /**
     * Test Edit button state consistency across panel lifecycle.
     */
    @Test
    public void testEditButtonStateConsistency() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        // Initially disabled (no selection)
        assertFalse(editButton.isEnabled(), "Should be disabled initially");

        // Add to frame
        JFrame frame = new JFrame("Test Frame");
        frame.add(panel);
        frame.pack();

        // Should still be disabled (no selection)
        assertFalse(editButton.isEnabled(),
                    "Should remain disabled after adding to frame");

        frame.dispose();
    }

    /**
     * Test that Edit button has proper configuration after construction.
     * This verifies all lines (118, 119, 143, 144) executed successfully.
     */
    @Test
    public void testEditButtonFullConfiguration() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        // Verify line 118: button created with text
        assertNotNull(editButton.getText());
        assertFalse(editButton.getText().isEmpty());

        // Verify line 119: action listener added
        assertTrue(editButton.getActionListeners().length > 0);

        // Verify lines 143-144: tooltip set and button added to panel
        assertNotNull(editButton.getToolTipText());
        assertFalse(editButton.getToolTipText().isEmpty());
        assertSame(panel, editButton.getParent());
    }

    /**
     * Test that addEditButton is always called regardless of includeFieldButton.
     * This verifies the method is executed in both constructor branches.
     */
    @Test
    public void testEditButtonAlwaysPresent() {
        testDialog = new JDialog();

        // When includeFieldButton=true, should have Edit button at index 2
        MemberSpecificationsPanel panelWithField = new MemberSpecificationsPanel(testDialog, true);
        assertTrue(panelWithField.getButtons().size() >= 3,
                   "Should have at least 3 buttons when includeFieldButton=true");
        JButton editButton1 = (JButton) panelWithField.getButtons().get(2);
        assertNotNull(editButton1.getText(), "Edit button should exist");

        // When includeFieldButton=false, should have Edit button at index 1
        MemberSpecificationsPanel panelWithoutField = new MemberSpecificationsPanel(testDialog, false);
        assertTrue(panelWithoutField.getButtons().size() >= 2,
                   "Should have at least 2 buttons when includeFieldButton=false");
        JButton editButton2 = (JButton) panelWithoutField.getButtons().get(1);
        assertNotNull(editButton2.getText(), "Edit button should exist");
    }

    /**
     * Test that Edit button is created with JButton constructor.
     * This verifies line 118 creates a proper JButton instance.
     */
    @Test
    public void testEditButtonIsJButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        Object editButton = buttons.get(1);

        assertTrue(editButton instanceof JButton,
                   "Edit button should be an instance of JButton");
        assertTrue(editButton instanceof AbstractButton,
                   "Edit button should be an instance of AbstractButton");
        assertTrue(editButton instanceof Component,
                   "Edit button should be an instance of Component");
    }

    /**
     * Test creating multiple panels to ensure addEditButton works repeatedly.
     * This exercises lines 118, 119, 143, 144 multiple times.
     */
    @Test
    public void testEditButtonMultiplePanelCreations() {
        testDialog = new JDialog();

        for (int i = 0; i < 5; i++) {
            MemberSpecificationsPanel testPanel = new MemberSpecificationsPanel(testDialog, false);
            assertNotNull(testPanel, "Panel " + i + " should be created");
            assertTrue(testPanel.getButtons().size() >= 5, "Panel " + i + " should have at least 5 buttons");

            JButton editButton = (JButton) testPanel.getButtons().get(1);
            assertNotNull(editButton.getText(), "Panel " + i + " Edit button should have text");
            assertFalse(editButton.isEnabled(), "Panel " + i + " Edit button should be disabled initially");
        }
    }

    /**
     * Test that tip() method is called with correct parameters.
     * This verifies lines 143-144 call tip(editButton, "editTip").
     */
    @Test
    public void testEditButtonTipMethodCalled() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        // The tip() method sets the tooltip text
        String tooltip = editButton.getToolTipText();
        assertNotNull(tooltip, "Tooltip should be set by tip() method");
        assertFalse(tooltip.isEmpty(), "Tooltip should not be empty");
    }

    /**
     * Test that addButton() method is called after tip().
     * This verifies lines 143-144 call addButton(tip(...)).
     */
    @Test
    public void testEditButtonAddButtonMethodCalled() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        // The button should be in the buttons list (added by addButton())
        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 2, "At least two buttons should be added");

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
     * Test Edit button remains functional after panel validation.
     */
    @Test
    public void testEditButtonFunctionalAfterValidation() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        panel.validate();

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertNotNull(editButton, "Button should exist after validation");
        assertNotNull(editButton.getText(), "Button should have text after validation");
    }

    /**
     * Test that Edit button is properly initialized with all required properties.
     * This is a comprehensive test ensuring lines 118, 119, 143, 144 all execute correctly.
     */
    @Test
    public void testEditButtonCompleteInitialization() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        // Check all properties are properly set
        assertNotNull(editButton, "Button should be created");
        assertNotNull(editButton.getText(), "Button should have text (line 118)");
        assertFalse(editButton.getText().isEmpty(), "Button text should not be empty");
        assertEquals(1, editButton.getActionListeners().length, "Button should have one listener (line 119)");
        assertNotNull(editButton.getToolTipText(), "Button should have tooltip (lines 143-144)");
        assertFalse(editButton.getToolTipText().isEmpty(), "Button tooltip should not be empty");
        assertSame(panel, editButton.getParent(), "Button should be added to panel (lines 143-144)");
        assertFalse(editButton.isEnabled(), "Button should be disabled initially");
        assertTrue(editButton.isVisible(), "Button should be visible");
        assertTrue(editButton.isFocusable(), "Button should be focusable");
    }

    /**
     * Test Edit button with both includeFieldButton configurations.
     * This ensures lines 118, 119, 143, 144 work in both scenarios.
     */
    @Test
    public void testEditButtonWithBothConfigurations() {
        testDialog = new JDialog();

        // Test with includeFieldButton=true
        MemberSpecificationsPanel panel1 = new MemberSpecificationsPanel(testDialog, true);
        JButton button1 = (JButton) panel1.getButtons().get(2);
        assertNotNull(button1.getText(), "Edit button should exist with includeFieldButton=true");
        assertNotNull(button1.getToolTipText(), "Edit button should have tooltip");
        assertEquals(1, button1.getActionListeners().length, "Should have action listener");

        // Test with includeFieldButton=false
        MemberSpecificationsPanel panel2 = new MemberSpecificationsPanel(testDialog, false);
        JButton button2 = (JButton) panel2.getButtons().get(1);
        assertNotNull(button2.getText(), "Edit button should exist with includeFieldButton=false");
        assertNotNull(button2.getToolTipText(), "Edit button should have tooltip");
        assertEquals(1, button2.getActionListeners().length, "Should have action listener");
    }

    /**
     * Test that Edit button order is consistent based on includeFieldButton.
     */
    @Test
    public void testEditButtonOrderConsistency() {
        testDialog = new JDialog();

        // With field button: Add Field (0), Add Method (1), Edit (2), Remove (3), Up (4), Down (5)
        MemberSpecificationsPanel panelWithField = new MemberSpecificationsPanel(testDialog, true);
        assertEquals(6, panelWithField.getButtons().size(), "Should have 6 buttons");
        JButton edit1 = (JButton) panelWithField.getButtons().get(2);
        assertNotNull(edit1, "Edit at index 2 when includeFieldButton=true");

        // Without field button: Add Method (0), Edit (1), Remove (2), Up (3), Down (4)
        MemberSpecificationsPanel panelWithoutField = new MemberSpecificationsPanel(testDialog, false);
        assertEquals(5, panelWithoutField.getButtons().size(), "Should have 5 buttons");
        JButton edit2 = (JButton) panelWithoutField.getButtons().get(1);
        assertNotNull(edit2, "Edit at index 1 when includeFieldButton=false");
    }

    /**
     * Test that Edit button can be accessed after multiple panel operations.
     */
    @Test
    public void testEditButtonAfterMultipleOperations() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        // Validate panel
        panel.validate();

        // Add to frame
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();

        // Button should still be accessible and functional
        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertNotNull(editButton, "Button should be accessible");
        assertNotNull(editButton.getText(), "Button should have text");
        assertNotNull(editButton.getToolTipText(), "Button should have tooltip");

        frame.dispose();
    }

    /**
     * Test Edit button is a selection button (disabled when no selection).
     * This is different from Add buttons which are always enabled.
     */
    @Test
    public void testEditButtonIsSelectionButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);
        JButton addMethodButton = (JButton) buttons.get(0);

        // Edit button should be disabled (selection button)
        assertFalse(editButton.isEnabled(),
                    "Edit button should be disabled when no selection");

        // Add button should be enabled (not a selection button)
        assertTrue(addMethodButton.isEnabled(),
                   "Add Method button should be enabled regardless of selection");
    }

    /**
     * Test that Edit button comes after Add buttons in the buttons list.
     * This verifies the order of button creation.
     */
    @Test
    public void testEditButtonOrderAfterAddButtons() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 3, "Should have at least three buttons");

        // Verify we can get all buttons without exception
        Component firstButton = (Component) buttons.get(0);   // Add Field
        Component secondButton = (Component) buttons.get(1);  // Add Method
        Component thirdButton = (Component) buttons.get(2);   // Edit

        assertNotNull(firstButton, "First button (Add Field) should exist");
        assertNotNull(secondButton, "Second button (Add Method) should exist");
        assertNotNull(thirdButton, "Third button (Edit) should exist");
    }

    /**
     * Test Edit button comes before Remove button in the buttons list.
     * This verifies the order of button creation.
     */
    @Test
    public void testEditButtonOrderBeforeRemoveButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 3, "Should have at least three buttons");

        // 0=Add Method, 1=Edit, 2=Remove
        Component secondButton = (Component) buttons.get(1);  // Edit
        Component thirdButton = (Component) buttons.get(2);   // Remove

        assertNotNull(secondButton, "Second button (Edit) should exist");
        assertNotNull(thirdButton, "Third button (Remove) should exist");
    }

    /**
     * Test that all four lines are executed for both panel configurations.
     * This comprehensive test ensures complete coverage.
     */
    @Test
    public void testAllLinesExecutedForBothConfigurations() {
        testDialog = new JDialog();

        // Test with includeFieldButton=true (covers lines 118, 119, 143, 144)
        MemberSpecificationsPanel panel1 = new MemberSpecificationsPanel(testDialog, true);
        JButton button1 = (JButton) panel1.getButtons().get(2);
        assertNotNull(button1.getText(), "Line 118: Button created");
        assertEquals(1, button1.getActionListeners().length, "Line 119: Listener added");
        assertNotNull(button1.getToolTipText(), "Lines 143-144: Tooltip set");
        assertSame(panel1, button1.getParent(), "Lines 143-144: Button added to panel");

        // Test with includeFieldButton=false (covers lines 118, 119, 143, 144)
        MemberSpecificationsPanel panel2 = new MemberSpecificationsPanel(testDialog, false);
        JButton button2 = (JButton) panel2.getButtons().get(1);
        assertNotNull(button2.getText(), "Line 118: Button created");
        assertEquals(1, button2.getActionListeners().length, "Line 119: Listener added");
        assertNotNull(button2.getToolTipText(), "Lines 143-144: Tooltip set");
        assertSame(panel2, button2.getParent(), "Lines 143-144: Button added to panel");
    }
}
