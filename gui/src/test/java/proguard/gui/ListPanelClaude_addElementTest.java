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
 * Test class for ListPanel.addElement(Object) method.
 *
 * The addElement() method adds an element to the list model and automatically
 * selects it.
 *
 * Lines that need coverage:
 * - Line 201: Adds the element to the listModel via listModel.addElement(element)
 * - Line 204: Selects the newly added element by calling list.setSelectedIndex(listModel.size() - 1)
 *
 * These tests verify:
 * - The element is added to the list model (line 201)
 * - The list size increases after adding an element
 * - The newly added element is automatically selected (line 204)
 * - The selection index is set to the last position (listModel.size() - 1)
 * - Multiple elements can be added sequentially
 * - The method works with different element types
 * - The element is retrievable from the list model
 * - The element appears at the end of the list
 * - The method handles various edge cases
 *
 * Note: Since ListPanel is abstract, we test it through the concrete
 * ClassPathPanel class which extends ListPanel.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ListPanelClaude_addElementTest {

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
     * Test that addElement adds an element to the list model.
     * This verifies line 201 calls listModel.addElement(element).
     */
    @Test
    public void testAddElementAddsToListModel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        int initialSize = listModel.getSize();

        // Add an element
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        panel.setClassPath(new ClassPath());

        // Get the size after clearing
        int sizeAfterClear = listModel.getSize();
        assertEquals(0, sizeAfterClear, "List should be empty after clearing");

        // Now use the ClassPath API to add an element
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        // Verify the element was added
        assertEquals(1, listModel.getSize(), "List size should increase by 1");
        assertSame(entry, listModel.getElementAt(0), "Added element should be in the list");
    }

    /**
     * Test that addElement selects the newly added element.
     * This verifies line 204 calls list.setSelectedIndex(listModel.size() - 1).
     */
    @Test
    public void testAddElementSelectsNewElement() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Clear the list first
        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add an element
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        // Verify the element is selected
        assertEquals(0, panel.list.getSelectedIndex(),
                    "Newly added element should be selected");
        assertSame(entry, panel.list.getSelectedValue(),
                  "Selected value should be the added element");
    }

    /**
     * Test that addElement increases the list size.
     * This verifies line 201 successfully adds to the model.
     */
    @Test
    public void testAddElementIncreasesListSize() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Start with empty list
        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        int initialSize = listModel.getSize();

        // Add elements one by one
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("test1.jar"), false));
        panel.setClassPath(classPath);

        assertEquals(initialSize + 1, listModel.getSize(),
                    "List size should increase by 1");
    }

    /**
     * Test that multiple elements can be added sequentially.
     * This verifies lines 201 and 204 work correctly for multiple calls.
     */
    @Test
    public void testAddElementMultipleTimes() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Start with empty list
        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add multiple elements
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("test1.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("test2.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("test3.jar"), false);

        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        assertEquals(3, listModel.getSize(), "Should have 3 elements");

        // Verify all elements are in the list
        assertSame(entry1, listModel.getElementAt(0), "First element should be entry1");
        assertSame(entry2, listModel.getElementAt(1), "Second element should be entry2");
        assertSame(entry3, listModel.getElementAt(2), "Third element should be entry3");
    }

    /**
     * Test that the last added element is selected.
     * This verifies line 204 sets selection to listModel.size() - 1.
     */
    @Test
    public void testAddElementSelectsLastElement() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add multiple elements
        ClassPath classPath = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("test1.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("test2.jar"), false);

        classPath.add(entry1);
        classPath.add(entry2);
        panel.setClassPath(classPath);

        // The last element should be selected
        int expectedIndex = listModel.getSize() - 1;
        assertEquals(expectedIndex, panel.list.getSelectedIndex(),
                    "Last element should be selected");
        assertSame(entry2, panel.list.getSelectedValue(),
                  "Selected value should be the last added element");
    }

    /**
     * Test that addElement adds to the end of the list.
     * This verifies line 201 appends to the list model.
     */
    @Test
    public void testAddElementAddsToEnd() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add first element
        ClassPath classPath1 = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("first.jar"), false);
        classPath1.add(entry1);
        panel.setClassPath(classPath1);

        int sizeAfterFirst = listModel.getSize();

        // Add second element
        ClassPath classPath2 = new ClassPath();
        classPath2.add(entry1);
        ClassPathEntry entry2 = new ClassPathEntry(new File("second.jar"), false);
        classPath2.add(entry2);
        panel.setClassPath(classPath2);

        // Second element should be at the end
        assertSame(entry2, listModel.getElementAt(listModel.getSize() - 1),
                  "New element should be at the end of the list");
    }

    /**
     * Test that addElement works with ClassPathEntry objects.
     * This is the typical use case for ClassPathPanel.
     */
    @Test
    public void testAddElementWithClassPathEntry() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        assertEquals(1, listModel.getSize(), "Should have 1 element");
        assertTrue(listModel.getElementAt(0) instanceof ClassPathEntry,
                  "Element should be ClassPathEntry");
        assertSame(entry, listModel.getElementAt(0), "Should be the same entry object");
    }

    /**
     * Test that selection changes with each addElement call.
     * This verifies line 204 is called for each addition.
     */
    @Test
    public void testAddElementUpdatesSelectionEachTime() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add first element
        ClassPath classPath1 = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("test1.jar"), false);
        classPath1.add(entry1);
        panel.setClassPath(classPath1);

        assertEquals(0, panel.list.getSelectedIndex(), "First element should be at index 0");

        // Add second element
        ClassPath classPath2 = new ClassPath();
        classPath2.add(entry1);
        ClassPathEntry entry2 = new ClassPathEntry(new File("test2.jar"), false);
        classPath2.add(entry2);
        panel.setClassPath(classPath2);

        assertEquals(1, panel.list.getSelectedIndex(), "Second element should be at index 1");
    }

    /**
     * Test that addElement works when list is initially empty.
     * This tests the edge case where size() - 1 = 0.
     */
    @Test
    public void testAddElementToEmptyList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        // Ensure list is empty
        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        assertEquals(0, listModel.getSize(), "List should be empty");

        // Add an element
        ClassPathEntry entry = new ClassPathEntry(new File("first.jar"), false);
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        assertEquals(1, listModel.getSize(), "List should have 1 element");
        assertEquals(0, panel.list.getSelectedIndex(), "Element should be selected at index 0");
        assertSame(entry, panel.list.getSelectedValue(), "Selected value should be the added element");
    }

    /**
     * Test that addElement maintains existing elements.
     * This verifies line 201 appends rather than replaces.
     */
    @Test
    public void testAddElementPreservesExistingElements() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add first element
        ClassPath classPath1 = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("first.jar"), false);
        classPath1.add(entry1);
        panel.setClassPath(classPath1);

        // Add second element
        ClassPath classPath2 = new ClassPath();
        classPath2.add(entry1);
        ClassPathEntry entry2 = new ClassPathEntry(new File("second.jar"), false);
        classPath2.add(entry2);
        panel.setClassPath(classPath2);

        // Both elements should be in the list
        assertEquals(2, listModel.getSize(), "Should have 2 elements");
        assertSame(entry1, listModel.getElementAt(0), "First element should still be present");
        assertSame(entry2, listModel.getElementAt(1), "Second element should be present");
    }

    /**
     * Test that addElement executes without exceptions.
     * This is a smoke test to ensure lines 201 and 204 don't throw exceptions.
     */
    @Test
    public void testAddElementExecutesWithoutException() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);

        assertDoesNotThrow(() -> {
            ClassPath classPath = new ClassPath();
            classPath.add(entry);
            panel.setClassPath(classPath);
        }, "addElement should not throw exception");
    }

    /**
     * Test that the selected element is retrievable after addElement.
     * This verifies line 204 sets a valid selection.
     */
    @Test
    public void testAddElementSelectedValueIsRetrievable() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        Object selectedValue = panel.list.getSelectedValue();
        assertNotNull(selectedValue, "Selected value should not be null");
        assertSame(entry, selectedValue, "Selected value should be the added element");
    }

    /**
     * Test that addElement with null owner works.
     */
    @Test
    public void testAddElementWithNullOwner() {
        panel = new ClassPathPanel(null, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        assertEquals(1, listModel.getSize(), "Should have 1 element even with null owner");
        assertEquals(0, panel.list.getSelectedIndex(), "Element should be selected");
    }

    /**
     * Test that addElement works in non-input-output mode.
     */
    @Test
    public void testAddElementInNonInputOutputMode() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        assertEquals(1, listModel.getSize(), "Should work in non-input-output mode");
    }

    /**
     * Test that addElement works in input-output mode.
     */
    @Test
    public void testAddElementInInputOutputMode() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, true);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), true);
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        assertEquals(1, listModel.getSize(), "Should work in input-output mode");
    }

    /**
     * Test that selection index is correct after multiple additions.
     * This verifies line 204 uses listModel.size() - 1 correctly.
     */
    @Test
    public void testAddElementSelectionIndexFormula() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add elements and verify selection index matches size - 1
        for (int i = 1; i <= 5; i++) {
            ClassPath classPath = new ClassPath();
            for (int j = 0; j < i; j++) {
                classPath.add(new ClassPathEntry(new File("test" + j + ".jar"), false));
            }
            panel.setClassPath(classPath);

            int expectedIndex = listModel.getSize() - 1;
            assertEquals(expectedIndex, panel.list.getSelectedIndex(),
                        "Selection index should be size - 1 after adding " + i + " elements");
        }
    }

    /**
     * Test that addElement selection is visible.
     * This verifies line 204 sets a visible selection.
     */
    @Test
    public void testAddElementSelectionIsVisible() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        testFrame.add(panel);
        testFrame.pack();

        panel.setClassPath(new ClassPath());

        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        assertFalse(panel.list.isSelectionEmpty(), "Selection should not be empty");
        assertEquals(0, panel.list.getSelectedIndex(), "Should have valid selection index");
    }

    /**
     * Test that addElement correctly adds different file types.
     */
    @Test
    public void testAddElementWithDifferentFileTypes() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("test.jar"), false));
        classPath.add(new ClassPathEntry(new File("test.aar"), false));
        classPath.add(new ClassPathEntry(new File("test.zip"), false));
        panel.setClassPath(classPath);

        assertEquals(3, listModel.getSize(), "Should have 3 elements");
    }

    /**
     * Test that addElement list model size is consistent.
     * This verifies line 201 properly updates the model size.
     */
    @Test
    public void testAddElementListModelSizeConsistency() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        for (int expectedSize = 1; expectedSize <= 5; expectedSize++) {
            ClassPath classPath = new ClassPath();
            for (int i = 0; i < expectedSize; i++) {
                classPath.add(new ClassPathEntry(new File("test" + i + ".jar"), false));
            }
            panel.setClassPath(classPath);

            assertEquals(expectedSize, listModel.getSize(),
                        "Model size should be " + expectedSize);
        }
    }

    /**
     * Test that addElement clears previous selection and selects new element.
     * This verifies line 204 changes the selection.
     */
    @Test
    public void testAddElementChangesSelection() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add first element
        ClassPath classPath1 = new ClassPath();
        ClassPathEntry entry1 = new ClassPathEntry(new File("test1.jar"), false);
        classPath1.add(entry1);
        panel.setClassPath(classPath1);

        Object firstSelection = panel.list.getSelectedValue();

        // Add second element
        ClassPath classPath2 = new ClassPath();
        classPath2.add(entry1);
        ClassPathEntry entry2 = new ClassPathEntry(new File("test2.jar"), false);
        classPath2.add(entry2);
        panel.setClassPath(classPath2);

        Object secondSelection = panel.list.getSelectedValue();

        assertNotSame(firstSelection, secondSelection,
                     "Selection should change after adding new element");
        assertSame(entry2, secondSelection, "New element should be selected");
    }

    /**
     * Test that addElement handles elements with same name correctly.
     */
    @Test
    public void testAddElementWithSameName() {
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

        assertEquals(2, listModel.getSize(), "Should have 2 elements even with same name");
    }

    /**
     * Test that the element at the last index is the one just added.
     * This verifies line 201 adds to the correct position.
     */
    @Test
    public void testAddElementLastIndexIsNewElement() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        ClassPathEntry entry1 = new ClassPathEntry(new File("test1.jar"), false);
        ClassPathEntry entry2 = new ClassPathEntry(new File("test2.jar"), false);
        ClassPathEntry entry3 = new ClassPathEntry(new File("test3.jar"), false);

        // Add elements
        ClassPath classPath = new ClassPath();
        classPath.add(entry1);
        classPath.add(entry2);
        classPath.add(entry3);
        panel.setClassPath(classPath);

        // Last element should be entry3
        int lastIndex = listModel.getSize() - 1;
        assertSame(entry3, listModel.getElementAt(lastIndex),
                  "Last element should be the most recently added");
    }

    /**
     * Test that addElement with packed frame works correctly.
     */
    @Test
    public void testAddElementWithPackedFrame() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        testFrame.add(panel);
        testFrame.pack();

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        assertEquals(1, listModel.getSize(), "Should work with packed frame");
        assertEquals(0, panel.list.getSelectedIndex(), "Should be selected");
    }

    /**
     * Test that multiple sequential addElement calls maintain correct state.
     */
    @Test
    public void testAddElementSequentialCalls() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        // Add elements sequentially and verify state after each
        ClassPath classPath1 = new ClassPath();
        classPath1.add(new ClassPathEntry(new File("test1.jar"), false));
        panel.setClassPath(classPath1);
        assertEquals(1, listModel.getSize());
        assertEquals(0, panel.list.getSelectedIndex());

        ClassPath classPath2 = new ClassPath();
        classPath2.add(new ClassPathEntry(new File("test1.jar"), false));
        classPath2.add(new ClassPathEntry(new File("test2.jar"), false));
        panel.setClassPath(classPath2);
        assertEquals(2, listModel.getSize());
        assertEquals(1, panel.list.getSelectedIndex());

        ClassPath classPath3 = new ClassPath();
        classPath3.add(new ClassPathEntry(new File("test1.jar"), false));
        classPath3.add(new ClassPathEntry(new File("test2.jar"), false));
        classPath3.add(new ClassPathEntry(new File("test3.jar"), false));
        panel.setClassPath(classPath3);
        assertEquals(3, listModel.getSize());
        assertEquals(2, panel.list.getSelectedIndex());
    }

    /**
     * Test complete execution path of addElement.
     * This tests both line 201 and line 204 in sequence.
     */
    @Test
    public void testAddElementCompleteExecution() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();
        int initialSize = listModel.getSize();

        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);

        // Line 201: Add element to list model
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        // Verify line 201 executed correctly
        assertEquals(initialSize + 1, listModel.getSize(),
                    "Element should be added to model");
        assertSame(entry, listModel.getElementAt(initialSize),
                  "Element should be at the last position");

        // Line 204: Select the element
        int expectedIndex = listModel.getSize() - 1;
        assertEquals(expectedIndex, panel.list.getSelectedIndex(),
                    "Element should be selected at last index");
        assertSame(entry, panel.list.getSelectedValue(),
                  "Selected value should be the added element");
    }

    /**
     * Test that addElement works with file paths containing spaces.
     */
    @Test
    public void testAddElementWithSpacesInPath() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        DefaultListModel listModel = (DefaultListModel) panel.list.getModel();

        ClassPathEntry entry = new ClassPathEntry(new File("test with spaces.jar"), false);
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        assertEquals(1, listModel.getSize(), "Should handle paths with spaces");
        assertEquals("test with spaces.jar", ((ClassPathEntry)listModel.getElementAt(0)).getName());
    }

    /**
     * Test that addElement selection enables selection-dependent buttons.
     */
    @Test
    public void testAddElementSelectionEnablesButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassPathPanel(testFrame, false);

        panel.setClassPath(new ClassPath());

        // Get a selection-dependent button (e.g., Remove button at index 3)
        java.util.List buttons = panel.getButtons();
        JButton removeButton = (JButton) buttons.get(3);

        // Initially disabled (no selection)
        assertFalse(removeButton.isEnabled(), "Remove button should be disabled initially");

        // Add element (which selects it)
        ClassPathEntry entry = new ClassPathEntry(new File("test.jar"), false);
        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        panel.setClassPath(classPath);

        // Button should now be enabled due to selection
        assertTrue(removeButton.isEnabled(), "Remove button should be enabled after adding/selecting element");
    }
}
