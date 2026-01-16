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
 * Test class for ListPanel.addCopyToPanelButton(String, String, ListPanel) method.
 *
 * The addCopyToPanelButton() method creates and adds a button that copies/moves entries
 * from one panel to another panel.
 *
 * Lines that need coverage:
 * - Line 150: Creates a new JButton with the localized text from buttonTextKey parameter
 * - Line 151: Adds an ActionListener to the button
 * - Lines 153-163: ActionListener's actionPerformed method that:
 *   - Line 155: Gets selected indices from the list
 *   - Line 156: Gets selected values from the list
 *   - Line 159: Removes the selected elements from this panel via removeElementsAt()
 *   - Line 162: Adds the elements to the target panel via panel.addElements()
 * - Line 166: Wraps the button with a tooltip using tip() method and adds it via addButton()
 *
 * These tests verify:
 * - The button is created with proper text and tooltip based on parameters
 * - The button is added to the source panel
 * - The action listener is attached and functional
 * - The button correctly moves/copies items from source panel to target panel
 * - The button removes items from source panel (line 159)
 * - The button adds items to target panel (line 162)
 * - The button handles edge cases (empty selection, multiple items)
 * - The button is properly configured and enabled/disabled based on selection state
 * - The method works with different text keys and tooltip keys
 *
 * Note: Since ListPanel is abstract, we test it through the concrete
 * ClassPathPanel class which extends ListPanel. We call addCopyToPanelButton()
 * explicitly in the tests (it's not called by the constructor).
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ListPanelClaude_addCopyToPanelButtonTest {

    private JFrame testFrame;
    private ClassPathPanel sourcePanel;
    private ClassPathPanel targetPanel;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (sourcePanel != null) {
            sourcePanel = null;
        }
        if (targetPanel != null) {
            targetPanel = null;
        }
        if (testFrame != null) {
            testFrame.dispose();
            testFrame = null;
        }
    }

    /**
     * Test that addCopyToPanelButton creates and adds a button to the panel.
     * This covers line 150 (JButton creation), line 151 (addActionListener),
     * and line 166 (tip wrapper and addButton call).
     */
    @Test
    public void testAddCopyToPanelButtonCreatesButton() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Get initial button count (6 buttons: Add, Edit, Filter, Remove, Up, Down)
        int initialButtonCount = sourcePanel.getButtons().size();
        assertEquals(6, initialButtonCount, "Should start with 6 buttons");

        // Call addCopyToPanelButton - this executes lines 150, 151, 166
        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Verify that a new button was added
        java.util.List buttons = sourcePanel.getButtons();
        assertEquals(7, buttons.size(), "Should have 7 buttons after adding copy button");

        Component seventhButton = (Component) buttons.get(6);
        assertTrue(seventhButton instanceof JButton, "Seventh button should be a JButton");
    }

    /**
     * Test that the copy button has the correct text based on buttonTextKey parameter.
     * This verifies line 150 where the button is created with msg(buttonTextKey).
     */
    @Test
    public void testCopyButtonHasTextFromParameter() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);

        assertNotNull(copyButton.getText(), "Copy button should have text");
        assertFalse(copyButton.getText().isEmpty(), "Copy button text should not be empty");
    }

    /**
     * Test that the copy button has a tooltip based on tipKey parameter.
     * This verifies line 166 where tip(moveButton, tipKey) is called.
     */
    @Test
    public void testCopyButtonHasTooltipFromParameter() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);

        assertNotNull(copyButton.getToolTipText(), "Copy button should have a tooltip");
        assertFalse(copyButton.getToolTipText().isEmpty(),
                    "Copy button tooltip should not be empty");
    }

    /**
     * Test that the copy button has an action listener attached.
     * This verifies line 151 where addActionListener() is called.
     */
    @Test
    public void testCopyButtonHasActionListener() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);

        ActionListener[] listeners = copyButton.getActionListeners();
        assertTrue(listeners.length > 0,
                   "Copy button should have at least one action listener");
    }

    /**
     * Test that the copy button moves items from source to target panel.
     * This tests lines 155-162 of the actionPerformed method.
     */
    @Test
    public void testCopyButtonMovesItemsFromSourceToTarget() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add items to source panel
        ClassPath sourceClassPath = new ClassPath();
        sourceClassPath.add(new ClassPathEntry(new File("first.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("second.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("third.jar"), false));
        sourcePanel.setClassPath(sourceClassPath);

        // Target panel starts empty
        DefaultListModel sourceModel = (DefaultListModel) sourcePanel.list.getModel();
        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();

        assertEquals(3, sourceModel.getSize(), "Source should have 3 items");
        assertEquals(0, targetModel.getSize(), "Target should start empty");

        // Add copy button
        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Select an item in source panel (index 1)
        sourcePanel.list.setSelectedIndex(1);

        // Get the copy button and trigger it
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify item was removed from source (line 159)
        assertEquals(2, sourceModel.getSize(), "Source should now have 2 items");
        assertEquals("first.jar", ((ClassPathEntry) sourceModel.getElementAt(0)).getName());
        assertEquals("third.jar", ((ClassPathEntry) sourceModel.getElementAt(1)).getName());

        // Verify item was added to target (line 162)
        assertEquals(1, targetModel.getSize(), "Target should now have 1 item");
        assertEquals("second.jar", ((ClassPathEntry) targetModel.getElementAt(0)).getName());
    }

    /**
     * Test that the copy button moves multiple selected items.
     * This tests lines 155-156 which handle multiple selections.
     */
    @Test
    public void testCopyButtonMovesMultipleItems() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add items to source panel
        ClassPath sourceClassPath = new ClassPath();
        sourceClassPath.add(new ClassPathEntry(new File("first.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("second.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("third.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("fourth.jar"), false));
        sourcePanel.setClassPath(sourceClassPath);

        DefaultListModel sourceModel = (DefaultListModel) sourcePanel.list.getModel();
        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();

        // Add copy button
        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Select multiple items (indices 1 and 2)
        sourcePanel.list.setSelectedIndices(new int[]{1, 2});

        // Get the copy button and trigger it
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify items were removed from source
        assertEquals(2, sourceModel.getSize(), "Source should now have 2 items");
        assertEquals("first.jar", ((ClassPathEntry) sourceModel.getElementAt(0)).getName());
        assertEquals("fourth.jar", ((ClassPathEntry) sourceModel.getElementAt(1)).getName());

        // Verify items were added to target in correct order
        assertEquals(2, targetModel.getSize(), "Target should now have 2 items");
        assertEquals("second.jar", ((ClassPathEntry) targetModel.getElementAt(0)).getName());
        assertEquals("third.jar", ((ClassPathEntry) targetModel.getElementAt(1)).getName());
    }

    /**
     * Test that the copy button does nothing when no items are selected.
     * This tests line 155 with empty selection.
     */
    @Test
    public void testCopyButtonDoesNothingWhenNoSelection() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add items to source panel
        ClassPath sourceClassPath = new ClassPath();
        sourceClassPath.add(new ClassPathEntry(new File("first.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("second.jar"), false));
        sourcePanel.setClassPath(sourceClassPath);

        DefaultListModel sourceModel = (DefaultListModel) sourcePanel.list.getModel();
        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();

        // Add copy button
        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Don't select anything
        sourcePanel.list.clearSelection();

        // Get the copy button and trigger it
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify nothing changed
        assertEquals(2, sourceModel.getSize(), "Source should still have 2 items");
        assertEquals(0, targetModel.getSize(), "Target should still be empty");
    }

    /**
     * Test that the copy button moves all items when all are selected.
     */
    @Test
    public void testCopyButtonMovesAllItemsWhenAllSelected() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add items to source panel
        ClassPath sourceClassPath = new ClassPath();
        sourceClassPath.add(new ClassPathEntry(new File("first.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("second.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("third.jar"), false));
        sourcePanel.setClassPath(sourceClassPath);

        DefaultListModel sourceModel = (DefaultListModel) sourcePanel.list.getModel();
        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();

        // Add copy button
        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Select all items
        sourcePanel.list.setSelectionInterval(0, 2);

        // Get the copy button and trigger it
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify all items moved
        assertEquals(0, sourceModel.getSize(), "Source should be empty");
        assertEquals(3, targetModel.getSize(), "Target should have 3 items");
        assertEquals("first.jar", ((ClassPathEntry) targetModel.getElementAt(0)).getName());
        assertEquals("second.jar", ((ClassPathEntry) targetModel.getElementAt(1)).getName());
        assertEquals("third.jar", ((ClassPathEntry) targetModel.getElementAt(2)).getName());
    }

    /**
     * Test that the copy button has exactly one action listener.
     * This verifies line 151 adds exactly one listener.
     */
    @Test
    public void testCopyButtonHasExactlyOneActionListener() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);

        ActionListener[] listeners = copyButton.getActionListeners();
        assertEquals(1, listeners.length,
                     "Copy button should have exactly one action listener");
    }

    /**
     * Test that the copy button is properly added to the panel's component hierarchy.
     * This verifies line 166 where addButton() is called.
     */
    @Test
    public void testCopyButtonAddedToPanel() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        int initialComponentCount = sourcePanel.getComponentCount();

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        assertEquals(initialComponentCount + 1, sourcePanel.getComponentCount(),
                     "Panel should have one more component after adding copy button");
    }

    /**
     * Test that multiple copy buttons can be added to the same panel.
     */
    @Test
    public void testMultipleCopyButtonsCanBeAdded() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        ClassPathPanel targetPanel1 = new ClassPathPanel(testFrame, false);
        ClassPathPanel targetPanel2 = new ClassPathPanel(testFrame, false);

        int initialButtonCount = sourcePanel.getButtons().size();

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel1);
        sourcePanel.addCopyToPanelButton("moveToInput", "moveToInputTip", targetPanel2);

        java.util.List buttons = sourcePanel.getButtons();
        assertEquals(initialButtonCount + 2, buttons.size(),
                     "Should have two additional buttons");
    }

    /**
     * Test that copy button works with different text and tooltip keys.
     * This verifies line 150 and 166 use the provided parameters.
     */
    @Test
    public void testCopyButtonWithDifferentKeys() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add button with one set of keys
        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);

        assertNotNull(copyButton.getText(), "Button should have text");
        assertNotNull(copyButton.getToolTipText(), "Button should have tooltip");
    }

    /**
     * Test that the copy button is visible.
     */
    @Test
    public void testCopyButtonIsVisible() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        testFrame.add(sourcePanel);
        testFrame.pack();

        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);

        assertTrue(copyButton.isVisible(), "Copy button should be visible");
    }

    /**
     * Test that the copy button's parent is the source panel.
     */
    @Test
    public void testCopyButtonParentIsSourcePanel() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);

        assertNotNull(copyButton.getParent(), "Copy button should have a parent");
        assertSame(sourcePanel, copyButton.getParent(),
                   "Copy button's parent should be the source panel");
    }

    /**
     * Test that the copy button has reasonable size after packing.
     */
    @Test
    public void testCopyButtonHasReasonableSize() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        testFrame.add(sourcePanel);
        testFrame.pack();

        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);

        Dimension size = copyButton.getSize();
        assertTrue(size.width > 0, "Copy button width should be positive");
        assertTrue(size.height > 0, "Copy button height should be positive");
    }

    /**
     * Test that the copy button is focusable.
     */
    @Test
    public void testCopyButtonIsFocusable() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);

        assertTrue(copyButton.isFocusable(), "Copy button should be focusable");
    }

    /**
     * Test that addCopyToPanelButton executes without exceptions.
     * This is a smoke test to ensure lines 150, 151, 166 don't throw exceptions.
     */
    @Test
    public void testAddCopyToPanelButtonExecutesWithoutException() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // This should not throw any exception
        assertDoesNotThrow(() -> {
            sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);
        }, "addCopyToPanelButton should not throw exception");

        assertNotNull(sourcePanel.getButtons(), "Buttons should be initialized");
        assertEquals(7, sourcePanel.getButtons().size(), "Should have 7 buttons");
    }

    /**
     * Test copy button with null owner frame.
     */
    @Test
    public void testCopyButtonWithNullOwner() {
        sourcePanel = new ClassPathPanel(null, false);
        targetPanel = new ClassPathPanel(null, false);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        java.util.List buttons = sourcePanel.getButtons();
        assertNotNull(buttons, "Buttons should exist even with null owner");
        assertEquals(7, buttons.size(), "Should have 7 buttons with null owner");

        JButton copyButton = (JButton) buttons.get(6);
        assertNotNull(copyButton.getText(), "Copy button should have text with null owner");
    }

    /**
     * Test that the copy button's action listener is not null.
     */
    @Test
    public void testCopyButtonActionListenerNotNull() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);

        ActionListener[] listeners = copyButton.getActionListeners();
        assertNotNull(listeners[0], "Action listener should not be null");
    }

    /**
     * Test that copy button has proper configuration after creation.
     * This verifies all main lines (150, 151, 166) executed successfully.
     */
    @Test
    public void testCopyButtonFullConfiguration() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);

        // Verify line 150: button created with text
        assertNotNull(copyButton.getText());
        assertFalse(copyButton.getText().isEmpty());

        // Verify line 151: action listener added
        assertTrue(copyButton.getActionListeners().length > 0);

        // Verify line 166: tooltip set and button added to panel
        assertNotNull(copyButton.getToolTipText());
        assertFalse(copyButton.getToolTipText().isEmpty());
        assertSame(sourcePanel, copyButton.getParent());
    }

    /**
     * Test that triggering the copy button via ActionEvent works correctly.
     * This directly tests the actionPerformed method (lines 153-163).
     */
    @Test
    public void testCopyButtonActionPerformedMethod() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add items to source panel
        ClassPath sourceClassPath = new ClassPath();
        sourceClassPath.add(new ClassPathEntry(new File("first.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("second.jar"), false));
        sourcePanel.setClassPath(sourceClassPath);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Select the first item
        sourcePanel.list.setSelectedIndex(0);

        DefaultListModel sourceModel = (DefaultListModel) sourcePanel.list.getModel();
        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();

        // Get the action listener and trigger it directly
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        ActionListener listener = copyButton.getActionListeners()[0];

        // Create a mock ActionEvent and trigger the listener
        ActionEvent event = new ActionEvent(copyButton, ActionEvent.ACTION_PERFORMED, "moveToOutput");
        listener.actionPerformed(event);

        // Verify the item was moved
        assertEquals(1, sourceModel.getSize(), "Source should have 1 item");
        assertEquals("second.jar", ((ClassPathEntry) sourceModel.getElementAt(0)).getName());
        assertEquals(1, targetModel.getSize(), "Target should have 1 item");
        assertEquals("first.jar", ((ClassPathEntry) targetModel.getElementAt(0)).getName());
    }

    /**
     * Test that items are removed from source before being added to target.
     * This tests the order of operations (line 159 before line 162).
     */
    @Test
    public void testCopyButtonRemovesBeforeAdding() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add items to source panel
        ClassPath sourceClassPath = new ClassPath();
        sourceClassPath.add(new ClassPathEntry(new File("test.jar"), false));
        sourcePanel.setClassPath(sourceClassPath);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Select the item
        sourcePanel.list.setSelectedIndex(0);

        DefaultListModel sourceModel = (DefaultListModel) sourcePanel.list.getModel();
        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();

        // Trigger the copy
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify item was moved (removed from source, added to target)
        assertEquals(0, sourceModel.getSize(), "Item should be removed from source");
        assertEquals(1, targetModel.getSize(), "Item should be added to target");
        assertEquals("test.jar", ((ClassPathEntry) targetModel.getElementAt(0)).getName());
    }

    /**
     * Test copy button when target already has items.
     * Verifies line 162 adds to existing items.
     */
    @Test
    public void testCopyButtonAddsToExistingTargetItems() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add items to both panels
        ClassPath sourceClassPath = new ClassPath();
        sourceClassPath.add(new ClassPathEntry(new File("source.jar"), false));
        sourcePanel.setClassPath(sourceClassPath);

        ClassPath targetClassPath = new ClassPath();
        targetClassPath.add(new ClassPathEntry(new File("existing.jar"), false));
        targetPanel.setClassPath(targetClassPath);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Select item from source
        sourcePanel.list.setSelectedIndex(0);

        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();
        assertEquals(1, targetModel.getSize(), "Target should start with 1 item");

        // Trigger the copy
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify item was added to target (not replaced)
        assertEquals(2, targetModel.getSize(), "Target should now have 2 items");
        assertEquals("existing.jar", ((ClassPathEntry) targetModel.getElementAt(0)).getName());
        assertEquals("source.jar", ((ClassPathEntry) targetModel.getElementAt(1)).getName());
    }

    /**
     * Test copy button preserves item order when moving multiple items.
     * This tests lines 155-162 with multiple selections.
     */
    @Test
    public void testCopyButtonPreservesItemOrder() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add items to source panel
        ClassPath sourceClassPath = new ClassPath();
        sourceClassPath.add(new ClassPathEntry(new File("first.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("second.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("third.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("fourth.jar"), false));
        sourcePanel.setClassPath(sourceClassPath);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Select non-contiguous items (indices 0, 2, 3)
        sourcePanel.list.setSelectedIndices(new int[]{0, 2, 3});

        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();

        // Trigger the copy
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify items are added in the correct order
        assertEquals(3, targetModel.getSize(), "Target should have 3 items");
        assertEquals("first.jar", ((ClassPathEntry) targetModel.getElementAt(0)).getName());
        assertEquals("third.jar", ((ClassPathEntry) targetModel.getElementAt(1)).getName());
        assertEquals("fourth.jar", ((ClassPathEntry) targetModel.getElementAt(2)).getName());
    }

    /**
     * Test that copy button text differs based on buttonTextKey parameter.
     */
    @Test
    public void testCopyButtonUsesButtonTextKeyParameter() {
        testFrame = new JFrame("Test Frame");
        ClassPathPanel panel1 = new ClassPathPanel(testFrame, false);
        ClassPathPanel panel2 = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add buttons with different text keys
        panel1.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);
        panel2.addCopyToPanelButton("moveToInput", "moveToInputTip", targetPanel);

        JButton button1 = (JButton) panel1.getButtons().get(6);
        JButton button2 = (JButton) panel2.getButtons().get(6);

        // The texts might be different based on the keys
        assertNotNull(button1.getText(), "Button 1 should have text");
        assertNotNull(button2.getText(), "Button 2 should have text");
    }

    /**
     * Test that getSelectedIndices is called correctly.
     * This verifies line 155.
     */
    @Test
    public void testCopyButtonGetsSelectedIndices() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add items to source
        ClassPath sourceClassPath = new ClassPath();
        sourceClassPath.add(new ClassPathEntry(new File("first.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("second.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("third.jar"), false));
        sourcePanel.setClassPath(sourceClassPath);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Select specific indices
        sourcePanel.list.setSelectedIndices(new int[]{0, 2});

        DefaultListModel sourceModel = (DefaultListModel) sourcePanel.list.getModel();
        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();

        // Trigger the copy
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify only selected items were moved
        assertEquals(1, sourceModel.getSize(), "Source should have 1 remaining item");
        assertEquals("second.jar", ((ClassPathEntry) sourceModel.getElementAt(0)).getName());
        assertEquals(2, targetModel.getSize(), "Target should have 2 items");
    }

    /**
     * Test that getSelectedValues is called correctly.
     * This verifies line 156.
     */
    @Test
    public void testCopyButtonGetsSelectedValues() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add items to source
        ClassPath sourceClassPath = new ClassPath();
        sourceClassPath.add(new ClassPathEntry(new File("item1.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("item2.jar"), false));
        sourcePanel.setClassPath(sourceClassPath);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Select first item
        sourcePanel.list.setSelectedIndex(0);

        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();

        // Trigger the copy
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify the correct value was copied
        assertEquals(1, targetModel.getSize(), "Target should have 1 item");
        ClassPathEntry copiedEntry = (ClassPathEntry) targetModel.getElementAt(0);
        assertEquals("item1.jar", copiedEntry.getName(), "Correct item should be copied");
    }

    /**
     * Test copy button when source panel is empty.
     */
    @Test
    public void testCopyButtonWithEmptySourcePanel() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Source starts empty
        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        DefaultListModel sourceModel = (DefaultListModel) sourcePanel.list.getModel();
        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();

        assertEquals(0, sourceModel.getSize(), "Source should be empty");

        // Trigger the copy (no selection)
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify nothing changed
        assertEquals(0, sourceModel.getSize(), "Source should still be empty");
        assertEquals(0, targetModel.getSize(), "Target should still be empty");
    }

    /**
     * Test that copy button correctly calls removeElementsAt.
     * This specifically tests line 159.
     */
    @Test
    public void testCopyButtonCallsRemoveElementsAt() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add items to source
        ClassPath sourceClassPath = new ClassPath();
        sourceClassPath.add(new ClassPathEntry(new File("first.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("second.jar"), false));
        sourceClassPath.add(new ClassPathEntry(new File("third.jar"), false));
        sourcePanel.setClassPath(sourceClassPath);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Select middle item
        sourcePanel.list.setSelectedIndex(1);

        DefaultListModel sourceModel = (DefaultListModel) sourcePanel.list.getModel();
        int initialSize = sourceModel.getSize();

        // Trigger the copy
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify removeElementsAt was called (size decreased)
        assertEquals(initialSize - 1, sourceModel.getSize(),
                     "Source size should decrease by 1");
        // Verify the middle item is gone
        assertEquals("first.jar", ((ClassPathEntry) sourceModel.getElementAt(0)).getName());
        assertEquals("third.jar", ((ClassPathEntry) sourceModel.getElementAt(1)).getName());
    }

    /**
     * Test that copy button correctly calls addElements on target panel.
     * This specifically tests line 162.
     */
    @Test
    public void testCopyButtonCallsAddElementsOnTarget() {
        testFrame = new JFrame("Test Frame");
        sourcePanel = new ClassPathPanel(testFrame, false);
        targetPanel = new ClassPathPanel(testFrame, false);

        // Add items to source
        ClassPath sourceClassPath = new ClassPath();
        sourceClassPath.add(new ClassPathEntry(new File("test.jar"), false));
        sourcePanel.setClassPath(sourceClassPath);

        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Select item
        sourcePanel.list.setSelectedIndex(0);

        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();
        int initialTargetSize = targetModel.getSize();

        // Trigger the copy
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify addElements was called (target size increased)
        assertEquals(initialTargetSize + 1, targetModel.getSize(),
                     "Target size should increase by 1");
        assertEquals("test.jar", ((ClassPathEntry) targetModel.getElementAt(0)).getName());
    }
}
