package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassPath;
import proguard.ClassPathEntry;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Tests for proguard.gui.ListPanel.setElementsAt([Ljava/lang/Object;[I)V
 *
 * This test class covers the setElementsAt method which replaces multiple elements
 * at specified indices in the list and selects all replaced elements.
 *
 * Lines to cover:
 * - Line 269: for (int index = 0; index < elements.length; index++)
 * - Line 271: listModel.setElementAt(elements[index], indices[index]);
 * - Line 275: list.setSelectedIndices(indices);
 */
public class ListPanelClaude_setElementsAtTest {

    private JFrame frame;
    private ClassPathPanel panel;

    @BeforeEach
    void setUp() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping test in headless environment");
        frame = new JFrame();
        panel = new ClassPathPanel(frame, true);
        frame.getContentPane().add(panel);
        frame.pack();
    }

    @AfterEach
    void tearDown() {
        if (frame != null) {
            frame.dispose();
        }
    }

    // Tests for line 269: for loop iteration

    @Test
    void testSetElementsAt_EmptyArrays_LoopNotExecuted() {
        // Initialize with some elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/initial/path.jar"), false));
        panel.setClassPath(classPath);

        assertEquals(1, panel.getClassPath().size());

        // Set empty arrays - loop should not execute
        Object[] elements = new Object[0];
        int[] indices = new int[0];

        // Access through reflection since setElementsAt is protected
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify list unchanged
        assertEquals(1, panel.getClassPath().size());
    }

    @Test
    void testSetElementsAt_SingleElement_LoopExecutesOnce() {
        // Initialize with two elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first/path.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second/path.jar"), false));
        panel.setClassPath(classPath);

        assertEquals(2, panel.getClassPath().size());

        // Replace single element - loop executes once (index 0 < 1)
        Object[] elements = new Object[]{new ClassPathEntry(new java.io.File("/replaced.jar"), false)};
        int[] indices = new int[]{0};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify replacement
        ClassPath result = panel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("replaced.jar"));
        assertTrue(result.get(1).getName().contains("second"));
    }

    @Test
    void testSetElementsAt_MultipleElements_LoopExecutesMultipleTimes() {
        // Initialize with four elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        panel.setClassPath(classPath);

        assertEquals(4, panel.getClassPath().size());

        // Replace three elements - loop executes three times (0, 1, 2 < 3)
        Object[] elements = new Object[]{
            new ClassPathEntry(new java.io.File("/new1.jar"), false),
            new ClassPathEntry(new java.io.File("/new2.jar"), false),
            new ClassPathEntry(new java.io.File("/new3.jar"), false)
        };
        int[] indices = new int[]{0, 1, 2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify all three replacements
        ClassPath result = panel.getClassPath();
        assertEquals(4, result.size());
        assertTrue(result.get(0).getName().contains("new1.jar"));
        assertTrue(result.get(1).getName().contains("new2.jar"));
        assertTrue(result.get(2).getName().contains("new3.jar"));
        assertTrue(result.get(3).getName().contains("fourth.jar"));
    }

    // Tests for line 271: listModel.setElementAt(elements[index], indices[index])

    @Test
    void testSetElementsAt_ReplacesElementAtFirstIndex() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Replace at index 0
        Object[] elements = new Object[]{new ClassPathEntry(new java.io.File("/replaced.jar"), false)};
        int[] indices = new int[]{0};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify element at index 0 was replaced
        ClassPath result = panel.getClassPath();
        assertTrue(result.get(0).getName().contains("replaced.jar"));
    }

    @Test
    void testSetElementsAt_ReplacesElementAtLastIndex() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Replace at last index (2)
        Object[] elements = new Object[]{new ClassPathEntry(new java.io.File("/replaced.jar"), false)};
        int[] indices = new int[]{2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify element at index 2 was replaced
        ClassPath result = panel.getClassPath();
        assertTrue(result.get(2).getName().contains("replaced.jar"));
        assertTrue(result.get(0).getName().contains("first.jar"));
        assertTrue(result.get(1).getName().contains("second.jar"));
    }

    @Test
    void testSetElementsAt_ReplacesElementsAtNonContiguousIndices() {
        // Initialize with five elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fifth.jar"), false));
        panel.setClassPath(classPath);

        // Replace at non-contiguous indices 0, 2, 4
        Object[] elements = new Object[]{
            new ClassPathEntry(new java.io.File("/new1.jar"), false),
            new ClassPathEntry(new java.io.File("/new3.jar"), false),
            new ClassPathEntry(new java.io.File("/new5.jar"), false)
        };
        int[] indices = new int[]{0, 2, 4};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify specific indices were replaced
        ClassPath result = panel.getClassPath();
        assertTrue(result.get(0).getName().contains("new1.jar"));
        assertTrue(result.get(1).getName().contains("second.jar")); // Unchanged
        assertTrue(result.get(2).getName().contains("new3.jar"));
        assertTrue(result.get(3).getName().contains("fourth.jar")); // Unchanged
        assertTrue(result.get(4).getName().contains("new5.jar"));
    }

    @Test
    void testSetElementsAt_ReplacesMultipleContiguousElements() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        panel.setClassPath(classPath);

        // Replace contiguous elements at indices 1, 2
        Object[] elements = new Object[]{
            new ClassPathEntry(new java.io.File("/replaced2.jar"), false),
            new ClassPathEntry(new java.io.File("/replaced3.jar"), false)
        };
        int[] indices = new int[]{1, 2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify replacements
        ClassPath result = panel.getClassPath();
        assertEquals(4, result.size());
        assertTrue(result.get(0).getName().contains("first.jar"));
        assertTrue(result.get(1).getName().contains("replaced2.jar"));
        assertTrue(result.get(2).getName().contains("replaced3.jar"));
        assertTrue(result.get(3).getName().contains("fourth.jar"));
    }

    @Test
    void testSetElementsAt_PreservesListSize() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        int originalSize = panel.getClassPath().size();

        // Replace elements
        Object[] elements = new Object[]{
            new ClassPathEntry(new java.io.File("/new1.jar"), false),
            new ClassPathEntry(new java.io.File("/new2.jar"), false)
        };
        int[] indices = new int[]{0, 2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify size unchanged (replacement, not insertion)
        assertEquals(originalSize, panel.getClassPath().size());
    }

    @Test
    void testSetElementsAt_CorrectsParallelArrayMapping() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Test parallel array mapping: elements[0]->indices[0], elements[1]->indices[1]
        Object[] elements = new Object[]{
            new ClassPathEntry(new java.io.File("/for-index-2.jar"), false),
            new ClassPathEntry(new java.io.File("/for-index-0.jar"), false)
        };
        int[] indices = new int[]{2, 0}; // Note: out of order

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify correct parallel mapping
        ClassPath result = panel.getClassPath();
        assertTrue(result.get(0).getName().contains("for-index-0.jar"));
        assertTrue(result.get(1).getName().contains("second.jar"));
        assertTrue(result.get(2).getName().contains("for-index-2.jar"));
    }

    // Tests for line 275: list.setSelectedIndices(indices)

    @Test
    void testSetElementsAt_SelectsSingleReplacedElement() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Clear any existing selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.clearSelection();
        } catch (Exception e) {
            fail("Failed to clear selection: " + e.getMessage());
        }

        // Replace element at index 1
        Object[] elements = new Object[]{new ClassPathEntry(new java.io.File("/replaced.jar"), false)};
        int[] indices = new int[]{1};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            int[] selectedIndices = list.getSelectedIndices();
            assertEquals(1, selectedIndices.length);
            assertEquals(1, selectedIndices[0]);
        } catch (Exception e) {
            fail("Failed to verify selection: " + e.getMessage());
        }
    }

    @Test
    void testSetElementsAt_SelectsMultipleReplacedElements() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        panel.setClassPath(classPath);

        // Clear any existing selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.clearSelection();
        } catch (Exception e) {
            fail("Failed to clear selection: " + e.getMessage());
        }

        // Replace elements at indices 0, 2, 3
        Object[] elements = new Object[]{
            new ClassPathEntry(new java.io.File("/new1.jar"), false),
            new ClassPathEntry(new java.io.File("/new3.jar"), false),
            new ClassPathEntry(new java.io.File("/new4.jar"), false)
        };
        int[] indices = new int[]{0, 2, 3};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify all replaced indices are selected
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            int[] selectedIndices = list.getSelectedIndices();
            assertEquals(3, selectedIndices.length);
            assertArrayEquals(new int[]{0, 2, 3}, selectedIndices);
        } catch (Exception e) {
            fail("Failed to verify selection: " + e.getMessage());
        }
    }

    @Test
    void testSetElementsAt_SelectionReplacesExistingSelection() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        panel.setClassPath(classPath);

        // Set initial selection to indices 1, 2
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndices(new int[]{1, 2});
        } catch (Exception e) {
            fail("Failed to set initial selection: " + e.getMessage());
        }

        // Replace element at index 3 only
        Object[] elements = new Object[]{new ClassPathEntry(new java.io.File("/replaced.jar"), false)};
        int[] indices = new int[]{3};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify selection was replaced (not appended)
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            int[] selectedIndices = list.getSelectedIndices();
            assertEquals(1, selectedIndices.length);
            assertEquals(3, selectedIndices[0]);
        } catch (Exception e) {
            fail("Failed to verify selection: " + e.getMessage());
        }
    }

    @Test
    void testSetElementsAt_SelectsIndicesInOrder() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        panel.setClassPath(classPath);

        // Replace elements with out-of-order indices
        Object[] elements = new Object[]{
            new ClassPathEntry(new java.io.File("/new.jar"), false),
            new ClassPathEntry(new java.io.File("/another.jar"), false),
            new ClassPathEntry(new java.io.File("/third.jar"), false)
        };
        int[] indices = new int[]{3, 0, 2}; // Out of order

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify selection includes all indices (order may be normalized by Swing)
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            int[] selectedIndices = list.getSelectedIndices();
            assertEquals(3, selectedIndices.length);
            // Swing normalizes to sorted order
            assertArrayEquals(new int[]{0, 2, 3}, selectedIndices);
        } catch (Exception e) {
            fail("Failed to verify selection: " + e.getMessage());
        }
    }

    // Integration tests covering complete execution path

    @Test
    void testSetElementsAt_WithNullOwner() {
        // Create panel with null owner
        ClassPathPanel nullOwnerPanel = new ClassPathPanel(null, true);

        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        nullOwnerPanel.setClassPath(classPath);

        // Replace element
        Object[] elements = new Object[]{new ClassPathEntry(new java.io.File("/replaced.jar"), false)};
        int[] indices = new int[]{1};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(nullOwnerPanel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify operation succeeded
        ClassPath result = nullOwnerPanel.getClassPath();
        assertTrue(result.get(1).getName().contains("replaced.jar"));
    }

    @Test
    void testSetElementsAt_InputModePanel() {
        // Create panel with input mode (second parameter = true)
        ClassPathPanel inputPanel = new ClassPathPanel(frame, true);

        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/input1.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/input2.jar"), false));
        inputPanel.setClassPath(classPath);

        // Replace element
        Object[] elements = new Object[]{new ClassPathEntry(new java.io.File("/replaced.jar"), false)};
        int[] indices = new int[]{0};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(inputPanel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify operation succeeded
        ClassPath result = inputPanel.getClassPath();
        assertTrue(result.get(0).getName().contains("replaced.jar"));
    }

    @Test
    void testSetElementsAt_OutputModePanel() {
        // Create panel with output mode (second parameter = false)
        ClassPathPanel outputPanel = new ClassPathPanel(frame, false);

        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/output1.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/output2.jar"), false));
        outputPanel.setClassPath(classPath);

        // Replace element
        Object[] elements = new Object[]{new ClassPathEntry(new java.io.File("/replaced.jar"), false)};
        int[] indices = new int[]{1};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(outputPanel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify operation succeeded
        ClassPath result = outputPanel.getClassPath();
        assertTrue(result.get(1).getName().contains("replaced.jar"));
    }

    @Test
    void testSetElementsAt_ReplaceAllElements() {
        // Initialize with three elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Replace all elements
        Object[] elements = new Object[]{
            new ClassPathEntry(new java.io.File("/new1.jar"), false),
            new ClassPathEntry(new java.io.File("/new2.jar"), false),
            new ClassPathEntry(new java.io.File("/new3.jar"), false)
        };
        int[] indices = new int[]{0, 1, 2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify all elements replaced and all selected
        ClassPath result = panel.getClassPath();
        assertEquals(3, result.size());
        assertTrue(result.get(0).getName().contains("new1.jar"));
        assertTrue(result.get(1).getName().contains("new2.jar"));
        assertTrue(result.get(2).getName().contains("new3.jar"));

        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            int[] selectedIndices = list.getSelectedIndices();
            assertEquals(3, selectedIndices.length);
            assertArrayEquals(new int[]{0, 1, 2}, selectedIndices);
        } catch (Exception e) {
            fail("Failed to verify selection: " + e.getMessage());
        }
    }

    @Test
    void testSetElementsAt_CompleteExecutionPath() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/original1.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/original2.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/original3.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/original4.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/original5.jar"), false));
        panel.setClassPath(classPath);

        // Clear initial selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.clearSelection();
        } catch (Exception e) {
            fail("Failed to clear selection: " + e.getMessage());
        }

        // Replace elements at non-contiguous indices
        Object[] elements = new Object[]{
            new ClassPathEntry(new java.io.File("/replaced1.jar"), false),
            new ClassPathEntry(new java.io.File("/replaced3.jar"), false),
            new ClassPathEntry(new java.io.File("/replaced5.jar"), false)
        };
        int[] indices = new int[]{0, 2, 4};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify complete execution:
        // 1. Loop executed 3 times (line 269)
        // 2. Elements replaced at correct indices (line 271)
        // 3. All replaced indices selected (line 275)
        ClassPath result = panel.getClassPath();
        assertEquals(5, result.size());
        assertTrue(result.get(0).getName().contains("replaced1.jar"));
        assertTrue(result.get(1).getName().contains("original2.jar"));
        assertTrue(result.get(2).getName().contains("replaced3.jar"));
        assertTrue(result.get(3).getName().contains("original4.jar"));
        assertTrue(result.get(4).getName().contains("replaced5.jar"));

        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            int[] selectedIndices = list.getSelectedIndices();
            assertEquals(3, selectedIndices.length);
            assertArrayEquals(new int[]{0, 2, 4}, selectedIndices);
        } catch (Exception e) {
            fail("Failed to verify selection: " + e.getMessage());
        }
    }

    @Test
    void testSetElementsAt_ReverseOrderIndices() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        panel.setClassPath(classPath);

        // Replace with reverse order indices (still uses parallel array mapping)
        Object[] elements = new Object[]{
            new ClassPathEntry(new java.io.File("/for-3.jar"), false),
            new ClassPathEntry(new java.io.File("/for-1.jar"), false),
            new ClassPathEntry(new java.io.File("/for-0.jar"), false)
        };
        int[] indices = new int[]{3, 1, 0}; // Reverse order

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify correct parallel mapping despite reverse order
        ClassPath result = panel.getClassPath();
        assertTrue(result.get(0).getName().contains("for-0.jar"));
        assertTrue(result.get(1).getName().contains("for-1.jar"));
        assertTrue(result.get(2).getName().contains("third.jar"));
        assertTrue(result.get(3).getName().contains("for-3.jar"));
    }

    @Test
    void testSetElementsAt_SingleElementAtMiddleIndex() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fifth.jar"), false));
        panel.setClassPath(classPath);

        // Replace single element at middle index
        Object[] elements = new Object[]{new ClassPathEntry(new java.io.File("/replaced.jar"), false)};
        int[] indices = new int[]{2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify only middle element replaced, others unchanged
        ClassPath result = panel.getClassPath();
        assertEquals(5, result.size());
        assertTrue(result.get(0).getName().contains("first.jar"));
        assertTrue(result.get(1).getName().contains("second.jar"));
        assertTrue(result.get(2).getName().contains("replaced.jar"));
        assertTrue(result.get(3).getName().contains("fourth.jar"));
        assertTrue(result.get(4).getName().contains("fifth.jar"));
    }

    @Test
    void testSetElementsAt_MultipleReplacementsVerifyEachIteration() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/a.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/b.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/c.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/d.jar"), false));
        panel.setClassPath(classPath);

        // Replace four elements - verifies loop iterations 0, 1, 2, 3 (< 4)
        Object[] elements = new Object[]{
            new ClassPathEntry(new java.io.File("/w.jar"), false),
            new ClassPathEntry(new java.io.File("/x.jar"), false),
            new ClassPathEntry(new java.io.File("/y.jar"), false),
            new ClassPathEntry(new java.io.File("/z.jar"), false)
        };
        int[] indices = new int[]{3, 2, 1, 0}; // Reverse order to test parallel mapping

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify each iteration worked correctly with parallel mapping
        ClassPath result = panel.getClassPath();
        assertEquals(4, result.size());
        assertTrue(result.get(0).getName().contains("z.jar")); // elements[3] -> indices[3]=0
        assertTrue(result.get(1).getName().contains("y.jar")); // elements[2] -> indices[2]=1
        assertTrue(result.get(2).getName().contains("x.jar")); // elements[1] -> indices[1]=2
        assertTrue(result.get(3).getName().contains("w.jar")); // elements[0] -> indices[0]=3
    }

    @Test
    void testSetElementsAt_VerifyNoInsertionOrDeletion() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        int originalSize = panel.getClassPath().size();

        // Replace two elements
        Object[] elements = new Object[]{
            new ClassPathEntry(new java.io.File("/new1.jar"), false),
            new ClassPathEntry(new java.io.File("/new3.jar"), false)
        };
        int[] indices = new int[]{0, 2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "setElementsAt", Object[].class, int[].class);
            method.setAccessible(true);
            method.invoke(panel, elements, indices);
        } catch (Exception e) {
            fail("Failed to invoke setElementsAt: " + e.getMessage());
        }

        // Verify size unchanged - no insertions or deletions occurred
        assertEquals(originalSize, panel.getClassPath().size());

        // Verify replacement semantics (not insertion)
        ClassPath result = panel.getClassPath();
        assertTrue(result.get(0).getName().contains("new1.jar"));
        assertTrue(result.get(1).getName().contains("second.jar")); // Unchanged
        assertTrue(result.get(2).getName().contains("new3.jar"));
    }
}
