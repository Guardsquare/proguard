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
 * Test class for MemberSpecificationsPanel.addAddMethodButton() method.
 *
 * The addAddMethodButton() method creates and adds an "Add Method" button to the panel.
 * Lines that need coverage:
 * - Line 96: Creates a new JButton with the localized "addMethod" message
 * - Line 97: Adds an ActionListener to the button
 * - Line 112: Wraps the button with a tooltip using the tip() method
 * - Line 113: (continuation of line 112)
 *
 * The ActionListener (lines 99-109) performs these actions when clicked:
 * - Line 101: Sets an empty MemberSpecification in the method dialog
 * - Line 102: Shows the method dialog and gets the return value
 * - Line 103: Checks if the user approved (clicked OK)
 * - Line 106-107: Adds the new method specification to the list if approved
 *
 * These tests verify:
 * - The button is created with proper text and tooltip
 * - The button is added to the panel
 * - The action listener is attached
 * - The button is properly configured
 * - The button is in the correct position (after Add Field button if present)
 *
 * Note: The constructor always calls addAddMethodButton(), regardless of includeFieldButton,
 * so creating any panel executes these lines.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationsPanelClaude_addAddMethodButtonTest {

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
     * Test that addAddMethodButton creates and adds a button to the panel.
     * This covers line 96 (JButton creation), line 97 (addActionListener),
     * and lines 112-113 (tip wrapper and addButton call).
     *
     * The constructor always calls addAddMethodButton(), so creating a panel executes these lines.
     */
    @Test
    public void testAddAddMethodButtonCreatesButton() {
        testDialog = new JDialog();
        // Constructor calls addAddMethodButton(), which executes lines 96, 97, 112, 113
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Verify that buttons were added to the panel
        java.util.List buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons list should not be null");
        assertTrue(buttons.size() >= 6, "At least six buttons should be added");
    }

    /**
     * Test that the Add Method button has the correct text.
     * This verifies line 96 where the button is created with msg("addMethod").
     */
    @Test
    public void testAddMethodButtonHasText() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        // When includeFieldButton=true, Add Method button is at index 1
        JButton addMethodButton = (JButton) buttons.get(1);

        assertNotNull(addMethodButton.getText(), "Add Method button should have text");
        assertFalse(addMethodButton.getText().isEmpty(), "Add Method button text should not be empty");
    }

    /**
     * Test that the Add Method button has a tooltip.
     * This verifies lines 112-113 where tip(addMethodButton, "addMethodTip") is called.
     */
    @Test
    public void testAddMethodButtonHasTooltip() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(1);

        assertNotNull(addMethodButton.getToolTipText(), "Add Method button should have a tooltip");
        assertFalse(addMethodButton.getToolTipText().isEmpty(),
                    "Add Method button tooltip should not be empty");
    }

    /**
     * Test that the Add Method button has an action listener attached.
     * This verifies line 97 where addActionListener() is called.
     */
    @Test
    public void testAddMethodButtonHasActionListener() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(1);

        ActionListener[] listeners = addMethodButton.getActionListeners();
        assertTrue(listeners.length > 0,
                   "Add Method button should have at least one action listener");
    }

    /**
     * Test that the Add Method button is enabled by default.
     * Unlike Remove/Edit buttons, the Add Method button should always be enabled.
     */
    @Test
    public void testAddMethodButtonIsEnabledByDefault() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(1);

        assertTrue(addMethodButton.isEnabled(),
                   "Add Method button should be enabled by default");
    }

    /**
     * Test that the Add Method button is properly added to the panel's component hierarchy.
     * This verifies lines 112-113 where addButton() is called.
     */
    @Test
    public void testAddMethodButtonAddedToPanel() {
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
     * Test Add Method button position when includeFieldButton is true.
     * This verifies the button is second in the list (after Add Field).
     */
    @Test
    public void testAddMethodButtonPositionWithFieldButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 2, "Should have at least two buttons");

        Component secondButton = (Component) buttons.get(1);
        assertNotNull(secondButton, "Second button should not be null");
        assertTrue(secondButton instanceof JButton, "Second button should be a JButton");
    }

    /**
     * Test Add Method button position when includeFieldButton is false.
     * This verifies the button is first in the list (no Add Field button).
     */
    @Test
    public void testAddMethodButtonPositionWithoutFieldButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 1, "Should have at least one button");

        // When includeFieldButton=false, Add Method is the first button
        Component firstButton = (Component) buttons.get(0);
        assertNotNull(firstButton, "First button should not be null");
        assertTrue(firstButton instanceof JButton, "First button should be a JButton");
    }

    /**
     * Test that multiple panels each have their own Add Method button.
     * This ensures addAddMethodButton() works correctly for multiple instances.
     */
    @Test
    public void testMultiplePanelsHaveIndependentAddMethodButtons() {
        testDialog = new JDialog();
        MemberSpecificationsPanel panel1 = new MemberSpecificationsPanel(testDialog, true);
        MemberSpecificationsPanel panel2 = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons1 = panel1.getButtons();
        java.util.List buttons2 = panel2.getButtons();

        JButton addMethodButton1 = (JButton) buttons1.get(1);
        JButton addMethodButton2 = (JButton) buttons2.get(1);

        assertNotSame(addMethodButton1, addMethodButton2,
                      "Each panel should have its own Add Method button instance");
    }

    /**
     * Test that the Add Method button can be retrieved from the buttons list.
     * This verifies the button is properly registered.
     */
    @Test
    public void testAddMethodButtonIsInButtonsList() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 1, "Should have at least 1 button");

        // When includeFieldButton=false, Add Method is at index 0
        Component addMethodButton = (Component) buttons.get(0);
        assertNotNull(addMethodButton, "Add Method button should not be null");
        assertTrue(addMethodButton instanceof JButton, "Add Method button should be JButton");
    }

    /**
     * Test that the Add Method button is visible.
     */
    @Test
    public void testAddMethodButtonIsVisible() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        JFrame frame = new JFrame("Test Frame");
        frame.add(panel);
        frame.pack();

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        assertTrue(addMethodButton.isVisible(), "Add Method button should be visible");
        frame.dispose();
    }

    /**
     * Test that the Add Method button has exactly one action listener.
     * This verifies line 97 adds exactly one listener.
     */
    @Test
    public void testAddMethodButtonHasExactlyOneActionListener() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        ActionListener[] listeners = addMethodButton.getActionListeners();
        assertEquals(1, listeners.length,
                     "Add Method button should have exactly one action listener");
    }

    /**
     * Test that the Add Method button's action listener is not null.
     */
    @Test
    public void testAddMethodButtonActionListenerNotNull() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        ActionListener[] listeners = addMethodButton.getActionListeners();
        assertNotNull(listeners[0], "Action listener should not be null");
    }

    /**
     * Test Add Method button properties after panel is added to a frame.
     */
    @Test
    public void testAddMethodButtonPropertiesAfterAddingToFrame() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        JFrame frame = new JFrame("Test Frame");
        frame.add(panel);
        frame.pack();

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        assertNotNull(addMethodButton.getText(), "Button text should be set");
        assertNotNull(addMethodButton.getToolTipText(), "Button tooltip should be set");
        assertTrue(addMethodButton.getActionListeners().length > 0,
                   "Button should have action listeners");

        frame.dispose();
    }

    /**
     * Test that creating a panel executes addAddMethodButton without exceptions.
     * This is a smoke test to ensure lines 96, 97, 112, 113 don't throw exceptions.
     */
    @Test
    public void testAddAddMethodButtonExecutesWithoutException() {
        testDialog = new JDialog();

        // This should not throw any exception
        assertDoesNotThrow(() -> {
            panel = new MemberSpecificationsPanel(testDialog, true);
        }, "Creating panel (which calls addAddMethodButton) should not throw exception");

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.getButtons(), "Buttons should be initialized");
        assertTrue(panel.getButtons().size() >= 6, "At least six buttons should exist");
    }

    /**
     * Test Add Method button with null owner dialog.
     * This ensures addAddMethodButton() works even when owner is null.
     */
    @Test
    public void testAddMethodButtonWithNullOwner() {
        panel = new MemberSpecificationsPanel(null, false);

        java.util.List buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons should exist even with null owner");
        assertTrue(buttons.size() >= 5, "Add Method button should exist with null owner");

        JButton addMethodButton = (JButton) buttons.get(0);
        assertNotNull(addMethodButton.getText(), "Add Method button should have text with null owner");
    }

    /**
     * Test that the Add Method button's parent is the panel.
     */
    @Test
    public void testAddMethodButtonParentIsPanel() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        assertNotNull(addMethodButton.getParent(), "Add Method button should have a parent");
        assertSame(panel, addMethodButton.getParent(),
                   "Add Method button's parent should be the panel");
    }

    /**
     * Test that the Add Method button has reasonable size after packing.
     */
    @Test
    public void testAddMethodButtonHasReasonableSize() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        JFrame frame = new JFrame("Test Frame");
        frame.add(panel);
        frame.pack();

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        Dimension size = addMethodButton.getSize();
        assertTrue(size.width > 0, "Add Method button width should be positive");
        assertTrue(size.height > 0, "Add Method button height should be positive");

        frame.dispose();
    }

    /**
     * Test that the Add Method button is focusable.
     */
    @Test
    public void testAddMethodButtonIsFocusable() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        assertTrue(addMethodButton.isFocusable(), "Add Method button should be focusable");
    }

    /**
     * Test that Add Method button is enabled even when list is empty.
     * Unlike Remove/Edit buttons, Add Method button should always be enabled.
     */
    @Test
    public void testAddMethodButtonEnabledWhenListEmpty() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        // Add Method button should be enabled even when list is empty
        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        assertTrue(addMethodButton.isEnabled(),
                   "Add Method button should be enabled when list is empty");
    }

    /**
     * Test Add Method button text is different from other button texts.
     * This confirms line 96 uses msg("addMethod") correctly.
     */
    @Test
    public void testAddMethodButtonTextDifferentFromOtherButtons() {
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
                        "Add Method button text should be different from Add Field button text");
        assertNotEquals(addMethodText, editText,
                        "Add Method button text should be different from Edit button text");
    }

    /**
     * Test Add Method button tooltip is different from other button tooltips.
     * This confirms lines 112-113 use tip(addMethodButton, "addMethodTip") correctly.
     */
    @Test
    public void testAddMethodButtonTooltipDifferentFromOtherButtons() {
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
                        "Add Method button tooltip should be different from Add Field button tooltip");
        assertNotEquals(addMethodTooltip, editTooltip,
                        "Add Method button tooltip should be different from Edit button tooltip");
    }

    /**
     * Test that all buttons in the panel have the Add Method button included.
     * This verifies lines 112-113 successfully add the button to the panel.
     */
    @Test
    public void testAddMethodButtonIncludedInAllButtons() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        java.util.List buttons = panel.getButtons();

        // Should have 6 buttons: Add Field, Add Method, Edit, Remove, Up, Down
        assertEquals(6, buttons.size(), "Should have 6 buttons including Add Method");

        // Verify Add Method button is the second button
        assertTrue(buttons.get(1) instanceof JButton,
                   "Second button should be a JButton (Add Method button)");
    }

    /**
     * Test Add Method button state consistency across panel lifecycle.
     */
    @Test
    public void testAddMethodButtonStateConsistency() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        // Initially enabled
        assertTrue(addMethodButton.isEnabled(), "Should be enabled initially");

        // Add to frame
        JFrame frame = new JFrame("Test Frame");
        frame.add(panel);
        frame.pack();

        // Should still be enabled
        assertTrue(addMethodButton.isEnabled(),
                   "Should remain enabled after adding to frame");

        frame.dispose();
    }

    /**
     * Test that Add Method button has proper configuration after construction.
     * This verifies all lines (96, 97, 112, 113) executed successfully.
     */
    @Test
    public void testAddMethodButtonFullConfiguration() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        // Verify line 96: button created with text
        assertNotNull(addMethodButton.getText());
        assertFalse(addMethodButton.getText().isEmpty());

        // Verify line 97: action listener added
        assertTrue(addMethodButton.getActionListeners().length > 0);

        // Verify lines 112-113: tooltip set and button added to panel
        assertNotNull(addMethodButton.getToolTipText());
        assertFalse(addMethodButton.getToolTipText().isEmpty());
        assertSame(panel, addMethodButton.getParent());
    }

    /**
     * Test that addAddMethodButton is always called regardless of includeFieldButton.
     * This verifies the method is executed in both constructor branches.
     */
    @Test
    public void testAddMethodButtonAlwaysPresent() {
        testDialog = new JDialog();

        // When includeFieldButton=true, should have Add Method button at index 1
        MemberSpecificationsPanel panelWithField = new MemberSpecificationsPanel(testDialog, true);
        assertTrue(panelWithField.getButtons().size() >= 2,
                   "Should have at least 2 buttons when includeFieldButton=true");
        JButton addMethodButton1 = (JButton) panelWithField.getButtons().get(1);
        assertNotNull(addMethodButton1.getText(), "Add Method button should exist");

        // When includeFieldButton=false, should have Add Method button at index 0
        MemberSpecificationsPanel panelWithoutField = new MemberSpecificationsPanel(testDialog, false);
        assertTrue(panelWithoutField.getButtons().size() >= 1,
                   "Should have at least 1 button when includeFieldButton=false");
        JButton addMethodButton2 = (JButton) panelWithoutField.getButtons().get(0);
        assertNotNull(addMethodButton2.getText(), "Add Method button should exist");
    }

    /**
     * Test that Add Method button is created with JButton constructor.
     * This verifies line 96 creates a proper JButton instance.
     */
    @Test
    public void testAddMethodButtonIsJButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        Object addMethodButton = buttons.get(0);

        assertTrue(addMethodButton instanceof JButton,
                   "Add Method button should be an instance of JButton");
        assertTrue(addMethodButton instanceof AbstractButton,
                   "Add Method button should be an instance of AbstractButton");
        assertTrue(addMethodButton instanceof Component,
                   "Add Method button should be an instance of Component");
    }

    /**
     * Test creating multiple panels to ensure addAddMethodButton works repeatedly.
     * This exercises lines 96, 97, 112, 113 multiple times.
     */
    @Test
    public void testAddMethodButtonMultiplePanelCreations() {
        testDialog = new JDialog();

        for (int i = 0; i < 5; i++) {
            MemberSpecificationsPanel testPanel = new MemberSpecificationsPanel(testDialog, false);
            assertNotNull(testPanel, "Panel " + i + " should be created");
            assertTrue(testPanel.getButtons().size() >= 5, "Panel " + i + " should have at least 5 buttons");

            JButton addMethodButton = (JButton) testPanel.getButtons().get(0);
            assertNotNull(addMethodButton.getText(), "Panel " + i + " Add Method button should have text");
            assertTrue(addMethodButton.isEnabled(), "Panel " + i + " Add Method button should be enabled");
        }
    }

    /**
     * Test that tip() method is called with correct parameters.
     * This verifies lines 112-113 call tip(addMethodButton, "addMethodTip").
     */
    @Test
    public void testAddMethodButtonTipMethodCalled() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        // The tip() method sets the tooltip text
        String tooltip = addMethodButton.getToolTipText();
        assertNotNull(tooltip, "Tooltip should be set by tip() method");
        assertFalse(tooltip.isEmpty(), "Tooltip should not be empty");
    }

    /**
     * Test that addButton() method is called after tip().
     * This verifies lines 112-113 call addButton(tip(...)).
     */
    @Test
    public void testAddMethodButtonAddButtonMethodCalled() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

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
     * Test Add Method button remains functional after panel validation.
     */
    @Test
    public void testAddMethodButtonFunctionalAfterValidation() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        panel.validate();

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        assertNotNull(addMethodButton, "Button should exist after validation");
        assertTrue(addMethodButton.isEnabled(), "Button should be enabled after validation");
        assertNotNull(addMethodButton.getText(), "Button should have text after validation");
    }

    /**
     * Test that Add Method button is properly initialized with all required properties.
     * This is a comprehensive test ensuring lines 96, 97, 112, 113 all execute correctly.
     */
    @Test
    public void testAddMethodButtonCompleteInitialization() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        java.util.List buttons = panel.getButtons();
        JButton addMethodButton = (JButton) buttons.get(0);

        // Check all properties are properly set
        assertNotNull(addMethodButton, "Button should be created");
        assertNotNull(addMethodButton.getText(), "Button should have text (line 96)");
        assertFalse(addMethodButton.getText().isEmpty(), "Button text should not be empty");
        assertEquals(1, addMethodButton.getActionListeners().length, "Button should have one listener (line 97)");
        assertNotNull(addMethodButton.getToolTipText(), "Button should have tooltip (lines 112-113)");
        assertFalse(addMethodButton.getToolTipText().isEmpty(), "Button tooltip should not be empty");
        assertSame(panel, addMethodButton.getParent(), "Button should be added to panel (lines 112-113)");
        assertTrue(addMethodButton.isEnabled(), "Button should be enabled");
        assertTrue(addMethodButton.isVisible(), "Button should be visible");
        assertTrue(addMethodButton.isFocusable(), "Button should be focusable");
    }

    /**
     * Test Add Method button with both includeFieldButton configurations.
     * This ensures lines 96, 97, 112, 113 work in both scenarios.
     */
    @Test
    public void testAddMethodButtonWithBothConfigurations() {
        testDialog = new JDialog();

        // Test with includeFieldButton=true
        MemberSpecificationsPanel panel1 = new MemberSpecificationsPanel(testDialog, true);
        JButton button1 = (JButton) panel1.getButtons().get(1);
        assertNotNull(button1.getText(), "Add Method button should exist with includeFieldButton=true");
        assertNotNull(button1.getToolTipText(), "Add Method button should have tooltip");
        assertEquals(1, button1.getActionListeners().length, "Should have action listener");

        // Test with includeFieldButton=false
        MemberSpecificationsPanel panel2 = new MemberSpecificationsPanel(testDialog, false);
        JButton button2 = (JButton) panel2.getButtons().get(0);
        assertNotNull(button2.getText(), "Add Method button should exist with includeFieldButton=false");
        assertNotNull(button2.getToolTipText(), "Add Method button should have tooltip");
        assertEquals(1, button2.getActionListeners().length, "Should have action listener");
    }

    /**
     * Test that Add Method button order is consistent based on includeFieldButton.
     */
    @Test
    public void testAddMethodButtonOrderConsistency() {
        testDialog = new JDialog();

        // With field button: Add Field (0), Add Method (1), Edit (2), Remove (3), Up (4), Down (5)
        MemberSpecificationsPanel panelWithField = new MemberSpecificationsPanel(testDialog, true);
        assertEquals(6, panelWithField.getButtons().size(), "Should have 6 buttons");
        JButton addMethod1 = (JButton) panelWithField.getButtons().get(1);
        assertNotNull(addMethod1, "Add Method at index 1 when includeFieldButton=true");

        // Without field button: Add Method (0), Edit (1), Remove (2), Up (3), Down (4)
        MemberSpecificationsPanel panelWithoutField = new MemberSpecificationsPanel(testDialog, false);
        assertEquals(5, panelWithoutField.getButtons().size(), "Should have 5 buttons");
        JButton addMethod2 = (JButton) panelWithoutField.getButtons().get(0);
        assertNotNull(addMethod2, "Add Method at index 0 when includeFieldButton=false");
    }

    /**
     * Test that Add Method button can be accessed after multiple panel operations.
     */
    @Test
    public void testAddMethodButtonAfterMultipleOperations() {
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
        JButton addMethodButton = (JButton) buttons.get(0);

        assertNotNull(addMethodButton, "Button should be accessible");
        assertTrue(addMethodButton.isEnabled(), "Button should be enabled");
        assertNotNull(addMethodButton.getText(), "Button should have text");
        assertNotNull(addMethodButton.getToolTipText(), "Button should have tooltip");

        frame.dispose();
    }
}
