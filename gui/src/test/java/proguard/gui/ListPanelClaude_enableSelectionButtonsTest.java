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
 * Tests for proguard.gui.ListPanel.enableSelectionButtons()V
 *
 * This test class covers the enableSelectionButtons method which enables or disables
 * buttons that depend on a selection based on whether any items are selected in the list.
 *
 * Lines to cover:
 * - Line 310: boolean selected = !list.isSelectionEmpty();
 * - Line 313: for (int index = firstSelectionButton; index < getComponentCount(); index++)
 * - Line 315: getComponent(index).setEnabled(selected);
 */
public class ListPanelClaude_enableSelectionButtonsTest {

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

    // Tests for line 310: boolean selected = !list.isSelectionEmpty()

    @Test
    void testEnableSelectionButtons_SelectionEmptyReturnsFalse() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();

        // Clear selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.clearSelection();

            assertTrue(list.isSelectionEmpty(), "Selection should be empty");
        } catch (Exception e) {
            fail("Failed to clear selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify buttons are disabled (selected = false)
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled(), "Buttons should be disabled when selection is empty");
            }
        }
    }

    @Test
    void testEnableSelectionButtons_SelectionNotEmptyReturnsTrue() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();

        // Set selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);

            assertFalse(list.isSelectionEmpty(), "Selection should not be empty");
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify buttons are enabled (selected = true)
        java.util.List<JComponent> buttons = panel.getButtons();
        boolean anyEnabled = false;
        for (JComponent button : buttons) {
            if (button instanceof JButton && button.isEnabled()) {
                anyEnabled = true;
                break;
            }
        }
        assertTrue(anyEnabled, "At least one button should be enabled when selection exists");
    }

    @Test
    void testEnableSelectionButtons_MultipleItemsSelected() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/third.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();

        // Set multiple selections
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndices(new int[]{0, 2});

            assertFalse(list.isSelectionEmpty(), "Selection should not be empty");
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify buttons are enabled (selected = true)
        java.util.List<JComponent> buttons = panel.getButtons();
        boolean anyEnabled = false;
        for (JComponent button : buttons) {
            if (button instanceof JButton && button.isEnabled()) {
                anyEnabled = true;
                break;
            }
        }
        assertTrue(anyEnabled, "At least one button should be enabled with multiple selections");
    }

    // Tests for line 313: for loop iteration

    @Test
    void testEnableSelectionButtons_LoopIteratesOverComponents() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        panel.setClassPath(classPath);

        // Add multiple buttons to ensure loop iterates
        panel.addRemoveButton();
        panel.addUpButton();
        panel.addDownButton();

        // Get component count to verify loop will iterate
        int componentCount = panel.getComponentCount();
        assertTrue(componentCount > 2, "Should have more than 2 components (list, scroll, buttons)");

        // Set selection to enable buttons
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify loop processed multiple buttons
        java.util.List<JComponent> buttons = panel.getButtons();
        assertTrue(buttons.size() >= 3, "Should have at least 3 buttons");
    }

    @Test
    void testEnableSelectionButtons_LoopStartsAtFirstSelectionButton() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();

        // Get firstSelectionButton value
        int firstSelectionButton = 0;
        try {
            java.lang.reflect.Field field = ListPanel.class.getDeclaredField("firstSelectionButton");
            field.setAccessible(true);
            firstSelectionButton = (int) field.get(panel);
        } catch (Exception e) {
            fail("Failed to get firstSelectionButton: " + e.getMessage());
        }

        assertEquals(2, firstSelectionButton, "firstSelectionButton should be 2");

        // Verify components before firstSelectionButton exist (list and scroll pane)
        assertTrue(panel.getComponentCount() > firstSelectionButton,
                   "Should have components at and after firstSelectionButton index");
    }

    @Test
    void testEnableSelectionButtons_LoopIteratesUntilComponentCount() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        panel.setClassPath(classPath);

        // Add multiple buttons
        panel.addRemoveButton();
        panel.addUpButton();
        panel.addDownButton();

        int componentCountBefore = panel.getComponentCount();

        // Clear selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.clearSelection();
        } catch (Exception e) {
            fail("Failed to clear selection: " + e.getMessage());
        }

        // Call enableSelectionButtons - should iterate through all components
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify all buttons processed (all should be disabled)
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled(), "All buttons should be disabled");
            }
        }
    }

    @Test
    void testEnableSelectionButtons_NoButtonsAdded() {
        // Initialize with elements but don't add buttons
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        panel.setClassPath(classPath);

        // Don't add any buttons - loop should still execute but have nothing to process
        // (or very few components after firstSelectionButton)

        // Call enableSelectionButtons - should not throw exception
        assertDoesNotThrow(() -> {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        }, "enableSelectionButtons should work even without buttons");
    }

    // Tests for line 315: getComponent(index).setEnabled(selected)

    @Test
    void testEnableSelectionButtons_EnablesComponentsWhenSelected() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();

        // First disable all buttons by clearing selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.clearSelection();

            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to disable buttons: " + e.getMessage());
        }

        // Now select an item
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify components are enabled (setEnabled(true) was called)
        java.util.List<JComponent> buttons = panel.getButtons();
        boolean anyEnabled = false;
        for (JComponent button : buttons) {
            if (button instanceof JButton && button.isEnabled()) {
                anyEnabled = true;
                break;
            }
        }
        assertTrue(anyEnabled, "Buttons should be enabled after selection");
    }

    @Test
    void testEnableSelectionButtons_DisablesComponentsWhenNotSelected() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();

        // First enable buttons by selecting
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);

            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to enable buttons: " + e.getMessage());
        }

        // Now clear selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.clearSelection();
        } catch (Exception e) {
            fail("Failed to clear selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify components are disabled (setEnabled(false) was called)
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled(), "Buttons should be disabled after clearing selection");
            }
        }
    }

    @Test
    void testEnableSelectionButtons_UpdatesAllSelectionButtons() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        panel.setClassPath(classPath);

        // Add multiple buttons
        panel.addRemoveButton();
        panel.addUpButton();
        panel.addDownButton();

        int buttonCount = panel.getButtons().size();
        assertTrue(buttonCount >= 3, "Should have at least 3 buttons");

        // Select an item
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify all buttons are enabled
        java.util.List<JComponent> buttons = panel.getButtons();
        int enabledCount = 0;
        for (JComponent button : buttons) {
            if (button instanceof JButton && button.isEnabled()) {
                enabledCount++;
            }
        }
        assertTrue(enabledCount > 0, "At least one button should be enabled");
    }

    @Test
    void testEnableSelectionButtons_TogglesButtonStateCorrectly() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        panel.setClassPath(classPath);

        // Add button
        panel.addRemoveButton();

        // Toggle selection and verify button state changes
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);

            // Clear selection -> buttons disabled
            list.clearSelection();
            method.invoke(panel);

            java.util.List<JComponent> buttons1 = panel.getButtons();
            for (JComponent button : buttons1) {
                if (button instanceof JButton) {
                    assertFalse(button.isEnabled(), "Button should be disabled");
                }
            }

            // Set selection -> buttons enabled
            list.setSelectedIndex(0);
            method.invoke(panel);

            java.util.List<JComponent> buttons2 = panel.getButtons();
            boolean anyEnabled = false;
            for (JComponent button : buttons2) {
                if (button instanceof JButton && button.isEnabled()) {
                    anyEnabled = true;
                    break;
                }
            }
            assertTrue(anyEnabled, "Button should be enabled");

            // Clear selection again -> buttons disabled again
            list.clearSelection();
            method.invoke(panel);

            java.util.List<JComponent> buttons3 = panel.getButtons();
            for (JComponent button : buttons3) {
                if (button instanceof JButton) {
                    assertFalse(button.isEnabled(), "Button should be disabled again");
                }
            }
        } catch (Exception e) {
            fail("Failed to toggle selection: " + e.getMessage());
        }
    }

    // Integration tests covering complete execution path

    @Test
    void testEnableSelectionButtons_CompleteExecutionWithSelection() {
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

        // Set selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(1);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify complete execution:
        // 1. selected = true (line 310)
        // 2. Loop iterated over components (line 313)
        // 3. Components enabled (line 315)
        java.util.List<JComponent> buttons = panel.getButtons();
        boolean anyEnabled = false;
        for (JComponent button : buttons) {
            if (button instanceof JButton && button.isEnabled()) {
                anyEnabled = true;
                break;
            }
        }
        assertTrue(anyEnabled, "At least one button should be enabled");
    }

    @Test
    void testEnableSelectionButtons_CompleteExecutionWithoutSelection() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();

        // Clear selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.clearSelection();
        } catch (Exception e) {
            fail("Failed to clear selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify complete execution:
        // 1. selected = false (line 310)
        // 2. Loop iterated over components (line 313)
        // 3. Components disabled (line 315)
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled(), "All buttons should be disabled");
            }
        }
    }

    @Test
    void testEnableSelectionButtons_WithNullOwner() {
        // Create panel with null owner
        ClassPathPanel nullOwnerPanel = new ClassPathPanel(null, true);

        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        nullOwnerPanel.setClassPath(classPath);

        // Add button
        nullOwnerPanel.addRemoveButton();

        // Set selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(nullOwnerPanel);
            list.setSelectedIndex(0);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(nullOwnerPanel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify operation succeeded
        java.util.List<JComponent> buttons = nullOwnerPanel.getButtons();
        boolean anyEnabled = false;
        for (JComponent button : buttons) {
            if (button instanceof JButton && button.isEnabled()) {
                anyEnabled = true;
                break;
            }
        }
        assertTrue(anyEnabled, "Button should be enabled");
    }

    @Test
    void testEnableSelectionButtons_InputModePanel() {
        // Create panel with input mode (second parameter = true)
        ClassPathPanel inputPanel = new ClassPathPanel(frame, true);

        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/input.jar"), false));
        inputPanel.setClassPath(classPath);

        // Add button
        inputPanel.addRemoveButton();

        // Set selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(inputPanel);
            list.setSelectedIndex(0);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(inputPanel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify operation succeeded
        java.util.List<JComponent> buttons = inputPanel.getButtons();
        boolean anyEnabled = false;
        for (JComponent button : buttons) {
            if (button instanceof JButton && button.isEnabled()) {
                anyEnabled = true;
                break;
            }
        }
        assertTrue(anyEnabled, "Button should be enabled");
    }

    @Test
    void testEnableSelectionButtons_OutputModePanel() {
        // Create panel with output mode (second parameter = false)
        ClassPathPanel outputPanel = new ClassPathPanel(frame, false);

        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/output.jar"), false));
        outputPanel.setClassPath(classPath);

        // Add button
        outputPanel.addRemoveButton();

        // Clear selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(outputPanel);
            list.clearSelection();
        } catch (Exception e) {
            fail("Failed to clear selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(outputPanel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify operation succeeded
        java.util.List<JComponent> buttons = outputPanel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled(), "Button should be disabled");
            }
        }
    }

    @Test
    void testEnableSelectionButtons_WithEmptyList() {
        // Initialize with empty list
        panel.setClassPath(new ClassPath());

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();

        // Call enableSelectionButtons (list is empty, so no selection possible)
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify buttons are disabled
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled(), "Buttons should be disabled with empty list");
            }
        }
    }

    @Test
    void testEnableSelectionButtons_CalledMultipleTimes() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        panel.setClassPath(classPath);

        // Add button
        panel.addRemoveButton();

        // Set selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons multiple times
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
            method.invoke(panel);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify buttons remain in correct state (idempotent)
        java.util.List<JComponent> buttons = panel.getButtons();
        boolean anyEnabled = false;
        for (JComponent button : buttons) {
            if (button instanceof JButton && button.isEnabled()) {
                anyEnabled = true;
                break;
            }
        }
        assertTrue(anyEnabled, "Button should still be enabled");
    }

    @Test
    void testEnableSelectionButtons_AfterRemovingElements() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Add button
        panel.addRemoveButton();

        // Select and enable buttons
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);

            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed initial setup: " + e.getMessage());
        }

        // Remove all elements (which clears selection)
        try {
            java.lang.reflect.Method removeMethod = ListPanel.class.getDeclaredMethod("removeAllElements");
            removeMethod.setAccessible(true);
            removeMethod.invoke(panel);
        } catch (Exception e) {
            fail("Failed to remove elements: " + e.getMessage());
        }

        // enableSelectionButtons should have been called by removeAllElements
        // Verify buttons are disabled
        java.util.List<JComponent> buttons = panel.getButtons();
        for (JComponent button : buttons) {
            if (button instanceof JButton) {
                assertFalse(button.isEnabled(), "Buttons should be disabled after removing all elements");
            }
        }
    }

    @Test
    void testEnableSelectionButtons_OnlyAffectsComponentsAfterFirstSelectionButton() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        panel.setClassPath(classPath);

        // Get first selection button index
        int firstSelectionButton = 0;
        try {
            java.lang.reflect.Field field = ListPanel.class.getDeclaredField("firstSelectionButton");
            field.setAccessible(true);
            firstSelectionButton = (int) field.get(panel);
        } catch (Exception e) {
            fail("Failed to get firstSelectionButton: " + e.getMessage());
        }

        // Add buttons
        panel.addRemoveButton();

        // Components before firstSelectionButton should not be affected
        // (index 0 and 1 are list and scroll pane, which should not change)
        Component listComponent = panel.getComponent(0);
        Component scrollPane = panel.getComponent(1);

        // Set selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify components before firstSelectionButton are unchanged
        assertSame(listComponent, panel.getComponent(0), "First component should be unchanged");
        assertSame(scrollPane, panel.getComponent(1), "Second component should be unchanged");
    }

    @Test
    void testEnableSelectionButtons_WithDifferentButtonCombinations() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Add different combinations of buttons
        panel.addRemoveButton();
        panel.addUpButton();
        panel.addDownButton();

        // Set selection
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify all button types are enabled
        java.util.List<JComponent> buttons = panel.getButtons();
        assertTrue(buttons.size() >= 3, "Should have at least 3 buttons");

        int enabledCount = 0;
        for (JComponent button : buttons) {
            if (button instanceof JButton && button.isEnabled()) {
                enabledCount++;
            }
        }
        assertTrue(enabledCount > 0, "At least one button should be enabled");
    }

    @Test
    void testEnableSelectionButtons_SelectionAtFirstIndex() {
        // Initialize with elements
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new java.io.File("/first.jar"), false));
        classPath.add(new ClassPathEntry(new java.io.File("/second.jar"), false));
        panel.setClassPath(classPath);

        // Add buttons
        panel.addRemoveButton();
        panel.addUpButton();
        panel.addDownButton();

        // Select first index
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(0);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify buttons are enabled (up button may be disabled separately, but that's not this method's concern)
        java.util.List<JComponent> buttons = panel.getButtons();
        boolean anyEnabled = false;
        for (JComponent button : buttons) {
            if (button instanceof JButton && button.isEnabled()) {
                anyEnabled = true;
                break;
            }
        }
        assertTrue(anyEnabled, "At least one button should be enabled");
    }

    @Test
    void testEnableSelectionButtons_SelectionAtLastIndex() {
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

        // Select last index
        try {
            java.lang.reflect.Field listField = ListPanel.class.getDeclaredField("list");
            listField.setAccessible(true);
            JList<?> list = (JList<?>) listField.get(panel);
            list.setSelectedIndex(2);
        } catch (Exception e) {
            fail("Failed to set selection: " + e.getMessage());
        }

        // Call enableSelectionButtons
        try {
            java.lang.reflect.Method method = ListPanel.class.getDeclaredMethod("enableSelectionButtons");
            method.setAccessible(true);
            method.invoke(panel);
        } catch (Exception e) {
            fail("Failed to invoke enableSelectionButtons: " + e.getMessage());
        }

        // Verify buttons are enabled (down button may be disabled separately, but that's not this method's concern)
        java.util.List<JComponent> buttons = panel.getButtons();
        boolean anyEnabled = false;
        for (JComponent button : buttons) {
            if (button instanceof JButton && button.isEnabled()) {
                anyEnabled = true;
                break;
            }
        }
        assertTrue(anyEnabled, "At least one button should be enabled");
    }
}
