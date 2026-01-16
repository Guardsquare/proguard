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
 * Tests for proguard.gui.ListPanel.removeAllElements()V
 *
 * This test class covers the removeAllElements method which removes all elements
 * from the list and updates button states.
 *
 * Lines to cover:
 * - Line 297: listModel.removeAllElements();
 * - Line 301: enableSelectionButtons();
 */
public class ListPanelClaude_removeAllElementsTest {

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

    // Tests for line 297: listModel.removeAllElements()

    @Test
    void testRemoveAllElements_RemovesAllElementsFromPopulatedList() {
        // Initialize with multiple elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        assertEquals(3, panel.getClassPath().size());

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify all elements removed
        assertEquals(0, panel.getClassPath().size());
    }

    @Test
    void testRemoveAllElements_EmptyListRemainsEmpty() {
        // Start with empty list
        panel.setClassPath(new ClassPath());
        assertEquals(0, panel.getClassPath().size());

        // Remove all elements (idempotent operation)
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify list still empty
        assertEquals(0, panel.getClassPath().size());
    }

    @Test
    void testRemoveAllElements_RemovesSingleElement() {
        // Initialize with single element
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/only.jar"), false));
        panel.setClassPath(classPath);

        assertEquals(1, panel.getClassPath().size());

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify element removed
        assertEquals(0, panel.getClassPath().size());
    }

