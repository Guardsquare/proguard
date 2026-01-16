package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassPath;
import proguard.ClassPathEntry;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ListPanel.moveElementsAt(int[], int) method.
 *
 * The moveElementsAt() method moves elements at specified indices by a given offset.
 * It does this by: remembering the selected elements, removing them, updating their
 * indices by the offset, and reinserting them at the new positions.
 *
 * Lines that need coverage:
 * - Line 230: Gets selected values via list.getSelectedValues()
 * - Line 233: Removes elements at specified indices via removeElementsAt(indices)
 * - Line 236: Loops through indices array (for loop condition: index < indices.length)
 * - Line 238: Updates each index by adding offset: indices[index] += offset
 * - Line 242: Reinserts elements at updated indices via insertElementsAt(selectedElements, indices)
 *
 * These tests verify:
 * - Elements are remembered before removal (line 230)
 * - Elements are removed from original positions (line 233)
 * - Indices are updated by the offset (lines 236-238)
 * - Elements are reinserted at new positions (line 242)
 * - The method works with positive offsets (move down)
 * - The method works with negative offsets (move up)
 * - The method works with zero offset (no movement)
 * - The method works with single element
 * - The method works with multiple elements
 * - Selection is maintained after move
 *
 * Note: Since ListPanel is abstract, we test it through the concrete
 * ClassPathPanel class which extends ListPanel. The moveElementsAt() method
 * is protected, but it's called by the Up and Down button actions.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ListPanelClaude_moveElementsAtTest {

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
     * Test that moveElementsAt moves element down by positive offset.
     * This tests lines 230, 233, 236-238, 242 with positive offset.
     */
    @Test
    public void testMoveElementsAtMovesElementDown() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("first.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("second.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("third.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select first element and move down
        panel.list.setSelectedIndex(0);

        // Get Down button and trigger it (which calls moveElementsAt with offset +1)
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // Verify element moved down
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName());
        assertEquals("third.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName());
    }

    /**
     * Test that moveElementsAt moves element up by negative offset.
     * This tests lines 230, 233, 236-238, 242 with negative offset.
     */
    @Test
    public void testMoveElementsAtMovesElementUp() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("first.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("second.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("third.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select second element and move up
        panel.list.setSelectedIndex(1);

        // Get Up button and trigger it (which calls moveElementsAt with offset -1)
        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Verify element moved up
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName());
        assertEquals("third.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName());
    }

    /**
     * Test that moveElementsAt moves multiple elements together.
     * This tests lines 236-238 with multiple indices.
     */
    @Test
    public void testMoveElementsAtMovesMultipleElements() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("first.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("second.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("third.jar"), false);
        ClassPathEntry entry4 = new ClassPathEntry(new File("fourth.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        classPath.add(entry4);
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select first two elements and move down
        panel.list.setSelectedIndices(new int[]{0, 1});

        // Get Down button and trigger it
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // Verify both elements moved down together
        assertEquals("third.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName());
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName());
        assertEquals("fourth.jar", ((ClassPathEntry) listModel.getElementAt(3)).getName());
    }

    /**
     * Test that moveElementsAt remembers selected elements.
     * This verifies line 230 saves the selected values.
     */
    @Test
    public void testMoveElementsAtRemembersSelectedElements() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("first.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("second.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("third.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select second element
        panel.list.setSelectedIndex(1);
        Object selectedBefore = panel.list.getSelectedValue();

        // Move up
        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Verify the same element is still selected (now at index 0)
        Object selectedAfter = panel.list.getSelectedValue();
        assertSame(selectedBefore, selectedAfter,
                  "Same element should remain selected after move");
        assertEquals(entry2, selectedAfter, "Selected element should be entry2");
    }

    /**
     * Test that moveElementsAt updates indices correctly.
     * This specifically tests line 238: indices[index] += offset.
     */
    @Test
    public void testMoveElementsAtUpdatesIndices() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("first.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("second.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("third.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        // Select middle element (index 1) and move down (offset +1)
        panel.list.setSelectedIndex(1);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // After move, selected element should be at index 2 (1 + 1)
        assertEquals(2, panel.list.getSelectedIndex(),
                    "Selected index should be updated by offset");
    }

    /**
     * Test that moveElementsAt loop iterates correct number of times.
     * This tests line 236: index < indices.length.
     */
    @Test
    public void testMoveElementsAtLoopIteratesCorrectly() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 5; i++) {
            classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        // Select 3 elements and move them
        panel.list.setSelectedIndices(new int[]{1, 2, 3});

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // All 3 selected indices should be updated
        int[] selectedIndices = panel.list.getSelectedIndices();
        assertEquals(3, selectedIndices.length,
                    "All selected elements should remain selected");
        assertEquals(2, selectedIndices[0], "First should be at index 2");
        assertEquals(3, selectedIndices[1], "Second should be at index 3");
        assertEquals(4, selectedIndices[2], "Third should be at index 4");
    }

    /**
     * Test that moveElementsAt maintains selection after move.
     * This verifies line 242 reinserts with proper selection.
     */
    @Test
    public void testMoveElementsAtMaintainsSelection() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("first.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("second.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("third.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        // Select and move
        panel.list.setSelectedIndex(1);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Selection should not be empty
        assertFalse(panel.list.isSelectionEmpty(),
                   "Selection should be maintained after move");

        // The moved element should still be selected
        assertEquals(entry2, panel.list.getSelectedValue(),
                    "Moved element should remain selected");
    }

    /**
     * Test that moveElementsAt with offset 0 doesn't change positions.
     * This tests the edge case where offset = 0.
     */
    @Test
    public void testMoveElementsAtWithZeroOffset() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("first.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("second.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("third.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select middle element
        panel.list.setSelectedIndex(1);

        // Note: Can't directly call with offset 0, but verify order is stable
        // after any operation that doesn't change position
        Object[] valuesBefore = new Object[listModel.getSize()];
        for (int i = 0; i < listModel.getSize(); i++) {
            valuesBefore[i] = listModel.getElementAt(i);
        }

        // Move down then up (net offset 0)
        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        JButton upButton = (JButton) buttons.get(4);

        downButton.doClick();
        upButton.doClick();

        // Order should be back to original
        for (int i = 0; i < listModel.getSize(); i++) {
            assertSame(valuesBefore[i], listModel.getElementAt(i),
                      "Element at index " + i + " should be unchanged");
        }
    }

    /**
     * Test that moveElementsAt calls removeElementsAt.
     * This verifies line 233.
     */
    @Test
    public void testMoveElementsAtRemovesElements() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        classPath.add(new ClassPathEntry(new File("third.jar"), false));
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        int initialSize = listModel.getSize();

        // During move, elements are temporarily removed
        // We can't directly observe this, but we can verify the end result is correct
        panel.list.setSelectedIndex(1);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Size should be same (removed then reinserted)
        assertEquals(initialSize, listModel.getSize(),
                    "List size should remain the same after move");
    }

    /**
     * Test that moveElementsAt calls insertElementsAt.
     * This verifies line 242.
     */
    @Test
    public void testMoveElementsAtReinsertsElements() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("first.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("second.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("third.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select and move
        panel.list.setSelectedIndex(0);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // Element should be reinserted at new position
        assertEquals(entry1, listModel.getElementAt(1),
                    "Element should be reinserted at new position");
    }

    /**
     * Test that moveElementsAt preserves element identity.
     * This verifies line 230 and 242 work with actual objects.
     */
    @Test
    public void testMoveElementsAtPreservesElementIdentity() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("first.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("second.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("third.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select and move
        panel.list.setSelectedIndex(1);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // The same object should be at the new position
        assertSame(entry2, listModel.getElementAt(0),
                  "Same object instance should be at new position");
    }

    /**
     * Test moveElementsAt with single element.
     * This tests edge case where indices.length = 1.
     */
    @Test
    public void testMoveElementsAtWithSingleElement() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        classPath.add(new ClassPathEntry(new File("third.jar"), false));
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select single element and move
        panel.list.setSelectedIndex(1);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Verify single element moved
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals(1, panel.list.getSelectedIndices().length,
                    "Single element should remain selected");
    }

    /**
     * Test moveElementsAt executes without exceptions.
     * This is a smoke test for lines 230-242.
     */
    @Test
    public void testMoveElementsAtExecutesWithoutException() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        panel.setClassPath(classPath);

        panel.list.setSelectedIndex(0);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        assertDoesNotThrow(() -> downButton.doClick(),
                          "moveElementsAt should not throw exception");
    }

    /**
     * Test that moveElementsAt works with null owner.
     */
    @Test
    public void testMoveElementsAtWithNullOwner() {
        panel = new ClassPathPanel(null, false);

        // Set up initial list
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        panel.list.setSelectedIndex(1);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName(),
                    "Should work with null owner");
    }

    /**
     * Test moveElementsAt in non-input-output mode.
     */
    @Test
    public void testMoveElementsAtInNonInputOutputMode() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        panel.setClassPath(classPath);

        panel.list.setSelectedIndex(0);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        assertEquals("second.jar",
                    ((ClassPathEntry) panel.list.getModel().getElementAt(0)).getName(),
                    "Should work in non-input-output mode");
    }

    /**
     * Test moveElementsAt in input-output mode.
     */
    @Test
    public void testMoveElementsAtInInputOutputMode() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, true);

        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), true));
        classPath.add(new ClassPathEntry(new File("second.jar"), true));
        panel.setClassPath(classPath);

        panel.list.setSelectedIndex(0);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(6); // Different index in input-output mode
        downButton.doClick();

        assertEquals("second.jar",
                    ((ClassPathEntry) panel.list.getModel().getElementAt(0)).getName(),
                    "Should work in input-output mode");
    }

    /**
     * Test moveElementsAt with large offset.
     */
    @Test
    public void testMoveElementsAtWithLargeOffset() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 5; i++) {
            classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select first element and move down multiple times
        panel.list.setSelectedIndex(0);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);

        // Move down 3 times (offset +3 total)
        downButton.doClick();
        downButton.doClick();
        downButton.doClick();

        // First element should be at index 3 now
        assertEquals("test0.jar", ((ClassPathEntry) listModel.getElementAt(3)).getName());
        assertEquals(3, panel.list.getSelectedIndex());
    }

    /**
     * Test moveElementsAt maintains list size.
     */
    @Test
    public void testMoveElementsAtMaintainsListSize() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        classPath.add(new ClassPathEntry(new File("third.jar"), false));
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        int sizeBefore = listModel.getSize();

        panel.list.setSelectedIndex(1);

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        assertEquals(sizeBefore, listModel.getSize(),
                    "List size should remain constant after move");
    }

    /**
     * Test that moveElementsAt correctly moves non-contiguous selections.
     */
    @Test
    public void testMoveElementsAtWithNonContiguousSelection() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 5; i++) {
            classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select indices 1 and 3 (non-contiguous)
        panel.list.setSelectedIndices(new int[]{1, 3});

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Both should move up
        assertEquals("test1.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("test3.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName());
    }

    /**
     * Test moveElementsAt with packed frame.
     */
    @Test
    public void testMoveElementsAtWithPackedFrame() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        testFrame.add(panel);
        testFrame.pack();

        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("first.jar"), false));
        classPath.add(new ClassPathEntry(new File("second.jar"), false));
        panel.setClassPath(classPath);

        panel.list.setSelectedIndex(0);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        assertEquals("second.jar",
                    ((ClassPathEntry) panel.list.getModel().getElementAt(0)).getName(),
                    "Should work with packed frame");
    }

    /**
     * Test that offset addition works correctly.
     * This specifically tests line 238.
     */
    @Test
    public void testMoveElementsAtOffsetAddition() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 4; i++) {
            classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        // Select element at index 1, move down (offset +1)
        panel.list.setSelectedIndex(1);

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // Element should now be at index 2 (1 + 1)
        assertEquals(2, panel.list.getSelectedIndex(),
                    "Index should be increased by offset");

        // Select element at index 2, move up (offset -1)
        panel.list.setSelectedIndex(2);

        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Element should now be at index 1 (2 + (-1))
        assertEquals(1, panel.list.getSelectedIndex(),
                    "Index should be decreased by negative offset");
    }

    /**
     * Test complete execution path of moveElementsAt.
     * This tests lines 230-242 in sequence.
     */
    @Test
    public void testMoveElementsAtCompleteExecution() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("first.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("second.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("third.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select second element (index 1)
        panel.list.setSelectedIndex(1);

        // Line 230: Remember selected elements
        Object selectedBefore = panel.list.getSelectedValue();
        assertSame(entry2, selectedBefore);

        // Lines 233, 236-238, 242: Move up (offset -1)
        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // Verify line 233: Element removed from index 1
        // Verify line 238: Index updated: 1 + (-1) = 0
        // Verify line 242: Element reinserted at index 0
        assertEquals(entry2, listModel.getElementAt(0),
                    "Element should be at new position (index 0)");

        // Verify selection maintained
        assertEquals(0, panel.list.getSelectedIndex(),
                    "Selection should be at new index");
        assertSame(entry2, panel.list.getSelectedValue(),
                  "Same element should remain selected");

        // Verify order
        assertEquals("second.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("first.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName());
        assertEquals("third.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName());
    }

    /**
     * Test moveElementsAt with multiple elements and positive offset.
     */
    @Test
    public void testMoveElementsAtMultipleElementsPositiveOffset() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 5; i++) {
            classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select indices 0, 1 and move down
        panel.list.setSelectedIndices(new int[]{0, 1});

        java.util.List buttons = panel.getButtons();
        JButton downButton = (JButton) buttons.get(5);
        downButton.doClick();

        // test2 should be first, then test0, test1
        assertEquals("test2.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("test0.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName());
        assertEquals("test1.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName());

        // Selection should be at indices 1, 2
        int[] selectedIndices = panel.list.getSelectedIndices();
        assertEquals(2, selectedIndices.length);
        assertEquals(1, selectedIndices[0]);
        assertEquals(2, selectedIndices[1]);
    }

    /**
     * Test moveElementsAt with multiple elements and negative offset.
     */
    @Test
    public void testMoveElementsAtMultipleElementsNegativeOffset() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 5; i++) {
            classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Select indices 2, 3 and move up
        panel.list.setSelectedIndices(new int[]{2, 3});

        java.util.List buttons = panel.getButtons();
        JButton upButton = (JButton) buttons.get(4);
        upButton.doClick();

        // test2, test3 should move to positions 1, 2
        assertEquals("test0.jar", ((ClassPathEntry) listModel.getElementAt(0)).getName());
        assertEquals("test2.jar", ((ClassPathEntry) listModel.getElementAt(1)).getName());
        assertEquals("test3.jar", ((ClassPathEntry) listModel.getElementAt(2)).getName());
        assertEquals("test1.jar", ((ClassPathEntry) listModel.getElementAt(3)).getName());

        // Selection should be at indices 1, 2
        int[] selectedIndices = panel.list.getSelectedIndices();
        assertEquals(2, selectedIndices.length);
        assertEquals(1, selectedIndices[0]);
        assertEquals(2, selectedIndices[1]);
    }
}
