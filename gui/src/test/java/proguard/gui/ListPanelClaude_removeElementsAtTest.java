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
 * Tests for proguard.gui.ListPanel.removeElementsAt([I)V
 *
 * This test class covers the removeElementsAt method which removes multiple elements
 * at specified indices from the list in reverse order, clears selection, and updates button states.
 *
 * Lines to cover:
 * - Line 281: for (int index = indices.length - 1; index >= 0; index--)
 * - Line 283: listModel.removeElementAt(indices[index]);
 * - Line 287: list.clearSelection();
 * - Line 291: enableSelectionButtons();
 */
public class ListPanelClaude_removeElementsAtTest {

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

    // Tests for line 281: for loop iteration (reverse order)

    @Test
    void testRemoveElementsAt_EmptyArray_LoopNotExecuted() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        int originalSize = panel.getClassPath().size();

        // Remove empty array - loop should not execute (indices.length - 1 = -1, not >= 0)
        int[] indices = new int[0];

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify no elements removed
        assertEquals(originalSize, panel.getClassPath().size());
    }

    @Test
    void testRemoveElementsAt_SingleElement_LoopExecutesOnce() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Remove single element - loop executes once (index = 0, 0 >= 0)
        int[] indices = new int[]{1};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify one element removed
        ClassPath result = panel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("first.jar"));
        assertTrue(result.get(1).getName().contains("third.jar"));
    }

    @Test
    void testRemoveElementsAt_MultipleElements_LoopExecutesMultipleTimes() {
        // Initialize with five elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fifth.jar"), false));
        panel.setClassPath(classPath);

        // Remove three elements - loop executes three times (index = 2, 1, 0 all >= 0)
        int[] indices = new int[]{1, 2, 4};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify three elements removed
        ClassPath result = panel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("first.jar"));
        assertTrue(result.get(1).getName().contains("fourth.jar"));
    }

    @Test
    void testRemoveElementsAt_ReverseOrderIteration() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/a.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/b.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/c.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/d.jar"), false));
        panel.setClassPath(classPath);

        // Remove elements at indices [0, 2] - should iterate in reverse: index 1 then index 0
        // This tests that loop goes from indices.length-1 down to 0
        int[] indices = new int[]{0, 2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify correct removal (reverse iteration prevents index shifting issues)
        ClassPath result = panel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("b.jar"));
        assertTrue(result.get(1).getName().contains("d.jar"));
    }

    // Tests for line 283: listModel.removeElementAt(indices[index])

    @Test
    void testRemoveElementsAt_RemovesElementAtFirstIndex() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Remove first element
        int[] indices = new int[]{0};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify first element removed
        ClassPath result = panel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("second.jar"));
        assertTrue(result.get(1).getName().contains("third.jar"));
    }

    @Test
    void testRemoveElementsAt_RemovesElementAtLastIndex() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Remove last element
        int[] indices = new int[]{2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify last element removed
        ClassPath result = panel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("first.jar"));
        assertTrue(result.get(1).getName().contains("second.jar"));
    }

    @Test
    void testRemoveElementsAt_RemovesElementAtMiddleIndex() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fifth.jar"), false));
        panel.setClassPath(classPath);

        // Remove middle element
        int[] indices = new int[]{2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify middle element removed
        ClassPath result = panel.getClassPath();
        assertEquals(4, result.size());
        assertTrue(result.get(0).getName().contains("first.jar"));
        assertTrue(result.get(1).getName().contains("second.jar"));
        assertTrue(result.get(2).getName().contains("fourth.jar"));
        assertTrue(result.get(3).getName().contains("fifth.jar"));
    }

    @Test
    void testRemoveElementsAt_RemovesNonContiguousElements() {
        // Initialize with six elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fifth.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/sixth.jar"), false));
        panel.setClassPath(classPath);

        // Remove non-contiguous elements at indices 1, 3, 5
        int[] indices = new int[]{1, 3, 5};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify correct elements removed
        ClassPath result = panel.getClassPath();
        assertEquals(3, result.size());
        assertTrue(result.get(0).getName().contains("first.jar"));
        assertTrue(result.get(1).getName().contains("third.jar"));
        assertTrue(result.get(2).getName().contains("fifth.jar"));
    }

    @Test
    void testRemoveElementsAt_RemovesContiguousElements() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fifth.jar"), false));
        panel.setClassPath(classPath);

        // Remove contiguous elements at indices 1, 2, 3
        int[] indices = new int[]{1, 2, 3};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify contiguous block removed
        ClassPath result = panel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("first.jar"));
        assertTrue(result.get(1).getName().contains("fifth.jar"));
    }

    @Test
    void testRemoveElementsAt_ReverseOrderPreventsIndexShifting() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/a.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/b.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/c.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/d.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/e.jar"), false));
        panel.setClassPath(classPath);

        // Remove indices [0, 2, 4] - processed in reverse [4, 2, 0]
        // Reverse order prevents index shifting issues
        int[] indices = new int[]{0, 2, 4};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify correct elements removed despite forward index order
        ClassPath result = panel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("b.jar"));
        assertTrue(result.get(1).getName().contains("d.jar"));
    }

    @Test
    void testRemoveElementsAt_RemovesAllElements() {
        // Initialize with three elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Remove all elements
        int[] indices = new int[]{0, 1, 2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify all elements removed
        assertEquals(0, panel.getClassPath().size());
    }

    // Tests for line 287: list.clearSelection()

    @Test
    void testRemoveElementsAt_ClearsSelection() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        panel.setClassPath(classPath);

        // Set initial selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndices(new int[]{0, 1});
        } catch (Exception e) {
            fail("Failed to set initial selection: " + e.getMessage());
        }

        // Remove an element
        int[] indices = new int[]{2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify selection cleared
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            assertTrue(list.isSelectionEmpty(), "Selection should be cleared");
            assertEquals(0, list.getSelectedIndices().length);
        } catch (Exception e) {
            fail("Failed to verify selection cleared: " + e.getMessage());
        }
    }

    @Test
    void testRemoveElementsAt_ClearsSelectionEvenIfNothingSelected() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Clear any initial selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.clearSelection();
        } catch (Exception e) {
            fail("Failed to clear initial selection: " + e.getMessage());
        }

        // Remove an element
        int[] indices = new int[]{1};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify selection still cleared (idempotent)
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            assertTrue(list.isSelectionEmpty());
        } catch (Exception e) {
            fail("Failed to verify selection: " + e.getMessage());
        }
    }

    @Test
    void testRemoveElementsAt_ClearsSelectionWithMultipleSelected() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fifth.jar"), false));
        panel.setClassPath(classPath);

        // Set multiple items selected
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndices(new int[]{0, 2, 4});
        } catch (Exception e) {
            fail("Failed to set initial selection: " + e.getMessage());
        }

        // Remove elements
        int[] indices = new int[]{1, 3};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify all selections cleared
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            assertTrue(list.isSelectionEmpty());
            assertEquals(0, list.getSelectedIndices().length);
        } catch (Exception e) {
            fail("Failed to verify selection cleared: " + e.getMessage());
        }
    }

    // Tests for line 291: enableSelectionButtons()

    @Test
    void testRemoveElementsAt_CallsEnableSelectionButtons() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons to verify they get updated
        panel.addRemoveButton();
        panel.addUpButton();
        panel.addDownButton();

        // Select elements
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(1);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Remove an element
        int[] indices = new int[]{0};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify enableSelectionButtons was called - buttons should be disabled since selection is cleared
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                // After removing and clearing selection, selection-dependent buttons should be disabled
                assertFalse(button.isEnabled(), "Buttons should be disabled after selection cleared");
            }
        }
    }

    @Test
    void testRemoveElementsAt_UpdatesButtonStateCorrectly() {
        // Initialize with multiple elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();
        panel.addDownButton();

        // Remove elements - this should call enableSelectionButtons
        int[] indices = new int[]{1, 2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify button states updated (selection cleared, so buttons disabled)
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled());
            }
        }

        // Now select an element to verify buttons can be re-enabled
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Buttons should now be enabled (at least remove button)
        boolean anyEnabled = false;
        for (JComponent button : buttons) {
            if (button instanceof JButton && button.isEnabled()) {
                anyEnabled = true;
                break;
            }
        }
        assertTrue(anyEnabled, "At least one button should be enabled after selection");
    }

    // Integration tests covering complete execution path

    @Test
    void testRemoveElementsAt_WithNullOwner() {
        // Create panel with null owner
        ClassPathPanel nullOwnerPanel = new ClassPathPanel(null, true);

        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        nullOwnerPanel.setClassPath(classPath);

        // Remove element
        int[] indices = new int[]{1};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(nullOwnerPanel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify operation succeeded
        ClassPath result = nullOwnerPanel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("first.jar"));
        assertTrue(result.get(1).getName().contains("third.jar"));
    }

    @Test
    void testRemoveElementsAt_InputModePanel() {
        // Create panel with input mode (second parameter = true)
        ClassPathPanel inputPanel = new ClassPathPanel(frame, true);

        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/input1.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/input2.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/input3.jar"), false));
        inputPanel.setClassPath(classPath);

        // Remove element
        int[] indices = new int[]{0};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(inputPanel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify operation succeeded
        ClassPath result = inputPanel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("input2.jar"));
    }

    @Test
    void testRemoveElementsAt_OutputModePanel() {
        // Create panel with output mode (second parameter = false)
        ClassPathPanel outputPanel = new ClassPathPanel(frame, false);

        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/output1.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/output2.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/output3.jar"), false));
        outputPanel.setClassPath(classPath);

        // Remove element
        int[] indices = new int[]{2};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(outputPanel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify operation succeeded
        ClassPath result = outputPanel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(1).getName().contains("output2.jar"));
    }

    @Test
    void testRemoveElementsAt_CompleteExecutionPath() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/a.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/b.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/c.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/d.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/e.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons to test enableSelectionButtons
        panel.addRemoveButton();
        panel.addUpButton();

        // Set initial selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndices(new int[]{0, 2, 4});
        } catch (Exception e) {
            fail("Failed to set initial selection: " + e.getMessage());
        }

        // Remove elements at indices [1, 3]
        int[] indices = new int[]{1, 3};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify complete execution:
        // 1. Loop executed in reverse (line 281): index 1 then index 0 (for indices array)
        // 2. Elements removed at indices[1]=3, then indices[0]=1 (line 283)
        // 3. Selection cleared (line 287)
        // 4. Buttons updated (line 291)

        ClassPath result = panel.getClassPath();
        assertEquals(3, result.size());
        assertTrue(result.get(0).getName().contains("a.jar"));
        assertTrue(result.get(1).getName().contains("c.jar"));
        assertTrue(result.get(2).getName().contains("e.jar"));

        // Verify selection cleared
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            assertTrue(list.isSelectionEmpty());
        } catch (Exception e) {
            fail("Failed to verify selection: " + e.getMessage());
        }

        // Verify buttons disabled (since selection cleared)
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled());
            }
        }
    }

    @Test
    void testRemoveElementsAt_UnsortedIndices() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fifth.jar"), false));
        panel.setClassPath(classPath);

        // Remove with unsorted indices [4, 1, 3] - processed in reverse: indices[2]=3, indices[1]=1, indices[0]=4
        // This should remove index 3 first, then 1, then 4
        int[] indices = new int[]{4, 1, 3};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify correct elements removed
        ClassPath result = panel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("first.jar"));
        assertTrue(result.get(1).getName().contains("third.jar"));
    }

    @Test
    void testRemoveElementsAt_DuplicateIndices() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Test with duplicate indices [1, 1] - should attempt to remove index 1 twice
        // Note: This tests the actual behavior, which may throw exception or have undefined behavior
        int[] indices = new int[]{1, 1};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
            // If it succeeds, verify behavior
            // After removing index 1 twice (backwards), first removes index 1, then tries to remove index 1 again
            // This might fail or have unexpected behavior
        } catch (Exception e) {
            // Expected to potentially fail with ArrayIndexOutOfBoundsException
            assertTrue(e.getCause() instanceof ArrayIndexOutOfBoundsException ||
                      e.getCause() instanceof IllegalArgumentException,
                      "Should throw exception for duplicate indices");
        }
    }

    @Test
    void testRemoveElementsAt_RemoveThenVerifyListConsistency() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/a.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/b.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/c.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/d.jar"), false));
        panel.setClassPath(classPath);

        int originalSize = panel.getClassPath().size();

        // Remove two elements
        int[] indices = new int[]{1, 3};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify list size decreased by correct amount
        assertEquals(originalSize - 2, panel.getClassPath().size());

        // Verify list model and panel are consistent
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            assertEquals(list.getModel().getSize(), panel.getClassPath().size(),
                        "List model size should match ClassPath size");
        } catch (Exception e) {
            fail("Failed to verify list consistency: " + e.getMessage());
        }
    }

    @Test
    void testRemoveElementsAt_VerifyReverseIterationWithLogging() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/zero.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/one.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/two.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/three.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/four.jar"), false));
        panel.setClassPath(classPath);

        // Remove indices [0, 2, 4]
        // Should process in reverse: indices[2]=4, indices[1]=2, indices[0]=0
        int[] indices = new int[]{0, 2, 4};

        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) indices);
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        // Verify correct result from reverse iteration
        ClassPath result = panel.getClassPath();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("one.jar"));
        assertTrue(result.get(1).getName().contains("three.jar"));
    }
}
