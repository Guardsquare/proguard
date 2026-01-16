package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassPath;
import proguard.ClassPathEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ListPanel.addUpButton() method.
 *
 * The addUpButton() method creates and adds an "Up" button to the panel.
 * Lines that need coverage:
 * - Line 99: Creates a new JButton with the localized "moveUp" message
 * - Line 100: Adds an ActionListener to the button
 * - Lines 102-111: ActionListener's actionPerformed method that:
 *   - Line 104: Gets selected indices from the list
 *   - Lines 105-106: Checks if selection exists and first element is not at position 0
 *   - Line 109: Moves selected elements up by calling moveElementsAt(selectedIndices, -1)
 * - Line 114: Wraps the button with a tooltip using tip() method and adds it via addButton()
 *
 * These tests verify:
 * - The button is created with proper text and tooltip
 * - The button is added to the panel
 * - The action listener is attached and functional
 * - The button correctly moves items up in the list
 * - The button handles edge cases (first item, multiple selections, empty list)
 * - The button is properly configured and enabled/disabled based on selection state
 *
 * Note: Since ListPanel is abstract, we test it through the concrete
 * ClassPathPanel class which extends ListPanel and calls addUpButton()
 * in its constructor.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ListPanelClaude_addUpButtonTest {

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
     * Test that addUpButton creates and adds a button to the panel.
     * This covers line 99 (JButton creation), line 100 (addActionListener),
     * and line 114 (tip wrapper and addButton call).
     *
     * The constructor calls addUpButton(), so creating a panel executes these lines.
     */
    @Test
    public void testAddUpButtonCreatesButton() {
        testFrame = new JFrame("Test Frame");
        // Constructor calls addUpButton(), which executes lines 99, 100, 114
        panel = new ClassPathPanel(testFrame, false);

        // Verify that buttons were added to the panel
        java.util.List buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons list should not be null");
        assertTrue(buttons.size() >= 5, "At least five buttons should be added (Add, Edit, Filter, Remove, Up, ...)");

        // The fifth button should be the Up button (0=Add, 1=Edit, 2=Filter, 3=Remove, 4=Up)
        assertTrue(buttons.size() > 4, "Should have at least 5 buttons");
        Component fifthButton = (Component) buttons.get(4);
        assertTrue(fifthButton instanceof JButton, "Fifth button should be a JButton");
    }

    /**
     * Test that the Up button has the correct text.
     * This verifies line 99 where the button is created with msg("moveUp").
     */
    @Test
    public void testUpButtonHasText() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        assertNotNull(upButton.getText(), "Up button should have text");
        assertFalse(upButton.getText().isEmpty(), "Up button text should not be empty");
    }

    /**
     * Test that the Up button has a tooltip.
     * This verifies line 114 where tip(upButton, "moveUpTip") is called.
     */
    @Test
    public void testUpButtonHasTooltip() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        assertNotNull(upButton.getToolTipText(), "Up button should have a tooltip");
        assertFalse(upButton.getToolTipText().isEmpty(),
                    "Up button tooltip should not be empty");
    }

    /**
     * Test that the Up button has an action listener attached.
     * This verifies line 100 where addActionListener() is called.
     */
    @Test
    public void testUpButtonHasActionListener() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        ActionListener[] listeners = upButton.getActionListeners();
        assertTrue(listeners.length > 0,
                   "Up button should have at least one action listener");
    }

    /**
     * Test that the Up button is disabled by default when no items are selected.
     * The Up button should be disabled when no items are in the list.
     */
    @Test
    public void testUpButtonIsDisabledByDefault() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        assertFalse(upButton.isEnabled(),
                    "Up button should be disabled by default when no items are selected");
    }

    /**
     * Test that the Up button's action listener moves items up correctly.
     * This tests lines 104-109 of the actionPerformed method.
     */
    @Test
    public void testUpButtonMovesItemUp() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with multiple entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        classPath.add(new ClassPathEntry(new File("third.jar"), false));

        // Set the class path in the panel
        panel.setClassPath(classPath);

        // Get the list model to verify state
        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        assertEquals(3, listModel.getSize(), "Should have 3 items");

        // Select the second item (index 1)
        panel.list.setSelectedIndex(1);

        // Get the Up button and trigger it
        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        // The button should be enabled since an item is selected
        assertTrue(upButton.isEnabled(), "Up button should be enabled when item is selected");

        // Click the Up button
        upButton.doClick();

        // Verify the second item moved to first position
        ClassPathEntry firstEntry = (ClassPathEntry) listModel.getElementAt(0);
        assertEquals("second.jar", firstEntry.getName(),
                     "Second item should have moved to first position");

        ClassPathEntry secondEntry = (ClassPathEntry) listModel.getElementAt(1);
        assertEquals("first.jar", secondEntry.getName(),
                     "First item should have moved to second position");

        // Verify selection moved with the item
        assertEquals(0, panel.list.getSelectedIndex(),
                     "Selection should have moved with the item");
    }

    /**
     * Test that the Up button does not move the first item.
     * This tests lines 105-106 checking if selectedIndices[0] > 0.
     */
    @Test
    public void testUpButtonDoesNotMoveFirstItem() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with multiple entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));

        panel.setClassPath(classPath);

        // Select the first item (index 0)
        panel.list.setSelectedIndex(0);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Click the Up button
        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Verify the order hasn't changed
        ClassPathEntry firstEntry = (ClassPathEntry) listModel.getElementAt(0);
        assertEquals("first.jar", firstEntry.getName(),
                     "First item should remain in first position");

        ClassPathEntry secondEntry = (ClassPathEntry) listModel.getElementAt(1);
        assertEquals("second.jar", secondEntry.getName(),
                     "Second item should remain in second position");
    }

    /**
     * Test that the Up button correctly handles multiple selected items.
     * This tests line 104 which gets all selected indices.
     */
    @Test
    public void testUpButtonMovesMultipleItemsUp() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with multiple entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        classPath.add(new ClassPathEntry(new File("third.jar"), false));
        classPath.add(new ClassPathEntry(new File("fourth.jar"), false));

        panel.setClassPath(classPath);

        // Select items at indices 2 and 3 (third and fourth)
        panel.list.setSelectedIndices(new int[]{2, 3});

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Click the Up button
        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Verify the items moved up together
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("third.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName(),
                     "Third item should have moved up to second position");
        assertEquals("fourth.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName(),
                     "Fourth item should have moved up to third position");
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(3)).getName());
    }

    /**
     * Test that the Up button does nothing when no items are selected.
     * This tests line 105 checking if selectedIndices.length > 0.
     */
    @Test
    public void testUpButtonDoesNothingWhenNoSelection() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));

        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Don't select anything
        panel.list.clearSelection();

        // Click the Up button (though it should be disabled)
        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Verify the order hasn't changed
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName());
    }

    /**
     * Test that the Up button has exactly one action listener.
     * This verifies line 100 adds exactly one listener.
     */
    @Test
    public void testUpButtonHasExactlyOneActionListener() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        ActionListener[] listeners = upButton.getActionListeners();
        assertEquals(1, listeners.length,
                     "Up button should have exactly one action listener");
    }

    /**
     * Test that the Up button is properly added to the panel's component hierarchy.
     * This verifies line 114 where addButton() is called.
     */
    @Test
    public void testUpButtonAddedToPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // The button should be in the panel's component hierarchy
        int buttonCount = 0;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                buttonCount++;
            }
        }
        assertTrue(buttonCount >= 5,
                   "At least five buttons should be in panel's component hierarchy");
    }

    /**
     * Test that multiple panels each have their own Up button.
     * This ensures addUpButton() works correctly for multiple instances.
     */
    @Test
    public void testMultiplePanelsHaveIndependentUpButtons() {
        testFrame = new JFrame("Test Frame");
        ClassPathPanel panel1 = new ClassPathPanel(testFrame, false);
        ClassPathPanel panel2 = new ClassPathPanel(testFrame, true);

        java.util.List buttons1 = panel1.getButtons();
        java.util.List buttons2 = panel2.getButtons();

        JButton upButton1 = (JButton) buttons1.get(4);
        JButton upButton2 = (JButton) buttons2.get(5); // Different index for inputAndOutput mode

        assertNotSame(upButton1, upButton2,
                      "Each panel should have its own Up button instance");
    }

    /**
     * Test that the Up button is in the correct position in the buttons list.
     * This verifies the button order.
     */
    @Test
    public void testUpButtonIsInCorrectPosition() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        // Should have 6 buttons: Add, Edit, Filter, Remove, Up, Down
        assertEquals(6, buttons.size(), "Should have 6 buttons");

        // Verify Up button is the fifth button (index 4)
        assertTrue(buttons.get(4) instanceof JButton,
                   "Fifth button should be a JButton (Up button)");
    }

    /**
     * Test that the Up button is visible.
     */
    @Test
    public void testUpButtonIsVisible() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        assertTrue(upButton.isVisible(), "Up button should be visible");
    }

    /**
     * Test that the Up button's parent is the panel.
     */
    @Test
    public void testUpButtonParentIsPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        assertNotNull(upButton.getParent(), "Up button should have a parent");
        assertSame(panel, upButton.getParent(),
                   "Up button's parent should be the panel");
    }

    /**
     * Test that the Up button has reasonable size after packing.
     */
    @Test
    public void testUpButtonHasReasonableSize() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        Dimension size = upButton.getSize();
        assertTrue(size.width > 0, "Up button width should be positive");
        assertTrue(size.height > 0, "Up button height should be positive");
    }

    /**
     * Test that the Up button is focusable.
     */
    @Test
    public void testUpButtonIsFocusable() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        assertTrue(upButton.isFocusable(), "Up button should be focusable");
    }

    /**
     * Test that Up button is disabled when list is empty.
     */
    @Test
    public void testUpButtonDisabledWhenListEmpty() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Up button should be disabled when list is empty
        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        assertFalse(upButton.isEnabled(),
                    "Up button should be disabled when list is empty");
    }

    /**
     * Test that Up button is enabled when an item is selected.
     */
    @Test
    public void testUpButtonEnabledWhenItemSelected() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Add items to the list
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        panel.setClassPath(classPath);

        // Select an item
        panel.list.setSelectedIndex(1);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        assertTrue(upButton.isEnabled(),
                   "Up button should be enabled when an item is selected");
    }

    /**
     * Test Up button with different panel configurations.
     * This ensures addUpButton() works with different constructor parameters.
     */
    @Test
    public void testUpButtonWithDifferentConfigurations() {
        testFrame = new JFrame("Test Frame");

        // Test with inputAndOutput=false
        ClassPathPanel panel1 = new ClassPathPanel(testFrame, false);
        assertNotNull(panel1.getButtons().get(4), "Up button should exist with inputAndOutput=false");

        // Test with inputAndOutput=true
        ClassPathPanel panel2 = new ClassPathPanel(testFrame, true);
        // In inputAndOutput mode, Up button is at index 5 (0=AddInput, 1=AddOutput, 2=Edit, 3=Filter, 4=Remove, 5=Up)
        assertNotNull(panel2.getButtons().get(5), "Up button should exist with inputAndOutput=true");
    }

    /**
     * Test that creating a panel executes addUpButton without exceptions.
     * This is a smoke test to ensure lines 99, 100, 114 don't throw exceptions.
     */
    @Test
    public void testAddUpButtonExecutesWithoutException() {
        testFrame = new JFrame("Test Frame");

        // This should not throw any exception
        assertDoesNotThrow(() -> {
            panel = new ClassPathPanel(testFrame, false);
        }, "Creating panel (which calls addUpButton) should not throw exception");

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.getButtons(), "Buttons should be initialized");
        assertTrue(panel.getButtons().size() >= 5, "At least five buttons should exist");
    }

    /**
     * Test Up button with null owner frame.
     * This ensures addUpButton() works even when owner is null.
     */
    @Test
    public void testUpButtonWithNullOwner() {
        panel = new ClassPathPanel(null, false);

        java.util.List buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons should exist even with null owner");
        assertTrue(buttons.size() >= 5, "Up button should exist with null owner");

        JButton upButton = (JButton) buttons.get(4);
        assertNotNull(upButton.getText(), "Up button should have text with null owner");
    }

    /**
     * Test Up button text is different from other button texts.
     * This confirms line 99 uses msg("moveUp") correctly.
     */
    @Test
    public void testUpButtonTextDifferentFromOtherButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);
        JButton removeButton = (JButton) buttons.get(3);
        JButton upButton = (JButton) buttons.get(4);

        String addText = addButton.getText();
        String removeText = removeButton.getText();
        String upText = upButton.getText();

        assertNotEquals(addText, upText,
                        "Up button text should be different from Add button text");
        assertNotEquals(removeText, upText,
                        "Up button text should be different from Remove button text");
    }

    /**
     * Test Up button tooltip is different from other button tooltips.
     * This confirms line 114 uses tip(upButton, "moveUpTip") correctly.
     */
    @Test
    public void testUpButtonTooltipDifferentFromOtherButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);
        JButton removeButton = (JButton) buttons.get(3);
        JButton upButton = (JButton) buttons.get(4);

        String addTooltip = addButton.getToolTipText();
        String removeTooltip = removeButton.getToolTipText();
        String upTooltip = upButton.getToolTipText();

        assertNotEquals(addTooltip, upTooltip,
                        "Up button tooltip should be different from Add button tooltip");
        assertNotEquals(removeTooltip, upTooltip,
                        "Up button tooltip should be different from Remove button tooltip");
    }

    /**
     * Test that the Up button comes after Add, Edit, Filter, and Remove buttons.
     * This verifies the order of button creation.
     */
    @Test
    public void testUpButtonOrderAfterOtherButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 5, "Should have at least five buttons");

        // Verify we can get all buttons without exception
        Component firstButton = (Component) buttons.get(0);
        Component secondButton = (Component) buttons.get(1);
        Component thirdButton = (Component) buttons.get(2);
        Component fourthButton = (Component) buttons.get(3);
        Component fifthButton = (Component) buttons.get(4);

        assertNotNull(firstButton, "First button (Add) should exist");
        assertNotNull(secondButton, "Second button (Edit) should exist");
        assertNotNull(thirdButton, "Third button (Filter) should exist");
        assertNotNull(fourthButton, "Fourth button (Remove) should exist");
        assertNotNull(fifthButton, "Fifth button (Up) should exist");
    }

    /**
     * Test that the Up button's action listener is not null.
     */
    @Test
    public void testUpButtonActionListenerNotNull() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        ActionListener[] listeners = upButton.getActionListeners();
        assertNotNull(listeners[0], "Action listener should not be null");
    }

    /**
     * Test that Up button has proper configuration after construction.
     * This verifies all three main lines (99, 100, 114) executed successfully.
     */
    @Test
    public void testUpButtonFullConfiguration() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        // Verify line 99: button created with text
        assertNotNull(upButton.getText());
        assertFalse(upButton.getText().isEmpty());

        // Verify line 100: action listener added
        assertTrue(upButton.getActionListeners().length > 0);

        // Verify line 114: tooltip set and button added to panel
        assertNotNull(upButton.getToolTipText());
        assertFalse(upButton.getToolTipText().isEmpty());
        assertSame(panel, upButton.getParent());
    }

    /**
     * Test that Up button action correctly invokes moveElementsAt with -1 offset.
     * This specifically tests line 109.
     */
    @Test
    public void testUpButtonUsesCorrectOffset() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        classPath.add(new ClassPathEntry(new File("third.jar"), false));

        panel.setClassPath(classPath);

        // Select the third item (index 2)
        panel.list.setSelectedIndex(2);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        String itemName = ((ClassPathEntry) listModel.getElementAt(2)).getName();
        assertEquals("third.jar", itemName);

        // Click the Up button
        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Verify the item moved up by exactly 1 position (offset -1)
        itemName = ((ClassPathEntry) listModel.getElementAt(1)).getName();
        assertEquals("third.jar", itemName,
                     "Item should have moved up by exactly 1 position");

        // Verify new selection is at index 1
        assertEquals(1, panel.list.getSelectedIndex(),
                     "Selection should be at new position (index 1)");
    }

    /**
     * Test that Up button works correctly when moving the last item up.
     */
    @Test
    public void testUpButtonMovesLastItemUp() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        classPath.add(new ClassPathEntry(new File("third.jar"), false));

        panel.setClassPath(classPath);

        // Select the last item (index 2)
        panel.list.setSelectedIndex(2);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Click the Up button
        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Verify the order
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("third.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName(),
                     "Last item should have moved to middle position");
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName());
    }

    /**
     * Test Up button state consistency across panel lifecycle.
     */
    @Test
    public void testUpButtonStateConsistency() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);

        // Initially disabled
        assertFalse(upButton.isEnabled(), "Should be disabled initially");

        // Add to frame
        testFrame.add(panel);
        testFrame.pack();

        // Should still be disabled (no selection)
        assertFalse(upButton.isEnabled(),
                    "Should remain disabled after adding to frame");

        // Add items and select one
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        panel.setClassPath(classPath);
        panel.list.setSelectedIndex(1);

        // Should now be enabled
        assertTrue(upButton.isEnabled(),
                   "Should be enabled when item is selected");
    }

    /**
     * Test that triggering the Up button via ActionEvent works correctly.
     * This directly tests the actionPerformed method (lines 102-111).
     */
    @Test
    public void testUpButtonActionPerformedMethod() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));

        panel.setClassPath(classPath);

        // Select the second item
        panel.list.setSelectedIndex(1);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Get the action listener and trigger it directly
        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        ActionListener listener = upButton.getActionListeners()[0];

        // Create a mock ActionEvent and trigger the listener
        ActionEvent event = new ActionEvent(upButton, ActionEvent.ACTION_PERFORMED, "moveUp");
        listener.actionPerformed(event);

        // Verify the item moved
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName(),
                     "Item should have moved to first position");
    }
}