    @Test
    void testRemoveAllElements_RemovesManyElements() {
        // Initialize with many elements
        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 10; i++) {
            classPath.add(new ClassPathEntry(new java.io.File("/file" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        assertEquals(10, panel.getClassPath().size());

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify all elements removed
        assertEquals(0, panel.getClassPath().size());
    }

    @Test
    void testRemoveAllElements_ClearsListModel() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify list model is empty
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            assertEquals(0, list.getModel().getSize(), "List model should be empty");
        } catch (Exception e) {
            fail("Failed to verify list model: " + e.getMessage());
        }
    }

    @Test
    void testRemoveAllElements_ClearsListModelAndClassPath() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/a.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/b.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/c.jar"), false));
        panel.setClassPath(classPath);

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify both ClassPath and list model are empty
        assertEquals(0, panel.getClassPath().size());

        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            assertEquals(0, list.getModel().getSize());
        } catch (Exception e) {
            fail("Failed to verify list model: " + e.getMessage());
        }
    }

    @Test
    void testRemoveAllElements_MultipleInvocationsIdempotent() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Remove all elements multiple times
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
            method.invoke(panel);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify list remains empty
        assertEquals(0, panel.getClassPath().size());
    }

    // Tests for line 301: enableSelectionButtons()

    @Test
    void testRemoveAllElements_CallsEnableSelectionButtons() {
        // Initialize with elements and buttons
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();
        panel.addDownButton();

        // Select an element so buttons are enabled
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify enableSelectionButtons was called - buttons should be disabled
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled(), "Buttons should be disabled after removing all elements");
            }
        }
    }

    @Test
    void testRemoveAllElements_UpdatesButtonStatesWhenNoSelection() {
        // Initialize with elements and buttons
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();
        panel.addDownButton();

        // Don't select anything
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.clearSelection();
        } catch (Exception e) {
            fail("Failed to clear selection: " + e.getMessage());
        }

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify buttons remain disabled
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled());
            }
        }
    }

    @Test
    void testRemoveAllElements_DisablesSelectionDependentButtons() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();
        panel.addDownButton();

        // Select elements to enable buttons
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndices(new int[]{0, 2});
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Verify at least one button is enabled before removal
        java.util.List<JComponent> buttonsBeforeRemoval = panel.getButtons();
        boolean anyEnabledBefore = false;
        for (JComponent button : buttonsBeforeRemoval) {
            if (button instanceof JButton && button.isEnabled()) {
                anyEnabledBefore = true;
                break;
            }
        }
        assertTrue(anyEnabledBefore, "At least one button should be enabled with selection");

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify all buttons are disabled after removal
        java.util.List<JComponent> buttonsAfterRemoval = panel.getButtons();
        for (JComponent button : buttonsAfterRemoval) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled(), "All buttons should be disabled after removing all elements");
            }
        }
    }

    @Test
    void testRemoveAllElements_ButtonStateConsistentWithEmptyList() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/test.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify buttons are in correct state for empty list
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled(), "Buttons should be disabled when list is empty");
            }
        }
    }

    // Integration tests covering complete execution path

    @Test
    void testRemoveAllElements_WithNullOwner() {
        // Create panel with null owner
        ClassPathPanel nullOwnerPanel = new ClassPathPanel(null, true);

        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        nullOwnerPanel.setClassPath(classPath);

        assertEquals(2, nullOwnerPanel.getClassPath().size());

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(nullOwnerPanel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify operation succeeded
        assertEquals(0, nullOwnerPanel.getClassPath().size());
    }

    @Test
    void testRemoveAllElements_InputModePanel() {
        // Create panel with input mode (second parameter = true)
        ClassPathPanel inputPanel = new ClassPathPanel(frame, true);

        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/input1.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/input2.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/input3.jar"), false));
        inputPanel.setClassPath(classPath);

        assertEquals(3, inputPanel.getClassPath().size());

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(inputPanel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify operation succeeded
        assertEquals(0, inputPanel.getClassPath().size());
    }

    @Test
    void testRemoveAllElements_OutputModePanel() {
        // Create panel with output mode (second parameter = false)
        ClassPathPanel outputPanel = new ClassPathPanel(frame, false);

        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/output1.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/output2.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/output3.jar"), false));
        outputPanel.setClassPath(classPath);

        assertEquals(3, outputPanel.getClassPath().size());

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(outputPanel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify operation succeeded
        assertEquals(0, outputPanel.getClassPath().size());
    }

    @Test
    void testRemoveAllElements_CompleteExecutionPath() {
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
        panel.addDownButton();

        // Set initial selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndices(new int[]{1, 3});
        } catch (Exception e) {
            fail("Failed to set initial selection: " + e.getMessage());
        }

        assertEquals(5, panel.getClassPath().size());

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify complete execution:
        // 1. All elements removed (line 297)
        // 2. Buttons updated (line 301)

        assertEquals(0, panel.getClassPath().size());

        // Verify list model is empty
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            assertEquals(0, list.getModel().getSize());
        } catch (Exception e) {
            fail("Failed to verify list model: " + e.getMessage());
        }

        // Verify buttons disabled
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled());
            }
        }
    }

    @Test
    void testRemoveAllElements_ClearsSelectionImplicitly() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Set selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndices(new int[]{0, 2});

            assertFalse(list.isSelectionEmpty(), "Selection should exist before removal");
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify selection is cleared implicitly (no elements to select)
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            assertTrue(list.isSelectionEmpty(), "Selection should be empty after removing all elements");
        } catch (Exception e) {
            fail("Failed to verify selection: " + e.getMessage());
        }
    }

    @Test
    void testRemoveAllElements_FromLargeList() {
        // Initialize with large number of elements
        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 100; i++) {
            classPath.add(new ClassPathEntry(new java.io.File("/file" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        assertEquals(100, panel.getClassPath().size());

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify all elements removed
        assertEquals(0, panel.getClassPath().size());

        // Verify list model is empty
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            assertEquals(0, list.getModel().getSize());
        } catch (Exception e) {
            fail("Failed to verify list model: " + e.getMessage());
        }
    }

    @Test
    void testRemoveAllElements_AfterPartialRemoval() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/fourth.jar"), false));
        panel.setClassPath(classPath);

        // Remove some elements first
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod(
                "removeElementsAt", int[].class);
            method.setAccessible(true);
            method.invoke(panel, (Object) new int[]{1, 3});
        } catch (Exception e) {
            fail("Failed to invoke removeElementsAt: " + e.getMessage());
        }

        assertEquals(2, panel.getClassPath().size());

        // Now remove all remaining elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify all elements removed
        assertEquals(0, panel.getClassPath().size());
    }

    @Test
    void testRemoveAllElements_ThenAddElements() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        assertEquals(0, panel.getClassPath().size());

        // Add new elements
        ClassPath newClassPath = new ClassPath();
        newClassPath.add(new ClassPathEntry(new java.io.File("/new1.jar"), false));
        newClassPath.add(new ClassPathEntry(new java.io.File("/new2.jar"), false));
        newClassPath.add(new ClassPathEntry(new java.io.File("/new3.jar"), false));
        panel.setClassPath(newClassPath);

        // Verify new elements added successfully
        assertEquals(3, panel.getClassPath().size());
        assertTrue(panel.getClassPath().get(0).getName().contains("new1.jar"));
    }

    @Test
    void testRemoveAllElements_VerifyListModelConsistency() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/a.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/b.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/c.jar"), false));
        panel.setClassPath(classPath);

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify ClassPath and list model are consistent
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            assertEquals(panel.getClassPath().size(), list.getModel().getSize(),
                        "ClassPath size should match list model size");
            assertEquals(0, list.getModel().getSize());
        } catch (Exception e) {
            fail("Failed to verify consistency: " + e.getMessage());
        }
    }

    @Test
    void testRemoveAllElements_WithMultipleButtonTypes() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Add various button types
        panel.addRemoveButton();
        panel.addUpButton();
        panel.addDownButton();

        // Select to enable buttons
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify all button types are properly disabled
        java.util.List<JComponent> buttons = panel.getButtons();
        assertTrue(buttons.size() > 0, "Should have buttons");
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled(), "All button types should be disabled");
            }
        }
    }

    @Test
    void testRemoveAllElements_DoesNotThrowExceptionOnEmpty() {
        // Start with empty list
        panel.setClassPath(new ClassPath());

        // Remove all elements should not throw exception
        assertDoesNotThrow(() -> {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        }, "removeAllElements should not throw exception on empty list");

        // Verify still empty
        assertEquals(0, panel.getClassPath().size());
    }

    @Test
    void testRemoveAllElements_EffectOnListModelSize() {
        // Initialize with known number of elements
        ClassPath classPath = new ClassPath();
        for (int i = 0; i < 5; i++) {
            classPath.add(new ClassPathEntry(new java.io.File("/file" + i + ".jar"), false));
        }
        panel.setClassPath(classPath);

        // Get initial list model size
        int initialModelSize = 0;
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            initialModelSize = list.getModel().getSize();
        } catch (Exception e) {
            fail("Failed to get initial model size: " + e.getMessage());
        }

        assertEquals(5, initialModelSize);

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify list model size is now 0
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);

            assertEquals(0, list.getModel().getSize(), "List model should be empty");
        } catch (Exception e) {
            fail("Failed to verify final model size: " + e.getMessage());
        }
    }

    @Test
    void testRemoveAllElements_PreservesListStructure() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        panel.setClassPath(classPath);

        // Get reference to list
        JList<?> listBefore = null;
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            listBefore = (JList<?>) listField.get(panel);
        } catch (Exception e) {
            fail("Failed to get list reference: " + e.getMessage());
        }

        // Remove all elements
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("removeAllElements");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke removeAllElements: " + e.getMessage());
        }

        // Verify list object is the same (structure preserved, only contents cleared)
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> listAfter = (JList<?>) listField.get(panel);

            assertSame(listBefore, listAfter, "List object should be the same instance");
        } catch (Exception e) {
            fail("Failed to verify list reference: " + e.getMessage());
        }
    }
}
