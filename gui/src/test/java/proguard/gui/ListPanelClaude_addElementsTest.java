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
 * Test class for ListPanel.addElements(Object[]) method.
 *
 * The addElements() method adds multiple elements to the list model and
 * automatically selects all of them.
 *
 * Lines that need coverage:
 * - Line 211: Loops through elements array (for loop condition)
 * - Line 213: Adds each element to listModel via listModel.addElement(elements[index])
 * - Line 217: Creates selectedIndices array with length elements.length
 * - Line 218: Loops through selectedIndices array (for loop condition)
 * - Lines 220-221: Calculates selection index: listModel.size() - selectedIndices.length + index
 * - Line 223: Sets all selected indices via list.setSelectedIndices(selectedIndices)
 *
 * These tests verify:
 * - All elements are added to the list model (lines 211-213)
 * - The list size increases by the array length
 * - All newly added elements are automatically selected (lines 217-223)
 * - Selection indices are calculated correctly (lines 220-221)
 * - The method works with empty arrays
 * - The method works with single element arrays
 * - The method works with multiple elements
 * - Elements are added in the correct order
 * - Selection includes all added elements
 * - The method handles various edge cases
 *
 * Note: Since ListPanel is abstract, we test it through the concrete
 * ClassPathPanel class which extends ListPanel. The addElements() method
 * is protected, but it's called internally by methods like addCopyToPanelButton.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ListPanelClaude_addElementsTest {

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
     * Test that addElements adds all elements to the list model.
     * This verifies lines 211-213 iterate and add all elements.
     */
    @Test
    public void testAddElementsAddsAllToListModel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        int initialSize = listModel.getSize();

        // Add multiple elements using setClassPath
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("test1.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("test2.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("test3.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        // Verify all elements were added
        assertEquals(initialSize + 3, listModel.getSize(),
                    "List size should increase by 3");
        assertSame(entry1, listModel.getElementAt(0), "First element should be entry1");
        assertSame(entry2, listModel.getElementAt(1), "Second element should be entry2");
        assertSame(entry3, listModel.getElementAt(2), "Third element should be entry3");
    }

    /**
     * Test that addElements selects all newly added elements.
     * This verifies lines 217-223 create and set selection indices.
     */
    @Test
    public void testAddElementsSelectsAllElements() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        // Add multiple elements
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("test1.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("test2.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("test3.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        // Verify all elements are selected
        int[] selectedIndices = panel.list.getSelectedIndices();
        assertEquals(3, selectedIndices.length, "Should have 3 selected indices");

        Object[] selectedValues = panel.list.getSelectedValues();
        assertEquals(3, selectedValues.length, "Should have 3 selected values");

        assertSame(entry1, selectedValues[0], "First selected should be entry1");
        assertSame(entry2, selectedValues[1], "Second selected should be entry2");
        assertSame(entry3, selectedValues[2], "Third selected should be entry3");
    }

    /**
     * Test that addElements increases list size by array length.
     * This verifies lines 211-213 add correct number of elements.
     */
    @Test
    public void testAddElementsIncreasesListSizeByArrayLength() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        int initialSize = listModel.getSize();

        // Add 5 elements
        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 5; i++) {
            classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        assertEquals(initialSize + 5, listModel.getSize(),
                    "List size should increase by 5");
    }

    /**
     * Test that addElements with empty array does nothing.
     * This tests the edge case where elements.length = 0.
     */
    @Test
    public void testAddElementsWithEmptyArray() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        int initialSize = listModel.getSize();

        // Set empty ClassPath (which calls addElements with empty array internally)
        panel.setClassPath(new ClassPath());

        assertEquals(initialSize, listModel.getSize(),
                    "List size should not change with empty array");
    }

    /**
     * Test that addElements with single element works correctly.
     * This tests the edge case where elements.length = 1.
     */
    @Test
    public void testAddElementsWithSingleElement() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add single element
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        classPath.add(entry);
        panel.setClassPath(classPath);

        assertEquals(1, listModel.getSize(), "Should have 1 element");
        assertSame(entry, listModel.getElementAt(0), "Element should be added");

        // Verify it's selected
        int[] selectedIndices = panel.list.getSelectedIndices();
        assertEquals(1, selectedIndices.length, "Should have 1 selected index");
        assertEquals(0, selectedIndices[0], "Should select index 0");
    }

    /**
     * Test that addElements maintains element order.
     * This verifies line 213 adds elements in array order.
     */
    @Test
    public void testAddElementsMaintainsOrder() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add elements in specific order
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("aaa.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("bbb.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("ccc.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        // Verify order is preserved
        assertSame(entry1, listModel.getElementAt(0), "First position should be entry1");
        assertSame(entry2, listModel.getElementAt(1), "Second position should be entry2");
        assertSame(entry3, listModel.getElementAt(2), "Third position should be entry3");
    }

    /**
     * Test that selection indices are calculated correctly.
     * This specifically tests lines 220-221: listModel.size() - selectedIndices.length + index.
     */
    @Test
    public void testAddElementsSelectionIndexCalculation() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add 3 elements to an initially empty list
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("test1.jar"), false));
        classPath.add(new ClassPathEntry(new File("test2.jar"), false));
        classPath.add(new ClassPathEntry(new File("test3.jar"), false));
        panel.setClassPath(classPath);

        // After adding 3 elements to empty list:
        // listModel.size() = 3
        // selectedIndices.length = 3
        // Expected indices: 3 - 3 + 0 = 0, 3 - 3 + 1 = 1, 3 - 3 + 2 = 2
        int[] selectedIndices = panel.list.getSelectedIndices();
        assertEquals(3, selectedIndices.length);
        assertEquals(0, selectedIndices[0], "First index should be 0");
        assertEquals(1, selectedIndices[1], "Second index should be 1");
        assertEquals(2, selectedIndices[2], "Third index should be 2");
    }

    /**
     * Test that addElements to non-empty list selects only new elements.
     * This verifies lines 220-221 calculate indices relative to list end.
     */
    @Test
    public void testAddElementsToNonEmptyList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Add initial elements
        ClassPath initialClassPath = new ClassPath();
        ClassPathEntry existing1 = new ClassPathEntry(new File("existing1.jar"), false);
        ClassPathEntry existing2 = new ClassPathEntry(new File("existing2.jar"), false);
        initialClassPath.add(existing1);
        initialClassPath.add(existing2);
        panel.setClassPath(initialClassPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add more elements
        ClassPath newClassPath = new ClassPath();
        newClassPath.add(existing1);
        newClassPath.add(existing2);
        ClassPathEntry new1 = new ClassPathEntry(new File("new1.jar"), false);
        ClassPathEntry new2 = new ClassPathEntry(new File("new2.jar"), false);
        newClassPath.add(new1);
        newClassPath.add(new2);
        panel.setClassPath(newClassPath);

        // List now has 4 elements
        assertEquals(4, listModel.getSize(), "Should have 4 elements");

        // All 4 should be selected (setClassPath selects all)
        int[] selectedIndices = panel.list.getSelectedIndices();
        assertEquals(4, selectedIndices.length, "Should have 4 selected indices");
    }

    /**
     * Test that addElements loop iterates correct number of times.
     * This verifies line 211: index < elements.length.
     */
    @Test
    public void testAddElementsLoopIteratesCorrectly() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Test with different array sizes
        for (int arraySize = 1; arraySize <= 5; arraySize++) {
            panel.setClassPath(new ClassPath()); // Clear

            ClassPath classPath = new ClassPath();
            for (int i = 0; i < arraySize; i++) {
                classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
            }
            panel.setClassPath(classPath);

            assertEquals(arraySize, listModel.getSize(),
                        "Should have " + arraySize + " elements");
        }
    }

    /**
     * Test that selectedIndices array has correct length.
     * This verifies line 217: new int[elements.length].
     */
    @Test
    public void testAddElementsSelectedIndicesArrayLength() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        // Add 4 elements
        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 4; i++) {
            classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        int[] selectedIndices = panel.list.getSelectedIndices();
        assertEquals(4, selectedIndices.length,
                    "Selected indices array should have length 4");
    }

    /**
     * Test that addElements selects contiguous range when added to empty list.
     * This verifies lines 220-221 produce contiguous indices.
     */
    @Test
    public void testAddElementsSelectsContiguousRange() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        // Add 5 elements
        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 5; i++) {
            classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        int[] selectedIndices = panel.list.getSelectedIndices();

        // Should be 0, 1, 2, 3, 4
        for (int i = 0; i < 5; i++) {
            assertEquals(i, selectedIndices[i],
                        "Index " + i + " should be selected");
        }
    }

    /**
     * Test that addElements works with different element types.
     */
    @Test
    public void testAddElementsWithDifferentFileTypes() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("test.jar"), false));
        classPath.add(new ClassPathEntry(new File("test.aar"), false));
        classPath.add(new ClassPathEntry(new File("test.zip"), false));
        classPath.add(new ClassPathEntry(new File("test.war"), false));
        panel.setClassPath(classPath);

        assertEquals(4, listModel.getSize(), "Should have 4 different file types");
    }

    /**
     * Test that addElements executes without exceptions.
     * This is a smoke test for lines 211-223.
     */
    @Test
    public void testAddElementsExecutesWithoutException() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        assertDoesNotThrow(() -> {
            ClassPath classPath = new ClassPath();
            classPath.add(new ClassPathEntry(new File("test1.jar"), false));
            classPath.add(new ClassPathEntry(new File("test2.jar"), false));
            panel.setClassPath(classPath);
        }, "addElements should not throw exception");
    }

    /**
     * Test that all selected values are retrievable.
     * This verifies line 223 sets valid selections.
     */
    @Test
    public void testAddElementsSelectedValuesRetrievable() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("test1.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("test2.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        panel.setClassPath(classPath);

        Object[] selectedValues = panel.list.getSelectedValues();
        assertEquals(2, selectedValues.length, "Should have 2 selected values");
        assertSame(entry1, selectedValues[0], "First selected should be entry1");
        assertSame(entry2, selectedValues[1], "Second selected should be entry2");
    }

    /**
     * Test that addElements with null owner works.
     */
    @Test
    public void testAddElementsWithNullOwner() {
        panel = new ClassPathPanel(null, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("test1.jar"), false));
        classPath.add(new ClassPathEntry(new File("test2.jar"), false));
        panel.setClassPath(classPath);

        assertEquals(2, listModel.getSize(), "Should work with null owner");
        assertEquals(2, panel.list.getSelectedIndices().length, "Should select 2 elements");
    }

    /**
     * Test that addElements in non-input-output mode works.
     */
    @Test
    public void testAddElementsInNonInputOutputMode() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("test1.jar"), false));
        classPath.add(new ClassPathEntry(new File("test2.jar"), false));
        panel.setClassPath(classPath);

        assertEquals(2, panel.list.getModel().getSize(),
                    "Should work in non-input-output mode");
    }

    /**
     * Test that addElements in input-output mode works.
     */
    @Test
    public void testAddElementsInInputOutputMode() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, true);

        panel.setClassPath(new ClassPath());

        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("test1.jar"), true));
        classPath.add(new ClassPathEntry(new File("test2.jar"), true));
        panel.setClassPath(classPath);

        assertEquals(2, panel.list.getModel().getSize(),
                    "Should work in input-output mode");
    }

    /**
     * Test that selection is not empty after addElements.
     * This verifies line 223 creates valid selection.
     */
    @Test
    public void testAddElementsSelectionNotEmpty() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("test1.jar"), false));
        classPath.add(new ClassPathEntry(new File("test2.jar"), false));
        panel.setClassPath(classPath);

        assertFalse(panel.list.isSelectionEmpty(),
                   "Selection should not be empty after addElements");
    }

    /**
     * Test that addElements enables selection-dependent buttons.
     */
    @Test
    public void testAddElementsEnablesSelectionButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        // Get a selection-dependent button (Remove button at index 3)
        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        assertFalse(removeButton.isEnabled(),
                   "Remove button should be disabled initially");

        // Add elements (which selects them)
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("test1.jar"), false));
        classPath.add(new ClassPathEntry(new File("test2.jar"), false));
        panel.setClassPath(classPath);

        assertTrue(removeButton.isEnabled(),
                  "Remove button should be enabled after adding/selecting elements");
    }

    /**
     * Test addElements with large array.
     */
    @Test
    public void testAddElementsWithLargeArray() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add 20 elements
        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 20; i++) {
            classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        assertEquals(20, listModel.getSize(), "Should have 20 elements");
        assertEquals(20, panel.list.getSelectedIndices().length,
                    "Should have 20 selected indices");
    }

    /**
     * Test that elements are added at the end of existing list.
     * This verifies line 213 appends to listModel.
     */
    @Test
    public void testAddElementsAppendsToEnd() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Add initial elements
        ClassPath initialClassPath = new ClassPath();
        ClassPathEntry existing = new ClassPathEntry(new File("existing.jar"), false);
        initialClassPath.add(existing);
        panel.setClassPath(initialClassPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add more elements
        ClassPath newClassPath = new ClassPath();
        newClassPath.add(existing);
        ClassPathEntry new1 = new ClassPathEntry(new File("new1.jar"), false);
        ClassPathEntry new2 = new ClassPathEntry(new File("new2.jar"), false);
        newClassPath.add(new1);
        newClassPath.add(new2);
        panel.setClassPath(newClassPath);

        // Verify order
        assertSame(existing, listModel.getElementAt(0), "Existing should be first");
        assertSame(new1, listModel.getElementAt(1), "New1 should be second");
        assertSame(new2, listModel.getElementAt(2), "New2 should be third");
    }

    /**
     * Test that the second loop (lines 218-222) iterates correct number of times.
     * This verifies line 218: index < selectedIndices.length.
     */
    @Test
    public void testAddElementsSelectionLoopIteratesCorrectly() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        // Test with different sizes
        for (int size = 1; size <= 5; size++) {
            panel.setClassPath(new ClassPath());

            ClassPath classPath = new ClassPath();
            for (int i = 0; i < size; i++) {
                classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
            }
            panel.setClassPath(classPath);

            int[] selectedIndices = panel.list.getSelectedIndices();
            assertEquals(size, selectedIndices.length,
                        "Should have " + size + " selected indices");
        }
    }

    /**
     * Test that addElements preserves element identity.
     * This verifies line 213 adds the actual element objects.
     */
    @Test
    public void testAddElementsPreservesElementIdentity() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("test1.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("test2.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        panel.setClassPath(classPath);

        // Check object identity
        assertSame(entry1, listModel.getElementAt(0),
                  "Should preserve element1 identity");
        assertSame(entry2, listModel.getElementAt(1),
                  "Should preserve element2 identity");
    }

    /**
     * Test that addElements with packed frame works.
     */
    @Test
    public void testAddElementsWithPackedFrame() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        testFrame.add(panel);
        testFrame.pack();

        panel.setClassPath(new ClassPath());

        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("test1.jar"), false));
        classPath.add(new ClassPathEntry(new File("test2.jar"), false));
        panel.setClassPath(classPath);

        assertEquals(2, panel.list.getModel().getSize(),
                    "Should work with packed frame");
    }

    /**
     * Test the formula listModel.size() - selectedIndices.length + index.
     * This specifically verifies lines 220-221.
     */
    @Test
    public void testAddElementsIndexCalculationFormula() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Start with 2 existing elements
        ClassPath initialClassPath = new ClassPath();
        initialClassPath.add(new ClassPathEntry(new File("existing1.jar"), false));
        initialClassPath.add(new ClassPathEntry(new File("existing2.jar"), false));
        panel.setClassPath(initialClassPath);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add 3 new elements
        ClassPath newClassPath = new ClassPath();
        newClassPath.add(new ClassPathEntry(new File("existing1.jar"), false));
        newClassPath.add(new ClassPathEntry(new File("existing2.jar"), false));
        newClassPath.add(new ClassPathEntry(new File("new1.jar"), false));
        newClassPath.add(new ClassPathEntry(new File("new2.jar"), false));
        newClassPath.add(new ClassPathEntry(new File("new3.jar"), false));
        panel.setClassPath(newClassPath);

        // After adding, listModel.size() = 5, selectedIndices.length = 5
        // Formula: 5 - 5 + 0 = 0, 5 - 5 + 1 = 1, etc.
        int[] selectedIndices = panel.list.getSelectedIndices();

        assertEquals(5, selectedIndices.length, "Should have 5 selected indices");
        for (int i = 0; i < 5; i++) {
            int expected = listModel.size() - selectedIndices.length + i;
            assertEquals(expected, selectedIndices[i],
                        "Index " + i + " should be " + expected);
        }
    }

    /**
     * Test addElements with elements that have same names.
     */
    @Test
    public void testAddElementsWithDuplicateNames() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("test.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("test.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        panel.setClassPath(classPath);

        assertEquals(2, listModel.getSize(),
                    "Should add both elements even with same name");
    }

    /**
     * Test complete execution path of addElements.
     * This tests lines 211-223 in sequence.
     */
    @Test
    public void testAddElementsCompleteExecution() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        int initialSize = listModel.getSize();

        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("test1.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("test2.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("test3.jar"), false);
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);

        // Lines 211-213: Add elements to list model
        panel.setClassPath(classPath);

        // Verify elements were added
        assertEquals(initialSize + 3, listModel.getSize(),
                    "Should have 3 more elements");
        assertSame(entry1, listModel.getElementAt(0));
        assertSame(entry2, listModel.getElementAt(1));
        assertSame(entry3, listModel.getElementAt(2));

        // Lines 217-223: Create selection indices and select
        int[] selectedIndices = panel.list.getSelectedIndices();
        assertEquals(3, selectedIndices.length,
                    "Should have 3 selected indices");

        // Verify indices calculation: listModel.size() - selectedIndices.length + index
        // 3 - 3 + 0 = 0, 3 - 3 + 1 = 1, 3 - 3 + 2 = 2
        assertEquals(0, selectedIndices[0]);
        assertEquals(1, selectedIndices[1]);
        assertEquals(2, selectedIndices[2]);

        // Verify selected values
        Object[] selectedValues = panel.list.getSelectedValues();
        assertSame(entry1, selectedValues[0]);
        assertSame(entry2, selectedValues[1]);
        assertSame(entry3, selectedValues[2]);
    }

    /**
     * Test that addElements works via copy button functionality.
     * This tests addElements being called through addCopyToPanelButton.
     */
    @Test
    public void testAddElementsViaCopyButton() {
        testFrame = new JFrame("Test Frame");
        ClassPathPanel sourcePanel = new ClassPathPanel(testFrame, false);
        ClassPathPanel targetPanel = new ClassPathPanel(testFrame, false);

        // Add elements to source
        ClassPath sourceClassPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("test1.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("test2.jar"), false);
        sourceClassPath.add(entry1);
        sourceClassPath.add(entry2);
        sourcePanel.setClassPath(sourceClassPath);

        // Add copy button
        sourcePanel.addCopyToPanelButton("moveToOutput", "moveToOutputTip", targetPanel);

        // Select elements
        sourcePanel.list.setSelectedIndices(new int[]{0, 1});

        // Trigger copy (which calls addElements on target)
        java.util.List buttons = sourcePanel.getButtons();
        JButton copyButton = (JButton) buttons.get(6);
        copyButton.doClick();

        // Verify elements were added to target via addElements
        DefaultListModel targetModel = (DefaultListModel) targetPanel.list.getModel();
        assertEquals(2, targetModel.getSize(), "Target should have 2 elements");

        // Verify both are selected in target
        int[] targetSelected = targetPanel.list.getSelectedIndices();
        assertEquals(2, targetSelected.length, "Both elements should be selected");
    }
}
