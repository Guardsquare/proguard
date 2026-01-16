package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassSpecificationsPanel.setClassSpecifications(List) method.
 *
 * The setClassSpecifications() method sets the list of ClassSpecification objects
 * to be displayed in the panel.
 *
 * Lines that need coverage:
 * - Line 136: listModel.clear() - Clears the existing list model
 * - Line 138: if (classSpecifications != null) - Null check for the input list
 * - Line 140: for (int index = 0; index < classSpecifications.size(); index++) - Loop through list
 * - Line 142: listModel.addElement(classSpecifications.get(index)) - Add each element
 * - Line 148: enableSelectionButtons() - Update button states
 * - Line 149: End of method (closing brace)
 *
 * These tests verify:
 * - Null list handling (clears the model)
 * - Empty list handling
 * - Single specification handling
 * - Multiple specifications handling
 * - Replacing existing specifications
 * - Button states are updated after setting specifications
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassSpecificationsPanelClaude_setClassSpecificationsTest {

    private JFrame testFrame;
    private ClassSpecificationsPanel panel;

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
     * Test setClassSpecifications with null list.
     * This covers lines 136 (clear), 138 (null check returns false), 148 (enableSelectionButtons).
     */
    @Test
    public void testSetClassSpecificationsWithNull() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Add some initial data
        List<ClassSpecification> initial = new ArrayList<>();
        initial.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));
        panel.setClassSpecifications(initial);
        assertNotNull(panel.getClassSpecifications(), "Should have data initially");

        // Now set to null - covers lines 136, 138 (false branch), 148, 149
        panel.setClassSpecifications(null);

        // Verify the list is cleared
        assertNull(panel.getClassSpecifications(),
                   "Setting null should result in empty list");
    }

    /**
     * Test setClassSpecifications with empty list.
     * This covers lines 136 (clear), 138 (true), 140 (loop doesn't execute), 148, 149.
     */
    @Test
    public void testSetClassSpecificationsWithEmptyList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Set empty list - covers lines 136, 138 (true), 140 (size=0), 148, 149
        List<ClassSpecification> emptyList = new ArrayList<>();
        panel.setClassSpecifications(emptyList);

        // Verify result is null (empty)
        assertNull(panel.getClassSpecifications(),
                   "Setting empty list should result in null");
    }

    /**
     * Test setClassSpecifications with single specification.
     * This covers lines 136, 138 (true), 140 (loop once), 142 (add element), 148, 149.
     */
    @Test
    public void testSetClassSpecificationsWithSingleSpec() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        ClassSpecification spec = new ClassSpecification(
            "Test", 0, 0, null, "com.example.TestClass", null, null
        );
        List<ClassSpecification> list = new ArrayList<>();
        list.add(spec);

        // Covers lines 136, 138 (true), 140 (index 0), 142 (add), 148, 149
        panel.setClassSpecifications(list);

        List result = panel.getClassSpecifications();
        assertNotNull(result, "Should return non-null list");
        assertEquals(1, result.size(), "Should have 1 specification");
        assertEquals("Test", ((ClassSpecification)result.get(0)).comments);
    }

    /**
     * Test setClassSpecifications with multiple specifications.
     * This covers lines 136, 138 (true), 140 (loop multiple times), 142 (add multiple), 148, 149.
     */
    @Test
    public void testSetClassSpecificationsWithMultipleSpecs() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test1", 0, 0, null, "Class1", null, null));
        list.add(new ClassSpecification("Test2", 0, 0, null, "Class2", null, null));
        list.add(new ClassSpecification("Test3", 0, 0, null, "Class3", null, null));

        // Covers lines 136, 138 (true), 140 (loop 3 times), 142 (add 3 times), 148, 149
        panel.setClassSpecifications(list);

        List result = panel.getClassSpecifications();
        assertNotNull(result, "Should return non-null list");
        assertEquals(3, result.size(), "Should have 3 specifications");
        assertEquals("Test1", ((ClassSpecification)result.get(0)).comments);
        assertEquals("Test2", ((ClassSpecification)result.get(1)).comments);
        assertEquals("Test3", ((ClassSpecification)result.get(2)).comments);
    }

    /**
     * Test setClassSpecifications replaces existing content.
     * This verifies line 136 (clear) properly removes old content.
     */
    @Test
    public void testSetClassSpecificationsReplacesExisting() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Set initial list
        List<ClassSpecification> list1 = new ArrayList<>();
        list1.add(new ClassSpecification("Old", 0, 0, null, "OldClass", null, null));
        panel.setClassSpecifications(list1);
        assertEquals(1, panel.getClassSpecifications().size());

        // Set new list - line 136 clears old content
        List<ClassSpecification> list2 = new ArrayList<>();
        list2.add(new ClassSpecification("New", 0, 0, null, "NewClass", null, null));
        panel.setClassSpecifications(list2);

        List result = panel.getClassSpecifications();
        assertEquals(1, result.size(), "Should have 1 specification after replacement");
        assertEquals("New", ((ClassSpecification)result.get(0)).comments,
                     "Should have the new specification, not the old one");
    }

    /**
     * Test setClassSpecifications with large list.
     * This verifies the loop (line 140) handles many iterations correctly.
     */
    @Test
    public void testSetClassSpecificationsWithLargeList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new ClassSpecification("Test" + i, 0, 0, null, "Class" + i, null, null));
        }

        // Covers line 140 looping 50 times, line 142 adding 50 times
        panel.setClassSpecifications(list);

        List result = panel.getClassSpecifications();
        assertEquals(50, result.size(), "Should have 50 specifications");
        assertEquals("Test0", ((ClassSpecification)result.get(0)).comments);
        assertEquals("Test49", ((ClassSpecification)result.get(49)).comments);
    }

    /**
     * Test that enableSelectionButtons is called (line 148).
     * Verify button states are updated after setting specifications.
     */
    @Test
    public void testSetClassSpecificationsUpdatesButtonStates() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        // Initially disabled (no selection)
        assertFalse(editButton.isEnabled(), "Edit button should be disabled initially");

        // Set specifications - line 148 calls enableSelectionButtons
        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));
        panel.setClassSpecifications(list);

        // Button should still be disabled (no selection made)
        assertFalse(editButton.isEnabled(),
                    "Edit button should remain disabled after setting specs (no selection)");
    }

    /**
     * Test setClassSpecifications called multiple times.
     * Verifies line 136 (clear) works correctly on repeated calls.
     */
    @Test
    public void testSetClassSpecificationsMultipleTimes() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // First call
        List<ClassSpecification> list1 = new ArrayList<>();
        list1.add(new ClassSpecification("First", 0, 0, null, "Class1", null, null));
        panel.setClassSpecifications(list1);
        assertEquals(1, panel.getClassSpecifications().size());

        // Second call
        List<ClassSpecification> list2 = new ArrayList<>();
        list2.add(new ClassSpecification("Second", 0, 0, null, "Class2", null, null));
        list2.add(new ClassSpecification("Third", 0, 0, null, "Class3", null, null));
        panel.setClassSpecifications(list2);
        assertEquals(2, panel.getClassSpecifications().size());

        // Third call with null
        panel.setClassSpecifications(null);
        assertNull(panel.getClassSpecifications());

        // Fourth call with new data
        List<ClassSpecification> list3 = new ArrayList<>();
        list3.add(new ClassSpecification("Fourth", 0, 0, null, "Class4", null, null));
        panel.setClassSpecifications(list3);
        assertEquals(1, panel.getClassSpecifications().size());
    }

    /**
     * Test setClassSpecifications with specifications having different properties.
     * Verifies line 142 correctly adds various specification types.
     */
    @Test
    public void testSetClassSpecificationsWithVariousProperties() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Comment1", 1, 2, "Annotation1", "Class1", "ExtAnnot1", "ExtClass1"));
        list.add(new ClassSpecification("Comment2", 3, 4, "Annotation2", "Class2", "ExtAnnot2", "ExtClass2"));
        list.add(new ClassSpecification(null, 0, 0, null, null, null, null));

        panel.setClassSpecifications(list);

        List result = panel.getClassSpecifications();
        assertEquals(3, result.size());

        ClassSpecification spec1 = (ClassSpecification) result.get(0);
        assertEquals("Comment1", spec1.comments);
        assertEquals("Class1", spec1.className);

        ClassSpecification spec2 = (ClassSpecification) result.get(1);
        assertEquals("Comment2", spec2.comments);
        assertEquals("Class2", spec2.className);

        ClassSpecification spec3 = (ClassSpecification) result.get(2);
        assertNull(spec3.comments);
        assertNull(spec3.className);
    }

    /**
     * Test setClassSpecifications maintains order.
     * Verifies the loop (line 140, 142) maintains specification order.
     */
    @Test
    public void testSetClassSpecificationsMaintainsOrder() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("A", 0, 0, null, "ClassA", null, null));
        list.add(new ClassSpecification("B", 0, 0, null, "ClassB", null, null));
        list.add(new ClassSpecification("C", 0, 0, null, "ClassC", null, null));
        list.add(new ClassSpecification("D", 0, 0, null, "ClassD", null, null));

        panel.setClassSpecifications(list);

        List result = panel.getClassSpecifications();
        assertEquals("A", ((ClassSpecification)result.get(0)).comments);
        assertEquals("B", ((ClassSpecification)result.get(1)).comments);
        assertEquals("C", ((ClassSpecification)result.get(2)).comments);
        assertEquals("D", ((ClassSpecification)result.get(3)).comments);
    }

    /**
     * Test setClassSpecifications after clearing with null.
     * Verifies the method works correctly after being called with null.
     */
    @Test
    public void testSetClassSpecificationsAfterNull() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Set to null first
        panel.setClassSpecifications(null);
        assertNull(panel.getClassSpecifications());

        // Now set with actual data
        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));
        panel.setClassSpecifications(list);

        List result = panel.getClassSpecifications();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * Test setClassSpecifications doesn't throw exception with null elements.
     * Verifies line 142 handles edge cases.
     */
    @Test
    public void testSetClassSpecificationsWithNullElements() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test1", 0, 0, null, "Class1", null, null));
        list.add(null); // null element
        list.add(new ClassSpecification("Test2", 0, 0, null, "Class2", null, null));

        // Should not throw exception
        assertDoesNotThrow(() -> panel.setClassSpecifications(list));

        List result = panel.getClassSpecifications();
        assertEquals(3, result.size());
        assertNull(result.get(1)); // null element preserved
    }

    /**
     * Test setClassSpecifications clears before adding.
     * Specifically tests line 136 executes before line 140-142.
     */
    @Test
    public void testSetClassSpecificationsClearsFirst() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Set initial list with 5 items
        List<ClassSpecification> list1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list1.add(new ClassSpecification("Test" + i, 0, 0, null, "Class" + i, null, null));
        }
        panel.setClassSpecifications(list1);
        assertEquals(5, panel.getClassSpecifications().size());

        // Set new list with 2 items - line 136 should clear all 5 first
        List<ClassSpecification> list2 = new ArrayList<>();
        list2.add(new ClassSpecification("New1", 0, 0, null, "NewClass1", null, null));
        list2.add(new ClassSpecification("New2", 0, 0, null, "NewClass2", null, null));
        panel.setClassSpecifications(list2);

        List result = panel.getClassSpecifications();
        assertEquals(2, result.size(), "Should have exactly 2 items, not 7");
        assertEquals("New1", ((ClassSpecification)result.get(0)).comments);
        assertEquals("New2", ((ClassSpecification)result.get(1)).comments);
    }

    /**
     * Test setClassSpecifications with same list instance multiple times.
     */
    @Test
    public void testSetClassSpecificationsSameListMultipleTimes() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));

        // Set same list multiple times
        panel.setClassSpecifications(list);
        assertEquals(1, panel.getClassSpecifications().size());

        panel.setClassSpecifications(list);
        assertEquals(1, panel.getClassSpecifications().size());

        panel.setClassSpecifications(list);
        assertEquals(1, panel.getClassSpecifications().size());
    }

    /**
     * Test setClassSpecifications in different panel configurations.
     */
    @Test
    public void testSetClassSpecificationsWithDifferentPanelConfigurations() {
        testFrame = new JFrame("Test Frame");

        // Test with different constructor parameters
        ClassSpecificationsPanel panel1 = new ClassSpecificationsPanel(testFrame, false, false);
        ClassSpecificationsPanel panel2 = new ClassSpecificationsPanel(testFrame, true, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));

        panel1.setClassSpecifications(list);
        panel2.setClassSpecifications(list);

        assertEquals(1, panel1.getClassSpecifications().size());
        assertEquals(1, panel2.getClassSpecifications().size());
    }

    /**
     * Test setClassSpecifications executes without exception.
     * Smoke test for all lines 136, 138, 140, 142, 148, 149.
     */
    @Test
    public void testSetClassSpecificationsExecutesWithoutException() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));

        // Should execute all lines without exception
        assertDoesNotThrow(() -> panel.setClassSpecifications(list));
        assertDoesNotThrow(() -> panel.setClassSpecifications(null));
        assertDoesNotThrow(() -> panel.setClassSpecifications(new ArrayList<>()));
    }

    /**
     * Test setClassSpecifications with panel having null owner.
     */
    @Test
    public void testSetClassSpecificationsWithNullOwner() {
        panel = new ClassSpecificationsPanel(null, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));

        panel.setClassSpecifications(list);

        List result = panel.getClassSpecifications();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * Test setClassSpecifications with single element at boundary.
     * Tests loop condition at line 140 with size = 1.
     */
    @Test
    public void testSetClassSpecificationsLoopBoundary() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Test with exactly 1 element (boundary)
        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Single", 0, 0, null, "SingleClass", null, null));

        panel.setClassSpecifications(list);

        assertEquals(1, panel.getClassSpecifications().size());
        assertEquals("Single", ((ClassSpecification)panel.getClassSpecifications().get(0)).comments);
    }

    /**
     * Test that setClassSpecifications properly integrates with getClassSpecifications.
     * Round-trip test.
     */
    @Test
    public void testSetAndGetClassSpecificationsRoundTrip() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> original = new ArrayList<>();
        original.add(new ClassSpecification("Test1", 1, 2, "Ann1", "Class1", "ExtAnn1", "ExtClass1"));
        original.add(new ClassSpecification("Test2", 3, 4, "Ann2", "Class2", "ExtAnn2", "ExtClass2"));

        panel.setClassSpecifications(original);
        List result = panel.getClassSpecifications();

        assertEquals(original.size(), result.size());
        for (int i = 0; i < original.size(); i++) {
            ClassSpecification orig = original.get(i);
            ClassSpecification res = (ClassSpecification) result.get(i);
            assertEquals(orig.comments, res.comments);
            assertEquals(orig.className, res.className);
        }
    }
}
