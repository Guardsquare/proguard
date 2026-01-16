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
 * Test class for ListPanel.addDownButton() method.
 *
 * The addDownButton() method creates and adds a "Down" button to the panel.
 * Lines that need coverage:
 * - Line 120: Creates a new JButton with the localized "moveDown" message
 * - Line 121: Adds an ActionListener to the button
 * - Lines 123-132: ActionListener's actionPerformed method that:
 *   - Line 125: Gets selected indices from the list
 *   - Lines 126-127: Checks if selection exists and last element is not at the last position
 *   - Line 130: Moves selected elements down by calling moveElementsAt(selectedIndices, 1)
 * - Line 135: Wraps the button with a tooltip using tip() method and adds it via addButton()
 *
 * These tests verify:
 * - The button is created with proper text and tooltip
 * - The button is added to the panel
 * - The action listener is attached and functional
 * - The button correctly moves items down in the list
 * - The button handles edge cases (last item, multiple selections, empty list)
 * - The button is properly configured and enabled/disabled based on selection state
 *
 * Note: Since ListPanel is abstract, we test it through the concrete
 * ClassPathPanel class which extends ListPanel and calls addDownButton()
 * in its constructor.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ListPanelClaude_addDownButtonTest {

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
     * Test that addDownButton creates and adds a button to the panel.
     * This covers line 120 (JButton creation), line 121 (addActionListener),
     * and line 135 (tip wrapper and addButton call).
     *
     * The constructor calls addDownButton(), so creating a panel executes these lines.
     */
    @Test
    public void testAddDownButtonCreatesButton() {
        testFrame = new JFrame("Test Frame");
        // Constructor calls addDownButton(), which executes lines 120, 121, 135
        panel = new ClassPathPanel(testFrame, false);

        // Verify that buttons were added to the panel
        java.util.List buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons list should not be null");
        assertTrue(buttons.size() >= 6, "At least six buttons should be added (Add, Edit, Filter, Remove, Up, Down)");

        // The sixth button should be the Down button (0=Add, 1=Edit, 2=Filter, 3=Remove, 4=Up, 5=Down)
        assertTrue(buttons.size() > 5, "Should have at least 6 buttons");
        Component sixthButton = (Component) buttons.get(5);
        assertTrue(sixthButton instanceof JButton, "Sixth button should be a JButton");
    }

    /**
     * Test that the Down button has the correct text.
     * This verifies line 120 where the button is created with msg("moveDown").
     */
    @Test
    public void testDownButtonHasText() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        assertNotNull(downButton.getText(), "Down button should have text");
        assertFalse(downButton.getText().isEmpty(), "Down button text should not be empty");
    }

    /**
     * Test that the Down button has a tooltip.
     * This verifies line 135 where tip(downButton, "moveDownTip") is called.
     */
    @Test
    public void testDownButtonHasTooltip() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        assertNotNull(downButton.getToolTipText(), "Down button should have a tooltip");
        assertFalse(downButton.getToolTipText().isEmpty(),
                    "Down button tooltip should not be empty");
    }

    /**
     * Test that the Down button has an action listener attached.
     * This verifies line 121 where addActionListener() is called.
     */
    @Test
    public void testDownButtonHasActionListener() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        ActionListener[] listeners = downButton.getActionListeners();
        assertTrue(listeners.length > 0,
                   "Down button should have at least one action listener");
    }

    /**
     * Test that the Down button is disabled by default when no items are selected.
     * The Down button should be disabled when no items are in the list.
     */
    @Test
    public void testDownButtonIsDisabledByDefault() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        assertFalse(downButton.isEnabled(),
                    "Down button should be disabled by default when no items are selected");
    }

    /**
     * Test that the Down button's action listener moves items down correctly.
     * This tests lines 125-130 of the actionPerformed method.
     */
    @Test
    public void testDownButtonMovesItemDown() {
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

        // Get the Down button and trigger it
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        // The button should be enabled since an item is selected
        assertTrue(downButton.isEnabled(), "Down button should be enabled when item is selected");

        // Click the Down button
        downButton.doClick();

        // Verify the second item moved to third position
        ClassPathEntry secondEntry = (ClassPathEntry) listModel.getElementAt(1);
        assertEquals("third.jar", secondEntry.getName(),
                     "Third item should have moved to second position");

        ClassPathEntry thirdEntry = (ClassPathEntry) listModel.getElementAt(2);
        assertEquals("second.jar", thirdEntry.getName(),
                     "Second item should have moved to third position");

        // Verify selection moved with the item
        assertEquals(2, panel.list.getSelectedIndex(),
                     "Selection should have moved with the item");
    }

    /**
     * Test that the Down button does not move the last item.
     * This tests lines 126-127 checking if selectedIndices[selectedIndices.length-1] < listModel.getSize()-1.
     */
    @Test
    public void testDownButtonDoesNotMoveLastItem() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with multiple entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));

        panel.setClassPath(classPath);

        // Select the last item (index 1)
        panel.list.setSelectedIndex(1);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Click the Down button
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // Verify the order hasn't changed
        ClassPathEntry firstEntry = (ClassPathEntry) listModel.getElementAt(0);
        assertEquals("first.jar", firstEntry.getName(),
                     "First item should remain in first position");

        ClassPathEntry secondEntry = (ClassPathEntry) listModel.getElementAt(1);
        assertEquals("second.jar", secondEntry.getName(),
                     "Last item should remain in last position");
    }

    /**
     * Test that the Down button correctly handles multiple selected items.
     * This tests line 125 which gets all selected indices.
     */
    @Test
    public void testDownButtonMovesMultipleItemsDown() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with multiple entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        classPath.add(new ClassPathEntry(new File("third.jar"), false));
        classPath.add(new ClassPathEntry(new File("fourth.jar"), false));

        panel.setClassPath(classPath);

        // Select items at indices 0 and 1 (first and second)
        panel.list.setSelectedIndices(new int[]{0, 1});

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Click the Down button
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // Verify the items moved down together
        assertEquals("third.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName(),
                     "First item should have moved down to second position");
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName(),
                     "Second item should have moved down to third position");
        assertEquals("fourth.jar", ((ClassPathEntry) listModel.getElementAt(3)).getName());
    }

    /**
     * Test that the Down button does nothing when no items are selected.
     * This tests line 126 checking if selectedIndices.length > 0.
     */
    @Test
    public void testDownButtonDoesNothingWhenNoSelection() {
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

        // Click the Down button (though it should be disabled)
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // Verify the order hasn't changed
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName());
    }

    /**
     * Test that the Down button has exactly one action listener.
     * This verifies line 121 adds exactly one listener.
     */
    @Test
    public void testDownButtonHasExactlyOneActionListener() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        ActionListener[] listeners = downButton.getActionListeners();
        assertEquals(1, listeners.length,
                     "Down button should have exactly one action listener");
    }

    /**
     * Test that the Down button is properly added to the panel's component hierarchy.
     * This verifies line 135 where addButton() is called.
     */
    @Test
    public void testDownButtonAddedToPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

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
     * Test that multiple panels each have their own Down button.
     * This ensures addDownButton() works correctly for multiple instances.
     */
    @Test
    public void testMultiplePanelsHaveIndependentDownButtons() {
        testFrame = new JFrame("Test Frame");
        ClassPathPanel panel1 = new ClassPathPanel(testFrame, false);
        ClassPathPanel panel2 = new ClassPathPanel(testFrame, true);

        java.util.List buttons1 = panel1.getButtons();
        java.util.List buttons2 = panel2.getButtons();

        JButton downButton1 = (JButton) buttons1.get(5);
        JButton downButton2 = (JButton) buttons2.get(6); // Different index for inputAndOutput mode

        assertNotSame(downButton1, downButton2,
                      "Each panel should have its own Down button instance");
    }

    /**
     * Test that the Down button is in the correct position in the buttons list.
     * This verifies the button order.
     */
    @Test
    public void testDownButtonIsInCorrectPosition() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        // Should have 6 buttons: Add, Edit, Filter, Remove, Up, Down
        assertEquals(6, buttons.size(), "Should have 6 buttons");

        // Verify Down button is the sixth button (index 5)
        assertTrue(buttons.get(5) instanceof JButton,
                   "Sixth button should be a JButton (Down button)");
    }

    /**
     * Test that the Down button is visible.
     */
    @Test
    public void testDownButtonIsVisible() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        assertTrue(downButton.isVisible(), "Down button should be visible");
    }

    /**
     * Test that the Down button's parent is the panel.
     */
    @Test
    public void testDownButtonParentIsPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        assertNotNull(downButton.getParent(), "Down button should have a parent");
        assertSame(panel, downButton.getParent(),
                   "Down button's parent should be the panel");
    }

    /**
     * Test that the Down button has reasonable size after packing.
     */
    @Test
    public void testDownButtonHasReasonableSize() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        testFrame.add(panel);
        testFrame.pack();

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        Dimension size = downButton.getSize();
        assertTrue(size.width > 0, "Down button width should be positive");
        assertTrue(size.height > 0, "Down button height should be positive");
    }

    /**
     * Test that the Down button is focusable.
     */
    @Test
    public void testDownButtonIsFocusable() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        assertTrue(downButton.isFocusable(), "Down button should be focusable");
    }

    /**
     * Test that Down button is disabled when list is empty.
     */
    @Test
    public void testDownButtonDisabledWhenListEmpty() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Down button should be disabled when list is empty
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        assertFalse(downButton.isEnabled(),
                    "Down button should be disabled when list is empty");
    }

    /**
     * Test that Down button is enabled when an item is selected.
     */
    @Test
    public void testDownButtonEnabledWhenItemSelected() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Add items to the list
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        panel.setClassPath(classPath);

        // Select the first item (not the last)
        panel.list.setSelectedIndex(0);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        assertTrue(downButton.isEnabled(),
                   "Down button should be enabled when a non-last item is selected");
    }

    /**
     * Test Down button with different panel configurations.
     * This ensures addDownButton() works with different constructor parameters.
     */
    @Test
    public void testDownButtonWithDifferentConfigurations() {
        testFrame = new JFrame("Test Frame");

        // Test with inputAndOutput=false
        ClassPathPanel panel1 = new ClassPathPanel(testFrame, false);
        assertNotNull(panel1.getButtons().get(5), "Down button should exist with inputAndOutput=false");

        // Test with inputAndOutput=true
        ClassPathPanel panel2 = new ClassPathPanel(testFrame, true);
        // In inputAndOutput mode, Down button is at index 6 (0=AddInput, 1=AddOutput, 2=Edit, 3=Filter, 4=Remove, 5=Up, 6=Down)
        assertNotNull(panel2.getButtons().get(6), "Down button should exist with inputAndOutput=true");
    }

    /**
     * Test that creating a panel executes addDownButton without exceptions.
     * This is a smoke test to ensure lines 120, 121, 135 don't throw exceptions.
     */
    @Test
    public void testAddDownButtonExecutesWithoutException() {
        testFrame = new JFrame("Test Frame");

        // This should not throw any exception
        assertDoesNotThrow(() -> {
            panel = new ClassPathPanel(testFrame, false);
        }, "Creating panel (which calls addDownButton) should not throw exception");

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.getButtons(), "Buttons should be initialized");
        assertTrue(panel.getButtons().size() >= 6, "At least six buttons should exist");
    }

    /**
     * Test Down button with null owner frame.
     * This ensures addDownButton() works even when owner is null.
     */
    @Test
    public void testDownButtonWithNullOwner() {
        panel = new ClassPathPanel(null, false);

        java.util.List buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons should exist even with null owner");
        assertTrue(buttons.size() >= 6, "Down button should exist with null owner");

        JButton downButton = (JButton) buttons.get(5);
        assertNotNull(downButton.getText(), "Down button should have text with null owner");
    }

    /**
     * Test Down button text is different from other button texts.
     * This confirms line 120 uses msg("moveDown") correctly.
     */
    @Test
    public void testDownButtonTextDifferentFromOtherButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);
        JButton upButton = (JButton) buttons.get(4);
        JButton downButton = (JButton) buttons.get(5);

        String addText = addButton.getText();
        String upText = upButton.getText();
        String downText = downButton.getText();

        assertNotEquals(addText, downText,
                        "Down button text should be different from Add button text");
        assertNotEquals(upText, downText,
                        "Down button text should be different from Up button text");
    }

    /**
     * Test Down button tooltip is different from other button tooltips.
     * This confirms line 135 uses tip(downButton, "moveDownTip") correctly.
     */
    @Test
    public void testDownButtonTooltipDifferentFromOtherButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);
        JButton upButton = (JButton) buttons.get(4);
        JButton downButton = (JButton) buttons.get(5);

        String addTooltip = addButton.getToolTipText();
        String upTooltip = upButton.getToolTipText();
        String downTooltip = downButton.getToolTipText();

        assertNotEquals(addTooltip, downTooltip,
                        "Down button tooltip should be different from Add button tooltip");
        assertNotEquals(upTooltip, downTooltip,
                        "Down button tooltip should be different from Up button tooltip");
    }

    /**
     * Test that the Down button comes after all other buttons.
     * This verifies the order of button creation.
     */
    @Test
    public void testDownButtonOrderAfterOtherButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 6, "Should have at least six buttons");

        // Verify we can get all buttons without exception
        Component firstButton = (Component) buttons.get(0);
        Component secondButton = (Component) buttons.get(1);
        Component thirdButton = (Component) buttons.get(2);
        Component fourthButton = (Component) buttons.get(3);
        Component fifthButton = (Component) buttons.get(4);
        Component sixthButton = (Component) buttons.get(5);

        assertNotNull(firstButton, "First button (Add) should exist");
        assertNotNull(secondButton, "Second button (Edit) should exist");
        assertNotNull(thirdButton, "Third button (Filter) should exist");
        assertNotNull(fourthButton, "Fourth button (Remove) should exist");
        assertNotNull(fifthButton, "Fifth button (Up) should exist");
        assertNotNull(sixthButton, "Sixth button (Down) should exist");
    }

    /**
     * Test that the Down button's action listener is not null.
     */
    @Test
    public void testDownButtonActionListenerNotNull() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        ActionListener[] listeners = downButton.getActionListeners();
        assertNotNull(listeners[0], "Action listener should not be null");
    }

    /**
     * Test that Down button has proper configuration after construction.
     * This verifies all three main lines (120, 121, 135) executed successfully.
     */
    @Test
    public void testDownButtonFullConfiguration() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        // Verify line 120: button created with text
        assertNotNull(downButton.getText());
        assertFalse(downButton.getText().isEmpty());

        // Verify line 121: action listener added
        assertTrue(downButton.getActionListeners().length > 0);

        // Verify line 135: tooltip set and button added to panel
        assertNotNull(downButton.getToolTipText());
        assertFalse(downButton.getToolTipText().isEmpty());
        assertSame(panel, downButton.getParent());
    }

    /**
     * Test that Down button action correctly invokes moveElementsAt with 1 offset.
     * This specifically tests line 130.
     */
    @Test
    public void testDownButtonUsesCorrectOffset() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        classPath.add(new ClassPathEntry(new File("third.jar"), false));

        panel.setClassPath(classPath);

        // Select the first item (index 0)
        panel.list.setSelectedIndex(0);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        String itemName = ((ClassPathEntry) listModel.getElementAt(0)).getName();
        assertEquals("first.jar", itemName);

        // Click the Down button
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // Verify the item moved down by exactly 1 position (offset 1)
        itemName = ((ClassPathEntry) listModel.getElementAt(1)).getName();
        assertEquals("first.jar", itemName,
                     "Item should have moved down by exactly 1 position");

        // Verify new selection is at index 1
        assertEquals(1, panel.list.getSelectedIndex(),
                     "Selection should be at new position (index 1)");
    }

    /**
     * Test that Down button works correctly when moving the first item down.
     */
    @Test
    public void testDownButtonMovesFirstItemDown() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        classPath.add(new ClassPathEntry(new File("third.jar"), false));

        panel.setClassPath(classPath);

        // Select the first item (index 0)
        panel.list.setSelectedIndex(0);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Click the Down button
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // Verify the order
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName(),
                     "First item should have moved to second position");
        assertEquals("third.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName());
    }

    /**
     * Test Down button state consistency across panel lifecycle.
     */
    @Test
    public void testDownButtonStateConsistency() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        // Initially disabled
        assertFalse(downButton.isEnabled(), "Should be disabled initially");

        // Add to frame
        testFrame.add(panel);
        testFrame.pack();

        // Should still be disabled (no selection)
        assertFalse(downButton.isEnabled(),
                    "Should remain disabled after adding to frame");

        // Add items and select one (not the last)
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        panel.setClassPath(classPath);
        panel.list.setSelectedIndex(0);

        // Should now be enabled
        assertTrue(downButton.isEnabled(),
                   "Should be enabled when non-last item is selected");
    }

    /**
     * Test that triggering the Down button via ActionEvent works correctly.
     * This directly tests the actionPerformed method (lines 123-132).
     */
    @Test
    public void testDownButtonActionPerformedMethod() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));

        panel.setClassPath(classPath);

        // Select the first item
        panel.list.setSelectedIndex(0);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Get the action listener and trigger it directly
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        ActionListener listener = downButton.getActionListeners()[0];

        // Create a mock ActionEvent and trigger the listener
        ActionEvent event = new ActionEvent(downButton, ActionEvent.ACTION_PERFORMED, "moveDown");
        listener.actionPerformed(event);

        // Verify the item moved
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName(),
                     "Item should have moved to second position");
    }

    /**
     * Test that Down button does not move items when the last item in a selection is at the end.
     * This tests the boundary condition in lines 126-127.
     */
    @Test
    public void testDownButtonDoesNotMoveWhenLastSelectedItemIsAtEnd() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        classPath.add(new ClassPathEntry(new File("third.jar"), false));

        panel.setClassPath(classPath);

        // Select items including the last one (indices 1 and 2)
        panel.list.setSelectedIndices(new int[]{1, 2});

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Click the Down button
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // Verify the order hasn't changed (because last selected item is at the end)
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName());
        assertEquals("third.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName());
    }

    /**
     * Test that Down button correctly checks the last selected index.
     * This specifically tests line 127: selectedIndices[selectedIndices.length-1].
     */
    @Test
    public void testDownButtonChecksLastSelectedIndex() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with entries
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        classPath.add(new ClassPathEntry(new File("third.jar"), false));
        classPath.add(new ClassPathEntry(new File("fourth.jar"), false));

        panel.setClassPath(classPath);

        // Select multiple items where the last one is NOT at the end (indices 0, 1)
        panel.list.setSelectedIndices(new int[]{0, 1});

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Click the Down button
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // Verify the items moved (because last selected index 1 < size-1)
        assertEquals("third.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName());
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName());
        assertEquals("fourth.jar", ((ClassPathEntry) listModel.getElementAt(3)).getName());
    }

    /**
     * Test Down button with single item list.
     * Verifies line 127 correctly handles the edge case.
     */
    @Test
    public void testDownButtonWithSingleItemList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with single entry
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("only.jar"), false));

        panel.setClassPath(classPath);

        // Select the only item
        panel.list.setSelectedIndex(0);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Click the Down button
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // Verify the item hasn't moved (it's the only and last item)
        assertEquals("only.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals(1, listModel.getSize(), "List should still have 1 item");
    }

    /**
     * Test that all buttons including Down button are in the component list.
     */
    @Test
    public void testDownButtonIncludedInAllButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        java.util.List buttons = panel.getButtons();

        // Should have 6 buttons: Add, Edit, Filter, Remove, Up, Down
        assertEquals(6, buttons.size(), "Should have 6 buttons including Down");

        // Verify Down button is the sixth button
        assertTrue(buttons.get(5) instanceof JButton,
                   "Sixth button should be a JButton (Down button)");
    }
}
